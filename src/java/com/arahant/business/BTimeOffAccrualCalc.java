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
package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BTimeOffAccrualCalc extends SimpleBusinessObjectBase<TimeOffAccrualCalc> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BTimeOffAccrualCalc.class);

	public BTimeOffAccrualCalc() {
	}

	public BTimeOffAccrualCalc(String id) {
		super(id);
	}

	public BTimeOffAccrualCalc(TimeOffAccrualCalc o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new TimeOffAccrualCalc();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(TimeOffAccrualCalc.class, key);
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BTimeOffAccrualCalc(id).delete();
	}

	@Override
	public void delete() {
		for (BTimeOffAccrualCalcSeniority c : BTimeOffAccrualCalcSeniority.makeArray(ArahantSession.getHSU().createCriteria(TimeOffAccrualCalcSeniority.class).eq(TimeOffAccrualCalcSeniority.CALC, bean).list()))
			c.delete();

		super.delete();
	}

	public HrBenefitConfig getBenefitConfig() {
		return bean.getBenefitConfig();
	}

	public void setBenefitConfig(HrBenefitConfig benefitConfig) {
		bean.setBenefitConfig(benefitConfig);
	}

	public short getCarryOverAmount() {
		return bean.getCarryOverAmount();
	}

	public void setCarryOverAmount(int carryOverAmount) {
		bean.setCarryOverAmount((short) carryOverAmount);
	}

	public short getCarryOverPercentage() {
		return bean.getCarryOverPercentage();
	}

	public void getCarryOverPercentage(short carryOverRatio) {
		bean.setCarryOverPercentage(carryOverRatio);
	}

	public char getMethod() {
		return bean.getMethod();
	}

	public void setMethod(String method) {
		bean.setMethod(method.charAt(0));
	}

	public char getStartFlag() {
		return bean.getStartFlag();
	}

	public void setStartFlag(String startFlag) {
		bean.setStartFlag(startFlag.charAt(0));
	}

	public String getTimeOffAccrualCalcId() {
		return bean.getTimeOffAccrualCalcId();
	}

	public void setTimeOffAccrualCalcId(String timeOffAccrualCalcId) {
		bean.setTimeOffAccrualCalcId(timeOffAccrualCalcId);
	}

	public short getTrialPeriod() {
		return bean.getTrialPeriod();
	}

	public void setTrialPeriod(int trialPeriod) {
		bean.setTrialPeriod((short) trialPeriod);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public int getPeriodStartDate() {
		return bean.getPeriodStartDate();
	}

	public void setPeriodStartDate(int dt) {
		bean.setPeriodStartDate(dt);
	}

	public static BTimeOffAccrualCalc[] listBenefitConfigTimeOff(final String id) {
		HibernateCriteriaUtil<TimeOffAccrualCalc> hcu = ArahantSession.getHSU().createCriteria(TimeOffAccrualCalc.class).orderByDesc(TimeOffAccrualCalc.FIRST_ACTIVE).joinTo(TimeOffAccrualCalc.BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, id);

		return makeArray(hcu.list());
	}

	public BHRAccruedTimeOff[] listTimeOff(BEmployee bemp, int startDate, int endDate, int max, boolean proRate) {

		//System.out.println("Start is "+startDate+" end is "+endDate+" hire is "+bemp.getHireDate());

//		if (bemp.getPaySchedule()==null)
//			throw new ArahantWarning("Pay schedule must be set up for the organizational group this employee is a member of - "+bemp.getNameLFM()+".");

		if (bemp.getHireDate() == 0)
			throw new ArahantWarning("Employee does not have a start date - " + bemp.getNameLFM() + ".");

//		final List<HrAccrual> l = ArahantSession.getHSU().createCriteria(HrAccrual.class).eq(HrAccrual.EMPLOYEE, bemp.getEmployee()).orderByDesc(HrAccrual.ACCRUALDATE).eq(HrAccrual.HRBENEFIT, bean.getBenefitConfig().getHrBenefit()).list();

		return masterTimeOffCalculation(bemp, startDate, endDate, bean.getAccrualType() == 'R');
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();

			BTimeOffAccrualCalc calc = new BTimeOffAccrualCalc("00000-0000000000");
			BEmployee bemp = new BEmployee("00000-0000000003");

			//System.out.println(calc.gAccruedTime(new BEmployee("00000-0000000003")));

			for (BHRAccruedTimeOff bato : calc.listTimeOff(bemp, 0, 20100201, 0, false))
				System.out.println(bato.getAccrualDate() + " " + bato.getDescription() + " " + bato.getAccrualHours() + " " + bato.getRunningTotal());
			hsu.rollbackTransaction();
		} catch (Exception e) {
			hsu.rollbackTransaction();
			logger.error(e);
		}
	}

	private int getNextAccrualDate(boolean arrears, Calendar lastAccrualDate, PaySchedule ps, int referenceDate, int accrualStartDate, int searchEndDate, List<PaySchedulePeriod> periods) throws ArahantWarning {
		int nextAccrualDate = 0;

		//figure out the first accrual
		if (lastAccrualDate == null) {
			lastAccrualDate = DateUtils.getCalendar(accrualStartDate);

			if (bean.getMethod() == TimeOffAccrualCalc.METHOD_ANNUAL)
				if (!arrears)
					if (bean.getStartFlag() == TimeOffAccrualCalc.START_CALENDAR) {
						lastAccrualDate.set(Calendar.DAY_OF_YEAR, 1);
						nextAccrualDate = DateUtils.getDate(lastAccrualDate);
					} else { // HIRE_DATE
						lastAccrualDate = DateUtils.getCalendar(referenceDate);
						lastAccrualDate.set(Calendar.YEAR, DateUtils.getCalendar(accrualStartDate).get(Calendar.YEAR));
						if (lastAccrualDate.get(Calendar.DAY_OF_YEAR) > DateUtils.getCalendar(accrualStartDate).get(Calendar.DAY_OF_YEAR))
							lastAccrualDate.add(Calendar.YEAR, -1);
						nextAccrualDate = DateUtils.getDate(lastAccrualDate);
					}
				else if (bean.getStartFlag() == TimeOffAccrualCalc.START_CALENDAR) {
					lastAccrualDate.set(Calendar.DAY_OF_YEAR, 1);
					lastAccrualDate.add(Calendar.YEAR, 1);
					nextAccrualDate = DateUtils.getDate(lastAccrualDate);
				} else { // HIRE_DATE
					lastAccrualDate = DateUtils.getCalendar(referenceDate);
					lastAccrualDate.set(Calendar.YEAR, DateUtils.getCalendar(accrualStartDate).get(Calendar.YEAR));
					if (lastAccrualDate.get(Calendar.DAY_OF_YEAR) < DateUtils.getCalendar(accrualStartDate).get(Calendar.DAY_OF_YEAR))
						lastAccrualDate.add(Calendar.YEAR, 1);
					nextAccrualDate = DateUtils.getDate(lastAccrualDate);
				}
			else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_MONTHLY)
				if (!arrears) {
					lastAccrualDate.set(Calendar.DAY_OF_MONTH, 1);
					nextAccrualDate = DateUtils.getDate(lastAccrualDate);
				} else {
					lastAccrualDate.set(Calendar.DAY_OF_MONTH, 1);
					lastAccrualDate.add(Calendar.MONTH, 1);
					nextAccrualDate = DateUtils.getDate(lastAccrualDate);
				}
			else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY) {
				//advance is not allowed for hourly because it doesn't make sense
				//so we use this field to determine what day to give accruals
				//last day of pay period OR first day of next pay period
				if (!arrears)
					nextAccrualDate = DateUtils.add(periods.get(0).getLastDate(), 1);
				else
					nextAccrualDate = periods.get(0).getLastDate();
				periods.remove(periods.get(0));
			} else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD)
				//always give accruals on first day of pay
				if (!arrears) {
					PaySchedulePeriod firstPeriod = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class).eq(PaySchedulePeriod.PAY_SCHEDULE, ps).lt(PaySchedulePeriod.PERIOD_END, accrualStartDate).orderByDesc(PaySchedulePeriod.PERIOD_END).first();
					int accrualStartDateProp = BProperty.getInt(StandardProperty.AccrualStartDate);
					if (firstPeriod == null || (firstPeriod.getLastDate() < accrualStartDateProp && accrualStartDateProp != 0)) { //dont start accruing before the set prop val
						nextAccrualDate = DateUtils.addDays(periods.get(0).getLastDate(), 1);
						periods.remove(periods.get(0));
					} else
						nextAccrualDate = DateUtils.addDays(firstPeriod.getLastDate(), 1);
				} else {
					nextAccrualDate = DateUtils.addDays(periods.get(0).getLastDate(), 1);
					periods.remove(periods.get(0));
				}
		} else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_ANNUAL) {
			lastAccrualDate.add(Calendar.YEAR, 1);
			nextAccrualDate = DateUtils.getDate(lastAccrualDate);
		} else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_MONTHLY) {
			lastAccrualDate.add(Calendar.MONTH, 1);
			nextAccrualDate = DateUtils.getDate(lastAccrualDate);
		} else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY)
			//periods should have already been set, so just use the first period in the list
			if (!periods.isEmpty()) {
				if (!arrears)
					nextAccrualDate = DateUtils.add(periods.get(0).getLastDate(), 1);
				else
					nextAccrualDate = periods.get(0).getLastDate();
				periods.remove(periods.get(0));
			} else
				nextAccrualDate = searchEndDate;
		else if (bean.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD)
			if (!periods.isEmpty()) {
				nextAccrualDate = DateUtils.addDays(periods.get(0).getLastDate(), 1);
				periods.remove(periods.get(0));
			} else
				nextAccrualDate = DateUtils.addDays(searchEndDate, 1);
		return nextAccrualDate;
	}

	private HrAccrual makeFakeAccrual(int date, double hours, String desc) {
		HrAccrual hra = new HrAccrual();
		hra.setAccrualDate(date);
		hra.setAccrualHours(hours);
		hra.setDescription(desc);
		hra.setHrBenefit(bean.getBenefitConfig().getHrBenefit());
		return hra;

	}

	private static BTimeOffAccrualCalc[] makeArray(List<TimeOffAccrualCalc> l) throws ArahantException {
		final BTimeOffAccrualCalc[] ret = new BTimeOffAccrualCalc[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BTimeOffAccrualCalc(l.get(loop));
		return ret;
	}

	public void setBenefitConfigId(String configId) {
		bean.setBenefitConfig(ArahantSession.getHSU().get(HrBenefitConfig.class, configId));
	}

	@Override
	public void insertChecks() throws ArahantException {
		if (ArahantSession.getHSU().createCriteria(TimeOffAccrualCalc.class).dateSpanCompare(TimeOffAccrualCalc.FIRST_ACTIVE, TimeOffAccrualCalc.LAST_ACTIVE, bean.getFirstActiveDate(), bean.getLastActiveDate()).eq(TimeOffAccrualCalc.BENEFIT_CONFIG, bean.getBenefitConfig()).exists())
			throw new ArahantWarning("There is already a time off configuration active for the specified dates. Only one configuration can be active at a time.");
	}

	@Override
	public void updateChecks() throws ArahantException {
		if (ArahantSession.getHSU().createCriteria(TimeOffAccrualCalc.class).ne(TimeOffAccrualCalc.CALC_ID, bean.getTimeOffAccrualCalcId()).dateSpanCompare(TimeOffAccrualCalc.FIRST_ACTIVE, TimeOffAccrualCalc.LAST_ACTIVE, bean.getFirstActiveDate(), bean.getLastActiveDate()).eq(TimeOffAccrualCalc.BENEFIT_CONFIG, bean.getBenefitConfig()).exists())
			throw new ArahantWarning("There is already a time off configuration active for the specified dates. Only one configuration can be active at a time.");
	}

	public void setCarryOverPercentage(int carryOverRatio) {
		bean.setCarryOverPercentage((short) carryOverRatio);
	}

	public boolean getArrears() {
		return (bean.getAccrualType() == 'R');
	}

	public void setArrears(boolean a) {
		if (a)
			bean.setAccrualType(TimeOffAccrualCalc.TYPE_ARREARS);
		else
			bean.setAccrualType(TimeOffAccrualCalc.TYPE_ADVANCED);
	}

	public String getAccrualType() {
		return bean.getAccrualType() + "";
	}

	public void setAccrualType(char a) {
		bean.setAccrualType(a);
	}

	private TimeOffAccrualCalcSeniority getSeniorityRules(List<TimeOffAccrualCalcSeniority> seniorities, int hireDate, int currentDate) {
		long yearCount = DateUtils.getDaysBetween(currentDate, hireDate) / 365;

		for (TimeOffAccrualCalcSeniority s : seniorities)
			if (s.getYearsOfService() <= yearCount)
				return s;
		throw new ArahantWarning("Seniorities not set up for " + bean.getBenefitConfig().getName() + " at " + yearCount + " years.");
	}

	private BHRAccruedTimeOff[] masterTimeOffCalculation(BEmployee bemp, int searchStartDate, int searchEndDate, boolean arrears) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<BHRAccruedTimeOff> lst = new ArrayList<BHRAccruedTimeOff>();

		boolean propBalanceForwardAccruals = BProperty.getBoolean("BalanceForwardAccruals", false);
		CompanyDetail coDetail = hsu.get(CompanyDetail.class, bemp.getCompany().getCompanyId());
		short fiscalBeginningMonth = coDetail.getFiscalBeginningMonth();  // May have a non-zero value even if startFlag != START_FISCAL
		int hireDate = bemp.getHireDate();
		int coverageStartDate = bemp.getBenefitJoin(bean.getBenefitConfig().getBenefitConfigId()).getCoverageStartDate();
		int referenceDate = hireDate;
		int anchorDate = propBalanceForwardAccruals && hireDate < coverageStartDate ? coverageStartDate : hireDate;

		// want to establish referenceDate to pass to getNextAccrualDate first time
		// will be the calendar start date (01/01) or fical year start date (xx/01) or specific start date mm/dd) or anniversary date immediately preceding the later of hire date or coverage start date

		if (propBalanceForwardAccruals) {
			Calendar rdt = DateUtils.getCalendar(anchorDate);
			if (this.getMethod() == TimeOffAccrualCalc.METHOD_ANNUAL)
				if (this.getStartFlag() == TimeOffAccrualCalc.START_CALENDAR) {
					rdt.add(Calendar.YEAR, 1);
					rdt.set(Calendar.DAY_OF_YEAR, 1);
					// rdt.add(Calendar.DAY_OF_YEAR, -1);
					referenceDate = DateUtils.getDate(rdt);  // Last calendar day of the year in which accruals begin
				} else if (this.getStartFlag() == TimeOffAccrualCalc.START_FISCAL) {
					rdt.set(Calendar.MONTH, fiscalBeginningMonth - 1);
					rdt.set(Calendar.DAY_OF_MONTH, 1);
					// rdt.add(Calendar.DAY_OF_YEAR, -1);
					if (DateUtils.getDate(rdt) < anchorDate)
						rdt.add(Calendar.YEAR, 1);
					referenceDate = DateUtils.getDate(rdt);  // Ends up being the last day of the Fiscal Year in which accruals begin
				} else if (this.getStartFlag() == TimeOffAccrualCalc.START_SPECIFIC) {
					int periodStartDate = bean.getPeriodStartDate();
					rdt.set(Calendar.MONTH, periodStartDate / 100 - 1);
					rdt.set(Calendar.DAY_OF_MONTH, periodStartDate - (periodStartDate / 100) * 100);
					// rdt.add(Calendar.DAY_OF_YEAR, -1);
					if (DateUtils.getDate(rdt) < anchorDate)
						rdt.add(Calendar.YEAR, 1);
					referenceDate = DateUtils.getDate(rdt);  // Ends up being the last day of the year which started on the Specific date, and includes the day which accruals begin
				} else { // if (this.getStartFlag() == TimeOffAccrualCalc.START_HIRE)
					referenceDate = DateUtils.setYear(referenceDate, rdt.get(Calendar.YEAR));
					if (referenceDate < anchorDate)
						referenceDate = DateUtils.addDays(DateUtils.setYear(referenceDate, rdt.get(Calendar.YEAR) + 1), 0); // was -1
				}
			else if (this.getMethod() == TimeOffAccrualCalc.METHOD_MONTHLY) {
				// rdt.add(Calendar.MONTH, 1);
				rdt.set(Calendar.DAY_OF_MONTH, 1);
				// rdt.add(Calendar.DAY_OF_YEAR, -1);
				if (DateUtils.getDate(rdt) < anchorDate)
					rdt.add(Calendar.MONTH, 1);
				referenceDate = DateUtils.getDate(rdt);
			} else if (this.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD) {
				rdt.add(Calendar.MONTH, 1);
				rdt.set(Calendar.DAY_OF_MONTH, 1);
				rdt.add(Calendar.DAY_OF_YEAR, -1);
				referenceDate = DateUtils.getDate(rdt);
			} else if (this.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY) {
				rdt.add(Calendar.MONTH, 1);
				rdt.set(Calendar.DAY_OF_MONTH, 1);
				rdt.add(Calendar.DAY_OF_YEAR, -1);
				referenceDate = DateUtils.getDate(rdt);
			}
		}

		boolean searchEndDateSet = (searchEndDate != 0);

		if (searchEndDate == 0) {	//If no ending date, calculate from start date to end of period start date is in.
			Calendar sed = Calendar.getInstance(); // start with date of today (or should it be start date?)
			if (this.getMethod() == TimeOffAccrualCalc.METHOD_ANNUAL)
				if (this.getStartFlag() == TimeOffAccrualCalc.START_CALENDAR) {
					sed.add(Calendar.YEAR, 1);
					if (propBalanceForwardAccruals)
						sed.set(Calendar.DAY_OF_YEAR, 1);
					sed.add(Calendar.DAY_OF_YEAR, -1);
					searchEndDate = DateUtils.getDate(sed);
				} else if (this.getStartFlag() == TimeOffAccrualCalc.START_FISCAL) {
					sed.set(Calendar.MONTH, fiscalBeginningMonth - 1);
					sed.set(Calendar.DAY_OF_MONTH, 1);
					sed.add(Calendar.DAY_OF_YEAR, -1);
					if (DateUtils.getDate(sed) < DateUtils.now())
						sed.add(Calendar.YEAR, 1);
					searchEndDate = DateUtils.getDate(sed);  // Ends up being the last day of the Fiscal Year containing today
				} else if (this.getStartFlag() == TimeOffAccrualCalc.START_SPECIFIC) {
					int periodStartDate = bean.getPeriodStartDate();
					sed.set(Calendar.MONTH, periodStartDate / 100 - 1);
					sed.set(Calendar.DAY_OF_MONTH, periodStartDate - (periodStartDate / 100) * 100);
					sed.add(Calendar.DAY_OF_YEAR, -1);
					if (DateUtils.getDate(sed) < DateUtils.now())
						sed.add(Calendar.YEAR, 1);
					searchEndDate = DateUtils.getDate(sed);  // Ends up being the last day of the year which started on the Specific date, and containing today
				} else { // if (this.getStartFlag() == TimeOffAccrualCalc.START_HIRE)
					sed = DateUtils.getCalendar(bemp.getHireDate() == 0 ? DateUtils.now() : bemp.getHireDate());
					sed.set(Calendar.YEAR, DateUtils.getNow().get(Calendar.YEAR));
					if (propBalanceForwardAccruals && DateUtils.getDate(sed) < DateUtils.now()) {
						sed.add(Calendar.YEAR, 1);
						sed.add(Calendar.DAY_OF_YEAR, -1);
					}
					searchEndDate = DateUtils.getDate(sed);
				}
			else if (this.getMethod() == TimeOffAccrualCalc.METHOD_MONTHLY) {
				sed.add(Calendar.MONTH, 1);
				sed.set(Calendar.DAY_OF_MONTH, 1);
				sed.add(Calendar.DAY_OF_YEAR, -1);
				searchEndDate = DateUtils.getDate(sed);
			} else if (this.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD) {
				sed.add(Calendar.MONTH, 1);
				sed.set(Calendar.DAY_OF_MONTH, 1);
				sed.add(Calendar.DAY_OF_YEAR, -1);
				searchEndDate = DateUtils.getDate(sed);
			} else if (this.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY) {
				sed.add(Calendar.MONTH, 1);
				sed.set(Calendar.DAY_OF_MONTH, 1);
				sed.add(Calendar.DAY_OF_YEAR, -1);
				searchEndDate = DateUtils.getDate(sed);
			}
			int today = DateUtils.now();
			if (today > searchEndDate)
				searchEndDate = today;
		}

		if (propBalanceForwardAccruals)
			if (searchStartDate < anchorDate)
				searchStartDate = anchorDate;

		int loopDate = BProperty.getInt(StandardProperty.AccrualStartDate);
		if (loopDate < hireDate)
			loopDate = propBalanceForwardAccruals && hireDate < coverageStartDate ? coverageStartDate : hireDate;
		int nextAccrualDate;
		double runningTotal = 0.00;
		// Add logic to deal with non- calendar / non-hire date carryover
		// Calendar carryOverDateCal = DateUtils.getCalendar(propBalanceForwardAccruals &&  hireDate < coverageStartDate ?  coverageStartDate : hireDate); // Hire_date
		Calendar carryOverDateCal = DateUtils.getCalendar(hireDate); // Hire_date
		if (this.getStartFlag() == TimeOffAccrualCalc.START_CALENDAR) // calendar year
			carryOverDateCal.set(Calendar.DAY_OF_YEAR, 1);
		if (this.getStartFlag() == TimeOffAccrualCalc.START_FISCAL) { // fiscal year
			carryOverDateCal.set(Calendar.MONTH, fiscalBeginningMonth - 1);
			carryOverDateCal.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (this.getStartFlag() == TimeOffAccrualCalc.START_SPECIFIC) { // fiscal year
			int periodStartDate = bean.getPeriodStartDate();
			carryOverDateCal.set(Calendar.MONTH, periodStartDate / 100 - 1);
			carryOverDateCal.set(Calendar.DAY_OF_MONTH, periodStartDate - (periodStartDate / 100) * 100);
		}

		List<Timesheet> accrualTimesheets = hsu.createCriteria(Timesheet.class).
				eq(Timesheet.PERSON, bemp.getPerson()).
				dateBetween(Timesheet.ENDDATE, loopDate, searchEndDate)
				.orderBy(Timesheet.ENDDATE)
				.joinTo(Timesheet.PROJECTSHIFT)
				.joinTo(ProjectShift.PROJECT)
				.joinTo(Project.HRBENEFITPROJECTJOINS)
				.eq(HrBenefitProjectJoin.HR_BENEFIT_CONFIG, bean.getBenefitConfig())
				.list();

		List<HrAccrual> accrualEntries = hsu.createCriteria(HrAccrual.class).
				dateBetween(HrAccrual.ACCRUALDATE, loopDate, searchEndDate).orderBy(HrAccrual.ACCRUALDATE).
				eq(HrAccrual.EMPLOYEE, bemp.getEmployee()).
				eq(HrAccrual.HRBENEFIT, bean.getBenefitConfig().getHrBenefit()).list();

		//only worry about the other timesheets if it's an hourly accrual
		List<Project> pl;
		List<Timesheet> hourlyTimesheets = new ArrayList<Timesheet>();
		if (bean.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY) {
			pl = hsu.createCriteria(Project.class).joinTo(Project.HRBENEFITPROJECTJOINS).list();

			hourlyTimesheets = hsu.createCriteria(Timesheet.class).
					orderBy(Timesheet.BEGINNING_ENTRY_DATE).
					dateBetween(Timesheet.WORKDATE, loopDate, searchEndDate).
					eq(Timesheet.PERSON, bemp.getPerson()).joinTo(ProjectShift.PROJECTSHIFTID).notIn(ProjectShift.PROJECT, pl).list();
		}
		double timesheetTotal = 0.0;

		//seniorites for accrual values
		List<TimeOffAccrualCalcSeniority> seniorities = hsu.createCriteria(TimeOffAccrualCalcSeniority.class).
				eq(TimeOffAccrualCalcSeniority.CALC, bean).
				orderByDesc(TimeOffAccrualCalcSeniority.YEARS_OF_SERVICE).list();
		//pay schedule info for Hourly/Per Pay Period calculations
		PaySchedule ps = bemp.getPaySchedule();
		List<PaySchedulePeriod> periods = hsu.createCriteria(PaySchedulePeriod.class).
				eq(PaySchedulePeriod.PAY_SCHEDULE, ps).
				dateBetween(PaySchedulePeriod.PERIOD_END, loopDate, searchEndDate).
				orderBy(PaySchedulePeriod.PERIOD_END).list();
		if (periods.isEmpty() && (this.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY || this.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD))
			throw new ArahantWarning("Pay periods must be set up before calculating accruals.");
		//FIRST we MUST figure out the next day to accrue time depending on the method and arrears
		if (!propBalanceForwardAccruals)
			nextAccrualDate = getNextAccrualDate(arrears, null, ps, hireDate, loopDate, searchEndDate, periods);
		else
			nextAccrualDate = referenceDate;

		if (nextAccrualDate < loopDate) {
			TimeOffAccrualCalcSeniority s = getSeniorityRules(seniorities, hireDate, loopDate);
			runningTotal += s.getHoursAccrued();
			if (nextAccrualDate >= searchStartDate)
				lst.add(new BHRAccruedTimeOff(makeFakeAccrual(nextAccrualDate, 0, "Opening Balance"), runningTotal));

			if (!propBalanceForwardAccruals)
				nextAccrualDate = getNextAccrualDate(arrears, DateUtils.getCalendar(nextAccrualDate), ps, hireDate, loopDate, searchEndDate, periods);
			else
				nextAccrualDate = getNextAccrualDate(arrears, DateUtils.getCalendar(nextAccrualDate), ps, referenceDate, loopDate, searchEndDate, periods);
		}

		while (loopDate <= searchEndDate) {
			if (loopDate == searchStartDate && lst.isEmpty())
				lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, 0, "Opening Balance"), runningTotal));
			Calendar loopDateCal = DateUtils.getCalendar(loopDate);
			//apply annual rules
			double adjHours = runningTotal;
			int loopDateMonth = loopDateCal.get(Calendar.MONTH);
			int loopDateDay = loopDateCal.get(Calendar.DAY_OF_MONTH);
			int carryOverMonth = carryOverDateCal.get(Calendar.MONTH);
			int carryOverDay = carryOverDateCal.get(Calendar.DAY_OF_MONTH);
			if ((loopDateMonth == carryOverMonth && loopDateDay == carryOverDay)
					|| (loopDateMonth == 1 && carryOverMonth == 1)
					&& (loopDateDay == 29 || carryOverDay == 29)
					&& (loopDateDay == 28 || carryOverDay == 28)) {
				TimeOffAccrualCalcSeniority s = getSeniorityRules(seniorities, hireDate, loopDate);
				//if we have a positive accrual value, deal with anniversary adjustments
				if (runningTotal > 0.0) {
					int carryOverAmount = s.getTimeOffCalc().getCarryOverAmount();
					int carryOverPercent = s.getTimeOffCalc().getCarryOverPercentage();
					if (carryOverAmount == 0 && carryOverPercent == 0)
						runningTotal = 0.0;
					else {
						if (carryOverPercent != 0)
							runningTotal = runningTotal * ((double) carryOverPercent / 100); //carry over X percentage
						if (carryOverAmount != 0)
							if (runningTotal > carryOverAmount)
								runningTotal = carryOverAmount;
					}
					if (loopDate >= searchStartDate) //show it to the user
						lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, propBalanceForwardAccruals ? runningTotal - adjHours : 0, "Anniversary Adjustment"), runningTotal));
				}
			}

			if (nextAccrualDate == loopDate) //add accrued hours
			{
				TimeOffAccrualCalcSeniority s = getSeniorityRules(seniorities, hireDate, loopDate);
				//figure out the next date for an accrual
				if (!propBalanceForwardAccruals)
					nextAccrualDate = getNextAccrualDate(arrears, DateUtils.getCalendar(nextAccrualDate), ps, hireDate, loopDate, searchEndDate, periods);
				else
					nextAccrualDate = getNextAccrualDate(arrears, DateUtils.getCalendar(nextAccrualDate), ps, referenceDate, loopDate, searchEndDate, periods);
				//if the NEXT accrual is outside the search date, need to prorate the last accrual
				if (nextAccrualDate > searchEndDate && searchEndDateSet) {
					if (bean.getMethod() != TimeOffAccrualCalc.METHOD_HOURLY)
						runningTotal += getProRatedAccrual(s, loopDate, searchEndDate, nextAccrualDate);
					else
						runningTotal += (timesheetTotal * s.getHoursAccrued());

					if (loopDate >= searchStartDate) //show it to the user
						if (bean.getMethod() != TimeOffAccrualCalc.METHOD_HOURLY)
							lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, getProRatedAccrual(s, loopDate, searchEndDate, nextAccrualDate), "Pro-Rated " + getAccrualDescription()), runningTotal));
						else {
							lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, (timesheetTotal * s.getHoursAccrued()), "Pro-Rated " + getAccrualDescription()), runningTotal));
							timesheetTotal = 0.0;
						}
				} else {
					if (bean.getMethod() != TimeOffAccrualCalc.METHOD_HOURLY)
						runningTotal += s.getHoursAccrued();
					else
						runningTotal += (timesheetTotal * s.getHoursAccrued());

					if (loopDate >= searchStartDate) //show it to the user
						if (bean.getMethod() != TimeOffAccrualCalc.METHOD_HOURLY)
							lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, s.getHoursAccrued(), getAccrualDescription()), runningTotal));
						else {
							lst.add(new BHRAccruedTimeOff(makeFakeAccrual(loopDate, (timesheetTotal * s.getHoursAccrued()), getAccrualDescription()), runningTotal));
							timesheetTotal = 0.0;
						}
				}
			}

			for (HrAccrual a : accrualEntries) //adjust for manual accruals
				if (a.getAccrualDate() == loopDate) {
					runningTotal += a.getAccrualHours();
					if (loopDate >= searchStartDate) //show it to the user
						lst.add(new BHRAccruedTimeOff(a, runningTotal));
				}
			for (Timesheet t : accrualTimesheets) //subtract for timesheet accruals
				if (t.getEndDate() == loopDate) {
					runningTotal -= t.getTotalHours();
					if (loopDate >= searchStartDate) //show it to the user
						lst.add(new BHRAccruedTimeOff(t, runningTotal));
				}

			for (Timesheet t : hourlyTimesheets)
				if (t.getEndDate() == loopDate)
					timesheetTotal += t.getTotalHours();
			loopDate = DateUtils.addDays(loopDate, 1);
		}
		if (searchEndDateSet)
			lst.add(new BHRAccruedTimeOff(makeFakeAccrual(searchEndDate, 0, "Closing Balance"), runningTotal));

		final BHRAccruedTimeOff[] ret = new BHRAccruedTimeOff[lst.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = lst.get(loop);

		return ret;
	}

	private String getAccrualDescription() {
		if (this.getMethod() == TimeOffAccrualCalc.METHOD_ANNUAL)
			return "Annual Accrual";
		else if (this.getMethod() == TimeOffAccrualCalc.METHOD_MONTHLY)
			return "Monthly Accrual";
		else if (this.getMethod() == TimeOffAccrualCalc.METHOD_PAYPERIOD)
			return "Pay Period Accrual";
		else if (this.getMethod() == TimeOffAccrualCalc.METHOD_HOURLY)
			return "Hourly Accrual";
		return "";
	}

	private double getProRatedAccrual(TimeOffAccrualCalcSeniority s, int loopDate, int searchEndDate, int nextAccrualDate) {
		double denominator = DateUtils.getWorkDaysBetween(loopDate, nextAccrualDate);
		double numerator = DateUtils.getWorkDaysBetween(loopDate, searchEndDate);
		double percentage = numerator / denominator;

		return (s.getHoursAccrued() * percentage);


	}
}

/*
 * Vacation (2) weeks

 Accrual of 3.07 hrs per pay period

 Accrual begins day one of employment can't use until after start date which is 90 days



 Vacation (3) weeks

 Accrual of 4.61 hours per pay period



 Vacation (8) weeks and (10) weeks

 All vacation available day 1 of employment



 For vacation of 2 and 3 weeks in year three it moves up one week to 3 and 4 weeks.

 Accrual is based on vacation days  divided by 26 pay periods. (80/26=3.07) or ( 120/26=4.61)



 All vacation is based on Calendar year.

 No carryover of vacation

 New employees is prorated until the end of year and starts new in January
 */
