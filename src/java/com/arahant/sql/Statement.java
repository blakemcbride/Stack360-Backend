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
 * Statement.java
 *
 * Created on October 28, 2007, 9:11 PM
 *
 */

package com.arahant.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.arahant.utils.Utils.*;

/**
 *
 * @author Blake McBride
 */

public class Statement {
    private Database db;
    private Connection conn;
	private boolean autoCommit;
    private java.sql.Statement stmt;
    private java.sql.PreparedStatement pstmt;
    private boolean isSelect;
    private ResultSet rset;
    private String pstr;
    private int numInBatch;
    private int maxBatchSize;
    private HashMap<String, Integer> fieldMap;
    
    /** Creates a new instance of Statement */
    public Statement(Database dbv) {
        db = dbv;
        conn = db.getConnection();
		try {
			autoCommit = conn.getAutoCommit();
		} catch (SQLException ex) {
			autoCommit = false;
		}
        stmt = null;
        pstmt = null;
        rset = null;
        isSelect = false;
        pstr = null;
        numInBatch = 0;
        maxBatchSize = 1000;
    }
    
    
    public void close() {
        resetStatement();
        try {
            if (stmt != null)
                stmt.close();
            if (pstmt != null)
                pstmt.close();
//            conn.commit();
        } catch (SQLException ex) {
            setErrorString(getStackTrace(ex, "Error closing a Statement"));
        }
        db = null;
        stmt = null;
    }

	/**
	 * Execute an SQL script with special abilities with PostgreSQL dump format.
	 *
	 * @param file the file name of the SQL script to run
	 * @param type
	 * <ul>
	 * <li>0=execute the entire script
	 * <li>1=execute th create statements only
	 * <li>2=execute the insert statements only
	 * <li>3=execute th constraints only
	 * </ul>
	 * @return true if processed okay, false if error
	 * <p>
	 * Note that these options are only designed to work with PostgreSQL dumps.
	 */
    public boolean executeSQLScript(String file, int type) {
		boolean processCmd = type <= 1;
        makeStatement();
        BufferedReader fp;
        InputStream is = openFromJar(file);
        if (is == null)
            return false;
        fp = new BufferedReader(new InputStreamReader(is));
                
        int lineno = 0;
        boolean autoCmt = false;
        try {
            autoCmt = conn.getAutoCommit();
            String cmd = "";
            String line;
            conn.setAutoCommit(true);
            
            while ((line=fp.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.length() == 0  ||  line.startsWith("--"))
                    continue;
                if (cmd.length() != 0)
                    cmd += " " + line;
                else
                    cmd += line;
                if (cmd.charAt(cmd.length()-1) == ';') {
                    if (cmd.startsWith("SET client_encoding")  ||  cmd.startsWith("CREATE PROCEDURAL LANGUAGE plpgsql") || cmd.startsWith("CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql")) {
                        cmd = "";
                        continue;
                    }
					if (type == 1  &&  (cmd.startsWith("ALTER TABLE")  &&  cmd.contains("ADD CONSTRAINT")  ||  cmd.startsWith("INSERT INTO ")))
						break;
					if (type == 2)
						if (cmd.startsWith("INSERT INTO "))
							processCmd = true;
						else if (cmd.startsWith("ALTER TABLE")  &&  cmd.contains("ADD CONSTRAINT"))
							break;
					if (type == 3  &&  processCmd == false  &&  cmd.startsWith("ALTER TABLE")  &&  cmd.contains("ADD CONSTRAINT"))
						processCmd = true;
					if (processCmd) {
						cmd = cmd.substring(0, cmd.length()-1);
						stmt.executeUpdate(cmd);
					}
                    cmd = "";
                }
            }
            fp.close();
//            conn.commit();
            conn.setAutoCommit(autoCmt);
            return true;
        } catch (Throwable ex) {
            try {
                conn.setAutoCommit(autoCmt);
            } catch (SQLException e) {
            }
            setErrorString(getStackTrace(ex, "Error reading file " + file + " at line " + lineno));
            return false;
        }      
    }

	public boolean executeSQLScript(String file) {
		return executeSQLScript(file, 0);
	}
    
    public boolean execute(String str) {
        makeStatement();
        str = str.trim();
        if (str.length() > 6  &&  str.substring(0, 6).equalsIgnoreCase("select")) {
            try {
                rset = stmt.executeQuery(str);
            } catch (SQLException ex) {
                setErrorString(getStackTrace(ex, "Error executing " + str));
                return false;
            }
            isSelect = true;
        } else {
            isSelect = false;
            try {
                stmt.executeUpdate(str);
				if (!autoCommit)
					conn.commit();
            } catch (SQLException ex) {
                setErrorString(getStackTrace(ex, "Error executing " + str));
                return false;
            }
        }
        return true;
    }
    
