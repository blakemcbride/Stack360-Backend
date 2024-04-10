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
package com.arahant.tutorial;

import com.arahant.beans.HrBenefitJoin;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 * Hibernate example 19. 
 * This example demonstrates querying multiple tables
 */
public class Hibernate19 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  This example queries mutliple tables to formulate its result. In this particular case,
			//  We start at the HRBenefitsJoin table and query multiple tables to find employees who pay for
			//  benefits and who is covered under those benefits, if any. Covered individuals will be listed
			//  under the paying person.

			//Creates a list of all policy benefits (where the paying individual is also the covered individual
			List<HrBenefitJoin> joins = hsu.createCriteria(HrBenefitJoin.class).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).list();
			for (HrBenefitJoin join : joins) {
				System.out.println(join.getPayingPerson().getNameFML() + ": " + join.getHrBenefitConfigId());

				//Selects covered individuals by searching for benefits where the covered person is not the same as the paying person,
				//but all other fields are equal.
				List<HrBenefitJoin> depJoins = hsu.createCriteria(HrBenefitJoin.class)
						.eq(HrBenefitJoin.PAYING_PERSON, join.getPayingPerson())
						.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, join.getHrBenefitConfig())
						.ne(HrBenefitJoin.COVERED_PERSON, join.getCoveredPerson()).list();

				//Prints out covered individuals
				for (HrBenefitJoin depJoin : depJoins)
					System.out.println("\t-- " + depJoin.getCoveredPerson().getNameFML());
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
