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


package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;

import java.io.File;
import java.util.Collections;
import java.util.*;

/**
 *
 */
public class ADPTimeExport {

	public String build(final int startDate, final int endDate, final String[] orgGroupID) throws Exception {
		File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "ADP Hours Export File " + DateUtils.now() + ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			//Writing title
			dfw.writeField("e-TIME");
			dfw.endRecord();

			//Writing column headers
			dfw.writeField("Co Code");
			dfw.writeField("Batch Id");
			dfw.writeField("File #");
			dfw.writeField("Pay #");
			dfw.writeField("Temp Dept");
			dfw.writeField("Temp Rate");
			dfw.writeField("Reg Hours");
			dfw.writeField("O/T Hours");
			dfw.writeField("Hours 3 Code");
			dfw.writeField("Hours 3 Amount");
			dfw.writeField("Hours 3 Code");
			dfw.writeField("Hours 3 Amount");
			dfw.writeField("Hours 3 Code");
			dfw.writeField("Hours 3 Amount");
			dfw.writeField("Hours 3 Code");
			dfw.writeField("Hours 3 Amount");
			dfw.endRecord();

			//Create Set of all employee IDs from specified org group and children groups
			Set<String> empSet = new HashSet<String>();
			for (String og : orgGroupID) {
				BOrgGroup bog = new BOrgGroup(og);

				empSet.addAll(bog.getAllPersonIdsForOrgGroupHierarchy(true));
			}
			Iterator<String> empIterator = empSet.iterator();

			//Converts set of employee IDs to BEmployees
			Set<BEmployee> beSet = new HashSet<BEmployee>();
			while (empIterator.hasNext()) {
				String temp = empIterator.next();
				if (!temp.equals(""))
					beSet.add(new BEmployee(temp));
			}
			List<BEmployee> beList = new ArrayList<BEmployee>();
			beList.addAll(beSet);

			//Sort employees by employee number
			EmployeeComparator comparator = new EmployeeComparator();
			Collections.sort(beList, comparator);
			Iterator<BEmployee> beIterator = beList.iterator();

			//Loop for each Employee
			while (beIterator.hasNext()) {
//				System.out.println("**************************");
				BEmployee be = beIterator.next();

				//All timesheets within the specified date for the employee
				List<Timesheet> tsList = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, be.getEmployee()).between(Timesheet.WORKDATE, startDate, endDate).orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).list();
				List<Timesheet> timesheetsForWeek = new ArrayList<Timesheet>();
				List<HoursType> hours = new ArrayList<HoursType>();
				Iterator<OrgGroupAssociation> ogaI;
				Set<OrgGroupAssociation> ogaSet = be.getOrgGroupAssociations();
				HoursType currentHours = new HoursType();
				int recentDate = 0;
				if (ogaSet != null) {
					ogaI = ogaSet.iterator();
					if (ogaSet.size() > 1)
						if (be.getDefaultProject() == null)
							while (ogaI.hasNext()) {
								OrgGroupAssociation oga = ogaI.next();

								if (DateUtils.getDate(oga.getRecordChangeDate()) > recentDate) {
									recentDate = DateUtils.getDate(oga.getRecordChangeDate());
									currentHours = new HoursType(new BOrgGroup(oga.getOrgGroupId()).getOrgGroup());
								}
							}
						else
							currentHours = new HoursType(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.DEFAULT_PROJECT, be.getDefaultProject()).first());
					else
						currentHours = new HoursType(new BOrgGroup(ogaI.next().getOrgGroupId()).getOrgGroup());
				}

