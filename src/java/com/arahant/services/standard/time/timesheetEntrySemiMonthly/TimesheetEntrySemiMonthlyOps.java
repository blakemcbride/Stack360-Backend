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
package com.arahant.services.standard.time.timesheetEntrySemiMonthly;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import static com.arahant.utils.IntegerDates.*;
import com.arahant.utils.IntegerDates.PeriodInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardTimeTimesheetEntrySemiMonthlyOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class TimesheetEntrySemiMonthlyOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimesheetEntrySemiMonthlyOps.class);

	public TimesheetEntrySemiMonthlyOps() {
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
	public GetNextRejectedDateReturn getNextRejectedDate(/*@WebParam(name = "in")*/final GetNextRejectedDateInput in) {
		final GetNextRejectedDateReturn ret = new GetNextRejectedDateReturn();

		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());
			PeriodInfo pi = new PeriodInfo(p);
			
			int rejectDate = p.getNextRejectedDate(in.getDate());
			ret.setDate(rejectDate);
			if (rejectDate == 0) {
				ret.setStartDate(beginningPeriodDate(pi, DateUtils.today()));
				ret.setEndDate(endingPeriodDate(pi, DateUtils.today()));
			}

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
			PeriodInfo pi = new PeriodInfo(p);
			
			int rejectDate = p.getPreviousRejectedDate(in.getDate());
			ret.setDate(rejectDate);
			if (rejectDate == 0) {
				ret.setStartDate(beginningPeriodDate(pi, DateUtils.today()));
				ret.setEndDate(endingPeriodDate(pi, DateUtils.today()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetCurrentPeriodReturn getCurrentPeriod(/*@WebParam(name = "in")*/final GetCurrentPeriodInput in) {
		final GetCurrentPeriodReturn ret = new GetCurrentPeriodReturn();

		try {
			checkLogin(in);
			
			String personId = in.getPersonId();
			if (personId.length() > 5) {
				final BPerson p = new BPerson(in.getPersonId());
				PeriodInfo pi = new PeriodInfo(p);

				ret.setStartDate(beginningPeriodDate(pi, DateUtils.today()));
				ret.setEndDate(endingPeriodDate(pi, DateUtils.today()));
				ret.setShowBillable(pi.showBillable());
			}
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
	public ListTimesheetsForPersonOnDateReturn listTimesheetsForPersonOnDate(/*@WebParam(name = "in")*/final ListTimesheetsForPersonOnDateInput in) {

		final ListTimesheetsForPersonOnDateReturn ret = new ListTimesheetsForPersonOnDateReturn();

		try {
			checkLogin(in);

			BEmployee e;
			if (!hsu.currentlySuperUser() && isEmpty(in.getPersonId()))
				e = new BEmployee(BPerson.getCurrent());
			else {
				if (in.getPersonId().equals(hsu.getSuperUserPersonId())) //super user has no timesheets
					return ret;
				e = new BEmployee(in.getPersonId());
			}
			
			PeriodInfo pi = new PeriodInfo(e);
			int date = (int) in.getTimesheetDate();
			if (date == 0)
				date = DateUtils.today();

			int beginningDate = beginningPeriodDate(pi, date);
			int endingDate = endingPeriodDate(pi, date);
			final BTimesheet[] ts = e.listTimesheetsForPeriod(beginningDate, endingDate);

			ret.setTimesheetTransmit(ts);

			double totalBillableHours = 0;
			double totalHours = 0;

			for (final BTimesheet element : ts) {
				if (element.getBillable() == 'Y')
					totalBillableHours += element.getTotalHours();

				totalHours += element.getTotalHours();
			}

			ret.setTotalBillableHours(totalBillableHours);
			ret.setTotalHours(totalHours);

			ret.setEmployeeFinalizedDate(e.getTimesheetFinalDate());

			int [] rejectDays = e.getRejectedDays();
			
			if (rejectDays.length > 0) {
				int [] rejectPeriods = rejectDays.clone();

				for (int i=0 ; i < rejectPeriods.length ; i++) {
					int dt = rejectPeriods[i];
					int y = DateUtils.getYear(dt);
					int m = DateUtils.getMonth(dt);
					int d = DateUtils.getDay(dt);
					int p = d <= 15 ? 1 : 2;
					rejectPeriods[i] = y * 1000 + m * 10 + p;
				}
				int uniquePeriods = 0;
				for (int i=0 ; i < rejectPeriods.length ; i++) {
					uniquePeriods++;
					while (i+1 < rejectPeriods.length  &&  rejectPeriods[i] == rejectPeriods[i+1])
						i++;
				}
				ret.setRemainingRejectedDays(uniquePeriods);
				ret.setMode(1);  // read only
				for (int i=0 ; i < rejectDays.length ; i++) 
					if (rejectDays[i] >= beginningDate  &&  rejectDays[i] <= endingDate) {
						ret.setMode(2);  // can write because in reject period
					}
			}
			ret.setStartDate(beginningDate);
			ret.setEndDate(endingDate);
			ret.setShowBillable(pi.showBillable());

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
			
			PeriodInfo pi = new PeriodInfo(p);
			
			// mark the whole period corrected
			int begDate = beginningPeriodDate(pi, in.getDate());
			int endDate = endingPeriodDate(pi, in.getDate());

			p.markRejectionCorrected(begDate, endDate);

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

			if (true) throw new ArahantException("XXYY");
			/*

			final String benefitId = bemp.saveTimeEntry(in.getBeginningTime(),
					in.getBillable(), in.getDescription(), in.getEndTime(),
					in.getProjectId(), in.getTotalHours(), in.getWorkDate(), in.getEndDate(), in.getTimesheetId());



			if (!isEmpty(benefitId)) {
				final BHRBenefit b = new BHRBenefit(benefitId);
				ret.setOverrunBenefitName(b.getName());
				ret.setOverrunBenefitHours(bemp.getTimeOffCurrentPeriod(benefitId));
			}

			finishService(ret);

			 */

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

			if (!hsu.currentlyArahantUser())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));

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
	public GetNextPeriodReturn getNextPeriod(/*@WebParam(name = "in")*/final GetNextPeriodInput in) {
		final GetNextPeriodReturn ret = new GetNextPeriodReturn();
		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());
			
			PeriodInfo pi = new PeriodInfo(p);
			int startDate;
			if (in.getForward())
				startDate = nextPeriodStartDate(pi, in.getStartDate());
			else
				startDate = prevPeriodStartDate(pi, in.getStartDate());
			ret.setStartDate(startDate);
			ret.setEndDate(endingPeriodDate(pi, startDate));

			finishService(ret);
		} catch (final Exception e) {
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
}
