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

import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;
import com.arahant.services.standard.webservices.dynamicWebServices.IArahantDynamicWebService;

/**
 *
 *
 *


 */
public class Tutorial2  {


	public DataObject listShows(DataObject inputMap) throws ArahantWarning {

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

}
