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

import com.arahant.beans.Employee;
import com.arahant.beans.EmployeeChanged;
import com.arahant.beans.Person;
import com.arahant.utils.StandardProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.exports.CompuPayExport;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class BEmployeeChanged {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BEmployeeChanged.class);

	private static final String[] exports = {"CompuPay", "ConsociatesEDI", "PayDay"};

	public static void runExport(String id) {

		switch (Short.parseShort(id)) {
			case 0:
				runCompuPayExport();
				break;
			case 1:
				runConsociatesEDI();
				break;
			default:
				throw new ArahantException("Unknown export requested - id was " + id);
		}
	}

	private static void runCompuPayExport() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<EmployeeChanged> l = hsu.createCriteria(EmployeeChanged.class)
				.eq(EmployeeChanged.INTERFACEID, (short) 0)
				.list();

		List<Person> emps = new LinkedList<Person>();
		for (EmployeeChanged ec : l)
			emps.add(ec.getEmployee());

		hsu.delete(l);

		CompuPayExport cpe = new CompuPayExport();
		for (Person e : emps)
			cpe.exportEmployee(new BEmployee(e.getPersonId()));
	}

	private static void runConsociatesEDI() {
		String ddi = BProperty.get(StandardProperty.ConsociatesId);
		if (ddi != null && !"".equals(ddi))
			try {
				logger.info("Doing EDI transmission");
				ArahantSession.getHSU().beginTransaction();
				ArahantSession.getHSU().setCurrentPersonToArahant();

				final BEDITransaction x = new BEDITransaction();

				x.create();

				x.setReceiver(BProperty.get(StandardProperty.ConsociatesId));

				//	x.insert();
				ArahantSession.getHSU().commitTransaction();

				ArahantSession.getHSU().beginTransaction();

				x.sendExport();

			} catch (final Exception e) {
				ArahantSession.getHSU().rollbackTransaction();
				logger.error(e);
			}
	}

	public static class Export {
		public String name;
		public String key;

		private Export(Short s) {
			key = s.toString();
			name = exports[s];
		}
	}

	public static Export[] listExports() {
		List ids = ArahantSession.getHSU().doQuery("select distinct interfaceId from EmployeeChanged order by interfaceId");

		Export[] ret = new Export[ids.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new Export((Short) ids.get(loop));

		return ret;
	}

	public static void setEmployeeChanged(String personId, int interfaceId) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			if (hsu.createCriteria(EmployeeChanged.class)
					.eq(EmployeeChanged.INTERFACEID, (short) interfaceId)
					.joinTo(EmployeeChanged.EMPLOYEE)
					.eq(Employee.PERSONID, personId).exists())
				return;

			Employee emp = hsu.get(Employee.class, personId);
			if (emp == null)
				return; //not an employee
			EmployeeChanged ec = new EmployeeChanged();
			ec.setEarliestChangeDate(new Date());
			ec.setEmployee(emp);
			ec.setInterfaceId((short) interfaceId);

			hsu.insert(ec);
		} catch (org.hibernate.NonUniqueObjectException noex) {
			//Already done this one
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void setPersonChanged(String personId, int interfaceId) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			if (hsu.createCriteria(EmployeeChanged.class)
					.eq(EmployeeChanged.INTERFACEID, (short) interfaceId)
					.joinTo(EmployeeChanged.EMPLOYEE)
					.eq(Employee.PERSONID, personId).exists())
				return;

			Person emp = hsu.get(Person.class, personId);
			if (emp == null)
				return; //not an employee
			EmployeeChanged ec = new EmployeeChanged();
			ec.setEarliestChangeDate(new Date());
			ec.setEmployee(emp);
			ec.setInterfaceId((short) interfaceId);

			hsu.insert(ec);
		} catch (org.hibernate.NonUniqueObjectException noex) {
			//Already done this one
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String args[]) {
		ArahantSession.getHSU().beginTransaction();
		ArahantSession.getHSU().setCurrentPersonToArahant();

		BEmployeeChanged.setEmployeeChanged("00001-0000000001", 0);
		ArahantSession.getHSU().commitTransaction();
	}

}
