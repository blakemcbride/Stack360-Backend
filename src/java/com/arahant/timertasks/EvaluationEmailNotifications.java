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
import com.arahant.beans.HrEmployeeEval;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BCompany;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProperty;
import com.arahant.utils.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.kissweb.StringUtils;

public class EvaluationEmailNotifications extends TimerTaskBase {

	private static final ArahantLogger logger = new ArahantLogger(EvaluationEmailNotifications.class);

	@Override
	public void execute() throws Exception {

		//get all the supervisors of Company Groups
		for (OrgGroupAssociation oga : ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.ORGGROUPTYPE, BCompanyBase.COMPANY_TYPE).list()) {
			BEmployee supervisor = new BEmployee(oga.getPersonId());
			BOrgGroup orgGroup = new BOrgGroup(oga.getOrgGroup());
			//if he doesn't have an email entered, well we can't do anything anyway
			if (StringUtils.isEmpty(supervisor.getPersonalEmail())) {
				continue;
			}
			//get evaluation email info
			String notify = orgGroup.getEvalEmailNotify();
			int firstEvalAfter = orgGroup.getEvalEmailFirstDays();
			int notifyDays = orgGroup.getEvalEmailNotifyDays();
			String notifyRepeat = orgGroup.getEvalEmailNotifySendDays();
			Calendar todayCal = DateUtils.getNow();
			if (todayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || todayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				continue;
			} else if (notifyRepeat.charAt(todayCal.get(Calendar.DAY_OF_WEEK) - 2) == 'N') {
				continue;
			}
			//if this org group has eval notifications off, just continue
			if (notify.equals("N")) {
				continue;
			}

			//if this org group inherits eval notification information, get inherited
			if (notify.equals("I")) {
				notify = orgGroup.getEvalEmailNotifyInherited();
				firstEvalAfter = orgGroup.getEvalEmailFirstDaysInherited();
				notifyDays = orgGroup.getEvalEmailNotifyDaysInherited();
				notifyRepeat = orgGroup.getEvalEmailNotifySendDaysInherited();
			}

			//if inherited is "N" or "I", just continue
			if (!notify.equals("Y")) {
				continue;
			}

			int today = DateUtils.getDate(new Date());

			Criteria tempCrit = ArahantSession.getHSU().getSession().createCriteria(Employee.class);
			tempCrit.add(Restrictions.ne(Employee.PERSONID, supervisor.getPersonId()));
			tempCrit.createCriteria(Employee.ORGGROUPASSOCIATIONS, "oga");
			tempCrit.add(Restrictions.eq("oga." + OrgGroupAssociation.ORG_GROUP_ID, oga.getOrgGroupId()));
			tempCrit.createCriteria(Employee.HREMPLOYEEEVALSFOREMPLOYEEID, "evals");
			tempCrit.add(Restrictions.or
					(Restrictions.and
									(Restrictions.le("evals." + HrEmployeeEval.NEXTEVALDATE, DateUtils.addDays(today, notifyDays)),
											Restrictions.gt("evals." + HrEmployeeEval.NEXTEVALDATE, today)),
							// Restrictions.and(
							Restrictions.and(
									Restrictions.le("evals." + HrEmployeeEval.EVALDATE, DateUtils.addDays(today, notifyDays)),
									//Restrictions.gt("evals." + HrEmployeeEval.EVALDATE, today)
									//),
									Restrictions.eq("evals." + HrEmployeeEval.FINALDATE, 0)
							)
							// )
					)
			);
			ProjectionList pl = Projections.projectionList();
			pl.add(Projections.property(Employee.PERSONID));
			tempCrit.setProjection(pl);

//			for(Object temp : tempCrit.list())
//			{
//				System.out.println(new BEmployee((String)temp).getNameFML());
//			}
//
//			if(true)
//				return;


			//PASSED ALL CHECKS!  Now lets get people who need to be evaluated

			//get all persons who aren't the supervisor
//			HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).distinct().selectFields(Employee.PERSONID).ne(Employee.PERSONID, supervisor.getPersonId());
//			//who are in the supervisor's org group
//			hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, oga.getOrgGroup());
//
//
//			hcu.joinTo(Employee.HREMPLOYEEEVALSFOREMPLOYEEID, "evals");
//
//			HibernateCriterionUtil cri1 = hcu.makeCriteria();
//			HibernateCriterionUtil cri1a = hcu.makeCriteria();
//			HibernateCriterionUtil cri1b = hcu.makeCriteria();
//			HibernateCriterionUtil cri2 = hcu.makeCriteria();
//			HibernateCriterionUtil cri2a = hcu.makeCriteria();
//			HibernateCriterionUtil cri2b = hcu.makeCriteria();
//			HibernateCriterionUtil cri2c = hcu.makeCriteria();
//			HibernateCriterionUtil cri3 = hcu.makeCriteria();
//
//			cri1a.le("evals." + HrEmployeeEval.NEXTEVALDATE, DateUtils.addDays(today, notifyDays));
//			cri1b.gt("evals." + HrEmployeeEval.NEXTEVALDATE, today);
//			cri1.and(cri1a, cri1b);
//			cri2a.le("evals." + HrEmployeeEval.EVALDATE,DateUtils.addDays(today, notifyDays));
//			cri2b.gt("evals." + HrEmployeeEval.EVALDATE, today);
//			cri2c.eq("evals." + HrEmployeeEval.FINALDATE, 0);
//			cri2.and(cri2a, cri2b, cri2c);
//			cri3.or(cri1,cri2);
//			cri3.add();

			List empsToEval = tempCrit.list();

			List<Employee> otherEmps = ArahantSession.getHSU().createCriteria(Employee.class).ne(Employee.PERSONID, supervisor.getPersonId()).notIn(Employee.PERSONID, empsToEval).joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, oga.getOrgGroup()).list();
			for (Employee e : otherEmps) {
				BEmployee emp = new BEmployee(e);
				if (emp.getHireDate() > 0) {
					int firstEvalDate = DateUtils.addDays(emp.getHireDate(), firstEvalAfter);
					if (firstEvalDate < DateUtils.addDays(today, notifyDays) && firstEvalDate > today) {
						empsToEval.add(e.getPersonId());
					}
				}
			}
//			HibernateCriteriaUtil<Employee> hcu2 = hcu.joinTo(Employee.HREMPLOYEEEVALSFOREMPLOYEEID,"evals");
//			HibernateCriteriaUtil<Employee> hcu3 = hcu.joinTo(Employee.HREMPLSTATUSHISTORIES, "histories");
//
//			int today = DateUtils.getDate(new Date());
//			final HibernateCriterionUtil cri1 = hcu2.makeCriteria();
//			final HibernateCriterionUtil cri2 = hcu.makeCriteria();
//			final HibernateCriterionUtil cri2a = hcu.makeCriteria();
//			final HibernateCriterionUtil cri2b = hcu3.makeCriteria();
//			final HibernateCriterionUtil cri3 = hcu.makeCriteria();

