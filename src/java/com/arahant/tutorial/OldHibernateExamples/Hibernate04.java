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

import com.arahant.beans.Employee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate04 {
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();
		
		try
		{	
			//to create a new object in the database, just new it
			final Employee p=new Employee();
			
			//you must provide the key
			//We have an ID generator to help
			p.generateId();
			
			
			//populate it with values
			p.setLname("Smith");
			p.setFname("Susan");
			p.setTitle("Mrs.");
			
			
			//insert the new object
			//insert will do only an insert
			hsu.insert(p);
			
			//you could change the object once it's saved or loaded
			p.setFname("Duncan");
			p.setTitle("Mr.");
			
			//insert or update will do either an insert or an update, as appropriate
			hsu.saveOrUpdate(p);
			
			//the delete method removes the object from the database 
	//		hsu.delete(p);
					
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

	
