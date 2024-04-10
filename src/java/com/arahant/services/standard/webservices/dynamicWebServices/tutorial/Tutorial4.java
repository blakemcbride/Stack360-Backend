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

package com.arahant.services.standard.webservices.dynamicWebServices.tutorial;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

/**
 *
 *
 *


 */
public class Tutorial4  {


	public DataObject searchServices(DataObject in)
	{

		DataObject d=new DataObject();

		d.put("highCap", BProperty.getInt(StandardProperty.SEARCH_MAX));
		d.put("lowCap", BProperty.getInt(StandardProperty.COMBO_MAX));

/*
		BService[] ary=BService.search(ArahantSession.getHSU(), in.getString("accountingSystemId"), in.getString("description")
				, d.getInt("highCap"));

		DataObject [] retAry=new DataObject[ary.length];
		for (int loop=0;loop<ary.length;loop++)
		{
			BService s=ary[loop];
			DataObject sd=new DataObject();
			sd.put("serviceId", s.getProductId());
			sd.put("accsysId", s.getAccsysId());
			sd.put("serviceType", s.getProductType());
			sd.put("description", s.getDescription());
			boolean hasAccount=s.getGlAccount() != null;
			sd.put("hasGLExpenseAccount", hasAccount);
			
			if (hasAccount)
			{
				sd.put("glExpenseAccountNumber", s.getGlAccount().getAccountNumber());
				sd.put("glExpenseAccountName", s.getGlAccount().getAccountName());

			}

			retAry[loop]=sd;
		}

 *
 */


		DataObject [] retAry=new DataObject[100];
		for (int loop=0;loop<retAry.length;loop++)
		{
			DataObject sd=new DataObject();
			sd.put("serviceId", loop+"");
			sd.put("accsysId", loop+"");
			sd.put("serviceType", "X");
			sd.put("description", "Service "+loop);
			sd.put("hasGLExpenseAccount", false);


			retAry[loop]=sd;
		}

		d.put("itemArray",retAry);
		
		return d;
	}

}
