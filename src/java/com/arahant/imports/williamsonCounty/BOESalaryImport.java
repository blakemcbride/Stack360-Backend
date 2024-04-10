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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.imports.williamsonCounty;

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import java.util.List;

/**
 *
 */
public class BOESalaryImport {

	protected static class Name
	{
		String fname;
		String lname;
		String mname="";

		public Name(String name)
		{
		//	if (isEmpty(name))
		//		name=". .";

			if (name.indexOf(' ')==-1)
			{
				fname=".";
				lname=name;
			}
			else
			{
				lname=name.substring(0, name.indexOf(' ')).trim();
				fname=name.substring(name.indexOf(' ')+1).trim();

				int spos=fname.indexOf(' ');
				if (spos!=-1)
				{
					mname=fname.substring(spos+1).trim();
					if (mname.length()>20)
						mname=mname.substring(0,20);
					fname=fname.substring(0,spos).trim();
				}
			}

			if (lname.endsWith(","))
				lname=lname.substring(0,lname.length()-1);
			if (mname.endsWith(","))
				mname=mname.substring(0,mname.length()-1);
		}
	}

	public static void main(String args[])
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		try
		{
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();

			DelimitedFileReader dfr=new DelimitedFileReader("/Users/Arahant/Drury/BoeSal2.csv");

			dfr.nextLine();

			int count=0;
			while (dfr.nextLine())
			{

				if (++count%50==0)
					System.out.println(count);
				
				String ref=dfr.getString(0);
				double sal=MoneyUtils.parseMoney(dfr.getString(5));


				Employee emp=hsu.createCriteria(Employee.class)
					.eq(Employee.SSN, ref)
					.first();

				if (emp==null)
				{
					
					System.out.println("Missing ref "+ref+" "+dfr.getString(1));
					continue;

				}
				BEmployee bemp=new BEmployee(emp);

				bemp.setWageAndPosition(bemp.getPositionId(),"00001-0000000001" , sal, DateUtils.now());

				bemp.update();


			}


			hsu.commitTransaction();
		}
		catch (Exception e)
		{
			hsu.rollbackTransaction();
			e.printStackTrace();
		}
	}

}
