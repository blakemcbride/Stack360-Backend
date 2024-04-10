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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "report_selection")
public class ReportSelection extends ArahantBean implements java.io.Serializable, Comparable<ReportSelection> {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "report_selection";
	public static final String REPORT_SELECTION_ID = "reportSelectionId";
	private String reportSelectionId;
	public static final String REPORT = "report";
	private Report report;
	public static final String REPORT_ID = "reportId";
	private String reportId;
	public static final String SEQ_NO = "sequenceNumber";
	private short sequenceNumber;
	public static final String SELECTION_TYPE = "selectionType";
	private char selectionType = 'V';
	public static final String SELECTION_COLUMN = "selectionColumn";
	private short selectionColumn;
	public static final String DESCRIPTION = "description";
	private String description;
	public static final String SELECTION_OPERATOR = "selectionOperator";
	private String selectionOperator;
	public static final String SELECTION_VALUE = "selectionValue";
	private String selectionValue;
	public static final String SELECTION_COLUMN_2 = "selectionColumn2";
	private short selectionColumn2;
	public static final String LOGIC_OPERATOR = "logicOperator";
	private char logicOperator = 'N';
	public static final String LEFT_PARENS = "leftParens";
	private short leftParens = 0;
	public static final String RIGHT_PARENS = "rightParens";
	private short rightParens = 0;
	public static final String SELECTION_VALUE_LIST = "selectionValueList";
	private String selectionValueList;

	//Default constructor
	public ReportSelection() {
	}

	@Id
	@Column(name = "report_selection_id")
	public String getReportSelectionId() {
		return reportSelectionId;
	}

	public void setReportSelectionId(String reportSelectionId) {
		this.reportSelectionId = reportSelectionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	public Report getReport() {
		return this.report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@Column(name = "report_id", insertable = false, updatable = false)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Column(name = "seqno")
	public short getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Column(name = "left_parens")
	public short getLeftParens() {
		return leftParens;
	}

	public void setLeftParens(short leftParens) {
		this.leftParens = leftParens;
	}

	@Column(name = "logic_operator")
	public char getLogicOperator() {
		return logicOperator;
	}

	public void setLogicOperator(char logicOperator) {
		this.logicOperator = logicOperator;
	}

	@Column(name = "right_parens")
	public short getRightParens() {
		return rightParens;
	}

	public void setRightParens(short rightParens) {
		this.rightParens = rightParens;
	}

	@Column(name = "selection_column")
	public short getSelectionColumn() {
		return selectionColumn;
	}

	public void setSelectionColumn(short selectionColumn) {
		this.selectionColumn = selectionColumn;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "selection_column2")
	public short getSelectionColumn2() {
		return selectionColumn2;
	}

	public void setSelectionColumn2(short selectionColumn2) {
		this.selectionColumn2 = selectionColumn2;
	}

	@Column(name = "selection_operator")
	public String getSelectionOperator() {
		return selectionOperator;
	}

	public void setSelectionOperator(String selectionOperator) {
		this.selectionOperator = selectionOperator;
	}

	@Column(name = "selection_type")
	public char getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(char selectionType) {
		this.selectionType = selectionType;
	}

	@Column(name = "selection_value")
	public String getSelectionValue() {
		return selectionValue;
	}

	public void setSelectionValue(String selectionValue) {
		this.selectionValue = selectionValue;
	}

	@Column(name = "selection_value_list")
	public String getSelectionValueList() {
		return selectionValueList;
	}

	public void setSelectionValueList(String selectionValueList) {
		this.selectionValueList = selectionValueList;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "report_selection_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setReportSelectionId(IDGenerator.generate(this));
		return reportSelectionId;
	}

	@Override
	public ReportSelection clone() {
		ReportSelection rs = new ReportSelection();
		rs.generateId();
		rs.setReport(report);
		rs.setSequenceNumber(sequenceNumber);
		rs.setDescription(description);
		rs.setLeftParens(leftParens);
		rs.setLogicOperator(logicOperator);
		rs.setRightParens(rightParens);
		rs.setSelectionColumn(selectionColumn);
		rs.setSelectionColumn2(selectionColumn2);
		rs.setSelectionOperator(selectionOperator);
		rs.setSelectionType(selectionType);
		rs.setSelectionValue(selectionValue);
		return rs;
	}

	@Override
	public boolean equals(Object o) {
		if (reportSelectionId == null && o == null)
			return true;
		if (reportSelectionId != null && o instanceof ReportSelection)
			return reportSelectionId.equals(((ReportSelection) o).getReportSelectionId());

		return false;
	}

	@Override
	public int hashCode() {
		if (reportSelectionId == null)
			return 0;
		return reportSelectionId.hashCode();
	}

	@Override
	public int compareTo(ReportSelection o) {
		if (sequenceNumber == o.sequenceNumber)
			return 0;
		else if (sequenceNumber > o.sequenceNumber)
			return 1;
		else
			return -1;
	}
}
