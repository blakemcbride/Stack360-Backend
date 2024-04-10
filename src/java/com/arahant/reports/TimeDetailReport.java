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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.reports;

import com.arahant.beans.Employee;
import com.arahant.beans.Holiday;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Timesheet;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class TimeDetailReport extends ReportBase {

	public TimeDetailReport() throws ArahantException {
		super("TimeDtRpt","Time Detail");
	}

	public String build(OrgGroup og, int startDate, int endDate) {
		try {
			List<String> empIdsList = new ArrayList<String>();

			empIdsList.addAll(new BOrgGroup(og).getAllPersonIdsForOrgGroupHierarchy(true));

			String[] empIdsArray = new String[empIdsList.size()];
			int loop = 0;
			for (String s :empIdsList)
			{
				empIdsArray[loop++] = s;
			}

			return build(empIdsArray, startDate, endDate);
		}
		catch (Exception e) {
            e.printStackTrace();
        }

		return "";
	}

	public String build(String[] empIds, int startDate, int endDate) throws DocumentException, Exception {
		try {
			boolean includeMoney = false;
			List<Employee> eList = hsu.createCriteria(Employee.class)
													 .in(Employee.PERSONID, empIds)
													 .orderBy(Employee.LNAME)
													 .orderBy(Employee.FNAME)
													 .joinTo(Employee.TIMESHEETS)
													 .between(Timesheet.WORKDATE, startDate, endDate)
													 .list();
			boolean money = includeMoney;
			String pattern = "0.00";
			DecimalFormat df = new DecimalFormat(pattern);
			int[] timeColWidths = {11, 16, 9, 9, 9, 7, 8, 8, 10, 13};
			int[] infoColWidths = {17, 20, 15, 15, 20, 13};
			int[] summColWidths = {33, 32, 8, 17};
			int[] finalSummColWidths = {33, 32, 8, 8, 9};

			PdfPTable timeTable = makeTable(timeColWidths);
			PdfPTable infoTable = makeTable(infoColWidths);
			PdfPTable accountSummTable = makeTable(summColWidths);
			PdfPTable finalSummTable = makeTable(finalSummColWidths);
			HoursType allHours = new HoursType();
			setBaseFontSize(7);

			for(Employee e : eList) {
				List<Timesheet> ts = new ArrayList<Timesheet>();
				List<Timesheet> timesheetsForWeek = new ArrayList<Timesheet>();
				double totalHours = 0.0;
				double totalRegHours = 0.0;
				double totalOTHours = 0.0;
				double totalPersHours = 0.0;
				double totalSickHours = 0.0;
				double totalVacHours = 0.0;
				double totalHoliHours = 0.0;
				double totalBereavHours = 0.0;
				double totalJuryHours = 0.0;
				double totalCallHours = 0.0;
				double totalNoOTHours = 0.0;
				boolean newOrgGroup = true;
				int firstDay = 0;
				List<HoursType> hours = new ArrayList<HoursType>();
				HoursType currentHours = new HoursType();
				BEmployee be = new BEmployee(e);
				int weekStartDay = new BOrgGroup(be.getOrgGroupId()).getNewWeekBeginDay();
				weekStartDay = weekStartDay == 0 ? 1 : weekStartDay;
				List<Holiday> holidays = ArahantSession.getHSU().createCriteria(Holiday.class)
																.joinTo(Holiday.ORG_GROUP)
													            .eq(OrgGroup.ORGGROUPID, be.getOrgGroupId())
													            .list();
				double dailyWorkHours = (Utils.doubleEqual(be.getWorkHours(), 0.0, .00001) ? 8.0 : be.getWorkHours());
				double weeklyWorkHours = 0.0;

				Iterator<OrgGroupAssociation> ogaI = null;
				Set<OrgGroupAssociation> ogaSet = be.getOrgGroupAssociations();
				int recentDate = 0;
				if(ogaSet != null) {
					ogaI = ogaSet.iterator();
					if(ogaSet.size() > 1) {
						if(be.getDefaultProject() == null) {
							while(ogaI.hasNext()) {
								OrgGroupAssociation oga = ogaI.next();

								if(DateUtils.getDate(oga.getRecordChangeDate()) > recentDate) {
									recentDate = DateUtils.getDate(oga.getRecordChangeDate());
									currentHours = new HoursType(new BOrgGroup(oga.getOrgGroupId()).getOrgGroup());
								}
							}
						}
						else
							currentHours = new HoursType(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.DEFAULT_PROJECT, be.getDefaultProject()).first());
					}
					else
						currentHours = new HoursType(new BOrgGroup(ogaI.next().getOrgGroupId()).getOrgGroup());
				}
				else
					ogaI = null;
//				while(ogaI.hasNext()) {
//					OrgGroupAssociation oga = ogaI.next();
//
//					if(DateUtils.getDate(oga.getRecordChangeDate()) > recentDate) {
//						recentDate = DateUtils.getDate(oga.getRecordChangeDate());
//						currentHours = new HoursType(new BOrgGroup(oga.getOrgGroupId()).getOrgGroup());
//					}
//				}
				hours.add(currentHours);

				setAlternateRowColor(new BaseColor(235, 235, 235));
				//First header info row
				writeAlign(infoTable, "Employee:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getLastName() + ", " + be.getFirstName(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "ID:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getExtRef(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Time Zone:",  Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, "", Element.ALIGN_LEFT, true);
				//Second header info row
				writeAlign(infoTable, "Status:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getEmployeeStatusName(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Status Date:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, DateUtils.getDateFormatted(be.getEmployeeStatusDate()), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Pay Rule:",  Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, (be.getExempt() ? "Exempt" : "Non-exempt"), Element.ALIGN_LEFT, true);
				//Third header info row
				writeAlign(infoTable, "Primary Account:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getOrgGroupRef(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Start:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, DateUtils.getDateFormatted(be.getHireDate()), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "End:",  Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, (be.getTermDate() == 0 ? "" : be.getTermDate()).toString(), Element.ALIGN_LEFT, true);
				addTable(infoTable);
				infoTable.flushContent();

				//Write timesheet column headers
				setAlternateRowColor(new BaseColor(220, 220, 220));
				writeColHeader(timeTable, "Date", Element.ALIGN_CENTER, 1, true);
				writeColHeader(timeTable, "Xfr/Move Account", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Apply To", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "In Punch", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Out Punch", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Money Amount", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Adj/Ent Amount", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Totaled Amount", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Cum. Tot. Amount", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Comments", Element.ALIGN_CENTER, 1, true);
				
//				if(be.getEmployee().getTimesheets() != null)
//					ts.addAll(be.getEmployee().getTimesheets());
				ts.addAll(hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, be.getPerson())
															 .dateBetween(Timesheet.WORKDATE, startDate, endDate)
															 .orderBy(Timesheet.WORKDATE)
															 .orderBy(Timesheet.BEGINNINGTIME)
															 .list());
//				TimesheetComparator comparator = new TimesheetComparator();
//				Collections.sort(ts, comparator);

				if(!ts.isEmpty())
					firstDay = ts.get(0).getWorkDate();
				
				for(Timesheet t : ts) {
					if(t.getWorkDate() >= firstDay) {
						weeklyWorkHours = dailyWorkHours * (holidaysOff(t, holidays, weekStartDay));
						calculateOvertimeHours(timesheetsForWeek, weeklyWorkHours, currentHours);
						timesheetsForWeek.clear();
						firstDay = DateUtils.getDate(DateUtils.getDate(firstDay + ((weekStartDay + 6) - (DateUtils.getCalendar(firstDay).get(Calendar.DAY_OF_WEEK))) + 1));
						timesheetsForWeek.add(t);
					}
					else {
						timesheetsForWeek.add(t);
					}
					currentHours = selectOrgGroup(hours, t, newOrgGroup, currentHours);
					writeAlign(timeTable, DateUtils.getDateFormatted(t.getWorkDate()), Element.ALIGN_RIGHT, false);   //DATE

					try {
						OrgGroup org = ArahantSession.getHSU().createCriteria(OrgGroup.class)
								.eq(OrgGroup.DEFAULT_PROJECT, t.getProjectShift().getProject()).first();
						if(org == null)
							org = t.getProjectShift().getProject().getRequestingOrgGroup();
						if(!org.getExternalId().equals(be.getOrgGroupRef()))
							writeAlign(timeTable, org.getExternalId(), Element.ALIGN_RIGHT, false);   //HOME ORG GROUP
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}
					catch(Exception ex) {
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}

					if(timeRelated(t).hasNext()) {
						HrBenefitConfig config = timeRelated(t).next();
						String name = config.getHrBenefit().getName();
						if(name.toLowerCase().contains("vacation")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addVacHours(t.getTotalHours());
							totalVacHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("personal")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addPersHours(t.getTotalHours());
							totalPersHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("sick")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addSickHours(t.getTotalHours());
							totalSickHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("holiday")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addHoliHours(t.getTotalHours());
							totalHoliHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("bereav")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addBereavHours(t.getTotalHours());
							totalBereavHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("jury")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addJuryHours(t.getTotalHours());
							totalJuryHours += t.getTotalHours();
						}
						else if(name.toLowerCase().contains("call")) {
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);
							currentHours.addCallHours(t.getTotalHours());
							totalCallHours += t.getTotalHours();
						}
						else
							writeAlign(timeTable, name, Element.ALIGN_RIGHT, false);

						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						if(money)
							writeAlign(timeTable, df.format(t.getTotalHours() * be.getWageAmount()), Element.ALIGN_RIGHT, false);
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, t.getTotalHours(), Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}
					else {
						currentHours.addRegHours(t.getTotalHours());
						totalRegHours += t.getTotalHours();
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, DateUtils.getTimeFormatted(t.getBeginningTime()), Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, DateUtils.getTimeFormatted(t.getEndTime()), Element.ALIGN_RIGHT, false);
						if(money)
							writeAlign(timeTable, df.format(t.getTotalHours() * be.getWageAmount()), Element.ALIGN_RIGHT, false);
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, df.format(t.getTotalHours()), Element.ALIGN_RIGHT, false);
					}
					totalHours += t.getTotalHours();
					writeAlign(timeTable, df.format(totalHours), Element.ALIGN_RIGHT, false);
					
					if(t.getMessage() != null)
						writeAlign(timeTable, (t.getMessage().getMessage() == null ? "" : t.getMessage().getMessage()), Element.ALIGN_LEFT, false);
					else
						writeAlign(timeTable, "", Element.ALIGN_LEFT, false);
				}
				calculateOvertimeHours(timesheetsForWeek, weeklyWorkHours, currentHours);
				timesheetsForWeek.clear();
				for(HoursType h : hours) {
					totalOTHours += h.getOTHours();
					if(h.getOTHours() > 0.0)
						totalRegHours -= h.getOTHours();
				}

				writeColHeader(accountSummTable, "Labor Account Summary", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Money", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Hours", Element.ALIGN_LEFT, 1, true);
				for(HoursType h : hours) {
					h.addRegHours(-1 * (h.getOTHours() > 0 ? h.getOTHours() : 0));
					writeAlign(accountSummTable, h.getOg().getExternalId(), Element.ALIGN_LEFT, false, 4);
					if(h.getTotalHours() > 0.00001) {
						//Write Total Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Hours Totals", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getTotalHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getTotalHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getOTHours() > 0.00001) {
						//Write OT Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Overtime", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getOTHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getOTHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getPersHours() > 0.00001) {
						//Write Personal Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Personal", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getPersHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getPersHours() * 1.0), Element.ALIGN_LEFT, false);
					}
					if(h.getRegHours() > 0.00001) {
						//Write Regular Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Regular", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getRegHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getRegHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getSickHours() > 0.00001) {
						//Write Sick Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Sick", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getSickHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getSickHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getVacHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Vacation", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getVacHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getVacHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getHoliHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Holiday", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getHoliHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getHoliHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getBereavHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Bereavement", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getBereavHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getBereavHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getJuryHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "Jury Duty", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getJuryHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getJuryHours()), Element.ALIGN_LEFT, false);
					}
					if(h.getCallHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, "On Call", Element.ALIGN_LEFT, false);
						if(money)
							writeAlign(accountSummTable, "$" + df.format(h.getCallHours() * be.getWageAmount()), Element.ALIGN_LEFT, false);
						else
							writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
						writeAlign(accountSummTable, df.format(h.getCallHours()), Element.ALIGN_LEFT, false);
					}

					allHours.addAllHours(h);
				}

				writeAlign(accountSummTable, "Combined Pay Code Summary", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Pay Code", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Money", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Hours", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(accountSummTable, "Hours Totals", Element.ALIGN_LEFT, false);
				if(money)
					writeAlign(accountSummTable, df.format(totalHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
				else
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(accountSummTable, df.format(totalHours), Element.ALIGN_LEFT, false);
				writeColHeader(accountSummTable, "Totals:", Element.ALIGN_LEFT, 2, true);
				if(money)
					writeColHeader(accountSummTable, df.format(totalHours * be.getWageAmount()), Element.ALIGN_LEFT, 1, true);
				else
					writeColHeader(accountSummTable, "$" + df.format(0.0), Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, df.format(totalHours), Element.ALIGN_LEFT, 1, true);

				writeAlign(accountSummTable, "Pay Code Summary", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Pay Code", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Money", Element.ALIGN_LEFT, true);
				writeAlign(accountSummTable, "Hours", Element.ALIGN_LEFT, true);
				if(totalOTHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Overtime", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalOTHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalOTHours), Element.ALIGN_LEFT, false);
				}
				if(totalPersHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Personal", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalPersHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalPersHours), Element.ALIGN_LEFT, false);
				}
				if(totalRegHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Regular", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalRegHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalRegHours), Element.ALIGN_LEFT, false);
				}
				if(totalSickHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Sick", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalSickHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalSickHours), Element.ALIGN_LEFT, false);
				}
				if(totalVacHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Vacation", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalVacHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalVacHours), Element.ALIGN_LEFT, false);
				}
				if(totalHoliHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Holiday", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalHoliHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalHoliHours), Element.ALIGN_LEFT, false);
				}
				if(totalBereavHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Bereavement", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalBereavHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalBereavHours), Element.ALIGN_LEFT, false);
				}
				if(totalJuryHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "Jury Duty", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalJuryHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalJuryHours), Element.ALIGN_LEFT, false);
				}
				if(totalCallHours > 0.00001) {
					writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, "On Call", Element.ALIGN_LEFT, false);
					if(money)
						writeAlign(accountSummTable, df.format(totalCallHours * be.getWageAmount()), Element.ALIGN_LEFT, false);
					else
						writeAlign(accountSummTable, "", Element.ALIGN_LEFT, false);
					writeAlign(accountSummTable, df.format(totalCallHours), Element.ALIGN_LEFT, false);
				}

				writeColHeader(accountSummTable, "Totals:", Element.ALIGN_LEFT, 2, true);
				if(money)
					writeColHeader(accountSummTable, df.format(totalHours * be.getWageAmount()), Element.ALIGN_LEFT, 1, true);
				else
					writeColHeader(accountSummTable, "$" + df.format(0.0), Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, df.format(totalHours), Element.ALIGN_LEFT, 1, true);

				addTable(timeTable);
				addTable(accountSummTable);
				infoTable.flushContent();
				timeTable.flushContent();
				accountSummTable.flushContent();
				newPage();
			}

			writeAlign(finalSummTable, "Final Summary", Element.ALIGN_LEFT, true);
			writeAlign(finalSummTable, "Pay Code", Element.ALIGN_LEFT, true);
			writeAlign(finalSummTable, "Money", Element.ALIGN_LEFT, true);
			writeAlign(finalSummTable, "Hours", Element.ALIGN_LEFT, true);
			writeAlign(finalSummTable, "Wages", Element.ALIGN_LEFT, true);

			if(allHours.getOTHours() > 0.00001) {
				//Write OT Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Overtime", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getOTHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getPersHours() > 0.00001) {
				//Write Personal Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Personal", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getPersHours() * 1.0), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getRegHours() > 0.00001) {
				//Write Regular Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Regular", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getRegHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getSickHours() > 0.00001) {
				//Write Sick Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Sick", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getSickHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getVacHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Vacation", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getVacHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getHoliHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Holiday", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getHoliHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getBereavHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Bereavement", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getBereavHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getJuryHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "Jury Duty", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getJuryHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}
			if(allHours.getCallHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "On Call", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, df.format(allHours.getCallHours()), Element.ALIGN_LEFT, false);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, false);
			}

			writeColHeader(finalSummTable, "", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "Grand Total:", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "$" + df.format(0.0), Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, df.format(allHours.getTotalHours()), Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "$" + df.format(0.0), Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "Total Number of Employees: " + eList.size(), Element.ALIGN_LEFT, 5, true);

			addTable(finalSummTable);
		} finally {
			close();
		}

		return getFilename();
	}

