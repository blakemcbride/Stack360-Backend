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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;

public class SavePersonBackgroundInput extends TransmitInputBase {

	void setData(BPerson bc)
	{
		if (!isEmpty(militaryBranch))
		{
			bc.setMilitaryBranch(militaryBranch.charAt(0));
			bc.setMilitaryStartDate((enlistFromYear * 100) + enlistFromMonth);
			bc.setMilitaryEndDate((enlistToYear * 100) + enlistToMonth);
			bc.setMilitaryRank(dischargeRank);
		}
		else
		{
			bc.setMilitaryBranch('U');
			bc.setMilitaryStartDate(0);
			bc.setMilitaryEndDate(0);
			bc.setMilitaryRank("");
		}

		if (!isEmpty(dischargeType))
		{
			bc.setMilitaryDischargeType(dischargeType.charAt(0));
			bc.setMilitaryDischargeExplain(dischargeExplain);
		}
		else
		{
			bc.setMilitaryDischargeType('U');
			bc.setMilitaryDischargeExplain("");
		}

		if (!isEmpty(convicted))
		{
			bc.setConvictedOfCrime(convicted.charAt(0));
			bc.setConvictedOfWhat(convictedDescription);
		}
		else
		{
			bc.setConvictedOfCrime('U');
			bc.setConvictedOfWhat("");
		}

		bc.setWorkedForCompanyBefore(workedFor.charAt(0));
		bc.setWorkedForCompanyWhen(workedForWhen);
	}
	
	@Validation (table="employee",column="person_id",required=true)
	private String personId;
	@Validation (table="person", column="military_branch", required=false)
	private String militaryBranch;
	@Validation (required=false)
	private int enlistFromMonth;
	@Validation (required=false)
	private int enlistToMonth;
	@Validation (required=false)
	private int enlistFromYear;
	@Validation (required=false)
	private int enlistToYear;
	@Validation (table="person", column="military_rank", required=false)
	private String dischargeRank;
	@Validation (table="person", column="military_discharge_type", required=false)
	private String dischargeType;
	@Validation (table="person", column="convicted_of_crime", required=false)
	private String convicted;
	@Validation (table="person", column="convicted_of_what", required=false)
	private String convictedDescription;
	@Validation (table="person", column="worked_for_company_before", required=true)
	private String workedFor;
	@Validation (table="person", column="worked_for_company_when", required=false)
	private String workedForWhen;
	@Validation (table="person", column="military_discharge_explain", required=false)
	private String dischargeExplain;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}

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

	
