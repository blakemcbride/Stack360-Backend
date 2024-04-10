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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitPackage;
import com.arahant.beans.HrBenefitPackageJoin;
import com.arahant.beans.HrBenefitPackageJoinId;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.HRBenefitPackageReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.List;

public class BHRBenefitPackage extends BusinessLogicBase implements IDBFunctions {

	private HrBenefitPackage hrBenefitPackage;

	public BHRBenefitPackage(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param package1
	 */
	public BHRBenefitPackage(final HrBenefitPackage package1) {
		hrBenefitPackage = package1;
	}

	/**
	 */
	public BHRBenefitPackage() {
	}


	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrBenefitPackage = new HrBenefitPackage();
		hrBenefitPackage.generateId();
		return hrBenefitPackage.getPackageId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		if (hrBenefitPackage == null)
			return;
		ArahantSession.getHSU().delete(hrBenefitPackage.getHrBenefitPackageJoins());
		ArahantSession.getHSU().delete(hrBenefitPackage);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrBenefitPackage);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrBenefitPackage = ArahantSession.getHSU().get(HrBenefitPackage.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrBenefitPackage);
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRBenefitPackage[] list(final HibernateSessionUtil hsu) {

		return BHRBenefitPackage.makeArray(hsu.createCriteria(HrBenefitPackage.class).orderBy(HrBenefitPackage.NAME).list());

	}

	/**
	 * @param name
	 */
	static BHRBenefitPackage[] makeArray(final List<HrBenefitPackage> l) {
		final BHRBenefitPackage[] ret = new BHRBenefitPackage[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitPackage(l.get(loop));
		return ret;
	}

	/**
	 * @return @see com.arahant.beans.HrBenefitPackage#getName()
	 */
	public String getName() {
		return hrBenefitPackage.getName();
	}

	/**
	 * @return @see com.arahant.beans.HrBenefitPackage#getPackageId()
	 */
	public String getPackageId() {
		return hrBenefitPackage.getPackageId();
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrBenefitPackage#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrBenefitPackage.setName(name);
	}

	/**
	 * @param packageId
	 * @see com.arahant.beans.HrBenefitPackage#setPackageId(java.lang.String)
	 */
	public void setPackageId(final String packageId) {
		hrBenefitPackage.setPackageId(packageId);
	}

	/**
	 * @return
	 */
	public BHRBenefit[] listAssignedBenefits() {

		return BHRBenefit.makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).joinTo(HrBenefit.HRBENEFITPACKAGEJOINS).eq(HrBenefitPackageJoin.HRBENEFITPACKAGE, hrBenefitPackage).list());

	}

	/**
	 * @return
	 */
	public BHRBenefit[] listUnassignedBenefits() {


		final List assigned = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).joinTo(HrBenefit.HRBENEFITPACKAGEJOINS).eq(HrBenefitPackageJoin.HRBENEFITPACKAGE, hrBenefitPackage).list();

		final List<HrBenefit> l = ArahantSession.getHSU().getAll(HrBenefit.class);
		l.removeAll(assigned);

		return BHRBenefit.makeArray(l);
	}

	/**
	 * @param benefitIds
	 */
	public void assign(final String[] benefitIds) {
		for (final String element : benefitIds) {
			final HrBenefitPackageJoin bpj = new HrBenefitPackageJoin();
			bpj.setHrBenefit(ArahantSession.getHSU().get(HrBenefit.class, element));
			bpj.setHrBenefitPackage(hrBenefitPackage);
			final HrBenefitPackageJoinId id = new HrBenefitPackageJoinId();
			id.setBenefitId(element);
			id.setPackageId(hrBenefitPackage.getPackageId());
			bpj.setId(id);
			ArahantSession.getHSU().insert(bpj);
		}
	}

	/**
	 * @param benefitIds
	 * @throws ArahantDeleteException
	 */
	public void unassign(final String[] benefitIds) throws ArahantDeleteException {
		ArahantSession.getHSU().delete(ArahantSession.getHSU().createCriteria(HrBenefitPackageJoin.class).eq(HrBenefitPackageJoin.HRBENEFITPACKAGE, hrBenefitPackage).joinTo(HrBenefitPackageJoin.HRBENEFIT).in(HrBenefit.BENEFITID, benefitIds).list());
	}

	public static String getReport(final String packageId, final HibernateSessionUtil hsu) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRBPRept", ".pdf");

		new HRBenefitPackageReport().build(hsu, fyle, hsu.get(HrBenefitPackage.class, packageId));

		return FileSystemUtils.getHTTPPath(fyle);
	}
}
