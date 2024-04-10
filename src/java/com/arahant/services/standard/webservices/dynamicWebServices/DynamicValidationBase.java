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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Arahant
 */
public class DynamicValidationBase implements IDynamicWebServiceValidation {

	@Override
	public DataObject getAllValidations(DataObject in) {

		List<DataObject> listOfValidationFields = new ArrayList<DataObject>();
		final Method[] mems = this.getClass().getMethods();
		for (final Method element : mems) {
			final String methodName = element.getName();
			if (methodName.startsWith("getValidationFor")) {
				try {
					Method meth = this.getClass().getMethod(methodName);
					DataObject outputMap = (DataObject) meth.invoke(this);
					listOfValidationFields.add(outputMap);
				} catch (Exception ex) {
					throw new ArahantException("DynamicValidationBase.getAllValidations error: " + ex.getMessage());
				}
			}

		}
		//loop and put the validations into array
		DataObject validations = new DataObject();
		DataObject[] validation = new DataObject[listOfValidationFields.size()];
		for (int i = 0; i < listOfValidationFields.size(); i++) {
			validation[i] = listOfValidationFields.get(i);
		}
		validations.put(VALIDATION_STRING, validation);
		validations.setName(VALIDATION_STRING);
		return validations;
	}

	@Override
	public DataObject getMethodValidation(String validationMethod) {
		final Method[] mems = this.getClass().getMethods();
		for (final Method element : mems) {
			final String methodName = element.getName();
			if (methodName.equalsIgnoreCase(validationMethod)) {
				try {
					Method meth = this.getClass().getMethod(methodName);
					DataObject outputMap = (DataObject) meth.invoke(this);
					DataObject[] validation = new DataObject[1];
					DataObject out = new DataObject();
					validation[0] = outputMap;
					out.put(VALIDATION_STRING, outputMap);
					return out;
				} catch (Exception ex) {
					throw new ArahantException("DynamicValidationBase.getMethodValidation error: " + ex.getMessage());
				}
			}

		}
		return null;
	}

	@Override
	public void validate(String methodName, DataObject in) {
		//don't need to return anything
		//if fail then error will be throw by DataObjectValidation.validate()
		DataObjectValidation validation = new DataObjectValidation();
		DataObject methodFields = getMethodValidation(methodName);
		if (methodFields==null) return;
		boolean result = validation.validate(methodFields, in, methodName);
	}
	public void getEmpty(DataObject in, Object obj){
		//just in client needs valition, then call empty
	}
}
