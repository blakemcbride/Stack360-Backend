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

import com.arahant.beans.Person;
import com.arahant.beans.PersonalReference;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BPersonalReference extends SimpleBusinessObjectBase<PersonalReference> {

	public BPersonalReference() {
	}

	public BPersonalReference(String id) {
		super(id);
	}

	public BPersonalReference(PersonalReference o) {
		bean = o;
	}

	public String getReferenceId() {
		return bean.getReferenceId();
	}

	public String getAddress() {
		return bean.getAddress();
	}

	public void setAddress(String address) {
		bean.setAddress(address);
	}

	public String getCompany() {
		return bean.getCompany();
	}

	public void setCompany(String company) {
		bean.setCompany(company);
	}

	public String getPhone() {
		return bean.getPhone();
	}

	public void setPhone(String phone) {
		bean.setPhone(phone);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getReferenceName() {
		return bean.getReferenceName();
	}

	public void setReferenceName(String referenceName) {
		bean.setReferenceName(referenceName);
	}

	public String getRelationshipOther() {
		return bean.getRelationshipOther();
	}

	public void setRelationshipOther(String relationshipOther) {
		bean.setRelationshipOther(relationshipOther);
	}

	public char getRelationshipType() {
		return bean.getRelationshipType();
	}

	public void setRelationshipType(char relationshipType) {
		bean.setRelationshipType(relationshipType);
	}

	public short getYearsKnown() {
		return bean.getYearsKnown();
	}

	public void setYearsKnown(short yearsKnown) {
		bean.setYearsKnown(yearsKnown);
	}

	static BPersonalReference[] makeArray(List<PersonalReference> l) {
		BPersonalReference[] ret = new BPersonalReference[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPersonalReference(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new PersonalReference();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PersonalReference.class, key);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
		super.insert();
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BPersonalReference(id).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		super.delete();
	}

	public static BPersonalReference[] list(String empId, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(PersonalReference.class)
				.orderBy(PersonalReference.REFERENCE_NAME)
				.setMaxResults(max)
				.eq(PersonalReference.PERSON, new BPerson(empId).getPerson())
				.list());
	}
}
