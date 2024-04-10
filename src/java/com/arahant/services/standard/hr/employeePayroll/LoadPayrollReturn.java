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


package com.arahant.services.standard.hr.employeePayroll;

import com.arahant.business.BElectronicFundTransfer;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEmployee;


public class LoadPayrollReturn extends TransmitReturnBase {

    private String maritalStatus;
    private String localTaxCode;
    private String earnedIncomeCreditStatus;
    private String addFederalIncomeTaxType;
    private String addStateIncomeTaxType;
    private String addLocalIncomeTaxType;
    private String addStateDisabilityTaxType;
    private String taxState;
    private String unemploymentState;
    private String w4StatusId;
    private int payPeriodsPerYear;
    private double expectedHoursPerPayPeriod;
    private double addFederalIncomeTaxAmount;
    private double addStateIncomeTaxAmount;
    private double addLocalIncomeTaxAmount;
    private double addStateDisabilityTaxAmount;
    private int federalExemptions;
    private int stateExemptions;
    private double federalExtraWithheld;
    private double stateExtraWithheld;
    private boolean exempt;
    private LoadPayrollReturnItem[] item;
    private String bankAccountId;

    void setData(BEmployee bc) {
        maritalStatus = bc.getMaritalStatus();
        localTaxCode = bc.getLocalTaxCode();
        earnedIncomeCreditStatus = bc.getEarnedIncomeCreditStatus();
        addFederalIncomeTaxType = bc.getAddFederalIncomeTaxType();
        addStateIncomeTaxType = bc.getAddStateIncomeTaxType();
        addLocalIncomeTaxType = bc.getAddLocalIncomeTaxType();
        addStateDisabilityTaxType = bc.getAddStateDisabilityTaxType();
        bankAccountId = bc.getBankAccountId();
        taxState = bc.getTaxState();
        unemploymentState = bc.getUnemploymentState();
        w4StatusId = bc.getW4Status() + "";
        payPeriodsPerYear = bc.getPayPeriodsPerYear();
        expectedHoursPerPayPeriod = bc.getExpectedHoursPerPayPeriod();
        addFederalIncomeTaxAmount = bc.getAddFederalIncomeTaxAmount();
        addStateIncomeTaxAmount = bc.getAddStateIncomeTaxAmount();
        addLocalIncomeTaxAmount = bc.getAddLocalIncomeTaxAmount();
        addStateDisabilityTaxAmount = bc.getAddStateDisabilityTaxAmount();
        federalExemptions = bc.getFederalExemptions();
        stateExemptions = bc.getStateExemptions();
        federalExtraWithheld = bc.getFederalExtraWithheld();
        stateExtraWithheld = bc.getStateExtraWithheld();
        exempt = bc.getExempt();

        BElectronicFundTransfer[] eft = BElectronicFundTransfer.getEFTs(bc);

        item = new LoadPayrollReturnItem[eft.length];
        for (int loop = 0; loop < item.length; loop++)
            item[loop] = new LoadPayrollReturnItem(eft[loop]);
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getLocalTaxCode() {
        return localTaxCode;
    }

    public void setLocalTaxCode(String localTaxCode) {
        this.localTaxCode = localTaxCode;
    }

    public String getEarnedIncomeCreditStatus() {
        return earnedIncomeCreditStatus;
    }

    public void setEarnedIncomeCreditStatus(String earnedIncomeCreditStatus) {
        this.earnedIncomeCreditStatus = earnedIncomeCreditStatus;
    }

    public String getAddFederalIncomeTaxType() {
        return addFederalIncomeTaxType;
    }

    public void setAddFederalIncomeTaxType(String addFederalIncomeTaxType) {
        this.addFederalIncomeTaxType = addFederalIncomeTaxType;
    }

    public String getAddStateIncomeTaxType() {
        return addStateIncomeTaxType;
    }

    public void setAddStateIncomeTaxType(String addStateIncomeTaxType) {
        this.addStateIncomeTaxType = addStateIncomeTaxType;
    }

    public String getAddLocalIncomeTaxType() {
        return addLocalIncomeTaxType;
    }

    public void setAddLocalIncomeTaxType(String addLocalIncomeTaxType) {
        this.addLocalIncomeTaxType = addLocalIncomeTaxType;
    }

    public String getAddStateDisabilityTaxType() {
        return addStateDisabilityTaxType;
    }

    public void setAddStateDisabilityTaxType(String addStateDisabilityTaxType) {
        this.addStateDisabilityTaxType = addStateDisabilityTaxType;
    }

    public String getTaxState() {
        return taxState;
    }

    public void setTaxState(String taxState) {
        this.taxState = taxState;
    }

    public String getUnemploymentState() {
        return unemploymentState;
    }

    public void setUnemploymentState(String unemploymentState) {
        this.unemploymentState = unemploymentState;
    }

    public String getW4StatusId() {
        return w4StatusId;
    }

    public void setW4StatusId(String w4StatusId) {
        this.w4StatusId = w4StatusId;
    }

    public int getPayPeriodsPerYear() {
        return payPeriodsPerYear;
    }

    public void setPayPeriodsPerYear(int payPeriodsPerYear) {
        this.payPeriodsPerYear = payPeriodsPerYear;
    }

    public double getExpectedHoursPerPayPeriod() {
        return expectedHoursPerPayPeriod;
    }

    public void setExpectedHoursPerPayPeriod(double expectedHoursPerPayPeriod) {
        this.expectedHoursPerPayPeriod = expectedHoursPerPayPeriod;
    }

    public double getAddFederalIncomeTaxAmount() {
        return addFederalIncomeTaxAmount;
    }

    public void setAddFederalIncomeTaxAmount(double addFederalIncomeTaxAmount) {
        this.addFederalIncomeTaxAmount = addFederalIncomeTaxAmount;
    }

    public double getAddStateIncomeTaxAmount() {
        return addStateIncomeTaxAmount;
    }

    public void setAddStateIncomeTaxAmount(double addStateIncomeTaxAmount) {
        this.addStateIncomeTaxAmount = addStateIncomeTaxAmount;
    }

    public double getAddLocalIncomeTaxAmount() {
        return addLocalIncomeTaxAmount;
    }

    public void setAddLocalIncomeTaxAmount(double addLocalIncomeTaxAmount) {
        this.addLocalIncomeTaxAmount = addLocalIncomeTaxAmount;
    }

    public double getAddStateDisabilityTaxAmount() {
        return addStateDisabilityTaxAmount;
    }

    public void setAddStateDisabilityTaxAmount(double addStateDisabilityTaxAmount) {
        this.addStateDisabilityTaxAmount = addStateDisabilityTaxAmount;
    }

    public int getFederalExemptions() {
        return federalExemptions;
    }

    public void setFederalExemptions(int federalExemptions) {
        this.federalExemptions = federalExemptions;
    }

    public int getStateExemptions() {
        return stateExemptions;
    }

    public void setStateExemptions(int stateExemptions) {
        this.stateExemptions = stateExemptions;
    }

    public double getFederalExtraWithheld() {
        return federalExtraWithheld;
    }

    public void setFederalExtraWithheld(double federalExtraWithheld) {
        this.federalExtraWithheld = federalExtraWithheld;
    }

    public double getStateExtraWithheld() {
        return stateExtraWithheld;
    }

    public void setStateExtraWithheld(double stateExtraWithheld) {
        this.stateExtraWithheld = stateExtraWithheld;
    }

    public boolean getExempt() {
        return exempt;
    }

    public void setExempt(boolean exempt) {
        this.exempt = exempt;
    }

    public LoadPayrollReturnItem[] getItem() {
        if (item == null)
            item = new LoadPayrollReturnItem[0];
        return item;
    }

    public void setItem(LoadPayrollReturnItem[] item) {
        this.item = item;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

}

	
