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
import com.arahant.utils.XML;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Blake McBride
 */
public class DataObjectMap implements Iterable<DataObject> {

	private HashMap<String, DataObject> objMap = new HashMap<String, DataObject>();

	public DataObjectMap() {
	}

	public DataObjectMap(List<WsDataObject> wsobjarray) {
		DataObject dobj;
		if (wsobjarray != null)
			for (WsDataObject wsobj : wsobjarray) {
				dobj = new DataObject(wsobj);
				objMap.put(dobj.getName(), dobj);
			}
	}

	public List<WsDataObject> toWsList() {
		List<WsDataObject> wsList = new LinkedList<WsDataObject>();
		for (DataObject obj : this)
			wsList.add(obj.toWsDataObject());
		return wsList;
	}

	public DataObjectMap put(DataObject obj) {
		objMap.put(obj.getName(), obj);
		return this;
	}

	public DataObject get(String name) {
		return objMap.get(name);
	}

	public boolean containsKey(String name) {
		return objMap.containsKey(name);
	}

	public DataObject getPath(String name) {
		String[] path = name.split("/");
		DataObjectMap map = this;
		int i = 0;
		int len = path.length - 1;
		for (; i < len; i++)
			map = getDataObjectMap(path[i]);
		return map.get(path[i]);
	}

