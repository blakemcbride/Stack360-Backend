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

package com.arahant.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FixedSizeFileReader {

	
	private BufferedReader reader;
	private int nextStart=0;

	public FixedSizeFileReader(String filename) throws FileNotFoundException
	{
		reader=new BufferedReader(new FileReader(filename));
	}

	public void close() {
		try {
			reader.close();
		} catch (IOException ex) {
			Logger.getLogger(FixedSizeFileReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private class Field
	{
		public Field(String name,int start, int end)
		{
			this.name=name;
			this.start=start;
			this.end=end;
		}
		String name;
		int start;
		int end;
	}

	private HashMap<String,Field> fields=new HashMap<String, Field>();

	/*public void addField(String name, int start, int end)
	{
		fields.put(name, new Field(name, start, end));
	}
*/
	public void addField(String name, int start, int len)
	{
		fields.put(name, new Field(name, start-1, start-1+len-1));
		nextStart=start-1+len;
	}


	public void addField(String name, int len)
	{
		fields.put(name, new Field(name, nextStart, nextStart+len-1));
		nextStart+=len;
	}


	public void printCurrentLine()
	{
		System.out.println(currentLine);
	}

	public String getField(String name)
	{
		try
		{
			Field f=fields.get(name);
			if (f==null)
				return "";
			return currentLine.substring(f.start, f.end+1).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public int getInt(String name)
	{
		try
		{
			Field f=fields.get(name);
			if (f==null)
				return 0;
			return Integer.parseInt(currentLine.substring(f.start, f.end+1).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public double getDouble(String name)
	{
		try
		{
			Field f=fields.get(name);
			if (f==null)
				return 0;
			return Double.parseDouble(currentLine.substring(f.start, f.end+1).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	private String currentLine;

	public void skipChars(int count)
	{
	/*	for (int loop=0;loop<count;loop++)
			try {
				reader.read();
			} catch (IOException ex) {
				Logger.getLogger(FixedSizeFileReader.class.getName()).log(Level.SEVERE, null, ex);
			}
	 */
	}
	public boolean nextLine()
	{
		try {
			currentLine = reader.readLine();
			if (currentLine == null) {
				return false;
			}
			currentLine=currentLine.trim();
		//	System.out.println(currentLine);
		} catch (IOException ex) {
			return false;
		}
		return true;
	}
}
