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
@Table(name = "report")
public class Report extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "report";
	public static final String REPORT_ID = "reportId";
	private String reportId;
	public static final String REPORT_TYPE = "reportType";
	private int reportType;
	public static final String REPORT_NAME = "reportName";
	private String reportName;
	public static final String DESCRIPTION = "description";
	private String description;
	public static final String PAGE_ORIENTATION = "pageOrientation";
	private char pageOrientation = 'P';
	public static final String PAGE_OFFSET_LEFT = "pageOffsetLeft";
	private double pageOffsetLeft = 0.5;
	public static final String PAGE_OFFSET_TOP = "pageOffsetTop";
	private double pageOffsetTop = 0.5;
	public static final String DEFAULT_FONT_SIZE = "defaultFontSize";
	private short defaultFontSize = 12;
	public static final String LINES_IN_COL_TITLE = "linesInColumnTitle";
	private short linesInColumnTitle = 1;
	public static final String DEFAULT_SPACE_BETWEEN_COLS = "defaultSpaceBetweenColumns";
	private double defaultSpaceBetweenColumns = 0.1;
	public static final String COMPANY_ID = "companyId";
	private String companyId;
	public static final String COMPANY = "company";
	private CompanyDetail company;

	//Default constructor
	public Report() {
	}

	@Column(name = "default_font_size")
	public short getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(short defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	@Column(name = "default_space_between_columns")
	public double getDefaultSpaceBetweenColumns() {
		return defaultSpaceBetweenColumns;
	}

	public void setDefaultSpaceBetweenColumns(double defaultSpaceBetweenColumns) {
		this.defaultSpaceBetweenColumns = defaultSpaceBetweenColumns;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "lines_in_column_title")
	public short getLinesInColumnTitle() {
		return linesInColumnTitle;
	}

	public void setLinesInColumnTitle(short linesInColumnTitle) {
		this.linesInColumnTitle = linesInColumnTitle;
	}

	@Column(name = "page_offset_left")
	public double getPageOffsetLeft() {
		return pageOffsetLeft;
	}

	public void setPageOffsetLeft(double pageOffsetLeft) {
		this.pageOffsetLeft = pageOffsetLeft;
	}

	@Column(name = "page_offset_top")
	public double getPageOffsetTop() {
		return pageOffsetTop;
	}

	public void setPageOffsetTop(double pageOffsetTop) {
		this.pageOffsetTop = pageOffsetTop;
	}

	@Column(name = "page_orientation")
	public char getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(char pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	@Id
	@Column(name = "report_id")
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Column(name = "report_name")
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "report_type")
	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail companyId) {
		this.company = companyId;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "report_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setReportId(IDGenerator.generate(this));
		return reportId;
	}

	@Override
	public Report clone() {
		Report r = new Report();
		r.generateId();
		r.setDefaultFontSize(defaultFontSize);
		r.setDefaultSpaceBetweenColumns(defaultSpaceBetweenColumns);
		r.setDescription(description);
		r.setLinesInColumnTitle(linesInColumnTitle);
		r.setPageOffsetLeft(pageOffsetLeft);
		r.setPageOffsetTop(pageOffsetTop);
		r.setPageOrientation(pageOrientation);
		r.setReportName(reportName);
		r.setReportType(reportType);
		return r;
	}

	@Override
	public boolean equals(Object o) {
		if (reportId == null && o == null)
			return true;
		if (reportId != null && o instanceof Report)
			return reportId.equals(((Report) o).getReportId());

		return false;
	}

	@Override
	public int hashCode() {
		if (reportId == null)
			return 0;
		return reportId.hashCode();
	}
}
