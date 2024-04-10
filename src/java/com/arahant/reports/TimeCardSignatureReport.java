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
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.beans.Timesheet;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
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
public class TimeCardSignatureReport extends ReportBase {

	public TimeCardSignatureReport() throws ArahantException {
		super("TimeCrdRpt","Time Card For Signature", true);
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
			int[] timeColWidths = {11, 16, 20, 20, 15, 8, 8, 10, 20};
			int[] infoColWidths = {17, 20, 15, 15, 20, 13};
			int[] summColWidths = {33, 8, 32, 17};
			int[] finalSummColWidths = {33, 8, 32, 17};
			int[] signatureColWidths = {20, 52, 20, 52};
			String reportRun = "";
					
			reportRun = DateUtils.getDateTimeFormatted(DateUtils.now(), DateUtils.nowTime());

			PdfPTable timeTable = makeTable(timeColWidths);
			PdfPTable infoTable = makeTable(infoColWidths);
			PdfPTable weeklySummTable = makeTable(summColWidths);
			PdfPTable periodSummTable = makeTable(summColWidths);
			PdfPTable finalSummTable = makeTable(finalSummColWidths);
			PdfPTable signatureTable = makeTable(signatureColWidths);
			HoursType allHours = new HoursType();
			setBaseFontSize(7);
			
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "Employee Signoff:", Element.ALIGN_RIGHT, true);
			writeAlign(signatureTable, "________________________________________", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "Supervisor Signoff:", Element.ALIGN_RIGHT, true);
			writeAlign(signatureTable, "________________________________________", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "Date:", Element.ALIGN_RIGHT, true);
			writeAlign(signatureTable, "____________________", Element.ALIGN_LEFT, true);
			writeAlign(signatureTable, "Date:", Element.ALIGN_RIGHT, true);
			writeAlign(signatureTable, "____________________", Element.ALIGN_LEFT, true);

