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
public class ProspectImport {

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

	//Shakeley
	private final static int COMPANY_NAME_COL=1;
	private final static int ALT_COMPANY_NAME_COL=-1; //place to get name if main name is blank
	private final static int LAST_NAME_COL=9;
	private final static int FIRST_NAME_COL=7;
	private final static int MIDDLE_NAME_COL=8;
	private final static int JOB_TITLE_COL=-1;
	private final static int WORK_PHONE_COL=2;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=-1;
	private final static int EMAIL_COL=-1;
	private final static int STREET_COL=3;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=4;
	private final static int STATE_COL=5;
	private final static int ZIP_COL=6;
	private final static int COUNTRY_COL=-1;
	private final static int COMPANY_SIZE_COL=10;
	private final static int EMPLOYEE_NAME_COL=0;
	private final static int INDUSTRY_COL=11;

	private static Question[]questions=new Question[]{new Question("Employee Size", 10), new Question("Industry", 11)};

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
	/*
	 * private final static int COMPANY_NAME_COL=5;
	private final static int ALT_COMPANY_NAME_COL=-1; //place to get name if main name is blank
	private final static int LAST_NAME_COL=3;
	private final static int FIRST_NAME_COL=1;
	private final static int MIDDLE_NAME_COL=2;
	private final static int JOB_TITLE_COL=7;
	private final static int WORK_PHONE_COL=13;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=14;
	private final static int EMAIL_COL=15;
	private final static int STREET_COL=9;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=10;
	private final static int STATE_COL=11;
	private final static int ZIP_COL=12;
	private final static int COUNTRY_COL=-1;
	private final static int COMPANY_SIZE_COL=-1;
	private final static int EMPLOYEE_NAME_COL=-1;
	 * */

	private final static String sourceName="Import File";
	private static String salesPersonId=null; //"00001-0000000006" Jason

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



			String statusId;

			ProspectStatus status=hsu.createCriteria(ProspectStatus.class)
				.orderBy(ProspectStatus.SEQ)
				.first();

			if (status!=null)
				statusId=status.getProspectStatusId();
			else
			{
				BProspectStatus stat=new BProspectStatus();
				statusId=stat.create();
				stat.setActive("Y");
				stat.setCode("Imported");
				stat.setDescription("Imported");
				stat.setInitialSequence(0);
				stat.insert();
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
			
			DelimitedFileReader fr = new DelimitedFileReader("/Users/Arahant/shakely/DWayneList.csv");
		
			for (int loop=0;loop<1;loop++)
				fr.skipLine();

			int count=0;
			while (fr.nextLine()) {

				if (++count%50==0)
				{
					System.out.println(count);
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				if (getString(fr,LAST_NAME_COL)==null || getString(fr,LAST_NAME_COL).trim().equals(""))
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

				if (hsu.createCriteria(ProspectCompany.class)
					.eq(ProspectCompany.NAME,companyName)
					.exists())
				{
					//company exists, if person exists, continue
					BProspectCompany pros=new BProspectCompany(hsu.createCriteria(ProspectCompany.class)
						.eq(ProspectCompany.NAME,companyName).first());

					if (hsu.createCriteria(Person.class)
						.eq(Person.FNAME,getString(fr,FIRST_NAME_COL))
						.eq(Person.LNAME,getString(fr,LAST_NAME_COL))
						.joinTo(Person.ORGGROUPASSOCIATIONS)
						.eq(OrgGroupAssociation.ORG_GROUP_ID,pros.getOrgGroupId())
						.exists())
						continue;

					//otherwise add them

					BProspectContact contact=new BProspectContact();
					contact.create();
					contact.setFirstName(getString(fr,FIRST_NAME_COL));
					contact.setLastName(getString(fr,LAST_NAME_COL));
					contact.setMiddleName(getString(fr,MIDDLE_NAME_COL));
					contact.setJobTitle(max(getString(fr,JOB_TITLE_COL),60));
					contact.setWorkPhone((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim());
					contact.setWorkFax(getString(fr,WORK_FAX_COL));
					contact.setPersonalEmail(getString(fr,EMAIL_COL));
					contact.setStreet(max(getString(fr,STREET_COL),60));
					contact.setStreet2(max(getString(fr,STREET_2_COL),60));
					contact.setCity(getString(fr,CITY_COL));
					contact.setState(getString(fr,STATE_COL));
					contact.setZip(max(getString(fr,ZIP_COL),10));
					contact.setCountry(countryCodes.get(getString(fr,COUNTRY_COL)));
					contact.setProspectType(ProspectContact.TYPE_UNKNOWN);
					contact.insert();

					contact.setOrgGroupId(pros.getOrgGroupId());

				}
				else
				{
					//add company
					BProspectCompany pros=new BProspectCompany();
					String id=pros.create();
					pros.setName(companyName);
					pros.setMainContactFname(getString(fr,FIRST_NAME_COL));
					pros.setMainContactLname(getString(fr,LAST_NAME_COL));
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
			hsu.commitTransaction();
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

}
