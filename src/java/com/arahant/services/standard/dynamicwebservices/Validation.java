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



package com.arahant.services.standard.dynamicwebservices;

import com.arahant.utils.dynamicwebservices.DataObjectMap;

/**
 *
 * @author Blake McBride
 */
public class Validation {
	private DataObjectMap out;
	private DataObjectMap vals;
	private final static String validations = "_validations";
	
	public Validation(DataObjectMap v) {
		out = v;
		vals = out.getDataObjectMap(validations);
		if (vals == null) {
			vals = new DataObjectMap();
			out.put(validations, vals);
		}
	}
	
	public final void addString(Class cls, String field, int min, int max) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "string");
		fDOM.put("_min", min);
		fDOM.put("_max", max);
		fDOM.put("_required", min != 0);
	}
	
	public final void addNumber(Class cls, String field, double min, double max, boolean required, boolean canbezero) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "number");
		fDOM.put("_min", min);
		fDOM.put("_max", max);
		fDOM.put("_required", required);
		fDOM.put("_canbezero", canbezero);
	}
	
	public final void addDate(Class cls, String field, int min, int max, boolean required, boolean canbezero) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "date");
		fDOM.put("_min", min);
		fDOM.put("_max", max);
		fDOM.put("_required", required);
		fDOM.put("_canbezero", canbezero);
	}
	
	public final void addArray(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "array");
		fDOM.put("_required", required);
	}
	
	public final void addBoolean(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "boolean");
		fDOM.put("_required", required);
	}
	
	public final void addRadioStr(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "string");
		fDOM.put("_required", required);
		fDOM.put("_min", required ? 1 : 0);
		fDOM.put("_max", 1000);
	}
	
	public final void addRadioNum(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "number");
		fDOM.put("_required", required);
		fDOM.put("_canbezero", !required);
	}
	
	public final void addRadioDate(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "date");
		fDOM.put("_required", required);
		fDOM.put("_canbezero", !required);
	}
	
	public final void addRadioBoolean(Class cls, String field, boolean required) {
		DataObjectMap fDOM = getfDOM(cls, field);
		fDOM.put("_type", "boolean");
		fDOM.put("_required", required);
	}
	
	private DataObjectMap getfDOM(Class cls, String field) {
		String className = cls.getSimpleName();
		className = className.toLowerCase().charAt(0) + className.substring(1, className.length());
		DataObjectMap cDOM = vals.getDataObjectMap(className);
		if (cDOM == null)
			vals.put(className, cDOM=new DataObjectMap());
		DataObjectMap fDOM = cDOM.getDataObjectMap(field);
		if (fDOM == null)
			cDOM.put(field, fDOM=new DataObjectMap());
		return fDOM;
	}
	
}
