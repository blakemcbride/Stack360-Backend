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

package com.arahant.services.standard.webservices.dynamicWebServices;

import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import java.lang.reflect.Method;

/**
 *
 * Arahant
 */
public class DynamicServiceValidator extends ServiceBase  {
	public void validateService(DataObject in, Object validationInstance, String methodName)  throws ArahantException {
	try {
			Method meth = validationInstance.getClass().getMethod("validate", new Class[]{String.class,DataObject.class});
			meth.invoke(validationInstance, new Object[]{methodName,in});
		} catch (Exception ex) {
			//System.out.println("ERROR " + ex.getCause().getMessage());
			//System.out.println("ERROR localized" + ex.getLocalizedMessage());
			//System.out.println("ERROR toString" + ex.toString());
			throw new ArahantException("DynamicServiceValidator error: " + ex.getCause().getMessage());
		}
	}
}
