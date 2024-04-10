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
@Table(name = "report_title")
public class ReportTitle extends ArahantBean implements java.io.Serializable, Comparable<ReportTitle> {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "report_title";
	public static final String REPORT_TITLE_ID = "reportTitleId";
	private String reportTitleId;
	public static final String REPORT = "report";
	private Report report;
	public static final String REPORT_ID = "reportId";
	private String reportId;
	public static final String PAGE_LOCATION = "pageLocation";
	private char pageLocation;
	public static final String SEQ_NO = "sequenceNumber";
	private short sequenceNumber;
	public static final String FIELD_TYPE = "fieldType";
	private char fieldType = 'T';
	public static final String FIELD_FORMAT = "fieldFormat";
	private short fieldFormat = 0;
	public static final String FONT_SIZE = "fontSize";
	private short fontSize;
	public static final String FONT_UNDERLINE = "fontUnderline";
	private char fontUnderline = 'N';
	public static final String FONT_BOLD = "fontBold";
	private char fontBold = 'N';
	public static final String FONT_ITALIC = "fontItalic";
	private char fontItalic = 'N';
	public static final String END_LINE = "endLine";
	private char endLine = 'Y';
	public static final String JUSTIFICATION = "justification";
	private char justification = 'C';
	public static final String LEFT_OFFSET = "leftOffset";
	private double leftOffset = 0.0;
	public static final String VERBIAGE = "verbiage";
	private String verbiage;

	//Default constructor
	public ReportTitle() {
	}

	@Column(name = "end_line")
	public char getEndLine() {
		return endLine;
	}

	public void setEndLine(char endLine) {
		this.endLine = endLine;
	}

	@Column(name = "field_format")
	public short getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(short fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	@Column(name = "field_type")
	public char getFieldType() {
		return fieldType;
	}

	public void setFieldType(char fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "font_bold")
	public char getFontBold() {
		return fontBold;
	}

	public void setFontBold(char fontBold) {
		this.fontBold = fontBold;
	}

	@Column(name = "font_italic")
	public char getFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(char fontItalic) {
		this.fontItalic = fontItalic;
	}

	@Column(name = "font_size")
	public short getFontSize() {
		return fontSize;
	}

	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}

	@Column(name = "font_underline")
	public char getFontUnderline() {
		return fontUnderline;
	}

	public void setFontUnderline(char fontUnderline) {
		this.fontUnderline = fontUnderline;
	}

	@Column(name = "justification")
	public char getJustification() {
		return justification;
	}

	public void setJustification(char justification) {
		this.justification = justification;
	}

	@Column(name = "left_offset")
	public double getLeftOffset() {
		return leftOffset;
	}

	public void setLeftOffset(double leftOffset) {
		this.leftOffset = leftOffset;
	}

	@Column(name = "page_location")
	public char getPageLocation() {
		return pageLocation;
	}

	public void setPageLocation(char pageLocation) {
		this.pageLocation = pageLocation;
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

	@Id
	@Column(name = "report_title_id")
	public String getReportTitleId() {
		return reportTitleId;
	}

	public void setReportTitleId(String reportTitleId) {
		this.reportTitleId = reportTitleId;
	}

	@Column(name = "seqno")
	public short getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Column(name = "verbiage")
	public String getVerbiage() {
		return verbiage;
	}

	public void setVerbiage(String verbiage) {
		this.verbiage = verbiage;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "report_title_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setReportTitleId(IDGenerator.generate(this));
		return reportTitleId;
	}

	@Override
	public ReportTitle clone() {
		ReportTitle rt = new ReportTitle();
		rt.generateId();
		rt.setEndLine(endLine);
		rt.setFieldFormat(fieldFormat);
		rt.setFieldType(fieldType);
		rt.setFontBold(fontBold);
		rt.setFontItalic(fontItalic);
		rt.setFontSize(fontSize);
		rt.setFontUnderline(fontUnderline);
		rt.setJustification(justification);
		rt.setLeftOffset(leftOffset);
		rt.setPageLocation(pageLocation);
		rt.setReportId(reportId);
		rt.setSequenceNumber(sequenceNumber);
		rt.setVerbiage(verbiage);

		return rt;
	}

	@Override
	public boolean equals(Object o) {
		if (reportTitleId == null && o == null)
			return true;
		if (reportTitleId != null && o instanceof ReportTitle)
			return reportTitleId.equals(((ReportTitle) o).getReportTitleId());

		return false;
	}

	@Override
	public int hashCode() {
		if (reportTitleId == null)
			return 0;
		return reportTitleId.hashCode();
	}

	@Override
	public int compareTo(ReportTitle o) {
		if (sequenceNumber == o.sequenceNumber)
			return 0;
		else if (sequenceNumber > o.sequenceNumber)
			return 1;
		else
			return -1;
	}
}
