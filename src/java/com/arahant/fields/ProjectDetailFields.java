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


package com.arahant.fields;


public class ProjectDetailFields {
	public static final String APPROVAL_DATE_TIME = "Approval Date/Time";
	public static final String APPROVAL_ENTERED_BY = "Approval Entered By";
	public static final String APPROVED_BY = "Approved By";
	public static final String BENEFIT_ASSOCIATIONS = "Benefit Associations";
	public static final String BILLABLE_STATUS = "Billable Status";
	public static final String ACCESIBLE_TO_ALL = "Show on Timesheet Project Quick List for all Employees";
	public static final String ACTUAL_BILLABLE = "Actual Billable";
	public static final String ACTUAL_NON_BILLABLE = "Actual Non-Billable";
	public static final String ASSIGNED_PEOPLE = "Assigned People";
	public static final String BILLING_RATE = "Billing Rate";
	public static final String CATEGORY = "Category";
	public static final String CHECKLISTS = "Current Route Stop Check List Details";
	public static final String CLIENT_PRIORITY = "Client Priority";
	public static final String COMMENTS = "Comments";
	public static final String COMPANY_PRIORITY = "Company Priority";
	public static final String DECISION_POINT = "Current Route Stop Decision Point";
	public static final String DEF_BILLING_RATE = "Default Billing Rate";
	public static final String DEF_PROD_SERVICE = "Default Product/Service";
	public static final String DETAIL = "Detail";
	public static final String DOLLAR_CAP = "Dollar Cap";
	public static final String ESTIMATE_ON_DATE = "Estimated On Date";
	public static final String EST_BILLABLE = "Estimated Billable";
	public static final String EST_TIME_SPAN = "Estimated Time Span";
	public static final String FORMS = "Forms";
	public static final String GROUP_PRIORITY = "Organizational Group Priority";
	public static final String MANAGING_EMP = "Managing Employee";
	public static final String NAME = "ID";
	public static final String DATE_TIME_REPORTED = "Created Date/Time";
	public static final String PERCENT_COMPLETE = "Percent Complete";
	public static final String PROMISED_ON_DATE = "Promised On Date";
	public static final String REFERENCE = "External Reference";
	public static final String REQ_COMPANY = "Requesting Company";
	public static final String REQ_GROUP = "Requesting Organizational Group";
	public static final String REQ_PERSON = "Requesting Person";
	public static final String ROUTE = "Route";
	public static final String ROUTE_STOP_COMPANY = "Current Route Stop Company";
	public static final String ROUTE_STOP_GROUP = "Current Route Stop Organizational Group";
	public static final String ROUTE_STOP_PHASE = "Current Route Stop Phase";
	public static final String ROUTING_HISTORY = "Routing And Assignment History";
	public static final String SPONSORING_EMP = "Created By";
	public static final String STATUS = "Status";
	public static final String SUBPROJECTS = "Sub-Projects";
	public static final String SUMMARY = "Summary";
	public static final String TIMESHEETS = "Timesheets";
	public static final String TYPE = "Type";
	public static final String [] displayIds = new String[]{
		APPROVAL_DATE_TIME, APPROVAL_ENTERED_BY, APPROVED_BY, BENEFIT_ASSOCIATIONS, BILLABLE_STATUS, ACCESIBLE_TO_ALL,
		ACTUAL_BILLABLE, ACTUAL_NON_BILLABLE, ASSIGNED_PEOPLE, BILLING_RATE, CATEGORY, CHECKLISTS, CLIENT_PRIORITY, 
		COMMENTS, COMPANY_PRIORITY, DECISION_POINT, DEF_BILLING_RATE, DEF_PROD_SERVICE, DETAIL, DOLLAR_CAP, ESTIMATE_ON_DATE,
		EST_BILLABLE, EST_TIME_SPAN, FORMS, GROUP_PRIORITY, MANAGING_EMP, NAME, DATE_TIME_REPORTED, PERCENT_COMPLETE, PROMISED_ON_DATE,
		REFERENCE, REQ_COMPANY, REQ_GROUP, REQ_PERSON, ROUTE, ROUTE_STOP_COMPANY, ROUTE_STOP_GROUP, ROUTE_STOP_PHASE, ROUTING_HISTORY,
		SPONSORING_EMP, STATUS, SUBPROJECTS, SUMMARY, TIMESHEETS, TYPE
	};
	public static final String[][] displayGroups = new String[][] {
		{"Summary", SUMMARY, DETAIL, CATEGORY, TYPE, REFERENCE, DATE_TIME_REPORTED, SPONSORING_EMP, ACCESIBLE_TO_ALL, REQ_COMPANY, REQ_GROUP, REQ_PERSON, MANAGING_EMP, BILLABLE_STATUS, ASSIGNED_PEOPLE},
		{"Billing & Estimate", BILLABLE_STATUS, BILLING_RATE, DEF_BILLING_RATE, DOLLAR_CAP, DEF_PROD_SERVICE, EST_BILLABLE, ACTUAL_BILLABLE, ACTUAL_NON_BILLABLE, EST_TIME_SPAN, ESTIMATE_ON_DATE, PROMISED_ON_DATE, APPROVED_BY, APPROVAL_ENTERED_BY, APPROVAL_DATE_TIME},
		{"Status", CATEGORY, TYPE, ROUTE, ROUTE_STOP_COMPANY, ROUTE_STOP_GROUP, DECISION_POINT, STATUS, COMPANY_PRIORITY, GROUP_PRIORITY, CLIENT_PRIORITY, ASSIGNED_PEOPLE}
	};
}

	
