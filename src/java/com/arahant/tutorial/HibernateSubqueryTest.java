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
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;


public class HibernateSubqueryTest {
	
	@SuppressWarnings("unchecked")
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		try
		{	
	/*		
			HibernateDetachedCriteriaUtil<HrEmplStatusHistory> hdcu=new HibernateDetachedCriteriaUtil<HrEmplStatusHistory>(HrEmplStatusHistory.class,"hist2");
			hdcu.max(HrEmplStatusHistory.EFFECTIVEDATE, "maxdate");
		//	hdcu.eqJoinedField(HrEmplStatusHistory.EMPLOYEE, "emp");
						 
						 
			
			HibernateCriteriaUtil<Employee> hcu=hsu.createCriteria(Employee.class,"emp");
			hcu.joinTo("emp."+Employee.HREMPLSTATUSHISTORIES,"hist")
			.eqPropertySubquery("hist."+HrEmplStatusHistory.EFFECTIVEDATE, hdcu);
			
			*/
			
			
			//using a subquery in criteria

			final Criteria criteria = hsu.getSession().createCriteria(Employee.class, "emp"); 

			final DetachedCriteria dc = DetachedCriteria.forClass(HrEmplStatusHistory.class, "hist2") 
			//  .setProjection(Projections.max("hist2.maxdate")) ;
			.add(Restrictions.eqProperty("emp.person_id", "hist2.employee_id")); 


			criteria.add(Subqueries.exists(dc)); 
			
			
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

	