    public boolean executeRead(String str) {
        if (execute(str) == false)
            return false;
        return next();
    }
    
    public boolean executeRead() {
        if (execute() == false)
            return false;
        return next();
    }
    
    private String addValuesToInsert(String cmd) {
        StringTokenizer st = new StringTokenizer(cmd, "(", true);        
        StringBuilder cmd2 = new StringBuilder();
        StringBuilder values = new StringBuilder(" VALUES (");
        
        String token = st.nextToken("(");  //  upto the (
        cmd2.append(token);
        
        token = st.nextToken("(");    //  the (
        cmd2.append(token);
        
        while (true) {
            String col = st.nextToken(",)").trim();    //  a column name            
            cmd2.append(col);
            values.append(":").append(col);
            
            token = st.nextToken(",)").trim();   //  , or )
            if (token.equals(",")) {
                cmd2.append(", ");
                values.append(", ");
            } else {
                cmd2.append(token);
                values.append(")");
                break;
            }           
        }
        
        boolean foundValues = false;
        // get next token past any space
        while (true) {
            try {
                token = st.nextToken(" (");
            } catch (Exception exception) {
                break;
            }
            if (token.equals(" "))
                continue;
            //  assume token == "values"
            cmd2.append(" VALUES");
            foundValues = true;
			break;
        }
        
        if (foundValues) {
            //  just copy the rest of the string
            while (true) {
                try {
                    token = st.nextToken(";");
                } catch (Exception exception) {
                    break;
                }
                cmd2.append(token);       
            }
        } else {
            // make up the VALUES clause with variables
            cmd2.append(values);
        }
        
        return cmd2.toString();
    }
    
    private String parseCommand(String cmd) {
        if (cmd.substring(0, 7).equalsIgnoreCase("insert "))
            cmd = addValuesToInsert(cmd);
        
        fieldMap = null;
        StringTokenizer st = new StringTokenizer(cmd, ":'", true);
        boolean inQuote = false;
        int n = 1;
        String token;
        StringBuilder cmd2 = new StringBuilder();
        while (true) {
            try {
                token = st.nextToken(":'");
            } catch (Exception exception) {
                break;
            }
            if (inQuote) {
                if (token.equals("'"))
                    inQuote = false;
                cmd2.append(token);
                continue;
            }
            if (token.equals("'")) {
                inQuote = true;
                cmd2.append(token);
                continue;
            }
            if (!token.equals(":")) {
                cmd2.append(token);
                continue;
            }
            //  found valid : parameter
            token = st.nextToken(" \"%&'()*+,-./:;<=>?|");  // don't catch exception because there must be a variable name here
            if (fieldMap == null)
                    fieldMap = new HashMap<String, Integer>(32);
            fieldMap.put(token, n++);
            cmd2.append("?");
        }
        return cmd2.toString();
    }
    
    /**
     * This method initializes a Statement object to a prepared statement.  Enhancements to typical prepared statements are as follows.
     * <br><br>
     * :variable syntax can be used in place of ? parameters.  Parameters can then be referred to by name (without the :) in addition to index positions.
     * <br><br>
     * Insert statements no longer need the VALUES clause.  One will be built automatically containing :names with the same as the column names.
     * 
     * @param cmd the SQL command
     * @return boolean - true if success
     */
    
