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
 * Hibernate example 11.
 * This example demonstrates reading a table returning only specified columns.
 */
public class Hibernate11 {

	/* The following line prevents the Java compiler from warning about the danger
	 * that would occur if the typecast below is incorrect.  */
@SuppressWarnings("unchecked")
	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			/*
			 * By use of the selectFields method you can specify a particular subset
			 * of columns to read in.  If you select only one column you get a list of
			 * that one type back.  Each list element represents one record.  So the
			 * number of elements in the list will equal the number of records returned.
			 * If you specify more than one column you get back a
			 * list of arrays.  Each list element represents one record.  Then each
			 * array element within each list element represents a column.
			 *
			 * This example program demonstrates three different variations.
			 * The first variation shows a list of String columns being returned.
			 */

			List<String> names = (List) hsu.createCriteria(Employee.class).selectFields(Person.LNAME).list();

			for (String lname : names)
				System.out.println(lname);

			System.out.println();

			/* This next example shows the return of a single Integer column.  */

			List<Integer> dobs = (List) hsu.createCriteria(Employee.class).selectFields(Person.DOB).list();

			for (Integer dob : dobs)
				System.out.println(dob);

			System.out.println();

			/* The last variation shows returning two columns.  Since the columns may not have the
			 * same data type an general Object array is returned rather than a particular type.
			 * The correct object types are actually returned.  However, this variation shows
			 * how the return type must be typecast [(Integer) i[1]] in order to use it in its native data type.
			 */

			List<Object[]> v = (List) hsu.createCriteria(Employee.class).selectFields(Person.LNAME, Person.DOB).list();

			for (Object[] i : v)
				System.out.println(i[0] + " " + ((Integer) i[1] + 1) + "  " + i[1].getClass());


			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
