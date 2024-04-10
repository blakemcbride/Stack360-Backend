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


/**
 *
 *
 */

package com.arahant.imports.glaggs;

import com.arahant.beans.Agency;
import com.arahant.beans.AgencyJoin;
import com.arahant.beans.Agent;
import com.arahant.beans.AgentJoin;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BAgency;
import com.arahant.business.BAgencyJoin;
import com.arahant.business.BAgent;
import com.arahant.business.BAgentJoin;
import com.arahant.business.BCompany;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;

public class AgentImportXML extends GLAGSFixedImportBase {

	 private static final transient ArahantLogger logger = new ArahantLogger(
            AgentImportXML.class);

	public void importFile(String fileName) throws Exception
	{
		//for speed purposes
		hsu.dontAIIntegrate();
		hsu.noAutoFlush();

		List<AgentJoin> agentJoins = new ArrayList<AgentJoin>();
		List<AgencyJoin> agencyJoins = new ArrayList<AgencyJoin>();

		Document doc = DOMUtils.createDocument(new File(fileName));

		NodeList agencyList=DOMUtils.getNodes(doc, "//Agency");

		//for all agencies
		for (int loop=0;loop<agencyList.getLength();loop++)
		{
			Node agency=agencyList.item(loop);

			String agencyName=DOMUtils.getString(agency, "AgencyName");
			String agencyId=DOMUtils.getString(agency, "AgencyId");
			String agencyAddress1=DOMUtils.getString(agency, "AddressLine1");
			String agencyAddress2=DOMUtils.getString(agency, "AddressLine2");
			String agencyCity=DOMUtils.getString(agency, "City");
			String agencyState=DOMUtils.getString(agency, "State");
			String agencyZip=DOMUtils.getString(agency, "Zip");

//			System.out.println("Agency Name: " + agencyName);
//			System.out.println("Agency Id: " + agencyId);
//			System.out.println("Agency Address 1: " + agencyAddress1);
//			System.out.println("Agency Address 2: " + agencyAddress2);
//			System.out.println("Agency City: " + agencyCity);
//			System.out.println("Agency State: " + agencyState);
//			System.out.println("Agency Zip: " + agencyZip);

			BAgency newAgency=new BAgency();

			//Create a new agency if one does not already exist
			if (!hsu.createCriteria(Agency.class).eq(Agency.EXTERNAL_REF, agencyId).exists())
			{
				newAgency.create();

				newAgency.setName(agencyName);
				newAgency.setAgencyExternalId(agencyId);
				newAgency.setStreet(agencyAddress1);
				newAgency.setStreet2(agencyAddress2);
				newAgency.setCity(agencyCity);
				newAgency.setState(agencyState);
				newAgency.setZip(agencyZip);

				newAgency.insert();
			}
			else
				newAgency = new BAgency(hsu.createCriteria(Agency.class).eq(Agency.AGENCY_EXTERNAL_ID, agencyId).first());

			//get all company ids for agency
			NodeList compIds=DOMUtils.getNodes(agency, "BCBSCompanyIds/BCBSCompanyId");

			for (int i=0;i<compIds.getLength();i++)
			{
				Node compId=compIds.item(i);

				String id = DOMUtils.getNodeValue(compId);

				//System.out.println("Agency Company Id " + i + ": " + id);

				//for every bcbs id
				//find the vendor by account
				VendorCompany vc = hsu.createCriteria(VendorCompany.class).eq(VendorCompany.ACCOUNTNUMBER, id).first();
				if (vc != null)
				{
					//find the company from the vendor
					BCompany bc = new BCompany(vc.getAssociatedCompany());

					if (bc != null)
					{
						//if agency join does not already exist
						if (!hsu.createCriteria(AgencyJoin.class).eq(AgencyJoin.AGENCY, newAgency.getBean()).eq(AgencyJoin.COMPANY, bc.getBean()).exists())
						{
							//insert a agency join record
							BAgencyJoin aj = new BAgencyJoin();
							aj.create();
							aj.setCompany(bc);
							aj.setAgency(newAgency.getBean());
							aj.insert();

							agencyJoins.add(aj.getBean());
						}
					}
					else
					{
						logger.error("Company not found for vendor with id: " + vc.getAssociatedCompany().getCompanyId());
						//System.out.println("Company not found for vendor with id: " + vc.getAssociatedCompany().getCompanyId());
					}
				}
				else
					System.out.println("Vendor Not Found with Id: " + id);
			}

			//Remove any unused Agency Joins
			List<AgencyJoin> listOfAgencyJoins = hsu.createCriteria(AgencyJoin.class).eq(AgencyJoin.AGENCY, newAgency.getBean()).list();
			//Set<AgencyJoin> setOfAgencyJoins = new HashSet();
			//setOfAgencyJoins.addAll(listOfAgencyJoins);
			listOfAgencyJoins.removeAll(agencyJoins);
			for (AgencyJoin a : listOfAgencyJoins)
			{
				BAgencyJoin ba = new BAgencyJoin(a);
				ba.delete();
			}
			agencyJoins.clear();

			//get all agents in agency
			NodeList agents=DOMUtils.getNodes(agency, "Agents/Agent");

			//for all agents
			for (int i=0;i<agents.getLength();i++)
			{
				Node agent=agents.item(i);

				String agentId=DOMUtils.getString(agent, "AgentId");
				String firstName=DOMUtils.getString(agent, "FirstName");
				String middleName=DOMUtils.getString(agent, "MiddleName");
				String lastName=DOMUtils.getString(agent, "LastName");

				String agentAddress1=DOMUtils.getString(agent, "AddressLine1");
				String agentAddress2=DOMUtils.getString(agent, "AddressLine2");
				String agentCity=DOMUtils.getString(agent, "City");
				String agentState=DOMUtils.getString(agent, "State");
				String agentZip=DOMUtils.getString(agent, "Zip");
				String phoneNumber=DOMUtils.getString(agent, "PhoneNumber");
				String email=DOMUtils.getString(agent, "EmailAddress");

//				System.out.println("Agent Id: " + agentId);
//				System.out.println("First Name: " + firstName);
//				System.out.println("Middle Name: " + middleName);
//				System.out.println("Last Name: " + lastName);
//				System.out.println("Address 1: " + agentAddress1);
//				System.out.println("Address 2: " + agentAddress2);
//				System.out.println("City: " + agentCity);
//				System.out.println("State: " + agentState);
//				System.out.println("Zip: " + agentZip);
//				System.out.println("PhoneNumber: " + phoneNumber);
//				System.out.println("Email: " + email);

				BAgent newAgent=new BAgent();

				//Create a new agent if one does not already exist
				if (!hsu.createCriteria(Agent.class).eq(Agent.EXT_REF, agentId).exists())
				{
					newAgent.create();

					newAgent.setExtRef(agentId);
					newAgent.setFirstName(firstName);
					newAgent.setMiddleName(middleName);
					newAgent.setLastName(lastName);
					newAgent.setStreet(agentAddress1);
					newAgent.setStreet2(agentAddress2);
					newAgent.setCity(agentCity);
					newAgent.setState(agentState);
					newAgent.setZip(agentZip);
					newAgent.setHomePhone(phoneNumber);
					newAgent.setPersonalEmail(email);

					newAgent.insert();
				}
				else
					newAgent = new BAgent(hsu.createCriteria(Agent.class).eq(Agent.EXT_REF, agentId).first());

				//Associate this agent to the current Agency
				new BPerson(newAgent.getPersonId()).assignToOrgGroup(newAgency.getOrgGroupId(), false);

				//get all company ids for the agent
				NodeList agentCompIds=DOMUtils.getNodes(agent, "BCBSCompanyIds/BCBSCompanyId");

				for (int j=0;j<agentCompIds.getLength();j++)
				{
					Node agentCompId=agentCompIds.item(j);

					String id = DOMUtils.getNodeValue(agentCompId);
					//System.out.println("Agent Company Id " + j + ": " + id);

					//for every bcbs id
					//find the vendor by account
					VendorCompany vc = hsu.createCriteria(VendorCompany.class).eq(VendorCompany.ACCOUNTNUMBER, id).first();
					if (vc != null)
					{
						//find the company from the vendor
						BCompany bc = new BCompany(vc.getAssociatedCompany());

						if (bc != null)
						{
							//if agent join does not already exist
							if (!hsu.createCriteria(AgentJoin.class).eq(AgentJoin.AGENT, newAgent.getBean()).eq(AgentJoin.COMPANY, bc.getBean()).exists())
							{
								//insert a agent join record
								BAgentJoin aj = new BAgentJoin();
								aj.create();
								aj.setAgent(newAgent.getBean());
								aj.setCompany(bc);
								aj.setApproved('N');

								aj.insert();

								agentJoins.add(aj.getBean());
							}
						}
						else
						{
							logger.error("Company not found for vendor with id: " + vc.getAssociatedCompany().getCompanyId());
							//System.out.println("Company not found for vendor with id: " + vc.getAssociatedCompany().getCompanyId());
						}
					}
					else
						System.out.println("Vendor Not Found with Id: " + id);
				}

				//Remove any unused Agency Joins
				List<AgentJoin> listOfAgentJoins = hsu.createCriteria(AgentJoin.class).eq(AgentJoin.AGENT, newAgent.getBean()).list();
				listOfAgentJoins.removeAll(agentJoins);
				for (AgentJoin a : listOfAgentJoins)
				{
					BAgentJoin ba = new BAgentJoin(a);
					ba.delete();
				}
				agentJoins.clear();
			}
			//System.out.println("-------------------------------");
		}
	}

	public static void main (String args[])
	{
		
		try {
			ArahantSession.getHSU().beginTransaction();

			ArahantSession.getHSU().setCurrentPersonToArahant();

			AgentImportXML ai = new AgentImportXML();
			ai.importFile("/home/brad/Desktop/AgentImport.xml");
			//ArahantSession.getHSU().commitTransaction();
			ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(AgentImportXML.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
