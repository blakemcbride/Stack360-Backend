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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Blake McBride
 */
public class DataObjectList implements Iterable<DataObject> {

	private List<DataObject> objList = new ArrayList<DataObject>();

	public DataObjectList() {
	}

	public DataObjectList(List<WsDataObject> wsobjarray) {
		DataObject dobj;
		if (wsobjarray != null)
			for (WsDataObject wsobj : wsobjarray) {
				dobj = new DataObject(wsobj);
				objList.add(dobj);
			}
	}

	public List<WsDataObject> toWsList() {
		List<WsDataObject> wsList = new LinkedList<WsDataObject>();
		for (DataObject obj : this)
			wsList.add(obj.toWsDataObject());
		return wsList;
	}

	public DataObjectList add(DataObject obj) {
		objList.add(obj);
		return this;
	}

	public DataObject get(int idx) {
		return objList.get(idx);
	}

	public DataObjectList put(boolean val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public boolean getBoolean(int idx) {
		return (Boolean) objList.get(idx).getValue();
	}

	public DataObjectList add(char val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public char getChar(int idx) {
		return (Character) objList.get(idx).getValue();
	}

	public DataObjectList add(String val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public String getString(int idx) {
		return (String) objList.get(idx).getValue();
	}

	public DataObjectList add(int val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public int getInt(int idx) {
		return (Integer) objList.get(idx).getValue();
	}

	public DataObjectList add(long val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public long getLong(int idx) {
		return (Long) objList.get(idx).getValue();
	}

	public DataObjectList add(double val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public double getDouble(int idx) {
		return (Double) objList.get(idx).getValue();
	}

	public DataObjectList add(int[] val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public int[] getIntArray(int idx) {
		return (int[]) objList.get(idx).getValue();
	}

	public DataObjectList add(long[] val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public long[] getLongArray(int idx) {
		return (long[]) objList.get(idx).getValue();
	}

	public DataObjectList add(double[] val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public double[] getDoubleArray(int idx) {
		return (double[]) objList.get(idx).getValue();
	}

	public DataObjectList add(String[] val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public String[] getStringArray(int idx) {
		return (String[]) objList.get(idx).getValue();
	}

	public DataObjectList add(DataObjectList val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public DataObjectList add(DataObjectMap val) {
		objList.add(new DataObject(null, val));
		return this;
	}

	public DataObjectList add(String name, DataObjectList val) {
		objList.add(new DataObject(name, val));
		return this;
	}

	public DataObjectList getDataObjectList(int idx) {
		return (DataObjectList) objList.get(idx).getValue();
	}

	@Override
	public Iterator<DataObject> iterator() {
		return objList.iterator();
	}

	public void print() {
		this.printObj(0);
	}

	public void printObj(int level) {
		for (DataObject dobj : this) {
			for (int i = 0; i++ < (level * 4);)
				System.out.print(" ");
			if (dobj.getName() == null)
				System.out.print("-- " + ", " + dobj.getType() + ", " + dobj.getRank());
			else
				System.out.print(dobj.getName() + ", " + dobj.getType() + ", " + dobj.getRank());
			if (dobj.getRank() > 0)
				System.out.print(", " + toString(dobj.getShape()));
			System.out.print(" = ");
			if (dobj.getType().equals(DataObject.OBJECTMAP_TYPE)) {
				DataObjectMap dom = (DataObjectMap) dobj.getValue();
				System.out.println();
				if (dom != null)
					dom.printObj(level + 1);
			} else if (dobj.getType().equals(DataObject.OBJECTLIST_TYPE)) {
				DataObjectList dol = (DataObjectList) dobj.getValue();
				System.out.println();
				if (dol != null)
					dol.printObj(level + 1);
			} else if (dobj.getRank() == 0)
				if (dobj.getType().equals(DataObject.STRING_TYPE))
					System.out.println("\"" + dobj.getValue() + "\"");
				else
					System.out.println(dobj.getValue());
			else if (dobj.getType().equals(DataObject.INTEGER_TYPE))
				System.out.println(toString(dobj.getIntArray()));
			else if (dobj.getType().equals(DataObject.LONG_TYPE))
				System.out.println(toString(dobj.getLongArray()));
			else if (dobj.getType().equals(DataObject.DOUBLE_TYPE))
				System.out.println(toString(dobj.getDoubleArray()));
			else if (dobj.getType().equals(DataObject.STRING_TYPE)) {
				System.out.println();
				String[] sa = dobj.getStringArray();
				for (int i = 0; i < sa.length; i++) {
					for (int i2 = 0; i2++ < (level * 4+2);)
						System.out.print(" ");
					System.out.println("\"" + sa[i] + "\"");
				}
			} else
				System.out.println();
		}
	}

	private static String toString(int[] value) {
		StringBuilder sb = new StringBuilder();
		int[] vec = value;
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

	private static String toString(long[] value) {
		StringBuilder sb = new StringBuilder();
		long[] vec = value;
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

	private static String toString(double[] value) {
		StringBuilder sb = new StringBuilder();
		double[] vec = value;
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

	public int size() {
		return objList.size();
	}

}
