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
import com.arahant.beans.CompanyQuestionDetail;
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
public class VisitorTrackImport {

	static HashMap<String, String> countryCodes=new HashMap<String, String>();

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

	private final static int COMPANY_NAME_COL=0;
	private final static int ALT_COMPANY_NAME_COL=10; //place to get name if main name is blank
	private final static int LAST_NAME_COL=-1;
	private final static int FIRST_NAME_COL=-1;
	private final static int MIDDLE_NAME_COL=-1;
	private final static int JOB_TITLE_COL=-1;
	private final static int WORK_PHONE_COL=11;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=-1;
	private final static int EMAIL_COL=-1;
	private final static int STREET_COL=2;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=3;
	private final static int STATE_COL=4;
	private final static int ZIP_COL=5;
	private final static int COUNTRY_COL=6;
	private final static int COMPANY_SIZE_COL=10;
	private final static int EMPLOYEE_NAME_COL=-1;
	private final static int INDUSTRY_COL=11;

	private static Question[]questions=new Question[]{new Question("Company Web site URL?", 1),
		new Question("Visitor Track Info - GEO City", 7),
		new Question("Visitor Track Info - GEO State/Prov/Reg", 8),
		new Question("Visitor Track Info - GEO Country", 9),
		new Question("Visitor Track Info - Parent Company", 10),
		new Question("Visitor Track Info - Industry", 12),
		new Question("Visitor Track Info - Primary SICs", 13),
		new Question("Visitor Track Info - Search Phrase", 15),
		new Question("Visitor Track Info - Landing Page", 16),
		new Question("Visitor Track Info - Return Visit", 17),
		new Question("Visitor Track Info - Date / Time", 18),
		new Question("Visitor Track Info - Pages", 19),
		new Question("Visitor Track Info - Target 1", 20),
		new Question("Visitor Track Info - Target 2", 21),
		new Question("Visitor Track Info - Hot Lead", 22),
	};

	private static class Question
	{
		public Question(String q, int p)
		{
			question=q;
			pos=p;
		}
		String question;
		int pos;
		String id;
	}
	

	private final static String sourceName="Internet";
	private static String salesPersonId=null; // Jason

	public static final String getString (DelimitedFileReader fr, int col)
	{
		if (col==-1)
			return "";
		return fr.getString(col);
	}

