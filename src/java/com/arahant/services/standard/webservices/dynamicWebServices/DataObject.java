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
import com.arahant.utils.DOMUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.bind.annotation.*;

/**
 *
 */



public class DataObject implements Iterable<DataObject> {

	private String name;
	private String type;
	private String value;
	private HashMap<String,DataObject> objMap=new HashMap<String, DataObject>();
	private ArrayList<DataObject> objList=new ArrayList<DataObject>();

	private Integer rank;
	private String shape;
	private String index;

	public DataObject()
	{

	}

	public DataObject(String name, long x)
	{
		this.name=name;
		this.value=x+"";
		type="Long";
	}
	public DataObject(String name, int x)
	{
		this.name=name;
		this.value=x+"";
		type="Integer";
	}
	public DataObject(String name, short x)
	{
		this.name=name;
		this.value=x+"";
		type="Short";
	}
	public DataObject(String name, double x)
	{
		this.name=name;
		this.value=x+"";
		type="Double";
	}
	public DataObject(String name, float x)
	{
		this.name=name;
		this.value=x+"";
		type="Float";
	}
	public DataObject(String name, String x)
	{
		this.name=name;
		this.value=x;
		type="String";
	}
	public DataObject(String name, char x)
	{
		this.name=name;
		this.value=x+"";
		type="Character";
	}

	public void put(String name,String []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="StringArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (String x : data)
		{
			DataObject d=new DataObject();
			d.value=x;
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	public void put(String name,DataObject []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="ObjectArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (DataObject x : data)
		{
			x.setIndex(count+"");
			array.objList.add(x);
			count++;
		}
	}

	public void put(String name,String [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="StringArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2];
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}


	public void put(String name,long [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="LongArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}

	public void put(String name,int [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="IntegerArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}

	public void put(String name,short [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="ShortArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}

	public void put(String name,double [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="DoubleArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}
	
	public void put(String name,float [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="FloatArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}

	public void put(String name,char [][]data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="CharArray";
		array.rank=1;
		array.shape=data.length+"";
		if (data.length>0)
			array.shape+=","+data[0].length;
		objMap.put(name, array);
		int count=0;
		for (int loop=0;loop<data.length;loop++)
			for (int loop2=0;loop2<data[loop].length;loop2++)
			{
				DataObject d=new DataObject();
				d.value=data[loop][loop2]+"";
				d.setIndex(loop+","+loop2);
				array.objList.add(d);
				count++;
			}
	}


	public void put(String name,long []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="LongArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (long x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	public void put(String name,int []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="IntegerArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (int x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	public void put(String name,short []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="ShortArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (short x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	public void put(String name,double []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="DoubleArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (double x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	public void put(String name,float []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="FloatArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (float x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}


	public void put(String name,char []data)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject array=new DataObject();
		array.name=name;
		array.type="CharArray";
		array.rank=0;
		array.shape=data.length+"";
		objMap.put(name, array);
		int count=0;
		for (char x : data)
		{
			DataObject d=new DataObject();
			d.value=x+"";
			d.setIndex(count+"");
			array.objList.add(d);
			count++;
		}
	}

	@XmlElement(name="data")
	public DataObject[] getDataVals() {
		DataObject[]dataVals=new DataObject[0];
		if (objMap.size()>0)
		{
			dataVals=new DataObject[objMap.size()];
			int count=0;
			for (DataObject o : objMap.values())
				dataVals[count++]=o;
		}
		if (objList.size()>0)
		{
			dataVals=new DataObject[objList.size()];
			int count=0;
			for (DataObject o : objList)
				dataVals[count++]=o;
		}
		return dataVals;
	}

	public void setDataVals(DataObject[] dataVals) {
		for (DataObject o : dataVals)
		{
			if (o.name!=null)
				objMap.put(o.name, o);
			else
				objList.add(o);
		}
	}

	public boolean booleanVal()
	{
		if (value==null)
			return false;
		return Boolean.parseBoolean(value);
	}

	public int intVal()
	{
		if (value==null || "".equals(value))
			return 0;
		return Integer.parseInt(value);
	}


	public short shortVal()
	{
		if (value==null)
			return 0;
		return Short.parseShort(value);
	}


	public long longVal()
	{
		if (value==null)
			return 0;
		return Long.parseLong(value);
	}


	public float floatVal()
	{
		if (value==null)
			return 0;
		return Float.parseFloat(value);
	}
  

	public double doubleVal()
	{
		if (value==null)
			return 0;
		return Double.parseDouble(value);
	}

	public char charVal()
	{
		if (value==null || value.length()==0)
			return 0;
		return value.charAt(0);
	}


	public String[] stringArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			String[] ret=new String[1];
			ret[0]=value;
			return ret;
		}

		String[] ret=new String[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).value;
		return ret;
	}

	public String[][] stringArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		String[][] ret=new String[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.value;
		}
		return ret;
	}

	public long[][] longArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		long[][] ret=new long[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.longVal();
		}
		return ret;
	}

	public short[][] shortArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		short[][] ret=new short[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.shortVal();
		}
		return ret;
	}

	public int[][] intArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		int[][] ret=new int[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.intVal();
		}
		return ret;
	}

	public float[][] floatArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		float[][] ret=new float[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.floatVal();
		}
		return ret;
	}

	public double[][] doubleArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		double[][] ret=new double[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.doubleVal();
		}
		return ret;
	}

	public char[][] charArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		char[][] ret=new char[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.charVal();
		}
		return ret;
	}

	public boolean[][] booleanArray2Val()
	{
		int dim1=Integer.parseInt(shape.substring(0,shape.indexOf(',')));
		int dim2=Integer.parseInt(shape.substring(shape.indexOf(',')+1));
		boolean[][] ret=new boolean[dim1][dim2];
		for (int loop=0;loop<objList.size();loop++)
		{
			DataObject d=objList.get(loop);
			int x=Integer.parseInt(d.index.substring(0,d.index.indexOf(',')));
			int y=Integer.parseInt(d.index.substring(d.index.indexOf(',')+1));
			ret[x][y]=d.booleanVal();
		}
		return ret;
	}


	public long[] longArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			long[] ret=new long[1];
			ret[0]=longVal();
			return ret;
		}

		long[] ret=new long[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).longVal();
		return ret;
	}

	public short[] shortArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			short[] ret=new short[1];
			ret[0]=shortVal();
			return ret;
		}

		short[] ret=new short[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).shortVal();
		return ret;
	}


	public int[] intArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			int[] ret=new int[1];
			ret[0]=intVal();
			return ret;
		}

		int[] ret=new int[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).intVal();
		return ret;
	}

	public float[] floatArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			float[] ret=new float[1];
			ret[0]=floatVal();
			return ret;
		}


		float[] ret=new float[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).floatVal();
		return ret;
	}

