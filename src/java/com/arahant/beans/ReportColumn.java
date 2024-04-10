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

@Entity
@Table(name = "report_column")
public class ReportColumn extends ArahantBean implements java.io.Serializable, Comparable<ReportColumn> {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "report_column";
	public static final String REPORT_COLUMN_ID = "reportColumnId";
	private String reportColumnId;
	public static final String REPORT = "report";
	private Report report;
	public static final String REPORT_ID = "reportId";
	private String reportId;
	public static final String SEQ_NO = "sequenceNumber";
	private short sequenceNumber;
	public static final String COLUMN_ID = "columnId";
	private short columnId;
	public static final String LEADING_SPACE = "leadingSpace";
	private double leadingSpace = 0.0;
	public static final String LINES = "lines";
	private short lines = 1;
	public static final String USE_ALL_LINES = "useAllLines";
	private char useAllLines = 'N';
	public static final String VERTICAL_JUSTIFICATION = "verticalJustification";
	private char verticalJustification = 'T';
	public static final String TITLE = "title";
	private String title;
	public static final String TITLE_JUSTIFICATION = "titleJustification";
	private char titleJustification = 'L';
	public static final String COLUMN_JUSTIFICATION = "columnJustification";
	private char columnJustification = 'L';
	public static final String FORMAT_CODE = "formatCode";
	private short formatCode = 0;
	public static final String NUMERIC_DIGITS = "numericDigits";
	private short numericDigits = 0;
	public static final String DISPLAY_TOTALS = "displayTotals";
	private char displayTotals = 'N';
	public static final String BREAK_LEVEL = "breakLevel";
	private short breakLevel = 0;
	public static final String SORT_ORDER = "sortOrder";
	private short sortOrder = 0;
	public static final String SORT_DIRECTION = "sortDirection";
	private char sortDirection = 'A';

	//Default constructor
	public ReportColumn() {
	}

	@Id
	@Column(name = "report_column_id")
	public String getReportColumnId() {
		return reportColumnId;
	}

	public void setReportColumnId(String reportColumnId) {
		this.reportColumnId = reportColumnId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	public Report getReport() {
		return report;
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

	@Column(name = "break_level")
	public short getBreakLevel() {
		return breakLevel;
	}

	public void setBreakLevel(short breakLevel) {
		this.breakLevel = breakLevel;
	}

	@Column(name = "column_id")
	public short getColumnId() {
		return columnId;
	}

	public void setColumnId(short columnId) {
		this.columnId = columnId;
	}

	@Column(name = "column_justification")
	public char getColumnJustification() {
		return columnJustification;
	}

	public void setColumnJustification(char columnJustification) {
		this.columnJustification = columnJustification;
	}

	@Column(name = "display_totals")
	public char getDisplayTotals() {
		return displayTotals;
	}

	public void setDisplayTotals(char displayTotals) {
		this.displayTotals = displayTotals;
	}

	@Column(name = "format_code")
	public short getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(short formatCode) {
		this.formatCode = formatCode;
	}

	@Column(name = "leading_space")
	public double getLeadingSpace() {
		return leadingSpace;
	}

	public void setLeadingSpace(double leadingSpace) {
		this.leadingSpace = leadingSpace;
	}

	@Column(name = "lines")
	public short getLines() {
		return lines;
	}

	public void setLines(short lines) {
		this.lines = lines;
	}

	@Column(name = "numeric_digits")
	public short getNumericDigits() {
		return numericDigits;
	}

	public void setNumericDigits(short numericDigits) {
		this.numericDigits = numericDigits;
	}

	@Column(name = "seqno")
	public short getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "title_justification")
	public char getTitleJustification() {
		return titleJustification;
	}

	public void setTitleJustification(char titleJustification) {
		this.titleJustification = titleJustification;
	}

	@Column(name = "use_all_lines")
	public char getUseAllLines() {
		return useAllLines;
	}

	public void setUseAllLines(char useAllLines) {
		this.useAllLines = useAllLines;
	}

	@Column(name = "vertical_justification")
	public char getVerticalJustification() {
		return verticalJustification;
	}

	public void setVerticalJustification(char verticalJustification) {
		this.verticalJustification = verticalJustification;
	}

	@Column(name = "sort_direction")
	public char getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(char sortDirection) {
		this.sortDirection = sortDirection;
	}

	@Column(name = "sort_order")
	public short getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "report_column_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setReportColumnId(IDGenerator.generate(this));
		return reportColumnId;
	}

	@Override
	public ReportColumn clone() {
		ReportColumn rc = new ReportColumn();
		rc.generateId();
		rc.setReportId(reportId);
		rc.setSequenceNumber(sequenceNumber);
		rc.setBreakLevel(breakLevel);
		rc.setColumnId(columnId);
		rc.setColumnJustification(columnJustification);
		rc.setDisplayTotals(displayTotals);
		rc.setFormatCode(formatCode);
		rc.setLeadingSpace(leadingSpace);
		rc.setLines(lines);
		rc.setNumericDigits(numericDigits);
		rc.setTitle(title);
		rc.setTitleJustification(titleJustification);
		rc.setUseAllLines(useAllLines);
		rc.setVerticalJustification(verticalJustification);

		return rc;
	}

	@Override
	public boolean equals(Object o) {
		if (reportColumnId == null && o == null)
			return true;
		if (reportColumnId != null && o instanceof ReportColumn)
			return reportColumnId.equals(((ReportColumn) o).getReportColumnId());

		return false;
	}

	@Override
	public int hashCode() {
		if (reportColumnId == null)
			return 0;
		return reportColumnId.hashCode();
	}

	@Override
	public int compareTo(ReportColumn o) {
		if (sequenceNumber == o.sequenceNumber)
			return 0;
		else if (sequenceNumber > o.sequenceNumber)
			return 1;
		else
			return -1;
	}
}
