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
import com.arahant.beans.HrBenefitConfig;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ImportInsuranceCodes extends GLAGSFixedImportBase {

	private HashMap<String,HashMap<String,String>> beneConfigs=new HashMap<String, HashMap<String, String>>();


	public void importFile(String fileName) throws Exception
	{
		DelimitedFileReader dfr=new DelimitedFileReader(fileName,'\t','"');

		hsu.dontAIIntegrate();

		preLoadMaps();

	/*	System.out.println("Pre load agents");
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

		
*/
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
	/*
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
]
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
			if (company.getMainContactLName()==null || company.getMainContactLName().trim().equals(""))
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
	 *
	 *
	 */
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
			/*
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
			if (company.getMainContactLName()==null || company.getMainContactLName().trim().equals(""))
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
			 * 
			 */
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

			HrBenefitConfig c=hsu.get(HrBenefitConfig.class, configId);

			c.setInsuranceCode(beneCode);

			hsu.saveOrUpdate(c);

		}

	}


	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			ImportInsuranceCodes x = new ImportInsuranceCodes();
			x.importFile("/Users/Arahant/GLAGGS/eddi_sample/DRCMB255.csv");
			ArahantSession.getHSU().commitTransaction();
		//	ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(ImportInsuranceCodes.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
