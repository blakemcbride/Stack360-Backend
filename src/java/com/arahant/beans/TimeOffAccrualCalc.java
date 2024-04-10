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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = TimeOffAccrualCalc.TABLE_NAME)
public class TimeOffAccrualCalc extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "time_off_accrual_calc";
	public static final char METHOD_ANNUAL = 'A';
	public static final char METHOD_HOURLY = 'H';
	public static final char METHOD_PAYPERIOD = 'P';
	public static final char METHOD_MONTHLY = 'M';
	public static final char START_HIRE = 'H';
	public static final char START_CALENDAR = 'C';
	public static final char START_FISCAL = 'F';
	public static final char START_SPECIFIC = 'S';
	public static final char TYPE_ADVANCED = 'D';
	public static final char TYPE_ARREARS = 'R';
	public static final String BENEFIT_CONFIG = "benefitConfig";
	public static final String FIRST_ACTIVE = "firstActiveDate";
	public static final String LAST_ACTIVE = "lastActiveDate";
	public static final String START_FLAG = "startFlag";
	public static final String CALC_ID = "timeOffAccrualCalcId";
	private HrBenefitConfig benefitConfig;
	private String timeOffAccrualCalcId;
	private char method; //A=Annual Allotment, H=Hourly Allotment
	private short trialPeriod;  //days after hire until vacation is available
	private char startFlag; //vacation starts calculating at (C)alendar year or (H)ire year
	private short carryOverAmount; //amount of time that can be carried over - 0 will reset to nothing
	private short carryOverPercentage; //ratio of left over time that can be carried over
	private char accrualType; //calc in advance 'D' or arrears 'R'
	private int firstActiveDate;
	private int lastActiveDate;
	private int periodStartDate; // MMDD

	@Column(name = "accrual_type")
	public char getAccrualType() {
		return accrualType;
	}

	public void setAccrualType(char a) {
		this.accrualType = a;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_config_id")
	public HrBenefitConfig getBenefitConfig() {
		return benefitConfig;
	}

	public void setBenefitConfig(HrBenefitConfig benefitConfig) {
		this.benefitConfig = benefitConfig;
	}

	@Column(name = "max_carry_over_hours")
	public short getCarryOverAmount() {
		return carryOverAmount;
	}

	public void setCarryOverAmount(short carryOverAmount) {
		this.carryOverAmount = carryOverAmount;
	}

	@Column(name = "carry_over_percentage")
	public short getCarryOverPercentage() {
		return carryOverPercentage;
	}

	public void setCarryOverPercentage(short carryOverPercentage) {
		this.carryOverPercentage = carryOverPercentage;
	}

	@Column(name = "accrual_method")
	public char getMethod() {
		return method;
	}

	public void setMethod(char method) {
		this.method = method;
	}

	@Column(name = "period_start")
	public char getStartFlag() {
		return startFlag;
	}

	public void setStartFlag(char startFlag) {
		this.startFlag = startFlag;
	}

	@Id
	@Column(name = "time_off_accrual_calc_id")
	public String getTimeOffAccrualCalcId() {
		return timeOffAccrualCalcId;
	}

	public void setTimeOffAccrualCalcId(String timeOffAccrualCalcId) {
		this.timeOffAccrualCalcId = timeOffAccrualCalcId;
	}

	@Column(name = "trial_period")
	public short getTrialPeriod() {
		return trialPeriod;
	}

	public void setTrialPeriod(short trialPeriod) {
		this.trialPeriod = trialPeriod;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "first_active_date")
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	@Column(name = "period_start_date")
	public int getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(int periodStartDate) {
		this.periodStartDate = periodStartDate;
	}
	

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "time_off_accrual_calc_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return timeOffAccrualCalcId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeOffAccrualCalc other = (TimeOffAccrualCalc) obj;
		if ((this.timeOffAccrualCalcId == null) ? (other.timeOffAccrualCalcId != null) : !this.timeOffAccrualCalcId.equals(other.timeOffAccrualCalcId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + (this.timeOffAccrualCalcId != null ? this.timeOffAccrualCalcId.hashCode() : 0);
		return hash;
	}
}
