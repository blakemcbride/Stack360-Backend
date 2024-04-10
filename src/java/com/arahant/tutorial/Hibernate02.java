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
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 02.
 * This example is designed to show how the basic Hibernate Criteria Utility works
 * for record selection.  It also shows the enumeration of a result set represented
 * by a linked list.  All the records are read into memory at one time.  This
 * could be a problem if there are a lot of records.
 */


public class Hibernate02 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			// Create a Criteria object in order to set the criteria of the search
			HibernateCriteriaUtil<HrEeoCategory> hcu = hsu.createCriteria(HrEeoCategory.class);
			// Set the order by.  This can be done any number of times to order by one thing within another.
			// Other things besides order by can be set here.  We'll show those in other examples.
			hcu.orderBy(HrEeoCategory.NAME);
			// Once we are done, perform the search and return a list representing the returned rows.
			List<HrEeoCategory> v = hcu.list();

			//  The above three lines could be done, and is typically done, on a single line like the following:
			//  List<HrEeoCategory> v = hsu.createCriteria(HrEeoCategory.class).orderBy(HrEeoCategory.NAME).list();

			//  Enumerate through the returned list (v).
			//  With each record (r) you can access the fields.
			for (HrEeoCategory r : v)
				System.out.println(r.getEeoCategoryId() + "  " + r.getName());

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
