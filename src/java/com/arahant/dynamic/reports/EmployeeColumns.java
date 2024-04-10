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
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EmployeeColumns {

	private static final List<DynamicReportColumn> columns = new ArrayList<DynamicReportColumn>()
	{{									//hibernate column name      //column description           //compare type	//type (for displaying)				//owner class   //join column path
//		add(new DynamicReportColumn(1,  Person.FNAME,               "First Name",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(2,  Person.LNAME,               "Last Name",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(3,  Person.MNAME,               "Middle Name",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(4,  Person.NICKNAME,            "Nickname",						String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(5,  Person.SSN,					"SSN",							String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(6,  Person.SEX,					"Sex",							char.class,		DynamicReportColumn.TYPE_CHARACTER,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(7,  Employee.EXTREF,            "External ID",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(8,  Person.DOB,					"DOB",							int.class,		DynamicReportColumn.TYPE_DATE,		Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(9,  Person.PERSONALEMAIL,       "Email",						String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{}, ""));
//		add(new DynamicReportColumn(10, Address.STREET,				"Street 1",						String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{1}, ""));
//		add(new DynamicReportColumn(11, Address.STREET2,			"Street 2",						String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{1}, ""));
//		add(new DynamicReportColumn(12, Address.CITY,				"City",							String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{1}, ""));
//		add(new DynamicReportColumn(13, Address.STATE,				"State",						String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{1}, ""));
//		add(new DynamicReportColumn(14, Address.ZIP,				"Zip",							String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{1}, ""));
//		add(new DynamicReportColumn(15, OrgGroup.NAME,				"Organizational Group Name",	String.class,   DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{3, 4}, OrgGroup.ORGGROUPID));
		//add(new DynamicReportColumn(15, Phone.PHONENUMBER,			"Home Phone",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{2}, ""));
		//add(new DynamicReportColumn(16, Phone.PHONENUMBER,			"Work Phone",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{2}, ""));
		//add(new DynamicReportColumn(17, Phone.PHONENUMBER,			"Cell Phone",					String.class,	DynamicReportColumn.TYPE_STRING,	Employee.class, new int[]{2}, ""));
	}};

	private static final List<DynamicJoinColumn> joinColumns = new ArrayList<DynamicJoinColumn>()
	{{
		add(new DynamicJoinColumn(1, Person.ADDRESSES, Address.class));
		add(new DynamicJoinColumn(2, Person.PHONES, Phone.class));
		add(new DynamicJoinColumn(3, Person.ORGGROUPASSOCIATIONS, OrgGroupAssociation.class));
		add(new DynamicJoinColumn(4, OrgGroupAssociation.ORGGROUP, OrgGroup.class));
	}};

	public EmployeeColumns() {
	}

	public static Class getOwnerClass()
	{
		return Employee.class;
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
