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
import java.util.Date;
import java.util.List;

/**
 *
 * Arahant
 */
public class HrBenefitJoinColumns {

	private static final List<DynamicReportColumn> columns = new ArrayList<DynamicReportColumn>()
	{{									//hibernate column name            //column description         //compare type	//type (for displaying)				//control type								//available operators										//List selections			//break level sort compare				//owner class		 //join column path
		add(new DynamicReportColumn(1,  HrBenefitJoin.RECORD_CHANGE_DATE,  "Record Change Date",        Date.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(2,  HrBenefitJoin.RECORD_CHANGE_TYPE,  "Record Change Type",        char.class,		DynamicReportColumn.TYPE_CHARACTER,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT,	Arrays.asList("EQ", "NE"),									new RecordChangeTypeList(),	null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(3,  Person.FNAME,                      "Employee First Name",       String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{6}, ""));
		add(new DynamicReportColumn(4,  Person.LNAME,                      "Employee Last Name",        String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{6}, ""));
		add(new DynamicReportColumn(5,  Person.FNAME,                      "Covered Person First Name", String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{7}, ""));
		add(new DynamicReportColumn(6,  Person.LNAME,                      "Covered Person Last Name",  String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{7}, ""));
		add(new DynamicReportColumn(7,  HrBenefitConfig.NAME,              "Benefit Configuration",     String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new BenefitConfigsList(),	HrBenefitConfig.BENEFIT_CONFIG_ID,		HrBenefitJoin.class, new int[]{1}, HrBenefitConfig.BENEFIT_CONFIG_ID));
		add(new DynamicReportColumn(8,  HrBenefit.NAME,                    "Benefit",                   String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new BenefitsList(),			HrBenefit.BENEFITID,					HrBenefitJoin.class, new int[]{1, 4}, HrBenefit.BENEFITID));
		add(new DynamicReportColumn(9,  HrBenefitCategory.DESCRIPTION,     "Benefit Category",          String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new BenefitCategoriesList(),HrBenefitCategory.BENEFIT_CATEGORY_ID,	HrBenefitJoin.class, new int[]{1, 4, 5}, HrBenefitCategory.BENEFIT_CATEGORY_ID));
		add(new DynamicReportColumn(10, HrBenefitJoin.POLICY_START_DATE,   "Policy Start Date",         int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(11, HrBenefitJoin.POLICY_END_DATE,     "Policy End Date",           int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(12, HrBenefitJoin.COVERAGE_START_DATE, "Coverage Start Date",       int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(13, HrBenefitJoin.COVERAGE_END_DATE,   "Coverage End Date",         int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(14, Person.DOB,                        "Covered Person DOB",        int.class,		DynamicReportColumn.TYPE_DATE,		DynamicReportColumn.CONTROLTYPE_DATE,		Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{7}, ""));
		add(new DynamicReportColumn(15, HrBenefitJoin.AMOUNT_COVERED,      "Coverage Amount",           double.class,	DynamicReportColumn.TYPE_MONEY,		DynamicReportColumn.CONTROLTYPE_NUMERIC,	Arrays.asList("EQ", "NE", "GE", "LE", "GT", "LT"),			null,						null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(16, HrBenefitJoin.USING_COBRA,         "Cobra",                     char.class,		DynamicReportColumn.TYPE_CHARACTER,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT,	Arrays.asList("EQ", "NE"),									new CobraList(),			null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(17, Person.SSN,						   "Employee SSN",              String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{6}, ""));
		add(new DynamicReportColumn(18, Person.SSN,						   "Covered Person SSN",        String.class,	DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE", "LK"),							null,						Person.PERSONID,						HrBenefitJoin.class, new int[]{7}, ""));
		add(new DynamicReportColumn(19, VendorCompany.NAME,				   "Benefit Provider",			String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new VendorsList(),			VendorCompany.ORGGROUPID,				HrBenefitJoin.class, new int[]{1, 4, 8}, VendorCompany.ORGGROUPID));
		add(new DynamicReportColumn(20, OrgGroup.NAME,					   "Organizational Group Name",	String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_LIST,		Arrays.asList("EQ", "NE", "IN"),							new OrgGroupsList(),		OrgGroup.ORGGROUPID,					HrBenefitJoin.class, new int[]{6, 9, 10}, OrgGroup.ORGGROUPID));
		add(new DynamicReportColumn(21, HrBenefitJoin.APPROVED,			   "Approved",                  char.class,     DynamicReportColumn.TYPE_CHARACTER, DynamicReportColumn.CONTROLTYPE_TRI_SELECT, Arrays.asList("EQ", "NE"),									new ApprovedList(),			null,									HrBenefitJoin.class, new int[]{}, ""));
		add(new DynamicReportColumn(22, HrEmplDependent.RELATIONSHIP_TYPE, "Dependent Type",			String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT, Arrays.asList("EQ", "NE"),									new RelationshipList(),		null,									HrBenefitJoin.class, new int[]{11}, ""));
		add(new DynamicReportColumn(23, Address.STREET,					   "Employee Address 1",		String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 12}, Address.ADDRESSID));
		add(new DynamicReportColumn(24, Address.STREET2,				   "Employee Address 2",		String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 12}, Address.ADDRESSID));
		add(new DynamicReportColumn(25, Address.CITY,					   "Employee City",				String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 12}, Address.ADDRESSID));
		add(new DynamicReportColumn(26, Address.STATE,					   "Employee State",			String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 12}, Address.ADDRESSID));
		add(new DynamicReportColumn(27, Address.ZIP,					   "Employee Zip",				String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 12}, Address.ADDRESSID));
		add(new DynamicReportColumn(28, Phone.PHONENUMBER,				   "Phone",						String.class,   DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TEXT,		Arrays.asList("EQ", "NE"),									null,						null,									HrBenefitJoin.class, new int[]{6, 13}, Phone.PHONEID));
		add(new DynamicReportColumn(29, Person.SEX,						   "Dependent Gender",			char.class,		DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT,	Arrays.asList("EQ", "NE"),									new GenderList(),			Person.PERSONID,						HrBenefitJoin.class, new int[]{7}, ""));
		add(new DynamicReportColumn(30, Person.SEX,						   "Employee Gender",			char.class,		DynamicReportColumn.TYPE_STRING,	DynamicReportColumn.CONTROLTYPE_TRI_SELECT,	Arrays.asList("EQ", "NE"),									new GenderList(),			Person.PERSONID,						HrBenefitJoin.class, new int[]{6}, ""));
	}};

	private static final List<DynamicJoinColumn> joinColumns = new ArrayList<DynamicJoinColumn>()
	{{
		add(new DynamicJoinColumn(1, HrBenefitJoin.HR_BENEFIT_CONFIG, HrBenefitConfig.class));
		add(new DynamicJoinColumn(2, HrBenefitJoin.HRBENEFIT, HrBenefit.class));
		add(new DynamicJoinColumn(3, HrBenefitJoin.HR_BENEFIT_CATEGORY, HrBenefitCategory.class));
		add(new DynamicJoinColumn(4, HrBenefitConfig.HR_BENEFIT, HrBenefit.class));
		add(new DynamicJoinColumn(5, HrBenefit.BENEFIT_CATEGORY, HrBenefitCategory.class));
		add(new DynamicJoinColumn(6, HrBenefitJoin.PAYING_PERSON, Person.class));
		add(new DynamicJoinColumn(7, HrBenefitJoin.COVERED_PERSON, Person.class));
		add(new DynamicJoinColumn(8, HrBenefit.BENEFIT_PROVIDER, VendorCompany.class));
		add(new DynamicJoinColumn(9, Person.ORGGROUPASSOCIATIONS, OrgGroupAssociation.class));
		add(new DynamicJoinColumn(10, OrgGroupAssociation.ORGGROUP, OrgGroup.class));
		add(new DynamicJoinColumn(11, HrBenefitJoin.RELATIONSHIP, HrEmplDependent.class));
		add(new DynamicJoinColumn(12, Person.ADDRESSES, Address.class));
		add(new DynamicJoinColumn(13, Person.PHONES, Phone.class));
	}};

	public HrBenefitJoinColumns() {
	}

	public static Class getOwnerClass()
	{
		return HrBenefitJoin.class;
	}

	private static class BenefitConfigsList implements ISelectionList {
		@Override
		public List<HrBenefitConfig> list() {
			return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID, HrBenefitConfig.NAME).list();
		}
	}

	private static class BenefitsList implements ISelectionList {
		@Override
		public List<HrBenefit> list() {
			return ArahantSession.getHSU().createCriteria(HrBenefit.class).selectFields(HrBenefit.BENEFITID, HrBenefit.NAME).list();
		}
	}

	private static class BenefitCategoriesList implements ISelectionList {
		@Override
		public List<HrBenefitCategory> list() {
			return ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.BENEFIT_CATEGORY_ID, HrBenefitCategory.DESCRIPTION).list();
		}
	}

	private static class VendorsList implements ISelectionList {
		@Override
		public List<VendorCompany> list() {
			return ArahantSession.getHSU().createCriteria(VendorCompany.class).selectFields(VendorCompany.ORGGROUPID, VendorCompany.NAME).list();
		}
	}

	private static class OrgGroupsList implements ISelectionList {
		@Override
		public List<OrgGroup> list() {
			return ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).selectFields(OrgGroup.ORGGROUPID, OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE).list();
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

	private static class CobraList implements ISelectionList {
		@Override
		public List<String> list() {
			List<String> l = new ArrayList<String>();
			l.add("Yes");
			l.add("No");
			return l;
		}
	}

	private static class ApprovedList implements ISelectionList {
		@Override
		public List<String> list() {
			List<String> l = new ArrayList<String>();
			l.add("Yes");
			l.add("No");
			return l;
		}
	}

	private static class RelationshipList implements ISelectionList {
		@Override
		public List<String> list() {
			List<String> l = new ArrayList<String>();
			l.add("Spouse");
			l.add("Child");
			return l;
		}
	}

	private static class GenderList implements ISelectionList {
		@Override
		public List<String> list() {
			List<String> l = new ArrayList<String>();
			l.add("Male");
			l.add("Female");
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

	public static void main(String[] args) {
		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class, "00001-0000073686"));

		List<Employee> emps = ArahantSession.getHSU().createCriteria(Employee.class).activeEmployee()
																					.list();
		List<Person> deps = ArahantSession.getHSU().createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME)
																				.activeDependent()
																				.joinTo(Person.DEP_JOINS_AS_DEPENDENT)
																				.in(HrEmplDependent.EMPLOYEE, emps)
																				.list();

		String in = "select p.lname, p.fname, p.ssn, p.sex from person p where p.person_id in (";
		for(Person p : deps) {
			in += ("\'" + p.getPersonId() + "\'");
			in += ",";
		}
		in = in.substring(0, in.length() - 1);

		in += ") order by p.lname, p.fname;";

		System.out.println(in);
	}
}
