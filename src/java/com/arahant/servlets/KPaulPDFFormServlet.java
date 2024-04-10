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

import com.arahant.beans.ApplicantAppStatus;
import com.arahant.beans.ProphetLogin;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantSource;
import com.arahant.business.BApplicantStatus;
import com.arahant.business.BApplication;
import com.arahant.business.BApplicationStatus;
import com.arahant.business.BEducation;
import com.arahant.business.BFormType;
import com.arahant.business.BPersonalReference;
import com.arahant.business.BPreviousEmployment;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.standard.hr.w4.PDFBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Process W4 PDF form update Employee personal and W4 form info upload sealed
 * PDF to database
 *
 *
 */
public class KPaulPDFFormServlet extends HttpServlet {

	private static final ArahantLogger logger = new ArahantLogger(KPaulPDFFormServlet.class);
	private static final long serialVersionUID = 1L;
	private String applicantId = "";

	private File createSignedPDFFolder(String prefix) {
		try {
			//create the directory to put the filled in PDF files
			File reportDir = new File(FileSystemUtils.getWorkingDirectory(), PDF_OutputDirectory);
			if (!reportDir.exists())
				reportDir.mkdir();
			//create the output PDF
			if (prefix.length() == 0)
				prefix = "signedPDF";
			File outputPDF = File.createTempFile(prefix, ".pdf", reportDir);
			return outputPDF;
		} catch (IOException ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private int getDate(String date) {
		int intdate;
		String dateSplit[] = date.split("/");
		if (dateSplit.length < 2)
			return 0; //invalid date
		String date1 = dateSplit[0];
		String date2 = dateSplit[1];
		if (date1.length() == 1)
			date1 = "0" + date1;//make it two digit month
		intdate = Integer.parseInt(date2 + date1);
		return intdate;
	}

	private void createEducation(AcroFields pdfFormFields, BApplicant emp) throws ArahantException {

		try {
			//check if employee attended any schools
			if (pdfFormFields.getField("fld_education_HS").trim().length() > 0) {
				BEducation edu = new BEducation();
				edu.create();
				edu.setSchoolName(pdfFormFields.getField("fld_education_HS"));
				edu.setSchoolLocation(pdfFormFields.getField("fld_education_HSAddress"));
				edu.setStartDate(getDate(pdfFormFields.getField("fld_education_HSFrom")));
				edu.setEndDate(getDate(pdfFormFields.getField("fld_education_HSTo")));
				edu.setGraduate(pdfFormFields.getField("fld_education_HSGraduatedYes").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				edu.setSubject(pdfFormFields.getField("fld_education_HSDegree"));
				edu.setSchoolType('H');
				edu.setPerson(emp.getPerson());
				edu.insert();
				ArahantSession.getHSU().flush();
			}

			if (pdfFormFields.getField("fld_education_College").trim().length() > 0) {
				char schoolType;
				if (pdfFormFields.getField("fld_education_CollegeDegree").toLowerCase().contains("bachelor"))
					schoolType = 'U';
				else
					schoolType = 'G';
				BEducation edu = new BEducation();
				edu.create();
				edu.setSchoolName(pdfFormFields.getField("fld_education_College"));
				edu.setSchoolLocation(pdfFormFields.getField("fld_education_CollegeAddress"));
				edu.setStartDate(getDate(pdfFormFields.getField("fld_education_CollegeFrom")));
				edu.setEndDate(getDate(pdfFormFields.getField("fld_education_CollegeTo")));
				edu.setGraduate(pdfFormFields.getField("fld_education_CollegeGraduatedYes").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				edu.setSubject(pdfFormFields.getField("fld_education_CollegeDegree"));
				edu.setSchoolType(schoolType);
				edu.setPerson(emp.getPerson());
				edu.insert();
				ArahantSession.getHSU().flush();
			}

			if (pdfFormFields.getField("fld_education_Other").trim().length() > 0) {
				BEducation edu = new BEducation();
				edu.create();
				edu.setSchoolName(pdfFormFields.getField("fld_education_Other"));
				edu.setSchoolLocation(pdfFormFields.getField("fld_education_OtherAddress"));
				edu.setStartDate(getDate(pdfFormFields.getField("fld_education_OtherFrom")));
				edu.setEndDate(getDate(pdfFormFields.getField("fld_education_OtherTo")));
				edu.setGraduate(pdfFormFields.getField("fld_education_OtherGraduatedYes").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				edu.setSubject(pdfFormFields.getField("fld_education_OtherDegree"));
				edu.setSchoolType('O');
				edu.setPerson(emp.getPerson());
				edu.insert();
				ArahantSession.getHSU().flush();
			}
		} catch (Exception e) {
			throw new ArahantException("Error: [" + e.getMessage() + "] Please make sure all fields in the Education section were entered correctly.");
		}
	}

	private void createPreviousEmployment(AcroFields pdfFormFields, BApplicant emp) throws ArahantException {
		try {
			if (pdfFormFields.getField("fld_previousEmployment_Company_1").trim().length() > 0) {
				BPreviousEmployment prev = new BPreviousEmployment();
				prev.create();
				prev.setPerson(emp.getPerson());
				prev.setCompany(pdfFormFields.getField("fld_previousEmployment_Company_1"));
				prev.setPhone(pdfFormFields.getField("fld_previousEmployment_area1") + pdfFormFields.getField("fld_previousEmployment_Phone_1"));
				//prev.setAddress(pdfFormFields.getField("fld_previousEmployment_Address_1"));
				prev.setSupervisor(pdfFormFields.getField("fld_previousEmployment_Supervisor1"));
				prev.setJobTitle(pdfFormFields.getField("fld_previousEmployment_JobTitle1"));
				prev.setStartingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_StartingSalary1")));
				prev.setEndingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_EndingSalary1")));
				prev.setResponsibilities(pdfFormFields.getField("fld_previousEmployment_Responsibilities1"));
				prev.setStartDate(getDate(pdfFormFields.getField("fld_previousEmploymentFrom1")));
				prev.setEndDate(getDate(pdfFormFields.getField("fld_previousEmployment_To1")));
				prev.setContactSupervisor(pdfFormFields.getField("fld_previousEmployment_MayWeContactYes1").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				prev.setReasonForLeaving(pdfFormFields.getField("fld_previousEmployment_Reason_for_Leaving1"));
				prev.insert();
				ArahantSession.getHSU().flush();
			}
			if (pdfFormFields.getField("fld_previousEmployment_Company_2").trim().length() > 0) {

				BPreviousEmployment prev = new BPreviousEmployment();
				prev.create();
				prev.setPerson(emp.getPerson());
				prev.setCompany(pdfFormFields.getField("fld_previousEmployment_Company_2"));
				prev.setPhone(pdfFormFields.getField("fld_previousEmployment_area2") + pdfFormFields.getField("fld_previousEmployment_Phone_2"));
				//prev.setAddress(pdfFormFields.getField("fld_previousEmployment_Address_2"));
				prev.setSupervisor(pdfFormFields.getField("fld_previousEmployment_Supervisor2"));
				prev.setJobTitle(pdfFormFields.getField("fld_previousEmployment_JobTitle2"));
				prev.setStartingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_StartingSalary2")));
				prev.setEndingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_EndingSalary2")));
				prev.setResponsibilities(pdfFormFields.getField("fld_previousEmployment_Responsibilities2"));
				prev.setStartDate(getDate(pdfFormFields.getField("fld_previousEmploymentFrom2")));
				prev.setEndDate(getDate(pdfFormFields.getField("fld_previousEmployment_To2")));
				prev.setContactSupervisor(pdfFormFields.getField("fld_previousEmployment_MayWeContactYes2").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				prev.setReasonForLeaving(pdfFormFields.getField("fld_previousEmployment_Reason_for_Leaving2"));
				prev.insert();
				ArahantSession.getHSU().flush();

			}

			if (pdfFormFields.getField("fld_previousEmployment_Company_3").trim().length() > 0) {

				BPreviousEmployment prev = new BPreviousEmployment();
				prev.create();
				prev.setPerson(emp.getPerson());
				prev.setCompany(pdfFormFields.getField("fld_previousEmployment_Company_3"));
				prev.setPhone(pdfFormFields.getField("fld_previousEmployment_area3") + pdfFormFields.getField("fld_previousEmployment_Phone_3"));
				//prev.setAddress(pdfFormFields.getField("fld_previousEmployment_Address_3"));
				prev.setSupervisor(pdfFormFields.getField("fld_previousEmployment_Supervisor3"));
				prev.setJobTitle(pdfFormFields.getField("fld_previousEmployment_JobTitle3"));
				prev.setStartingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_StartingSalary3")));
				prev.setEndingSalary(Integer.parseInt(pdfFormFields.getField("fld_previousEmployment_EndingSalary3")));
				prev.setResponsibilities(pdfFormFields.getField("fld_previousEmployment_Responsibilities3"));
				prev.setStartDate(getDate(pdfFormFields.getField("fld_previousEmploymentFrom3")));
				prev.setEndDate(getDate(pdfFormFields.getField("fld_previousEmployment_To3")));
				prev.setContactSupervisor(pdfFormFields.getField("fld_previousEmployment_MayWeContactYes3").equalsIgnoreCase("Yes") ? 'Y' : 'N');
				prev.setReasonForLeaving(pdfFormFields.getField("fld_previousEmployment_Reason_for_Leaving3"));
				prev.insert();
				ArahantSession.getHSU().flush();

			}
		} catch (Exception e) {
			throw new ArahantException("Error: [" + e.getMessage() + "] Please make sure all fields in the Previous Employment section were entered correctly.");
		}
	}

	private char determineReferenceRelationship(String relationship) {
		char relation = 'O';
		if (relationship.toLowerCase().contains("worker"))
			return 'C';
		if (relationship.toLowerCase().contains("relative"))
			return 'R';
		if (relationship.toLowerCase().contains("friend"))
			return 'F';
		if (relationship.toLowerCase().contains("teacher"))
			return 'T';
		if (relationship.toLowerCase().contains("supervisor"))
			return 'S';
		return relation;
	}

	private void createPreferences(AcroFields pdfFormFields, BApplicant emp) throws ArahantException {
		try {
			if (pdfFormFields.getField("fld_ref_Full_Name").trim().length() > 0) {

				BPersonalReference ref = new BPersonalReference();
				ref.create();
				ref.setPerson(emp.getPerson());
				ref.setReferenceName(pdfFormFields.getField("fld_ref_Full_Name"));
				ref.setRelationshipType(determineReferenceRelationship(pdfFormFields.getField("fld_ref_Relationship")));
				ref.setCompany(pdfFormFields.getField("fld_ref_Company"));
				ref.setPhone(pdfFormFields.getField("fld_ref_area1") + pdfFormFields.getField("fld_ref_Phone_1"));
				ref.setAddress(pdfFormFields.getField("fld_ref_Address"));
				String yearsKnown = pdfFormFields.getField("fld_ref_Years_Known") + "";
				if (!yearsKnown.equalsIgnoreCase(""))
					ref.setYearsKnown(Short.parseShort(pdfFormFields.getField("fld_ref_Years_Known")));
				ref.insert();
				ArahantSession.getHSU().flush();

			}

			if (pdfFormFields.getField("fld_ref_Full_Name_2").trim().length() > 0) {

				BPersonalReference ref = new BPersonalReference();
				ref.create();
				ref.setPerson(emp.getPerson());
				ref.setReferenceName(pdfFormFields.getField("fld_ref_Full_Name_2"));
				ref.setRelationshipType(determineReferenceRelationship(pdfFormFields.getField("fld_ref_Relationship_2")));
				ref.setCompany(pdfFormFields.getField("fld_ref_Company_2"));
				ref.setPhone(pdfFormFields.getField("fld_ref_area2") + pdfFormFields.getField("fld_ref_Phone_2"));
				ref.setAddress(pdfFormFields.getField("fld_ref_Address_2"));
				String yearsKnown = pdfFormFields.getField("fld_ref_Years_Known_2") + "";
				if (!yearsKnown.equalsIgnoreCase(""))
					ref.setYearsKnown(Short.parseShort(pdfFormFields.getField("fld_ref_Years_Known_2")));
				ref.insert();
				ArahantSession.getHSU().flush();

			}
			if (pdfFormFields.getField("fld_ref_Full_Name_3").trim().length() > 0) {

				BPersonalReference ref = new BPersonalReference();
				ref.create();
				ref.setPerson(emp.getPerson());
				ref.setReferenceName(pdfFormFields.getField("fld_ref_Full_Name_3"));
				ref.setRelationshipType(determineReferenceRelationship(pdfFormFields.getField("fld_ref_Relationship_3")));
				ref.setCompany(pdfFormFields.getField("fld_ref_Company_3"));
				ref.setPhone(pdfFormFields.getField("fld_ref_area3") + pdfFormFields.getField("fld_ref_Phone_3"));
				ref.setAddress(pdfFormFields.getField("fld_ref_Address_3"));
				String yearsKnown = pdfFormFields.getField("fld_ref_Years_Known_3") + "";
				if (!yearsKnown.equalsIgnoreCase(""))
					ref.setYearsKnown(Short.parseShort(pdfFormFields.getField("fld_ref_Years_Known_3")));
				ref.insert();
				ArahantSession.getHSU().flush();

			}
		} catch (Exception e) {
			throw new ArahantException("Error: [" + e.getMessage() + "] Please make sure all fields in the References section were entered correctly.");
		}
	}

	private char determineMilitaryBranch(String branch) {
		char cBranch = 'U';
		if (branch.toLowerCase().contains("army"))
			return 'A';

		if (branch.toLowerCase().contains("air"))
			return 'F';
		if (branch.toLowerCase().contains("navy"))
			return 'N';
		if (branch.toLowerCase().contains("marine"))
			return 'M';
		if (branch.toLowerCase().contains("coast"))
			return 'C';
		if (branch.toLowerCase().contains("national"))
			return 'G';
		return cBranch;
	}

	private char determineDischarge(String type) {
		char cDischarge = 'U';
		if (type.toLowerCase().contains("honorable"))
			return 'H';
		if (type.toLowerCase().contains("general"))
			return 'G';
		if (type.toLowerCase().contains("other"))
			return 'O';
		if (type.toLowerCase().contains("bad"))
			return 'B';
		if (type.toLowerCase().contains("dishonorable"))
			return 'D';
		return cDischarge;
	}

	private void createNewEmployee(AcroFields pdfFormFields) throws ArahantException {
		applicantId = "";
		try {
			BApplicant emp = new BApplicant();
			emp.create();
			emp.setFirstName(pdfFormFields.getField("fld_applicant_firstname"));
			emp.setLastName(pdfFormFields.getField("fld_applicant_lastname"));
			emp.setMiddleName(pdfFormFields.getField("fld_applicant_middle"));
			emp.setPersonalEmail(pdfFormFields.getField("fld_applicant_email"));
			String ssn = pdfFormFields.getField("fld_applicant_ssn");
			String ssn1 = ssn.substring(0, 3);
			String ssn2 = ssn.substring(3, 5);
			String ssn3 = ssn.substring(5);
			emp.setSsn(ssn1 + "-" + ssn2 + "-" + ssn3);

			emp.setStreet(pdfFormFields.getField("fld_applicant_street") + " " + pdfFormFields.getField("fld_applicant_apartment"));
			emp.setCity(pdfFormFields.getField("fld_applicant_city"));
			emp.setState(pdfFormFields.getField("fld_applicant_state"));
			emp.setZip(pdfFormFields.getField("fld_applicant_zip"));
			emp.setHomePhone(pdfFormFields.getField("fld_applicant_phone"));
			emp.setRecordType('R');

			//MILITARY
			emp.setMilitaryBranch(determineMilitaryBranch(pdfFormFields.getField("fld_military_Branch")));
			emp.setMilitaryDischargeType(determineDischarge(pdfFormFields.getField("fld_military_Type of Discharge")));
			emp.setMilitaryDischargeExplain(pdfFormFields.getField("fld_military_explainDischarge"));
			emp.setMilitaryRank(pdfFormFields.getField("fld_military_Rank at Discharge"));
			emp.setMilitaryStartDate(getDate(pdfFormFields.getField("fld_military_From")));
			emp.setMilitaryStartDate(getDate(pdfFormFields.getField("fld_military_To")));

			//CRIME
			emp.setConvictedOfCrime(pdfFormFields.getField("fld_ConvictedYes").equalsIgnoreCase("Yes") ? 'Y' : 'N');
			emp.setConvictedOfWhat(pdfFormFields.getField("fldExplainCrime"));

			emp.setCitizenship(pdfFormFields.getField("fld_applicant_citizenYes").equalsIgnoreCase("Yes") ? "US" : "U");

			emp.setWorkedForCompanyBefore(pdfFormFields.getField("fld_applicant_previousWorkedForCompanyYes").equalsIgnoreCase("Yes") ? 'Y' : 'N');
			emp.setWorkedForCompanyWhen(pdfFormFields.getField("fld_applicant_previousWorkedForCompanyYesWhen"));

			emp.setDateAvailable(DateUtils.getDate(pdfFormFields.getField("fld_applicant_dateavailable")));

			String salary = pdfFormFields.getField("fld_applicant_salary");
			if (!salary.equals(""))
				emp.setDesiredSalary(Integer.parseInt(salary));

			emp.setApplicantSourceId(BApplicantSource.findOrMake("Internet Application"));
			emp.setApplicantStatusId(BApplicantStatus.findOrMake("New Internet Applicant"));

			emp.insert();

			//Create application for this applicant
			BApplication bapl = new BApplication();
			bapl.create();
			bapl.setApplicant(emp);
			bapl.setDate(DateUtils.now());
			bapl.setStatusId(getApplicationStatusId());

			bapl.insert();
			applicantId = emp.getPersonId();
			ArahantSession.getHSU().flush();

			createEducation(pdfFormFields, emp);
			createPreferences(pdfFormFields, emp);
			createPreviousEmployment(pdfFormFields, emp);
		} catch (Exception e) {
			throw new ArahantException(e.getMessage() + " Please make sure all fields in the Applicant Information section were entered correctly.");
		}
	}

	private String getApplicationStatusId() {
		ApplicantAppStatus astat = ArahantSession.getHSU().createCriteria(ApplicantAppStatus.class).eq(ApplicantAppStatus.SEQ, (short) 0).first();
		String appStatusId;
		if (astat == null) {
			BApplicationStatus bas = new BApplicationStatus();
			appStatusId = bas.create();
			bas.setName("Initial Status");
			bas.setActive(true);
			bas.insert();
		} else
			appStatusId = astat.getApplicantAppStatusId();
		return appStatusId;
	}

	private ProphetLogin getLogin() {
		return ArahantSession.getHSU().createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, ArahantSession.systemName()).first();
	}

	private void uploadFileToServer(File file, AcroFields pdfFormFields) {
		try {
			//save the pdf file to database
			ProphetLogin login = getLogin();
			FileUploadServlet fs = new FileUploadServlet();
			String user = login.getUserLogin();// pdfFormFields.getField("fld_user");
			String password = login.getUserPassword();// pdfFormFields.getField("fld_password");
			String personId = applicantId;//pdfFormFields.getField("fld_employeeId");
			String comment = "Electronically signed KPaul PDF application form";
			String extension = "pdf";
			String formTypeId = BFormType.findOrMake("SignPDF", comment);//pdfFormFields.getField("fld_formTypeId");

			HashMap<String, String> nameValuePairs = new HashMap<String, String>();
			nameValuePairs.put("formTypeId", formTypeId);
			nameValuePairs.put("personId", personId);
			nameValuePairs.put("comments", comment);
			nameValuePairs.put("extension", extension);

			List<File> pdfFile = new ArrayList<File>();
			pdfFile.add(file);
			fs.processUploadRequest("hrFormUpload", user, password, nameValuePairs, pdfFile, null, null);
			//delete the file
			file.delete();
		} catch (Exception ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void sealPDF(String inputFilename, PrintWriter out) {
		try {
			//basically load the PDF and setFormFlattening(true);
			PdfReader rdr = new PdfReader(inputFilename);
			AcroFields pdfFormFields = rdr.getAcroFields();
			String prefix = pdfFormFields.getField("fld_applicant_lastname") + pdfFormFields.getField("fld_applicant_firstname") + pdfFormFields.getField("fld_applicant_ssn") + "_";
			File outputPDF = createSignedPDFFolder(prefix);
			PdfStamper stamp = new PdfStamper(rdr, new FileOutputStream(outputPDF));
			pdfFormFields.removeField("Submit");
			stamp.setFormFlattening(true);//this seals it            
			stamp.close();
			File fileToDelete = new File(inputFilename);
			fileToDelete.delete();

			try {
				ArahantSession.getHSU().beginTransaction();
				createNewEmployee(pdfFormFields);

				ArahantSession.getHSU().commitTransaction();
				uploadFileToServer(outputPDF, pdfFormFields);
				out.println("Thank you for your interest in employment with KPaul. Your information has been submitted and will be reviewed. Should your qualifications best match our needs at this time, a representative from the Human Resources Department will be contacting you..");
			} catch (Exception e) {
				logger.error(e);
				ArahantSession.getHSU().rollbackTransaction();
				out.println("[Error: " + e.getMessage() + "]\n Please make sure you have properly filled in the correct data and resubmit the form again.");
			}
		} catch (DocumentException ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private String getURL(String url) {
		//get everything up to the last slash
		int pos = url.lastIndexOf("/");
		return url.substring(0, pos + 1);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArahantSession.openHSU();
		String jobId = request.getParameter("jobid");
		if (jobId != null) {
			getPDFForm(request, response, jobId);
			ArahantSession.clearSession();
			return;
		}
		File pdfOut = createSignedPDFFolder("");
		//Here request is the reference of HttpServletRequest.
		BufferedInputStream is = new BufferedInputStream(request.getInputStream());
		OutputStream os = new FileOutputStream(pdfOut);
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1)
			os.write(buffer, 0, bytesRead);
		is.close();
		os.close();

		//return response
		response.setContentType("text/html"); // Code 1
		PrintWriter out = response.getWriter();

		sealPDF(pdfOut.getAbsolutePath(), out);
		ArahantSession.clearSession();
	}
	PdfReader reader = null;
	public String filename;
	private String PDF_OutputDirectory = "reports";
	public String PDF_FORM_TEMPLATE_DIR = "/KPaulCustomPDF/";

	public PdfStamper stampIt(String pdfInputFilename, boolean allowEdit) {
		try {
			reader = null;
			reader = new PdfReader(PDF_FORM_TEMPLATE_DIR + pdfInputFilename);

			//create the directory to put the filled in PDF files
			File reportDir = new File(FileSystemUtils.getWorkingDirectory(), PDF_OutputDirectory);
			if (!reportDir.exists())
				reportDir.mkdir();

			//create the output PDF
			File outputPDF = File.createTempFile(pdfInputFilename, ".pdf", reportDir);
			this.filename = outputPDF.getName();

			if (!allowEdit) {
				PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputPDF));
				return stamp;
			} else {
				PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputPDF), '\0', true);
				return stamp;
			}

		} catch (DocumentException ex) {
			Logger.getLogger(PDFBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(PDFBase.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private void getPDFForm(HttpServletRequest request, HttpServletResponse response, String jobId) {
		try {
			String url = request.getRequestURL().toString();
			//System.out.println("Job " + position);

			PdfStamper stamp = stampIt("kpauljs.pdf", true);
			AcroFields pdfFormFields = stamp.getAcroFields();
			pdfFormFields.setField("fld_jobid", jobId);
			pdfFormFields.setField("fld_url", url);
			stamp.close();
			response.sendRedirect(getURL(url) + PDF_OutputDirectory + "/" + filename);
		} catch (DocumentException ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(KPaulPDFFormServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static void main(String[] args) {
		KPaulPDFFormServlet pdf = new KPaulPDFFormServlet();
		File f = new File("test.pdf");
		AcroFields pdfFormFields = null;
		pdf.uploadFileToServer(f, pdfFormFields);
	}
}
/**
 * Field fld_ref_area1 value = Field fld_education_CollegeAddress value = Field
 * fld_ref_Company value = Field fld_ref_Relationship_3 value = Field
 * fld_ref_Relationship_2 value = Field fld_ref_area2 value = Field
 * fld_military_Type of Discharge value = Field fld_education_CollegeGraduatedNo
 * value = Field fld_applicant_salary value = Field fld_ref_Address_1 value =
 * Field fld_ref_Address_2 value = Field fld_ref_Address_3 value = Field
 * fld_applicant_previousWorkedForCompanyYes value = Field fld_applicant_zip
 * value = Field fld_previousEmployment_Supervisor2 value = Field
 * fld_ref_Phone_4 value = Field fld_previousEmployment_Supervisor1 value =
 * Field fld_ref_Phone_3 value = Field fld_ref_Phone_2 value = Field
 * fld_previousEmployment_EndingSalary1 value = Field
 * fld_previousEmployment_EndingSalary3 value = Field
 * fld_previousEmployment_EndingSalary2 value = Field fld_education_OtherTo
 * value = Field fld_previousEmployment_Supervisor3 value = Field
 * fld_applicant_applyfor value = Field fld_previousEmployment_StartingSalary3
 * value = Field fld_previousEmployment_StartingSalary1 value = Field
 * fld_previousEmployment_StartingSalary2 value = Field fld_applicant_apartment
 * value = Field fld_military_explainDischarge value = Field fldExplainCrime
 * value = Field fld_applicant_date value = Field fld_applicant_authorizedYes
 * value = Field fld_applicant_citizenNo value = Field fld_ref_Years_Known_2
 * value = Field fld_ref_Years_Known_3 value = Field
 * fld_previousEmployment_Address_2 value = Field
 * fld_previousEmployment_Address_3 value = Field
 * fld_previousEmployment_Responsibilities3 value = Field fld_applicant_city
 * value = Field fld_education_HS value = Field
 * fld_previousEmployment_Responsibilities1 value = Field
 * fld_previousEmployment_Responsibilities2 value = Field
 * fld_previousEmployment_Address_1 value = Field fld_education_CollegeDegree
 * value = Field fld_education_HSGraduatedYes value = Field
 * fld_applicant_previousWorkedForCompanyYesWhen value = Field
 * fld_education_OtherGraduatedYes value = Field
 * fld_previousEmployment_Reason_for_Leaving1 value = Field
 * fld_previousEmployment_Reason_for_Leaving2 value = Field
 * fld_previousEmployment_Reason_for_Leaving3 value = Field fld_military_To
 * value = Field fld_military_Branch value = Field fld_ref_Full Name_3 value =
 * Field fld_ref_Full Name_2 value = Field fld_education_OtherDegree value =
 * Field fld_ref_Years_Known value = Field fld_ConvictedNo value = Field
 * fld_education_CollegeFrom value = Field fld_education_OtherGraduatedNo value
 * = Field fld_previousEmployment_To2 value = Field fld_previousEmployment_To3
 * value = Field fld_previousEmployment_area1 value = Field
 * fld_previousEmployment_area2 value = Field fld_previousEmployment_area3 value
 * = Field fld_previousEmployment_To1 value = Field fld_education_College value
 * = Field fld_applicant_lastname value = Field
 * fld_previousEmployment_MayWeContactYes1 value = Field fld_education_HSFrom
 * value = Field fld_applicant_dateavailable value = Field fld_applicant_state
 * value = Field fld_previousEmployment_MayWeContactYes2 value = Field
 * fld_previousEmployment_MayWeContactYes3 value = Field
 * fld_education_CollegeGraduatedYes value = Field fld_applicant_firstname value
 * = Field fld_education_HSDegree value = Field fld_education_HSGraduatedNo
 * value = Field fld_education_OtherFrom value = Field fld_applicant_ssn value =
 * Field fld_applicant_street value = Field fld_education_HSTo value = Field
 * Submit value = Field fld_education_OtherAddress value = Field Signature value
 * = Field fld_applicant_previousWorkedForCompanyNo value = Field
 * fld_ref_Full_Name value = Field fld_applicant_middle value = Field
 * fld_ref_Company_3 value = Field fld_applicant_email value = Field
 * fld_ref_Company_2 value = Field fld_military_Rank at Discharge value = Field
 * fld_ref_Relationship value = Field fld_applicant_authorizedNo value = Field
 * fld_previousEmploymentFrom1 value = Field fld_previousEmploymentFrom3 value =
 * Field fld_previousEmploymentFrom2 value = Field fld_ConvictedYes value =
 * Field fld_previousEmployment_MayWeContactNo3 value = Field
 * fld_previousEmployment_MayWeContactNo2 value = Field
 * fld_previousEmployment_MayWeContactNo1 value = Field fld_military_From value
 * = Field fld_previousEmployment_JobTitle2 value = Field fld_education_Other
 * value = Field fld_previousEmployment_JobTitle3 value = Field
 * fld_applicant_citizenYes value = Field fld_previousEmployment_Phone_3 value =
 * Field fld_previousEmployment_Phone_2 value = Field
 * fld_previousEmployment_Phone_1 value = Field fld_education_CollegeTo value =
 * Field fld_previousEmployment_JobTitle1 value = Field fld_education_HSAddress
 * value = Field fld_applicant_phone value = Field
 * fld_previousEmployment_Company_3 value = Field
 * fld_previousEmployment_Company_1 value = Field
 * fld_previousEmployment_Company_2 value =
 */
