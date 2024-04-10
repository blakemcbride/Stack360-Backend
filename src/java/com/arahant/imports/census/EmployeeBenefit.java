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


package com.arahant.imports.census;

public class EmployeeBenefit {

	private String configId;
	private int policyStartDate;
	private int policyEndDate;
	private String changeReasonId;
	private String comment = "";
	private int coverageStartDate;
	private int coverageEndDate;
	private double amount;

	public EmployeeBenefit() {}

	/**
	 * Creates an employee benefit representation
	 *
	 * @param configId required benefit configuration id that must exist already
	 * @param policyStartDate required the policy start date
	 * @param changeReasonId required benefit change id that must exist already
	 */
	public EmployeeBenefit(String configId, int policyStartDate, String changeReasonId) {
		this.configId = configId;
//		System.out.println("Config Id is "+configId);
		if (configId == null || configId.equals(""))
			throw new RuntimeException("Can't have blank config id");
		this.policyStartDate = policyStartDate;
		this.changeReasonId = changeReasonId;
	}

	public EmployeeBenefit(String configId, int policyStartDate, double amt, String changeReasonId) {
		this.configId = configId;
//		System.out.println("Config Id is "+configId);
		if (configId == null || configId.equals(""))
			throw new RuntimeException("Can't have blank config id");
		this.policyStartDate = policyStartDate;
		this.changeReasonId = changeReasonId;
		amount = amt;
	}

	public int getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(int policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public int getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(int policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getChangeReasonId() {
		return changeReasonId;
	}

	public void setChangeReasonId(String changeReasonId) {
		this.changeReasonId = changeReasonId;
	}

	public int getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(int coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public int getCoverageStartDate() {
		return coverageStartDate;
	}

	public void setCoverageStartDate(int coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
