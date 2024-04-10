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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.inventory.productInventory;

import com.arahant.business.BItemInspection;

/**
 *
 */
public class NewLotInputItem {

	private int inspectionDate;
	private String inspectorId;
	private String status;
	private String comments;
	private String inspectorName; //place holder for front end, not used by server


	void setData(BItemInspection ii) {
		ii.setInspectionDate(inspectionDate);
		ii.setInspectorId(inspectorId);
		ii.setStatus(status);
		ii.setComments(comments);
	}


	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}


	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(int inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getInspectorId() {
		return inspectorId;
	}

	public void setInspectorId(String inspectorId) {
		this.inspectorId = inspectorId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



}
