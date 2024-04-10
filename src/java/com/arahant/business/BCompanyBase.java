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
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public abstract class BCompanyBase extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BCompanyBase.class);
	protected CompanyBase company_base;
	private Address address;
	private Phone mainPhone;
	private Phone mainFax;
	private BPerson mainContact;

	public BCompanyBase() {
	}

	public static List<CompanyBase> getAll() {
		return ArahantSession.getHSU().getAll(CompanyBase.class);
	}

	public CompanyBase getBean() {
		return company_base;
	}

	public String getCompanyId() {
		return getOrgGroupId();
	}

	public String getTypeFormatted() {
		return getOrgType();
	}

	public BOrgGroup[] searchOrgGroups(String name, int highCap) {
		return searchOrgGroups(name, highCap, false);
	}

	public BOrgGroup[] searchOrgGroups(String name, int highCap, boolean includeCompanyOrgGroup) {
		return searchOrgGroups(name, highCap, includeCompanyOrgGroup, new ArrayList<String>(0));
	}

	public BOrgGroup[] searchOrgGroups(String name, int highCap, boolean includeCompanyOrgGroup, List<String> excludeIds) {
		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(highCap).notIn(OrgGroup.ORGGROUPID, excludeIds).like(OrgGroup.NAME, name).eq(OrgGroup.OWNINGCOMPANY, company_base);

		if (!includeCompanyOrgGroup)
			hcu.ne(OrgGroup.ORGGROUPID, getOrgGroupId());

		return BOrgGroup.makeArray(hcu.list());
	}

	/**
	 * @throws ArahantException
	 *
	 */
	protected void initMembers(final CompanyBase comp) throws ArahantException {
		company_base = comp;
		//	address=getAddress();
		//	mainPhone=getPhoneByMainPhoneId();
		//	mainFax=getPhoneByMainFaxId();

		//	if (getMainContactForOrgGroup(company)!=null)
		//		mainContact=new BPerson(getMainContactForOrgGroup(company));


	}

	@Override
	public String create() throws ArahantException {
		company_base.setOrgGroupId(IDGenerator.generate(company_base));

		address = new Address();
		mainPhone = new Phone();
		mainFax = new Phone();


		address.setAddressId(IDGenerator.generate(address));

		mainPhone.setPhoneId(IDGenerator.generate(mainPhone));
		mainFax.setPhoneId(IDGenerator.generate(mainFax));


		address.setOrgGroup(company_base);
		mainPhone.setOrgGroup(company_base);
		mainFax.setOrgGroup(company_base);

		mainFax.setPhoneType(PHONE_FAX);
		mainPhone.setPhoneType(PHONE_WORK);

		company_base.setOrgGroupType(getOrgGroupType());
		company_base.setOwningCompany(company_base);

		return company_base.getOrgGroupId();
	}

	/**
	 * @throws ArahantException
	 *
	 */
	@Override
	public void update() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.saveOrUpdate(address);
		hsu.saveOrUpdate(mainPhone);
		hsu.saveOrUpdate(mainFax);
		//	hsu.saveOrUpdate(company);
		if (mainContact != null) {
			logger.debug("Updating mainContact");
			mainContact.update();
			mainContact.assignToOrgGroup(company_base.getOrgGroupId(), true);
		}
	}

	@Override
	public void insert() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.insert(address);
		hsu.insert(mainPhone);
		hsu.insert(mainFax);

		//	hsu.insert(company);

		if (mainContact != null) {
			logger.debug("Inserting mainContact");
			mainContact.person.setCompanyBase(company_base);
			mainContact.insert();
			mainContact.assignToOrgGroup(company_base.getOrgGroupId(), true);
		}

		if (autoOwn)
			company_base.setOwningCompany(company_base);
	}
	//for speed on import programs - can eliminate a lot of update statements
	private boolean autoOwn = true;

	public void dontAutoOwn() {
		autoOwn = false;
	}

	/*
	 * public void load(final String key) throws ArahantException {
	 * logger.debug("Loading "+key); company=hsu.get(CompanyBase.class, key); if
	 * (company==null) throw new ArahantException("Company not found in
	 * database."); initMembers(); }
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (company_base.getInvoices() != null && company_base.getInvoices().size() > 0)
			throw new ArahantDeleteException("Can't delete a company with invoice records!");

		if (company_base.getProjects() != null && company_base.getProjects().size() > 0)
			throw new ArahantDeleteException("Can't delete a company with projects associated.");
		
		String org_group_id = company_base.getOrgGroupId();
		company_base.setOwningCompany(null);
		hsu.flush();
		

		try {
			if (company_base.getPersons() != null) {
				Set<Person> contacts = company_base.getPersons();
				for (Person p : contacts) {
					BPerson bp = new BPerson(p);
					bp.delete();
				}
			}
			hsu.flush();

			//delete address and phones
			List<ArahantBean> deletes = new LinkedList<ArahantBean>();
			deletes.addAll(company_base.getAddresses());
			deletes.addAll(company_base.getPhones());
			deletes.addAll(company_base.getQuestionDetails());
			company_base.getAddresses().clear();
			company_base.getPhones().clear();
			company_base.getQuestionDetails().clear();
			hsu.flush();

			hsu.delete(deletes);

			for (OrgGroup o : company_base.getOrgGroups())
				o.setOwningCompany(null);
			company_base.getPersons().clear();

			hsu.delete(company_base);
			final BOrgGroup og = new BOrgGroup(company_base);
			og.destroy();
			
			hsu.commitTransaction();
			hsu.beginTransaction();  //  just to leave things in the state I presume they were in when we arrived
		} catch (final Exception e) {
//			e.printStackTrace();
			hsu.rollbackTransaction();				
			throw new ArahantDeleteException();
		}
	}

	public String getFederalEmployerId() {
		return company_base.getFederalEmployerId();
	}

	public String getIdentifier() {
		return company_base.getExternalId();
	}

	public String getOrgGroupId() {
		return company_base.getOrgGroupId();
	}

	public abstract int getOrgGroupType();

	public void setFederalEmployerId(final String federalEmployerId) {
		company_base.setFederalEmployerId(federalEmployerId);
	}

	public void setIdentifier(final String identifier) {
		company_base.setExternalId(identifier);
	}

	public String getName() {
		return company_base.getName();
	}

	public String getStreet() {
		return getAddress().getStreet();
	}

	public String getAddressId() {
		return getAddress().getAddressId();
	}

	public String getCity() {
		if (getAddress().getCity() == null)
			return "";
		return getAddress().getCity();
	}

	public String getCountry() {
		if (getAddress().getCountry() == null)
			return "";
		return getAddress().getCountry();
	}

	public String getStreet2() {
		if (getAddress().getStreet2() == null)
			return "";
		return getAddress().getStreet2();
	}

	public String getMainContactPersonalEmail() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getPersonalEmail();
	}

	public String getMainContactFname() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getFirstName();
	}

	public String getMainContactLname() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getLastName();
	}

	public String getMainContactMname() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getMiddleName();
	}

	public String getMainContactLogin() {
		final BPerson c = getMainContact();
		if (c != null)
			return c.getUserLogin();
		return "";
	}

	public String getMainContactPassword() {
		final BPerson c = getMainContact();
		if (c != null)
			return c.getUserPassword();
		return "";
	}

	public String getMainContactPersonId() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getPersonId();
	}

	public String getMainContactScreenGroupId() {
		final BPerson c = getMainContact();
		if (c != null && c.getScreenGroup() != null)
			return c.getScreenGroup().getScreenGroupId();
		return "";
	}

	public String getMainContactScreenGroupName() {
		final BPerson c = getMainContact();
		if (c != null && c.getScreenGroup() != null)
			return c.getScreenGroup().getName();
		return "";
	}

	public String getMainContactScreenGroupExtId() {
		final BPerson c = getMainContact();
		if (c != null && c.getScreenGroup() != null) {
			final BScreenGroup group = new BScreenGroup(c.getScreenGroup());
			return group.getExtId();
		}
		return "";
	}

	public String getMainContactSecurityGroupId() {
		final BPerson c = getMainContact();
		if (c != null && c.getSecurityGroup() != null)
			return c.getSecurityGroup().getSecurityGroupId();
		return "";
	}

	public String getMainContactSecurityGroupName() {
		final BPerson c = getMainContact();
		if (c != null && c.getSecurityGroup() != null)
			return c.getSecurityGroup().getId();
		return "";
	}

	public String getMainContactJobTitle() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getJobTitle();
	}

	public String getMainContactWorkFax() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getWorkFaxNumber();
	}

	public String getMainContactWorkPhone() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getWorkPhoneNumber();
	}

	public String getMainFaxId() {
		if (getPhoneByMainFaxId() == null)
			return "";
		return getPhoneByMainFaxId().getPhoneId();
	}

	public String getMainFaxNumber() {
		if (getPhoneByMainFaxId() == null)
			return "";
		return getPhoneByMainFaxId().getPhoneNumber();
	}

	public String getMainPhoneId() {
		if (getPhoneByMainPhoneId() == null)
			return "";
		return getPhoneByMainPhoneId().getPhoneId();
	}

	public String getMainPhoneNumber() {
		if (getPhoneByMainPhoneId() == null || (getPhoneByMainPhoneId().getPhoneNumber() == null))
			return "";
		return getPhoneByMainPhoneId().getPhoneNumber();
	}

	public String getState() {
		if (getAddress().getState() == null)
			return "";
		return getAddress().getState();
	}

	public String getZip() {
		if (getAddress().getZip() == null)
			return "";
		return getAddress().getZip();
	}

	public String getCounty() {
		if (getAddress().getCounty() == null)
			return "";
		return getAddress().getCounty();
	}

	protected Phone getPhoneByMainPhoneId() {
		for (Phone p : company_base.getPhones())
			if (p.getPhoneType() == PHONE_WORK)
				return p;

		return null;
		/*
		 *
		 * return hsu.createCriteria(Phone.class) .eq(Phone.ORGGROUP, company)
		 * .eq(Phone.PHONETYPE, PHONE_WORK) .first();
		 */
	}

	protected Phone getPhoneByMainFaxId() {
		for (Phone p : company_base.getPhones())
			if (p.getPhoneType() == PHONE_FAX)
				return p;
		return null;
		/*
		 * return hsu.createCriteria(Phone.class) .eq(Phone.ORGGROUP, company)
		 * .eq(Phone.PHONETYPE, PHONE_FAX) .first();
		 */
	}

	protected Address getAddress() throws ArahantException {
		if (address != null)
			return address;
		try {
			if (company_base.getAddresses().size() > 0)
				address =/*
						 * company.getAddresses().iterator().next();
						 */
						ArahantSession.getHSU().createCriteria(Address.class).eq(Address.ORGGROUP, company_base).first();
			else //if(address==null)
			{
				address = new Address();
				address.setAddressId(IDGenerator.generate(address));
				address.setAddressType(ADDR_WORK);
				address.setOrgGroup(company_base);
				address.setCity("");
				address.setState("");
				address.setStreet("");
				address.setZip("");
				ArahantSession.getHSU().insert(address);

			}

			return address;
		} catch (final RuntimeException e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * @param name
	 */
	public void setName(final String name) {
		company_base.setName(name);
	}

	/**
	 * @param mainContactWorkPhone
	 * @throws ArahantException
	 */
	public void setMainContactWorkPhone(final String mainContactWorkPhone) throws ArahantException {
		if (checkMainContact(mainContactWorkPhone))
			getMainContact().setWorkPhone(mainContactWorkPhone);
	}

	public void setMainContactHomePhone(String mainContactHomePhone) {
		if (checkMainContact(mainContactHomePhone))
			getMainContact().setHomePhone(mainContactHomePhone);
	}

	public void setMainContactMobilePhone(String mainContactMobilePhone) {
		if (checkMainContact(mainContactMobilePhone))
			getMainContact().setMobilePhone(mainContactMobilePhone);
	}

	/**
	 * @param mainContactWorkFax
	 * @throws ArahantException
	 */
	public void setMainContactWorkFax(final String mainContactWorkFax) throws ArahantException {
		if (checkMainContact(mainContactWorkFax))
			getMainContact().setWorkFax(mainContactWorkFax);
	}

	abstract boolean checkMainContact(String val) throws ArahantException;

	/**
	 * @param mainContactPersonalEmail
	 * @throws ArahantException
	 */
	public void setMainContactPersonalEmail(final String mainContactPersonalEmail) throws ArahantException {

		if (checkMainContact(mainContactPersonalEmail))
			getMainContact().setPersonalEmail(mainContactPersonalEmail);
	}

	/**
	 * @param mainContactJobTitle
	 * @throws ArahantException
	 */
	public void setMainContactJobTitle(final String mainContactJobTitle) throws ArahantException {
		if (checkMainContact(mainContactJobTitle))
			getMainContact().setJobTitle(mainContactJobTitle);
	}

	/**
	 * @param mainContactLname
	 * @throws ArahantException
	 */
	public void setMainContactLname(final String mainContactLname) throws ArahantException {
		if (checkMainContact(mainContactLname))
			getMainContact().setLastName(mainContactLname);
	}

	/**
	 * @param mainContactFname
	 * @throws ArahantException
	 */
	public void setMainContactFname(final String mainContactFname) throws ArahantException {
		if (checkMainContact(mainContactFname))
			getMainContact().setFirstName(mainContactFname);
	}

	public void setMainContactMname(final String mainContactMname) throws ArahantException {
		if (checkMainContact(mainContactMname))
			getMainContact().setMiddleName(mainContactMname);
	}

	public void setMainContactNname(final String mainContactNname) throws ArahantException {
		if (checkMainContact(mainContactNname))
			getMainContact().setNickName(mainContactNname);
	}

	public void setMainContactLogin(final String login) throws ArahantException {
		if (checkMainContact(login))
			getMainContact().setUserLogin(login);
	}

	public void setMainContactPassword(final String mainContactPassword, boolean canLogin) throws ArahantException {
		if (checkMainContact(mainContactPassword))
			getMainContact().setUserPassword(mainContactPassword, canLogin);
	}

	public void setMainContactScreenGroupId(final String mainContactScreenGroupId) throws ArahantException {
		if (checkMainContact(mainContactScreenGroupId))
			getMainContact().setScreenGroupId(mainContactScreenGroupId);
	}

	public void setMainContactSecurityGroupId(final String mainContactSecurityGroupId) throws ArahantException {
		if (checkMainContact(mainContactSecurityGroupId))
			getMainContact().setSecurityGroupId(mainContactSecurityGroupId);
	}

	public void setStreet(final String street) {
		getAddress().setStreet(street);
	}

	public void setStreet2(final String street2) {
		getAddress().setStreet2(street2);
	}

	public void setCity(final String city) {
		getAddress().setCity(city);
	}

	public void setCountry(String country) {
		getAddress().setCountry(country);
	}

	public void setTimeZone(short timeZone) {
		getAddress().setTimeZoneOffset(timeZone);
	}

	public void setState(final String state) {
		getAddress().setState(state);
	}

	public void setZip(final String zip) {
		getAddress().setZip(zip);
	}

	public void setCounty(final String county) {
		getAddress().setCounty(county);
	}

	public void setMainPhoneNumber(final String mainPhoneNumber) throws ArahantException {
		if (getPhoneByMainPhoneId() == null) {
			mainPhone = new Phone();
			mainPhone.setPhoneId(IDGenerator.generate(mainPhone));
			mainPhone.setPhoneType(PHONE_WORK);
			company_base.getPhones().add(mainPhone);
			mainPhone.setOrgGroup(company_base);
		}
		getPhoneByMainPhoneId().setPhoneNumber(mainPhoneNumber);
	}

	public void setMainFaxNumber(final String mainFaxNumber) throws ArahantException {
		if (getPhoneByMainFaxId() == null) {
			mainFax = new Phone();
			mainFax.setPhoneId(IDGenerator.generate(mainFax));
			company_base.getPhones().add(mainFax);
			mainFax.setOrgGroup(company_base);
			mainFax.setPhoneType(PHONE_FAX);
		}
		getPhoneByMainFaxId().setPhoneNumber(mainFaxNumber);
	}

	public boolean getMainContactCanLogin() {
		final BPerson c = getMainContact();
		if (c == null)
			return false;
		return c.getCanLogin() == 'Y';
	}

	public static BCompanyBase[] search(final String name, boolean includeProspects, final int max) throws ArahantException {
		final HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, name).setMaxResults(max);

		if (!includeProspects)
			hcu.ne(CompanyBase.ORGGROUPTYPE, PROSPECT_TYPE);

		return makeArrayEx(hcu.list());
	}

	public static BCompanyBase[] searchCompanySpecific(final String name, boolean includeProspects, boolean activeOnly, final int max) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		CompanyDetail cd = hsu.getCurrentCompany();
		List<CompanyBase> ret = new ArrayList<CompanyBase>(hsu.createCriteria(CompanyDetail.class).orderBy(OrgGroup.NAME).eq(CompanyDetail.ORGGROUPID, cd.getCompanyId()).like(OrgGroup.NAME, name).list());
		HibernateCriteriaUtil<ClientCompany> crit = hsu.createCriteria(ClientCompany.class);
		if (activeOnly)
			crit.gtOrEq(ClientCompany.INACTIVEDATE, DateUtils.today(), 0);
		ret.addAll(crit.orderBy(OrgGroup.NAME).eqOrNull(ClientCompany.COMPANY, cd).like(OrgGroup.NAME, name).setMaxResults(max - ret.size()).list());
		if (includeProspects)
			ret.addAll(ArahantSession.getHSU().createCriteria(ProspectCompany.class).orderBy(OrgGroup.NAME).eqOrNull(ProspectCompany.ASSOCIATED_COMPANY, cd).like(OrgGroup.NAME, name).setMaxResults(max - ret.size()).list());
		return makeArrayEx(ret);
	}

	public static BCompanyBase[] searchCompanySpecific(final String name, boolean includeProspects, final int max) throws ArahantException {
		return searchCompanySpecific(name, includeProspects, false, max);
	}

	@SuppressWarnings("unchecked")
	public static List<String> searchCompanySpecificIds(final String name, boolean includeProspects, final int max) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<String> ret = new ArrayList<String>();
		CompanyDetail cd = hsu.getCurrentCompany();
		ret.addAll((List) hsu.createCriteria(CompanyDetail.class).selectFields(CompanyDetail.ORGGROUPID).orderBy(OrgGroup.NAME).eq(CompanyDetail.ORGGROUPID, cd.getCompanyId()).like(OrgGroup.NAME, name).list());
		ret.addAll((List) hsu.createCriteria(ClientCompany.class).selectFields(ClientCompany.ORGGROUPID).orderBy(OrgGroup.NAME).eqOrNull(ClientCompany.COMPANY, cd).like(OrgGroup.NAME, name).setMaxResults(max - ret.size()).list());

		if (includeProspects)
			ret.addAll((List) hsu.createCriteria(ProspectCompany.class).selectFields(ProspectCompany.ORGGROUPID).orderBy(OrgGroup.NAME).eqOrNull(ProspectCompany.ASSOCIATED_COMPANY, cd).like(OrgGroup.NAME, name).setMaxResults(max - ret.size()).list());

		return ret;
	}

	public static BCompanyBase[] searchByCompanyType(final String name, boolean includeProspects, final int max, int companyType) throws ArahantException {
		final HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, name).eq(CompanyBase.ORGGROUPTYPE, companyType).setMaxResults(max);

		if (!includeProspects)
			hcu.ne(CompanyBase.ORGGROUPTYPE, PROSPECT_TYPE);

		return makeArrayEx(hcu.list());
	}

	public static BCompanyBase[] searchByCompanyTypes(final String name, boolean includeProspects, final int max, List companyType) throws ArahantException {
		final HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, name).in(CompanyBase.ORGGROUPTYPE, companyType).setMaxResults(max);

		if (!includeProspects)
			hcu.ne(CompanyBase.ORGGROUPTYPE, PROSPECT_TYPE);

		return makeArrayEx(hcu.list());
	}

	public abstract String getOrgType();

	public String getMainContactName() {
		final BPerson c = getMainContact();
		if (c == null)
			return "";
		return c.getNameLFM();
	}

	public static BProject[] createProjectsFromStandard(final HibernateSessionUtil hsu, final String companyId, final String[] projectIDs) throws ArahantException {

		final CompanyBase c = hsu.get(CompanyBase.class, companyId);
		final BProject[] ret = new BProject[projectIDs.length];

		for (int loop = 0; loop < projectIDs.length; loop++) {

			final StandardProject sp = hsu.get(StandardProject.class, projectIDs[loop]);

			final BProject p = new BProject();
			p.create();
			p.setRequestingOrgGroupId(c.getOrgGroupId());
			p.setBillable(sp.getBillable());
			p.setBillingMethod(sp.getBillingMethod());
			p.setBillingRate(sp.getBillingRate());
			p.setDateReported(DateUtils.getDate(new Date()));
			p.setDescription(sp.getDescription());
			p.setDetailDesc(sp.getDetailDesc());
			p.setDollarCap(sp.getDollarCap());
			p.setOrgGroupId(c.getOrgGroupId());
			if (sp.getProductService() != null)
				p.setProductServiceId(sp.getProductService().getProductId());
			p.setProjectCategoryId(sp.getProjectCategory().getProjectCategoryId());
			p.setProjectTypeId(sp.getProjectType().getProjectTypeId());
			p.setRequesterName(sp.getRequesterName());
			p.setAllEmployees(sp.getAllEmployees());

			BRouteTypeAssoc rta = new BRouteTypeAssoc(p.getProjectCategoryId(), p.getProjectTypeId());

			if (!isEmpty(rta.getRouteId())) {
				p.setRouteId(rta.getRouteId());
				BRoute rt = new BRoute(rta.getRouteId());
				if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
					p.setRouteStopId(rt.getRouteStopId());
					p.setProjectStatusId(rt.getProjectStatusId());
				}
			}

			if (isEmpty(p.getRouteStopId()) || isEmpty(p.getProjectStatusId()))
				throw new ArahantWarning("There is not a valid Project Route associated to the selected Standard Project's Category/Type combination.  Please note that if there is a Project Route associated to this combination, ensure that the Project Route has an Initial Route Stop and Project Status.");

			p.insert();

			ret[loop] = p;
		}
		return ret;
	}

	/**
	 *  Search for companies with / without billable timesheets
	 *
	 * @param hsu
	 * @param name
	 * @param identifier
	 * @param billableItemsIndicator 1=billable, 2=non-billable, 0=both
	 * @param max max number of records to return
	 * @param fromDate clients with billable time from this date till now go first
	 * @return
	 * @throws ArahantException
	 */
	public static List<Record> searchBillingState(final HibernateSessionUtil hsu,
												  final String name,
												  final String identifier,
												  final int billableItemsIndicator,
												  final int max,
												  final int fromDate) throws ArahantException {
		final Connection db = KissConnection.get();
		final List<Record> recs = new ArrayList<>();
		List<Record> trecs;
		final ArrayList<Object> args = new ArrayList<>();

		args.add(fromDate);
		if (name != null  &&  !name.isEmpty()  &&  !name.equals("%"))
			args.add(name);
		if (identifier != null  &&  !identifier.isEmpty()  &&  !identifier.equals("%"))
			args.add(identifier);

		try {
			if (billableItemsIndicator == 1 || billableItemsIndicator == 0) {
				// recent billable first
				String select = "select distinct og.group_name, og.org_group_id, og.org_group_type, 1 recent_billable, og.external_id, og.org_group_type " +
						"from company_base cb " +
						"join org_group og " +
						"  on cb.org_group_id = og.org_group_id " +
						"where og.org_group_type not in (8, 16) " +
						"      and og.org_group_id in (select proj.requesting_org_group " +
						"                              from project proj " +
						"                              join project_shift ps " +
						"                                on proj.project_id = ps.project_id " +
						"                              join timesheet ts " +
						"                                on ps.project_shift_id = ts.project_shift_id " +
						"                              where ts.billable = 'Y' " +
						"                                    and ts.entry_state = 'A' " +
						"                                    and ts.end_date >= ?)";
				if (name != null  &&  !name.isEmpty()  &&  !name.equals("%"))
					select += "and upper(og.group_name) like upper(?)\n";
				if (identifier != null  &&  !identifier.isEmpty()  &&  !identifier.equals("%"))
					select += "and upper(og.external_id) like upper(?)\n";
				select += "order by og.group_name";
				recs.addAll(db.fetchAll(max, select, args));

				// non-recent but still billable
				if (fromDate > 0) {
					select = "select distinct og.group_name, og.org_group_id, og.org_group_type, 2 recent_billable, og.external_id, og.org_group_type " +
							"from company_base cb " +
							"join org_group og " +
							"  on cb.org_group_id = og.org_group_id " +
							"where og.org_group_type not in (8, 16) " +
							"      and og.org_group_id in (select proj.requesting_org_group " +
							"                              from project proj " +
							"                              join project_shift ps " +
							"                                on proj.project_id = ps.project_id " +
							"                              join timesheet ts " +
							"                                on ps.project_shift_id = ts.project_shift_id " +
							"                              where ts.billable = 'Y' " +
							"                                    and ts.entry_state = 'A' " +
							"                                    and ts.end_date < ?)";
					if (name != null && !name.isEmpty() && !name.equals("%"))
						select += "and upper(og.group_name) like upper(?)\n";
					if (identifier != null && !identifier.isEmpty() && !identifier.equals("%"))
						select += "and upper(og.external_id) like upper(?)\n";
					select += "order by og.group_name";
					trecs = db.fetchAll(max, select, args);
					recs.addAll(trecs);
				}
			}

			// without billable
			if (billableItemsIndicator == 2 || billableItemsIndicator == 0) {
				String select = "select distinct og.group_name, og.org_group_id, og.org_group_type, 0 recent_billable, og.external_id, og.org_group_type " +
						"from company_base cb " +
						"join org_group og " +
						"  on cb.org_group_id = og.org_group_id " +
						"where og.org_group_type not in (8, 16) " +
						"      and og.org_group_id not in (select proj.requesting_org_group " +
						"                              from project proj " +
						"                              join project_shift ps " +
						"                                on proj.project_id = ps.project_id " +
						"                              join timesheet ts " +
						"                                on ps.project_shift_id = ts.project_shift_id " +
						"                              where ts.billable = 'Y' " +
						"                                    and ts.entry_state = 'A' " +
						"                                    and (ts.end_date >= ? or 1=1))";
				if (name != null  &&  !name.isEmpty()  &&  !name.equals("%"))
					select += "and upper(og.group_name) like upper(?) ";
				if (identifier != null  &&  !identifier.isEmpty()  &&  !identifier.equals("%"))
					select += "and upper(og.external_id) like upper(?) ";
				select += "order by og.group_name";
				trecs = db.fetchAll(max, select, args);
				recs.addAll(trecs);
			}

			return recs;
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

		/**
         *
         * @param hsu
         * @param name
         * @param identifier
         * @param billableItemsIndicator
         * @param max
         * @param fromDate  clients with billable time from this date till now go first
         * @return
		 *
		 *
         * @throws ArahantException
         */
	public static BCompanyBase[] searchBillingState_old(final HibernateSessionUtil hsu, final String name, final String identifier, final int billableItemsIndicator, final int max, final int fromDate) throws ArahantException {
		HibernateCriteriaUtil<CompanyBase> hcu = hsu.createCriteria(CompanyBase.class);

		hcu.distinct();

		hcu.like(OrgGroup.NAME, name);

		hcu.like(CompanyBase.EXTERNAL_REF, identifier);

		hcu.orderBy(OrgGroup.NAME);

		final List<CompanyBase> cblist = new LinkedList<CompanyBase>();

        List<CompanyBase> hasRecentBillable;
		List<CompanyBase> hasBillable = new LinkedList<CompanyBase>();
		List<CompanyBase> hasNoBillable = new LinkedList<CompanyBase>();

		List<CompanyBase> tlist = hsu.createCriteria(CompanyBase.class).like(OrgGroup.NAME, name).like(CompanyBase.EXTERNAL_REF, identifier).notIn(CompanyBase.ORGGROUPTYPE, new Integer[]{PROSPECT_TYPE, AGENT_TYPE}).orderBy(CompanyBase.NAME).joinTo(CompanyBase.ORGGROUPS).joinTo(OrgGroup.PROJECTS).joinTo(Project.TIMESHEETS).eq(Timesheet.BILLABLE, 'Y').eq(Timesheet.STATE, TIMESHEET_APPROVED).list();

		if (billableItemsIndicator == 1 || billableItemsIndicator == 0) //has billable
			hasBillable = tlist;

		if (billableItemsIndicator == 2 || billableItemsIndicator == 0) //has no billable
		{
			hcu = hsu.createCriteria(CompanyBase.class).distinct().orderBy(CompanyBase.NAME).notIn(CompanyBase.ORGGROUPTYPE, new Integer[]{PROSPECT_TYPE, AGENT_TYPE});

			if (!isEmpty(name) && !name.equals("*"))
				hcu.like(OrgGroup.NAME, name);

			if (!isEmpty(identifier) && !identifier.equals("*"))
				hcu.like(CompanyBase.EXTERNAL_REF, identifier);

			hasNoBillable = hcu.list();
			hasNoBillable.removeAll(tlist);
		}

        tlist = hsu.createCriteria(CompanyBase.class).like(OrgGroup.NAME, name)
                .like(CompanyBase.EXTERNAL_REF, identifier)
                .notIn(CompanyBase.ORGGROUPTYPE, new Integer[]{PROSPECT_TYPE, AGENT_TYPE})
                .orderBy(CompanyBase.NAME)
                .joinTo(CompanyBase.ORGGROUPS)
                .joinTo(OrgGroup.PROJECTS)
                .joinTo(Project.TIMESHEETS)
                .eq(Timesheet.BILLABLE, 'Y')
                .eq(Timesheet.STATE, TIMESHEET_APPROVED)
                .ge(Timesheet.ENDDATE, fromDate)
                .list();
        hasRecentBillable = tlist;
        hasBillable.removeAll(tlist);

        cblist.addAll(hasRecentBillable);
        cblist.addAll(hasBillable);
		cblist.addAll(hasNoBillable);

		final List<BCompanyBase> comps = new ArrayList<BCompanyBase>(cblist.size());

		for (final CompanyBase base : cblist)
			comps.add(BCompanyBase.get(base.getOrgGroupId()));
		/*
		 * if (base instanceof ClientCompany) comps.add(new
		 * BClientCompany((ClientCompany)base)); else if (base instanceof
		 * VendorCompany) comps.add(new BVendorCompany((VendorCompany)base));
		 * else if (base instanceof CompanyDetail) comps.add(new
		 * BCompany((CompanyDetail)base)); else { logger.warn("Found a bad
		 * record - company base that can't load subclass
		 * "+base.getOrgGroupId()); //throw new ArahantException("Search
		 * returned bad results!"); }
		 */
		//	Collections.sort(comps,new BillingStateComparator());

		int size = comps.size();

		if (max > 0 && comps.size() > max)
			size = max;

		final BCompanyBase[] ret = new BCompanyBase[size];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = comps.get(loop);

		return ret;
	}

	public boolean hasBillableTimesheets() {
		/*
		 * There is an interesting problem here.  Sometimes a project is associated with a sub-org-group of a client rather than the top-level client.
		 * In that case, this returns the wrong result because it is checking company groups and finds no projects because the projects were associated
		 * to a sub-org group of the company - which isn't searched.
		 */
		final HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class)
                .eq(OrgGroup.ORGGROUPID, getOrgGroupId())
                .joinTo(CompanyBase.PROJECTS)
                .joinTo(Project.TIMESHEETS)
                .eq(Timesheet.BILLABLE, 'Y')
                .eq(Timesheet.STATE, TIMESHEET_APPROVED);
		return hcu.exists();

		/*
		 * Iterator<Project>pitr=company.getProjects().iterator();
		 *
		 * while (pitr.hasNext()) if (pitr.next().getUnbilledTimesheetCount()>0)
		 * return true;
		 *
		 * return false;
		 */
	}

    public boolean hasBillableTimesheetsFromDate(int date) {
		/*
		 * There is an interesting problem here.  Sometimes a project is associated with a sub-org-group of a client rather than the top-level client.
		 * In that case, this returns the wrong result because it is checking company groups and finds no projects because the projects were associated
		 * to a sub-org group of the company - which isn't searched.
		 */
        final HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class)
                .eq(OrgGroup.ORGGROUPID, getOrgGroupId())
                .joinTo(CompanyBase.PROJECTS)
                .joinTo(Project.TIMESHEETS)
                .eq(Timesheet.BILLABLE, 'Y')
                .eq(Timesheet.STATE, TIMESHEET_APPROVED)
                .ge(Timesheet.ENDDATE, date);
        return hcu.exists();
    }

	public String getHasBillableTimesheets() {
		return hasBillableTimesheets() ? "Yes" : "No";
	}

	public static BCompanyBase get(String id) {
		if ("ReqCo".equals(id)) {
			ClientCompany cc = new ClientCompany();
			cc.setName("Requesting Company");
			cc.setOrgGroupId("ReqCo");
			return new BClientCompany(cc);
		}
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		try {
			ClientCompany cc = hsu.get(ClientCompany.class, id);
			if (cc != null)
				return new BClientCompany(cc);
		} catch (Exception e) {
			//try the next type
		}
		try {
			CompanyDetail cd = hsu.get(CompanyDetail.class, id);
			if (cd != null)
				return new BCompany(cd);
		} catch (Exception e) {
			//try the next type
		}
		try {
			VendorCompany vc = hsu.get(VendorCompany.class, id);
			if (vc != null)
				return new BVendorCompany(vc);
		} catch (Exception e) {
			//try the next type
		}
		try {
			ProspectCompany pc = hsu.get(ProspectCompany.class, id);
			if (pc != null)
				return new BProspectCompany(pc);
		} catch (Exception e) {
			//try the next type
		}
		return null;
	}

	public String getEdiApplicationReceiverId() {
		return company_base.getApplicationReceiverId();
	}

	public String getEdiApplicationSenderId() {
		return company_base.getApplicationSenderId();
	}

	public String getEdiInterchangeReceiverId() {
		return company_base.getInterchangeReceiverId();
	}

	public String getEdiInterchangeSenderId() {
		return company_base.getInterchangeSenderId();
	}

	public String getEdiTransferDirectory() {
		return company_base.getComDirectory();
	}

	public String getEdiTransferEncrpytionKey() {
		return company_base.getEncryptionKeyId();
	}

	public String getEdiTransferHost() {
		if (!isEmpty(company_base.getComUrl()))
			if (company_base.getComUrl().charAt(0) == 's') {
				String url = company_base.getComUrl();

				return url.substring(url.indexOf("@") + 1, url.lastIndexOf(":"));
			} else
				try {
					URL url = new URL(company_base.getComUrl());
					return url.getHost();
				} catch (Exception e) {
					return "";
				}

		return "";
	}

	public String getEdiTransferPassword() {
		return company_base.getComPassword();
	}

	public int getEdiTransferPort() {
		if (!isEmpty(company_base.getComUrl()))
			if (company_base.getComUrl().charAt(0) == 's') {
				String url = company_base.getComUrl();

				return Integer.parseInt(url.substring(url.lastIndexOf(":") + 1, url.length()));
			} else
				try {
					URL url = new URL(company_base.getComUrl());
					return url.getPort();
				} catch (Exception e) {
					return 0;
				}

		return 0;
	}

	public String getEdiTransferSchemeId() {
		if (!isEmpty(company_base.getComUrl()))
			if (company_base.getComUrl().charAt(0) == 's') {
				String url = company_base.getComUrl();

				return url.substring(0, url.indexOf(":"));
			} else
				try {
					URL url = new URL(company_base.getComUrl());
					return url.getProtocol();
				} catch (Exception e) {
					return "";
				}

		return "";
	}

	public String getEdiTransferUsername() {
		if (!isEmpty(company_base.getComUrl()))
			if (company_base.getComUrl().charAt(0) == 's') {
				String url = company_base.getComUrl();

				return url.substring(url.lastIndexOf("/") + 1, url.indexOf("@"));
			} else
				try {
					URL url = new URL(company_base.getComUrl());
					return url.getUserInfo();
				} catch (Exception e) {
					return "";
				}

		return "";
	}

	public void setEdiApplicationReceiverId(String ediApplicationReceiverId) {
		company_base.setApplicationReceiverId(ediApplicationReceiverId);
	}

	public void setEdiApplicationSenderId(String ediApplicationSenderId) {
		company_base.setApplicationSenderId(ediApplicationSenderId);
	}

	public void setEdiInterchangeReceiverId(String ediInterchangeReceiverId) {
		company_base.setInterchangeReceiverId(ediInterchangeReceiverId);
	}

	public void setEdiInterchangeSenderId(String ediInterchangeSenderId) {
		company_base.setInterchangeSenderId(ediInterchangeSenderId);
	}

	public void setEdiTransferDirectory(String ediTransferDirectory) {
		company_base.setComDirectory(ediTransferDirectory);
	}

	public void setEdiTransferEncryptionKey(String ediTransferEncryptionKey) {
		company_base.setEncryptionKeyId(ediTransferEncryptionKey);
	}

	public String getEditTransferEncryptionKeyText() {
		return company_base.getPublicEncryptionKey();
	}

	public void setEdiTransferEncryptionKeyText(String ediTransferEncryptionKeyText) {
		company_base.setPublicEncryptionKey(ediTransferEncryptionKeyText);
	}

	public void setEdiTransferPassword(String ediTransferPassword) {
		company_base.setComPassword(ediTransferPassword);
	}

	public void setEdiTransferURL(String ediTransferSchemeId, String ediTransferHost, int ediTransferPort, String ediTransferUsername) {
		if (!isEmpty(ediTransferSchemeId))
			if (ediTransferSchemeId.equals("sftp"))
				company_base.setComUrl(ediTransferSchemeId.trim() + "://" + ediTransferUsername + "@" + ediTransferHost + ":" + ediTransferPort);
			else
				try {
					URL url = new URL(ediTransferSchemeId, ediTransferUsername + "@" + ediTransferHost, ediTransferPort, "");
					company_base.setComUrl(url.toExternalForm());
				} catch (MalformedURLException ex) {
					throw new ArahantException(ex);
				}
		else
			company_base.setComUrl("");
	}

	public BOrgGroup[] listAllOrgGroups(int max) {
		return BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.OWNINGCOMPANY, company_base).orderBy(OrgGroup.NAME).setMaxResults(max).list());
	}

	public String getId() {
		return company_base.getOrgGroupId();
	}

	public static BCompanyBase[] search(String name, String identifier, int type, int cap) {
		HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).like(CompanyBase.NAME, name).like(CompanyBase.EXTERNAL_REF, identifier).orderBy(CompanyBase.NAME).setMaxResults(cap);
		if (type != 0) {
			ArrayList<Integer> ar = new ArrayList<Integer>();
			for (int loop = 1; loop < 16; loop++)
				if ((type & loop) > 0)
					ar.add(loop);

			hcu.in(CompanyBase.ORGGROUPTYPE, ar);
		}

		return makeArrayEx(hcu.list());
	}

	public BPerson getMainContact() {
		if (mainContact == null) {
			Person c = getMainContactForOrgGroup(company_base);
			if (c != null)
				mainContact = new BPerson(c);
		}
		return mainContact;
	}

	protected void setMainContact(BPerson p) {
		mainContact = p;
	}

	public void setArahantURL(String arahantUrl) {
		company_base.setArahantURL(arahantUrl);
	}

	public String getArahantURL() {
		return company_base.getArahantURL();
	}

	public static BCompanyBase[] search(String id, String mainContactFirstName, String mainContactLastName, String name, int max, String sortOn, boolean sortAsc) {

		HibernateCriteriaUtil<CompanyBase> hcu = ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyBase.class).setMaxResults(max).eq(CompanyBase.ORGGROUPTYPE, COMPANY_TYPE).like(CompanyBase.NAME, name).like(CompanyBase.EXTERNAL_REF, id);

		if (mainContactFirstName.trim().equals("%"))
			mainContactFirstName = "";
		if (mainContactLastName.trim().equals("%"))
			mainContactLastName = "";

		HibernateCriteriaUtil mainContactHCU = null;

		if (!isEmpty(mainContactFirstName) || !isEmpty(mainContactLastName))
			mainContactHCU = hcu.leftJoinTo(CompanyBase.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').joinTo(OrgGroupAssociation.PERSON).like(Person.FNAME, mainContactFirstName).like(Person.LNAME, mainContactLastName);

		if ("address".equals(sortOn))
			if (sortAsc)
				hcu.joinTo(CompanyBase.ADDRESSES).orderBy(Address.STREET);
			else
				hcu.joinTo(CompanyBase.ADDRESSES).orderByDesc(Address.STREET);
		else if ("companyName".equals(sortOn))
			if (sortAsc)
				hcu.orderBy(CompanyBase.NAME);
			else
				hcu.orderByDesc(CompanyBase.NAME);
		else if ("fax".equals(sortOn)) {
			if (mainContactHCU == null)
				mainContactHCU = hcu.leftJoinTo(CompanyBase.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').leftJoinTo(OrgGroupAssociation.PERSON).like(Person.FNAME, mainContactFirstName).like(Person.LNAME, mainContactLastName);

			if (sortAsc)
				mainContactHCU.leftJoinTo(Person.PHONES).eq(Phone.PHONETYPE, ArahantConstants.PHONE_FAX).orderBy(Phone.PHONENUMBER);
			else
				mainContactHCU.leftJoinTo(Person.PHONES).eq(Phone.PHONETYPE, ArahantConstants.PHONE_FAX).orderByDesc(Phone.PHONENUMBER);

		} else if ("phone".equals(sortOn)) {
			if (mainContactHCU == null)
				mainContactHCU = hcu.leftJoinTo(CompanyBase.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').leftJoinTo(OrgGroupAssociation.PERSON).like(Person.FNAME, mainContactFirstName).like(Person.LNAME, mainContactLastName);

			if (sortAsc)
				mainContactHCU.leftJoinTo(Person.PHONES).eq(Phone.PHONETYPE, ArahantConstants.PHONE_WORK).orderBy(Phone.PHONENUMBER);
			else
				mainContactHCU.leftJoinTo(Person.PHONES).eq(Phone.PHONETYPE, ArahantConstants.PHONE_WORK).orderByDesc(Phone.PHONENUMBER);
		} else
			hcu.orderBy(CompanyBase.NAME);
		return makeArrayEx(hcu.list());
	}

	public String getDaysToSend() {
		return company_base.getDaysToSend();
	}

	public void setDaysToSend(String daysToSend) {
		if (daysToSend.length() == 7)
			company_base.setDaysToSend(daysToSend);
		else
			throw new ArahantException("days_to_send can only accept 7 characters");
	}

	public String getEdiActivated() {
		return String.valueOf(company_base.getEdiActivated());
	}

	public void setEdiActivated(String ediActivated) {
		company_base.setEdiActivated(ediActivated.charAt(0));
	}

	public int getTimeToSend() {
		return company_base.getTimeToSend();
	}

	public void setTimeToSend(int timeToSend) {
		company_base.setTimeToSend((short) timeToSend);
	}

	public String getEdiFileType() {
		return company_base.getEdiFileType() + "";
	}

	public void setEdiFileType(String value) {
		company_base.setEdiFileType(isEmpty(value) ? 'U' : value.charAt(0));
	}

	public String getEdiFileStatus() {
		return company_base.getEdiFileStatus() + "";
	}

	public void setEdiFileStatus(String value) {
		company_base.setEdiFileStatus(isEmpty(value) ? 'U' : value.charAt(0));
	}

	static BCompanyBase[] makeArrayEx(List<CompanyBase> list) {
		final BCompanyBase[] ct = new BCompanyBase[list.size()];

		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		int index = 0;

		final Iterator<CompanyBase> plistItr = list.iterator();

		while (plistItr.hasNext() && index < ct.length) {
			final CompanyBase c = plistItr.next();

			if (c instanceof ClientCompany)
				ct[index++] = new BClientCompany(c.getOrgGroupId());
			else if (c instanceof CompanyDetail)
				ct[index++] = new BCompany(c.getOrgGroupId());
			else if (c instanceof VendorCompany)
				ct[index++] = new BVendorCompany(c.getOrgGroupId());
			else if (c instanceof ProspectCompany)
				ct[index++] = new BProspectCompany(c.getOrgGroupId());
			else if (c instanceof Agency)
				ct[index++] = new BAgency(c.getOrgGroupId());
			else {
				try {
					final ClientCompany cc = hsu.get(ClientCompany.class, c.getOrgGroupId());
					if (cc != null)
						ct[index++] = new BClientCompany(c.getOrgGroupId());
				} catch (final Exception ignored) {
				}
				try {
					final CompanyDetail cd = hsu.get(CompanyDetail.class, c.getOrgGroupId());
					if (cd != null)
						ct[index++] = new BCompany(c.getOrgGroupId());
				} catch (final Exception ignored) {
				}
				try {
					final VendorCompany vc = hsu.get(VendorCompany.class, c.getOrgGroupId());
					if (vc != null)
						ct[index++] = new BVendorCompany(c.getOrgGroupId());
				} catch (final Exception ignored) {
				}
				try {
					final ProspectCompany vc = hsu.get(ProspectCompany.class, c.getOrgGroupId());
					if (vc != null)
						ct[index++] = new BProspectCompany(c.getOrgGroupId());
				} catch (final Exception ignored) {
				}
				try {
					final Agency vc = hsu.get(Agency.class, c.getOrgGroupId());
					if (vc != null)
						ct[index++] = new BAgency(c.getOrgGroupId());
				} catch (final Exception ignored) {
				}
			}
		}
		return ct;
	}
}
