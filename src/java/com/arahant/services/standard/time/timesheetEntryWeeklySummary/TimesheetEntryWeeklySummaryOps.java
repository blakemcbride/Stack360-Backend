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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
//import com.arahant.reports.waytogo.SignInForm;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.IDGenerator;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateUtils;
import org.kissweb.TimeUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetEntryWeeklySummaryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetEntryWeeklySummaryOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(TimesheetEntryWeeklySummaryOps.class);

	@WebMethod()
	public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in) {
		final SearchCompanyByTypeReturn ret = new SearchCompanyByTypeReturn();
		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(), false, ret.getHighCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();

		try {
			checkLogin(in);
			boolean includeUser = false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES) == ACCESS_LEVEL_WRITE)
				includeUser = true;

			if (hsu.currentlyArahantUser())
				includeUser = false;

			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(), in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1, includeUser));

			if (!hsu.currentlyArahantUser() && includeUser)
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			int accessLevel = BRight.checkRight(PROJECT_ACCESS_UNASSIGNED);

			ret.setProjects(Arrays.asList(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
                    0, 0, in.getPersonId(), in.isQuickList(), in.getUser(), null, null, ret.getHighCap())), accessLevel, BPerson.getCurrent().getPerson());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("SetBillable"));
			ret.setChangeDescription(BRight.checkRight(CHANGE_TIMESHEET_DESCRIPTION));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	private void assignPersonToShift(String shiftID, String personID) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		final BProjectShift bps = new BProjectShift(shiftID);
        final BPerson bper = new BPerson(personID);
		ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
				.eq(ProjectEmployeeJoin.PERSON, bper.getPerson())
				.eq(ProjectEmployeeJoin.PROJECTSHIFT, bps.getProjectShift())
				.first();
		if (pej == null) {
			pej = new ProjectEmployeeJoin();
			pej.generateId();
			pej.setPerson(bper.getPerson());
			pej.setPersonPriority((short) 10);
			pej.setDateAssigned(DateUtils.today());
			pej.setTimeAssigned(TimeUtils.now());
			pej.setProjectShift(bps.getProjectShift());
			hsu.insert(pej);
		}
    }

	@WebMethod()
	public AddTimeEntryReturn addTimeEntry(/*@WebParam(name = "in")*/final AddTimeEntryInput in) {
		final AddTimeEntryReturn ret = new AddTimeEntryReturn();

		try {
			checkLogin(in);

			BEmployee bemp;

			if (!hsu.currentlySuperUser() && isEmpty(in.getPersonId()))
				bemp = new BEmployee(BPerson.getCurrent());
			else {
				if (in.getPersonId().equals(hsu.getSuperUserPersonId()))
					throw new ArahantWarning(ArahantSession.systemName() + " user may not log time.");
				bemp = new BEmployee(in.getPersonId());
			}

			final String benefitId = bemp.newTimeEntry(in.getBeginningTime(),
					"Y", in.getDescription(), in.getEndTime(), in.getPersonId(),
					in.getProjectId(), in.getShiftId(), in.getTotalHours(), in.getWorkDate(), -1, "", in.getTotalExpenses(), in.getTotalPay(), 0);

            assignPersonToShift(in.getShiftId(), in.getPersonId());

			if (!isEmpty(benefitId)) {
				final BHRBenefit b = new BHRBenefit(benefitId);
				ret.setOverrunBenefitName(b.getName());
				ret.setOverrunBenefitHours(bemp.getTimeOffCurrentPeriod(benefitId));
			}

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    private void unassignPerson(String shiftID, String personID) throws Exception {
		final Connection db = ArahantSession.getKissConnection();
		final Record rec = db.fetchOne("select * from project_employee_join where project_shift_id=? and person_id=?", shiftID, personID);
		if (rec != null) {
			final Record hrec = db.newRecord("project_employee_history");
			IDGenerator.generate(hrec, "project_employee_history_id");
			hrec.set("person_id", personID);
			hrec.set("change_person_id", ArahantSession.getCurrentPerson().getPersonId());
			hrec.set("change_date", DateUtils.today());
			hrec.set("change_time", TimeUtils.now());
			hrec.set("change_type", "D");
			hrec.set("project_shift_id", shiftID);
			hrec.addRecord();
			rec.delete();
		}
    }

	@WebMethod()
	public DeleteTimesheetReturn deleteTimesheet(/*@WebParam(name = "in")*/final DeleteTimesheetInput in) {
		final DeleteTimesheetReturn ret = new DeleteTimesheetReturn();
		try {
			checkLogin(in);

			List<Timesheet> tshts = hsu.createCriteria(Timesheet.class).in(Timesheet.TIMESHEETID, in.getTimesheetIds()).list();
			for (Timesheet ts : tshts)
				if (ts.getState() != 'I'  &&  ts.getState() != 'A')
					hsu.delete(ts);
			unassignPerson(in.getShiftId(), in.getPersonID());
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ApproveTimeReturn approveTime(/*@WebParam(name = "in")*/final ApproveTimeInput in) {

		final ApproveTimeReturn ret = new ApproveTimeReturn();

		try {
			checkLogin(in);
			boolean approve = "A".equals(in.getDirection());
			List<Timesheet> tshts = hsu.createCriteria(Timesheet.class).in(Timesheet.TIMESHEETID, in.getTimesheetIds()).list();
			for (Timesheet ts : tshts) {
				if (ts.getState() != 'I')
					if (approve)
						ts.setState('A');
					else if (ts.getState() == 'A')
						ts.setState('N');
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetNextRejectedDateReturn getNextRejectedDate(/*@WebParam(name = "in")*/final GetNextRejectedDateInput in) {
		final GetNextRejectedDateReturn ret = new GetNextRejectedDateReturn();

		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());

			ret.setDate(p.getNextRejectedDate(in.getDate()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetPreviousRejectedDateReturn getPreviousRejectedDate(/*@WebParam(name = "in")*/final GetPreviousRejectedDateInput in) {
		final GetPreviousRejectedDateReturn ret = new GetPreviousRejectedDateReturn();

		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());

			ret.setDate(p.getPreviousRejectedDate(in.getDate()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListTimesheetsForProjectOnDateReturn listTimesheetsForProjectOnDate(/*@WebParam(name = "in")*/final ListTimesheetsForProjectOnDateInput in) {

		final ListTimesheetsForProjectOnDateReturn ret = new ListTimesheetsForProjectOnDateReturn();

		try {
			checkLogin(in);
			final String shiftId = in.getShiftId();

			List<Timesheet> tsLst;
			if (shiftId == null || shiftId.isEmpty())
				tsLst = hsu.createCriteria(Timesheet.class)
						.eq(Timesheet.WORKDATE, in.getWorkDate())
						.joinTo(Timesheet.PROJECTSHIFT)
						.eq(ProjectShift.PROJECT_ID, in.getProjectId())
						.list();
			else
				tsLst = hsu.createCriteria(Timesheet.class)
						.eq(Timesheet.WORKDATE, in.getWorkDate())
						.eq(Timesheet.PROJECT_SHIFT_ID, shiftId)
						.list();
			if (in.isShowAll()) {
				BProject bp = new BProject(in.getProjectId());
				List<Person> emps = bp.getAssignedPersons2(null);
				for (Person emp : emps) {
					boolean found = false;
					for (Timesheet ts : tsLst) {
						if (ts.getPersonId().equals(emp.getPersonId())) {
							found = true;
							break;
						}
					}
					if (!found) {
						String newShiftId = shiftId;
						if (newShiftId == null || newShiftId.isEmpty()) {
							ProjectShift shft = hsu.createCriteria(ProjectShift.class)
									.eq(ProjectShift.PROJECT_ID, in.getProjectId())
									.first();
							newShiftId = shft.getProjectShiftId();
							// this should actually never be used because the front-end must not
						}
						Timesheet ts = new Timesheet();
						ts.setProjectShift(new BProjectShift(newShiftId).getProjectShift());
						ts.setPersonId(emp.getPersonId());
						ts.setWorkDate(in.getWorkDate());
						ts.setBillable('Y');
						ts.setEndDate(in.getWorkDate());
						ts.setBeginningEntryDate(new Date());
						ts.setBeginningEntryPerson(BPerson.getCurrent().getPerson());
						ts.setState('N');
						ts.setTotalHours(0.0);
						ts.setTimesheetId("New");  // signal new / not in DB
						tsLst.add(ts);
					}
				}
			}

			BTimesheet [] bts = new BTimesheet[tsLst.size()];
			int i = 0;
			double totalHours = 0;
			for (Timesheet t : tsLst) {
				bts[i++] = new BTimesheet(t);
				totalHours += t.getTotalHours();
			}
			Arrays.sort(bts, new Comparator<BTimesheet>() {
				@Override
				public int compare(BTimesheet o1, BTimesheet o2) {
					return (new BPerson(o1.getTimesheet().getPersonId())).getNameLFM()
							.compareToIgnoreCase((new BPerson(o2.getTimesheet().getPersonId())).getNameLFM());
			//		return o1.getTimesheet().getPerson().getNameLFM().compareToIgnoreCase(o2.getTimesheet().getPerson().getNameLFM());
				}
			});

			ret.setTimesheetTransmit(bts);
			ret.setTotalHours(totalHours);
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadTimeEntryReturn loadTimeEntry(/*@WebParam(name = "in")*/final LoadTimeEntryInput in) {
		final LoadTimeEntryReturn ret = new LoadTimeEntryReturn();

		try {
			checkLogin(in);

			final BTimesheet t = new BTimesheet(in.getTimesheetId());

			ret.setData(t);

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public MarkRejectionCorrectedReturn markRejectionCorrected(/*@WebParam(name = "in")*/final MarkRejectionCorrectedInput in) {

		final MarkRejectionCorrectedReturn ret = new MarkRejectionCorrectedReturn();

		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());

			p.markRejectionCorrected(in.getDate());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveTimeEntryReturn saveTimeEntry(/*@WebParam(name = "in")*/final SaveTimeEntryInput in) {


		final SaveTimeEntryReturn ret = new SaveTimeEntryReturn();

		try {
			checkLogin(in);

			BEmployee bemp;
			if (!hsu.currentlySuperUser() && isEmpty(in.getPersonId()))
				bemp = new BEmployee(BPerson.getCurrent());
			else
				bemp = new BEmployee(in.getPersonId());

			if ("New".equals(in.getTimesheetId())) {
				if (in.getTotalHours() > .01  ||  in.getTotalExpenses() > .01  ||  in.getTotalPay() > .01) {
					Timesheet ts = new Timesheet();
					ts.generateId();
					ts.setProjectShift(new BProjectShift(in.getShiftId()).getProjectShift());
					ts.setPersonId(in.getPersonId());
					ts.setPerson(new BPerson(in.getPersonId()).getPerson());
					ts.setWorkDate(in.getWorkDate());
					ts.setBillable('Y');
					ts.setEndDate(in.getWorkDate());
					ts.setBeginningEntryDate(new Date());
					ts.setBeginningEntryPerson(BPerson.getCurrent().getPerson());
					ts.setState('N');
					ts.setTotalHours(in.getTotalHours());
					ts.setTotalExpenses((float) in.getTotalExpenses());
					ts.setFixedPay(in.getTotalPay());
					ts.setBeginningTime(0);
					ts.setEndTime(0);
					ts.setWageType(new BWageType(bemp.getWageTypeId()).getBean());
					hsu.insert(ts);
				}
			} else {
				final String benefitId = bemp.saveTimeEntry(in.getBeginningTime(),
						"Y", in.getDescription(), in.getEndTime(),
						in.getProjectId(), in.getShiftId(), in.getTotalHours(), in.getWorkDate(), -1, in.getTimesheetId(), "", (float) in.getTotalExpenses(), in.getTotalPay(), 0);

				if (!isEmpty(benefitId)) {
					final BHRBenefit b = new BHRBenefit(benefitId);
					ret.setOverrunBenefitName(b.getName());
					ret.setOverrunBenefitHours(bemp.getTimeOffCurrentPeriod(benefitId));
				}
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetQuickListReturn getQuickList(/*@WebParam(name = "in")*/final GetQuickListInput in) {

		final GetQuickListReturn ret = new GetQuickListReturn();

		try {
			checkLogin(in);

			ret.setItem(new BPerson(in.getPersonId()).getQuickList(true));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in) {
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
	public GetTimesheetDetailReturn getTimesheetDetail(/*@WebParam(name = "in")*/final GetTimesheetDetailInput in) {
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
	public SelectProjectReturn selectProject(SelectProjectInput in) {
		SelectProjectReturn ret = new SelectProjectReturn();

		try {
			checkLogin(in);
			String extRef = in.getExtRef();
			if (extRef == null || extRef.isEmpty())
				ret.setFound(false);
			else {
				Project proj = hsu.createCriteria(Project.class)
						.eq(Project.REFERENCE, extRef)
						.first();
				if (proj == null)
					ret.setFound(false);
				else {
					BProject bp = new BProject(proj);
					ret.setFound(true);
					ret.setProjectId(proj.getProjectId());
					ret.setClientName(bp.getRequestingCompanyName());
					ret.setActiveDate(bp.getStartDate());
					ret.setTermDate(bp.getEndDate());
					ret.setCurrentStatus(bp.getCurrentStatus().getDescription());
				}
			}
			finishService(ret);
		} catch (Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SignInFormReturn signInForm(SignInFormInput in) {
		SignInFormReturn ret = new SignInFormReturn();

		try {
			checkLogin(in);
			//ret.setFilename((new SignInForm()).generate(in.getProjectId(), in.getShiftId(), in.getDate()));
			finishService(ret);
		} catch (Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}