	public double[] doubleArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			double[] ret=new double[1];
			ret[0]=doubleVal();
			return ret;
		}


		double[] ret=new double[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).doubleVal();
		return ret;
	}

	public char[] charArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			char[] ret=new char[1];
			ret[0]=charVal();
			return ret;
		}

		char[] ret=new char[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).value.charAt(0);
		return ret;
	}

	public boolean[] booleanArrayVal()
	{
		if (objList.size()==0 && type.indexOf("Array")==-1)
		{
			//Flash will send array length 1 up as just 1 element not in array
			boolean[] ret=new boolean[1];
			ret[0]=booleanVal();
			return ret;
		}

		boolean[] ret=new boolean[objList.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=objList.get(loop).booleanVal();
		return ret;
	}



	public void put(String name, Number x)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject d=new DataObject();
		d.name=name;
		d.value=x.toString();

		if (x instanceof Float)
			d.type="Float";
		if (x instanceof Double)
			d.type="Double";
		if (x instanceof Integer)
			d.type="Integer";
		if (x instanceof Long)
			d.type="Long";
		if (x instanceof Short)
			d.type="Short";


		objMap.put(name, d);
	}

	public void put(String name, String x)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject d=new DataObject();
		d.name=name;
		d.value=x;
		d.type="String";
		objMap.put(name, d);
	}

	public void put(String name, char x)
	{
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject d=new DataObject();
		d.name=name;
		d.value=x+"";
		d.type="Character";
		objMap.put(name, d);
	}

	public DataObject get(String name)
	{
		//modifying this get:

		//what is happening is that when put(string,DataObject[])
		//the DataObject was not being put into the objMap
		//what we'll do is check if we have it in the objMap
		//if not, then check the objList
		//make sure setName is call, getObject will use this name
		DataObject dao = new DataObject();
		if (objMap.get(name) != null){
			return objMap.get(name);
		} else {
			//else get it from the list
			for (DataObject o : objList){
				if (o.getName().equalsIgnoreCase(name)){
					return o;
				}
			}
		}
		return null;
	}

	public String getString(String name)
	{
		if (objMap.get(name)==null)
			return "";
		return objMap.get(name).value;
	}

	public char getChar(String name)
	{
		return objMap.get(name).value.charAt(0);
	}

	public long getLong(String name)
	{
		return objMap.get(name).longVal();
	}

	public int getInt(String name)
	{
		if (objMap.get(name)==null)
			return 0;
		return objMap.get(name).intVal();
	}

	public String[][] getStringArray2(String name)
	{
		return objMap.get(name).stringArray2Val();
	}

	public double[][] getDoubleArray2(String name)
	{
		return objMap.get(name).doubleArray2Val();
	}

	public float[][] getFloatArray2(String name)
	{
		return objMap.get(name).floatArray2Val();
	}

	public short[][] getShortArray2(String name)
	{
		return objMap.get(name).shortArray2Val();
	}

	public int[][] getIntArray2(String name)
	{
		return objMap.get(name).intArray2Val();
	}

	public long[][] getLongArray2(String name)
	{
		return objMap.get(name).longArray2Val();
	}

	public char[][] getCharArray2(String name)
	{
		return objMap.get(name).charArray2Val();
	}

	public boolean[][] getBooleanArray2(String name)
	{
		return objMap.get(name).booleanArray2Val();
	}

	public String[] getStringArray(String name)
	{
		if (objMap.size() > 0){
			return objMap.get(name).stringArrayVal();
		} else {
			String[] data = new String[objList.size()];
			int i = 0;
			for (DataObject o : objList){
				data[i] = o.getStringValue();
			}
			return data;
		}
		
	}

	public int[] getIntArray(String name)
	{
		return objMap.get(name).intArrayVal();
	}

	public short[] getShortArray(String name)
	{
		return objMap.get(name).shortArrayVal();
	}

	public long[] getLongArray(String name)
	{
		return objMap.get(name).longArrayVal();
	}

	public float[] getFloatArray(String name)
	{
		return objMap.get(name).floatArrayVal();
	}

	public double[] getDoubleArray(String name)
	{
		return objMap.get(name).doubleArrayVal();
	}

	public boolean[] getBooleanArray(String name)
	{
		return objMap.get(name).booleanArrayVal();
	}

	public char[] getCharArray(String name)
	{
		return objMap.get(name).charArrayVal();
	}

	public short getShort(String name)
	{
		return objMap.get(name).shortVal();
	}

	public float getFloat(String name)
	{
		return objMap.get(name).floatVal();
	}

	public double getDouble(String name)
	{
		return objMap.get(name).doubleVal();
	}

	public int size()
	{
		return objMap.size();
	}

	public void clear()
	{
		objMap.clear();
	}

	public void remove(String name)
	{
		if (!objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" not found.");
		objMap.remove(name);
	}

	@Override
	public Iterator<DataObject> iterator() {
		return objMap.values().iterator();
	}


	
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	/**
	 * Don't call this method, it's for the web service layer only
	 * @param value
	 */
	@XmlAttribute(name="value")
	public String getAttrValue() {

		if (value==null)
			return null;

		return DOMUtils.escapeText(value);
	}

	/**
	 * Don't call this method, it's for the web service layer only
	 * @param value
	 */
	public void setAttrValue(String value) {
		this.value = DOMUtils.unescapeText(value);
	}



	/**
	 * Don't call this method, it's for the web service layer only
	 * @param value
	 */
	public String getStringValue() {
		if (value!=null)
			return DOMUtils.escapeText(value);
		return null;
	}

	/**
	 * Don't call this method, it's for the web service layer only
	 * @param value
	 */
	public void setStringValue(String value) {
		this.value = DOMUtils.unescapeText(value);
	}


	

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 

	@XmlAttribute(name="rank")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}  

	@XmlAttribute(name="shape")
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@XmlAttribute(name="index")
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void put(String name, DataObject obj) {
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		obj.name=name;
		obj.type="Object";
		objMap.put(name, obj);
	}

	public void put(String name, boolean x) {
		if (objMap.containsKey(name))
			throw new ArahantException("Data element "+name+" already exists.");
		DataObject d=new DataObject();
		d.name=name;
		d.value=x?"true":"false";
		d.type="Boolean";
		objMap.put(name, d);
	}

	public boolean getBoolean(String name) {
		if (objMap.get(name)==null)
			return false;
		return objMap.get(name).booleanVal();
	}

}
