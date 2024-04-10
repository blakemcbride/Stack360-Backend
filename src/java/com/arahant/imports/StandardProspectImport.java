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


package com.arahant.imports;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StandardProspectImport {

	private static final HashMap<String, String> countryCodes = new HashMap<String, String>();
	private static final HashMap<String, Integer> colMap = new HashMap<String, Integer>();

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

	private static void instantiateColMap() {
		colMap.clear();
		colMap.put("Company Name", -1);
		colMap.put("Web Site/URL", -1);
		colMap.put("HQ Address 1", -1);
		colMap.put("HQ City", -1);
		colMap.put("HQ State/Prov/Reg", -1);
		colMap.put("HQ Zip", -1);
		colMap.put("HQ Country Code", -1);
		colMap.put("GEO City", -1);
		colMap.put("GEO State/Prov/Reg", -1);
		colMap.put("GEO Country", -1);
		colMap.put("Parent Company", -1);
		colMap.put("Phone", -1);
		colMap.put("Industry", -1);
		colMap.put("SIC Code", -1);
		colMap.put("Search Engine", -1);
		colMap.put("Search Phrase", -1);
		colMap.put("Landing Page", -1);
		colMap.put("Return Visit", -1);
		colMap.put("Date / Time", -1);
		colMap.put("Pages", -1);
		colMap.put("Target 1", -1);
		colMap.put("Target 2", -1);
		colMap.put("Hot Lead", -1);
		colMap.put("Contact Last Name", -1);
		colMap.put("Contact First Name", -1);
		colMap.put("Contact Middle Name", -1);
		colMap.put("Contact Job Title", -1);
		colMap.put("Contact E-mail", -1);
		colMap.put("Company Size", -1);
		colMap.put("Revenue", -1);
		colMap.put("Extension", -1);
		colMap.put("Fax Number", -1);
		colMap.put("Apartment/Apt./Suite/Ste.", -1);
	}

	private static int COMPANY_NAME_COL() {
		return colMap.get("Company Name");
	}

	private static int COMPANY_WEB_SITE() {
		return colMap.get("Web Site/URL");
	}

	private static int STREET_COL() {
		return colMap.get("HQ Address 1");
	}

	private static int CITY_COL() {
		return colMap.get("HQ City");
	}

	private static int STATE_COL() {
		return colMap.get("HQ State/Prov/Reg");
	}

	private static int ZIP_COL() {
		return colMap.get("HQ Zip");
	}

	private static int COUNTRY_COL() {
		return colMap.get("HQ Country Code");
	}

	private static int GEO_CITY() {
		return colMap.get("GEO City");
	}

	private static int GEO_STATE() {
		return colMap.get("GEO State/Prov/Reg");
	}

	private static int GEO_COUNTRY() {
		return colMap.get("GEO Country");
	}

	private static int PARENT_COMPANY_NAME_COL() {
		return colMap.get("Parent Company");
	}

	private static int WORK_PHONE_COL() {
		return colMap.get("Phone");
	}

	private static int INDUSTRY_COL() {
		return colMap.get("Industry");
	}

	private static int SIC() {
		return colMap.get("SIC Code");
	}

	private static int SEARCH_ENGINE() {
		return colMap.get("Search Engine");
	}

	private static int SEARCH_PHRASE() {
		return colMap.get("Search Phrase");
	}

	private static int LANDING_PAGE() {
		return colMap.get("Landing Page");
	}

	private static int RETURN_VISIT() {
		return colMap.get("Return Visit");
	}

	private static int DATE_TIME() {
		return colMap.get("Date / Time");
	}

	private static int PAGES() {
		return colMap.get("Pages");
	}

	private static int TARGET_1() {
		return colMap.get("Target 1");
	}

	private static int TARGET_2() {
		return colMap.get("Target 2");
	}

	private static int HOT_LEAD() {
		return colMap.get("Hot Lead");
	}

	private static int LAST_NAME_COL() {
		return colMap.get("Contact Last Name");
	}

	private static int FIRST_NAME_COL() {
		return colMap.get("Contact First Name");
	}

	private static int MIDDLE_NAME_COL() {
		return colMap.get("Contact Middle Name");
	}

	private static int JOB_TITLE_COL() {
		return colMap.get("Contact Job Title");
	}

	private static int EMAIL_COL() {
		return colMap.get("Contact E-mail");
	}

	private static int COMPANY_SIZE_COL() {
		return colMap.get("Company Size");
	}

	private static int REVENUE_COL() {
		return colMap.get("Revenue");
	}

	private static int WORK_PHONE_EXT_COL() {
		return -1;
	}

	private static int WORK_FAX_COL() {
		return -1;
	}

	private static int STREET_2_COL() {
		return -1;
	}

	private static int EMPLOYEE_NAME_COL() {
		return -1;
	}
	
	private static List<Question> questions = new ArrayList<Question>();

	private static void instantiateQuestions() {
		questions.clear();
		questions.add(new Question("Company Web site URL?", COMPANY_WEB_SITE()));
		questions.add(new Question("How many employees do they have?", COMPANY_SIZE_COL()));
		questions.add(new Question("Visitor Track Info - GEO City", GEO_CITY()));
		questions.add(new Question("Visitor Track Info - GEO State/Prov/Reg", GEO_STATE()));
		questions.add(new Question("Visitor Track Info - GEO Country", GEO_COUNTRY()));
		questions.add(new Question("Visitor Track Info - Parent Company", PARENT_COMPANY_NAME_COL()));
		questions.add(new Question("Visitor Track Info - Industry", INDUSTRY_COL()));
		questions.add(new Question("Visitor Track Info - Primary SICs", SIC()));
		questions.add(new Question("Visitor Track Info - Search Phrase", SEARCH_PHRASE()));
		questions.add(new Question("Visitor Track Info - Landing Page", LANDING_PAGE()));
		questions.add(new Question("Visitor Track Info - Return Visit", RETURN_VISIT()));
		questions.add(new Question("Visitor Track Info - Date / Time", DATE_TIME()));
		questions.add(new Question("Visitor Track Info - Pages", PAGES()));
		questions.add(new Question("Visitor Track Info - Target 1", TARGET_1()));
		questions.add(new Question("Visitor Track Info - Target 2", TARGET_2()));
		questions.add(new Question("Visitor Track Info - Hot Lead", HOT_LEAD()));
	}

	private static void setColumns(DelimitedFileReader fr) {
		try {
			fr.nextLine();
			System.out.println("Total Headers Found: " + fr.size());
			for (int i = 0; i < fr.size(); i++) {
				String keyValue = getColMapKey(fr.getString(i), i);
				if (colMap.get(keyValue) == null || colMap.get(keyValue) == -1)
					colMap.put(keyValue, i);
			}
			System.out.println("\n-----Starting Import-----");
		} catch (Exception ex) {
		}
	}

	/**
	 * Converts a standard or non-standard header into a standard header if possible.
	 * Adds a new field otherwise.
	 * 
	 * @param colHeader
	 * @param col
	 * @return 
	 */
	private static String getColMapKey(String colHeader, int col) {
		String chLC = colHeader.toLowerCase().trim();
		for (String key : colMap.keySet()) {
			String keyLC = key.toLowerCase().trim();
			boolean found = false;
			if (keyLC.equals(chLC))
				found = true;
			else if (key.equals("Company Name")) {
				if (chLC.equals("company")  ||  chLC.equals("organization"))
					found = true;
			} else if (key.equals("Web Site/URL")) {
				if (chLC.contains("web")  ||  chLC.contains("url")  ||  chLC.contains("internet"))
					found = true;
			} else if (key.equals("HQ Address 1")) {
				if ((chLC.contains("street")  ||  chLC.contains("address"))  &&  !chLC.contains("2"))
					found = true;
			} else if (key.equals("HQ City")) {
				if (chLC.contains("city"))
					found = true;
			} else if (key.equals("HQ State/Prov/Reg")) {
				if (chLC.contains("state") ||  chLC.contains("provence") ||  chLC.contains("region"))
					found = true;
			} else if (key.equals("HQ Zip")) {
				if (chLC.contains("zip") ||  chLC.contains("postal"))
					found = true;
			} else if (key.equals("HQ Country Code")) {
				if (chLC.contains("country"))
					found = true;
			} else if (key.equals("GEO State/Prov/Reg")) {
				if (chLC.contains("geo state")  ||  chLC.contains("geo provence")  ||  chLC.contains("geo region"))
					found = true;
			} else if (key.equals("Parent Company")) {
				if (chLC.contains("parent"))
					found = true;
			} else if (key.equals("Phone")) {
				if (chLC.contains("phone"))
					found = true;
			} else if (key.equals("SIC Code")) {
				if (chLC.equals("sic"))
					found = true;
			} else if (key.equals("Contact Last Name")) {
				if (chLC.contains("last"))
					found = true;
			} else if (key.equals("Contact First Name")) {
				if (chLC.contains("first"))
					found = true;
			} else if (key.equals("Contact Middle Name")) {
				if (chLC.contains("middle")  ||  chLC.contains("initial")  ||  chLC.equals("mi"))
					found = true;
			} else if (key.equals("Contact Job Title")) {
				if (chLC.contains("title") ||  chLC.contains("position"))
					found = true;
			} else if (key.equals("Contact E-mail")) {
				if (chLC.contains("e-mail") ||  chLC.contains("email"))
					found = true;
			} else if (key.equals("Company Size")) {
				if (chLC.contains("employees")  &&  (chLC.contains("#")  ||  chLC.contains("num")  ||  chLC.contains("no")))
					found = true;
			} else if (key.equals("Revenue")) {
				if (chLC.contains("revenue"))
					found = true;
			} else if (key.equals("Extension")) {
				if (chLC.contains("ext"))
					found = true;
			} else if (key.equals("Fax Number")) {
				if (chLC.contains("fax"))
					found = true;
			} else if (key.equals("Apartment/Apt./Suite/Ste.")) {
				if (chLC.contains("suite")  ||  chLC.contains("ste"))
					found = true;
			}
			if (found) {
					System.out.println("Column Match Found: " + colHeader + " ---> " + key);
					return key;				
			}
		}
		System.out.println("Prospect Question Created: " + colHeader);
		questions.add(new Question(colHeader, col));
		return colHeader;
	}

	private static class Question {

		public Question(String q, int p) {
			question = q;
			pos = p;
		}
		String question;
		int pos;
		String id;
	}
	
	private static String salesPersonId = null; // Jason

	private static String getString(DelimitedFileReader fr, int col) {
		if (col == -1)
			return "";
		return fr.getString(col);
	}

	private static String max(String s, int m) {
		if (s.length() <= m)
			return s;
		return s.substring(0, m);
	}

	public static void doImport(String filename, String defaultSalespersonId, String sourceId, String statusId, String typeId) {
		salesPersonId = defaultSalespersonId;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.setCurrentPersonToArahant();
		try {
			DelimitedFileReader fr = new DelimitedFileReader(filename);

			//Setup standard column map
			instantiateColMap();

			//Read in column headers
			setColumns(fr);

			//Setup questions
			instantiateQuestions();

			//Sets up questions
			for (int i = 0; i < questions.size(); i++) {
				CompanyQuestion q = hsu.createCriteria(CompanyQuestion.class).eq(CompanyQuestion.QUESTION, questions.get(i).question).first();

				if (q == null) {
					BCompanyQuestion cq = new BCompanyQuestion();
					cq.create();
					cq.setQuestion(questions.get(i).question.trim());
					cq.setAddAfterId(null);
					cq.insert();
					q = cq.getBean();
				}

				questions.get(i).id = q.getCompanyQuesId();
			}

			if (salesPersonId == null && EMPLOYEE_NAME_COL() == -1) {
				BEmployee bemp = new BEmployee();
				salesPersonId = bemp.create();
				bemp.setFirstName("Import");
				bemp.setLastName("Sales");
				bemp.setSsn(null);
				bemp.insert();
			}

			HashMap<String, String> salesPersonMap = new HashMap<String, String>();

			hsu.beginTransaction();
			while (fr.nextLine()) {
				if (getString(fr, COMPANY_NAME_COL()) == null || getString(fr, COMPANY_NAME_COL()).trim().equals(""))
					continue;

				if (EMPLOYEE_NAME_COL() != -1) {
					//find the sales person
					String fname;
					String lname;

					String name = getString(fr, EMPLOYEE_NAME_COL());

					if (salesPersonMap.containsKey(name))
						salesPersonId = salesPersonMap.get(name);
					else {

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

				//does this prospect company exist?
				String companyName = getString(fr, COMPANY_NAME_COL());

				if (companyName.trim().equals(""))
					companyName = getString(fr, PARENT_COMPANY_NAME_COL());

				if (companyName.length() > 60)
					companyName = companyName.substring(0, 60);

				if (hsu.createCriteria(ProspectCompany.class).eq(ProspectCompany.NAME, companyName).exists()) {
					System.out.println("Found... " + companyName);

					//company exists, if person exists, continue
					BProspectCompany pros = new BProspectCompany(hsu.createCriteria(ProspectCompany.class).eq(ProspectCompany.NAME, companyName).first());

					//update questions
					for (int i = 0; i < questions.size(); i++)
						try {
							if (getString(fr, questions.get(i).pos) != null && !getString(fr, questions.get(i).pos).trim().equals("")) {
								//see if already answered
								CompanyQuestionDetail cqd = hsu.createCriteria(CompanyQuestionDetail.class).eq(CompanyQuestionDetail.COMPANY, pros.getBean()).joinTo(CompanyQuestionDetail.COMPANY_QUESTION).eq(CompanyQuestion.ID, questions.get(i).id).first();

								if (cqd == null) {
									BCompanyQuestionDetail det = new BCompanyQuestionDetail();
									det.create();
									det.setCompanyId(pros.getOrgGroupId());
									det.setQuestionId(questions.get(i).id);
									det.setResponse(getString(fr, questions.get(i).pos));
									det.insert();
								} else {
									BCompanyQuestionDetail det = new BCompanyQuestionDetail(cqd);
									det.setResponse(getString(fr, questions.get(i).pos));
									det.update();
								}
							}
						} catch (Exception e) {
							//ignore
						}

					String fname = "";
					String lname = "";
					boolean newContact = false;
					if (StringUtils.isEmpty(pros.getMainContactFname()) || pros.getMainContactFname().equalsIgnoreCase("unknown"))
						if (FIRST_NAME_COL() != -1)
							pros.setMainContactFname(getString(fr, FIRST_NAME_COL()));
						else
							pros.setMainContactFname("Unknown");
					else if (!pros.getMainContactFname().equals(getString(fr, FIRST_NAME_COL()))) {
						fname = getString(fr, FIRST_NAME_COL());
						newContact = true;
					}
					if (StringUtils.isEmpty(pros.getMainContactLname()) || pros.getMainContactFname().equalsIgnoreCase("unknown"))
						if (LAST_NAME_COL() != -1)
							pros.setMainContactLname(getString(fr, LAST_NAME_COL()));
						else
							pros.setMainContactLname("Unknown");
					else if (!pros.getMainContactLname().equals(getString(fr, LAST_NAME_COL()))) {
						lname = getString(fr, LAST_NAME_COL());
						newContact = newContact && true;
					}
					if (!newContact) {
						if (StringUtils.isEmpty(pros.getMainContactMname()))
							pros.setMainContactMname(getString(fr, MIDDLE_NAME_COL()));
						if (StringUtils.isEmpty(pros.getMainContactJobTitle()))
							pros.setMainContactJobTitle(max(getString(fr, JOB_TITLE_COL()), 60));
						if (StringUtils.isEmpty(pros.getMainContactWorkPhone()))
							pros.setMainContactWorkPhone((getString(fr, WORK_PHONE_COL()) + " " + getString(fr, WORK_PHONE_EXT_COL())).trim());
						if (StringUtils.isEmpty(pros.getMainContactWorkFax()))
							pros.setMainContactWorkFax(getString(fr, WORK_FAX_COL()));
						if (StringUtils.isEmpty(pros.getMainContactPersonalEmail()))
							pros.setMainContactPersonalEmail(getString(fr, EMAIL_COL()));
					} else {
						BProspectContact bp = new BProspectContact();
						bp.create();
						bp.setCompanyId(pros.getOrgGroupId());
						bp.setFirstName(fname);
						bp.setLastName(lname);
						if (StringUtils.isEmpty(pros.getMainContactMname()))
							bp.setMiddleName(getString(fr, MIDDLE_NAME_COL()));
						if (StringUtils.isEmpty(pros.getMainContactJobTitle()))
							bp.setJobTitle(max(getString(fr, JOB_TITLE_COL()), 60));
						if (StringUtils.isEmpty(pros.getMainContactPersonalEmail()))
							bp.setPersonalEmail(pros.getMainContactPersonalEmail());
						bp.insert();
						hsu.flush();

						Set<Phone> phones = new HashSet<Phone>();
						if (StringUtils.isEmpty(pros.getMainContactWorkPhone())) {
							Phone p = new Phone();
							p.generateId();
							p.setPhoneType(1);
							p.setOrgGroup(pros.getBean());
							p.setPerson(bp.getPerson());
							p.setPhoneNumber(pros.getMainContactWorkPhone());
							hsu.insert(p);
							phones.add(p);
						}
						if (StringUtils.isEmpty(pros.getMainContactWorkFax())) {
							Phone p = new Phone();
							p.generateId();
							p.setPhoneType(2);
							p.setOrgGroup(pros.getBean());
							p.setPerson(bp.getPerson());
							p.setPhoneNumber(pros.getMainContactWorkFax());
							hsu.insert(p);
							phones.add(p);
						}

						hsu.flush();
						bp.setPhones(phones);
						bp.assignToOrgGroup(pros.getOrgGroupId(), false);
					}

					if (StringUtils.isEmpty(pros.getMainPhoneNumber()))
						pros.setMainPhoneNumber((getString(fr, WORK_PHONE_COL()) + " " + getString(fr, WORK_PHONE_EXT_COL())).trim());
					if (StringUtils.isEmpty(pros.getMainFaxNumber()))
						pros.setMainFaxNumber(getString(fr, WORK_FAX_COL()));
					if (StringUtils.isEmpty(pros.getStreet()))
						pros.setStreet(max(getString(fr, STREET_COL()), 60));
					if (StringUtils.isEmpty(pros.getStreet2()))
						pros.setStreet2(max(getString(fr, STREET_2_COL()), 60));
					if (StringUtils.isEmpty(pros.getCity()))
						pros.setCity(getString(fr, CITY_COL()));
					if (StringUtils.isEmpty(pros.getState()))
						pros.setState(getString(fr, STATE_COL()));
					if (StringUtils.isEmpty(pros.getZip()))
						pros.setZip(max(getString(fr, ZIP_COL()), 10));
					if (StringUtils.isEmpty(pros.getCountry()))
						pros.setCountry(countryCodes.get(getString(fr, COUNTRY_COL())));
					if (pros.getStatus() == null)
						pros.setStatusId(statusId);
					if (pros.getStatus() == null)
						pros.setProspectTypeId(typeId);
					if (pros.getSalesPerson() == null)
						pros.setSalesPersonId(salesPersonId);
					if (pros.getSource() == null)
						pros.setSourceId(sourceId);

					pros.insert();

					hsu.commitTransaction();
					hsu.beginTransaction();
				} else {
					System.out.println("Importing... " + companyName);
					//add company
					BProspectCompany pros = new BProspectCompany();
					String id = pros.create();
					pros.setName(companyName);
					if (FIRST_NAME_COL() != -1 && !StringUtils.isEmpty(getString(fr, FIRST_NAME_COL())))
						pros.setMainContactFname(getString(fr, FIRST_NAME_COL()));
					else
						pros.setMainContactFname("Unknown");
					if (LAST_NAME_COL() != -1 && !StringUtils.isEmpty(getString(fr, LAST_NAME_COL())))
						pros.setMainContactLname(getString(fr, LAST_NAME_COL()));
					else
						pros.setMainContactLname("Unknown");
					pros.setMainContactMname(getString(fr, MIDDLE_NAME_COL()));
					pros.setMainContactJobTitle(max(getString(fr, JOB_TITLE_COL()), 60));
					pros.setMainContactWorkPhone((getString(fr, WORK_PHONE_COL()) + " " + getString(fr, WORK_PHONE_EXT_COL())).trim());
					pros.setMainContactWorkFax(getString(fr, WORK_FAX_COL()));
					pros.setMainContactPersonalEmail(getString(fr, EMAIL_COL()));
					pros.setStreet(max(getString(fr, STREET_COL()), 60));
					pros.setStreet2(max(getString(fr, STREET_2_COL()), 60));
					pros.setCity(getString(fr, CITY_COL()));
					pros.setState(getString(fr, STATE_COL()));
					pros.setZip(max(getString(fr, ZIP_COL()), 10));
					pros.setCountry(countryCodes.get(getString(fr, COUNTRY_COL())));
					pros.setMainPhoneNumber((getString(fr, WORK_PHONE_COL()) + " " + getString(fr, WORK_PHONE_EXT_COL())).trim());
					pros.setMainFaxNumber(getString(fr, WORK_FAX_COL()));
					pros.setStatusId(statusId);
					pros.setSalesPersonId(salesPersonId);
					pros.setSourceId(sourceId);
					pros.setProspectTypeId(typeId);

					pros.insert();

					hsu.commitTransaction();
					hsu.beginTransaction();

					for (int i = 0; i < questions.size(); i++)
						try {
							if (!StringUtils.isEmpty(getString(fr, questions.get(i).pos).trim())) {
								BCompanyQuestionDetail det = new BCompanyQuestionDetail();
								det.create();
								det.setCompanyId(id);
								det.setQuestionId(questions.get(i).id);
								det.setResponse(getString(fr, questions.get(i).pos));
								det.insert();
								hsu.commitTransaction();
								hsu.beginTransaction();
							}
						} catch (Exception e) {
							//ignore
						}

					//set main contact address info too
					BPerson bp = new BPerson(pros.getMainContactPersonId());
					bp.setStreet(max(getString(fr, STREET_COL()), 60));
					bp.setStreet2(max(getString(fr, STREET_2_COL()), 60));
					bp.setCity(getString(fr, CITY_COL()));
					bp.setState(getString(fr, STATE_COL()));
					bp.setZip(max(getString(fr, ZIP_COL()), 10));
					bp.setCountry(countryCodes.get(getString(fr, COUNTRY_COL())));

					bp.update();
					hsu.commitTransaction();
					hsu.beginTransaction();
				}
			}
			System.out.println("-----Import Done-----");
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(StandardProspectImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		doImport("/home/xichen/Desktop/VisitorTrackReport.csv", "00001-0000386688", "00001-0000000008", "00001-0000000008", "00001-0000000001");

	}
}