			// pull the top level org groups for this company, ordered alphabetically
			HibernateCriteriaUtil<OrgGroupHierarchy> hcp = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, hsu.getCurrentCompany().getOrgGroupId());
			hcp.joinTo(OrgGroupHierarchy.ORGGROUPBYCHILDGROUPID).orderBy(OrgGroup.NAME);

			String [] valueOrder = new String[hcp.set().size()];
			String [] valueOutput = new String[hcp.set().size()];

			HibernateScrollUtil<OrgGroupHierarchy> scp = hcp.scroll();
			int valueOrderCount = 0;

			while (scp.next()) {
				OrgGroupHierarchy ogh = scp.get();
				valueOutput[valueOrderCount] =  ogh.getOrgGroupByChildGroupId().getName();
				valueOrder[valueOrderCount] = ogh.getChildGroupId();  // hang onto this to serve as the parent id to seek when I work through the values
				valueOrderCount++;
			}

			scp.close();

		
			newPage();
			
			for(Employee e : eList) {
				List<Timesheet> ts = new ArrayList<Timesheet>();
				List<Timesheet> timesheetsForWeek = new ArrayList<Timesheet>();
				double weekTotalHours = 0.0;
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
				boolean needWeekTotals = false;
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
/*
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
*/ 
			int valueTarget = 0;
			String [] orgGroupSet = new String[valueOutput.length];
			while (valueTarget < valueOrderCount) {
				boolean didWrite = false;
				Iterator<OrgGroupAssociation> ogai = be.getOrgGroupAssociations().iterator();
				while (ogai.hasNext()) {
					OrgGroup og = ogai.next().getOrgGroup();
					Iterator<OrgGroupHierarchy> oghi = og.getOrgGroupHierarchiesForChildGroupId().iterator();
					while (oghi.hasNext()) {
						OrgGroup ogi = oghi.next().getOrgGroupByParentGroupId();
						if (ogi.getOrgGroupId().equalsIgnoreCase(valueOrder[valueTarget])) {
							orgGroupSet[valueTarget] = og.getName();
							didWrite = true;
						}
					}
				}
			valueTarget++;
			}
				hours.add(currentHours); //  currentHours is all zeros at this point

				setAlternateRowColor(new BaseColor(235, 235, 235));
				//First header info row
				writeAlign(infoTable, "Employee:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getLastName() + ", " + be.getFirstName(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Run Date/Time:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, reportRun, Element.ALIGN_LEFT, true);
				writeAlign(infoTable, valueOutput[1] + ":" , Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, (!isEmpty(orgGroupSet[1]) ? orgGroupSet[1] : ""), Element.ALIGN_LEFT, true);
				//Second header info row
				writeAlign(infoTable, "ID:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getExtRef(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Period Start Date:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, DateUtils.getDateFormatted(startDate), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, valueOutput[2] + ":", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, (!isEmpty(orgGroupSet[2]) ? orgGroupSet[2] : ""), Element.ALIGN_LEFT, true);
				//Third header info row
				writeAlign(infoTable, "Status:", Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, be.getEmployeeStatusName(), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, "Period End Date:",  Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, DateUtils.getDateFormatted(endDate), Element.ALIGN_LEFT, true);
				writeAlign(infoTable, valueOutput[3] + ":",  Element.ALIGN_RIGHT, true);
				writeAlign(infoTable, (!isEmpty(orgGroupSet[3]) ? orgGroupSet[3] : ""), Element.ALIGN_LEFT, true);
				addTable(infoTable);
				infoTable.flushContent();

				//Write timesheet column headers
				setAlternateRowColor(new BaseColor(220, 220, 220));
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				seperatorLine(timeTable);
				writeColHeader(timeTable, "Date", Element.ALIGN_CENTER, 1, true);
				writeColHeader(timeTable, "Status    Day", Element.ALIGN_LEFT, 1, true);
				writeColHeader(timeTable, "Clock In Date/Time", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Clock Out Date/Time", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Other Time Category", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Hours Worked", Element.ALIGN_RIGHT, 1, true);
				writeColHeader(timeTable, "Other Hours", Element.ALIGN_RIGHT, 1, true);
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
// added here to
					for(HoursType h : hours) {
						totalOTHours += h.getOTHours();
						if(h.getOTHours() > 0.0)
							totalRegHours -= h.getOTHours();
						h.addRegHours(-1 * (h.getOTHours() > 0 ? h.getOTHours() : 0));
						allHours.addAllHours(h);
					}
// fix totals calculation					
						if (needWeekTotals) {
							writeColHeader(weeklySummTable, "Week Summary", Element.ALIGN_LEFT, 1, true); // Week 1
							writeColHeader(weeklySummTable, "", Element.ALIGN_LEFT, 1, true);
							writeColHeader(weeklySummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
							writeColHeader(weeklySummTable, "Hours", Element.ALIGN_LEFT, 1, true);
//							writeColHeader(weeklySummTable, "", Element.ALIGN_LEFT, 1, true);
							
							for(HoursType h : hours) {
//								writeAlign(weeklySummTable, h.getOg().getExternalId(), Element.ALIGN_LEFT, true, 5);
								if(h.getOTHours() > 0.00001) {
									//Write OT Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getOTHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Overtime", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getOTHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getPersHours() > 0.00001) {
									//Write Personal Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getPersHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Personal", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getPersHours() * 1.0), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getRegHours() > 0.00001) {
									//Write Regular Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getRegHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Regular", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getRegHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getSickHours() > 0.00001) {
									//Write Sick Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getSickHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Sick", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getSickHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getVacHours() > 0.00001) {
									//Write Vacation Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getVacHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Vacation", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getVacHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getHoliHours() > 0.00001) {
									//Write Vacation Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getHoliHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Holiday", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getHoliHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getBereavHours() > 0.00001) {
									//Write Vacation Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getBereavHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Bereavement", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getBereavHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getJuryHours() > 0.00001) {
									//Write Vacation Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getJuryHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Jury Duty", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getJuryHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getCallHours() > 0.00001) {
									//Write Vacation Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getCallHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "On Call", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getCallHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
								if(h.getTotalHours() > 0.00001) {
									//Write Total Hours
									writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									if(money)
										writeAlign(weeklySummTable, "$" + df.format(h.getTotalHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
									else
										writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, "Hours Totals", Element.ALIGN_LEFT, true);
									writeAlign(weeklySummTable, df.format(h.getTotalHours()), Element.ALIGN_LEFT, true);
//									writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
								}
							}
							addTable(timeTable); // prints the timesheets detail lines
							
							seperatorLine(weeklySummTable);
							seperatorLine(weeklySummTable);
							seperatorLine(weeklySummTable);
							seperatorLine(weeklySummTable);
//							seperatorLine(weeklySummTable);

							addTable(weeklySummTable); // prints the weekly summary totals
							
							timeTable.flushContent(); // clears the timesheet detail lines
							weeklySummTable.flushContent(); // clears the weekly summary totals
// Start Week 2
							hours.clear();
							currentHours = new HoursType();
							hours.add(currentHours);
							weekTotalHours = 0.00;
							//Write timesheet column headers
							setAlternateRowColor(new BaseColor(220, 220, 220));
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							seperatorLine(timeTable);
							writeColHeader(timeTable, "Date", Element.ALIGN_CENTER, 1, true);
							writeColHeader(timeTable, "Status    Day", Element.ALIGN_LEFT, 1, true);
							writeColHeader(timeTable, "Clock In Date/Time", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Clock Out Date/Time", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Other Time Category", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Hours Worked", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Other Hours", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Cum. Tot. Amount", Element.ALIGN_RIGHT, 1, true);
							writeColHeader(timeTable, "Comments", Element.ALIGN_CENTER, 1, true);
						}
						else
							needWeekTotals = true;
					// start new week
						firstDay = DateUtils.getDate(DateUtils.getDate(firstDay + ((weekStartDay + 6) - (DateUtils.getCalendar(firstDay).get(Calendar.DAY_OF_WEEK))) + 1));
						timesheetsForWeek.add(t);
					}
					else {
						timesheetsForWeek.add(t); // continue week
					}
//					currentHours = selectOrgGroup(hours, t, newOrgGroup, currentHours);
	
					// timesheet detail line buildup
					writeAlign(timeTable, DateUtils.getDateFormatted(t.getWorkDate()), Element.ALIGN_RIGHT, false);   //DATE
					String stateSS = " ";
					switch (t.getState()) {
						case 65: stateSS = "Approved ";
								 break;
						case 78: stateSS = "New      ";
								 break;
						case 67: stateSS = "Changed  ";
								 break;
						case 70: stateSS = "Fixed    ";
								 break;
						case 83: stateSS = "Submitted";
								 break;
						case 82: stateSS = "Rejected ";
								 break;
						case 80: stateSS = "Problem  ";
								 break;
						case 68: stateSS = "Deferred ";
								 break;
						case 73: stateSS = "Invoiced ";
								 break;
					}
					
					writeAlign(timeTable, stateSS + " " + DateUtils.getDayOfWeekAbbreviated(t.getWorkDate()), Element.ALIGN_LEFT, false); // Day of Week
/*
					try {
						OrgGroup org = ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.DEFAULT_PROJECT, t.getProject()).first();
						if(org == null)
							org = t.getProject().getRequestingOrgGroup();
						if(!org.getExternalId().equals(be.getOrgGroupRef()))
							writeAlign(timeTable, org.getExternalId(), Element.ALIGN_RIGHT, false);   //HOME ORG GROUP
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}
					catch(Exception ex) {
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}
*/
					if(!timeRelated(t).hasNext()) {
						currentHours.addRegHours(t.getTotalHours());
						totalRegHours += t.getTotalHours();
						writeAlign(timeTable, DateUtils.getDateTimeFormatted(t.getWorkDate(),t.getBeginningTime()), Element.ALIGN_RIGHT, false);
						
						if (t.getEndTime() < 0 && t.getTotalHours() == 0) 
							writeAlign(timeTable, "*** missing punch ***", Element.ALIGN_RIGHT, false);
						else 
							writeAlign(timeTable, DateUtils.getDateTimeFormatted(t.getEndDate(),t.getEndTime()), Element.ALIGN_RIGHT, false);

						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						
						/* if(money)
							writeAlign(timeTable, df.format(t.getTotalHours() * be.getWageAmount()), Element.ALIGN_RIGHT, false);
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						*/
						
						writeAlign(timeTable, df.format(t.getTotalHours()), Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
					}
					else {
						if (!(t.getBeginningTime() > 0) )
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						else
							writeAlign(timeTable, DateUtils.getDateTimeFormatted(t.getWorkDate(), t.getBeginningTime()), Element.ALIGN_RIGHT, false);
						if (!(t.getEndTime() > 0)) 
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						else 
							writeAlign(timeTable, DateUtils.getDateTimeFormatted(t.getEndDate(),t.getEndTime()), Element.ALIGN_RIGHT, false);
					
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

						/* if(money)
							writeAlign(timeTable, df.format(t.getTotalHours() * be.getWageAmount()), Element.ALIGN_RIGHT, false);
						else
							writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						*/
						
						writeAlign(timeTable, "", Element.ALIGN_RIGHT, false);
						writeAlign(timeTable, t.getTotalHours(), Element.ALIGN_RIGHT, false);
					}
					weekTotalHours += t.getTotalHours();
					totalHours += t.getTotalHours();
					writeAlign(timeTable, df.format(weekTotalHours), Element.ALIGN_RIGHT, false); // Cumulative hours on timesheet
					if(t.getDescription() != null)
						writeAlign(timeTable, (t.getDescription() == null ? "" : t.getDescription()), Element.ALIGN_LEFT, false);
					else
						writeAlign(timeTable, "", Element.ALIGN_LEFT, false);
				}
				// All timesheets for this employee are done
				calculateOvertimeHours(timesheetsForWeek, weeklyWorkHours, currentHours);
				timesheetsForWeek.clear();
				for(HoursType h : hours) {
					totalOTHours += h.getOTHours();
					if(h.getOTHours() > 0.0)
						totalRegHours -= h.getOTHours();
				}
				
/*				writeColHeader(accountSummTable, "Labor Account Summary", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Money", Element.ALIGN_LEFT, 1, true);
				writeColHeader(accountSummTable, "Hours", Element.ALIGN_LEFT, 1, true);
*/
				for(HoursType h : hours) {
					h.addRegHours(-1 * (h.getOTHours() > 0 ? h.getOTHours() : 0));
/*					writeAlign(accountSummTable, h.getOg().getExternalId(), Element.ALIGN_LEFT, false, 4);
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
*/
					allHours.addAllHours(h);
				}

/*				writeAlign(accountSummTable, "Combined Pay Code Summary", Element.ALIGN_LEFT, true);
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
*/
				
				writeColHeader(weeklySummTable, "Week Summary", Element.ALIGN_LEFT, 1, true); // Week 2
				writeColHeader(weeklySummTable, "", Element.ALIGN_LEFT, 1, true);
				writeColHeader(weeklySummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
				writeColHeader(weeklySummTable, "Hours", Element.ALIGN_LEFT, 1, true);
//				writeColHeader(weeklySummTable, "", Element.ALIGN_LEFT, 1, true);
				for(HoursType h : hours) {
//					writeAlign(weeklySummTable, h.getOg().getExternalId(), Element.ALIGN_LEFT, true, 5);
					if(h.getOTHours() > 0.00001) {
						//Write OT Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getOTHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Overtime", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getOTHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getPersHours() > 0.00001) {
						//Write Personal Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getPersHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Personal", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getPersHours() * 1.0), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getRegHours() > 0.00001) {
						//Write Regular Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getRegHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Regular", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getRegHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getSickHours() > 0.00001) {
						//Write Sick Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getSickHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Sick", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getSickHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getVacHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getVacHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Vacation", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getVacHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getHoliHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getHoliHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Holiday", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getHoliHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getBereavHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getBereavHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Bereavement", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getBereavHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getJuryHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getJuryHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Jury Duty", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getJuryHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getCallHours() > 0.00001) {
						//Write Vacation Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getCallHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "On Call", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getCallHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
					if(h.getTotalHours() > 0.00001) {
						//Write Total Hours
						writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						if(money)
							writeAlign(weeklySummTable, "$" + df.format(h.getTotalHours() * be.getWageAmount()), Element.ALIGN_LEFT, true);
						else
							writeAlign(weeklySummTable, "", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, "Hours Totals", Element.ALIGN_LEFT, true);
						writeAlign(weeklySummTable, df.format(h.getTotalHours()), Element.ALIGN_LEFT, true);
//						writeAlign(weeklySummTable, "", Element.ALIGN_RIGHT, true);
					}
				}
				addTable(timeTable); // prints the timesheets detail lines
				
				seperatorLine(weeklySummTable);
				seperatorLine(weeklySummTable);
				seperatorLine(weeklySummTable);
				seperatorLine(weeklySummTable);
//				seperatorLine(weeklySummTable);
				addTable(weeklySummTable); // prints the weekly summary totals
				timeTable.flushContent(); // clears the timesheet detail lines
				weeklySummTable.flushContent(); // clears the weekly summary totals
				
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				// added above to catch timesheet details and weekly totals when timesheets run out for this employee
				// writeColHeader(weeklySummTable, "Week 2 Summary", Element.ALIGN_LEFT, 1, true);
				writeColHeader(periodSummTable, "Period Summary", Element.ALIGN_LEFT, 2, true);
				// writeAlign(periodSummTable, " ", Element.ALIGN_LEFT, true);
				writeColHeader(periodSummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
				writeColHeader(periodSummTable, "Hours", Element.ALIGN_LEFT, 1, true);
//				writeColHeader(periodSummTable, " ", Element.ALIGN_LEFT, 1, true);
				if(totalOTHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalOTHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Overtime", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalOTHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalPersHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalPersHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Personal", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalPersHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalRegHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalRegHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Regular", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalRegHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalSickHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalSickHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Sick", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalSickHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalVacHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalVacHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Vacation", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalVacHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalHoliHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalHoliHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Holiday", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalHoliHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalBereavHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalBereavHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Bereavement", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalBereavHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalJuryHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalJuryHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "Jury Duty", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalJuryHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}
				if(totalCallHours > 0.00001) {
					writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					if(money)
						writeAlign(periodSummTable, df.format(totalCallHours * be.getWageAmount()), Element.ALIGN_LEFT, true);
					else
						writeAlign(periodSummTable, "", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, "On Call", Element.ALIGN_LEFT, true);
					writeAlign(periodSummTable, df.format(totalCallHours), Element.ALIGN_LEFT, true);
//					writeAlign(periodSummTable, "", Element.ALIGN_RIGHT, true);
				}

				writeColHeader(periodSummTable, "Totals:", Element.ALIGN_LEFT, 3, true);
/*				if(money)
					writeColHeader(periodSummTable, df.format(totalHours * be.getWageAmount()), Element.ALIGN_LEFT, 1, true);
				else
					writeColHeader(periodSummTable, "$" + df.format(0.0), Element.ALIGN_LEFT, 1, true);
*/
				writeColHeader(periodSummTable, df.format(totalHours), Element.ALIGN_LEFT, 1, true);
//				writeColHeader(periodSummTable, "", Element.ALIGN_RIGHT, 1, true);
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				seperatorLine(periodSummTable);
				
				addTable(timeTable);
				addTable(weeklySummTable);
				addTable(periodSummTable);
				infoTable.flushContent();
				timeTable.flushContent();
				weeklySummTable.flushContent();
				periodSummTable.flushContent();
				
				addTable(signatureTable);
				newPage();
			}

			writeColHeader(finalSummTable, "Final Summary", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "Pay Code", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "Hours", Element.ALIGN_LEFT, 1, true);
//			writeColHeader(finalSummTable, "", Element.ALIGN_LEFT, 1, true);

			if(allHours.getOTHours() > 0.00001) {
				//Write OT Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Overtime", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getOTHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getPersHours() > 0.00001) {
				//Write Personal Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Personal", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getPersHours() * 1.0), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getRegHours() > 0.00001) {
				//Write Regular Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Regular", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getRegHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getSickHours() > 0.00001) {
				//Write Sick Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Sick", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getSickHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getVacHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Vacation", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getVacHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getHoliHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Holiday", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getHoliHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getBereavHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Bereavement", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getBereavHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getJuryHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "Jury Duty", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getJuryHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
			if(allHours.getCallHours() > 0.00001) {
				//Write Vacation Hours
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, "On Call", Element.ALIGN_LEFT, true);
				writeAlign(finalSummTable, df.format(allHours.getCallHours()), Element.ALIGN_LEFT, true);
//				writeAlign(finalSummTable, "", Element.ALIGN_RIGHT, true);
			}
				seperatorLine(finalSummTable);
				seperatorLine(finalSummTable);
				seperatorLine(finalSummTable);
				seperatorLine(finalSummTable);
//				seperatorLine(finalSummTable);

			writeColHeader(finalSummTable, "", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, "Grand Total:", Element.ALIGN_LEFT, 1, true);
			writeColHeader(finalSummTable, df.format(allHours.getTotalHours()), Element.ALIGN_LEFT, 1, true);
//			writeColHeader(finalSummTable, "" , Element.ALIGN_RIGHT, 1, true);
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
				.eq(OrgGroup.DEFAULT_PROJECT, t.getProjectShift().getProject())
				.first();

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

		TimeCardSignatureReport tdr = new TimeCardSignatureReport();
		try {
//			tdr.build(empIds, startDate, endDate);
			tdr.build(new BOrgGroup("00001-0000000176").getOrgGroup(), startDate, endDate);
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}
