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


/**
 *
 */
package com.arahant.services.standard.time.timesheetByPayPeriod;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.exports.AccruedTimeOffByGroupExport;
import com.arahant.reports.AccruedTimeOffByGroupReport;
import com.arahant.reports.TimeDetailReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetByPayPeriodOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetByPayPeriodOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimesheetByPayPeriodOps.class);

	public TimesheetByPayPeriodOps() {
	}

	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*
			 * @WebParam(name = "in")
			 */final SearchOrgGroupsInput in) {
		final SearchOrgGroupsReturn ret = new SearchOrgGroupsReturn();
		try {
			checkLogin(in);

			if (BRight.checkRight("SeeAllOrgGroups") == ACCESS_LEVEL_WRITE)
				ret.setItem(BOrgGroup.search(in.getName(), false, ret.getHighCap()));
			else {
				List<OrgGroup> parentGroups = hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').list();

				List<OrgGroup> childGroups = new ArrayList<OrgGroup>();
				for (OrgGroup o : parentGroups) {
					BOrgGroup bo = new BOrgGroup(o);
					childGroups.addAll(bo.getAllOrgGroupsInHierarchy2());
				}
				ret.setItem(BOrgGroup.makeArray(childGroups));
			}

			if (!isEmpty(in.getId())) {
				if (ret.getItem().length <= ret.getLowCap()) {
					//if it's in the list, set selected item
					for (SearchOrgGroupsReturnItem ogri : ret.getItem())
						if (in.getId().equals(ogri.getId()))
							ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
				} else
					for (BOrgGroup borg : BPerson.getCurrent().searchSubordinateGroups(hsu, in.getName(), 0))
						if (in.getId().equals(borg.getOrgGroupId()))
							ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
			} else {
				OrgGroup og = hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').first();


//				if (og==null)
//					og=hsu.createCriteria(OrgGroup.class)
//						.joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
//						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
//						.first();

				if (og != null)
					ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(og)));
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*
			 * @WebParam(name = "in")
			 */final SearchPersonsInput in) {
		final SearchPersonsReturn ret = new SearchPersonsReturn();
		try {
			checkLogin(in);

			if (in.getIncludeSubGroups())
				ret.setItem(BPerson.searchPersonsInHierarchy(in.getOrgGroupId(), in.getLastName(), in.getFirstName(), new String[0], ret.getHighCap()));
			else
				ret.setItem(BPerson.makeArray(ArahantSession.getHSU().createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, in.getOrgGroupId()).list()));

			/*
			 * if (!isEmpty(in.getProjectId())) { BProject bp=new
			 * BProject(in.getProjectId()); if (bp.getCurrentPerson()!=null)
			 * ret.setSelectedItem(new
			 * SearchPersonsReturnItem(bp.getCurrentPerson()));
			}
			 */

			if (!isEmpty(in.getPersonId()))
				if (ret.getItem().length <= ret.getLowCap()) {
					//if it's in the list, set selected item
					for (SearchPersonsReturnItem ogri : ret.getItem())
						if (in.getPersonId().equals(ogri.getPersonId()))
							ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(in.getPersonId())));
				} else
					for (BPerson bp : BPerson.searchPersons(in.getOrgGroupId(), in.getLastName(), in.getFirstName(), 0))
						if (in.getPersonId().equals(bp.getPersonId()))
							ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(in.getPersonId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListPayPeriodForGroupReturn listPayPeriodForGroup(/*
			 * @WebParam(name = "in")
			 */final ListPayPeriodForGroupInput in) {

		final ListPayPeriodForGroupReturn ret = new ListPayPeriodForGroupReturn();

		try {
			checkLogin(in);

			BOrgGroup og = new BOrgGroup(in.getOrgGroupId());
			if (og.getPaySchedule() == null)
				throw new ArahantWarning("Pay Periods not set up for this organizational group.");
			else
				ret.setItem(BPaySchedulePeriod.search(og.getPaySchedule().getPayScheduleId(), 0, 0, 10000));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchTimesheetsReturn searchTimesheets(/*
			 * @WebParam(name = "in")
			 */final SearchTimesheetsInput in) {

		final SearchTimesheetsReturn ret = new SearchTimesheetsReturn();

		try {
			checkLogin(in);
			HibernateCriteriaUtil<Employee> hcu;

			if (!isEmpty(in.getPersonId()))
				hcu = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.PERSONID, in.getPersonId());
			else
				if (in.getIncludeSubGroups()) {
					BOrgGroup bog = new BOrgGroup(in.getOrgGroupId());
					hcu = ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).notNull(Person.TIMESHEETS).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).in(OrgGroup.ORGGROUPID, bog.getAllOrgGroupsInHierarchy());
				} else
					hcu = ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).notNull(Person.TIMESHEETS).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, in.getOrgGroupId());




			HibernateScrollUtil<Employee> scr = hcu.scroll();
			List<SearchTimesheetsReturnItem> item = new ArrayList<SearchTimesheetsReturnItem>();
			List<SearchTimesheetsReturnTimeRelated> accruedTime = new ArrayList<SearchTimesheetsReturnTimeRelated>();

			while (scr.next()) {
				BEmployee be = new BEmployee(scr.get());
				int currentDate = 0;
				double totalDailyHours = 0.0;
				double totalHours = 0.0;
				HashMap<String, Double> pto = new HashMap<String, Double>();

				List<Timesheet> ts = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, be.getPerson()).dateBetween(Timesheet.WORKDATE, in.getStartDate(), in.getEndDate()).orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).list();
				for (Timesheet t : ts) {
					BTimesheet bt = new BTimesheet(t);

					if (bt.isTimeOff() && bt.getTotalHours() > 0) {
						BHRBenefit bene = new BHRBenefit(bt.getBenefitId());
						pto.put(bene.getBenefitId(), pto.get(bene.getBenefitId()) == null ? bt.getTotalHours() : pto.get(bene.getBenefitId()) + bt.getTotalHours());
					}

					if (bt.getWorkDate() != currentDate && currentDate != 0) {
						item.add(new SearchTimesheetsReturnItem(be, currentDate, totalDailyHours));
						item.add(new SearchTimesheetsReturnItem(bt));
						currentDate = bt.getWorkDate();
						totalDailyHours = 0.0;
					} else {
						item.add(new SearchTimesheetsReturnItem(bt));
						currentDate = bt.getWorkDate();
					}

					totalDailyHours += bt.getTotalHours();
					totalHours += bt.getTotalHours();
				}

				if (totalHours > 0) {
					item.add(new SearchTimesheetsReturnItem(be, currentDate, totalDailyHours));
					item.add(new SearchTimesheetsReturnItem(be, totalHours));

					for (String key : pto.keySet())
						accruedTime.add(new SearchTimesheetsReturnTimeRelated(be, key, pto.get(key), in.getStartDate(), in.getEndDate()));
				}

