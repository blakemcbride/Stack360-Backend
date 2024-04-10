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

import com.arahant.beans.Agency;
import com.arahant.beans.Agent;
import com.arahant.beans.AgentJoin;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BAgencyJoin;
import com.arahant.business.BCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class DRCMBImport extends GLAGSFixedImportBase{
	
	private HashMap<String,HashMap<String,String>> beneConfigs=new HashMap<String, HashMap<String, String>>();


	public void importFile(String fileName) throws Exception
	{
		DelimitedFileReader dfr=new DelimitedFileReader(fileName,'\t','"');

		hsu.dontAIIntegrate();

		preLoadMaps();

		System.out.println("Pre load agents");
		for (Agent a : hsu.getAll(Agent.class))
		{
			System.out.println(a.getNameLFM());
			if (!isEmpty(a.getExtRef()))
			{
				agentToPerson.put(Integer.parseInt(a.getExtRef())+"",a);
				Agency x=hsu.createCriteria(Agency.class)
					.ne(Agency.NAME, "Misc")
					.joinTo(Agency.ORGGROUPASSOCIATIONS)
					.eq(OrgGroupAssociation.PERSON,a)
					.first();

				if (x!=null)
					agentToCompany.put(Integer.parseInt(a.getExtRef())+"", x);
			}

		}

		dfr.nextLine();

		int count=0;

		while (dfr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("1-"+count);
				hsu.commitTransaction();
			//	hsu.rollbackTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
			int recordType=dfr.getInt(0);
			if (recordType!=1)
				break;
			String id=dfr.getString(1);

			int xid=Integer.parseInt(id);
			id=xid+"";
			
			if (isDeadCompany(id))
				continue;

			if (groupToCompany.get(id)!=null) //TODO: take out later - just to speed testing for now
				continue;

			String name=dfr.getString(2).trim();
			String addr1=dfr.getString(3);
			String addr2=dfr.getString(4);
			String city=dfr.getString(5);
			String state=dfr.getString(6);
			String zip=dfr.getString(7);
			String county=dfr.getString(8);
			String phone=(dfr.getString(9)+" "+dfr.getString(10)).trim();
			String email=dfr.getString(11);
			String sts=dfr.getString(12);
			String termDt=dfr.getString(13);
			String trsn=dfr.getString(14);
			String origEffDate=dfr.getString(15);



			if (zip.length()>10)
				zip=zip.substring(0, 10);

			if (phone.length()>20)
				phone=phone.substring(0,20);

			int memberCount=0;
			try
			{
				memberCount=dfr.getInt(17);
			}
			catch (Exception e)
			{

			}
			String agentCode="";
			try
			{
				agentCode=dfr.getString(17);
			//	System.out.println(agentCode);
			}
			catch (Exception e)
			{

			}

			CompanyDetail cd=null;
			/*hsu.createCriteria(CompanyDetail.class)
				.eq(CompanyDetail.EXTERNAL_REF, id)
				.first();
			*/
			name=fixGroupName(name);
			BCompany company;
			if (cd==null)
			{
				company=new BCompany();
				company.create();
				company.setName(name);
				company.setExtRef(id);
				company.insert();

				if (!isEmpty(agentCode))
				{
					Agent a=null;
					try
					{
						a=agentToPerson.get(Integer.parseInt(agentCode)+"");
					}
					catch (Exception e)
					{
						System.out.println("Could not parse "+agentCode);//skip it
					}
					if (a!=null)
					{
						AgentJoin aj=new AgentJoin();
						aj.generateId();
						aj.setAgent(a);
						aj.setCompany(company.getBean());
						hsu.insert(aj);


						Agency x=agentToCompany.get(Integer.parseInt(agentCode)+"");

						if (x!=null)
						{
							BAgencyJoin baj=new BAgencyJoin();
							baj.create();
							baj.setCompany(company);
							baj.setAgency(x);
							baj.insert();
						}


					}
					else
					{
						System.out.println("Could not find "+agentCode);
					}
				}

			}
			else
			{
				company=new BCompany(cd);
			}

			company.setName(name);
			company.setMainPhoneNumber(phone);
			if (company.getMainContactLname()==null || company.getMainContactLname().trim().equals(""))
			{
				company.setMainContactFname(".");
				company.setMainContactLname(".");
			}
			company.setStreet(addr1);
			company.setStreet2(addr2);
			company.setCity(city);
			company.setState(state);
			company.setZip(zip);
			company.setCounty(county);
			company.setMainContactPersonalEmail(email);
			company.update();

			getStatus(company.getOrgGroupId(), "Active");
			getStatus(company.getOrgGroupId(), "Inactive");

			if (isCombineId(id))
				groupToCompany.put(id, company.getOrgGroupId());
		}


		while (dfr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("2-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
			int recordType=dfr.getInt(0);
			if (recordType!=2)
				break;
			String id=dfr.getString(1);
			String id2=dfr.getString(2);
			String name=dfr.getString(3).trim();
			String addr1=dfr.getString(4);
			String addr2=dfr.getString(5);
			String city=dfr.getString(6);
			String state=dfr.getString(7);
			String zip=dfr.getString(8);
			String county=dfr.getString(9);
			String phone=(dfr.getString(10)+" "+dfr.getString(11)).trim();
			String email=dfr.getString(12);
		/*	String sts=dfr.getString(13);
			String termDt=dfr.getString(14);
			String trsn=dfr.getString(15);
			String origEffDate=dfr.getString(16);
		*/

			int xid=Integer.parseInt(id);
			id=xid+"";

			if (groupToCompany.get(id)!=null) //TODO: take out later - just to speed testing for now
				continue;

			if (zip.length()>10)
				zip=zip.substring(0, 10);

			if (phone.length()>20)
				phone=phone.substring(0,20);

			name=fixGroupName(name);
			int memberCount=0;
			try
			{
				memberCount=dfr.getInt(17);
			}
			catch (Exception e)
			{

			}
			String agentCode="";
			try
			{
				agentCode=dfr.getString(13);
			}
			catch (Exception e)
			{

			}
			CompanyDetail cd=null;
			/*hsu.createCriteria(CompanyDetail.class)
				.eq(CompanyDetail.EXTERNAL_REF, id)
				.first();
				*/
			BCompany company;
			if (cd==null)
			{
				company=new BCompany();
				company.create();
				company.setName(name);
				company.setExtRef(id);
				company.insert();
				if (isCombineId(id))
					groupToCompany.put(id, company.getOrgGroupId());


				if (!isEmpty(agentCode))
				{
					try
					{
						Agent a=agentToPerson.get(Integer.parseInt(agentCode)+"");
						if (a!=null)
						{
							AgentJoin aj=new AgentJoin();
							aj.generateId();
							aj.setAgent(a);
							aj.setCompany(company.getBean());
							hsu.insert(aj);
						}
					}
					catch (Exception e)
					{
						//skip it
					}
				}

			}
			else
			{
				company=new BCompany(cd);
			}

			company.setName(name);
			company.setMainPhoneNumber(phone);
			if (company.getMainContactLname()==null || company.getMainContactLname().trim().equals(""))
			{
				company.setMainContactFname(".");
				company.setMainContactLname(".");
			}
			company.setStreet(addr1);
			company.setStreet2(addr2);
			company.setCity(city);
			company.setState(state);
			company.setZip(zip);
			company.setCounty(county);
			company.setMainContactPersonalEmail(email);
			company.update();
		}


		while (dfr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("3-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
			int recordType=dfr.getInt(0);
			if (recordType!=3)
				break;

			String companyRef=dfr.getString(1);

			int xid=Integer.parseInt(companyRef);
			companyRef=xid+"";

			String companyId=groupToCompany.get(companyRef);

			hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));

			String statusName=dfr.getString(5).trim();

			if (statusName.length()>20)
				statusName=statusName.substring(0, 20);

			boolean cobra="COBRA".equals(statusName);
			//find status id
			String beneCode=dfr.getString(3);



			String categoryName=dfr.getString(7);
			String benefitName=dfr.getString(8);
			String configName=dfr.getString(9);
			String desc=dfr.getString(10);
			if (benefitName.length()>60)
				benefitName=benefitName.substring(0,60);
			if (desc.length()>60)
				desc=desc.substring(0,60);  //TODO: see about making desc longer

			String categoryId=getCategoryId(companyId, categoryName);

			String benefitId=getBenefitId(companyId, benefitName, categoryId, cobra, companyRef);

			String benefitClassId=getBenefitClassId(companyId, statusName);

			String configId=getBenefitConfigId(companyId, configName, benefitId, desc, benefitClassId);


			if (beneConfigs.get(companyId)==null)
				beneConfigs.put(companyId, new HashMap<String, String>());

			beneConfigs.get(companyId).put(beneCode,configId);


		}

		while (dfr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("4-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
			int recordType=dfr.getInt(0);
			if (recordType!=4)
				break;


			String ref=dfr.getString(1);

			int x=Integer.parseInt(ref);
			ref=x+"";
			//this is contacts
			String companyId=groupToCompany.get(ref);

			if (isEmpty(companyId))
			{
				System.out.println("Unknown company ref "+ref);
				continue;
			}

			hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));

			String contactLevel=dfr.getString(4);
			String desc=dfr.getString(5);
			String lastName=dfr.getString(6);
			String firstName=dfr.getString(7);
			String middleName=dfr.getString(8);
			String title=dfr.getString(9);
			String phone=(dfr.getString(10)+" "+dfr.getString(11)).trim();
			String fax;

				try
				{
					Integer.parseInt(title);
					title="";
				}
				catch (Exception e)
				{

				}

			if (title.length()>20)
				title=title.substring(0,20);

			if (middleName.length()>20)
				middleName=middleName.substring(0,20);
			
			if (isEmpty(lastName))
				lastName=".";
			if (isEmpty(firstName))
				firstName=".";
			
			try
			{
				fax=(dfr.getString(12)+" "+dfr.getString(13)).trim();
			}
			catch (Exception e)
			{
				fax="";
			}
			
			Employee emp=null;
			/*hsu.createCriteria(Employee.class)
				.eq(Employee.FNAME, firstName)
				.eq(Employee.LNAME, lastName)
				.eq(Employee.MNAME, middleName)
				.first();
			*/
			if (emp==null)
			{
				BEmployee bemp=new BEmployee();
				bemp.create();
				bemp.setFirstName(firstName);
				bemp.setLastName(lastName);
				bemp.setMiddleName(middleName);

				bemp.setJobTitle(title);
				bemp.setWorkPhone(phone);
				bemp.setWorkFax(fax);
				makeLoginDefaults(bemp);
				try
				{
					bemp.insert();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}

				assignToOrgGroup(companyId, true, bemp.getEmployee());
			}
		}

		while (dfr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("5-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
			int recordType=dfr.getInt(0);
			if (recordType!=5)
				break;

			//this is contacts
			String ref=dfr.getString(1);
			int x=Integer.parseInt(ref);
			ref=x+"";

			if (isDeadCompany(ref))
			{
				System.out.println("**** Member found for dead company "+ref);
				continue;
			}

			String companyId=groupToCompany.get(ref);
			hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));

			String subscriberSSN=fixSSN(dfr.getString(3));

			int subCount=dfr.getInt(4);
			String relationship=dfr.getString(5);

			String firstName=dfr.getString(6);
			String middleName=dfr.getString(7);
			String lastName=dfr.getString(8);

			String bday=dfr.getString(9);
			String sex=dfr.getString(10);

			String status=dfr.getString(11);
			String what=dfr.getString(12);

			String effectiveDate=dfr.getString(13);
			String termDate=dfr.getString(14);

			String bene1=dfr.getString(16);
			String bene2=dfr.getString(17);
			String bene3=dfr.getString(18);
			String bene4=dfr.getString(19);

			int start=DateUtils.getDate(sdfSlashes.parse(effectiveDate));
			int end=0;

			try
			{
				if (termDate!=null && !termDate.trim().equals("") )
					end=DateUtils.getDate(sdfSlashes.parse(termDate));
			}
			catch (Exception e)
			{
				//doesn't have one
			}
			if (subCount==1)
			{

				Employee emp=null;
		 /*hsu.createCriteria(Employee.class)
					.eq(Employee.FNAME, firstName)
					.eq(Employee.LNAME, lastName)
					.eq(Employee.MNAME, middleName)
					.first();

				if (emp==null)
				{
					emp=hsu.createCriteria(Employee.class)
						.eq(Employee.SSN, subscriberSSN)
						.first();
				}
		 */

				if (ssnToPersonId.containsKey(subscriberSSN))
				{
					BEmployee bemp=new BEmployee(ssnToPersonId.get(subscriberSSN));

					HrEmplStatusHistory hist=new HrEmplStatusHistory();
					hist.generateId();
					hist.setEmployee(bemp.getEmployee());
					hist.setEffectiveDate(DateUtils.now());
					hist.setHrEmployeeStatus(getStatus(companyId, status));
					hsu.insert(hist);
					
					if (!"NA".equals(bene1.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene1), start,end,null);

					if (!"NA".equals(bene2.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene2), start,end,null);

					if (!"NA".equals(bene3.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene3), start,end,null);

					if (!"NA".equals(bene4.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene4), start,end,null);


					continue;

				}
				BEmployee bemp=null;
				if (emp==null)
				{
					bemp=new BEmployee();
					bemp.create();
					bemp.setFirstName(firstName);
					bemp.setLastName(lastName);
					bemp.setMiddleName(middleName);
					bemp.setSsn(subscriberSSN);
					bemp.setDob(DateUtils.getDate(sdfSlashes.parse(bday)));
					makeLoginDefaults(bemp);
					bemp.insertNoDefaults();
					emp=bemp.getEmployee();
					assignToOrgGroup(companyId, false,emp);


					HrEmplStatusHistory hist=new HrEmplStatusHistory();
					hist.generateId();
					hist.setEmployee(bemp.getEmployee());
					hist.setEffectiveDate(DateUtils.now());
					hist.setHrEmployeeStatus(getStatus(companyId, status));
					hsu.insert(hist);

					
					if (!"NA".equals(bene1.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene1), start,end,null);

					if (!"NA".equals(bene2.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene2), start,end,null);

					if (!"NA".equals(bene3.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene3), start,end,null);

					if (!"NA".equals(bene4.trim()))
						makeBenefitJoinIfNotFound(bemp.getEmployee(), bemp.getEmployee(), beneConfigs.get(companyId).get(bene4), start,end,null);

				}
				else
				{
					bemp=new BEmployee(emp);
					bemp.setSsn(subscriberSSN);
					bemp.setSex(sex);
					bemp.setFirstName(firstName);
					bemp.setLastName(lastName);
					bemp.setMiddleName(middleName);
					bemp.setDob(DateUtils.getDate(sdfSlashes.parse(bday)));
					bemp.update();
				}
				
				//TODO: what if changed from one company to another?!

				ssnToPersonId.put(subscriberSSN, bemp.getPersonId());


			}
			else //this is a dependent
			{
				Person per=null;
				/*hsu.createCriteria(Person.class)
					.eq(Person.FNAME, firstName)
					.eq(Person.LNAME, lastName)
					.eq(Person.MNAME, middleName)
					.joinTo(Person.DEP_JOINS_AS_DEPENDENT)
					.joinTo(HrEmplDependent.EMPLOYEE)
					.eq(Employee.SSN, subscriberSSN)
					.first();
*/
				//TODO: what if the name changed?

				if (per==null)
				{
					BPerson bp = new BPerson();
					bp.create();
					bp.setSsn(null);
					bp.setSex(sex);
					bp.setFirstName(firstName);
					bp.setLastName(lastName);
					bp.setMiddleName(middleName);
					bp.setDob(DateUtils.getDate(sdfSlashes.parse(bday)));
					bp.insert();

					String relType="O";
					if ("S".equals(relationship)||"D".equals(relationship))
						relType="C";
					if ("H".equals(relationship)||"W".equals(relationship))
						relType="S";

					Employee emp=hsu.get(Employee.class, ssnToPersonId.get(subscriberSSN));
					HrEmplDependent dep=makeRelationship(bp.getPerson(), emp, relType);

					if (!"NA".equals(bene1.trim()))
						makeBenefitJoinIfNotFound(emp, bp.getPerson(), beneConfigs.get(companyId).get(bene1), start,end,dep);

					if (!"NA".equals(bene2.trim()))
						makeBenefitJoinIfNotFound(emp, bp.getPerson(), beneConfigs.get(companyId).get(bene2), start,end,dep);

					if (!"NA".equals(bene3.trim()))
						makeBenefitJoinIfNotFound(emp, bp.getPerson(), beneConfigs.get(companyId).get(bene3), start,end,dep);

					if (!"NA".equals(bene4.trim()))
						makeBenefitJoinIfNotFound(emp, bp.getPerson(), beneConfigs.get(companyId).get(bene4), start,end,dep);


				}
				else
				{
					BPerson bp = new BPerson(per);
					bp.setSex(sex);
					bp.setFirstName(firstName);
					bp.setLastName(lastName);
					bp.setMiddleName(middleName);
					bp.setDob(DateUtils.getDate(sdfSlashes.parse(bday)));
					bp.update();

					final BHREmplDependent d=new BHREmplDependent(ssnToPersonId.get(subscriberSSN), bp.getPersonId());

					String relType="O";
					if ("S".equals(relationship)||"D".equals(relationship))
						relType="C";
					if ("H".equals(relationship)||"W".equals(relationship))
						relType="S";
					d.setRelationshipType(relType);
					d.updateNoCheck();
				}
			}
		}
	}


	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			DRCMBImport x = new DRCMBImport();
			x.importFile("/Users/Arahant/GLAGGS/eddi_sample/DRCMB255.csv");
			ArahantSession.getHSU().commitTransaction();
		//	ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
