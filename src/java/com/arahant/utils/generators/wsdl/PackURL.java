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
public class PackURL {

	public static String fixName( String path)
	{
            path=path.replace('\\', '/');
             String remainder=path.substring(path.indexOf("/services/")+"/services/".length()-1);;
                    StringTokenizer stok=new StringTokenizer(remainder, "/");
                    String name="";
                    
                    while (stok.hasMoreTokens())
                    {
                        String tok=stok.nextToken();
                        if (tok.endsWith("Ops.java"))
                            continue;
                        name+=tok.substring(0,1).toUpperCase()+tok.substring(1);
                    }
 
/*			final int pos=in.indexOf("custom");
			
			String name=in.substring(pos+"custom".length()+1);
			
			final int endPos=name.indexOf('\\');
			
			final int endPos2=name.indexOf('/');
					
			if (endPos!=-1 && (endPos< endPos2 || endPos2==-1))
				name=name.substring(0,endPos);
			if (endPos2!=-1 && (endPos2< endPos || endPos==-1))
				name=name.substring(0,endPos2);
			
                        
                        name=name.substring(0,1).toUpperCase()+name.substring(1);
*/
                   
            return name;

	}
        
	
	public static void main (final String args[])
	{
		final String in=args[0];

		System.out.println(fixName(in));

		//System.out.println(args[0].substring(args[0].indexOf("com")).replaceAll("/", "_").replaceAll("\\\\", "_")+"_");
	}
}

	
