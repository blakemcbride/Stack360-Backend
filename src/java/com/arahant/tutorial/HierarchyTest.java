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
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class HierarchyTest {

    public static void getGA(Employee employee) {
        Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();
		for (OrgGroupAssociation oga : assocs) {
			OrgGroup og = oga.getOrgGroup();
			System.out.println("GA " + og.getName());
		}
    }

    public static void getGH(BEmployee emp) {
        BOrgGroup og = new BOrgGroup(emp.getOrgGroupId());

        Set<OrgGroupHierarchy> ogh = og.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		for (OrgGroupHierarchy h : ogh)
			System.out.println("GH  " + h.getOrgGroupByChildGroupId().getName());
    }

    public static void getGHCriteria(BEmployee emp, HibernateSessionUtil hsu) {
        List<OrgGroupHierarchy> v = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, emp.getOrgGroupId()).list();
        for (OrgGroupHierarchy g : v) {
            System.out.println("getGHCriteria  " + g.getOrgGroupByChildGroupId().getName());
        }

    }

    public static void main(final String args[]) {
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        try {

            List<String> empIds = new ArrayList<String>();
            empIds.add("00000-0000000004");
            //empIds.add("00000-0000000005");

            for (String id : empIds) {
                BEmployee emp = new BEmployee(id);

                getGA(emp.getEmployee());
                getGH(emp);
                getGHCriteria(emp, hsu);

            }
            hsu.commitTransaction();
        } catch (final Exception e) {
            e.printStackTrace();
            hsu.rollbackTransaction();
        }
    }
}
