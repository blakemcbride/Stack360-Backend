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


package com.arahant.services.standard.project.viewParent;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * Created on Nov 6, 2008
 */
public class ExportProjectViewInput extends TransmitInputBase {
	@Validation (required=true)
	private String id;
	@Validation (min=1,max=2,required=true)
	private int exportType;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public int getExportType() {
		return exportType;
	}

	public void setExportType(int exportType) {
		this.exportType = exportType;
	}
}

	