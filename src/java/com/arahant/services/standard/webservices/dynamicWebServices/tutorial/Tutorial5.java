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

import com.arahant.business.BPerson;
import com.arahant.business.BSearchMetaInput;
import com.arahant.business.BSearchOutput;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

/**
 *
 *
 *


 */
public class Tutorial5  {


	public DataObject searchPersons(DataObject in)
	{

		DataObject d=new DataObject();

		BSearchMetaInput bsmi=new BSearchMetaInput(in, new String[]{"lname","fname"});
		BSearchOutput<BPerson> peeps=BPerson.searchPersons(bsmi, 50);

		peeps.setMetaSearchReturn(d);
		
		DataObject [] retAry=new DataObject[peeps.getItems().length];
		for (int loop=0;loop<retAry.length;loop++)
		{
			BPerson bp=peeps.getItems()[loop];
			DataObject sd=new DataObject();
			sd.put("lname", bp.getLastName());
			sd.put("fname", bp.getFirstName());

			retAry[loop]=sd;
		}

		d.put("personsArray",retAry);
		
		return d;
	}

}
  
