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

package com.arahant.imports.glaggs;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FixedSizeFileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class MEMBImport extends GLAGSFixedImportBase {

	public void importFile(String fileName) throws Exception
	{
		hsu.dontAIIntegrate();

		preLoadMaps();

		FixedSizeFileReader fr=new FixedSizeFileReader(fileName);

		fr.addField("AgentId", 2);
		fr.addField("GroupNumber", 6);
		fr.addField("SubGroupNumber", 2);
		fr.addField("ContractNumber",  9); //ssn?
		fr.addField("SubscriberName",  18);
		fr.addField("SubscriberDOB",  8);
		fr.addField("ActiveFlag",  1);
		fr.addField("CrossCode",  4); //cross benefits hosp/med/surg
		fr.addField("ShieldCode", 4); //shield beneits provider/drugs/dental/vision
		fr.addField("ConfigCode",  4);  //contract type single/double/family
		fr.addField("SomeFlag", 1);  //TODO: what is this flag?
		fr.addField("Sex", 1);

		fr.addField("Dep1FName",  5);
		fr.addField("Dep1Bday",  8);
		fr.addField("Dep1Active",  1);  //TODO: Verify this
		fr.addField("Dep1Relationship",  1);
		fr.addField("Dep1Sex",  1);
		
		fr.addField("Dep2FName", 5);
		fr.addField("Dep2Bday", 8);
		fr.addField("Dep2Active", 1);
		fr.addField("Dep2Relationship", 1);
		fr.addField("Dep2Sex", 1);
		
		fr.addField("Dep3FName", 5);
		fr.addField("Dep3Bday", 8);
		fr.addField("Dep3Active", 1);
		fr.addField("Dep3Relationship", 1);
		fr.addField("Dep3Sex", 1);

		fr.addField("Dep4FName", 5);
		fr.addField("Dep4Bday", 8);
		fr.addField("Dep4Active", 1);
		fr.addField("Dep4Relationship", 1);
		fr.addField("Dep4Sex", 1);

		fr.addField("Dep5FName", 5);
		fr.addField("Dep5Bday", 8);
		fr.addField("Dep5Active", 1);
		fr.addField("Dep5Relationship", 1);
		fr.addField("Dep5Sex", 1);

		fr.addField("Dep6FName", 5);
		fr.addField("Dep6Bday", 8);
		fr.addField("Dep6Active", 1);
		fr.addField("Dep6Relationship", 1);
		fr.addField("Dep6Sex", 1);


		fr.addField("Dep7FName", 5);
		fr.addField("Dep7Bday",  8);
		fr.addField("Dep7Active",  1);  //TODO: Verify this
		fr.addField("Dep7Relationship", 1);
		fr.addField("Dep7Sex",  1);


		fr.addField("Dep8FName",5);
		fr.addField("Dep8Bday", 8);
		fr.addField("Dep8Active", 1);  //TODO: Verify this
		fr.addField("Dep8Relationship",  1);
		fr.addField("Dep8Sex", 1);


		fr.addField("Dep9FName",  5);
		fr.addField("Dep9Bday", 8);
		fr.addField("Dep9Active",  1);  //TODO: Verify this
		fr.addField("Dep9Relationship",  1);
		fr.addField("Dep9Sex",  1);






		int count=0;
		fr.skipChars(1); //there are 3 garbage chars at start of file

		while (fr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("1-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}

//			//System.out.println("cross benefits "+fr.getField("ServiceCode1"));
//			//System.out.println("shield benefits "+fr.getField("ServiceCode2"));
//			//System.out.println("single double fam "+fr.getField("ServiceCode3"));

			String ref=fr.getField("GroupNumber")+fr.getField("SubGroupNumber");


			String ssn=fixSSN(fr.getField("ContractNumber"));


			Employee emp=null;//TODO: for initial import
		/*	hsu.createCriteria(Employee.class)
					.eq(Employee.SSN, ssn)
					.first();
					*/
			Name name=new Name(fr.getField("SubscriberName"));
			String companyId=insuranceToCompany.get(ref.trim());

			if (isEmpty(companyId))
			{
				System.out.println("*** missing company ref "+ref);
				continue;
			}

		//	System.out.println("Found company ref "+ref);

			hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));

			String ssex=fr.getField("Sex");

			if (isEmpty(ssex))
				ssex="U";

			BEmployee bemp=null;
			if (ssnToPersonId.containsKey(ssn))
			{
				bemp=new BEmployee(ssnToPersonId.get(ssn));
				emp=bemp.getEmployee();
			}
			else

			if (emp==null)
			{
				
				bemp=new BEmployee();
				bemp.create();
				bemp.setFirstName(name.fname);
				bemp.setLastName(name.lname);
				bemp.setMiddleName(name.mname);
				bemp.setSsn(ssn);
				bemp.setSex(ssex);
				bemp.setDob(DateUtils.getDate(sdf.parse(fr.getField("SubscriberDOB"))));
				makeLoginDefaults(bemp);
				//System.out.println("Doing insert");
				try
				{
					bemp.insertNoDefaults();
				}
				catch (Exception e)
				{
					//System.out.println("Skipping");
					continue;
				}

				HrEmplStatusHistory hist=new HrEmplStatusHistory();
				hist.generateId();
				hist.setEmployee(bemp.getEmployee());
				hist.setEffectiveDate(DateUtils.now());
				hist.setHrEmployeeStatus(getStatus(companyId, "Active"));
				hsu.insert(hist);

				//System.out.println("did insert");
				assignToOrgGroup(companyId, false, bemp.getPerson());
				//System.out.println("did assign");
				emp=bemp.getEmployee();
			}
			else
			{
				bemp=new BEmployee(emp);
				bemp.setFirstName(name.fname);
				bemp.setLastName(name.lname);
				bemp.setMiddleName(name.mname);
				bemp.setSex(ssex);
				bemp.setDob(DateUtils.getDate(sdf.parse(fr.getField("SubscriberDOB"))));
				bemp.update();
			}

			//System.out.println("Categories");
			//need blue cross category
			String bcCategory=getCategoryId (companyId, "Blue Cross");

			//need shield category
			String bsCategory=getCategoryId(companyId, "Blue Shield");

			//config is single/double/family

			String configName="Single";

			String conCode=fr.getField("ConfigCode");

			if ("0100".equals(conCode))
				configName="Single";
			else
				if ("0200".equals(conCode))
					configName="Double";
				else
					if ("0300".equals(conCode))
						configName="Family";
					else
						configName="Code "+conCode;



			//System.out.println("Benefits");
			String bsBenefit=getBenefitId(companyId, fr.getField("ShieldCode"), bsCategory, false, ref);
			String bcBenefit=getBenefitId(companyId, fr.getField("CrossCode"), bcCategory, false, ref);

			//System.out.println("configs");
			String bsConfig=getBenefitConfigId(companyId,fr.getField("ShieldCode")+" "+configName,bsBenefit,"",null);
			String bcConfig=getBenefitConfigId(companyId,fr.getField("CrossCode")+" "+ configName, bcBenefit, "", null);

			//System.out.println("bene join");
			makeBenefitJoinIfNotFound(bemp.getPerson(), bemp.getPerson(), bcConfig, DateUtils.now(),0,null);
			makeBenefitJoinIfNotFound(bemp.getPerson(), bemp.getPerson(), bsConfig, DateUtils.now(),0,null);

			for (int loop=1;loop<10;loop++)
			{
				String s="Dep"+loop;


				String fname=fr.getField(s+"FName");

				if (isEmpty(fname))
					break;
				String bday=fr.getField(s+"Bday");
				String active=fr.getField(s+"Active");
				String relationship=fr.getField(s+"Relationship");
				String sex=fr.getField(s+"Sex");

				if ("E1978080".equals(bday))
				{
					fr.printCurrentLine();
					//System.out.println(fname);
				}
				int dob=DateUtils.getDate(sdf.parse(bday));

				//System.out.println("dep");
				Person per=null; //TODO: for import
				/*hsu.createCriteria(Person.class)
					.like(Person.FNAME, fname+"%")
					.eq(Person.LNAME, bemp.getLastName())
					.eq(Person.DOB, dob)
					.joinTo(Person.DEP_JOINS_AS_DEPENDENT)
					.joinTo(HrEmplDependent.EMPLOYEE)
					.eq(Employee.SSN, bemp.getSsn())
					.first();
*/
				//TODO: what if the name changed?

				HrEmplDependent dep=null;
				if (per==null)
				{
					BPerson bp = new BPerson();
					bp.create();
					bp.setSsn("999-99-9999");
					bp.setSex(sex);
					bp.setFirstName(fname);
					bp.setLastName(emp.getLname());
					bp.setMiddleName("");
					bp.setDob(DateUtils.getDate(sdf.parse(bday)));
					bp.insertNoChecks();
					//System.out.println("did dep insert");

					per=bp.getPerson();

					final BHREmplDependent d=new BHREmplDependent();


					//3 is husband
					//4 is wife

					String relType="C";

					if ("3".equals(relationship)||"4".equals(relationship))
						relType="S";
					dep=makeRelationship(per, emp,relType);
					//System.out.println("relat insert done");
					

				}
			/*	else
				{
					BPerson bp = new BPerson(per);
					bp.setSex(sex);
					bp.setFirstName(fname);
					bp.setDob(DateUtils.getDate(sdf.parse(bday)));
					bp.update();

					final BHREmplDependent d=new BHREmplDependent(bemp.getPersonId(), bp.getPersonId());

					String relType="C";

					if ("3".equals(relationship)||"4".equals(relationship))
						relType="S";
					d.setRelationshipType(relType);
					d.update();
				}
			*/
				//System.out.println("Dep joins");
				makeBenefitJoinIfNotFound(bemp.getPerson(), per, bcConfig, DateUtils.now(),0,dep);
				makeBenefitJoinIfNotFound(bemp.getPerson(), per, bsConfig, DateUtils.now(),0,dep);
			}

			//System.out.println("Did one");
		}

		fr.close();

	}


	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			MEMBImport x = new MEMBImport();
			x.importFile("/Users/Arahant/GLAGGS/eddi_sample/MEMB256.txt");
			ArahantSession.getHSU().commitTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
