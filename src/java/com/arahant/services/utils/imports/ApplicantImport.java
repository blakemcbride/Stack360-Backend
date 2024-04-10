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

import com.arahant.beans.ApplicantAppStatus;
import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.beans.ApplicantSource;
import com.arahant.beans.ApplicantStatus;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantAnswer;
import com.arahant.business.BApplicantQuestion;
import com.arahant.business.BApplicantQuestionChoice;
import com.arahant.business.BApplicantSource;
import com.arahant.business.BApplicantStatus;
import com.arahant.business.BApplicationStatus;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;

/**
 *
 */
public class ApplicantImport {

	private final static int NAME_COL=1;
	private final static int EMAIL_COL=2;
	private final static int CITY_COL=3;
	private final static int STATE_COL=4;
	private final static int SCHOOL_COL=5;
	private final static int MAJOR_COL=6;
	private final static int GRADUATION_COL=7;

	public void doImport() throws Exception
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();

		DelimitedFileReader dfr=new DelimitedFileReader("/Users/Arahant/prep/scholarApplicants.csv");

		ApplicantSource as=hsu.createCriteria(ApplicantSource.class)
			.eq(ApplicantSource.DESCRIPTION, "Import")
			.first();

		String sourceId;
		if (as==null)
		{
			BApplicantSource bas=new BApplicantSource();
			sourceId=bas.create();
			bas.setDescription("Import");
			bas.insert();

		}
		else
			sourceId=as.getApplicantSourceId();


		ApplicantStatus stat=hsu.createCriteria(ApplicantStatus.class)
			.eq(ApplicantStatus.SEQ,(short)0)
			.first();

		String statusId;
		if (stat==null)
		{
			BApplicantStatus bas=new BApplicantStatus();
			statusId=bas.create();
			bas.setName("Initial Status");
			bas.setConsiderForHire(true);
			bas.insert();
		}
		else
			statusId=stat.getApplicantStatusId();

		ApplicantAppStatus astat=hsu.createCriteria(ApplicantAppStatus.class)
			.eq(ApplicantAppStatus.SEQ,(short)0)
			.first();
		String appStatusId;
		if (astat==null)
		{
			BApplicationStatus bas=new BApplicationStatus();
			appStatusId=bas.create();
			bas.setName("Initial Status");
			bas.setActive(true);
			bas.insert();
		}
		else
			appStatusId=astat.getApplicantAppStatusId();





		dfr.nextLine();

		while (dfr.nextLine())
		{
			String name=dfr.getString(NAME_COL);

			Name n=new Name(name);

			BApplicant ap=new BApplicant();
			ap.create();
			ap.setFirstName(n.fname);
			ap.setMiddleName(n.mname);
			ap.setLastName(n.lname);

			ap.setPersonalEmail(dfr.getString(EMAIL_COL));
			ap.setCity(dfr.getString(CITY_COL));
			ap.setState(dfr.getString(STATE_COL).toUpperCase());
			
			ap.setApplicantStatusId(statusId);
			ap.setApplicantSourceId(sourceId);
			ap.insert();

			checkQuestion("GraduationYear", "Graduation Year");
			setAnswer(ap, "GraduationYear", dfr.getString(GRADUATION_COL));

			checkQuestion("School", "School");
			setAnswer(ap, "School", dfr.getString(SCHOOL_COL));

			checkQuestion("Major", "Major");
			setAnswer(ap, "Major", dfr.getString(MAJOR_COL));
			
		}
	}


	private void checkQuestion(String property, String question)
	{
		String id=BProperty.get(property);
		if (id==null || id.equals(""))
		{
			BApplicantQuestion baq=new BApplicantQuestion();
			baq.create();
			baq.setQuestion(question);
			baq.setInternalUse(false);
			baq.setAnswerType(ApplicantQuestion.TYPE_STRING);
			baq.insert();

			BProperty prop=new BProperty(property);
			prop.setValue(baq.getId());
			prop.update();
		}
	}
	private void setAnswer(BApplicant bap, String property, String answer)
	{
		String id=BProperty.get(property);
		if (id!=null && !id.equals(""))
		{
			BApplicantAnswer bans=new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(answer);
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	private void setAnswerChoice(BApplicant bap, String property, String description)
	{
		String id=BProperty.get(property);

		ApplicantQuestionChoice aqc=ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
				.eq(ApplicantQuestionChoice.DESCRIPTION, description)
				.first();
		BApplicantQuestionChoice baqc = new BApplicantQuestionChoice(aqc);
		if (aqc==null)
		{
			baqc.create();
			baqc.setDescription(description);
			baqc.setQuestion(new BApplicantQuestion(BProperty.get(property)).getBean());
			baqc.insert();

		}
		if (id!=null && !id.equals(""))
		{
			BApplicantAnswer bans=new BApplicantAnswer();
			bans.create();
			bans.setApplicant(bap);
			bans.setAnswer(ApplicantQuestion.TYPE_LIST + "", "", 0, baqc.getBean().getApplicantQuestionChoiceId());
			bans.setQuestionId(BProperty.get(property));
			bans.insert();
		}
	}

	protected class Name
	{
		public String fname;
		public String lname;
		public String mname="";

		public Name(String name)
		{
			if (isEmpty(name))
				name=". .";

			if (name.indexOf(' ')==-1)
			{
				fname=".";
				lname=name;
			}
			else
			{
				lname=name.substring(0, name.indexOf(' ')).trim();
				fname=name.substring(name.indexOf(' ')+1).trim();

				int spos=fname.indexOf(' ');
				if (spos!=-1)
				{
					mname=fname.substring(spos+1).trim();
					if (mname.length()>20)
						mname=mname.substring(0,20);
					fname=fname.substring(0,spos).trim();
				}
			}

			if (lname.endsWith(","))
				lname=lname.substring(0,lname.length()-1);
			if (mname.endsWith(","))
				mname=mname.substring(0,mname.length()-1);
		}
	}

	protected boolean isEmpty(String s) {
		return s==null || s.trim().equals("");
	}

	public static void main (String []args)
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();
		try
		{
			new ApplicantImport().doImport();
			hsu.commitTransaction();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}

}
