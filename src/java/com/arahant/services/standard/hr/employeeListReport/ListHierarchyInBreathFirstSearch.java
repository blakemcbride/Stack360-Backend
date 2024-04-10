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
package com.arahant.services.standard.hr.employeeListReport;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListHierarchyInBreathFirstSearch {

	public List<String> orgNameInBreathFirstSearch;

	public ListHierarchyInBreathFirstSearch() {
	}

	private boolean isEmployeeInThisGroup(BEmployee employee, String groupId) {
		//check if the employee is in the group in this org level
		Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();
		if (assocs.size() > 0)
			for (OrgGroupAssociation oga : assocs) {
				OrgGroup og = oga.getOrgGroup();
				if (og.getOrgGroupId().equals(groupId))
					return true;
			}
		return false;
	}

	private void buildHierarchyInBreathFirstSearch(BOrgGroup og, BEmployee employee, List<String> groupsInBreadthFirst) {
		//given an OrgGroup, return a list of sub groups in breath first approach
		Set<OrgGroupHierarchy> myg = og.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		Iterator ith = myg.iterator();
		@SuppressWarnings("unchecked")
		List<String> gids = new ArrayList();

		while (ith.hasNext()) {
			OrgGroupHierarchy oga2 = (OrgGroupHierarchy) ith.next();

			//check it this employee belong to this group??
			//does this group contain the the group assocation?
			gids.add(oga2.getOrgGroupByChildGroupId().getOrgGroupId());
			if (isEmployeeInThisGroup(employee, oga2.getOrgGroupByChildGroupId().getOrgGroupId())) {
				groupsInBreadthFirst.add(oga2.getOrgGroupByChildGroupId().getOrgGroupId());
				orgNameInBreathFirstSearch.add(oga2.getOrgGroupByChildGroupId().getName());
			}
		}
		for (String id : gids) {

			BOrgGroup og2 = new BOrgGroup(id);
			//System.out.println(" Next level for Group " + og2.getName());
			buildHierarchyInBreathFirstSearch(og2, employee, groupsInBreadthFirst);
		}
	}

	@SuppressWarnings("unchecked")
	public String getOrganizationLevel(BEmployee emp, Integer orgLevel) {
		try {
			List<String> groupsInBreadthFirst = new ArrayList();
			orgNameInBreathFirstSearch = new ArrayList();

			//get the top level company and walk down the hierarchy
			BOrgGroup ogx = new BOrgGroup(emp.getCompany().getOrgGroupId());

			buildHierarchyInBreathFirstSearch(ogx, emp, groupsInBreadthFirst);

			if (groupsInBreadthFirst.isEmpty())
				//this person is probably not in the hierarchy
				//get the org from the employee's group id
				groupsInBreadthFirst.add(emp.getOrgGroupId());
			else
				//add top level company if it does not exist already
				if (!groupsInBreadthFirst.contains(ogx.getCompanyId()))
					groupsInBreadthFirst.add(0, ogx.getCompanyId());

			if (orgLevel <= groupsInBreadthFirst.size()) {
				String id = groupsInBreadthFirst.get(orgLevel - 1);
				BOrgGroup orgGroup = new BOrgGroup(id);
				return orgGroup.getName();
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void walkUpTheTreeAndGetTheGroups(OrgGroup currentOrg, String orgId, List orgList) {
		//stop walking up when we reach ORGROUP_ID = orgId
		if (currentOrg.getOrgGroupId().equalsIgnoreCase(orgId))
			return;
		//get my parent and it's name and id
		BOrgGroup parent = new BOrgGroup(currentOrg);
		BOrgGroup grandParent = parent.getParent();
		orgList.add(0, grandParent.getName()); //add each org to the top of the list
		walkUpTheTreeAndGetTheGroups(grandParent.getOrgGroup(), orgId, orgList);
	}

	public List getEmployeeGroupAssociationHierarchy(String empId, String orgId) {
		BEmployee emp = new BEmployee(empId);
		Set<OrgGroupAssociation> assocs = emp.getOrgGroupAssociations();

		BOrgGroup group = new BOrgGroup(orgId);
		List<String> groups = group.getAllOrgGroupsInHierarchy();
		@SuppressWarnings("unchecked")
		List<String> orgList = new ArrayList();

		//loop through the groups and see if this Employee' Org is in it
		//we need to do this because an employee can be in multiple locations in the hierarchy
		//but we're interested if only the ORGROUP_ID = orgID
		for (String gr : groups)
			for (OrgGroupAssociation og : assocs)
				if (gr.equalsIgnoreCase(og.getOrgGroupId())) {
					//System.out.println("Org " + og.getOrgGroupId() + "  " + og.getOrgGroup().getName());
					orgList.add(0, og.getOrgGroup().getName());
					walkUpTheTreeAndGetTheGroups(og.getOrgGroup(), orgId, orgList);
					break;
				}
		return orgList;
	}
}
