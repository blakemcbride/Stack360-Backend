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
import java.util.Iterator;
import java.util.List;

import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateDetachedCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate10 {

	@SuppressWarnings("unchecked")
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		try
		{			
			//using a subquery in criteria

			//This is not currently working due to a known bug in Hibernate.  I keep this here so I can test to see when they fix it.
			//there are plenty of other ways to do the same thing
			
			final HibernateDetachedCriteriaUtil<HrEmplStatusHistory> hdcu=new HibernateDetachedCriteriaUtil<HrEmplStatusHistory>(HrEmplStatusHistory.class,"hist2");
			hdcu.max(HrEmplStatusHistory.EFFECTIVEDATE, "maxdate");

						 
			
			final HibernateCriteriaUtil<Employee> hcu=hsu.createCriteria(Employee.class,"emp");
			hcu.joinTo(Employee.HREMPLSTATUSHISTORIES,"hist")
			.eqPropertySubquery(HrEmplStatusHistory.EFFECTIVEDATE, hdcu);
			
			
			
			final List<Employee> cc=hcu.list();
			
			final Iterator<Employee> citr=cc.iterator();
			while (citr.hasNext())
				System.out.println(citr.next().getLname());
			
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

	