			//who have an evaluation between now and 'notifyDays' in the future
//			cri1.le("evals." + HrEmployeeEval.NEXTEVALDATE, DateUtils.addDays(today, notifyDays)).gt("evals." + HrEmployeeEval.NEXTEVALDATE, today);
//			//OR
//			//don't have an evaluation at all
//			//cri2a.sizeEq(Employee.HREMPLOYEEEVALSFOREMPLOYEEID, 0);
//			//and its time for their initial evaluation
//			int day = DateUtils.addDays(DateUtils.addDays(today, -notifyDays),-firstEvalAfter);
//			cri2b.gt("histories." + HrEmplStatusHistory.EFFECTIVEDATE, day);
//			//cri2.and(cri2a, cri2b);
//			cri3.or(cri1, cri2b);
//			cri3.add();

			//List<Employee> empsToEval = hcu.list();
			//System.out.println("Supervisor " + supervisor.getNameFML() + " in " + oga.getOrgGroup().getName());
			//System.out.println("Returned " + empsToEval.size() + " Employees to evaluate.");
			if (empsToEval == null || empsToEval.size() == 0) {
				continue;
			}
			String message = "The following Employees need to be evaluated: \n\n";

			for (Object e : empsToEval) {
				BEmployee emp = new BEmployee((String) e);
				int evalDate = 0;
				//if they haven't had an evaluation yet
				if (emp.getLastEvaluationDate() == 0) {
					evalDate = DateUtils.addDays(emp.getCurrentHiredDate(), firstEvalAfter);
				} else {
					if (emp.getLastEvaluation().getFinalDate() == 0) {
						evalDate = emp.getLastEvaluation().getEvalDate();
					} else {
						evalDate = emp.getNextEvaluationDate();
					}
				}

				if (evalDate != 0) {
					if (emp.getLastEvaluationDate() != 0) {
						if (evalDate < today) {
							message += emp.getNameLFM() + " -- " + DateUtils.getDateFormatted(evalDate) + " (LATE)\n";
						} else {
							message += emp.getNameLFM() + " -- " + DateUtils.getDateFormatted(evalDate) + "\n";
						}
					} else {
						if (evalDate < today) {
							message += emp.getNameLFM() + " -- " + DateUtils.getDateFormatted(evalDate) + " (FIRST TIME -- LATE)\n";
						} else {
							message += emp.getNameLFM() + " -- " + DateUtils.getDateFormatted(evalDate) + " (FIRST TIME)\n";
						}
					}
				}
			}

			Mail.send(BProperty.get(StandardProperty.AdminEmail), supervisor.getPersonalEmail(), "Evaluation Notification", message);
			logger.info("From: " + BProperty.get(StandardProperty.AdminEmail) + "\nTo: " + supervisor.getPersonalEmail() + "\nSubject: " + "Evaluation Notification\n\n" + message);
			System.out.println("From: " + BProperty.get(StandardProperty.AdminEmail) + "\nTo: " + supervisor.getPersonalEmail() + "\nSubject: " + "Evaluation Notification\n\n" + message);
		}
	}

	public static void main(String[] args) {
		try {
			EvaluationEmailNotifications imp = new EvaluationEmailNotifications();
			imp.hsu = ArahantSession.getHSU();
			imp.hsu.setCurrentPersonToArahant();
			BCompany b = new BCompany("00001-0000000001");
			imp.hsu.setCurrentCompany(b.getBean());

			imp.execute();
		} catch (Exception ex) {
			Logger.getLogger(EvaluationEmailNotifications.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
