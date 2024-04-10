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
 * Hibernate example 03.
 * This example is the same as the previous example except that the record selection
 * is performed on one line instead of having each component of the selection separated.
 * This is how it would normally be used.  This example is much easier to read
 * and comprehend.
 */

public class Hibernate03 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  Read in all the hr_eeo_category records ordered by name.
			//  This actually reads all the records into memory (a linked list) at this point.
			//  Be careful about this.  It is faster but uses a lot of memory.
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
