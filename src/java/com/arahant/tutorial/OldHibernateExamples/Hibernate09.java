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


/**
 * Created on May 2, 2007
 * 
 */
package com.arahant.tutorial.OldHibernateExamples;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.arahant.beans.Employee;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.OrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate09 {

	@SuppressWarnings("unchecked")
	public static void main(final String args[]) {
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {

			//using a subquery in hql

			final Query q = hsu.createQuery("select emp from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga where hist.hrEmployeeStatus = :stat and hist.effectiveDate = " +
					"(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp) \n and oga.orgGroup = :og " +
					" order by emp.lname, emp.fname ");

			q.setEntity("stat", hsu.getFirst(HrEmployeeStatus.class));
			q.setEntity("og", hsu.get(OrgGroup.class, "00000-0000000007"));

			final List<Employee> cc = q.list();

			final Iterator<Employee> citr = cc.iterator();
			while (citr.hasNext()) {
				System.out.println(citr.next().getLname());
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			//always rollback on exceptions
			hsu.rollbackTransaction();
		}
	}
}

	
