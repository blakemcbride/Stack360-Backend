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


package com.arahant.utils.generators.validation;


import java.io.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.*;

import com.arahant.utils.DOMUtils;



public class Generator {
/*
 * TABLE_CAT String => table catalog (may be null) 
TABLE_SCHEM String => table schema (may be null) 
TABLE_NAME String => table name 
COLUMN_NAME String => column name 
DATA_TYPE int => SQL type from java.sql.Types 
TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified 
COLUMN_SIZE int => column size. 
BUFFER_LENGTH is not used. 
DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable. 
NUM_PREC_RADIX int => Radix (typically either 10 or 2) 
NULLABLE int => is NULL allowed. 
columnNoNulls - might not allow NULL values 
columnNullable - definitely allows NULL values 
columnNullableUnknown - nullability unknown 
REMARKS String => comment describing column (may be null) 
COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) 
SQL_DATA_TYPE int => unused 
SQL_DATETIME_SUB int => unused 
CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column 
ORDINAL_POSITION int => index of column in table (starting at 1) 
IS_NULLABLE String => ISO rules are used to determine the nullability for a column. 
YES --- if the parameter can include NULLs 
NO --- if the parameter cannot include NULLs 
empty string --- if the nullability for the parameter is unknown 
SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) 
SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF) 
SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
IS_AUTOINCREMENT String => Indicates whether this column is auto incremented 
YES --- if the column is auto incremented 
NO --- if the column is not auto incremented 
empty string --- if it cannot be determined whether the column is auto incremented parameter is unknown 
 */

	
	Map<String, HashMap>  tables=new HashMap<String, HashMap>();
	
	private static class ColData
	{
		public String name;
		
		public int dataType;
		
		public int columnSize;
		
		public int digits;
		
		public String toString()
		{
			return name;
			
		}
	}
	
	
	static Connection con=null;
	
	static {
		try
		{
		 final Properties props=new Properties();
			
			InputStream hcfg=Generator.class.getClassLoader().getResourceAsStream("hibernate.cfg.xml");
		
			Document doc=DOMUtils.createDocument(hcfg);
		
			String db=DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.url\"]");
			props.put("url",db);
			//System.out.println("Attempting to open database at "+db);
		
			Class.forName(DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.driver_class\"]"));
			
		
			props.put("user",DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.username\"]"));
			props.put("password",DOMUtils.getString(doc, "//property[@name=\"hibernate.connection.password\"]"));

		/*	   
			   //props.load(Generator.class.getClassLoader().getResourceAsStream("Connection.props"));
			   props.put("user", "postgres");
			   props.put("password", "postgres");
			   props.put("url", "jdbc:postgresql://localhost/arahant");
			   props.put("driver", "org.postgresql.Driver");
				
				Class.forName((String)props.get("driver"));
		 * */
		 con = DriverManager.getConnection((String)props.get("url"),props);
		 
		 //Note that I don't have a good way to close this connection yet.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Generator() throws IOException
	{
		   try {
			   
			  		/*		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			    
			    String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
			    "databaseName=integra;user=sa;password=;";
			 con = DriverManager.getConnection(connectionUrl);
			 
			 */
			 final DatabaseMetaData dmd=con.getMetaData();
		
			final ResultSet rs=dmd.getTables("public", null, null, new String[]{"TABLE"});
		
			while (rs.next())
			{
				final String tableName=rs.getString(3);
				final HashMap<String, ColData> colMap=new HashMap<String, ColData>();
				tables.put(tableName, colMap);
				
				//System.out.println("******"+tableName);
				
				final ResultSet cols=dmd.getColumns("public", null, tableName, null);
				while (cols.next())
				{
					final ColData cd=new ColData();
					
					cd.name=cols.getString("COLUMN_NAME");
					cd.columnSize=cols.getInt("COLUMN_SIZE");
					cd.dataType=cols.getInt("DATA_TYPE");
					cd.digits=cols.getInt("DECIMAL_DIGITS");
					
					
					colMap.put(cd.name, cd);
				//	System.out.println(cd.toString());
				}
			}
			
				
		}  catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String generate(final String dir,final String packname,final String origPack) throws Exception {
		StringBuilder ret = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("classStart.as")));

		while (br.ready())
			ret.append(br.readLine()).append("\n");

		br.close();

		ret = new StringBuilder(ret.toString().replaceAll("%1", packname));

		final String fpath = dir + "/Validation.xml";

		try {
			final FileInputStream fis = new FileInputStream(fpath);

			final Document doc = DOMUtils.createDocument(fis);

			final NodeList nl = DOMUtils.getNodes(doc, "validation/method");

			for (int loop = 0; loop < nl.getLength(); loop++)
				ret.append(handleMethod(nl.item(loop)));
		} catch (final FileNotFoundException e) {
			//make a Validation 
			final File directory = new File(dir);
			final File[] f = directory.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					return name.endsWith("Ops.java");
				}

			});
			//System.out.println("found "+f.length);
			//	System.out.println(f[0].getName());
			//   System.out.println("origPack is '"+origPack+"'");
			String pname = origPack.substring(origPack.indexOf("com")).replace('\\', '.').replace('/', '.');
			//     System.out.println("pname is "+pname);
			if (pname.endsWith("."))
				pname = pname.substring(0, pname.length() - 1);
			final String className = pname + "." + f[0].getName().substring(0, f[0].getName().length() - 5);


			final Class c = Class.forName(className);


			final FileWriter fw = new FileWriter(fpath);

			fw.write("<validation>\n");

			final Method[] methods = c.getMethods();

			for (final Method element : methods) {
				if (element.getParameterTypes().length == 0)
					continue;

				final Class inputClass = element.getParameterTypes()[0];

				final Method[] getters = inputClass.getMethods();

				final String methName = element.getName();
				if (methName.equals("wait") || methName.equals("equals"))
					continue;

				fw.write("\t<method name=\"" + element.getName() + "\" >\n");

				for (final Method m : getters) {
					if (m.getName().startsWith("get")) {
						String fieldName = m.getName().substring(3);
						fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

						if (fieldName.equals("class"))
							continue;

						final Class type = m.getReturnType();


						String typ = "";
						if (type.equals(String.class)) {
							typ = "string";
						}
						if (type.equals(Integer.class) || type.equals(Long.class) || type.equals(java.lang.Short.TYPE) || type.equals(java.lang.Integer.TYPE) || type.equals(java.lang.Long.TYPE)) {
							typ = "int";
							if (fieldName.indexOf("Date") != -1)
								typ = "date";
							if (fieldName.indexOf("Time") != -1)
								typ = "time";
						}
						if (type.equals(Float.class) || type.equals(Double.class) || type.equals(java.lang.Float.TYPE) || type.equals(java.lang.Double.TYPE))
							typ = "float";
						if (type.equals(Boolean.class) || type.equals(java.lang.Boolean.TYPE))
							typ = "boolean";


						if (typ.equals(""))
							typ = "array";

						if (fieldName.equals("user"))
							continue;

						if (fieldName.equals("password"))
							continue;

						if (fieldName.indexOf("Phone") != -1 || fieldName.contains("Fax")) {
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"string\" table=\"phone\" column=\"phone_number\" />\n");
							continue;
						}

						if (typ.equals("string") && (fieldName.toUpperCase().contains("FIRSTNAME") || fieldName.toUpperCase().contains("FNAME"))) {
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"string\" table=\"person\" column=\"fname\" />\n");
							continue;
						}
						if (typ.equals("string") && (fieldName.toUpperCase().contains("LASTNAME") || fieldName.toUpperCase().contains("LNAME"))) {
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"string\" table=\"person\" column=\"lname\" />\n");
							continue;
						}
