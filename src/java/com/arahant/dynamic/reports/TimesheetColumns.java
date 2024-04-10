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




package com.arahant.dynamic.reports;

import com.arahant.beans.*;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class TimesheetColumns {

	private static final List<DynamicReportColumn> columns = new ArrayList<DynamicReportColumn>()
	{{									//hibernate column name            //column description         //compare type	//type (for displaying)				//control type								//available operators										//List selections	 //break level sort 	//owner class		 //join column path
		add(new DynamicReportColumn(1,  Timesheet.TOTALHOURS,			   "Total Hours",				double.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_NUMERIC,	Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,				 null,					Timesheet.class, new int[]{}, ""));
		add(new DynamicReportColumn(2,  OrgGroup.NAME,					   "Organizational Group Name",	String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new OrgGroupsList(), OrgGroup.ORGGROUPID,	Timesheet.class, new int[]{4, 5}, OrgGroup.ORGGROUPID));
		add(new DynamicReportColumn(3,  Person.FNAME,                      "Employee First Name",       String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "IN", "LK"),						null,				 Person.PERSONID,		Timesheet.class, new int[]{1}, ""));
		add(new DynamicReportColumn(4,  Person.LNAME,                      "Employee Last Name",        String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "IN", "LK"),						null,				 Person.PERSONID,		Timesheet.class, new int[]{1}, ""));
		add(new DynamicReportColumn(5,  Timesheet.WORKDATE,				   "Work Date",					int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,				 null,					Timesheet.class, new int[]{}, ""));
		add(new DynamicReportColumn(6,  Project.PROJECTNAME,			   "Project",					String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new ProjectsList(),	 Project.PROJECTID,		Timesheet.class, new int[]{4}, Project.PROJECTID));
//		add(new DynamicReportColumn(8,  HrBenefit.NAME,                    "Benefit",                   String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new BenefitsList(),					HrBenefitJoin.class, new int[]{1, 4}, HrBenefit.BENEFITID));
//		add(new DynamicReportColumn(9,  HrBenefitCategory.DESCRIPTION,     "Benefit Category",          String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new BenefitCategoriesList(),		HrBenefitJoin.class, new int[]{1, 4, 5}, HrBenefitCategory.BENEFIT_CATEGORY_ID));
//		add(new DynamicReportColumn(10, HrBenefitJoin.POLICY_START_DATE,   "Policy Start Date",         int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(11, HrBenefitJoin.POLICY_END_DATE,     "Policy End Date",           int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(12, HrBenefitJoin.COVERAGE_START_DATE, "Coverage Start Date",       int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(13, HrBenefitJoin.COVERAGE_END_DATE,   "Coverage End Date",         int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(14, Person.DOB,                        "Covered Person DOB",        int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{7}, ""));
//		add(new DynamicReportColumn(15, HrBenefitJoin.AMOUNT_COVERED,      "Coverage Amount",           double.class,	DynamicReportColumn.TYPE_MONEY,		DynamicReportColumn.CONTROLTYPE_NUMERIC,	Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,								HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(16, HrBenefitJoin.USING_COBRA,         "Cobra",                     char.class,		DynamicReportColumn.TYPE_CHARACTER,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT,	Arrays.asList("EQ", "NE"),									new CobraList(),					HrBenefitJoin.class, new int[]{}, ""));
//		add(new DynamicReportColumn(17, Person.SSN,						   "Employee SSN",              String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "IN", "LK"),						null,								HrBenefitJoin.class, new int[]{6}, ""));
//		add(new DynamicReportColumn(18, Person.SSN,						   "Covered Person SSN",        String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "IN", "LK"),						null,								HrBenefitJoin.class, new int[]{7}, ""));
//		add(new DynamicReportColumn(19, VendorCompany.NAME,				   "Benefit Provider",			String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new VendorsList(),					HrBenefitJoin.class, new int[]{1, 4, 8}, VendorCompany.ORGGROUPID));
//		add(new DynamicReportColumn(21, HrBenefitJoin.APPROVED,			   "Approved",                  char.class,   HrBenefitJoin.class, new int[]{}, ""));
	}};

	private static final List<DynamicJoinColumn> joinColumns = new ArrayList<DynamicJoinColumn>()
	{{
		add(new DynamicJoinColumn(1, Timesheet.PERSON, Person.class));
		add(new DynamicJoinColumn(2, Person.ORGGROUPASSOCIATIONS, OrgGroupAssociation.class));
		add(new DynamicJoinColumn(3, OrgGroupAssociation.ORGGROUP, OrgGroup.class));
// XXYY		add(new DynamicJoinColumn(4, Timesheet.PROJECT, Project.class));
		add(new DynamicJoinColumn(5, Project.REQUESTING_ORG_GROUP, OrgGroup.class));
//		add(new DynamicJoinColumn(2, HrBenefitJoin.HRBENEFIT, HrBenefit.class));
//		add(new DynamicJoinColumn(3, HrBenefitJoin.HR_BENEFIT_CATEGORY, HrBenefitCategory.class));
//		add(new DynamicJoinColumn(4, HrBenefitConfig.HR_BENEFIT, HrBenefit.class));
//		add(new DynamicJoinColumn(5, HrBenefit.BENEFIT_CATEGORY, HrBenefitCategory.class));
//		add(new DynamicJoinColumn(7, HrBenefitJoin.COVERED_PERSON, Person.class));
//		add(new DynamicJoinColumn(8, HrBenefit.BENEFIT_PROVIDER, VendorCompany.class));
	}};

	public TimesheetColumns() {
	}

	public static Class<?> getOwnerClass()
	{
		return HrBenefitJoin.class;
	}

	private static class OrgGroupsList implements ISelectionList {
		@Override
		public List<OrgGroup> list() {
			return ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).selectFields(OrgGroup.ORGGROUPID, OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE).list();
		}
	}

	private static class ProjectsList implements ISelectionList {
		@Override
		public List<Project> list() {
			return ArahantSession.getHSU().createCriteria(Project.class).orderBy(Project.PROJECTNAME).selectFields(Project.PROJECTID, Project.PROJECTNAME).list();
		}
	}

	private static class RecordChangeTypeList implements ISelectionList {
		@Override
		public List<String> list() {
			List<String> l = new ArrayList<String>();
			l.add("New");
			l.add("Modify");
			l.add("Delete");
			return l;
		}
	}

	public static List<DynamicReportColumn> getColumns() {
		List<DynamicReportColumn> cols = new ArrayList<DynamicReportColumn>();

		for(DynamicReportColumn d : columns)
			cols.add(d);
		
		return cols;
	}

	public static DynamicReportColumn getColumnByName(String name) {
		for (DynamicReportColumn column : columns)
			if (column.getColumnName().equals(name))
				return column;
		return null;
	}

	public static DynamicReportColumn getColumnByIndex(int index)
	{
		return columns.get(index - 1);
	}

	public static List<DynamicJoinColumn> getJoinColumns() {
		List<DynamicJoinColumn> cols = new ArrayList<DynamicJoinColumn>();

		for(DynamicJoinColumn d : joinColumns)
			cols.add(d);

		return cols;
	}

	public static DynamicJoinColumn getJoinColumnByName(String name) {
		for (DynamicJoinColumn column : joinColumns)
			if (column.getJoinColumn().equals(name))
				return column;
		return null;
	}

	public static DynamicJoinColumn getJoinColumnByIndex(int index)
	{
		return joinColumns.get(index - 1);
	}
}
