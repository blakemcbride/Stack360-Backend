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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class OrgGroupsTest {

	public HibernateSessionUtil hsu = ArahantSession.getHSU();

	public class ReportData {

		private String orgLable;
		private String empName;
		private int hireDate;
		private String title;

		/**
		 * @return the orgLable
		 */
		public String getOrgLable() {
			return orgLable;
		}

		/**
		 * @param orgLable the orgLable to set
		 */
		public void setOrgLable(String orgLable) {
			this.orgLable = orgLable;
		}

		/**
		 * @return the empName
		 */
		public String getEmpName() {
			return empName;
		}

		/**
		 * @param empName the empName to set
		 */
		public void setEmpName(String empName) {
			this.empName = empName;
		}

		/**
		 * @return the hireDate
		 */
		public int getHireDate() {
			return hireDate;
		}

		/**
		 * @param hireDate the hireDate to set
		 */
		public void setHireDate(int hireDate) {
			this.hireDate = hireDate;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
	
	static List<ReportData> reportDataList = new ArrayList<ReportData>();

	public void setReportData(String orgLable, Person p) {
		ReportData rd = new ReportData();
		BEmployee emp = new BEmployee(p.getPersonId());
		rd.setOrgLable(orgLable);
		rd.setEmpName(p.getNameLFM());
		rd.setHireDate(emp.getHireDate());
		rd.setTitle(emp.getJobTitle());
		reportDataList.add(rd);
	}

	public void getTopOrg(String orgId) {
		BOrgGroup og = new BOrgGroup(orgId);
		List<Person> personAssociation = hsu.createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, orgId).list();
		for (Person pp : personAssociation)
			setReportData(og.getOrgGroup().getName(), pp);
	}

	public void displayGroupsByOrgId2(String orgId, String orgLabel) {
		BOrgGroup og = new BOrgGroup(orgId);
		List<OrgGroupHierarchy> v = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, orgId).list();
		String prevLable = orgLabel;
		for (OrgGroupHierarchy o : v) {
			orgLabel += o.getOrgGroupByChildGroupId().getName() + " - ";
			//System.out.println(orgLabel);
			List<Person> personAssociation = hsu.createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, o.getChildGroupId()).list();

			for (Person pp : personAssociation)
				setReportData(orgLabel, pp);
			//do I have anymore under me?
			List<OrgGroupHierarchy> v2 = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, o.getChildGroupId()).list();
			if (v2.size() > 0)
				//recurse
				//orgLabel += "-" + o.getOrgGroupByChildGroupId().getName();
				displayGroupsByOrgId2(o.getChildGroupId(), orgLabel);
			orgLabel = prevLable;
		}
	}

	public void displayGroupsByOrgId(String orgId) {
		//get Group hierarchy for this org
		//List<OrgGroupHierarchy> v = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, orgId).list();
		BOrgGroup og = new BOrgGroup(orgId);
		List<String> groups = og.getAllOrgGroupsInHierarchy();
		int depth = 0;
		String[] orgNames = new String[depth];

		for (String id : groups) {
			BOrgGroup group = new BOrgGroup(id);
			System.out.println("Group is " + group.getName());

			//this gets all person in the hiearchy for the Parent
			//do this if depth has reached
			//Set<String> person = group.getAllPersonIdsForOrgGroupHierarchy(true);

			//this get person just for this orgId
			List<Person> personAssociation = hsu.createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, id).list();

			for (Person pp : personAssociation)
				System.out.println("\tPerson name is " + pp.getNameFL());
			System.out.println("\t--");
		}
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

	public List getEmployeeGroupAssociation(String empId, String orgId) {
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

	public static void main(String[] args) {
		OrgGroupsTest og = new OrgGroupsTest();
//       og.getTopOrg("00001-0000000014");
//        og.displayGroupsByOrgId2("00001-0000000014", "");
//        for (ReportData d : reportDataList) {
//            System.out.println(d.orgLable + " " + d.getEmpName());
//        }

		Person pp = new Person();

		//PrintObjectValues.print(pp);
		String pid = "00001-0000000581";
		Date d = new Date();
		System.out.println("Time  " + d.getTime());
		System.out.println("Time  " + (d.getTime() - 1000));
		//BProject proj = new BProject(pid);
		//System.out.println("Pr " + proj.getDetailDesc());
//        String orgId = "00001-0000000021";
//        String empId = "00001-0000000034";
//        List list = og.getEmployeeGroupAssociation(empId, orgId);
	}
}
