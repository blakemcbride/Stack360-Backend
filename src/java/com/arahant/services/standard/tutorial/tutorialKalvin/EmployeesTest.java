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

import com.arahant.beans.Address;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplDependentWizard;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BHREmplDependentWizard;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * Arahant
 */
public class EmployeesTest {

    public HibernateSessionUtil hsu = ArahantSession.getHSU();

    public static String empid = "00001-0000002645";
    public EmployeesTest() {

    }
    public void printEmployeInfo() {
        BPerson emp = new BPerson(empid);
        //PrintObjectValues.print(emp);
        BHREmplDependent[] dps = emp.getBEmployee().getDependents();

        System.out.println("printEmployeInfo size of dep is " + dps.length);
    }
    public void getPendingDep() {
        List<HrEmplDependent> lpend = hsu.createCriteria(HrEmplDependent.class).in(HrEmplDependent.EMPLOYEE, new Employee[]{new BEmployee(empid).getEmployee()}).list();
        System.out.println("getPendingDep Size is " + lpend.size());
    }
    public void listActiveDep(){
        List<HrEmplDependent> lpend = hsu.createCriteria(HrEmplDependent.class).in(HrEmplDependent.EMPLOYEE, new Employee[]{new BEmployee(empid).getEmployee()}).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();
        System.out.println("listActiveDep Size is " + lpend.size());
    }
    public void getDependents(){
        List<HrEmplDependentWizard> dw = hsu.createCriteria(HrEmplDependentWizard.class).eq(HrEmplDependentWizard.EMPLOYEE_ID, empid).list();
        for (HrEmplDependentWizard w : dw){
            if (w.getRecordType()=='C')
            System.out.println("Name is " + (new BHREmplDependentWizard(w)).getLastName());
        }
        System.out.println("getDependents Size is " + dw.size());
    }
    public void getDependent(){
        BHREmplDependent bc = new BHREmplDependent("00001-0000002646");
    }
	public void getEmployeeUsingHQL(){
		Query q = hsu.createQuery("Select p,a from Employee p, Address a where a.person = p and lname like 'A%' order by a.city,p.fname");
		List<Object> emps = q.list();

		for (int i=0; i<emps.size(); i++){
			Object[] emp = (Object[])emps.get(i);
			Person p = (Person)emp[0];
			Address a = (Address)emp[1];
			System.out.println("Emp " + p.getNameFL() + " " + p.getPersonalEmail());
		}
//		for (Person p : emps){
//			System.out.println(p.getNameFL());
//		}
	}
    public static void main(String[] args) {
        EmployeesTest emp = new EmployeesTest();
        emp.getEmployeeUsingHQL();
    }
}
