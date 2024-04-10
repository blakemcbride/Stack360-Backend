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


/**
 * Created on Sep 19, 2006
 * 
 */
package com.arahant.tutorial.OldHibernateExamples;

import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;


public class Hibernate03 {
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		try
		{	
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();  //need current person in general

			//get the all records in table
			List<Person> pl=hsu.getAll(Person.class);

			for (Person p : pl)
				System.out.println(p.getNameLFM());

			//always either commit or rollback the transaction
			hsu.commitTransaction();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			//always rollback on exceptions
			hsu.rollbackTransaction();
		}
	}
}

	
