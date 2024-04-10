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

import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class CignaChangesToCSV {

	public void dumpPcr(DelimitedFileWriter dfw, BPerson bp, Date date) throws Exception
	{
		PersonChangeRequest pcr = ArahantSession.getHSU()
			.createCriteria(PersonChangeRequest.class)
			.eq(PersonChangeRequest.PERSON, bp.getPerson())
			.ge(PersonChangeRequest.REQUEST_DATE, date)
			.eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_CIGNA)
			.orderByDesc(PersonChangeRequest.REQUEST_DATE)
			.first();

		Document doc = DOMUtils.createDocument(pcr.getRequestData());
		Node n = DOMUtils.getNode(doc, "CIGNADATA");

		dfw.writeField(DOMUtils.getString(n, "employeeLastName"));
		dfw.writeField(DOMUtils.getString(n, "employeeFirstName"));
		dfw.writeField(DOMUtils.getString(n, "employeeMiddleName"));
		dfw.writeField(DOMUtils.getString(n, "employeeSSN"));
		dfw.writeField(DateUtils.getDateFormatted(DOMUtils.getInt(n, "employeeDOB")));
		dfw.writeField(DOMUtils.getString(n, "employeeSex"));
		dfw.writeField(DOMUtils.getString(n, "employeeAddress"));

		dfw.writeField(DOMUtils.getString(n, "employeeCity"));
		dfw.writeField(DOMUtils.getString(n, "employeeState"));
		dfw.writeField(DOMUtils.getString(n, "employeeZip"));
		dfw.writeField(DOMUtils.getString(n, "employeeEmail"));
		dfw.writeField(DOMUtils.getString(n, "employeePhone"));
		dfw.writeField(DOMUtils.getString(n, "employeeWorkPhone"));
		dfw.writeField(DOMUtils.getString(n, "pointOfService"));
		dfw.writeField(DOMUtils.getString(n, "HMO"));
		dfw.writeField(DOMUtils.getString(n, "network"));

		dfw.writeField(DOMUtils.getString(n, "pointOfServiceOpen"));
		dfw.writeField(DOMUtils.getString(n, "HMOOpen"));
		dfw.writeField(DOMUtils.getString(n, "networkOpen"));
		dfw.writeField(DOMUtils.getString(n, "openAccessPlus"));
		dfw.writeField(DOMUtils.getString(n, "openAccessPlusInNetwork"));
		dfw.writeField(DOMUtils.getString(n, "PPO"));

		dfw.writeField(DOMUtils.getString(n, "networkPPOEPO"));
		dfw.writeField(DOMUtils.getString(n, "PPA"));
		dfw.writeField(DOMUtils.getString(n, "medicalIndemnity"));
		dfw.writeField(DOMUtils.getString(n, "writeIn"));
		dfw.writeField(DOMUtils.getString(n, "writeInText"));
		dfw.writeField(DOMUtils.getString(n, "HRA"));

		dfw.writeField(DOMUtils.getString(n, "HSA"));
		dfw.writeField(DOMUtils.getString(n, "pharmacyHRA"));
		dfw.writeField(DOMUtils.getString(n, "dentalHRA"));
		dfw.writeField(DOMUtils.getString(n, "withOpenAccessPlus"));
		dfw.writeField(DOMUtils.getString(n, "withOpenAccessPlusNetwork"));
		dfw.writeField(DOMUtils.getString(n, "withEPO"));

		dfw.writeField(DOMUtils.getString(n, "withIndemnity"));
		dfw.writeField(DOMUtils.getString(n, "CIGNACareNetwork"));
		dfw.writeField(DOMUtils.getString(n, "applicableOption"));
		dfw.writeField(DOMUtils.getString(n, "flexibleSpendingHealthCare"));
		dfw.writeField(DOMUtils.getString(n, "flexibleSpendingDependentDayCare"));
		dfw.writeField(DOMUtils.getString(n, "flexibleSpendingDeclineCoverage"));

		dfw.writeField(DOMUtils.getString(n, "CIGNADentalCare"));
		dfw.writeField(DOMUtils.getString(n, "dentalPPO"));
		dfw.writeField(DOMUtils.getString(n, "dentalEPO"));
		dfw.writeField(DOMUtils.getString(n, "dentalIndemnity"));
		dfw.writeField(DOMUtils.getString(n, "dentalCoverage"));
		dfw.writeField(DOMUtils.getString(n, "otherInsurance"));


		dfw.writeField(DOMUtils.getString(n, "life"));
		dfw.writeField(DOMUtils.getString(n, "lifeValue"));
		dfw.writeField(DOMUtils.getString(n, "additionalLife"));
		dfw.writeField(DOMUtils.getString(n, "additionalLifeValue"));
		dfw.writeField(DOMUtils.getString(n, "dependentLifeSpouse"));
		dfw.writeField(DOMUtils.getString(n, "dependentLifeSpouseValue"));


		dfw.writeField(DOMUtils.getString(n, "dependendLifeChild"));
		dfw.writeField(DOMUtils.getString(n, "dependendLifeChildValue"));
		dfw.writeField(DOMUtils.getString(n, "adnd"));
		dfw.writeField(DOMUtils.getString(n, "adndValue"));
		dfw.writeField(DOMUtils.getString(n, "additionalAdnd"));
		dfw.writeField(DOMUtils.getString(n, "additionalAdndValue"));

		dfw.writeField(DOMUtils.getString(n, "std"));
		dfw.writeField(DOMUtils.getString(n, "stdValue"));
		dfw.writeField(DOMUtils.getString(n, "ltd"));
		dfw.writeField(DOMUtils.getString(n, "ltdValue"));
		dfw.writeField(DOMUtils.getString(n, "declineLife"));
		dfw.writeField(DOMUtils.getString(n, "declineAdnd"));


		dfw.writeField(DOMUtils.getString(n, "declineStd"));
		dfw.writeField(DOMUtils.getString(n, "declineLtd"));
		dfw.writeField(DOMUtils.getString(n, "openAccessCore"));
		dfw.writeField(DOMUtils.getString(n, "openAccessPremium"));
		dfw.writeField(DOMUtils.getString(n, "declineCoverage"));

		
		Node depNode=DOMUtils.getNode(n, "dependents");

		int depCount=0;

		NodeList deps=DOMUtils.getNodes(depNode, "dependent");

		if (deps.getLength()>10)
			throw new Exception("More than 10 deps encountered");

		for (int loop=0;loop<deps.getLength();loop++)
		{
			depCount++;
			writeDependent(dfw,deps.item(loop));
		}

		for (int loop=depCount;loop<10;loop++)
			for (int fields=0;fields<DEP_FIELD_COUNT;fields++)
				dfw.writeField("");

		

		Node otherNode=DOMUtils.getNode(n, "otherCoverage");

		int otherCount=0;

		NodeList others=DOMUtils.getNodes(otherNode, "other");

		if (others.getLength()>10)
			throw new Exception("More than 10 others encountered");

		for (int loop=0;loop<others.getLength();loop++)
		{
			otherCount++;
			writeOther(dfw,others.item(loop));
		}

		for (int loop=otherCount;loop<10;loop++)
			for (int fields=0;fields<OTHER_FIELD_COUNT;fields++)
				dfw.writeField("");

		dfw.endRecord();
	}

	private final int DEP_FIELD_COUNT=6;
	private final int OTHER_FIELD_COUNT=9;

	public void writeHeader(DelimitedFileWriter dfw) throws Exception
	{
		dfw.writeField("employeeLastName");
		dfw.writeField("employeeFirstName");
		dfw.writeField("employeeMiddleName");
		dfw.writeField("employeeSSN");
		dfw.writeField("employeeDOB");
		dfw.writeField("employeeSex");
		dfw.writeField("employeeAddress");

		dfw.writeField("employeeCity");
		dfw.writeField("employeeState");
		dfw.writeField("employeeZip");
		dfw.writeField("employeeEmail");
		dfw.writeField("employeePhone");
		dfw.writeField("employeeWorkPhone");
		dfw.writeField("pointOfService");
		dfw.writeField("HMO");
		dfw.writeField("network");

		dfw.writeField("pointOfServiceOpen");
		dfw.writeField("HMOOpen");
		dfw.writeField("networkOpen");
		dfw.writeField("openAccessPlus");
		dfw.writeField("openAccessPlusInNetwork");
		dfw.writeField("PPO");

		dfw.writeField("networkPPOEPO");
		dfw.writeField("PPA");
		dfw.writeField("medicalIndemnity");
		dfw.writeField("writeIn");
		dfw.writeField("writeInText");
		dfw.writeField("HRA");

		dfw.writeField("HSA");
		dfw.writeField("pharmacyHRA");
		dfw.writeField("dentalHRA");
		dfw.writeField("withOpenAccessPlus");
		dfw.writeField("withOpenAccessPlusNetwork");
		dfw.writeField("withEPO");

		dfw.writeField("withIndemnity");
		dfw.writeField("CIGNACareNetwork");
		dfw.writeField("applicableOption");
		dfw.writeField("flexibleSpendingHealthCare");
		dfw.writeField("flexibleSpendingDependentDayCare");
		dfw.writeField("flexibleSpendingDeclineCoverage");

		dfw.writeField("CIGNADentalCare");
		dfw.writeField("dentalPPO");
		dfw.writeField("dentalEPO");
		dfw.writeField("dentalIndemnity");
		dfw.writeField("dentalCoverage");
		dfw.writeField("otherInsurance");


		dfw.writeField("life");
		dfw.writeField("lifeValue");
		dfw.writeField("additionalLife");
		dfw.writeField("additionalLifeValue");
		dfw.writeField("dependentLifeSpouse");
		dfw.writeField("dependentLifeSpouseValue");


		dfw.writeField("dependendLifeChild");
		dfw.writeField("dependendLifeChildValue");
		dfw.writeField("adnd");
		dfw.writeField("adndValue");
		dfw.writeField("additionalAdnd");
		dfw.writeField("additionalAdndValue");

		dfw.writeField("std");
		dfw.writeField("stdValue");
		dfw.writeField("ltd");
		dfw.writeField("ltdValue");
		dfw.writeField("declineLife");
		dfw.writeField("declineAdnd");


		dfw.writeField("declineStd");
		dfw.writeField("declineLtd");
		dfw.writeField("openAccessCore");
		dfw.writeField("openAccessPremium");
		dfw.writeField("declineCoverage");

		for (int loop=1;loop<11;loop++)
		{
			dfw.writeField("dep_"+loop+"_FirstName");
			dfw.writeField("dep_"+loop+"_MiddleName");
			dfw.writeField("dep_"+loop+"_LastName");
			dfw.writeField("dep_"+loop+"_DOB");
			dfw.writeField("dep_"+loop+"_Gender");
			dfw.writeField("dep_"+loop+"_SSN");
		}

		for (int loop=1;loop<11;loop++)
		{
			dfw.writeField("other_"+loop+"_FirstName");
			dfw.writeField("other_"+loop+"_MiddleName");
			dfw.writeField("other_"+loop+"_LastName");
			dfw.writeField("other_"+loop+"_SSN");

			dfw.writeField("other_"+loop+"_EffDate");
			dfw.writeField("other_"+loop+"_MedicareA");
			dfw.writeField("other_"+loop+"_MedicareA");
			dfw.writeField("other_"+loop+"_Medicaid");
			dfw.writeField("other_"+loop+"_Other");

		}

		dfw.endRecord();
	}


	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

	private int getDate(String dobStr) throws Exception {
		if (dobStr.equals("NaN")) {
			return 0;
		}
		try {
			return DateUtils.getDate(sdf.parse(dobStr.replaceAll("GMT-0400", "").replaceAll("GMT-0500", "").replaceAll("GMT-0600", "").replaceAll("GMT-0700", "").replaceAll("GMT-0800", "")));
		} catch (Exception e) {
			return 0;
		}
	}

	public void run() throws Exception
	{
		DelimitedFileWriter dfw=new DelimitedFileWriter("CIGNA.csv");

		writeHeader(dfw);

		HibernateSessionUtil hsu=ArahantSession.getHSU();

		Calendar start=Calendar.getInstance();
		start.set(Calendar.MONTH, Calendar.DECEMBER);
		start.set(Calendar.DAY_OF_MONTH, 11);
		start.set(Calendar.YEAR,2009);
		start.set(Calendar.HOUR, 0);
		Date date=start.getTime();

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
						.selectFields(Person.PERSONID)
						.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).orderBy(Person.SSN)
						.joinTo(Person.CHANGE_REQUESTS)
						.ge(PersonChangeRequest.REQUEST_DATE, date)
						.eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_CIGNA)
						.orderBy(PersonChangeRequest.REQUEST_DATE);



		HashSet<String> ids=new HashSet<String>();
		ids.addAll((List)hcu.list());

		int count=0;
		for (String id : ids)
		{
			System.out.println(++count);
			BPerson bp=new BPerson(id);

			dumpPcr(dfw, bp, date);
		}


		dfw.close();

	}



	public static void main(String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			new CignaChangesToCSV().run();
			ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
			ArahantSession.getHSU().rollbackTransaction();

		}



	}

	private void writeDependent(DelimitedFileWriter dfw, Node n) throws Exception {
		dfw.writeField(DOMUtils.getString(n, "personFirstName"));
		dfw.writeField(DOMUtils.getString(n, "personMiddleName"));
		dfw.writeField(DOMUtils.getString(n, "personLastName"));
		dfw.writeField(DateUtils.getDateFormatted(getDate(DOMUtils.getString(n, "personDOB"))));
		dfw.writeField(DOMUtils.getString(n, "personGender"));
		dfw.writeField(DOMUtils.getString(n, "personSSN"));
	}

	private void writeOther(DelimitedFileWriter dfw, Node n) throws Exception {

		dfw.writeField(DOMUtils.getString(n, "personFirstName"));
		dfw.writeField(DOMUtils.getString(n, "personMiddleName"));
		dfw.writeField(DOMUtils.getString(n, "personLastName"));
		dfw.writeField(DOMUtils.getString(n, "personSSN"));
		dfw.writeField(DateUtils.getDateFormatted(getDate(DOMUtils.getString(n, "personEffDate"))));
		dfw.writeField(DOMUtils.getString(n, "personMedicareA"));
		dfw.writeField(DOMUtils.getString(n, "personMedicareB"));
		dfw.writeField(DOMUtils.getString(n, "personMedicaid"));
		dfw.writeField(DOMUtils.getString(n, "personOther"));

	}




}
