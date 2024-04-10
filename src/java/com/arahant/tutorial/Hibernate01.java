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

import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

/**
 * Hibernate example 01.
 * This example shows the basic steps necessary to perform SQL table access.
 */

public class Hibernate01 {

	public static void main(final String args[]) {
		/* Create a hibernate session.  This is done only once.  Use this
		 * object for all access to the database.
		 */
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			//  Much of the Arahant business logic depends on the user who is
			//  logged in.  It is best to explicitly set it.
			hsu.setCurrentPersonToArahant();

			//  All data manipulation should be encapsulated in a transaction.
			//  This command starts one.
			hsu.beginTransaction();

			//  You would do the file I/O logic here.


			// Always either commit or rollback the transaction
			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			// Always rollback on exceptions
			hsu.rollbackTransaction();
		}
	}
}
