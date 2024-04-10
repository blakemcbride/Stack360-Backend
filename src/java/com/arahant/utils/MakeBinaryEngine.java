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
 * Created on Oct 25, 2007
 * 
 */
package com.arahant.utils;

import java.io.File;
import java.io.FileOutputStream;



/**
 * 
 *
 * Created on Oct 25, 2007
 *
 */
public class MakeBinaryEngine {

	public static void main (String args[])
	{
		
		
		try {
			System.out.println("Making new ai engine templates.  This will take a few minutes.");
			File fyle=new File("C:\\Arahant\\Prophet\\Backend\\build\\BinaryTemplate.jeb");
			fyle.delete();
			ArahantSession.getAITemplate().bsave(new FileOutputStream("C:\\Arahant\\Prophet\\Backend\\src\\BinaryTemplate.jeb"));
			ArahantSession.getAITemplate().bsave(new FileOutputStream("C:\\Arahant\\Prophet\\Backend\\build\\BinaryTemplate.jeb"));
			System.out.println("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

	
