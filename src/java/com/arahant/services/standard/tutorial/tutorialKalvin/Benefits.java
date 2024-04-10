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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;
import java.util.Set;

/**
 *
 * Arahant
 */
public class Benefits {

    public HibernateSessionUtil hsu = ArahantSession.getHSU();

    public void getAllBenefitCategory() {
        List<HrBenefitCategory> list = hsu.createCriteria(HrBenefitCategory.class).list();

        for (HrBenefitCategory c : list) {
            System.out.println("Category name: " + c.getBenefitCatId() + "-" + c.getDescription());
        }

    }

    public void getBenefitCategoryByType() {
        //get category of type dental
        List<HrBenefitCategory> list = hsu.createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL).list();

        for (HrBenefitCategory c : list) {
            System.out.println("Category by type name: " + c.getBenefitCatId() + "-" + c.getDescription());
        }

    }

    public void getBenefitsOfCertainCategoryType() {
        //get benefit of type Health
        List<HrBenefit> list = hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH).list();
        for (HrBenefit b : list) {
            System.out.println("Benefit name: " + b.getName());
            //now get what type of coverage this benefit has
            Set<HrBenefitConfig> configs = b.getBenefitConfigs();
            for (HrBenefitConfig bc : configs) {
                System.out.println("\tConfig: " + bc.getName());
            }

        }
    }

    public void getListEmployeesJoiningAParticularBenefit() {
        //all elections are in HrBenefitJoins, do joins to get the Health benefit
        List<Person> bj = hsu.createCriteria(Person.class).joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
                .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
                .joinTo(HrBenefitConfig.HR_BENEFIT)
                .joinTo(HrBenefit.BENEFIT_CATEGORY)
                .eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
                .list();

        for (Person p : bj) {
            System.out.println("Employee joining " + p.getNameFML());
        }
    }

    public void getBenefit() {
        List<HrBenefit> list = hsu.createCriteria(HrBenefit.class).list();
        for (HrBenefit b : list) {
            System.out.println("Benefit name: " + b.getName());
            //now get what type of coverage this benefit has
            Set<HrBenefitConfig> configs = b.getBenefitConfigs();
            for (HrBenefitConfig bc : configs) {
                System.out.println("\tConfig: " + bc.getName());
            }

        }
    }

    public void getEmployeeAndDependents() {
        //first get employee and then get getDependents
        List<Employee> emp = hsu.createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).list();
        for (Employee c : emp) {
            System.out.println("Employee: " + c.getNameFML());
            Set<HrEmplDependent> dep = c.getHrEmplDependents();
            for (HrEmplDependent d : dep) {
                System.out.println("\tDependent: " + d.getPerson().getNameFML() + " Type: " + d.getRelationshipType());
            }
        }
    }

    public void getEmployeeWhoElectedBenefit() {
        //list employees with benefits, order by last name
        //uses scroll for this one
        HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU()
                .createCriteria(HrBenefitJoin.class)
                .eq(HrBenefitJoin.APPROVED, 'Y')
                .distinct()
                .joinTo(HrBenefitJoin.PAYING_PERSON).eq(Person.RECORD_TYPE, 'R').orderBy(Person.LNAME);

        HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

        while (scr.next()) {
            System.out.println(scr.get().getPayingPerson().getNameLFM() +"(" + scr.get().getPayingPerson().getPersonId() + ")" + " Benefit Type: " + scr.get().getHrBenefitConfig().getName());

        }
        scr.close();
    }

    public static void main(String[] args) {
        Benefits ben = new Benefits();
        // ben.getBenefit();
        // ben.getAllBenefitCategory();
        // ben.getBenefitCategoryByType();
        // ben.getBenefitsOfCertainCategoryType();
        // ben.getListEmployeesJoiningAParticularBenefit();
        // ben.getEmployeeAndDependents();
        ben.getEmployeeWhoElectedBenefit();
    }
}
