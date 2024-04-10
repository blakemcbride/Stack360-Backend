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
 * Hibernate example 08.
 * This example demonstrates reading associated records in a different table.
 */

public class Hibernate08 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  read in all the employees
			List<Employee> v = hsu.createCriteria(Employee.class).list();

			for (Employee i : v) {
				/* The following line gets the HrEeoCategory record that joins to the current Employee record.
				 * It knows how to get this record because of the table mapping which exists elsewhere.
				 * The data type returned depends on the type of join that was defined as follows:
				 *      one-to-one   - return the joining record object
				 *      many-to-one  - return the joining record object
				 *      one-to-many  - a Set of joining records is returned
				 *      many-to-many - a Set of joining records is returned
				 *
				 * If no matching records exist, a null (in the case of a single record) is returned,
				 * or a null set is returned in the case of many records.
				 *
				 * The first time this is called, the system reads in all of the joins.  Therefore, it
				 * doesn't do a sub-read for each employee record in the list.
				 */
				HrEeoCategory c = i.getHrEeoCategory();
				/* You have to check for the possability of no joining records  */
				if (c != null)
					System.out.println(i.getLname() + "  " + c.getEeoCategoryId() + "  " + c.getName());
				else
					System.out.println(i.getLname() + " has no EEO Category");
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
