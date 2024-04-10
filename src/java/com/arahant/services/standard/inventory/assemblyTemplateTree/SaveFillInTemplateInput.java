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
 * 
 */
package com.arahant.services.standard.inventory.assemblyTemplateTree;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class SaveFillInTemplateInput extends TransmitInputBase {
	
	@Validation (required=false)
	private SaveFillInTemplateInputItem[] tree;
	@Validation (required=true)
	private String finalLocationId;
	@Validation (required=false)
	private String serialNumber;

	public SaveFillInTemplateInputItem[] getTree()
	{
		if (tree==null)
			tree= new SaveFillInTemplateInputItem[0];
		return tree;
	}
	public void setTree(SaveFillInTemplateInputItem[] tree)
	{
		this.tree=tree;
	}
	public String getFinalLocationId() {
		return finalLocationId;
	}

	public void setFinalLocationId(String finalLocationId) {
		this.finalLocationId = finalLocationId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}

	
