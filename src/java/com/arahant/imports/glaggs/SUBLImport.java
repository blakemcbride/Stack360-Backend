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
import com.arahant.business.BEmployee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FixedSizeFileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SUBLImport extends GLAGSFixedImportBase {


	private HashMap<String,String> groupToCompany=new HashMap<String, String>();


	public void importFile(String fileName) throws Exception
	{
		hsu.dontAIIntegrate();

		for (CompanyDetail dt : hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).list())
		{
			groupToCompany.put(dt.getExternalId(), dt.getOrgGroupId());
		}

		FixedSizeFileReader fr=new FixedSizeFileReader(fileName);

		fr.addField("AgentId", 1, 2);
		fr.addField("GroupNumber", 3, 5);
		fr.addField("GroupSuffix", 8, 3);
		fr.addField("DateBilled", 11, 8); //Policy start date?
		fr.addField("ContractNumber", 19, 9); //ssn?
		fr.addField("SubscriberRate", 28, 7);
		fr.addField("SubscriberName", 36, 18);
		fr.addField("CrossCode", 54, 4); //cross benefits hosp/med/surg
		fr.addField("ShieldCode", 58, 4); //shield beneits provider/drugs/dental/vision
		fr.addField("ConfigCode", 62, 4);  //contract type single/double/family
		fr.addField("MedicareElig", 66, 1);
		fr.addField("TotalMemberCount", 67, 1);


		int count=0;
		fr.skipChars(3); //there are 3 garbage chars at start of file

	
		while (fr.nextLine())
		{
			if (++count%50==0)
			{
				System.out.println("1-"+count);
				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			}

	/*		System.out.println("cross benefits "+fr.getField("ServiceCode1"));
			System.out.println("shield benefits "+fr.getField("ServiceCode2"));
			System.out.println("single double fam "+fr.getField("ServiceCode3"));
*/
			String ref=fr.getField("GroupNumber");


			String ssn=fr.getField("ContractNumber");
			String companyId=groupToCompany.get(ref);

			if (isEmpty(companyId))
			{
				System.out.println("*** missing company ref "+ref);
				continue;
			}

			hsu.setCurrentCompany(hsu.get(CompanyDetail.class,companyId));


			Employee emp=hsu.createCriteria(Employee.class)
					.eq(Employee.SSN, ssn)
					.first();

			Name name=new Name(fr.getField("SubscriberName"));
			

			BEmployee bemp=null;
			if (emp==null)
			{
				bemp=new BEmployee();
				bemp.create();
				bemp.setFirstName(name.fname);
				bemp.setLastName(name.lname);
				bemp.setMiddleName(name.mname);
				bemp.setSsn(ssn);
				makeLoginDefaults(bemp);
				bemp.insert();
				bemp.assignToOrgGroup(companyId, false);
				emp=bemp.getEmployee();

			}
			else
			{
				bemp=new BEmployee(emp);
				bemp.setFirstName(name.fname);
				bemp.setLastName(name.lname);
				bemp.setMiddleName(name.mname);
				bemp.update();
			}

				//need blue cross category
			String bcCategory=getCategoryId(companyId, "Blue Cross");

			//need shield category
			String bsCategory=getCategoryId(companyId, "Blue Shield");

			//config is single/double/family

			String configName="Single";

			String conCode=fr.getField("ConfigCode");

			if ("0200".equals(conCode))
				configName="Double";
			if ("0300".equals(conCode))
				configName="Family";

			String bsBenefit=getBenefitId(companyId, fr.getField("ShieldCode"), bsCategory, false, ref);
			String bcBenefit=getBenefitId(companyId, fr.getField("CrossCode"), bcCategory, false, ref);


			String bsConfig=getBenefitConfigId(companyId,fr.getField("ShieldCode")+" "+configName,bsBenefit,"",null);
			String bcConfig=getBenefitConfigId(companyId,fr.getField("CrossCode")+" "+ configName, bcBenefit, "", null);


			makeBenefitJoinIfNotFound(bemp.getPerson(), bemp.getPerson(), bcConfig, DateUtils.getDate(sdf.parse(fr.getField("DateBilled"))),0,null);
			makeBenefitJoinIfNotFound(bemp.getPerson(), bemp.getPerson(), bsConfig, DateUtils.getDate(sdf.parse(fr.getField("DateBilled"))),0,null);

		}

		fr.close();

	}


	public static void main (String args[])
	{
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();
			SUBLImport x = new SUBLImport();
			x.importFile("/Users/Arahant/GLAGGS/eddi_sample/SUBL255.txt");
			ArahantSession.getHSU().commitTransaction();
		} catch (Exception ex) {
			ArahantSession.getHSU().rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
