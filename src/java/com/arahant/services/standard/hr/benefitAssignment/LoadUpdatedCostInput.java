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
 */
package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class LoadUpdatedCostInput extends TransmitInputBase {

	@Validation(required = true)
	private String configId;
	@Validation(required = true)
	private double amount;
	@Validation(required = true, type = "date")
	private int policyStartDate;
	@Validation(required = false, type = "date")
	private int lastCoverageChangeDate;
	@Validation(required = false, type = "date")
	private int policyEndDate;
	@Validation(required = true)
	private String personId;
	@Validation(required = true)
	private boolean usingCobra;
	@Validation(required = false)
	private LoadUpdatedCostInputItem[] item;
	@Validation(required = false)
	private String bcrId;
	@Validation(required = true)
	private String policyBenefitJoinId;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(int policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(int policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public int getLastCoverageChangeDate() {
		return lastCoverageChangeDate;
	}

	public void setLastCoverageChangeDate(int lastCoverageChangeDate) {
		this.lastCoverageChangeDate = lastCoverageChangeDate;
	}

	public boolean getUsingCobra() {
		return usingCobra;
	}

	public void setUsingCobra(boolean usingCobra) {
		this.usingCobra = usingCobra;
	}

	public LoadUpdatedCostInputItem[] getItem() {
		return item;
	}

	public void setItem(LoadUpdatedCostInputItem[] item) {
		this.item = item;
	}

	public String getBcrId() {
		return bcrId;
	}

	public void setBcrId(String bcrId) {
		this.bcrId = bcrId;
	}

	public String getPolicyBenefitJoinId() {
		return policyBenefitJoinId;
	}

	public void setPolicyBenefitJoinId(String policyBenefitJoinId) {
		this.policyBenefitJoinId = policyBenefitJoinId;
	}
	
}
