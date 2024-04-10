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

import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ClientCompany;
import com.arahant.business.BPerson;
import com.arahant.business.BClientCompany;
import com.arahant.business.BClientContact;
import com.arahant.business.BClientStatus;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ClientImport {

	
	private final static int COMPANY_NAME_COL=0;
	private final static int ALT_COMPANY_NAME_COL=-1; //place to get name if main name is blank
	private final static int LAST_NAME_COL=-1;
	private final static int FIRST_NAME_COL=-1;
	private final static int MIDDLE_NAME_COL=-1;
	private final static int NICK_NAME_COL=-1;
	private final static int JOB_TITLE_COL=-1;
	private final static int WORK_PHONE_COL=-1;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=-1;
	private final static int EMAIL_COL=-1;
	private final static int STREET_COL=1;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=2;
	private final static int STATE_COL=3;
	private final static int ZIP_COL=4;
	private final static int EXTERNAL_REF=-1;
	private final static int CONTRACT_DATE=-1;




	/* Prep settings 
	private final static int COMPANY_NAME_COL=4;
	private final static int ALT_COMPANY_NAME_COL=-1; //place to get name if main name is blank
	private final static int LAST_NAME_COL=2;
	private final static int FIRST_NAME_COL=0;
	private final static int MIDDLE_NAME_COL=-1;
	private final static int NICK_NAME_COL=1;
	private final static int JOB_TITLE_COL=-1;
	private final static int WORK_PHONE_COL=6;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=-1;
	private final static int EMAIL_COL=5;
	private final static int STREET_COL=-1;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=-1;
	private final static int STATE_COL=-1;
	private final static int ZIP_COL=-1;
	private final static int EXTERNAL_REF=3;
	private final static int CONTRACT_DATE=7;
*/
	/*individual settings* *
	private final static int COMPANY_NAME_COL=-1;
	private final static int ALT_COMPANY_NAME_COL=-1; //place to get name if main name is blank
	private final static int LAST_NAME_COL=1;
	private final static int FIRST_NAME_COL=0;
	private final static int MIDDLE_NAME_COL=-1;
	private final static int JOB_TITLE_COL=-1;
	private final static int WORK_PHONE_COL=-1;
	private final static int WORK_PHONE_EXT_COL=-1;
	private final static int WORK_FAX_COL=-1;
	private final static int EMAIL_COL=-1;
	private final static int STREET_COL=2;
	private final static int STREET_2_COL=-1;
	private final static int CITY_COL=3;
	private final static int STATE_COL=4;
	private final static int ZIP_COL=5;
	private final static int EXTERNAL_REF=-1;
	private final static int CONTRACT_DATE=-1;
	private final static int NICK_NAME_COL=-1;
*/

	public static final String getString (DelimitedFileReader fr, int col)
	{
		if (col==-1)
			return "";
		return fr.getString(col);
	}

	public static final int getDate (DelimitedFileReader fr, int col)
	{
		if (col==-1)
			return 0;

		String d=fr.getString(col);

		if (d.indexOf(' ')!=-1)
			d=d.substring(0,d.indexOf(' '));

		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		try {
			return DateUtils.getDate(sdf.parse(d));
		} catch (ParseException ex) {
			Logger.getLogger(ClientImport.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	public static String max(String s, int m)
	{
		if (s.length()<=m)
			return s;
		return s.substring(0,m);
	}

	public static void doImport(String filename)
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.setCurrentPersonToArahant();
		try {

			BClientStatus stat=BClientStatus.findOrMake("Active");

			DelimitedFileReader fr = new DelimitedFileReader(filename);

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


			//	if (count>100)
			//		break;

				//does this company exist?
				String companyName=getString(fr,COMPANY_NAME_COL);

				if (companyName.trim().equals(""))
					companyName=getString(fr,ALT_COMPANY_NAME_COL);

				if (companyName.trim().equals("")) //use the person name
					companyName=getString(fr,LAST_NAME_COL)+", "+getString(fr,FIRST_NAME_COL);

				if (hsu.createCriteria(ClientCompany.class)
					.eq(ClientCompany.NAME,companyName)
					.exists())
				{
					//company exists, if person exists, continue
					BClientCompany pros=new BClientCompany(hsu.createCriteria(ClientCompany.class)
						.eq(ClientCompany.NAME,companyName).first());

					if (hsu.createCriteria(Person.class)
						.eq(Person.FNAME,getString(fr,FIRST_NAME_COL))
						.eq(Person.LNAME,getString(fr,LAST_NAME_COL))
						.joinTo(Person.ORGGROUPASSOCIATIONS)
						.eq(OrgGroupAssociation.ORG_GROUP_ID,pros.getOrgGroupId())
						.exists())
						continue;

					//otherwise add them

					BClientContact contact=new BClientContact();
					contact.create();
					contact.setFirstName(getString(fr,FIRST_NAME_COL));
					contact.setLastName(getString(fr,LAST_NAME_COL));
					contact.setNickName(getString(fr,NICK_NAME_COL));
					contact.setMiddleName(getString(fr,MIDDLE_NAME_COL));
					contact.setJobTitle(max(getString(fr,JOB_TITLE_COL),60));
					contact.setWorkPhone(max((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim(),20));
					contact.setWorkFax(getString(fr,WORK_FAX_COL));
					contact.setPersonalEmail(getString(fr,EMAIL_COL));
					contact.setStreet(max(getString(fr,STREET_COL),60));
					contact.setStreet2(max(getString(fr,STREET_2_COL),60));
					contact.setCity(getString(fr,CITY_COL));
					contact.setState(getString(fr,STATE_COL));
					contact.setZip(max(getString(fr,ZIP_COL),10));
					contact.insert();

					contact.setOrgGroupId(pros.getOrgGroupId());

				}
				else
				{
					//add company
					BClientCompany pros=new BClientCompany();
					pros.create();
					String lname=getString(fr,LAST_NAME_COL);
					if (lname==null||lname.trim().equals(""))
						lname=".";
					String fname=getString(fr,FIRST_NAME_COL);
					if (fname==null||fname.trim().equals(""))
						fname=".";
					pros.setName(companyName);
					pros.setMainContactFname(fname);
					pros.setMainContactLname(lname);
					pros.setMainContactNname(getString(fr,NICK_NAME_COL));
					pros.setMainContactMname(getString(fr,MIDDLE_NAME_COL));
					pros.setMainContactJobTitle(max(getString(fr,JOB_TITLE_COL),60));
					pros.setMainContactWorkPhone(max((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim(),20));
					pros.setMainContactWorkFax(getString(fr,WORK_FAX_COL));
					pros.setMainContactPersonalEmail(getString(fr,EMAIL_COL));
					pros.setStreet(max(getString(fr,STREET_COL),60));
					pros.setStreet2(max(getString(fr,STREET_2_COL),60));
					pros.setCity(getString(fr,CITY_COL));
					pros.setState(getString(fr,STATE_COL));
					pros.setZip(max(getString(fr,ZIP_COL),10));
					pros.setMainPhoneNumber(max((getString(fr,WORK_PHONE_COL)+" "+getString(fr,WORK_PHONE_EXT_COL)).trim(),20));
					pros.setMainFaxNumber(getString(fr,WORK_FAX_COL));
					pros.setIdentifier(getString(fr, EXTERNAL_REF));
					pros.setContractDate(getDate(fr, CONTRACT_DATE));

					pros.insert();

					//set main contact address info too
					BPerson bp=new BPerson(pros.getMainContactPersonId());
					bp.setStreet(max(getString(fr,STREET_COL),60));
					bp.setStreet2(max(getString(fr,STREET_2_COL),60));
					bp.setCity(getString(fr,CITY_COL));
					bp.setState(getString(fr,STATE_COL));
					bp.setZip(max(getString(fr,ZIP_COL),10));

					bp.update();

				}
			}
			hsu.commitTransaction();
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(ClientImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		//doImport("/Users/Arahant/Sacks/individuals.csv");
	//	doImport("/Users/Arahant/Sacks/corporations.csv");
	//	doImport("/Users/Arahant/Sacks/nonprofits.csv");
	//	doImport("/Users/Arahant/Sacks/partnerships.csv");
	//	doImport("/Users/Arahant/Sacks/trusts.csv");
		//doImport("/Users/Arahant/prep/prepclients.csv");


    }


}
