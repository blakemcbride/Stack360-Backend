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

package com.arahant.tutorial.OldHibernateExamples;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import org.hibernate.mapping.Collection;

/**
 *
 */
public class RLGMedicalChange {

	public static void main (String args[])
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();

		HashSet<HrBenefitJoin> benes=new HashSet<HrBenefitJoin>();
		//find all wizard med benefits
		benes.addAll(hsu.createCriteria(HrBenefitJoin.class)
			.gt(HrBenefitJoin.POLICY_START_DATE, 20100501)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());


		benes.addAll(hsu.createCriteria(HrBenefitJoin.class)
			.gt(HrBenefitJoin.COVERAGE_START_DATE, 20100501)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());

		List<HrBenefitJoin> bl=new ArrayList<HrBenefitJoin>(benes.size());

		Collections.sort(bl, new Comparator<HrBenefitJoin>() {

			@Override
			public int compare(HrBenefitJoin o1, HrBenefitJoin o2) {
				return o1.getPayingPerson().compareTo(o2.getPayingPerson());
			}
		});

		for (HrBenefitJoin bj : benes)
		{
			System.out.println(bj.getPayingPerson().getNameLFM()+" - "+bj.getCoveredPerson().getNameLFM());
		}

		//find all pending records
		hsu.rollbackTransaction();
	}

}
