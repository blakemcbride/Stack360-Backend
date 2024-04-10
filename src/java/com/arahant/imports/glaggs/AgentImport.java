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
import com.arahant.business.BAgency;
import com.arahant.business.BAgent;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class AgentImport extends GLAGSFixedImportBase {


	public void importFile(String fileName) throws Exception
	{
		DelimitedFileReader dfr=new DelimitedFileReader(fileName);

		hsu.dontAIIntegrate();

		hsu.noAutoFlush();

		ArahantSession.setFastKeys(true);

		dfr.nextLine();
		dfr.nextLine();

		int count=0;

		for (Agency a : hsu.createCriteria(Agency.class).list())
			agencyToCompany.put(a.getName(), a);

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

			int pos=0;
			String name=dfr.getString(pos++);
			String agencyName=dfr.getString(pos++).trim();
			String strt1=dfr.getString(pos++);
			String strt2=dfr.getString(pos++);
			String city=dfr.getString(pos++);
			String state=dfr.getString(pos++);
			String zip=dfr.getString(pos++);
			String id=dfr.getString(pos++);
			String phone=dfr.getString(pos++);
			String email=dfr.getString(pos++);

			BAgent agent=new BAgent();

			if (!hsu.createCriteria(Agent.class).eq(Agent.EXT_REF, id).exists())
			{
				agent.create();
				Name n=new Name(name);
				agent.setLastName(n.lname);
				agent.setFirstName(n.fname);
				agent.setMiddleName(n.mname);
				agent.setStreet(strt1);
				agent.setStreet2(strt2);
				agent.setCity(city);
				agent.setState(state);
				agent.setZip(zip);
				agent.setWorkPhone(phone);
				agent.setPersonalEmail(email);
				agent.setExtRef(id);
				agent.insert();
				
				if (!isEmpty(agent.getExtRef()))
					agentToPerson.put(Integer.parseInt(agent.getExtRef())+"",agent.getBean());
			}
			else
				continue;

			//bcbs account id for what agencies are allowed to access it and what agents are allowed to access it

		//	agent.assignToOrgGroup("00001-0000000007", false); //misc group


			if (isEmpty(agencyName))
				agencyName=dfr.getString(0);

			if (isEmpty(name) && isEmpty(id))
				continue;


			if (agencyToCompany.get(agencyName)==null)
			{
				BAgency agency=new BAgency();
				agency.create();

				agency.setName(agencyName);
				agency.setIdentifier("");
				agency.setStreet(strt1);
				agency.setStreet2(strt2);
				agency.setCity(city);
				agency.setState(state);
				agency.setZip(zip);

				agency.insert();
				agencyToCompany.put(agencyName, agency.getBean());
			}

			if (agent != null)
				agent.assignToOrgGroup(agencyToCompany.get(agencyName).getOrgGroupId(), false);

		}

	}
/*
	public void importFile2(String fileName) throws Exception
	{
		DelimitedFileReader dfr=new DelimitedFileReader(fileName);

		hsu.dontAIIntegrate();

		hsu.noAutoFlush();

		ArahantSession.setFastKeys(true);

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

			String name=dfr.getString(2);
			String id=dfr.getString(3);
			String agentId=dfr.getString(4);
			if (isEmpty(id))
				continue;

			if (isEmpty(name))
				name=dfr.getString(0);

			if (isEmpty(name) && isEmpty(agentId))
				continue;


			String reducedId=name;

			if (agencyToCompany.get(reducedId)==null)
			{

				BAgency agency=new BAgency();
				agency.create();
				if (isEmpty(name))
					name=agentId;

				agency.setName(name);
				agency.setIdentifier("");
				agency.insert();
				agencyToCompany.put(reducedId, agency.getBean());
			}

			if ("0".equals(reducedId))
				continue;

			if (!isEmpty(agentId))
			{
				Agent a=agentToPerson.get(Integer.parseInt(agentId)+"");

				if (a!=null)
					new BAgent(a).assignToOrgGroup(agencyToCompany.get(reducedId).getOrgGroupId(), false);
			}


		}

	}
*/
	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			AgentImport x = new AgentImport();
			x.importFile("/Users/Arahant/GLAGGS/MasterAgent.csv");
	//		x.importFile2("/Users/Arahant/GLAGGS/AgencyMaster.csv");


			ArahantSession.getHSU().commitTransaction();
		//	ArahantSession.getHSU().rollbackTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


}