    public boolean prepare(String cmd) {
        if (pstmt != null  &&  pstr != null  &&  pstr.equals(cmd))
            return true;
        resetStatement();
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                return false;
            }
            pstmt = null;
            pstr = null;
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                return false;
            }
            stmt = null;
        }
        try {
            pstmt = conn.prepareStatement(parseCommand(cmd));
            pstr = cmd;
			isSelect = cmd.length() > 6  &&  cmd.substring(0, 6).equalsIgnoreCase("select");
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    public boolean next() {
        try {
            return rset.next();
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return false;
    }
    
    public int getInt(String fld) {
        try {
            return rset.getInt(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
	/**
	 * 
	 * @param fldno starting at 1
	 * @return 
	 */
    public int getInt(int fldno) {
        try {
            return rset.getInt(fldno);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
    public long getLong(String fld) {
        try {
            return rset.getLong(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public long getLong(int fld) {
        try {
            return rset.getLong(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
    public float getFloat(String fld) {
        try {
            return rset.getFloat(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public float getFloat(int fld) {
        try {
            return rset.getFloat(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
    public double getDouble(String fld) {
        try {
            return rset.getDouble(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public double getDouble(int fld) {
        try {
            return rset.getDouble(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return 0;
    }
    
    public String getString(String fld) {
        try {
            return rset.getString(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return "";
    }

	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public String getString(int fld) {
        try {
            return rset.getString(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return "";
    }

	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public Date getDate(int fld) {
        try {
            return rset.getDate(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

    public Date getDate(String fld) {
        try {
            return rset.getDate(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public Time getTime(int fld) {
        try {
            return rset.getTime(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

    public Time getTime(String fld) {
        try {
            return rset.getTime(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public Timestamp getTimestamp(int fld) {
        try {
            return rset.getTimestamp(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

    public Timestamp getTimestamp(String fld) {
        try {
            return rset.getTimestamp(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

	/**
	 * 
	 * @param fld starting at 1
	 * @return 
	 */
    public byte [] getBinary(int fld) {
        try {
            return rset.getBytes(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

    public byte [] getBinary(String fld) {
        try {
            return rset.getBytes(fld);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
        return null;
    }

    public void setInt(String fld, int val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setInt(\"" + fld + "\", ?): field not found.");
        setInt(idx, val);
    }
    
	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setInt(int idx, int val) {
        try {
            pstmt.setInt(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }
    
    public void setLong(String fld, long val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setLong(\"" + fld + "\", ?): field not found.");
        setLong(idx, val);
    }
    
	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setLong(int idx, long val) {
        try {
            pstmt.setLong(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }
    
    public void setFloat(String fld, float val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setFloat(\"" + fld + "\", ?): field not found.");
        setFloat(idx, val);
    }
    
	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setFloat(int idx, float val) {
        try {
            pstmt.setFloat(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }
    
    public void setDouble(String fld, double val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setDouble(\"" + fld + "\", ?): field not found.");
        setDouble(idx, val);
    }
    
	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setDouble(int idx, double val) {
        try {
            pstmt.setDouble(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }
    
    public void setString(String fld, String val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setString(\"" + fld + "\", ?): field not found.");
        setString(idx, val);
    }
    
	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setString(int idx, String val) {
        try {
            pstmt.setString(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }

    public void setDate(String fld, Date val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setString(\"" + fld + "\", ?): field not found.");
        setDate(idx, val);
    }

	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setDate(int idx, Date val) {
        try {
            pstmt.setDate(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }

    public void setTime(String fld, Time val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setString(\"" + fld + "\", ?): field not found.");
        setTime(idx, val);
    }

	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setTime(int idx, Time val) {
        try {
            pstmt.setTime(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }

    public void setTimestamp(String fld, Timestamp val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setString(\"" + fld + "\", ?): field not found.");
        setTimestamp(idx, val);
    }

	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setTimestamp(int idx, Timestamp val) {
        try {
            pstmt.setTimestamp(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }

    public void setBinary(String fld, byte [] val) {
        Integer idx = 0;
        if (fieldMap == null  ||  null == (idx=fieldMap.get(fld)))
            throw new Error("Statement::setString(\"" + fld + "\", ?): field not found.");
        setBinary(idx, val);
    }

	/**
	 * 
	 * @param idx column number, starting at 1
	 * @param val 
	 */
    public void setBinary(int idx, byte [] val) {
        try {
            pstmt.setBytes(idx, val);
        } catch (SQLException ex) {
            exitDisplayError(getStackTrace(ex));
        }
    }

	public boolean execute() {
		if (isSelect)
			try {
				rset = pstmt.executeQuery();
				return true;

			} catch (SQLException ex1) {
				setErrorString(getStackTrace(ex1));
				return false;
			}
		try {
			if (numInBatch > 0) {
				pstmt.executeBatch();
				numInBatch = 0;
			} else
				pstmt.execute();
		} catch (SQLException ex) {
			setErrorString(getStackTrace(ex));
			return false;
		}
		return true;
	}
    
    public boolean addBatch() {
        try {
            pstmt.addBatch();
        } catch (SQLException ex) {
            setErrorString(getStackTrace(ex));
            return false;
        }
        numInBatch++;
        if (numInBatch >= maxBatchSize)
            execute();
        return true;
    }
    
    private boolean makeStatement() {
        resetStatement();
        if (stmt != null)
            return true;
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                return false;
            }
            pstmt = null;
        }
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    private void resetStatement() {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException ex) {
            }
            rset = null;
        }
        if (numInBatch > 0) {
            try {
                pstmt.executeBatch();
				if (!autoCommit)
					conn.commit();
            } catch (SQLException ex) {
                exitDisplayError(getStackTrace(ex));
            }
            numInBatch = 0;
        }
        isSelect = false;
        fieldMap = null;
    }

	public ResultSet getResultSet() {
		return rset;
	}
			
}
