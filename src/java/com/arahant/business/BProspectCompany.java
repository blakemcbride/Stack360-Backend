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
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.lisp.ABCL;
import com.arahant.utils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class BProspectCompany extends BCompanyBase {

	private ProspectCompany prospectCompany;
	private static final ArahantLogger logger = new ArahantLogger(BProspectCompany.class);

	public BProspectCompany() {
	}

	public BProspectCompany(String key) {
		internalLoad(key);
		company_base = prospectCompany;
	}

	public BProspectCompany(ProspectCompany company) {
		prospectCompany = company;
		initMembers(prospectCompany);
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProspectCompany(id).delete();
	}

	public void copyFromAndDelete(String prospectId) {

		List<ProspectLog> pl = ArahantSession.getHSU().createCriteria(ProspectLog.class).joinTo(ProspectLog.ORG_GROUP).eq(OrgGroup.ORGGROUPID, prospectId).list();

		for (ProspectLog l : pl) {
			l.setOrgGroup(prospectCompany);
			ArahantSession.getHSU().saveOrUpdate(l);
		}

		for (ProspectContact c : ArahantSession.getHSU().createCriteria(ProspectContact.class).ne(ProspectContact.LNAME, getMainContactLname()).ne(ProspectContact.FNAME, getMainContactFname()).joinTo(ProspectContact.COMPANYBASE).eq(OrgGroup.ORGGROUPID, prospectId).list()) {
			c.setCompanyBase(prospectCompany);
			ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, c).delete();
			BProspectContact bpc = new BProspectContact(c);
			bpc.assignToOrgGroup(prospectCompany.getOrgGroupId(), true);
		}

		for (CompanyQuestionDetail cq : ArahantSession.getHSU().createCriteria(CompanyQuestionDetail.class).joinTo(CompanyQuestionDetail.COMPANY).eq(OrgGroup.ORGGROUPID, prospectId).list()) {
			cq.setCompany(company_base);
			ArahantSession.getHSU().saveOrUpdate(cq);
		}

		BProspectCompany oldCompany = new BProspectCompany(prospectId);
		oldCompany.delete();
	}

	public int getCertainty() {
		return prospectCompany.getCertainty();
	}

	public int getFirstContactDate() {
		return prospectCompany.getFirstContactDate();
	}
	
	public double getOpportunityValue() {
		return prospectCompany.getOpportunityValue();
	}
	
	public void setOpportunityValue(double val) {
		prospectCompany.setOpportunityValue(val);
	}

	public int getLastLogDate() {
		/*
		 * ProspectLog pl=getLastLog();
		 *
		 * if (pl==null) return 0;
		 *
		 * return pl.getContactDate();
		 */
		return prospectCompany.getLastLogDate();
		/*
		 * int max=0;
		 *
		 * logger.info("Logs="+prospectCompany.getProspectLogs().size()); for
		 * (ProspectLog pl : prospectCompany.getProspectLogs()) {
		 * logger.info("Log date="+pl.getContactDate()); if
		 * (pl.getContactDate()>max) max=pl.getContactDate(); } return max;
		 */
	}

	public int getLastLogTime() {

		return prospectCompany.getLastLogTime();
		/*
		 * ProspectLog pl=getLastLog();
		 *
		 * if (pl==null) return 0;
		 *
		 * return pl.getContactTime(); /* int max=0; int time=-1; for
		 * (ProspectLog pl : prospectCompany.getProspectLogs()) if
		 * (pl.getContactDate()>max) { max=pl.getContactDate();
		 * time=pl.getContactTime(); } return time;
		 */
	}

	public BProspectSource getSource() {
		return new BProspectSource(prospectCompany.getProspectSource());
	}

	public String getSourceCode() {
		return prospectCompany.getProspectSource().getSourceCode();
	}

	public String getSourceDetail() {
		return prospectCompany.getSourceDetail();
	}
	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		prospectCompany = ArahantSession.getHSU().get(ProspectCompany.class, key);
		if (prospectCompany == null)
			throw new ArahantException("Failed to load prospect with key " + key);
		company_base = prospectCompany;
		initMembers(prospectCompany);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			super.delete();
			ArahantSession.getHSU().delete(prospectCompany);
		} catch (final Exception e) {
			throw new ArahantDeleteException();
		}
	}

	public void setMainContactProspectType(int mainContactType) {
		if (mainContactType != 0 && checkMainContact(mainContactType + ""))
			(new BProspectContact(getMainContact().getPersonId())).setProspectType(mainContactType);
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(prospectCompany);
	}

	@Override
	public String create() throws ArahantException {
		prospectCompany = new ProspectCompany();
		company_base = prospectCompany;
		super.create();
		prospectCompany.setOrgGroupId(getOrgGroupId());
		prospectCompany.setOrgGroupType(PROSPECT_TYPE);
		prospectCompany.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
		return getOrgGroupId();
	}

	@Override
	public void insert() throws ArahantException {
		company_base.setOwningCompany(null);
		prospectCompany.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		ArahantSession.getHSU().insert(prospectCompany);
		super.insert();
	}

	@Override
	public String getOrgType() {
		return "Prospect";
	}

	@Override
	public int getOrgGroupType() {
		return PROSPECT_TYPE;
	}

	@Override
	boolean checkMainContact(final String val) throws ArahantException {
		if (getMainContact() == null) {
			if (isEmpty(val))
				return false;

			final BProspectContact bpc = new BProspectContact();
			bpc.create();
			setMainContact(bpc);

		}
		return true;
	}

	public String getOrgGroupTypeName() {
		return "Prospect";
	}

	public BProspectStatus getStatus() {
		return new BProspectStatus(prospectCompany.getProspectStatus());
	}

	public String getStatusCode() {
		if (prospectCompany.getProspectStatus() == null)
			return "";
		else
			return prospectCompany.getProspectStatus().getCode();
	}

	public String getTypeCode() {
		if (prospectCompany.getProspectType() == null)
			return "";
		else
			return prospectCompany.getProspectType().getTypeCode();
	}

	public void setCertainty(short certainty) {
		prospectCompany.setCertainty(certainty);
	}

	public void setFirstContactDate(int firstContactDate) {
		prospectCompany.setFirstContactDate(firstContactDate);
	}

	public void setSourceDetail(String sourceDetail) {
		prospectCompany.setSourceDetail(sourceDetail);
	}

	public ProspectType getProspectType() {
		return prospectCompany.getProspectType();
	}

	public void setProspectType(ProspectType prospectType) {
		prospectCompany.setProspectType(prospectType);
	}

	public String getProspectTypeId() {
		return prospectCompany.getProspectTypeId();
	}

	public void setProspectTypeId(String prospectTypeId) {
		prospectCompany.setProspectType(ArahantSession.getHSU().get(ProspectType.class, prospectTypeId));
	}

	public void setSourceId(String sourceId) {
		prospectCompany.setProspectSource(ArahantSession.getHSU().get(ProspectSource.class, sourceId));
	}

	public void setStatusId(String statusId) {
		if (prospectCompany.getProspectStatusId() == null || !prospectCompany.getProspectStatusId().equals(statusId))
			prospectCompany.setStatusChangeDate(new Date());
		prospectCompany.setProspectStatus(ArahantSession.getHSU().get(ProspectStatus.class, statusId));
	}

	public void setSalesPersonId(String salesPersonId) {
		prospectCompany.setSalesPerson(ArahantSession.getHSU().get(Employee.class, salesPersonId));
	}

	private static BProspectCompany[] makeArray(HibernateScrollUtil<ProspectCompany> scr) {
		List<ProspectCompany> l = new ArrayList<ProspectCompany>();
		while (scr.next()) {
			ProspectCompany pc = scr.get();
			if (!l.contains(pc))
				l.add(pc);
		}

		return BProspectCompany.makeArray(l);
	}

	public static BProspectCompany[] makeArray(List<ProspectCompany> l) {
		BProspectCompany[] ret = new BProspectCompany[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectCompany(l.get(loop));

		return ret;
	}

	public static HibernateCriteriaUtil<ProspectCompany> addSalesPersonFilter(HibernateCriteriaUtil<ProspectCompany> hcu) {
		if (ArahantSession.getHSU().currentlySuperUser())
			return hcu;
		if (!BPerson.getCurrent().isEmployee())
			throw new ArahantWarning("Non-employees may not view prospects.");
		BEmployee bemp = new BEmployee(BPerson.getCurrent());
		if (bemp.isSupervisor())
			return hcu;
		//otherwise, can only see prospects that apply to them
		hcu.eq(ProspectCompany.SALESPERSON, bemp.employee);

		return hcu;
	}

	public BEmployee getSalesPerson() {
		return new BEmployee(prospectCompany.getSalesPerson().getPersonId());
	}

	public Date getRecordChangeDate() {
		return prospectCompany.getRecordChangeDate();
	}

	public Date getWhenAdded() {
		return prospectCompany.getWhenAdded();
	}

	public void setWhenAdded(Date AddedDate) {
		prospectCompany.setWhenAdded(AddedDate);
	}

	public int getNumberOfEmployees() {
		return prospectCompany.getNumberOfEmployees();
	}

	public void setNumberOfEmployees(int n) {
		prospectCompany.setNumberOfEmployees(n);
	}

	public int getGrossIncome() {
		return prospectCompany.getGrossIncome();
	}

	public void setGrossIncome(int inc) {
		prospectCompany.setGrossIncome(inc);
	}

	public String getWebsite() {
		return prospectCompany.getWebsite();
	}

	public void setWebsite(String url) {
		prospectCompany.setWebsite(url);
	}

	///////////////////////////////////////////////////////////////////////////////
	/////// TODO REMOVE THESE ONCE EVERYONE IS CONVERTED TO PAGING & SORTING METHOD
	public static BProspectCompany[] search(String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, int cap, boolean hasPhone) {
		HibernateCriteriaUtil<ProspectCompany> hcu = ArahantSession.getHSU().createCriteria(ProspectCompany.class).setMaxResults(cap);

		if (!isEmpty(excludeId))
			hcu.ne(ProspectCompany.ORGGROUPID, excludeId);

		hcu.orderBy(ProspectCompany.NAME);

		hcu.like(ProspectCompany.EXTERNAL_REF, identifier).like(ProspectCompany.NAME, name);

		if (hasPhone)
			hcu.joinTo(ProspectCompany.PHONES).isNotEmpty(Phone.PHONENUMBER);

		if ((!isEmpty(fname) && !"%".equals(fname.trim())) || (!isEmpty(lname) && !"%".equals(lname.trim()))) {

			final HibernateCriteriaUtil joinOrgAssoc = hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
			final HibernateCriteriaUtil joinPerson = joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);
			joinPerson.like(Person.FNAME, fname);
			joinPerson.like(Person.LNAME, lname);
		}


		if (!isEmpty(statusId))
			hcu.eq(ProspectCompany.PROSPECT_STATUS, ArahantSession.getHSU().get(ProspectStatus.class, statusId));

		if (!isEmpty(sourceId))
			hcu.joinTo(ProspectCompany.PROSPECT_SOURCE).eq(ProspectSource.PROSPECT_SOURCE_ID, sourceId);

		addSalesPersonFilter(hcu);

		return makeArray(hcu.list());
	}

	public static BProspectCompany[] search(String name, String identifier, int cap) {
		HibernateScrollUtil<ProspectCompany> scr = addSalesPersonFilter(ArahantSession.getHSU().createCriteria(ProspectCompany.class).setMaxResults(cap).like(ProspectCompany.NAME, name).like(ProspectCompany.EXTERNAL_REF, identifier).orderBy(ProspectCompany.NAME)).scroll();

		List<ProspectCompany> prospectList = new ArrayList<ProspectCompany>();
		while (scr.next())
			prospectList.add(scr.get());

		return makeArray(prospectList);
	}
	/////// TODO REMOVE THESE ONCE EVERYONE IS CONVERTED TO PAGING & SORTING METHOD
	///////////////////////////////////////////////////////////////////////////////

	public static BSearchOutput<BProspectCompany> searchBySalesperson(BSearchMetaInput bSearchMetaInput, String personId, String[] sourceIds, String[] statusIds, int fromDate, int toDate, boolean includeInactiveStatuses, String name) {
		BSearchOutput<BProspectCompany> bSearchOutput = new BSearchOutput<BProspectCompany>(bSearchMetaInput);
		HibernateCriteriaUtil<ProspectCompany> hcu = searchBySalesperson(personId, sourceIds, statusIds, fromDate, toDate, includeInactiveStatuses, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true, name);

		HibernateScrollUtil<ProspectCompany> prospectCompanies;
		if (bSearchMetaInput.isUsingPaging())
			prospectCompanies = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			prospectCompanies = hcu.scroll();

		// set output

		bSearchOutput.setItems(makeArray(prospectCompanies));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() >= 2 && bSearchMetaInput.getSortType() <= 7) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = searchBySalesperson(personId, sourceIds, statusIds, fromDate, toDate, includeInactiveStatuses, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false, name);

			int totalRecords = hcu.count(ProspectCompany.NAME);

			// TODO - THIS IS A PROBLEM - WE NEED A WAY TO DETERMINE WHAT THE ACTUAL START INDEX IS
			// this is the record to start from if using paging, but note that it may be asking for an
			// index that is now out of bounds in which case it must back up to earlier page boundary 
			// (e.g. 50 items per page, was initially 102 items -> asking for first index of 100, but
			// now only 98 items o should return first index of 50)
			bSearchOutput.setPagingFirstItemIndex(bSearchMetaInput.getPagingFirstItemIndex());
			bSearchOutput.setTotalItemsPaging(totalRecords);
			bSearchOutput.setItemsPerPage(bSearchMetaInput.getItemsPerPage());
		}


		return bSearchOutput;
	}

	private static HibernateCriteriaUtil<ProspectCompany> searchBySalesperson(String personId, String[] sourceIds, String[] statusIds, int fromDate,
			int toDate, boolean inactiveStatuses, int sortType, boolean sortAsc, boolean includeSorting, String name) {
		HibernateCriteriaUtil<ProspectCompany> hcu = ArahantSession.getHSU().createCriteria(ProspectCompany.class);
		hcu.like(ProspectCompany.NAME, name);
		hcu.ge(ProspectCompany.DATE, fromDate);
		if (toDate > 0)
			hcu.le(ProspectCompany.DATE, toDate);

		HibernateCriteriaUtil statHCU = hcu.joinTo(ProspectCompany.PROSPECT_STATUS);

		if (statusIds.length > 0)
			statHCU.in(ProspectStatus.PROSPECT_STATUS_ID, statusIds);
		else if (inactiveStatuses == false)
			statHCU.eq(ProspectStatus.ACTIVE, 'Y');

		HibernateCriteriaUtil sourceHCU = hcu.joinTo(ProspectCompany.PROSPECT_SOURCE);
		if (sourceIds.length > 0)
			sourceHCU.in(ProspectSource.PROSPECT_SOURCE_ID, sourceIds);

		if (!isEmpty(personId))
			hcu.joinTo(ProspectCompany.SALESPERSON).eq(Employee.PERSONID, personId);

		HibernateCriteriaUtil joinOrgAssoc;
		HibernateCriteriaUtil joinPerson;

		if (includeSorting)
			switch (sortType) {
				default: // name
					if (sortAsc)
						hcu.orderBy(ProspectCompany.NAME);
					else
						hcu.orderByDesc(ProspectCompany.NAME);
					break;
				case 2: // status code
					/*
					 * Blake said sort it by sequence if (sortAsc) {
					 * statHCU.orderBy(ProspectStatus.CODE); } else {
					 * statHCU.orderByDesc(ProspectStatus.CODE);
					}
					 */
					if (sortAsc)
						statHCU.orderBy(ProspectStatus.SEQ);
					else
						statHCU.orderByDesc(ProspectStatus.SEQ);
					break;
				case 3: // source code
					if (sortAsc)
						sourceHCU.orderBy(ProspectSource.SOURCE_CODE);
					else
						sourceHCU.orderByDesc(ProspectSource.SOURCE_CODE);
					break;
				case 4: // last prospect log
					if (sortAsc)
						hcu.orderBy(ProspectCompany.LAST_LOG_DATE).orderBy(ProspectCompany.LAST_LOG_TIME); //	hcu.leftJoinTo(ProspectCompany.PROSPECT_LOG).orderBy(ProspectLog.DATE).orderBy(ProspectLog.TIME);
					else
						hcu.orderByDesc(ProspectCompany.LAST_LOG_DATE).orderByDesc(ProspectCompany.LAST_LOG_TIME); //	hcu.leftJoinTo(ProspectCompany.PROSPECT_LOG).orderByDesc(ProspectLog.DATE).orderByDesc(ProspectLog.TIME);
					break;
				case 5: // first contact date
					if (sortAsc)
						hcu.orderBy(ProspectCompany.DATE);
					else
						hcu.orderByDesc(ProspectCompany.DATE);
					break;
				case 6: // primary contact last name
					joinOrgAssoc = hcu.leftJoinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');
					joinPerson = joinOrgAssoc.leftJoinTo(OrgGroupAssociation.PERSON);

					if (sortAsc)
						joinPerson.orderBy(Person.LNAME);
					else
						joinPerson.orderByDesc(Person.LNAME);

					break;
				case 7: // primary contact first name
					joinOrgAssoc = hcu.leftJoinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');
					joinPerson = joinOrgAssoc.leftJoinTo(OrgGroupAssociation.PERSON);

					if (sortAsc)
						joinPerson.orderBy(Person.FNAME);
					else
						joinPerson.orderByDesc(Person.FNAME);

					break;
			}

		return hcu;

	}

	public static BSearchOutput<BProspectCompany> search(BSearchMetaInput bSearchMetaInput, String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone) {
		return search(bSearchMetaInput, identifier, fname, lname, name, statusId, sourceId, "", excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, BProperty.getInt(StandardProperty.SEARCH_MAX));
	}

	public static BSearchOutput<BProspectCompany> search(BSearchMetaInput bSearchMetaInput, String identifier, String fname, String lname, String name, String statusId, String sourceId, String typeId, String excludeId, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone, int max) {
		if (bSearchMetaInput.getSortType() == 0) {
			bSearchMetaInput.setSortType(7); //set to status default descending
			bSearchMetaInput.setSortAsc(false);
		}

		BSearchOutput<BProspectCompany> bSearchOutput = new BSearchOutput<BProspectCompany>(bSearchMetaInput);
		HibernateCriteriaUtil<ProspectCompany> hcu = search(bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), identifier, fname, lname, name, statusId, sourceId, typeId, excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, true);

		if (max > 0)
			hcu.setMaxResults(max);
		HibernateScrollUtil<ProspectCompany> prospectCompanies;
		if (bSearchMetaInput.isUsingPaging())
			prospectCompanies = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			prospectCompanies = hcu.scroll();

		// set output

		bSearchOutput.setItems(makeArray(prospectCompanies));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() >= 2 && bSearchMetaInput.getSortType() <= 12) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = search(bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), identifier, fname, lname, name, statusId, sourceId, typeId, excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, false);

			int totalRecords = hcu.count(ProspectCompany.NAME);

			// TODO - THIS IS A PROBLEM - WE NEED A WAY TO DETERMINE WHAT THE ACTUAL START INDEX IS
			// this is the record to start from if using paging, but note that it may be asking for an
			// index that is now out of bounds in which case it must back up to earlier page boundary 
			// (e.g. 50 items per page, was initially 102 items -> asking for first index of 100, but
			// now only 98 items o should return first index of 50)
			bSearchOutput.setPagingFirstItemIndex(bSearchMetaInput.getPagingFirstItemIndex());
			bSearchOutput.setTotalItemsPaging(totalRecords);
			bSearchOutput.setItemsPerPage(bSearchMetaInput.getItemsPerPage());
		}

		return bSearchOutput;
	}

	public static HibernateCriteriaUtil<ProspectCompany> search(int sortType, boolean sortAsc, String identifier, String fname, String lname, String name, String statusId, String sourceId, String excludeId, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone, boolean includeSorting) {
		return search(sortType, sortAsc, identifier, fname, lname, name, statusId, sourceId, "", excludeId, hasPhone, hasEmail, salesPersonId, firstContactDateAfter, firstContactDateBefore, lastContactDateAfter, lastContactDateBefore, lastLogDateAfter, lastLogDateBefore, statusDateAfter, statusDateBefore, activesOnly, timeZone, includeSorting);
	}

	public static HibernateCriteriaUtil<ProspectCompany> search(int sortType, boolean sortAsc, String identifier, String fname, String lname, String name, String statusId, String sourceId, String typeId, String excludeId, boolean hasPhone, boolean hasEmail, String salesPersonId, int firstContactDateAfter, int firstContactDateBefore, int lastContactDateAfter, int lastContactDateBefore, int lastLogDateAfter, int lastLogDateBefore, int statusDateAfter, int statusDateBefore, boolean activesOnly, short timeZone, boolean includeSorting) {
		boolean didSort = false;

		HibernateCriteriaUtil<ProspectCompany> hcu = ArahantSession.getHSU().createCriteria(ProspectCompany.class);

		if (firstContactDateAfter != 0 || firstContactDateBefore != 0)
			hcu.dateBetween(ProspectCompany.DATE, firstContactDateAfter, firstContactDateBefore);
		if (lastContactDateAfter != 0 || lastContactDateBefore != 0)
			hcu.dateBetween(ProspectCompany.LAST_CONTACT_DATE, lastContactDateAfter, lastContactDateBefore);
		if (lastLogDateAfter != 0 || lastContactDateBefore != 0)
			hcu.dateBetween(ProspectCompany.LAST_LOG_DATE, lastLogDateAfter, lastContactDateBefore);
		if (statusDateAfter != 0 || statusDateBefore != 0)
			hcu.dateBetween(ProspectCompany.PROSPECT_STATUS_DATE, DateUtils.getDate(statusDateAfter), DateUtils.getDate(statusDateBefore));

		if (!isEmpty(excludeId))
			hcu.ne(ProspectCompany.ORGGROUPID, excludeId);
		if (timeZone > -11 && timeZone < -4)
			hcu.joinTo(ProspectCompany.ADDRESSES).eq(Address.TIMEZONE_OFFSET, timeZone); //Act like we did the sort.  We can only search by 1 time zone, so no reason to sort by Time Zones too... causes some crazy error

		if (!isEmpty(salesPersonId))
			hcu.eq(ProspectCompany.SALESPERSON, new BEmployee(salesPersonId).getEmployee());

		hcu.like(ProspectCompany.EXTERNAL_REF, identifier).like(ProspectCompany.NAME, name);

		if (hasPhone)
			hcu.joinTo(ProspectCompany.PHONES).isNotEmpty(Phone.PHONENUMBER);

		if (hasEmail)
			hcu.joinTo(ProspectCompany.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.PERSON).isNotEmpty(Person.PERSONALEMAIL);

		//final
		HibernateCriteriaUtil joinOrgAssoc; //=hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
		//final
		HibernateCriteriaUtil joinPerson; //=joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);

		if ((!isEmpty(fname) && !"%".equals(fname.trim())) || (!isEmpty(lname) && !"%".equals(lname.trim()))) {

			joinOrgAssoc = hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
			joinPerson = joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);
			joinPerson.like(Person.FNAME, fname);
			joinPerson.like(Person.LNAME, lname);
		}

		if (!isEmpty(statusId))
			hcu.eq(ProspectCompany.PROSPECT_STATUS, ArahantSession.getHSU().get(ProspectStatus.class, statusId));

		if (!isEmpty(typeId))
			hcu.eq(ProspectCompany.PROSPECT_TYPE, ArahantSession.getHSU().get(ProspectType.class, typeId));

		if (activesOnly) {
			List<ProspectStatus> statuses = ArahantSession.getHSU().createCriteria(ProspectStatus.class).eq(ProspectStatus.ACTIVE, 'Y').list();
			hcu.in(ProspectCompany.PROSPECT_STATUS, statuses);
		}

		if (!isEmpty(sourceId))
			hcu.joinTo(ProspectCompany.PROSPECT_SOURCE).eq(ProspectSource.PROSPECT_SOURCE_ID, sourceId);

		addSalesPersonFilter(hcu);

		if (includeSorting)
			switch (sortType) {
				// new String[]{"name", "identifier", "firstContactDate", "lastContactDate", "lastLogDate", "nextContactDate", "status", "statusDate", "source", "salesPerson", "certainty", "timeZone"});

				default: //status
					if (!didSort)
						hcu.joinTo(ProspectCompany.PROSPECT_STATUS).orderBy(ProspectStatus.SEQ, sortAsc);
					break;
				case 1: // name
					if (!didSort)
						hcu.orderBy(ProspectCompany.NAME, sortAsc);
					break;
				case 2: // identifier
					if (!didSort)
						hcu.orderBy(ProspectCompany.EXTERNAL_REF, sortAsc);
					break;
				case 3: // firstContactDate
					if (!didSort)
						hcu.orderBy(ProspectCompany.DATE, sortAsc);
					break;
				case 4: // lastContactDate
					if (!didSort)
						hcu.orderBy(ProspectCompany.LAST_CONTACT_DATE, sortAsc);
					break;
				case 5: // lastLogDate
					if (!didSort)
						hcu.orderBy(ProspectCompany.LAST_LOG_DATE, sortAsc);
					break;
				case 6: // nextContactDate
					if (!didSort)
						hcu.orderBy(ProspectCompany.NEXT_CONTACT_DATE, sortAsc);
					break;
				case 7: // status
					if (!didSort)
						hcu.joinTo(ProspectCompany.PROSPECT_STATUS).orderBy(ProspectStatus.SEQ, sortAsc);
					break;
				case 8: // statusDate
					if (!didSort)
						hcu.orderBy(ProspectCompany.PROSPECT_STATUS_DATE, sortAsc);
					break;
				case 9: // source
					if (!didSort)
						hcu.orderBy(ProspectCompany.PROSPECT_SOURCE, sortAsc);
					break;
//				case 10: // primary contact last name
//					if(!ogaJoin)
//					{
//						joinOrgAssoc = hcu.leftJoinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');
//						joinPerson = joinOrgAssoc.leftJoinTo(OrgGroupAssociation.PERSON);
//
//						if (sortAsc) {
//							joinPerson.orderBy(Person.LNAME);
//						} else {
//							joinPerson.orderByDesc(Person.LNAME);
//						}
//					}
//
//					break;
//				case 11: // primary contact first name
//					if(!ogaJoin)
//					{
//						joinOrgAssoc = hcu.leftJoinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');
//						joinPerson = joinOrgAssoc.leftJoinTo(OrgGroupAssociation.PERSON);
//
//						if (sortAsc) {
//							joinPerson.orderBy(Person.FNAME);
//						} else {
//							joinPerson.orderByDesc(Person.FNAME);
//						}
//					}
//
//					break;
				case 10: //salesPerson
					if (!didSort)
						hcu.joinTo(ProspectCompany.SALESPERSON).orderBy(Person.LNAME, sortAsc).orderBy(Person.FNAME, sortAsc);
					break;
				case 11: //certainty
					if (!didSort)
						hcu.orderBy(ProspectCompany.CERTAINTY, sortAsc);
					break;
				case 12: //timeZone
					if (!didSort && timeZone == 100)
						hcu.joinTo(ProspectCompany.ADDRESSES).orderBy(Address.TIMEZONE_OFFSET, sortAsc);
					break;
				case 13:  //  Type
					break;
				case 14: // When Added
					break;
				case 15: //  Number of employees
					break;
				case 16: //  Gross Revenue
					break;
				case 17: //  Opportunity Value
					break;
				case 18: //  Weighted Value
					break;
				case 19: //  State
					break;
			}
		return hcu;
	}

	public static BProspectCompany[] searchProspects(String name, String employeeId, String[] excludeIds, int max) {
		HibernateCriteriaUtil<ProspectCompany> hcu = ArahantSession.getHSU().createCriteria(ProspectCompany.class).orderBy(ProspectCompany.NAME).setMaxResults(max);

		if (!isEmpty(name))
			hcu.like(ProspectCompany.NAME, name);

		if (!isEmpty(employeeId))
			hcu.eq(ProspectCompany.SALESPERSON, new BEmployee(employeeId).getEmployee());

		hcu.notIn(ProspectCompany.COMPANY_ID, excludeIds);

		return makeArray(hcu.list());
	}

	public static BSearchOutput<BEmployee> searchSalesPeople(BSearchMetaInput bSearchMetaInput, String firstName, String lastName) {

		HibernateCriteriaUtil<Employee> hcu = searchSalesPeople(firstName, lastName, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true);

		/*
		 * BSearchOutput<BEmployee> bSearchOutput = new
		 * BSearchOutput<BEmployee>(bSearchMetaInput);
		 *
		 * List<Employee> employees=hcu.getPage(bSearchMetaInput);
		 *
		 * // set output
		 * bSearchOutput.setItems(BEmployee.makeArrayEx(employees));
		 *
		 *
		 * if (bSearchMetaInput.isUsingPaging()) { hcu =
		 * searchSalesPeople(firstName, lastName,
		 * bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false);
		 *
		 * // TODO - THIS IS A PROBLEM - WE NEED A WAY TO DETERMINE WHAT THE
		 * ACTUAL START INDEX IS // this is the record to start from if using
		 * paging, but note that it may be asking for an // index that is now
		 * out of bounds in which case it must back up to earlier page boundary
		 * // (e.g. 50 items per page, was initially 102 items -> asking for
		 * first index of 100, but // now only 98 items o should return first
		 * index of 50)
		 *
		 * bSearchOutput.setTotalItemsPaging(hcu.count(Employee.LNAME));
		 *
		 * }
		 *
		 * return bSearchOutput;\
		 */
		return makeSearchOutput(bSearchMetaInput, hcu);
	}

	public static BSearchOutput<BEmployee> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Employee> hcu) {
		BSearchOutput<BEmployee> ret = new BSearchOutput<BEmployee>(searchMeta);

		HibernateScrollUtil<Employee> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BEmployee.makeArray(scr));

		return ret;
	}

	public static BSearchOutput<BProspectCompany> makeSearchOutputP(BSearchMetaInput searchMeta, HibernateCriteriaUtil<ProspectCompany> hcu) {
		BSearchOutput<BProspectCompany> ret = new BSearchOutput<BProspectCompany>(searchMeta);

		HibernateScrollUtil<ProspectCompany> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(makeArray(scr));

		return ret;
	}

	private static HibernateCriteriaUtil<Employee> searchSalesPeople(String firstName, String lastName, int sortType, boolean sortAsc, boolean includeSorting) {
		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).like(Employee.FNAME, firstName).like(Employee.LNAME, lastName);

		if (ArahantSession.getHSU().currentlyArahantUser())
			// limit to those with prospects
			hcu.sizeNe(Employee.PROSPECTS, 0);
		else {
			// limit to those with prospects or the current user
			final HibernateCriterionUtil cri1 = hcu.makeCriteria();
			final HibernateCriterionUtil cri2 = hcu.makeCriteria();
			final HibernateCriterionUtil cri3 = hcu.makeCriteria();

			cri1.sizeNe(Employee.PROSPECTS, 0);
			cri2.eq(Employee.PERSONID, ArahantSession.getHSU().getCurrentPerson().getPersonId());
			cri3.or(cri1, cri2);
			cri3.add();
		}

		if (includeSorting)
			// establish sort
			switch (sortType) {
				default: // last name
					if (sortAsc)
						hcu.orderBy(Person.LNAME);
					else
						hcu.orderByDesc(Person.LNAME);
					break;
				case 2: // first name
					if (sortAsc)
						hcu.orderBy(Person.FNAME);
					else
						hcu.orderByDesc(Person.FNAME);
					break;
			}

		return hcu;
	}

	public Date getStatusChangeDate() {
		return prospectCompany.getStatusChangeDate();
	}

	public int getNextContactDate() {
		return prospectCompany.getNextContactDate();
	}

	public void setNextContactDate(int nextContactDate) {
		prospectCompany.setNextContactDate(nextContactDate);
	}

	public int getLastContactDate() {
		return prospectCompany.getLastContactDate();
	}

	public void setLastContactDate(int lastContactDate) {
		prospectCompany.setLastContactDate(lastContactDate);
	}

	public BProspectLog getLastLog() {
		return new BProspectLog(ArahantSession.getHSU().createCriteria(ProspectLog.class).orderByDesc(ProspectLog.CONTACT_DATE).eq(ProspectLog.ORG_GROUP, prospectCompany).notNull(ProspectLog.SALES_ACTIVITY_RESULT).first());
	}

	public static BSearchOutput<BProspectCompany> searchSalesQueue(final int cap, final String prospectName, final String prospectStatusId, final int contactDate, final int lastContactFrom, final int lastContactTo, final String activityId, final String resultId, final boolean active, BSearchMetaInput bSearchMetaInput, final String empId) {
		HibernateCriteriaUtil<ProspectCompany> hcu = ArahantSession.getHSU().createCriteria(ProspectCompany.class).setMaxResults(cap);

		if (!isEmpty(empId))
			hcu.eq(ProspectCompany.SALESPERSON, new BPerson(empId).getPerson());

		HibernateCriteriaUtil plHcu = hcu;
		HibernateCriteriaUtil activityHcu = null;
		HibernateCriteriaUtil resultHcu = null;
		HibernateCriteriaUtil statusHcu = null;

		if (active) {

			if (!isEmpty(activityId) && !isEmpty(resultId)) {
				HibernateCriteriaUtil plHcu2 = plHcu.joinTo(ProspectCompany.PROSPECT_LOG, "prospectLog");
				activityHcu = plHcu2.joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY, "salesActivity").eq("salesActivity." + SalesActivity.ID, activityId);
				resultHcu = plHcu2.joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY_RESULT, "salesActivityResult").eq("salesActivityResult." + SalesActivityResult.SALES_ACTIVITY_RESULT_ID, resultId);
			} else if (!isEmpty(resultId))
				resultHcu = plHcu.joinTo(ProspectCompany.PROSPECT_LOG, "prospectLog").joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY_RESULT, "salesActivityResult").eq("salesActivityResult." + SalesActivityResult.SALES_ACTIVITY_RESULT_ID, resultId);
			else if (!isEmpty(activityId))
				activityHcu = plHcu.joinTo(ProspectCompany.PROSPECT_LOG, "prospectLog").joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY, "salesActivity").eq("salesActivity." + SalesActivity.ID, activityId);


			hcu.dateBetween(ProspectCompany.LAST_LOG_DATE_WITH_RESULT, lastContactFrom, lastContactTo);

			if (contactDate != 0)
				hcu.eq(ProspectCompany.NEXT_CONTACT_DATE, contactDate);
		} else
			hcu.notIn(ProspectCompany.ORGGROUPID, ArahantSession.getHSU().createCriteria(ProspectCompany.class).selectFields(ProspectCompany.ORGGROUPID).joinTo(ProspectCompany.PROSPECT_LOG).notNull(ProspectLog.SALES_ACTIVITY_RESULT).list());

		hcu.like(ProspectCompany.NAME, prospectName);

		if (!isEmpty(prospectStatusId))
			statusHcu = hcu.joinTo(ProspectCompany.PROSPECT_STATUS).eq(ProspectStatus.PROSPECT_STATUS_ID, prospectStatusId);

		//"prospectName", "prospectStatus","lastContactDate","activity","result","scheduledContact", "addedDate"});

		switch (bSearchMetaInput.getSortType()) {
			case 1:
				hcu.orderBy(ProspectCompany.NAME, bSearchMetaInput.isSortAsc());
				break;
			case 2:
				if (statusHcu != null  &&  !isEmpty(prospectStatusId))
					statusHcu.orderBy(ProspectStatus.CODE, bSearchMetaInput.isSortAsc());
				else
					hcu.joinTo(ProspectCompany.PROSPECT_STATUS).orderBy(ProspectStatus.CODE, bSearchMetaInput.isSortAsc());
				break;
			case 3:
				hcu.orderBy(ProspectCompany.LAST_LOG_DATE_WITH_RESULT, bSearchMetaInput.isSortAsc());
				break;
			case 4:
				if (activityHcu != null  &&  !isEmpty(activityId))
					activityHcu.orderBy("salesActivity." + SalesActivity.ACTIVITY_CODE, bSearchMetaInput.isSortAsc());
				else if (!isEmpty(resultId))
					plHcu.joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY).orderBy("salesActivity." + SalesActivity.ACTIVITY_CODE, bSearchMetaInput.isSortAsc());
				else
					plHcu.joinTo(ProspectCompany.PROSPECT_LOG).joinTo(ProspectLog.SALES_ACTIVITY).orderBy(SalesActivity.ACTIVITY_CODE, bSearchMetaInput.isSortAsc());
				break;
			case 5:
				if (resultHcu != null  &&  !isEmpty(resultId))
					resultHcu.orderBy("salesActivityResult." + SalesActivityResult.DESCRIPTION, bSearchMetaInput.isSortAsc());
				else if (!isEmpty(activityId))
					plHcu.joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY_RESULT, "salesActivityResult").orderBy("salesActivityResult." + SalesActivityResult.DESCRIPTION, bSearchMetaInput.isSortAsc());
				else
					plHcu.joinTo(ProspectCompany.PROSPECT_LOG, "prospectLog").joinTo("prospectLog." + ProspectLog.SALES_ACTIVITY_RESULT, "salesActivityResult").orderBy("salesActivityResult." + SalesActivityResult.DESCRIPTION, bSearchMetaInput.isSortAsc());
				break;
			case 6:
				hcu.orderBy(ProspectCompany.NEXT_CONTACT_DATE, bSearchMetaInput.isSortAsc());
				break;
			case 7:
				hcu.orderBy(ProspectCompany.ADDED_DATE, bSearchMetaInput.isSortAsc());
				break;
			default:
				if (active)
					hcu.orderBy(ProspectCompany.NEXT_CONTACT_DATE, bSearchMetaInput.isSortAsc());
				else
					hcu.orderBy(ProspectCompany.ADDED_DATE, bSearchMetaInput.isSortAsc());
		}

		return makeSearchOutputP(bSearchMetaInput, hcu);
	}

	public short getTimeZone() {
		return getAddress().getTimeZoneOffset();
	}
	
	/**
	 * If a prospect is erroneously converted to a client, this method converts them from
	 * a client back to a prospect.
	 * 
	 * @param id
	 * @return 
	 */
	private static BProspectCompany convertFromClient(String id) {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			if (hsu.get(OrgGroup.class, id) == null) //prevent any sql injection attacks
				throw new ArahantException("Bad ID passed in to convert from client " + id);
			
			String owningCompanyId = hsu.getCurrentCompany().getOrgGroupId();
			int today = DateUtils.today();

			//remove from any parent groups
			hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.CHILD_ID, id).delete();
			String statusId = hsu.createCriteria(ProspectStatus.class).eq(ProspectStatus.ACTIVE, 'Y')
					.eqOrNull(ProspectStatus.COMPANY_ID, owningCompanyId)
					.gtOrEq(ProspectStatus.LAST_ACTIVE_DATE, today, 0)
					.orderBy(ProspectStatus.SEQ).first().getProspectStatusId();
			String prospectTypeId = hsu.createCriteria(ProspectType.class)
					.eqOrNull(ProspectType.COMPANY_ID, owningCompanyId)
					.gtOrEq(ProspectType.LAST_ACTIVE_DATE, today, 0)
					.first().getProspectTypeId();
			String sourceId = hsu.createCriteria(ProspectSource.class)
					.eqOrNull(ProspectSource.COMPANY_ID, owningCompanyId)
					.gtOrEq(ProspectSource.LAST_ACTIVE_DATE, today, 0)
					.first().getProspectSourceId();
			
			// find first active employee
			String personId = null;
			HibernateScrollUtil<Employee> empscr = hsu.createCriteria(Employee.class)
					.eq(Employee.COMPANYBASE, hsu.getCurrentCompany())
					.scroll();
			while (empscr.next()) {
				BEmployee bemp = new BEmployee(empscr.get());
				HrEmployeeStatus stat = bemp.getCurrentStatus();
				if (stat != null  &&  stat.getActive() == 'Y') {
					personId = bemp.getPersonId();
					empscr.close();
					break;
				}
			}
			assert(personId != null);
	
			
			//TODO: someday find a way to change this to use hibernate 
			//create a client company record
			hsu.executeSQL("insert into prospect (org_group_id,company_id,prospect_status_id,person_id,prospect_type_id,record_person_id,prospect_source_id,first_contact_date,record_change_date, record_change_type, status_change_date) values ('" + id + "','" + owningCompanyId + "','" + statusId + "','" + personId + "','" + prospectTypeId + "','" + personId + "','" + sourceId + "', 0, '2006-06-09', 'N', '2006-06-09')");

			hsu.clear();//have to clear so it will be able to see the client

			//delete the prospect company record
			hsu.executeSQL("delete from client where org_group_id='" + id + "'");
			
			//change org group types in org groups
			hsu.executeSQL("update org_group set org_group_type=" + PROSPECT_TYPE + " where owning_entity_id='" + id + "'");
			hsu.executeSQL("update company_base set org_group_type=" + PROSPECT_TYPE + " where org_group_id='" + id + "'");
			//change org group types in contacts
			BOrgGroup borg = new BOrgGroup(id);
			Set<String> perIds = borg.getAllPersonIdsForOrgGroupHierarchy(false);
			for (Person p : hsu.createCriteria(Person.class).in(Person.PERSONID, perIds).list()) {
				p.setOrgGroupType(PROSPECT_TYPE);
				hsu.saveOrUpdate(p);
			}

			//create client contact records for all contacts
			hsu.executeSQL("insert into prospect_contact select person_id, 5 from person where person_id in (select oga.person_id from org_group_association oga "
					+ "join prospect cc on cc.org_group_id=oga.org_group_id) and person_id not in (select person_id from prospect_contact)");
			return new BProspectCompany(id);
		} catch (Exception e) {
			throw new ArahantException("Can not create new prospect.", e);
		}
	}
	
	public static void main(String [] args) {
		System.out.println("starting main");
		ABCL.init();
		convertFromClient("00001-0000000147");  // AES Beaver Valley
		//  ...
		ArahantSession.getHSU().commitTransaction();
		System.out.println("main complete");
	}

}
