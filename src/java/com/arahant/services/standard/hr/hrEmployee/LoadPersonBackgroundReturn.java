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


/**
 * 
 *
 */
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;

public class LoadPersonBackgroundReturn extends TransmitReturnBase {

	void setData(BPerson bc)
	{
		militaryBranch=bc.getMilitaryBranch() + "";
		enlistFromMonth=bc.getMilitaryStartDate() % 100;
		enlistToMonth=bc.getMilitaryEndDate() % 100;
		enlistFromYear=bc.getMilitaryStartDate() / 100;
		enlistToYear=bc.getMilitaryEndDate() / 100;
		dischargeRank=bc.getMilitaryRank();
		dischargeType=bc.getMilitaryDischargeType() + "";
		convicted=bc.getConvictedOfCrime() + "";
		convictedDescription=bc.getConvictedOfWhat();
		workedFor=bc.getWorkedForCompanyBefore() + "";
		workedForWhen=bc.getWorkedForCompanyWhen();
		dischargeExplain=bc.getMilitaryDischargeExplain();
	}
	
	private String militaryBranch;
	private int enlistFromMonth;
	private int enlistToMonth;
	private int enlistFromYear;
	private int enlistToYear;
	private String dischargeRank;
	private String dischargeType;
	private String convicted;
	private String convictedDescription;
	private String workedFor;
	private String workedForWhen;
	private String dischargeExplain;

	public String getConvicted() {
		return convicted;
	}

	public void setConvicted(String convicted) {
		this.convicted = convicted;
	}

	public String getConvictedDescription() {
		return convictedDescription;
	}

	public void setConvictedDescription(String convictedDescription) {
		this.convictedDescription = convictedDescription;
	}

	public String getDischargeExplain() {
		return dischargeExplain;
	}

	public void setDischargeExplain(String dischargeExplain) {
		this.dischargeExplain = dischargeExplain;
	}

	public String getDischargeRank() {
		return dischargeRank;
	}

	public void setDischargeRank(String dischargeRank) {
		this.dischargeRank = dischargeRank;
	}

	public String getDischargeType() {
		return dischargeType;
	}

	public void setDischargeType(String dischargeType) {
		this.dischargeType = dischargeType;
	}

	public int getEnlistFromMonth() {
		return enlistFromMonth;
	}

	public void setEnlistFromMonth(int enlistFromMonth) {
		this.enlistFromMonth = enlistFromMonth;
	}

	public int getEnlistFromYear() {
		return enlistFromYear;
	}

	public void setEnlistFromYear(int enlistFromYear) {
		this.enlistFromYear = enlistFromYear;
	}

	public int getEnlistToMonth() {
		return enlistToMonth;
	}

	public void setEnlistToMonth(int enlistToMonth) {
		this.enlistToMonth = enlistToMonth;
	}

	public int getEnlistToYear() {
		return enlistToYear;
	}

	public void setEnlistToYear(int enlistToYear) {
		this.enlistToYear = enlistToYear;
	}

	public String getMilitaryBranch() {
		return militaryBranch;
	}

	public void setMilitaryBranch(String militaryBranch) {
		this.militaryBranch = militaryBranch;
	}

	public String getWorkedFor() {
		return workedFor;
	}

	public void setWorkedFor(String workedFor) {
		this.workedFor = workedFor;
	}

	public String getWorkedForWhen() {
		return workedForWhen;
	}

	public void setWorkedForWhen(String workedForWhen) {
		this.workedForWhen = workedForWhen;
	}
}

	
