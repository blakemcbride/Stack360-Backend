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


package com.arahant.utils.dynamicwebservices;

import com.arahant.services.standard.dynamicwebservices.WsDataObject;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Blake McBride
 */
public class DataObject {

	public static final int MaxStringSize = 10;
	public static final String BOOLEAN_TYPE = "Boolean";
	public static final String CHARACTER_TYPE = "Character";
	public static final String STRING_TYPE = "String";
	public static final String INTEGER_TYPE = "Integer";
	public static final String LONG_TYPE = "Long";
	public static final String DOUBLE_TYPE = "Double";
	public static final String OBJECTMAP_TYPE = "ObjectMap";
	public static final String OBJECTLIST_TYPE = "ObjectList";

	//
	private String name;
	private int rank = 0;
	private int[] shape;
	private Object value;

	public WsDataObject toWsDataObject() {
		WsDataObject obj = new WsDataObject();
		obj.setName(name);
		obj.setType(this.getType());
		obj.setRank(rank);
		if (rank > 0 && shape != null) {
			StringBuilder sb = new StringBuilder();
			boolean addComma = false;
			for (int i : shape) {
				if (addComma)
					sb.append(',');
				else
					addComma = true;
				sb.append(i);
			}
			obj.setShape(sb.toString());
		}
		@SuppressWarnings("rawtypes")
		Class cls = value.getClass();
		if (rank == 0 && shape == null  &&  value != null) {
			if (cls == Boolean.class)
				obj.setValue(((Boolean) value) == true ? "true" : "false");
			else if (cls == Character.class)
				obj.setValue(((Character) value).toString());
			else if (cls == String.class) {
				String str = (String) value;
				if (str.length() <= MaxStringSize)
					obj.setValue(str);
				else
					obj.setLongValue(str);
			} else if (cls == Integer.class)
				obj.setValue(((Integer) value).toString());
			else if (cls == Long.class)
				obj.setValue(((Long) value).toString());
			else if (cls == Double.class)
				obj.setValue(((Double) value).toString());
			else if (cls == DataObjectMap.class)
				obj.getObjectList().addAll(((DataObjectMap) value).toWsList());
			else if (cls == DataObjectList.class)
				obj.getObjectList().addAll(((DataObjectList) value).toWsList());
		}  else if (rank > 0 && shape != null && value != null) {
			if (cls == int[].class)
				obj.setLongValue(stringIntArray());
			else if(cls == long[].class)
				obj.setLongValue(stringLongArray());
			else if(cls == double[].class)
				obj.setLongValue(stringDoubleArray());
			else if (cls == String[].class)
				obj.getStringList().addAll(stringArray());
		}
		return obj;
	}

	public DataObject(WsDataObject obj) {
		this.name = obj.getName();
		String type = obj.getType();
		this.rank = obj.getRank();
		String sh = obj.getShape();
		if (this.rank > 0  &&  sh != null) {
			String[] sa = sh.split(",");
			shape = new int[sa.length];
			for (int i=0 ; i < sa.length ; i++)
				shape[i] = Integer.parseInt(sa[i]);
		}
		String val = obj.getValue();
		if (shape == null) {
			if (type.equals(BOOLEAN_TYPE))
				value = val.equals("true");
			else if (type.equals(CHARACTER_TYPE))
				value = val.charAt(0);
			else if (type.equals(STRING_TYPE))
				value = val != null ? val : obj.getLongValue();
			else if (type.equals(INTEGER_TYPE))
				value = Integer.parseInt(val);
			else if (type.equals(LONG_TYPE))
				value = Long.parseLong(val);
			else if (type.equals(DOUBLE_TYPE))
				value = Double.parseDouble(val);
			else if (type.equals(OBJECTMAP_TYPE))
				value = new DataObjectMap(obj.getObjectList());
			else if (type.equals(OBJECTLIST_TYPE))
				value = new DataObjectList(obj.getObjectList());
		} else {
			if (type.equals(INTEGER_TYPE))
				value = integerArray(obj.getLongValue());
			else if(type.equals(LONG_TYPE))
				value = longArray(obj.getLongValue());
			else if(type.equals(DOUBLE_TYPE))
				value = doubleArray(obj.getLongValue());
			else if(type.equals(STRING_TYPE))
				value = toStringArray(obj.getStringList());
		}
	}

