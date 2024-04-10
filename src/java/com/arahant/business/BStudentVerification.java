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
import com.arahant.beans.StudentVerification;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

public class BStudentVerification extends SimpleBusinessObjectBase<StudentVerification> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BStudentVerification(id).delete();
	}

	public BStudentVerification() {
	}

	public BStudentVerification(String id) {
		super(id);
	}

	public BStudentVerification(StudentVerification o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new StudentVerification();
		return bean.generateId();
	}

	public String getId() {
		return bean.getStudentVerificationId();
	}

	public static short getTerm(String term) {
		term = term.trim();
		if ("F".equals(term))
			return 1;

		if ("I".equals(term))
			return 2;

		if ("P".equals(term))
			return 3;

		if ("U".equals(term))
			return 4;

		throw new ArahantException("Unknown term type encountered.");
	}

	public String getTerm() {
		switch (bean.getCalendarPeriod()) {
			case 1:
				return "F";
			case 2:
				return "I";
			case 3:
				return "P";
			case 4:
				return "U";
			default:
				throw new ArahantException("Unknown term type encountered.");
		}
	}

	public String getTermName() {
		return BStudentVerification.getTermName(bean.getCalendarPeriod());
	}

	public static String getTermName(short term) {
		switch (term) {
			case 1:
				return "Fall";
			case 2:
				return "Winter";
			case 3:
				return "Spring";
			case 4:
				return "Summer";
			default:
				throw new ArahantException("Unknown term type encountered.");
		}
	}

	public int getYear() {
		return bean.getSchoolYear();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(StudentVerification.class, key);
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public void setTerm(String term) {
		switch (term.charAt(0)) {
			case 'F':
				bean.setCalendarPeriod(StudentVerification.PERIOD_FALL);
				break;
			case 'I':
				bean.setCalendarPeriod(StudentVerification.PERIOD_WINTER);
				break;
			case 'P':
				bean.setCalendarPeriod(StudentVerification.PERIOD_SPRING);
				break;
			case 'U':
				bean.setCalendarPeriod(StudentVerification.PERIOD_SUMMER);
				break;
			default:
				throw new ArahantException("Unknown term type encountered.");
		}
	}

	public void setYear(int year) {
		bean.setSchoolYear((short) year);
	}
}
