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

package com.arahant.imports;

import com.arahant.beans.CompanyBase;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BVendorCompany;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateScrollUtil;
import org.kissweb.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class CASAdapterImport {
	public String[] build(final String CASfilename) throws Exception {
		File empCsvFile = new File(FileSystemUtils.getWorkingDirectory(), "Adapted CAS Employee Import " + DateUtils.now() + ".csv");
        DelimitedFileWriter dfw = new DelimitedFileWriter(empCsvFile.getAbsolutePath(), false);
		File depCsvFile = new File(FileSystemUtils.getWorkingDirectory(), "Adapted CAS Dependent Import " + DateUtils.now() + ".csv");
        DelimitedFileWriter dfw2 = new DelimitedFileWriter(depCsvFile.getAbsolutePath(), false);

		try {
			DelimitedFileReader dfr = new DelimitedFileReader(CASfilename);
			writeEmpHeaderColumns(dfw);
			writeDepHeaderColumns(dfw2);
			
			String[] familyMedPlan = new String[]{"",""};
			String[] familyDentalPlan = new String[]{"",""};
			String[] familyVisionPlan = new String[]{"",""};
			String[] life = new String[]{"",""};
			String[] add = new String[]{"",""};
			String hireDate = "";
			String medConfig = "";
			String dentalConfig = "";
			String visionConfig = "";

			while(dfr.nextLine()) {
				if(dfr.getString(2).equalsIgnoreCase("ee")) {
					hireDate = "";
					familyMedPlan = new String[]{"",""};
					familyDentalPlan = new String[]{"",""};
					familyVisionPlan = new String[]{"",""};
					life= new String[]{"",""};
					add = new String[]{"",""};
					medConfig = "";
					dentalConfig = "";
					visionConfig = "";

					dfw.writeField(dfr.getString(7));							// Writes ID number
					dfw.writeField(dfr.getString(7));							// Writes employee SSN
					dfw.writeField(dfr.getString(4));							// Writes first name
					dfw.writeField(dfr.getString(5));							// Writes middle name/initial
					dfw.writeField(dfr.getString(3) + " " + dfr.getString(6));  // Writes last name + suffix
					dfw.writeField(dfr.getString(17));							// Writes hire date
					hireDate = dfr.getString(17);
					dfw.writeField(dfr.getString(12));							// Writes street 1
					dfw.writeField(dfr.getString(13));							// Writes street 2
					dfw.writeField(dfr.getString(14));							// Writes city
					dfw.writeField(dfr.getString(15));							// Writes state
					dfw.writeField(dfr.getString(16));							// Writes zip
					dfw.writeField(dfr.getString(9));							// Writes DOB
					dfw.writeField(dfr.getString(10));							// Writes gender
					dfw.writeField("S");										// Writes pay type (H or S)
					dfw.writeField(dfr.getString(19).replaceAll(",", ""));		// Writes pay amount
					dfw.writeField("555-555-5555");								// Writes home phone
					dfw.writeField(dfr.getString(18));							// Writes job title
					dfw.writeField(dfr.getString(33));							// TODO, need additional column - Writes benefit class
					dfw.writeField(dfr.getString(1));							// Writes org group Id
					dfw.writeField(dfr.getString(33));							// Writes medical group ID
					dfw.writeField(dfr.getString(34));							// Writes prescription group ID
//					if(dfr.getString(1).length() > 0)
//						dfw.writeField(dfr.getString(1).substring(0, dfr.getString(1).length() > 16 ? 15 : dfr.getString(1).length() - 1));	// Writes org group, need org group ext ref ID, not name
//					else
//						dfw.writeField("");

//					System.out.println(dfr.getString(3) + " " + " " + dfr.getString(4));  // Writes last name + suffix);
//					int x = 0;
//					if(dfr.getString(3).trim().equals("Baker")) {
//						x = 0;
//					}

					if(dfr.getString(20).trim().equals("")) {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}
					else {
						if(dfr.getString(20).contains("1"))	{					// Writes Med Benefit 1
							dfw.writeField("Blue Plan 1");
							if(!dfr.getString(21).trim().toLowerCase().equals("ee"))
							{
								familyMedPlan[0] = "Blue Plan 1";				// Sets Med Benefit 1 for family
								familyMedPlan[1] = "Blue Plan 1";
								medConfig = formatConfig(dfr.getString(21));
							}
							dfw.writeField(formatConfig(dfr.getString(21)));	// Writes Med Benefit 1 Coverage Level
							dfw.writeField(hireDate);							// Writes Med Benefit 1 Start
						}
						else if(dfr.getString(20).contains("2")) {
							dfw.writeField("Blue Plan 2");
							if(!dfr.getString(21).trim().toLowerCase().equals("ee"))
							{
								familyMedPlan[0] = "Blue Plan 2";				// Sets Med Benefit 1 for family
								familyMedPlan[1] = "Blue Plan 2";
								medConfig = formatConfig(dfr.getString(21));
							}
							dfw.writeField(formatConfig(dfr.getString(21)));	// Writes Med Benefit 1 Coverage Level
							dfw.writeField(hireDate);							// Writes Med Benefit 1 Start
						}
						else if(dfr.getString(20).contains("3")) {
							dfw.writeField("Blue Plan 3");
							if(!dfr.getString(21).trim().toLowerCase().equals("ee"))
							{
								familyMedPlan[0] = "Blue Plan 3";				// Sets Med Benefit 1 for family
								familyMedPlan[1] = "Blue Plan 3";
								medConfig = formatConfig(dfr.getString(21));
							}
							dfw.writeField(formatConfig(dfr.getString(21)));	// Writes Med Benefit 1 Coverage Level
							dfw.writeField(hireDate);							// Writes Med Benefit 1 Start
						}
						else {
							dfw.writeField("");
							dfw.writeField("");
							dfw.writeField("");
						}
					}
					
					dfw.writeField("");											// Writes Med Benefit 2
					dfw.writeField("");											// Writes Med Benefit 2 Coverage Level
					dfw.writeField("");											// Writes Med Benefit 2 Start
					
					if(dfr.getString(22).toLowerCase().contains("waiv") || dfr.getString(22).trim().equals("")) {
						dfw.writeField("");
						dfw.writeField("");										// Writes Dental Benefit Coverage Level
						dfw.writeField("");
					}
					else {
						dfw.writeField("Delta Dental");							// Writes Dental Benefit
						dfw.writeField(formatConfig(dfr.getString(22)));		// Writes Dental Benefit Coverage Level
						dentalConfig = formatConfig(dfr.getString(22));
						dfw.writeField(hireDate);								// Writes Dental Start
					}
					if(dfr.getString(22).toLowerCase().contains("ee & sp"))
						familyDentalPlan[1] = "Delta Dental";					// Sets Dental Benefit 1 for spouse
					else if(dfr.getString(22).toLowerCase().contains("child"))
						familyDentalPlan[0] = "Delta Dental";					// Sets Dental Benefit 1 for child(ren)
					else if(dfr.getString(22).toLowerCase().contains("family") || dfr.getString(22).toLowerCase().contains("ee & 1"))
					{
						familyDentalPlan[0] = "Delta Dental";					// Sets Dental Benefit 1 for family
						familyDentalPlan[1] = "Delta Dental";
					}

					if(dfr.getString(23).toLowerCase().contains("- e")) {
						dfw.writeField("EyeMed Vision");						// Writes Vision Benefit
						dfw.writeField(formatConfig(dfr.getString(23).substring(0, dfr.getString(23).indexOf("-"))));	// Writes Vision Coverage Level
						visionConfig = formatConfig(dfr.getString(23).substring(0, dfr.getString(23).indexOf("-")));
						dfw.writeField(hireDate);								// Writes Vision Start Date
						if(dfr.getString(23).toLowerCase().contains(" sp"))
							familyVisionPlan[1] = "EyeMed Vision";				// Sets Vision Benefit 1 for spouse
						else if(dfr.getString(23).toLowerCase().contains("child"))
							familyVisionPlan[0] = "EyeMed Vision";				// Sets Vision Benefit 1 for child(ren)
						else if(dfr.getString(23).toLowerCase().contains("family") || dfr.getString(22).toLowerCase().contains("ee & 1"))
						{
							familyVisionPlan[0] = "EyeMed Vision";				// Sets Vision Benefit 1 for family
							familyVisionPlan[1] = "EyeMed Vision";
						}
					}
					else if(dfr.getString(23).toLowerCase().contains("- g")) {
						dfw.writeField("Vision Service Plan");					// Writes Vision Benefit
						dfw.writeField(formatConfig(dfr.getString(23).substring(0, dfr.getString(23).indexOf("-"))));	// Writes Vision Coverage Level
						visionConfig = formatConfig(dfr.getString(23).substring(0, dfr.getString(23).indexOf("-")));
						dfw.writeField(hireDate);								// Writes Vision Start Date
						if(dfr.getString(23).toLowerCase().contains(" sp"))
							familyVisionPlan[1] = "Vision Service Plan";		// Sets Vision Benefit 1 for spouse
						else if(dfr.getString(23).toLowerCase().contains("child"))
							familyVisionPlan[0] = "Vision Service Plan";		// Sets Vision Benefit 1 for child(ren)
						else if(dfr.getString(23).toLowerCase().contains("family") || dfr.getString(22).toLowerCase().contains("ee & 1"))
						{
							familyVisionPlan[0] = "Vision Service Plan";		// Sets Vision Benefit 1 for family
							familyVisionPlan[1] = "Vision Service Plan";
						}
					}
					else {
						dfw.writeField("");
						dfw.writeField("");										// Writes Vision Coverage Level
						dfw.writeField("");
					}

					if(!dfr.getString(24).trim().equals("")) {
						dfw.writeField(dfr.getString(24).trim().equals("") ? "" : "Basic Life"); // Writes Flex 1 Benefit
						dfw.writeField(dfr.getString(24).trim().equals("") ? "" : hireDate);	// Writes Flex 1 Start Date
						dfw.writeField(dfr.getString(24).replaceAll(",", "").replaceAll("$", ""));	// Writes Flex 1 Amount
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					dfw.writeField("");											// Writes Flex 2 Benefit
					dfw.writeField("");											// Writes Flex 2 Start Date
					dfw.writeField("");											// Writes Flex 3 Amount
					dfw.writeField("");											// Writes Flex 3 Benefit
					dfw.writeField("");											// Writes Flex 3 Start Date
					dfw.writeField("");											// Writes Flex 3 Amount

					if(!dfr.getString(25).trim().equals("") && !dfr.getString(25).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(25).trim().equals("") ? "" : "Voluntary Employee Life"); // Writes Voluntary Benefit 1
						dfw.writeField(dfr.getString(25).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 1 Start
						dfw.writeField(dfr.getString(25).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 1 Amount
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(26).trim().equals("") && !dfr.getString(26).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(26).trim().equals("") ? "" : "Voluntary Spouse Life");	// Writes Voluntary Benefit 2
						dfw.writeField(dfr.getString(26).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 2 Start
						dfw.writeField(dfr.getString(26).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 2 Amount
						life[1] = dfr.getString(26).replaceAll(",", "").replaceAll("$", "");		// Sets spouse voluntary life benefit
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}						

					if(!dfr.getString(27).trim().equals("") && !dfr.getString(27).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(27).trim().equals("") ? "" : "Voluntary Dependent Child(ren) Life"); // Writes Voluntary Benefit 3
						dfw.writeField(dfr.getString(27).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 3 Start
						dfw.writeField(dfr.getString(27).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 3 Amount
						life[0] = dfr.getString(27).replaceAll(",", "").replaceAll("$", "");		// Sets child voluntary life benefit
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(28).trim().equals("") && !dfr.getString(28).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(28).trim().equals("") ? "" : "Voluntary AD & D Employee");	// Writes Voluntary Benefit 4
						dfw.writeField(dfr.getString(28).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 4 Start
						dfw.writeField(dfr.getString(28).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 4 Amount
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(29).trim().equals("") && !dfr.getString(29).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(29).trim().equals("") ? "" : "Voluntary AD&D Spouse");	// Writes Voluntary Benefit 5
						dfw.writeField(dfr.getString(29).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 5 Start
						dfw.writeField(dfr.getString(29).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 5 Amount
						add[1] = dfr.getString(29).replaceAll(",", "").replaceAll("$", "");			// Sets spouse AD&D
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(30).trim().equals("") && !dfr.getString(30).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(30).trim().equals("") ? "" : "Voluntary AD&D Child"); // Writes Voluntary Benefit 6
						dfw.writeField(dfr.getString(30).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 6 Start
						dfw.writeField(dfr.getString(30).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 6 Amount
						dfr.getString(30).replaceAll(",", "").replaceAll("$", "");					// Sets child AD&D
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(31).trim().equals("") && !dfr.getString(31).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(31).trim().equals("") ? "" : "Voluntary Short Term Disability"); // Writes Voluntary Benefit 7
						dfw.writeField(dfr.getString(31).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 7 Start
						dfw.writeField(dfr.getString(31).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 7 Amount
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}

					if(!dfr.getString(32).trim().equals("") && !dfr.getString(32).toLowerCase().contains("waiv")) {
						dfw.writeField(dfr.getString(32).trim().equals("") ? "" : "Voluntary Long Term Disability"); // Writes Voluntary Benefit 8
						dfw.writeField(dfr.getString(32).trim().equals("") ? "" : hireDate);	// Writes Voluntary Benefit 8 Start
						dfw.writeField(dfr.getString(32).replaceAll(",", "").replaceAll("$", ""));	// Writes Voluntary Benefit 8 Amount
					}
					else {
						dfw.writeField("");
						dfw.writeField("");
						dfw.writeField("");
					}
					
					dfw.endRecord();
				}
				else if(!dfr.getString(2).trim().equals("")) {
					int i = 0;
					dfw2.writeField(dfr.getString(7));							// Writes employee SSN
					if(dfr.getString(2).equals("F"))
						dfw2.writeField("S");
					else
						dfw2.writeField(dfr.getString(2));							// Writes relationship code
					if(dfr.getString(2).equals("S") || dfr.getString(2).equals("Sp") || dfr.getString(2).equals("F"))
						i = 1;
					else if(dfr.getString(2).equals("C"))
						i = 0;
					dfw2.writeField("");										// Writes relationship if other(N/A)
					dfw2.writeField(dfr.getString(8));							// Writes dependent SSN
					dfw2.writeField(dfr.getString(4));							// Writes first name
					dfw2.writeField(dfr.getString(5));							// Writes middle name/initial
					dfw2.writeField(dfr.getString(3) + " " + dfr.getString(6)); // Writes last name + suffix
					dfw2.writeField(dfr.getString(9));							// Writes DOB
					dfw2.writeField(dfr.getString(10));							// Writes gender
					
					dfw2.writeField(familyMedPlan[i]);							// Writes Med Benefit 1
					dfw2.writeField(familyMedPlan[i].trim().isEmpty() ? "" : hireDate);	// Writes Med Benefit 1 Start
					dfw2.writeField("");										// Writes Med Benefit 2
					dfw2.writeField("");										// Writes Med Benefit 2 Start
					dfw2.writeField(familyDentalPlan[i]);						// Writes Dental Benefit
					dfw2.writeField(familyDentalPlan[i].trim().isEmpty() ? "" : hireDate); // Writes Dental Start
					dfw2.writeField(familyVisionPlan[i]);						// Writes Vision Benefit
					dfw2.writeField(familyVisionPlan[i].trim().isEmpty() ? "" : hireDate); // Writes Vision Start Date
					
					if(i == 1 && !life[i].trim().isEmpty()) {
						dfw2.writeField("Voluntary Spouse Life");				// Writes Voluntary Benefit 1
						dfw2.writeField(hireDate);							// Writes Voluntary Benefit 1 Start
						dfw2.writeField(life[i]);								// Writes Voluntary Benefit 1 Amount
					}
					else {
						dfw2.writeField("");
						dfw2.writeField("");
						dfw2.writeField("");
					}

					if(i == 0 && !life[i].trim().isEmpty()) {
						dfw2.writeField("Voluntary Dependent Child(ren) Life");	// Writes Voluntary Benefit 2
						dfw2.writeField(hireDate);							// Writes Voluntary Benefit 2 Start
						dfw2.writeField(life[i]);								// Writes Voluntary Benefit 2 Amount
					}
					else {
						dfw2.writeField("");
						dfw2.writeField("");
						dfw2.writeField("");
					}

					if(i == 1 && !add[i].trim().isEmpty()) {
						dfw2.writeField("Voluntary AD&D Spouse");				// Writes Voluntary Benefit 3
						dfw2.writeField(hireDate);							// Writes Voluntary Benefit 3 Start
						dfw2.writeField(add[i]);								// Writes Voluntary Benefit 3 Amount
					}
					else {
						dfw2.writeField("");
						dfw2.writeField("");
						dfw2.writeField("");
					}

					if(i == 0 && !add[i].trim().isEmpty()) {
						dfw2.writeField("Voluntary AD&D Child");				// Writes Voluntary Benefit 4
						dfw2.writeField(hireDate);							// Writes Voluntary Benefit 4 Start
						dfw2.writeField(add[i]);								// Writes Voluntary Benefit 4 Amount
					}
					else {
						dfw2.writeField("");
						dfw2.writeField("");
						dfw2.writeField("");
					}

					dfw2.writeField(dfr.getString(33));
					
					dfw2.endRecord();
				}
			}
		}
		catch (IOException ex) {
            Logger.getLogger(CASAdapterImport.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally {
			dfw.close();
			dfw2.close();
		}
		
		String[] fileNames = {empCsvFile.getName(), depCsvFile.getName()};
		return fileNames;
	}

	private String formatConfig(String s) {
		String s2 = s.toLowerCase().trim();

		if(s2.equals("ee") || s2.equals("employee") || s2.contains("single"))
			return "EE";
		else if(s2.contains("1") || s2.contains(" sp"))
			return "EE+1";
		else if(s2.contains("family"))
			return "Family";
		else if(s2.contains("(ren)"))
			return "Employee Plus Two or More";
		else if(s2.contains("child"))
			return "EE+1";
		else
			return "ERROR: " + s;
	}

	private void writeEmpHeaderColumns(DelimitedFileWriter dfw) throws Exception {
		//Writing Header Columns
		dfw.writeField("ID");
		dfw.writeField("SSN");
		dfw.writeField("First Name");
		dfw.writeField("Middle Name");
		dfw.writeField("Last Name");
		dfw.writeField("Hire Date");
		dfw.writeField("Street 1");
		dfw.writeField("Street 2");
		dfw.writeField("City");
		dfw.writeField("State");
		dfw.writeField("Zip");
		dfw.writeField("DOB");
		dfw.writeField("Gender");
		dfw.writeField("Pay Type (H or S)");
		dfw.writeField("Pay");
		dfw.writeField("Home Phone");
		dfw.writeField("Job Title");
		dfw.writeField("Benefit Class");
		dfw.writeField("Org Group");
		dfw.writeField("Policy Group ID");
		dfw.writeField("Presc. Group ID");
		dfw.writeField("Med Benefit 1");
		dfw.writeField("Med Benefit 1 Coverage Level");
		dfw.writeField("Med Benefit 1 Start");
		dfw.writeField("Med Benefit 2");
		dfw.writeField("Med Benefit 2 Coverage Level");
		dfw.writeField("Med Benefit 2 Start");
		dfw.writeField("Dental Benefit");
		dfw.writeField("Dental Benefit Coverage Level");
		dfw.writeField("Dental Start");
		dfw.writeField("Vision Benefit");
		dfw.writeField("Vision Coverage Level");
		dfw.writeField("Vision Start Date");
		dfw.writeField("Basic Life");
		dfw.writeField("Basic Life Start");
		dfw.writeField("Basic Life Amount");
		dfw.writeField("Flex 1 Benefit");
		dfw.writeField("Flex 1 Start Date");
		dfw.writeField("Flex 1 Amount");
		dfw.writeField("Flex 2 Benefit");
		dfw.writeField("Flex 2 Start Date");
		dfw.writeField("Flex 2 Amount");
		dfw.writeField("Voluntary Life ");
		dfw.writeField("Voluntary Life Start");
		dfw.writeField("Voluntary Life Amount");
		dfw.writeField("Voluntary Spouse Life ");
		dfw.writeField("Voluntary Spouse Life Start");
		dfw.writeField("Voluntary Spouse Life Amount");
		dfw.writeField("Voluntary Child Life");
		dfw.writeField("Voluntary Child Life Start");
		dfw.writeField("Voluntary Child Life Amount");
		dfw.writeField("Voluntary AD&D");
		dfw.writeField("Voluntary AD&D Start");
		dfw.writeField("Voluntary AD&D Amount");
		dfw.writeField("Voluntary Spouse AD&D");
		dfw.writeField("Voluntary Spouse AD&D Start");
		dfw.writeField("Voluntary Spouse AD&D Amount");
		dfw.writeField("Voluntary Child AD&D");
		dfw.writeField("Voluntary Child AD&D Start");
		dfw.writeField("Voluntary Child AD&D Amount");
		dfw.writeField("Voluntary STD");
		dfw.writeField("Voluntary STD Start");
		dfw.writeField("Voluntary STD Amount");
		dfw.writeField("Voluntary LTD");
		dfw.writeField("Voluntary LTD Start");
		dfw.writeField("Voluntary LTD Amount");
		dfw.endRecord();
	}

	private void writeDepHeaderColumns(DelimitedFileWriter dfw) throws Exception {
		//Writing Header Columns
		dfw.writeField("Employee SSN");
		dfw.writeField("Relationship Code");
		dfw.writeField("Relationship if Other");
		dfw.writeField("SSN");
		dfw.writeField("First Name");
		dfw.writeField("Middle Name");
		dfw.writeField("Last Name");
		dfw.writeField("DOB");
		dfw.writeField("Gender");
		dfw.writeField("Med Benefit 1");
		dfw.writeField("Med Benefit 1 Start");
		dfw.writeField("Med Benefit 2");
		dfw.writeField("Med Benefit 2 Start");
		dfw.writeField("Dental Benefit");
		dfw.writeField("Dental Start");
		dfw.writeField("Vision Benefit");
		dfw.writeField("Vision Start Date");
		dfw.writeField("Voluntary Spouse Life");
		dfw.writeField("Voluntary Spouse Life Start");
		dfw.writeField("Voluntary Spouse Life Amount");
		dfw.writeField("Voluntary Child Life");
		dfw.writeField("Voluntary Child Life Start");
		dfw.writeField("Voluntary Child Life Amount");
		dfw.writeField("Voluntary Spouse AD&D");
		dfw.writeField("Voluntary Spouse AD&D Start");
		dfw.writeField("Voluntary Spouse AD&D Amount");
		dfw.writeField("Voluntary Child AD&D");
		dfw.writeField("Voluntary Child AD&D Start");
		dfw.writeField("Voluntary Child AD&D Amount");
		dfw.writeField("Policy Group Id");
		dfw.endRecord();
	}

	public static void main(String[] args) {
		/******* Writes interface file for employee and dependent imports based off of CAS' spreadsheet *********/
//		try {
//			new CASAdapterImport().build("/home/xichen/Desktop/CAS Resources 2011 COBRA New.csv");
//		}
//		catch (Exception e) {
//            e.printStackTrace();
//        }

		/******* Writes SQL statement that imports all EDI information into the database *********/
		HibernateScrollUtil<CompanyBase> scr = ArahantSession.getHSU().createCriteria(CompanyBase.class).notNull(CompanyBase.COM_URL).scroll();
		HashMap<String, String> sqlCommands = new HashMap<String, String>();
		boolean ftp = true;

		while(scr.next()) {
			String sql = "update company_base\n";
			String setLine = "set ";
			String whereLine = "where org_group_id=";

			CompanyBase c = scr.get();
			VendorCompany v = new BVendorCompany(c.getOrgGroupId()).getBean();
//			VendorCompany v = ArahantSession.getHSU().createCriteria(VendorCompany.class).eq(VendorCompany.ORGGROUPID, c.getOrgGroupId()).first();

			whereLine += addDashes(c.getOrgGroupId()) + ";";

			if(c.getComUrl().startsWith("s"))
				ftp = false;
			else
				ftp = true;

			if(ftp) {
				if(!StringUtils.isEmpty(c.getEncryptionKeyId()))
					setLine += "encryption_key_id=" + addDashes(c.getEncryptionKeyId()) + ", ";
				if(!StringUtils.isEmpty(c.getPublicEncryptionKey()))
					setLine += "public_encryption_key=" + addDashes(c.getPublicEncryptionKey()) + ", ";
			}

			setLine += "com_url=" + addDashes(c.getComUrl()) + ", ";
			setLine += "com_password=" + addDashes(c.getComPassword()) + ", ";
			setLine += "com_directory=" + addDashes(c.getComDirectory()) + ", ";
			setLine += "application_sender_id=" + addDashes(c.getApplicationSenderId()) + ", ";
			setLine += "application_receiver_id=" + addDashes(c.getApplicationReceiverId()) + ", ";
			setLine += "interchange_sender_id=" + addDashes(c.getInterchangeSenderId()) + ", ";
			if(!StringUtils.isEmpty(c.getEdiFileType() + ""))
				setLine += "edi_file_type=" + addDashes(c.getEdiFileType() + "") + ", ";
			if(!StringUtils.isEmpty(c.getEdiFileType() + ""))
				setLine += "edi_file_status=" + addDashes(c.getEdiFileStatus() + "") + ", ";
			if(!StringUtils.isEmpty(c.getEdiActivated() + ""))
				setLine += "edi_activated=" + addDashes(c.getEdiActivated() + "") + ", ";
			if(!StringUtils.isEmpty(c.getFederalEmployerId()))
				setLine += "federal_employer_id=" + addDashes(c.getFederalEmployerId()) + ", ";
			setLine += "interchange_receiver_id=" + addDashes(c.getInterchangeReceiverId()) + "\n";

			sql += setLine + whereLine;

			sql += "\n\nupdate vendor\n";
			sql += "set ";
			if(!StringUtils.isEmpty(v.getAccountNumber()))
				sql += "account_number=" + v.getAccountNumber() + ", ";
			sql += "interface_id=" + v.getInterfaceId() + "\n";
			sql += "where org_group_id=" + addDashes(c.getOrgGroupId()) + ";";

			sqlCommands.put(c.getOrgGroupId(), sql);
		}

		for(String id : sqlCommands.keySet())
			System.out.println(sqlCommands.get(id) + "\n\n");


		
	}

	private static String addDashes(String s) {
		return "'" + s + "'";
	}
}
