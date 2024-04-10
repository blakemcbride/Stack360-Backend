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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import org.kissweb.StringUtils;


public class ListFormatCodesReturnItem {

	private int formatCode;
	private String codeTemplate;

	public ListFormatCodesReturnItem() {

	}

	ListFormatCodesReturnItem(int code, DynamicReportColumn drc) {
		codeTemplate = DynamicReportBase.getFormatCodeDescriptionByType(code, drc.getFormatType());

		if(!StringUtils.isEmpty(codeTemplate))
			formatCode = code;
	}

	public String getCodeTemplate() {
		return codeTemplate;
	}

	public void setCodeTemplate(String codeTemplate) {
		this.codeTemplate = codeTemplate;
	}

	public int getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(int formatCode) {
		this.formatCode = formatCode;
	}
}

	
