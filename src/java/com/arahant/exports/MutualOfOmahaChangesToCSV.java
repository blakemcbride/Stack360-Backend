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
public class MutualOfOmahaChangesToCSV {
	public void dumpPcr(DelimitedFileWriter dfw, BPerson bp, Date date) throws Exception
	{
		PersonChangeRequest pcr = ArahantSession.getHSU()
			.createCriteria(PersonChangeRequest.class)
			.eq(PersonChangeRequest.PERSON, bp.getPerson())
			.ge(PersonChangeRequest.REQUEST_DATE, date)
			.eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_MUTUAL_OF_OMAHA)
			.orderByDesc(PersonChangeRequest.REQUEST_DATE)
			.first();

		if (pcr==null)
			return;

		Document doc = DOMUtils.createDocument(pcr.getRequestData());
		Node n = DOMUtils.getNode(doc, "MUTUALDATA");


		if(DOMUtils.getString(n, "lifeAdndEmployee").equals("FALSE") &&
			DOMUtils.getString(n, "lifeAdndChild").equals("FALSE") &&
			DOMUtils.getString(n, "lifeAdndSpouse").equals("FALSE")
			)
			return;


		dfw.writeField(DOMUtils.getString(n, "employeeLastName"));
		dfw.writeField(DOMUtils.getString(n, "employeeFirstName"));
		dfw.writeField(DOMUtils.getString(n, "employeeMiddleName"));
		dfw.writeField(DOMUtils.getString(n, "employeeSSN"));
		dfw.writeField(DateUtils.getDateFormatted(DOMUtils.getInt(n, "employeeDOB")));
		dfw.writeField(DOMUtils.getString(n, "employeeSex"));
	//	dfw.writeField(DOMUtils.getString(n, "employeeAddress"));

	//	dfw.writeField(bp.getCity());
	//	dfw.writeField(bp.getState());
	//	dfw.writeField(bp.getZip());

	//	dfw.writeField(DOMUtils.getString(n, "employeeSalary"));
		write(dfw,DOMUtils.getString(n, "lifeAdndEmployee"));


		write(dfw,DOMUtils.getString(n, "std"));

		write(dfw,DOMUtils.getString(n, "ltd"));

		String married = DOMUtils.getString(n, "employeeMaritalStatus");
		if (married.equals("M")) {
			married = "Married";
		}
		if (married.equals("D")) {
			married = "Divorced";
		}
		if (married.equals("W")) {
			married = "Widowed";
		}
		if (married.equals("S")) {
			married = "Single";
		}
//		dfw.writeField(married);

		write(dfw,DOMUtils.getString(n, "basicLifeAdndEmployee"));

		write(dfw,DOMUtils.getString(n, "lifeAdndEmployeeBen"));
	//	write(dfw,DOMUtils.getString(n, "lifeAdndEmployeePrem"));
		write(dfw,DOMUtils.getString(n, "lifeAdndSpouse"));
		write(dfw,DOMUtils.getString(n, "lifeAdndSpouseBen"));
	//	write(dfw,DOMUtils.getString(n, "lifeAdndSpousePrem"));
		write(dfw,DOMUtils.getString(n, "lifeAdndChild"));


		write(dfw,DOMUtils.getString(n, "lifeAdndChildBen"));
	//	write(dfw,DOMUtils.getString(n, "lifeAdndChildPrem"));



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

		for (int loop=depCount;loop<MAX_DEPS;loop++)
			for (int fields=0;fields<DEP_FIELD_COUNT;fields++)
				dfw.writeField("");



