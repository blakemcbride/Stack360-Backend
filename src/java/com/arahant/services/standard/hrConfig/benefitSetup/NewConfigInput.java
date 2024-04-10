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
package com.arahant.services.standard.hrConfig.benefitSetup;
import com.arahant.annotation.Validation;

import com.arahant.business.BHRBenefitConfig;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewConfigInput extends TransmitInputBase {

	void setData(final BHRBenefitConfig bc)
	{
		
		bc.setBenefitId(benefitId);
		bc.setName(name);
		bc.setCoversEmployee(coversEmployee);
		bc.setSpouseNonEmployee(coversNonEmployeeSpouse);
		bc.setCoversChildren(coversChildren);
		bc.setSpouseNonEmpOrChildren(coversNonEmployeeSpouseOrChildren);
		bc.setMaxDependents(maxDependents);
		bc.setAdditionalInfo(additionalInfo);
		bc.setStartDate(activeDate);
		bc.setEndDate(inactiveDate);
		bc.setAutoAssign(autoAssign);
		bc.setSpouseDeclinesExternalCoverage(spouseDeclinesExternalCoverage);
		bc.setSpouseEmployee(coversEmployeeSpouse);
		bc.setCoversEmployeeSpouseOrChildren(coversEmployeeSpouseOrChildren);
		bc.setEmployerCost(employerCost);
		bc.setMaxAmount(maxAmount);
		bc.setIncludeInBilling(includeInBilling);

		bc.setBenefitClasses(getIds());
		
	}
	
	@Validation (required=false)
	private String benefitId;
	@Validation (required=true, table="hr_benefit_config",column="config_name")
	private String name;
	@Validation (required=false)
	private boolean coversEmployee;
	@Validation (required=false)
	private boolean coversNonEmployeeSpouse;
	@Validation (required=false)
	private boolean coversChildren;
	@Validation (required=false)
	private boolean coversNonEmployeeSpouseOrChildren;
	@Validation (required=false)
	private int maxDependents;
	@Validation (required=false)
	private String additionalInfo;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int activeDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int inactiveDate;
	@Validation (required=false)
	private boolean autoAssign;
	@Validation (required=false)
	private boolean spouseDeclinesExternalCoverage;
	@Validation (required=false)
	private boolean coversEmployeeSpouse;
	@Validation (required=false)
	private boolean coversEmployeeSpouseOrChildren;
	@Validation (min=0,required=false)
	private double employerCost;
	@Validation (min=0,required=false)
	private double maxAmount;
	@Validation (required=false)
	private boolean includeInBilling;
	@Validation (required=false)
	private String[] ids;

	public String[] getIds() {
		if (ids==null)
			ids=new String[0];
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}



	public boolean getIncludeInBilling() {
		return includeInBilling;
	}

	public void setIncludeInBilling(boolean includeInBilling) {
		this.includeInBilling = includeInBilling;
	}



	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(final String name)
	{
		this.name=name;
	}
	public boolean getCoversEmployee()
	{
		return coversEmployee;
	}
	public void setCoversEmployee(final boolean coversEmployee)
	{
		this.coversEmployee=coversEmployee;
	}
	public boolean getCoversNonEmployeeSpouse()
	{
		return coversNonEmployeeSpouse;
	}
	public void setCoversNonEmployeeSpouse(final boolean coversSpouse)
	{
		this.coversNonEmployeeSpouse=coversSpouse;
	}
	public boolean getCoversChildren()
	{
		return coversChildren;
	}
	public void setCoversChildren(final boolean coversChildren)
	{
		this.coversChildren=coversChildren;
	}
	public boolean getCoversNonEmployeeSpouseOrChildren()
	{
		return coversNonEmployeeSpouseOrChildren;
	}
	public void setCoversNonEmployeeSpouseOrChildren(final boolean coversSpouseOrChildren)
	{
		this.coversNonEmployeeSpouseOrChildren=coversSpouseOrChildren;
	}
	public int getMaxDependents()
	{
		return maxDependents;
	}
	public void setMaxDependents(final int maxDependents)
	{
		this.maxDependents=maxDependents;
	}


	public String getAdditionalInfo()
	{
		return additionalInfo;
	}
	public void setAdditionalInfo(final String additionalInfo)
	{
		this.additionalInfo=additionalInfo;
	}

	/**
	 * @return Returns the endDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setInactiveDate(final int endDate) {
		this.inactiveDate = endDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getActiveDate() {
		return activeDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setActiveDate(final int startDate) {
		this.activeDate = startDate;
	}
	public boolean getAutoAssign()
	{
		return autoAssign;
	}
	public void setAutoAssign(final boolean autoAssign)
	{
		this.autoAssign=autoAssign;
	}
	public boolean getSpouseDeclinesExternalCoverage()
	{
		return spouseDeclinesExternalCoverage;
	}
	public void setSpouseDeclinesExternalCoverage(final boolean spouseDeclinesExternalCoverage)
	{
		this.spouseDeclinesExternalCoverage=spouseDeclinesExternalCoverage;
	}
	public boolean getCoversEmployeeSpouse()
	{
		return coversEmployeeSpouse;
	}
	public void setCoversEmployeeSpouse(final boolean spouseIsEmployee)
	{
		this.coversEmployeeSpouse=spouseIsEmployee;
	}

	/**
	 * @return Returns the coversEmployeeSpouseOrChildren.
	 */
	public boolean isCoversEmployeeSpouseOrChildren() {
		return coversEmployeeSpouseOrChildren;
	}
	/**
	 * @param coversEmployeeSpouseOrChildren The coversEmployeeSpouseOrChildren to set.
	 */
	public void setCoversEmployeeSpouseOrChildren(
			final boolean coversEmployeeSpouseOrChildren) {
		this.coversEmployeeSpouseOrChildren = coversEmployeeSpouseOrChildren;
	}
	/**
	 * @return Returns the employerCost.
	 */
	public double getEmployerCost() {
		return employerCost;
	}
	/**
	 * @param employerCost The employerCost to set.
	 */
	public void setEmployerCost(double employerCost) {
		this.employerCost = employerCost;
	}
	/**
	 * @return Returns the maxAmount.
	 */
	public double getMaxAmount() {
		return maxAmount;
	}
	/**
	 * @param maxAmount The maxAmount to set.
	 */
	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

}

	
