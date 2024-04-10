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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.*;
import com.arahant.reports.HREvaluationReport;
import com.arahant.reports.HREvaluationResponseReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class BHREmployeeEval extends BusinessLogicBase implements IDBFunctions {

	private HrEmployeeEval hrEmployeeEval;

	public BHREmployeeEval() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHREmployeeEval(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param eval
	 */
	public BHREmployeeEval(final HrEmployeeEval eval) {
		hrEmployeeEval = eval;
	}

	@Override
	public String create() throws ArahantException {
		hrEmployeeEval = new HrEmployeeEval();
		hrEmployeeEval.setState('S');
		hrEmployeeEval.generateId();
		return hrEmployeeEval.getEmployeeEvalId();
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {

		checkAccess();

		final Calendar ecal = DateUtils.getCalendar(hrEmployeeEval.getEvalDate());
		ecal.add(Calendar.DAY_OF_YEAR, 30);

		if (DateUtils.getNow().after(ecal) && BRight.checkRight(DELETE_OLD_EVALUATIONS) != ACCESS_LEVEL_WRITE)
			throw new ArahantDeleteException("Can't delete evaluation after 30 days.");

		ArahantSession.getHSU().delete(hrEmployeeEval.getHrEmplEvalDetails());
		ArahantSession.getHSU().delete(hrEmployeeEval);
	}

	@Override
	public void insert() throws ArahantException {
		checkAccess();
		ArahantSession.getHSU().insert(hrEmployeeEval);
	}

	private void checkAccess() throws ArahantSecurityException, ArahantJessException {
		if (BRight.checkRight(CHANGE_EVALUATIONS) != ACCESS_LEVEL_WRITE)
			if (!ArahantSession.getHSU().getCurrentPerson().getPersonId().equals(hrEmployeeEval.getEmployeeByEmployeeId().getPersonId()))
				throw new ArahantSecurityException("You do not have access to change this evaluation.");
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmployeeEval = ArahantSession.getHSU().get(HrEmployeeEval.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		checkAccess();
		ArahantSession.getHSU().saveOrUpdate(hrEmployeeEval);
	}

	public String getDescription() {
		return hrEmployeeEval.getDescription();
	}

	public String getEmployeeEvalId() {
		return hrEmployeeEval.getEmployeeEvalId();
	}

	public int getEvalDate() {
		return hrEmployeeEval.getEvalDate();
	}

	public int getNextEvalDate() {
		return hrEmployeeEval.getNextEvalDate();
	}

	/**
	 * @param description
	 * @see com.arahant.beans.HrEmployeeEval#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		hrEmployeeEval.setDescription(description);
	}

	/**
	 * @param employeeEvalId
	 * @see com.arahant.beans.HrEmployeeEval#setEmployeeEvalId(java.lang.String)
	 */
	public void setEmployeeEvalId(final String employeeEvalId) {
		hrEmployeeEval.setEmployeeEvalId(employeeEvalId);
	}

	/**
	 * @param evalDate
	 * @see com.arahant.beans.HrEmployeeEval#setEvalDate(int)
	 */
	public void setEvalDate(final int evalDate) {
		hrEmployeeEval.setEvalDate(evalDate);
	}

	/**
	 * @param nextEvalDate
	 * @see com.arahant.beans.HrEmployeeEval#setNextEvalDate(int)
	 */
	public void setNextEvalDate(final int nextEvalDate) {
		hrEmployeeEval.setNextEvalDate(nextEvalDate);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHREmployeeEval(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String[] evalIds, final boolean showPrivate) throws Exception {

		final BHREmployeeEval[] employeeEvals = makeArray(hsu.createCriteria(HrEmployeeEval.class).in(HrEmployeeEval.EMPLOYEEEVALID, evalIds).list());
		return new HREvaluationReport().build(hsu, employeeEvals[0].getEmployeeId(), employeeEvals, showPrivate);
	}

	/**
	 * @param l
	 * @return
	 */
	public static BHREmployeeEval[] makeArray(final List<HrEmployeeEval> l) {
		final BHREmployeeEval[] employeeEvals = new BHREmployeeEval[l.size()];
		for (int loop = 0; loop < employeeEvals.length; loop++)
			employeeEvals[loop] = new BHREmployeeEval(l.get(loop));
		return employeeEvals;
	}

	/**
	 * @param hsu
	 * @param employeeId
	 * @return
	 */
	public static BHREmployeeEval[] list(final HibernateSessionUtil hsu, final String employeeId) {

		return makeArray(hsu.createCriteria(HrEmployeeEval.class)
				.orderByDesc(HrEmployeeEval.EVALDATE)
				.joinTo(HrEmployeeEval.EMPLOYEEBYEMPLOYEEID)
				.eq(Person.PERSONID, employeeId)
				.list());
	}

	/**
	 * @return
	 */
	public String getSupervisorFName() {
		if (hrEmployeeEval.getEmployeeBySupervisorId() != null)
			return hrEmployeeEval.getEmployeeBySupervisorId().getFname();

		return "";
	}

	public String getSupervisorNameLFM() {
		if (hrEmployeeEval.getEmployeeBySupervisorId() != null)
			return hrEmployeeEval.getEmployeeBySupervisorId().getNameLFM();

		return "";
	}

	/**
	 * @return
	 */
	public String getSupervisorLName() {
		if (hrEmployeeEval.getEmployeeBySupervisorId() != null)
			return hrEmployeeEval.getEmployeeBySupervisorId().getLname();

		return "";
	}

	/**
	 * @return
	 */
	public String getEmployeeId() {

		return hrEmployeeEval.getEmployeeByEmployeeId().getPersonId();
	}

	/**
	 * @return
	 */
	public String getSupervisorId() {
		if (hrEmployeeEval.getEmployeeBySupervisorId() != null)
			return hrEmployeeEval.getEmployeeBySupervisorId().getPersonId();

		return "";
	}

	/**
	 * @return @see com.arahant.beans.HrEmployeeEval#getComments()
	 */
	public String getComments() {
		return hrEmployeeEval.getComments();
	}

	/**
	 * @param comments
	 * @see com.arahant.beans.HrEmployeeEval#setComments(java.lang.String)
	 */
	public void setComments(final String comments) {
		hrEmployeeEval.setComments(comments);
	}

	/**
	 * @return @throws ArahantException
	 */
	public BHREmployeeEvalDetail[] listDetails() throws ArahantException {


		// DAVID TO REWORK THIS IF A BETTER SOLUTION IS NEEDED

		LinkedList<BHREmployeeEvalDetail> retList = new LinkedList<BHREmployeeEvalDetail>();

		// grab all possible evaluaton categories
		final List categories = ArahantSession.getHSU().createCriteria(HrEvalCategory.class).orderBy(HrEvalCategory.NAME).list();

		// grab all empoloyee evaluations done
		final HrEmployeeEval eval = hrEmployeeEval;
		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrEmplEvalDetail.class)
				.eq(HrEmplEvalDetail.HREMPLOYEEEVAL, eval);
		final List evals = hcu.list();



		// spin through the categories and determine if an eval exists
		HrEvalCategory category;
		HrEmplEvalDetail detail;
		BHREmployeeEvalDetail eedet;
		for (int idx = 0; idx < categories.size(); idx++) {
			category = (HrEvalCategory) categories.get(idx);
			eedet = null;

			// look for this item as a detail
			for (int idx2 = 0; idx2 < evals.size(); idx2++) {
				detail = (HrEmplEvalDetail) evals.get(idx2);

				// check candidate
				if (category.getEvalCatId().equals(detail.getHrEvalCategory().getEvalCatId())) {
					// we got a matching eval
					eedet = new BHREmployeeEvalDetail(detail);
					evals.remove(idx2);
					break;
				}
			}

			// did we get a matching detail?
			if (eedet == null) {

				//if eval is expired, skip this one
				if (category.getLastActiveDate() != 0 && category.getLastActiveDate() < DateUtils.now())
					continue;

				//if eval not required yet, skip it
				if (category.getFirstActiveDate() > DateUtils.now())
					continue;

				// no so make an "empty" one
				eedet = new BHREmployeeEvalDetail();
				eedet.stub();
				eedet.setEmployeeEvalId(getEmployeeEvalId());
				eedet.setEvalCategoryId(category.getEvalCatId());
				eedet.setNotes("");
				eedet.setScore((short) 0);
			}

			retList.add(eedet);
		}



		return retList.toArray(new BHREmployeeEvalDetail[retList.size()]);
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrEmployeeEval.setEmployeeByEmployeeId(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param supervisorId
	 */
	public void setSupervisorId(final String supervisorId) {
		Employee sup = ArahantSession.getHSU().get(Employee.class, supervisorId);
		if (sup == null)
			throw new ArahantWarning("The supervisor provided is not an actual employee in the system.");
		hrEmployeeEval.setEmployeeBySupervisorId(ArahantSession.getHSU().get(Employee.class, supervisorId));
	}

	/**
	 * @return (Employee Edit, Supervisor Edit, Finalized on <date>)
	 */
	public String getStatus() {
		switch (hrEmployeeEval.getState()) {
			case 'E':
				return "Employee Edit";
			case 'S':
				return "Supervisor Edit";
			case 'F':
				return "Finalized on " + DateUtils.getDateFormatted(hrEmployeeEval.getFinalDate());
		}
		return "Unknown status!";
	}

	public int getFinalDate() {
		return hrEmployeeEval.getFinalDate();
	}

	/**
	 * @return
	 */
	public String getEmployeeComments() {

		return hrEmployeeEval.getEComments();
	}

	/**
	 * @return
	 */
	public String getInternalSupervisorComments() {

		return hrEmployeeEval.getPComments();
	}

	/**
	 * @param employeeComments
	 */
	public void setEmployeeComments(final String employeeComments) {
		hrEmployeeEval.setEComments(employeeComments);
	}

	/**
	 * @param internalSupervisorComments
	 */
	public void setInternalSupervisorComments(final String internalSupervisorComments) {
		hrEmployeeEval.setPComments(internalSupervisorComments);
	}

	/**
	 * @throws ArahantException
	 *
	 */
	public void markForEmployeeEdit() throws ArahantException {
		hrEmployeeEval.setState('E');
		BMessage.send(hrEmployeeEval.getEmployeeBySupervisorId(), hrEmployeeEval.getEmployeeByEmployeeId(),
				"Evaluation",
				"Please edit your evaluation.");
	}

	/**
	 * @throws ArahantException
	 *
	 */
	public void finalizeEvaluation() throws ArahantException {
		hrEmployeeEval.setState('F');
		hrEmployeeEval.setFinalDate(DateUtils.now());
		BMessage.send(hrEmployeeEval.getEmployeeBySupervisorId(), hrEmployeeEval.getEmployeeByEmployeeId(),
				"Evaluation",
				"Your evaluation has been finalized.");
	}

	/**
	 * @throws ArahantException
	 *
	 */
	public void markForSupervisorEdit() throws ArahantException {
		hrEmployeeEval.setState('S');
		BMessage.send(hrEmployeeEval.getEmployeeByEmployeeId(), hrEmployeeEval.getEmployeeBySupervisorId(),
				"Evaluation",
				"Employee is finished with evaluation.");
	}

	/**
	 * @param hsu
	 * @param evalIds
	 * @return
	 * @throws IOException
	 * @throws ArahantException
	 * @throws DocumentException
	 */
	public static String getEmployeeVersionReport(final HibernateSessionUtil hsu, final String[] evalIds) throws IOException, DocumentException, ArahantException {
		final BHREmployeeEval[] employeeEvals = makeArray(hsu.createCriteria(HrEmployeeEval.class).in(HrEmployeeEval.EMPLOYEEEVALID, evalIds).list());

		return new HREvaluationResponseReport().build(hsu, employeeEvals[0].getEmployeeId(), employeeEvals);

	}

	/**
	 * @return
	 */
	public String getState() {

		return hrEmployeeEval.getState() + "";
	}
}
