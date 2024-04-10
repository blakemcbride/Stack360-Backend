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

package com.arahant.services.standard.webservices.dynamicWebServices.tutorial;

import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;
import com.arahant.services.standard.webservices.dynamicWebServices.IArahantDynamicWebService;

/**
 *
 *
 *


 */
public class Tutorial3 /*implements  */ {

/*	@Override
	public DataObject run(DataObject inputMap, String methodName) throws ArahantWarning {

		if ("listShows".equals(methodName))
			return listShows(inputMap);

		if ("listQuotes".equals(methodName))
			return pickedShow(inputMap);

		throw new ArahantException("Unknown method called in Tutorial 3 "+methodName);
	}
*/

	public DataObject listQuotes(DataObject in)
	{

		DataObject d=new DataObject();

		switch (in.getInt("showId"))
		{
			case 1:

				d.put("itemArray",new DataObject[]{
					makeQuote("Jerry", "Seinfeld", "She's got man hands!"),
					makeQuote("George", "Costanza", "Hi, my name is George, I'm unemployed and I live with my parents."),
					makeQuote("Cosmo", "Kramer", "It's a Festivus miracle."),
					makeQuote("Sam", "Putty", "Feels like an Arby's night."),
				});
				break;

			case 2:
				d.put("itemArray",new DataObject[]{
					makeQuote("Bruce", "Dickinson", "I gotta have more Cowbell!"),
					makeQuote("Barry", "Gibb", "Talking bout, chest hair, and crazy cool medallions."),
				});
				break;

			case 3:

				d.put("itemArray",new DataObject[]{
					makeQuote("Al", "Bundy", "Feed me, or feed me to something. I just want to be part of the food chain"),
					makeQuote("Peggy", "Bundy", "Al just had it in his head that the cop was an impersonator. I'm sorry I put that in your head, Al."),
					makeQuote("Al", "Bundy", "Why doesn't Willie Nelson hold a benefit for me? He could call it AlAid"),
					makeQuote("Al", "Bundy", "Eighty-nine bottles of beer on the wall, eighty-nine bottles of beer, if one of those bottles should happen to fall... eighty-ten bottles of beer on the wall."),
					});
				break;
		}
		

		return d;
	}

	private DataObject makeQuote(String fname, String lname, String quote)
	{
		DataObject d=new DataObject();

		d.put("characterFirstName", fname);
		d.put("characterLastName", lname);
		d.put("quote", quote);

		return d;
	}


	public DataObject listShows(DataObject in)
	{
		DataObject d1=new DataObject();
		d1.put("id", "1");
		d1.put("name", "Seinfeld");

		DataObject d2=new DataObject();
		d2.put("id", "2");
		d2.put("name", "SNL");

		DataObject d3=new DataObject();
		d3.put("id", "3");
		d3.put("name", "Married With Children");

		DataObject d=new DataObject();

		d.put("itemArray",new DataObject[]{d1,d2,d3});

		return d;
	}

	public DataObject testSend(DataObject in)
	{
		for (String x : in.getStringArray("quoteArray"))
			System.out.println(x);
		return new DataObject();
	}



}
