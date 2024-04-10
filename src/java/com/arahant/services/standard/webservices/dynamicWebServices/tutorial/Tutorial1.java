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
public class Tutorial1 {

	public DataObject test(DataObject inputMap) throws ArahantWarning {

		int x=inputMap.getInt("x");
		int y=inputMap.getInt("y");

		DataObject d=new DataObject();
		d.put("answer",x+y);

		return d;
		
	}

}