	private static String[] toStringArray(List<String> lst) {
		int sz = lst.size(), i =0;
		String[] sa = new String[sz];
		for (String str : lst)
			sa[i++] = str;
		return sa;
	}

	public DataObject(String name, boolean val) {
		this.name = name;
		this.value = (Boolean) val;
	}

	public DataObject(String name, char val) {
		this.name = name;
		this.value = (Character) val;
	}

	public DataObject(String name, String val) {
		this.name = name;
		this.value = val;
	}

	public DataObject(String name, int val) {
		this.name = name;
		this.value = (Integer) val;
	}

	public DataObject(String name, long val) {
		this.name = name;
		this.value = (Long) val;
	}

	public DataObject(String name, double val) {
		this.name = name;
		this.value = (Double) val;
	}

	public DataObject(String name, DataObjectMap val) {
		this.name = name;
		this.value = val;
	}

	public DataObject(String name, DataObjectList val) {
		this.name = name;
		this.value = val;
	}

	public DataObject(String name, int[] val) {
		this.name = name;
		this.value = val;
		this.rank = 1;
		this.shape = new int[1];
		this.shape[0] = val.length;
	}

	public DataObject(String name, long[] val) {
		this.name = name;
		this.value = val;
		this.rank = 1;
		this.shape = new int[1];
		this.shape[0] = val.length;
	}

	public DataObject(String name, double[] val) {
		this.name = name;
		this.value = val;
		this.rank = 1;
		this.shape = new int[1];
		this.shape[0] = val.length;
	}

	public DataObject(String name, String[] val) {
		this.name = name;
		this.value = val;
		this.rank = 1;
		this.shape = new int[1];
		this.shape[0] = val.length;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		if (value == null)
			return null;
		@SuppressWarnings("rawtypes")
		Class cls = value.getClass();

		if (cls == Boolean.class)
			return BOOLEAN_TYPE;
		else if (cls == Character.class)
			return CHARACTER_TYPE;
		else if (cls == String.class || cls == String[].class)
			return STRING_TYPE;
		else if (cls == Integer.class || cls == int[].class)
			return INTEGER_TYPE;
		else if (cls == Long.class || cls == long[].class)
			return LONG_TYPE;
		else if (cls == Double.class || cls == double[].class)
			return DOUBLE_TYPE;
		else if (cls == DataObjectMap.class)
			return OBJECTMAP_TYPE;
		else if (cls == DataObjectList.class)
			return OBJECTLIST_TYPE;
		else
			return null;
	}

	public int getRank() {
		return rank;
	}

	public int[] getShape() {
		return shape;
	}

	public Object getValue() {
		return value;
	}

	public int[] getIntArray() {
		return (int[]) value;
	}

	public long[] getLongArray() {
		return (long[]) value;
	}

	public double[] getDoubleArray() {
		return (double[]) value;
	}

	public String[] getStringArray() {
		return (String[]) value;
	}

	private String stringIntArray() {
		if (value == null || rank != 1 || value.getClass() != int[].class)
			return null;
		StringBuilder sb = new StringBuilder();
		int[] vec = (int[]) value;
		boolean addSpace = false;
		for (int i = 0; i < vec.length; i++)
			if (addSpace)
				sb.append(" ").append(String.valueOf(vec[i]));
			else {
				sb.append(String.valueOf(vec[i]));
				addSpace = true;
			}
		return sb.toString();
	}

