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



package com.arahant.dynamic.services.standard.misc.cloneCompany;

import com.arahant.business.*;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

/**
 *
 * @author Blake McBride
 */
public class CloneCompany {
	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		out.put("returnValue", -1);  //  signal error - in case of exception
		out.put("returnMessage", "Function failed.");

		String newCompanyName = in.getString("newCompanyName");
		String companyToClone = in.getString("companyToClone");
		String oldCompanyName;
		
		if ("C".equals(companyToClone))
			oldCompanyName = "Co-Op Template";
		else if ("D".equals(companyToClone))
			oldCompanyName = "Direct Market Template";
		else if ("R".equals(companyToClone))
			oldCompanyName = "Realty Template";
		else if ("S".equals(companyToClone))
			oldCompanyName = "SMB Template";
		else 
			return;
		
		BCompany [] BCompanyArray = BCompany.search(newCompanyName, 1);
		if (BCompanyArray.length != 0) {
			out.put("returnMessage", "Company with name \"" + newCompanyName + "\" already exists.");
			return;
		}

		
		BCompanyArray = BCompany.search(oldCompanyName, 1);
		if (BCompanyArray.length != 1) {
			out.put("returnMessage", "Company \"" + oldCompanyName + "\" not found to copy from.");
			return;
		}
		BCompany oldComp = BCompanyArray[0];
		
		BCompany newComp = oldComp.clone(newCompanyName);
		BHREmployeeStatus.clone(oldComp, newComp);
		BAppointmentLocation.clone(oldComp, newComp);
		BProspectSource.clone(oldComp, newComp);
		BProspectStatus.clone(oldComp, newComp);
		BProspectType.clone(oldComp, newComp);

		out.put("returnValue", 0);
		out.put("returnMessage", "Function succeeded.");
	}	
	
}
