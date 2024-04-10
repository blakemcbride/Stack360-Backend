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

import com.arahant.beans.HrEmergencyContact;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BHrEmergencyContact extends SimpleBusinessObjectBase<HrEmergencyContact> {

	public BHrEmergencyContact() {
	}

	public BHrEmergencyContact(final String contactId) throws ArahantException {
		internalLoad(contactId);
	}

	public BHrEmergencyContact(final HrEmergencyContact contact) {
		bean = contact;
	}

	@Override
	public String create() throws ArahantException {
		bean = new HrEmergencyContact();
		return bean.generateId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrEmergencyContact.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void insert() throws ArahantException {
		super.insert();
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	public static void delete(final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHrEmergencyContact(element).delete();
	}

	public String getAddress() {
		return bean.getAddress();
	}

	public void setAddress(String address) {
		bean.setAddress(address);
	}

	public String getContactId() {
		return bean.getContactId();
	}
//
//	public void setContactId(String contactId) {
//		this.contactId = contactId;
//	}

	public String getName() {
		return bean.getName();
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public int getSeqno() {
		return bean.getSeqno();
	}

	public void setSeqno(int seqno) {
		bean.setSeqno((short) seqno);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getHomePhone() {
		return bean.getHomePhone();
	}

	public void setHomePhone(String homePhone) {
		bean.setHomePhone(homePhone);
	}

	public String getCellPhone() {
		return bean.getCellPhone();
	}

	public void setCellPhone(String cellPhone) {
		bean.setCellPhone(cellPhone);
	}

	public String getWorkPhone() {
		return bean.getWorkPhone();
	}

	public void setWorkPhone(String workPhone) {
		bean.setWorkPhone(workPhone);
	}

	public String getRelationship() {
		return bean.getRelationship();
	}

	public void setRelationship(String relationship) {
		bean.setRelationship(relationship);
	}

	static BHrEmergencyContact[] makeArray(List<HrEmergencyContact> l) {
		BHrEmergencyContact[] ret = new BHrEmergencyContact[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHrEmergencyContact(l.get(loop));
		return ret;
	}

	public static BHrEmergencyContact[] list(Person person) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrEmergencyContact.class).orderBy(HrEmergencyContact.SEQNO).eq(HrEmergencyContact.PERSON, person).list());
	}
}
