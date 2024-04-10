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
 * Hibernate example 17. 
 * This example demonstrates an exclusive outer left join.
 */
public class Hibernate17 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  This example performs an exclusive outer left join, where the the result is only the unmatched values
			//  from the left table. Unlike the standard outer left join, the matched values will not be returned.
			//  This call will do an exclusive outer left read of all employees joining to the HREEOCATEGORY table.
			//  This should result in only the employees who do not have an HREEOCATEGORY.
			List<Employee> v = hsu.createCriteria(Employee.class).leftJoinTo(Employee.HREEOCATEGORY).isNull(HrEeoCategory.EEOCATEGORYID).list();

			for (Employee i : v) {
				HrEeoCategory hrc = i.getHrEeoCategory();
				System.out.println(i.getNameFML() + " " + (hrc == null ? null : hrc.getName()));
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
