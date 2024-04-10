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
package com.arahant.tutorial;

import com.arahant.beans.ArahantHistoryBean;
import com.arahant.beans.Employee;
import com.arahant.beans.IAuditedBean;
import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.HibernateUtil;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;


public class TestLocks {

	private static HashMap queuedHistoryItems = new HashMap();

	public static void main(final String args[]) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			Person p = hsu.get(Person.class, "00001-0000000007"); //00001-0000193336
			System.out.println("Read Record       " + p.getPersonId() + " - " + p.getFname());

			p.setFname(p.getFname() + " (Tested Locks)");
			System.out.println("Changed Record    " + p.getPersonId() + " - " + p.getFname());

			handleHistoryRecord(p);
			hsu.getSession().update(p);
			//hsu.update(p);
			System.out.println("Saved Record      " + p.getPersonId() + " - " + p.getFname());

			//HibernateSessionUtil hsu2 = ArahantSession.openHSU();
			Session hsu2 = HibernateUtil.getSessionFactory().openSession();

			//HibernateSessionUtil hsu2 = ArahantSession.openHSU();

			Employee e = (Employee) hsu2.get(Employee.class, "00001-0000000007"); //00001-0000193336
			System.out.println("Re-Read Record    " + e.getPersonId() + " - " + e.getFname());

			hsu.close();
			hsu2.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void handleHistoryRecord(IAuditedBean modifiedBean) {
		Session tempSession = HibernateUtil.getSessionFactory().openSession();

		if (modifiedBean instanceof HibernateProxy) //Hibernate creates insane internal classes i.e. 'PersonPending$$EnhancerByCGLIB23u9840'
		{
			System.out.println("Cannot create history record out of proxy object " + modifiedBean.getClass().getName());
			tempSession.close();
			return;
		}
		IAuditedBean originalBean = (IAuditedBean) tempSession.get(modifiedBean.getClass(), (Serializable) modifiedBean.keyValue());
		if (originalBean == null) {
			modifiedBean.setRecordChangeDate(new java.util.Date());
			modifiedBean.setRecordChangeType('N');
			Person pers = ArahantSession.getHSU().getCurrentPerson();
			modifiedBean.setRecordPersonId(pers.getPersonId());
		} else if (true)
			if (originalBean.getRecordChangeType() == 'N') {
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object
						//hb.setField("personId", "00001-0000000007");

						try {
							final Class getterInputTypes[] = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class setterInputTypes[] = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object args[] = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}
						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('N');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						tempSession.save(hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						//logger.error(e);
					}

					//change the current record information for the later history creation

					modifiedBean.setRecordChangeDate(new java.util.Date());
					modifiedBean.setRecordChangeType('M');
					Person per = ArahantSession.getHSU().getCurrentPerson();
					if (per == null)
						modifiedBean.setRecordPersonId("00001-0000000007");//originalBean.getRecordPersonId());
					else
						modifiedBean.setRecordPersonId(per.getPersonId());
				}
			} else if (originalBean.getRecordChangeType() == 'M')
				if (queuedHistoryItems.get(originalBean.getClass().getName() + originalBean.keyValue().toString()) == null) {
					try {
						ArahantHistoryBean hb = originalBean.historyObject();
						hb.copy(originalBean); //copy everything into your history object

						try {
							final Class getterInputTypes[] = new Class[0];
							final Method getter = originalBean.getClass().getMethod("getBenefitChangeReason", getterInputTypes);

							final Class setterInputTypes[] = new Class[1];
							setterInputTypes[0] = com.arahant.beans.HrBenefitChangeReason.class;
							final Method setter = hb.getClass().getMethod("setBenefitChangeReason", setterInputTypes);

							if (getter != null && setter != null) {
								final Object args[] = new Object[1];
								args[0] = getter.invoke(originalBean, (Object[]) null);
								setter.invoke(hb, args);
							}
						} catch (Exception e) {
							//not an instance of HrBenefitJoin so continue
						}

						//set the history information
						hb.setRecordChangeDate(originalBean.getRecordChangeDate());
						hb.setRecordChangeType(originalBean.getRecordChangeType());
						hb.setRecordPersonId(originalBean.getRecordPersonId());

						//set the history information if it didnt exist (shouldn't ever hit this)
						if (hb.getRecordChangeDate() == null)
							hb.setRecordChangeDate(new java.util.Date());
						if (hb.getRecordChangeType() == 0)
							hb.setRecordChangeType('M');
						if (hb.getRecordPersonId() == null)
							hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
						hb.generateId();

						tempSession.save(hb);
						queuedHistoryItems.put(originalBean.getClass().getName() + originalBean.keyValue().toString(), originalBean);

					} catch (Exception e) {
						//logger.error(e);
					}

					//change the current record information for the later history creation

					modifiedBean.setRecordChangeDate(new java.util.Date());
					modifiedBean.setRecordChangeType('M');
					Person per2 = ArahantSession.getHSU().getCurrentPerson();
					if (per2 == null)
						modifiedBean.setRecordPersonId(originalBean.getRecordPersonId());
					else
						modifiedBean.setRecordPersonId(per2.getPersonId());
				}
		tempSession.flush();
		tempSession.close();
	}
}
