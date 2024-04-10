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


package com.arahant.imports.census;

import com.arahant.beans.Person;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileReader;
import org.kissweb.DelimitedFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class DependentImportValidator {

	public String validate(String fileName, boolean debug) throws Exception {
		File errorLog = new File(FileSystemUtils.getWorkingDirectory(), "Dependent Import Error Log " + DateUtils.now() + (debug ? " DEBUG.csv" : ".csv"));
		DelimitedFileWriter dfw = new DelimitedFileWriter(errorLog.getAbsolutePath(), false);

		try {
			DelimitedFileReader dfr = new DelimitedFileReader(fileName);
			Set<String> uniqueSSN;
			String logLine;
			int rowCounter = 0;
			boolean sampleLine = false;

			uniqueSSN = (Set) ArahantSession.getHSU().createCriteria(Person.class).selectFields(Person.SSN).set();

			dfr.nextLine();  //Reads column headers
			++rowCounter;

			while (dfr.nextLine()) {  //Checks for sample line
				if (dfr.getString(1).trim().equals("999999999") && !sampleLine) {
					sampleLine = true;
					++rowCounter;
					continue;
				}
				++rowCounter;

				//Checks for valid unique SSN
				if (dfr.getString(3).trim().equals(""))
					if (debug)
						logLine = "Row: " + rowCounter + " | SSN for " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "is not a valid SSN." + "(Value: " + dfr.getString(3) + ")";
					else
						logLine = "SSN for " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "is not a valid SSN." + "(Value: " + dfr.getString(3) + ")";
				else
					if (!uniqueSSN.add(dfr.getString(3)) || dfr.getString(0).equals(dfr.getString(3))) {
						if (debug)
							logLine = "Row: " + rowCounter + " | SSN for " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "already exists. Duplicate SSN are not allowed." + "(Value: " + dfr.getString(3) + ")";
						else
							logLine = "SSN: SSN for " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "already exists. Duplicate SSN are not allowed." + "(Value: " + dfr.getString(3) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//Checks for last name
				if (dfr.getString(6).trim().equals("")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | Last Name: " + dfr.getString(0) + " " + "must have a last name." + "(Value: " + dfr.getString(6) + ")";
					else
						logLine = "Last Name: " + dfr.getString(0) + " " + "must have a last name." + "(Value: " + dfr.getString(6) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for first name
				if (dfr.getString(4).trim().equals("")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | First Name: " + dfr.getString(0) + " " + "must have a first name." + "(Value: " + dfr.getString(4) + ")";
					else
						logLine = "First Name: " + dfr.getString(0) + " " + "must have a first name." + "(Value: " + dfr.getString(4) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for a valid relationship code
				if (!dfr.getString(1).trim().toUpperCase().equals("S") && !dfr.getString(1).trim().toUpperCase().equals("C") && !dfr.getString(1).trim().toUpperCase().equals("O")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | Relationship Code: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid relationship code: S, C or O." + "(Value: " + dfr.getString(1) + ")";
					else
						logLine = "Relationship Code: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid relationship code: S, C or O." + "(Value: " + dfr.getString(1) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for valid gender
//				if(!dfr.getString(8).trim().toUpperCase().equals("F") && !dfr.getString(8).trim().toUpperCase().equals("M") && !dfr.getString(8).trim().toUpperCase().equals("U")) {
//					if(debug)
//						logLine = "Row: " + rowCounter + " | Gender: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid gender: F, M or U." + "(Value: " + dfr.getString(8) + ")";
//					else
//						logLine = "Gender: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid gender: F, M or U." + "(Value: " + dfr.getString(8) + ")";
//					dfw.writeField(logLine);
//					dfw.endRecord();
//				}

				//Checks for benefit class
//				if(dfr.getString(17).trim().equals("")) {
//					dfw.writeField("Row: " + rowCounter + " | Benefit Class: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid benefit class." + "(Value: " + dfr.getString(17) + ")");
//					dfw.endRecord();
//				}

				//If medical benefit 1 is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(9).trim().equals(""))
					if (dfr.getString(10).trim().equals("") || DateUtils.getDate(dfr.getString(10)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 1 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid medical benefit 1 start date." + "(Value: " + dfr.getString(10) + ")";
						else
							logLine = "Medical Benefit 1 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid medical benefit 1 start date." + "(Value: " + dfr.getString(10) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//If medical benefit 2 is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(11).trim().equals(""))
					if (dfr.getString(12).trim().equals("") || DateUtils.getDate(dfr.getString(12)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 2 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid medical benefit 2 start date." + "(Value: " + dfr.getString(12) + ")";
						else
							logLine = "Medical Benefit 2 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid medical benefit 2 start date." + "(Value: " + dfr.getString(12) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//If dental benefit is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(13).trim().equals(""))
					if (dfr.getString(14).trim().equals("") || DateUtils.getDate(dfr.getString(14)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Dental Benefit Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid dental benefit start date." + "(Value: " + dfr.getString(14) + ")";
						else
							logLine = "Dental Benefit Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid dental benefit start date." + "(Value: " + dfr.getString(14) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//If vision benefit is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(15).trim().equals(""))
					if (dfr.getString(16).trim().equals("") || DateUtils.getDate(dfr.getString(16)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Vision Benefit Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid vision benefit start date." + "(Value: " + dfr.getString(16) + ")";
						else
							logLine = "Vision Benefit Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid vision benefit start date." + "(Value: " + dfr.getString(16) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//If voluntary benefit 1 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(17).trim().equals("")) {
					if (dfr.getString(18).trim().equals("") || DateUtils.getDate(dfr.getString(18)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 1 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 1 start date." + "(Value: " + dfr.getString(18) + ")";
						else
							logLine = "Voluntary Benefit 1 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 1 start date." + "(Value: " + dfr.getString(18) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(19).trim().equals("") || Float.valueOf(dfr.getString(19).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 1 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 1 amount." + "(Value: " + dfr.getString(19) + ")";
						else
							logLine = "Voluntary Benefit 1 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 1 amount." + "(Value: " + dfr.getString(19) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 2 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(20).trim().equals("")) {
					if (dfr.getString(21).trim().equals("") || DateUtils.getDate(dfr.getString(21)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 2 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 2 start date." + "(Value: " + dfr.getString(21) + ")";
						else
							logLine = "Voluntary Benefit 2 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 2 start date." + "(Value: " + dfr.getString(21) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(22).trim().equals("") || Float.valueOf(dfr.getString(22).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 2 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 2 amount." + "(Value: " + dfr.getString(22) + ")";
						else
							logLine = "Voluntary Benefit 2 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 2 amount." + "(Value: " + dfr.getString(22) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 3 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(23).trim().equals("")) {
					if (dfr.getString(24).trim().equals("") || DateUtils.getDate(dfr.getString(24)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 3 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 3 start date." + "(Value: " + dfr.getString(24) + ")";
						else
							logLine = "Voluntary Benefit 3 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 3 start date." + "(Value: " + dfr.getString(24) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(25).trim().equals("") || Float.valueOf(dfr.getString(25).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 3 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 3 amount." + "(Value: " + dfr.getString(25) + ")";
						else
							logLine = "Voluntary Benefit 3 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 3 amount." + "(Value: " + dfr.getString(25) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 4 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(26).trim().equals("")) {
					if (dfr.getString(27).trim().equals("") || DateUtils.getDate(dfr.getString(27)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 4 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 4 start date." + "(Value: " + dfr.getString(27) + ")";
						else
							logLine = "Voluntary Benefit 4 Start Date: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a valid voluntary benefit 4 start date." + "(Value: " + dfr.getString(27) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(28).trim().equals("") || Float.valueOf(dfr.getString(28).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 4 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 4 amount." + "(Value: " + dfr.getString(28) + ")";
						else
							logLine = "Voluntary Benefit 4 Amount: " + dfr.getString(4).trim() + " " + dfr.getString(6).trim() + " " + "does not have a voluntary benefit 4 amount." + "(Value: " + dfr.getString(28) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(DependentImportValidator.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			dfw.close();
		}

		return errorLog.getName();
	}

	public static void main(String[] args) {
		try {
			new DependentImportValidator().validate("/home/xichen/Desktop/CAS Import/Adapted CAS Dependent Import 20110502.csv", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
