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
package com.arahant.services.standard.hr.employeePayroll;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BEmployee;
import com.arahant.annotation.Validation;
import com.arahant.business.BElectronicFundTransfer;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SavePayrollInput extends TransmitInputBase {

	void setData(BEmployee bc)
	{
		
		bc.setPersonId(personId);
		bc.setMaritalStatus(maritalStatus);
		bc.setLocalTaxCode(localTaxCode);
		bc.setEarnedIncomeCreditStatus(earnedIncomeCreditStatus);
		bc.setAddFederalIncomeTaxType(addFederalIncomeTaxType);
		bc.setAddStateIncomeTaxType(addStateIncomeTaxType);
		bc.setAddLocalIncomeTaxType(addLocalIncomeTaxType);
		bc.setAddStateDisabilityTaxType(addStateDisabilityTaxType);
		bc.setTaxState(taxState);
		bc.setUnemploymentState(unemploymentState);
		bc.setPayPeriodsPerYear(payPeriodsPerYear);
		bc.setExpectedHoursPerPayPeriod(expectedHoursPerPayPeriod);
		bc.setAddFederalIncomeTaxAmount(addFederalIncomeTaxAmount);
		bc.setAddStateIncomeTaxAmount(addStateIncomeTaxAmount);
		bc.setAddLocalIncomeTaxAmount(addLocalIncomeTaxAmount);
		bc.setAddStateDisabilityTaxAmount(addStateDisabilityTaxAmount);
		bc.setFederalExemptions(federalExemptions);
		bc.setStateExemptions(stateExemptions);
		bc.setFederalExtraWithheld(federalExtraWithheld);
		bc.setStateExtraWithheld(stateExtraWithheld);
		bc.setExempt(exempt);
		
		List<String> eftIds=new LinkedList<String>();

		//need to spin and set sequences to prevent collisions

		short seq=100;

		for (BElectronicFundTransfer beft : BElectronicFundTransfer.getEFTs(bc))
		{
			beft.setSeq(seq++);
			beft.update();
			ArahantSession.getHSU().flush();
		}

		seq=0;
		
		double percent=0;
		boolean check=false;
		
		for (SavePayrollInputItem i : getItem())
		{
			eftIds.add(i.saveData(bc,seq++));
			
			if (i.getAccountType().equals("P"))
			{
				percent+=i.getAmount();
				check=true;
			}
		}

		if (check && Math.round(percent)!=100)
			throw new ArahantException("Percentages must add up to 100.");

		BElectronicFundTransfer.deleteNotIn(bc,eftIds);
		bc.setW4Status(w4StatusId);
		bc.setPayrollBankAccountId(bankAccountId);
	}
	
	@Validation (table="person",column="person_id",required=true)
	private String personId;
	@Validation (table="employee",column="marital_status",required=false)
	private String maritalStatus;
	@Validation (table="employee",column="local_tax_code",required=false)
	private String localTaxCode;
	@Validation (table="employee",column="earned_income_credit_status",required=false)
	private String earnedIncomeCreditStatus;
	@Validation (table="employee",column="add_federal_income_tax_type",required=false)
	private String addFederalIncomeTaxType;
	@Validation (table="employee",column="add_state_income_tax_type",required=false)
	private String addStateIncomeTaxType;
	@Validation (table="employee",column="add_local_income_tax_type",required=false)
	private String addLocalIncomeTaxType;
	@Validation (table="employee",column="add_state_disability_tax_type",required=false)
	private String addStateDisabilityTaxType;
	@Validation (table="employee",column="payroll_bank_code",required=false)
	private String payrollBankCode;
	@Validation (table="employee",column="tax_state",required=false)
	private String taxState;
	@Validation (table="employee",column="unemployement_state",required=false)
	private String unemploymentState;
	@Validation (min=0,max=366,required=false)
	private int payPeriodsPerYear;
	@Validation (min=0,max=2080,required=false)
	private int expectedHoursPerPayPeriod;
	@Validation (min=0,max=100000,required=false)
	private double addFederalIncomeTaxAmount;
	@Validation (min=0,max=100000,required=false)
	private double addStateIncomeTaxAmount;
	@Validation (min=0,max=100000,required=false)
	private double addLocalIncomeTaxAmount;
	@Validation (min=0,max=100000,required=false)
	private double addStateDisabilityTaxAmount;
	@Validation (min=0,max=100,required=false)
	private int federalExemptions;
	@Validation (min=0,max=100,required=false)
	private int stateExemptions;
	@Validation (min=0,max=100000,required=false)
	private double federalExtraWithheld;
	@Validation (min=0,max=100000,required=false)
	private double stateExtraWithheld;
	@Validation (required=false)
	private boolean exempt;
	@Validation (required=false)
	private SavePayrollInputItem []item;
	@Validation (required=false, min=0, max=16)
	private String w4StatusId;
	@Validation (required=false)
	private String bankAccountId;
	
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getMaritalStatus()
	{
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus)
	{
		this.maritalStatus=maritalStatus;
	}
	public String getLocalTaxCode()
	{
		return localTaxCode;
	}
	public void setLocalTaxCode(String localTaxCode)
	{
		this.localTaxCode=localTaxCode;
	}
	public String getEarnedIncomeCreditStatus()
	{
		return earnedIncomeCreditStatus;
	}
	public void setEarnedIncomeCreditStatus(String earnedIncomeCreditStatus)
	{
		this.earnedIncomeCreditStatus=earnedIncomeCreditStatus;
	}
	public String getAddFederalIncomeTaxType()
	{
		return addFederalIncomeTaxType;
	}
	public void setAddFederalIncomeTaxType(String addFederalIncomeTaxType)
	{
		this.addFederalIncomeTaxType=addFederalIncomeTaxType;
	}
	public String getAddStateIncomeTaxType()
	{
		return addStateIncomeTaxType;
	}
	public void setAddStateIncomeTaxType(String addStateIncomeTaxType)
	{
		this.addStateIncomeTaxType=addStateIncomeTaxType;
	}
	public String getAddLocalIncomeTaxType()
	{
		return addLocalIncomeTaxType;
	}
	public void setAddLocalIncomeTaxType(String addLocalIncomeTaxType)
	{
		this.addLocalIncomeTaxType=addLocalIncomeTaxType;
	}
	public String getAddStateDisabilityTaxType()
	{
		return addStateDisabilityTaxType;
	}
	public void setAddStateDisabilityTaxType(String addStateDisabilityTaxType)
	{
		this.addStateDisabilityTaxType=addStateDisabilityTaxType;
	}
	public String getPayrollBankCode()
	{
		return payrollBankCode;
	}
	public void setPayrollBankCode(String payrollBankCode)
	{
		this.payrollBankCode=payrollBankCode;
	}
	public String getTaxState()
	{
		return taxState;
	}
	public void setTaxState(String taxState)
	{
		this.taxState=taxState;
	}
	public String getUnemploymentState()
	{
		return unemploymentState;
	}
	public void setUnemploymentState(String unemploymentState)
	{
		this.unemploymentState=unemploymentState;
	}
	public int getPayPeriodsPerYear()
	{
		return payPeriodsPerYear;
	}
	public void setPayPeriodsPerYear(int payPeriodsPerYear)
	{
		this.payPeriodsPerYear=payPeriodsPerYear;
	}
	public int getExpectedHoursPerPayPeriod()
	{
		return expectedHoursPerPayPeriod;
	}
	public void setExpectedHoursPerPayPeriod(int expectedHoursPerPayPeriod)
	{
		this.expectedHoursPerPayPeriod=expectedHoursPerPayPeriod;
	}
	public double getAddFederalIncomeTaxAmount()
	{
		return addFederalIncomeTaxAmount;
	}
	public void setAddFederalIncomeTaxAmount(double addFederalIncomeTaxAmount)
	{
		this.addFederalIncomeTaxAmount=addFederalIncomeTaxAmount;
	}
	public double getAddStateIncomeTaxAmount()
	{
		return addStateIncomeTaxAmount;
	}
	public void setAddStateIncomeTaxAmount(double addStateIncomeTaxAmount)
	{
		this.addStateIncomeTaxAmount=addStateIncomeTaxAmount;
	}
	public double getAddLocalIncomeTaxAmount()
	{
		return addLocalIncomeTaxAmount;
	}
	public void setAddLocalIncomeTaxAmount(double addLocalIncomeTaxAmount)
	{
		this.addLocalIncomeTaxAmount=addLocalIncomeTaxAmount;
	}
	public double getAddStateDisabilityTaxAmount()
	{
		return addStateDisabilityTaxAmount;
	}
	public void setAddStateDisabilityTaxAmount(double addStateDisabilityTaxAmount)
	{
		this.addStateDisabilityTaxAmount=addStateDisabilityTaxAmount;
	}
	public int getFederalExemptions()
	{
		return federalExemptions;
	}
	public void setFederalExemptions(int federalExemptions)
	{
		this.federalExemptions=federalExemptions;
	}
	public int getStateExemptions()
	{
		return stateExemptions;
	}
	public void setStateExemptions(int stateExemptions)
	{
		this.stateExemptions=stateExemptions;
	}
	public double getFederalExtraWithheld()
	{
		return federalExtraWithheld;
	}
	public void setFederalExtraWithheld(double federalExtraWithheld)
	{
		this.federalExtraWithheld=federalExtraWithheld;
	}
	public double getStateExtraWithheld()
	{
		return stateExtraWithheld;
	}
	public void setStateExtraWithheld(double stateExtraWithheld)
	{
		this.stateExtraWithheld=stateExtraWithheld;
	}
	public boolean getExempt()
	{
		return exempt;
	}
	public void setExempt(boolean exempt)
	{
		this.exempt=exempt;
	}

	public SavePayrollInputItem[] getItem() {
		if (item==null)
			item=new SavePayrollInputItem[0];
		return item;
	}

	public void setItem(SavePayrollInputItem[] item) {
		this.item = item;
	}

	public String getW4StatusId() {
		return w4StatusId;
	}

	public void setW4StatusId(String w4StatusId) {
		this.w4StatusId = w4StatusId;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	
}

	