//	private class TimesheetComparator implements Comparator {
//		@Override
//		public int compare(Object ts1, Object ts2) {
//			if(((Timesheet)ts1).getWorkDate() < ((Timesheet)ts2).getWorkDate())
//				return -1;
//			else if(((Timesheet)ts1).getWorkDate() > ((Timesheet)ts2).getWorkDate())
//				return 1;
//			else if(((Timesheet)ts1).getBeginningTime() < ((Timesheet)ts2).getBeginningTime())
//				return -1;
//			else
//				return 1;
//		}
//	}

	private Iterator<HrBenefitConfig> timeRelated(Timesheet ts) {
		Iterator<HrBenefitConfig> configs;
		Set<HrBenefitConfig> newConfigs = new HashSet<HrBenefitConfig>();
		Set<HrBenefitConfig> tempConfigs = ts.getProjectShift().getProject().getBenefitConfigs();
		if(tempConfigs != null) {
			configs = tempConfigs.iterator();
		}
		else
			configs = null;

		while(configs.hasNext()) {
			HrBenefitConfig hrbene = configs.next();
			if(hrbene.getHrBenefit().getTimeRelated() == 'Y')
				newConfigs.add(hrbene);
		}

		return newConfigs.iterator();
	}

	private HoursType selectOrgGroup(List<HoursType> hours, Timesheet t, boolean newOrgGroup, HoursType currentHours) {
		OrgGroup tsOrgGroup = ArahantSession.getHSU().createCriteria(OrgGroup.class)
				.eq(OrgGroup.DEFAULT_PROJECT, t.getProjectShift().getProject()).first();

		if(tsOrgGroup == null)
			tsOrgGroup = t.getProjectShift().getProject().getRequestingOrgGroup();
		for (HoursType hour : hours) {
			if (hour.getOg() == tsOrgGroup) {
				newOrgGroup = false;
				currentHours = hour;
				break;
			}
			newOrgGroup = true;
		}
		if (newOrgGroup && !timeRelated(t).hasNext()) {
			currentHours = new HoursType(tsOrgGroup);
			hours.add(currentHours);
		}
		return currentHours;
	}

	private double holidaysOff(Timesheet t, List<Holiday> holidays, int weekStartDay) {
		double workDays = 7.0;
		
		for(int i = 0; i < 7; i++) {
			if((weekStartDay + i) == Calendar.SATURDAY ||
			   (weekStartDay + i) == Calendar.SUNDAY) {
				workDays -= 1.0;
				continue;
			}
			for(Holiday h : holidays) {
				if(t.getWorkDate() + i == h.getHdate())
					workDays -= (h.getPartOfDay() == 'F' ? 1.0 : 0.5);
			}
		}
		
		return workDays;
	}

	private void calculateOvertimeHours(List<Timesheet> timesheetsForWeek, double expectedWeeklyHours, HoursType currentHours) {
		double totalWeeklyHours = 0.0;
		double totalBenefitHours = 0.0;

		for(Timesheet t : timesheetsForWeek) {
			if(timeRelated(t).hasNext())
				totalBenefitHours += t.getTotalHours();
			else
				totalWeeklyHours += t.getTotalHours();
		}
		currentHours.addOTHours(totalWeeklyHours - totalBenefitHours - expectedWeeklyHours > 0.00001 ? totalWeeklyHours - totalBenefitHours - expectedWeeklyHours : 0);
	}

	private class HoursType {
        private double regHours;
        private double otHours;
        private double vacHours;
        private double sickHours;
        private double persHours;
		private double holiHours;
		private double salaryHours;
		private double bereavHours;
		private double juryHours;
		private double callHours;
		private double noOtHours;
        private OrgGroup og;
		private int date;

        public HoursType() {
            this.regHours = 0.0;
            this.otHours = 0.0;
            this.vacHours = 0.0;
            this.sickHours = 0.0;
            this.persHours = 0.0;
			this.holiHours = 0.0;
			this.salaryHours = 0.0;
			this.bereavHours = 0.0;
			this.juryHours = 0.0;
			this.callHours = 0.0;
			this.noOtHours = 0.0;
            this.og = null;
        }

        public HoursType(final OrgGroup og) {
            this.regHours = 0.0;
            this.otHours = 0.0;
            this.vacHours = 0.0;
            this.sickHours = 0.0;
            this.persHours = 0.0;
			this.holiHours = 0.0;
			this.salaryHours = 0.0;
			this.bereavHours = 0.0;
			this.juryHours = 0.0;
			this.callHours = 0.0;
			this.noOtHours = 0.0;
            this.og = og;
        }

		public double getBereavHours() {
			return bereavHours;
		}

		public void setBereavHours(double bereavHours) {
			this.bereavHours = bereavHours;
		}

		public void addBereavHours(double bereavHours) {
			this.bereavHours += bereavHours;
		}

		public double getCallHours() {
			return callHours;
		}

		public void setCallHours(double callHours) {
			this.callHours = callHours;
		}

		public void addCallHours(double callHours) {
			this.callHours += callHours;
		}

		public double getJuryHours() {
			return juryHours;
		}

		public void setJuryHours(double juryHours) {
			this.juryHours = juryHours;
		}

		public void addJuryHours(double juryHours) {
			this.juryHours += juryHours;
		}

		public double getNoOtHours() {
			return noOtHours;
		}

		public void setNoOtHours(double noOtHours) {
			this.noOtHours = noOtHours;
		}

		public void addNoOtHours(double noOtHours) {
			this.noOtHours += noOtHours;
		}

		public double getSalaryHours() {
			return salaryHours;
		}

		public void setSalaryHours(double salaryHours) {
			this.salaryHours = salaryHours;
		}

		public void addSalaryHours(double salaryHours) {
			this.salaryHours += salaryHours;
		}

        public double getRegHours() {
            return regHours;
        }

        public void setRegHours(double hours) {
            this.regHours = hours;
        }

        public void addRegHours(double hours) {
            this.regHours += hours;
        }

        public double getOTHours() {
            return otHours;
        }

        public void setOTHours(double hours) {
            this.otHours = hours;
        }

        public void addOTHours(double hours) {
            this.otHours += hours;
        }

        public double getVacHours() {
            return vacHours;
        }

        public void setVacHours(double hours) {
            this.vacHours = hours;
        }

        public void addVacHours(double hours) {
            this.vacHours += hours;
        }

        public double getSickHours() {
            return sickHours;
        }

        public void setSickHours(double hours) {
            this.sickHours = hours;
        }

        public void addSickHours(double hours) {
            this.sickHours += hours;
        }

        public double getPersHours() {
            return persHours;
        }

        public void setPersHours(double hours) {
            this.persHours = hours;
        }

        public void addPersHours(double hours) {
            this.persHours += hours;
        }

        public double getHoliHours() {
            return holiHours;
        }

		public void setHoliHours(double hours) {
            this.holiHours = hours;
        }

        public void addHoliHours(double hours) {
            this.holiHours += hours;
        }

        public OrgGroup getOg() {
            return og;
        }

        public void setOg(OrgGroup og) {
            this.og = og;
        }

		public boolean hasTime() {
			return !(Utils.doubleEqual(this.regHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.otHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.vacHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.sickHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.persHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.holiHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.salaryHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.bereavHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.juryHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.callHours, 0.0, .00001) &&
					 Utils.doubleEqual(this.noOtHours, 0.0, .00001));
		}

		public int getDate() {
			return date;
		}

		public void setDate(int date) {
			this.date = date;
		}

		public void addAllHours(HoursType ht) {
			this.regHours += ht.getRegHours();
			this.otHours += ht.getOTHours();
			this.persHours += ht.getPersHours();
			this.sickHours += ht.getSickHours();
			this.vacHours += ht.getVacHours();
			this.holiHours += ht.getHoliHours();
			this.salaryHours += ht.getSalaryHours();
			this.bereavHours += ht.getBereavHours();
			this.juryHours += ht.getJuryHours();
			this.callHours += ht.getCallHours();
			this.noOtHours += ht.getNoOtHours();
		}

		private double getTotalHours() {
			return this.regHours +
				   this.otHours +
				   this.persHours +
				   this.sickHours +
				   this.vacHours +
				   this.holiHours +
				   this.salaryHours +
				   this.bereavHours +
				   this.juryHours +
				   this.callHours +
				   this.noOtHours;
		}
    }
	
	public static void main(String args[]) {
		String[] empIds = {
						   "00001-0000000006"
						   ,
						   "00001-0000000079"
						   ,
						   "00001-0000000083"
						   ,
						   "00001-0000000290"
						   ,
						   "00001-0000000143"
						   ,
						   "00001-0000000086"
						   ,
						   "00001-0000000149"
						   ,
						   "00001-0000000012"
						  };
		int startDate = 20110320;
		int endDate = 20110402;

		TimeDetailReport tdr = new TimeDetailReport();
		try {
//			tdr.build(empIds, startDate, endDate);
			tdr.build(new BOrgGroup("00001-0000000176").getOrgGroup(), startDate, endDate);
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}
