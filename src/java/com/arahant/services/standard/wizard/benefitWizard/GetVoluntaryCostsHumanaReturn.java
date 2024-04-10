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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.BenefitCostCalculator;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.IPerson;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetVoluntaryCostsHumanaReturn extends TransmitReturnBase {

	public void setData(GetVoluntaryCostsHumanaInput in) {
		String empId = isEmpty(in.getEmployeeId()) ? ArahantSession.getHSU().getCurrentPerson().getPersonId() : in.getEmployeeId();

		List<IPerson> covered = new ArrayList<IPerson>();

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (in.getEmployeeCovered())
			covered.add(hsu.get(Person.class, empId));

		for (String s : in.getIds()) {
			HrEmplDependent d = hsu.get(HrEmplDependent.class, s);

			if (d != null)
				covered.add(d.getPerson());
			else {
				HrEmplDependent dep = hsu.get(HrEmplDependent.class, s);

				String k = dep.getDependentId();
				IPerson ip = hsu.get(Person.class, k);
				if (ip == null)
					ip = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, k).first();
				covered.add(ip);
			}
		}
		BEmployee be = new BEmployee(empId);
		if (be.getIsNewHire()) {
			BHRBenefit bhrb = new BHRBenefit(new BHRBenefitConfig(in.getConfigId()).getBenefit());
			Calendar hireCal = DateUtils.getCalendar(be.getHireDate());
			switch (bhrb.getEligibilityType()) {
				case 2:
					hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
					if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
						hireCal.add(Calendar.MONTH, 1);
					hireCal.set(Calendar.DAY_OF_MONTH, 1);
					break;
				case 3:
					hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
					if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
						hireCal.add(Calendar.MONTH, 1);
					hireCal.set(Calendar.DAY_OF_MONTH, 1);
					break;
				case 4:
					hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
					break;
				case 5:
					hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
					break;
				default:
					break;
			}
			in.setAsOfDate(DateUtils.getDate(hireCal));
		}
		BHRBenefitConfig bcc = new BHRBenefitConfig(in.getConfigId());
		actualAnnualCost = BenefitCostCalculator.calculateCostNewMethodAnnualWithDependencies(in.getAsOfDate(), bcc.getBean(), be.getPerson(), false, in.getCoverageCost(), in.getAsOfDate(), 0, covered, -1);
		actualPppCost = BenefitCostCalculator.calculateCostNewMethodWithDependencies(in.getAsOfDate(), bcc.getBean(), be.getPerson(), false, in.getCoverageCost(), in.getAsOfDate(), 0, covered, -1);
		if (actualPppCost < 0)
			actualPppCost = 0;
		actualMonthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthlyWithDependencies(in.getAsOfDate(), bcc.getBean(), be.getPerson(), false, in.getCoverageCost(), in.getAsOfDate(), 0, covered, -1);
	}
	private double actualPppCost;
	private double actualMonthlyCost;
	private double actualAnnualCost;

	public double getActualPppCost() {
		return actualPppCost;
	}

	public void setActualPppCost(double actualPppCost) {
		this.actualPppCost = actualPppCost;
	}

	public double getActualMonthlyCost() {
		return actualMonthlyCost;
	}

	public void setActualMonthlyCost(double actualMonthlyCost) {
		this.actualMonthlyCost = actualMonthlyCost;
	}

	public double getActualAnnualCost() {
		return actualAnnualCost;
	}

	public void setActualAnnualCost(double actualAnnualCost) {
		this.actualAnnualCost = actualAnnualCost;
	}
}
