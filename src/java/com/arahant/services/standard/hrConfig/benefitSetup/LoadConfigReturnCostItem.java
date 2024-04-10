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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.hrConfig.benefitSetup;

import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BBenefitConfigCostAge;
import com.arahant.business.BBenefitConfigCostStatus;

/**
 *
 */
public class LoadConfigReturnCostItem {

	//entry - id (string), orgGroupId (string), orgGroupName (string), statusType (string),
	//statuses (array of id - string, name - string),
	//baseAmountSource (string), baseAmount (number), baseRoundAmount (number),
	//baseCapAmount (double), multiplierSource (string), formulaMultiplier (number),
	//formulaDivider (number), firstActiveDate (number), lastActiveDate (number), age (array)

	private String id;
	private String orgGroupId;
	private String orgGroupName;
	private String statusType;
	private String baseAmountSource;
	private double baseAmount;
	private double baseRoundAmount;
	private double baseCapAmount;
	private String multiplierSource;
	private double formulaMultiplier;
	private double formulaDivider;
	private int firstActiveDate;
	private int lastActiveDate;
	private LoadConfigReturnCostItemStatus[] statuses;
	private LoadConfigReturnCostItemAge[] age;

	public LoadConfigReturnCostItem()
	{

	}
	LoadConfigReturnCostItem(BBenefitConfigCost bcc) {
		id=bcc.getId();
		orgGroupId=bcc.getOrgGroupId();
		orgGroupName=bcc.getOrgGroupName();
		statusType=bcc.getStatusType();
		baseAmountSource=bcc.getBaseAmountSource();
		baseAmount=bcc.getBaseAmount();
		baseRoundAmount=bcc.getBaseRoundAmount();
		baseCapAmount=bcc.getBaseCapAmount();
		multiplierSource=bcc.getMultiplierSource();
		formulaMultiplier=bcc.getFormulaMultiplier();
		formulaDivider=bcc.getFormulaDivider();
		firstActiveDate=bcc.getFirstActiveDate();
		lastActiveDate=bcc.getLastActiveDate();

		BBenefitConfigCostStatus []stats=bcc.getStatuses();

		statuses=new LoadConfigReturnCostItemStatus[stats.length];
		for (int loop=0;loop<statuses.length;loop++)
			statuses[loop]=new LoadConfigReturnCostItemStatus(stats[loop]);

		BBenefitConfigCostAge []ages=bcc.getAges();
		age=new LoadConfigReturnCostItemAge[ages.length];
		for (int loop=0;loop<ages.length;loop++)
			age[loop]=new LoadConfigReturnCostItemAge(ages[loop]);

	}

	public LoadConfigReturnCostItemAge[] getAge() {
		return age;
	}

	public void setAge(LoadConfigReturnCostItemAge[] age) {
		this.age = age;
	}

	public double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public String getBaseAmountSource() {
		return baseAmountSource;
	}

	public void setBaseAmountSource(String baseAmountSource) {
		this.baseAmountSource = baseAmountSource;
	}

	public double getBaseCapAmount() {
		return baseCapAmount;
	}

	public void setBaseCapAmount(double baseCapAmount) {
		this.baseCapAmount = baseCapAmount;
	}

	public double getBaseRoundAmount() {
		return baseRoundAmount;
	}

	public void setBaseRoundAmount(double baseRoundAmount) {
		this.baseRoundAmount = baseRoundAmount;
	}

	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public double getFormulaDivider() {
		return formulaDivider;
	}

	public void setFormulaDivider(double formulaDivider) {
		this.formulaDivider = formulaDivider;
	}

	public double getFormulaMultiplier() {
		return formulaMultiplier;
	}

	public void setFormulaMultiplier(double formulaMultiplier) {
		this.formulaMultiplier = formulaMultiplier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getMultiplierSource() {
		return multiplierSource;
	}

	public void setMultiplierSource(String multiplierSource) {
		this.multiplierSource = multiplierSource;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public LoadConfigReturnCostItemStatus[] getStatuses() {
		return statuses;
	}

	public void setStatuses(LoadConfigReturnCostItemStatus[] statuses) {
		this.statuses = statuses;
	}

}
