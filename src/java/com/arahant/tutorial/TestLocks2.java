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
import com.arahant.beans.Person;
import com.arahant.utils.HibernateUtil;
import java.util.Date;
import org.hibernate.Session;

public class TestLocks2 {

	public static void main(final String args[]) {
		try {
			//HibernateSessionUtil hsu = ArahantSession.getHSU();
			Session hsu = HibernateUtil.getSessionFactory().openSession();
			hsu.beginTransaction();

			Person p = (Person) hsu.get(Person.class, "00001-0000000007"); //00001-0000193336
			System.out.println("Read Record       " + p.getPersonId() + " - " + p.getFname());

			p.setFname(p.getFname() + " (Tested Locks)");
			System.out.println("Changed Record    " + p.getPersonId() + " - " + p.getFname());

			Session tempSession = HibernateUtil.getSessionFactory().openSession();
			Person pOrig = (Person) tempSession.get(p.getClass(), p.keyValue());
			ArahantHistoryBean hb = pOrig.historyObject();
			hb.copy(pOrig);
			hb.setRecordChangeDate(new Date());
			hb.setRecordChangeType('M');
			hb.setRecordPersonId("00001-0000000007");
			hb.generateId();
			hsu.save(hb);
			hsu.flush();
			tempSession.close();
			System.out.println("History Record    " + p.getPersonId() + " - " + p.getFname());

			hsu.update(p);
			System.out.println("Saved Record      " + p.getPersonId() + " - " + p.getFname());

			//HibernateSessionUtil hsu2 = ArahantSession.openHSU();
			Session hsu2 = HibernateUtil.getSessionFactory().openSession();

			Employee e = (Employee) hsu2.get(Employee.class, "00001-0000000007"); //00001-0000193336 
			System.out.println("Re-Read Record    " + e.getPersonId() + " - " + e.getFname());

			hsu.close();
			hsu2.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
