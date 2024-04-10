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



package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.annotation.Validation;
import com.arahant.beans.HrBeneficiary;
import com.arahant.business.BHRBeneficiary;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BHRPhysician;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import java.util.HashMap;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewAssignedBenefitConfigInput extends TransmitInputBase {
	
	@Validation (required=false)
	private boolean warningsAccepted;
	@Validation (required=false)
	private double amountOverrideAnnual;
	@Validation (required=false)
	private String configId;
	@Validation (required=false)
	private String personId;
	/**
	 * Policy end date
	 */
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int endDate;
	@Validation (required=false)
	private String insuranceId;
	@Validation (required=false)
	private NewAssignedBenefitConfigInputItem []item;
	/**
	 * Policy start date
	 */
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int startDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int lastCoverageChangeDate;
	@Validation (required=false)
	private NewAssignedBenefitConfigInputPhysician[] physician;
	@Validation (required=false)
	private NewAssignedBenefitConfigInputBeneficiary []primaryBeneficiaries;
	@Validation (required=false)
	private NewAssignedBenefitConfigInputBeneficiary []contingentBeneficiaries;
	@Validation (required=false)
	private boolean usingCOBRA;
	// only used when assigning benefit to non-employee dependent and unable to determine which sponsor is sponsoring
	// the dependent (does not have benefit of same category currently assigned and has multiple sponsors in system)
	@Validation (required=false)
	private String sponsoringEmployeeId;
	@Validation (required=true)
	private String benefitChangeReasonId;
	@Validation (type="date",required=false)
	private int acceptedDateCOBRA;
	@Validation (min=0,max=36,required=false)
	private int maxMonthsCOBRA;
	@Validation (min=0,max=1,required=false)
	private String amountPaidType;
	@Validation (required=false)
	private boolean useAmountOverride;
	

	void setData(final BHRBenefitJoin policyBenefitJoin) throws ArahantException
	{
		policyBenefitJoin.setBenefitConfigId(configId);
		policyBenefitJoin.setPayingPersonId(personId);
		policyBenefitJoin.setCoveredPersonId(personId);
		policyBenefitJoin.setPolicyEndDate(endDate);
		policyBenefitJoin.setInsuranceId(insuranceId);
		policyBenefitJoin.setPolicyStartDate(startDate);
		policyBenefitJoin.setCoverageChangeDate(lastCoverageChangeDate);
		policyBenefitJoin.setUsingCOBRA(usingCOBRA);
		policyBenefitJoin.setChangeReason(benefitChangeReasonId);
		policyBenefitJoin.setAcceptedDateCOBRA(acceptedDateCOBRA);
		policyBenefitJoin.setMaxMonthsCOBRA(maxMonthsCOBRA);
		policyBenefitJoin.setAmountPaidType("F");

		useAmountOverride = false;
		
		if (useAmountOverride)
			policyBenefitJoin.setAmountPaid(amountOverrideAnnual); //must come after set of paying and covered person
		else
			policyBenefitJoin.setAmountPaid(0);
		policyBenefitJoin.setUseAmountOverride(useAmountOverride);
		
		// if we were sent up a sponsoring employee id, this person is a non-employee dependent
		// getting a new benefit and we need to fill out the specific dependent relationship
		if (this.sponsoringEmployeeId!=null && this.sponsoringEmployeeId.length() > 0) 
		{
			BHREmplDependent emplDependent = new BHREmplDependent(this.sponsoringEmployeeId, this.personId);
			policyBenefitJoin.setRelationship(emplDependent.getEmplDependent());
		}

		HashMap<String, BHRBenefitJoin> depJoins = new HashMap<String, BHRBenefitJoin>();
		// spin through all the passed up people
		for (NewAssignedBenefitConfigInputItem element : getItem()) 
		{	
			// this is the policy owner
			if (element.getPersonId().equals(personId)) 
			{
				policyBenefitJoin.setCoverageStartDate(element.getStartDate());
				policyBenefitJoin.setCoverageEndDate(element.getEndDate());
				policyBenefitJoin.setOriginalCoverageDate(element.getOriginalCoverageDate());
				policyBenefitJoin.setAmountCovered(element.getAmountCovered());
				policyBenefitJoin.setComments(element.getComments());
				policyBenefitJoin.setOtherInsurance(element.getOtherInsurance());
				policyBenefitJoin.setOtherInsuanceIsPrimary(element.getOtherInsurancePrimary());
				depJoins.put(element.getPersonId(), policyBenefitJoin);
				continue;
			}
			
			// save a dependent of the policy owner
			depJoins.put(element.getPersonId(), policyBenefitJoin.saveDependentBenefitAssignment(new BHREmplDependent(personId,element.getPersonId()), 
					element.getStartDate(),
					element.getEndDate(),
					element.getOriginalCoverageDate(),
					element.getAmountCovered(),
					usingCOBRA,
					startDate,
					endDate,
					lastCoverageChangeDate,
					element.getComments(),element.getOtherInsurance(),element.getOtherInsurancePrimary()));
		}
		
		policyBenefitJoin.deleteBeneficiaries();
		policyBenefitJoin.insert();
		
		for (NewAssignedBenefitConfigInputBeneficiary bene : getPrimaryBeneficiaries())
		{
			BHRBeneficiary b=new BHRBeneficiary();
			b.create();
			b.setAssociatedBenefit(policyBenefitJoin);
			b.setPercentage(bene.getPercentage());
			b.setRelationship(bene.getRelationship());
			b.setBeneficiary(bene.getBeneficiary());
			b.setAddress(bene.getAddress());
			b.setBeneficiaryType(HrBeneficiary.PRIMARY);
			b.setSsn(bene.getSsn());
			b.setDob(bene.getDob());
			b.insert();
		}
		
		for (NewAssignedBenefitConfigInputBeneficiary bene : getContingentBeneficiaries())
		{
			BHRBeneficiary b=new BHRBeneficiary();
			b.create();
			b.setAssociatedBenefit(policyBenefitJoin);
			b.setPercentage(bene.getPercentage());
			b.setRelationship(bene.getRelationship());
			b.setBeneficiary(bene.getBeneficiary());
			b.setAddress(bene.getAddress());
			b.setBeneficiaryType(HrBeneficiary.CONTINGENT);
			b.setSsn(bene.getSsn());
			b.setDob(bene.getDob());
			b.insert();
		}

		for(NewAssignedBenefitConfigInputPhysician phys : getPhysician()) {
			BHRPhysician b = new BHRPhysician();

			b.create();
			b.setBenefitJoin(depJoins.get(phys.getPersonId()).getBean());
			b.setPhysicianName(phys.getName());
			b.setPhysicianCode(phys.getCode());
			b.setAddress(phys.getAddress());
			b.setChangeReason(phys.getChangeReason());
			b.setAnnualVisit(phys.getAnnualVisit());
			b.setChangeDate(phys.getChangeDate());
			b.insert();
		}
	}
	

	public String getBenefitChangeReasonId() {
		return benefitChangeReasonId;
	}

	public void setBenefitChangeReasonId(String benefitChangeReasonId) {
		this.benefitChangeReasonId = benefitChangeReasonId;
	}

	public int getAcceptedDateCOBRA() {
		return acceptedDateCOBRA;
	}

	public void setAcceptedDateCOBRA(int acceptedDateCOBRA) {
		this.acceptedDateCOBRA = acceptedDateCOBRA;
	}

	public int getMaxMonthsCOBRA() {
		return maxMonthsCOBRA;
	}

	public void setMaxMonthsCOBRA(int maxMonthsCOBRA) {
		this.maxMonthsCOBRA = maxMonthsCOBRA;
	}

	/**
	 * @return Returns the amountOverrideAnnual.
	 */
	public double getAmountOverrideAnnual() {
		return amountOverrideAnnual;
	}
	/**
	 * @param amountOverrideAnnual The amountOverrideAnnual to set.
	 */
	public void setAmountOverrideAnnual(final double amountPaid) {
		this.amountOverrideAnnual = amountPaid;
	}
	/**
	 * @return Returns the usingCOBRA.
	 */
	public boolean getUsingCOBRA() {
		return usingCOBRA;
	}
	/**
	 * @param usingCOBRA The usingCOBRA to set.
	 */
	public void setUsingCOBRA(final boolean usingCOBRA) {
		this.usingCOBRA = usingCOBRA;
	}
	/**
	 * @return Returns the configId.
	 */
	public String getConfigId() {
		return configId;
	}
	/**
	 * @param configId The configId to set.
	 */
	public void setConfigId(final String configId) {
		this.configId = configId;
	}
	/**
	 * @return Returns the employeeId.
	 */
	public String getPersonId() {
		return personId;
	}
	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setPersonId(final String employeeId) {
		this.personId = employeeId;
	}
	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}

	public int getLastCoverageChangeDate() {
		return lastCoverageChangeDate;
	}

	public void setLastCoverageChangeDate(int lastCoverageChangeDate) {
		this.lastCoverageChangeDate = lastCoverageChangeDate;
	}

	/**
	 * @return Returns the insuranceId.
	 */
	public String getInsuranceId() {
		return insuranceId;
	}
	/**
	 * @param insuranceId The insuranceId to set.
	 */
	public void setInsuranceId(final String insuranceId) {
		this.insuranceId = insuranceId;
	}

	/**
	 * @return Returns the item.
	 */
	public NewAssignedBenefitConfigInputItem[] getItem() {
        if (item==null)
            return new NewAssignedBenefitConfigInputItem[0];
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final NewAssignedBenefitConfigInputItem[] item) {
		this.item = item;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * @return Returns the warningsAccepted.
	 */
	public boolean isWarningsAccepted() {
		return warningsAccepted;
	}
	/**
	 * @param warningsAccepted The warningsAccepted to set.
	 */
	public void setWarningsAccepted(boolean warningsAccepted) {
		this.warningsAccepted = warningsAccepted;
	}
	/**
	 * @return Returns the sponsoringEmployeeId.
	 */
	public String getSponsoringEmployeeId() {
		return sponsoringEmployeeId;
	}
	/**
	 * @param sponsoringEmployeeId The sponsoringEmployeeId to set.
	 */
	public void setSponsoringEmployeeId(final String sponsoringEmployeeId) {
		this.sponsoringEmployeeId = sponsoringEmployeeId;
	}

	public String getAmountPaidType() {
		return amountPaidType;
	}

	public void setAmountPaidType(String amountPaidType) {
		this.amountPaidType = amountPaidType;
	}

	public NewAssignedBenefitConfigInputBeneficiary[] getContingentBeneficiaries() {
		if (contingentBeneficiaries==null)
			contingentBeneficiaries=new NewAssignedBenefitConfigInputBeneficiary[0];
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(NewAssignedBenefitConfigInputBeneficiary[] contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	public NewAssignedBenefitConfigInputBeneficiary[] getPrimaryBeneficiaries() {
		if (primaryBeneficiaries==null)
			primaryBeneficiaries=new NewAssignedBenefitConfigInputBeneficiary[0];
		return primaryBeneficiaries;
	}

	public void setPrimaryBeneficiaries(NewAssignedBenefitConfigInputBeneficiary[] primaryBeneficiaries) {
		this.primaryBeneficiaries = primaryBeneficiaries;
	}

	public boolean isUseAmountOverride() {
		return useAmountOverride;
	}

	public void setUseAmountOverride(boolean useAmountOverride) {
		this.useAmountOverride = useAmountOverride;
	}

	public NewAssignedBenefitConfigInputPhysician[] getPhysician() {
		if(physician == null)
			physician = new NewAssignedBenefitConfigInputPhysician[0];
		return physician;
	}

	public void setPhysician(NewAssignedBenefitConfigInputPhysician[] physician) {
		this.physician = physician;
	}
	
	
	
	
}

	
