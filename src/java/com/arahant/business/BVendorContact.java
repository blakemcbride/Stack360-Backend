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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.*;

public class BVendorContact extends BPerson implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BVendorContact.class);

	VendorContact vendorContact;

	public BVendorContact() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BVendorContact(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param person
	 * @param groupId
	 * @throws ArahantException
	 */
	public BVendorContact(final Person person, final String groupId) throws ArahantException {
		super(person, groupId);
		vendorContact = (VendorContact) person;
	}

	/**
	 * @param personId
	 * @param groupId
	 * @throws ArahantException
	 */
	public BVendorContact(final String personId, final String groupId) throws ArahantException {
		super(ArahantSession.getHSU().get(Person.class, personId), groupId);
		vendorContact = (VendorContact) person;
	}

	/**
	 * @param contact
	 * @throws ArahantException
	 */
	public BVendorContact(final VendorContact contact) throws ArahantException {
		super(contact);
		vendorContact = contact;
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
			final Iterator ogaItr = ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, orgGroupId)
					.list()
					.iterator();

			while (ogaItr.hasNext()) {
				final OrgGroupAssociation oga = (OrgGroupAssociation) ogaItr.next();
				if (oga.getPerson().equals(person))
					continue;
				oga.setPrimaryIndicator('N');
				ArahantSession.getHSU().saveOrUpdate(oga);
			}
		}
	}

	@Override
	public String create() throws ArahantException {
		vendorContact = new VendorContact();
		person = vendorContact;
		person.generateId();
		person.setOrgGroupType(VENDOR_TYPE);

		// create vendor contact specific stuff
		this.createOther();

		return getPersonId();
	}

	@Override
	protected void createOther() throws ArahantException {
		super.createOther();

		// vendor contact specific creation stuff goes here
		//final Set <VendorContact> s=person.getVendorContacts();
		//s.add(vendorContact);
		//person.setVendorContacts(s);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		super.load(key);
		vendorContact = (VendorContact) person;
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void insert() throws ArahantException {
		super.insert();
		ArahantSession.getHSU().insert(vendorContact);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(vendorContact);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		//hsu.delete(vendorContact);
		super.delete();
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BVendorContact(element).delete();
	}

	/**
	 * @param groupId
	 * @return
	 * @throws ArahantException
	 */
	public static BVendorContact[] list(final HibernateSessionUtil hsu, final String groupId) throws ArahantException {

		final List<VendorContact> plist = hsu.createCriteria(VendorContact.class)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.joinTo(Person.ORGGROUPASSOCIATIONS)
				.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, groupId)
				.list();


		return makeAray(plist, groupId);
	}

	static BVendorContact[] makeAray(final List<VendorContact> plist, final String groupId) throws ArahantException {
		final BVendorContact[] ret = new BVendorContact[plist.size()];

		for (int loop = 0; loop < plist.size(); loop++)
			ret[loop] = new BVendorContact(plist.get(loop), groupId);
		return ret;
	}

	/**
	 * @param hsu
	 * @param firstName
	 * @param lastName
	 * @param orgGroupId
	 * @param associatedIndicator
	 * @param max
	 * @return
	 * @throws ArahantException
	 */
	public static BVendorContact[] search(final HibernateSessionUtil hsu, final String firstName, final String lastName, final String orgGroupId, final int associatedIndicator, final int max) throws ArahantException {

		final HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).eq(Person.ORGGROUPTYPE, VENDOR_TYPE);

		hcu.setMaxResults(max);

		final HibernateCriteriaUtil personHcu = hcu;//hcu.joinTo(VendorContact.MAIN_CONTACT);
		personHcu.orderBy(Person.LNAME);
		if (!isEmpty(firstName))
			personHcu.like(Person.FNAME, firstName);
		if (!isEmpty(lastName))
			personHcu.like(Person.LNAME, lastName);

		OrgGroup og = null;
		if (!isEmpty(orgGroupId)) {
			og = hsu.get(OrgGroup.class, orgGroupId);
			if (og.getOwningCompany() != null)
				personHcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, og.getOwningCompany().getOrgGroupId());

			//TODO figure out how to do this in the query
			//	HibernateCriteriaUtil orgAssocHcu=personHcu.leftJoinTo(Person.ORGGROUPASSOCIATIONS);
			//	HibernateCriteriaUtil orgGroupHcu=orgAssocHcu.leftJoinTo(OrgGroupAssociation.ORGGROUP);
			//	orgGroupHcu.ne(OrgGroup.ORGGROUPID, ccsc.getOrgGroupId());
		}

		if (!isEmpty(orgGroupId)) {
			final List p = hsu.createCriteria(Person.class)
					.selectFields(Person.PERSONID)
					.joinTo(Person.ORGGROUPASSOCIATIONS)
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
				//hcu.isNotNull(Person.ORGGROUPASSOCIATIONS); //make sure there are some, inner join
				hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
				break;
			case 2: //not associated	
				// hibernate can't handle sizeEq here
				//hcu.isNull(Person.ORGGROUPASSOCIATIONS);
				//hcu.sizeEq(ProspectContact.ORGGROUPASSOCIATIONS,0);
				hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);
				break;
		}


		List<Person> res = new LinkedList<Person>();

		if (og != null) {
			final Iterator<Person> resItr = hcu.list().iterator();

			while (resItr.hasNext())
				try {
					final Person cc = resItr.next();
					final Iterator ogaItr = cc.getOrgGroupAssociations().iterator();
					boolean found = false;
					while (ogaItr.hasNext()) {
						final OrgGroupAssociation oga = (OrgGroupAssociation) ogaItr.next();
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

		final BVendorContact[] ret = new BVendorContact[res.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BVendorContact((VendorContact) res.get(loop));

		return ret;
	}

	/**
	 * @param hsu
	 * @param groupId
	 * @param lastNameStartsWith
	 * @param primary
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public static BVendorContact[] list(final HibernateSessionUtil hsu, final String groupId, final String lastNameStartsWith, final boolean primary, final int cap) throws ArahantException {

		final HibernateCriteriaUtil<VendorContact> hcu = hsu.createCriteria(VendorContact.class)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.like(Person.LNAME, lastNameStartsWith);


		final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		if (primary)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, groupId)
				.list();


		return makeAray(hcu.list(), groupId);
	}

	public static BVendorContact[] list(final HibernateSessionUtil hsu, final String groupId, final String lastNameStartsWith, final boolean primary, final int cap, String[] excludeIds) throws ArahantException {

		final HibernateCriteriaUtil<VendorContact> hcu = hsu.createCriteria(VendorContact.class)
				.notIn(Person.PERSONID, excludeIds)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.like(Person.LNAME, lastNameStartsWith);


		final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		if (primary)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, groupId)
				.list();


		return makeAray(hcu.list(), groupId);
	}
}
