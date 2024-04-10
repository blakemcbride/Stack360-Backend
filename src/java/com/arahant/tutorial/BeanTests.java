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
import com.arahant.services.standard.hr.employeeListReport.ListHierarchyInBreathFirstSearch;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Hibernate example 14. Deleting an existing record.
 *
 */
public class BeanTests {

	public static List<OrgGroup> groupsInBreadFirstTest;

	public static void displaySubGroups(BEmployee employee, BOrgGroup og) {
		//given an OrgGroup, return a list of sub groups in breath first approach
		Set<OrgGroupHierarchy> myg = og.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		List<String> gids = new ArrayList();
		for (OrgGroupHierarchy oga2 : myg) {
			gids.add(oga2.getOrgGroupByChildGroupId().getOrgGroupId());
			if (isEmployeeInThisGroup(employee, oga2.getOrgGroupByChildGroupId().getOrgGroupId())) {
				groupsInBreadFirstTest.add(og.getOrgGroup());
				System.out.println("    Group Added " + oga2.getOrgGroupByChildGroupId().getName());
			}
		}
		for (String id : gids) {

			BOrgGroup og2 = new BOrgGroup(id);
			//System.out.println(" Next level for Group " + og2.getName());
			displaySubGroups(employee, og2);
		}
	}

	private static void showHier(Employee employee) {
		Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();

		List<OrgGroup> ogList = new ArrayList<OrgGroup>();

		if (assocs.size() > 0) {
			OrgGroupAssociation oga = assocs.iterator().next();
			OrgGroup og = oga.getOrgGroup();
			ogList.add(og);
			System.out.println(" hier first " + og.getName());

			while (og.getOrgGroupHierarchiesForChildGroupId().size() > 0) {
				og = og.getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId();

				ogList.add(og);
			}
			for (OrgGroup g : ogList)
				System.out.println("OG is" + g.getName());
		}
	}

	private static boolean isEmployeeInThisGroup(BEmployee employee, String groupId) {
		Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();
		//System.out.println("isEmployeeInThisGroup");
		if (assocs.size() > 0)
			for (OrgGroupAssociation oga : assocs) {
				OrgGroup og = oga.getOrgGroup();
				if (og.getOrgGroupId().equals(groupId))
					//System.out.println("Matched Gid=" + groupId + " to oga=" + og.getOrgGroupId());
					return true;
			}
		return false;
	}

	private static void buildHierarchyInBreathFirstSearch(BOrgGroup og, BEmployee employee, List<String> groupsInBreadthFirst) {
		//given an OrgGroup, return a list of sub groups in breath first approach
		Set<OrgGroupHierarchy> myg = og.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		List<String> gids = new ArrayList();
		for (OrgGroupHierarchy oga2 : myg) {
			OrgGroup x = oga2.getOrgGroupByChildGroupId();
			if (isEmployeeInThisGroup(employee, x.getOrgGroupId())) {
				gids.add(oga2.getChildGroupId());
				//System.out.println("Added group id " + x.getName());
				groupsInBreadthFirst.add(oga2.getChildGroupId());
			}
		}
		for (String id : gids) {
			//recurse on sub groups to get other groups underneath it
			BOrgGroup og2 = new BOrgGroup(id);
			buildHierarchyInBreathFirstSearch(og2, employee, groupsInBreadthFirst);
		}
	}

	private static void buildHierarchyInBreathFirstSearch2(String thisLevelId, BOrgGroup og, BEmployee employee, List<String> groupsInBreadthFirst) {
		//given an OrgGroup, return a list of sub groups in breath first approach
		Set<OrgGroupHierarchy> myg = og.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		List<String> gids = new ArrayList();
		for (OrgGroupHierarchy oga2 : myg) {
			OrgGroup x = oga2.getOrgGroupByChildGroupId();
			if (isEmployeeInThisGroup(employee, x.getOrgGroupId())) {
				gids.add(oga2.getChildGroupId());
				//System.out.println("Added group id " + x.getName());
				groupsInBreadthFirst.add(oga2.getChildGroupId());
			}
		}
		for (String id : gids) {
			//recurse on sub groups to get other groups underneath it
			BOrgGroup og2 = new BOrgGroup(id);
			buildHierarchyInBreathFirstSearch(og2, employee, groupsInBreadthFirst);
		}
	}