	public static String max(String s, int m)
	{
		if (s.length()<=m)
			return s;
		return s.substring(0,m);
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		doImport("/home/brad/Desktop/VisitorTrackReport.csv", "00001-0000386688","00001-0000000008");

	}
	public static void doImport(String filename, String defaultSalespersonId, String statusId)
	{
		salesPersonId=defaultSalespersonId;
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.setCurrentPersonToArahant();
		try {

			for (int loop=0;loop<questions.length;loop++)
			{
				CompanyQuestion q=hsu.createCriteria(CompanyQuestion.class)
					.eq(CompanyQuestion.QUESTION, questions[loop].question)
					.first();

				if (q==null)
				{
					BCompanyQuestion cq=new BCompanyQuestion();
					cq.create();
					cq.setQuestion(questions[loop].question);
					cq.setAddAfterId(null);
					cq.insert();
					q=cq.getBean();
				}

				questions[loop].id=q.getCompanyQuesId();
			}




			ProspectSource source=hsu.createCriteria(ProspectSource.class).eq(ProspectSource.SOURCE_CODE, sourceName).first();

			String sourceId;
			if (source!=null)
				sourceId=source.getProspectSourceId();
			else
			{
				BProspectSource src=new BProspectSource();
				sourceId=src.create();
				src.setCode(sourceName);
				src.setDescription(sourceName);
				src.insert();
			}

			if (salesPersonId==null && EMPLOYEE_NAME_COL==-1)
			{
				BEmployee bemp=new BEmployee();
				salesPersonId=bemp.create();
				bemp.setFirstName("Import");
				bemp.setLastName("Sales");
				bemp.setSsn(null);
				bemp.insert();
			}

			HashMap<String,String> salesPersonMap=new HashMap<String,String>();
			
			DelimitedFileReader fr = new DelimitedFileReader(filename);
		
			for (int loop=0;loop<1;loop++)
				fr.skipLine();

			hsu.beginTransaction();
			int count=0;
			while (fr.nextLine()) {

				//System.out.println(++count);

				if (getString(fr,COMPANY_NAME_COL)==null || getString(fr,COMPANY_NAME_COL).trim().equals(""))
					continue;

				if (EMPLOYEE_NAME_COL!=-1)
				{
					//find the sales person
					String fname;
					String lname;

					String name=getString(fr, EMPLOYEE_NAME_COL);

					if (salesPersonMap.containsKey(name))
						salesPersonId=salesPersonMap.get(name);
					else
					{

						fname=name.substring(0,name.indexOf(' ')).trim();
						lname=name.substring(name.indexOf(' ')).trim();

						Employee emp=hsu.createCriteria(Employee.class)
								.eq(Employee.FNAME, fname)
								.eq(Employee.LNAME, lname)
								.first();

						//if not found, make them
						if (emp==null)
						{
							BEmployee bemp=new BEmployee();
							salesPersonId=bemp.create();
							bemp.setFirstName(fname);
							bemp.setLastName(lname);
							bemp.setSsn(null);
							bemp.insert();
							emp=bemp.getEmployee();
						}

						salesPersonId=emp.getPersonId();
						salesPersonMap.put(name, salesPersonId);
					}
				}

			//	if (count>100)
			//		break;

				//does this prospect company exist?
				String companyName=getString(fr,COMPANY_NAME_COL);

				if (companyName.trim().equals(""))
					companyName=getString(fr,ALT_COMPANY_NAME_COL);

				if (companyName.length() > 60)
					companyName = companyName.substring(0, 60);

				if (hsu.createCriteria(ProspectCompany.class)
					.eq(ProspectCompany.NAME,companyName)
					.exists())
				{
					//company exists, if person exists, continue
					BProspectCompany pros=new BProspectCompany(hsu.createCriteria(ProspectCompany.class)
						.eq(ProspectCompany.NAME,companyName).first());


					//update questions
					for (int loop=0;loop<questions.length;loop++)
						try
						{
							if (getString(fr,questions[loop].pos)!=null && !getString(fr,questions[loop].pos).trim().equals(""))
							{
								//see if already answered
								CompanyQuestionDetail cqd=hsu.createCriteria(CompanyQuestionDetail.class)
									.eq(CompanyQuestionDetail.COMPANY, pros.getBean())
									.joinTo(CompanyQuestionDetail.COMPANY_QUESTION)
									.eq(CompanyQuestion.ID, questions[loop].id)
									.first();

								if (cqd==null)
								{
									BCompanyQuestionDetail det=new BCompanyQuestionDetail();
									det.create();
									det.setCompanyId(pros.getOrgGroupId());
									det.setQuestionId(questions[loop].id);
									det.setResponse(getString(fr,questions[loop].pos));
									det.insert();
								}
								else
								{
									BCompanyQuestionDetail det=new BCompanyQuestionDetail(cqd);
									det.setResponse(getString(fr,questions[loop].pos));
									det.update();
								}
							}
						}
						catch (Exception e)
						{
							//ignore
						}

				}
				else
				{
					//add company
					BProspectCompany pros=new BProspectCompany();
					String id=pros.create();
					pros.setName(companyName);
					pros.setMainContactFname("Unknown");
					pros.setMainContactLname("Unknown");
					pros.setMainContactMname(getString(fr,MIDDLE_NAME_COL));
					pros.setMainContactJobTitle(max(getString(fr,JOB_TITLE_COL),60));
					pros.setMainContactWorkPhone((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim());
					pros.setMainContactWorkFax(getString(fr,WORK_FAX_COL));
					pros.setMainContactPersonalEmail(getString(fr,EMAIL_COL));
					pros.setStreet(max(getString(fr,STREET_COL),60));
					pros.setStreet2(max(getString(fr,STREET_2_COL),60));
					pros.setCity(getString(fr,CITY_COL));
					pros.setState(getString(fr,STATE_COL));
					pros.setZip(max(getString(fr,ZIP_COL),10));
					pros.setCountry(countryCodes.get(getString(fr,COUNTRY_COL)));
					pros.setMainPhoneNumber((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim());
					pros.setMainFaxNumber(getString(fr,WORK_FAX_COL));
					pros.setMainContactProspectType(ProspectContact.TYPE_UNKNOWN);

					pros.setStatusId(statusId);
					pros.setSalesPersonId(salesPersonId);
					pros.setSourceId(sourceId);

					pros.insert();

					for (int loop=0;loop<questions.length;loop++)
						try
						{
							if (getString(fr,questions[loop].pos)!=null && !getString(fr,questions[loop].pos).trim().equals(""))
							{
								BCompanyQuestionDetail det=new BCompanyQuestionDetail();
								det.create();
								det.setCompanyId(id);
								det.setQuestionId(questions[loop].id);
								det.setResponse(getString(fr,questions[loop].pos));
								det.insert();
							}
						}
						catch (Exception e)
						{
							//ignore
						}

					//set main contact address info too
					BPerson bp=new BPerson(pros.getMainContactPersonId());
					bp.setStreet(max(getString(fr,STREET_COL),60));
					bp.setStreet2(max(getString(fr,STREET_2_COL),60));
					bp.setCity(getString(fr,CITY_COL));
					bp.setState(getString(fr,STATE_COL));
					bp.setZip(max(getString(fr,ZIP_COL),10));
					bp.setCountry(countryCodes.get(getString(fr,COUNTRY_COL)));

					bp.update();

				}
			}
			//System.out.println("-----Import Done-----");
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(VisitorTrackImport.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

}
