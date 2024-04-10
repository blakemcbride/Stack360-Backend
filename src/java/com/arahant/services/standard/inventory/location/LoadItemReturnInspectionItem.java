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

package com.arahant.services.standard.inventory.location;

import com.arahant.business.BItemInspection;

/**
 *
 */
public class LoadItemReturnInspectionItem {
	private String comments;
	private String inspectorName;
	private int inspectionDate;
	private String status;

	public LoadItemReturnInspectionItem()
	{

	}

	LoadItemReturnInspectionItem(BItemInspection i)
	{
		comments=i.getComments();
		inspectorName=i.getInspectorName();
		inspectionDate=i.getInspectionDate();
		status=i.getStatus();
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

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
