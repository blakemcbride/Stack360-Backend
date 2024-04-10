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

import com.arahant.beans.ReportTitle;

/**
 *
 * Arahant
 */
public class SaveDynamicReportTitle {

	private String pageLocation;
	private String fieldType;
	private short fieldFormat;
	private short fontSize;
	private String fontUnderline;
	private String fontBold;
	private String fontItalic;
	private String endLine;
	private String justification;
	private double leftOffset;
	private String verbiage;
	private String reportTitleId;

	public SaveDynamicReportTitle() {
	}

	public SaveDynamicReportTitle(ReportTitle rt) {
		this.pageLocation = rt.getPageLocation() + "";
		this.fieldType = rt.getFieldType() + "";
		this.fieldFormat = rt.getFieldFormat();
		this.fontSize = rt.getFontSize();
		this.fontUnderline = rt.getFontUnderline() + "";
		this.fontBold = rt.getFontBold() + "";
		this.fontItalic = rt.getFontItalic() + "";
		this.endLine = rt.getEndLine() + "";
		this.justification = rt.getJustification() + "";
		this.leftOffset = rt.getLeftOffset();
		this.verbiage = rt.getVerbiage();
		this.reportTitleId = rt.getReportTitleId();
	}



	public short getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(short fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	public short getFontSize() {
		return fontSize;
	}

	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}

	public double getLeftOffset() {
		return leftOffset;
	}

	public void setLeftOffset(double leftOffset) {
		this.leftOffset = leftOffset;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getPageLocation() {
		return pageLocation;
	}

	public void setPageLocation(String pageLocation) {
		this.pageLocation = pageLocation;
	}

	public String getVerbiage() {
		return verbiage;
	}

	public void setVerbiage(String verbiage) {
		this.verbiage = verbiage;
	}

	public String getEndLine() {
		return endLine;
	}

	public void setEndLine(String endLine) {
		this.endLine = endLine;
	}

	public String getFontBold() {
		return fontBold;
	}

	public void setFontBold(String fontBold) {
		this.fontBold = fontBold;
	}

	public String getFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(String fontItalic) {
		this.fontItalic = fontItalic;
	}

	public String getFontUnderline() {
		return fontUnderline;
	}

	public void setFontUnderline(String fontUnderline) {
		this.fontUnderline = fontUnderline;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getReportTitleId() {
		return reportTitleId;
	}

	public void setReportTitleId(String reportTitleId) {
		this.reportTitleId = reportTitleId;
	}
 
	
}
