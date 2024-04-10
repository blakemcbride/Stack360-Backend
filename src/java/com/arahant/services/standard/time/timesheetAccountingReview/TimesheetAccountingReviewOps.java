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


package com.arahant.services.standard.time.timesheetAccountingReview;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.beans.ProjectShift;
import com.arahant.beans.Timesheet;
import com.arahant.reports.ReportBase;
import com.arahant.utils.*;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.services.ServiceBase;
import com.arahant.business.*;
import com.arahant.reports.TimesheetAccountingReviewReport;
import org.kissweb.DelimitedFileWriter;

import java.io.File;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeTimesheetAccountingReviewOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimesheetAccountingReviewOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimesheetAccountingReviewOps.class);

	public TimesheetAccountingReviewOps() {
	}

	@WebMethod()
	public SearchTimesheetsReturn searchTimesheets(/*@WebParam(name = "in")*/final SearchTimesheetsInput in) {
		final SearchTimesheetsReturn ret = new SearchTimesheetsReturn();
		try {
			checkLogin(in);

			ret.setItem(BTimesheet.searchForAccountingReview(
					in.getBillableItemsIndicator(), in.getCompanyId(), in.getPersonId(),
					in.getProjectId(), in.getTimesheetEndDate(), in.getTimesheetStartDate(),
					in.getTimesheetState(), in.getInvoiceId(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MarkTimesheetsDeferredReturn markTimesheetsDeferred(/*@WebParam(name = "in")*/final MarkTimesheetsDeferredInput in) {
		final MarkTimesheetsDeferredReturn ret = new MarkTimesheetsDeferredReturn();
		try {
			checkLogin(in);

			BTimesheet.markDeferred(in.getTimesheetIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MarkTimesheetsRejectedReturn markTimesheetsRejected(/*@WebParam(name = "in")*/final MarkTimesheetsRejectedInput in) {
		final MarkTimesheetsRejectedReturn ret = new MarkTimesheetsRejectedReturn();
		try {
			checkLogin(in);

			BTimesheet.rejectTimesheets(in.getTimesheetIds(), in.getMessage());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MarkTimesheetsApprovedReturn markTimesheetsApproved(/*@WebParam(name = "in")*/final MarkTimesheetsApprovedInput in) {
		final MarkTimesheetsApprovedReturn ret = new MarkTimesheetsApprovedReturn();
		try {
			checkLogin(in);

			BTimesheet.approve(in.getTimesheetIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in) {
		final SearchCompaniesReturn ret = new SearchCompaniesReturn();
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
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();
		try {
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(), in.getLastName(), "", ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
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
					0, 0, null, false, in.getUser(), null, null, ret.getCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new TimesheetAccountingReviewReport().build(in.getBillableItemsIndicator(), in.getCompanyId(), in.getPersonId(),
					in.getProjectId(), in.getTimesheetEndDate(), in.getTimesheetStartDate(),
					in.getTimesheetState(), in.getInvoiceId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {
		final GetExportReturn ret = new GetExportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(createExport(in.getBillableItemsIndicator(), in.getCompanyId(), in.getPersonId(),
					in.getProjectId(), in.getTimesheetEndDate(), in.getTimesheetStartDate(),
					in.getTimesheetState(), in.getInvoiceId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private static class ExportRow {
	    String name;
	    String workerID;
	    String customer;
	    String project;
		int firstDateOnProject;
	    double [] hours;
	    double totalHours;

	    ExportRow(String name, String workerID, String customer, String project, String shift, int ndays) {
	        this.name = name;
	        this.workerID = workerID;
	        this.customer = customer;
	        this.project = project + " (" + shift + ")";
	        hours = new double[ndays];
        }
    }

	private String createExport(int billableItemsIndicator, String companyId, String personId, String projectId, int timesheetEndDate, int timesheetStartDate, String timesheetState, String invoiceId) throws Exception {
        final File f = ReportBase.createTempFile("workExport", ".csv");
        DelimitedFileWriter dfw = new DelimitedFileWriter(f.getAbsolutePath(), ',');
        if (timesheetEndDate == 0)
            timesheetEndDate = DateUtils.today();
        if (timesheetStartDate == 0)
            timesheetStartDate = DateUtils.add(timesheetEndDate, -7);
        try {
            if (invoiceId.equals("%"))
                invoiceId = "";

            writeHeader(dfw, timesheetStartDate, timesheetEndDate);

            HibernateScrollUtil<Timesheet> tss = BTimesheet.searchForAccountingReviewExport(
                    billableItemsIndicator, companyId, personId,
                    projectId, timesheetEndDate, timesheetStartDate,
                    timesheetState, invoiceId, 0);

            boolean done = false;
            final int ndays = 1 + (int) DateUtils.getDaysBetween(timesheetEndDate, timesheetStartDate);
            if (tss.next()) {
				Timesheet ts = tss.get();
                do {
                    BTimesheet bts = new BTimesheet(ts);
                    BProject bp = bts.getProject();
                    BEmployee be = new BEmployee(bts.getPersonId());
                    String extRef = be.getExtRef();
					ProjectShift ps = bts.getProjectShift();

                    ExportRow billableDays = new ExportRow(bts.getEmployeeFirstName() + " " + bts.getEmployeeLastName(),
                            extRef == null ? "" : extRef,
                            bp.getCompanyName(),
                            bp.getSummary(),
							ps.getShiftStart(),
                            ndays);
                    ExportRow nonbillableDays = new ExportRow(bts.getEmployeeFirstName() + " " + bts.getEmployeeLastName(),
							extRef == null ? "" : extRef,
                            bp.getCompanyName(),
                            bp.getSummary(),
							ps.getShiftStart(),
                            ndays);

                    int workDate = bts.getWorkDate();
                    double totalBillableHours = 0;
                    double totalNonBillableHours = 0;
                    double billableHours = 0;
                    double nonbillableHours = 0;
                    int idx = -1;
                    // this loop exits if the person or project changes
                    for (int dt = timesheetStartDate; dt <= timesheetEndDate; ) {

                    	int previdx = idx;
                        idx = (int) DateUtils.getDaysBetween(dt, timesheetStartDate);
                        if (idx != previdx) {
                        	billableHours = 0;
                        	nonbillableHours = 0;
						}

                        if (workDate > dt) {
                            billableDays.hours[idx] = billableHours;
                            nonbillableDays.hours[idx] = nonbillableHours;
                            if (bts.getBillable() == 'Y') {
								billableHours = bts.getTotalHours();
								nonbillableHours = 0;
							} else {
								nonbillableHours = bts.getTotalHours();
								billableHours = 0;
							}
                            dt = workDate;
                            idx = (int) DateUtils.getDaysBetween(dt, timesheetStartDate);
                        } else if (workDate == dt) {
                            if (bts.getBillable() == 'Y')
                                billableHours += bts.getTotalHours();
                            else
                                nonbillableHours += bts.getTotalHours();
                        }
                        if (bts.getBillable() == 'Y')
                            totalBillableHours += bts.getTotalHours();
                        else
                            totalNonBillableHours += bts.getTotalHours();

						{
							Timesheet ts2 = hsu.createCriteria(Timesheet.class)
									.eq(Timesheet.PERSON, ts.getPerson())
									.eq(Timesheet.BILLABLE, 'Y')
									.gt(Timesheet.TOTALHOURS, 0.1)
									.orderBy(Timesheet.WORKDATE)
									.joinTo(Timesheet.PROJECTSHIFT)
									.eq(ProjectShift.PROJECT, ts.getProjectShift().getProject())
									.first();
							if (ts2 != null)
								billableDays.firstDateOnProject = ts2.getWorkDate();
						}

                        if (tss.next()) {
                            ts = tss.get();
                            if (!ts.getPersonId().equals(bts.getPersonId()) || !ts.getProjectShift().getProjectId().equals(bp.getProjectId())) {
                                billableDays.hours[idx] = billableHours;
                                nonbillableDays.hours[idx] = nonbillableHours;
                                break;
                            } else {
								bts = new BTimesheet(ts);
                                if (bts.getWorkDate() != workDate)
                                    workDate = bts.getWorkDate();
                                continue;
                            }
                        } else {
                            billableDays.hours[idx] = billableHours;
                            nonbillableDays.hours[idx] = nonbillableHours;
                            done = true;
                            break;
                        }
                    }
                    billableDays.totalHours = totalBillableHours;
                    nonbillableDays.totalHours = totalNonBillableHours;

                    if (billableDays.totalHours > .009) {
                        dfw.writeField(billableDays.name);
                        dfw.writeField(billableDays.workerID);
                        dfw.writeField(billableDays.customer);
                        dfw.writeField(billableDays.project);
                        dfw.writeField("Billable");
						dfw.writeDate(bp.getEstimatedFirstDate());
						dfw.writeDate(bp.getEstimatedLastDate());
						dfw.writeDate(billableDays.firstDateOnProject);
                        for (int i=0 ; i < billableDays.hours.length ; i++)
                            dfw.writeField(billableDays.hours[i]);
                        dfw.writeField(billableDays.totalHours);
                        dfw.endRecord();
                    }
                    if (nonbillableDays.totalHours > .009) {
                        dfw.writeField(nonbillableDays.name);
                        dfw.writeField(nonbillableDays.workerID);
                        dfw.writeField(nonbillableDays.customer);
                        dfw.writeField(nonbillableDays.project);
                        dfw.writeField("Non-billable");
						dfw.writeDate(bp.getEstimatedFirstDate());
						dfw.writeDate(bp.getEstimatedLastDate());
						dfw.writeDate(nonbillableDays.firstDateOnProject);
						for (int i=0 ; i < nonbillableDays.hours.length ; i++)
                            dfw.writeField(nonbillableDays.hours[i]);
                        dfw.writeField(nonbillableDays.totalHours);
                        dfw.endRecord();
                    }
                } while (!done);
            }
        } finally {
            dfw.close();
        }
        return FileSystemUtils.getHTTPPath(f);
    }

    private double fill(DelimitedFileWriter dfw, double hours, int first, int last) throws Exception {
        for (int day = first; day <= last; day = DateUtils.addDays(day, 1)) {
            dfw.writeField(hours);
            hours = 0;
        }
        return 0;
    }

	private void writeHeader(DelimitedFileWriter dfw, int startDate, int endDate) throws Exception {
	    dfw.writeField("NAME");
        dfw.writeField("WORKER ID");
        dfw.writeField("CUSTOMER");
        dfw.writeField("PROJECT");
        dfw.writeField("TYPE");
        dfw.writeField("PROJECT START DATE");
		dfw.writeField("PROJECT END DATE");
		dfw.writeField("FIRST DATE ON PROJECT");
        for ( ; startDate <= endDate ; startDate = DateUtils.addDays(startDate, 1))
            dfw.writeField(DateUtils.getDateFormatted(startDate));
        dfw.writeField("Total");
        dfw.endRecord();
    }
}
