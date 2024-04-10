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
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BStandardProject extends BusinessLogicBase implements IDBFunctions {
	
	private static final transient ArahantLogger logger = new ArahantLogger(BStandardProject.class);
	private StandardProject standardProject;

	public BStandardProject() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BStandardProject(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param project
	 */
	public BStandardProject(final StandardProject project) {
		standardProject = project;
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		standardProject = new StandardProject();
		standardProject.generateId();
		return getProjectId();
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(standardProject);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(standardProject);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		standardProject = ArahantSession.getHSU().get(StandardProject.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(standardProject);
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getAllEmployees()
	 */
	public char getAllEmployees() {
		return standardProject.getAllEmployees();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getBillable()
	 */
	public char getBillable() {
		return standardProject.getBillable();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getBillingMethod()
	 */
	public Integer getBillingMethod() {
		return standardProject.getBillingMethod();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getBillingRate()
	 */
	public float getBillingRate() {
		return standardProject.getBillingRate();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getDescription()
	 */
	public String getDescription() {
		return standardProject.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getDetailDesc()
	 */
	public String getDetailDesc() {
		return standardProject.getDetailDesc();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getDollarCap()
	 */
	public double getDollarCap() {
		return standardProject.getDollarCap();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getNextBillingDate()
	 */
	public Integer getNextBillingDate() {
		return standardProject.getNextBillingDate();
	}

	/**
	 * @return @see com.arahant.beans.StandardProject#getProjectId()
	 */
	public String getProjectId() {
		return standardProject.getProjectId();
	}

	/**
	 * @param allEmployees
	 * @see com.arahant.beans.StandardProject#setAllEmployees(char)
	 */
	public void setAllEmployees(final char allEmployees) {
		standardProject.setAllEmployees(allEmployees);
	}

	/**
	 * @param billable
	 * @see com.arahant.beans.StandardProject#setBillable(char)
	 */
	public void setBillable(final char billable) {
		standardProject.setBillable(billable);
	}

	/**
	 * @param billingMethod
	 * @see
	 * com.arahant.beans.StandardProject#setBillingMethod(java.lang.Integer)
	 */
	public void setBillingMethod(final Integer billingMethod) {
		standardProject.setBillingMethod(billingMethod);
	}

	/**
	 * @param billingRate
	 * @see com.arahant.beans.StandardProject#setBillingRate(float)
	 */
	public void setBillingRate(final float billingRate) {
		standardProject.setBillingRate(billingRate);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.StandardProject#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		standardProject.setDescription(description);
	}

	/**
	 * @param detailDesc
	 * @see com.arahant.beans.StandardProject#setDetailDesc(java.lang.String)
	 */
	public void setDetailDesc(final String detailDesc) {
		standardProject.setDetailDesc(detailDesc);
	}

	/**
	 * @param dollarCap
	 * @see com.arahant.beans.StandardProject#setDollarCap(double)
	 */
	public void setDollarCap(final double dollarCap) {
		standardProject.setDollarCap(dollarCap);
	}

	/**
	 * @param employee
	 * @see
	 * com.arahant.beans.StandardProject#setEmployee(com.arahant.beans.Employee)
	 */
	public void setEmployee(final Employee employee) {
		standardProject.setEmployee(employee);
	}

	/**
	 * @param nextBillingDate
	 * @see
	 * com.arahant.beans.StandardProject#setNextBillingDate(java.lang.Integer)
	 */
	public void setNextBillingDate(final Integer nextBillingDate) {
		standardProject.setNextBillingDate(nextBillingDate);
	}

	/**
	 * @param orgGroup
	 * @see
	 * com.arahant.beans.StandardProject#setOrgGroup(com.arahant.beans.OrgGroup)
	 */
	public void setOrgGroup(final OrgGroup orgGroup) {
		standardProject.setOrgGroup(orgGroup);
	}

	/**
	 * @param person
	 * @see
	 * com.arahant.beans.StandardProject#setPerson(com.arahant.beans.Person)
	 */
	public void setPerson(final Person person) {
		standardProject.setPerson(person);
	}

	/**
	 * @param projectId
	 * @see com.arahant.beans.StandardProject#setProjectId(java.lang.String)
	 */
	public void setProjectId(final String projectId) {
		standardProject.setProjectId(projectId);
	}

	/**
	 * @param reference
	 * @see com.arahant.beans.StandardProject#setReference(java.lang.String)
	 */
	public void setReference(final String reference) {
		standardProject.setReference(reference);
	}

	/**
	 * @param requesterName
	 * @see com.arahant.beans.StandardProject#setRequesterName(java.lang.String)
	 */
	public void setRequesterName(final String requesterName) {
		standardProject.setRequesterName(requesterName);
	}

	/**
	 * @param hsu
	 * @param projectIds
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] projectIds) throws ArahantDeleteException, ArahantException {
		for (final String element : projectIds)
			new BStandardProject(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BStandardProject[] list(final HibernateSessionUtil hsu) {
		final List splList = hsu.createCriteria(StandardProject.class).orderBy(StandardProject.DESCRIPTION).list();


		final BStandardProject[] spl = new BStandardProject[splList.size()];
		for (int loop = 0; loop < spl.length; loop++)
			spl[loop] = new BStandardProject((StandardProject) splList.get(loop));

		return spl;
	}

	/**
	 * @return
	 */
	public String getProjectCategoryId() {

		return standardProject.getProjectCategory().getProjectCategoryId();
	}

	/**
	 * @return
	 */
	public String getProjectCategoryCode() {

		return standardProject.getProjectCategory().getCode();
	}

	/**
	 * @return
	 */
	public String getEmployeeId() {

		if (standardProject.getEmployee() != null)
			return standardProject.getEmployee().getPersonId();
		return "";
	}

	/**
	 * @return
	 */
	public String getEmployeeName() {

		if (standardProject.getEmployee() != null)
			return standardProject.getEmployee().getNameLFM();
		return "";
	}

	/**
	 * @return
	 */
	public String getProjectTypeId() {

		return standardProject.getProjectType().getProjectTypeId();
	}

	/**
	 * @return
	 */
	public String getProjectCode() {

		return standardProject.getProjectType().getCode();
	}

	/**
	 * @return
	 */
	public String getReference() {

		return standardProject.getReference();
	}

	/**
	 * @return
	 */
	public String getRequesterName() {

		return standardProject.getRequesterName();
	}

	/**
	 * @return
	 */
	public String getProductId() {
		if (standardProject.getProductService() != null)
			return standardProject.getProductService().getProductId();
		return "";
	}

	/**
	 * @return
	 */
	public String getProductName() {

		if (standardProject.getProductService() != null)
			return standardProject.getProductService().getDescription();
		return "";
	}

	/**
	 * @param projectTypeId
	 */
	public void setProjectTypeId(final String projectTypeId) {
		standardProject.setProjectType(ArahantSession.getHSU().get(ProjectType.class, projectTypeId));
	}

	/**
	 * @param projectCategoryId
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		standardProject.setProjectCategory(ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		standardProject.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param productId
	 */
	public void setProductServiceId(final String productId) {
		standardProject.setProductService(ArahantSession.getHSU().get(Service.class, productId));
	}

	public BService getProduct() {
		if (standardProject.getProductService() == null)
			return null;
		return new BService(standardProject.getProductService());
	}

	public BEmployee getEmployee() {
		if (standardProject.getEmployee() == null)
			return null;
		return new BEmployee(standardProject.getEmployee());
	}

	public BProjectCategory getProjectCategory() {
		if (standardProject.getProjectCategory() == null)
			return null;
		return new BProjectCategory(standardProject.getProjectCategory());
	}

	public BProjectType getProjectType() {
		if (standardProject.getProjectType() == null)
			return null;
		return new BProjectType(standardProject.getProjectType());
	}

	public CompanyDetail getCompany() {
		return standardProject.getCompany();
	}

	public void setCompany(CompanyDetail cd) {
		standardProject.setCompany(cd);
	}
}
