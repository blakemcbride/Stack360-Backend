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


package com.arahant.utils.dbreset;

import com.arahant.utils.DateUtils;
import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;

//import com.arahant.services.TestBase;
import jess.TestBase;


public class ResetBase {
	protected static void executeFile(String filename,Statement st) throws IOException, SQLException
	{
		 String filecontents="";
		 
		 BufferedReader br=new BufferedReader(new FileReader(filename));
		 
		 while (br.ready())
		 {
			 String line=br.readLine();
			 if (line.trim().startsWith("--"))
				 continue;
			 filecontents+=line;
		 }
		 
		 StringTokenizer stk=new StringTokenizer(filecontents,";");
		 
		 while (stk.hasMoreTokens())
		 {
			 String sql=stk.nextToken();
			 
			 if (sql.startsWith("SET client_encoding") || sql.startsWith("CREATE PROCEDURAL LANGUAGE plpgsql"))
				 continue;
			 
			 if (sql.startsWith("SET standard_conforming_strings") || sql.startsWith("ALTER PROCEDURAL LANGUAGE plpgsql"))
				 continue;
			 
			 if (sql.trim().equals(""))
				 continue;
			 
			 System.out.println(sql);
			 st.execute(sql);
		 }
		 
	}
	

	
	/*
	 * 
	/**
	 * @param con
	 * @throws SQLException
	 * @throws IOException
	 */
	protected static void addForm(Connection con, String formId, String formTypeId, int date, String tifName, String comment) throws SQLException, IOException {
		PreparedStatement psfrm=con.prepareStatement("insert into person_form (person_form_id,form_type_id, form_date, person_id,comments,source,form ) values ('"+formId+"','"+formTypeId+"',"+date+",?,'"+comment+"','',?)");

	//	TestBase tb=new com.arahant.services.TestBase();
	//	tb.setConnection();
		psfrm.setString(1, "00001-0000000011");
	//	tb.dropConnection();
		
		InputStream is=ResetBase.class.getClassLoader().getResourceAsStream(tifName);
		
		int length=0;
		
		int x=is.read();
		
		while (x!=-1)
		{
			length++;
			x=is.read();
		}

		psfrm.setBinaryStream(2, ResetBase.class.getClassLoader().getResourceAsStream(tifName), length);
		 
		psfrm.executeUpdate();
	}
	/*
	public static void main(String args[])
	{
		try
		{
			Properties props=new Properties();
			props.put("user", "postgres");
			props.put("password", "postgres");
			props.put("url", "jdbc:postgresql://192.168.1.6/development");
			props.put("driver", "org.postgresql.Driver");
			
			Class.forName((String)props.get("driver"));
			Connection con = DriverManager.getConnection((String)props.get("url"),props);
			
		
			
			ResetBase.addForm(con,"00000-0000000000","00000-0000000000",20070101,"test1.tif","tif 1");
			ResetBase.addForm(con,"00000-0000000001","00000-0000000000",20071103,"test2.tif","tif 2");
			ResetBase.addForm(con,"00000-0000000002","00000-0000000001",20071009,"test3.tif","tif 3");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	*/
	 
}
