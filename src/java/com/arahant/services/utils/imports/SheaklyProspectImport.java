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
package com.arahant.services.utils.imports;

import com.arahant.beans.CompanyQuestion;
import com.arahant.beans.Employee;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProspectCompany;
import com.arahant.beans.ProspectContact;
import com.arahant.beans.ProspectSource;
import com.arahant.beans.ProspectStatus;
import com.arahant.business.BCompanyQuestion;
import com.arahant.business.BCompanyQuestionDetail;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectContact;
import com.arahant.business.BProspectSource;
import com.arahant.business.BProspectStatus;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SheaklyProspectImport {

	static HashMap<String, String> countryCodes = new HashMap<String, String>();

	static {
		//http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm
		countryCodes.put("United States", "US");
		countryCodes.put("US", "US");
		countryCodes.put("Finland", "FI");
		countryCodes.put("Canada", "CA");
		countryCodes.put("Chile", "CL");
		countryCodes.put("Belgium", "BE");
		countryCodes.put("Netherlands", "NL");
		countryCodes.put("Australia", "AU");
		countryCodes.put("Mexico", "MX");
		countryCodes.put("South Korea", "KR");
		countryCodes.put("Barbados", "BB");
		countryCodes.put("Brazil", "BR");
		countryCodes.put("New Zealand", "NZ");
		countryCodes.put("Bermuda", "BM");
		countryCodes.put("Costa Rica", "CR");
	}
	//Sheakely
	private final static int COMPANY_NAME_COL = 2;
	private final static int ALT_COMPANY_NAME_COL = -1; //place to get name if main name is blank
	private final static int LAST_NAME_COL = 16;
	private final static int FIRST_NAME_COL = 15;
	private final static int MIDDLE_NAME_COL = -1;
	private final static int JOB_TITLE_COL = -1;
	private final static int WORK_PHONE_COL = 3;
	private final static int WORK_PHONE_EXT_COL = -1;
	private final static int WORK_FAX_COL = -1;
	private final static int EMAIL_COL = -1;
	private final static int STREET_COL = 17;
	private final static int STREET_2_COL = -1;
	private final static int CITY_COL = 18;
	private final static int STATE_COL = 19;
	private final static int ZIP_COL = 20;
	private final static int COUNTRY_COL = -1;
	private final static int EXT_REF = 0;
	private final static int STATUS = 5;
	private final static int EMPLOYEE_NAME_COL = 4;
	private final static int PAYROLL_FNAME = 7;
	private final static int PAYROLL_LNAME = 8;
	private final static int PAYROLL_TITLE = 9;
	private final static int PAYROLL_EMAIL = 11;
	private final static int PAYROLL_FAX = 12;
	private final static int PAYROLL_PHONE = 10;
	private static Question[] questions = new Question[]{new Question("Employee Size", 14), new Question("Industry", -1), new Question("Payroll Provider", 6), new Question("Pay Frequency", 13), new Question("Other Notes", 25)};

	private static class Question {

		public Question(String q, int p) {
			question = q;
			pos = p;
		}
		String question;
		int pos;
		String id;
	}
	private final static String sourceName = "Import File";
	private static String salesPersonId = null;

	public static final String getString(DelimitedFileReader fr, int col) {
		if (col == -1) {
			return "";
		}
		return fr.getString(col).trim();
	}

	public static String max(String s, int m) {
		if (s.length() <= m) {
			return s;
		}
		return s.substring(0, m);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void doImport(String filename) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.setCurrentPersonToArahant();
		ArahantSession.setFastKeys(true);
		try {

			for (int loop = 0; loop < questions.length; loop++) {
				CompanyQuestion q = hsu.createCriteria(CompanyQuestion.class).eq(CompanyQuestion.QUESTION, questions[loop].question).first();

				if (q == null) {
					BCompanyQuestion cq = new BCompanyQuestion();
					cq.create();
					cq.setQuestion(questions[loop].question);
					cq.setAddAfterId(null);
					cq.insert();
					q = cq.getBean();
				}

				questions[loop].id = q.getCompanyQuesId();
			}


			HashMap<String, String> statusMap = new HashMap<String, String>();

			String statusId;


			ProspectSource source = hsu.createCriteria(ProspectSource.class).eq(ProspectSource.SOURCE_CODE, sourceName).first();

			String sourceId;
			if (source != null) {
				sourceId = source.getProspectSourceId();
			} else {
				BProspectSource src = new BProspectSource();
				sourceId = src.create();
				src.setCode(sourceName);
				src.setDescription(sourceName);
				src.insert();
			}

			if (salesPersonId == null && EMPLOYEE_NAME_COL == -1) {
				BEmployee bemp = new BEmployee();
				salesPersonId = bemp.create();
				bemp.setFirstName("Import");
				bemp.setLastName("Sales");
				bemp.setSsn(null);
				bemp.insert();
			}

			HashMap<String, String> salesPersonMap = new HashMap<String, String>();

			DelimitedFileReader fr = new DelimitedFileReader(filename);

//			for (int loop=0;loop<5451;loop++)
			fr.skipLine();

			int count = 0;
			while (fr.nextLine()) {

				if (++count % 50 == 0) {
					System.out.println(count);
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				//does this prospect company exist?
				String companyName = getString(fr, COMPANY_NAME_COL);

				if (companyName.equals("")) {
					companyName = getString(fr, ALT_COMPANY_NAME_COL);
				}

				if (companyName.equals("")) {
					continue;
				}

				//		if (getString(fr,LAST_NAME_COL)==null || getString(fr,LAST_NAME_COL).equals(""))
				//			continue;
				String status = getString(fr, STATUS);

				if (statusMap.containsKey(status)) {
					statusId = statusMap.get(status);
				} else {
					ProspectStatus ps = hsu.createCriteria(ProspectStatus.class).eq(ProspectStatus.DESCRIPTION, status).first();

					if (ps == null) {
						BProspectStatus stat = new BProspectStatus();
						statusId = stat.create();
						stat.setActive("Y");
						stat.setCode(status);
						stat.setDescription(status);
						stat.setInitialSequence(-1);
						stat.insert();
					} else {
						statusId = ps.getProspectStatusId();
					}

					statusMap.put(status, statusId);
				}

				if (EMPLOYEE_NAME_COL != -1) {
					//find the sales person
					String fname;
					String lname;

					String name = getString(fr, EMPLOYEE_NAME_COL);

					if (salesPersonMap.containsKey(name)) {
						salesPersonId = salesPersonMap.get(name);
					} else {

						fname = name.substring(0, name.indexOf(' ')).trim();
						lname = name.substring(name.indexOf(' ')).trim();

						Employee emp = hsu.createCriteria(Employee.class).eq(Employee.FNAME, fname).eq(Employee.LNAME, lname).first();

						//if not found, make them
						if (emp == null) {
							BEmployee bemp = new BEmployee();
							salesPersonId = bemp.create();
							bemp.setFirstName(fname);
							bemp.setLastName(lname);
							bemp.setSsn(null);
							bemp.insert();
							emp = bemp.getEmployee();
						}

						salesPersonId = emp.getPersonId();
						salesPersonMap.put(name, salesPersonId);
					}
				}

				//	if (count>100)
				//		break;



				if (hsu.createCriteria(ProspectCompany.class).eq(ProspectCompany.NAME, companyName).exists()) {
					//company exists, if person exists, continue
					BProspectCompany pros = new BProspectCompany(hsu.createCriteria(ProspectCompany.class).eq(ProspectCompany.NAME, companyName).first());

					if (hsu.createCriteria(Person.class).eq(Person.FNAME, getString(fr, FIRST_NAME_COL)).eq(Person.LNAME, getString(fr, LAST_NAME_COL)).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, pros.getOrgGroupId()).exists()) {
						continue;
					}

					//otherwise add them
					BProspectContact contact;

					if (!getString(fr, LAST_NAME_COL).equals("")) {
						contact = new BProspectContact();
						contact.create();
						contact.setFirstName(getString(fr, FIRST_NAME_COL));
						contact.setLastName(getString(fr, LAST_NAME_COL));
						contact.setMiddleName(getString(fr, MIDDLE_NAME_COL));
						if (getString(fr, FIRST_NAME_COL).equals("")) {
							contact.setFirstName(".");
						}
						if (getString(fr, LAST_NAME_COL).equals("")) {
							contact.setLastName(".");
						}
						contact.setJobTitle(max(getString(fr, JOB_TITLE_COL), 60));
						contact.setWorkPhone((getString(fr, WORK_PHONE_COL) + " " + getString(fr, WORK_PHONE_EXT_COL)));
						contact.setWorkFax(getString(fr, WORK_FAX_COL));
						contact.setPersonalEmail(getString(fr, EMAIL_COL));
						contact.setStreet(max(getString(fr, STREET_COL), 60));
						contact.setStreet2(max(getString(fr, STREET_2_COL), 60));
						contact.setCity(getString(fr, CITY_COL));
						contact.setState(getString(fr, STATE_COL));
						contact.setZip(max(getString(fr, ZIP_COL), 10));
						contact.setCountry(countryCodes.get(getString(fr, COUNTRY_COL)));
						contact.setProspectType(ProspectContact.TYPE_UNKNOWN);
						contact.insert();

						contact.setOrgGroupId(pros.getOrgGroupId());
					}

					if (!getString(fr, PAYROLL_LNAME).equals("")) {
						contact = new BProspectContact();
						contact.create();
						contact.setFirstName(getString(fr, PAYROLL_FNAME));
						contact.setLastName(getString(fr, PAYROLL_LNAME));
						if (getString(fr, PAYROLL_FNAME).equals("")) {
							contact.setFirstName(".");
						}
						if (getString(fr, PAYROLL_LNAME).equals("")) {
							contact.setLastName(".");
						}
						contact.setJobTitle(max(getString(fr, PAYROLL_TITLE), 60));
						contact.setWorkPhone((getString(fr, PAYROLL_PHONE)));
						contact.setWorkFax(getString(fr, PAYROLL_FAX));
						contact.setPersonalEmail(getString(fr, PAYROLL_EMAIL));
						contact.setStreet(max(getString(fr, STREET_COL), 60));
						contact.setStreet2(max(getString(fr, STREET_2_COL), 60));
						contact.setCity(getString(fr, CITY_COL));
						contact.setState(getString(fr, STATE_COL));
						contact.setZip(max(getString(fr, ZIP_COL), 10));
						contact.setCountry(countryCodes.get(getString(fr, COUNTRY_COL)));
						contact.setProspectType(ProspectContact.TYPE_UNKNOWN);
						contact.insert();

						contact.setOrgGroupId(pros.getOrgGroupId());
					}

				} else {
					//add company
					BProspectCompany pros = new BProspectCompany();
					String id = pros.create();
					pros.setName(companyName);
					if (!getString(fr, LAST_NAME_COL).equals("")) {
						pros.setMainContactFname(getString(fr, FIRST_NAME_COL));
						pros.setMainContactLname(getString(fr, LAST_NAME_COL));
						if (getString(fr, FIRST_NAME_COL).equals("")) {
							pros.setMainContactFname(".");
						}
						if (getString(fr, LAST_NAME_COL).equals("")) {
							pros.setMainContactLname(".");
						}
						pros.setMainContactMname(getString(fr, MIDDLE_NAME_COL));
						pros.setMainContactJobTitle(max(getString(fr, JOB_TITLE_COL), 60));
						pros.setMainContactWorkPhone((getString(fr, WORK_PHONE_COL) + " " + getString(fr, WORK_PHONE_EXT_COL)));
						pros.setMainContactWorkFax(getString(fr, WORK_FAX_COL));
						pros.setMainContactPersonalEmail(getString(fr, EMAIL_COL));
						pros.setMainContactProspectType(ProspectContact.TYPE_UNKNOWN);
					}
					pros.setStreet(max(getString(fr, STREET_COL), 60));
					pros.setStreet2(max(getString(fr, STREET_2_COL), 60));
					pros.setCity(getString(fr, CITY_COL));
					pros.setState(getString(fr, STATE_COL));
					pros.setZip(max(getString(fr, ZIP_COL), 10));
					pros.setCountry(countryCodes.get(getString(fr, COUNTRY_COL)));
					pros.setMainPhoneNumber(max((getString(fr, WORK_PHONE_COL) + " " + getString(fr, WORK_PHONE_EXT_COL)), 20));
					pros.setMainFaxNumber(max(getString(fr, WORK_FAX_COL), 20));
					pros.setIdentifier(getString(fr, EXT_REF));
					pros.setStatusId(statusId);
					pros.setSalesPersonId(salesPersonId);
					pros.setSourceId(sourceId);

					pros.insert();

					for (int loop = 0; loop < questions.length; loop++) {
						try {
							if (getString(fr, questions[loop].pos) != null && !getString(fr, questions[loop].pos).equals("")) {
								BCompanyQuestionDetail det = new BCompanyQuestionDetail();
								det.create();
								det.setCompanyId(id);
								det.setQuestionId(questions[loop].id);
								det.setResponse(getString(fr, questions[loop].pos));
								det.insert();
							}
						} catch (Exception e) {
							//ignore
						}
					}

					if (!getString(fr, LAST_NAME_COL).equals("")) {
						//set main contact address info too
						BPerson bp = new BPerson(pros.getMainContactPersonId());
						bp.setStreet(max(getString(fr, STREET_COL), 60));
						bp.setStreet2(max(getString(fr, STREET_2_COL), 60));
						bp.setCity(getString(fr, CITY_COL));
						bp.setState(getString(fr, STATE_COL));
						bp.setZip(max(getString(fr, ZIP_COL), 10));
						bp.setCountry(countryCodes.get(getString(fr, COUNTRY_COL)));
						bp.update();
					}

					if (!getString(fr, PAYROLL_LNAME).equals("")) {
						BProspectContact contact = new BProspectContact();
						contact.create();
						contact.setFirstName(getString(fr, PAYROLL_FNAME));
						contact.setLastName(getString(fr, PAYROLL_LNAME));
						if (getString(fr, FIRST_NAME_COL).equals("")) {
							pros.setMainContactFname(".");
						}
						if (getString(fr, LAST_NAME_COL).equals("")) {
							pros.setMainContactLname(".");
						}
						contact.setJobTitle(max(getString(fr, PAYROLL_TITLE), 60));
						contact.setWorkPhone(max(getString(fr, PAYROLL_PHONE), 20));
						contact.setWorkFax(max(getString(fr, PAYROLL_FAX), 20));
						contact.setPersonalEmail(getString(fr, PAYROLL_EMAIL));
						contact.setStreet(max(getString(fr, STREET_COL), 60));
						contact.setStreet2(max(getString(fr, STREET_2_COL), 60));
						contact.setCity(getString(fr, CITY_COL));
						contact.setState(getString(fr, STATE_COL));
						contact.setZip(max(getString(fr, ZIP_COL), 10));
						contact.setCountry(countryCodes.get(getString(fr, COUNTRY_COL)));
						contact.setProspectType(ProspectContact.TYPE_UNKNOWN);
						contact.insert();

						contact.setOrgGroupId(pros.getOrgGroupId());
					}

				}
			}
			//hsu.commitTransaction();  //For Testing File Upload Servlet commits transaction
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(SheaklyProspectImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {

		SheaklyProspectImport.doImport("/home/brad/Desktop/Opportunities.csv");

	}
}
