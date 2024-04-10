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
import com.arahant.beans.IHrBenefitJoin;
import com.arahant.beans.IHrBenefitJoinCurrent;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class RLGMailTo {

	public static void main (String args[])
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();

		HashSet<IHrBenefitJoinCurrent> joins=new HashSet<IHrBenefitJoinCurrent>();

		joins.addAll(hsu.createCriteria(IHrBenefitJoinCurrent.class)
			.eq(HrBenefitJoin.POLICY_START_DATE, 20100601)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());

		joins.addAll(hsu.createCriteria(IHrBenefitJoinCurrent.class)
			.eq(HrBenefitJoin.COVERAGE_START_DATE, 20100601)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());

		List <IHrBenefitJoinCurrent> l=new ArrayList<IHrBenefitJoinCurrent>();

		l.addAll(joins);

		Collections.sort(l,new Comparator<IHrBenefitJoinCurrent>() {

			@Override
			public int compare(IHrBenefitJoinCurrent o1, IHrBenefitJoinCurrent o2) {
				if (o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM())!=0)
					return o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM());

				if (o1.getCoveredPerson().equals(o1.getPayingPerson()))
				{
					return -1;
				}

				if (o2.getCoveredPerson().equals(o2.getPayingPerson()))
				{
					return 1;
				}

				return o1.getCoveredPerson().getNameLFM().compareTo(o2.getCoveredPerson().getNameLFM());
			}
		});

		System.out.println("\n\nAdded Coverage List\n\n");

		for (IHrBenefitJoinCurrent bj : l)
		{
			System.out.println (bj.getPayingPerson().getNameLFM()+"\t"+bj.getCoveredPerson().getNameLFM()+"\t"+bj.getHrBenefitConfig().getName()/*+"\t"+DateUtils.getDateFormatted(bj.getPolicyStartDate())+"\t"+DateUtils.getDateFormatted(bj.getCoverageStartDate())*/);
		}


		System.out.println("\n\nTerminated List\n\n");

		joins=new HashSet<IHrBenefitJoinCurrent>();

		joins.addAll(hsu.createCriteria(IHrBenefitJoinCurrent.class)
			.ne(HrBenefitJoin.POLICY_END_DATE, 0)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());

		joins.addAll(hsu.createCriteria(IHrBenefitJoinCurrent.class)
			.ne(HrBenefitJoin.COVERAGE_END_DATE, 0)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list());

		l=new ArrayList<IHrBenefitJoinCurrent>();

		l.addAll(joins);

		Collections.sort(l,new Comparator<IHrBenefitJoinCurrent>() {

			@Override
			public int compare(IHrBenefitJoinCurrent o1, IHrBenefitJoinCurrent o2) {
				if (o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM())!=0)
					return o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM());

				if (o1.getCoveredPerson().equals(o1.getPayingPerson()))
				{
					return -1;
				}

				if (o2.getCoveredPerson().equals(o2.getPayingPerson()))
				{
					return 1;
				}

				return o1.getCoveredPerson().getNameLFM().compareTo(o2.getCoveredPerson().getNameLFM());
			}
		});

		for (IHrBenefitJoinCurrent bj : l)
		{
			System.out.println (bj.getPayingPerson().getNameLFM()+"\t"+bj.getCoveredPerson().getNameLFM()/*+"\t"+DateUtils.getDateFormatted(bj.getPolicyStartDate())+"\t"+DateUtils.getDateFormatted(bj.getCoverageStartDate())*/);
		}




		System.out.println("\n\nDeclined List\n\n");

		joins=new HashSet<IHrBenefitJoinCurrent>();

		for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class)
			.joinTo(HrBenefitJoin.HRBENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list())
		{
			//got my decline, look to see if ever had a medical
				if (hsu.createCriteria(IHrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
				.exists())
			joins.add(bj);

		}

		l=new ArrayList<IHrBenefitJoinCurrent>();

		l.addAll(joins);

		Collections.sort(l,new Comparator<IHrBenefitJoinCurrent>() {

			@Override
			public int compare(IHrBenefitJoinCurrent o1, IHrBenefitJoinCurrent o2) {
				if (o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM())!=0)
					return o1.getPayingPerson().getNameLFM().compareTo(o2.getPayingPerson().getNameLFM());

				if (o1.getCoveredPerson().equals(o1.getPayingPerson()))
				{
					return -1;
				}

				if (o2.getCoveredPerson().equals(o2.getPayingPerson()))
				{
					return 1;
				}

				return o1.getCoveredPerson().getNameLFM().compareTo(o2.getCoveredPerson().getNameLFM());
			}
		});

		for (IHrBenefitJoinCurrent bj : l)
		{
			System.out.println (bj.getPayingPerson().getNameLFM()+"\t"+bj.getCoveredPerson().getNameLFM()/*+"\t"+DateUtils.getDateFormatted(bj.getPolicyStartDate())+"\t"+DateUtils.getDateFormatted(bj.getCoverageStartDate())*/);
		}


/*
		for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.AMOUNT_COVERED,(double)0)
			.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID,"00001-0000000045")
			.list())
		{

			System.out.println(bj.getPayingPerson().getNameLFM());
			HrBenefitJoin fixer=hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
				.ne(HrBenefitJoin.AMOUNT_COVERED, (double)0)
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID,"00001-0000000045")
				.first();

			bj.setAmountCovered(fixer.getAmountCovered());

			hsu.saveOrUpdate(bj);
		}
*/
	/*	HashSet<HrBenefitJoin> benes=new HashSet<HrBenefitJoin>();
		//find all wizard dental benefits
		benes.addAll(hsu.createCriteria(HrBenefitJoin.class)
			.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
			.eq(HrBenefitJoin.APPROVED, 'Y')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL)
			.list());


	

		List<HrBenefitJoin> bl=new ArrayList<HrBenefitJoin>(benes.size());

		Collections.sort(bl, new Comparator<HrBenefitJoin>() {

			@Override
			public int compare(HrBenefitJoin o1, HrBenefitJoin o2) {
				return o1.getPayingPerson().compareTo(o2.getPayingPerson());
			}
		});

		String emails="";

		for (HrBenefitJoin bj : benes)
		{
			if ("PLEASE ENTER".equals(bj.getPayingPerson().getPersonalEmail()))
				System.out.println(bj.getPayingPerson().getNameLFM());
			emails+=bj.getPayingPerson().getPersonalEmail()+";";
		}

		System.out.println(emails);
	 * */

		//find all pending records
		hsu.rollbackTransaction();
	}

}
