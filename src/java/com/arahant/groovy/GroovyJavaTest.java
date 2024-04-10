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
package com.arahant.groovy;

import com.arahant.lisp.ABCL;


/**
 *
 * @author Blake McBride
 */
public class GroovyJavaTest {

	public static void main(final String [] args) throws Exception {
		GroovyClass.invoke(true, "com/arahant/groovy/GroovyTest.groovy", "main", null, 77);

		if (true)
			return;
		
		ABCL.init();
		
		
		
		String name = (String) GroovyClass.invoke(true, "com/arahant/groovy/exports/NorthAmericaAdministratorsExport.groovy", "doExport", (Object) null);
		System.out.println("Name = " + name);
		
	}
	
	public static void fun() {
		System.out.println("Hello from Java 2!");
	}
}
