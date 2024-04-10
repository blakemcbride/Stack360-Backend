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

import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProspectCompany;
import com.arahant.beans.ProspectSource;
import com.arahant.business.BCompanyQuestionDetail;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectContact;
import com.arahant.business.BProspectSource;
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
    public  void importProspects(String filename, String sourceName, String sourceDescription, int COMPANY_NAME_COL,
			int ALT_COMPANY_NAME_COL,int LAST_NAME_COL, int FIRST_NAME_COL, int MIDDLE_NAME_COL, int JOB_TITLE_COL, int WORK_PHONE_COL,
			int WORK_PHONE_EXT_COL, int WORK_FAX_COL, int EMAIL_COL, int STREET_COL, int STREET_2_COL, int CITY_COL,
			int STATE_COL, int ZIP_COL, int COUNTRY_COL, int COMPANY_SIZE_COL, String statusId, String salesPersonId,
			String [] questionId, int []answerCol)
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.setCurrentPersonToArahant();
		try {

			ProspectSource source=hsu.createCriteria(ProspectSource.class).eq(ProspectSource.SOURCE_CODE, sourceName).first();

			String sourceId;
			if (source!=null)
				sourceId=source.getProspectSourceId();
			else
			{
				BProspectSource src=new BProspectSource();
				sourceId=src.create();
				src.setCode(sourceName);
				src.setDescription(sourceDescription);
				src.insert();
			}


			DelimitedFileReader fr = new DelimitedFileReader(filename);

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



			//	if (count>100)
			//		break;

				//does this vendor company exist?
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

					pros.setStatusId(statusId);
					pros.setSalesPersonId(salesPersonId);
					pros.setSourceId(sourceId);

					pros.insert();

					for (int loop=0;loop<questionId.length;loop++)
					{
						try
						{
							if (getString(fr,answerCol[loop])!=null && !getString(fr,answerCol[loop]).trim().equals(""))
							{
								BCompanyQuestionDetail det=new BCompanyQuestionDetail();
								det.create();
								det.setCompanyId(id);
								det.setQuestionId(questionId[loop]);
								det.setResponse(getString(fr,answerCol[loop]));
								det.insert();
							}
						}
						catch (Exception e)
						{
							//ignore
						}
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
