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
package com.arahant.exports;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.ServiceSubscribedJoin;
import com.arahant.beans.VendorCompany;
import com.arahant.beans.VendorGroup;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.business.BVendorCompany;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 *
 * Arahant
 */
public class CobraGuard {
	ArahantLogger logger = new ArahantLogger(CobraGuard.class);
	private boolean isFlex = false;
	private String outputLocation = "";
	String HTTPS_URL = "https://www.cobraguard.net/api/1.1/loader.html";
	public static final String AUTHORIZATION = "670a65dac44b049884ab4de61e7cd8d4";
	public String ORG_ID = "7807";
	public static String EMFILE = "EMFile";
	public static String ELFILE = "ELFile";
	public static String DPFILE = "DPFile";
	public static String TERMFILE = "TERMFile";
	public static String UTF8 = "UTF-8";
	File logFile;
	FileWriter fstream;
	BufferedWriter out;
	public static final String DIRECTORY = FileSystemUtils.getWorkingDirectory().getAbsolutePath() + "/Logfiles/";

	private String logFilePath = "COBRA_LOG.txt";

	public CobraGuard(HibernateCriteriaUtil hcuTermActives, HibernateCriteriaUtil hcuTermInactives, HibernateCriteriaUtil benefitJoinActives, String cobraGuardId) {

		if(BProperty.getBoolean("LogCobraFeed"))
		{
			try{
				boolean success = (new File(DIRECTORY)).mkdir();
				logFile = new File(DIRECTORY + logFilePath);
				fstream = new FileWriter(logFile,true);
				out = new BufferedWriter(fstream);
				out.write("COBRA FEED: Preparing files for ID " + cobraGuardId + "\n");
				out.flush();
			}
			catch(Exception e){
				//just continue
			}
		}
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		Map employeeMap = new HashMap();
		try {

			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();
			ORG_ID = cobraGuardId;
			Format formatter;
			Date date = new Date();
			formatter = new SimpleDateFormat("yyyyMMdd");
			String dateYYYYMMDD;
			dateYYYYMMDD = formatter.format(date);
			DelimitedFileWriter writer;

			//the 4 files needed by CobraGuard
			String employeeFile = ORG_ID + "_" + dateYYYYMMDD + "_EM_Refresh.csv";
			String dependentFile = ORG_ID + "_" + dateYYYYMMDD + "_DP_Refresh.csv";
			String electionFile = ORG_ID + "_" + dateYYYYMMDD + "_EL_Refresh.csv";
			String eventFile = ORG_ID + "_" + dateYYYYMMDD + "_TERM_Refresh.csv";

			//Create Employee file first
			writer = createCobraFile(employeeFile);
			final Set<String> empIds = new HashSet<String>();
			empIds.addAll((List<String>) (List) ArahantSession.getHSU().createCriteria(Employee.class).selectFields(Employee.PERSONID).eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany()).list());

			for (String personId : empIds) {
				BEmployee emp = new BEmployee(personId);
				//System.out.println("Employees " + emp.getNameFML());
				//have we created a file for this employee?
				if (!employeeMap.containsKey(emp.getPersonId())) {
					employeeMap.put(emp.getPersonId(), emp.getPersonId());
					createEmployeeCobraGuardFile(emp, writer);
				}
			}
			writer.close();
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
//					FileWriter fstream = new FileWriter(logFilePath,true);
//					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Employee File Created for ID " + cobraGuardId + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}

			//Next Create Dependent file
			writer = createCobraFile(dependentFile);
			for (String personId : empIds) {
				//Person person = bj.getCoveredPerson();
				//System.out.println("Dependents " + bj.getCoveredPersonId());
				BPerson bp = new BPerson(personId);
				for(BHREmplDependent dep : bp.getBEmployee().getDependents())
				{
					if(dep.isCurrentlyActive())
					{
						createDependentCobraGuardFile(bp.getBEmployee(), writer, dep);
					}
				}
			}
			writer.close();
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
//					FileWriter fstream = new FileWriter(logFilePath,true);
//					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Dependent File Created for ID " + cobraGuardId + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}

			//Next Create Election file
			writer = createCobraFile(electionFile);

			HibernateScrollUtil<HrBenefitJoin> scr = benefitJoinActives.scroll();
			while (scr.next()) {
				//System.out.println(cobraGuardId + " - " + scr.internalScroll().getRowNumber());
				HrBenefitJoin bj=scr.get();
				BEmployee emp = new BEmployee(bj.getPayingPerson().getPersonId());

				if(bj.getCoverageStartDate() == 0)
				{
					System.out.println("COBRA: Coverage Start Dates not set for ID " + bj.getBenefitJoinId());
					if(BProperty.getBoolean("LogCobraFeed"))
					{
						try{
		//					FileWriter fstream = new FileWriter(logFilePath,true);
		//					BufferedWriter out = new BufferedWriter(fstream);
							out.write("COBRA FEED: Coverage Start Dates not set for ID " + bj.getBenefitJoinId() + " - "+ cobraGuardId + "\n");
							out.flush();
						}
						catch(Exception e){
							//just continue
						}
					}
				}
				else
				{
					createElectionCobraGuardFile(emp, writer, hsu, bj);
				}
				
			}
			scr.close();
			writer.close();
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
//					FileWriter fstream = new FileWriter(logFilePath,true);
//					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Election File Created for ID " + cobraGuardId + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}

			//Next Create Event file
			writer = createCobraFile(eventFile);

			HibernateScrollUtil<HrBenefitJoin> scr2 = hcuTermActives.scroll();
			while (scr2.next()) {
				//System.out.println(cobraGuardId + " - " + scr2.internalScroll().getRowNumber());
				HrBenefitJoin bj=scr2.get();
				BEmployee emp = new BEmployee(bj.getPayingPerson().getPersonId());
				createEventCobraGuardFile(emp, writer, hsu, bj);
			}
			scr2.close();
			HibernateScrollUtil<HrBenefitJoinH> scr3 = hcuTermInactives.scroll();
			while (scr3.next()) {
				//System.out.println(cobraGuardId + " - " + scr2.internalScroll().getRowNumber());
				HrBenefitJoinH bj=scr3.get();
				BEmployee emp = new BEmployee(bj.getPayingPerson().getPersonId());
				createEventCobraGuardFile(emp, writer, hsu, bj);
			}
			scr3.close();
			writer.close();
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
//					FileWriter fstream = new FileWriter(logFilePath,true);
//					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Event File Created for ID " + cobraGuardId + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}

			//need to call HTTPSSender, NO LONGER USED
			// HTTPSSender hTTSSender = new HTTPSSender();
			File empFile = new File(this.outputLocation + "/" + employeeFile);
			File depFile = new File(this.outputLocation + "/" + dependentFile);
			File elFile = new File(this.outputLocation + "/" + electionFile);
			File evFile = new File(this.outputLocation + "/" + eventFile);

			this.sendIt(empFile, depFile, elFile, evFile);

			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
//					FileWriter fstream = new FileWriter(logFilePath,true);
//					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Files Sent for ID " + cobraGuardId + "\n");
					out.flush();
					out.close();
				}
				catch(Exception e){
					//just continue
				}
			}			
			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}

	private int getEventCode(String changeReasonId, HibernateSessionUtil hsu, HrBenefitJoin bj) {
		//get the eventType from the changeReason table using the id
		HrBenefitChangeReason bcr = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.ID, changeReasonId).first();

		if (bcr == null) {
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
					out.write("COBRA FEED: Change Reason null for benefit join: " + bj.getBenefitJoinId() + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}
			return 0;
		} else {
			int event_type = bcr.getEventType();
			//mapping to CobraGuard event-code
			switch (event_type) {
				case HrBenefitChangeReason.EVENT_TYPE_VOLUNTARY_TERM:
					return 111; //Voluntary termination and should return 2 for CobraGuard but our business logic is different, so we'll use 111 and set it to 2 on the client side
				case HrBenefitChangeReason.EVENT_TYPE_INVOLUNTARY_TERM:
					return 1; //Involuntary termination, return 1 for CobraGuard,
				case HrBenefitChangeReason.EVENT_TYPE_REDUCTION_IN_HOURS:
					return 2; //Reduction in Hours
				case HrBenefitChangeReason.EVENT_TYPE_DIVORCE:
					return 3;//Dissolution of Marriage
				case HrBenefitChangeReason.EVENT_TYPE_DEATH:
					return 4;//Death of Employee
				case HrBenefitChangeReason.EVENT_TYPE_ENTITLED_MEDICARE:
					return 5;//Employee becoming entitled to Medicare
				case HrBenefitChangeReason.EVENT_TYPE_DEPENDENT_INELIGIBLE:
					return 6;//Loss of Dependent status
			}
		}
		if(BProperty.getBoolean("LogCobraFeed"))
		{
			try{
				out.write("COBRA FEED: Change Reason code error for benefit join: " + bj.getBenefitJoinId() + " - ("+ bcr.getDescription() + ")\n");
				out.flush();
			}
			catch(Exception e){
				//just continue
			}
		}
		return 0;
	}

	private int getEventCode(String changeReasonId, HibernateSessionUtil hsu, HrBenefitJoinH bj) {
		//get the eventType from the changeReason table using the id
		HrBenefitChangeReason bcr = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.ID, changeReasonId).first();

		if (bcr == null) {
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
					out.write("COBRA FEED: Change Reason null for benefit join: " + bj.getHistory_id() + "\n");
					out.flush();
				}
				catch(Exception e){
					//just continue
				}
			}
			return 0;
		} else {
			int event_type = bcr.getEventType();
			//mapping to CobraGuard event-code
			switch (event_type) {
				case HrBenefitChangeReason.EVENT_TYPE_VOLUNTARY_TERM:
					return 111; //Voluntary termination and should return 2 for CobraGuard but our business logic is different, so we'll use 111 and set it to 2 on the client side
				case HrBenefitChangeReason.EVENT_TYPE_INVOLUNTARY_TERM:
					return 1; //Involuntary termination, return 1 for CobraGuard,
				case HrBenefitChangeReason.EVENT_TYPE_REDUCTION_IN_HOURS:
					return 2; //Reduction in Hours
				case HrBenefitChangeReason.EVENT_TYPE_DIVORCE:
					return 3;//Dissolution of Marriage
				case HrBenefitChangeReason.EVENT_TYPE_DEATH:
					return 4;//Death of Employee
				case HrBenefitChangeReason.EVENT_TYPE_ENTITLED_MEDICARE:
					return 5;//Employee becoming entitled to Medicare
				case HrBenefitChangeReason.EVENT_TYPE_DEPENDENT_INELIGIBLE:
					return 6;//Loss of Dependent status
			}
		}
		if(BProperty.getBoolean("LogCobraFeed"))
		{
			try{
				out.write("COBRA FEED: Change Reason code error for benefit join: " + bj.getHistory_id() + " - ("+ bcr.getDescription() + ")\n");
				out.flush();
			}
			catch(Exception e){
				//just continue
			}
		}
		return 0;
	}

	private Integer determineCoverageLevel(BHRBenefitConfig config) {
		Integer coverageLevel = 0;
		isFlex = false;

		//If coverage type is Flex then return 1
		if (config.getBenefitCategory().getBenefitType() == (HrBenefitCategory.FLEX_TYPE)) {
			isFlex = true;
			return 1;
		}
		//Employee Only
		if (config.getCoversEmployee()) {
			coverageLevel = 1;
		}
		// Employee + Spouse
		if (config.getCoversEmployee() && (config.getCoversEmployeeSpouse() || config.getSpouseNonEmployee())) {
			coverageLevel = 2;
		}
		// Employee + Child(ren)
		//might need to check max children
		if (config.getCoversEmployee() && config.getCoversChildren()) {
			coverageLevel = 3;
		}
		// Family
		if (config.getCoversEmployee() && (config.getCoversEmployeeSpouseOrChildren() || config.getSpouseNonEmpOrChildren()) && config.getMaxChildren() == 0) {
			coverageLevel = 4;
		}
		// Employee + 1
		if (config.getCoversEmployee() && (config.getCoversEmployeeSpouseOrChildren() || config.getSpouseNonEmpOrChildren()) && config.getMaxChildren() == 1) {
			coverageLevel = 5;
		}
		return coverageLevel;
	}

	private DelimitedFileWriter createCobraFile(String filename) {


		DelimitedFileWriter writer;
		String CobraGurad_OutputDirectory = "cobraguard";
		File csvFile;
		//create the directory to put the filled in PDF files
		File reportDir = new File(FileSystemUtils.getWorkingDirectory(), CobraGurad_OutputDirectory);
		if (!reportDir.exists()) {
			reportDir.mkdir();
		}

		this.outputLocation = reportDir.getAbsolutePath();

		csvFile = new File(reportDir + "/" + filename);
		try {
			csvFile.createNewFile();
			//System.out.println("File is at " + csvFile.getAbsolutePath());
			try {
				writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);
				return writer;
			} catch (IOException ex) {
				Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (IOException ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;

	}

	private void createEventCobraGuardFile(BEmployee emp, DelimitedFileWriter writer, HibernateSessionUtil hsu, HrBenefitJoin bj) {
		try {
			String changeReason = "";
			try {
				changeReason = bj.getBenefitChangeReason().getHrBenefitChangeReasonId();
			} catch (Exception e) {
				//if null
				//keep going
				changeReason = "";
			}
			if (changeReason.equalsIgnoreCase("")) {
				changeReason = bj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
			}

			Person person = hsu.createCriteria(Person.class).eq(Person.PERSONID, bj.getCoveredPersonId()).first();

			writer.writeField(ORG_ID);
			writer.writeField(emp.getSsn().replaceAll("-", ""));
			String depSSN = "";
			if (!emp.getSsn().equalsIgnoreCase(person.getUnencryptedSsn())) {
				depSSN = person.getUnencryptedSsn().replaceAll("-", "");
			}
			writer.writeField(depSSN.replaceAll("-", ""));//dependent SSN
			writer.writeField(person.getFname());
			writer.writeField(person.getLname());
			writer.writeField(person.getDob());
			writer.writeField(bj.getCoverageEndDate());

			//Employee status.  If terminated, need to know why for ARRA
			int event_code = getEventCode(changeReason, hsu, bj);
			String terminateCode = "";
			if (event_code == 1) {
				terminateCode = "1";  //1 = Involuntarily terminated, 2 = Voluntarily terminated
			} else if (event_code == 111) {
				terminateCode = "2";
				event_code = 1; //set it back to 1 because it was 111
			}
			writer.writeField(event_code);//event code
			writer.writeField(terminateCode);//arra code if Terminated, 1 or 2(involuntary)
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.endRecord();
		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createEventCobraGuardFile(BEmployee emp, DelimitedFileWriter writer, HibernateSessionUtil hsu, HrBenefitJoinH bj) {
		try {
			String changeReason = "";
			try {
				changeReason = bj.getBenefitChangeReason().getHrBenefitChangeReasonId();
			} catch (Exception e) {
				//if null
				//keep going
				changeReason = "";
			}
			if (changeReason.equalsIgnoreCase("") && bj.getLifeEvent()!= null) {
				changeReason = bj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
			}
			else
			{
				if(changeReason.equals(""))
					changeReason = BHRBenefitChangeReason.findOrMake("Miscellaneous").getHrBenefitChangeReasonId();
			}

			Person person = hsu.createCriteria(Person.class).eq(Person.PERSONID, bj.getCoveredPersonId()).first();

			writer.writeField(ORG_ID);
			writer.writeField(emp.getSsn().replaceAll("-", ""));
			String depSSN = "";
			if (!emp.getSsn().equalsIgnoreCase(person.getUnencryptedSsn())) {
				depSSN = person.getUnencryptedSsn().replaceAll("-", "");
			}
			writer.writeField(depSSN.replaceAll("-", ""));//dependent SSN
			writer.writeField(person.getFname());
			writer.writeField(person.getLname());
			writer.writeField(person.getDob());
			writer.writeField(bj.getCoverageEndDate());

			//Employee status.  If terminated, need to know why for ARRA
			int event_code = getEventCode(changeReason, hsu, bj);
			String terminateCode = "";
			if (event_code == 1) {
				terminateCode = "1";  //1 = Involuntarily terminated, 2 = Voluntarily terminated
			} else if (event_code == 111) {
				terminateCode = "2";
				event_code = 1; //set it back to 1 because it was 111
			}
			writer.writeField(event_code);//event code
			writer.writeField(terminateCode);//arra code if Terminated, 1 or 2(involuntary)
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.endRecord();
		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createElectionCobraGuardFile(BEmployee emp, DelimitedFileWriter writer, HibernateSessionUtil hsu, HrBenefitJoin hbj) {
		try {
			//if (hbj.getUsingCOBRA()=='Y') {
			Person person = hsu.createCriteria(Person.class).eq(Person.PERSONID, hbj.getCoveredPersonId()).first();

			BHRBenefitConfig config = new BHRBenefitConfig(hbj.getHrBenefitConfig());
			int coverageLevel = determineCoverageLevel(config);
			if(coverageLevel == 0)
			{
				if(BProperty.getBoolean("LogCobraFeed"))
				{
					try{
	//					FileWriter fstream = new FileWriter(logFilePath,true);
	//					BufferedWriter out = new BufferedWriter(fstream);
						out.write("COBRA FEED: No Valid Coverage Level determined for benefit join - " + hbj.getBenefitJoinId() + "\n");
						out.flush();
					}
					catch(Exception e){
						//just continue
					}
				}
				return;
			}

			String fsa = "";
			writer.writeField(emp.getSsn().replaceAll("-", ""));
			writer.writeField(config.getBenefit().getPlanId());
			writer.writeField(coverageLevel);
			writer.writeField(hbj.getCoverageStartDate());
			writer.writeField(person.getFname());
			writer.writeField(person.getLname());
			writer.writeField(person.getDob());
			writer.writeField(person.getUnencryptedSsn().replaceAll("-", ""));

			//if employee was covered under Flex then get the amount
			if (isFlex) {
				fsa = hbj.getCalculatedCost().replace("$", "");//remove the $
			}
			writer.writeField(fsa); //FSA Amount.
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.endRecord();

			// }
		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createDependentCobraGuardFile(BEmployee emp, DelimitedFileWriter writer, BHREmplDependent dp) {
		try {
			writer.writeField(emp.getSsn().replaceAll("-", ""));
			writer.writeField(!dp.getSsn().replaceAll("-", "").equals("999999999") ? dp.getSsn().replaceAll("-", "") : "");
			writer.writeField(dp.getFirstName());
			writer.writeField(dp.getMiddleName());
			writer.writeField(dp.getLastName());
			String relation = dp.getRelationshipType();
			if (relation.equalsIgnoreCase("c")) {
				relation = "Child";
			} else {
				relation = "Spouse";
			}
			writer.writeField(relation);
			writer.writeField(dp.getDob());
			writer.writeField(dp.getSex());
			writer.writeField(dp.getPersonalEmail());
			writer.writeField(dp.getStreet());
			writer.writeField(dp.getStreet2());
			writer.writeField(dp.getCity());
			writer.writeField(dp.getState());
			writer.writeField(formatZip(dp.getZip()));

			writer.writeField(formatPhoneNumber(emp.getHomePhone()));
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.endRecord();

		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void createEmployeeCobraGuardFile(BEmployee emp, DelimitedFileWriter writer) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		String vendorId = hsu.createCriteria(VendorCompany.class).eq(VendorCompany.NAME, "CobraGuard")
																 .selectFields(VendorCompany.ORGGROUPID)
																 .stringVal();
		OrgGroup orgGroup = new BOrgGroup(emp.getOrgGroupId()).getOrgGroup();
		String locationCode = hsu.createCriteria(VendorGroup.class).eq(VendorGroup.VENDOR, new BVendorCompany(vendorId).getBean())
																   .eq(VendorGroup.ORG_GROUP, orgGroup)
																   .selectFields(VendorGroup.GROUP_VENDOR_ID)
																   .stringVal();
		try {
			writer.writeField(emp.getSsn().replaceAll("-", ""));
			writer.writeField(emp.getFirstName());
			writer.writeField(emp.getMiddleName());
			writer.writeField(emp.getLastName());
			writer.writeField(emp.getDob());
			writer.writeField(emp.getSex());
			writer.writeField(emp.getPersonalEmail());
			writer.writeField(emp.getStreet());
			writer.writeField(emp.getStreet2());
			writer.writeField(emp.getCity());
			writer.writeField(emp.getState());
			writer.writeField(formatZip(emp.getZip()));
			writer.writeField(emp.getHireDate());
			writer.writeField(formatPhoneNumber(emp.getHomePhone()));
			writer.writeField(StringUtils.isEmpty(locationCode) ? "" : locationCode);
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.writeField("");
			writer.endRecord();
		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private String formatZip(String zip) {
		String newZip = "";
		for(int i = 0; i < zip.length(); i++)
			if(Character.isDigit(zip.charAt(i))) {
				newZip = newZip + zip.substring(i, i + 1);
			}

		if(newZip.length() == 5)
			return newZip;
		else if(newZip.length() == 9)
			return newZip = newZip.substring(0, 5) + "-" + newZip.substring(5);
		else if(newZip.length() > 5)
			return newZip = newZip.substring(0, 5);
		else
			return zip;
	}

	private String formatPhoneNumber(String number)
	{
		String ret = "";
		number = number.trim();
		for (int i = 0; i < number.length(); i++)
		{
			if(Character.isDigit(number.charAt(i)))
				ret += number.charAt(i) + "";
		}
		if(ret.length() == 10)
			ret = "(" + ret.substring(0, 3) + ") " + ret.substring(3);
		else
			return number;

		return ret;
	}

	public String execPHP() {
		//sudo apt-get install php5-curl
		//sudo apt-get install php5-cli
		//sudo /etc/init.d/apache2 restart
		//chmod to the cobraguard.php to be executable


		StringBuilder output = new StringBuilder();
		try {
			String line;

			Process p = Runtime.getRuntime().exec("php " + this.outputLocation + "/cobraguard.php " + this.outputLocation);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				output.append(line);
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return output.toString();
	}

	public static void main(final String args[]) {
		//the client calling this class should pass in a list of employee ids and ORG ID
		Set<String> empIds = new HashSet<String>();
//        empIds.add("00001-0000468731");//Gates db
//        empIds.add("00001-0000468715");
//        empIds.add("00001-0000469492");
//        empIds.add("00001-0000469493");
//        empIds.add("00001-0000469494");
//        empIds.add("00001-0000469495");
		empIds.add("00001-0000468749");
		empIds.add("00001-0000468803");
		empIds.add("00001-0000468804");
		empIds.add("00001-0000468805");


		int orgId = 1234;
		//new CobraGuard(new HashSet(),"5266",null);
	}

	private void sendIt(File emFile, File dpFile, File elFile, File termFile) {
		try {
			PostMethod filePost = new PostMethod(HTTPS_URL);
			Part[] parts = {
				new StringPart("AuthString", AUTHORIZATION),
				new StringPart("OrgID", ORG_ID),
				new FilePart(EMFILE, emFile),
				new FilePart(DPFILE, dpFile),
				new FilePart(ELFILE, elFile),
				new FilePart(TERMFILE, termFile)
			};
			if(BProperty.getBoolean("LogCobraFeed"))
			{
				try{
					FileWriter fstream = new FileWriter(logFilePath,true);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write("COBRA FEED: Attempting to send files" + "\n");
					out.close();
				}
				catch(Exception e){
					//just continue
				}
			}

			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			HttpClient client = new HttpClient();
			int status = client.executeMethod(filePost);
			InputStream is = filePost.getResponseBodyAsStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {

				if(BProperty.getBoolean("LogCobraFeed"))
				{
					try{
						FileWriter fstream = new FileWriter(logFilePath,true);
						BufferedWriter out = new BufferedWriter(fstream);
						out.write("COBRA FEED: Response from COBRA GUARD ver2 - " + line + "\n");
						out.close();
					}
					catch(Exception e){
						//just continue
					}
				}
			}
			filePost.releaseConnection();
		} catch (IOException ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
//    private void sendIt(File emFile, File dpFile, File elFile, File termFile) {
//        try {
//
//            HttpClient client = new HttpClient();
//            MultipartPostMethod mPost = new MultipartPostMethod(HTTPS_URL);
//            client.setConnectionTimeout(8000);
//
//            mPost.addParameter("AuthString", AUTHORIZATION);
//            mPost.addParameter("OrgID", ORG_ID);
//            mPost.addParameter(EMFILE, emFile);
//            mPost.addParameter(DPFILE, dpFile);
//            mPost.addParameter(ELFILE, elFile);
//            mPost.addParameter(TERMFILE, termFile);
//
//            int statusCode1 = client.executeMethod(mPost);
//
//            System.out.println("statusLine>>>" + mPost.getStatusLine());
//            InputStream is = mPost.getResponseBodyAsStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println("RESPONSE FROM CORBRA GUARD ..." + line);
//            }
//            mPost.releaseConnection();
//        } catch (IOException ex) {
//            Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
