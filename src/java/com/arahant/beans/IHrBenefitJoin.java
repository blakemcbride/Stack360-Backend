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

package com.arahant.beans;

import java.util.Date;

/**
 *
 */
public interface IHrBenefitJoin extends IArahantBean{


	public static final String BENEFIT_JOIN_ID = "benefitJoinId";
	public static final String POLICY_START_DATE="policyStartDate";
	public static final String POLICY_END_DATE="policyEndDate";
	public static final String AMOUNT_PAID="amountPaid";
	public static final String HR_BENEFIT_CONFIG_ID="hrBenefitConfigId";
	public static final String PAYING_PERSON_ID="payingPersonId";
	public static final String COVERED_PERSON_ID="coveredPersonId";
	public static final String HR_BENEFIT_CONFIG="hrBenefitConfig";
	public static final String USING_COBRA="usingCOBRA";
	public static final String CHANGE_REASON_STRING="changeDescription";
	public static final String COVERED_PERSON="coveredPerson";
	public static final String PAYING_PERSON="payingPerson";
	public static final String COVERAGE_START_DATE = "coverageStartDate";
    public static final String COVERAGE_END_DATE = "coverageEndDate";
	public static final String APPROVED = "benefitApproved";
	public static final String RELATIONSHIP = "relationship";

	public String getBenefitJoinId();

	public int getPolicyEndDate();
	public Date getRecordChangeDate();
	public char getRecordChangeType();

	public String getChangeDescription();

	public String getComments();

	public int getCoverageStartDate();
	public int getCoverageEndDate();
	public String getCalculatedCost();
	public double getAmountCovered();

	public Person getCoveredPerson();
	public String getCoveredPersonId();

	public Person getPayingPerson();
	public String getPayingPersonId();

	public HrEmplDependent getRelationship();
	public String getRelationshipId();

	public HrBenefitConfig getHrBenefitConfig();
	public String getHrBenefitConfigId();

	public int getPolicyStartDate();

	public char getUsingCOBRA();
	public char getBenefitApproved();
	public HrBenefitChangeReason getBenefitChangeReason();
	public LifeEvent getLifeEvent();

}