//						typ="float";
						if (type.equals(java.lang.Character.TYPE))
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"string\" min=0 max=1 />\n");
						else if (typ.equals("date"))
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"" + typ + "\" min=\"19000101\" max=\"30000101\" />\n");
						else if (typ.equals("time"))
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"" + typ + "\" min=\"0\" max=\"240000\" />\n");
						else
							fw.write("\t\t<field name=\"" + fieldName + "\" required=\"false\" type=\"" + typ + "\" />\n");

					}
				}
				//	<field name="newPassword" table="prophet_login" column="user_password" required="true" type="string" />


				fw.write("\t</method>\n");

			}

			fw.write("</validation>\n");
			fw.flush();
			fw.close();

			return generate(dir, packname, origPack);

		}
		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("classEnd.as")));

		while (br.ready())
			ret.append(br.readLine()).append("\n");

		br.close();

		return ret.toString();
	}
	
	private String handleMethod(final Node node) throws Exception
	{
		
		String ret="";
		final NodeList nl=DOMUtils.getNodes(node, "./field");
		
		final String methodName=DOMUtils.getAttribute((Element)node, "name");
	
                ret+="\n\t\t\t////////////////////////////////////////////////////////////";
		ret+="\n\t\t\t// "+methodName;
		ret+="\n\t\t\t"+"vmap[\""+methodName+"\"] = new Dictionary();\n";
		
		for (int loop=0;loop<nl.getLength();loop++)
			ret+=handleField((Element)nl.item(loop),methodName);
		
		
		
		
		return ret;
	}


	private String handleField(final Element node, final String methodName) throws Exception {
		// get the column data
		final String tablename = DOMUtils.getAttribute(node, "table");
		final String column = DOMUtils.getAttribute(node, "column");

		ColData cd = null;

		if (tablename != null && tables.get(tablename) != null)
			cd = (ColData) tables.get(tablename).get(column);

		String paramName = DOMUtils.getAttribute(node, "name");

		if (paramName.equals("type")) //we need this because return classes inherit from event
			paramName = "typex";

		final boolean required = DOMUtils.getBoolean(node, "@required");
		final String type = DOMUtils.getAttribute(node, "type");

		StringBuilder ret = new StringBuilder();

		final String map = "(vmap[\"" + methodName + "\"] as Dictionary)[\"" + paramName + "\"]";

		final String vobj = "((" + map + " as ValidationData))";


		ret.append("\t\t\t").append(map).append(" = new ValidationData();\n");

		ret.append("\t\t\t").append(vobj).append(".required = ").append(required).append(";\n");

		String max = "10000000";
		String min = "-12345";
		int digits = 0;


		if (cd != null) {

			if (cd.dataType == 12 || cd.dataType == -1 || cd.dataType == 1) {
				min = "0";

				max = cd.columnSize + "";
			}
			//digits=cd.digits;

		}

		if (type.equals("date")) {
			min = "10000101";
			max = "30000101";
		}

		if (type.equals("time")) {
			min = "0";
			max = "235959999";
		}

		if (type.equals("ssn")) {
			min = "11";
			max = "11";
		}

		final String digitsx = DOMUtils.getAttribute(node, "digits");
		if (digitsx != null && !digitsx.trim().equals(""))
			digits = DOMUtils.getInt(node, "@digits");

		final String maxx = DOMUtils.getAttribute(node, "max");
		if (maxx != null && !maxx.trim().equals(""))
			max = DOMUtils.getAttribute(node, "max");

		final String minx = DOMUtils.getAttribute(node, "min");
		if (minx != null && !minx.trim().equals(""))
			min = DOMUtils.getAttribute(node, "min");

		if (max.equals("-1"))
			max = "4000";

		ret.append("\t\t\t").append(vobj).append(".min = ").append(min).append(";\n");
		ret.append("\t\t\t").append(vobj).append(".max = ").append(max).append(";\n");
		ret.append("\t\t\t").append(vobj).append(".digits = ").append(digits).append(";\n");
		ret.append("\t\t\t").append(vobj).append(".type = \"").append(type).append("\";\n");


		final NodeList nl = DOMUtils.getNodes(node, "value");
		if (nl.getLength() > 0) {
			ret.append("\t\t\t").append(vobj).append(".allowedData = [];\n");

			for (int loop = 0; loop < nl.getLength(); loop++)
				ret.append("\t\t\t").append(vobj).append(".allowedData.add(\"").append(DOMUtils.getAttribute((Element) nl.item(loop), "value")).append("\");\n");
		}

		return ret.toString();
	}


	public static void main (String args[]) 
	{
		try
		{
			final Generator g=new Generator();
			String srcDir="";
			
			if (args.length==0)
                        {
                            args=new String[3];
                            
                            args[0]="./src/java/com/arahant/services/standard/hr/hrTrainingCategory";
                            args[1]="com/arahant/services/standard/hr/hrTrainingCategory";
                            args[2]="com.arahant.services.standard.hr.hrTrainingCategory";
							args[3]="../Frontend_Flex";
                        }
      
			srcDir=args[0];
			
			if (srcDir.toUpperCase().indexOf("AQDEV")!=-1)
				return;
			
		//	System.out.println(srcDir);
			
			String flexDir=args[3];
			
			if (!flexDir.endsWith("/")&&!flexDir.endsWith("\\"))
				flexDir+="/";
			
			String destDir=flexDir;
			
			
			
			final File f=new File(destDir);
			
		//	System.out.println("*********"+f.getAbsolutePath());
			
			destDir+="src/com/arahant/app/screen";
			
				
			destDir+=args[1].substring(args[1].indexOf("services")+"services".length());
			
			String packname=args[2].replaceFirst("services", "app.screen");
                        
                        if (packname.equals("com.arahant.app.screen.main"))
                            packname="com.arahant.app.main";
			
	//		if (packname.equals("main"))
	//			return;
			
			if (packname.endsWith("peachtreeApp"))
				return;
			if (packname.endsWith("quickbooksApp"))
				return;
		
                        if (packname.endsWith("scanner"))
				return;
		
                        if (destDir.indexOf("/main")!=-1)
                            destDir=flexDir+"src/com/arahant/app/main";
			
			destDir+="/service/ServiceValidation.as";
                        
                        
                        System.out.println("srcDir is '"+srcDir+"'");
			
			
			final String x=g.generate(srcDir,packname,srcDir);
		//	System.out.println(x);
			
			System.out.println("Validation for "+packname+" generated in "+destDir+".");
			
			
			final BufferedWriter bw=new BufferedWriter(new FileWriter(destDir));
			bw.write(x);
			bw.flush();
			bw.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
