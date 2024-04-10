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
import com.arahant.beans.ReportGraphic;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;

public class BReportGraphic extends SimpleBusinessObjectBase<ReportGraphic> implements IDBFunctions {

	/**
	 */
	public BReportGraphic() {
	}

	/**
	 * @param reportId
	 * @throws ArahantException
	 */
	public BReportGraphic(final String reportGraphicId) throws ArahantException {
		internalLoad(reportGraphicId);
	}

	/**
	 * @param Report
	 */
	public BReportGraphic(final ReportGraphic rg) {
		bean = rg;
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public String getReportGraphicId() {
		return bean.getReportGraphicId();
	}

	public void setReportGraphicId(String reportGraphicId) {
		bean.setReportGraphicId(reportGraphicId);
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

	public byte[] getGraphic() {
		return bean.getGraphic();
	}

	public void setGraphic(byte[] graphic) {
		bean.setGraphic(graphic);
	}

	public double getXPos() {
		return bean.getXPos();
	}

	public void setXPos(double xPos) {
		bean.setXPos(xPos);
	}

	public double getYPos() {
		return bean.getYPos();
	}

	public void setYPos(double yPos) {
		bean.setYPos(yPos);
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
			new BReportGraphic(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ReportGraphic();
		bean.generateId();
		return getReportId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ReportGraphic.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}
}
