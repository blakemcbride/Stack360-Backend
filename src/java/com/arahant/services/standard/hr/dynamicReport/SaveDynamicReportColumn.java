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

package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.beans.ReportColumn;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Arahant
 */
public class SaveDynamicReportColumn {

	private double leadingSpace;
	private int lines;
	private String useAllLines;
	private String verticalJustification;
	private String title;
	private String titleJustification;
	private String columnJustification;
	private int formatCode;
	private int numericDigits;
	private String displayTotals;
	private int breakLevel;
	private String reportColumnId;
	private int columnId;
	private ColumnOperator[] operators;
	private int controlType;
	private int formatType;
	private ListObject[] listObject;
	private int sortOrder;
	private String sortDirection;

	public SaveDynamicReportColumn() {
	}

	public SaveDynamicReportColumn(ReportColumn rc) {
		leadingSpace = rc.getLeadingSpace();
		lines = rc.getLines();
		useAllLines = rc.getUseAllLines() + "";
		verticalJustification = rc.getVerticalJustification() + "";
		title = rc.getTitle();
		titleJustification = rc.getTitleJustification() + "";
		columnJustification = rc.getColumnJustification() + "";
		formatCode = rc.getFormatCode();
		numericDigits = rc.getNumericDigits();
		displayTotals = rc.getDisplayTotals() + "";
		breakLevel = rc.getBreakLevel();
		reportColumnId = rc.getReportColumnId();
		columnId = rc.getColumnId();
		DynamicReportColumn drc = DynamicReportBase.getColumnByTypeIndex(rc.getReport().getReportType(), rc.getColumnId());
		this.controlType = drc.getControlType();
		this.formatType = drc.getFormatType();
		sortOrder = rc.getSortOrder();
		sortDirection = String.valueOf(rc.getSortDirection());

		List<String> l = (List)drc.getOperators();
		this.operators = new ColumnOperator[l.size()];
		for (int i = 0; i < l.size(); i++)
			operators[i] = new ColumnOperator(l.get(i));

		List<List<Object>> ls = drc.getRunTimeSelectionList();
		this.listObject = new ListObject[ls.size()];
		for (int i = 0; i < ls.size(); i++)
			listObject[i] = new ListObject(ls.get(i));
	}

	public SaveDynamicReportColumn(DynamicReportColumn rc) {
		leadingSpace = 0;
		lines = 1;
		useAllLines = "N";
		verticalJustification = "T";
		title = rc.getDisplayName();
		titleJustification = "L";
		columnJustification = "L";
		formatCode = 0;
		numericDigits = 0;
		displayTotals = "N";
		breakLevel = 0;
		reportColumnId = "";
		columnId = rc.getIndex();
		List<String> l = (List<String>)rc.getOperators();
		this.operators = new ColumnOperator[l.size()];
		for (int i = 0; i < l.size(); i++)
			operators[i] = new ColumnOperator(l.get(i));
		this.controlType = rc.getControlType();
		List<List<Object>> ls = rc.getRunTimeSelectionList();
		this.listObject = new ListObject[ls.size()];
		for (int i = 0; i < ls.size(); i++)
			listObject[i] = new ListObject(ls.get(i));
		this.formatType = rc.getFormatType();
		sortOrder = 0;
		sortDirection = "A";
	}

	public ListObject[] getListObject() {
		return listObject;
	}

	public void setListObject(ListObject[] listObject) {
		this.listObject = listObject;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public ColumnOperator[] getOperators() {
		return operators;
	}

	public void setOperators(ColumnOperator[] operators) {
		this.operators = operators;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public int getBreakLevel() {
		return breakLevel;
	}

	public void setBreakLevel(int breakLevel) {
		this.breakLevel = breakLevel;
	}

	public int getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(int formatCode) {
		this.formatCode = formatCode;
	}

	public double getLeadingSpace() {
		return leadingSpace;
	}

	public void setLeadingSpace(double leadingSpace) {
		this.leadingSpace = leadingSpace;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getNumericDigits() {
		return numericDigits;
	}

	public void setNumericDigits(int numericDigits) {
		this.numericDigits = numericDigits;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColumnJustification() {
		return columnJustification;
	}

	public void setColumnJustification(String columnJustification) {
		this.columnJustification = columnJustification;
	}

	public String getDisplayTotals() {
		return displayTotals;
	}

	public void setDisplayTotals(String displayTotals) {
		this.displayTotals = displayTotals;
	}

	public String getTitleJustification() {
		return titleJustification;
	}

	public void setTitleJustification(String titleJustification) {
		this.titleJustification = titleJustification;
	}

	public String getUseAllLines() {
		return useAllLines;
	}

	public void setUseAllLines(String useAllLines) {
		this.useAllLines = useAllLines;
	}

	public String getVerticalJustification() {
		return verticalJustification;
	}

	public void setVerticalJustification(String verticalJustification) {
		this.verticalJustification = verticalJustification;
	}

	public String getReportColumnId() {
		return reportColumnId;
	}

	public void setReportColumnId(String reportColumnId) {
		this.reportColumnId = reportColumnId;
	}

	public int getFormatType() {
		return formatType;
	}

	public void setFormatType(int formatType) {
		this.formatType = formatType;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
