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
 * Database.java
 *
 * Created on October 28, 2007, 9:09 PM
 */

package com.arahant.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.arahant.utils.Utils.*;

/**
 *
 * @author Blake McBride
 */
public class Database {
   private Connection db;
   private Statement stmt;
    
    /** Creates a new instance of Database */
    public Database(Connection dbv) {
        db = dbv;
    }
    
    public static Database OpenDatabase(String propFile) {
        Connection db;
        Properties props = new Properties();
        
        InputStream fi = openFromJar(propFile);
        if (fi == null) {
            setErrorString("Error obtaining connection proporties from file \"" + propFile + "\"");
            return null;
        }
        try {
            props.load(fi);
            fi.close();
        } catch (Throwable ex) {
            setErrorString(getStackTrace(ex, "Error obtaining connection proporties from file \"" + propFile + "\""));
            return null;
        }
        
        try {
            Class.forName((String)props.get("driver"));
        } catch (Throwable e) {
            setErrorString(getStackTrace(e, "Unable to load JDBC driver " + (String)props.get("driver")));
            return null;
        }
        
        try {
            db = DriverManager.getConnection((String) props.get("url"), props);
//            db.setAutoCommit(false);  // so we can use cursors and not rereive the entire result set at once
        } catch (Throwable e) {
            setErrorString(getStackTrace(e, "Error connecting to the database " + (String) props.get("url")));
            return null;
        }        
        return new Database(db);       
    }
    
    public static Database OpenDatabase(String driver, String url, String user, String pw) {
        Connection db;
            
        try {
            Class.forName(driver);
        } catch (Throwable e) {
            setErrorString(getStackTrace(e, "Unable to load JDBC driver " + driver));
            return null;
        }
        
        try {
            db = DriverManager.getConnection(url, user, pw);
//            db.setAutoCommit(false);  // so we can use cursors and not rereive the entire result set at once
        } catch (Throwable e) {
            setErrorString(getStackTrace(e, "Error connecting to the database " + url + " as " + user));
            return null;
        }        
        return new Database(db);       
    }
    
    public void close() {
        clearErrorString();
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        try {
//            db.commit();
            db.close();
        } catch (SQLException ex) {
            setErrorString(getStackTrace(ex, "Error closing a database"));
        }
        db = null;
    }

    public void release() {
        clearErrorString();
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        try {
            db.commit();
//            db.close();
        } catch (SQLException ex) {
            setErrorString(getStackTrace(ex, "Error closing a database"));
        }
        db = null;
    }
    
    public Connection getConnection() {
        return db;
    }
    
    public Statement createStatement() {
        return new Statement(this);
    }

    public boolean executeSQLScript(String file) {
        if (stmt == null)
            stmt = this.createStatement();
        if (stmt == null)
            return false;
        return stmt.executeSQLScript(file);
    }

	/**
	 * Execute an SQL script with special abilities with PostgreSQL dump format.
	 *
	 * @param file the file name of the SQL script to run
	 * @param type
	 * <ul>
	 * <li>0=execute the entire script
	 * <li>1=execute the create statements only
	 * <li>2=execute the insert statements only
	 * <li>3=execute the constraints only
	 * </ul>
	 * @return true if processed okay, false if error
	 * <p>
	 * Note that these options are only designed to work with PostgreSQL dumps.
	 */
    public boolean executeSQLScript(String file, int type) {
        if (stmt == null)
            stmt = this.createStatement();
        if (stmt == null)
            return false;
        return stmt.executeSQLScript(file, type);
    }
    
    public boolean execute(String str) {
        if (stmt == null)
            stmt = this.createStatement();
        if (stmt == null)
            return false;
        return stmt.execute(str);
    }
    
    public int getNextSequence(int dbid, String table, String field) {
        int r=0;
        try {
            java.sql.Statement tstmt = db.createStatement();
            tstmt.setFetchSize(1);
            r = 0;
            String buf = String.format("select %s from %s where %s < '%05d-0000000000' and %s >= '%05d-0000000000' order by %s DESC", field, table, field, dbid+1, field, dbid, field);
            ResultSet rec = tstmt.executeQuery(buf);
            if (rec.next()) {
                String fld = rec.getString(field);
                r = Integer.parseInt(fld.substring(6)) + 1;
            }
            rec.close();
            tstmt.close();
        } catch (Throwable ex) {
            exitDisplayError(getStackTrace(ex, "Error on getNextSequence()"));
        }
        return r;
    }

}
