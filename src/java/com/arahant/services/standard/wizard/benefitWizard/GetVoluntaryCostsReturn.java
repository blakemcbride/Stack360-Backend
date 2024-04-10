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
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BBenefitConfigCostAge;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetVoluntaryCostsReturn extends TransmitReturnBase {
	
	private static final transient ArahantLogger logger = new ArahantLogger(GetVoluntaryCostsReturn.class);
	
	private double actualPppCost;
	private double actualMonthlyCost;
	private double actualAnnualCost;

	public void setData(GetVoluntaryCostsInput in) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		String empId = isEmpty(in.getEmployeeId()) ? hsu.getCurrentPerson().getPersonId() : in.getEmployeeId();
		BEmployee be = new BEmployee(empId);
		BHRBenefitConfig bc = new BHRBenefitConfig(in.getConfigId());
		BHRBenefit ben = new BHRBenefit(bc.getBenefit());
		BBenefitConfigCost bcc = BenefitCostCalculator.findConfigCost(bc, be.getStatusId(), be.getOrgGroups(), in.getAsOfDate());
		//  above could be null
		BBenefitConfigCostAge bcca = BenefitCostCalculator.findBenefitConfigCostAge(bcc, be, 0, DateUtils.today());
		//  above could be null too

		List<IPerson> covered = new ArrayList<IPerson>();


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
					ip = hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, k).first();
				covered.add(ip);
			}
		}
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
		
		actualAnnualCost = BenefitCostCalculator.calculateEmployeeAnnualCost(be, ben, bcc, bcca, in.getCoverageCost());
		int ppy = BEmployee.getPPY(be.getPerson());
		if (ppy == 0) {
			logger.error("No pay periods defined for employee " + be.getFirstName() + " " + be.getLastName());
			actualPppCost = 0.0;
		} else
			actualPppCost = Utils.round(actualAnnualCost / ppy, 2);
		actualMonthlyCost = Utils.round(actualAnnualCost / 12, 2);
	}

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
