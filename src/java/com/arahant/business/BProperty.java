/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
*/


package com.arahant.business;

import com.arahant.beans.Property;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.PropertyReport;
import com.arahant.utils.*;
import org.kissweb.database.Connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 Use this class when Hibernate is active.
 */
public final class BProperty extends BPropertyKissOnly {

	private Property bean;

	public BProperty() {
		bean = new Property();
	}

	public BProperty(String name) {
		super();
		load(name);
	}

	public BProperty(Property p) {
		super();
		bean = p;
	}

	public static boolean getBoolean(String name, boolean defaultVal) {
		final String sval = get(name, String.valueOf(defaultVal));
		if (isEmpty(sval))
			return defaultVal;
		else
			return sval.equalsIgnoreCase("TRUE") || sval.equalsIgnoreCase("YES");
	}

	public static boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public static int getInt(String name, int defaultVal) {
		final String sval = get(name, String.valueOf(defaultVal));
		if (isEmpty(sval))
			return defaultVal;
        else
			try {
				return Integer.parseInt(sval);
			} catch (NumberFormatException e) {
				return defaultVal;
			}
	}

	public static int getInt(String name) {
		return getInt(name, 0);
	}

	public int getInt() {
		try {
			return Integer.parseInt(getValue());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public float getFloat() {
		try {
			return Float.parseFloat(getValue());
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	public static float getFloat(String name, float defaultVal) {
		final String sval = get(name, String.valueOf(defaultVal));
		if (isEmpty(sval))
			return defaultVal;
		else
			try {
				return Float.parseFloat(sval);
			} catch (NumberFormatException e) {
				return defaultVal;
			}
	}

	public static float getFloat(String name) {
		return getFloat(name, 0f);
	}

	public String create() throws ArahantException {
		return "";
	}

	public void load(String key) throws ArahantException {
		boolean closeSession = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null || !hsu.isOpen()) {
			hsu = ArahantSession.openHSU();
			closeSession = true;
		}
		bean = hsu.get(Property.class, key);
		//if the property isn't there, make a blank one for future use
		if (bean == null) {
			bean = new Property();
			bean.setPropName(key);
			bean.setDescription(key);
			bean.setPropValue("");
			hsu.insert(bean);
			bean = hsu.get(Property.class, key);  //force AI register, etc.
		}
		if (closeSession)
			ArahantSession.clearSession();
	}

	public String getName() {
		return bean.getPropName();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setName(String name) {
		bean.setPropName(name);
	}

	public void setValue(String value) {
		bean.setPropValue(value);
	}

	public String getValue() {
		boolean closeSession = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null || !hsu.isOpen()) {
			ArahantSession.openHSU();
			closeSession = true;
		}
		String ret = "";
		try {
			if (bean.getPropValue() != null)
				ret = bean.getPropValue();
		} catch (Exception e) {
		}
		if (closeSession)
			ArahantSession.clearSession();
		return ret;
	}

    public static String getID(String name) {
        final String id = get(name);
        if (id == null || id.isEmpty())
            return null;
        return IDGeneratorKiss.expandKey(id);
    }

	public static String getIDWithCheck(final String name) {
		final String id = get(name);
		if (id == null || id.isEmpty())
			throw new ArahantException("Property " + name + " is not set.");
		if (!id.replaceAll("[0-9-]", "").isEmpty() ||                      // must have only characters 0-9 and -
				id.replaceAll("[^-]", "").length() != 1 ||                 // must have a single -
				id.length() < 3 || id.length() > 16 ||                     // must be between 3 and 16 characters in length
				id.charAt(0) == '-' || id.charAt(id.length()-1) == '-')    // - must be in the middle
			throw new ArahantException("Property " + name + " has an invalid value.");
		return IDGeneratorKiss.expandKey(id);
	}

	public static String get(String name, String defaultVal) {
		boolean closeSession = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null || !hsu.isOpen()) {
			ArahantSession.openHSU();
			closeSession = true;
		}
		final String val = BPropertyKissOnly.get(name, defaultVal);
		if (closeSession)
			ArahantSession.clearSession();
		return val;
	}

	public static String get(String name) {
		return get(name, null);
	}

	/**
	 * Gets a list of ID's from the property table.  Each ID is validated and expanded.
	 *
	 * @param propName
	 * @param table
	 * @param column
	 * @return
	 */
	public static List<String> getIDList(String propName, String table, String column) {
		final Connection db = KissConnection.get();
		final List<String> nl = new ArrayList<>();
		final String prop = get(propName);
		if (prop != null  &&  !prop.isEmpty()) {
			final String [] ids = prop.trim().split(" ");
			for (String id : ids) {
				if (!id.isEmpty()) {
					id = IDGeneratorKiss.expandKey(id);
					try {
						if (db.fetchOne("select " + column + " from " + table + " where " + column + " = ?", id) == null)
							throw new ArahantException(column + " in " + propName + " does not exist in " + table);
					} catch (Exception e) {
						throw new ArahantException(e);
					}
					nl.add(id);
				}
			}
		}
		return nl;
	}

	/**
	 * Get and validate a single ID from a property
	 *
	 * @param propName
	 * @param table
	 * @param column
	 * @return
	 */
	public static String getOneID(String propName, String table, String column) {
		List<String> ids = getIDList(propName, table, column);
		if (ids.isEmpty())
			throw new ArahantException(column + " in " + propName + " does not exist in " + table);
		if (ids.size() > 1)
			throw new ArahantException("Too many IDs in property " + propName);
		return ids.get(0);
	}

	public static void delete(HibernateSessionUtil hsu, String[] ids) {
		for (String id : ids) {
			BProperty bp = new BProperty(id);
			hsu.delete(bp.bean);
		}
	}

	public static BProperty[] list() {
		BProperty[] ret;
		boolean closeSession = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null || !hsu.isOpen()) {
			hsu = ArahantSession.openHSU();
			closeSession = true;
		}
		ret = makeArray(hsu.createCriteria(Property.class).orderBy(Property.NAME).list());
		if (closeSession)
			ArahantSession.clearSession();
		return ret;
	}

	static BProperty[] makeArray(Collection<Property> c) {
		boolean closeSession = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu == null || !hsu.isOpen()) {
			ArahantSession.openHSU();
			closeSession = true;
		}
		BProperty[] ret = new BProperty[c.size()];

		int loop = 0;

		for (Property p : c)
			ret[loop++] = new BProperty(p);
		if (closeSession)
			ArahantSession.clearSession();
		return ret;
	}

	public static String getReport() {
		return new PropertyReport().build();
	}
	
	/**
	 * When the "WizardConfigurator" property is set to "WC12000" then this is Wizard logic version 3.0.  Otherwise it is an earlier version.
	 * Williamson County & Prism are at 3.0.
	 * 
	 * @return true if Wizard version is 3.0
	 */
	public static boolean isWizard3() {
		return get("WizardConfigurator").equals("WC12000");
	}
	
	/**
	 * If this is Prism / NCMU then the "Prism" property should be set to an integer.
	 * 
	 * @return 0=not Prism, 1=Prism configuration 1, etc.
	 */
	public static int prismVersion() {
		String val = get("Prism").trim();
		if (val.equals(""))
			return 0;
		return Integer.parseInt(val);
	}

	public Property getBean() {
		return bean;
	}

	public void insert() {
		ArahantSession.getHSU().insert(bean);
	}

	public void update() {
        ArahantSession.getHSU().update(bean);
    }

}