	private String stringLongArray() {
		if (value == null || rank != 1 || value.getClass() != long[].class)
			return null;
		StringBuilder sb = new StringBuilder();
		long[] vec = (long[]) value;
		boolean addSpace = false;
		for (int i = 0; i < vec.length; i++)
			if (addSpace)
				sb.append(" ").append(String.valueOf(vec[i]));
			else {
				sb.append(String.valueOf(vec[i]));
				addSpace = true;
			}
		return sb.toString();
	}

	private String stringDoubleArray() {
		if (value == null || rank != 1 || value.getClass() != double[].class)
			return null;
		StringBuilder sb = new StringBuilder();
		double[] vec = (double[]) value;
		boolean addSpace = false;
		for (int i = 0; i < vec.length; i++)
			if (addSpace)
				sb.append(" ").append(String.valueOf(vec[i]));
			else {
				sb.append(String.valueOf(vec[i]));
				addSpace = true;
			}
		return sb.toString();
	}

	/**
	 * Specifically meant for XML output
	 * @return 
	 */
	private String stringStringArray() {
		if (value == null || rank != 1 || value.getClass() != String[].class)
			return null;
		StringBuilder sb = new StringBuilder();
		String [] vec = (String[]) value;
		boolean addSpace = false;
		for (int i = 0; i < vec.length; i++) {
//			String str = fixString(String.valueOf(vec[i]));
			String str = String.valueOf(vec[i]);
			str = str.replaceAll("\"", "\\\\\"");
			if (addSpace)
				sb.append(" \"").append(str).append("\"");
			else {
				sb.append("\"").append(str).append("\"");
				addSpace = true;
			}
		}
		return sb.toString();
	}

	private List<String> stringArray() {
		if (value == null || rank != 1 || value.getClass() != String[].class)
			return null;
		return Arrays.asList((String[]) value);
	}

	private int[] integerArray(String lst) {
		if (lst == null)
			return new int[0];
		String [] items = lst.split(" ");
		int [] vec = new int[items.length];
		for (int i=0 ; i < items.length ; i++)
			vec[i] = Integer.parseInt(items[i]);
		return vec;
	}

	private long[] longArray(String lst) {
		if (lst == null)
			return new long[0];
		String [] items = lst.split(" ");
		long [] vec = new long[items.length];
		for (int i=0 ; i < items.length ; i++)
			vec[i] = Long.parseLong(items[i]);
		return vec;
	}

	private double[] doubleArray(String lst) {
		if (lst == null)
			return new double[0];
		String [] items = lst.split(" ");
		double [] vec = new double[items.length];
		for (int i=0 ; i < items.length ; i++)
			vec[i] = Double.parseDouble(items[i]);
		return vec;
	}
	
	@Override
	public String toString() {
		@SuppressWarnings("rawtypes")
		Class cls = value.getClass();
		if (rank == 0 && shape == null  &&  value != null) {
			if (cls == Boolean.class)
				return ((Boolean) value) == true ? "true" : "false";
			else if (cls == Character.class)
				return ((Character) value).toString();
			else if (cls == String.class) {
				return (String) value;
			} else if (cls == Integer.class)
				return ((Integer) value).toString();
			else if (cls == Long.class)
				return ((Long) value).toString();
			else if (cls == Double.class)
				return ((Double) value).toString();
			else if (cls == DataObjectMap.class)
				return "";
			else if (cls == DataObjectList.class)
				return "";
		} else if (rank > 0 && shape != null && value != null) {
			if (cls == int[].class)
				return stringIntArray();
			else if(cls == long[].class)
				return stringLongArray();
			else if(cls == double[].class)
				return stringDoubleArray();
			else if (cls == String[].class)
				return stringStringArray();
		}
		return "";
	}
	
	/**
	 * Fix string for XML output
	 * 
	 * @param str
	 * @return str with XML escape characters 
	 */
//	private static String fixString(String str) {
//		str = str.replaceAll("&", "&amp;");
//		str = str.replaceAll("<", "&lt;");
//		str = str.replaceAll(">", "&gt;");
//		str = str.replaceAll("'", "&apos;");
//		str = str.replaceAll("\"", "&quot;");
//		return str;
//	}

}
