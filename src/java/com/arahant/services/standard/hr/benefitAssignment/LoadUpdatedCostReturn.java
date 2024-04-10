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

import com.arahant.services.TransmitReturnBase;


public class LoadUpdatedCostReturn extends TransmitReturnBase {

	private double employerPPPCost;
	private double employerMonthlyCost;
	private double employerAnnualCost;
	private double employeePPPCost;
	private double employeeMonthlyCost;
	private double employeeAnnualCost;
	private double benefitPPPAmount;
	private double benefitMonthlyAmount;
	private double benefitAnnualAmount;
	private int ppy;

	public double getEmployerPPPCost() {
		return employerPPPCost;
	}

	public void setEmployerPPPCost(double employerPPPCost) {
		this.employerPPPCost = employerPPPCost;
	}

	public double getEmployerMonthlyCost() {
		return employerMonthlyCost;
	}

	public void setEmployerMonthlyCost(double employerMonthlyCost) {
		this.employerMonthlyCost = employerMonthlyCost;
	}

	public double getEmployerAnnualCost() {
		return employerAnnualCost;
	}

	public void setEmployerAnnualCost(double employerAnnualCost) {
		this.employerAnnualCost = employerAnnualCost;
	}

	public double getEmployeePPPCost() {
		return employeePPPCost;
	}

	public void setEmployeePPPCost(double employeePPPCost) {
		this.employeePPPCost = employeePPPCost;
	}

	public double getEmployeeMonthlyCost() {
		return employeeMonthlyCost;
	}

	public void setEmployeeMonthlyCost(double employeeMonthlyCost) {
		this.employeeMonthlyCost = employeeMonthlyCost;
	}

	public double getEmployeeAnnualCost() {
		return employeeAnnualCost;
	}

	public void setEmployeeAnnualCost(double employeeAnnualCost) {
		this.employeeAnnualCost = employeeAnnualCost;
	}

	public double getBenefitPPPAmount() {
		return benefitPPPAmount;
	}

	public void setBenefitPPPAmount(double benefitPPPAmount) {
		this.benefitPPPAmount = benefitPPPAmount;
	}

	public double getBenefitMonthlyAmount() {
		return benefitMonthlyAmount;
	}

	public void setBenefitMonthlyAmount(double benefitMonthlyAmount) {
		this.benefitMonthlyAmount = benefitMonthlyAmount;
	}

	public double getBenefitAnnualAmount() {
		return benefitAnnualAmount;
	}

	public void setBenefitAnnualAmount(double benefitAnnualAmount) {
		this.benefitAnnualAmount = benefitAnnualAmount;
	}

	public int getPpy() {
		return ppy;
	}

	public void setPpy(int ppy) {
		this.ppy = ppy;
	}

}
