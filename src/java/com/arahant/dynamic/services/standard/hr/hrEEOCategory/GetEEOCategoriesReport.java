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

import com.arahant.business.BHREEOCategory;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObjectMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class GetEEOCategoriesReport {

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		try {
			out.put("fileName", BHREEOCategory.getReport(hsu));
		} catch (Exception ex) {
			Logger.getLogger(GetEEOCategoriesReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