	public DataObjectMap put(String name, boolean val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public boolean getBoolean(String name) {
		return (Boolean) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, char val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public char getChar(String name) {
		return (Character) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, String val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public String getString(String name) {
		return (String) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, int val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public int getInt(String name) {
		return (Integer) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, long val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public long getLong(String name) {
		return (Long) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, double val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public double getDouble(String name) {
		return (Double) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, int[] val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public int[] getIntArray(String name) {
		return (int[]) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, long[] val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public long[] getLongArray(String name) {
		return (long[]) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, double[] val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public double[] getDoubleArray(String name) {
		return (double[]) objMap.get(name).getValue();
	}

	public DataObjectMap put(String name, String[] val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public String[] getStringArray(String name) {
		DataObject obj = objMap.get(name);
		return obj == null ? null : (String[]) obj.getValue();
	}

	public DataObjectMap put(String name, DataObjectMap val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public DataObjectMap put(String name, DataObjectList val) {
		objMap.put(name, new DataObject(name, val));
		return this;
	}

	public DataObjectMap getDataObjectMap(String name) {
		DataObject obj = objMap.get(name);
		return obj == null ? null : (DataObjectMap) obj.getValue();
	}

	@Override
	public Iterator<DataObject> iterator() {
		return objMap.values().iterator();
	}

	public void print() {
		this.printObj(0);
	}

	public void printObj(int level) {
		for (DataObject dobj : this) {
			String name = dobj.getName();
			if (name != null  &&  name.equals("_password"))
				continue;
			for (int i = 0; i++ < (level * 4);)
				System.out.print(" ");
			if (name == null)
				System.out.print("-- " + ", " + dobj.getType() + ", " + dobj.getRank());
			else
				System.out.print(name + ", " + dobj.getType() + ", " + dobj.getRank());
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
					for (int i2 = 0; i2++ < (level * 4 + 2);)
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
		return objMap.size();
	}
	
	public static void main(String [] args) {
		DataObjectMap dom = new DataObjectMap();
		
		dom.put("val1", 77);
		dom.put("val2", "ab'<\"cd");
		dom.put("lonStr", "abcdefghijk<\"lmnopqrs\\tuvwxyz");
		String [] sa = new String[2];
		sa[0] = "ab\"'<>&cd";
		sa[1] = "GHJK";
		dom.put("sarray", sa);
		
		DataObjectMap dom2 = new DataObjectMap();
		dom2.put("sub1", 66);
		dom2.put("sub2", 3.141);
		int [] ia = new int[3];
		ia[0] = 66;
		ia[1] = 77;
		ia[2]  = 88;
		dom2.put("ia", ia);
		dom.put("mysub", dom2);
				
		
		dom.print();
		
		Document doc = dom.toXML();
		
		String str = XML.toFormattedString(doc);
		System.out.println(str);
		
		DataObjectMap newMap = new DataObjectMap(doc);
		newMap.print();
	}
	
	public DataObjectMap(Document doc) {
		Node n = doc.getDocumentElement();
		if (!DataObject.OBJECTMAP_TYPE.equals(n.getNodeName()))
			return;
		node(this, n.getFirstChild());
	}
	
	private DataObjectMap node(DataObjectMap map, Node n) {
		while (null != n) {
			String name = n.getNodeName();
			NamedNodeMap atts = n.getAttributes();
			String type = atts.getNamedItem("type").getNodeValue();
			int rank = Integer.parseInt(atts.getNamedItem("rank").getNodeValue());
			Node valueNode = atts.getNamedItem("value");
			String value = valueNode == null ? n.getTextContent() : valueNode.getNodeValue();
			if (rank == 0) {
				if (DataObject.OBJECTMAP_TYPE.equals(type))
					map.put(name, node(new DataObjectMap(), n.getFirstChild()));
				else if (DataObject.INTEGER_TYPE.equals(type))
					map.put(name, Integer.parseInt(value));
				else if (DataObject.STRING_TYPE.equals(type))
					map.put(name, value);
				else if (DataObject.BOOLEAN_TYPE.equals(type))
					map.put(name, "true".equals(value));
				else if (DataObject.CHARACTER_TYPE.equals(type))
					map.put(name, value.charAt(0));
				else if (DataObject.DOUBLE_TYPE.equals(type))
					map.put(name, Double.parseDouble(value));
				else if (DataObject.LONG_TYPE.equals(type))
					map.put(name, Long.parseLong(value));
			} else if (rank == 1) {
				int shape = Integer.parseInt(atts.getNamedItem("shape").getNodeValue());
				String [] svals = null;
				if (!DataObject.STRING_TYPE.equals(type))
					svals = value.split(" ");
				if (DataObject.INTEGER_TYPE.equals(type)) {
					int [] vals = new int[shape];
					for (int i=0 ; i < shape ; i++)
						vals[i] = Integer.parseInt(svals[i]);
					map.put(name, vals);
				} else if (DataObject.DOUBLE_TYPE.equals(type)) {
					double [] vals = new double[shape];
					for (int i=0 ; i < shape ; i++)
						vals[i] = Double.parseDouble(svals[i]);
					map.put(name, vals);
				} else if (DataObject.LONG_TYPE.equals(type)) {
					long [] vals = new long[shape];
					for (int i=0 ; i < shape ; i++)
						vals[i] = Long.parseLong(svals[i]);
					map.put(name, vals);
				} else if (DataObject.STRING_TYPE.equals(type)) {
					String [] vals = new String[shape];
					char [] vec = value.toCharArray();
					for (int i=0, j=0 ; i < shape ; i++) {
						while (j < vec.length  &&  vec[j] != '"')
							j++;
						j++;
						StringBuilder sb = new StringBuilder();
						for ( ; j < vec.length  &&  vec[j] != '"' ; j++)
							if (vec[j] == '\\')
								sb.append(vec[++j]);
							else
								sb.append(vec[j]);
						j++;
						vals[i] = sb.toString();
					}
					map.put(name, vals);
				}
			}
			n = n.getNextSibling();
		}
		return map;
	}
	
	public Document toXML() {
		Document doc = XML.create();
		Element elm = doc.createElement(DataObject.OBJECTMAP_TYPE);
		doc.appendChild(elm);
		for (DataObject dobj : this)
			elm.appendChild(createNode(doc, dobj));
		return doc;
	}
	
	private Element createNode(Document doc, DataObject dobj) {
		Element newElm = doc.createElement(dobj.getName());
		String type = dobj.getType();
		newElm.setAttribute("type", type);
		int rank = dobj.getRank();
		newElm.setAttribute("rank", "" + rank);
		if (rank > 0) {
			int[] shape = dobj.getShape();
			String ss = "";
			for (int i = 0; i < rank; i++)
				if (i == 0)
					ss += shape[i];
				else
					ss += " " + shape[i];
			newElm.setAttribute("shape", ss);
		}
		if (type.equals(DataObject.STRING_TYPE))
			if (rank > 0)
				newElm.setTextContent(dobj.toString());
			else {
				String sval = (String) dobj.getValue();
				if (sval.length() <= DataObject.MaxStringSize)
					newElm.setAttribute("value", sval);
				else
					newElm.setTextContent((String) dobj.getValue());
			}
		else if (type.equals(DataObject.OBJECTMAP_TYPE))
			for (DataObject subobj : (DataObjectMap) dobj.getValue())
				newElm.appendChild(createNode(doc, subobj));
		else if (rank > 0)
			newElm.setTextContent(dobj.toString());
		else
			newElm.setAttribute("value", dobj.toString());
		return newElm;
	}

}
