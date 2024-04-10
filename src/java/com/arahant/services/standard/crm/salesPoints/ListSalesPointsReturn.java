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
 * 
 */
package com.arahant.services.standard.crm.salesPoints;

import com.arahant.beans.Employee;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.ProspectLog;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.business.BSalesActivity;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import java.util.StringTokenizer;

public class ListSalesPointsReturn extends TransmitReturnBase {

	private String xmlString;

	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

	public void setData(BSalesActivity[] bsa, int fromDate, int toDate)
	{
		xmlString = "<root>";

		int pts = 0;
		int total = 0;

		for (Employee e : ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).list())
		{
			BEmployee be = new BEmployee(e);
			xmlString += "<node><personName>" + be.getEmployee().getNameLF() + "</personName>";
			for(BSalesActivity bs : bsa)
			{
				//count all the logs where activity is bs and employee is be
				if ((fromDate != 0) && (toDate != 0))
				{
					pts = ArahantSession.getHSU().createCriteria(ProspectLog.class)
							.eq(ProspectLog.SALES_ACTIVITY, bs.getBean())
							.eq(ProspectLog.EMPLOYEE, be.getEmployee())
							.ge(ProspectLog.CONTACT_DATE, fromDate)
							.le(ProspectLog.CONTACT_DATE, toDate)
							.count();
				}
				else if ((fromDate != 0) && (toDate == 0))
				{
					pts = ArahantSession.getHSU().createCriteria(ProspectLog.class)
							.eq(ProspectLog.SALES_ACTIVITY, bs.getBean())
							.eq(ProspectLog.EMPLOYEE, be.getEmployee())
							.ge(ProspectLog.CONTACT_DATE, fromDate)
							.count();
				}
				else if ((fromDate == 0) && (toDate != 0))
				{
					pts = ArahantSession.getHSU().createCriteria(ProspectLog.class)
							.eq(ProspectLog.SALES_ACTIVITY, bs.getBean())
							.eq(ProspectLog.EMPLOYEE, be.getEmployee())
							.le(ProspectLog.CONTACT_DATE, toDate)
							.count();
				}
				else
				{
					pts = ArahantSession.getHSU().createCriteria(ProspectLog.class)
							.eq(ProspectLog.SALES_ACTIVITY, bs.getBean())
							.eq(ProspectLog.EMPLOYEE, be.getEmployee())
							.count();
				}
				
				total += pts * bs.getSalesPoints();
				xmlString += "<" + makeVar(bs.getActivityCode()) + ">" + pts + "</" + makeVar(bs.getActivityCode()) + ">";
				
			}
			xmlString += "<totalPoints>" + total + "</totalPoints>";
			xmlString += "</node>";
			total = 0;
		}
		xmlString +="</root>";
	}

	public void setCap(int x)
	{
		cap=x;
	}

	public int getCap()
	{
		return cap;
	}

	public String getXmlString()
	{
		return xmlString;
	}
	public void setXmlString(String xmlString)
	{
		this.xmlString=xmlString;
	}

	public String makeVar(String x)
	{
		String var = "";
		StringTokenizer st = new StringTokenizer(x);
		String temp = "";
		char f;

		//do first word
		temp = st.nextToken();
		temp = temp.toLowerCase();
		var += temp;

		//do the rest
		while (st.hasMoreTokens()) {
			temp = st.nextToken();
			temp = temp.toUpperCase();
			f = temp.charAt(0);
			temp = temp.toLowerCase();
			temp = f + temp.substring(1, temp.length());
			var += temp;
		}

		return var;
	}
}

	
