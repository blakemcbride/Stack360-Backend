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


package com.arahant.services.standard.hr.benefitConfigAdvancedCost;

import com.arahant.business.BBenefitClass;
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.services.TransmitReturnBase;

public class LoadConfigReturn extends TransmitReturnBase {

	private String name;
	private boolean coversEmployee;
	private boolean coversNonEmployeeSpouse;
	private boolean coversChildren;
	private boolean coversNonEmployeeSpouseOrChildren;
	private int maxDependents;
	private String groupId;
	private String additionalInfo;
	private boolean autoAssign;
	private boolean spouseDeclinesExternalCoverage;
	private boolean coversEmployeeSpouse;
	private int activeDate, inactiveDate;
	private boolean coversEmployeeSpouseOrChildren;
	private boolean includeInBilling;
	private LoadConfigReturnItem benefitClass[];
	private LoadConfigReturnCostItem configCost[];

	void setData(final BHRBenefitConfig bc) {

		name = bc.getName();
		coversEmployee = bc.getCoversEmployee();
		coversNonEmployeeSpouse = bc.getSpouseNonEmployee();
		coversChildren = bc.getCoversChildren();
		coversNonEmployeeSpouseOrChildren = bc.getSpouseNonEmpOrChildren();
		maxDependents = bc.getMaxChildren();

		groupId = bc.getGroupId();
		additionalInfo = bc.getAdditionalInfo();

		autoAssign = bc.getAutoAssign();
		spouseDeclinesExternalCoverage = bc.getSpouseDeclinesExternalCoverage();
		coversEmployeeSpouse = bc.getCoversEmployeeSpouse();

		activeDate = bc.getStartDate();
		inactiveDate = bc.getEndDate();
		coversEmployeeSpouseOrChildren = bc.getCoversEmployeeSpouseOrChildren();

		includeInBilling = bc.getIncludeInBilling();

		BBenefitClass[] bcl = bc.getBenefitClasses();

		benefitClass = new LoadConfigReturnItem[bcl.length];

		for (int loop = 0; loop < bcl.length; loop++)
			benefitClass[loop] = new LoadConfigReturnItem(bcl[loop]);

		//TODO: set config costs
		BBenefitConfigCost[] bcost = bc.getBenefitCosts();

		configCost = new LoadConfigReturnCostItem[bcost.length];

		for (int loop = 0; loop < configCost.length; loop++)
			configCost[loop] = new LoadConfigReturnCostItem(bcost[loop]);
	}

	public LoadConfigReturnCostItem[] getConfigCost() {
		return configCost;
	}

	public void setConfigCost(LoadConfigReturnCostItem[] configCost) {
		this.configCost = configCost;
	}

	public boolean getIncludeInBilling() {
		return includeInBilling;
	}

	public void setIncludeInBilling(boolean includeInBilling) {
		this.includeInBilling = includeInBilling;
	}

	public LoadConfigReturnItem[] getBenefitClass() {
		return benefitClass;
	}

	public void setBenefitClass(LoadConfigReturnItem[] benefitClass) {
		this.benefitClass = benefitClass;
	}

	/**
	 * @return Returns the coversEmployeeSpouseOrChildren.
	 */
	public boolean isCoversEmployeeSpouseOrChildren() {
		return coversEmployeeSpouseOrChildren;
	}

	/**
	 * @param coversEmployeeSpouseOrChildren The coversEmployeeSpouseOrChildren
	 * to set.
	 */
	public void setCoversEmployeeSpouseOrChildren(
			final boolean coversEmployeeSpouseOrChildren) {
		this.coversEmployeeSpouseOrChildren = coversEmployeeSpouseOrChildren;
	}

	/**
	 * @return Returns the activeDate.
	 */
	public int getActiveDate() {
		return activeDate;
	}

	/**
	 * @param activeDate The activeDate to set.
	 */
	public void setActiveDate(final int activeDate) {
		this.activeDate = activeDate;
	}

	/**
	 * @return Returns the inactiveDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}

	/**
	 * @param inactiveDate The inactiveDate to set.
	 */
	public void setInactiveDate(final int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getCoversEmployee() {
		return coversEmployee;
	}

	public void setCoversEmployee(final boolean coversEmployee) {
		this.coversEmployee = coversEmployee;
	}

	public boolean getCoversNonEmployeeSpouse() {
		return coversNonEmployeeSpouse;
	}

	public void setCoversNonEmployeeSpouse(final boolean coversSpouse) {
		this.coversNonEmployeeSpouse = coversSpouse;
	}

	public boolean getCoversChildren() {
		return coversChildren;
	}

	public void setCoversChildren(final boolean coversChildren) {
		this.coversChildren = coversChildren;
	}

	public boolean getCoversNonEmployeeSpouseOrChildren() {
		return coversNonEmployeeSpouseOrChildren;
	}

	public void setCoversNonEmployeeSpouseOrChildren(final boolean coversSpouseOrChildren) {
		this.coversNonEmployeeSpouseOrChildren = coversSpouseOrChildren;
	}

	public int getMaxDependents() {
		return maxDependents;
	}

	public void setMaxDependents(final int maxDependents) {
		this.maxDependents = maxDependents;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(final String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public boolean getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(final boolean autoAssign) {
		this.autoAssign = autoAssign;
	}

	public boolean getSpouseDeclinesExternalCoverage() {
		return spouseDeclinesExternalCoverage;
	}

	public void setSpouseDeclinesExternalCoverage(final boolean spouseDeclinesExternalCoverage) {
		this.spouseDeclinesExternalCoverage = spouseDeclinesExternalCoverage;
	}

	public boolean getCoversEmployeeSpouse() {
		return coversEmployeeSpouse;
	}

	public void setCoversEmployeeSpouse(final boolean spouseIsEmployee) {
		this.coversEmployeeSpouse = spouseIsEmployee;
	}
}
