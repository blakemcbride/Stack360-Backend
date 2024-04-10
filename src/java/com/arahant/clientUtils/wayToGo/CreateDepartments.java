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


package com.arahant.clientUtils.wayToGo;

import com.arahant.beans.ClientCompany;
import com.arahant.business.BOrgGroup;
import com.arahant.lisp.ABCL;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;

/**
 *
 * @author Blake McBride
 */
public class CreateDepartments {
	
		public static void main(String[] args) {
			ABCL.init();  // init security system
			HibernateSessionUtil hsu = ArahantSession.getHSU(false);
			hsu.setCurrentPersonToArahant();
			
			HibernateCriteriaUtil hcu = hsu.createCriteria(ClientCompany.class);
			HibernateScrollUtil<ClientCompany> scr = hcu.scroll();
			while (scr.next()) {
				ClientCompany c = scr.get();
				String parentID = c.getOrgGroupId();

				addSubGroup(hsu, parentID, "Apparel");
				addSubGroup(hsu, parentID, "Bakery");
				addSubGroup(hsu, parentID, "Cardboard");
				addSubGroup(hsu, parentID, "Cart Pusher");
				addSubGroup(hsu, parentID, "Code 6");
				addSubGroup(hsu, parentID, "Consumables");
				addSubGroup(hsu, parentID, "Deli");
				addSubGroup(hsu, parentID, "Features");
				addSubGroup(hsu, parentID, "Frozen/Dairy");
				addSubGroup(hsu, parentID, "Homelines");
				addSubGroup(hsu, parentID, "Meat");
				addSubGroup(hsu, parentID, "Produce");
				addSubGroup(hsu, parentID, "Remodel");
				addSubGroup(hsu, parentID, "Stock/Zone");
				addSubGroup(hsu, parentID, "Zone");
				addSubGroup(hsu, parentID, "Stocking");
				addSubGroup(hsu, parentID, "Maintenance");

				hsu.flush();
				System.out.println(c.getName());
			}
			hsu.commitTransaction();
			hsu.close();
		}
		
		private static void addSubGroup(HibernateSessionUtil hsu, String parentID, String name) {
				BOrgGroup bog = new BOrgGroup();
				bog.create();  // creates OrgGroup & ID
				bog.setCompanyId(hsu.getCurrentCompany().getCompanyId());
				bog.setOrgGroupType(BOrgGroup.CLIENT_TYPE);
				bog.setName(name);
				bog.insert();
				bog.setParent(parentID);
		}
		
}
