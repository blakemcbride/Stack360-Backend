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

/**
 *
 * Arahant
 */
public class DataObjectValidation {

	private String fieldName;
	private String type;
	private long minvalue;
	private long maxvalue;
	private int decimalPlaces = 0;
	private boolean canBeZero = true;
	private boolean required = false;

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	public static final String DataObjectValidationType_STRING = "string";
	public static final String DataObjectValidationType_NUMBER = "number";
	public static final String DataObjectValidationType_DATE = "date";
	public static final String DataObjectValidationType_ARRAY = "array";
	public static final String DataObjectValidationType_BOOLEAN = "boolean";
	public static final String DataObjectValidationFieldAttribute_TYPE = "type";
	public static final String DataObjectValidationFieldAttribute_MIN = "min";
	public static final String DataObjectValidationFieldAttribute_MAX = "max";
	public static final String DataObjectValidationFieldAttribute_DECIMAL = "decimal";
	public static final String DataObjectValidationFieldAttribute_CANBEZERO = "canbezero";
	public static final String DataObjectValidationFieldAttribute_REQUIRED = "required";

	public boolean getCanBeZero() {
		return canBeZero;
	}

	public void setCanBeZero(boolean canBeZero) {
		this.canBeZero = canBeZero;
	}

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public long getMaxvalue() {
		return maxvalue;
	}

	public void setMaxvalue(long maxvalue) {
		this.maxvalue = maxvalue;
	}

	public long getMinvalue() {
		return minvalue;
	}

	public void setMinvalue(long minvalue) {
		this.minvalue = minvalue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		if (type.equalsIgnoreCase(DataObjectValidationType_ARRAY)) {
			setMinvalue(0);
			if (required) {
				setMinvalue(1);
			}
			setMaxvalue(500);
		} else if (type.equalsIgnoreCase(DataObjectValidationType_STRING)) {
			setMinvalue(0);
			if (required) {
				setMinvalue(1);
			}
			setMaxvalue(1000000);
		} else if (type.equalsIgnoreCase(DataObjectValidationType_NUMBER)) {
			setMinvalue(-12345);
			setMaxvalue(10000000);
		} else if (type.equalsIgnoreCase(DataObjectValidationType_DATE)) {
			setMinvalue(10000101);
			setMaxvalue(30000101);
		} else if (type.equalsIgnoreCase(DataObjectValidationType_BOOLEAN)) {
		} else {
			throw new ArahantException("Invalid field type:  Valid types[string,array,number,date,boolean] but found [" + type + "].");
		}
	}

	private void create(DataObject fields) {
		DataObject fieldAttributes = new DataObject();
		fieldAttributes.put(DataObjectValidationFieldAttribute_TYPE, this.getType());
		fieldAttributes.put(DataObjectValidationFieldAttribute_MIN, this.getMinvalue());
		fieldAttributes.put(DataObjectValidationFieldAttribute_MAX, this.getMaxvalue());
		fieldAttributes.put(DataObjectValidationFieldAttribute_DECIMAL, this.getDecimalPlaces());
		fieldAttributes.put(DataObjectValidationFieldAttribute_CANBEZERO, this.getCanBeZero());
		fieldAttributes.put(DataObjectValidationFieldAttribute_REQUIRED, this.getRequired());
		fieldAttributes.setName(this.getFieldName());
		fields.put(this.getFieldName(), fieldAttributes);

	}

