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
import com.arahant.business.interfaces.ISearchClientCompanies;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.ClientReport;
import com.arahant.utils.*;

import java.io.Serializable;
import java.util.*;

public class BClientCompany extends BCompanyBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BClientCompany.class);
	private ClientCompany clientCompany;

	public static BClientCompany convertFromProspect(String id) {

		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			if (hsu.get(OrgGroup.class, id) == null) //prevent any sql injection attacks
				throw new ArahantException("Bad ID passed in to convert from prospect " + id);

			//remove from any parent groups
			hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.CHILD_ID, id).delete();

			String statusId = hsu.createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ).first().getClientStatusId();
			//TODO: someday find a way to change this to use hibernate 
			//create a client company record
			hsu.executeSQL("insert into client (org_group_id,company_id,client_status_id) values ('" + id + "','" + hsu.getCurrentCompany().getOrgGroupId() + "','" + statusId + "')");

			hsu.clear();//have to clear so it will be able to see the client

			//delete the prospect company record
			hsu.executeSQL("delete from prospect where org_group_id='" + id + "'");
			//change org group types in org groups
			hsu.executeSQL("update org_group set org_group_type=" + CLIENT_TYPE + " where owning_entity_id='" + id + "'");
			hsu.executeSQL("update company_base set org_group_type=" + CLIENT_TYPE + " where org_group_id='" + id + "'");
			//change org group types in contacts
			BOrgGroup borg = new BOrgGroup(id);
			Set<String> perIds = borg.getAllPersonIdsForOrgGroupHierarchy(false);
			for (Person p : hsu.createCriteria(Person.class).in(Person.PERSONID, perIds).list()) {
				p.setOrgGroupType(CLIENT_TYPE);
				hsu.saveOrUpdate(p);
			}
			//create client contact records for all contacts
			hsu.executeSQL("insert into client_contact select person_id from person where person_id in (select oga.person_id from org_group_association oga "
					+ "join client cc on cc.org_group_id=oga.org_group_id) and person_id not in (select person_id from client_contact)");
			return new BClientCompany(id);
		} catch (Exception e) {
			throw new ArahantException("Can not create new client.", e);
		}

	}

	public BClientCompany() {
	}

	public BClientCompany(final String key) throws ArahantException {
		internalLoad(key);
		company_base = clientCompany;
	}

	/**
	 * @param company
	 * @throws ArahantException
	 */
	public BClientCompany(final ClientCompany company) throws ArahantException {
		clientCompany = company;
		super.company_base = clientCompany;
		if (!company.getOrgGroupId().equals("ReqCo"))
			initMembers(clientCompany);
	}

	public static void deleteCompanies(final HibernateSessionUtil hsu, final String[] companyIds) throws ArahantException {
		for (final String element : companyIds) {
			final BCompanyBase bc = new BClientCompany(element);
			bc.delete();
		}
	}

	public static String getDefaultBillingRateFormatted() {
		CompanyDetail cd = ArahantSession.getHSU().getFirst(CompanyDetail.class);
		if (cd.getBillingRate() != 0)
			return MoneyUtils.formatMoney(cd.getBillingRate());

		return "Employee Rate";
	}

	public static float getDefaultBillingRate() {
		CompanyDetail cd = ArahantSession.getHSU().getFirst(CompanyDetail.class);
		if (cd.getBillingRate() != 0)
			return cd.getBillingRate();
		return 0;
	}

	public int getLastContactDate() {
		return clientCompany.getLastContactDate();
	}

	public void setLastContactDate(final int lastContactDate) {
		clientCompany.setLastContactDate(lastContactDate);
	}

	public ClientStatus getClientStatus() {
		return clientCompany.getClientStatus();
	}

	public void setClientStatus(ClientStatus clientStatus) {
		clientCompany.setClientStatus(clientStatus);
	}

	public String getClientStatusId() {
		return clientCompany.getClientStatusId();
	}

	public void setClientStatusId(String clientStatusId) {
		setClientStatus(ArahantSession.getHSU().get(ClientStatus.class, clientStatusId));
	}

	public String getStatusComments() {
		return clientCompany.getStatusComments();
	}

	public void setStatusComments(final String statusComments) {
		clientCompany.setStatusComments(statusComments);
	}
	
	public String getBillToName() {
		return clientCompany.getBillToName();
	}

	public char getCopyPicturesToDisk() {
		return clientCompany.getCopyPicturesToDisk();
	}

	public void setCopyPicturesToDisk(char copyPicturesToDisk) {
		clientCompany.setCopyPicturesToDisk(copyPicturesToDisk);
	}

	public String getPictureDiskPath() {
		return clientCompany.getPictureDiskPath();
	}

	public void setPictureDiskPath(String pictureDiskPath) {
		clientCompany.setPictureDiskPath(pictureDiskPath);
	}

	public char getCopyOnlyExternal() {
		return clientCompany.getCopyOnlyExternal();
	}

	public void setCopyOnlyExternal(char c) {
		clientCompany.setCopyOnlyExternal(c);
	}

	public char getCopyInactiveProjects() {
		return clientCompany.getCopyInactiveProjects();
	}

	public void setCopyInactiveProjects(char c) {
		clientCompany.setCopyInactiveProjects(c);
	}

	@Override
	public ClientCompany getBean() {
		return clientCompany;
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		clientCompany = ArahantSession.getHSU().get(ClientCompany.class, key);
		if (clientCompany == null)
			throw new ArahantException("Failed to load client with key " + key);
		company_base = clientCompany;
		initMembers(clientCompany);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			super.delete();
			ArahantSession.getHSU().delete(clientCompany);
		} catch (final Exception e) {
			throw new ArahantDeleteException();
		}

	}

	/**
	 * @throws ArahantException
	 *
	 */
	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(clientCompany);
	}

	@Override
	public String create() throws ArahantException {
		clientCompany = new ClientCompany();
		company_base = clientCompany;
		super.create();
		clientCompany.setOrgGroupId(getOrgGroupId());
		clientCompany.setOrgGroupType(CLIENT_TYPE);
		clientCompany.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		return clientCompany.getOrgGroupId();
	}

	@Override
	public void insert() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		company_base.setOwningCompany(null);
		if (clientCompany.getClientStatus() == null)
			clientCompany.setClientStatus(hsu.createCriteria(ClientStatus.class).orderBy(ClientStatus.SEQ).first());

		hsu.insert(clientCompany);
		super.insert();
	}

	public static BClientCompany[] searchClientCompanies(final HibernateSessionUtil hsu, final ISearchClientCompanies in, final int max) throws ArahantException {
		final HibernateCriteriaUtil<ClientCompany> hcu = hsu.createCriteria(ClientCompany.class);

		if (max > 0)
			hcu.setMaxResults(max);

		hcu.like(OrgGroup.NAME, in.getName());
		hcu.like(OrgGroup.EXTERNAL_REF, in.getId());
		hcu.orderBy(OrgGroup.NAME);

		if ((!isEmpty(in.getMainContactFirstName()) && !"%".equals(in.getMainContactFirstName()))
				|| (!isEmpty(in.getMainContactLastName()) && !"%".equals(in.getMainContactLastName()))) {

			final HibernateCriteriaUtil joinOrgAssoc = hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
			final HibernateCriteriaUtil joinPerson = joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);
			joinPerson.like(Person.FNAME, in.getMainContactFirstName());
			joinPerson.like(Person.LNAME, in.getMainContactLastName());
		}

		if (in.getStatus() == 1) // active
			hcu.eq(ClientCompany.INACTIVEDATE, 0);
		else if (in.getStatus() == 2) // inactive
			hcu.ne(ClientCompany.INACTIVEDATE, 0);
		return makeArray(hcu.list());
	}

	static BClientCompany[] makeArray(final List<ClientCompany> l) throws ArahantException {

		final BClientCompany[] ret = new BClientCompany[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BClientCompany(l.get(loop));

		return ret;
	}

	/**
	 * @return @see com.arahant.beans.ClientCompany#getBillingRate()
	 */
	public float getBillingRate() {
		return clientCompany.getBillingRate();
	}

	/**
	 * @return @see com.arahant.beans.ClientCompany#getContractDate()
	 */
	public int getContractDate() {
		return clientCompany.getContractDate();
	}

	/**
	 * @return @see com.arahant.beans.ClientCompany#getInactiveDate()
	 */
	public int getInactiveDate() {
		return clientCompany.getInactiveDate();
	}

	/**
	 * @param billingRate
	 * @see com.arahant.beans.ClientCompany#setBillingRate(float)
	 */
	public void setBillingRate(final float billingRate) {
		clientCompany.setBillingRate(billingRate);
	}

	/**
	 * @param contractDate
	 * @see com.arahant.beans.ClientCompany#setContractDate(int)
	 */
	public void setContractDate(final int contractDate) {
		clientCompany.setContractDate(contractDate);
	}

	/**
	 * @param inactiveDate
	 * @see com.arahant.beans.ClientCompany#setInactiveDate(int)
	 */
	public void setInactiveDate(final int inactiveDate) {
		clientCompany.setInactiveDate(inactiveDate);
	}

	public short getPaymentTerms() {
		return clientCompany.getPaymentTerms();
	}

	public void setPaymentTerms(short terms) {
		clientCompany.setPaymentTerms(terms);
	}

	public String getVendorNumber() {
		return clientCompany.getVendorNumber();
	}

	public void setVendorNumber(String vin) {
		clientCompany.setVendorNumber(vin);
	}
	/**
	 * @param orgGroupId
	 * @see com.arahant.beans.ClientCompany#setOrgGroupId(java.lang.String)
	 */
	public void setOrgGroupId(final String orgGroupId) {
		clientCompany.setOrgGroupId(orgGroupId);
	}

	public String getGlSalesAccount() {
		if (clientCompany.getGlAccountByDfltSalesAcct() == null)
			return "";
		return clientCompany.getGlAccountByDfltSalesAcct().getGlAccountId();
	}

	public String getDefaultProjectCode() {
	    return clientCompany.getDefaultProjectCode();
    }

    public void setDefaultProjectCode(String pc) {
        clientCompany.setDefaultProjectCode(pc);
    }

	/**
	 * @return
	 */
	public String getOrgGroupTypeName() {
		if ("ReqCo".equals(clientCompany.getOrgGroupId()))
			return "ANY";
		return "CLIENT";
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.BCompanyBase#getOrgGroupType()
	 */
	@Override
	public int getOrgGroupType() {
		if ("ReqCo".equals(clientCompany.getOrgGroupId()))
			return 0;
		return CLIENT_TYPE;
	}

	/**
	 * @param glSalesAccount
	 */
	public void setGlSalesAccount(final String glSalesAccount) {
		final GlAccount acc = ArahantSession.getHSU().get(GlAccount.class, glSalesAccount);
		if (acc != null)
			clientCompany.setGlAccountByDfltSalesAcct(acc);
	}

	@Override
	boolean checkMainContact(final String val) throws ArahantException {
		if (getMainContact() == null) {
			if (isEmpty(val))
				return false;

			final BClientContact be = new BClientContact();
			be.create();
			//be.insert();
			setMainContact(be);

		}
		return true;
	}

	public CompanyDetail getAssociatedCompany() {
		return clientCompany.getAssociatedCompany();
	}

	public static BCompanyBase[] listWithBillingIndicator(final HibernateSessionUtil hsu) throws ArahantException {
		final List<ClientCompany> plist = new LinkedList<ClientCompany>();

		BCompanyBase[] companyList;

		final List<ClientCompany> otherComps = hsu.createCriteria(ClientCompany.class).list();

		final List<ClientCompany> resList = hsu.createCriteria(ClientCompany.class).distinct().eq(CompanyBase.ORGGROUPTYPE, CLIENT_TYPE).joinTo(CompanyBase.PROJECTS).joinTo(Project.TIMESHEETS).eq(Timesheet.BILLABLE, 'Y').eq(Timesheet.STATE, TIMESHEET_APPROVED).list();

		otherComps.removeAll(resList);

		final Iterator<ClientCompany> resItr = resList.iterator();
		while (resItr.hasNext())
			try {
				final ClientCompany c = resItr.next();
				c.setName("*" + c.getName());
			} catch (final Exception e) //something is wrong, no org group 
			{
				continue;
			}

		plist.addAll(resList);
		plist.addAll(otherComps);


		//if (plist.size()!=0)
		{
			java.util.Collections.sort(plist, new CompanyComparator());
			companyList = new BCompanyBase[plist.size()];


			int index = 0;

			final Iterator<ClientCompany> plistItr = plist.iterator();

			while (plistItr.hasNext()) {
				final ClientCompany company = plistItr.next();
				companyList[index] = new BClientCompany(company);
				index++;
			}
		}
		return companyList;
	}

	static class CompanyComparator implements Comparator<CompanyBase>, Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 5711950108682321389L;

		/*
		 * (non-Javadoc) @see java.util.Comparator#compare(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public int compare(final CompanyBase arg0, final CompanyBase arg1) {

			return arg0.getName().compareTo(arg1.getName());
		}
	}

	public BTimesheet[] listBillableTimesheets() {
		final HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class);

		hcu.eq(Timesheet.BILLABLE, 'Y');
		hcu.eq(Timesheet.STATE, TIMESHEET_APPROVED);

		hcu.in(ProjectShift.PROJECT, company_base.getProjects());
		hcu.joinTo(Timesheet.PROJECTSHIFT);
		hcu.joinTo(ProjectShift.PROJECT);
		hcu.orderBy(Project.PROJECTNAME);
		hcu.orderBy(Timesheet.WORKDATE);

		return BTimesheet.makeArray(hcu.list());
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.BCompanyBase#getOrgType()
	 */
	@Override
	public String getOrgType() {
		if ("ReqCo".equals(getOrgGroupId()))
			return "Any";
		return "Client";
	}

	public static BCompanyBase[] list(final HibernateSessionUtil hsu, final int max) throws ArahantException {
		final HibernateCriteriaUtil<ClientCompany> hcu = hsu.createCriteria(ClientCompany.class);

		if (max > 0)
			hcu.setMaxResults(max);

		hcu.orderBy(OrgGroup.NAME);

		return makeArray(hcu.list());
	}

	public static BClientCompany[] list(final int max) throws ArahantException {
		final HibernateCriteriaUtil<ClientCompany> hcu = ArahantSession.getHSU()
				.createCriteria(ClientCompany.class)
				.joinTo(ClientCompany.CLIENT_STATUS)
				.orderBy(ClientStatus.SEQ).setMaxResults(max);
		return makeArray(hcu.list());
	}

	public static BClientCompany[] listActiveClients(final int max) throws ArahantException {
		final HibernateCriteriaUtil<ClientCompany> hcu = ArahantSession.getHSU()
				.createCriteria(ClientCompany.class)
				.gtOrEq(ClientCompany.INACTIVEDATE, DateUtils.today(), 0)
				.joinTo(ClientCompany.CLIENT_STATUS)
				.orderBy(ClientStatus.SEQ).setMaxResults(max);
		return makeArray(hcu.list());
	}

	public static BClientCompany[] listInactiveClients(final int max) throws ArahantException {
		final HibernateCriteriaUtil<ClientCompany> hcu = ArahantSession.getHSU()
				.createCriteria(ClientCompany.class)
				.ltAndNeq(ClientCompany.INACTIVEDATE, DateUtils.today(), 0)
				.joinTo(ClientCompany.CLIENT_STATUS)
				.orderBy(ClientStatus.SEQ).setMaxResults(max);
		return makeArray(hcu.list());
	}

	/**
	 * @param hsu
	 * @return
	 * @throws ArahantException
	 */
	public static String getReport(final HibernateSessionUtil hsu, final boolean addr, final boolean billRate, final boolean phone, final boolean contractDate, final boolean identifier, final boolean contactName, final int sortType, final boolean sortAsc) throws ArahantException {

		final HibernateCriteriaUtil<ClientCompany> hcu = hsu.createCriteria(ClientCompany.class);

		//0=idenftifer, 1=name, 2=contract
		if (sortAsc)
			switch (sortType) {
				case 0:
					hcu.orderBy(OrgGroup.EXTERNAL_REF);
					break;
				case 1:
					hcu.orderBy(OrgGroup.NAME);
					break;
				case 2:
					hcu.orderBy(ClientCompany.CONTRACTDATE);
					break;
			}
		else
			switch (sortType) {
				case 0:
					hcu.orderByDesc(OrgGroup.EXTERNAL_REF);
					break;
				case 1:
					hcu.orderByDesc(OrgGroup.NAME);
					break;
				case 2:
					hcu.orderByDesc(ClientCompany.CONTRACTDATE);
					break;
			}


		final BClientCompany[] c = makeArray(hcu.list());

		final ClientReport cr = new ClientReport();

		return cr.getReport(c, addr, billRate, phone, contractDate, identifier, contactName, sortType, sortAsc);
	}

	/**
	 * @param hsu
	 * @param name
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public static BClientCompany[] searchClientCompanies(final HibernateSessionUtil hsu, final String name, final int cap) throws ArahantException {

		final List<ClientCompany> ccList = hsu.createCriteria(ClientCompany.class).like(OrgGroup.NAME, name).orderBy(OrgGroup.NAME).setMaxResults(cap).list();

		return makeArray(ccList);
	}
}
