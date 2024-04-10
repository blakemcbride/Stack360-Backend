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

package com.arahant.timertasks;

import com.arahant.beans.Employee;
import com.arahant.beans.EmployeeChanged;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Invoice;
import com.arahant.beans.Person;
import com.arahant.business.BEmployeeChanged;
import com.arahant.business.BInvoice;
import com.arahant.business.BProject;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WmCoNotifyLateBilling extends TimerTaskBase {

	@Override
	public void execute() throws Exception {

		List<String> donePersonIds = new LinkedList<String>();

		Calendar sixtyDaysAgo = Calendar.getInstance();
		sixtyDaysAgo.add(Calendar.DAY_OF_YEAR, -60);


		//delete notify exports over 60 days
		hsu.createCriteria(EmployeeChanged.class).eq(EmployeeChanged.INTERFACEID, EmployeeChanged.TYPE_NOTIFY_LATE_BILLING)
				.lt(EmployeeChanged.DATE, sixtyDaysAgo.getTime())
				.delete();

		hsu.createCriteria(EmployeeChanged.class).eq(EmployeeChanged.INTERFACEID, EmployeeChanged.TYPE_NOTIFY_LATE_BILLING_TIER_2)
				.lt(EmployeeChanged.DATE, sixtyDaysAgo.getTime())
				.delete();


		//add people already done tier 2
		for (EmployeeChanged ec : hsu.createCriteria(EmployeeChanged.class).eq(EmployeeChanged.INTERFACEID, EmployeeChanged.TYPE_NOTIFY_LATE_BILLING_TIER_2).list())
			donePersonIds.add(ec.getEmployee().getPersonId());

		//find anyone on COBRA whose first bill is over 45 days late

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
				.notIn(Person.PERSONID, donePersonIds);

		hcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
				.eq(HrBenefitJoin.USING_COBRA, 'Y');

		hcu.sizeNe(Person.INVOICES, 0);

		HibernateScrollUtil<Person> scr = hcu.scroll();

		Calendar fortyFiveDaysAgo = Calendar.getInstance();
		fortyFiveDaysAgo.add(Calendar.DAY_OF_YEAR, -45);

		while (scr.next()) {
			Person p = scr.get();
			Invoice i = hsu.createCriteria(Invoice.class)
					.eq(Invoice.PERSON, p)
					.lt(Invoice.CREATEDATE, fortyFiveDaysAgo.getTime())
					.orderBy(Invoice.CREATEDATE)
					.first();

			if (i == null)
				continue;

			if (i.getCreateDate() == null) {
				continue;
			}

			if (i.getCreateDate().after(fortyFiveDaysAgo.getTime()))
				continue;

			BInvoice bi = new BInvoice(i);
			if (bi.getBalance() > 0) {
				BProject.makeProject(p.getPersonId(), "00001-0000005729", "COBRA 45 Days Late",
						p.getNameLFM() + " " + p.getUnencryptedSsn() + " is over 45 days late on paying for COBRA",
						"00001-0000000005");

				donePersonIds.add(p.getPersonId());

				BEmployeeChanged.setPersonChanged(p.getPersonId(), EmployeeChanged.TYPE_NOTIFY_LATE_BILLING_TIER_2);

			}
		}

		scr.close();

		//add people already done tier 1
		for (EmployeeChanged ec : hsu.createCriteria(EmployeeChanged.class).eq(EmployeeChanged.INTERFACEID, EmployeeChanged.TYPE_NOTIFY_LATE_BILLING).list())
			donePersonIds.add(ec.getEmployee().getPersonId());

		//find anyone on COBRA who has a bill over 30 days late
		hcu = hsu.createCriteria(Person.class)
				.notIn(Person.PERSONID, donePersonIds);

		hcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
				.eq(HrBenefitJoin.USING_COBRA, 'Y');

		hcu.sizeGt(Person.INVOICES, 1);

		scr = hcu.scroll();

		Calendar thirtyDaysAgo = Calendar.getInstance();
		thirtyDaysAgo.add(Calendar.DAY_OF_YEAR, -30);

		while (scr.next()) {
			Person p = scr.get();
			Invoice i = hsu.createCriteria(Invoice.class)
					.eq(Invoice.PERSON, p)
					.lt(Invoice.CREATEDATE, thirtyDaysAgo.getTime())
					.orderByDesc(Invoice.CREATEDATE)
					.first();

			if (i == null)
				continue;

			if (i.getCreateDate().after(thirtyDaysAgo.getTime()))
				continue;

			BInvoice bi = new BInvoice(i);
			if (bi.getBalance() > 0) {
				//need to do notify
				BProject.makeProject(p.getPersonId(), "00001-0000005729", "COBRA 30 Days Late",
						p.getNameLFM() + " " + p.getUnencryptedSsn() + " is over 30 days late on paying for COBRA",
						"00001-0000000005");

				donePersonIds.add(p.getPersonId());

				BEmployeeChanged.setPersonChanged(p.getPersonId(), EmployeeChanged.TYPE_NOTIFY_LATE_BILLING);
			}
		}

		scr.close();

		//find LOA's over 30 days late
		HibernateCriteriaUtil<Employee> ehcu = hsu.createCriteria(Employee.class)
				.notIn(Employee.PERSONID, donePersonIds)
				.employeeCurrentStatus("00001-0000000003"); //LOA status

		ehcu.joinTo(Employee.INVOICES)
				.lt(Invoice.CREATEDATE, thirtyDaysAgo.getTime());

		HibernateScrollUtil<Employee> escr = ehcu.scroll();

		while (escr.next()) {
			Person p = escr.get();
			Invoice i = hsu.createCriteria(Invoice.class)
					.eq(Invoice.PERSON, p)
					.orderByDesc(Invoice.CREATEDATE)
					.first();

			BInvoice bi = new BInvoice(i);
			if (bi.getBalance() > 0) {
				//need to do notify
				BProject.makeProject(p.getPersonId(), "00001-0000006717", "LOA Benefit Payments 30 Days Late",
						p.getNameLFM() + " " + p.getUnencryptedSsn() + " is over 30 days late on paying for benefits.",
						"00001-0000000005");

				donePersonIds.add(p.getPersonId());
			}
		}

		escr.close();

		//find anyone on COBRA that has reached their max months
		Calendar aYearAgo = DateUtils.getNow();
		aYearAgo.add(Calendar.YEAR, -1);

		HibernateScrollUtil<HrBenefitJoin> bjscr = hsu.createCriteria(HrBenefitJoin.class)
				.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
				.eq(HrBenefitJoin.USING_COBRA, 'Y')
				.le(HrBenefitJoin.ACCEPTED_COBRA_DATE, DateUtils.getDate(aYearAgo))
				.scroll();

		while (bjscr.next()) {
			HrBenefitJoin bj = bjscr.get();
			Calendar deadline = Calendar.getInstance();
			deadline.add(Calendar.MONTH, -bj.getMaxMonthsCOBRA());

			Calendar start = DateUtils.getCalendar(bj.getAcceptedDateCOBRA());

			if (start.before(deadline)) {
				System.out.println("max months exceeded");
				Person p = bj.getPayingPerson();
				BProject.makeProject(p.getPersonId(), "00001-0000005729", "COBRA maximum months reached",
						p.getNameLFM() + " " + p.getUnencryptedSsn() + " has exceeded their allowed cobra months.",
						"00001-0000000005");
			}

		}
		bjscr.close();
	}

	public static void main(String args[]) throws Exception {
		new WmCoNotifyLateBilling().execute();
	}
}
