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



package com.arahant.dynamic.services.standard.hr.paychexExport

import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps
import com.arahant.utils.dynamicwebservices.DataObjectMap
import com.arahant.utils.HibernateSessionUtil
import com.arahant.groovy.GroovyClass
/**
 *
 * @author Blake McBride
 * 
 * Paychex time export
 */
class TimeExport {
	
	public static void main(DataObjectMap input, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service) 
	{
		int begDate = input.getInt("begPeriod")
		int endDate = input.getInt("endPeriod")
		/*  The commented import above and the commented line following would be used when running Java code.
		 *  But the GroovyClass and invokeConstructor lines have to be used instead when executing dynamically loaded Groovy
		 *  code.  The reason is that at the time this file is loaded, the Groovy class it wants to call doesn't exist
		 *  yet so the import would fail.  We have to dynamically load the Groovy class we want to execute.
		*/
//		String file = new NorthAmericaAdministratorsExport().export()			
		GroovyClass gc = new GroovyClass(true, "com/arahant/groovy/exports/PaychexTimeExport.groovy")
//		String file = gc.invokeConstructor().invokeMethod("export", [begDate, endDate] as Object[])
		String file = gc.invokeConstructor().export(begDate, endDate)

		out.put "exportFileName", file
		
//		println file
		out.put "returnValue", 0
		out.put "returnMessage", "Function succeeded."	
	}	
}

