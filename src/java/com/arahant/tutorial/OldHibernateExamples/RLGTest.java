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
import java.util.List;

/**
 *
 */
public class RLGTest {

	public static void main (String args[])
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();
		
		//find all wizard benefits
		List<HrBenefit> benes=hsu.createCriteria(HrBenefit.class)
			.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
			.list();

	/*	benes.addAll(hsu.createCriteria(HrBenefit.class)
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.OPEN_ENROLLMENT_WIZARD,'Y')
				.list());
*/

		//remove any unapproved that point to a non-wiz benefit
		hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.APPROVED, 'N')
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.notIn(HrBenefitConfig.HR_BENEFIT, benes)
			.delete();


		//find everyone with a pending record
		List<String> ids=(List)hsu.createCriteria(Person.class)
			.selectFields(Person.PERSONID)
			.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
			.eq(HrBenefitJoin.APPROVED, 'N')
			.list();

		//find all people that have pending, but no declines
		List<String> haveDeclines=(List)hsu.createCriteria(Person.class)
			.selectFields(Person.PERSONID)
			.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
			.eq(HrBenefitJoin.APPROVED, 'N')
			.joinTo(HrBenefitJoin.HRBENEFIT)
			.list();


		ids.removeAll(haveDeclines);

		List<Person> pl=hsu.createCriteria(Person.class)
			.in(Person.PERSONID, ids)
			.list();


		

		
		//for each person, what benefit do they need decline on
		for (Person p : pl)
		{
			System.out.println(p.getNameLFM());

			for (HrBenefit b :benes)
			{
				if (!hsu.createCriteria(HrBenefitJoin.class)
					.eq(HrBenefitJoin.PAYING_PERSON,p)
					.eq(HrBenefitJoin.COVERED_PERSON,p)
					.eq(HrBenefitJoin.APPROVED, 'N')
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.eq(HrBenefitConfig.HR_BENEFIT, b)
					.exists())
				{
					System.out.println("Need decline for "+b.getName());
					BHRBenefitJoin bbj =new BHRBenefitJoin();
					bbj.create();
					bbj.setBenefitApproved(false);
					bbj.setHrBenefit(b);
					bbj.setPayingPerson(p);
					bbj.setCoveredPerson(p);
					bbj.setPolicyStartDate(20100601);
					bbj.setCoverageStartDate(20100601);
					bbj.insert(true);
				}
			}
		}


		//if they have a medical decline and a medical pending, need to delete the medical decline
		for (HrBenefitJoin dbj : hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.APPROVED, 'N')
			.joinTo(HrBenefitJoin.HRBENEFIT)
			.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
			.list())
		{
			if (hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.APPROVED, 'N')
				.eq(HrBenefitJoin.PAYING_PERSON, dbj.getPayingPerson())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y')
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
				.exists())
			{
				System.out.println("*** SHOULD DELETE DECLINE ***");
				hsu.delete(dbj);
			}
		}

		//now for everybody with a pending, if they have a complete match
		//of pending benefit with non-pending benefit, remove the pending benefit

		for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.APPROVED, 'N')
			.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
			.isNotNull(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.list())
		{
			//since we captured beneficiaries, we need the ones that have those
			if (bj.getBeneficiaries().size()>0)
				continue;

			HrBenefitJoin approvedBJ=hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
				.eq(HrBenefitJoin.COVERED_PERSON, bj.getCoveredPerson())
				.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bj.getHrBenefitConfigId())
				.first();

			if (approvedBJ!=null)
			{
			//	System.out.println("Found possible match "+approvedBJ.getHrBenefitConfig().getName());
				boolean fullMatch=true;

				//are all the ones found both ways

				//are all approved in pending?
				for (HrBenefitJoin searchBJ : hsu.createCriteria(HrBenefitJoin.class)
					.eq(HrBenefitJoin.APPROVED, 'Y')
					.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, approvedBJ.getHrBenefitConfig())
					.eq(HrBenefitJoin.PAYING_PERSON, approvedBJ.getPayingPerson())
					.eq(HrBenefitJoin.POLICY_START_DATE, approvedBJ.getPolicyStartDate())
					.list())
				{
					if (!hsu.createCriteria(HrBenefitJoin.class)
						.eq(HrBenefitJoin.APPROVED, 'N')
						.eq(HrBenefitJoin.PAYING_PERSON, searchBJ.getPayingPerson())
						.eq(HrBenefitJoin.COVERED_PERSON, searchBJ.getCoveredPerson())
						.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, bj.getHrBenefitConfigId())
						.eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate())
						.exists())
					{
						fullMatch=false;
						break;
					}
				}

				//find out if all pendings are in approved
				if (fullMatch)
				{
					for (HrBenefitJoin searchBJ : hsu.createCriteria(HrBenefitJoin.class)
						.eq(HrBenefitJoin.APPROVED, 'N')
						.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bj.getHrBenefitConfig())
						.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
						.eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate())
						.list())
					{
						if (searchBJ.getBeneficiaries().size()>0)
						{
							fullMatch=false;
							break;
						}
						if (!hsu.createCriteria(HrBenefitJoin.class)
							.eq(HrBenefitJoin.APPROVED, 'Y')
							.eq(HrBenefitJoin.PAYING_PERSON, searchBJ.getPayingPerson())
							.eq(HrBenefitJoin.COVERED_PERSON, searchBJ.getCoveredPerson())
							.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, approvedBJ.getHrBenefitConfigId())
							.eq(HrBenefitJoin.POLICY_START_DATE, approvedBJ.getPolicyStartDate())
							.exists())
						{
							fullMatch=false;
							break;
						}
					}
				}

				if (fullMatch)
				{
					//delete the pending record chain
					hsu.createCriteria(HrBenefitJoin.class)
						.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bj.getHrBenefitConfig())
						.eq(HrBenefitJoin.APPROVED,'N')
						.eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson())
						.eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate())
						.delete();
			 		System.out.println(approvedBJ.getHrBenefitConfig().getName()+" Was fully matched");
				}
			//	else
			//		System.out.println(approvedBJ.getHrBenefitConfig().getName()+" Was not fully matched");

			}
		}


		//find all pending records
		hsu.commitTransaction();
	}

}