	public static String printOrganization(BEmployee emp, Integer orgNumber) {
		BOrgGroup og = new BOrgGroup(emp.getOrgGroupId());
		List<String> groupsInBreadthFirst = new ArrayList();

		BOrgGroup ogx = new BOrgGroup(emp.getOrgGroupId());
		//need to add the top level org to group before getting all it's sub groups
		groupsInBreadthFirst.add(ogx.getOrgGroupId());
		buildHierarchyInBreathFirstSearch(ogx, emp, groupsInBreadthFirst);

		//add top level company if it does not exist already
		if (!groupsInBreadthFirst.contains(ogx.getCompanyId()))
			groupsInBreadthFirst.add(0, ogx.getCompanyId());
		for (String s : groupsInBreadthFirst) {
			BOrgGroup gg = new BOrgGroup(s);
			//System.out.println("***  group " + gg.getOrgGroup().getName() + " Max Depth " + og.getMaxDepth());
		}

		if (orgNumber <= groupsInBreadthFirst.size()) {
			String id = groupsInBreadthFirst.get(orgNumber - 1);
			BOrgGroup gg = new BOrgGroup(id);
			return gg.getName();
		}
		return "";
	}

	public static void getGA(Employee employee) {
		Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();
		for (OrgGroupAssociation oga : assocs) {
			OrgGroup og = oga.getOrgGroup();
			System.out.println("GA " + og.getName() + employee.getCompanyBase().getCompanyId());
		}
	}

	public static void main(final String args[]) {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
//			hsu.setCurrentPersonToArahant();
//			hsu.beginTransaction();
//
//                        List<String> empIds = new ArrayList<String>();
//                        empIds.add("00000-0000000004");
//                        empIds.add("00000-0000000005");
//
//                        for (String empid : empIds){
//                            BEmployee emp = new BEmployee(empid);
//                            BOrgGroup og = new BOrgGroup(emp.getOrgGroupId());
//
//                            groupsInBreadFirstTest = new ArrayList();
//                             //System.out.println("Company is " + emp.getEmployee().getCompanyBase().getName() + " id" + og.getCompanyId());
//                             groupsInBreadFirstTest.add(og.getOrgGroup());
//
//                             //displaySubGroups(og);
//                            printOrgGroups(emp,3);
//                        }

			List<String> empIds = new ArrayList<String>();
			groupsInBreadFirstTest = new ArrayList();
			empIds.add("00000-0000000001");
			empIds.add("00000-0000000005");
			empIds.add("00000-0000000008");

			for (String id : empIds) {
				BEmployee emp = new BEmployee(id);
				System.out.println("======" + id);
				//showHier(emp.getEmployee());
				//getGA(emp.getEmployee());
				// displaySubGroups(emp,new BOrgGroup("",emp.getOrgGroupId()));
				ListHierarchyInBreathFirstSearch bh = new ListHierarchyInBreathFirstSearch();
				System.out.println(bh.getOrganizationLevel(emp, 1));
				System.out.println(bh.getOrganizationLevel(emp, 2));
				System.out.println(bh.getOrganizationLevel(emp, 3));
				System.out.println(bh.getOrganizationLevel(emp, 4));
				System.out.println(bh.getOrganizationLevel(emp, 5));

				BOrgGroup co = new BOrgGroup(emp.getCompany().getOrgGroupId());

				System.out.println("CO IS " + co.getName());
				displaySubGroups(emp, co);
				if (groupsInBreadFirstTest.isEmpty()) {
					System.out.println(co.getCompanyName());
					System.out.println(emp.getOrgGroupName());
					System.out.println(emp.getOrgGroupId());
					BOrgGroup cwo = new BOrgGroup(emp.getOrgGroupId());
					System.out.println(cwo.getName());
				}
			}

			//showHier(emp.getEmployee());

			hsu.commitTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
