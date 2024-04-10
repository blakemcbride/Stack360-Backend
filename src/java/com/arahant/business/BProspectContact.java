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
package com.arahant.business;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProspectContact;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class BProspectContact extends BPerson {

	private ProspectContact prospectContact;

	public BProspectContact(String id) {
		internalLoad(id);
	}

	public BProspectContact() {
	}

	public BProspectContact(ProspectContact o) {
		super(o);
		prospectContact = o;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProspectContact(id).delete();
	}

	public static BProspectContact[] listContacts(String groupId, String lastNameStartsWith, boolean primary, int cap) {
		final HibernateCriteriaUtil<ProspectContact> hcu = ArahantSession.getHSU().createCriteria(ProspectContact.class)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.like(Person.LNAME, lastNameStartsWith);


		final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		if (primary)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, groupId)
				.list();


		return makeArray(hcu.list());


	}

	public static BProspectContact[] makeArray(List<ProspectContact> l) {
		BProspectContact[] ret = new BProspectContact[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectContact(l.get(loop));
		return ret;
	}

	public static BProspectContact[] searchProspectContacts(String firstName, String lastName, String orgGroupId, int associatedIndicator, int cap) {
		final HibernateCriteriaUtil<ProspectContact> hcu = ArahantSession.getHSU().createCriteria(ProspectContact.class);

		hcu.setMaxResults(cap);

		hcu.eq(Person.ORGGROUPTYPE, PROSPECT_TYPE);

		hcu.orderBy(Person.LNAME);

		hcu.like(Person.FNAME, firstName);

		hcu.like(Person.LNAME, lastName);

		OrgGroup og = null;
		if (!isEmpty(orgGroupId)) {
			og = ArahantSession.getHSU().get(OrgGroup.class, orgGroupId);
			if (og.getOwningCompany() != null)
				hcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, og.getOwningCompany().getOrgGroupId());


			//TODO figure out how to do this in the query
			//	HibernateCriteriaUtil orgAssocHcu=personHcu.leftJoinTo(Person.ORGGROUPASSOCIATIONS);
			//	HibernateCriteriaUtil orgGroupHcu=orgAssocHcu.leftJoinTo(OrgGroupAssociation.ORGGROUP);
			//	orgGroupHcu.ne(OrgGroup.ORGGROUPID, ccsc.getOrgGroupId());
		}


		if (!isEmpty(orgGroupId)) {
			final List p = ArahantSession.getHSU().createCriteria(ProspectContact.class)
					.selectFields(Person.PERSONID)
					.joinTo(ProspectContact.ORGGROUPASSOCIATIONS)
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, orgGroupId)
					.distinct()
					.list();
			hcu.notIn(Person.PERSONID, p);
		}


		switch (associatedIndicator) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.isNotNull(ProspectContact.ORGGROUPASSOCIATIONS); //make sure there are some, inner join
				break;
			case 2: //not associated	
				// hibernate can't handle sizeEq here
				hcu.isNull(ProspectContact.ORGGROUPASSOCIATIONS);
				//hcu.sizeEq(ProspectContact.ORGGROUPASSOCIATIONS,0);
				break;
		}



		List<ProspectContact> res = new LinkedList<ProspectContact>();

		if (og != null) {
			final Iterator<ProspectContact> resItr = hcu.list().iterator();

			while (resItr.hasNext())
				try {
					final ProspectContact cc = resItr.next();
					final Iterator<OrgGroupAssociation> ogaItr = cc.getOrgGroupAssociations().iterator();
					boolean found = false;
					while (ogaItr.hasNext()) {
						final OrgGroupAssociation oga = ogaItr.next();
						if (oga.getOrgGroup().equals(og)) {
							found = true;
							break;
						}
					}
					if (!found)
						res.add(cc);
				} catch (final Exception e) {
					continue; //if it failed to clear, skip it
				}
		} else
			res = hcu.list();

		final BProspectContact[] ret = new BProspectContact[res.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectContact(res.get(loop));

		return ret;
	}
	
	private void internalLoad(String key) {
		prospectContact = ArahantSession.getHSU().get(ProspectContact.class, key);
		if (prospectContact == null)
			prospectContact = new ProspectContact();
		person = prospectContact;
	}

	@Override
	public void load(String key) {
		internalLoad(key);
	}

	@Override
	public String create() throws ArahantException {
		prospectContact = new ProspectContact();
		person = prospectContact;
		person.generateId();
		person.setOrgGroupType(PROSPECT_TYPE);

		return getPersonId();
	}

	/**
	 * @param orgGroupId
	 * @param b
	 * @throws ArahantException
	 */
	@Override
	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {

		super.assignToOrgGroup(orgGroupId, isPrimary);
		if (isPrimary) {
			//if this one is primary, remove any other primaries for that org group
			final Iterator<OrgGroupAssociation> ogaItr = ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, orgGroupId)
					.list()
					.iterator();

			while (ogaItr.hasNext()) {
				final OrgGroupAssociation oga = ogaItr.next();
				if (oga.getPerson().equals(person))
					continue;
				oga.setPrimaryIndicator('N');
				ArahantSession.getHSU().saveOrUpdate(oga);
			}
		}
	}

	public int getProspectType() {
		return prospectContact.getContactType();
	}

	public void setProspectType(int type) {
		prospectContact.setContactType((short) type);
	}
}