//				if(be.getProject().getBenefitAccrual(new BEmployee(bt.getPersonId())) == 0)
//					ret.setTimeRelated(a);
//				ret.setItem(BTimesheet.list(ret.getCap()));
			}
			ret.setItem(item);
			ret.setTimeRelated(accruedTime);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*
			 * @WebParam(name = "in")
			 */final GetProjectDetailInput in) {
		final GetProjectDetailReturn ret = new GetProjectDetailReturn();

		try {
			checkLogin(in);

			ret.setData(new BProject(in.getProjectId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetTimesheetDetailReturn getTimesheetDetail(/*
			 * @WebParam(name = "in")
			 */final GetTimesheetDetailInput in) {
		final GetTimesheetDetailReturn ret = new GetTimesheetDetailReturn();

		try {
			checkLogin(in);

			ret.setData(new BTimesheet(in.getTimesheetId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReportAccruedTimeOffReturn reportAccruedTimeOff(/*
			 * @WebParam(name = "in")
			 */final ReportAccruedTimeOffInput in) {
		final ReportAccruedTimeOffReturn ret = new ReportAccruedTimeOffReturn();
		try {
			checkLogin(in);

			ret.setFileName(new BEmployee(in.getEmployeeId()).getAccruedTimeOffReport(in.getAccrualAccountId(), in.getStartDate(), in.getEndDate()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {

		final GetReportReturn ret = new GetReportReturn();

		try {
			checkLogin(in);

			if (in.getReportType().equals("1"))
				ret.setReportUrl(new TimeDetailReport().build(new BOrgGroup(in.getOrgGroupId()).getOrgGroup(), in.getFromDate(), in.getToDate()));
//			else if(in.getExportType().equals("2"))
//				ret.setReportUrl("");
			else
				throw new ArahantWarning("Export type not specified.");

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitsReturn listBenefits(/*
			 * @WebParam(name = "in")
			 */final ListBenefitsInput in) {
		final ListBenefitsReturn ret = new ListBenefitsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefit.listTimeRelatedBenefits(hsu));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetAccrualTotalsReportReturn getAccrualTotalsReport(/*
			 * @WebParam(name = "in")
			 */final GetAccrualTotalsReportInput in) {

		final GetAccrualTotalsReportReturn ret = new GetAccrualTotalsReportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl(new AccruedTimeOffByGroupReport().Build(in.getOrgGroupId(), in.getIncludeSubGroups(), in.getBenefitId(), in.getSortType()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetAccrualTotalsExportReturn getAccrualTotalsExport(/*
			 * @WebParam(name = "in")
			 */final GetAccrualTotalsExportInput in) {

		final GetAccrualTotalsExportReturn ret = new GetAccrualTotalsExportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl(new AccruedTimeOffByGroupExport().build(in.getOrgGroupId(), in.getIncludeSubGroups(), in.getBenefitId(), in.getSortType()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
