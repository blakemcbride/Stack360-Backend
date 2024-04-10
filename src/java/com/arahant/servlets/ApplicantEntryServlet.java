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
package com.arahant.servlets;

import com.arahant.beans.*;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantAnswer;
import com.arahant.business.BApplicantQuestion;
import com.arahant.business.BApplicantQuestionChoice;
import com.arahant.business.BApplicantSource;
import com.arahant.business.BApplicantStatus;
import com.arahant.business.BApplication;
import com.arahant.business.BApplicationStatus;
import com.arahant.business.BEducation;
import com.arahant.business.BFormType;
import com.arahant.business.BHREEORace;
import com.arahant.business.BPersonForm;
import com.arahant.business.BPersonalReference;
import com.arahant.business.BPreviousEmployment;
import com.arahant.business.BProperty;
import com.arahant.utils.*;
import com.arahant.utils.Mail;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 *
 * Date Applied Job Being Applied Applicant Name (Applicant First Name
 * concatenated with Applicant Last Name) Gender Race/Ethnicity Veteran Status
 * Identify where they learned about the employment opportunity
 *
 * Applicant information to store: Date Applied Job Being Applied First Name
 * (Required) Middle Name Last Name (Required) Nick Name E-mail Address Line 1
 * (Required) Address Line 2 City (Required) State (Required) Zip Code
 * (Required) Home Phone Work Phone Mobile Phone Contact Number (Required)
 * Resume File (this is an attached document with extensions .doc, .rtf or .pdf
 * Notes section for the Applicant to place Additional Notes
 *
 */
public class ApplicantEntryServlet extends HttpServlet {

	private static final transient ArahantLogger logger = new ArahantLogger(ApplicantEntryServlet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			HibernateSessionUtil hsu = ArahantSession.openHSU();
			hsu.beginTransaction();

			String resumeFileName = "";

			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(0, FileSystemUtils.createTempDirFile(null)); // use files for everything
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			@SuppressWarnings("unchecked")
			List<FileItem> allFileItems = servletFileUpload.parseRequest(request); // this will generate temp files
			List<File> files = new ArrayList<File>();
			Iterator<FileItem> iterator = allFileItems.iterator();
			HashMap<String, String> nameValuePairs = new HashMap<String, String>();

			// tell our disk factory we will do file cleanup
			diskFileItemFactory.setFileCleaningTracker(null);
			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();

				if (!fileItem.isFormField()) {
					DiskFileItem diskFileItem = (DiskFileItem) fileItem;
					//System.out.println(fileItem.getName());
					resumeFileName = fileItem.getName();
					files.add(FileSystemUtils.moveToTempFile(diskFileItem.getStoreLocation(), "fileUpload", ".dat"));
				} else
					nameValuePairs.put(fileItem.getFieldName(), fileItem.getString());

			}

            /*
			 BufferedReader r=request.getReader();
			 String line=null;
			 while ((line=r.readLine())!=null)
			 {
			 System.out.println(line);
			 }
			 */
			String job = nameValuePairs.get("jobAppliedFor");
			String fname = nameValuePairs.get("applicantFirstName");
			String lname = nameValuePairs.get("applicantLastName");
			String mname = nameValuePairs.get("applicantMiddleName");
			String nname = nameValuePairs.get("applicantNickName");
			String gender = nameValuePairs.get("gender");
			String race = nameValuePairs.get("race");
			String veteran = nameValuePairs.get("veteran");
			String email = nameValuePairs.get("email");
			String addr1 = nameValuePairs.get("addressLine1");
			String addr2 = nameValuePairs.get("addressLine2");
			String city = nameValuePairs.get("city");
			String state = nameValuePairs.get("state");
			String zip = nameValuePairs.get("zip");
			String homePhone = nameValuePairs.get("homePhone");
			String workPhone = nameValuePairs.get("workPhone");
			String mobilePhone = nameValuePairs.get("mobilePhone");
			String contactPhone = nameValuePairs.get("contactPhone");
			String notes = nameValuePairs.get("notes");

			String refbyother = nameValuePairs.get("refbyother");
			String refbyemp = nameValuePairs.get("refbyemp");
			String whereLearned = nameValuePairs.get("whereLearned");

			String dateAvailable = nameValuePairs.get("dateAvailable");
			String desiredSalary = nameValuePairs.get("desiredSalary");

			String militaryBranch = nameValuePairs.get("militaryBranch");
			String militaryStartMonth = nameValuePairs.get("militaryStartMonth");
			String militaryStartYear = nameValuePairs.get("militaryStartYear");
			String militaryEndMonth = nameValuePairs.get("militaryEndMonth");
			String militaryEndYear = nameValuePairs.get("militaryEndYear");
			String militaryRank = nameValuePairs.get("militaryRank");
			String militaryDischargeType = nameValuePairs.get("militaryDischargeType");
			String militaryDischargeExplain = nameValuePairs.get("militaryDischargeExplain");
			String convictedOfCrime = nameValuePairs.get("convictedOfCrime");
			String convictedOfWhat = nameValuePairs.get("convictedOfWhat");
			String workedForCompanyBefore = nameValuePairs.get("workedForCompanyBefore");
			String workedForCompanyWhen = nameValuePairs.get("workedForCompanyWhen");

			//Personal Reference 1
			String referenceName = nameValuePairs.get("referenceName");
			String relationshipType = nameValuePairs.get("relationshipType");
			String relationshipOther = nameValuePairs.get("relationshipOther");
			String refCompany = nameValuePairs.get("refCompany");
			String refPhone = nameValuePairs.get("refPhone");
			String refAddress = nameValuePairs.get("refAddress");
			String yearsKnown = nameValuePairs.get("yearsKnown");

			//Personal Reference 2
			String referenceName2 = nameValuePairs.get("referenceName2");
			String relationshipType2 = nameValuePairs.get("relationshipType2");
			String relationshipOther2 = nameValuePairs.get("relationshipOther2");
			String refCompany2 = nameValuePairs.get("refCompany2");
			String refPhone2 = nameValuePairs.get("refPhone2");
			String refAddress2 = nameValuePairs.get("refAddress2");
			String yearsKnown2 = nameValuePairs.get("yearsKnown2");

			//Personal Reference 3
			String referenceName3 = nameValuePairs.get("referenceName3");
			String relationshipType3 = nameValuePairs.get("relationshipType3");
			String relationshipOther3 = nameValuePairs.get("relationshipOther3");
			String refCompany3 = nameValuePairs.get("refCompany3");
			String refPhone3 = nameValuePairs.get("refPhone3");
			String refAddress3 = nameValuePairs.get("refAddress3");
			String yearsKnown3 = nameValuePairs.get("yearsKnown3");

			//Previous Employment 1
			String empStartMonth = nameValuePairs.get("empStartMonth");
			String empStartYear = nameValuePairs.get("empStartYear");
			String empEndMonth = nameValuePairs.get("empEndMonth");
			String empEndYear = nameValuePairs.get("empEndYear");
			String empCompany = nameValuePairs.get("empCompany");
			String empPhone = nameValuePairs.get("empPhone");
			String empAddress = nameValuePairs.get("empAddress");
			String supervisor = nameValuePairs.get("supervisor");
			String jobTitle = nameValuePairs.get("jobTitle");
			String startingSalary = nameValuePairs.get("startingSalary");
			String endingSalary = nameValuePairs.get("endingSalary");
			String responsibilities = nameValuePairs.get("responsibilities");
			String reasonForLeaving = nameValuePairs.get("reasonForLeaving");
			String contactSupervisor = nameValuePairs.get("contactSupervisor");

			//Previous Employment 2
			String empStartMonth2 = nameValuePairs.get("empStartMonth2");
			String empStartYear2 = nameValuePairs.get("empStartYear2");
			String empEndMonth2 = nameValuePairs.get("empEndMonth2");
			String empEndYear2 = nameValuePairs.get("empEndYear2");
			String empCompany2 = nameValuePairs.get("empCompany2");
			String empPhone2 = nameValuePairs.get("empPhone2");
			String empAddress2 = nameValuePairs.get("empAddress2");
			String supervisor2 = nameValuePairs.get("supervisor2");
			String jobTitle2 = nameValuePairs.get("jobTitle2");
			String startingSalary2 = nameValuePairs.get("startingSalary2");
			String endingSalary2 = nameValuePairs.get("endingSalary2");
			String responsibilities2 = nameValuePairs.get("responsibilities2");
			String reasonForLeaving2 = nameValuePairs.get("reasonForLeaving2");
			String contactSupervisor2 = nameValuePairs.get("contactSupervisor2");

			//Previous Employment 3
			String empStartMonth3 = nameValuePairs.get("empStartMonth3");
			String empStartYear3 = nameValuePairs.get("empStartYear3");
			String empEndMonth3 = nameValuePairs.get("empEndMonth3");
			String empEndYear3 = nameValuePairs.get("empEndYear3");
			String empCompany3 = nameValuePairs.get("empCompany3");
			String empPhone3 = nameValuePairs.get("empPhone3");
			String empAddress3 = nameValuePairs.get("empAddress3");
			String supervisor3 = nameValuePairs.get("supervisor3");
			String jobTitle3 = nameValuePairs.get("jobTitle3");
			String startingSalary3 = nameValuePairs.get("startingSalary3");
			String endingSalary3 = nameValuePairs.get("endingSalary3");
			String responsibilities3 = nameValuePairs.get("responsibilities3");
			String reasonForLeaving3 = nameValuePairs.get("reasonForLeaving3");
			String contactSupervisor3 = nameValuePairs.get("contactSupervisor3");

			//Education
			String schoolStartMonth = nameValuePairs.get("schoolStartMonth");
			String schoolStartYear = nameValuePairs.get("schoolStartYear");
			String schoolEndMonth = nameValuePairs.get("schoolEndMonth");
			String schoolEndYear = nameValuePairs.get("schoolEndYear");
			String schoolType = nameValuePairs.get("schoolType");
			String schoolName = nameValuePairs.get("schoolName");
			String schoolLocation = nameValuePairs.get("schoolLocation");
			String graduate = nameValuePairs.get("graduate");
			String subject = nameValuePairs.get("subject");
			String otherType = nameValuePairs.get("otherType");

			//Education 2
			String schoolStartMonth2 = nameValuePairs.get("schoolStartMonth2");
			String schoolStartYear2 = nameValuePairs.get("schoolStartYear2");
			String schoolEndMonth2 = nameValuePairs.get("schoolEndMonth2");
			String schoolEndYear2 = nameValuePairs.get("schoolEndYear2");
			String schoolType2 = nameValuePairs.get("schoolType2");
			String schoolName2 = nameValuePairs.get("schoolName2");
			String schoolLocation2 = nameValuePairs.get("schoolLocation2");
			String graduate2 = nameValuePairs.get("graduate2");
			String subject2 = nameValuePairs.get("subject2");
			String otherType2 = nameValuePairs.get("otherType2");

			//Education 3
			String schoolStartMonth3 = nameValuePairs.get("schoolStartMonth3");
			String schoolStartYear3 = nameValuePairs.get("schoolStartYear3");
			String schoolEndMonth3 = nameValuePairs.get("schoolEndMonth3");
			String schoolEndYear3 = nameValuePairs.get("schoolEndYear3");
			String schoolType3 = nameValuePairs.get("schoolType3");
			String schoolName3 = nameValuePairs.get("schoolName3");
			String schoolLocation3 = nameValuePairs.get("schoolLocation3");
			String graduate3 = nameValuePairs.get("graduate3");
			String subject3 = nameValuePairs.get("subject3");
			String otherType3 = nameValuePairs.get("otherType3");


			int dateApplied = DateUtils.now();

			if (gender == null || gender.trim().equals("") || gender.equals("null"))
				gender = "U";


			gender = gender.toUpperCase();

			BApplicant bap = new BApplicant();
			bap.create();
			bap.setSsn(null);
			bap.setFirstName(fname);
			bap.setLastName(lname);
			bap.setMiddleName(mname);
			bap.setNickName(nname);
			if (gender != null && gender.trim().length() > 0)
				bap.setSex(gender);
			if (race != null && race.trim().length() > 0)
				switch (race.charAt(0)) {
					case 'W':
						bap.setRaceId(BHREEORace.findOrMake("White"));
						break;
					case 'B':
						bap.setRaceId(BHREEORace.findOrMake("Black or African American"));
						break;
					case 'H':
						bap.setRaceId(BHREEORace.findOrMake("Hispanic or Latino"));
						break;
					case 'A':
						bap.setRaceId(BHREEORace.findOrMake("Asian"));
						break;
					case 'I':
						bap.setRaceId(BHREEORace.findOrMake("American Indian or Alaskan Native"));
						break;
					case 'P':
						bap.setRaceId(BHREEORace.findOrMake("Native Hawaiian or Other Pacific Islander"));
						break;
					case 'T':
						bap.setRaceId(BHREEORace.findOrMake("Two or More Race"));
						break;
				}

			bap.setPersonalEmail(email);
			bap.setStreet(addr1);
			bap.setStreet2(addr2);
			bap.setCity(city);
			bap.setState(state);
			bap.setZip(zip);
			bap.setHomePhone(homePhone);
			bap.setWorkPhone(workPhone);
			bap.setMobilePhone(mobilePhone);
			bap.getPerson().setRecordPersonId(bap.getPersonId());

			bap.setComments(notes);

			bap.setFirstAwareDate(dateApplied);

			if (!isEmpty(dateAvailable))
				bap.setDateAvailable(Integer.parseInt(dateAvailable));

			if (!isEmpty(desiredSalary))
				bap.setDesiredSalary(Integer.parseInt(desiredSalary));

			if (!isEmpty(militaryBranch))
				bap.setMilitaryBranch(militaryBranch.charAt(0));

			if (!isEmpty(militaryStartMonth) && !isEmpty(militaryStartYear))
				bap.setMilitaryStartDate((Integer.parseInt(militaryStartYear) * 100) + Integer.parseInt(militaryStartMonth));

			if (!isEmpty(militaryEndMonth) && !isEmpty(militaryEndYear))
				bap.setMilitaryEndDate((Integer.parseInt(militaryEndYear) * 100) + Integer.parseInt(militaryEndMonth));

			if (!isEmpty(militaryRank))
				bap.setMilitaryRank(militaryRank);

			if (!isEmpty(militaryDischargeType)) {
				bap.setMilitaryDischargeType(militaryDischargeType.charAt(0));

				if (militaryDischargeType.charAt(0) == 'O' && !isEmpty(militaryDischargeExplain))
					bap.setMilitaryDischargeExplain(militaryDischargeExplain);
			}

			if (!isEmpty(convictedOfCrime)) {
				bap.setConvictedOfCrime(convictedOfCrime.charAt(0));

				if (convictedOfCrime.charAt(0) == 'Y' && !isEmpty(convictedOfWhat))
					bap.setConvictedOfWhat(convictedOfWhat);
			}

			if (!isEmpty(workedForCompanyBefore)) {
				bap.setWorkedForCompanyBefore(workedForCompanyBefore.charAt(0));

				if (workedForCompanyBefore.charAt(0) == 'Y' && !isEmpty(workedForCompanyWhen))
					bap.setWorkedForCompanyWhen(workedForCompanyWhen);
			}

			ApplicantSource as = hsu.createCriteria(ApplicantSource.class)
					.eq(ApplicantSource.DESCRIPTION, "Internet")
					.first();
			String sourceId;
			if (as == null) {
				BApplicantSource bas = new BApplicantSource();
				sourceId = bas.create();
				bas.setDescription("Internet");
				bas.insert();

			} else
				sourceId = as.getApplicantSourceId();
			bap.setApplicantSourceId(sourceId);

			ApplicantStatus stat = hsu.createCriteria(ApplicantStatus.class)
					.eq(ApplicantStatus.SEQ, (short) 0)
					.first();

			String statusId;
			if (stat == null) {
				BApplicantStatus bas = new BApplicantStatus();
				statusId = bas.create();
				bas.setName("Initial Status");
				bas.setConsiderForHire(true);
				bas.insert();
			} else
				statusId = stat.getApplicantStatusId();

			ApplicantAppStatus astat = hsu.createCriteria(ApplicantAppStatus.class)
					.eq(ApplicantAppStatus.SEQ, (short) 0)
					.first();
			String appStatusId;
			if (astat == null) {
				BApplicationStatus bas = new BApplicationStatus();
				appStatusId = bas.create();
				bas.setName("Initial Status");
				bas.setActive(true);
				bas.insert();
			} else
				appStatusId = astat.getApplicantAppStatusId();

			bap.setApplicantStatusId(statusId);
			bap.insert();

			//Personal Reference 1
			if (!isEmpty(referenceName)) {
				BPersonalReference bRef = new BPersonalReference();
				bRef.create();
				bRef.setPerson(bap.getPerson());

				bRef.setReferenceName(referenceName);

				if (!isEmpty(relationshipType)) {
					bRef.setRelationshipType(relationshipType.charAt(0));

					if (relationshipType.charAt(0) == 'O' && !isEmpty(relationshipOther))
						bRef.setRelationshipOther(relationshipOther);
				} else {
					bRef.setRelationshipType('O');
					bRef.setRelationshipOther("Not Specified");
				}

				if (!isEmpty(refCompany))
					bRef.setCompany(refCompany);

				if (!isEmpty(refPhone))
					bRef.setPhone(refPhone);

				if (!isEmpty(refAddress))
					bRef.setAddress(refAddress);

				if (!isEmpty(yearsKnown))
					bRef.setYearsKnown(Short.parseShort(yearsKnown));
				else
					bRef.setYearsKnown((short) 0);

				bRef.insert();
			}

			//Personal Reference 2
			if (!isEmpty(referenceName2)) {
				BPersonalReference bRef2 = new BPersonalReference();
				bRef2.create();
				bRef2.setPerson(bap.getPerson());

				bRef2.setReferenceName(referenceName2);

				if (!isEmpty(relationshipType2)) {
					bRef2.setRelationshipType(relationshipType2.charAt(0));

					if (relationshipType2.charAt(0) == 'O' && !isEmpty(relationshipOther2))
						bRef2.setRelationshipOther(relationshipOther2);
				} else {
					bRef2.setRelationshipType('O');
					bRef2.setRelationshipOther("Not Specified");
				}

				if (!isEmpty(refCompany2))
					bRef2.setCompany(refCompany2);

				if (!isEmpty(refPhone2))
					bRef2.setPhone(refPhone2);

				if (!isEmpty(refAddress2))
					bRef2.setAddress(refAddress2);

				if (!isEmpty(yearsKnown2))
					bRef2.setYearsKnown(Short.parseShort(yearsKnown2));
				else
					bRef2.setYearsKnown((short) 0);

				bRef2.insert();
			}

			//Personal Reference 3
			if (!isEmpty(referenceName3)) {
				BPersonalReference bRef3 = new BPersonalReference();
				bRef3.create();
				bRef3.setPerson(bap.getPerson());

				bRef3.setReferenceName(referenceName3);

				if (!isEmpty(relationshipType3)) {
					bRef3.setRelationshipType(relationshipType3.charAt(0));

					if (relationshipType3.charAt(0) == 'O' && !isEmpty(relationshipOther3))
						bRef3.setRelationshipOther(relationshipOther3);
				} else {
					bRef3.setRelationshipType('O');
					bRef3.setRelationshipOther("Not Specified");
				}

				if (!isEmpty(refCompany3))
					bRef3.setCompany(refCompany3);

				if (!isEmpty(refPhone3))
					bRef3.setPhone(refPhone3);

				if (!isEmpty(refAddress3))
					bRef3.setAddress(refAddress3);

				if (!isEmpty(yearsKnown3))
					bRef3.setYearsKnown(Short.parseShort(yearsKnown3));
				else
					bRef3.setYearsKnown((short) 0);

				bRef3.insert();
			}

			//Previous Employment 1
			if (!isEmpty(empCompany)) {
				BPreviousEmployment bEmp = new BPreviousEmployment();
				bEmp.create();
				bEmp.setPerson(bap.getPerson());

				bEmp.setCompany(empCompany);

				if (!isEmpty(empStartMonth) && !isEmpty(empStartYear))
					bEmp.setStartDate((Integer.parseInt(empStartYear) * 100) + Integer.parseInt(empStartMonth));

				if (!isEmpty(empEndMonth) && !isEmpty(empEndYear))
					bEmp.setEndDate((Integer.parseInt(empEndYear) * 100) + Integer.parseInt(empEndMonth));

				if (!isEmpty(empPhone))
					bEmp.setPhone(empPhone);
/*
				if (!isEmpty(empAddress))
					bEmp.setAddress(empAddress);
				else
					bEmp.setAddress("");
*/
				if (!isEmpty(supervisor))
					bEmp.setSupervisor(supervisor);

				if (!isEmpty(jobTitle))
					bEmp.setJobTitle(jobTitle);

				if (!isEmpty(startingSalary))
					bEmp.setStartingSalary(Integer.parseInt(startingSalary));

				if (!isEmpty(endingSalary))
					bEmp.setEndingSalary(Integer.parseInt(endingSalary));

				if (!isEmpty(responsibilities))
					bEmp.setResponsibilities(responsibilities);

				if (!isEmpty(reasonForLeaving))
					bEmp.setReasonForLeaving(reasonForLeaving);

				if (!isEmpty(contactSupervisor))
					bEmp.setContactSupervisor(contactSupervisor.charAt(0));

				bEmp.insert();
			}

			//Previous Employment 2
			if (!isEmpty(empCompany2)) {
				BPreviousEmployment bEmp2 = new BPreviousEmployment();
				bEmp2.create();
				bEmp2.setPerson(bap.getPerson());

				bEmp2.setCompany(empCompany2);

				if (!isEmpty(empStartMonth2) && !isEmpty(empStartYear2))
					bEmp2.setStartDate((Integer.parseInt(empStartYear2) * 100) + Integer.parseInt(empStartMonth2));

				if (!isEmpty(empEndMonth2) && !isEmpty(empEndYear2))
					bEmp2.setEndDate((Integer.parseInt(empEndYear2) * 100) + Integer.parseInt(empEndMonth2));

				if (!isEmpty(empPhone2))
					bEmp2.setPhone(empPhone2);
/*
				if (!isEmpty(empAddress2))
					bEmp2.setAddress(empAddress2);
				else
					bEmp2.setAddress("");
*/
				if (!isEmpty(supervisor2))
					bEmp2.setSupervisor(supervisor2);

				if (!isEmpty(jobTitle2))
					bEmp2.setJobTitle(jobTitle2);

				if (!isEmpty(startingSalary2))
					bEmp2.setStartingSalary(Integer.parseInt(startingSalary2));

				if (!isEmpty(endingSalary2))
					bEmp2.setEndingSalary(Integer.parseInt(endingSalary2));

				if (!isEmpty(responsibilities2))
					bEmp2.setResponsibilities(responsibilities2);

				if (!isEmpty(reasonForLeaving2))
					bEmp2.setReasonForLeaving(reasonForLeaving2);

				if (!isEmpty(contactSupervisor2))
					bEmp2.setContactSupervisor(contactSupervisor2.charAt(0));

				bEmp2.insert();
			}

			//Previous Employment 3
			if (!isEmpty(empCompany3)) {
				BPreviousEmployment bEmp3 = new BPreviousEmployment();
				bEmp3.create();
				bEmp3.setPerson(bap.getPerson());

				bEmp3.setCompany(empCompany3);

				if (!isEmpty(empStartMonth3) && !isEmpty(empStartYear3))
					bEmp3.setStartDate((Integer.parseInt(empStartYear3) * 100) + Integer.parseInt(empStartMonth3));

				if (!isEmpty(empEndMonth3) && !isEmpty(empEndYear3))
					bEmp3.setEndDate((Integer.parseInt(empEndYear3) * 100) + Integer.parseInt(empEndMonth3));

				if (!isEmpty(empPhone3))
					bEmp3.setPhone(empPhone3);
/*
				if (!isEmpty(empAddress3))
					bEmp3.setAddress(empAddress3);
				else
					bEmp3.setAddress("");
*/
				if (!isEmpty(supervisor3))
					bEmp3.setSupervisor(supervisor3);

				if (!isEmpty(jobTitle3))
					bEmp3.setJobTitle(jobTitle3);

				if (!isEmpty(startingSalary3))
					bEmp3.setStartingSalary(Integer.parseInt(startingSalary3));

				if (!isEmpty(endingSalary3))
					bEmp3.setEndingSalary(Integer.parseInt(endingSalary3));

				if (!isEmpty(responsibilities3))
					bEmp3.setResponsibilities(responsibilities3);

				if (!isEmpty(reasonForLeaving3))
					bEmp3.setReasonForLeaving(reasonForLeaving3);

				if (!isEmpty(contactSupervisor3))
					bEmp3.setContactSupervisor(contactSupervisor3.charAt(0));

				bEmp3.insert();
			}

			//Education
			if (!isEmpty(schoolName)) {
				BEducation bEd = new BEducation();
				bEd.create();
				bEd.setPerson(bap.getPerson());

				bEd.setSchoolName(schoolName);

				if (!isEmpty(schoolStartMonth) && !isEmpty(schoolStartYear))
					bEd.setStartDate((Integer.parseInt(schoolStartYear) * 100) + Integer.parseInt(schoolStartMonth));

				if (!isEmpty(schoolEndMonth) && !isEmpty(schoolEndYear))
					bEd.setEndDate((Integer.parseInt(schoolEndYear) * 100) + Integer.parseInt(schoolEndMonth));

				if (!isEmpty(schoolType)) {
					bEd.setSchoolType(schoolType.charAt(0));

					if (schoolType.charAt(0) == 'O' && !isEmpty(otherType))
						bEd.setOtherType(otherType);
				} else {
					bEd.setSchoolType('O');
					bEd.setOtherType("Not Specified");
				}

				if (!isEmpty(schoolLocation))
					bEd.setSchoolLocation(schoolLocation);
				else
					bEd.setSchoolLocation("Unknown");

				if (!isEmpty(graduate))
					bEd.setGraduate(graduate.charAt(0));
				else
					bEd.setGraduate('N');

				if (!isEmpty(subject))
					bEd.setSubject(subject);

				bEd.insert();
			}

			//Education 2
			if (!isEmpty(schoolName2)) {
				BEducation bEd2 = new BEducation();
				bEd2.create();
				bEd2.setPerson(bap.getPerson());

				bEd2.setSchoolName(schoolName2);

				if (!isEmpty(schoolStartMonth2) && !isEmpty(schoolStartYear2))
					bEd2.setStartDate((Integer.parseInt(schoolStartYear2) * 100) + Integer.parseInt(schoolStartMonth2));

				if (!isEmpty(schoolEndMonth2) && !isEmpty(schoolEndYear2))
					bEd2.setEndDate((Integer.parseInt(schoolEndYear2) * 100) + Integer.parseInt(schoolEndMonth2));

				if (!isEmpty(schoolType2)) {
					bEd2.setSchoolType(schoolType2.charAt(0));

					if (schoolType2.charAt(0) == 'O' && !isEmpty(otherType2))
						bEd2.setOtherType(otherType2);
				} else {
					bEd2.setSchoolType('O');
					bEd2.setOtherType("Not Specified");
				}

				if (!isEmpty(schoolLocation2))
					bEd2.setSchoolLocation(schoolLocation2);
				else
					bEd2.setSchoolLocation("Unknown");

				if (!isEmpty(graduate2))
					bEd2.setGraduate(graduate2.charAt(0));
				else
					bEd2.setGraduate('N');

				if (!isEmpty(subject2))
					bEd2.setSubject(subject2);

				bEd2.insert();
			}

			//Education 3
			if (!isEmpty(schoolName3)) {
				BEducation bEd3 = new BEducation();
				bEd3.create();
				bEd3.setPerson(bap.getPerson());

				bEd3.setSchoolName(schoolName3);

				if (!isEmpty(schoolStartMonth3) && !isEmpty(schoolStartYear3))
					bEd3.setStartDate((Integer.parseInt(schoolStartYear3) * 100) + Integer.parseInt(schoolStartMonth3));

				if (!isEmpty(schoolEndMonth3) && !isEmpty(schoolEndYear3))
					bEd3.setEndDate((Integer.parseInt(schoolEndYear3) * 100) + Integer.parseInt(schoolEndMonth3));

				if (!isEmpty(schoolType3)) {
					bEd3.setSchoolType(schoolType3.charAt(0));

					if (schoolType3.charAt(0) == 'O' && !isEmpty(otherType3))
						bEd3.setOtherType(otherType3);
				} else {
					bEd3.setSchoolType('O');
					bEd3.setOtherType("Not Specified");
				}

				if (!isEmpty(schoolLocation3))
					bEd3.setSchoolLocation(schoolLocation3);
				else
					bEd3.setSchoolLocation("Unknown");

				if (!isEmpty(graduate3))
					bEd3.setGraduate(graduate3.charAt(0));
				else
					bEd3.setGraduate('N');

				if (!isEmpty(subject3))
					bEd3.setSubject(subject3);

				bEd3.insert();
			}


			BApplication bapl = new BApplication();
			bapl.create();
			bapl.setApplicant(bap);
			bapl.setDate(DateUtils.now());
			bapl.setStatusId(appStatusId);
			bapl.insert();

			//to set up question for contact phone
			checkQuestion("ApplicantContactPhoneQuestId", "Contact Phone");
			setAnswer(bap, "ApplicantContactPhoneQuestId", contactPhone);
			//to set up question for where learned
			checkQuestion("ApplicantHeardPositionQuestId", "Where learned about position");
			if (whereLearned.equalsIgnoreCase("Employee"))
				setAnswer(bap, "ApplicantHeardPositionQuestId", refbyemp);
			else if (whereLearned.equalsIgnoreCase("Other"))
				setAnswer(bap, "ApplicantHeardPositionQuestId", refbyother);
			else
				setAnswer(bap, "ApplicantHeardPositionQuestId", whereLearned);

			//to set up question for veteran
			checkQuestion("ApplicantVeteranQuestionId", "Veteran");
			setAnswer(bap, "ApplicantVeteranQuestionId", veteran);

//Resume File (this is an attached document with extensions .doc, .rtf or .pdf

			///////////BEGIN FORM INSERT

			String formTypeId = BProperty.get(StandardProperty.ApplicantResumeFormTypeId);

			if (formTypeId.equals("")) {
				BFormType ft = new BFormType();

				formTypeId = ft.create();
				ft.setCode("Resume");
				ft.setDescription("Resume");
				ft.setFormType(FormType.TYPE_PERSON);
				ft.insert();

				BProperty prop = new BProperty(StandardProperty.ApplicantResumeFormTypeId);
				prop.setValue(formTypeId);
				prop.update();
			}

			String personId = bap.getPersonId();
			String comments = "Resume from Web Appliciation";
			String extension = resumeFileName.substring(resumeFileName.lastIndexOf('.') + 1);


			File fyle = new File(files.get(0).getAbsolutePath());

			byte[] formData;

			try {
				formData = new byte[(int) fyle.length()];


				FileInputStream fis = new FileInputStream(fyle);
				fis.read(formData);

				BPersonForm pf = new BPersonForm();
				pf.create();
				pf.setComments(comments);
				pf.setFormTypeId(formTypeId);
				pf.setPersonId(personId);
				pf.setFormDate(DateUtils.now());
				pf.setFormData(formData, extension);
				pf.setExtension(extension);
				pf.insert();
			} catch (Exception e) {
				//no form
			}


			////////////END FORM INSERT

			if (stat != null && stat.getSendEmail() == 'Y' && !isEmpty(stat.getEmailSource()))
				Mail.send(stat.getEmailSource(), email, stat.getEmailSubject(), stat.getEmailText());
			else //send the default email message
			{
				//send acknowledgement
				String fromEmail = BProperty.get(StandardProperty.AppRecieveFromEmailAddress);
				if (fromEmail != null && !fromEmail.trim().equals(""))
					Mail.send(fromEmail, email, "Application Received", BProperty.get(StandardProperty.ApplicationReceivedMessage));

				//TODO: Move this to a configuration
				String ret = "<HTML><BODY>Thank you for your interest in employment with " + hsu.getFirst(CompanyDetail.class).getName() + ". "
						+ "Your information has been submitted and will be reviewed. Should your qualifications best "
						+ "match our needs at this time, a representative from the Human Resources Department will be "
						+ "contacting you.</BODY></HTML>";
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.print(ret);

				out.flush();
				out.close();
			}

			ArahantSession.getHSU().commitTransaction();
		} catch (Exception e) {
			ArahantSession.getHSU().rollbackTransaction();
			logger.error(e);
			throw new ServletException(e);
		} finally {
			ArahantSession.clearSession();
		}
	}

	private void checkQuestion(String property, String question) {
		String id = BProperty.get(property);
		if (id == null || id.equals("")) {
			BApplicantQuestion baq = new BApplicantQuestion();
			baq.create();
			baq.setQuestion(question);
			baq.setInternalUse(false);
			baq.setAnswerType(ApplicantQuestion.TYPE_STRING);
			baq.insert();

			BProperty prop = new BProperty(property);
			prop.setValue(baq.getId());
			prop.update();
		}
	}

	private void setAnswer(BApplicant bap, String property, String answer) {
		String id = BProperty.get(property);
		if (id != null && !id.equals("")) {
			BApplicantAnswer bans = new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(answer);
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	private void setAnswerChoice(BApplicant bap, String property, String description) {
		String id = BProperty.get(property);

		ApplicantQuestionChoice aqc = ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
				.eq(ApplicantQuestionChoice.DESCRIPTION, description)
				.first();
		BApplicantQuestionChoice baqc = new BApplicantQuestionChoice(aqc);
		if (aqc == null) {
			baqc.create();
			baqc.setDescription(description);
			baqc.setQuestion(new BApplicantQuestion(BProperty.get(property)).getBean());
			baqc.insert();

		}
		if (id != null && !id.equals("")) {
			BApplicantAnswer bans = new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(ApplicantQuestion.TYPE_LIST + "", "", 0, baqc.getBean().getApplicantQuestionChoiceId());
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	protected boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Check login servlet";
	}// </editor-fold>
}
