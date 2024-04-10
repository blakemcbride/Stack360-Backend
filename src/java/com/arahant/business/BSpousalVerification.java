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
import com.arahant.beans.SpousalVerification;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BSpousalVerification extends SimpleBusinessObjectBase<SpousalVerification> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BSpousalVerification(id).delete();
	}

	public static BSpousalVerification getVerification(BPerson spouse, short year) {
		SpousalVerification v = ArahantSession.getHSU().createCriteria(SpousalVerification.class)
				.eq(SpousalVerification.PERSON, spouse.person)
				.eq(SpousalVerification.YEAR, year)
				.first();

		if (v == null)
			return null;

		return new BSpousalVerification(v);
	}

	static BSpousalVerification[] makeArray(List<SpousalVerification> l) {
		BSpousalVerification[] ret = new BSpousalVerification[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BSpousalVerification(l.get(loop));
		return ret;
	}

	public BSpousalVerification() {
	}

	public BSpousalVerification(String id) {
		super(id);
	}

	public BSpousalVerification(SpousalVerification o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new SpousalVerification();
		return bean.generateId();
	}

	public int getDateReceived() {
		return bean.getDateReceived();
	}

	public String getId() {
		return bean.getSpousalVerificationId();
	}

	public int getYear() {
		if (bean.getYear() > 9999)
			return (bean.getYear() / 10);
		return bean.getYear();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(SpousalVerification.class, key);
	}

	public void setDateReceived(int dateReceived) {
		bean.setDateReceived(dateReceived);
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public void setYear(int year) {
		bean.setYear((short) year);
	}

	public int getYearHalf() {
		if (bean.getYear() > 9999)
			return (bean.getYear() % 10);
		return 0;
	}
}
