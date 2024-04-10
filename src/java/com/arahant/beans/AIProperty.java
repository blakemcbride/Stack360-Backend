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


package com.arahant.beans;

import com.arahant.utils.ArahantSession;
import com.arahant.utils.JessBean;
import java.util.*;
import jess.JessException;
import jess.Rete;

/**
 *
 *
 * This class is linked to Jess so that Jess sees the instance values.
 */
public class AIProperty extends JessBean {

	private String name;
	private String value;
	private String personId;
	private String key;
	private String value2;
	private double dblVal;
	private int intVal;
	private double retDblVal;
	private Set<String> values = new HashSet<String>();
	private List<String> lvalues1 = new LinkedList<String>();
	private List<String> lvalues2 = new LinkedList<String>();

	public static boolean getBoolean(String name, String key) {
		AIProperty prop = new AIProperty(name, key);
		return "true".equalsIgnoreCase(prop.value);
	}

	public static List<String> getList(String name) {
		String s = getValue(name);
		List<String> l = new LinkedList<String>();
		if (s != null && !s.trim().equals("")) {
			StringTokenizer stok = new StringTokenizer(s, ",");
			while (stok.hasMoreTokens())
				l.add(stok.nextToken());
		}
		return l;
	}

	/**
	 * Used to find the value of a property.
	 *
	 * For example: name="ColorOfPlanet" key= "Earth" value may be "blue"
	 *
	 * After this call, loadValue() gets the value it had.
	 *
	 * @param name property name
	 * @param key
	 */
	public AIProperty(String name, String key) {
		this.name = name;
		this.key = key;
		if (ArahantSession.getCurrentPerson() != null)
			personId = ArahantSession.getCurrentPerson().getPersonId();
		internalLoadValue();
	}

	public AIProperty(String name, String key, String personId) {
		this.name = name;
		this.key = key;
		this.personId = personId;
		internalLoadValue();
	}

	public AIProperty(Rete r, String name, String key, String personId) {
		super(r);
		this.name = name;
		this.key = key;
		this.personId = personId;
		internalLoadValue();
	}

	public AIProperty(Rete r, String name, String key, String personId, int val) {
		super(r);
		this.name = name;
		this.key = key;
		this.personId = personId;
		this.intVal = val;
		internalLoadValue();
	}

	public AIProperty(String name, String key, double x) {
		this.name = name;
		this.key = key;
		this.dblVal = x;
		personId = ArahantSession.getCurrentPerson().getPersonId();
		internalLoadValue();
	}

	public AIProperty(String name, String key, int x) {
		this.name = name;
		this.key = key;
		this.intVal = x;
		personId = ArahantSession.getCurrentPerson().getPersonId();
		internalLoadValue();
	}

	public Set<String> getValues() {
		return values;
	}

	public List<String> getListValues2() {
		return lvalues2;
	}

	public List<String> getListValues1() {
		return lvalues1;
	}

	public void add(String val) {
		values.add(val);
	}

	public void addList1(String val) {
		lvalues1.add(val);
	}

	public void addList2(String val) {
		lvalues2.add(val);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, name);
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	public void setValue(String value) {
		firePropertyChange("value", this.value, value);
		this.value = value;
	}

	public AIProperty() {
		super();
		personId = ArahantSession.getCurrentPerson().getPersonId();
	}

	public AIProperty(String name) {
		this();
		this.name = name;
		internalLoadValue();
	}

	public static String getValue(String name) {
		return new AIProperty(name).value;
	}

	public static boolean getBoolean(String name) {
		return "true".equalsIgnoreCase(new AIProperty(name).value);
	}

	public boolean getBoolean() {
		return "true".equalsIgnoreCase(value);
	}

	public static int getInt(String name) {
		try {
			return Integer.parseInt(new AIProperty(name).value);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getInt() {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	private String internalLoadValue() {
		linkToEngine();
		//ArahantSession.getAI().watchAll();
		runAIEngine();
		//ArahantSession.AIEval("(facts)");
		return value;
	}

	public String loadValue() {
		return internalLoadValue();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		firePropertyChange("key", this.key, key);
		this.key = key;
		runAIEngine();
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		firePropertyChange("value2", this.value2, value2);
		this.value2 = value2;
	}

	public double getDblVal() {
		return dblVal;
	}

	public void setDblVal(double dblVal) {
		firePropertyChange("dblVal", this.dblVal, dblVal);
		this.dblVal = dblVal;
		runAIEngine();
	}

	public double getRetDblVal() {
		return retDblVal;
	}

	public void setRetDblVal(double retDblVal) {
		this.retDblVal = retDblVal;
	}

	public int getIntVal() {
		return intVal;
	}

	public void setIntVal(int intVal) {
		firePropertyChange("intVal", this.intVal, intVal);
		this.intVal = intVal;
		runAIEngine();
	}

	@Override
	public void linkToEngine() {
		//describe bean to engine
		try {

			getAIEngine().add(this);

		} catch (JessException e) {
			e.printStackTrace();
		}
	}
;
}
