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

import com.arahant.beans.Employee;
import com.arahant.beans.HrWage;
import com.arahant.business.BEmployee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Iterator;
import java.util.List;

/**
 * Hibernate example 20. 
 * This example demonstrates the use of business classes
 * to simplify the query process.
 */
public class Hibernate20 {

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  In this example, we will start in the Employee table and find out what the
			//  employee's current salary is. This will be done first using the standard
			//  Hibernate Criteria method, and then again using business classes which were
			//  built on top of the Criteria methods. This is to demonstrate the difference
			//  between the two.
			List<Employee> v = hsu.createCriteria(Employee.class).joinTo(Employee.HRWAGES).list();

			System.out.println("Standard Hibernate Criteria:\n");

			//  This will go through an employee's wage history and pick out the wage
			//  with the most current effective date in order to determine each
			//  employee's current salary/wage.
			for (Employee i : v) {
				Iterator<HrWage> wage = i.getHrWages().iterator();
				HrWage currentWage = new HrWage();
				currentWage.setEffectiveDate(0);
				while (wage.hasNext()) {
					HrWage tempWage = wage.next();
					if (tempWage.getEffectiveDate() > currentWage.getEffectiveDate())
						currentWage = tempWage;
				}

				System.out.println(i.getNameFML() + " " + currentWage.getWageAmount());
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}

		System.out.println("\n\n\n");

		try {
			hsu.setCurrentPersonToArahant();
			hsu.beginTransaction();

			//  The following will show the exact same implementation as above, except done
			//  through the use of business classes. Business classes contain many useful
			//  methods which can be used to streamline certain queries. Notice how much less
			//  code there is, since the business class method does a lot of the leg work which
			//  had to be implemented in the previous version.
			List<Employee> v = hsu.createCriteria(Employee.class).joinTo(Employee.HRWAGES).list();

			System.out.println("Using Business Classes:\n");
			for (Employee i : v) {
				BEmployee be = new BEmployee(i);
				System.out.println(be.getNameFML() + " " + be.getCurrentSalary());
			}

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
