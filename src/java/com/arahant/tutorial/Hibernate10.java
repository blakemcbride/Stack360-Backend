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
import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 10.
 * This example demonstrates reading the parent in an inherited table situation.
 * Since the parent table can be the parent of more than one child table the actual
 * class returned is of the specific child that it is.  You must dynamically query
 * the instance (returned to see what type it is.  If there are no child tables
 * represented then an instance of the parent table is returned.
 */

public class Hibernate10 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			/* Read in all the person records.  Applicable child tables are read in too.
			 * The instance returned will be an instance of the child record (which includes
			 * all of the parent table fields as shown in a previous example.
			 */

			List<Person> v = hsu.createCriteria(Person.class).list();

			//  See how we enumerate through the records as instances of the more
			//  general type but the actual instance may be an instance of the more specific type.
			for (Person i : v) {
				System.out.print(i.getLname() + "  " + i.getClass() + "  -  ");
				if (i instanceof Employee)
					System.out.println("Employee");
				else
					System.out.println("not Employee");
			}
			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
