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
import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveAssignedBenefitConfigInput extends TransmitInputBase {

	@Validation (required=false)
	private double amountOverrideAnnual;
	@Validation (required=false)
	private String personConfigId;
	/**
	 * Policy End Date (not coverage end date)
	 */
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int endDate;
	@Validation (required=false)
	private String insuranceId;
	@Validation (required=false)
	private SaveAssignedBenefitConfigInputItem item[];
	/**
	 * Policy Start Date (not coverage start date)
	 */
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int startDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int lastCoverageChangeDate;
	@Validation (required=false)
	private boolean warningsAccepted;
	@Validation (required=false)
	private boolean usingCOBRA;
	@Validation (required=true)
	private String benefitChangeReasonId;
	@Validation (type="date",required=false)
	private int acceptedDateCOBRA;
	@Validation (min=0,max=36,required=false)
	private int maxMonthsCOBRA;
	@Validation (min=0,max=1,required=false)
	private String amountPaidType;
	@Validation (required=false)
	private SaveAssignedBenefitConfigInputPhysician[] physician;
	@Validation (required=false)
	private SaveAssignedBenefitConfigInputBeneficiary []primaryBeneficiaries;
	@Validation (required=false)
	private SaveAssignedBenefitConfigInputBeneficiary []contingentBeneficiaries;
	@Validation (required=false)
	private boolean useAmountOverride;

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
	public double getAmountOverrideAnnual()
	{
		return amountOverrideAnnual;
	}
	public void setAmountOverrideAnnual(final double amountPaid)
	{
		this.amountOverrideAnnual=amountPaid;
	}
	public int getEndDate()
	{
		return endDate;
	}
	public void setEndDate(final int endDate)
	{
		this.endDate=endDate;
	}
	public String getInsuranceId()
	{
		return insuranceId;
	}
	public void setInsuranceId(final String insuranceId)
	{
		this.insuranceId=insuranceId;
	}
	
	/**
	 * @return Returns the item.
	 */
	public SaveAssignedBenefitConfigInputItem[] getItem() {
        if (item==null)
            return new SaveAssignedBenefitConfigInputItem[0];
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final SaveAssignedBenefitConfigInputItem[] item) {
		this.item = item;
	}
	
	public int getStartDate()
	{
		return startDate;
	}
	
	public void setStartDate(final int startDate)
	{
		this.startDate=startDate;
	}

	public int getLastCoverageChangeDate() {
		return lastCoverageChangeDate;
	}

	public void setLastCoverageChangeDate(int lastCoverageChangeDate) {
		this.lastCoverageChangeDate = lastCoverageChangeDate;
	}

	/**
	 * @param x
	 * @throws ArahantException 
	 */
	void setData(BHRBenefitJoin x) throws ArahantException {
		
//		if (useAmountOverride)
//			x.setAmountPaid(amountOverrideAnnual); //must come after set of paying and covered person
//		else
			x.setAmountPaid(0);
		x.setUseAmountOverride(false);
		
		x.setInsuranceId(insuranceId);
		x.setUsingCOBRA(usingCOBRA);
		x.setChangeReason(benefitChangeReasonId);
		
		HashMap<String, BHRBenefitJoin> depJoins = new HashMap<String, BHRBenefitJoin>();

        // spin through all the passed up people
		for (SaveAssignedBenefitConfigInputItem element : getItem()) 
		{
            double amt=element.getAmountCovered();
            int end=element.getEndDate();
            int start=element.getStartDate();
            String person=element.getPersonId();
            
            if (x.getPayingPersonId().equals(person))
            {
                x.setCoverageStartDate(start);
                x.setCoverageEndDate(end);
				x.setOriginalCoverageDate(element.getOriginalCoverageDate());
                x.setAmountCovered(amt);
				x.setComments(element.getComments());
				x.setOtherInsurance(element.getOtherInsurance());
				x.setOtherInsuanceIsPrimary(element.getOtherInsurancePrimary());
				depJoins.put(person, x);
            }
            else
            {
                depJoins.put(person, x.saveDependentBenefitAssignment(new BHREmplDependent(x.getPayingPersonId(), person), 
						start, 
						end, 
						element.getOriginalCoverageDate(),
						amt, 
						usingCOBRA, 
						startDate, 
						endDate,
						lastCoverageChangeDate,
						element.getComments(),element.getOtherInsurance(),element.getOtherInsurancePrimary()));
            }
        }
		
		//have to do this second or the above will not be able to change the right things
		x.setPolicyEndDate(endDate);
		x.setPolicyStartDate(startDate);
		x.setCoverageChangeDate(lastCoverageChangeDate);
		x.setMaxMonthsCOBRA(maxMonthsCOBRA);
		x.setAcceptedDateCOBRA(acceptedDateCOBRA);
		
		List <String> doneIds=new LinkedList<String>();
		
		for (SaveAssignedBenefitConfigInputBeneficiary bene : getPrimaryBeneficiaries())
		{
			if (bene.getBeneficiaryId()==null || bene.getBeneficiaryId().trim().equals(""))
			{
				BHRBeneficiary b=new BHRBeneficiary();
				doneIds.add(b.create());
				b.setAssociatedBenefit(x);
				b.setPercentage(bene.getPercentage());
				b.setRelationship(bene.getRelationship());
				b.setBeneficiary(bene.getBeneficiary());
				b.setAddress(bene.getAddress());
				b.setBeneficiaryType(HrBeneficiary.PRIMARY);
				b.setSsn(bene.getSsn());
				b.setDob(bene.getDob());
				b.insert();
			}
			else
			{
				doneIds.add(bene.getBeneficiaryId());
				BHRBeneficiary b=new BHRBeneficiary(bene.getBeneficiaryId());
				b.setAssociatedBenefit(x);
				b.setPercentage(bene.getPercentage());
				b.setRelationship(bene.getRelationship());
				b.setBeneficiary(bene.getBeneficiary());
				b.setAddress(bene.getAddress());
				b.setBeneficiaryType(HrBeneficiary.PRIMARY);
				b.setSsn(bene.getSsn());
				b.setDob(bene.getDob());
				b.update();
			}
		}
		
		for (SaveAssignedBenefitConfigInputBeneficiary bene : getContingentBeneficiaries())
		{
			if (bene.getBeneficiaryId()==null || bene.getBeneficiaryId().trim().equals(""))
			{
				BHRBeneficiary b=new BHRBeneficiary();
				doneIds.add(b.create());
				b.setAssociatedBenefit(x);
				b.setPercentage(bene.getPercentage());
				b.setRelationship(bene.getRelationship());
				b.setBeneficiary(bene.getBeneficiary());
				b.setAddress(bene.getAddress());
				b.setBeneficiaryType(HrBeneficiary.CONTINGENT);
				b.setSsn(bene.getSsn());
				b.setDob(bene.getDob());
				b.insert();
			}
			else
			{
				doneIds.add(bene.getBeneficiaryId());
				BHRBeneficiary b=new BHRBeneficiary(bene.getBeneficiaryId());
				b.setAssociatedBenefit(x);
				b.setPercentage(bene.getPercentage());
				b.setRelationship(bene.getRelationship());
				b.setBeneficiary(bene.getBeneficiary());
				b.setAddress(bene.getAddress());
				b.setBeneficiaryType(HrBeneficiary.CONTINGENT);
				b.setSsn(bene.getSsn());
				b.setDob(bene.getDob());
				b.update();
			}
		}

		List<String> donePhysIds = new ArrayList<String>();
		for(SaveAssignedBenefitConfigInputPhysician phys : getPhysician()) {
			if(StringUtils.isEmpty(phys.getPhysicianId())) {
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
				donePhysIds.add(b.getPhysicianId());
			}
			else {
				BHRPhysician b = new BHRPhysician(phys.getPhysicianId());
				b.setBenefitJoin(depJoins.get(phys.getPersonId()).getBean());
				b.setPhysicianName(phys.getName());
				b.setPhysicianCode(phys.getCode());
				b.setAddress(phys.getAddress());
				b.setChangeReason(phys.getChangeReason());
				b.setAnnualVisit(phys.getAnnualVisit());
				b.setChangeDate(phys.getChangeDate());
				b.update();
				donePhysIds.add(b.getPhysicianId());
			}
		}
		
		BHRBeneficiary.deleteNotIn(x,doneIds);
		BHRPhysician.deleteNotIn(x, donePhysIds);
		
		x.setAmountPaidType(amountPaidType);
	}
	/**
	 * @return Returns the employeeConfigId.
	 */
	public String getPersonConfigId() {
		return personConfigId;
	}
	/**
	 * @param employeeConfigId The employeeConfigId to set.
	 */
	public void setPersonConfigId(String employeeConfigId) {
		this.personConfigId = employeeConfigId;
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

	public String getAmountPaidType() {
		return amountPaidType;
	}

	public void setAmountPaidType(String amountPaidType) {
		this.amountPaidType = amountPaidType;
	}

	public SaveAssignedBenefitConfigInputBeneficiary[] getContingentBeneficiaries() {
		if (contingentBeneficiaries==null)
			contingentBeneficiaries=new SaveAssignedBenefitConfigInputBeneficiary[0];
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(SaveAssignedBenefitConfigInputBeneficiary[] contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	public SaveAssignedBenefitConfigInputBeneficiary[] getPrimaryBeneficiaries() {
		if (primaryBeneficiaries==null)
			primaryBeneficiaries=new SaveAssignedBenefitConfigInputBeneficiary[0];
		return primaryBeneficiaries;
	}

	public void setPrimaryBeneficiaries(SaveAssignedBenefitConfigInputBeneficiary[] primaryBeneficiaries) {
		this.primaryBeneficiaries = primaryBeneficiaries;
	}

	public boolean isUseAmountOverride() {
		return useAmountOverride;
	}

	public void setUseAmountOverride(boolean useAmountOverride) {
		this.useAmountOverride = useAmountOverride;
	}

	public SaveAssignedBenefitConfigInputPhysician[] getPhysician() {
		if(physician == null)
			physician = new SaveAssignedBenefitConfigInputPhysician[0];
		return physician;
	}

	public void setPhysician(SaveAssignedBenefitConfigInputPhysician[] physician) {
		this.physician = physician;
	}
	
}

	
