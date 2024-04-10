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

import com.arahant.beans.Employee;
import com.arahant.beans.HrEeoCategory;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 16. 
 * This example demonstrates conditional table queries.
 */
public class Hibernate16 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  This example queries a table with a conditional statement, getting only results
			//  which fit the conditional. In this particular case, we are looking for all employees
			//  who are "Professionals"
			List<Employee> v = hsu.createCriteria(Employee.class).joinTo(Employee.HREEOCATEGORY).eq(HrEeoCategory.NAME, "Professionals").list();

			for (Employee i : v) {
				HrEeoCategory c = i.getHrEeoCategory();
				System.out.println(i.getFname() + " " + i.getLname() + "  " + c.getName());
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
