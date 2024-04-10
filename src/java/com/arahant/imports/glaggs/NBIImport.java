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
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.business.BAgencyJoin;
import com.arahant.business.BCompany;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FixedSizeFileReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class NBIImport extends GLAGSFixedImportBase {

	
	
	public void importFile(String fileName) throws Exception
	{
		hsu.dontAIIntegrate();
		hsu.noAutoFlush();


		//create a benefit change reason
		if (hsu.createCriteria(HrBenefitChangeReason.class)
				.eq(HrBenefitChangeReason.DESCRIPTION,"Miscellaneous")
				.exists())
		{
			BHRBenefitChangeReason cr=new BHRBenefitChangeReason();
			cr.create();
			cr.setDescription("Miscellaneous");
			cr.setTypeId(HrBenefitChangeReason.INTERNAL_STAFF_EDIT);
			cr.insert();
		}

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

		System.out.println ("Open reader");
		FixedSizeFileReader fr=new FixedSizeFileReader(fileName);

		System.out.println ("Open reader 2");

		fr.addField("ClusterCode", 1, 2);
		fr.addField("GroupNumber", 3, 5);
		fr.addField("GroupSuffix", 8, 3);
		fr.addField("GroupStatus", 11, 5);
		fr.addField("GroupName", 16, 28);
		fr.addField("Street", 44, 28);
		fr.addField("City", 72, 21);
		fr.addField("State", 93, 2);
		fr.addField("Zip", 95, 9);
		fr.addField("Contact", 104, 28);
		fr.addField("ContactTitle", 132, 10);
		fr.addField("Phone", 142, 10);

		/*
		 * The 15-digit County Name field starting at position 152 needs to be split into three separate fields.

3-digit "County Code"
6-digit "County Name"
6-digit "Customer ID"
		 */
		fr.addField("County", 155, 6);
		fr.addField("CID",161,6);
		fr.addField("BCBSMCluster", 167, 4);
		fr.addField("SICCD", 171, 4);
		fr.addField("RPT Effective Date", 175, 8);
		fr.addField("Orig Effective Date", 183, 8);
		fr.addField("Renewal Date", 191, 8);
		fr.addField("NewHireType", 199, 5);
		fr.addField("DecisionMaker", 204, 28);
		fr.addField("DecisionMakerTitle", 232, 10);
		fr.addField("IneligibleSegment", 242, 20);
		fr.addField("ConsultantName", 262, 28);
		fr.addField("ConsultantTitle", 290, 10);
	/*
	 * The 10-digit Consultant Title field starting at position 290 needs to be split into three separate fields.

2-digit "MA Code"
3-digit "Agency Code"
5-digit "Agent ID"
 */
//1700009026G1030DRC/SENECA PARTNERS INC     300 PARK STREET SUITE 400   BIRMINGHAM           MI48009    DARLENE MILLER              OFFICE MGR2487236650063OAKLAN40436913176211200608012006070120080701SP3  MICHAEL SKAFF               OWNER                         GERARD J MISERENDINO        06874011402008070105 NO CURRENT HEALTHCARE PLAN   G
//1700009040D9999DRC/CONSTRUCTION VIDEO MEDI 111 EAST NEWBERRY STREET    ROMEO                MI48065    KAREN STACHKUNIS            OFFICE MGR5867522580050MACOMB40437413177812200710112006061120090611S60  DWIGHT SPENGLER             OWNER                         DAVID N SHEERAN             064060941000000000   BLUE CROSS BLUE SHIELD OF MI 0

//1753865000D9999DRC/MAENGINEERING INC       200 E. BROWN STREET         BIRMINGHAM           MI48009    SALIM SESSINE               PRESIDENT 2482581610063OAKLAN14669813178911200812011989120120091201S30  SALIM SESSINE               PRESIDENT PART TIME           THEODORE SOUPHIS            030010287500000000                                0
//1753934001G1030DRCC/FISHER INSURANCE AGENCY2567 METROPOLITAN PARKWAY   STERLING HTS         MI48311    LARRY BELLIS                          5869791330050MACOMB13503514176411200510011989120120051201S90  LARRY BELLIS
		fr.addField("MACode", 290, 2);
		fr.addField("AgencyId", 292, 3);
		fr.addField("AgentId", 295,5);
		fr.addField("CancelEffDate", 300, 8);
		fr.addField("CancelReason", 308, 3);
		fr.addField("OtherCarrier", 111, 28);
		fr.addField("CertificateRiders", 339, 1); //Type of medical coverage cancelled. T=Traditional; P=PPO; S=POS
		fr.addField("Action", 340, 1);


		int count=0;
	//	fr.skipChars(3); //there are 3 garbage chars at start of file

		System.out.println ("did skip");
		Date last=new Date();
		while (fr.nextLine())
		{

	//		System.out.println(fr.getField("MACode"));
	//		if (true)
	//			continue;

			if (++count%100==0)
			{
				Date now=new Date();
				System.out.println("1-"+count+"  "+((now.getTime()-last.getTime())/1000));
				last=now;
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}
//fr.printCurrentLine();
			String ref=fr.getField("CID");


		//	if (isDeadCompany(ref))
		//		continue;

			String name=fr.getField("GroupName");

			name=fixGroupName(name);

		//	System.out.println(name+" = "+ref);
			
			CompanyDetail cd=null;

			String secId=(fr.getField("GroupNumber")+fr.getField("GroupSuffix")).trim();

			
			if (!isEmpty(ref) && groupToCompany.get(ref)!=null) 
			{
				if (!insuranceToCompany.containsKey(secId))
				{
					String companyId=groupToCompany.get(secId);
					hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));
					getBenefitId(companyId, secId, getCategoryId(companyId, "Import Use Only"), false, secId);
					insuranceToCompany.put(secId, companyId);
				}
				continue;
			}

		
			BCompany company;
			if (cd==null)
			{
				company=new BCompany();
				company.dontAutoOwn();
				String companyId=company.create();
				company.setExtRef(secId);

				setData(company,fr,name);
				company.insert();

				hsu.setCurrentCompany(company.getBean());

				getBenefitId(companyId, secId, getCategoryId(companyId, "Import Use Only"), false, secId);
				insuranceToCompany.put(secId, companyId);
				

				String managingAgentId=fr.getField("MACode").trim();

				try
				{
					if (!isEmpty(managingAgentId))
						managingAgentId=Integer.parseInt(managingAgentId)+"";
				}
				catch (Exception e)
				{
					System.out.println("Bad managing agent id "+managingAgentId);
				}

				if (!isEmpty(managingAgentId) && agencyToCompany.get(managingAgentId)!=null)
				{
					BAgencyJoin baj=new BAgencyJoin();
					baj.create();
					baj.setCompany(company);
					baj.setAgency(agencyToCompany.get(managingAgentId));
					baj.insert();

				}

				String agentId=fr.getField("AgentId").trim();

				try
				{
					if (!isEmpty(agentId))
						agentId=Integer.parseInt(agentId)+"";
				}
				catch (Exception e)
				{
					System.out.println("Bad agent id "+managingAgentId);
				}

				if (!isEmpty(agentId))
				{
					Agent a=null;
					try
					{
						a=agentToPerson.get(Integer.parseInt(agentId)+"");
					}
					catch (Exception e)
					{
						System.out.println("Agent id failed parse "+agentId);
					}
					if (a!=null)
					{
						AgentJoin aj=new AgentJoin();
						aj.generateId();
						aj.setAgent(a);
						aj.setCompany(company.getBean());
						hsu.insert(aj);

						Agency x=agentToCompany.get(Integer.parseInt(agentId)+"");

						if (x!=null)
						{
							BAgencyJoin baj=new BAgencyJoin();
							baj.create();
							baj.setCompany(company);
							baj.setAgency(x);
							baj.insert();
						}

					//	System.out.println("Found agent");
					}
					else
					{
						System.out.println("Agent join not found "+agentId);
					}
				}
				else
				{
					System.out.println("Missing agent id");
				}

			}
			else
			{
				company=new BCompany(cd);
				setData(company,fr,name);
				company.update();
			}


			if (!isEmpty(ref) && isCombineId(ref))
				groupToCompany.put(ref, company.getOrgGroupId());

			insuranceToCompany.put(secId, company.getOrgGroupId());

			getStatus(company.getOrgGroupId(), "Active");
			getStatus(company.getOrgGroupId(), "Inactive");

			
		}

		hsu.executeSQL("update org_group set owning_entity_id=org_group_id where org_group_id in (select org_group_id from company_detail)");


		fr.close();

	}

	final private void setData(BCompany company, FixedSizeFileReader fr, String name)
	{

		company.setName(name);
		company.setMainPhoneNumber(fr.getField("Phone"));

		company.setStreet(fr.getField("Street"));
		company.setStreet2("");
		company.setCity(fr.getField("City"));
		company.setState(fr.getField("State"));
		company.setZip(fr.getField("Zip"));
		company.setCounty(fr.getField("County"));




		String contactName=fr.getField("Contact");
		String contactTitle=fr.getField("ContactTitle");

		if (contactTitle.length()>20)
			contactTitle=contactTitle.substring(0,20);
		if (isEmpty(contactName))
		{
			contactName=fr.getField("DecisionMaker");
			contactTitle=fr.getField("DecisionMakerTitle");
		}

		Name cname=new Name(contactName);
		company.setMainContactFname(cname.fname);
		company.setMainContactLname(cname.lname);
		company.setMainContactMname(cname.mname);
		company.setMainContactJobTitle(contactTitle);

		if (company.getMainContactLname()==null || company.getMainContactLname().trim().equals(""))
		{
			company.setMainContactFname(".");
			company.setMainContactLname(".");
		}
 
	}

	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			NBIImport x = new NBIImport();
			x.importFile("/Users/Arahant/GLAGGS/eddi_sample/NBI252.txt");
			ArahantSession.getHSU().commitTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
