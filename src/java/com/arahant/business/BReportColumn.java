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
import com.arahant.beans.ReportColumn;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;

public class BReportColumn extends SimpleBusinessObjectBase<ReportColumn> implements IDBFunctions {

	/**
	 */
	public BReportColumn() {
	}

	/**
	 * @param reportTitleId
	 * @throws ArahantException
	 */
	public BReportColumn(final String reportColumnId) throws ArahantException {
		internalLoad(reportColumnId);
	}

	/**
	 * @param ReportTitle
	 */
	public BReportColumn(final ReportColumn rc) {
		bean = rc;
	}

	public String getReportColumnId() {
		return bean.getReportColumnId();
	}

	public void setReportColumnId(String reportColumnId) {
		bean.setReportColumnId(reportColumnId);
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

	public int getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	public void setSequenceNumber(int sequenceNumber) {
		bean.setSequenceNumber((short) sequenceNumber);
	}

	public int getBreakLevel() {
		return bean.getBreakLevel();
	}

	public void setBreakLevel(short breakLevel) {
		bean.setBreakLevel(breakLevel);
	}

	public void setBreakLevel(int breakLevel) {
		bean.setBreakLevel((short) breakLevel);
	}

	public int getSortOrder() {
		return bean.getSortOrder();
	}

	public void setSortOrder(short sortOrder) {
		bean.setSortOrder(sortOrder);
	}

	public void setSortOrder(int sortOrder) {
		bean.setSortOrder((short) sortOrder);
	}

	public int getColumnId() {
		return bean.getColumnId();
	}

	public void setColumnId(short columnId) {
		bean.setColumnId(columnId);
	}

	public void setColumnId(int columnId) {
		bean.setColumnId((short) columnId);
	}

	public String getColumnJustification() {
		return String.valueOf(bean.getColumnJustification());
	}

	public void setColumnJustification(char columnJustification) {
		bean.setColumnJustification(columnJustification);
	}

	public void setColumnJustification(String columnJustification) {
		bean.setColumnJustification(columnJustification.charAt(0));
	}

	public String getSortDirection() {
		return String.valueOf(bean.getSortDirection());
	}

	public void setSortDirection(char sortDirection) {
		bean.setSortDirection(sortDirection);
	}

	public void setSortDirection(String sortDirection) {
		bean.setSortDirection(sortDirection.charAt(0));
	}

	public String getDisplayTotals() {
		return String.valueOf(bean.getDisplayTotals());
	}

	public void setDisplayTotals(char displayTotals) {
		bean.setDisplayTotals(displayTotals);
	}

	public void setDisplayTotals(String displayTotals) {
		bean.setDisplayTotals(displayTotals.charAt(0));
	}

	public int getFormatCode() {
		return bean.getFormatCode();
	}

	public void setFormatCode(short formatCode) {
		bean.setFormatCode(formatCode);
	}

	public void setFormatCode(int formatCode) {
		bean.setFormatCode((short) formatCode);
	}

	public double getLeadingSpace() {
		return bean.getLeadingSpace();
	}

	public void setLeadingSpace(double leadingSpace) {
		bean.setLeadingSpace(leadingSpace);
	}

	public int getLines() {
		return bean.getLines();
	}

	public void setLines(short lines) {
		bean.setLines(lines);
	}

	public void setLines(int lines) {
		bean.setLines((short) lines);
	}

	public int getNumericDigits() {
		return bean.getNumericDigits();
	}

	public void setNumericDigits(short numericDigits) {
		bean.setNumericDigits(numericDigits);
	}

	public void setNumericDigits(int numericDigits) {
		bean.setNumericDigits((short) numericDigits);
	}

	public String getTitle() {
		return bean.getTitle();
	}

	public void setTitle(String title) {
		bean.setTitle(title);
	}

	public String getTitleJustification() {
		return String.valueOf(bean.getTitleJustification());
	}

	public void setTitleJustification(char titleJustification) {
		bean.setTitleJustification(titleJustification);
	}

	public void setTitleJustification(String titleJustification) {
		bean.setTitleJustification(titleJustification.charAt(0));
	}

	public String getUseAllLines() {
		return String.valueOf(bean.getUseAllLines());
	}

	public void setUseAllLines(char useAllLines) {
		bean.setUseAllLines(useAllLines);
	}

	public void setUseAllLines(String useAllLines) {
		bean.setUseAllLines(useAllLines.charAt(0));
	}

	public String getVerticalJustification() {
		return String.valueOf(bean.getVerticalJustification());
	}

	public void setVerticalJustification(char verticalJustification) {
		bean.setVerticalJustification(verticalJustification);
	}

	public void setVerticalJustification(String verticalJustification) {
		bean.setVerticalJustification(verticalJustification.charAt(0));
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
			new BReportColumn(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ReportColumn();
		bean.generateId();
		return getReportColumnId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ReportColumn.class, key);
		if (bean == null)
			throw new ArahantException("Failed to load report column with key " + key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void main(String[] args) {
		ReportColumn rc = ArahantSession.getHSU().get(ReportColumn.class, "00001-0000000097");

		System.out.println(rc.getFormatCode());
	}
}
