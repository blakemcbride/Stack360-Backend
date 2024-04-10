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

package com.arahant.tutorial.OldHibernateExamples;

import com.arahant.beans.GlAccount;
import com.arahant.beans.Person;
import com.arahant.beans.QuickbooksAccountChange;
import com.arahant.beans.QuickbooksPersonChange;
import com.arahant.beans.ZipcodeLookup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


public class Hibernate21 {
	public static void main (String []args)
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();

		/*
		 QuickbooksPersonChange pc=new QuickbooksPersonChange();
		pc.generateId();
		pc.setPerson(hsu.getFirst(Person.class));
		pc.setQbRecordId("1234");
		pc.setQbRecordRevision(3);
		pc.setRecordChanged('N');
		ArahantSession.getHSU().insert(pc);
		 

		QuickbooksAccountChange pc=new QuickbooksAccountChange();
		pc.generateId();
		pc.setAccount(hsu.getFirst(GlAccount.class));
		pc.setQbRecordId("1235");
		pc.setQbRecordRevision(3);
		pc.setRecordChanged('N');
		ArahantSession.getHSU().insert(pc);
		 *
		 * */

		ZipcodeLookup z=new ZipcodeLookup();
		z.generateId();
		z.setCity("Dickson");
		z.setCounty("Dickson");
		z.setState("TN");
		z.setZipcode("37055");
		hsu.getSession().save(z);

		hsu.commitTransaction();
	}
}
