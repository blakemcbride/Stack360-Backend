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
 *
 * Created on Oct 13, 2006
 */
package com.arahant.services.standard.billing.createTimesheetInvoice;

public class NewInvoiceLineItemTransmit  {

	private String[] timesheetIds;
	private String serviceId;
	private float adjHours;  // used in types H and D
	private double adjRate;  // used in types H and D
	private String description;
	private String type;  //  (H)ourly, (P)roject based billing, (D)ollar amount
	private double lineAmount;  //  for project-based billing only (type P)
	private String projectId;  //  for project-based billing only (type P)

	/**
	 * @return Returns the adjHours.
	 */
	public float getAdjHours() {
		return adjHours;
	}
	/**
	 * @param adjHours The adjHours to set.
	 */
	public void setAdjHours(final float adjHours) {
		this.adjHours = adjHours;
	}
	/**
	 * @return Returns the adjRate.
	 */
	public double getAdjRate() {
		return adjRate;
	}
	/**
	 * @param adjRate The adjRate to set.
	 */
	public void setAdjRate(final double adjRate) {
		this.adjRate = adjRate;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(double lineAmount) {
		this.lineAmount = lineAmount;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return Returns the timesheetIds.
	 */
	public String[] getTimesheetIds() {
		if (timesheetIds==null)
			return new String[0];
		return timesheetIds;
	}
	/**
	 * @param timesheetIds The timesheetIds to set.
	 */
	public void setTimesheetIds(final String[] timesheetIds) {
		this.timesheetIds = timesheetIds;
	}


}

	
