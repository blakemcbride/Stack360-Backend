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
import com.arahant.utils.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

public class BCompany extends BCompanyBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BCompany.class);
	private CompanyDetail companyDetail;

	public BCompany() {
	}

	/**
	 * @param orgGroupId
	 * @throws ArahantException
	 */
	public BCompany(final String orgGroupId) throws ArahantException {
		companyDetail = ArahantSession.getHSU().get(CompanyDetail.class, orgGroupId);
		company_base = companyDetail;
		initMembers(companyDetail);
		company_base = companyDetail;
	}

	/**
	 * @param detail
	 * @throws ArahantException
	 */
	public BCompany(final CompanyDetail detail) throws ArahantException {
		companyDetail = detail;
		initMembers(companyDetail);
	}

	public String getARAccountId() {
		if (companyDetail.getArAccount() == null)
			return "";
		return companyDetail.getArAccount().getGlAccountId();
	}

	public boolean getAccrualsUseTimeOffRequest() {
		return companyDetail.getMarkTimeOffOnApproval() == 'Y';
	}

	public double getBillingRate() {
		return companyDetail.getBillingRate();
	}

	public static String getDefaultBillingRateFormatted() {
		return "Employee Rate";
	}

	public String getCashAccountId() {
		if (companyDetail.getCashAccount() != null)
			return companyDetail.getCashAccount().getGlAccountId();
		return "";
	}

	public String getDefaultBillingRate() {
		if (getBillingRate() < .01)
			return getDefaultBillingRateFormatted();
		return MoneyUtils.formatMoney(getBillingRate());
	}

	public void setAccrualsUseTimeOffRequest(boolean accrualsUseTimeOffRequest) {
		companyDetail.setMarkTimeOffOnApproval(accrualsUseTimeOffRequest ? 'Y' : 'N');
	}

	public void setCashAccountId(String cashAccountId) {
		companyDetail.setCashAccount(ArahantSession.getHSU().get(GlAccount.class, cashAccountId));
	}

	public void setARAccountId(String arAccountId) {
		companyDetail.setArAccount(ArahantSession.getHSU().get(GlAccount.class, arAccountId));
	}

	public String getEmployeeAdvanceAccountId() {
		if (companyDetail.getEmployeeAdvanceAccount() != null)
			return companyDetail.getEmployeeAdvanceAccount().getGlAccountId();
		return "";
	}

	public void setEmployeeAdvanceAccountId(String employeeAdvanceAccountId) {
		companyDetail.setEmployeeAdvanceAccount(ArahantSession.getHSU().get(GlAccount.class, employeeAdvanceAccountId));
	}

	@Override
	public int getOrgGroupType() {
		return COMPANY_TYPE;
	}

	@Override
	public CompanyDetail getBean() {
		return companyDetail;
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			super.delete();
			ArahantSession.getHSU().delete(companyDetail);
		} catch (final RuntimeException e) {
			logger.error(e);
			throw new ArahantDeleteException();
		}
	}

	@Override
	public String create() throws ArahantException {
		companyDetail = new CompanyDetail();
		company_base = companyDetail;
		final String id = super.create();

		companyDetail.setOrgGroupId(id);
		return id;
	}

	@Override
	public void insert() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		company_base.setOwningCompany(null);
		hsu.insert(companyDetail);
		super.insert();

		//do I now have more than one?
		if (!ArahantSession.multipleCompanySupport && hsu.createCriteria(CompanyDetail.class).countNoOrder() > 1)
			ArahantSession.multipleCompanySupport = true;
	}

	public void setBillingRate(double billingRate) {
		companyDetail.setBillingRate((float) billingRate);
	}

	public void setExtRef(String id) {
		companyDetail.setExternalId(id);
	}

	public void setGLARAccountId(String glARAccountId) {
		companyDetail.setArAccount(ArahantSession.getHSU().get(GlAccount.class, glARAccountId));
	}

	public void setGLCashAccountId(String glCashAccountId) {
		companyDetail.setCashAccount(ArahantSession.getHSU().get(GlAccount.class, glCashAccountId));
	}

	public void setGLEmployeeAdvanceAccountId(String glEmployeeAdvanceAccountId) {
		companyDetail.setEmployeeAdvanceAccount(ArahantSession.getHSU().get(GlAccount.class, glEmployeeAdvanceAccountId));
	}
	
	public short getEmailOutType() {
		return companyDetail.getEmailOutType();
	}

	public void setEmailOutType(short emailOutType) {
		companyDetail.setEmailOutType(emailOutType);
	}

	public char getEmailOutAuthentication() {
		return companyDetail.getEmailOutAuthentication();
	}

	public void setEmailOutAuthentication(char emailOutAuthentication) {
		companyDetail.setEmailOutAuthentication(emailOutAuthentication);
	}

	public String getEmailOutHost() {
		return companyDetail.getEmailOutHost();
	}

	public void setEmailOutHost(String emailOutHost) {
		companyDetail.setEmailOutHost(emailOutHost);
	}

	public String getEmailOutDomain() {
		return companyDetail.getEmailOutDomain();
	}

	public void setEmailOutDomain(String emailOutDomain) {
		companyDetail.setEmailOutDomain(emailOutDomain);
	}

	public int getEmailOutPort() {
		return companyDetail.getEmailOutPort();
	}

	public void setEmailOutPort(int emailOutPort) {
		companyDetail.setEmailOutPort(emailOutPort);
	}

	public char getEmailOutEncryption() {
		return companyDetail.getEmailOutEncryption();
	}

	public void setEmailOutEncryption(char emailOutEncryption) {
		companyDetail.setEmailOutEncryption(emailOutEncryption);
	}

	public String getEmailOutUserId() {
		return companyDetail.getEmailOutUserId();
	}

	public void setEmailOutUserId(String emailOutUserId) {
		companyDetail.setEmailOutUserId(emailOutUserId);
	}

	public String getEmailOutUserPw() {
		return companyDetail.getEmailOutUserPw();
	}

	public void setEmailOutUserPw(String emailOutUserPw) {
		companyDetail.setEmailOutUserPw(emailOutUserPw);
	}

	public String getEmailOutFromName() {
		return companyDetail.getEmailOutFromName();
	}

	public void setEmailOutFromName(String emailOutFromName) {
		companyDetail.setEmailOutFromName(emailOutFromName);
	}

	public String getEmailOutFromEmail() {
		return companyDetail.getEmailOutFromEmail();
	}

	public void setEmailOutFromEmail(String emailOutFromEmail) {
		companyDetail.setEmailOutFromEmail(emailOutFromEmail);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(companyDetail);
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] companyIds) throws ArahantException {
		for (final String element : companyIds)
			new BCompany(element).delete();
	}

	@Override
	public void load(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		companyDetail = ArahantSession.getHSU().get(CompanyDetail.class, key);
		company_base = companyDetail;
		initMembers(companyDetail);
	}

	public static BCompany[] listCompanies(final HibernateSessionUtil hsu) throws ArahantException {
		return makeArray(hsu.createCriteria(CompanyDetail.class).orderBy(CompanyDetail.NAME).list());

	}

	public static BCompany[] listAssCompanies(final HibernateSessionUtil hsu, final String employeeId) throws ArahantException {
		return makeArray(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, employeeId).list());

	}

	public static BCompany[] listCompaniesNoFilter(final HibernateSessionUtil hsu) throws ArahantException {
		return makeArray(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).list());
	}

	public static BCompany[] makeArray(final List<CompanyDetail> l) throws ArahantException {
		final BCompany[] companyList = new BCompany[l.size()];

		int index = 0;

		final Iterator<CompanyDetail> plistItr = l.iterator();

		while (plistItr.hasNext())
			companyList[index++] = new BCompany(plistItr.next());
		return companyList;
	}

	public char getAccountingBasis() {
		return companyDetail.getAccountingBasis();
	}

	/**
	 * @param accountingBasis
	 * @see com.arahant.beans.CompanyDetail#setAccountingBasis(char)
	 */
	public void setAccountingBasis(final char accountingBasis) {
		companyDetail.setAccountingBasis(accountingBasis);
	}

	/**
	 * @return
	 */
	public String getOrgGroupTypeName() {
		return "COMPANY";
	}

	@Override
	boolean checkMainContact(final String val) throws ArahantException {
		if (getMainContact() == null) {
			if (isEmpty(val))
				return false;
			final BEmployee be = new BEmployee(ArahantSession.getHSU());
			be.create();
			setMainContact(be);
		}
		return true;
	}

	@Override
	public String getOrgType() {
		return "Company";
	}

	public String getEeo1UnitId() {
		return companyDetail.getEeo1UnitId();
	}

	public void setEeo1UnitId(String value) {
		companyDetail.setEeo1UnitId(value);
	}

	public String getEeo1CompanyId() {
		return companyDetail.getEeo1CompanyId();
	}

	public void setEeo1CompanyId(String value) {
		companyDetail.setEeo1CompanyId(value);
	}

	public boolean getEeo1Establishment() {
		return companyDetail.getEeo1Establishment() == 'Y';
	}

	public void setEeo1Establishment(boolean value) {
		companyDetail.setEeo1Establishment(value ? 'Y' : 'N');
	}

	public boolean getEeo1Headquarters() {
		return companyDetail.getEeo1Headquarters() == 'Y';
	}

	public void setEeo1Headquarters(boolean value) {
		companyDetail.setEeo1Headquarters(value ? 'Y' : 'N');
	}

	public String getDunBradstreet() {
		return companyDetail.getDunBradstreet();
	}

	public void setDunBradstreet(String value) {
		companyDetail.setDunBradstreet(value);
	}

	public String getNaicsCode() {
		return companyDetail.getNaicsCode();
	}

	public void setNaicsCode(String value) {
		companyDetail.setNaicsCode(value);
	}

	public short getFiscalBeginningMonth() {
		return companyDetail.getFiscalBeginningMonth();
	}

	public void setFiscalBeginningMonth(short dt) {
		companyDetail.setFiscalBeginningMonth(dt);
	}

	public static BCompany[] searchWithURL(String name, int max) {
		return makeArray(ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).like(CompanyDetail.NAME, name).setMaxResults(max).isNotNull(CompanyDetail.ARAHANT_URL).list());
	}

	public void setLogoData(byte[] logo) {
		companyDetail.setLogo(logo);
	}

	/**
	 * Places the logo in a temporary file and returns the name of that file.
	 *
	 * @return
	 * @throws ArahantException
	 */
	public String getLogo() throws ArahantException {
		try {
			byte[] contents = companyDetail.getLogo();
			if (contents != null && contents.length != 0) {
				File logo = FileSystemUtils.createTempFile("logo", "." + companyDetail.getLogoExtension());
				FileOutputStream fos = new FileOutputStream(logo);
				fos.write(contents);
				fos.close();
				return logo.getAbsolutePath();
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public File getReportLogo() {
		String fileName = getLogo();  //  only accepts jpeg, png, or gif formats
		File logoFile = null;
		if (fileName != null)
			logoFile = new File(fileName);
		if (logoFile == null)
			try {
				logoFile = new File(FileSystemUtils.getWorkingDirectory(), "report-logo.gif");
			} catch (Exception e) {
			}
		return logoFile;
	}

	/**
	 * Put the company logo in a file and return a File pointer to it. If
	 * company is not passed, use current company.
	 *
	 * @param bc optional
	 * @return
	 */
	public static File getReportLogo(BCompany bc) {
		if (bc == null) {
			CompanyDetail cd = ArahantSession.getHSU().getCurrentCompany();
			if (cd != null)
				bc = new BCompany(cd);
		}
		return bc == null ? null : bc.getReportLogo();
	}

	public void addServicesToCompany(String[] serviceIds, int date, String externalId) {
		for (final String id : serviceIds) {
			BServiceSubscribedJoin j = new BServiceSubscribedJoin();
			j.create();
			j.setServiceId(id);
			j.setCompany(this);
			j.setFirstDate(date);
			j.getBean().setExternalId(externalId);
			j.insert();
		}
	}

	public void removeServicesFromCompany(String[] serviceIds, int date) {
		HibernateCriteriaUtil<ServiceSubscribedJoin> hcu = ArahantSession.getHSU().createCriteria(ServiceSubscribedJoin.class).eq(ServiceSubscribedJoin.COMPANY, getBean()).joinTo(ServiceSubscribedJoin.SERVICE).in(ServiceSubscribed.SERVICEID, serviceIds);
		for (final ServiceSubscribedJoin ssj : hcu.list()) {
			BServiceSubscribedJoin bss = new BServiceSubscribedJoin(ssj);
			bss.setLastDate(date);
			bss.update();
		}
	}

	public BProject[] listActiveProjects(int max) {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).setMaxResults(max).orderByDesc(Project.DATEREPORTED).orderByDesc(Project.TIME_REPORTED);
		hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.COMPANY_ID, company_base.getCompanyId());
		hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
		return BProject.makeArray(hcu.list());
	}

	public BProject[] listInactiveProjects(int max) {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).setMaxResults(max).orderByDesc(Project.DATEREPORTED).orderByDesc(Project.TIME_REPORTED);
		hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.COMPANY_ID, company_base.getCompanyId());
		hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'N');
		return BProject.makeArray(hcu.list());
	}

	public String getAgentStatus(BAgent agent) {
		//see if an agent is associated to a company
		//if approved with company then "Active/Authorized"
		//else Active
		//otherwise, if the agent is not associated with the company then Inactive
		HibernateCriteriaUtil<AgentJoin> hcu = ArahantSession.getHSU().createCriteria(AgentJoin.class).eq(AgentJoin.AGENT, agent.agent).eq(AgentJoin.COMPANY, companyDetail);

		if (hcu.exists())
			if (hcu.eq(AgentJoin.APPROVED, 'Y').exists())
				return "Active/Authorized";
			else
				return "Active";
		return "Inactive";
	}

	public void setLogoExtension(String s) {
		companyDetail.setLogoExtension(s);
	}

	public void setLogoSource(String s) {
		companyDetail.setLogoSource(s);
	}

	public BCompany clone(String newName) {
		BCompany newComp = new BCompany();
		newComp.create();
		newComp.setName(newName);
		newComp.insert();
		return newComp;
	}

	public static BCompany[] search(String name, int cap) {
		return makeArray(ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class).like(CompanyDetail.NAME, name).setMaxResults(cap).orderBy(CompanyDetail.NAME).list());
	}

	public static BCompany[] searchWithFilter(String name, int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(CompanyDetail.class).like(CompanyDetail.NAME, name).setMaxResults(cap).orderBy(CompanyDetail.NAME).list());
	}
}
