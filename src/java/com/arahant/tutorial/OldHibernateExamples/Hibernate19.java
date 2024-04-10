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
import org.hibernate.criterion.CriteriaSpecification;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitJoinHDeletes;
import com.arahant.beans.IHrBenefitJoinCurrent;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate19 {
	
	@SuppressWarnings("unchecked")
	public static void main (final String args[])
	{
		//create a hibernate session
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		try
		{
			hsu.beginTransaction();

			//This is an example of query using interface and pulls back results from multiple tables
			List<IHrBenefitJoinCurrent> scr=hsu.getSession().createCriteria(IHrBenefitJoinCurrent.class)
					.setMaxResults(50)
					.list();
			

			for (IHrBenefitJoinCurrent dels : scr)
				System.out.println(dels.getRecordChangeType());

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

	
