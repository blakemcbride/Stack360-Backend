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


package com.arahant.genericImport;

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Blake McBride
 */
 public abstract class GenericImport {
	
	private static final ArahantLogger logger = new ArahantLogger(GenericImport.class);
	/**
	 * add new records, existing records are ignored and not updated
	 */
	public static final int APPEND_MODE = 1;
	/**
	 * add new records and update existing ones
	 */
	public static final int UPDATE_MODE = 2;
	/**
	 * add new records, update existing records, and inactivate records not in import file
	 */
	public static final int FULL_MODE = 3;
	
	public interface EnumInfo {
		int index();
		int size();
	}
		
	/**  This is the actual info kept regarding each column
	 */
	protected static class ColumnInfo {
		int col;			// internal column number
		public int ecolnum;		// the column number in the actual import file
		String [] names;	// the list of acceptible column title names the user could use for this column
		
		ColumnInfo(int col, String ... names) {
			this.col = col;
			this.names = names;
			ecolnum = -1;  //  we don't know their column number yet
		}
		
		public boolean equals(EnumInfo ecol) {
			return col == ecol.index();
		}
	}
		
	private String importFileName;
	protected final DelimitedFileReader df;
	protected int mode;
	private PrintWriter errorFile;
	private int errorCount = 0;
	/**
	 * List of internal columns.  All columns in internal order.
	 */
	private ArrayList<ColumnInfo> icols;
	/**
	 * List of external columns.  Only actually existing columns in the order they appear in the import file.
	 */
	protected ColumnInfo [] ecols;
	protected HibernateSessionUtil hsu;
	protected int rowsRead;
	protected int recordsAdded;
	protected int recordsUpdated;
	protected int recordsDeleted;
	private String typeOfImport;

	
	abstract protected void initColumns();
	
	private void initColumnsInternal() {
		icols = new ArrayList<ColumnInfo>();
		initColumns();
	}
	
	@SuppressWarnings("unchecked")
	protected void addCol(EnumInfo col, String ... names) {
		int idx = col.index();
		int sz = icols.size();
		
		for ( ; sz < idx ; sz++)
			icols.add(null);
		if (idx == sz)
			icols.add(new ColumnInfo(idx, names));
		else if (idx < sz)
			icols.set(idx, new ColumnInfo(idx, names));
	}
	
	protected void colRequired(EnumInfo col) {
		int i = col.index();
		ColumnInfo v = icols.get(i);
		if (icols.get(col.index()).ecolnum == -1) {
			errorFile.println("import file is missing required column " + icols.get(col.index()).names[0]);
			errorCount++;
		}
	}
	
	protected boolean colExists(EnumInfo col) {
		return icols.get(col.index()).ecolnum > -1;
	}
	
	protected void addError(String err) {
			if (errorFile != null)
				errorFile.println(err);
			errorCount++;					
	}
	
	protected ColumnInfo getColumnInfo(EnumInfo col) {
		return icols.get(col.index());
	}
		
	protected boolean columnHasData(EnumInfo col) {
		int i = getColumnInfo(col).ecolnum;
		if (i == -1)
			return false;
		String fld = df.getString(i).trim();
		return fld != null  &&  fld.length() != 0;
	}
	
	protected String getString(EnumInfo col) {
		return df.getString(getColumnInfo(col).ecolnum).trim();
	}
	
	protected String fixNum(String num) {
		return num.replaceAll("\\$", "").replaceAll(",", "");
	}
	
	protected GenericImport(String typeOfImport, String filename, int mode) throws FileNotFoundException {
		importFileName = filename;
		hsu = ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		this.mode = mode;
		df = new DelimitedFileReader(filename, ',', '"');
		this.typeOfImport = typeOfImport;
		initColumnsInternal();
	}
	
	public String parse() {
		File errorFileName;
		try {
			errorFileName = FileSystemUtils.createTempFile(typeOfImport + "ImportErrorLog", ".txt");
		} catch (Exception e) {
			logger.error(e);
			addError(e.getMessage());
			return "";
		}
		try {
			errorFile = new PrintWriter(new BufferedWriter(new FileWriter(errorFileName)));
		} catch (Exception e) {
			addError(e.getMessage());
			errorFile.close();
			logger.error(e);
			return FileSystemUtils.getHTTPPath(errorFileName);
		}
		errorFile.printf(typeOfImport + " import started on %s\n\n", DateUtils.getDateAndTimeFormatted(new Date()));
		errorFile.printf("File: %s\n\n", new File(importFileName).getName());
		try {
			buildExtCols();
			if (errorCount == 0) {
				assureRequiredColumns();
				rowsRead = 0;
				if (errorCount == 0) {
					checkRows();
					rowsRead = recordsAdded = recordsUpdated = recordsDeleted = 0;
					if (errorCount == 0) {
						parseRows();
						if (mode == FULL_MODE  &&  errorCount == 0)
							disableMissingRecords();
					}
				}
			}
		} finally {
			df.close();
			errorFile.printf("\n%d import records read\n", rowsRead);
			errorFile.printf("%d errors encountered\n", errorCount);
			errorFile.printf("%d " + typeOfImport + "s added\n", recordsAdded);
			errorFile.printf("%d " + typeOfImport + "s updated\n", recordsUpdated);
			errorFile.printf("%d " + typeOfImport + "s deactivated\n", recordsDeleted);
			errorFile.close();
		}
		hsu.commitTransaction();
		return FileSystemUtils.getHTTPPath(errorFileName);
	}
	
	private void buildExtCols() {
		try {
			if (!df.nextLine()) {
				addError("import file is missing the expected (first) column title line");
				return;
			}
		} catch (Exception e) {
			addError(e.getMessage());
			return;
		}
		ArrayList<ColumnInfo> ecolArrayList = new ArrayList<ColumnInfo>();
		int numCols = 0;
		int max = df.size();
		for (int fld=0 ; fld < max ; fld++) {
			String name = df.nextString();
            name = name.replaceAll("\\P{Print}", "");  // strip extraneous unicode characters
			for (int i=0 ; i < icols.size() ; i++) {
				ColumnInfo icol = icols.get(i);
				String [] nameArray = icol.names;
				for (int j=0 ; j < nameArray.length ; j++)
					if (equals(nameArray[j], name)) {
						if (icol.ecolnum != -1) {
							addError("import file has duplicate column titles (" + name + ")");
							return;
						}
						icol.ecolnum = fld;
						ecolArrayList.add(icol);
						numCols++;
					}
			}
		}
		if (numCols == 0) {
			addError("import file has no usable / recognizable columns");
			return;
		}
		ecols = ecolArrayList.toArray(new ColumnInfo[0]);  // putting "new ColumnInfo[0]" as an argument works like a cast ?!?
	}
	
	/**
	 * Validate that the expected required columns are present.
	 *
	 */
	abstract protected void assureRequiredColumns();
	
	/**
	 * Check all data lines in import file.
	 * 
	 */
	protected abstract void checkRows();
	
	/**
	 * Parse all data lines in import file.
	 * 
	 */
	abstract protected void parseRows();
	
	abstract protected void disableMissingRecords();
	
	/**
	 * Compare without regard to spaces or case.
	 * 
	 * @param s1
	 * @param s2
	 * @return true if they equal
	 */
	private static boolean equals(String s1, String s2) {
		s1 = s1.replaceAll(" ", "");
		s1 = s1.replaceAll("-", "");
		s2 = s2.replaceAll(" ", "");
		s2 = s2.replaceAll("-", "");
		return s1.equalsIgnoreCase(s2);
	}
	
	protected void flush(int recnum) {
		if (recnum % 20 == 0) {
			hsu.flush();
			hsu.clear();
		}	
	}
	
}
