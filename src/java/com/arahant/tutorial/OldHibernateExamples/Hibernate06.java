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
 * Created on May 2, 2007
 * 
 */
package com.arahant.tutorial.OldHibernateExamples;
import java.util.List;

import com.arahant.beans.OrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate06 {
	
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		try
		{		
			//queries are polymorphic - this will return Companies and Groups
			final List <OrgGroup> cc=hsu.createCriteria(OrgGroup.class).list();

			for (OrgGroup og : cc)
				System.out.println(og.getName() + "  " + og.getClass());
			
			hsu.commitTransaction();
		}
		catch (final Exception e)
		{
			//always rollback on exceptions
			hsu.rollbackTransaction();
		}
	}
}

	
