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
 *
 * Created on Feb 22, 2007
 */
package com.arahant.services.standard.hr.hrPosition;

import com.arahant.business.BHRPosition;


public class ListPositionsReturnItem {

	private String accrualAccountId;
	private String name;
	private int firstActiveDate;
	private int lastActiveDate;
	private String benefitClassId;
	private String benefitClassName;
	private float weeklyPerDiem;
	private short seqno;
	private String applicantDefault;
	
	public ListPositionsReturnItem()
	{
	}
	/**
	 * @return Returns the accrualAccountId.
	 */
	public String getPositionId() {
		return accrualAccountId;
	}

	/**
	 * @param accrualAccountId The accrualAccountId to set.
	 */
	public void setPositionId(final String accrualAccountId) {
		this.accrualAccountId = accrualAccountId;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getAccrualAccountId() {
		return accrualAccountId;
	}

	public void setAccrualAccountId(String accrualAccountId) {
		this.accrualAccountId = accrualAccountId;
	}

	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}

	public String getBenefitClassName() {
		return benefitClassName;
	}

	public void setBenefitClassName(String benefitClassName) {
		this.benefitClassName = benefitClassName;
	}

	public float getWeeklyPerDiem() {
		return weeklyPerDiem;
	}

	public void setWeeklyPerDiem(float weeklyPerDiem) {
		this.weeklyPerDiem = weeklyPerDiem;
	}

	public short getSeqno() {
		return seqno;
	}

	public void setSeqno(short seqno) {
		this.seqno = seqno;
	}

	public String getApplicantDefault() {
		return applicantDefault;
	}

	public void setApplicantDefault(String applicantDefault) {
		this.applicantDefault = applicantDefault;
	}

	ListPositionsReturnItem(final BHRPosition bc) {
		accrualAccountId = bc.getPositionId();
		name = bc.getName();
		lastActiveDate = bc.getLastActiveDate();
		firstActiveDate = bc.getFirstActiveDate();
		benefitClassId = bc.getBenefitClassId();
		benefitClassName = bc.getBenefitClassName();
		weeklyPerDiem = bc.getWeeklyPerDiem();
		seqno = bc.getSeqno();
		applicantDefault = bc.getApplicantDefault() + "";
	}
}

	
