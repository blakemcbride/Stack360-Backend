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
package com.arahant.utils;

import com.arahant.beans.AuditedBean;
import com.arahant.beans.IArahantBean;
import java.util.Date;
import org.hibernate.ScrollableResults;

final public class HibernateScrollUtil<T extends IArahantBean> {

	private ScrollableResults sr;
	private boolean first = true;
	private static ArahantLogger logger = new ArahantLogger(HibernateScrollUtil.class);
	private boolean closed = false;

	/**
	 * Checks to see if the current scroll object has been closed, and if not,
	 * closes it.
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			if (!closed)
				close();
		} catch (Throwable t) {
			//just eat it, nothing I can do here
		}
		super.finalize();
	}

	/**
	 * Sets the current scrollable results object equal to a given scrollable
	 * results object.
	 *
	 * @param res
	 */
	public HibernateScrollUtil(final ScrollableResults res) {
		sr = res;
	}

	final public boolean next() {
		if (first)
			first = false;
		else if (ArahantSession.getHSU().aiIntegrate) {
			//clear AI engine of this row
			try {
				Object o = get();
				if (o instanceof JessBean)
					ArahantSession.getAI().remove(get());
			} catch (Exception e) {
				logger.debug(e);
			}

			try {
				Object[] obs = getObjects();

				for (int loop = 0; loop < obs.length; loop++)
					try {
						if (obs[loop] instanceof JessBean)
							ArahantSession.getAI().remove(obs[loop]);
					} catch (Exception e) {
						logger.debug(e);
					}

			} catch (Exception e) {
				logger.debug(e);
			}
		}
		return sr.next();
	}

	/**
	 * Returns all of the objects inside of the current scrollable object.
	 *
	 * @return
	 */
	final public Object[] getObjects() {
		return sr.get();
	}

	/**
	 * Gets a specified item from the current scrollable object.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	final public T get() {
		if (ArahantSession.getHSU().aiIntegrate)
			ArahantSession.runAI();
		T t = (T) sr.get(0);
		if (t instanceof AuditedBean)
			((AuditedBean) t).saveOriginalBean();
		return t;
	}

	/**
	 * Returns the current scrollable object.
	 *
	 * @return
	 */
	final public ScrollableResults internalScroll() {
		return sr;
	}

	/**
	 * Closes the current scrollable object.
	 */
	final public void close() {
		try {
			if (!closed && ArahantSession.getHSU().inTransaction)
				sr.close();

			closed = true;
		} catch (Throwable t) {
			closed = true;
			//logger.error(t);
		}
	}

	/**
	 * Returns an item at the specified location of the scrollable object as a
	 * string.
	 *
	 * @param i
	 * @return
	 */
	final public String getString(int i) {
		if (sr.get(i) == null)
			return "";
		return sr.get(i).toString();
	}

	/**
	 * Returns an item at the specified location of the scrollable object as an
	 * integer.
	 *
	 * @param i
	 * @return
	 */
	final public int getInt(int i) {
		Object o = sr.get(i);
		if (o instanceof Number)
			return ((Number) o).intValue();
		return 0;
	}

	/**
	 * Returns an item at the specified location of the scrollable object as a
	 * double.
	 *
	 * @param i
	 * @return
	 */
	final public double getDouble(int i) {
		Object o = sr.get(i);
		if (o instanceof Number)
			return ((Number) o).doubleValue();
		return 0;
	}

	/**
	 * Returns an item at the specified location of the scrollable object as a
	 * date.
	 *
	 * @param i
	 * @return
	 */
	final public Date getDate(int i) {
		Object o = sr.get(i);
		if (o instanceof Date)
			return (Date) o;
		return null;
	}

	/**
	 * Sets the row number of an item in the scrollable object and then moves
	 * back to the previous item.
	 *
	 * @param row
	 */
	public void setBeforeRowNumber(int row) {
		sr.setRowNumber(row);
		sr.previous();
	}

	/**
	 * Returns an item at the specified location of the scrollable object.
	 *
	 * @param i
	 * @return
	 */
	public Object get(int i) {
		return sr.get(i);
	}

	/**
	 * Returns an item at the specified location of the scrollable object as a
	 * character.
	 *
	 * @param i
	 * @return
	 */
	public char getChar(int i) {
		return sr.getCharacter(i);
	}
}
