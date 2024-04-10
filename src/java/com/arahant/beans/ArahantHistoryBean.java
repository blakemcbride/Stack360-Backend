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

import com.arahant.utils.HibernateSessionUtil;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

public abstract class ArahantHistoryBean extends ArahantBean {

	protected String recordPersonId;
	protected char recordChangeType;
	protected Date recordChangeDate;
	protected String history_id;
	public static final String RECORD_CHANGE_DATE = "recordChangeDate";
	public static final String RECORD_CHANGE_TYPE = "recordChangeType";
	public static final String HISTORY_DATE = "recordChangeDate";
	public static final String HISTORY_ID = "history_id";

	@Override
	public String generateId() {
		UUID uuid = UUID.randomUUID();
		return history_id = (Long.toHexString(uuid.getLeastSignificantBits()) + Long.toHexString(uuid.getMostSignificantBits()));
	}

	/**
	 * @return Returns the recordChangeDate.
	 */
	public abstract Date getRecordChangeDate();

	/**
	 * @param recordChangeDate The recordChangeDate to set.
	 */
	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	/**
	 * @return Returns the recordChangePerson.
	 */
	public abstract String getRecordPersonId();

	/**
	 * @param recordChangePerson The recordChangePerson to set.
	 */
	public void setRecordPersonId(String recordChangePerson) {
		this.recordPersonId = recordChangePerson;
	}

	/**
	 * @return Returns the recordChangeType.
	 */
	public abstract char getRecordChangeType();

	/**
	 * @param recordChangeType The recordChangeType to set.
	 */
	public void setRecordChangeType(char recordChangeType) {
		this.recordChangeType = recordChangeType;
	}

	/**
	 * @return Returns the historyId.
	 */
	public abstract String getHistory_id();

	/**
	 * @param historyId The historyId to set.
	 */
	public void setHistory_id(String historyId) {
		this.history_id = historyId;
	}

	/**
	 * @param ab
	 */
	public void copy(IAuditedBean ab) {
		HibernateSessionUtil.copyCorresponding(this, ab);
	}

//	protected static Object copyCorresponding(final Object to, final Object from) {
//
//		if (from==null)
//			return to;
//
//		if (to==null)
//			return null;
//
//		final Method [] mems=from.getClass().getMethods();
//
//		for (final Method element : mems) {
//			final String name=element.getName();
//			if (name.startsWith("get"))
//			{
//				final String findName="s"+name.substring(1);
//
//				final Class pTypes[]=new Class[1];
//				pTypes[0]=element.getReturnType();
//				try {
//					final Method meth=to.getClass().getMethod(findName,pTypes);
//					if (meth==null)
//						continue;
//					final Object args[]=new Object[1];
//					args[0]=element.invoke(from,(Object[])null);
////					if(args[0].getClass() == String.class)
////					{
////						String temp = (String)args[0];
////						System.out.println(temp);
////					}
//					Object obj = args[0];
//					if (obj.getClass() == String.class  ||
//							obj.getClass() == Integer.class  ||
//							obj.getClass() == Long.class  ||
//							obj.getClass() == Float.class  ||
//							obj.getClass() == Double.class ||
//							obj.getClass() == Boolean.class ||
//							obj.getClass() == Character.class ||
//							obj.getClass() == Short.class)
//					meth.invoke(to,args);
//					//System.out.println("Copying " + from.getClass().getSimpleName() + "." + name + " (" + args[0].toString() + ") to " + to.getClass().getSimpleName() + "." + meth.getName());
//
//				} catch (final SecurityException e) {
//					continue;
//				} catch (final NoSuchMethodException e) {
//					continue;
//				}
//				catch (final InvocationTargetException e) {
//					continue;
//				}
//				catch (final IllegalAccessException e) {
//					continue;
//				}
//				catch (final Throwable e)
//				{
//					continue;
//				}
//			}
//
//		}
//		return to;
//	}
	/**
	 * @param string
	 * @param object
	 */
	public void setField(String fld, Object object) {
		if (object == null)
			return;

		String findName = "set" + fld;

		Method m[] = getClass().getMethods();

		for (int loop = 0; loop < m.length; loop++)
			if (m[loop].getName().equalsIgnoreCase(findName))
				if (m[loop].getParameterTypes().length == 1) {
					final Object args[] = new Object[]{object};
					try {
						m[loop].invoke(this, args);
					} catch (Exception e) {
						continue;
					}
					break;
				}

	}

	@Override
	public boolean equals(Object o) {
		if (history_id == null && o == null)
			return true;
		if (history_id != null && o instanceof ArahantHistoryBean)
			return history_id.equals(((ArahantHistoryBean) o).getHistory_id());

		return false;
	}

	@Override
	public int hashCode() {
		if (getHistory_id() == null)
			return 0;
		return getHistory_id().hashCode();
	}

	/**
	 * @return
	 */
	public abstract boolean alreadyThere();

	public String getChangeTypeFormatted() {
		char changeType = getRecordChangeType();
		String changeTypeFormatted = "";

		if (changeType == 'N')
			changeTypeFormatted = "New";
		else if (changeType == 'M')
			changeTypeFormatted = "Modify";
		else if (changeType == 'D')
			changeTypeFormatted = "Delete";

		return changeTypeFormatted;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			HrBenefitJoinH bean = new HrBenefitJoinH();
			System.out.println(bean.generateId());
		}
	}
}
