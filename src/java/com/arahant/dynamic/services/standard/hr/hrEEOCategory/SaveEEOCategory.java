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

package com.arahant.dynamic.services.standard.hr.hrEEOCategory;

import com.arahant.annotation.DynamicValidation;
import com.arahant.business.BHREEOCategory;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

/**
 *
 * Arahant
 */
public class SaveEEOCategory {

	@DynamicValidation (required=true, type="string")
	public String id;
	@DynamicValidation (max=45, min=1, type="string", required=true)
	public String name;
	@DynamicValidation (max=1000, min=0, type="number", required=true, canbezero=true)
	public String numberField;
	@DynamicValidation (max=30000101, min=19000101, type="date", required=true)
	public String dateField;
	@DynamicValidation (type="boolean", required=true)
	public String booleanField;

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		final BHREEOCategory x=new BHREEOCategory(in.getString("id"));
		x.setName(in.getString("name"));
		x.update();
	}

}
