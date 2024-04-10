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
import com.arahant.business.interfaces.IContactSearchCriteria;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.*;

public class BClientContact extends BPerson {

	private static final transient ArahantLogger logger = new ArahantLogger(BClientContact.class);
	private ClientContact clientContact;

	public BClientContact() {
	}

	public BClientContact(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BClientContact(final ClientContact contact, final String orgGroupId) throws ArahantException {
		super(contact, orgGroupId);
		clientContact = contact;
	}

	public BClientContact(final String personId, final String groupId) throws ArahantException {
		super(ArahantSession.getHSU().get(Person.class, personId), groupId);
		clientContact = (ClientContact) person;
	}

	public BClientContact(final ClientContact contact) throws ArahantException {
		super(contact);
		clientContact = contact;
	}

	public char getEmailInvoice() {
		return clientContact.getEmailInvoice();
	}

	public void setEmailInvoice(char emailInvoice) {
		clientContact.setEmailInvoice(emailInvoice);
	}

	public short getContactType() {
		return clientContact.getContactType();
	}

	public BClientContact setContactType(short type) {
		clientContact.setContactType(type);
		return this;
	}

	@Override
	public String create() throws ArahantException {
		clientContact = new ClientContact();
		person = clientContact;
		person.generateId();
		person.setOrgGroupType(CLIENT_TYPE);

		// create client contact specific stuff
		this.createOther();

		return getPersonId();
	}

	@Override
	protected void createOther() throws ArahantException {
		super.createOther();

		// client contact specific creation stuff goes here
		//	final Set <ClientContact> s=person.getClientContacts();
		//	s.add(clientContact);
		//	person.setClientContacts(s);
	}

	@Override
	public void insert() throws ArahantException {
		super.insert();
		ArahantSession.getHSU().insert(clientContact);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		super.load(key);
		clientContact = (ClientContact) person;
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		//hsu.delete(clientContact);
		//delete any prospect record
		try {
			//have to use sql here because of inheritance issues
			ArahantSession.getHSU().doSQL("delete from prospect_contact where person_id='" + clientContact.getPersonId() + "'");
		} catch (Exception e) {
			throw new ArahantDeleteException(e);
		}
		try {
			//have to use sql here because of inheritance issues
			//  no.  don't delete here because it occurs in the super call below. (Blake)
//			ArahantSession.getHSU().doSQL("delete from client_contact where person_id='"+clientContact.getPersonId()+"'");
		} catch (Exception e) {
			throw new ArahantDeleteException(e);
		}

		try {
			super.delete();
		} catch (ArahantDeleteException arahantDeleteException) {
			//dont worry about deleting person record in this case
			//could be used elsewhere (employee/dependent/etc)
		}
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(clientContact);
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BClientContact(element).delete();
	}

	public static BClientContact[] listContacts(final HibernateSessionUtil hsu, final String orgGroupId) throws ArahantException {
		final List<ClientContact> plist = hsu.createCriteria(ClientContact.class)
				.joinTo(Person.ORGGROUPASSOCIATIONS)
				.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, orgGroupId)
				.list();

		final BClientContact[] ret = new BClientContact[plist.size()];

		for (int loop = 0; loop < plist.size(); loop++)
			ret[loop] = new BClientContact(plist.get(loop), orgGroupId);

		return ret;
	}

	static BClientContact[] makeArray(final List<ClientContact> l) throws ArahantException {
		final BClientContact[] ret = new BClientContact[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BClientContact(l.get(loop));

		return ret;
	}

	public static BClientContact[] searchClientContacts(final HibernateSessionUtil hsu, final IContactSearchCriteria in, final int max) throws ArahantException {
		final HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class);

		hcu.setMaxResults(max);

		hcu.eq(Person.ORGGROUPTYPE, CLIENT_TYPE);

		hcu.orderBy(Person.LNAME);
		if (!isEmpty(in.getFirstName()))
			hcu.like(Person.FNAME, in.getFirstName());
		if (!isEmpty(in.getLastName()))
			hcu.like(Person.LNAME, in.getLastName());

		OrgGroup og = null;
		if (!isEmpty(in.getOrgGroupId())) {
			og = hsu.get(OrgGroup.class, in.getOrgGroupId());
			if (og.getOwningCompany() != null)
				hcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, og.getOwningCompany().getOrgGroupId());


			//TODO figure out how to do this in the query
			//	HibernateCriteriaUtil orgAssocHcu=personHcu.leftJoinTo(Person.ORGGROUPASSOCIATIONS);
			//	HibernateCriteriaUtil orgGroupHcu=orgAssocHcu.leftJoinTo(OrgGroupAssociation.ORGGROUP);
			//	orgGroupHcu.ne(OrgGroup.ORGGROUPID, ccsc.getOrgGroupId());
		}

		if (!isEmpty(in.getOrgGroupId())) {
			final List<Person> p = hsu.createCriteria(Person.class)
					.selectFields(Person.PERSONID)
					.joinTo(Person.ORGGROUPASSOCIATIONS)
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, in.getOrgGroupId())
					.distinct()
					.list();
			hcu.notIn(Person.PERSONID, p);
		}

		switch (in.getAssociatedIndicator()) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
				break;
			case 2:			//not associated	
				hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);
				break;
		}

		List<Person> res = new LinkedList<>();

		if (og != null) {

			for (Person cc : hcu.list())
				try {
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
				 //if it failed to clear, skip it
				}
		} else
			res = hcu.list();

		final BClientContact[] ret = new BClientContact[res.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BClientContact((ClientContact) res.get(loop));

		return ret;
	}

	@Override
	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {

		super.assignToOrgGroup(orgGroupId, isPrimary);
		if (isPrimary) {
			//if this one is primary, remove any other primaries for that org group

			for (OrgGroupAssociation oga : ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
					.joinTo(OrgGroupAssociation.ORGGROUP)
					.eq(OrgGroup.ORGGROUPID, orgGroupId)
					.list()) {
				if (oga.getPerson().equals(person))
					continue;
				oga.setPrimaryIndicator('N');
				ArahantSession.getHSU().saveOrUpdate(oga);
			}
		}
	}

	public static BClientContact[] listContacts(final HibernateSessionUtil hsu, final String groupId, final String lastNameStartsWith, final boolean primary, final int cap) throws ArahantException {

		final HibernateCriteriaUtil<ClientContact> hcu = hsu.createCriteria(ClientContact.class)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.like(Person.LNAME, lastNameStartsWith);

		final HibernateCriteriaUtil<ClientContact> ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		if (primary)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP)
				.eq(OrgGroup.ORGGROUPID, groupId)
				.list();

		return makeArray(hcu.list());
	}
}
