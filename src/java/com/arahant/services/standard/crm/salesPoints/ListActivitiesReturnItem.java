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
import com.arahant.business.BSalesActivity;
import java.util.StringTokenizer;

public class ListActivitiesReturnItem {
	
	public ListActivitiesReturnItem()
	{
		
	}

	ListActivitiesReturnItem (BSalesActivity bc)
	{
		code=bc.getActivityCode();
		points=bc.getSalesPoints();
		id=bc.getSalesActivityId();
		varName=makeVar(bc.getActivityCode());
	}
	
	private String code;
	private int points;
	private String id;
	private String varName;
	

	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public int getPoints()
	{
		return points;
	}
	public void setPoints(int points)
	{
		this.points=points;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getVarName()
	{
		return varName;
	}
	public void setVarName(String varName)
	{
		this.varName=varName;
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

	
