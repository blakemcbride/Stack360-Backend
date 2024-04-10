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
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;

/**
 * Hibernate example 04.
 * This example also goes through all the records of the same table.  However,
 * this example only reads in one record at a time (rather than reading in all the
 * records into a linked list in memory).  This example is a little slower than
 * the previous, but you don't have to worry about filling up all the memory if there
 * are a lot of records.  Good judgement is required.
 *
 * Note that this is not as slow as you might think.  Internally the system reads
 * in about 50 records at a time and only returns one at a time to the program.
 */


public class Hibernate04 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  Scroll through one record at a time.
			//  This reads in one record with each v.next().
			//  It is a little slower but doesn't use a lot of memory -
			//  only enough space for one record in memory at a time.
			HibernateScrollUtil<HrEeoCategory> v = hsu.createCriteria(HrEeoCategory.class).orderBy(HrEeoCategory.NAME).scroll();

			//  Read one record at a time and access the record data
			//  The get() returns the record, and then a getCOLUMN() is used to get the column data.
			while (v.next())
				System.out.println(v.get().getEeoCategoryId() + "  " + v.get().getName());

			v.close();

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
