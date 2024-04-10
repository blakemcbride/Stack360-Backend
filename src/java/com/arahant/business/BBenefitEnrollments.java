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
package com.arahant.business;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Arahant
 * This is a generic
 */
public class BBenefitEnrollments {

    public static boolean enrolledInPendingBenefit(String benefitId, String[] enrollingPerson) {

		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.COVERED_PERSON_ID, enrollingPerson).exists();
    }

    public static boolean enrolledInApprovedBenefit(String benefitId, String[] enrollingPerson) {
        return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.COVERED_PERSON_ID, enrollingPerson).exists();
    }

    public static boolean enrolledInPendingConfig(String benefitId, String[] possibleIds) {
        return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').exists();

    }

    public static boolean enrolledInApprovedConfig(String benefitId, String[] possibleIds) {
        return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').exists();

    }

	public static boolean enrolledInCategory(String categoryId, String[] possibleIds) {
		
		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
		   .joinTo(HrBenefit.BENEFIT_CATEGORY)
		   .eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'N')
				.in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds)
				.exists();
	}

	public static boolean enrolledInApprovedCategory(String categoryId, String[] possibleIds) {

		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
		   .joinTo(HrBenefit.BENEFIT_CATEGORY)
		   .eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds)
				.exists();
	}
}

