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


/**
 * Created on Dec 7, 2006
 * 
 */
package com.arahant.utils;



/**
 * 
 *
 * Created on Dec 7, 2006
 *
 */
public interface ArahantConstants {

	
	public static final String ARAHANT_SUPERUSER = ArahantSession.systemName();
	
	public static final int APPLICANT_TYPE = 0;
	public static final int EMPLOYEE_TYPE = 1;
	public static final int MANAGER_TYPE = 2;
	public static final int ACCOUNTANT_TYPE = 3;
	public static final int OWNER_TYPE = 4;
	public static final int COMPANY_TYPE = 1;
	public static final int CLIENT_TYPE = 2;
	public static final int PROSPECT_TYPE = 8;
	public static final int VENDOR_TYPE = 4;
    public static final int AGENT_TYPE = 16;
	public static final int PHONE_WORK = 1;
	public static final int PHONE_HOME = 2;
	public static final int PHONE_CELL = 3;
	public static final int PHONE_FAX = 4;
	public static final int ADDR_WORK = 1;
	public static final int ADDR_HOME = 2;
	/**
	 * 
	 */
	public static final char TIMESHEET_SUBMITTED = 'S';
	/**
	 * 
	 */
	public static final char TIMESHEET_CHANGED = 'C';
	/**
	 * 
	 */
	public static final char TIMESHEET_FIXED = 'F';
	/**
	 * 
	 */
	public static final char TIMESHEET_NEW = 'N';
	/**
	 * 
	 */
	public static final char TIMESHEET_REJECTED = 'R';
	/**
	 * 
	 */
	public static final char TIMESHEET_INVOICED = 'I';
	/**
	 * 
	 */
	public static final char TIMESHEET_APPROVED = 'A';
	
	public static final char TIMESHEET_PROBLEM = 'P';

	
	/**
	 * 
	 */
	public static final int ACCESS_LEVEL_WRITE = 2;
	/**
	 * 
	 */
	public static final int ACCESS_LEVEL_READ_ONLY = 1;
	/**
	 * 
	 */
	public static final int ACCESS_LEVEL_NOT_VISIBLE = 0;

	public static final String GOOGLE_API_KEY = "AIzaSyDSKtRUxiGkwGV2ZhenaPh43Fn21snT2hM";  // Arahant's Google key
}

	
