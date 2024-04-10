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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.business.interfaces;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public interface ProjectReportDataInterface {

	/**
	 * @return Returns the approved.
	 */
	public abstract boolean isApproved();

	/**
	 * @param approved The approved to set.
	 */
	public abstract void setApproved(boolean approved);

	/**
	 * @return Returns the client_company_id.
	 */
	public abstract String getClientCompanyId();

	/**
	 * @param client_company_id The client_company_id to set.
	 */
	public abstract void setClientCompanyId(String client_company_id);

	/**
	 * @return Returns the employee_id.
	 */
	public abstract String getEmployeeId();

	/**
	 * @param employee_id The employee_id to set.
	 */
	public abstract void setEmployeeId(String employee_id);

	/**
	 * @return Returns the end_date.
	 */
	public abstract int getEndDate();

	/**
	 * @param end_date The end_date to set.
	 */
	public abstract void setEndDate(int end_date);

	/**
	 * @return Returns the invoiced.
	 */
	public abstract boolean isInvoiced();

	/**
	 * @param invoiced The invoiced to set.
	 */
	public abstract void setInvoiced(boolean invoiced);

	/**
	 * @return Returns the org_group_id.
	 */
	public abstract String getOrgGroupId();

	/**
	 * @param org_group_id The org_group_id to set.
	 */
	public abstract void setOrgGroupId(String org_group_id);

	/**
	 * @return Returns the project_name.
	 */
	public abstract String getProjectId();


	public abstract void setProjectId(String projectId);

	/**
	 * @return Returns the start_date.
	 */
	public abstract int getStartDate();

	/**
	 * @param start_date The start_date to set.
	 */
	public abstract void setStartDate(int start_date);

	/**
	 * @return Returns the notApproved.
	 */
	public abstract boolean isNotApproved();

	/**
	 * @param notApproved The notApproved to set.
	 */
	public abstract void setNotApproved(boolean notApproved);

}