		dfw.endRecord();
	}

	private void write(DelimitedFileWriter dfw, String val) throws Exception
	{
		if (val.equals("NaN"))
			val="";
		if (val.equals("TRUE"))
			val="Yes";
		if (val.equals("FALSE"))
			val="No";
		dfw.writeField(val);
	}

	private final int DEP_FIELD_COUNT=5;
	private final int MAX_DEPS=5;

	public void writeHeader(DelimitedFileWriter dfw) throws Exception
	{
		dfw.writeField("employeeLastName");
		dfw.writeField("employeeFirstName");
		dfw.writeField("employeeMiddleName");
		dfw.writeField("employeeSSN");
		dfw.writeField("employeeDOB");
		dfw.writeField("employeeSex");
//		dfw.writeField("employeeAddress");

//		dfw.writeField("employeeCity");
//		dfw.writeField("employeeState");
//		dfw.writeField("employeeZip");

	//	dfw.writeField("employeeSalary");
		dfw.writeField("lifeAdndEmployee");


		dfw.writeField("std");

		dfw.writeField("ltd");

	//	dfw.writeField("employeeMaritalStatus");

		dfw.writeField("basicLifeAdndEmployee");

		dfw.writeField("lifeAdndEmployeeBen");
	//	dfw.writeField("lifeAdndEmployeePrem");
		dfw.writeField("lifeAdndSpouse");
		dfw.writeField("lifeAdndSpouseBen");
	//	dfw.writeField("lifeAdndSpousePrem");
		dfw.writeField("lifeAdndChild");


		dfw.writeField("lifeAdndChildBen");
	//	dfw.writeField("lifeAdndChildPrem");

		for (int loop=1;loop<=MAX_DEPS;loop++)
		{
			dfw.writeField("dep_"+loop+"_FirstName");
			dfw.writeField("dep_"+loop+"_MiddleName");
			dfw.writeField("dep_"+loop+"_LastName");
		//	dfw.writeField("dep_"+loop+"_DOB");
			dfw.writeField("dep_"+loop+"_Relationship");
			dfw.writeField("dep_"+loop+"_BenefitPercent");
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
		DelimitedFileWriter dfw=new DelimitedFileWriter("MUTUALOFOMAHA.csv");

		writeHeader(dfw);

		Calendar start=Calendar.getInstance();
		start.set(Calendar.MONTH, Calendar.NOVEMBER);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.YEAR,2009);
		start.set(Calendar.HOUR, 0);
		Date date=start.getTime();

		HibernateSessionUtil hsu=ArahantSession.getHSU();

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
						.selectFields(Person.PERSONID)
						.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).orderBy(Person.SSN)
						.joinTo(Person.CHANGE_REQUESTS)
					//	.ge(PersonChangeRequest.REQUEST_DATE, date)
						.eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_MUTUAL_OF_OMAHA)
						.orderBy(PersonChangeRequest.REQUEST_DATE);



		HashSet<String> ids=new HashSet<String>();
		ids.addAll((List)hcu.list());

		System.out.println("ID count "+ids.size());

		int count=0;
		for (String id : ids)
		{
			System.out.println(++count);
			BPerson bp=new BPerson(id);

			dumpPcr(dfw, bp, date);
		}

/*
		List <Person> l=hsu.createCriteria(Person.class)
			.notIn(Person.PERSONID, ids)
			.joinTo(Person.CHANGE_REQUESTS)
			.list();

		//everybody gets mutual of omaha

		for (Person p : l)
		{
			BPerson bp=new BPerson(p);
			dfw.writeField(bp.getLastName());
			dfw.writeField(bp.getFirstName());
			dfw.writeField(bp.getMiddleName());
			dfw.writeField(bp.getSsn());
			dfw.writeField(DateUtils.getDateFormatted(bp.getDob()));
			dfw.writeField(bp.getSex());
			dfw.writeField(bp.getStreet());

			dfw.writeField(bp.getCity());
			dfw.writeField(bp.getState());
			dfw.writeField(bp.getZip());

			dfw.writeField("0");
			dfw.writeField("FALSE");


			dfw.writeField("TRUE");

			dfw.writeField("TRUE");

			dfw.writeField("");

			dfw.writeField("TRUE");

			dfw.writeField("FALSE");
			dfw.writeField("FALSE");
			dfw.writeField("FALSE");
			dfw.writeField("FALSE");
			dfw.writeField("FALSE");
			dfw.writeField("FALSE");


			dfw.writeField("FALSE");
			dfw.writeField("FALSE");

			dfw.endRecord();

		}
 */

		//scr.close();
 
		dfw.close();

	}



	public static void main(String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			new MutualOfOmahaChangesToCSV().run();
			ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
			ArahantSession.getHSU().rollbackTransaction();

		}



	}

	private void writeDependent(DelimitedFileWriter dfw, Node n) throws Exception {
		write(dfw,DOMUtils.getString(n, "personFirstName"));
		write(dfw,DOMUtils.getString(n, "personMiddleName"));
		write(dfw,DOMUtils.getString(n, "personLastName"));
	//	write(dfw,DateUtils.getDateFormatted(getDate(DOMUtils.getString(n, "personDOB"))));
		write(dfw,DOMUtils.getString(n, "personRelationship"));
		write(dfw,DOMUtils.getString(n, "personBenPerc"));

	}



}
