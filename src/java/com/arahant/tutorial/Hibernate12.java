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

import com.arahant.beans.HrEeoCategory;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 12.
 * Adding a new record.
 *
 */

public class Hibernate12 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			/* First create a new record instance  */
			HrEeoCategory erec = new HrEeoCategory();

			/*  have the system generate a new record ID for it.  */
			erec.generateId();

			/*  set the values of the remaining fields  */
			erec.setName("Dog");

			/*  add (insert) the new record  */
			hsu.insert(erec);

			/*  The remaining code just lists all the records so you can see that the record was added.  */

			List<HrEeoCategory> v = hsu.createCriteria(HrEeoCategory.class).orderBy(HrEeoCategory.NAME).list();

			for (HrEeoCategory r : v)
				System.out.println(r.getEeoCategoryId() + "  " + r.getName());


			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
