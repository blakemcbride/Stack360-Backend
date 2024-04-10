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

import com.arahant.business.BReport;
import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.beans.ReportColumn;
import com.arahant.beans.ReportSelection;
import com.arahant.business.BReportColumn;
import com.arahant.business.BReportSelection;
import com.arahant.utils.ArahantSession;


public class SaveDynamicReportInput extends TransmitInputBase {

	@Validation(table="report", column="report_id", required=true)
	private String reportId;
	@Validation(table="report", column="report_type", required=true)
	private int reportType;
	@Validation(table="report", column="report_name", required=true)
	private String reportName;
	@Validation(table="report", column="description", required=true)
	private String description;
	@Validation(required=true)
	private String pageOrientation;
	@Validation(table="report", column="page_offset_left", required=true)
	private double pageOffsetLeft;
	@Validation(table="report", column="page_offset_right", required=true)
	private double pageOffsetTop;
	@Validation(table="report", column="default_font_size", required=true)
	private short defaultFontSize;
	@Validation(table="report", column="lines_in_column_title", required=true)
	private short linesInColumnTitle;
	@Validation(table="report", column="default_space_between_columns", required=true)
	private double defaultSpaceBetweenColumns;
	@Validation(required=true)
	private SaveDynamicReportColumn[] column;
	@Validation(required=true)
	private SaveDynamicReportSelection[] selection;
	@Validation(required=true)
	private SaveDynamicReportTitle[] title;


	void setData(BReport br) {
		br.setReportType(reportType);
		br.setReportName(reportName);
		br.setDescription(description);
		br.setPageOrientation(pageOrientation);
		br.setPageOffsetLeft(pageOffsetLeft);
		br.setPageOffsetTop(pageOffsetTop);
		br.setDefaultFontSize(defaultFontSize);
		br.setLinesInColumnTitle(linesInColumnTitle);
		br.setDefaultSpaceBetweenColumns(defaultSpaceBetweenColumns);
		br.setDefaultFontSize(defaultFontSize);

		//get rid of old columns, because we have a complete list coming in each time
		for(ReportColumn rc : br.getReportColumns())
		{
			BReportColumn brc = new BReportColumn(rc);
			brc.delete();
		}
		ArahantSession.getHSU().flush();

		int colCount = 0;
		for(SaveDynamicReportColumn col : column)
		{
			BReportColumn brc = new BReportColumn();
			brc.create();
			brc.setBreakLevel(col.getBreakLevel());
			brc.setColumnId(col.getColumnId());
			brc.setColumnJustification(col.getColumnJustification());
			brc.setDisplayTotals(col.getDisplayTotals());
			brc.setFormatCode(col.getFormatCode());
			brc.setLeadingSpace(col.getLeadingSpace());
			brc.setLines(col.getLines());
			brc.setNumericDigits(col.getNumericDigits());
			brc.setTitle(col.getTitle());
			brc.setTitleJustification(col.getTitleJustification());
			brc.setUseAllLines(col.getUseAllLines());
			brc.setVerticalJustification(col.getVerticalJustification());
			brc.setSequenceNumber(colCount);
			brc.setReport(br.getBean());
			brc.setSortOrder(col.getSortOrder());
			brc.setSortDirection(col.getSortDirection());
			brc.insert();
			
			colCount++;
		}

		for(ReportSelection rs : br.getReportSelections())
		{
			BReportSelection brs = new BReportSelection(rs);
			brs.delete();
		}
		ArahantSession.getHSU().flush();

		colCount = 0;
		if(selection != null)
		{
			for(SaveDynamicReportSelection sel : selection)
			{
				BReportSelection brs = new BReportSelection();
				brs.create();
				brs.setDescription(sel.getDescription());
				brs.setLogicOperator(sel.getLogicOperator());
				brs.setLeftParens(sel.getLeftParens());
				brs.setRightParens(sel.getRightParens());
				brs.setSelectionColumn(sel.getSelectionColumn());
				brs.setSelectionValue(sel.getSelectionValue());
				brs.setSelectionOperator(sel.getSelectionOperator());
				brs.setSelectionType(sel.getSelectionType());
				brs.setSelectionColumn2(sel.getSelectionColumn2());
				brs.setSequenceNumber(colCount);
				brs.setReport(br.getBean());
				brs.insert();

				colCount++;
			}
		}
	}


	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public SaveDynamicReportTitle[] getTitle() {
		return title;
	}

	public void setTitle(SaveDynamicReportTitle[] title) {
		this.title = title;
	}

	public SaveDynamicReportSelection[] getSelection() {
		return selection;
	}

	public void setSelection(SaveDynamicReportSelection[] selection) {
		this.selection = selection;
	}

	public SaveDynamicReportColumn[] getColumn() {
		return column;
	}

	public void setColumn(SaveDynamicReportColumn[] column) {
		this.column = column;
	}

	public short getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(short defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	public double getDefaultSpaceBetweenColumns() {
		return defaultSpaceBetweenColumns;
	}

	public void setDefaultSpaceBetweenColumns(double defaultSpaceBetweenColumns) {
		this.defaultSpaceBetweenColumns = defaultSpaceBetweenColumns;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public short getLinesInColumnTitle() {
		return linesInColumnTitle;
	}

	public void setLinesInColumnTitle(short linesInColumnTitle) {
		this.linesInColumnTitle = linesInColumnTitle;
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

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}



}

	
