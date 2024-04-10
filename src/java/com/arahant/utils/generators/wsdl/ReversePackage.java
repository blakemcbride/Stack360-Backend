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
 * Created on Jul 2, 2007
 * 
 */
package com.arahant.utils.generators.wsdl;

import java.util.StringTokenizer;



/**
 * 
 *
 * Created on Jul 2, 2007
 *
 */
public class ReversePackage {

	public static void main(final String args[])
	{
		String ret="";
		final StringTokenizer sttok=new StringTokenizer(args[0],".");
		while (sttok.hasMoreTokens())
		{
			ret=sttok.nextToken()+"."+ret;
		}
		
		System.out.println(ret.substring(0,ret.length()-1));
	}

}

	