//				while(ogaI.hasNext()) {
//					OrgGroupAssociation oga = ogaI.next();
//
//					if(DateUtils.getDate(oga.getRecordChangeDate()) > recentDate) {
//						recentDate = DateUtils.getDate(oga.getRecordChangeDate());
//						currentHours = new HoursType(new BOrgGroup(oga.getOrgGroupId()).getOrgGroup());
//					}
//				}
				hours.add(currentHours);
				final String homeOrgGroupId = currentHours.getOg().getOrgGroupId();
				double workDaysCount;
				double expectedWeeklyHours = 0.0;
				boolean newOrgGroup = true;
				int weekStartDay = new BOrgGroup(be.getOrgGroupId()).getNewWeekBeginDay();
				if (weekStartDay == 0)
					weekStartDay = Calendar.SUNDAY;

				Calendar startWeek = DateUtils.getNow();
				startWeek.set(Calendar.HOUR_OF_DAY, 0);
				startWeek.set(Calendar.MINUTE, 0);
				startWeek.set(Calendar.SECOND, 0);
				Calendar endWeek = DateUtils.getNow();
				endWeek.set(Calendar.HOUR_OF_DAY, 23);
				endWeek.set(Calendar.MINUTE, 59);
				endWeek.set(Calendar.SECOND, 59);

				for (Timesheet tempTS : tsList) {
					if (DateUtils.getCalendar(tempTS.getWorkDate()).before(startWeek) || DateUtils.getCalendar(tempTS.getWorkDate() - 1).after(endWeek)) {
						startWeek = DateUtils.getCalendar(tempTS.getWorkDate());
						endWeek = DateUtils.getCalendar(tempTS.getWorkDate());
						if (startWeek.get(Calendar.DAY_OF_WEEK) != weekStartDay)
							startWeek.add(Calendar.DAY_OF_YEAR, weekStartDay - startWeek.get(Calendar.DAY_OF_WEEK));

						if (endWeek.get(Calendar.DAY_OF_WEEK) != weekStartDay + 6)
							endWeek.add(Calendar.DAY_OF_YEAR, ((7 - (endWeek.get(Calendar.DAY_OF_WEEK) - (weekStartDay + 6))) % 7));

//						System.out.println("");
						workDaysCount = getWeeklyWorkDays(startWeek, tempTS, weekStartDay, homeOrgGroupId);
						expectedWeeklyHours = workDaysCount * getEmployeeExpectedHours(tempTS);
						calculateOvertimeHours(timesheetsForWeek, expectedWeeklyHours, startDate, endDate, hours);

						timesheetsForWeek.removeAll(timesheetsForWeek);

						if (startWeek.before(DateUtils.getCalendar(startDate - 1)))
							timesheetsForWeek.addAll(ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, be.getPerson()).dateBetween(Timesheet.WORKDATE, DateUtils.getDate(startWeek), startDate - 1).list());
						if (endWeek.after(DateUtils.getCalendar(endDate)))
							timesheetsForWeek.addAll(ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, be.getPerson()).dateBetween(Timesheet.WORKDATE, endDate + 1, DateUtils.getDate(endWeek)).list());
					}
					timesheetsForWeek.add(tempTS);

					if (tempTS.getWorkDate() >= startDate
							&& tempTS.getWorkDate() <= endDate) {
						//Find hours appropriate for each paid benefit type
						Iterator<HrBenefitConfig> configs = timeRelated(tempTS);
						if (configs.hasNext())
							while (configs.hasNext()) {
								HrBenefitConfig config = configs.next();
								if (config.getHrBenefit().getName().toLowerCase().contains("vacation"))
									currentHours.addVacHours(tempTS.getTotalHours());
								else if (config.getHrBenefit().getName().toLowerCase().contains("sick"))
									currentHours.addSickHours(tempTS.getTotalHours());
								else if (config.getHrBenefit().getName().toLowerCase().contains("personal"))
									currentHours.addPersHours(tempTS.getTotalHours());
								else if (config.getHrBenefit().getName().toLowerCase().contains("holiday"))
									currentHours.addHoliHours(tempTS.getTotalHours());
							}
						else
							currentHours = selectOrgGroup(hours, tempTS, newOrgGroup, currentHours);
						currentHours.addRegHours(tempTS.getTotalHours());
					}
				}
				if (!timesheetsForWeek.isEmpty())//
					calculateOvertimeHours(timesheetsForWeek, expectedWeeklyHours, startDate, endDate, hours);

				for (HoursType ht : hours)
					if (ht.hasTime()) {
						//Writing Co Code
						dfw.writeField(new BOrgGroup(getSuperGroup(homeOrgGroupId)).getExternalId()); /*
						 * Use new
						 * BOrgGroup(getSuperGroup(homeOrgGroupId)).getOrgGroupId())
						 * for testing
						 */

						//Writing Batch Id volumn values
						dfw.writeField(95);

						//Writing File #
						dfw.writeField(be.getExtRef()); /*
						 * Use be.getNameFML() for testing
						 */

						//Writing Pay # columm values
						dfw.writeField(1);

						//Writing Temp Dept if Non-Benefit
						if (!ht.hasTimeRelated() && !ht.getOg().getExternalId().equals((new BOrgGroup(homeOrgGroupId)).getExternalId()))
							dfw.writeField(ht.getOg().getExternalId()); /*
						 * Use ht.getOg().getOrgGroupId()) for testing
						 */
						else
							dfw.writeField("");

						//Writing Temp Rate
						dfw.writeField("");

						//Writing Reg Hours
						if (ht.getOTHours() > 0.00001) {
							ht.addRegHours(-1 * (ht.getOTHours() + ht.getPersHours() + ht.getSickHours() + ht.getVacHours() + ht.getHoliHours()));
							dfw.writeField(ht.getRegHours());
						} else {
							ht.addRegHours(-1 * (ht.getPersHours() + ht.getSickHours() + ht.getVacHours() + ht.getHoliHours()));
							dfw.writeField(ht.getRegHours());
						}
						//Writing O/T Hours
						if (ht.getOTHours() > 0.00001)//ht.getPersHours() + ht.getSickHours() + ht.getVacHours())
							dfw.writeField(ht.getOTHours());//- (ht.getPersHours() + ht.getSickHours() + ht.getVacHours()));
						else
							dfw.writeField("");

						if (ht.hasTimeRelated()) {
							//Writing Hours 3 Codes and Amounts
							if (!Utils.doubleEqual(ht.getVacHours(), 0, 0.00001)) {
								dfw.writeField("V");
								dfw.writeField(ht.getVacHours());
							}

							if (!Utils.doubleEqual(ht.getSickHours(), 0, 0.00001)) {
								dfw.writeField("S");
								dfw.writeField(ht.getSickHours());
							}

							if (!Utils.doubleEqual(ht.getPersHours(), 0, 0.00001)) {
								dfw.writeField("P");
								dfw.writeField(ht.getPersHours());
							}

							if (!Utils.doubleEqual(ht.getHoliHours(), 0, 0.00001)) {
								dfw.writeField("H");
								dfw.writeField(ht.getHoliHours());
							}
						}

						dfw.endRecord();
					}
			}
		} finally {
			dfw.close();
		}

		return csvFile.getName();
	}

	private String getSuperGroup(String org) {
		BOrgGroup bog = new BOrgGroup(org);
		if (bog.getParent() != null)
			if (bog.getParent().getParent() != null)
				return getSuperGroup(bog.getParent().getOrgGroupId());
		return org;
	}

	private double getEmployeeExpectedHours(Timesheet timesheet) {
		BEmployee emp = new BEmployee(timesheet.getPerson().getPersonId());
		if (Utils.doubleEqual(emp.getWorkHours(), 0, 0.00001))
//			System.out.println("Standard daily expected hours: 8");
			return 8.0;
//			System.out.println("Standard daily expected hours: " + emp.getWorkHours());
		return emp.getWorkHours();
	}

	private void calculateOvertimeHours(List<Timesheet> timesheetsForWeek, double expectedHours, int startDate, int endDate, List<HoursType> hours) {
		double total = 0.0;
		double afterTotal = 0.0;
		double beforeTotal = 0.0;
		double offHours = 0.0;
		List<Timesheet> goodTimesheets = new ArrayList<Timesheet>();
//        List<Integer> goodDates = new ArrayList<Integer>();
		HoursType currentHours = hours.get(0);

		for (Timesheet t : timesheetsForWeek) {
//			System.out.println("Looking at timesheet for " + t.getPerson().getNameFML() + " - Date: " + t.getWorkDate() + ", Start Time: " + t.getBeginningTime() + ", End Time: " + t.getEndTime() + ", Duration: " + t.getTotalHours());
			Iterator<HrBenefitConfig> configs = timeRelated(t);

			while (configs.hasNext()) {
				configs.next();
				offHours += t.getTotalHours();
			}

			if (t.getWorkDate() > endDate)
//				System.out.println("\tTimesheet takes place after date range, but within the same week.");
				afterTotal += t.getTotalHours();
			else if (t.getWorkDate() < startDate)
//				System.out.println("\tTimesheet takes place before date range, but within the same week.");
				beforeTotal += t.getTotalHours();
			else {
				goodTimesheets.add(t);
//				System.out.print("\tTimesheet added to total hours in range. Amount: " + t.getTotalHours() + ", Before Addition Total: " + total + ", ");
				total += t.getTotalHours();
//				System.out.println("After Addition Total: " + total);
			}
//			System.out.println("");
		}
//		System.out.println("Expected Hours: " + expectedHours);
//		System.out.println("Time-Related Hours: " + offHours);
//		System.out.println("In-Range Hours: " + total);
//		System.out.println("Before Range Hours: " + beforeTotal);
//		System.out.println("After Range Hours: " + afterTotal);
//		System.out.println("Total Worked Hours: " + (total + beforeTotal + afterTotal));
//		System.out.println("Calculated OT Hours: " + (total + beforeTotal + afterTotal - expectedHours - offHours));
//        System.out.println("");
		if (expectedHours + offHours < total + beforeTotal + afterTotal + 0.000001) { //do they have overtime?
			HashMap<OrgGroup, Double> otOGHours = new HashMap<OrgGroup, Double>();

			for (Timesheet t2 : goodTimesheets) {
//				goodDates.add(t2.getWorkDate());
//				System.out.println("Timesheet for " + t2.getWorkDate() + ", Duration: " + t2.getTotalHours() + ", Org Group: " + currentHours.getOg() + ". Remaining Expected Hours: " + expectedHours);

				//If current timesheet hours would result in exceeding the overtime threshold
				if (expectedHours - t2.getTotalHours() <= 0.00001)
//					System.out.println("\tTimesheet warrants overtime - " + (t2.getTotalHours() - expectedHours) + " added to " + otOGHours.get(currentHours.getOg()) + " resulting in: " + (otOGHours.get(currentHours.getOg()) + t2.getTotalHours() - expectedHours) + " total overtime hours.");
					otOGHours.put(currentHours.getOg(), (otOGHours.get(currentHours.getOg()) == null)
							? (t2.getTotalHours() - expectedHours)
							: (otOGHours.get(currentHours.getOg()) + t2.getTotalHours() - expectedHours));
				else if (otOGHours.get(currentHours.getOg()) == null)
//					System.out.println("\tTimesheet does not warrants overtime");
					otOGHours.put(currentHours.getOg(), 0.0);

				//Subtract timesheet hours from weekly expected hours
				expectedHours = (expectedHours - t2.getTotalHours() > -0.00001) ? (expectedHours - t2.getTotalHours()) : 0.0;
//				System.out.println("\tNew Expected Hours: " + expectedHours);
			}

			for (HoursType hour : hours)
//				System.out.println("Org Group: " + hour.getOg());
				if (otOGHours.get(hour.getOg()) != null)
//					System.out.println(hour.getOg() + " has overtime hours: " + otOGHours.get(hour.getOg()));
					hour.addOTHours(otOGHours.get(hour.getOg())); //					System.out.println("Total overtime hours for this individual/Org Group: " + hour.getOTHours());
		}
//		System.out.println("******************\n");
	}

	private double isHoliday(Timesheet ts, Integer date, String homeOrgGroupId) {
		String companyId = new BOrgGroup(homeOrgGroupId).getCompanyId();
		List<Holiday> holidays = ArahantSession.getHSU().createCriteria(Holiday.class).joinTo(Holiday.ORG_GROUP).eq(OrgGroup.ORGGROUPID, companyId).list();

		for (Holiday h : holidays)
			if (h.getHdate() == date)
				return (h.getPartOfDay() == 'F' ? 1.0 : 0.5);
		return 0.0;
	}

	private double getWeeklyWorkDays(Calendar startWeek, Timesheet ts, int weekStartDay, String homeOrgGroupId) {
		List<Integer> weekDates = new ArrayList();
		double workDaysCount = 0.0;

//		System.out.println("Start of week: " + DateUtils.getDate(startWeek) + ", day of week: " + startWeek.get(Calendar.DAY_OF_WEEK));
		for (int i = 0; i < 7; i++)
			if (((startWeek.get(Calendar.DAY_OF_WEEK) + i) != Calendar.SATURDAY
					&& (startWeek.get(Calendar.DAY_OF_WEEK) + i) != Calendar.SUNDAY))
				weekDates.add(DateUtils.getDate(startWeek) + i); //			   System.out.println("\tDate counted - " + DateUtils.getDate(startWeek) + i);

		for (Integer i : weekDates)
			workDaysCount += (1.0 - isHoliday(ts, i, homeOrgGroupId));

//		System.out.println("Standard weekly work days: " + workDaysCount);
		return workDaysCount;
	}

	private HoursType selectOrgGroup(List<HoursType> hours, Timesheet t2, boolean newOrgGroup, HoursType currentHours) {
		OrgGroup tsOrgGroup = ArahantSession.getHSU().createCriteria(OrgGroup.class)
				.eq(OrgGroup.DEFAULT_PROJECT, t2.getProjectShift().getProject())
				.first();

		if (tsOrgGroup == null)
			tsOrgGroup = t2.getProjectShift().getProject().getRequestingOrgGroup();
		for (HoursType hour : hours) {
			if (hour.getOg() == tsOrgGroup) {
				newOrgGroup = false;
				currentHours = hour;
				break;
			}
			newOrgGroup = true;
		}
		if (newOrgGroup) {
			currentHours = new HoursType(tsOrgGroup);
			hours.add(currentHours);
		}
		return currentHours;
	}

	private Iterator<HrBenefitConfig> timeRelated(Timesheet ts) {
		Iterator<HrBenefitConfig> configs;
		Set<HrBenefitConfig> newConfigs = new HashSet<HrBenefitConfig>();
		Set<HrBenefitConfig> tempConfigs = ts.getProjectShift().getProject().getBenefitConfigs();
		if (tempConfigs != null)
			configs = tempConfigs.iterator();
		else
			configs = null;

		while (configs.hasNext()) {
			HrBenefitConfig hrbene = configs.next();
			if (hrbene.getHrBenefit().getTimeRelated() == 'Y')
				newConfigs.add(hrbene);
		}

		return newConfigs.iterator();
	}

	private class EmployeeComparator implements Comparator {

		@Override
		public int compare(Object emp1, Object emp2) {
			if (((BEmployee) emp1).getExtRef() == null)
				return ((BEmployee) emp2).getExtRef() == null ? 0 : -1;
			else if (((BEmployee) emp2).getExtRef() == null)
				return 1;
			else
				return ((BEmployee) emp1).getExtRef().compareTo(((BEmployee) emp2).getExtRef());
		}
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
			return !(Utils.doubleEqual(this.regHours, 0.0, .00001)
					&& Utils.doubleEqual(this.otHours, 0.0, .00001)
					&& Utils.doubleEqual(this.vacHours, 0.0, .00001)
					&& Utils.doubleEqual(this.sickHours, 0.0, .00001)
					&& Utils.doubleEqual(this.persHours, 0.0, .00001)
					&& Utils.doubleEqual(this.holiHours, 0.0, .00001)
					&& Utils.doubleEqual(this.salaryHours, 0.0, .00001)
					&& Utils.doubleEqual(this.bereavHours, 0.0, .00001)
					&& Utils.doubleEqual(this.juryHours, 0.0, .00001)
					&& Utils.doubleEqual(this.callHours, 0.0, .00001)
					&& Utils.doubleEqual(this.noOtHours, 0.0, .00001));
		}

		public boolean hasTimeRelated() {
			return !(Utils.doubleEqual(this.vacHours, 0.0, .00001)
					&& Utils.doubleEqual(this.sickHours, 0.0, .00001)
					&& Utils.doubleEqual(this.persHours, 0.0, .00001)
					&& Utils.doubleEqual(this.holiHours, 0.0, .00001)
					&& Utils.doubleEqual(this.bereavHours, 0.0, .00001)
					&& Utils.doubleEqual(this.juryHours, 0.0, .00001)
					&& Utils.doubleEqual(this.callHours, 0.0, .00001)
					&& Utils.doubleEqual(this.noOtHours, 0.0, .00001));
		}

		public int getDate() {
			return date;
		}

		public void setDate(int date) {
			this.date = date;
		}

		private double getTotalHours() {
			return this.regHours
					+ this.otHours
					+ this.persHours
					+ this.sickHours
					+ this.vacHours
					+ this.holiHours
					+ this.salaryHours
					+ this.bereavHours
					+ this.juryHours
					+ this.callHours
					+ this.noOtHours;
		}
	}

	public static void main(String args[]) {
		try {
			ArahantSession.getHSU().dontAIIntegrate();
			int startDate = 20110529;
			int endDate = 20110611;
			List<OrgGroup> orgGroups = ArahantSession.getHSU().createCriteria(OrgGroup.class).list();
			List<String> orgGroupIDs = new ArrayList<String>();

			for (OrgGroup og : orgGroups)
				orgGroupIDs.add(og.getOrgGroupId());
			String[] orgGroupID = new String[orgGroupIDs.size()];

			orgGroupID = orgGroupIDs.toArray(orgGroupID);

//			String[] orgGroupID = {
//									"00001-0000000016"
//									,
//									"00001-0000000118"
//									,
//									"00001-0000000034"
//									,
//									"00001-0000000072"
//									,
//									"00001-0000000031"
//								  };

			ADPTimeExport adpTime = new ADPTimeExport();
			adpTime.build(startDate, endDate, orgGroupID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
