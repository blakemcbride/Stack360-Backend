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
 * Hibernate example 15. 
 * This example demonstrates a left outer join.
 */
public class Hibernate15 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  The leftJoinTo() causes a left outer join, which returns the matches values, along
			//  with all unmatched values from the left table, which is the Employee table in this case.
			//  This call does a left outer join read of all employees joining to the
			//  HREEOCATEGORY table. This should result in the full list of employees regardless of whether
			//  they have a HREEOCATEGORY or not. It will also display the HREEOCATEGORY for those that have it
			//  and "null" for those that do not.
			//  The argument for the leftJoinTo() function is from-table, dot, to-table.
			List<Employee> v = hsu.createCriteria(Employee.class).leftJoinTo(Employee.HREEOCATEGORY).list();


			for (Employee i : v) {
				HrEeoCategory c = i.getHrEeoCategory();

				//Check for null pointers to avoid exceptions
				System.out.println(i.getFname() + " " + i.getLname() + " " + (c == null ? null : c.getEeoCategoryId()));
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
