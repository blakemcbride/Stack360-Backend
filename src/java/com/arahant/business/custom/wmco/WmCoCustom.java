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

package com.arahant.business.custom.wmco;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class WmCoCustom {

	/**
	 * @param currentPerson
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public static BPerson[] listEmployeesInPersonsGroup(final Person currentPerson, final int cap) throws ArahantException {

		List<OrgGroup> orgs=ArahantSession.getHSU().createCriteriaNoCompanyFilter(OrgGroup.class)
			.eq(OrgGroup.ORGGROUPID,"00001-0000000004")
				//.joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
			//.eq(OrgGroupAssociation.PERSON, currentPerson)
			.list();

		List<Person> plist=ArahantSession.getHSU().createCriteriaNoCompanyFilter(Person.class)
			.setMaxResults(cap)
			//.activeEmployee()
			.orderBy(Person.LNAME).orderBy(Person.FNAME)
			.joinTo(Person.ORGGROUPASSOCIATIONS)
			.in(OrgGroupAssociation.ORGGROUP,orgs)
			//.joinTo(OrgGroup.ORGGROUPASSOCIATIONS,"orggroup2")
			//.joinTo("orggroup2."+OrgGroupAssociation.PERSON)
			//.eq(Person.PERSONID, currentPerson.getPersonId())
			.list();
		
		Set<Person> pset=new HashSet<Person>();
		
		String [] addPeople=new String[]{"00002-0000000001", //add charles
										"00002-0000000003", //add Luanne Wagner
										"00002-0000000004", //add Meghan Smith
										"00001-0000002983", //add christine
					};
		
		pset.addAll(plist);
		
		for (String id : addPeople)
			pset.add(ArahantSession.getHSU().get(Person.class,id));

		plist.clear();

		//if person is an employee, filter for active
		for (Person p : pset)
		{
			BPerson bp=new BPerson(p);
			if (bp.isEmployee() && !bp.getBEmployee().isActiveOn(DateUtils.now()))
				continue;

			plist.add(p);
		}
/*
		ArrayList<BEmployee> beml=new ArrayList<BEmployee>();

		for (BEmployee b : ret)
		{
			if (b.isActiveOn(DateUtils.now()))
				beml.add(b);
		}

		return beml.toArray(new BEmployee[beml.size()]);
 * */

		Collections.sort(plist);
		return BPerson.makeArray(plist);
	}


}
