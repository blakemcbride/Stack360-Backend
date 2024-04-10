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
package com.arahant.services.standard.webservices.dynamicWebServices;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import java.util.HashMap;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ExecuteInput extends TransmitInputBase {

	@Validation (required=true)
	private String classToBeLoaded;
	@Validation (required=true)
	private String methodName;
	@Validation (required=false)
	private DataObject data;

	public DataObject getData() {
		return data;
	}

	public void setData(DataObject data) {
		this.data = data;
	}
	

	public String getClassToBeLoaded()
	{
		return classToBeLoaded;
	}
	public void setClassToBeLoaded(String classToBeLoaded)
	{
		this.classToBeLoaded=classToBeLoaded;
	}
	public String getMethodName()
	{
		return methodName;
	}
	public void setMethodName(String methodName)
	{
		this.methodName=methodName;
	}




}

	
