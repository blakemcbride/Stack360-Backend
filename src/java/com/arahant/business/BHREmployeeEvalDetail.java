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

import com.arahant.beans.HrEmplEvalDetail;
import com.arahant.beans.HrEmployeeEval;
import com.arahant.beans.HrEvalCategory;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

public class BHREmployeeEvalDetail extends BusinessLogicBase implements IDBFunctions {

	private HrEmplEvalDetail hrEmplEvalDetail;

	public String getENotes() {
		return hrEmplEvalDetail.getENotes();
	}

	public short getEScore() {
		return hrEmplEvalDetail.getEScore();
	}

	public String getPNotes() {
		return hrEmplEvalDetail.getPNotes();
	}

	public void setENotes(final String ENotes) {
		hrEmplEvalDetail.setENotes(ENotes);
	}

	public void setEScore(final short EScore) {
		hrEmplEvalDetail.setEScore(EScore);
	}

	public void setPNotes(final String PNotes) {
		hrEmplEvalDetail.setPNotes(PNotes);
	}

	public BHREmployeeEvalDetail() {
	}

	public BHREmployeeEvalDetail(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BHREmployeeEvalDetail(final HrEmplEvalDetail detail) {
		hrEmplEvalDetail = detail;
	}

	@Override
	public String create() throws ArahantException {
		hrEmplEvalDetail = new HrEmplEvalDetail();
		hrEmplEvalDetail.generateId();
		return hrEmplEvalDetail.getDetailId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEmplEvalDetail);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrEmplEvalDetail);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmplEvalDetail = ArahantSession.getHSU().get(HrEmplEvalDetail.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrEmplEvalDetail);
	}

	public String getDetailId() {
		return hrEmplEvalDetail.getDetailId();
	}

	public String getNotes() {
		if (hrEmplEvalDetail.getNotes() == null)
			return "";
		return hrEmplEvalDetail.getNotes();
	}

	public short getScore() {
		return hrEmplEvalDetail.getScore();
	}

	public void setDetailId(final String detailId) {
		hrEmplEvalDetail.setDetailId(detailId);
	}

	public void setNotes(final String notes) {
		hrEmplEvalDetail.setNotes(notes);
	}

	public void setScore(final short score) {
		hrEmplEvalDetail.setScore(score);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String element : ids)
			new BHREmployeeEvalDetail(element).delete();
	}

	public void setEmployeeEvalId(final String employeeEvalId) {
		hrEmplEvalDetail.setHrEmployeeEval(ArahantSession.getHSU().get(HrEmployeeEval.class, employeeEvalId));
	}

	public void setEvalCategoryId(final String evalCatId) {
		hrEmplEvalDetail.setHrEvalCategory(ArahantSession.getHSU().get(HrEvalCategory.class, evalCatId));
	}

	public String getEvalCategoryName() {
		if (hrEmplEvalDetail.getHrEvalCategory() != null)
			return hrEmplEvalDetail.getHrEvalCategory().getName();
		return "";
	}

	public String getEvalCategoryId() {
		if (hrEmplEvalDetail.getHrEvalCategory() != null)
			return hrEmplEvalDetail.getHrEvalCategory().getEvalCatId();
		return "";
	}

	public String getEmployeeEvalId() {
		return hrEmplEvalDetail.getHrEmployeeEval().getEmployeeEvalId();
	}

	void stub() {
		hrEmplEvalDetail = new HrEmplEvalDetail();
	}

	public short getWeight() {
		return hrEmplEvalDetail.getHrEvalCategory().getWeight();
	}

	public String getEmployeeNotes() {
		if (hrEmplEvalDetail.getENotes() == null)
			return "";
		return hrEmplEvalDetail.getENotes();
	}

	public short getEmployeeScore() {
		return hrEmplEvalDetail.getEScore();
	}

	public String getInternalSupervisorNotes() {
		if (hrEmplEvalDetail.getPNotes() == null)
			return "";
		return hrEmplEvalDetail.getPNotes();
	}

	public void setInternalSupervisorNotes(final String internalSupervisorNotes) {
		hrEmplEvalDetail.setPNotes(internalSupervisorNotes);
	}

	public void setEmployeeScore(final short employeeScore) {
		hrEmplEvalDetail.setEScore(employeeScore);
	}

	public void setEmployeeNotes(final String employeeNotes) {
		hrEmplEvalDetail.setENotes(employeeNotes);
	}

	public String getCategoryDescription() {
		return hrEmplEvalDetail.getHrEvalCategory().getDescription();
	}
}
