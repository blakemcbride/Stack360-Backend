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


/**
 * 
 */
package com.arahant.services.standard.site.databaseQuery;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSiteDatabaseQueryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class DatabaseQueryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(DatabaseQueryOps.class);
	
	public DatabaseQueryOps() {
		super();
	}
	
    @WebMethod()
	public ExecuteQueryReturn executeQuery(/*@WebParam(name = "in")*/final ExecuteQueryInput in) {

		final ExecuteQueryReturn ret = new ExecuteQueryReturn();

		try
		{
			checkLogin(in);
			if(ArahantSession.getHSU().currentlyArahantUser())
			{
				Statement s = ArahantSession.getConnection().createStatement();
				if(in.getQueryString().contains("select") || in.getQueryString().contains("SELECT"))
				{
					ResultSet res = s.executeQuery(in.getQueryString());

					ResultSetMetaData rsmd = res.getMetaData();
					int numColumns = rsmd.getColumnCount();
					String[] columnNames = new String[numColumns];

					// Get the column names; column indices start from 1
					for (int i = 1; i < numColumns + 1; i++)
					{
						columnNames[i-1] = rsmd.getColumnName(i);
					}
					ret.setColumnNames(columnNames);

					String returnString = "";
					while(res.next())
					{
						boolean outOfColumns = false;
						int count = 1;
						while(!outOfColumns)
						{
							try {
								returnString += res.getObject(count++) + " | ";

							} catch (Exception e) {
								outOfColumns = true;
							}
						}
						returnString += "\n";
					}
					ret.setQueryResults(returnString);
				}
				else
				{
					int count = s.executeUpdate(in.getQueryString());
					ret.setQueryResults(count + " rows affected.");
				}
			}
			else
			{
				ret.setQueryResults("ONLY ARAHANT USER CAN USE THIS SCREEN");
			}

//			if(ArahantSession.getHSU().currentlyArahantUser())
//			{
//				Statement s = ArahantSession.getConnection().createStatement();
//				ResultSet res = s.executeQuery(in.getQueryString());
//
//				String returnString = "";
//				while(res.next())
//				{
//					boolean outOfColumns = false;
//					int count = 1;
//					while(!outOfColumns)
//					{
//						try {
//							returnString += res.getObject(count++) + " | ";
//
//						} catch (Exception e) {
//							outOfColumns = true;
//						}
//					}
//					returnString += "\n";
//				}
//				ret.setQueryResults(returnString);
//			}
//			else
//			{
//				ret.setQueryResults("ONLY ARAHANT USER CAN USE THIS SCREEN");
//			}

//This works but doesn't show Arahant formatted ID's for some reason
//			if(ArahantSession.getHSU().currentlyArahantUser())
//			{
//				ScrollableResults scr = ArahantSession.getHSU().getSession().createSQLQuery(in.getQueryString()).scroll();
//				String returnString = "";
//				while(scr.next())
//				{
//					boolean outOfColumns = false;
//					int count = 0;
//					Object[] objs = scr.get();
//					for(Object o : objs)
//					{
//						if(o != null)
//							returnString += o.toString() + " | ";
//						else
//							returnString += "(null) | ";
//					}
//					returnString += "\n";
//				}
//				ret.setQueryResults(returnString);
//			}
//			else
//			{
//				ret.setQueryResults("ONLY ARAHANT USER CAN USE THIS SCREEN");
//			}

			finishService(ret);
		}
		catch (final Exception e) {
			ret.setQueryResults("Error in query.");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	public static void main(String[] args) {
		ExecuteQueryInput in = new ExecuteQueryInput();
		in.setUser("arahant");
		in.setPassword("password");
		in.setQueryString("select count(*) from person where lname like 'Ad%';");
		ExecuteQueryReturn out = (new DatabaseQueryOps()).executeQuery(in);
		System.out.println(out.getQueryResults());
	}
	
	

}
