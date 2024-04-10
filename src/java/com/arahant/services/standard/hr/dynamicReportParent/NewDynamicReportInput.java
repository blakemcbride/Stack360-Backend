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
package com.arahant.services.standard.hr.dynamicReportParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BReport;


public class NewDynamicReportInput extends TransmitInputBase {

	@Validation (required=true, table="report", column="report_type")
	private Integer reportType;
	@Validation (required=true, table="report", column="report_name")
	private String reportName;
	@Validation (required=true, table="report", column="description")
	private String description;
	@Validation (required=true)
	private String pageOrientation;
	@Validation (required=true, table="report", column="page_offset_left")
	private double pageOffsetLeft;
	@Validation (required=true, table="report", column="page_offset_top")
	private double pageOffsetTop;
	@Validation (required=true, table="report", column="default_font_size")
	private int defaultFontSize;

	public void setData(BReport br)
	{
		br.setReportType(reportType);
		br.setReportName(reportName);
		br.setDescription(description);
		br.setPageOrientation(pageOrientation);
		br.setPageOffsetLeft(pageOffsetLeft);
		br.setPageOffsetTop(pageOffsetTop);
		br.setDefaultFontSize(defaultFontSize);

		//defaults
		br.setLinesInColumnTitle(1);
		br.setDefaultSpaceBetweenColumns(0.1);
	}

	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(int defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPageOffsetLeft() {
		return pageOffsetLeft;
	}

	public void setPageOffsetLeft(double pageOffsetLeft) {
		this.pageOffsetLeft = pageOffsetLeft;
	}

	public double getPageOffsetTop() {
		return pageOffsetTop;
	}

	public void setPageOffsetTop(double pageOffsetTop) {
		this.pageOffsetTop = pageOffsetTop;
	}

	public String getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

}

	
