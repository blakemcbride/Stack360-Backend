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
 * Created on Sep 8, 2006
 * 
 */
package com.arahant.utils;

import com.arahant.beans.ZipcodeLookup;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;



/**
 * 
 *
 * Created on Sep 8, 2006
 *
 */
public class ZipCodeLoader {
	
	public static void main(final String args[])
	{
		final BufferedReader br=new BufferedReader(new InputStreamReader(ZipCodeLoader.class.getClassLoader().getResourceAsStream("ziplist.csv")));
		final HibernateSessionUtil hsu = new HibernateSessionUtil();
		
		try {
			hsu.beginTransaction();
			while (br.ready())
			{
				final String line=br.readLine();
				
				final StringTokenizer st=new StringTokenizer(line,",");
				
				final String city=st.nextToken();
				final String state=st.nextToken();
				String zip=st.nextToken();
				final String county=st.nextToken();
				
				if (zip.length()<5)
					zip="0"+zip;
				
				
				final ZipcodeLookup zcl=new ZipcodeLookup();
				zcl.generateId();
				zcl.setCity(city);
				zcl.setCounty(county);
				zcl.setState(state);
				zcl.setZipcode(zip);
				
				hsu.insert(zcl);
				
			}
			
			hsu.commitTransaction();
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		hsu.close();
	}

}

	