	public static void createBooleanValidation(DataObject fields, String fieldName) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_BOOLEAN);
		dov.setFieldName(fieldName);
		dov.create(fields);
	}

		public static void createBooleanValidation(DataObject fields, String fieldName, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_BOOLEAN);
		dov.setRequired(required);
		dov.setFieldName(fieldName);
		dov.create(fields);
	}
	public static void createArrayValidation(DataObject fields, String fieldName, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_ARRAY);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.create(fields);
	}

	public static void createDateValidation(DataObject fields, String fieldName, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_DATE);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.create(fields);
	}

	public static void createDateValidation(DataObject fields, String fieldName, boolean required, boolean dateCanBeZero) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_DATE);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setCanBeZero(dateCanBeZero);
		dov.create(fields);
	}

	public static void createStringValidation(DataObject fields, String fieldName, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_STRING);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.create(fields);
	}

	public static void createStringValidation(DataObject fields, String fieldName, int min, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_STRING);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setMinvalue(min);
		dov.create(fields);
	}

	public static void createStringValidation(DataObject fields, String fieldName, int min, int max, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_STRING);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setMinvalue(min);
		dov.setMaxvalue(max);
		dov.create(fields);
	}

		public static void createNumberValidation(DataObject fields, String fieldName, boolean required) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_NUMBER);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.create(fields);
	}
	public static void createNumberValidation(DataObject fields, String fieldName, boolean required, int decimal) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_NUMBER);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setDecimalPlaces(decimal);
		dov.create(fields);
	}

	public static void createNumberValidation(DataObject fields, String fieldName, int min, boolean required, int decimal) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_NUMBER);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setMinvalue(min);
		dov.setDecimalPlaces(decimal);
		dov.create(fields);
	}

	public static void createNumberValidation(DataObject fields, String fieldName, int min, int max, boolean required, int decimal) {
		DataObjectValidation dov = new DataObjectValidation();
		dov.setType(DataObjectValidationType_NUMBER);
		dov.setFieldName(fieldName);
		dov.setRequired(required);
		dov.setMinvalue(min);
		dov.setMaxvalue(max);
		dov.setDecimalPlaces(decimal);
		dov.create(fields);
	}

	public static DataObject createValidationForMethod(String methodName, DataObject fields) {
		DataObject dataObjectMethod = new DataObject();
		dataObjectMethod.put(methodName, fields);
		dataObjectMethod.setName(methodName);
		return dataObjectMethod;
	}

	private boolean validateNumber(String fieldName, String fieldValue, DataObject method) throws Exception {

		int min = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MIN));
		int max = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MAX));
		int decimal = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_DECIMAL));
		boolean required = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_REQUIRED));
		//check if this is required, if so, make sure it's not null or empty
		if (required && isEmpty(fieldValue)) {
			throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " must be in this range: " + min + " to " + max);
		}

		if (!required && isEmpty(fieldValue)) {
			return true;
		}

		if (decimal > 0) {
			//check if length is valid
			int pos = fieldValue.indexOf(".");
			if (pos > 0) {
				String data = fieldValue.substring(pos + 1);
				if (data.length() > decimal) {
					throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " requires " + decimal + " decimal places.");
				}
				//if that passes then check the values
				double value = Double.parseDouble(fieldValue);
				if (value < min || value > max) {
					throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " must be in this range: " + min + " to " + max);
				}
				return true; //don't need to check the integer portion of this value
			}
		} else {
			//does this have decimal?
			if (fieldValue.contains(".")) {
				throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " cannot have decimal in it.  Please verify your validation atrribute.");
			}
		}

		//just check normal number range now
		//we have decimal value with no decimal points
		int value = Integer.parseInt(fieldValue);
		if ((value < min || value > max)) {
			throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " must be in this range: " + min + " to " + max);
		}
		return true;
	}

	private boolean isEmpty(final String str) {
		return (str == null || str.equals(""));
	}

	private boolean validateString(String fieldName, String fieldValue, DataObject method) throws Exception {
		int min = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MIN));
		int max = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MAX));
		boolean required = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_REQUIRED));

		//check if this is required, if so, make sure it's not null or empty
		if (required && isEmpty(fieldValue)) {
			throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " must be " + min + " to " + max + " characters.");
		}

		if (!required && isEmpty(fieldValue)) {
			return true;
		}

		if (fieldValue == null) {
			throw new Exception(fieldName + " cannot be NULL.");
		}

		if (fieldValue.length() < min || fieldValue.length() > max) {
			throw new Exception("Field/Value " + fieldName + "/" + fieldValue + " must be " + min + " to " + max + " characters.");
		}
		return true;
	}

	private boolean validateDate(String fieldName, String fieldValue, DataObject method) throws Exception {
		int min = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MIN));
		int max = Integer.parseInt(method.getString(DataObjectValidationFieldAttribute_MAX));
		boolean dateCanBeZero = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_CANBEZERO));
		boolean required = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_REQUIRED));

		//check if this is required, if so, make sure it's not null or empty
		if (required && isEmpty(fieldValue)) {
			throw new Exception("Field " + fieldName + " is required.");
		}

		if (!required && isEmpty(fieldValue)) {
			return true;
		}

		int value = Integer.parseInt(fieldValue);
		if (value == 0 && dateCanBeZero) {
			return true; //date can be zero
		}
		if (value == 0) {
			throw new Exception(fieldName + " cannot be zero");
		} else if (value < min || value > max) {
			throw new Exception("[DateOutOfRange] Field/Value " + fieldName + "/" + fieldValue + " must be this date range: " + min + " to " + max);
		}
		return true;
	}

	private boolean validateArray(String fieldName, String fieldValue[], DataObject method) throws Exception {
		boolean required = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_REQUIRED));
		if (fieldValue.length > 0) {
			return true;
		} else {
			throw new ArahantException("Field " + fieldName + " is an array and must contain at least one item in it.");
		}

	}

	private boolean validateBoolean(String fieldName, String fieldValue, DataObject method) throws Exception {
		boolean required = Boolean.parseBoolean(method.getString(DataObjectValidationFieldAttribute_REQUIRED));
		//check if this is required, if so, make sure it's not null or empty
		if (required && isEmpty(fieldValue)) {
			throw new Exception("Field " + fieldName + " is required.");
		}

		if (!required && isEmpty(fieldValue)) {
			return true;
		}

		if (fieldValue.equalsIgnoreCase("true") || fieldValue.equalsIgnoreCase("false")) {
			return true;
		} else {
			throw new ArahantException("Field " + fieldName + " is boolean.  It must contain true or false.");
		}
	}
	private void debugPrint(String fieldName, String fieldValue, DataObject m) {
		System.out.println("Field/value: " + fieldName + "/" + fieldValue);
		System.out.println("Field " + m.getName() + " Type = " + m.getString(DataObjectValidationFieldAttribute_TYPE));
		System.out.println("Field " + m.getName() + " Min = " + m.getString(DataObjectValidationFieldAttribute_MIN));
		System.out.println("Field " + m.getName() + " Max = " + m.getString(DataObjectValidationFieldAttribute_MAX));
		System.out.println("Field " + m.getName() + " zero = " + m.getString(DataObjectValidationFieldAttribute_CANBEZERO));
		System.out.println("Field " + m.getName() + " decimal = " + m.getString(DataObjectValidationFieldAttribute_DECIMAL));
		System.out.println("----------------------------------------------------\n");
	}

	public boolean validate(DataObject validator, DataObject validatee, String methodName) throws ArahantException {
		return validate(validator, validatee, methodName, false);
	}

	public boolean validate(DataObject validator, DataObject inputFields, String methodName, boolean debug) throws ArahantException {
		try {
			DataObject getValidations = validator.get("_validation");//get the list of validation fields
			DataObject methodFields = getValidations.get(methodName);//uses index 0 to get to the method name

			//in some cases we may just have no fields to validate
			if (methodFields == null) {
				return true;
			}

			String fieldName = "";
			String fieldValue = "";
			String fieldsToValidate = "";
			String fieldsInput = "";

			for (DataObject m : methodFields) {
				fieldsToValidate += m.getName() + ",";
				//loop throug the fields in the method
				for (DataObject inputData : inputFields) {
					fieldName = inputData.getName();
					fieldValue = inputData.getAttrValue();

					//if field matches, then check if the values are correct
					if (fieldName.equalsIgnoreCase(m.getName())) {
						//uncomment to debug
						if (debug) {
							debugPrint(fieldName, fieldValue, m);
						}

						fieldsInput += fieldName + ",";
						String fieldType = m.getString(DataObjectValidationFieldAttribute_TYPE);
						if (fieldType.equalsIgnoreCase(DataObjectValidationType_STRING)) {
							validateString(fieldName, fieldValue, m);
						} else if (fieldType.equalsIgnoreCase(DataObjectValidationType_NUMBER)) {
							validateNumber(fieldName, fieldValue, m);
						} else if (fieldType.equalsIgnoreCase(DataObjectValidationType_DATE)) {
							validateDate(fieldName, fieldValue, m);
						} else if (fieldType.equalsIgnoreCase(DataObjectValidationType_ARRAY)) {
							String[] stringArray = inputData.getStringArray(fieldName);
							validateArray(fieldName, stringArray, m);
						} else if (fieldType.equalsIgnoreCase(DataObjectValidationType_BOOLEAN)) {
							validateBoolean(fieldName, fieldValue, m);
						} else {
							System.out.println("Field type " + fieldType);
							throw new ArahantException("Validation failed: Unsupported data type [" + fieldType + "] Valid types[string,array,number,date].");
						}
					}
				}
			}

			//last check, make sure the number of fields to validate matches fields input
			if (fieldsInput.length() != fieldsToValidate.length()) {
				throw new ArahantException("The fields to validate does not match the input fields. \nFields to validate [" + fieldsToValidate + "] vs input [" + fieldsInput + "].  Please check to make sure the fields are typed correctly and the number of fields match.");
			}
		} catch (Exception e) {
			throw new ArahantException("Validation failed: " + e.getMessage());
		}
		return true;

	}


	// ========= VALIDATE
}
