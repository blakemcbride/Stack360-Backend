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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import static com.arahant.utils.IntegerDates.*;
import com.arahant.utils.IntegerDates.PeriodInfo;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetReview2Ops")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetReview2Ops extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(TimesheetReview2Ops.class);

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();

		try {
			checkLogin(in);
			boolean includeUser = false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES) == ACCESS_LEVEL_WRITE)
				includeUser = true;

			if (hsu.currentlySuperUser())
				includeUser = false;

			BEmployee[] emps = BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(), in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1, includeUser);

			if (in.getHasTimeReadyForApproval()) {
				final List<BEmployee> be = new LinkedList<BEmployee>();
				for (BEmployee element : emps)
					if (element.getHasTimeReadyForApproval())
						be.add(element);

				emps = be.toArray(new BEmployee[be.size()]);
			}

			ret.setEmployees(emps);
			if (in.getIncludeSelected() && !hsu.currentlySuperUser())
				ret.setSelectedItem(new Employees(new BEmployee(hsu.getCurrentPerson().getPersonId())));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ApproveTimesReturn approveTimes(/*@WebParam(name = "in")*/final ApproveTimesInput in) {
		final ApproveTimesReturn ret = new ApproveTimesReturn();

		try {
			checkLogin(in);

			BTimesheet.approve(in.getTimesheetIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public FinalizeTimeReturn finalizeTime(/*@WebParam(name = "in")*/final FinalizeTimeInput in) {

		final FinalizeTimeReturn ret = new FinalizeTimeReturn();

		try {
			checkLogin(in);

			BEmployee emp;

			if (hsu.currentlySuperUser())
				emp = new BEmployee(in.getPersonId());
			else {
				final BEmployee current = new BEmployee(BPerson.getCurrent());

				if (in.getPersonId().equals(""))
					emp = current;
				else {
					emp = new BEmployee(in.getPersonId());

					if (!emp.getPersonId().equals(current.getPersonId()) && BRight.checkRight(FINALIZE_OTHERS_TIME) != ACCESS_LEVEL_WRITE)
						throw new ArahantException("Can't finalize due to permissions");

				}
			}
			emp.finalizeTime(in.getDate());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public ListTimesheetsForReviewReturn listTimesheetsForReview(/*@WebParam(name = "in")*/final ListTimesheetsForReviewInput in) {
		final ListTimesheetsForReviewReturn ret = new ListTimesheetsForReviewReturn();

		try {
			checkLogin(in);

			final BEmployee emp = new BEmployee(in.getEmployeeId());
			int today = DateUtils.today();
			PeriodInfo pi = new PeriodInfo(emp);
			
			int begDate = in.getBegDateRange();
			int endDate = in.getEndDateRange();
			if (begDate == 0  &&  endDate == 0) {
					begDate = beginningPeriodDate(pi, today);
					endDate = endingPeriodDate(pi, today);
			}

			final BTimesheet[] t = emp.listTimesheetsForReview(in.isBillableFlag(), in.isFinalizedFlag(), in.isNonBillableFlag(), in.isNonFinalizedFlag(), null,
					in.isApprovedFlag(), in.isNonApprovedFlag(), begDate, endDate);

			ret.setTimesheetTransmit(t);

			ret.setEmployeeFinalizedDate(emp.getTimesheetFinalDate());
			
			ret.setBegDateRange(beginningPeriodDate(pi, today));
			ret.setEndDateRange(endingPeriodDate(pi, today));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

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
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, in.getPersonId(), in.isQuickList(), in.getUser(), null, null, ret.getCap()));

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
	public SaveTimeEntryReturn saveTimeEntry(/*@WebParam(name = "in")*/final SaveTimeEntryInput in) {

		final SaveTimeEntryReturn ret = new SaveTimeEntryReturn();

		try {
			checkLogin(in);

			final BTimesheet bt = new BTimesheet(in.getTimesheetId());
			in.setData(bt);
			bt.update();


			if (!isEmpty(bt.getBenefitId())) {
				final BHRBenefit b = new BHRBenefit(bt.getBenefitId());
				ret.setOverrunBenefitName(b.getName());
				ret.setOverrunBenefitHours(new BEmployee(bt.getPersonId()).getTimeOffCurrentPeriod(bt.getBenefitId()));
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public RejectDayReturn rejectDay(/*@WebParam(name = "in")*/final RejectDayInput in) {

		final RejectDayReturn ret = new RejectDayReturn();

		try {
			checkLogin(in);

			new BPerson(in.getPersonId()).rejectTimeDay(BPerson.getCurrent(), in.getDate(), in.getMessage());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetRejectedDaysReturn getRejectedDays(/*@WebParam(name = "in")*/final GetRejectedDaysInput in) {

		final GetRejectedDaysReturn ret = new GetRejectedDaysReturn();
		try {
			checkLogin(in);

			ret.setItem(new BPerson(in.getPersonId()).getRejectedDays());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public RejectTimeReturn rejectTime(/*@WebParam(name = "in")*/final RejectTimeInput in) {


		final RejectTimeReturn ret = new RejectTimeReturn();

		try {
			checkLogin(in);

			BTimesheet.rejectTimesheets(in.getTimesheetId(), in.getMessage());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public ListEmployeeTimeOffReturn listEmployeeTimeOff(/*@WebParam(name = "in")*/final ListEmployeeTimeOffInput in) {
		final ListEmployeeTimeOffReturn ret = new ListEmployeeTimeOffReturn();
		try {
			checkLogin(in);

			ret.setData(new BEmployee(in.getPersonId()));

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
	public DeleteTimesheetReturn deleteTimesheet(/*@WebParam(name = "in")*/final DeleteTimesheetInput in) {
		final DeleteTimesheetReturn ret = new DeleteTimesheetReturn();
		try {
			checkLogin(in);

			BTimesheet.delete(hsu, in.getTimesheetIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
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
					in.getBillable(), in.getDescription(), in.getEndTime(), in.getPersonId(),
					in.getProjectId(), null, in.getTotalHours(), in.getWorkDate(), in.getEndDate());

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

	@WebMethod()
	public GetRestrictHoursOverrideReturn getRestrictHoursOverride(/*@WebParam(name = "in")*/final GetRestrictHoursOverrideInput in) {
		final GetRestrictHoursOverrideReturn ret = new GetRestrictHoursOverrideReturn();

		ret.setRestrictHoursOverride(BProperty.getBoolean("FivePointsOperation", false));
		
		return ret;
	}

	@WebMethod()
	public GetTimeAdminReturn getTimeAdmin(/*@WebParam(name = "in")*/final GetTimeAdminInput in) {
		final GetTimeAdminReturn ret = new GetTimeAdminReturn();

		try {
			checkLogin(in);
			if (BRight.checkRight(ACCESS_ACCOUNTING) == ACCESS_LEVEL_WRITE)
				ret.setTimeAdmin(true);
			else
				ret.setTimeAdmin(false);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}
