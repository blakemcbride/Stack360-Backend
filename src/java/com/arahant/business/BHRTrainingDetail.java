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

import com.arahant.beans.Employee;
import com.arahant.beans.HrTrainingCategory;
import com.arahant.beans.HrTrainingDetail;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.HRTrainingReport;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.List;

public class BHRTrainingDetail extends BusinessLogicBase implements IDBFunctions {

	private HrTrainingDetail hrTrainingDetail;
	private static final transient ArahantLogger logger = new ArahantLogger(BHRTrainingDetail.class);

	public BHRTrainingDetail() {
		logger.debug("Created");
	}

	/**
	 * @param key
	 * @throws ArahantException
	 */
	public BHRTrainingDetail(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param detail
	 */
	public BHRTrainingDetail(final HrTrainingDetail detail) {
		hrTrainingDetail = detail;
	}

	@Override
	public String create() throws ArahantException {
		hrTrainingDetail = new HrTrainingDetail();
		hrTrainingDetail.generateId();
		return hrTrainingDetail.getTrainingId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrTrainingDetail);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrTrainingDetail);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrTrainingDetail = ArahantSession.getHSU().get(HrTrainingDetail.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrTrainingDetail);
	}

	public int getExpireDate() {
		return hrTrainingDetail.getExpireDate();
	}

	public int getTrainingDate() {
		return hrTrainingDetail.getTrainingDate();
	}

	public float getTrainingHours() {
		return hrTrainingDetail.getTrainingHours();
	}

	public String getTrainingId() {
		return hrTrainingDetail.getTrainingId();
	}

	public void setExpireDate(final int expireDate) {
		hrTrainingDetail.setExpireDate(expireDate);
	}

	public void setTrainingDate(final int trainingDate) {
		hrTrainingDetail.setTrainingDate(trainingDate);
	}

	public void setTrainingHours(final float trainingHours) {
		hrTrainingDetail.setTrainingHours(trainingHours);
	}

	public void setTrainingId(final String trainingId) {
		hrTrainingDetail.setTrainingId(trainingId);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String element : ids)
			new BHRTrainingDetail(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String employeeId) throws Exception {
		final File file = FileSystemUtils.createTempFile("HRTrainRept", ".pdf");
		new HRTrainingReport().build(hsu, file, employeeId);
		return FileSystemUtils.getHTTPPath(file);
	}

	/**
	 * @return
	 */
	public String getEmployeeId() {
		return hrTrainingDetail.getEmployee().getPersonId();
	}

	/**
	 * @return
	 */
	public String getTrainingCategoryId() {
		return hrTrainingDetail.getHrTrainingCategory().getCatId();
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrTrainingDetail.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param trainingCategoryId
	 */
	public void setTrainingCategoryId(final String trainingCategoryId) {
		hrTrainingDetail.setHrTrainingCategory(ArahantSession.getHSU().get(HrTrainingCategory.class, trainingCategoryId));
	}

	/**
	 * @return
	 */
	public String getTrainingCategoryName() {
		return hrTrainingDetail.getHrTrainingCategory().getName();
	}

	/**
	 * @param l
	 * @return
	 */
	static BHRTrainingDetail[] makeArray(final List<HrTrainingDetail> l) {
		final BHRTrainingDetail[] ret = new BHRTrainingDetail[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRTrainingDetail(l.get(loop));

		return ret;
	}
}
