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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.Holiday;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.Timesheet;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import java.util.Calendar;
import java.util.List;

public class AutoLogTime extends TimerTaskBase {

	private static final ArahantLogger logger = new ArahantLogger(AutoLogTime.class);

	 /*
	private String[] doHolidayTimesheets()
	{
		for each company
			find out holiday project by select project join to benefit where benefit_rule="GatesHoliday" and the benefit join to benefit category then benefit category company = company in loop
			for each person in company
			{
				check if person has been there 90 days
				if not, continue
				call make timesheet with holiday project

			}

	}
	*/

	@Override
	public void execute() throws Exception {

		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return;

		int today = DateUtils.now();

		//Check if today is a holiday
		Holiday h = ArahantSession.getHSU().createCriteria(Holiday.class).eq(Holiday.HDATE, DateUtils.now()).first();

		if (h != null) {
			char partOfDay = h.getPartOfDay();

			for (CompanyDetail cd : ArahantSession.getHSU().createCriteria(CompanyDetail.class).list()) {
				ArahantSession.getHSU().setCurrentCompany(cd);

				//Find out holiday project
				String projectId = ArahantSession.getHSU().createCriteria(Project.class).selectFields(Project.PROJECTID)
						.joinTo(Project.HRBENEFITPROJECTJOINS)
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.eq(HrBenefit.RULENAME, "GatesHoliday").stringVal();

				for (Person p : ArahantSession.getHSU().getCurrentCompany().getPersons()) {
					if (p instanceof Employee) {
						BEmployee be = new BPerson(p).getBEmployee();

						//If Employee has been with the company 90 days or more
						if ((DateUtils.now() - be.getHireDate()) >= 90) {
							BTimesheet bt = new BTimesheet();
							bt.create();
							bt.setPersonId(be.getPersonId());

							if ((be.getExpectedHoursPerPayPeriod() / 5) > 0)
								bt.setTotalHours(be.getExpectedHoursPerPayPeriod() / 5);
							else
								bt.setTotalHours(8);

							bt.setState('N');

							if (partOfDay == '1') {
								bt.setBeginningTime(80000000);
								bt.setTotalHours(bt.getTotalHours() / 2);
							} else if (partOfDay == '2') {
								bt.setBeginningTime(130000000);
								bt.setTotalHours(bt.getTotalHours() / 2);
							} else  //partOfDay == 'F'
							{
								bt.setBeginningTime(80000000);

							}

							bt.setEndTime((int) (bt.getBeginningTime() + (bt.getTotalHours() * 10000000)));

							bt.setDescription("Holiday");
							bt.setEndDate(today);
							bt.setWorkDate(today);
							bt.setBeginningEntryDate(new java.util.Date());
					//		bt.setProjectId(projectId);
							if (true) throw new ArahantException("XXYY");
							BProject bp = new BProject(projectId);
							bt.setBillable(bp.getBillable());
							bt.setWageTypeId(be.getWageTypeId());
							try {
								bt.insert();
							} catch (Exception e) //Employee does not have benefit associated to this project
							{
								logger.error(e);
								continue;
							}

							try {
								be.finalizeTime(today);
								be.update();
							} catch (Exception e) {
								logger.error(e);
								continue;
							}
						}
					}
				}

			}
		}

		List<String> didTimes = (List) hsu.createCriteriaNoCompanyFilter(Employee.class)
				.selectFields(Employee.PERSONID)
				.eq(Employee.AUTO_LOG_TIME, 'Y')
				.joinTo(Employee.TIMESHEETS)
				.eq(Timesheet.WORKDATE, DateUtils.now())
				.list();

		HibernateScrollUtil<Employee> scr = hsu.createCriteriaNoCompanyFilter(Employee.class)
				.notIn(Employee.PERSONID, didTimes)
				.eq(Employee.AUTO_LOG_TIME, 'Y')
				.scroll();

		while (scr.next()) {
			BEmployee bemp = new BEmployee(scr.get());

			if (bemp.getDefaultProject() == null)
				continue;

			BTimesheet bt = new BTimesheet();
			bt.create();
			bt.setPersonId(bemp.getPersonId());
			bt.setTotalHours(bemp.getExpectedHoursPerPayPeriod() / 5);
			bt.setState('N');
			bt.setBeginningTime(80000000);
			bt.setEndTime((int) (bt.getBeginningTime() + (bt.getTotalHours() * 10000000)));
			bt.setDescription("Auto Time Entry");
			bt.setEndDate(today);
			bt.setWorkDate(today);
			bt.setBeginningEntryDate(new java.util.Date());
	//		bt.setProjectId(bemp.getDefaultProjectId());
			if (true) throw new ArahantException("XXYY");
			BProject bp = new BProject(bemp.getDefaultProject());
			bt.setBillable(bp.getBillable());
			bt.setWageTypeId(bemp.getWageTypeId());
			bt.insert();

			try {
				bemp.finalizeTime(today);
				bemp.update();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		scr.close();

	}


	public static void main(String args[]) {
		//AutoLogTime atl=new AutoLogTime();
		//atl.run();

	}
}
