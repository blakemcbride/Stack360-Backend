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
package com.arahant.business;

import com.arahant.beans.Report;
import com.arahant.beans.ReportTitle;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;

public class BReportTitle extends SimpleBusinessObjectBase<ReportTitle> implements IDBFunctions {

	/**
	 */
	public BReportTitle() {
	}

	/**
	 * @param reportTitleId
	 * @throws ArahantException
	 */
	public BReportTitle(final String reportTitleId) throws ArahantException {
		internalLoad(reportTitleId);
	}

	/**
	 * @param ReportTitle
	 */
	public BReportTitle(final ReportTitle rt) {
		bean = rt;
	}

	public String getEndLine() {
		return String.valueOf(bean.getEndLine());
	}

	public void setEndLine(char endLine) {
		bean.setEndLine(endLine);
	}

	public void setEndLine(String endLine) {
		bean.setEndLine(endLine.charAt(0));
	}

	public int getFieldFormat() {
		return bean.getFieldFormat();
	}

	public void setFieldFormat(short fieldFormat) {
		bean.setFieldFormat(fieldFormat);
	}

	public void setFieldFormat(int fieldFormat) {
		bean.setFieldFormat((short) fieldFormat);
	}

	public String getFieldType() {
		return String.valueOf(bean.getFieldType());
	}

	public void setFieldType(char fieldType) {
		bean.setFieldType(fieldType);
	}

	public void setFieldType(String fieldType) {
		bean.setFieldType(fieldType.charAt(0));
	}

	public String getFontBold() {
		return String.valueOf(bean.getFontBold());
	}

	public void setFontBold(char fontBold) {
		bean.setFontBold(fontBold);
	}

	public void setFontBold(String fontBold) {
		bean.setFontBold(fontBold.charAt(0));
	}

	public String getFontItalic() {
		return String.valueOf(bean.getFontItalic());
	}

	public void setFontItalic(char fontItalic) {
		bean.setFontItalic(fontItalic);
	}

	public void setFontItalic(String fontItalic) {
		bean.setFontItalic(fontItalic.charAt(0));
	}

	public int getFontSize() {
		return bean.getFontSize();
	}

	public void setFontSize(short fontSize) {
		bean.setFontSize(fontSize);
	}

	public void setFontSize(int fontSize) {
		bean.setFontSize((short) fontSize);
	}

	public String getFontUnderline() {
		return String.valueOf(bean.getFontUnderline());
	}

	public void setFontUnderline(char fontUnderline) {
		bean.setFontUnderline(fontUnderline);
	}

	public void setFontUnderline(String fontUnderline) {
		bean.setFontUnderline(fontUnderline.charAt(0));
	}

	public String getJustification() {
		return String.valueOf(bean.getJustification());
	}

	public void setJustification(char justification) {
		bean.setJustification(justification);
	}

	public void setJustification(String justification) {
		bean.setJustification(justification.charAt(0));
	}

	public double getLeftOffset() {
		return bean.getLeftOffset();
	}

	public void setLeftOffset(double leftOffset) {
		bean.setLeftOffset(leftOffset);
	}

	public String getPageLocation() {
		return String.valueOf(bean.getPageLocation());
	}

	public void setPageLocation(char pageLocation) {
		bean.setPageLocation(pageLocation);
	}

	public void setPageLocation(String pageLocation) {
		bean.setPageLocation(pageLocation.charAt(0));
	}

	public Report getReport() {
		return bean.getReport();
	}

	public void setReport(Report report) {
		bean.setReport(report);
	}

	public String getReportId() {
		return bean.getReportId();
	}

	public void setReportId(String reportId) {
		bean.setReport(ArahantSession.getHSU().get(Report.class, reportId));
	}

	public String getReportTitleId() {
		return bean.getReportTitleId();
	}

	public void setReportTitleId(String reportTitleId) {
		bean.setReportTitleId(reportTitleId);
	}

	public int getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	public void setSequenceNumber(int sequenceNumber) {
		bean.setSequenceNumber((short) sequenceNumber);
	}

	public String getVerbiage() {
		return bean.getVerbiage();
	}

	public void setVerbiage(String verbiage) {
		bean.setVerbiage(verbiage);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for (String id : ids)
			new BReportTitle(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ReportTitle();
		bean.generateId();
		return getReportTitleId();
	}

	private void internalLoad(String key) throws ArahantException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}
}
