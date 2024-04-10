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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 07.
 * This example demonstrates reading inherited tables without the need for joins.
 */

public class Hibernate07 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			/* Read in all the employees.  The employee table is elsewhere defined
			 * to inherit from the person table.  Therefore, when reading in employee
			 * records you have immediate access to the parent columns as if they
			 * were in the employee table.
			 */

			List<Employee> v = hsu.createCriteria(Employee.class).list();

			//  See how the lname column can be accessed as if it were part of the
			//  employee table (even though it is part of the person table).
			for (Employee i : v)
				System.out.println(i.getLname());

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
