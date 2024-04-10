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

package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.beans.ServiceSubscribed;
import com.arahant.beans.ServiceSubscribedJoin;
import com.arahant.utils.ArahantSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class InterfaceTest {
	public InterfaceTest(){
		//get current company and check if this company subscribtes to the Service
		//service is Cobra Guard
		//System.out.println("Company " + ArahantSession.getHSU().getCurrentCompany().getCompanyId());
		ServiceSubscribed service = ArahantSession.getHSU().createCriteria(ServiceSubscribed.class).eq(ServiceSubscribed.SERVICENAME, "Cobra Guard").first();
		if (service != null){
			//check if company joined this service
			List<String> join = (List)ArahantSession.getHSU().createCriteriaNoCompanyFilter(ServiceSubscribedJoin.class)
					.selectFields(ServiceSubscribedJoin.COMPANY)
					.eq(ServiceSubscribedJoin.SERVICE, service)
					.list();
			if (join != null){
				System.out.println("Got servce");
			}
		}
	}
	public static void main(String[] args) {
		try {
			//new InterfaceTest();
			new com.arahant.timertasks.CobraGuard().run();
		} catch (Exception ex) {
			Logger.getLogger(InterfaceTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
