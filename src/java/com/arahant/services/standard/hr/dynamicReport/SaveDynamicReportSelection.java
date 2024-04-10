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

import com.arahant.beans.ReportSelection;
import com.arahant.business.BReportSelection;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;

/**
 *
 * Arahant
 */
public class SaveDynamicReportSelection {

	private String selectionType;
	private int selectionColumn;
	private String selectionColumnName;
	private String description;
	private String selectionOperator;
	private String selectionValue;
	private int selectionColumn2;
	private String selectionColumn2Name;
	private String logicOperator;
	private int leftParens;
	private int rightParens;
	private String reportSelectionId;
	private int seqNo;


	public SaveDynamicReportSelection() {
	}

	public SaveDynamicReportSelection(ReportSelection r) {
		BReportSelection rs = new BReportSelection(r);
		this.selectionType = rs.getSelectionType() + "";
		this.selectionColumn = rs.getSelectionColumn();
		this.description = rs.getDescription();
		this.selectionOperator = rs.getSelectionOperator();
		this.selectionValue = rs.getSelectionValue();
		this.selectionColumn2 = rs.getSelectionColumn2();
		this.logicOperator = rs.getLogicOperator() + "";
		this.leftParens = rs.getLeftParens();
		this.rightParens = rs.getRightParens();
		this.reportSelectionId = rs.getReportSelectionId();
		this.seqNo = rs.getSequenceNumber();
		DynamicReportColumn drc = DynamicReportBase.getColumnByTypeIndex(rs.getReport().getReportType(), rs.getSelectionColumn());
		this.selectionColumnName = drc.getDisplayName();
		try {
			this.selectionColumn2Name = DynamicReportBase.getColumnByTypeIndex(rs.getReport().getReportType(), rs.getSelectionColumn2()).getDisplayName();
		} catch (Exception e) {
			//do nothing
		}
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String getSelectionColumn2Name() {
		return selectionColumn2Name;
	}

	public void setSelectionColumn2Name(String selectionColumn2Name) {
		this.selectionColumn2Name = selectionColumn2Name;
	}

	public String getSelectionColumnName() {
		return selectionColumnName;
	}

	public void setSelectionColumnName(String selectionColumnName) {
		this.selectionColumnName = selectionColumnName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogicOperator() {
		return logicOperator;
	}

	public void setLogicOperator(String logicOperator) {
		this.logicOperator = logicOperator;
	}

	public int getLeftParens() {
		return leftParens;
	}

	public void setLeftParens(int leftParens) {
		this.leftParens = leftParens;
	}

	public int getRightParens() {
		return rightParens;
	}

	public void setRightParens(int rightParens) {
		this.rightParens = rightParens;
	}

	public int getSelectionColumn() {
		return selectionColumn;
	}

	public void setSelectionColumn(int selectionColumn) {
		this.selectionColumn = selectionColumn;
	}

	public int getSelectionColumn2() {
		return selectionColumn2;
	}

	public void setSelectionColumn2(int selectionColumn2) {
		this.selectionColumn2 = selectionColumn2;
	}

	public String getSelectionOperator() {
		return selectionOperator;
	}

	public void setSelectionOperator(String selectionOperator) {
		this.selectionOperator = selectionOperator;
	}

	public String getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(String selectionType) {
		this.selectionType = selectionType;
	}

	public String getSelectionValue() {
		return selectionValue;
	}

	public void setSelectionValue(String selectionValue) {
		this.selectionValue = selectionValue;
	}

	public String getReportSelectionId() {
		return reportSelectionId;
	}

	public void setReportSelectionId(String reportSelectionId) {
		this.reportSelectionId = reportSelectionId;
	}

	
}
