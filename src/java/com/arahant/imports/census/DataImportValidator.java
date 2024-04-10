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
public class DataImportValidator {

	public String validate(String fileName, boolean debug) throws Exception {
		File errorLog = new File(FileSystemUtils.getWorkingDirectory(), "Data Import Error Log " + DateUtils.now() + (debug ? " DEBUG.csv" : ".csv"));
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
				if (dfr.getString(1).trim().equals(""))
					if (debug)
						logLine = "Row: " + rowCounter + " | SSN for " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "is not a vlid SSN." + "(Value: " + dfr.getString(1) + ")";
					else
						logLine = "SSN for " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "is not a valid SSN." + "(Value: " + dfr.getString(1) + ")";
				else
					if (!uniqueSSN.add(dfr.getString(1))) {
						if (debug)
							logLine = "Row: " + rowCounter + " | SSN for " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "already exists. Duplicate SSN are not allowed." + "(Value: " + dfr.getString(1) + ")";
						else
							logLine = "SSN: SSN for " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "already exists. Duplicate SSN are not allowed." + "(Value: " + dfr.getString(1) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}

				//Checks for last name
				if (dfr.getString(4).trim().equals("")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | Last Name: " + dfr.getString(1) + " " + "must have a last name." + "(Value: " + dfr.getString(4) + ")";
					else
						logLine = "Last Name: " + dfr.getString(1) + " " + "must have a last name." + "(Value: " + dfr.getString(4) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for first name
				if (dfr.getString(2).trim().equals("")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | First Name: " + dfr.getString(1) + " " + "must have a first name." + "(Value: " + dfr.getString(2) + ")";
					else
						logLine = "First Name: " + dfr.getString(1) + " " + "must have a first name." + "(Value: " + dfr.getString(2) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for a valid hire date
				if (dfr.getString(5).trim().equals("") || DateUtils.getDate(dfr.getString(5)) == 0) {
					if (debug)
						logLine = "Row: " + rowCounter + " | Hire Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid hire date." + "(Value: " + dfr.getString(5) + ")";
					else
						logLine = "Hire Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid hire date." + "(Value: " + dfr.getString(5) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for valid gender
				if (!dfr.getString(12).trim().toUpperCase().equals("F") && !dfr.getString(12).trim().toUpperCase().equals("M") && !dfr.getString(12).trim().toUpperCase().equals("U")) {
					if (debug)
						logLine = "Row: " + rowCounter + " | Gender: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid gender: F, M or U." + "(Value: " + dfr.getString(12) + ")";
					else
						logLine = "Gender: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid gender: F, M or U." + "(Value: " + dfr.getString(12) + ")";
					dfw.writeField(logLine);
					dfw.endRecord();
				}

				//Checks for benefit class
//				if(dfr.getString(17).trim().equals("")) {
//					dfw.writeField("Row: " + rowCounter + " | Benefit Class: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid benefit class." + "(Value: " + dfr.getString(17) + ")");
//					dfw.endRecord();
//				}

				//If medical benefit 1 is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(21).trim().equals("")) {
					if (dfr.getString(22).trim().equals("") || dfr.getString(22).contains("ERROR: ")) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 1 Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 1 configuration." + "(Value: " + dfr.getString(22).replaceAll("ERROR: ", "") + ")";
						else
							logLine = "Medical Benefit 1 Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 1 configuration." + "(Value: " + dfr.getString(22).replaceAll("ERROR: ", "") + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(23).trim().equals("") || DateUtils.getDate(dfr.getString(23)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 1 start date." + "(Value: " + dfr.getString(23) + ")";
						else
							logLine = "Medical Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 1 start date." + "(Value: " + dfr.getString(23) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If medical benefit 2 is taken, checks to ensure configs and start dates are present
				if (!dfr.getString(24).trim().equals("")) {
					if (dfr.getString(25).trim().equals("") || dfr.getString(25).contains("ERROR: ")) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 2 Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 2 configuration." + "(Value: " + dfr.getString(25).replaceAll("ERROR: ", "") + ")";
						else
							logLine = "Medical Benefit 2 Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 2 configuration." + "(Value: " + dfr.getString(25).replaceAll("ERROR: ", "") + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(26).trim().equals("") || DateUtils.getDate(dfr.getString(26)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Medical Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 2 start date." + "(Value: " + dfr.getString(26) + ")";
						else
							logLine = "Medical Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid medical benefit 2 start date." + "(Value: " + dfr.getString(26) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If dental benefits are taken, checks to ensure configs and start dates are present
				if (!dfr.getString(27).trim().equals("")) {
					if (dfr.getString(28).trim().equals("") || dfr.getString(28).contains("ERROR: ")) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Dental Benefit Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid dental benefit configuration." + "(Value: " + dfr.getString(28).replaceAll("ERROR: ", "") + ")";
						else
							logLine = "Dental Benefit Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid dental benefit configuration." + "(Value: " + dfr.getString(28).replaceAll("ERROR: ", "") + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(29).trim().equals("") || DateUtils.getDate(dfr.getString(29)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Dental Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid dental benefit start date." + "(Value: " + dfr.getString(29) + ")";
						else
							logLine = "Dental Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid dental benefit start date." + "(Value: " + dfr.getString(29) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If vision benefits are taken, checks to ensure configs and start dates are present
				if (!dfr.getString(30).trim().equals("")) {
					if (dfr.getString(31).trim().equals("") || dfr.getString(31).contains("ERROR: ")) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Vision Benefit Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid vision benefit configuration." + "(Value: " + dfr.getString(31).replaceAll("ERROR: ", "") + ")";
						else
							logLine = "Vision Benefit Coverage Level/Config: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid vision benefit configuration." + "(Value: " + dfr.getString(31).replaceAll("ERROR: ", "") + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(32).trim().equals("") || DateUtils.getDate(dfr.getString(32)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Vision Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid vision start date." + "(Value: " + dfr.getString(32) + ")";
						else
							logLine = "Vision Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid vision start date." + "(Value: " + dfr.getString(32) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If basic life is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(33).trim().equals("")) {
					if (dfr.getString(34).trim().equals("") || DateUtils.getDate(dfr.getString(34)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Basic Life Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid basic life benefit start date." + "(Value: " + dfr.getString(34) + ")";
						else
							logLine = "Basic Life Benefit Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid basic life benefit start date." + "(Value: " + dfr.getString(34) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(35).trim().equals("") || Float.valueOf(dfr.getString(35).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Basic Life Benefit Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a basic life benefit amount." + "(Value: " + dfr.getString(35) + ")";
						else
							logLine = "Basic Life Benefit Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a basic life benefit amount." + "(Value: " + dfr.getString(35) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If flex benefit 1 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(36).trim().equals("")) {
					if (dfr.getString(37).trim().equals("") || DateUtils.getDate(dfr.getString(37)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Flex Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid flex benefit 1 start date." + "(Value: " + dfr.getString(37) + ")";
						else
							logLine = "Flex Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid flex benefit 1 start date." + "(Value: " + dfr.getString(37) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(38).trim().equals("") || Float.valueOf(dfr.getString(38).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Flex Benefit 1 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a flex benefit 1 amount." + "(Value: " + dfr.getString(38) + ")";
						else
							logLine = "Flex Benefit 1 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a flex benefit 1 amount." + "(Value: " + dfr.getString(38) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If flex benefit 2 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(39).trim().equals("")) {
					if (dfr.getString(40).trim().equals("") || DateUtils.getDate(dfr.getString(40)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Flex Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid flex benefit 2 start date." + "(Value: " + dfr.getString(40) + ")";
						else
							logLine = "Flex Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid flex benefit 2 start date." + "(Value: " + dfr.getString(40) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(41).trim().equals("") || Float.valueOf(dfr.getString(41).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Flex Benefit 2 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a flex benefit 2 amount." + "(Value: " + dfr.getString(41) + ")";
						else
							logLine = "Flex Benefit 2 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a flex benefit 2 amount." + "(Value: " + dfr.getString(41) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 1 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(42).trim().equals("")) {
					if (dfr.getString(43).trim().equals("") || DateUtils.getDate(dfr.getString(43)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 1 start date." + "(Value: " + dfr.getString(43) + ")";
						else
							logLine = "Voluntary Benefit 1 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 1 start date." + "(Value: " + dfr.getString(43) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(44).trim().equals("") || Float.valueOf(dfr.getString(44).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 1 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 1 amount." + "(Value: " + dfr.getString(44) + ")";
						else
							logLine = "Voluntary Benefit 1 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 1 amount." + "(Value: " + dfr.getString(44) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 2 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(45).trim().equals("")) {
					if (dfr.getString(46).trim().equals("") || DateUtils.getDate(dfr.getString(46)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 2 start date." + "(Value: " + dfr.getString(46) + ")";
						else
							logLine = "Voluntary Benefit 2 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 2 start date." + "(Value: " + dfr.getString(46) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(47).trim().equals("") || Float.valueOf(dfr.getString(47).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 2 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 2 amount." + "(Value: " + dfr.getString(47) + ")";
						else
							logLine = "Voluntary Benefit 2 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 2 amount." + "(Value: " + dfr.getString(47) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 3 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(48).trim().equals("")) {
					if (dfr.getString(49).trim().equals("") || DateUtils.getDate(dfr.getString(49)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 3 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 3 start date." + "(Value: " + dfr.getString(49) + ")";
						else
							logLine = "Voluntary Benefit 3 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 3 start date." + "(Value: " + dfr.getString(49) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(50).trim().equals("") || Float.valueOf(dfr.getString(50).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 3 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 3 amount." + "(Value: " + dfr.getString(50) + ")";
						else
							logLine = "Voluntary Benefit 3 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 3 amount." + "(Value: " + dfr.getString(50) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 4 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(51).trim().equals("")) {
					if (dfr.getString(52).trim().equals("") || DateUtils.getDate(dfr.getString(52)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 4 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 4 start date." + "(Value: " + dfr.getString(52) + ")";
						else
							logLine = "Voluntary Benefit 4 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 4 start date." + "(Value: " + dfr.getString(52) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(53).trim().equals("") || Float.valueOf(dfr.getString(53).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 4 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 4 amount." + "(Value: " + dfr.getString(53) + ")";
						else
							logLine = "Voluntary Benefit 4 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 4 amount." + "(Value: " + dfr.getString(53) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 5 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(54).trim().equals("")) {
					if (dfr.getString(55).trim().equals("") || DateUtils.getDate(dfr.getString(55)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 5 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 5 start date." + "(Value: " + dfr.getString(55) + ")";
						else
							logLine = "Voluntary Benefit 5 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 5 start date." + "(Value: " + dfr.getString(55) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(56).trim().equals("") || Float.valueOf(dfr.getString(56).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 5 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 5 amount." + "(Value: " + dfr.getString(56) + ")";
						else
							logLine = "Voluntary Benefit 5 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 5 amount." + "(Value: " + dfr.getString(56) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 6 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(57).trim().equals("")) {
					if (dfr.getString(58).trim().equals("") || DateUtils.getDate(dfr.getString(58)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 6 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 6 start date." + "(Value: " + dfr.getString(58) + ")";
						else
							logLine = "Voluntary Benefit 6 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 6 start date." + "(Value: " + dfr.getString(58) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(59).trim().equals("") || Float.valueOf(dfr.getString(59).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 6 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 6 amount." + "(Value: " + dfr.getString(59) + ")";
						else
							logLine = "Voluntary Benefit 6 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 6 amount." + "(Value: " + dfr.getString(59) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 7 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(60).trim().equals("")) {
					if (dfr.getString(61).trim().equals("") || DateUtils.getDate(dfr.getString(61)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 7 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 7 start date." + "(Value: " + dfr.getString(61) + ")";
						else
							logLine = "Voluntary Benefit 7 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 7 start date." + "(Value: " + dfr.getString(61) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(62).trim().equals("") || Float.valueOf(dfr.getString(62).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 7 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 7 amount." + "(Value: " + dfr.getString(62) + ")";
						else
							logLine = "Voluntary Benefit 7 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 7 amount." + "(Value: " + dfr.getString(62) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}

				//If voluntary benefit 8 is taken, checks to ensure start dates and amount are present
				if (!dfr.getString(63).trim().equals("")) {
					if (dfr.getString(64).trim().equals("") || DateUtils.getDate(dfr.getString(64)) == 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 8 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 8 start date." + "(Value: " + dfr.getString(64) + ")";
						else
							logLine = "Voluntary Benefit 8 Start Date: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a valid voluntary benefit 8 start date." + "(Value: " + dfr.getString(64) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
					if (dfr.getString(65).trim().equals("") || Float.valueOf(dfr.getString(65).trim()) <= 0) {
						if (debug)
							logLine = "Row: " + rowCounter + " | Voluntary Benefit 8 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 8 amount." + "(Value: " + dfr.getString(65) + ")";
						else
							logLine = "Voluntary Benefit 8 Amount: " + dfr.getString(2).trim() + " " + dfr.getString(4).trim() + " " + "does not have a voluntary benefit 8 amount." + "(Value: " + dfr.getString(65) + ")";
						dfw.writeField(logLine);
						dfw.endRecord();
					}
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(DataImportValidator.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			dfw.close();
		}

		return errorLog.getName();
	}

	public static void main(String[] args) {
		try {
			new DataImportValidator().validate("/home/xichen/Desktop/CAS Import/Adapted CAS Employee Import 20110502.csv", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
