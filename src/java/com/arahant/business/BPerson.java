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
import com.arahant.business.interfaces.IPersonList;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.LoginReport;
import com.arahant.reports.TimesheetExceptionReport;
import com.arahant.reports.TimesheetFinalizationReport;
import com.arahant.services.standard.wizard.benefitWizard.Enrollee;
import com.arahant.utils.*;
import org.kissweb.StringUtils;
import org.kissweb.database.ArrayListString;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Collections;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;

public class BPerson extends BusinessLogicBase implements IDBFunctions {

	private static final ArahantLogger logger = new ArahantLogger(BPerson.class);

	private boolean primary;
	protected Person person;
	protected ProphetLogin prophetLogin;
	private Phone workPhone;
	private Phone workFax;
	private Phone homePhone;
	private Phone cellPhone;
	private Address homeAddress;
	private Address workAddress;
	protected Set<IDBFunctions> pendingInserts = new HashSet<IDBFunctions>();
	protected Set<IDBFunctions> pendingUpdates = new HashSet<IDBFunctions>();
	private final HashMap<Integer, AddressPending> addresses = new HashMap<Integer, AddressPending>();
	private String lastEnteredTimeDate = "";
	private double hoursLastEntered = 0;
	private final int maxRowsTimeRelatedDropdown = 15;
	private int maxSubGroup = 0;
	private String missingTimeDay = "";
	private String realPersonId = "";
	private PersonCR pcr = null;
	private final HashMap<Integer, PhonePending> phones = new HashMap<Integer, PhonePending>();


	public BPerson() {
	}

	public BPerson(final Person p) throws ArahantException {
		if (p == null)
			throw new ArahantException("Empty person passed to constructor.");
		person = p;
		if (person.getProphetLogin() != null)
			prophetLogin = person.getProphetLogin();

		initMembers();
	}

	public BPerson(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BPerson(final Person p, final String orgGroupId) throws ArahantException {
		this(p);

		//this one is orgGroup based, need to find out for that org group
		//what the primary indicator is
		final OrgGroupAssociation oga = ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, p).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).first();
		primary = oga != null && oga.getPrimaryIndicator() == 'Y';
	}

	protected static BEmployee[] makeEmployeeArray(List<Person> list) {
		final BEmployee [] ret = new BEmployee[list.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPerson(list.get(loop)).getBEmployee();

		return ret;
	}

	public void ensureLoginRecordExists() {
		if (person.getProphetLogin() == null)
			if (person instanceof Employee) {
				BEmployee bemp = new BEmployee((Employee) person);
				bemp.makeLoginDefaults();
				bemp.update();
				prophetLogin = bemp.getProphetLogin();
			}
	}

	public List<CompanyDetail> getAllowedCompanies() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		//if no multi company support, just return the one company
		if (!ArahantSession.multipleCompanySupport) {
			List<CompanyDetail> ret = new ArrayList<>();
			ret.add(hsu.getFirst(CompanyDetail.class));
			return ret;
		}
		//find out what companies they are allowed in

		//  The following original line fails
//		List<CompanyDetail> compList = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, getPersonId()).setMaxResults(50).list();

		List<CompanyDetail> compList = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, getPersonId()).setMaxResults(50).list();

		//add any from the meta groups
		for (OrgGroup og : hsu.createCriteriaNoCompanyFilter(OrgGroup.class).setMaxResults(50).isNull(OrgGroup.OWNINGCOMPANY).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, getPersonId()).list()) {
			BOrgGroup borg = new BOrgGroup(og);
			compList.addAll(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).setMaxResults(50).joinTo(CompanyDetail.ORGGROUPS).in(OrgGroup.ORGGROUPID, borg.getAllOrgGroupsInHierarchy()).list());

		}

		//TODO: check if they are vendor which companies they are allowed in

		if (person.getOrgGroupType() == CLIENT_TYPE) {

			compList.add(new BClientCompany(person.getCompanyBase().getOrgGroupId()).getAssociatedCompany());

			if (compList.isEmpty())
				throw new ArahantWarning("No associated company detected.");
		}

		if (person.getOrgGroupType() == VENDOR_TYPE) {
			compList.add(new BVendorCompany(person.getCompanyBase().getOrgGroupId()).getAssociatedCompany());

			if (compList.isEmpty())
				throw new ArahantWarning("No associated company detected.");
		}

		if (person.getOrgGroupType() == AGENT_TYPE) {
			BAgent agent = new BAgent(person.getPersonId());

			for (BCompany comp : agent.getAgentCompanies())
				compList.add(comp.getBean());

			//if he's a supervisor, add choices from subordinates
			List<OrgGroup> orgs = hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.PERSON, person).list();

			List<String> orgIds = new LinkedList<String>();
			for (OrgGroup org : orgs)
				orgIds.addAll(new BOrgGroup(org).getAllOrgGroupsInHierarchy());

			compList.addAll(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).joinTo(CompanyDetail.AGENT_JOINS).joinTo(AgentJoin.AGENT).joinTo(Agent.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds).list());

			if (compList.isEmpty())
				throw new ArahantException("No associated company detected.");
		}

		//Arahant gets everybody
		if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ACCESS_LEVEL_WRITE)
			compList = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).setMaxResults(50).list();

		//remove duplicates
		HashSet<CompanyDetail> set = new HashSet<>(compList);
		compList.clear();
		compList.addAll(set);

		Collections.sort(compList);

		//if the company list is empty, give them the owner
		if (compList.isEmpty())
			compList.add(hsu.get(CompanyDetail.class, getCompany().getCompanyId()));

		return compList;
	}

	public List<CompanyDetail> getAllowedCompanies(String[] excludeIds) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		//if no multi company support, just return the one company
		if (!ArahantSession.multipleCompanySupport) {
			List<CompanyDetail> ret = new ArrayList<CompanyDetail>();
			ret.add(hsu.getFirst(CompanyDetail.class));
			return ret;
		}
		//find out what companies they are allowed in
		List<CompanyDetail> compList = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, getPersonId()).notIn(OrgGroupAssociation.ORG_GROUP_ID, excludeIds).setMaxResults(50).list();


		//add any from the meta groups
		for (OrgGroup og : hsu.createCriteriaNoCompanyFilter(OrgGroup.class).setMaxResults(50).isNull(OrgGroup.OWNINGCOMPANY).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSONID, getPersonId()).list()) {
			BOrgGroup borg = new BOrgGroup(og);
			compList.addAll(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).setMaxResults(50).joinTo(CompanyDetail.ORGGROUPS).in(OrgGroup.ORGGROUPID, borg.getAllOrgGroupsInHierarchy()).list());
		}

		//TODO: check if they are vendor which companies they are allowed in

		if (person.getOrgGroupType() == CLIENT_TYPE) {
			compList.add(new BClientCompany(person.getCompanyBase().getOrgGroupId()).getAssociatedCompany());

			if (compList.isEmpty())
				throw new ArahantWarning("No associated company detected.");
		}

		if (person.getOrgGroupType() == VENDOR_TYPE) {
			compList.add(new BVendorCompany(person.getCompanyBase().getOrgGroupId()).getAssociatedCompany());

			if (compList.isEmpty())
				throw new ArahantWarning("No associated company detected.");
		}

		if (person.getOrgGroupType() == AGENT_TYPE) {
			BAgent agent = new BAgent(person.getPersonId());

			for (BCompany comp : agent.getAgentCompanies())
				compList.add(comp.getBean());

			//if he's a supervisor, add choices from subordinates
			List<OrgGroup> orgs = hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.PERSON, person).list();

			List<String> orgIds = new LinkedList<String>();
			for (OrgGroup org : orgs)
				orgIds.addAll(new BOrgGroup(org).getAllOrgGroupsInHierarchy());

			compList.addAll(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).joinTo(CompanyDetail.AGENT_JOINS).joinTo(AgentJoin.AGENT).joinTo(Agent.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds).list());

			if (compList.isEmpty())
				throw new ArahantException("No associated company detected.");
		}

		//Arahant gets everybody
		if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ACCESS_LEVEL_WRITE)
			compList = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).notIn(CompanyDetail.COMPANY_ID, excludeIds).setMaxResults(50).list();

		//remove duplicates
		HashSet<CompanyDetail> set = new HashSet<>(compList);
		compList.clear();
		compList.addAll(set);

		Collections.sort(compList);

		//if the company list is empty, give them the owner
		if (compList.isEmpty())
			compList.add(hsu.get(CompanyDetail.class, getCompany().getCompanyId()));

		return compList;
	}

	public String getAutomotiveInsuranceCarrier() {
		return person.getAutoInsuranceCarrier();
	}

	public int getAutomotiveInsuranceStartDate() {
		return person.getAutoInsuranceStart();
	}

	public int getAutomotiveInsuranceExpirationDate() {
		return person.getAutoInsuranceExp();
	}

	public String getAutomotiveInsurancePolicyNumber() {
		return person.getAutoInsurancePolicy();
	}

	public String getAutomotiveInsuranceCoverage() {
		return person.getAutoInsuranceCoverage();
	}

	public double getBalance() {
		//add everything owed in invoices
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		double ret = 0;
		ret += hsu.createCriteria(InvoiceLineItem.class).sum(InvoiceLineItem.AMOUNT).joinTo(InvoiceLineItem.INVOICE).eq(Invoice.PERSON, person).doubleVal();

		//add all adjustments
		ret += hsu.createCriteria(Receipt.class).sum(Receipt.AMOUNT).eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).eq(Receipt.PERSON, person).doubleVal();

		//subract all reciepts

		ret -= hsu.createCriteria(Receipt.class).sum(Receipt.AMOUNT).ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT).eq(Receipt.PERSON, person).doubleVal();

		return ret;
	}

	public BAppointment[] getActiveAppointments(int date) {
		return BAppointment.makeArray(ArahantSession.getHSU().createCriteria(Appointment.class).orderBy(Appointment.TIME).eq(Appointment.DATE, date).eq(Appointment.STATUS, 'A').joinTo(Appointment.PERSON_JOINS).eq(AppointmentPersonJoin.PERSON, person).list());
	}

	public String getBankDraftBatchDescription() {
		if (person.getBankDraftBatches().isEmpty())
			return "";
		return person.getBankDraftBatches().iterator().next().getName();
	}

	public String getBankDraftBatchId() {
		if (person.getBankDraftBatches().isEmpty())
			return "";
		return person.getBankDraftBatches().iterator().next().getBankDraftId();
	}

	public int getBillingStatusDate() {
		HrBillingStatusHistory h = ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).orderByDesc(HrBillingStatusHistory.DATE).first();
		if (h == null)
			return 0;
		return h.getStartDate();
	}

	public String getBillingStatusId() {
		HrBillingStatusHistory h = ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).orderByDesc(HrBillingStatusHistory.DATE).first();
		if (h == null)
			return "";
		return h.getBillingStatus().getBillingStatusId();
	}

	public String getBillingStatusName() {
		HrBillingStatusHistory h = ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).orderByDesc(HrBillingStatusHistory.DATE).first();
		if (h == null)
			return "";
		return h.getBillingStatus().getName();
	}

	public String getCurrentSpouseOfSSN() {
		Employee emp = getEmployeeSpouse();
		if (emp == null)
			return "";
		return emp.getFname();
	}

	public BSpousalVerification[] getSpouseVerifications() {
		return BSpousalVerification.makeArray(ArahantSession.getHSU().createCriteria(SpousalVerification.class).eq(SpousalVerification.PERSON, person).orderByDesc(SpousalVerification.YEAR).list());
	}

	public boolean isAutoClockIn() {
		if (isEmployee())
			return getBEmployee().getClockInTimeLog();
		return false;
	}

	public boolean passwordExpired() {
		int expiresAfterDays = BProperty.getInt(StandardProperty.PasswordExpiresAfterDays);
		if (prophetLogin == null)
			return true;
		return DateUtils.addDays(prophetLogin.getPasswordEffectiveDate(), expiresAfterDays) < DateUtils.now();
	}

	public String punchIn() {
		String ret;
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		int startDate = DateUtils.now();
		int startTime = DateUtils.nowTime();
		int finalDate = 0;
		int finalTime = -1;

		//throw exception if this will be after an open timesheet
		Timesheet ts = hsu.createCriteria(Timesheet.class).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).eq(Timesheet.PERSON, person).first();

		if (ts != null && ts.getEndTime() == -1)
			if ((startDate > ts.getWorkDate() || (startDate == ts.getWorkDate() && startTime >= ts.getBeginningTime())))
				return "";
		if (ts != null
				&& (startDate < ts.getWorkDate() || startDate == ts.getWorkDate() && startTime <= ts.getBeginningTime()))
			return "";


		if (hsu.createCriteria(Timesheet.class).dateTimeSpanCompare(Timesheet.WORKDATE, Timesheet.BEGINNINGTIME, Timesheet.ENDDATE, Timesheet.ENDTIME, startDate,
				startTime, finalDate, finalTime).eq(Timesheet.PERSON, person).joinTo(Timesheet.PROJECTSHIFT).eq(ProjectShift.PROJECT, getDefaultProject()).exists())
			return "";
		final BTimesheet bc = new BTimesheet();
		ret = bc.create();
		bc.setPersonId(person.getPersonId());
		bc.setEndDate(finalDate);
		bc.setEndTime(finalTime);
		bc.setStartDate(startDate);
		bc.setStartTime(startTime);
		bc.setDescription("Auto clock in by Login");
		bc.setBeginningEntryDate(new Date());

		try {
			Project pr = getDefaultProject();
			bc.setBillable(pr.getBillable());
//  XXYY			bc.setProjectId(pr.getProjectId());
			if (true) throw new ArahantException("XXYY");
		} catch (Exception e) {
			throw new ArahantWarning("Please set up default project for this user to enable clock-in.");
		}

		if (finalTime != -1) {
			Date end = DateUtils.getDate(bc.getEndDate(), bc.getEndTime());
			Date start = DateUtils.getDate(bc.getStartDate(), bc.getStartTime());

			double timeDif = end.getTime() - start.getTime();
			timeDif = timeDif / 1000 / 60 / 60;

			bc.setTotalHours(timeDif);
		}
		bc.insert();
		return ret;
	}

	public void punchOut() {
		//TODO: handle punch out
		//find the last time for this guy for default project and set now in it
		Timesheet tso = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, person).joinTo(Timesheet.PROJECTSHIFT).eq(ProjectShift.PROJECT, getDefaultProject()).eq(Timesheet.ENDDATE, DateUtils.now()).eq(Timesheet.ENDTIME, -1).first();

		if (tso == null)
			return;
		BTimesheet ts = new BTimesheet(tso);

		ts.setEndDate(DateUtils.now());
		ts.setEndTime(DateUtils.nowTime());

		Date end = DateUtils.getDate(ts.getEndDate(), ts.getEndTime());
		Date start = DateUtils.getDate(ts.getStartDate(), ts.getStartTime());

		double timeDif = end.getTime() - start.getTime();
		timeDif = timeDif / 1000 / 60 / 60;

		ts.setTotalHours(timeDif);

		ts.update();
	}

	public void setSmoker(boolean smoker) {
		person.setSmoker(smoker ? 'Y' : 'N');
	}

	private Employee getEmployeeSpouse() {
		for (final HrEmplDependent dep : person.getDepJoinsWhereDependent())
			if (dep.getRelationshipType() == 'S' && dep.getDateInactive() == 0)
				return dep.getEmployee();
		return null;
	}

	public String getCurrentSpouseOfFirstName() {
		Employee emp = getEmployeeSpouse();
		if (emp == null)
			return "";
		return emp.getFname();
	}

	public String getCurrentSpouseOfLastName() {
		Employee emp = getEmployeeSpouse();
		if (emp == null)
			return "";
		return emp.getLname();
	}

	public int getDriversLicenseExpirationDate() {
		return person.getDriversLicenseExp();
	}

	public String getDriversLicenseNumber() {
		return person.getDriversLicenseNumber();
	}

	public String getDriversLicenseState() {
		return person.getDriversLicenseState();
	}

	public String getHicNumber() {
		return person.getHicNumber();
	}

	public void setHicNumber(String hicNumber) {
		person.setHicNumber(hicNumber);
	}

	public boolean getExistsInOtherBatch(String batchId) {
		return ArahantSession.getHSU().createCriteria(BankDraftBatch.class).ne(BankDraftBatch.BATCH_ID, batchId).joinTo(BankDraftBatch.PERSON).eq(Person.PERSONID, person.getPersonId()).exists();
	}

	public String getInheritedDefaultProjectId() {
		//check the org group
		if (person.getOrgGroupAssociations().size() > 0) {
			BOrgGroup borg = new BOrgGroup(person.getOrgGroupAssociations().iterator().next().getOrgGroup());
			return borg.getDefaultProjectId();
		}
		return "";
	}

	public String getOrgGroupId() {
		//just return some org group this person is in
		OrgGroup og = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).first();
		if (og == null)
			return "";
		return og.getOrgGroupId();
	}

	public String getTabaccoUse() {
		return person.getSmoker() + "";
	}

	public String getTermType() {
		return person.getStudentCalendarType() + "";
	}

	public String getAgreementAddressIp() {
		return person.getAgreementAddressIp();
	}

	public void setAgreementAddressIp(String agreementAddressIp) {
		if (agreementAddressIp.length() > 40)
			agreementAddressIp = agreementAddressIp.substring(0, 40);
		person.setAgreementAddressIp(agreementAddressIp);
	}

	public String getAgreementAddressUrl() {
		return person.getAgreementAddressUrl();
	}

	public void setAgreementAddressUrl(String agreementAddressUrl) {
		if (agreementAddressUrl.length() > 40)
			agreementAddressUrl = agreementAddressUrl.substring(0, 40);
		person.setAgreementAddressUrl(agreementAddressUrl);
	}

	public Date getAgreementDate() {
		return person.getAgreementDate();
	}

	public void setAgreementDate(Date agreementDate) {
		person.setAgreementDate(agreementDate);
	}

	public int getAgreementRevision() {
		return person.getAgreementRevision();
	}

	public void setAgreementRevision(int agreementRevision) {
		person.setAgreementRevision(agreementRevision);
	}

	public boolean hasCurrentBillingStatus() {
		return (ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).geOrEq(HrBillingStatusHistory.FINAL_DATE, DateUtils.now(), 0).exists());
	}

	public boolean isManager() {
		return ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').exists();
	}

	public void removeFromAIEngine() {
		person.removeFromAIEngine();
	}

	public void setBillingStatus(String billingStatusId, int billingStatusDate) {
		if (billingStatusDate == 0)
			return;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (billingStatusId.equals("CLEAR")) {
			billingStatusId = "";
			billingStatusDate = DateUtils.addDays(billingStatusDate, 1);
		}
		//if I already have this billing status, return
		if (hsu.createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).eq(HrBillingStatusHistory.DATE, billingStatusDate).joinTo(HrBillingStatusHistory.BILLING_STATUS).eq(HrBillingStatus.ID, billingStatusId).exists())
			return;

		//do I have one that is currently running?
		HrBillingStatusHistory hist = hsu.createCriteria(HrBillingStatusHistory.class).eq(HrBillingStatusHistory.PERSON, person).lt(HrBillingStatusHistory.DATE, billingStatusDate).orderByDesc(HrBillingStatusHistory.DATE).first();

		if (hist != null && (hist.getFinalDate() >= billingStatusDate || hist.getFinalDate() == 0)) {
			hist.setFinalDate(DateUtils.addDays(billingStatusDate, -1));
			hsu.saveOrUpdate(hist);
		}

		if (!isEmpty(billingStatusId)) {
			//now make the new one
			BBillingStatusHistory bsh = new BBillingStatusHistory();
			bsh.create();
			bsh.setPersonId(person.getPersonId());
			bsh.setBillingStatusId(billingStatusId);
			bsh.setStartDate(billingStatusDate);
			bsh.insert();
		}
	}

	public void setSortOrder(int order) {
		person.setSortOrder(order);
	}

	public void setStudent(boolean student) {
		person.setStudent(student ? 'Y' : 'N');
	}

	public void setTermType(String termType) {
		if (termType.trim().equals(""))
			termType = " ";
		person.setStudentCalendarType(termType.charAt(0));
	}

	void setPrimaryIndicator(final boolean primary) {
		this.primary = primary;
	}

	public void setTabaccoUse(String tabaccoUse) {
		if (isEmpty(tabaccoUse))
			person.setSmoker('U');
		else
			person.setSmoker(tabaccoUse.charAt(0));
	}

	public void setAutomotiveInsuranceCarrier(String automotiveInsuranceCarrier) {
		person.setAutoInsuranceCarrier(automotiveInsuranceCarrier);
	}

	public void setAutomotiveInsuranceStartDate(int automotiveInsuranceStartDate) {
		person.setAutoInsuranceStart(automotiveInsuranceStartDate);
	}

	public void setAutomotiveInsuranceExpirationDate(int automotiveInsuranceExpirationDate) {
		person.setAutoInsuranceExp(automotiveInsuranceExpirationDate);
	}

	public void setAutomotiveInsurancePolicyNumber(String automotiveInsurancePolicyNumber) {
		person.setAutoInsurancePolicy(automotiveInsurancePolicyNumber);
	}

	public void setAutomotiveInsuranceCoverage(String automotiveInsuranceCoverage) {
		person.setAutoInsuranceCoverage(automotiveInsuranceCoverage);
	}

	public void setDriversLicenseExpirationDate(int driversLicenseExpirationDate) {
		person.setDriversLicenseExp(driversLicenseExpirationDate);
	}

	public void setDriversLicenseNumber(String driversLicenseNumber) {
		person.setDriversLicenseNumber(driversLicenseNumber);
	}

	public void setDriversLicenseState(String driversLicenseState) {
		person.setDriversLicenseState(driversLicenseState);
	}

	public BPerson(final ProphetLogin login) throws ArahantException {
		internalLoad(login.getPersonId());
	}

	public void addPendingInsert(IDBFunctions i) {
		pendingInserts.add(i);
	}

	public void addPendingUpdate(IDBFunctions i) {
		pendingUpdates.add(i);
	}

	private void fixOwnership() {
		try {
			if (person.getCompanyBase() == null) {
				final OrgGroupAssociation oga = person.getOrgGroupAssociations().iterator().next();
				if (oga.getOrgGroup().getOwningCompany() != null)
					person.setCompanyBase(oga.getOrgGroup().getOwningCompany());
				else
					person.setCompanyBase(ArahantSession.getHSU().get(CompanyBase.class, oga.getOrgGroup().getOrgGroupId()));
			}
		} catch (final Exception e) {
			logger.warn("Can't fix ownership");
			//logger.error(e);
		}
	}

	private void internalLoad(final String key) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		logger.debug("Loading " + key);
		person = hsu.get(Employee.class, key);
		if (person == null)
			person = hsu.get(ClientContact.class, key);
		if (person == null)
			person = hsu.get(VendorContact.class, key);
		if (person == null)
			person = hsu.get(ProspectContact.class, key);
		if (person == null)
			person = hsu.get(Person.class, key);
		if (person == null)
			throw new ArahantException("ERROR: person not found." + " '" + key + "'");
		prophetLogin = hsu.get(ProphetLogin.class, key);
		//workPhone=getPhone(person, PHONE_WORK);
		//workFax=getPhone(person, PHONE_FAX);
		//homePhone=getPhone(person, PHONE_HOME);
		//cellPhone=getPhone(person, PHONE_CELL);
		initMembers();
		fixOwnership();
	}
	
	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void loadCurrent(final String user) {
		person = getCurrentPerson(user);
		prophetLogin = person.getProphetLogin();
		//workPhone=getPhone(person, PHONE_WORK);
		//workFax=getPhone(person, PHONE_FAX);
		//homePhone=getPhone(person, PHONE_HOME);
		//cellPhone=getPhone(person, PHONE_CELL);

		fixOwnership();
	}

	public static BPerson getCurrent() {
		return new BPerson(ArahantSession.getCurrentPerson());
	}

	/**
	 * Creates the Person record, ProphetLogin record, and home address record.
	 *
	 * @return
	 * @throws ArahantException
	 */
	@Override
	public String create() throws ArahantException {
		person = new Person();
		person.generateId();

		this.createOther();

		return getPersonId();
	}

	/**
	 * Creates the login and home address records.
	 *
	 */
	protected void createOther() throws ArahantException {
		prophetLogin = new ProphetLogin();
		prophetLogin.setPerson(person);
		prophetLogin.setPasswordEffectiveDate(DateUtils.now());
		prophetLogin.generateId();

		homeAddress = new Address();
		homeAddress.setAddressId(IDGenerator.generate(homeAddress));
		homeAddress.setPerson(person);
		homeAddress.setAddressType(ADDR_HOME);
	}

	private void initMembers() throws ArahantException {
		/*
		 * homeAddress=getAddress(person, ADDR_HOME); if (homeAddress==null) {
		 * homeAddress=new Address();
		 * homeAddress.setAddressId(IDGenerator.generate(homeAddress));
		 * homeAddress.setPerson(person); homeAddress.setAddressType(ADDR_HOME);
		 * hsu.insert(homeAddress); }
		 *
		 */
	}

	@Override
	public void insert() throws ArahantException {
		//Do some last minute checks
		if (person == null)
			return;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (person.getRecordType() == 'C') {
			if (person.getCompanyBase() == null)
				person.setCompanyBase(hsu.getCurrentCompany());
			person.setOrgGroupType(COMPANY_TYPE);
			hsu.insert(person);
		} else {
			saveChecks();

			person.setRecordType('R');

			if (getOrgGroupType() == 0)
				setOrgGroupType(COMPANY_TYPE);

			//must have a company
			if (person.getCompanyBase() == null)
				person.setCompanyBase(hsu.getCurrentCompany());

			hsu.insert(person);

			if (prophetLogin != null && !isEmpty(prophetLogin.getUserLogin()) && prophetLogin.getScreenGroup() != null) {
				prophetLogin.setPersonId(getPersonId());
				prophetLogin.setPerson(person);
				hsu.insert(prophetLogin);
			}

			if (workFax != null)
				hsu.insert(workFax);
			if (workPhone != null)
				hsu.insert(workPhone);
			if (cellPhone != null)
				hsu.insert(cellPhone);
			if (homePhone != null)
				hsu.insert(homePhone);

			if (homeAddress != null && isEmpty(homeAddress.getCountry()))
				homeAddress.setCountry("US");

			if (workAddress != null && isEmpty(workAddress.getCountry()))
				workAddress.setCountry("US");

			hsu.insert(homeAddress);
			hsu.insert(workAddress);

			for (IDBFunctions i : pendingInserts)
				i.insert();
			for (IDBFunctions i : pendingUpdates)
				i.update();
		}
	}

	public void insertNoChecks() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (getOrgGroupType() == 0)
			setOrgGroupType(COMPANY_TYPE);

		//must have a company
		if (person.getCompanyBase() == null)
			person.setCompanyBase(hsu.getCurrentCompany());

		hsu.insert(person);

		if (prophetLogin != null && !isEmpty(prophetLogin.getUserLogin()) && prophetLogin.getScreenGroup() != null) {
			prophetLogin.setPersonId(getPersonId());
			prophetLogin.setPerson(person);
			hsu.insert(prophetLogin);
		}

		if (workFax != null)
			hsu.insert(workFax);
		if (workPhone != null)
			hsu.insert(workPhone);
		if (cellPhone != null)
			hsu.insert(cellPhone);
		if (homePhone != null)
			hsu.insert(homePhone);

		if (homeAddress != null && isEmpty(homeAddress.getCountry()))
			homeAddress.setCountry("US");

		if (workAddress != null && isEmpty(workAddress.getCountry()))
			workAddress.setCountry("US");

		hsu.insert(homeAddress);

		hsu.insert(workAddress);

		for (IDBFunctions i : pendingInserts)
			i.insert();
		for (IDBFunctions i : pendingUpdates)
			i.update();
	}

	private void saveChecks() throws ArahantWarning {
		if (isEmpty(person.getLname()) || isEmpty(person.getFname()))
			throw new ArahantWarning("Name is required!");

		String ssn = person.getUnencryptedSsn();
		String encryptedSsn = person.getSsn();

		if (!isEmpty(ssn)) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			Connection db = KissConnection.get();
			try {
				Record rec = db.fetchOne("select person_id, fname, lname from person where person_id <> ? and (ssn=? or ssn=?)", person.getPersonId(), ssn, encryptedSsn);
				if (rec != null)
					throw new ArahantWarning("Social Security Number must be unique.  There is already a person in the system with that number. (Person ID = " + person.getPersonId() + " / SSN = " + ssn + ")  Be sure to check applicants.  (The pre-existing person is " + rec.get("fname") + " " + rec.get("lname") + ")");
			} catch (Exception throwables) {
				throw new ArahantException(throwables);
			}
		}

		if (person.getProphetLogin() != null && ArahantSession.getHSU().createCriteriaNoCompanyFilter(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, person.getProphetLogin().getUserLogin()).ne(ProphetLogin.PERSONID, person.getPersonId()).exists())
			throw new ArahantWarning("Selected Login ID is already in use.  Please choose another and re-save.");
	}
	
	@Override
	public void update() throws ArahantException {
		if (person == null)
			return;

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (person.getRecordType() == 'C') {
			//super.update();

			for (AddressPending ap : addresses.values())
				hsu.saveOrUpdate(ap);

			for (PhonePending pp : phones.values())
				hsu.saveOrUpdate(pp);

			hsu.saveOrUpdate(pcr);

			/*  need a shift  XXYY
			BProject proj = new BProject(getChangeRequest().getProject());
			proj.setDetailDesc(getDescription());
			proj.addComment(BPerson.getCurrent(), getDescription(), true);
			proj.update();
			 */
		} else {
			saveChecks();

			hsu.saveOrUpdate(person);
			if (prophetLogin != null && !isEmpty(prophetLogin.getUserLogin())) {
				prophetLogin.setPersonId(getPersonId());
				prophetLogin.setPerson(person);
				hsu.saveOrUpdate(prophetLogin);
			}
			if (workFax != null)
				hsu.saveOrUpdate(workFax);
			if (workPhone != null)
				hsu.saveOrUpdate(workPhone);
			if (cellPhone != null)
				hsu.saveOrUpdate(cellPhone);
			if (homePhone != null)
				hsu.saveOrUpdate(homePhone);
/*
			if (prophetLogin != null && prophetLogin.getScreenGroup() == null) {
				hsu.delete(prophetLogin);
				prophetLogin = null;
			}
*/
			hsu.saveOrUpdate(homeAddress);
			hsu.saveOrUpdate(workAddress);

			for (IDBFunctions i : pendingInserts)
				i.insert();
			for (IDBFunctions i : pendingUpdates)
				i.update();
		}
	}

	@Override
	public void delete() throws ArahantDeleteException {
		if (person.getRequestedProjects() != null && person.getRequestedProjects().size() > 0)
			throw new ArahantDeleteException("Can't delete person with projects associated.");
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		hsu.delete(person.getMessagesForFromPersonId());

		//delete address and phones
		List<ArahantBean> deletes = new LinkedList<ArahantBean>();
		deletes.addAll(person.getAddresses());
		deletes.addAll(person.getPhones());
		person.getAddresses().clear();
		person.getPhones().clear();

		hsu.delete(deletes);

		hsu.delete(person.getOrgGroupAssociations());
		hsu.delete(person.getQuestionDetails());
		hsu.delete(prophetLogin);
		hsu.delete(person.getEmployeeChanges());
		hsu.delete(person.getStudentVerifications());
		
		List<PasswordHistory> passhist = hsu.createCriteria(PasswordHistory.class).eq(PasswordHistory.PERSON, person).list();
		hsu.delete(passhist);
				
		hsu.delete(person);
	}

	public void setNewPassword(final String password) throws ArahantException {
		if (prophetLogin != null) {
			//see if we are doing old checks
			int reuseDays = BProperty.getInt(StandardProperty.PasswordReuseDays, 0);

			String newPass = encryptPassword(password);

			if (reuseDays > 0) {
				int dt = DateUtils.add(DateUtils.now(), -reuseDays);
				if (ArahantSession.getHSU().createCriteria(PasswordHistory.class).eq(PasswordHistory.PERSON, person).eq(PasswordHistory.PASSWORD, newPass).gt(PasswordHistory.RETIRED_DATE, dt).exists())
					throw new ArahantWarning("You may not reuse that password yet.  It's retire date is still too recent.");
			}

			PasswordHistory pwHist = new PasswordHistory();
			pwHist.generateId();
			pwHist.setDateRetired(DateUtils.now());
			pwHist.setPerson(person);
			pwHist.setUserPassword(prophetLogin.getUserPassword());
			ArahantSession.getHSU().insert(pwHist);
			prophetLogin.setUserPassword(newPass);
			prophetLogin.setPasswordEffectiveDate(DateUtils.now());
		}
	}

	public void assignPhone(final String phoneId) {
		final Phone a = ArahantSession.getHSU().get(Phone.class, phoneId);
		a.setPerson(person);
		ArahantSession.getHSU().saveOrUpdate(a);
	}

	/**
	 * @return @see com.arahant.beans.Person#getAddresses()
	 */
	public Set<Address> getAddresses() {
		return person.getAddresses();
	}

	/**
	 * @return @see com.arahant.beans.Person#getClientContacts()
	 *
	 * public Set getClientContacts() { return person.getClientContacts(); }
	 *
	 * /
	 **
	 * @return
	 * @see com.arahant.beans.Person#getCompany()
	 */
//	public Company getCompany() {
//		return person.getCompany();
//	}
	/**
	 * @return @see com.arahant.beans.Person#getLname()
	 */
	public String getLastName() {
		return person.getLname();
	}

	public String getFirstName() {
		return person.getFname();
	}

	public String getMiddleName() {
		return person.getMname();
	}

	public String getMiddleInitial() {
		if (!StringUtils.isEmpty(person.getMname()))
			return person.getMname().charAt(0) + "";
		else
			return "";
	}

	public String getNickName() {
		return person.getNickName();
	}

	/**
	 * @return @see com.arahant.beans.Person#getMessagesForFromPersonId()
	 */
	public Set getMessagesForFromPersonId() {
		return person.getMessagesForFromPersonId();
	}

	/**
	 * @return @see com.arahant.beans.Person#getMessagesForToPersonId()
	 */
	public Set getMessagesForToPersonId() {
		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		final HashSet<Message> ret = new HashSet<>();
		try {
			final List<Record> recs = db.fetchAll("select message_id from message m " +
					"join message_to to " +
					"  on m.message_id=to.message_id " +
					"where to.to_person_id=?", person.getPersonId());
			for (Record rec : recs)
				ret.add((new BMessage(rec.getString(""))).getBean());
			return ret;
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	/**
	 * @return @see com.arahant.beans.Person#getOrgGroupAssociations()
	 */
	public Set<OrgGroupAssociation> getOrgGroupAssociations() {
		return person.getOrgGroupAssociations();
	}
	
	/**
	 * Gets the set of BOrgGroup's the person is in 
	 * 
	 * @return 
	 */
	public Set<BOrgGroup> getOrgGroups() {
		Set<OrgGroupAssociation> ogas = getOrgGroupAssociations();
		HashSet<BOrgGroup> ret = new HashSet<BOrgGroup>();
		for (OrgGroupAssociation oga : ogas)
			ret.add(new BOrgGroup(oga.getOrgGroup()));
		return ret;
	}
	/**
	 * Gets the set of BOrgGroup's in which the person is supervisor 
	 * 
	 * @return 
	@SuppressWarnings("unchecked")
	public static List<String> getAllOrgGroupParents(List<String> orgGroupIds) {
		List<String> ret = new LinkedList<String>();

		if (orgGroupIds.isEmpty())
			return ret;

		ret.addAll(orgGroupIds);

		ret.addAll(getAllOrgGroupParents((List<String>) (List) ArahantSession.getHSU().createCriteria(OrgGroupHierarchy.class).selectFields(OrgGroupHierarchy.PARENT_ID).in(OrgGroupHierarchy.CHILD_ID, orgGroupIds).list()));

		return ret;
	}
	 */
	public List<String> getSupvOrgGroups() {
		Set<OrgGroupAssociation> ogas = getOrgGroupAssociations();
		List<String> ret = new LinkedList<String>();
		for (OrgGroupAssociation oga : ogas)
			if (oga.getPrimaryIndicator() == 'Y') {
				ret.add(oga.getOrgGroup().getOrgGroupId());
				break;
			}
		return ret;
	}

	/**
	 * @return @see com.arahant.beans.Person#getOrgGroupType()
	 */
	public int getOrgGroupType() {
		return person.getOrgGroupType();
	}

	/**
	 * @return @see com.arahant.beans.Person#getPersonalEmail()
	 */
	public String getPersonalEmail() {
		return person.getPersonalEmail();
	}

	/**
	 * @return @see com.arahant.beans.Person#getPersonId()
	 */
	public String getPersonId() {
		return person.getPersonId();
	}

	/**
	 * @return @see com.arahant.beans.Person#getPhones()
	 */
	public Set<Phone> getPhones() {
		return person.getPhones();
	}

	/**
	 * @return @see com.arahant.beans.Person#getProjectComments()
	 */
	public Set getProjectComments() {
		return person.getProjectComments();
	}

	/**
	 * @return @see com.arahant.beans.Person#getProjects()
	 */
	public Set getProjects() {
		return person.getRequestedProjects();
	}

	/**
	 * @return @see com.arahant.beans.Person#getProphetLogins()
	 */
	public ProphetLogin getProphetLogin() {
		return person.getProphetLogin();
	}

	/**
	 * @return @see com.arahant.beans.Person#getStandardProjects()
	 */
	public Set getStandardProjects() {
		return person.getRequestedStandardProjects();
	}

	/**
	 * @return @see com.arahant.beans.Person#getTitle()
	 */
	/*
	 * public String getTitle() { return person.getTitle(); }
	 */
	public String getJobTitle() {
		return person.getTitle();
	}

	/**
	 * @param fname
	 * @see com.arahant.beans.Person#setFname(java.lang.String)
	 */
	public void setFirstName(final String fname) {
		person.setFname(fname);
	}

	/**
	 * @param lname
	 * @see com.arahant.beans.Person#setLname(java.lang.String)
	 */
	public void setLastName(final String lname) {
		person.setLname(lname);
	}

	/**
	 * @param messagesForFromPersonId
	 * @see com.arahant.beans.Person#setMessagesForFromPersonId(java.util.Set)
	 */
	public void setMessagesForFromPersonId(final Set<Message> messagesForFromPersonId) {
		person.setMessagesForFromPersonId(messagesForFromPersonId);
	}

	/**
	 * @param orgGroupAssociations
	 * @see com.arahant.beans.Person#setOrgGroupAssociations(java.util.Set)
	 */
	public void setOrgGroupAssociations(final Set<OrgGroupAssociation> orgGroupAssociations) {
		person.setOrgGroupAssociations(orgGroupAssociations);
	}

	/**
	 * @param orgGroupType
	 * @see com.arahant.beans.Person#setOrgGroupType(int)
	 */
	public void setOrgGroupType(final int orgGroupType) {
		person.setOrgGroupType(orgGroupType);
	}

	/**
	 * @param personalEmail
	 * @see com.arahant.beans.Person#setPersonalEmail(java.lang.String)
	 */
	public void setPersonalEmail(final String personalEmail) {
		person.setPersonalEmail(personalEmail);
	}

	/**
	 * @param personId
	 * @see com.arahant.beans.Person#setPersonId(java.lang.String)
	 */
	public void setPersonId(final String personId) {
		person.setPersonId(personId);
		if (prophetLogin != null)
			prophetLogin.setPersonId(personId);
	}

	/**
	 * @param phones
	 * @see com.arahant.beans.Person#setPhones(java.util.Set)
	 */
	public void setPhones(final Set<Phone> phones) {
		person.setPhones(phones);
	}

	/**
	 * @param projectComments
	 * @see com.arahant.beans.Person#setProjectComments(java.util.Set)
	 */
	public void setProjectComments(final Set<ProjectComment> projectComments) {
		person.setProjectComments(projectComments);
	}

	public void setProjects(final Set<Project> projects) {
		person.setRequestedProjects(projects);
	}

	public void setRequestedStandardProjects(final Set<StandardProject> standardProjects) {
		person.setRequestedStandardProjects(standardProjects);
	}

	/**
	 * @param timeRejects
	 * @see com.arahant.beans.Person#setTimeRejects(java.util.Set)
	 */
	public void setTimeRejects(final Set<TimeReject> timeRejects) {
		person.setTimeRejects(timeRejects);
	}

	/**
	 * @param timesheets
	 * @see com.arahant.beans.Person#setTimesheets(java.util.Set)
	 */
	public void setTimesheets(final Set<Timesheet> timesheets) {
		person.setTimesheets(timesheets);
	}

//	public void setTitle(final String title) {
//		person.setJobTitle(jobTitle);
//	}
	public void setJobTitle(final String jobTitle) {
		person.setTitle(jobTitle);
	}

	public void setCitizenship(final String citizenship) {
		person.setCitizenship(citizenship);
	}

	public void setVisa(final String visa) {
		person.setVisa(visa);
	}

	public void setVisaStatusDate(final int visaStatusDate) {
		person.setVisaStatusDate(visaStatusDate);
	}

	public void setVisaExpirationDate(final int visaExpirationDate) {
		person.setVisaExpirationDate(visaExpirationDate);
	}

	public void setI9Part1(final boolean i9Completed) {
		person.setI9Part1(i9Completed ? 'Y' : 'N');
	}

	public void setI9Part2(final boolean i9Completed) {
		person.setI9Part2(i9Completed ? 'Y' : 'N');
	}

	@Override
	public String toString() {
		return person.toString();
	}

	public char getCanLogin() {
		try {
			if (prophetLogin == null)
				return 'N';
			return prophetLogin.getCanLogin();
		} catch (final Exception e) {
			return 'N';
		}
	}

	public String getUserLogin() {
		if (prophetLogin == null)
			return "";
		return prophetLogin.getUserLogin();
	}

	/**
	 * Returns an unencrypted version of the users password.
	 * 
	 * @return unencrypted version of the users password
	 */
	public String getUserPassword() throws ArahantException {
		if (prophetLogin == null)
			return "";
		String pw = prophetLogin.getUserPassword();
		if (pw == null  ||  pw.length() < 16  ||  !pw.matches("[0-9a-f]+"))
			return pw;  // the password is unencrypted

		try {
			pw = Crypto.decryptTripleDES(pw);
			if (pw != null) {
				int len = pw.length();
				if (len > 3  &&  pw.charAt(len-3) == '~'  &&  Character.isDigit(pw.charAt(len-2))  &&  Character.isDigit(pw.charAt(len-1)))
					pw = pw.substring(0, len-3);  //  remove the salt
			}
			return pw;
		} catch (Exception e) {
			// RKK - decrypt throws exception now, so to keep same behavior for non-encrypted passwords, do this
			return prophetLogin.getUserPassword();
		}
	}

	public static String decryptPassword(String pw) {
		if (pw == null  ||  pw.length() < 16  || !pw.matches("[0-9a-f]+"))
			return pw;  // the password is unencrypted already
		try {
			String pw2 = Crypto.decryptTripleDES(pw);
			if (pw2 != null) {
				int len = pw2.length();
				if (len > 3 && pw2.charAt(len - 3) == '~' && Character.isDigit(pw2.charAt(len - 2)) && Character.isDigit(pw2.charAt(len - 1)))
					pw = pw2.substring(0, len - 3);  //  remove the salt
			}
		} catch (Exception e) {
			// ignore
		}
		return pw;
	}

	public String getPersonGuid() {
		return person.getPersonGuid();
	}

	public void setPersonGuid(String personGuid) {
		person.setPersonGuid(personGuid);
	}

	/**
	 * @param canLogin
	 * @see com.arahant.beans.ProphetLogin#setCanLogin(java.lang.Character)
	 */
	public void setCanLogin(final Character canLogin) {
		if (checkLogin(canLogin.toString().trim()))
			prophetLogin.setCanLogin(canLogin);
	}

	/**
	 * @param userLogin
	 * @throws ArahantException
	 * @see com.arahant.beans.ProphetLogin#setUserLogin(java.lang.String)
	 */
	public void setUserLogin(final String userLogin) throws ArahantException {
		if (userLogin == null  ||  userLogin.isEmpty()) {
			if (prophetLogin == null)
				return;
			if ("00001-0000000000".equals(person.getPersonId()))
				throw new ArahantException("Can't blank out " + ArahantSession.systemName() + " login!");
			prophetLogin.setUserLogin(userLogin);
		} else if (checkLogin(userLogin)) {
			if (!userLogin.equals(prophetLogin.getUserLogin()))
				if (person.getProphetLogin() != null && ArahantSession.getHSU().createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, userLogin).ne(ProphetLogin.PERSONID, person.getPersonId()).exists())
					throw new ArahantWarning("Selected Login ID is already in use.  Please choose another and re-save.");

			prophetLogin.setUserLogin(userLogin);
		}
	}

	/**
	 * @param userPassword
	 * @see com.arahant.beans.ProphetLogin#setUserPassword(java.lang.String)
	 */
	public void setUserPassword(final String userPassword, boolean canLogin) throws ArahantException {
		setUserPassword(userPassword, canLogin, false);
	}

	public void setUserPassword(final String userPassword, boolean canLogin, boolean skipValidation) throws ArahantException {
		setCanLogin(canLogin ? 'Y' : 'N');

		BProphetLogin pl = new BProphetLogin();

		if (isEmpty(userPassword)) //empty means keep what is in the database
			return;

		//make sure the password passes the rules before setting it
		if (person.getProphetLogin() != null && person.getProphetLogin().getCanLogin() == 'Y' && !skipValidation)
			pl.validatePassword(userPassword);

		if (checkLogin(userPassword))
			try {
				if (!skipValidation)
					pl.validatePassword(userPassword);
				prophetLogin.setUserPassword(encryptPassword(userPassword));
			} catch (Exception e) {
				throw new ArahantException(e);
			}
	}
	
	/**
	 * This method is used to encrypt user login passwords.  It does not assign it to anything.  It just performs the encryption and returns it.
	 * 
	 * @param pw the unencrypted password
	 * @return the encrypted password
	 * @throws Exception 
	 */
	public String encryptPassword(String pw) {
		return encryptPassword(getPersonId(), pw);
	}

	public static String encryptPassword(String id, String pw) {
		try {
			if (id == null  ||  id.length() != 16) {
				logger.error(new Exception("Unable to salt the user password because there is no person context."));
				return Crypto.encryptTripleDES(pw);
			}
			return Crypto.encryptTripleDES(pw + "~" + id.substring(14));  // add salt
		} catch (Exception e) {
			throw new ArahantException("Unable to salt the user password because there is no person context.", e);
		}
	}

	public boolean isPrimary() {
		return primary;
	}

	public boolean isPrimary(String orgGroupId) {
		return ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).exists();
	}

	public String getWorkPhoneNumber() {
		try {
			return getPhone(person, PHONE_WORK).getPhoneNumber();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getWorkFaxNumber() {
		try {
			return getPhone(person, PHONE_FAX).getPhoneNumber();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getScreenGroupId() {
		try {
			return prophetLogin.getScreenGroup().getScreenGroupId();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getScreenGroupName() {
		try {
			return prophetLogin.getScreenGroup().getName();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getScreenGroupExtId() {
		try {
			final BScreenGroup group = new BScreenGroup(prophetLogin.getScreenGroup());

			return group.getExtId();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	private boolean checkWorkPhone(final String number) throws ArahantException {
		if (workPhone == null)
			workPhone = getPhone(person, PHONE_WORK);
		if (workPhone == null)
			if (!isEmpty(number)) {
				workPhone = new Phone();
				workPhone.generateId();
				workPhone.setPerson(person);
				workPhone.setPhoneType(PHONE_WORK);
				person.getPhones().add(workPhone);
			} else
				return false;
		return true;
	}

	private boolean checkWorkFax(final String number) throws ArahantException {
		if (workFax == null)
			workFax = getPhone(person, PHONE_FAX);
		if (workFax == null)
			if (!isEmpty(number)) {
				workFax = new Phone();
				workFax.generateId();
				workFax.setPerson(person);
				workFax.setPhoneType(PHONE_FAX);
				person.getPhones().add(workFax);
			} else
				return false;
		return true;
	}

	private boolean checkHomePhone(final String number) throws ArahantException {
		if (homePhone == null)
			homePhone = getPhone(person, PHONE_HOME);
		if (homePhone == null)
			if (!isEmpty(number)) {
				homePhone = new Phone();
				homePhone.generateId();
				homePhone.setPerson(person);
				homePhone.setPhoneType(PHONE_HOME);
			} else
				return false;
		return true;
	}

	private boolean checkCellPhone(final String number) throws ArahantException {
		if (cellPhone == null)
			cellPhone = getPhone(person, PHONE_CELL);
		if (cellPhone == null)
			if (!isEmpty(number)) {
				cellPhone = new Phone();
				cellPhone.generateId();
				cellPhone.setPerson(person);
				cellPhone.setPhoneType(PHONE_CELL);
			} else
				return false;
		return true;
	}

	private boolean checkLogin(final String val) {
		if (prophetLogin == null)
			if (!isEmpty(val)) {
				prophetLogin = new ProphetLogin();
				prophetLogin.setPersonId(person.getPersonId());
				prophetLogin.setPerson(person);
				prophetLogin.setPasswordEffectiveDate(DateUtils.now());
			} else
				return false;
		return true;
	}

	public void setWorkPhone(final String phoneNumber) throws ArahantException {
		if (checkWorkPhone(phoneNumber))
			workPhone.setPhoneNumber(phoneNumber);
	}

	public void setWorkFax(final String phoneNumber) throws ArahantException {
		if (checkWorkFax(phoneNumber))
			workFax.setPhoneNumber(phoneNumber);
	}

	public void setScreenGroupId(final String screenGroupId) {
		if (checkLogin(screenGroupId))
			prophetLogin.setScreenGroup(ArahantSession.getHSU().get(ScreenGroup.class, screenGroupId));
	}

	public void setNoCompanyScreenGroupId(final String noCompanyScreenGroupId) {
		if (checkLogin(noCompanyScreenGroupId))
			prophetLogin.setNoCompanyScreenGroup(ArahantSession.getHSU().get(ScreenGroup.class, noCompanyScreenGroupId));
	}

	public String getNoCompanyScreenGroupName() {
		try {
			return prophetLogin.getNoCompanyScreenGroup().getName();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getNoCompanyScreenGroupExtId() {
		try {
			final BScreenGroup group = new BScreenGroup(prophetLogin.getNoCompanyScreenGroup());

			return group.getExtId();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getNoCompanyScreenGroupId() {
		try {
			return prophetLogin.getNoCompanyScreenGroup().getScreenGroupId();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getDrugAlcoholAbuse() {
		return person.getDrugAlcoholAbuse() + "";
	}

	public void setDrugAlcoholAbuse(char drugAlcoholAbust) {
		person.setDrugAlcoholAbuse(drugAlcoholAbust);
	}

	public void setDrugAlcoholAbuse(String drugAlcoholAbust) {
		person.setDrugAlcoholAbuse(drugAlcoholAbust.charAt(0));
	}

	public void setDrugAlcoholAbuse(boolean drugAlcoholAbust) {
		person.setDrugAlcoholAbuse(drugAlcoholAbust ? 'Y' : 'N');
	}

	public String getNotMissedFiveDays() {
		return person.getNotMissedFiveDays() + "";
	}

	public void setNotMissedFiveDays(char notMissedFiveDays) {
		person.setNotMissedFiveDays(notMissedFiveDays);
	}

	public void setNotMissedFiveDays(String notMissedFiveDays) {
		person.setNotMissedFiveDays(notMissedFiveDays.charAt(0));
	}

	public void setNotMissedFiveDays(boolean notMissedFiveDays) {
		person.setNotMissedFiveDays(notMissedFiveDays ? 'Y' : 'N');
	}

	public String getReplaceEmployerPlan() {
		return person.getReplaceEmployerPlan() + "";
	}

	public void setReplaceEmployerPlan(char replaceEmployerPlan) {
		person.setReplaceEmployerPlan(replaceEmployerPlan);
	}

	public void setReplaceEmployerPlan(String replaceEmployerPlan) {
		person.setReplaceEmployerPlan(replaceEmployerPlan.charAt(0));
	}

	public void setReplaceEmployerPlan(boolean replaceEmployerPlan) {
		person.setReplaceEmployerPlan(replaceEmployerPlan ? 'Y' : 'N');
	}

	public String getTwoFamilyCancer() {
		return person.getTwoFamilyCancer() + "";
	}

	public void setTwoFamilyCancer(char twoFamilyCancer) {
		person.setTwoFamilyCancer(twoFamilyCancer);
	}

	public void setTwoFamilyCancer(String twoFamilyCancer) {
		person.setTwoFamilyCancer(twoFamilyCancer.charAt(0));
	}

	public void setTwoFamilyCancer(boolean twoFamilyCancer) {
		person.setTwoFamilyCancer(twoFamilyCancer ? 'Y' : 'N');
	}

	public String getTwoFamilyDiabetes() {
		return person.getTwoFamilyDiabetes() + "";
	}

	public void setTwoFamilyDiabetes(char twoFamilyDiabetes) {
		person.setTwoFamilyDiabetes(twoFamilyDiabetes);
	}

	public void setTwoFamilyDiabetes(String twoFamilyDiabetes) {
		person.setTwoFamilyDiabetes(twoFamilyDiabetes.charAt(0));
	}

	public void setTwoFamilyDiabetes(boolean twoFamilyDiabetes) {
		person.setTwoFamilyDiabetes(twoFamilyDiabetes ? 'Y' : 'N');
	}

	public String getTwoFamilyHeartCond() {
		return person.getTwoFamilyHeartCond() + "";
	}

	public void setTwoFamilyHeartCond(char twoFamilyHeartCond) {
		person.setTwoFamilyHeartCond(twoFamilyHeartCond);
	}

	public void setTwoFamilyHeartCond(String twoFamilyHeartCond) {
		person.setTwoFamilyHeartCond(twoFamilyHeartCond.charAt(0));
	}

	public void setTwoFamilyHeartCond(boolean twoFamilyHeartCond) {
		person.setTwoFamilyHeartCond(twoFamilyHeartCond ? 'Y' : 'N');
	}

	public String getHasOtherMedical() {
		return person.getHasOtherMedical() + "";
	}

	public void setHasOtherMedical(char hom) {
		person.setHasOtherMedical(hom);
	}

	public void setHasOtherMedical(String hom) {
		person.setHasOtherMedical(hom.charAt(0));
	}

	public void setHasOtherMedical(boolean twoFamilyHeartCond) {
		person.setHasOtherMedical(twoFamilyHeartCond ? 'Y' : 'N');
	}

	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {
		this.assignToOrgGroup(orgGroupId, isPrimary, 0, 0);
	}

	public void assignToOrgGroup(final String orgGroupId, final boolean isPrimary, final int startDate, final int finalDate) throws ArahantException {
		if (isEmpty(orgGroupId))
			throw new ArahantWarning("An org group is required");
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		OrgGroupAssociation oga = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).first();
		if (oga == null) {
			oga = new OrgGroupAssociation();
			final OrgGroupAssociationId ogi = new OrgGroupAssociationId();
			ogi.setOrgGroupId(orgGroupId);
			ogi.setPersonId(person.getPersonId());
			oga.setId(ogi);
			oga.setPerson(person);
			oga.setOrgGroup(hsu.get(OrgGroup.class, orgGroupId));
			oga.setOrgGroupType(getOrgGroupType());
			oga.linkToEngine();
		}
		oga.setPrimaryIndicator(isPrimary ? 'Y' : 'N');
		oga.setStartDate(startDate);
		oga.setFinalDate(finalDate);

		if (oga.getOrgGroup().getOwningCompany() != null)
			person.setCompanyBase(oga.getOrgGroup().getOwningCompany());
		else
			person.setCompanyBase(hsu.get(CompanyBase.class, oga.getOrgGroup().getOrgGroupId()));
		hsu.saveOrUpdate(oga);
	}

	public void assignToOrgGroupWithStatus(final String orgGroupId, final String statusId, final int statusDate, final String statusNote, final boolean isPrimary, final int startDate, final int finalDate) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (isEmpty(orgGroupId))
			throw new ArahantWarning("An org group is required");

		OrgGroupAssociation oga = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).first();
		if (oga == null) {
			oga = new OrgGroupAssociation();
			final OrgGroupAssociationId ogi = new OrgGroupAssociationId();
			ogi.setOrgGroupId(orgGroupId);
			ogi.setPersonId(person.getPersonId());
			oga.setId(ogi);
			oga.setPerson(person);
			oga.setOrgGroup(hsu.get(OrgGroup.class, orgGroupId));
			oga.setOrgGroupType(getOrgGroupType());
			oga.linkToEngine();
		}
		oga.setPrimaryIndicator(isPrimary ? 'Y' : 'N');
		oga.setStartDate(startDate);
		oga.setFinalDate(finalDate);

		final BEmployee be;
		be = new BEmployee(person.getPersonId());
		be.setStatusId(statusId, statusDate, statusNote);

		if (oga.getOrgGroup().getOwningCompany() != null)
			person.setCompanyBase(oga.getOrgGroup().getOwningCompany());
		else
			person.setCompanyBase(hsu.get(CompanyBase.class, oga.getOrgGroup().getOrgGroupId()));
		hsu.saveOrUpdate(oga);
	}

	public void assignToSingleOrgGroup(final String orgGroupId, final boolean isPrimary) throws ArahantException {
		// first ensure it is assigned an org group
		this.assignToOrgGroup(orgGroupId, isPrimary);

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (!AIProperty.getBoolean("AlwaysMultipleOrgGroups", getPersonId())) {
			// now remove any other org groups
			List<OrgGroupAssociation> assocs = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).joinTo(OrgGroupAssociation.ORGGROUP).ne(OrgGroup.ORGGROUPID, orgGroupId).list();

			for (OrgGroupAssociation assoc : assocs)
				hsu.delete(assoc);
		}
	}

	public String getHomePhone() {
		try {
			return getPhone(person, PHONE_HOME).getPhoneNumber();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getMobilePhone() {
		try {
			return getPhone(person, PHONE_CELL).getPhoneNumber();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkFax() {
		try {
			return getPhone(person, PHONE_FAX).getPhoneNumber();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkPhone() {
		try {
			return getPhone(person, PHONE_WORK).getPhoneNumber();
		} catch (final Exception e) {
			return "";
		}
	}

	public void setHomePhone(final String phoneNumber) throws ArahantException {
		if (checkHomePhone(phoneNumber))
			homePhone.setPhoneNumber(phoneNumber);
	}

	public void setMobilePhone(final String phoneNumber) throws ArahantException {
		if (checkCellPhone(phoneNumber))
			cellPhone.setPhoneNumber(phoneNumber);
	}

	public boolean canLogin() {
		if (prophetLogin == null)
			return false;
		return prophetLogin.getCanLogin().toString().trim().equals("Y");
	}

	public boolean hasMessages() {
//		check to see if user has any messages to him/her that have not been marked as deleted
		try {
			final Connection db = new Connection(ArahantSession.getHSU().getConnection());
			Record rec = db.fetchOne("select message_to_id from message_to where to_person_id = ? and to_show = 'Y'", person.getPersonId());
			return rec != null;
		} catch (final Exception e) {
			//forget it just go on
		}
		return false;
	}

	/**
	 *
	 */
	public BScreenOrGroup[] listTopLevelScreens() {
		final ProphetLogin log = prophetLogin;

		if (log == null || log.getUserLogin() == null)
			return new BScreenOrGroup[0];

		if (log.getScreenGroup() != null) {
			ProphetLoginOverride po = ArahantSession.getHSU().createCriteria(ProphetLoginOverride.class).eq(ProphetLoginOverride.PERSON, person).eq(ProphetLoginOverride.COMPANY, ArahantSession.getHSU().getCurrentCompany()).first();
			if (po != null)
				return new BScreenGroup(po.getScreenGroup()).listChildren();

			return new BScreenGroup(log.getScreenGroup()).listChildren();
		}
		return new BScreenOrGroup[0];
	}

	public BScreenOrGroup[] listTopLevelNoCompanyScreens() {
		final ProphetLogin log = prophetLogin;

		if (log == null || log.getUserLogin() == null)
			return new BScreenOrGroup[0];

		if (log.getScreenGroup() != null)
			return new BScreenGroup(log.getNoCompanyScreenGroup()).listChildren();

		return new BScreenOrGroup[0];
	}

	public BScreenGroup getTopLevelGroup() {
		final ProphetLogin log = prophetLogin;

		if (log == null || log.getUserLogin() == null)
			return null;

		ProphetLoginOverride po = ArahantSession.getHSU().createCriteria(ProphetLoginOverride.class).eq(ProphetLoginOverride.PERSON, person).eq(ProphetLoginOverride.COMPANY, ArahantSession.getHSU().getCurrentCompany()).first();
		if (po != null)
			return new BScreenGroup(po.getScreenGroup());

		return new BScreenGroup(log.getScreenGroup());
	}

	public BScreenGroup getTopLevelNoCompanyGroup() {
		final ProphetLogin log = prophetLogin;

		if (log == null || log.getUserLogin() == null)
			return null;

		if (log.getNoCompanyScreenGroup() != null)
			return new BScreenGroup(log.getNoCompanyScreenGroup());

		return null;
	}

	public static BPerson[] searchLogins(final HibernateSessionUtil hsu, final String user, final String lName, final String fName) throws ArahantException {
		return BPerson.makeArray(hsu.createCriteria(Person.class).like(Person.FNAME, fName).like(Person.LNAME, lName).orderBy(Person.LNAME).orderBy(Person.FNAME).setMaxResults(50).joinTo(Person.PROPHETLOGINS).like(ProphetLogin.USERLOGIN, user).list());
	}

	public static BPerson[] searchLogins2(final HibernateSessionUtil hsu, final String user, final String lName, final String fName, final String companyId) throws ArahantException {
		return BPerson.makeArray(hsu.createCriteria(Person.class).like(Person.FNAME, fName).like(Person.LNAME, lName).eq(Person.COMPANYBASE, new BCompany(companyId).getBean()).orderBy(Person.LNAME).orderBy(Person.FNAME).setMaxResults(50).joinTo(Person.PROPHETLOGINS).like(ProphetLogin.USERLOGIN, user).list());
	}

	public static BPerson[] search1(final HibernateSessionUtil hsu,
			final String lastName, final String firstName, final String companyId, final int max) throws ArahantException {

		final HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class);
		hcu.distinct();
		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);
		hcu.setMaxResults(max);

		if (!isEmpty(firstName))
			hcu.like(Person.FNAME, firstName);

		if (!isEmpty(lastName))
			hcu.like(Person.LNAME, lastName);

		if (!isEmpty(companyId))
			hcu.joinTo(Person.COMPANYBASE).eq(CompanyBase.ORGGROUPID, companyId);

		return BPerson.makeArray(hcu.list());
	}

	public static BSearchOutput<BPerson> searchScreenAndSecurityGroups(final BSearchMetaInput metaInput, final String fName, final String lName, final String screenGroupId, final String securityGroupId, final String companyId) throws ArahantException {
		final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteriaNoCompanyFilter(Person.class);

		hcu.like(Person.LNAME, lName);
		hcu.like(Person.FNAME, fName);

		hcu.eq(Person.ORGGROUPTYPE, COMPANY_TYPE);

		if (!isEmpty(companyId)) {
			BOrgGroup bo = new BOrgGroup(companyId);
			hcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, bo.getOrgGroupId());
		} else
			hcu.joinTo(Person.COMPANYBASE).orderBy(CompanyBase.NAME);

		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);

		final HibernateCriteriaUtil<Person> hcu2 = hcu.joinTo(Person.PROPHETLOGINS);

		if (!isEmpty(screenGroupId))
			hcu2.joinTo(ProphetLogin.SCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId);

		if (!isEmpty(securityGroupId))
			hcu2.joinTo(ProphetLogin.SECURITYGROUP).eq(SecurityGroup.SECURITYGROUPID, securityGroupId);

		return makeSearchOutput(metaInput, hcu); //makeArrayEx(hcu2.list());
	}

	public static BPerson[] searchScreenAndSecurityGroups(final String fName, final String lName, final String screenGroupId, final String securityGroupId, final String companyId) throws ArahantException {
		final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteriaNoCompanyFilter(Person.class);

		hcu.like(Person.LNAME, lName);
		hcu.like(Person.FNAME, fName);

		hcu.eq(Person.ORGGROUPTYPE, COMPANY_TYPE);

		if (!isEmpty(companyId)) {
			BOrgGroup bo = new BOrgGroup(companyId);
			hcu.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, bo.getOrgGroupId());
		} else
			hcu.joinTo(Person.COMPANYBASE).orderBy(CompanyBase.NAME);

		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);

		final HibernateCriteriaUtil<Person> hcu2 = hcu.joinTo(Person.PROPHETLOGINS);

		if (!isEmpty(screenGroupId))
			hcu2.joinTo(ProphetLogin.SCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId);

		if (!isEmpty(securityGroupId))
			hcu2.joinTo(ProphetLogin.SECURITYGROUP).eq(SecurityGroup.SECURITYGROUPID, securityGroupId);

		return makeArray(hcu2.list());
	}

	private static BPerson[] makeArray(final HibernateScrollUtil<Person> scr) throws ArahantException {
		List<Person> l = new ArrayList<Person>();
		while (scr.next())
			l.add(scr.get());
		return BPerson.makeArray(l);
	}

	public static BPerson[] makeArray(final Collection<Person> l) throws ArahantException {
		final BPerson[] ret = new BPerson[l.size()];
		int loop = 0;

		for (Person p : l)
			ret[loop++] = new BPerson(p);
		return ret;
	}

	public String getCompanyName() {
		if (person.getCompanyBase() != null)
			return person.getCompanyBase().getName();
		return "None";
	}

	public String getCompanyType() {
		String companyType = "";
		if (person.getCompanyBase() == null)
			return "None";

		if (person.getCompanyBase().getOrgGroupType() == ArahantConstants.COMPANY_TYPE)
			companyType = "Company";

		if (person.getCompanyBase().getOrgGroupType() == ArahantConstants.CLIENT_TYPE)
			companyType = "Client";

		if (person.getCompanyBase().getOrgGroupType() == ArahantConstants.VENDOR_TYPE)
			companyType = "Vendor";

		return companyType;
	}

	/**
	 * @param hsu
	 * @param name
	 * @param max
	 * @return
	 */
	public BOrgGroup[] searchSubordinateGroups(final HibernateSessionUtil hsu, final String name, final int max) {
		final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class);
		hcu.orderBy(OrgGroup.NAME);
		hcu.setMaxResults(max);

		if (!isEmpty(name))
			hcu.like(OrgGroup.NAME, name);

		List<OrgGroup> orgRes;

		final Iterator<OrgGroup> subGItr = getSubordinateGroups().iterator();

		final List<String> ids = new LinkedList<String>();

		while (subGItr.hasNext())
			ids.add(subGItr.next().getOrgGroupId());

		if (ids.size() > 0) {
			hcu.in(OrgGroup.ORGGROUPID, ids);

			orgRes = hcu.list();
		} else
			orgRes = new LinkedList<OrgGroup>();

		return BOrgGroup.makeArray(orgRes);
	}

	public BOrgGroup[] searchSubordinateGroups(final HibernateSessionUtil hsu, final String name, final int type, final int max) {
		final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class);
		hcu.orderBy(OrgGroup.NAME);
		hcu.setMaxResults(max);
		hcu.eq(OrgGroup.ORGGROUPTYPE, type);

		if (!isEmpty(name))
			hcu.like(OrgGroup.NAME, name);

		List<OrgGroup> orgRes;

		final Iterator<OrgGroup> subGItr = getSubordinateGroups().iterator();

		final List<String> ids = new LinkedList<String>();

		while (subGItr.hasNext())
			ids.add(subGItr.next().getOrgGroupId());

		if (ids.size() > 0) {
			hcu.in(OrgGroup.ORGGROUPID, ids);

			orgRes = hcu.list();
		} else
			orgRes = new LinkedList<OrgGroup>();

		return BOrgGroup.makeArray(orgRes);
	}

	@SuppressWarnings("unchecked")
	private Set<OrgGroup> getSubordinateGroups() {
		if (prophetLogin.getUserLogin().equals(ARAHANT_SUPERUSER)) {
			final Set<OrgGroup> s = new HashSet<OrgGroup>();
			s.addAll(ArahantSession.getHSU().getAll(OrgGroup.class));
			return s;
		}

		final Set<OrgGroup> ret = new HashSet<OrgGroup>();

		final Iterator ogaItr = person.getOrgGroupAssociations().iterator();

		while (ogaItr.hasNext()) {
			final OrgGroupAssociation oga = (OrgGroupAssociation) ogaItr.next();
			if (oga.getPrimaryIndicator() == 'Y')
				ret.addAll(getSubGroups(oga.getOrgGroup()));
			ret.add(oga.getOrgGroup());
		}
		return ret;
	}

	private Set<OrgGroup> getSubGroups(final OrgGroup og) {
		final Set<OrgGroup> ret = new HashSet<OrgGroup>();

		if (maxSubGroup++ > 1000) //abort after going down 1000
			return ret;

		final Iterator oghItr = og.getOrgGroupHierarchiesForParentGroupId().iterator();

		while (oghItr.hasNext()) {
			final OrgGroupHierarchy ogh = (OrgGroupHierarchy) oghItr.next();

			final OrgGroup child = ogh.getOrgGroupByChildGroupId();

			ret.add(child);

			ret.addAll(getSubGroups(child));
		}
		return ret;
	}

	public int getNextRejectedDate(final int date) {
		int dt = 0;

		TimeReject d = ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).gt(TimeReject.REJECTDATE, date).orderBy(TimeReject.REJECTDATE).first();

		if (d != null)
			dt = d.getRejectDate();
		else {
			d = ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).orderBy(TimeReject.REJECTDATE).first();
			if (d != null)
				dt = d.getRejectDate();
		}
		return dt;
	}

	/**
	 * @param date
	 * @return
	 */
	public int getPreviousRejectedDate(final int date) {
		int dt = 0;
		TimeReject d = ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).lt(TimeReject.REJECTDATE, date).orderByDesc(TimeReject.REJECTDATE).first();

		if (d != null)
			dt = d.getRejectDate();
		else {
			d = ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).orderByDesc(TimeReject.REJECTDATE).first();
			if (d != null)
				dt = d.getRejectDate();
		}
		return dt;
	}

	/**
	 * @param timesheetDate
	 * @return
	 */
	public BTimesheet[] listTimesheets(final long timesheetDate) {
		return BTimesheet.makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.WORKDATE, (int) timesheetDate).orderBy(Timesheet.BEGINNINGTIME).eq(Timesheet.PERSON, person).list());
	}

	public BTimesheet[] listTimesheetsForWeek(final long timesheetDate) {
		int fromDate;
		int toDate;

		Calendar c = DateUtils.getCalendar((int) timesheetDate);

		int d = c.get(Calendar.DAY_OF_MONTH);
		int m = c.get(Calendar.MONTH) + 1;
		int y = c.get(Calendar.YEAR);

		int date = y * 10000 + m * 100 + d;

		int day = c.get(Calendar.DAY_OF_WEEK);

		if (day == Calendar.SUNDAY) {
			fromDate = DateUtils.add(date, -6);
			toDate = date;
		} else {
			if (day != Calendar.MONDAY)
				day -= Calendar.MONDAY;
			else
				day = 0;

			date = DateUtils.add(date, -day);
			fromDate = date;

			date = DateUtils.add(date, 6);
			toDate = date;
		}
//		return BTimesheet.makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).orderBy(Timesheet.WORKDATE).eq(Timesheet.PERSON, person).list());
		return BTimesheet.makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateBetween(Timesheet.WORKDATE, fromDate, toDate).orderBy(Timesheet.WORKDATE).eq(Timesheet.PERSON, person).list());
	}
	
	public BTimesheet[] listTimesheetsForPeriod(int fromDate, int toDate) {
//		return BTimesheet.makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).orderBy(Timesheet.WORKDATE).eq(Timesheet.PERSON, person).list());
		return BTimesheet.makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateBetween(Timesheet.WORKDATE, fromDate, toDate).orderBy(Timesheet.WORKDATE).eq(Timesheet.PERSON, person).list());
	}

	/**
	 * @param date
	 * @throws ArahantException
	 */
	public void markRejectionCorrected(final int date) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final List<Timesheet> times = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, person).eq(Timesheet.WORKDATE, date).list();

		final Iterator<Timesheet> timeItr = times.iterator();

		while (timeItr.hasNext()) {
			final Timesheet t = timeItr.next();
			if (t.getState() == TIMESHEET_REJECTED || t.getState() == TIMESHEET_PROBLEM)
				throw new ArahantWarning("All rejected timesheets must be corrected before day can be marked corrected.");
		}

		hsu.delete(hsu.createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).eq(TimeReject.REJECTDATE, date).list());
	}
	
	public void markRejectionCorrected(int bdate, int edate) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final List<Timesheet> times = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, person).ge(Timesheet.WORKDATE, bdate).le(Timesheet.WORKDATE, edate).list();

		final Iterator<Timesheet> timeItr = times.iterator();

		while (timeItr.hasNext()) {
			final Timesheet t = timeItr.next();
			if (t.getState() == TIMESHEET_REJECTED || t.getState() == TIMESHEET_PROBLEM)
				throw new ArahantWarning("All rejected timesheets must be corrected before day can be marked corrected.");
		}

		hsu.delete(hsu.createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).ge(TimeReject.REJECTDATE, bdate).le(TimeReject.REJECTDATE, edate).list());
	}

	public String getTimesheetExceptionReport(final int fromDate, int toDate, final boolean includeSelf) throws IOException, DocumentException, ArahantException {
		final File fyle = FileSystemUtils.createTempFile("TSExcepRept", ".pdf");

		// get a list of the subordinate employees with their missing timesheets in the date range
		final IPersonList[] pl = getSubordinates(includeSelf);
		final List<List> exceptionList = new LinkedList<List>();
		List<Serializable> employeeAndTimeList;
		if (toDate == 0)
			toDate = DateUtils.getDate(new Date());
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final IPersonList element : pl) {
			final Person emp = hsu.get(Employee.class, element.getPersonId());

			employeeAndTimeList = new LinkedList<Serializable>();

			employeeAndTimeList.add(emp);

			// if they don't have a timesheet on a weekday that isn't a holiday
			// inside this timespan, then they get added to the list
			for (int dateLoop = fromDate; dateLoop < toDate; dateLoop = nextDay(dateLoop))
				if (isWorkDay(dateLoop, hsu))
					if (!hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, emp).eq(Timesheet.WORKDATE, dateLoop).exists())
						employeeAndTimeList.add(dateLoop);

			// check if more than the employee was added (actually got times)
			if (employeeAndTimeList.size() > 1)
				exceptionList.add(employeeAndTimeList);
		}

		// configure a finalize report and build it
		final TimesheetExceptionReport teReport = new TimesheetExceptionReport();
		teReport.build(hsu, fyle, exceptionList, fromDate, toDate);

		// construct a transmit return
		return FileSystemUtils.getHTTPPath(fyle);
	}

	private int nextDay(final int day) {
		final Date d = DateUtils.getDate(day);

		d.setTime(d.getTime() + 24 * 60 * 60 * 1000);

		return DateUtils.getDate(d);
	}

	protected boolean isWorkDay(final int day, final HibernateSessionUtil hsu) {
		final Calendar d = DateUtils.getCalendar(day);
		if (d.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || d.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return false;
		if (hsu.createCriteria(Holiday.class).eq(Holiday.HDATE, day).first() != null)
			return false;
		return true;
	}

	public String getPaymentInfoId() {
		PaymentInfo pi = ArahantSession.getHSU().createCriteria(PaymentInfo.class).eq(PaymentInfo.PERSON_ID, getPersonId()).first();
		if (pi == null)
			return null;
		return pi.getPaymentInfoId();
	}

	private class MissingTimeEntry {
		String id;
		int date;

		public MissingTimeEntry(final String i, final int d) {
			id = i;
			date = d;
		}
	}

	public BPerson[] listEmployeesForTimesheetExceptionReport(final boolean includeSelf, final int fromDate, int toDate) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final IPersonList[] pl = getSubordinates(includeSelf);

		if (toDate == 0)
			toDate = DateUtils.getDate(new Date());

		final List<MissingTimeEntry> retList = new LinkedList<MissingTimeEntry>();

		for (final IPersonList element : pl) {
			final Employee emp = hsu.get(Employee.class, element.getPersonId());

			// if they don't have a timesheet on a weekday that isn't a holiday
			//inside this timespan, then they get added to the list

			for (int dateLoop = fromDate; dateLoop <= toDate; dateLoop = nextDay(dateLoop)) {
				if (isWorkDay(dateLoop, hsu))
					if (!hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, emp).eq(Timesheet.WORKDATE, dateLoop).exists())
						retList.add(new MissingTimeEntry(emp.getPersonId(), dateLoop));
				if (retList.size() > 50)
					break;
			}
			if (retList.size() > 50)
				break;
		}

		int size = retList.size();
		if (size > 50)
			size = 50;

		final BPerson[] ret = new BPerson[size];

		for (int loop = 0; loop < size; loop++) {
			final MissingTimeEntry emt = retList.get(loop);
			ret[loop] = new BPerson(emt.id);
			ret[loop].setMissingTimeDay(DateUtils.getDateFormatted(emt.date));
		}
		return ret;
	}

	/**
	 * @return Returns the missingTimeDay.
	 */
	public String getMissingTimeDay() {
		return missingTimeDay;
	}

	/**
	 * @param missingTimeDay The missingTimeDay to set.
	 */
	public void setMissingTimeDay(final String missingTimeDay) {
		this.missingTimeDay = missingTimeDay;
	}

	public BProjectShift [] getQuickList(final boolean sort) throws Exception {
		
		//DCM get all the quicklist projects
		final List<ProjectShift> psl = new LinkedList<>();
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		final Person p = person;
		//int quicklistType = BProperty.getInt("ProjectQuicklist");
		/*
		if (quicklistType == 1) //Integra
		{
			//add the projects the employee has logged time to within a week
			ptl.addAll(addProjectsWithLoggedTimeLastWeek(true));
			//add the last set the user had
			int max = maxRowsTimeRelatedDropdown - ptl.size();
			if (max > 0)
				ptl.addAll(addProjectsAssignedToEmployee(ptl, max));
			//add the projects the employee has logged time to all time
			max = maxRowsTimeRelatedDropdown - ptl.size();
			if (max > 0)
				ptl.addAll(addProjectsWithLoggedTimeAllTime(true, ptl, max));
			//add the sick, etc types
			addWithoutDups(ptl, hsu.createCriteria(Project.class).orderBy(Project.PROJECTNAME).setMaxResults(maxRowsTimeRelatedDropdown).eq(Project.ALLEMPLOYEES, 'Y').joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N').list());

			if (sort)
				java.util.Collections.sort(ptl, new ProjectDescriptionComparator());
		} else if (quicklistType == 2) //Cimplify
		{
			Project pr = getDefaultProject();
			if (pr != null && pr.getRequestingOrgGroup() != null) {

				ptl.addAll(hsu.createCriteria(Project.class).eq(Project.REQUESTING_ORG_GROUP, pr.getRequestingOrgGroup()).list());
				List<Project> temp = new ArrayList<Project>();
				temp.addAll(ptl);
				for (Project pt : temp)
					if (!hsu.createCriteria(OrgGroup.class).eq(OrgGroup.DEFAULT_PROJECT, pt).exists())
						ptl.remove(pt);
			}
			if (ptl.size() < 25)
				//add the projects the employee has logged time to within a week
				ptl.addAll(addProjectsWithLoggedTimeLastWeek(false, ptl));
			//add the projects that are related to assigned time related benefits
			if (ptl.size() < 25)
				ptl.addAll(addTimeRelatedProjects(ptl));
			//add the last set the user had
			int max = 25 - ptl.size();
			if (max > 0)
				ptl.addAll(addProjectsAssignedToEmployee(ptl, max));
			//add the projects the employee has logged time to all time
			max = 25 - ptl.size();
			if (max > 0)
				ptl.addAll(addProjectsWithLoggedTimeAllTime(false, ptl, max));

			//add the sick, etc types
			max = 25 - ptl.size();
			if (max > 0)
				addWithoutDups(ptl, hsu.createCriteria(Project.class).orderBy(Project.PROJECTNAME).setMaxResults(max).eq(Project.ALLEMPLOYEES, 'Y').joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N').list());
		} else if (quicklistType == 3) // Five Points
		{
			Project pr = getDefaultProject();
			if (pr != null)
				ptl.add(pr);
			//add the projects that are related to assigned time related benefits
			if (ptl.size() < maxRowsTimeRelatedDropdown)
				ptl.addAll(addTimeRelatedProjects(ptl));
			//add the sick, etc types
			int max = maxRowsTimeRelatedDropdown - ptl.size();
			if (max > 0)
				addWithoutDups(ptl, hsu.createCriteria(Project.class).orderBy(Project.PROJECTNAME).setMaxResults(max).eq(Project.ALLEMPLOYEES, 'Y').joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N').list());
		}  else //Arahant and everyone else

		 */
		{
			//add the projects the employee has logged time to within a week
			psl.addAll(addProjectsWithLoggedTimeLastWeek(false));
			//add the projects that are related to assigned time related benefits
			if (psl.size() < maxRowsTimeRelatedDropdown)
				psl.addAll(addTimeRelatedProjects(psl));
			//add the last set the user had
			int max = maxRowsTimeRelatedDropdown - psl.size();
			if (max > 0)
				psl.addAll(addProjectsAssignedToEmployee(psl, max));
			//add the projects the employee has logged time to all time
			max = maxRowsTimeRelatedDropdown - psl.size();
			if (max > 0)
				psl.addAll(addProjectsWithLoggedTimeAllTime(false, psl, max));

			//add the sick, etc types
			max = maxRowsTimeRelatedDropdown - psl.size();
			if (max > 0) {
				HibernateCriteriaUtil<ProjectShift> crit = hsu.createCriteria(ProjectShift.class).setMaxResults(max)
						.joinTo(ProjectShift.PROJECT)
						.orderBy(Project.PROJECTNAME)
						.eq(Project.ALLEMPLOYEES, 'Y').joinTo(Project.PROJECTSTATUS);
				HibernateCriterionUtil cn1 = crit.makeCriteria();
				HibernateCriterionUtil cn2 = crit.makeCriteria();
				HibernateCriterionUtil cn3 = crit.makeCriteria();
				cn3.or(cn1.eq(ProjectStatus.ACTIVE, 'Y'), cn2.eq(ProjectStatus.ACTIVE, 'O'));
				cn3.add();
				addWithoutDups(psl, crit.list());
			}
		}
		return BProjectShift.makeArray(psl);
	}
	
	private void addWithoutDups(List<ProjectShift> psl, List<ProjectShift> projshfts) {
		for (ProjectShift p : projshfts) {
			boolean found = false;
			String id = p.getProjectShiftId();
			for (ProjectShift p2 : psl)
				if (p2.getProjectShiftId().equals(id)) {
					found = true;
					break;
				}
			if (!found)
				psl.add(p);
		}
	}

	private List<ProjectShift> addProjectsWithLoggedTimeLastWeek(boolean checkAllEmployees) throws Exception {
		ArrayList<ProjectShift> notInShiftIds = new ArrayList<ProjectShift>();
		ProjectShift dummy = new ProjectShift();
		dummy.setProjectShiftId("abc");
		notInShiftIds.add(dummy);  //  to avoid an empty list in the SQL statements
		return addProjectsWithLoggedTimeLastWeek(checkAllEmployees, notInShiftIds);
	}

	private List<ProjectShift> addProjectsWithLoggedTimeLastWeek(boolean checkAllEmployees, List<ProjectShift> notInProjectShifts) throws Exception {
		final Date now = new Date();
		final long weekBack = now.getTime() - 7 * 24 * 60 * 60 * 1000;
		final Date lastWeek = new Date(weekBack);
		int aWeekAgo = DateUtils.getDate(lastWeek);

		Connection db = ArahantSession.getKissConnection();
		ArrayListString notInProjectShiftIds = new ArrayListString();
		notInProjectShiftIds.add("xx");    //  can't be empty on <> ANY() so we add junk
		for (ProjectShift ps : notInProjectShifts)
			notInProjectShiftIds.add(ps.getProjectShiftId());
		final String select = "select ps.project_shift_id " +
				"              from project_shift ps " +
				"              join project proj " +
				"                on ps.project_id = proj.project_id " +
				"              join project_status pstat " +
				"                on proj.project_status_id = pstat.project_status_id " +
				"              where ps.project_shift_id <> ANY(?) " +
				"                    and pstat.active <> 'N' " +
				"                    and ps.project_shift_id in (select project_shift_id " +
				"                                             from timesheet ts " +
				"                                             where person_id = ? " +
				"                                                   and beginning_date >= ?) " +
				"                    and proj.all_employees <> ? " +
				"              order by proj.description";
		List<Record> recs = db.fetchAll(maxRowsTimeRelatedDropdown, select,
				notInProjectShiftIds, person.getPersonId(), aWeekAgo, checkAllEmployees ? 'Y' : 'X');  // yes, 'X'
		ArrayList<ProjectShift> ret = new ArrayList<>();
		for (Record rec : recs)
			ret.add(new BProjectShift(rec.getString("project_shift_id")).getProjectShift());
		return ret;


		/*

		List<String> notInProjectShiftIds = new ArrayList<>();
		for (ProjectShift ps : notInProjectShifts)
			notInProjectShiftIds.add(ps.getProjectShiftId());

		int aWeekAgo = DateUtils.getDate(lastWeek);
		HibernateCriteriaUtil<ProjectShift> hcu = ArahantSession.getHSU().createCriteria(ProjectShift.class)
				.distinct().setMaxResults(maxRowsTimeRelatedDropdown);
		hcu.joinTo(ProjectShift.TIMESHEET).eq(Timesheet.PERSON, person).ge(Timesheet.WORKDATE, aWeekAgo).orderByDesc(Timesheet.WORKDATE);
		if (notInProjectShiftIds.size() > 0)
			hcu.notIn(ProjectShift.PROJECTSHIFTID, notInProjectShiftIds);
		hcu = hcu.joinTo(ProjectShift.PROJECT).orderBy(Project.PROJECTNAME);
		if (checkAllEmployees)
			hcu.ne(Project.ALLEMPLOYEES, 'Y');
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		return hcu.list();
		 */
	}

	private List<ProjectShift> addTimeRelatedProjects(List<ProjectShift> psl) {
		final List<String> ids = new ArrayList<>(psl.size());
		for (ProjectShift projectShift : psl)
			ids.add(projectShift.getProjectShiftId());
		HibernateCriteriaUtil<ProjectShift> hcu = ArahantSession.getHSU().createCriteria(ProjectShift.class)
				.distinct().setMaxResults(maxRowsTimeRelatedDropdown - psl.size())
				.notIn(ProjectShift.PROJECTSHIFTID, ids)
				.joinTo(ProjectShift.PROJECT)
				.orderBy(Project.PROJECTNAME)
				.ne(Project.ALLEMPLOYEES, 'Y');
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.HRBENEFITPROJECTJOINS).joinTo(HrBenefitProjectJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, person);
		return hcu.list();
	}

	private List<ProjectShift> addProjectsAssignedToEmployee(List<ProjectShift> psl, int max) throws Exception {
		Connection db = ArahantSession.getKissConnection();
		ArrayListString ids = new ArrayListString();
		for (ProjectShift ps : psl)
			ids.add(ps.getProjectShiftId());
		List<Record> recs = db.fetchAll(max, "select ps.project_shift_id" +
				"                             from project_shift ps " +
				"                             join project proj " +
				"                               on ps.project_id = proj.project_id " +
				"                             join project_status pstat " +
				"                               on proj.project_status_id = pstat.project_status_id " +
				"                             where project_shift_id <> ANY(?) " +
				"                                   and pstat.active <> 'N' " +
				"                                   and ps.project_shift_id in (select project_shift_id " +
				"                                                               from project_employee_join " +
				"                                                               where person_id = ?) " +
				"                             order by proj.description ", ids, person.getPersonId());
		ArrayList<ProjectShift> ret = new ArrayList<>();
		for (Record rec : recs)
			ret.add(new BProjectShift(rec.getString("project_shift_id")).getProjectShift());
		return ret;

		/*
		final List<String> ids = new ArrayList<>(psl.size());
		for (ProjectShift ps : psl)
			ids.add(ps.getProjectShiftId());

//					add the ones that are in the user's status
		HibernateCriteriaUtil<ProjectShift> hcu = ArahantSession.getHSU().createCriteria(ProjectShift.class)
				.distinct().setMaxResults(max).notIn(ProjectShift.PROJECTSHIFTID, ids);
		hcu.joinTo(ProjectShift.PROJECT).orderBy(Project.PROJECTNAME);
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, person);
		return hcu.list();
		 */
	}

	private List<ProjectShift> addProjectsWithLoggedTimeAllTime(boolean checkAllEmployees, List<ProjectShift> psl, int max) {
		final List<String> ids = new ArrayList<>(psl.size());
		for (ProjectShift shift : psl)
			ids.add(shift.getProjectShiftId());

		HibernateCriteriaUtil<ProjectShift> hcu = ArahantSession.getHSU().createCriteria(ProjectShift.class).distinct().setMaxResults(max)
				.notIn(ProjectShift.PROJECTSHIFTID, ids)
				.joinTo(ProjectShift.PROJECT)
				.orderBy(Project.PROJECTNAME).ne(Project.ALLEMPLOYEES, 'Y');
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.TIMESHEETS).eq(Timesheet.PERSON, person).orderByDesc(Timesheet.WORKDATE);
		return hcu.list();
	}

	private class ProjectDescriptionComparator implements Comparator<Project> {

		@Override
		public int compare(final Project arg0, final Project arg1) {
			final Project p1 = arg0;
			final Project p2 = arg1;

			if (p1.getDescription() == null)
				if (p2.getDescription() == null)
					return 0;
				else
					return 1;

			return p1.getDescription().compareToIgnoreCase(p2.getDescription());
		}
	}

	public String getFinalizeReport(final boolean includeSelf, final int cutoffDate) throws DocumentException, IOException, ArahantException {

		final File fyle = FileSystemUtils.createTempFile("TSFinRept", ".pdf");
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// get a list of the subordinate employees with their finalization information
		final IPersonList[] pl = getSubordinates(includeSelf);
		final List<Employee> employeeList = new LinkedList<Employee>();
		for (final IPersonList element : pl) {
			final Employee emp = hsu.get(Employee.class, element.getPersonId());

			if (cutoffDate != 0 && emp.getTimesheetFinalDate() >= cutoffDate)
				continue;

			employeeList.add(emp);
		}
		java.util.Collections.sort(employeeList, new EmployeeByFinalDateComparator());

		// configure a finalize report and build it
		final TimesheetFinalizationReport tfReport = new TimesheetFinalizationReport();
		tfReport.build(hsu, fyle, employeeList, cutoffDate);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	/**
	 * @param cutoffDate
	 * @return
	 * @throws ArahantException
	 */
	public BEmployee[] listEmployeesForFinalizedReport(final int cutoffDate, final boolean includeSelf) throws ArahantException {

		logger.debug("Getting subordinates.");

		final IPersonList[] pl = getSubordinates(includeSelf);

		logger.debug("Got employees");

		final List<Employee> retList = new LinkedList<Employee>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final IPersonList element : pl) {
			final Employee emp = hsu.get(Employee.class, element.getPersonId());

			if (cutoffDate != 0 && emp.getTimesheetFinalDate() >= cutoffDate)
				continue;

			retList.add(emp);
		}
		logger.debug("Made employee list.");

		java.util.Collections.sort(retList, new EmployeeByFinalDateComparator());

		logger.debug("Sorted employee list.");

		int size = retList.size();
		if (size > 50)
			size = 50;

		final BEmployee[] ret = new BEmployee[size];

		for (int loop = 0; loop < size; loop++) {
			final Employee emp = retList.get(loop);
			ret[loop] = new BEmployee(emp);
			//get last time entered for this person
			final Timesheet lastDate = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, emp).orderByDesc(Timesheet.WORKDATE).first();

			double totalHours = 0;

			if (lastDate != null) {

				final List<Timesheet> times = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, emp).eq(Timesheet.WORKDATE, lastDate.getWorkDate()).list();

				final Iterator<Timesheet> tItr = times.iterator();

				while (tItr.hasNext()) {
					final Timesheet t = tItr.next();
					totalHours += t.getTotalHours();

				}
				ret[loop].setLastEnteredTimeDate(DateUtils.getDateFormatted(lastDate.getWorkDate()));
			} else
				ret[loop].setLastEnteredTimeDate(DateUtils.getDateFormatted(0));

			ret[loop].setHoursLastEntered(totalHours);

		}
		logger.debug("calculated totals.");
		return ret;
	}

	/**
	 * @return Returns the hoursLastEntered.
	 */
	public double getHoursLastEntered() {
		return hoursLastEntered;
	}

	/**
	 * @param hoursLastEntered The hoursLastEntered to set.
	 */
	public void setHoursLastEntered(final double hoursLastEntered) {
		this.hoursLastEntered = hoursLastEntered;
	}

	/**
	 * @return Returns the lastEnteredTimeDate.
	 */
	public String getLastEnteredTimeDate() {
		return lastEnteredTimeDate;
	}

	/**
	 * @param lastEnteredTimeDate The lastEnteredTimeDate to set.
	 */
	public void setLastEnteredTimeDate(final String lastEnteredTimeDate) {
		this.lastEnteredTimeDate = lastEnteredTimeDate;
	}

	/**
	 * @param date
	 * @param message
	 * @throws ArahantException
	 */
	public void rejectTimeDay(final BPerson current, final int date, final String message) throws ArahantException {
		if (ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).eq(TimeReject.REJECTDATE, date).first() == null) {
			final TimeReject tr = new TimeReject();
			tr.setPerson(person);
			tr.setRejectDate(date);
			tr.generateId();
			ArahantSession.getHSU().insert(tr);
			internalCreateMessage(current.getPersonId(), person.getPersonId(), message, "Time Entry Day Rejection");
		}
	}

	/**
	 * @return
	 */
	public int[] getRejectedDays() {
		final List<TimeReject> rejects = ArahantSession.getHSU().createCriteria(TimeReject.class).eq(TimeReject.PERSON, person).orderBy(TimeReject.REJECTDATE).list();
		final int[] dt = new int[rejects.size()];

		for (int loop = 0; loop < dt.length; loop++)
			dt[loop] = rejects.get(loop).getRejectDate();

		return dt;
	}

	private static class EmployeeByFinalDateComparator implements Comparator<Employee> {

		@Override
		public int compare(final Employee arg0, final Employee arg1) {
			return arg0.getTimesheetFinalDate() - arg1.getTimesheetFinalDate();
		}
	}

	public ScreenGroup getScreenGroup() {
		if (prophetLogin != null)
			return prophetLogin.getScreenGroup();
		return null;
	}

	public ScreenGroup getNoCompanyScreenGroup() {
		if (prophetLogin != null)
			return prophetLogin.getNoCompanyScreenGroup();

		return null;
	}

	/**
	 * @param companyId
	 */
	public void setCompanyId(final String companyId) {
		person.setCompanyBase(ArahantSession.getHSU().get(CompanyBase.class, companyId));
	}

	/*
	 * public String getMiddleName() { return person.getMi()+""; }
	 */
	public void setMiddleName(String mname) {
		person.setMname(mname);
	}

	public void setNickName(String nickName) {
		person.setNickName(nickName);
	}

	/**
	 * @param hsu
	 * @param firstName
	 * @param lastName
	 * @param associatedIndicator
	 * @param ssn
	 * @param orgGroupId
	 * @param max
	 * @throws ArahantException
	 */
	public BEmployee[] searchSubordinates(final HibernateSessionUtil hsu, final String firstName, final String lastName, final int associatedIndicator, final String ssn, final String orgGroupId, final int max) throws ArahantException {
		final HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class);

		//addInactiveEmployeeFilter(hcu);

		if (!isEmpty(ssn))
			hcu.like(Employee.SSN, ssn);

		hcu.setMaxResults(max);

		hcu.orderBy(Person.LNAME);

		if (!isEmpty(firstName))
			hcu.like(Person.FNAME, firstName);
		if (!isEmpty(lastName))
			hcu.like(Person.LNAME, lastName);


		final List<Person> filterPersons = getSubordinateList(true);
		final LinkedList<String> pids = new LinkedList<String>();
		for (final Person per : filterPersons)
			pids.add(per.getPersonId());

		hcu.in(Employee.PERSONID, pids);

		if (!isEmpty(orgGroupId))
			hcu.joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP,
					hsu.get(OrgGroup.class, orgGroupId));

		switch (associatedIndicator) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
				break;
			case 2:			//not associated	
				hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);
				break;
		}

		final List<Employee> res = hcu.list();

		final List<BEmployee> resFiltered = new ArrayList<BEmployee>(res.size());
		for (Employee re : res) {
			final BEmployee b = new BEmployee(re);
			if (b.showInLists())
				resFiltered.add(b);
		}

		final BEmployee[] ret = new BEmployee[resFiltered.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = resFiltered.get(loop);

		return ret;
	}

	public BEmployee[] searchSubordinates(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, final int activeIndictator, final boolean includeUser) throws ArahantException {
		List<Person> subords = null;

		if (BRight.checkRight(BRight.SEE_ALL_EMPLOYEES_IN_LISTS) != ACCESS_LEVEL_WRITE) {
			subords = new LinkedList<Person>();
			switch (activeIndictator) {
				case -1:
					subords.addAll(getSubordinateList(includeUser, true));
					break;
				case 0:
					subords.addAll(getSubordinateList(includeUser, true));//show all, no filter
					subords.addAll(getSubordinateList(includeUser, false));
					break;
				case 1:
					subords.addAll(getSubordinateList(includeUser, true));//active only
					break;
				case 2:
					subords.addAll(getSubordinateList(includeUser, false));//inactive only
					break;
			}
		}
		return searchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, activeIndictator, subords, null);
	}
	/*
	 * private static String addWhere(final String where, final String clause) {
	 * if (where.equals("")) return clause;
	 *
	 * return where+" and "+clause+" "; }
	 *
	 * private static String addLike(final String where, final String col,
	 * String val) { if (!isEmpty(val) && !val.equals("*") && !val.equals("%") )
	 * { val=val.replace('*', '%');
	 *
	 * return addWhere(where," lower(emp."+col+") like '"+val.toLowerCase()+"'
	 * ");
	 *
	 * }
	 * return where; }
	 *
	 */

	@SuppressWarnings("unchecked")
	private static BEmployee[] oldSearchEmployees(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, int activeIndictator, final List<Person> filterPersons, final String notInOrgGroup) throws ArahantException {
		//does company filter
		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).orderBy(Employee.MNAME).like(Employee.FNAME, fname).like(Employee.LNAME, lname).setMaxResults(max);

		hcu.isEmployee();

		if (!isEmpty(ssn))
			hcu.eq(Employee.SSN, ssn);

		if (filterPersons != null) {
			List<String> pidsw = new LinkedList<String>();

			for (final Person per : filterPersons)
				pidsw.add(per.getPersonId());

			if (pidsw.isEmpty())
				return new BEmployee[0];

			hcu.in(Employee.PERSONID, pidsw);
		}

		OrgGroup og;
		if (!isEmpty(orgGroupId)) {
			og = hsu.get(OrgGroup.class, orgGroupId);
			if (og.getOwningCompany() != null)
				hcu.joinTo(Employee.COMPANYBASE).eq(CompanyBase.ORGGROUPID, og.getOwningCompany().getOrgGroupId());
			hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, orgGroupId);
		}

		if (!isEmpty(notInOrgGroup))
			hcu.notIn(Employee.PERSONID, hsu.createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, notInOrgGroup).list());

		if (assocInd == 1)
			//hcu.isNotNull(Person.ORGGROUPASSOCIATIONS);
			hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
		if (assocInd == 2)
			//hcu.isNull(Person.ORGGROUPASSOCIATIONS);
			hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);


        // the methods in this switch very significantly slow down the query
		switch (activeIndictator) {
			case -1:
				hcu.activeEmployee60Days();
				break;
			case 0:  //show all, no filter
				break;
			case 1: //active only
				hcu.activeEmployee();
				break;
			case 2:  //inactive only
				hcu.inactiveEmployee();
				break;
			case 3:
				hcu.sizeEq(Employee.HREMPLSTATUSHISTORIES, 0);
				break;
		}


		return makeEmployeeArray(hcu.list());
	}

    /*
       The following is a much faster Kiss version of the above function.
     */
	public static BEmployee[] searchEmployees(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname,
											  final String orgGroupId, final int assocInd, int max, int activeIndictator,
											  final List<Person> filterPersons, final String notInOrgGroup) throws ArahantException {

	    // If features that this method doesn't handle are utilized, call the old method
	    if (orgGroupId != null  ||  assocInd != 0  ||  filterPersons != null  ||  notInOrgGroup != null)
	        return oldSearchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, activeIndictator, filterPersons, notInOrgGroup);

	    Connection db = KissConnection.get();

        boolean needAnd = false;
        ArrayList<Object> args = new ArrayList<>();

        String select =  "select p.person_id " +
                "from employee e " +
                "join person p " +
                "  on e.person_id = p.person_id " +
                "left join current_employee_status esh " +
                "  on e.person_id = esh.employee_id " +
                "join hr_employee_status es " +
                "  on esh.status_id = es.status_id ";

        if (activeIndictator == 1  ||  activeIndictator == -1) {
            select += " where active='Y'";
            needAnd = true;
        } else if (activeIndictator == 2) {
            select += " where active='N'";
            needAnd = true;
        }
        if (fname != null  &&  !fname.isEmpty()  &&  !fname.equals("%")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                select += "lower(fname) like lower(?)";
            else
                select += "fname like ?";
            args.add(fname);
        }
        if (lname != null  &&  !lname.isEmpty()  &&  !lname.equals("%")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                select += "lower(lname) like lower(?)";
            else
                select += "lname like ?";
            args.add(lname);
        }
        if (ssn != null  &&  !ssn.isEmpty()  &&  !ssn.equals("%")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += "ssn = ?";
            args.add(Person.encryptSsn(ssn));
        }

		if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
			select += " order by lower(p.lname), lower(p.fname), lower(p.mname), p.person_id ";
		else
            select += " order by p.lname, p.fname, p.mname, p.person_id ";

        try {
            if (max < 1)
                max = 70;
            List<Record> recs = db.fetchAll(max, select, args.toArray());
            List<BEmployee> el = new ArrayList<>();

            for (Record rec : recs)
                el.add(new BEmployee(rec.getString("person_id")));
            BEmployee [] ret = new BEmployee[el.size()];
            return el.toArray(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

		/*
	 * Use when hibernate fixes bug protected static void
	 * filterActiveInLastSixtyDays(final HibernateCriteriaUtil<Employee> hcu) {
	 *
	 * final HibernateDetachedCriteriaUtil<HrEmplStatusHistory> hist1 = new
	 * HibernateDetachedCriteriaUtil<HrEmplStatusHistory>(HrEmplStatusHistory.class,
	 * "hist1"); hist1.max(HrEmplStatusHistory.EFFECTIVEDATE, "histmax");
	 *
	 * final HibernateCriteriaUtil curStat =
	 * hcu.joinTo(Employee.HREMPLSTATUSHISTORIES, "curStat");
	 *
	 * curStat.eqPropertySubquery("curStat." +
	 * HrEmplStatusHistory.EFFECTIVEDATE, hist1);
	 *
	 *
	 * curStat.eq("curStat." + HrEmplStatusHistory.HREMPLOYEESTATUS + "." +
	 * HrEmployeeStatus.ACTIVE, 'Y');
	 *
	 * }
	 */

	SecurityGroup getSecurityGroup() {
		try {
			return person.getProphetLogin().getSecurityGroup();
		} catch (final Exception e) {
			return null;
		}
	}

	public void setSecurityGroupId(final String securityGroupId) {
		if (checkLogin(securityGroupId))
			prophetLogin.setSecurityGroup(ArahantSession.getHSU().get(SecurityGroup.class, securityGroupId));
	}

	public String getSecurityGroupId() {
		try {
			return person.getProphetLogin().getSecurityGroup().getSecurityGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getSecurityGroupId(String companyId) {
		try {
			return BRight.getCurrentSecurityGroupId(companyId);
		} catch (final Exception e) {
			return "";
		}
	}

	public String getSecurityGroupName() {
		try {
			return person.getProphetLogin().getSecurityGroup().getId();
		} catch (final Exception e) {
			return "";
		}
	}

	public BPerson[] searchSubordinates(final String firstName, final String lastName, final int max) throws ArahantException {
		final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class);
		hcu.setMaxResults(max);
		hcu.orderBy(Person.LNAME);
		hcu.like(Person.FNAME, firstName);
		hcu.like(Person.LNAME, lastName);

		final List<Person> filterPersons = getSubordinateList(true);
		final LinkedList<String> pids = new LinkedList<String>();
		for (final Person per : filterPersons)
			pids.add(per.getPersonId());

		hcu.in(Person.PERSONID, pids);
		return buildPersonReturnList(hcu.list());
	}

	private static BPerson[] buildPersonReturnList(final List<Person> l) throws ArahantException {
		final List<BPerson> resFiltered = new ArrayList<BPerson>(l.size());
		for (final Person p : l) {
			final BPerson b = new BPerson(p);
			if (p instanceof Employee) {
				if (new BEmployee((Employee) p).showInLists())
					resFiltered.add(b);
			} else
				resFiltered.add(b);
		}

		final BPerson[] ret = new BPerson[resFiltered.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = resFiltered.get(loop);

		return ret;
	}

	public static BPerson[] searchPersons(final String orgGroupId, final String lastName, final String firstName, final int max) throws ArahantException {
		return searchPersons(orgGroupId, lastName, firstName, new String[0], max);
	}

	public static BPerson[] searchPersons(final String orgGroupId, final String lastName, final String firstName, final String[] excludeIds, final int max) throws ArahantException {
		final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).orderBy(Person.LNAME).orderBy(Person.FNAME).notIn(Person.PERSONID, excludeIds).setMaxResults(max);

		if (!isEmpty(orgGroupId))
			hcu.joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);

		return buildPersonReturnList(hcu.list());
	}

	public static BPerson[] searchPersonsInHierarchy(final String orgGroupId, final String lastName, final String firstName, final String[] excludeIds, final int max) throws ArahantException {
		List<String> empIdsList = new ArrayList<String>();
		empIdsList.addAll(new BOrgGroup(orgGroupId).getAllPersonIdsForOrgGroupHierarchy(true));

		final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).in(Person.PERSONID, empIdsList).like(Person.FNAME, firstName).like(Person.LNAME, lastName).orderBy(Person.LNAME).orderBy(Person.FNAME).notIn(Person.PERSONID, excludeIds).setMaxResults(max);

		return buildPersonReturnList(hcu.list());
	}

	/**
	 * @param dateAssigned
	 * @param dateAssignedSearchType
	 * @param projectStatusId
	 * @param cap
	 * @return
	 */
	public BProject[] searchProjects(final int dateAssigned, final int dateAssignedSearchType, final String projectStatusId, final int cap) {
		final HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class);

		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, person).orderBy(ProjectEmployeeJoin.EMPLOYEE_PRIORITY).dateCompare(ProjectEmployeeJoin.DATE_ASSIGNED, dateAssigned, dateAssignedSearchType);

		hcu.orderBy(Project.PROJECTNAME);

		if (!isEmpty(projectStatusId))
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.PROJECTSTATUSID, projectStatusId);
		else
			hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		return BProject.makeArray(hcu.list());
	}

	public static BPerson[] search2(final String firstName, final String lastName, final String ssn, final String type, final int cap) throws ArahantException {
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME);
		//		.list());

		if (type.equals("E"))
			hcu.isEmployee();

		if (type.equals("D")) {
			hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);
			hcu.isNotEmployee();
		}
		return BPerson.makeArray(hcu.list());
	}

	public static BPerson[] searchCobras(final String firstName, final String lastName, final String ssn, final String type, final int cap) throws ArahantException {
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME);
		//		.list());

		hcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING).eq(HrBenefitJoin.USING_COBRA, 'Y');

		if (type.equals("E"))
			hcu.isEmployee();

		if (type.equals("D")) {
			hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);
			hcu.isNotEmployee();
		}
		return BPerson.makeArray(hcu.list());
	}

	public static BPerson[] search3(String companyId, final String firstName, final String lastName, final String[] exclude, final int cap) throws ArahantException {
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).notIn(Person.PERSONID, exclude).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME);

		hcu.joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).joinTo(OrgGroup.OWNINGCOMPANY).eq(CompanyBase.ORGGROUPID, companyId);

		return BPerson.makeArray(hcu.list());
	}

	/**
	 * This method returns the unencrypted SSN.
	 * 
	 * @return the unencrypted SSN
	 */
	public String getSsn() {
		return person.getUnencryptedSsn();
	}

	public String getSex() {
		return person.getSex() + "";
	}

	public String getNameLFM() {
		return person.getNameLFM();
	}

	public String getNameFML() {
		return person.getNameFML();
	}

	public int getDob() {
		return person.getDob();
	}

	public void setDob(final int dob) {
		person.setDob(dob);
	}

	public String getI9p1Confirmation() {
		return person.getI9p1Confirmation();
	}

	public void setI9p1Confirmation(String v) {
		person.setI9p1Confirmation(v);
	}

	public String getI9p2Confirmation() {
		return person.getI9p2Confirmation();
	}

	public void setI9p2Confirmation(String v) {
		person.setI9p2Confirmation(v);
	}

	public String getI9p1Person() {
		return person.getI9p1Person();
	}

	public void setI9p1Person(String v) {
		person.setI9p1Person(v);
	}

	public String getI9p2Person() {
		return person.getI9p2Person();
	}

	public void setI9p2Person(String v) {
		person.setI9p2Person(v);
	}

	public Date getI9p1When() {
		return person.getI9p1When();
	}

	public void setI9p1When(Date v) {
		person.setI9p1When(v);
	}

	public Date getI9p2When() {
		return person.getI9p2When();
	}

	public void setI9p2When(Date v) {
		person.setI9p2When(v);
	}

	public void setLinkedin(String url) {
		person.setLinkedin(url);
	}

	public String getLinkedin() {
		return person.getLinkedin();
	}

	public void setSex(char s)
	{
		s = Character.toUpperCase(s);
		if (s == 'M'  ||  s == 'F')
			person.setSex(s);
		else
			person.setSex('U');
	}

	public void setSex(final String s) {
		if (s == null || s.length() == 0)
			person.setSex('U');
		else
			setSex(s.charAt(0));
	}

	/**
	 * This method sets the SSN field to an encrypted value but is passed the unencrypted value.
	 * 
	 * @param ssn the unencrypted ssn
	 */
	public void setSsn(final String ssn) {
		person.setUnencryptedSsn(ssn);
	}
	
	/**
	 * Formats SSN to the expected format 999-99-9999
	 * 
	 * @param ssn
	 * @return 
	 */
	public static String formatSsn(String ssn) {
		StringBuilder sb = new StringBuilder();
		int len = ssn.length();
		for (int i=0 ; i < len ; i++) {
			Character c = ssn.charAt(i);
			if (Character.isDigit(c))
				sb.append(c);
		}
		if (sb.length() == 0)
			return null;
		StringBuilder sb2 = new StringBuilder();
		for (int i=sb.length() ; i < 9 ; i++)
			sb2.append('0');
		sb2.append(sb);
		return sb2.substring(0, 3) + "-" + sb2.substring(3, 5) + "-" + sb2.substring(5, 9);
	}
	
	/**
	 * Finds a BPerson record with the passed in SSN.  SSN's format is auto-corrected.
	 * 
	 * @param ssn
	 * @return 
	 */
	public static BPerson findPerson(String ssn) {
		ssn = formatSsn(ssn);
		if (ssn == null)
			return null;
		Person p = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.SSN, ssn).first();
		if (p == null)
			return null;
		return new BPerson(p);
	}

	public String getAge() {
		return getAge(person.getDob());
	}

	public short getAgeAsOf(int asOfDate) {
		if (person.getDob() < 1)
			return 0;

		final Calendar dob = DateUtils.getCalendar(person.getDob());
		final Calendar now = DateUtils.getCalendar(asOfDate);

		short age = -1;

		while (dob.before(now) || dob.equals(now)) {
			age++;
			dob.add(Calendar.YEAR, 1);
		}
		return age;
	}

	public static String getAge(final int birthday) {
		if (birthday < 1)
			return "";

		final Calendar dob = DateUtils.getCalendar(birthday);
		final Calendar now = DateUtils.getNow();

		int age = -1;

		while (dob.before(now)) {
			age++;
			dob.add(Calendar.YEAR, 1);
		}

		if (age < 0)
			return "";
		return age + "";
	}

	public String makeEmployee(String eeoCat, String eeoRace, char w4Status, final String extRef, final boolean overtime, String employeeStatusId) throws ArahantException {
		// Danger, this is straight SQL
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			final PreparedStatement ps = hsu.getConnection().prepareStatement("insert into employee (person_id,eeo_category_id,eeo_race_id,w4_status,ext_ref,overtime_pay) values (?,?,?,?,?,?)");

			ps.setString(1, person.getPersonId());
			if (isEmpty(eeoCat))
				eeoCat = null;
			if (isEmpty(eeoRace))
				eeoRace = null;

			ps.setString(2, eeoCat);
			ps.setString(3, eeoRace);
			ps.setString(4, w4Status + "");
			ps.setString(5, extRef);
			ps.setString(6, overtime ? "Y" : "N");

			ps.executeUpdate();

			ps.close();

			final String personId = person.getPersonId();
			hsu.evict(person);
			person = hsu.get(Employee.class, personId);

			new BEmployee((Employee) person).setStatusId(employeeStatusId, DateUtils.now());
			//now need to move the dependents over

			if (person.getDepJoinsWhereDependent().size() > 0) {
				final HrEmplDependent link = person.getDepJoinsWhereDependent().iterator().next();


				if (link.getRelationshipType() == 'S') {

					final Employee spouse = link.getEmployee();

					//	need to do the reverse too
					final BHREmplDependent dep = new BHREmplDependent();
					dep.createSpouseBackLink(spouse.getPersonId(), person.getPersonId(), link.getDateInactive());
					dep.insert();

					//move kids over
					for (final HrEmplDependent mdep : spouse.getHrEmplDependents()) {
						if (mdep.getRelationshipType() == 'S')
							continue;

						final HrEmplDependent d = new HrEmplDependent();
						d.generateId();
						d.setPerson(mdep.getPerson());
						d.setEmployee((Employee) person);
						d.setRelationship(mdep.getRelationship());
						d.setRelationshipType(mdep.getRelationshipType());
						d.setDateAdded(link.getDateAdded());
						hsu.insert(d);
					}
				}
			}
			return person.getPersonId();
		} catch (final SQLException e) {
			throw new ArahantException(e);
		}
	}

	/**
	 * @param hsu
	 * @param ssn
	 * @param firstName
	 * @param lastName
	 * @param statusId
	 * @param includeUser
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	/*
	 * @SuppressWarnings("unchecked") public BEmployee[] searchEmployees(final
	 * HibernateSessionUtil hsu, final String ssn, final String fname, final
	 * String lname, final String statusId, final int max) throws
	 * ArahantException {
	 *
	 *
	 * final String hql="select distenct emp from Employee emp "; String
	 * where=""; String joins=""; final String order=" order by emp.lname,
	 * emp.fname ";
	 *
	 * if (!isEmpty(ssn)) where=addWhere(where," emp.ssn='"+ssn+"' ");
	 * where=addLike(where, Person.FNAME, fname); where=addLike(where,
	 * Person.LNAME, lname);
	 *
	 *
	 *
	 * if (!isEmpty(statusId)) { joins+=" join emp.hrEmplStatusHistories hist
	 * join hist."+HrEmplStatusHistory.HREMPLOYEESTATUS+" empStat ";
	 * where=addWhere(where, " empStat.statusId='"+statusId+"' and " +
	 * "hist.effectiveDate=(select max(effectiveDate) from HrEmplStatusHistory
	 * hist2 where hist2.employee=emp and
	 * hist2.effectiveDate<="+DateUtils.now()+")" + " and ((select count(*) from
	 * HrEmplStatusHistory hist2 where hist2.employee=emp and
	 * hist2.effectiveDate=hist.effectiveDate)=1 " + " or
	 * hist.hrEmployeeStatus.dateType='S' )"); }
	 *
	 *
	 * if (!where.trim().equals("")) where=" where "+where;
	 *
	 * return
	 * BEmployee.makeArrayEx((List<Employee>)(List)hsu.doQuery(hql+joins+where+order,
	 * max)); }
	 */
	/**
	 * @return @throws ArahantException
	 */
	public String makeEmployee() throws ArahantException {
		return makeEmployee(ArahantSession.getHSU().getFirst(HrEeoCategory.class).getEeoCategoryId(), ArahantSession.getHSU().getFirst(HrEeoRace.class).getEeoId(),
				'U', "", true, "");
	}

	public String getFirstNote() {
		final PersonNote n = ArahantSession.getHSU().createCriteria(PersonNote.class).eq(PersonNote.PERSON, person).first();
		return (n == null) ? "" : n.getNote();
	}

	/**
	 * @param note
	 * @throws ArahantException
	 */
	public void setFirstNote(final String note) throws ArahantException {
		final PersonNote n = ArahantSession.getHSU().createCriteria(PersonNote.class).eq(PersonNote.PERSON, person).first();

		if (n != null) {
			n.setNote(note);
			ArahantSession.getHSU().saveOrUpdate(n);
		} else {
			final BPersonNote bnote = new BPersonNote();
			bnote.create();
			bnote.setPersonId(person.getPersonId());
			bnote.setNote(note);
			bnote.setNoteCategoryId(ArahantSession.getHSU().getFirst(HrNoteCategory.class).getCatId());
			bnote.insert();
		}
	}

	/**
	 * @param name
	 */
	public int getNumericColorPreference(final String name) {
		final UserAttribute ua = ArahantSession.getHSU().createCriteria(UserAttribute.class).eq(UserAttribute.PERSON, person).eq(UserAttribute.ATTRIBUTE, name).first();
		if (ua == null)
			return -1;
		return Integer.parseInt(ua.getAttributeValue());
	}

	public int getNumericPreference(final String name) {
		final UserAttribute ua = ArahantSession.getHSU().createCriteria(UserAttribute.class).eq(UserAttribute.PERSON, person).eq(UserAttribute.ATTRIBUTE, name).first();
		if (ua == null)
			return 0;
		return Integer.parseInt(ua.getAttributeValue());
	}

	public String getStringPreference(final String name) {
		final UserAttribute ua = ArahantSession.getHSU().createCriteria(UserAttribute.class).eq(UserAttribute.PERSON, person).eq(UserAttribute.ATTRIBUTE, name).first();
		if (ua == null)
			return "";
		return ua.getAttributeValue();
	}

	public boolean getBooleanPreference(final String name) {
		final UserAttribute ua = ArahantSession.getHSU().createCriteria(UserAttribute.class).eq(UserAttribute.PERSON, person).eq(UserAttribute.ATTRIBUTE, name).first();
		if (ua == null)
			return false;
		return ua.getAttributeValue().equalsIgnoreCase("TRUE");
	}

	public void savePreference(final String name, final int value) {
		final UserAttributeId id = new UserAttributeId();
		id.setPersonId(person.getPersonId());
		id.setUserAttribute(name);
		final UserAttribute ua = new UserAttribute();
		ua.setId(id);
		ua.setAttributeValue(value + "");
		ArahantSession.getHSU().saveOrUpdate(ua);
	}

	public void savePreference(String name, String value) {
		final UserAttributeId id = new UserAttributeId();
		id.setPersonId(person.getPersonId());
		id.setUserAttribute(name);
		final UserAttribute ua = new UserAttribute();
		ua.setId(id);
		ua.setAttributeValue(value);
		ArahantSession.getHSU().saveOrUpdate(ua);
	}

	public void savePreference(String name, boolean value) {
		final UserAttributeId id = new UserAttributeId();
		id.setPersonId(person.getPersonId());
		id.setUserAttribute(name);
		final UserAttribute ua = new UserAttribute();
		ua.setId(id);
		ua.setAttributeValue(value ? "TRUE" : "FALSE");
		ArahantSession.getHSU().saveOrUpdate(ua);
	}

	/**
	 * @return @throws ArahantException
	 */
	public BEmployee getBEmployee() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Employee emp = hsu.get(Employee.class, person.getPersonId());

		if (emp == null) {
			List<PersonCR> pcrs = hsu.createCriteria(PersonCR.class).eq(PersonCR.PERSON_PENDING, person).list();

			for (PersonCR pc : pcrs) {
				emp = hsu.get(Employee.class, pc.getRealRecordId());

				if (emp != null)
					return new BEmployee(emp);
			}
			return null;
		}
		//Can't have this warning yet other functionility is checking for the bad return?!
	/*
		 * if (emp == null) { throw new ArahantWarning("This functionality
		 * requires an Employee. Are you logged in as an Employee?\n(The Arahant
		 * super user, Agents, Clients, etc. are not employees.)"); }
		 */
		return new BEmployee(emp);
	}

	/**
	 * @param includeLoginAttemptStatus
	 * @param includeLoginName
	 * @param includeName
	 * @param loginAttemptDateFrom
	 * @param loginAttemptDateTo
	 * @param showFailedLoginAttempts
	 * @param showSuccessfulLoginAttempts
	 * @return
	 * @throws ArahantException
	 * @throws DocumentException
	 */
	public static String getLoginReport(boolean includeLoginAttemptStatus, boolean includeLoginName, boolean includeName, int loginAttemptDateFrom, int loginAttemptDateTo, boolean showFailedLoginAttempts, boolean showSuccessfulLoginAttempts) throws ArahantException, DocumentException {
		return new LoginReport().build(includeLoginAttemptStatus, includeLoginName, includeName, loginAttemptDateFrom, loginAttemptDateTo, showFailedLoginAttempts, showSuccessfulLoginAttempts);
	}

	/**
	 * @param projectIds
	 */
	public void unassignProjects(String[] projectIds) throws ArahantException {
		ArahantSession.getHSU().createCriteria(ProjectEmployeeJoin.class).eq(ProjectEmployeeJoin.PERSON, person).joinTo(ProjectEmployeeJoin.PROJECTSHIFT).joinTo(ProjectShift.PROJECT).in(Project.PROJECTID, projectIds).delete();
	}

	/**
	 * @param projectIds
	 */
	public void assignProjects(String[] projectIds) throws ArahantException {
		List<Project> projects =
				ArahantSession.getHSU().createCriteria(Project.class).in(Project.PROJECTID, projectIds).list();
		for (Project project : projects) {
			BProject bProject = new BProject(project);
			bProject.setCurrentPersonId(this.person.getPersonId());
			bProject.update();
		}
	}

	/**
	 * Returns true if they are a currently active employee.
	 *
	 */
	public boolean isActiveEmployee() {
		Employee emp;
		if (person instanceof Employee)
			emp = (Employee) person;
		else
			emp = ArahantSession.getHSU().get(Employee.class, person.getPersonId());
		if (emp == null)
			return false;
		return new BEmployee(emp).isActive() == 0;
	}

	/**
	 * Returns true if they were ever an employee.
	 *
	 */
	public boolean isEmployee() {
		boolean employee = (person instanceof Employee);

		if (!employee)
			try {
				employee = (ArahantSession.getHSU().get(Employee.class, person.getPersonId()) != null);
			} catch (Exception ignored) {
			}

		return employee;
	}

	/**
	 * @param hsu
	 * @param searchEmployees
	 * @param searchDependents
	 * @param ssn
	 * @param firstName
	 * @param lastName
	 * @param cap
	 * @param activeIndicator
	 * @param includeUser
	 * @return
	 * @throws ArahantException
	 */
	public static BPerson[] searchPersons(HibernateSessionUtil hsu, boolean searchEmployees, boolean searchDependents, String statusId,
			String ssn, String firstName, String lastName, int cap, int activeIndicator, boolean includeUser) throws ArahantException {

		Set<Person> perSet = new HashSet<Person>();

		if (searchEmployees) {
			HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class).like(Employee.FNAME, firstName).like(Employee.LNAME, lastName).orderBy(Employee.LNAME).orderBy(Employee.FNAME).setMaxResults(cap);

			if (!isEmpty(ssn))
				hcu.eq(Employee.SSN, ssn);

			switch (activeIndicator) {
				case 0:  //show all, no filter
					break;
				case 1: //active only
					hcu.activeEmployee();
					break;
				case 2:  //inactive only
					hcu.inactiveEmployee();
					break;
				case 3:  //specific status id
					hcu.employeeCurrentStatus(statusId);
					break;
			}
			perSet.addAll(hcu.list());
		}

		int max = cap - perSet.size();

		if (searchDependents && max != 0) {
			HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).like(Person.LNAME, lastName).like(Person.FNAME, firstName).orderBy(Person.LNAME).orderBy(Person.FNAME).setMaxResults(max);
			hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);
			hcu.isNotEmployee();

			if (!isEmpty(ssn))
				hcu.eq(Person.SSN, ssn);

			switch (activeIndicator) {
				case 0:  //show all, no filter
					break;
				case 1: //active only
					hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
					break;
				case 2:
					hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).ltAndNeq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
					break;
			}

			perSet.addAll(hcu.list());
		}
		List<Person> l = new ArrayList<Person>(perSet.size());
		l.addAll(perSet);
		Collections.sort(l);
		return makeArray(l);
	}

	@SuppressWarnings("unchecked")
	public static BSearchOutput<BPerson> searchPersons(BSearchMetaInput metaInput, boolean searchEmployees, String statusId,
			String ssn, String firstName, String lastName, int cap, int activeIndicator) throws ArahantException {

		if (true) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class, "person1").eq(Person.RECORD_TYPE, 'R').like(Person.FNAME, firstName).like(Person.LNAME, lastName).setMaxResults(cap);

			if (!isEmpty(ssn))
				hcu.eq(Person.SSN, ssn);

			List<String> orgs = AIProperty.getList("RestrictedOrgGroups");

			if (orgs.size() > 0) {
				Set<String> orgSet = new HashSet<String>(orgs);
				orgSet.addAll(orgs);

				for (String o : orgs)
					orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());

				hcu.joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgSet);
			}

			if (searchEmployees) {
				if (!hsu.currentlyArahantUser())
					if (BRight.checkRight(SEE_ALL_EMPLOYEES_IN_LISTS) != ACCESS_LEVEL_WRITE) {
						List<String> ordGroupIdsWherePrimary = (List) ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).selectFields(OrgGroupAssociation.ORG_GROUP_ID).eq(OrgGroupAssociation.PERSON, ArahantSession.getCurrentPerson()).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').list();//getSubordinateIds(true, true);
						hcu.joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, ordGroupIdsWherePrimary);
					}

				hcu.ne(Person.PERSONID, getPersonByLoginId(ARAHANT_SUPERUSER, hsu).getPersonId());

				switch (activeIndicator) {
					case 0:  //show all, no filter
						hcu.isEmployee();
						break;
					case 1: //active only
						hcu.activeEmployee();

						//attemp at HCU... subquery can't reference person1 table
//						hcu.joinTo(Employee.HREMPLSTATUSHISTORIES, "statusHistories1")
//								.eq("statusHistories1." + HrEmplStatusHistory.EFFECTIVEDATE, hsu.createCriteria(HrEmplStatusHistory.class, "statusHistories2")
//																			.max(HrEmplStatusHistory.EFFECTIVEDATE)
//																			.eqJoinedField(HrEmplStatusHistory.EMPLOYEE_ID, "person1." + Person.PERSONID)
//																			.le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now())
//																			.intValue())
//								.joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS, "status1")
//								.eq("status1." + HrEmployeeStatus.ACTIVE, 'Y')
//								.eq("status1." + HrEmployeeStatus.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany());


						//attempt at HQL... returning zero results
//						DetachedCriteria subquery = DetachedCriteria.forClass(HrEmplStatusHistory.class, "statusHistory2");
//						subquery.setProjection(Projections.max("statusHistory2.effectiveDate"));
//						subquery.add(Restrictions.eq("statusHistory2.employeeId", "person1.personId"));
//						subquery.add(Restrictions.le("statusHistory2.effectiveDate", DateUtils.now()));
//
//						Criteria crit = hcu.getCriteria();
//						crit.createCriteria("person1.hrEmplStatusHistories", "statusHistory1");
//						crit.add(Subqueries.propertyEq("statusHistory1.effectiveDate", subquery));
//						crit.createCriteria("statusHistory1.hrEmployeeStatus", "status1");
//						crit.add(Restrictions.eq("status1." + HrEmployeeStatus.ACTIVE, 'Y'));
//						crit.add(Restrictions.eq("status1." + HrEmployeeStatus.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany()));

						break;
					case 2:  //inactive only
						hcu.inactiveEmployee();
						break;
					case 3:  //specific status id
						hcu.employeeCurrentStatus(statusId);
						break;
				}
			} else {
				if (BRight.checkRight(ACCESS_HR) < ACCESS_LEVEL_READ_ONLY)
					hcu.isEmployee();
				hcu.isNotEmployee();
				//hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);

				switch (activeIndicator) {
					case 0:  //show all, no filter
						hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).in(HrEmplDependent.EMPLOYEE_ID, getSubordinateIds(true, true));
						break;
					case 1: //active only
						hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).in(HrEmplDependent.EMPLOYEE_ID, getSubordinateIds(true, true)).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
						break;
					case 2:
						hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).in(HrEmplDependent.EMPLOYEE_ID, getSubordinateIds(true, true)).ltAndNeq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
						break;
				}

			}
			// lname, fname, middleName, ssn, jobTitle, statusName, type

			switch (metaInput.getSortType()) {
				case 1:
					hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());
					break;
				case 2:
					hcu.orderBy(Person.FNAME, metaInput.isSortAsc());
					break;
				case 3:
					hcu.orderBy(Person.MNAME, metaInput.isSortAsc());
					break;
				case 4:
					hcu.orderBy(Person.SSN, metaInput.isSortAsc());
					break;
				case 5:
					hcu.orderBy(Person.TITLE, metaInput.isSortAsc());
					break;
				default:
					hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());
			}
			return makeSearchOutput(metaInput, hcu);
		} else {
			HibernateSessionUtil hsu = ArahantSession.getHSU();

			HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').like(Person.FNAME, firstName).like(Person.LNAME, lastName);

			if (!isEmpty(ssn))
				hcu.eq(Person.SSN, ssn);

			List<String> orgs = AIProperty.getList("RestrictedOrgGroups");

			if (orgs.size() > 0) {
				Set<String> orgSet = new HashSet<String>(orgs);
				orgSet.addAll(orgs);

				for (String o : orgs)
					orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());

				hcu.joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgSet);
			}

			if (searchEmployees) {
				List<String> subIds = getSubordinateIds(true, true);
				if (!hsu.currentlyArahantUser())
					hcu.in(Person.PERSONID, subIds);
				switch (activeIndicator) {
					case 0:  //show all, no filter
						hcu.isEmployee();
						break;
					case 1: //active only
						hcu.activeEmployee();
						break;
					case 2:  //inactive only
						hcu.inactiveEmployee();
						break;
					case 3:  //specific status id
						hcu.employeeCurrentStatus(statusId);
						break;
				}
			} else {
				if (BRight.checkRight(ACCESS_HR) < ACCESS_LEVEL_READ_ONLY)
					hcu.isEmployee();
				hcu.isNotEmployee();
				hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);

				switch (activeIndicator) {
					case 0:  //show all, no filter
						break;
					case 1: //active only
						hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
						break;
					case 2:
						hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).ltAndNeq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);
						break;
				}
			}
			// lname, fname, middleName, ssn, jobTitle, statusName, type

			switch (metaInput.getSortType()) {
				case 1:
					hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());
					break;
				case 2:
					hcu.orderBy(Person.FNAME, metaInput.isSortAsc());
					break;
				case 3:
					hcu.orderBy(Person.MNAME, metaInput.isSortAsc());
					break;
				case 4:
					hcu.orderBy(Person.SSN, metaInput.isSortAsc());
					break;
				case 5:
					hcu.orderBy(Person.TITLE, metaInput.isSortAsc());
					break;
				default:
					hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());

			}
			return makeSearchOutput(metaInput, hcu);
		}
	}

	public static BSearchOutput<BPerson> searchPersons(BSearchMetaInput metaInput, int cap) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class);

		// lname, fname, middleName, ssn, jobTitle, statusName, type

		switch (metaInput.getSortType()) {
			case 1:
				hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());
				break;
			case 2:
				hcu.orderBy(Person.FNAME, metaInput.isSortAsc());
				break;
			default:
				hcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc()).orderBy(Person.MNAME, metaInput.isSortAsc());
		}
		return makeSearchOutput(metaInput, hcu);
	}

	private static BSearchOutput<BPerson> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Person> hcu) {
		BSearchOutput<BPerson> ret = new BSearchOutput<BPerson>(searchMeta);

		HibernateScrollUtil<Person> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
//		ret.setItems(BPerson.makeArray(scr));
		BPerson[] temp = BPerson.makeArray(scr);
		List<Person> tempDistinct = new ArrayList<Person>();

		for (BPerson bPerson : temp)
			if (!tempDistinct.contains(bPerson.getPerson()))
				tempDistinct.add(bPerson.getPerson());
		ret.setItems(BPerson.makeArray(tempDistinct));

		return ret;
	}

	public BProject[] getProjectsWhereSubjectPerson() {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).eq(Project.SUBJECT_PERSON, person).orderByDesc(Project.DATEREPORTED).orderByDesc(Project.TIME_REPORTED);
		String restricted = AIProperty.getValue("RestrictedProjectType");
		if (!isEmpty(restricted)) {
			List<String> ids = new LinkedList<String>();
			StringTokenizer stok = new StringTokenizer(restricted, ",");
			while (stok.hasMoreTokens())
				ids.add(stok.nextToken());
			hcu.joinTo(Project.PROJECTTYPE).in(ProjectType.PROJECTTYPEID, ids);
		}
		return BProject.makeArray(hcu.list());
	}

	public Address getHomeAddress() {
		if (homeAddress != null)
			return homeAddress;
		homeAddress = getAddress(person, ADDR_HOME);
		if (homeAddress == null) {
			homeAddress = new Address();
			homeAddress.setAddressId(IDGenerator.generate(homeAddress));
			homeAddress.setPerson(person);
			homeAddress.setAddressType(ADDR_HOME);
			//	hsu.insert(homeAddress);
		}
		return homeAddress;
	}

	public Address getWorkAddress() {
		if (workAddress != null)
			return workAddress;

		workAddress = getAddress(person, ADDR_WORK);
		if (workAddress == null) {
			workAddress = new Address();
			workAddress.setAddressId(IDGenerator.generate(workAddress));
			workAddress.setPerson(person);
			workAddress.setAddressType(ADDR_WORK);
			//	hsu.insert(homeAddress);
		}
		return workAddress;
	}

	public String getCity() {
		return getHomeAddress().getCity();
	}

	public String getState() {
		return getHomeAddress().getState();
	}

	public String getCountry() {
		return getHomeAddress().getCountry();
	}

	public void setCountry(String c) {
		getHomeAddress().setCountry(c);
	}

	public void setCounty(String c) {
		getHomeAddress().setCounty(c);
	}

	public String getCounty() {
		return getHomeAddress().getCounty();
	}

	public String getStreet() {
		return getHomeAddress().getStreet();
	}

	public String getZip() {
		return getHomeAddress().getZip();
	}

	public String getStreet2() {
		return getHomeAddress().getStreet2();
	}

	public void setStreet2(final String street2) {
		getHomeAddress().setStreet2(street2);
	}

	public void setStreet(final String street) {
		getHomeAddress().setStreet(street);
	}

	public void setBStreet2(final String street2) {
		getWorkAddress().setStreet2(street2);
	}

	public void setBStreet(final String street) {
		getWorkAddress().setStreet(street);
	}

	public void setCity(final String city) {
		getHomeAddress().setCity(city);
	}

	public void setState(final String state) {
		getHomeAddress().setState(state);
	}

	public void setZip(final String zip) {
		getHomeAddress().setZip(zip);
	}

	public void setBCity(final String city) {
		getWorkAddress().setCity(city);
	}

	public void setBState(final String state) {
		getWorkAddress().setState(state);
	}

	public void setBZip(final String zip) {
		getWorkAddress().setZip(zip);
	}

	/**
	 * @throws ArahantDeleteException
	 *
	 */
	public void deleteExpiredBenefits() throws ArahantException {
		BHRBenefitJoin.deleteExpiredBenefitsReferencingPerson(this);
	}

	/**
	 * @param showApproved
	 * @param showDecline
	 * @param showNotApproved
	 * @return
	 */
	public BHRBenefitJoin[] listBenefitConfigs(boolean showApproved, boolean showDecline, boolean showNotApproved) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, person).geOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0);

		List<HrBenefitJoin> hbjlst = new ArrayList<HrBenefitJoin>();
		hbjlst.addAll(hcu.list());
//		for(HrBenefitJoin j : hcu.list())
//		{
//			BHRBenefitJoin bj = new BHRBenefitJoin(j);
//			if(bj.getEmployeeCovered().equals("N"))
//			{
//				if(bj.getDependentBenefitJoins().size() >= 1)
//				{
//					l.add(bj.getDependentBenefitJoins().get(0));
//				}
//			}
//			else
//			{
//				l.add(j);
//			}
//		}


		List<HrBenefitJoin> hbjlst2 = new ArrayList<HrBenefitJoin>(hbjlst.size());

		for (HrBenefitJoin ebj : hbjlst) {

			try {
				BEmployee emp = new BEmployee(ebj.getPayingPersonId());
				//if the employee has not gone through the wizard or has unfinished changes, and this join is unapproved, don't show it
				if ((emp.getBenefitWizardStatus().equals(Employee.BENEFIT_WIZARD_STATUS_UNFINALIZED + "") && ebj.getBenefitApproved() == 'N'))
					continue;
			} catch (Exception e) {
				//probably not an employee... do nothing
			}

			if (ebj.getBenefitApproved() == 'N' && !showNotApproved)
				continue;

			if (ebj.getBenefitDeclined() == 'Y' && ebj.getBenefitApproved() == 'Y' && !showDecline)
				continue;

			if (ebj.getBenefitApproved() == 'Y' && ebj.getBenefitDeclined() == 'N' && !showApproved)
				continue;

			if (!ebj.getCoveredPersonId().equals(ebj.getPayingPersonId()))
				if (ebj.getCoverageEndDate() != 0 && ebj.getCoverageEndDate() < DateUtils.now())
					continue;

			hbjlst2.add(ebj);
		}

		//primary sort on not yet approved, approved benefit, approved decline ... secondary sort on coverage configuration name
		Collections.sort(hbjlst2);

		return BHRBenefitJoin.makeArray(hbjlst2);
	}

	public BHRBenefitCategory[] listBenefitCats(boolean forDecline, boolean noFilter) {
		BHRBenefitCategory[] ret;
		if (!forDecline) {
			HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.DESCRIPTION);
			String restricted = AIProperty.getValue("RestrictedBenefitCategory");
			if (noFilter)
				restricted = null;
			if (!isEmpty(restricted)) {
				List<String> ids = new LinkedList<String>();
				StringTokenizer stok = new StringTokenizer(restricted, ",");
				while (stok.hasMoreTokens())
					ids.add(stok.nextToken());
				hcu.in(HrBenefitCategory.BENEFIT_CATEGORY_ID, ids);
			}
			ret = BHRBenefitCategory.makeArray(hcu.list());
		} else {
			List<String> ids = new LinkedList<String>();
			String restricted = AIProperty.getValue("RestrictedBenefitCategory");
			if (noFilter)
				restricted = null;
			if (!isEmpty(restricted)) {

				StringTokenizer stok = new StringTokenizer(restricted, ",");
				while (stok.hasMoreTokens())
					ids.add(stok.nextToken());
			}

			HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.DESCRIPTION).eq(HrBenefitCategory.REQUIRES_DECLINE, 'Y');

			if (ids.size() > 0)
				hcu.in(HrBenefitCategory.BENEFIT_CATEGORY_ID, ids);

			final List<HrBenefitCategory> l = hcu.list();


			hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).distinct().orderBy(HrBenefitCategory.DESCRIPTION);

			if (ids.size() > 0)
				hcu.in(HrBenefitCategory.BENEFIT_CATEGORY_ID, ids);

			l.addAll(hcu.joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.REQUIRES_DECLINE, 'Y').list());


			ret = BHRBenefitCategory.makeArray(l);

		}
		return ret;
	}

	public HrBenefitJoin getApprovedBenefitJoinOf(final BHRBenefitCategory beneCat) {
		for (final HrBenefitJoin ebj : person.getHrBenefitJoinsWhereCovered())
			try {
				if (ebj.getHrBenefitConfig().getHrBenefitCategory().equals(beneCat.bean) && ebj.getBenefitApproved() == 'Y' && (ebj.getCoverageEndDate() > DateUtils.now() || ebj.getCoverageEndDate() == 0))
					return ebj;
			} catch (final Exception e) {
				// ignore
			}
		return null;
	}

	public BHRBenefitJoin getCurrentBenefitForNewBenefit(final String benefitConfigId) throws ArahantException {
		HrBenefitJoin beneJoin = null;
		ArrayList<HrBenefitJoin> candidateBenefitJoins = new ArrayList<HrBenefitJoin>();

		// look for all assigned configs in the same benefit category so long as the
		// category is an exclusive category
		candidateBenefitJoins.addAll(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, person).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.EXCLUSIVE, 'Y').joinTo(HrBenefitCategory.HRBENEFIT).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId).list());

		// make sure a possible exact match is in there for the benefits that are not in exclusive categories ... 
		// date filtering below should correct any anomalies with this (e.g. benefit join in there twice, exact
		// match is future dated so a current non-exact match should be the current, etc)
		candidateBenefitJoins.addAll(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, person).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId).list());

		// find the one that is currently active or will become active first
		for (HrBenefitJoin candidateBenefitJoin : candidateBenefitJoins)
			if (beneJoin == null)
				beneJoin = candidateBenefitJoin;
			else if (candidateBenefitJoin.getPolicyStartDate() < beneJoin.getPolicyStartDate())
				beneJoin = candidateBenefitJoin;

		/*
		 * THIS OLD CODE WOULD CHECK FOR EACH JOIN (FROM CAT, FROM BEN, OR FROM
		 * CONF), BUT THE CAT AND BEN WOULD BE DECLINES, WHICH WE DON'T
		 * CURRENTLY CARE ABOUT
		 */

		/*
		 * HrBenefitJoin beneJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .eq(HrBenefitJoin.COVERED_PERSON,
		 * person) .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
		 * .joinTo(HrBenefitConfig.HR_BENEFIT)
		 * .joinTo(HrBenefit.BENEFIT_CATEGORY)
		 * .joinTo(HrBenefitCategory.HRBENEFIT)
		 * .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
		 * .eq(HrBenefitConfig.BENEFIT_CONFIG_ID,benefitConfigId) .first();
		 *
		 * if (beneJoin==null) {
		 * beneJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .eq(HrBenefitJoin.COVERED_PERSON,
		 * person) .joinTo(HrBenefitJoin.HRBENEFIT)
		 * .joinTo(HrBenefit.BENEFIT_CATEGORY) .eq(HrBenefitCategory.EXCLUSIVE,
		 * 'Y') .joinTo(HrBenefitCategory.HRBENEFIT)
		 * .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
		 * .eq(HrBenefitConfig.BENEFIT_CONFIG_ID,benefitConfigId) .first(); }
		 *
		 * if (beneJoin==null) {
		 * beneJoin=hsu.createCriteria(HrBenefitJoin.class)
		 * .eq(HrBenefitJoin.APPROVED, 'Y') .eq(HrBenefitJoin.COVERED_PERSON,
		 * person) .joinTo(HrBenefitJoin.HR_BENEFIT_CATEGORY)
		 * .eq(HrBenefitCategory.EXCLUSIVE, 'Y')
		 * .joinTo(HrBenefitCategory.HRBENEFIT)
		 * .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
		 * .eq(HrBenefitConfig.BENEFIT_CONFIG_ID,benefitConfigId) .first(); }
		 */

		return beneJoin == null ? null : new BHRBenefitJoin(beneJoin);
	}

	public List<HrBenefitJoin> changeBenefitTo(final String benefitConfigId, boolean useCobra, boolean skipValidation) throws ArahantException {
		return changeBenefitTo(benefitConfigId, useCobra, skipValidation, true);
	}

	public List<HrBenefitJoin> changeBenefitTo(final String benefitConfigId, boolean useCobra, boolean skipValidation, boolean insert) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<HrBenefitJoin> returnJoins = new ArrayList<HrBenefitJoin>();
		BHRBenefitJoin bBenfitJoin = this.getCurrentBenefitForNewBenefit(benefitConfigId);
		HrBenefitJoin beneJoin = bBenfitJoin.bean;

		// create the new benefit join (delay saving so we can safely delete old one)
		final BHRBenefitJoin ebj = new BHRBenefitJoin();
		ebj.create();
		ebj.setAmountCovered(beneJoin.getAmountCovered());
		ebj.setHrBenefitConfig(hsu.get(HrBenefitConfig.class, benefitConfigId));
		ebj.setCoveredPerson(person);
		ebj.setPayingPerson(person);
		ebj.setPolicyEndDate(0);
		ebj.setPolicyStartDate(beneJoin.getPolicyStartDate());
		ebj.setCoverageEndDate(0);
		ebj.setCoverageStartDate(beneJoin.getCoverageStartDate());
		ebj.setInsuranceId(beneJoin.getInsuranceId());
		ebj.setAmountPaid(beneJoin.getAmountPaid());
		ebj.setUsingCOBRA(useCobra ? 'Y' : 'N');

		// is our benefit join a policy benefit join?
		BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin(beneJoin);
		if (bBenefitJoin.isPolicyBenefitJoin()) {
			ArrayList<BHRBenefitJoin> dependentJoins = new ArrayList<BHRBenefitJoin>();

			// yes, so copy in the dependent benefit joins for active dependents
			// (delay saving so we can safely delete old ones)
			for (final HrBenefitJoin olddbj : bBenefitJoin.getDependentBenefitJoins()) {
				HrEmplDependent dependent = olddbj.getRelationship();

				System.out.println(olddbj.getCoveredPerson().getNameLFM());

				// skip if no dependent found, dependent is inactive as of now, or dependent has coverage ended as of now
				if (dependent == null || (dependent.getDateInactive() != 0 && dependent.getDateInactive() <= DateUtils.now())
						|| (olddbj.getCoverageEndDate() != 0 && olddbj.getCoverageEndDate() <= DateUtils.now()))
					continue;

				// this check in theory is pointless ... there should never be a benefit join for a dependent
				// that does not have a coverage start date, but check anyway
				if (olddbj.getCoverageStartDate() != 0) {
					final BHRBenefitJoin dbj = new BHRBenefitJoin();
					dbj.create();
					dbj.copyFrom(ebj);
					dbj.setPayingPerson(ebj.getBean().getPayingPerson());
					dbj.setHrBenefitConfig(ebj.getBean().getHrBenefitConfig());
					dbj.setPolicyStartDate(ebj.getPolicyStartDate());
					dbj.setCoveredPersonId(olddbj.getCoveredPersonId());
					//dbj.setRelationship(olddbj.getRelationship());
					dbj.setRelationship(new BHREmplDependent(olddbj.getRelationshipId()).getEmplDependent());
					dbj.setAmountCovered(olddbj.getAmountCovered());
					dbj.setCoverageEndDate(olddbj.getCoverageEndDate());
					dbj.setCoverageStartDate(olddbj.getCoverageStartDate());
					dbj.setUsingCOBRA(useCobra ? 'Y' : 'N');
					dependentJoins.add(dbj);

					System.out.println("Saving for " + ebj.getPayingPersonId() + " " + ebj.getPolicyStartDate() + " " + ebj.getBenefitConfig().getId());
				}
			}

			ebj.setChangeReason(bBenefitJoin.getBenefitChangeReasonId());
			// good to delete old policy and dependent benefit joins, new ones are not in play yet
			if (insert) {
				hsu.delete(bBenefitJoin.getDependentBenefitJoins());
				hsu.delete(beneJoin);
			}

			// now insert out the new policy and dependent benefit joins
			if (insert) {
				ebj.insert(skipValidation);
				for (BHRBenefitJoin dependentJoin : dependentJoins)
					dependentJoin.insert();
			} else {
				if (isEmpty(ebj.getBenefitChangeReasonId()) && ebj.getLifeEvent() == null) //make sure any temporary joins have a reason
					ebj.setChangeReason(hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).first().getHrBenefitChangeReasonId());
				for (BHRBenefitJoin dependentJoin : dependentJoins)
					if (isEmpty(dependentJoin.getBenefitChangeReasonId()) && dependentJoin.getLifeEvent() == null) //make sure any temporary joins have a reason
						dependentJoin.setChangeReason(hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).first().getHrBenefitChangeReasonId());
			}
			for (BHRBenefitJoin depJ : dependentJoins)
				returnJoins.add(depJ.bean);
		} else {
			// good to delete old policy benefit join, new one is not in play yet 
			hsu.delete(beneJoin);

			if (insert)
				ebj.insert(skipValidation);
			else if (isEmpty(ebj.getBenefitChangeReasonId()) && ebj.getLifeEvent() == null) //make sure any temporary joins have a reason
				ebj.setChangeReason(hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).first().getHrBenefitChangeReasonId());
		}
		returnJoins.add(ebj.bean);
		return returnJoins;
	}

	/**
	 * @param configId
	 * @return
	 * @throws ArahantException
	 */
	public int getNextBenefitStartDate(String configId) throws ArahantException {
		//if this benefit allows multiples next start date is now
		BHRBenefitConfig conf = new BHRBenefitConfig(configId);
		if (conf.bean.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
			return 0;

		//Do I have a benefit that has ended in this category?
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, person).ne(HrBenefitJoin.POLICY_END_DATE, 0).orderByDesc(HrBenefitJoin.POLICY_START_DATE);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, conf.bean.getHrBenefitCategory());

		HrBenefitJoin ebj = hcu.first();

		if (ebj != null)
			return DateUtils.add(ebj.getPolicyEndDate(), 1);

		return 0; //give up and return now
	}

	public HrBenefitJoin assignBenefitCategories(final String benefitId, final double amount, final int effectiveDate, boolean useCobra, String changeReasonId, boolean skipValidation, boolean insert) throws ArahantException {
		return assignBenefit(ArahantSession.getHSU().get(HrBenefitConfig.class, benefitId), effectiveDate, amount, useCobra, changeReasonId, skipValidation, insert);
	}

	public HrBenefitJoin assignBenefitCategories(final String benefitId, final double amount, final int effectiveDate, boolean useCobra, String changeReasonId, boolean skipValidation) throws ArahantException {
		return assignBenefit(ArahantSession.getHSU().get(HrBenefitConfig.class, benefitId), effectiveDate, amount, useCobra, changeReasonId, skipValidation, true);
	}

	protected HrBenefitJoin assignBenefit(final HrBenefitConfig bene, final int startDate, String changeReasonId, boolean skipValidation) throws ArahantException {
		return assignBenefit(bene, startDate, 0, false, changeReasonId, skipValidation, true);
	}

	private HrBenefitJoin assignBenefit(final HrBenefitConfig bene, final int startDate, final double amount, boolean useCobra, String changeReasonId, boolean skipValidation, boolean insert) throws ArahantException {
		final BHRBenefitJoin bBenefitJoin = new BHRBenefitJoin();
		bBenefitJoin.create();

		bBenefitJoin.setCoveredPersonId(person.getPersonId());
		bBenefitJoin.setPayingPersonId(person.getPersonId());
		bBenefitJoin.setBenefitConfigId(bene.getBenefitConfigId());
		bBenefitJoin.setPolicyEndDate(0);
		bBenefitJoin.setInsuranceId("");
		bBenefitJoin.setAmountPaid(amount);
		bBenefitJoin.setPolicyStartDate(startDate);
		bBenefitJoin.setCoverageStartDate(bene.getEmployee() == 'Y' ? startDate : 0);
		bBenefitJoin.setUsingCOBRA(useCobra);
		bBenefitJoin.setChangeReason(changeReasonId);
		if (insert)
			bBenefitJoin.insert(skipValidation);
		else if (isEmpty(bBenefitJoin.getBenefitChangeReasonId()) && bBenefitJoin.getLifeEvent() == null) { //make sure any temporary joins have a reason
			HrBenefitChangeReason cr = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).first();
			if (cr != null)
				bBenefitJoin.setChangeReason(cr.getHrBenefitChangeReasonId());
		}
		return bBenefitJoin.bean;
	}

	/**
	 * @return @throws ArahantException
	 */
	public String getStatus() throws ArahantException {
		if (isEmployee())
			return new BEmployee(this).getLastStatusName();

		for (HrEmplDependent dep : person.getDepJoinsWhereDependent())
			if (dep.getDateInactive() == 0)
				return "Active";

		for (HrEmplDependent dep : person.getDepJoinsWhereDependent())
			if (dep.getDateInactive() >= DateUtils.now())
				return "Active (until " + DateUtils.getDateFormatted(dep.getDateInactive()) + ")";

		for (HrEmplDependent dep : person.getDepJoinsWhereDependent())
			if (dep.getDateInactive() < DateUtils.now())
				return "Inactive (as of " + DateUtils.getDateFormatted(dep.getDateInactive()) + ")";

		return "";
	}

	/**
	 * @return @throws ArahantException
	 */
	public BPersonNote[] listNotes() throws ArahantException {

//		 DAVID TO REWORK THIS IF A BETTER SOLUTION IS NEEDED

		// get all possible note categories
		final List<HrNoteCategory> categories = ArahantSession.getHSU().getAll(HrNoteCategory.class);

		// get notes for this employee
		final Person e = person;
		final List<PersonNote> notes = ArahantSession.getHSU().createCriteria(PersonNote.class).eq(PersonNote.PERSON, e).list();


		LinkedList<BPersonNote> cats = new LinkedList<BPersonNote>();
		// spin through the categories and determine if a note exists
		HrNoteCategory category;
		PersonNote note;
		BPersonNote endt;
		for (HrNoteCategory hrNoteCategory : categories) {
			category = hrNoteCategory;
			endt = null;

			// look for this category as a note
			for (int idx2 = 0; idx2 < notes.size(); idx2++) {
				note = notes.get(idx2);

				// check candidate
				if (category.getCatId().equals(note.getHrNoteCategory().getCatId())) {
					// we got a matching note
					endt = new BPersonNote(note);
					notes.remove(idx2);
					break;
				}
			}

			// did we get a matching note?
			if (endt == null) {
				if (category.getLastActiveDate() != 0 && category.getLastActiveDate() < DateUtils.now())
					continue;

				if (DateUtils.now() < category.getFirstActiveDate())
					continue;
				// no so make an "empty" one
				endt = new BPersonNote();
				endt.stub();
				endt.setPersonId(getPersonId());
				endt.setNote("");
				endt.setNoteCategoryId(category.getCatId());
				endt.setNoteId("");
			}

			cats.add(endt);
		}

		return cats.toArray(new BPersonNote[0]);
	}

	/**
	 * @param note
	 * @throws ArahantException
	 */
	public void setNote(String note) throws ArahantException {
		PersonNote pn = ArahantSession.getHSU().createCriteria(PersonNote.class).eq(PersonNote.PERSON, person).joinTo(PersonNote.HRNOTECATEGORY).eq(HrNoteCategory.CATID, "00001-0000000000").first();
		if (pn == null) {
			BPersonNote bpn = new BPersonNote();
			bpn.create();
			bpn.setNote(note);
			bpn.setNoteCategoryId("00001-0000000000");
			bpn.setPersonId(person.getPersonId());
			bpn.insert();
		} else {
			BPersonNote bpn = new BPersonNote(pn);
			bpn.setNote(note);
			bpn.update();
		}
	}

	public Person getPerson() {
		return person;
	}

	public String getPositionId() {
		try {
			if (!isEmployee())
				return "";

			Employee emp = (Employee) person;
			HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, emp).orderByDesc(HrWage.EFFECTIVEDATE).first();
			if (w == null)
				return "";

			return w.getHrPosition().getPositionId();
		} catch (Exception e) {
			return "";
		}
	}

	public String getWageTypeId() {
		try {
			if (!isEmployee())
				return "";

			Employee emp = (Employee) person;
			HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, emp).orderByDesc(HrWage.EFFECTIVEDATE).first();
			if (w == null)
				return "";

			return w.getWageType().getWageTypeId();
		} catch (Exception e) {
			return "";
		}
	}

	public double getWageAmount() {
		try {
			if (!isEmployee())
				return 0;

			Employee emp = (Employee) person;
			HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, emp).ne(HrWage.WAGEAMOUNT, 0.0).orderByDesc(HrWage.EFFECTIVEDATE).first();
			if (w == null)
				return 0;

			return w.getWageAmount();
		} catch (Exception e) {
			return 0;
		}
	}

	public String getSubGroupId() {
		try {
			return ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).orderBy(OrgGroupAssociation.ORG_GROUP_ID).first().getOrgGroupId();
			//return getOrgGroupAssociations().iterator().next().getOrgGroupId();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param positionId
	 */
	public void setWageAndPosition(String positionId, String wageTypeId, double wageAmount, int effectiveDate) {
		if (!isEmployee())
			throw new ArahantWarning("Can not set position for a non-employee.");

		DecimalFormat formatter = new DecimalFormat("#.##");
		boolean samePosition = (isEmpty(getPositionId()) && isEmpty(positionId)) || getPositionId().equals(positionId);
		boolean sameWageType = (isEmpty(getWageTypeId()) && isEmpty(wageTypeId)) || getWageTypeId().equals(wageTypeId);
		boolean sameWage = formatter.format(getWageAmount()).equals(formatter.format(wageAmount));

		if (samePosition && sameWageType && sameWage)
			return;

		ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EFFECTIVEDATE, effectiveDate).joinTo(HrWage.EMPLOYEE).eq(Employee.PERSONID, person.getPersonId()).delete();
		BHRWage wage = new BHRWage();
		wage.create();
		wage.setEmployeeId(person.getPersonId());
		wage.setEffectiveDate(effectiveDate);
		wage.setPositionId(positionId);
		wage.setWageAmount(wageAmount);
		wage.setWageTypeId(wageTypeId);
		wage.insert();
	}

	public void setOrgGroupId(String orgGroupId) {
		BOrgGroup og = new BOrgGroup(orgGroupId);

		//make sure they aren't already in there before I create one
		if (!ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, person).eq(OrgGroupAssociation.ORGGROUP, og.orgGroup).exists())
			og.assignPeopleToGroup(new String[]{person.getPersonId()});
	}

	public boolean getHandicap() {
		return person.getHandicap() == 'Y';
	}

	public boolean getStudent() {
		return person.getStudent() == 'Y';
	}

	public String getCitizenship() {
		return person.getCitizenship();
	}

	public String getVisa() {
		return person.getVisa();
	}

	public int getVisaStatusDate() {
		return person.getVisaStatusDate();
	}

	public int getVisaExpirationDate() {
		return person.getVisaExpirationDate();
	}

	public boolean getI9Part1() {
		return person.getI9Part1() == 'Y';
	}

	public boolean getI9Part2() {
		return person.getI9Part2() == 'Y';
	}

	public BCompanyBase getCompany() {
		//	if (hsu.getCurrentPerson().getOrgGroupAssociations().size()==0 && hsu.currentlySuperUser())
		//	{
		if (person.getCompanyBase() != null)
			return BCompanyBase.get(person.getCompanyBase().getOrgGroupId());
		else
			return BCompanyBase.get(ArahantSession.getHSU().getCurrentCompany().getOrgGroupId());
		//	}
		//logger.info(hsu.getCurrentPerson().getOrgGroupAssociations().size());
		//	return	BCompanyBase.get(hsu.getCurrentPerson().getOrgGroupAssociations().iterator().next().getOrgGroup().getOwningCompany().getOrgGroupId());
	}

	public BProject[] listAssignedProjects(int cap) {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).setMaxResults(cap);
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).orderBy(ProjectEmployeeJoin.EMPLOYEE_PRIORITY).eq(ProjectEmployeeJoin.PERSON, person);
		hcu.orderBy(Project.PROJECTNAME);
		return BProject.makeArray(hcu.list());
	}

	public String getId() {
		return person.getPersonId();
	}

	public int getProjectTypeCount(BProjectType pt) {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).eq(Project.PROJECTTYPE, pt.getBean());
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');

		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, person);

		return hcu.count();
	}

	public BProject[] listProjects(final int cap, int start, String projectTypeId, String locationId) {
		//this should just be active projects

		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).orderBy(Project.PROJECTID);
		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, person);
		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.PROJECTTYPE).eq(ProjectType.PROJECTTYPEID, projectTypeId);

		if (!isEmpty(locationId)) {
			Set<String> orgIds = new HashSet<String>();
			orgIds.addAll(new BOrgGroup(locationId).getAllOrgGroupsInHierarchy());
			hcu.joinTo(Project.DONE_FOR_PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds);
		}
		return BProject.makeArray(hcu.getPage(start, cap));
	}

	/**
	 * @param cap
	 * @param start
	 * @param projectTypeId
	 * @return
	 */
	public BProject[] listProjectsForGroup(final int cap, int start, String projectTypeId, String locationId) {
		final HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).distinct().setMaxResults(cap).orderBy(Project.PROJECTID);

		hcu.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
		hcu.joinTo(Project.PROJECTTYPE).eq(ProjectType.PROJECTTYPEID, projectTypeId);
		/*
		 *
		 * hcu.joinTo(Project.CURRENT_ROUTE_STOP) .joinTo(RouteStop.ORG_GROUP)
		 * .joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
		 * .eq(OrgGroupAssociation.MAIN_CONTACT, employee);
		 */

		hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).joinTo(ProjectEmployeeJoin.PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).joinTo(OrgGroup.ORGGROUPASSOCIATIONS, "oga").eq("oga." + OrgGroupAssociation.PERSON, person);

		if (!isEmpty(locationId)) {
			Set<String> orgIds = new HashSet<String>();
			orgIds.addAll(new BOrgGroup(locationId).getAllOrgGroupsInHierarchy());
			hcu.joinTo(Project.DONE_FOR_PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds);
		}
		return BProject.makeArray(hcu.list());
	}

	public void setDefaultProjectId(String defaultProjectId) {
		Project project = null;

		if (!isEmpty(defaultProjectId))
			project = ArahantSession.getHSU().get(Project.class, defaultProjectId);

		person.setDefaultProject(project);
	}

	public String getDefaultProjectId() {
		Project defaultProject = this.getDefaultProject();

		if (defaultProject == null)
			return "";
		else
			return defaultProject.getProjectId();
	}

	public String getExplicitProjectId() {
		if (person.getDefaultProject() == null)
			return "";
		return person.getDefaultProject().getProjectId();
	}

	public Project getDefaultProject() {
		if (person.getDefaultProject() == null)
			//check the org group
			if (person.getOrgGroupAssociations().size() > 0) {
				BOrgGroup borg = new BOrgGroup(person.getOrgGroupAssociations().iterator().next().getOrgGroup());
				return borg.getDefaultProject();
			}
		return person.getDefaultProject();
	}

	public String getDefaultProjectName() {
		if (person.getDefaultProject() == null)
			return "";
		return new BProject(person.getDefaultProject()).getProjectName();
	}

	public String getSchoolTermName() {
		if (person.getStudentCalendarType() == 'S')
			return "Semester";
		if (person.getStudentCalendarType() == 'Q')
			return "Quarter";
		return "";
	}

	public BPersonForm[] getForms() {
		return BPersonForm.list(person.getPersonId());
	}

	public short getHeight() {
		return person.getHeight();
	}

	public void setHeight(short height) {
		person.setHeight(height);
	}

	public short getWeight() {
		return person.getWeight();
	}

	public void setWeight(short weight) {
		person.setWeight(weight);
	}

	public char getSmokingProgram() {
		return person.getSmokingProgram();
	}

	public void setSmokingProgram(char smokingProgram) {
		person.setSmokingProgram(smokingProgram);
	}

	public char getMilitaryBranch() {
		return person.getMilitaryBranch();
	}

	public void setMilitaryBranch(char militaryBranch) {
		person.setMilitaryBranch(militaryBranch);
	}

	public String getMilitaryDischargeExplain() {
		return person.getMilitaryDischargeExplain();
	}

	public void setMilitaryDischargeExplain(String militaryDischargeExplain) {
		person.setMilitaryDischargeExplain(militaryDischargeExplain);
	}

	public char getMilitaryDischargeType() {
		return person.getMilitaryDischargeType();
	}

	public void setMilitaryDischargeType(char militaryDischargeType) {
		person.setMilitaryDischargeType(militaryDischargeType);
	}

	public int getMilitaryEndDate() {
		return person.getMilitaryEndDate();
	}

	public void setMilitaryEndDate(int militaryEndDate) {
		person.setMilitaryEndDate(militaryEndDate);
	}

	public String getMilitaryRank() {
		return person.getMilitaryRank();
	}

	public void setMilitaryRank(String militaryRank) {
		person.setMilitaryRank(militaryRank);
	}

	public int getMilitaryStartDate() {
		return person.getMilitaryStartDate();
	}

	public void setMilitaryStartDate(int militaryStartDate) {
		person.setMilitaryStartDate(militaryStartDate);
	}

	public char getConvictedOfCrime() {
		return person.getConvictedOfCrime();
	}

	public void setConvictedOfCrime(char convictedOfCrime) {
		person.setConvictedOfCrime(convictedOfCrime);
	}

	public String getConvictedOfWhat() {
		return person.getConvictedOfWhat();
	}

	public void setConvictedOfWhat(String convictedOfWhat) {
		person.setConvictedOfWhat(convictedOfWhat);
	}

	public char getStudentCalendarType() {
		return person.getStudentCalendarType();
	}

	public void setStudentCalendarType(char studentCalendarType) {
		person.setStudentCalendarType(studentCalendarType);
	}

	public char getRecordType() {
		return person.getRecordType();
	}

	public void setRecordType(char recordType) {
		person.setRecordType(recordType);
	}

	public char getWorkedForCompanyBefore() {
		return person.getWorkedForCompanyBefore();
	}

	public void setWorkedForCompanyBefore(char workedForCompanyBefore) {
		person.setWorkedForCompanyBefore(workedForCompanyBefore);
	}

	public String getWorkedForCompanyWhen() {
		return person.getWorkedForCompanyWhen();
	}

	public void setWorkedForCompanyWhen(String workedForCompanyWhen) {
		person.setWorkedForCompanyWhen(workedForCompanyWhen);
	}

	public static String getRandomPassword() {
		int size = 10;

		//if the size < required size, up it
		int minSize = BProperty.getInt("PasswordMinimumLength");
		if (size < minSize)
			size = minSize;

		int minDigits = BProperty.getInt(StandardProperty.PasswordMinimumDigits);
		int minLetters = BProperty.getInt(StandardProperty.PasswordMinimumLetters);
		int minLower = BProperty.getInt(StandardProperty.PasswordMinimumLowerCase);
		int minSpecial = BProperty.getInt(StandardProperty.PasswordMinimumSpecialChars);
		int minUpper = BProperty.getInt(StandardProperty.PasswordMinimumUpperCase);

		if (minUpper + minLower > minLetters)
			minLetters = minUpper + minLower;

		if (size < minLetters + minSpecial + minDigits)
			size = minLetters + minSpecial + minDigits;

		//build a character string of these
		char [] newpass = new char[size];

		int idx = 0;

		for (int loop = 0; loop < minLower; loop++)
			newpass[idx++] = (char) ('a' + Math.rint(Math.random() * 26));

		for (int loop = 0; loop < minUpper; loop++)
			newpass[idx++] = (char) ('A' + Math.rint(Math.random() * 26));

		for (int loop = 0; loop < minDigits; loop++)
			newpass[idx++] = (char) ('0' + Math.rint(Math.random() * 10));

		for (int loop = 0; loop < minSpecial; loop++)
			newpass[idx++] = (char) ('(' + Math.rint(Math.random() * 7));

		//fill the rest with alternating case
		char c = 'A';
		while (idx < newpass.length) {
			newpass[idx++] = (char) (c + Math.rint(Math.random() * 26));
			if (c == 'A')
				c = 'a';
			else
				c = 'A';
		}

		//now run through and random swap them
		for (int loop = 0; loop < newpass.length; loop++) {
			char x = newpass[loop];
			int pos = (int) (Math.random() * newpass.length);
			newpass[loop] = newpass[pos];
			newpass[pos] = x;
		}

		return new String(newpass);
	}

	public String resetPassword() {
		if (isEmpty(person.getPersonalEmail()))
			return "Your email address is not set up in the system, so your password could not be reset.  Please contact your system administrator to reset your password and set up your email address.";

		String newPassword = getRandomPassword();

		setUserPassword(newPassword, person.getProphetLogin().getCanLogin() == 'Y');

		Mail.send(person, person, ArahantSession.systemName() + " Password Reset", BProperty.get("PasswordResetText", "Your " + ArahantSession.systemName() + " system password has been reset to ") + newPassword);

		return "Your password has been reset.  You should be receiving an email with your new password shortly.  You may change this new password after logging in to the system.";
	}

	public static String getSignInDate(String personId) {
		LoginLog l = ArahantSession.getHSU().createCriteria(LoginLog.class).eq(LoginLog.PERSONID, personId).orderByDesc(LoginLog.DATE).first();
		if (l == null)
			return "";
		return DateUtils.getDateFormatted(l.getLtime());
	}

	public static Date getSignIn(String personId) {
		LoginLog l = ArahantSession.getHSU().createCriteria(LoginLog.class).eq(LoginLog.PERSONID, personId).orderByDesc(LoginLog.DATE).first();
		if (l == null)
			return new Date(1900);
		return l.getLtime();
	}

	public static String getSignInDateAndTime(String personId) {
		LoginLog l = ArahantSession.getHSU().createCriteria(LoginLog.class).eq(LoginLog.PERSONID, personId).orderByDesc(LoginLog.DATE).first();
		if (l == null)
			return "";
		return DateUtils.getDateAndTimeFormatted(l.getLtime());
	}

	public BHrEmergencyContact getMainEmerContact() {
		HrEmergencyContact hec = ArahantSession.getHSU().createCriteria(HrEmergencyContact.class).eq(HrEmergencyContact.PERSON, person).orderBy(HrEmergencyContact.SEQNO).first();
		if (hec == null)
			return null;
		return new BHrEmergencyContact(hec);
	}

	public String getSecurityOverrideId(String companyId) {
		String ret = ArahantSession.getHSU().createCriteria(ProphetLoginOverride.class).eq(ProphetLoginOverride.COMPANY, new BCompany(companyId).getBean()).eq(ProphetLoginOverride.PERSON, person).selectFields(ProphetLoginOverride.SECURITY_GROUP_ID).stringVal();
		return (!isEmpty(ret) ? ret : "");
	}

	public String getScreenOverrideId(String companyId) {
		String ret = ArahantSession.getHSU().createCriteria(ProphetLoginOverride.class).eq(ProphetLoginOverride.COMPANY, new BCompany(companyId).getBean()).eq(ProphetLoginOverride.PERSON, person).selectFields(ProphetLoginOverride.SCREEN_GROUP_ID).stringVal();
		return (!isEmpty(ret) ? ret : "");
	}

	public String getActivelyAtWork() {
		return person.getActivelyAtWork() + "";
	}

	public void setActivelyAtWork(char activelyAtWork) {
		person.setActivelyAtWork(activelyAtWork);
	}

	public String getUnableToPerform() {
		return person.getUnableToPerform() + "";
	}

	public void setUnableToPerform(char unableToPerform) {
		person.setUnableToPerform(unableToPerform);
	}

	public char getHasAids() {
		return person.getHasAids();
	}

	public void setHasAids(char hasAids) {
		person.setHasAids(hasAids);
	}

	public char getHasCancer() {
		return person.getHasCancer();
	}

	public void setHasCancer(char hasCancer) {
		person.setHasCancer(hasCancer);
	}

	public char getHasHeartCondition() {
		return person.getHasHeartCondition();
	}

	public void setHasHeartCondition(char hasHeartCondition) {
		person.setHasHeartCondition(hasHeartCondition);
	}

	public String getDefaultEmailSender() {
		return person.getDefaultEmailSender();
	}

	public void setDefaultEmailSender(String yn) {
		person.setDefaultEmailSender(yn);
	}

	/**
	 * This method returns true if there are any pending change requests for the employee or any of their dependents.  
	 * Upon return, if a change request is found, the instance this method is associated with gets initialized to point
	 * to one of those persons having the pending change request.
	 * 
	 * @param personId the person paying for the benefit (usually the employee)
	 * @return true if any change requests are found
	 */
	public boolean hasPending(String personId) {
		HibernateCriteriaUtil<Person> ppHcu = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C');
		HibernateCriteriaUtil<Person> cr = ppHcu.joinTo(Person.CHANGE_REQS);
		cr.eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING);
		cr.joinTo(PersonCR.PERSON).eq(Person.PERSONID, personId);

		person = ppHcu.first();
		return person != null;
	}

	public void loadPending(String personId) {
		loadPending(personId, personId, null);
		realPersonId = personId;
	}

	public void loadPending(String personId, String requestorId, Project project) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<Person> ppHcu = hsu.createCriteria(Person.class);//.eq(Person.RECORD_TYPE, 'C');
		HibernateCriteriaUtil<Person> cr = ppHcu.joinTo(Person.CHANGE_REQS);
		cr.eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING);
		cr.joinTo(PersonCR.PERSON).eq(Person.PERSONID, personId);

		person = ppHcu.first();
//        person = hsu.createCriteria(Person.class)
//												.joinTo(Person.CHANGE_REQS)
//												.eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING)
//												.joinTo(PersonCR.PERSON)
//												.eq(Person.PERSONID, personId).first();

		if (person == null) {
			create();
			Person p = hsu.get(Person.class, personId);
			HibernateSessionUtil.copyCorresponding(person, p, Person.PERSONID);
			//did we change anything that needs to be saved??
			List<String> changes = compareChanges(person);
			if (changes.isEmpty())
				return;
			person.setRecordType('C');
			insert();

			pcr = new PersonCR();
			pcr.generateId();
			pcr.setRealRecord(p);
			pcr.setChangeRecord(person);
			pcr.setChangeStatus(PersonCR.STATUS_PENDING);
			pcr.setRequestor(hsu.get(Person.class, requestorId));
			pcr.setRequestTime(new Date());

			if (new BPerson(p).isEmployee())
				try {
					BWizardConfiguration wizConf = new BPerson(p).getBEmployee().getWizardConfiguration("E");
					//hopefully i already have a wizard project created
					project = hsu.createCriteria(Project.class).eq(Project.PROJECTSTATUS, wizConf.getProjectStatus()).eq(Project.PROJECTCATEGORY, wizConf.getProjectCategory()).eq(Project.PROJECTTYPE, wizConf.getProjectType()).eq(Project.DONE_FOR_PERSON, p).first();
					if (project == null) {
						BProject bp = new BProject();
						bp.create();
						bp.setDoneForPersonId(p.getPersonId());
						bp.setDescription(wizConf.getProjectSummary());
						bp.setDetailDesc("In Progress (not finalized)");
						bp.setProjectCategoryId(wizConf.getProjectCategory().getProjectCategoryId());
						bp.setProjectTypeId(wizConf.getProjectType().getProjectTypeId());
						bp.setProjectStatusId(wizConf.getProjectStatus().getProjectStatusId());
						bp.setRequestingOrgGroupId(hsu.getCurrentCompany().getOrgGroupId());
						bp.setBillable('N');
						bp.insert();
						hsu.flush();
						project = bp.project;
/*  XXYY
						for (WizardConfigurationProjectAssignment pta : hsu.createCriteria(WizardConfigurationProjectAssignment.class).eq(WizardConfigurationProjectAssignment.WIZARD_CONFIG, wizConf.getBean()).list())
							bp.assignPerson(pta.getPersonId(), 10000, false);
						bp.update();
 */
					}

				} catch (ArahantException arahantException) {
					logger.error(arahantException);
				}

			//last resort to  create the project
			if (project == null) {
				//create a project for this
				BProject bp = new BProject();
				bp.create();
				bp.setDoneForPersonId(personId);
				bp.setDescription("Employee Change Request");
				bp.setProjectCategoryId(BProjectCategory.findOrMake("Human Resources"));
				bp.setProjectTypeId(BProjectType.findOrMake("Change Request"));
				bp.setProjectStatusId(BProjectStatus.findOrMake("Requested"));
				bp.setRequestingOrgGroupId(hsu.getCurrentCompany().getOrgGroupId());
				bp.setBillable('N');
				bp.insert();
				/* XXYY
				for (Person supe : BusinessLogicBase.getSupervisors(hsu.getCurrentPerson()))
					bp.assignPerson(supe.getPersonId(), 10, true);
				 */

				pcr.setProject(bp.project);
			} else
				pcr.setProject(project);

			hsu.saveOrUpdate(pcr);
			hsu.flush();
		}
	}

	public void setHandicap(boolean handicap) {
		person.setHandicap(handicap ? 'Y' : 'N');
	}

	public void setStatePending(String state) {
		loadPendingAddress(ADDR_HOME).setState(state);
	}

	public void setStreetPending(String address1) {
		loadPendingAddress(ADDR_HOME).setStreet(address1);
	}

	public void setStreet2Pending(String address2) {
		loadPendingAddress(ADDR_HOME).setStreet2(address2);
	}

	public void setCityPending(String city) {
		loadPendingAddress(ADDR_HOME).setCity(city);
	}

	public void setZipPending(String zip) {
		loadPendingAddress(ADDR_HOME).setZip(zip);
	}

	public void setCountyPending(String county) {
		loadPendingAddress(ADDR_HOME).setCounty(county);
	}

	public void setHomePhonePending(String homePhone) {
		loadPendingPhone(PHONE_HOME).setPhoneNumber(homePhone);
	}

	public void setWorkPhonePending(String workPhone) {
		loadPendingPhone(PHONE_WORK).setPhoneNumber(workPhone);
	}

	public void setMobilePhonePending(String mobilePhone) {
		loadPendingPhone(PHONE_CELL).setPhoneNumber(mobilePhone);
	}

	public void setWorkFaxPending(String fax) {
		loadPendingPhone(PHONE_FAX).setPhoneNumber(fax);
	}

	public String getCountyPending() {
		return loadPendingAddress(ADDR_HOME).getCounty();
	}

	public String getStreetPending() {
		if (loadPendingAddress(ADDR_HOME) == null || loadPendingAddress(ADDR_HOME).getStreet() == null)
			return "";
		return loadPendingAddress(ADDR_HOME).getStreet();
	}

	public String getStreet2Pending() {
		if (loadPendingAddress(ADDR_HOME) == null || loadPendingAddress(ADDR_HOME).getStreet2() == null)
			return "";
		return loadPendingAddress(ADDR_HOME).getStreet2();
	}

	public String getCityPending() {
		if (loadPendingAddress(ADDR_HOME) == null || loadPendingAddress(ADDR_HOME).getCity() == null)
			return "";
		return loadPendingAddress(ADDR_HOME).getCity();
	}

	public String getStatePending() {
		if (loadPendingAddress(ADDR_HOME) == null || loadPendingAddress(ADDR_HOME).getState() == null)
			return "";
		return loadPendingAddress(ADDR_HOME).getState();
	}

	public String getZipPending() {
		if (loadPendingAddress(ADDR_HOME) == null || loadPendingAddress(ADDR_HOME).getZip() == null)
			return "";
		return loadPendingAddress(ADDR_HOME).getZip();
	}

	public String getHomePhonePending() {
		if (loadPendingPhone(PHONE_HOME) == null || loadPendingPhone(PHONE_HOME).getPhoneNumber() == null)
			return "";
		return loadPendingPhone(PHONE_HOME).getPhoneNumber();
	}

	public String getWorkPhoneNumberPending() {
		if (loadPendingPhone(PHONE_WORK) == null || loadPendingPhone(PHONE_WORK).getPhoneNumber() == null)
			return "";
		return loadPendingPhone(PHONE_WORK).getPhoneNumber();
	}

	public String getMobilePhonePending() {
		if (loadPendingPhone(PHONE_CELL) == null || loadPendingPhone(PHONE_CELL).getPhoneNumber() == null)
			return "";
		return loadPendingPhone(PHONE_CELL).getPhoneNumber();
	}

	public String getWorkFaxPending() {
		if (loadPendingPhone(PHONE_FAX) == null || loadPendingPhone(PHONE_FAX).getPhoneNumber() == null)
			return "";
		return loadPendingPhone(PHONE_FAX).getPhoneNumber();
	}

	public void fixChangeRequestProject(String id) {
		BProject bproject = new BProject(id);
		PersonCR cr = getChangeRequest();
		if (cr == null)
			return;
		Project oldProject = cr.getProject();
		if (oldProject != bproject.project) {
			cr.setProject(bproject.project);
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.update(cr);

			try {
				for (HrBenefitJoin j : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PROJECT, oldProject).list()) {
					j.setProject(bproject.project);
					hsu.update(j);
				}
				oldProject.setSubjectPerson(null);
				hsu.update(oldProject);
			} catch (ArahantDeleteException arahantDeleteException) {
				//do nothing
			}
		}
	}

	private PersonCR getChangeRequest() {
		if (pcr != null)
			return pcr;

		pcr = ArahantSession.getHSU().createCriteria(PersonCR.class).eq(PersonCR.PERSON_PENDING, person).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).first();

		if (pcr == null) {
			pcr = ArahantSession.getHSU().createCriteria(PersonCR.class).eq(PersonCR.PERSON, person).eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING).first();
			//throw new ArahantException("Could not find change request with id " + bean.getPersonId());
			if (pcr == null)
				return null;
		}

		//  System.out.println("Found " + bean.getPersonId());
		return pcr;
	}

	public AddressPending loadRealAddress(int addressType) {
		BPerson bperson = new BPerson(realPersonId);
		Address current = bperson.getAddress(bperson.person, addressType);
		AddressPending addpending = new AddressPending();
		if (current != null)
			HibernateSessionUtil.copyCorresponding(addpending, current, Address.ADDRESSID);
		return addpending;
	}

	public AddressPending loadPendingAddress(int addressType) {
		if (addresses.containsKey(addressType))
			return addresses.get(addressType);
		
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		List<String> ids = new ArrayList<String>();

		if (getChangeRequest() == null)
			//need to load real address if no pending
			return loadRealAddress(addressType);

		ids.add(getChangeRequest().getChangeRecordId());

		if (getChangeRequest().getRealRecord() != null)
			ids.add(getChangeRequest().getRealRecord().getPersonId());

		HibernateCriteriaUtil<AddressPending> ppHcu = hsu.createCriteria(AddressPending.class);
		HibernateCriteriaUtil<AddressPending> cr = ppHcu.joinTo(AddressPending.CHANGE_REQUESTS);
		ppHcu.joinTo(AddressPending.PERSON).in(Person.PERSONID, ids);
		cr.eq(AddressChangeRequest.CHANGE_STATUS, AddressChangeRequest.STATUS_PENDING);
		ppHcu.eq(Address.ADDRESSTYPE, addressType);

		AddressPending ap = ppHcu.first();

		if (ap == null) {
			ap = new AddressPending();
			ap.generateId();

			BPerson bp;

			if (getChangeRequest().getRealRecord() != null)
				bp = new BPerson(getChangeRequest().getRealRecord());
			else
				return null;
			
			Address current = bp.getAddress(bp.person, addressType);

			if (current != null)
				HibernateSessionUtil.copyCorresponding(ap, current, Address.ADDRESSID);
			else {
				ap.setPerson(getChangeRequest().getRealRecord());
				ap.setAddressType(addressType);
			}
			ap.setRecordType('C');
			hsu.insert(ap);

			AddressChangeRequest acr = new AddressChangeRequest();
			acr.generateId();
			acr.setRealRecord(current);
			acr.setChangeRecord(ap);
			acr.setChangeStatus(AddressChangeRequest.STATUS_PENDING);
			acr.setRequestor(getChangeRequest().getRequestor());
			acr.setRequestTime(new Date());
			acr.setProject(getChangeRequest().getProject());
			hsu.insert(acr);
		}

		addresses.put(addressType, ap);

		return ap;
	}

	public PhonePending loadRealPhone(int phoneType) {
		BPerson bperson = new BPerson(realPersonId);
		Phone current = bperson.getPhone(bperson.person, phoneType);

		PhonePending phonepending = new PhonePending();
		if (current != null)
			HibernateSessionUtil.copyCorresponding(phonepending, current, Phone.PHONEID);
		return phonepending;
	}

	public PhonePending loadPendingPhone(int phoneType) {
		if (phones.containsKey(phoneType))
			return phones.get(phoneType);

		List<String> ids = new ArrayList<String>();

		//this is a case where we want to editing something
		//but there is no pending record yet for the Person, so we need to get Real
		if (getChangeRequest() == null)
			return loadRealPhone(phoneType);

		ids.add(getChangeRequest().getChangeRecordId());

		if (getChangeRequest().getRealRecord() != null)
			ids.add(getChangeRequest().getRealRecord().getPersonId());

		HibernateCriteriaUtil<PhonePending> ppHcu = ArahantSession.getHSU().createCriteria(PhonePending.class);
		HibernateCriteriaUtil<PhonePending> cr = ppHcu.joinTo(PhonePending.CHANGE_REQUESTS);
		cr.eq(PhoneChangeRequest.CHANGE_STATUS, PhoneChangeRequest.STATUS_PENDING);
		ppHcu.joinTo(PhonePending.PERSON).in(Person.PERSONID, ids);
		ppHcu.eq(Phone.PHONETYPE, phoneType);

		PhonePending ap = ppHcu.first();

		if (ap == null) {
			ap = new PhonePending();
			ap.generateId();

			if (getChangeRequest().getRealRecord() == null)
				return null;

			BPerson bp = new BPerson(getChangeRequest().getRealRecord());
			Phone current = bp.getPhone(bp.person, phoneType);

			if (current != null)
				HibernateSessionUtil.copyCorresponding(ap, current, Phone.PHONEID);
			else {
				ap.setPerson(getChangeRequest().getRealRecord());
				ap.setPhoneType(phoneType);
			}

			ap.setRecordType('C');
			ArahantSession.getHSU().insert(ap);

			PhoneChangeRequest phcr = new PhoneChangeRequest();
			phcr.generateId();
			phcr.setRealRecord(current);
			phcr.setChangeRecord(ap);
			phcr.setChangeStatus(PhoneChangeRequest.STATUS_PENDING);
			phcr.setRequestor(getChangeRequest().getRequestor());
			phcr.setRequestTime(new Date());
			phcr.setProject(getChangeRequest().getProject());
			ArahantSession.getHSU().insert(phcr);
		}

		phones.put(phoneType, ap);

		return ap;
	}

	public BHREmplDependent[] listPendingDependents() {
		//do I already have pendings?
//		List<HrEmplDependentChangeRequest> l=hsu.createCriteria(HrEmplDependentChangeRequest.class)
//			.eq(HrEmplDependentChangeRequest.PROJECT, getChangeRequest().getProject())
//			.joinTo(HrEmplDependentChangeRequest.DEP_PENDING)
//			.geOrEq(HrEmplDependentPending.DATE_INACTIVE, DateUtils.now(), 0)
//			.list();
		if (getChangeRequest() != null) {
			Person[] ids = new Person[]{getChangeRequest().getChangeRecord(), getChangeRequest().getRealRecord()};
			List<HrEmplDependent> lpend = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C').in(HrEmplDependent.EMPLOYEE, ids).list();
			return BHREmplDependent.makeArray(lpend);
		} else
			return BHREmplDependent.makeArray(new ArrayList<HrEmplDependent>());
	}

	@SuppressWarnings("unchecked")
	public BHRBenefitJoin[] listPendingJoins() {
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());
		List<HrBenefitJoin> l = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).ne(HrBenefitJoin.EMPLOYEE_COVERED, 'N').list();

//        Set<HrBenefitJoin> s = new HashSet<HrBenefitJoin>();
//
//        for (HrBenefitJoin bj : l) {
//            if ((bj.getHrBenefitConfig().getHrBenefit().getOpenEnrollmentWizard() == 'N') && (bj.getHrBenefitConfig().getHrBenefitCategory().getOpenEnrollmentWizard() == 'N')) {
//                s.add(bj);
//            }
//        }
//
//        l.removeAll(s);

		return BHRBenefitJoin.makeArray(l);

//		return BHRBenefitJoin.makeArray(hsu.createCriteria(HrBenefitJoin.class)
//			.eq(HrBenefitJoin.PROJECT, getChangeRequest().getProject())
//			.eq(HrBenefitJoin.APPROVED, 'N')
//			.in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds)
//			.list());
	}

	public void apply() {
		apply(true, true);
	}

	public void apply(boolean doDeps) {
		apply(doDeps, true);
	}

	public void apply(boolean doDeps, boolean doDemogs) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		PersonCR cr = getChangeRequest();

		if (cr == null)
			return;
		if (doDemogs) {
			if (cr.getRealRecord() != null) {
				Person changeRec = cr.getChangeRecord();
				while (changeRec instanceof HibernateProxy) {
					hsu.evict(changeRec);
					changeRec = hsu.get(Person.class, changeRec.getPersonId());
				}
				Person realRec = cr.getRealRecord();
				while (realRec instanceof HibernateProxy) {
					hsu.evict(realRec);
					realRec = hsu.get(Person.class, realRec.getPersonId());
				}
				hsu.handleHistoryRecord(changeRec, realRec);

				HibernateSessionUtil.copyCorresponding(realRec, changeRec, Person.PERSONID, Person.RECORD_TYPE);

				hsu.saveOrUpdate(realRec);

				cr.setChangeStatus(PersonCR.STATUS_APPROVED);
				cr.setApprover(ArahantSession.getCurrentPerson());
				cr.setApprovalTime(new Date());
				hsu.saveOrUpdate(cr);
			} else {
				cr.getChangeRecord().setRecordType('R');
				hsu.saveOrUpdate(cr.getChangeRecord());
				cr.setChangeStatus(PersonCR.STATUS_APPROVED);
				cr.setApprover(ArahantSession.getCurrentPerson());
				cr.setApprovalTime(new Date());
				hsu.saveOrUpdate(cr);
			}
			hsu.flush();
			//pending home address
			applyAddress(ADDR_HOME);

			applyPhone(PHONE_HOME);
			applyPhone(PHONE_CELL);
			applyPhone(PHONE_WORK);
			applyPhone(PHONE_FAX);
		}

		List<String> doneIds = new ArrayList<String>();
		//apply pending dependents
		if (doDeps)
			for (HrEmplDependentChangeRequest dcr : hsu.createCriteria(HrEmplDependentChangeRequest.class).eq(HrEmplDependentChangeRequest.PROJECT, cr.getProject()).eq(HrEmplDependentChangeRequest.CHANGE_STATUS, HrEmplDependentChangeRequest.STATUS_PENDING).joinTo(HrEmplDependentChangeRequest.DEP_PENDING).list()) {
				if (doneIds.contains(dcr.getChangeRecord().getDependentId()))
					continue;
				doneIds.add(dcr.getChangeRecord().getDependentId());
				applyDependent(dcr);
			}

		if (cr.getProject() != null && doDemogs) {
			cr.getProject().setProjectStatus(new BProjectStatus(BProjectStatus.findOrMakeInactive("Completed - Approved")).getBean());
			cr.getProject().setDateCompleted(DateUtils.now());
			cr.getProject().setTimeCompleted(DateUtils.nowTime());
		}
	}

	public String fixDesc(String name, String val) {
		return name + (val == null ? "" : val) + "\n";
	}

	public String fixCmpDesc(String name, String val, String oldName, String oldVal) {
		return name + (val == null ? "" : val) + "\n" + oldName + (oldVal == null ? "" : oldVal) + "\n";
	}

	public String getDescription() {
		return getDescription(true);
	}

	private String getDescription(boolean doDeps) {
		String ret = "";

		Person original = getChangeRequest().getRealRecord();

		//original may be null if this is a brand new dependent

		if (original == null) {
			ret += fixDesc("Owning Company: ", person.getCompanyBase().getName());
			ret += fixDesc("First Name: ", person.getFname());
			ret += fixDesc("Middle Name: ", person.getMname());
			ret += fixDesc("Last Name: ", person.getLname());
			ret += fixDesc("Nick Name: ", person.getNickName());
			ret += fixDesc("SSN: ", person.getUnencryptedSsn());
			ret += fixDesc("Email: ", person.getPersonalEmail());
			ret += fixDesc("Gender: ", person.getSex() + "");
			try {
				ret += fixDesc("Street 1 :", loadPendingAddress(ADDR_HOME).getStreet());
				ret += fixDesc("Street 2 :", loadPendingAddress(ADDR_HOME).getStreet2());
				ret += fixDesc("City: ", loadPendingAddress(ADDR_HOME).getCity());
				ret += fixDesc("State: ", loadPendingAddress(ADDR_HOME).getState());
				ret += fixDesc("Zip: ", loadPendingAddress(ADDR_HOME).getZip());
				ret += fixDesc("County: ", loadPendingAddress(ADDR_HOME).getCounty());
			} catch (Exception e) {
				//skip it
			}
			if (loadPendingPhone(PHONE_HOME) != null)
				ret += fixDesc("Home Phone: ", loadPendingPhone(PHONE_HOME).getPhoneNumber());
			if (loadPendingPhone(PHONE_WORK) != null)
				ret += fixDesc("Work Phone: ", loadPendingPhone(PHONE_WORK).getPhoneNumber());
			if (loadPendingPhone(PHONE_CELL) != null)
				ret += fixDesc("Mobile Phone: ", loadPendingPhone(PHONE_CELL).getPhoneNumber());
			if (loadPendingPhone(PHONE_FAX) != null)
				ret += fixDesc("Fax: ", loadPendingPhone(PHONE_FAX).getPhoneNumber());
			ret += fixDesc("DOB: ", DateUtils.getDateFormatted(person.getDob()));
		} else {
			BPerson bp = new BPerson(original);

			ret += fixDesc("Owning Company: ", person.getCompanyBase().getName());
			ret += fixCmpDesc("Old First Name: ", original.getFname(), "New First Name: ", person.getFname());
			ret += fixCmpDesc("Old Middle Name: ", original.getMname(), "New Middle Name: ", person.getMname());
			ret += fixCmpDesc("Old Last Name: ", original.getLname(), "New Last Name: ", person.getLname());
			ret += fixCmpDesc("Old Nick Name: ", original.getNickName(), "New Nick Name: ", person.getNickName());
			ret += fixCmpDesc("Old SSN: ", original.getUnencryptedSsn(), "New SSN: ", person.getUnencryptedSsn());
			ret += fixCmpDesc("Old Email: ", original.getPersonalEmail(), "New Email: ", person.getPersonalEmail());
			ret += fixCmpDesc("Old Gender: ", original.getSex() + "", "New Gender: ", person.getSex() + "");
			try {
				ret += fixCmpDesc("Old Street 1: ", bp.getStreet(), "New Street 1: ", loadPendingAddress(ADDR_HOME).getStreet());
				ret += fixCmpDesc("Old Street 2: ", bp.getStreet2(), "New Street 2: ", loadPendingAddress(ADDR_HOME).getStreet2());
				ret += fixCmpDesc("Old City: ", bp.getCity(), "New City: ", loadPendingAddress(ADDR_HOME).getCity());
				ret += fixCmpDesc("Old State: ", bp.getState(), "New State: ", loadPendingAddress(ADDR_HOME).getState());
				ret += fixCmpDesc("Old Zip: ", bp.getZip(), "New Zip: ", loadPendingAddress(ADDR_HOME).getZip());
				ret += fixCmpDesc("Old County: ", bp.getCounty(), "New County: ", loadPendingAddress(ADDR_HOME).getCounty());
			} catch (Exception e) {
				//skip it
			}
			try {
				ret += fixCmpDesc("Old Home Phone: ", bp.getHomePhone(), "New Home Phone: ", loadPendingPhone(PHONE_HOME).getPhoneNumber());
				ret += fixCmpDesc("Old Work Phone: ", bp.getWorkPhoneNumber(), "New Work Phone: ", loadPendingPhone(PHONE_WORK).getPhoneNumber());
				ret += fixCmpDesc("Old Mobile Phone: ", bp.getMobilePhone(), "New Mobile Phone: ", loadPendingPhone(PHONE_CELL).getPhoneNumber());
				ret += fixCmpDesc("Old Fax: ", bp.getWorkFaxNumber(), "New Fax: ", loadPendingPhone(PHONE_FAX).getPhoneNumber());

			} catch (Exception e) {
				//skip it
			}
			ret += fixCmpDesc("Old DOB: ", DateUtils.getDateFormatted(original.getDob()), "New DOB: ", DateUtils.getDateFormatted(person.getDob()));
		}
		
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (doDeps)
			for (HrEmplDependentChangeRequest dcr : hsu.createCriteria(HrEmplDependentChangeRequest.class).eq(HrEmplDependentChangeRequest.PROJECT, getChangeRequest().getProject()).eq(HrEmplDependentChangeRequest.CHANGE_STATUS, HrEmplDependentChangeRequest.STATUS_PENDING).list()) {

				ret += "Dependent Relationship: " + dcr.getChangeRecord().getRelationshipType() + " " + dcr.getChangeRecord().getRelationship() + "\n";
//				BPerson depPend = new BPerson(dcr.getChangeRecord().getDependentId());
//				if (depPend.getRecordType() != 'C')
//					try {
////						depPend = new BPerson(hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, dcr.getChangeRecord().getDependentId()).first());
//					} catch (Exception e) {
//					}
//				throw new ArahantException("Could not find Dependent Pending record for id " + dcr.getChangeRecord().getDependentId());
				//depPend.loadPending(dcr.getChangeRecord().getPersonId(), dcr.getChangeRecord().getEmployeeId(), getChangeRequest().getProject());
//				ret += depPend.getDescription(false);
			}

		return ret;
	}

	private void applyPhone(int phoneType) {
		PhonePending pa = loadPendingPhone(phoneType);

		if (pa == null)
			return;

		if (pa.getChangeRequests().isEmpty())
			return;

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		PhoneChangeRequest acr = pa.getChangeRequests().iterator().next();

		if (acr.getRealRecord() != null) {
			HibernateSessionUtil.copyCorresponding(acr.getRealRecord(), acr.getChangeRecord(), Phone.PHONEID, Phone.RECORD_TYPE);
			hsu.saveOrUpdate(acr.getRealRecord());
		} else {
			acr.getChangeRecord().setRecordType('R');
			hsu.saveOrUpdate(acr.getChangeRecord());
		}

		acr.setChangeStatus(PhoneChangeRequest.STATUS_APPROVED);
		acr.setApprover(ArahantSession.getCurrentPerson());
		acr.setApprovalTime(new Date());
		hsu.saveOrUpdate(acr);
	}

	private void applyAddress(int addrType) {
		AddressPending pa = loadPendingAddress(addrType);

		if (pa == null)
			return;

		if (pa.getChangeRequests().isEmpty())
			return;

		AddressChangeRequest acr = pa.getChangeRequests().iterator().next();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (acr.getRealRecord() != null) {
			HibernateSessionUtil.copyCorresponding(acr.getRealRecord(), acr.getChangeRecord(), Address.ADDRESSID, Address.RECORD_TYPE);
			hsu.saveOrUpdate(acr.getRealRecord());
			hsu.flush();
		} else {
			acr.getChangeRecord().setRecordType('R');
			hsu.saveOrUpdate(acr.getChangeRecord());
			hsu.flush();
		}

		acr.setChangeStatus(AddressChangeRequest.STATUS_APPROVED);
		acr.setApprover(ArahantSession.getCurrentPerson());
		acr.setApprovalTime(new Date());
		hsu.saveOrUpdate(acr);
		hsu.flush();
	}

	private void applyDependent(HrEmplDependentChangeRequest acr) {
		String personId = acr.getChangeRecord().getDependentId();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Person p = hsu.get(Person.class, personId);

		if (p == null || p.getRecordType() == 'C') {
			Person pp = hsu.createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, personId).first();
			if (pp == null)
				throw new ArahantException("Could not find Person Pending record for id " + personId);
			new BPerson(pp).apply(false);
		}


		if (acr.getRealRecord() != null) {
			HibernateSessionUtil.copyCorresponding(acr.getRealRecord(), acr.getChangeRecord(), HrEmplDependent.RELATIONSHIP_ID, HrEmplDependent.RECORD_TYPE);
			hsu.saveOrUpdate(acr.getRealRecord());
		} else {
			acr.getChangeRecord().setRecordType('R');
			hsu.saveOrUpdate(acr.getChangeRecord());
		}

		acr.setChangeStatus(HrEmplDependentChangeRequest.STATUS_APPROVED);
		acr.setApprover(ArahantSession.getCurrentPerson());
		acr.setApprovalTime(new Date());
		hsu.saveOrUpdate(acr);
	}

	public void reject() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Project p = getChangeRequest().getProject();
		for (PersonCR cr : hsu.createCriteria(PersonCR.class).eq(PersonCR.PROJECT, p).list()) {
			cr.setChangeStatus(PersonCR.STATUS_REJECTED);
			hsu.saveOrUpdate(cr);
		}

		for (AddressChangeRequest cr : hsu.createCriteria(AddressChangeRequest.class).eq(AddressChangeRequest.PROJECT, p).list()) {
			cr.setChangeStatus(AddressChangeRequest.STATUS_REJECTED);
			hsu.saveOrUpdate(cr);
		}

		for (PhoneChangeRequest cr : hsu.createCriteria(PhoneChangeRequest.class).eq(PhoneChangeRequest.PROJECT, p).list()) {
			cr.setChangeStatus(PhoneChangeRequest.STATUS_REJECTED);
			hsu.saveOrUpdate(cr);
		}

		for (HrEmplDependentChangeRequest cr : hsu.createCriteria(HrEmplDependentChangeRequest.class).eq(HrEmplDependentChangeRequest.PROJECT, p).list()) {
			cr.setChangeStatus(HrEmplDependentChangeRequest.STATUS_REJECTED);
			hsu.saveOrUpdate(cr);
		}

		if (getChangeRequest().getProject() != null) {
			getChangeRequest().getProject().setProjectStatus(new BProjectStatus(BProjectStatus.findOrMakeInactive("Completed - Rejected")).getBean());
			getChangeRequest().getProject().setDateCompleted(DateUtils.now());
			getChangeRequest().getProject().setTimeCompleted(DateUtils.nowTime());
		}
	}

	public HrEmplDependent addDepedendent(Person pp, char relationship, int start, int end, String otherType) {
		return addDepedendent(pp, relationship, start, end, otherType, ArahantSession.getHSU().getCurrentPerson().getPersonId());
	}

	public HrEmplDependent addDepedendent(Person pp, char relationship, int start, int end, String otherType, String requestor) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (pp == null) //create new pending person
		{
			BPerson bpp = new BPerson();
			bpp.create();
			bpp.setRecordType('C');

			bpp.insert();
			pp = bpp.getPerson();

			PersonCR r = new PersonCR();
			r.generateId();
			r.setChangeRecord(pp);
			r.setRequestor(new BPerson(requestor).getPerson());
			r.setRequestTime(new Date());
			r.setChangeStatus(PersonCR.STATUS_PENDING);
			r.setProject(getChangeRequest().getProject());
			hsu.saveOrUpdate(r);
		}

		HrEmplDependent dep = new HrEmplDependent();
		dep.generateId();
		dep.setDependentId(pp.getPersonId());
		dep.setRelationshipType(relationship);
		dep.setRelationship(otherType);
		dep.setDateAdded(start);
		dep.setDateInactive(end);
		dep.setEmployee(new BEmployee(getChangeRequest().getRealRecord().getPersonId()).getEmployee());
		dep.setRecordType('C');
		hsu.saveOrUpdate(dep);

		HrEmplDependentChangeRequest cr = new HrEmplDependentChangeRequest();
		cr.generateId();
		cr.setChangeRecord(dep);
		cr.setChangeStatus(HrEmplDependentChangeRequest.STATUS_PENDING);
		cr.setProject(getChangeRequest().getProject());
		cr.setRequestTime(new Date());
		cr.setRequestor(new BPerson(requestor).getPerson());
		hsu.saveOrUpdate(cr);

		return dep;
	}

	public String noEnrollments(String benefitId, String changeResonId, int qualifyDate, String explanation) {
		return noEnrollments(benefitId, changeResonId, qualifyDate, explanation, false);
//		PersonCR changeRequest = getChangeRequest();
//		Person realRecord = null;
//
//        List<String> ids = new ArrayList<String>();
//        if (changeRequest != null){
//            ids.add(changeRequest.getChangeRecordId());
//			realRecord = changeRequest.getRealRecord();
//            if (realRecord != null) {
//                ids.add(realRecord.getPersonId());
//            }
//        }
//
//        //delete any beneficiary linked to this benefit
//
//        for (HrBenefitJoin bj : ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).list()){
//            ArahantSession.getHSU().createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();
//			ArahantSession.getHSU().createCriteria(HrPhysician.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();
//
//			bj.setPolicyEndDate(qualifyDate);
//			bj.setCoverageEndDate(qualifyDate);
//
//			ArahantSession.getHSU().flush();
//
//            new BHRBenefitJoin(bj).delete();
//        }
//
//        ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, benefitId).delete();
//
//        try {
//            BHRBenefitJoin bj = new BHRBenefitJoin();
//            bj.create();
//            bj.setPayingPerson(realRecord!=null? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
//            bj.setHrBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
//            bj.setCoveredPerson(realRecord!=null? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
//            bj.setBenefitApproved(false);
//            bj.setBenefitDeclined('Y');
//            bj.setChangeReason(changeResonId);
//            bj.setPolicyStartDate(qualifyDate);
//            bj.setChangeDescription(bj.getChangeReason());
//			bj.setEmployeeExplanation(explanation);
//            bj.insert(true);
//        } catch (Exception e) {
//            //logger.error(e);
//			//don't log it, throw
//			throw new ArahantException("Cannot create Benefit due to Null pointer in noEnrollments.");
//        }
	}

	public String noEnrollments(String benefitId, String changeResonId, int qualifyDate, String explanation, boolean approveDeclines) {
		return noEnrollments(benefitId, changeResonId, qualifyDate, explanation, approveDeclines, null);
	}

	public String noEnrollments(String benefitId, String changeResonId, int qualifyDate, String explanation, boolean approveDeclines, String categoryId) {
		String ret = null;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		PersonCR changeRequest = getChangeRequest();
		Person realRecord = null;

		List<String> ids = new ArrayList<String>();
		if (changeRequest != null) {
			ids.add(changeRequest.getChangeRecordId());
			realRecord = changeRequest.getRealRecord();
			if (realRecord != null)
				ids.add(realRecord.getPersonId());
		}

		//delete any beneficiary linked to this benefit
		if (!isEmpty(benefitId)) {
			for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).list()) {
				hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();
				hsu.createCriteria(HrPhysician.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();

				bj.setPolicyEndDate(qualifyDate);
				bj.setCoverageEndDate(qualifyDate);

				hsu.flush();

				new BHRBenefitJoin(bj).delete();
			}

			//delete unapproved declines
			hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, benefitId).delete();

			//delete unapproved declines in the category if it does not allow multiple benefits
			BHRBenefit bbj = new BHRBenefit(benefitId);
			if (!bbj.getBenefitCategory().getAllowsMultipleBenefitsBoolean())
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFIT_CATEGORY_ID, bbj.getBenefitCategoryId()).delete();
			//is there an existing approved enrollment in the benefit they are trying to decline?
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT_ID, benefitId).exists())
				approveDeclines = false;
			HrBenefitJoin dec = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_ID, benefitId).in(HrBenefitJoin.PAYING_PERSON_ID, ids).eq(HrBenefitJoin.APPROVED, 'Y').first();
			if (dec != null) //benefit join already exists that declines this benefit (approved)
				ret = dec.getBenefitJoinId();
			else
				try {
					BHRBenefitJoin bj = new BHRBenefitJoin();
					ret = bj.create();
					bj.setPayingPerson(realRecord != null ? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
					bj.setHrBenefit(hsu.get(HrBenefit.class, benefitId));
					bj.setCoveredPerson(realRecord != null ? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
					bj.setBenefitApproved(approveDeclines);
					bj.setBenefitDeclined('Y');
					bj.setChangeReason(changeResonId);
					bj.setPolicyStartDate(qualifyDate);
					bj.setChangeDescription(bj.getChangeReason());
					bj.setEmployeeExplanation(explanation);
					bj.insert(true);
				} catch (Exception e) {
					//logger.error(e);
					//don't log it, throw
					throw new ArahantException("Cannot create Benefit due to Null pointer in noEnrollments.");
				}
		} else {
			BHRBenefitCategory bcat = new BHRBenefitCategory(categoryId);
			hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY_ID, categoryId).delete();
			for (HrBenefit b : bcat.getBenefits()) {
				benefitId = b.getBenefitId();
				for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).list()) {
					hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();
					hsu.createCriteria(HrPhysician.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).delete();

					bj.setPolicyEndDate(qualifyDate);
					bj.setCoverageEndDate(qualifyDate);

					hsu.flush();

					new BHRBenefitJoin(bj).delete();
				}

				//delete unapproved declines
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, benefitId).delete();

				//is there an existing approved enrollment in the benefit they are trying to decline?
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, ids).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT_ID, benefitId).exists())
					approveDeclines = false;
			}
			HrBenefitJoin dec = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY_ID, categoryId).in(HrBenefitJoin.PAYING_PERSON_ID, ids).eq(HrBenefitJoin.APPROVED, 'Y').first();
			if (dec != null) //benefit join already exists that declines this category (approved)
				ret = dec.getBenefitJoinId();
			else
				try {
					BHRBenefitJoin bj = new BHRBenefitJoin();
					ret = bj.create();
					bj.setPayingPerson(realRecord != null ? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
					bj.setHrBenefitCategory(hsu.get(HrBenefitCategory.class, categoryId));
					bj.setCoveredPerson(realRecord != null ? realRecord : getPerson()); //if my change request is null, or the real record is null, use the current person
					bj.setBenefitApproved(approveDeclines);
					bj.setBenefitDeclined('Y');
					bj.setChangeReason(changeResonId);
					bj.setPolicyStartDate(qualifyDate);
					bj.setChangeDescription(bj.getChangeReason());
					bj.setEmployeeExplanation(explanation);
					bj.insert(true);
				} catch (Exception e) {
					//logger.error(e);
					//don't log it, throw
					throw new ArahantException("Cannot create Benefit due to Null pointer in noEnrollments.");
				}
		}
		return ret;
	}

	public String enrollInConfig(String configId, String changeReasonId, int qualifyingEventDate, String[] enrolleeIds, double coverageAmount) throws Exception {
		return enrollInConfig(configId, changeReasonId, qualifyingEventDate, enrolleeIds, coverageAmount, true, "");
	}

	public String enrollInConfig(String configId, String changeReasonId, int qualifyingEventDate, String[] enrolleeIds) throws Exception {
		return enrollInConfig(configId, changeReasonId, qualifyingEventDate, enrolleeIds, 0, true, "");
	}

	@SuppressWarnings("unchecked")
	public String enrollInConfig(String configId, String changeReasonId, int qualifyingEventDate, String[] enrolleeIds, double coverageAmount, boolean doEnrollment, String explanation) throws Exception {
		Enrollee[] ea = new Enrollee[enrolleeIds.length];
		int count = 0;
		for (String e : enrolleeIds) {
			Enrollee ne = new Enrollee();
			ne.setCoverageAmount(coverageAmount);
			ne.setRelationshipId(e);
			ea[count] = ne;
			count++;
		}
		if (true)
			return enrollInConfigMultipleCoverages(configId, changeReasonId, qualifyingEventDate, ea, coverageAmount, doEnrollment, explanation);

		//DECIDED TO OVERLOAD THIS METHOD AND CALL THE NEW ONE THAT ALLOWS VARYING COVERAGE AMOUNTS FOR EACH DEPENDENT AS WELL

		//internal class for saving beneficiary info before it gets deleted (moved to history)
		//reassign beneficiaries in the same benefit if they exist
		class Beneficiary {

			public String name;
			public String address;
			public int dob;
			public String ssn;
			public String relationship;
			public String type;
			public int percent;

			Beneficiary(BHRBeneficiary b) {
				name = b.getBeneficiary();
				address = b.getAddress();
				dob = b.getDob();
				ssn = b.getSsn();
				relationship = b.getRelationship();
				type = b.getBeneficiaryType();
				percent = b.getPercentage();
			}
		}

		class Physician {

			public String name;
			public String address;
			public String code;
			public String changeDescription;
			public int changeDate;
			public boolean annualVisit;
			public String coveredPersonId;

			private Physician(BHRPhysician p) {
				name = p.getPhysicianName();
				address = p.getAddress();
				code = p.getPhysicianCode();
				changeDate = p.getChangeDate();
				changeDescription = p.getChangeReason();
				annualVisit = p.getAnnualVisit();
				coveredPersonId = p.getBenefitJoin().getCoveredPersonId();
			}
		}

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());
		String changeDescription = new BHRBenefitChangeReason(changeReasonId).getDescription();

		final BHRBenefitConfig bbc = new BHRBenefitConfig(configId);
		final HrBenefit bene = bbc.getBenefit();
		boolean coversEmployee = bbc.getCoversEmployee();

		HashSet<String> enrollIds = new HashSet<String>();

		for (String x : enrolleeIds)
			try {
				BHREmplDependent pend = new BHREmplDependent(x);
//				if(pend.getRecordType()!='C')
//				{
//					pend = new BHREmplDependent(hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C').eq(HrEmplDependent.PERSON, x).first().getPerson().getPersonId());
//				}
				enrollIds.add(pend.getEnrollingPersonId());
			} catch (Exception e) {
				//we probably have a real record person here
				BHREmplDependent pend = new BHREmplDependent(x);
				enrollIds.add(pend.getPersonId());
			}
		if (doEnrollment) {
			//delete any pending declines
			for (HrBenefitJoin decBJ : hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, bene).list()) {

				decBJ.setCoverageEndDate(qualifyingEventDate);
				decBJ.setPolicyEndDate(qualifyingEventDate);
				hsu.update(decBJ);

				new BHRBenefitJoin(decBJ).delete();
			}
			HrBenefitJoin catDec = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bene.getHrBenefitCategory()).first();
			if (catDec != null) {
				catDec.setCoverageEndDate(qualifyingEventDate);
				catDec.setPolicyEndDate(qualifyingEventDate);
				hsu.update(catDec);

				new BHRBenefitJoin(catDec).delete();
			}
			//delete pending declines in the category if it is mutually exclusive
			if (!bene.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
				for (HrBenefitJoin decBJ : hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.HRBENEFIT, bene.getHrBenefitCategory().getBenefits()).list()) {
					decBJ.setCoverageEndDate(qualifyingEventDate);
					decBJ.setPolicyEndDate(qualifyingEventDate);
					hsu.update(decBJ);
					new BHRBenefitJoin(decBJ).delete();
				}
		}

		List<Beneficiary> copyBeneficiaries = new ArrayList<Beneficiary>();
		if (bene.getHasBeneficiaries() == 'Y') {
			for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
				for (HrBeneficiary beneficiary : join.getBeneficiaries()) {
					BHRBeneficiary bbeneficiary = new BHRBeneficiary(beneficiary);
					if ((bene.getContingentBeneficiaries() == 'Y' && bbeneficiary.getBeneficiaryType().equals("C")) || bbeneficiary.getBeneficiaryType().equals("P"))
						copyBeneficiaries.add(new Beneficiary(bbeneficiary));
				}
			if (copyBeneficiaries.isEmpty())
				for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
					for (HrBeneficiary beneficiary : join.getBeneficiaries()) {
						BHRBeneficiary bbeneficiary = new BHRBeneficiary(beneficiary);
						if ((bene.getContingentBeneficiaries() == 'Y' && bbeneficiary.getBeneficiaryType().equals("C")) || bbeneficiary.getBeneficiaryType().equals("P"))
							copyBeneficiaries.add(new Beneficiary(bbeneficiary));
					}
		}

		List<Physician> copyPhysicians = new ArrayList<Physician>();
		if (bene.getHasPhysicians() == 'Y') {
			for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
				for (HrPhysician physician : join.getPhysicians()) {
					BHRPhysician bphysician = new BHRPhysician(physician);
					copyPhysicians.add(new Physician(bphysician));
				}
			if (copyPhysicians.isEmpty())
				for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
					for (HrPhysician physician : join.getPhysicians()) {
						BHRPhysician bphysician = new BHRPhysician(physician);
						copyPhysicians.add(new Physician(bphysician));
					}
		}

		int differenceInEnrollment = 0;
		List<String> existingApprovedPersons = new LinkedList<String>();
		//look to see if they have the benefit already with the same config
		if (!enrolledInApprovedConfig(configId))
			differenceInEnrollment = 1;
		else {
			//go through all dependents and check them

			for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').list())
				//        System.out.println("Found " + bj.getCoveredPersonId());
				//does add employee in the found
				if (!bj.getCoveredPersonId().equalsIgnoreCase(bj.getPayingPersonId()))
					existingApprovedPersons.add(bj.getCoveredPersonId());
			//now check if any person was removed or added
			//how do we check this???
			//if the size of the lists of enrollIds and existingApprovedPersons don't match
			//then someone was added or removed
			if (existingApprovedPersons.size() != enrollIds.size())
				differenceInEnrollment = 1;
			else
				for (String currentCoveredPerson : existingApprovedPersons)
					if (!enrollIds.contains(currentCoveredPerson)) {
						differenceInEnrollment = 1;
						break;
					}
		}

		HrBenefit benefit = hsu.get(HrBenefitConfig.class, configId).getHrBenefit();

		//delete any pending records for configs that are exclusive
		//BEFORE this was using project_id in the query, but it has been removed
		//AND replaced by HrBenefitJoin.PAYING_PERSON_ID, possibleIds because project_id change frequently
		//BUT we just want to delete this pending benefits
		if (!benefit.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
			for (HrBenefitJoin pendingBJ : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, benefit.getHrBenefitCategory()).list()) {
				//delete beneficiary if there is one
				hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, pendingBJ.getBenefitJoinId()).delete();

				pendingBJ.setCoverageEndDate(qualifyingEventDate);
				pendingBJ.setPolicyEndDate(qualifyingEventDate);

				hsu.flush();

				new BHRBenefitJoin(pendingBJ).delete();
			}

		//check for existing enrollments in this benefit, delete them
		for (HrBenefitJoin pendingBJ : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefit.getBenefitId()).list()) {
			//delete beneficiary if there is one
			hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, pendingBJ.getBenefitJoinId()).delete();

			pendingBJ.setCoverageEndDate(qualifyingEventDate);
			pendingBJ.setPolicyEndDate(qualifyingEventDate);

			hsu.flush();

			new BHRBenefitJoin(pendingBJ).delete();
		}

		HrBenefitJoin approvedJoin = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).eq(HrBenefitJoin.APPROVED, 'Y').first();

		//could be same config but different coverage amount?
		if (approvedJoin != null && !Utils.doubleEqual(approvedJoin.getAmountCovered(), coverageAmount, 0.001))
			if (approvedJoin.getEmployeeCovered() == 'Y')
				differenceInEnrollment = 3;
			else //employee isn't covered, so need to check dep joins to see if coverage amount matches
			{
				BHRBenefitJoin bapprovedJoin = new BHRBenefitJoin(approvedJoin);
				for (HrBenefitJoin depBJ : bapprovedJoin.getDependentBenefitJoins())
					if (!Utils.doubleEqual(depBJ.getAmountCovered(), coverageAmount, 0.001))
						differenceInEnrollment = 4;
			}

		if (approvedJoin != null && new BHRBenefitJoin(approvedJoin).getBenefitCategoryType() == HrBenefitCategory.FLEX_TYPE)
			if (approvedJoin.getPolicyStartDate() != qualifyingEventDate)
				differenceInEnrollment = 1;

		//NOT JUST YET
		//is it the same as the approved benefit???
		if (differenceInEnrollment == 0) {
			//return approved benefit if there is one
			if (approvedJoin != null)
				return approvedJoin.getBenefitJoinId();
			//else return nothing
			return ""; //same config
		}
		HashMap<String, HrBenefitJoin> joinsCreated = new HashMap<String, HrBenefitJoin>();
		BHRBenefitJoin originalRecord = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.APPROVED, 'Y').first());

		BHRBenefitJoin masterRecord = new BHRBenefitJoin();
		masterRecord.create();
		masterRecord.setPayingPerson(getChangeRequest().getRealRecord());
		masterRecord.setCoveredPerson(getChangeRequest().getRealRecord());
		masterRecord.setBenefitConfigId(configId);
		BHRBenefitChangeReason bbcr = new BHRBenefitChangeReason(changeReasonId);

		//if the policy join already existed, no need to change the dates on it
		if (originalRecord.getBean() != null)
			if (originalRecord.getBenefitCategoryType() == HrBenefitCategory.FLEX_TYPE) {   //For flex type benefits only
				int termDate = DateUtils.addDays(qualifyingEventDate, -1);

				originalRecord.setPolicyEndDate(termDate);
				originalRecord.setCoverageEndDate(termDate);

				if (originalRecord.isPolicyBenefitJoin())
					for (HrBenefitJoin depJoin : originalRecord.getDependentBenefitJoins()) {
						depJoin.setPolicyEndDate(termDate);
						depJoin.setCoverageEndDate(termDate);
					}
				originalRecord.update();

				masterRecord.setPolicyStartDate(qualifyingEventDate);
				if (coversEmployee) {
					masterRecord.setCoverageStartDate(qualifyingEventDate);
					masterRecord.setAmountCovered(coverageAmount);
				} else
					masterRecord.setEmployeeCovered("N");
			} else {
				masterRecord.setPolicyStartDate(originalRecord.getPolicyStartDate());
				if (coversEmployee) {
					masterRecord.setCoverageStartDate(originalRecord.getCoverageStartDate());
					masterRecord.setAmountCovered(coverageAmount);
				} else
					masterRecord.setEmployeeCovered("N");
			}
		else {	//this is a new policy join

			if (bbcr.getType() == HrBenefitChangeReason.NEW_HIRE) //is this a new hire?  maybe we should calculate dates
			{
				BHRBenefit bhrb = new BHRBenefit(benefit);
				Calendar hireCal = DateUtils.getCalendar(this.getBEmployee().getCurrentHiredDate());
				switch (bhrb.getEligibilityType()) {
					case 2:
						hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
						if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
							hireCal.add(Calendar.MONTH, 1);
						hireCal.set(Calendar.DAY_OF_MONTH, 1);
						break;
					case 3:
						hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
						if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
							hireCal.add(Calendar.MONTH, 1);
						hireCal.set(Calendar.DAY_OF_MONTH, 1);
						break;
					case 4:
						hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
						break;
					case 5:
						hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
						break;
					default:
						break;

				}
				//are you trying to enroll as of a date before you are eligible?
				if (qualifyingEventDate < DateUtils.getDate(hireCal))
					qualifyingEventDate = DateUtils.getDate(hireCal);
			}
			masterRecord.setPolicyStartDate(qualifyingEventDate);
			if (coversEmployee) {
				masterRecord.setCoverageStartDate(qualifyingEventDate);
				masterRecord.setAmountCovered(coverageAmount);
			} else
				masterRecord.setEmployeeCovered("N");
		}

		masterRecord.setChangeDescription(changeDescription);
		masterRecord.setBenefitApproved(false);
		masterRecord.setEmployeeExplanation(explanation);

		if (bbcr.getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
			masterRecord.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
			masterRecord.setChangeReason(null);
		} else
			masterRecord.setChangeReason(changeReasonId);
		masterRecord.setProject(getChangeRequest().getProject());
		masterRecord.insert(true);
		joinsCreated.put(masterRecord.getPayingPersonId(), masterRecord.getBean());
		//if we have beneficiaries to copy, that means they should be copied to the new enrollment (unapproved of course)
//		BHRBenefitJoin masterRecord = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class)
//																				.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds)
//																				.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId)
//																				.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
//																				.eq(HrBenefitJoin.APPROVED, 'Y')
//																				.first());
		for (Beneficiary tempBeneficiary : copyBeneficiaries) {
			BHRBeneficiary newBeneficiary = new BHRBeneficiary();
			newBeneficiary.create();
			newBeneficiary.setAddress(tempBeneficiary.address);
			newBeneficiary.setRelationship(tempBeneficiary.relationship);
			newBeneficiary.setAssociatedBenefit(masterRecord);
			newBeneficiary.setBeneficiary(tempBeneficiary.name);
			newBeneficiary.setBeneficiaryType(tempBeneficiary.type);
			newBeneficiary.setDob(tempBeneficiary.dob);
			newBeneficiary.setPercentage(tempBeneficiary.percent);
			newBeneficiary.setSsn(tempBeneficiary.ssn);
			newBeneficiary.insert();
		}

		//since we're not creating pending records immediately anymore
		//we forced to get Real or Pending Record; therefore, when creating the join we have to use one of the Beans
		//consequently, we have duplicate codes here
		for (String x : enrolleeIds)
			try {
				BHREmplDependent pend = new BHREmplDependent(x);
				BHRBenefitJoin bbj = new BHRBenefitJoin();
				BHRBenefitJoin bbjDeps = new BHRBenefitJoin();
				if (existingApprovedPersons.contains(pend.getPersonId())) {
					bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
					if (bbjDeps.getBean() == null)
						bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
//					if(bbjDeps.getBenefitApproved()) {
//						bbjDeps.setBenefitApproved(false);
//						bbjDeps.update();
//					}
//					joinsCreated.put(bbjDeps.getPayingPersonId(), bbjDeps.getBean());
				}
//				else {
				//need to make a benefit join record for this person
				String id = bbj.create();
				bbj.setBenefitApproved(false);
				bbj.setBenefitConfigId(configId);
				bbj.setPayingPerson(getChangeRequest().getRealRecord());
				//if (!pend.getEnrollingPersonId().equals(pend.getPersonId())) {
				bbj.setCoveredPersonId(pend.getEnrollingPersonId()); //TODO: may have to come up with some kind of double map to get this to work
				bbj.setRelationshipId(pend.getEnrollingRelationshipId());
				//} else {
				//    bbj.setCoveredPersonId(masterRecord.getPayingPersonId());
				//    bbj.setRelationshipId(null);
				//    forceNewUser = true;
				//}
				bbj.setPolicyStartDate(masterRecord.getPolicyStartDate());
				if (bbjDeps.getBean() == null)
					bbj.setCoverageStartDate(qualifyingEventDate);
				else
					bbj.setCoverageStartDate(bbjDeps.getCoverageStartDate());
				bbj.setAmountCovered(coverageAmount);
				bbj.setChangeDescription(changeDescription);
				bbj.setEmployeeExplanation(explanation);
				if (new BHRBenefitChangeReason(changeReasonId).getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
					bbj.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
					bbj.setChangeReason(null);
				} else
					bbj.setChangeReason(changeReasonId);
				bbj.setProject(getChangeRequest().getProject());
				bbj.insert();
				joinsCreated.put(bbj.getPayingPersonId(), bbj.getBean());
//				}
				//no longer needed... got rid of
//                if (forceNewUser) {
//                    hsu.doSQL("update hr_benefit_join set covered_person='" + pend.getEnrollingPersonId() + "' , relationship_id='" + pend.getEnrollingRelationshipId() + "' where benefit_join_id='" + id + "'");
//                }
			} catch (Exception e) {
				//WE HAVE  a real record
				BHREmplDependent pend = new BHREmplDependent(x);
				BHRBenefitJoin bbj = new BHRBenefitJoin();
				BHRBenefitJoin bbjDeps = new BHRBenefitJoin();
				if (existingApprovedPersons.contains(pend.getPersonId())) {
					bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
					if (bbjDeps.getBean() == null)
						bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
//					if(bbjDeps.getBenefitApproved()) {
//						bbjDeps.setBenefitApproved(false);
//						bbjDeps.update();
//					}
//					joinsCreated.put(bbjDeps.getPayingPersonId(), bbjDeps.getBean());
				}
//				else {
				//need to make a benefit join record for this person
				String id = bbj.create();
				bbj.setBenefitApproved(false);
				bbj.setBenefitConfigId(configId);
				bbj.setPayingPerson(getChangeRequest().getRealRecord());
				//if (!pend.getPerson().getPersonId().equals(pend.getPersonId())) {
				bbj.setCoveredPersonId(pend.getPerson().getPersonId()); //TODO: may have to come up with some kind of double map to get this to work
				bbj.setRelationshipId(pend.getRelationshipId());
				//} else {
				//    bbj.setCoveredPersonId(masterRecord.getPayingPersonId());
				//    bbj.setRelationshipId(null);
				//    forceNewUser = true;
				//}
				bbj.setChangeDescription(changeDescription);
				bbj.setEmployeeExplanation(explanation);
				if (new BHRBenefitChangeReason(changeReasonId).getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
					bbj.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
					bbj.setChangeReason(null);
				} else
					bbj.setChangeReason(changeReasonId);
				bbj.setPolicyStartDate(masterRecord.getPolicyStartDate());
				if (bbjDeps.getBean() == null)
					bbj.setCoverageStartDate(qualifyingEventDate);
				else
					bbj.setCoverageStartDate(bbjDeps.getCoverageStartDate());
				bbj.setAmountCovered(coverageAmount);
				bbj.setProject(getChangeRequest().getProject());
				bbj.insert();
				joinsCreated.put(bbj.getPayingPersonId(), bbj.getBean());
				//                if (forceNewUser) {
				//                    hsu.doSQL("update hr_benefit_join set covered_person='" + pend.getPersonId() + "' , relationship_id='" + pend.getRelationshipId() + "' where benefit_join_id='" + id + "'");
				//                }
//				}
			}

		//if we have PHYSICIANS to copy, that means they should be copied to the new enrollment (unapproved of course)
		for (Physician tempPhysician : copyPhysicians)
			if (joinsCreated.containsKey(tempPhysician.coveredPersonId)) {
				BHRPhysician newPhysician = new BHRPhysician();
				newPhysician.create();
				newPhysician.setAddress(tempPhysician.address);
				newPhysician.setBenefitJoin(joinsCreated.get(tempPhysician.coveredPersonId));
				newPhysician.setPhysicianCode(tempPhysician.code);
				newPhysician.setPhysicianName(tempPhysician.name);
				newPhysician.setChangeReason(tempPhysician.changeDescription);
				newPhysician.setChangeDate(tempPhysician.changeDate);
				newPhysician.setAnnualVisit(tempPhysician.annualVisit);
				newPhysician.insert();
			}

		//check for over coverage
		if (masterRecord.checkOverCoverage() && !BProperty.getBoolean("wizardAllowInapplicable"))
			throw new ArahantWarning("You have selected a coverage level that is excessive\ngiven the dependents you are enrolling.");
		return masterRecord.getBenefitJoinId();
	}

	public void enrollInConfigWithCost(String configId, String changeReasonId, int qualifyingEventDate, String[] enrolleeIds, double coverage) throws Exception {
		enrollInConfig(configId, changeReasonId, qualifyingEventDate, enrolleeIds, coverage);
	}

	@SuppressWarnings("unchecked")
	public boolean enrolledInApprovedConfig(String benefitId) {
		//System.out.println(benefitId);
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());
		//REMOVED .eq(HrBenefitJoin.PROJECT, getChangeRequest().getProject())
		//because project id don't often match when there is an existing benefit that has not been approved
		//therefore, if there is existing benefit of the same config, we're not going to create a new benefit record
		//for it
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').exists();

	}

	private static class Costs {
		public double ppp = 0;
		public double monthly = 0;
		public double annual = 0;

		void add(Costs c) {
			ppp += c.ppp;
			monthly += c.monthly;
			annual += c.annual;
		}
	}

	/**
	 * Calculates the current total of pending benefits.  NEVER CALLED.
	 *
	 * @param benefitId an optional benefit to exclude from the total
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Costs getPendingBenefitTotalCost(String benefitId, int asOfDate, String categoryId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Costs ret = new Costs();
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() == null)
				throw new ArahantException("This functionality requires an approved employee.");
			else
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());

		HashSet<HrBenefit> doneBenefits = new HashSet<HrBenefit>();
		HashSet<HrBenefitCategory> doneCategories = new HashSet<HrBenefitCategory>();

		//make sure to skip any declines
		doneBenefits.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());

		doneCategories.addAll(hsu.createCriteria(HrBenefitCategory.class).joinTo(HrBenefitCategory.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());


		//BEmployee bemp=new BEmployee(getChangeRequest().getRealRecord().getPersonId());

		//First check unapproved benefits
		HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class);
		hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).orderBy(HrBenefitJoin.APPROVED) //N will come before Y, so unapproved will go first and block approved - saving queries
				.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds);
		if (!isEmpty(benefitId)) {

			List<String> notInBenefits = new ArrayList<String>();
			notInBenefits.add(benefitId);
			if (BProperty.isWizard3()) {
				//check if there are any dependent benefits that need to be auto enrolled that aren't in the wizard (the wizard takes care of auto enrolls too)
				List<BenefitDependency> bds = hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT_ID, benefitId).list();

				List<String> dependentBenefitIds = new ArrayList<String>();
				List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
				for (BenefitDependency bd : bds) {
					dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
					dependentBenefits.add(bd.getDependentBenefit());
				}
				BWizardConfiguration bz = getBEmployee().getWizardConfiguration("E");
				//get the matching benefits that are already in the wizard
				List<HrBenefit> benefitsInWizard = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();

				//remove those because the wizard will take care of these auto enrolls
				dependentBenefits.removeAll(benefitsInWizard);

				for (HrBenefit db : dependentBenefits)
					notInBenefits.add(db.getBenefitId());
			}
			hcu.joinTo(HrBenefitConfig.HR_BENEFIT).notIn(HrBenefit.BENEFITID, notInBenefits);
		}
		if (!isEmpty(categoryId)) {
			List<String> notInBenefits = new ArrayList<String>();
			if (BProperty.isWizard3()) {
				BHRBenefitCategory bbc = new BHRBenefitCategory(categoryId);
				//check if there are any dependent benefits that need to be auto enrolled that aren't in the wizard (the wizard takes care of auto enrolls too)
				List<HrBenefit> benefitsInCategory = bbc.getBenefits();
				List<BenefitDependency> bds = hsu.createCriteria(BenefitDependency.class).in(BenefitDependency.REQUIRED_BENEFIT, benefitsInCategory).list();

				List<String> dependentBenefitIds = new ArrayList<String>();
				List<HrBenefit> dependentBenefits = new ArrayList<HrBenefit>();
				for (BenefitDependency bd : bds) {
					if (!dependentBenefitIds.contains(bd.getDependentBenefit().getBenefitId()))
						dependentBenefitIds.add(bd.getDependentBenefit().getBenefitId());
					if (!dependentBenefits.contains(bd.getDependentBenefit()))
						dependentBenefits.add(bd.getDependentBenefit());
				}
				BWizardConfiguration bz = getBEmployee().getWizardConfiguration("E");
				//get the matching benefits that are already in the wizard
				List<HrBenefit> benefitsInWizard = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, dependentBenefitIds).joinTo(HrBenefit.WIZARD_CONF_BENEFITS).joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bz.getBean()).list();

				//remove those because the wizard will take care of these auto enrolls
				dependentBenefits.removeAll(benefitsInWizard);

				for (HrBenefit db : dependentBenefits)
					notInBenefits.add(db.getBenefitId());
			}
			if (notInBenefits.size() > 0)
				hcu.notIn(HrBenefitConfig.HR_BENEFIT_ID, notInBenefits);
			hcu.joinTo(HrBenefitConfig.HR_BENEFIT).ne(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(categoryId).getBean());
		}

		//get costs
		ret.add(calcCosts(hcu, asOfDate, doneBenefits, doneCategories));

		return ret;
	}

	@SuppressWarnings("unchecked")
	private Costs calcCosts(HibernateCriteriaUtil<HrBenefitConfig> hcu, int asOfDate, HashSet<HrBenefit> doneBenefits, HashSet<HrBenefitCategory> doneCategories) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Costs ret = new Costs();
		for (HrBenefitConfig c : hcu.list()) {

			if (doneBenefits.contains(c.getHrBenefit()))
				continue;
			if (!c.getHrBenefit().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
				if (doneCategories.contains(c.getHrBenefit().getHrBenefitCategory()))
					continue;

			doneBenefits.add(c.getHrBenefit());
			doneCategories.add(c.getHrBenefit().getHrBenefitCategory());

			BHRBenefitConfig conf = new BHRBenefitConfig(c);
			BPerson bp;
			if (getChangeRequest() != null)
				bp = new BPerson(getChangeRequest().getRealRecord().getPersonId());
			else
				bp = new BPerson(person.getPersonId());
			BHRBenefitJoin bj = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, c).eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson()).first());
			if (bj.getEmployeeCovered().equals("N") && bj.getDependentBenefitJoins().size() > 0)
				bj = new BHRBenefitJoin(bj.getDependentBenefitJoins().get(0));

			List<IPerson> coveredPeople = (List) hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson().getPerson()).eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bj.getBenefitConfig().getBean()).eq(HrBenefitJoin.APPROVED, bj.getBenefitApproved() ? 'Y' : 'N').ne(HrBenefitJoin.COVERAGE_START_DATE, 0).list();
			if (coveredPeople.isEmpty())
				coveredPeople.add(bj.getCoveredPerson().getPerson());

			ret.monthly += BenefitCostCalculator.calculateCostNewMethodMonthlyWithDependencies(asOfDate, c, person, false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1);//conf.calculateCostMonthly(asOfDate, conf.getBean(), bp.getPerson(), false, bj.getAmountCovered(), asOfDate, 0, coveredPeople);
			ret.annual += BenefitCostCalculator.calculateCostNewMethodAnnualWithDependencies(asOfDate, c, person, false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1);
			int ppy = BEmployee.getPPY(bp.getPersonId(), asOfDate);
			ret.ppp += BenefitCostCalculator.calculateCostNewMethodWithDependencies(asOfDate, c, person, false, bj.getAmountCovered(), asOfDate, 0, coveredPeople, -1);

			//System.out.println("Added "+conf.getName()+" and got "+ret);
		}
		return ret;
	}

	public BHRBenefitJoin[] listCurrentBenefitElections() {
		return listCurrentBenefitElections(true);
	}

	@SuppressWarnings("unchecked")
	public BHRBenefitJoin[] listCurrentBenefitElections(boolean includeDeclines) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class);

		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());

		List<HrBenefitJoin> l = hcu.orderBy(HrBenefitJoin.APPROVED) //N will come before Y, so unapproved will go first and block approved - saving queries
				.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list();

		List<HrBenefit> excludes = hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list();

		//add the benefits that the employee decided to decline (but not yet approved)
		excludes.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());

		excludes.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_CATEGORY).joinTo(HrBenefitCategory.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list());

		excludes.addAll(hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').list());

		List<HrBenefitJoin> removes = new ArrayList<HrBenefitJoin>();

		for (HrBenefitJoin bj : l) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

			if (bj.getBenefitApproved() == 'Y' && !bbj.isDecline())
				for (HrBenefit b : excludes) {
					HrBenefit eb = bbj.getBenefitConfig().getBenefit();
					HrBenefitCategory ec = eb.getHrBenefitCategory();
					boolean allowsMultiple = ec.getAllowsMultipleBenefitsBoolean();

					if (eb.equals(b))
						removes.add(bj);
					else if (!allowsMultiple && ec.equals(b.getHrBenefitCategory()))
						removes.add(bj);
				}
		}

		l.removeAll(removes);

		List<HrBenefitJoin> enrolls = new ArrayList<HrBenefitJoin>();

		List<HrBenefitJoin> declines = new ArrayList<HrBenefitJoin>();

		for (HrBenefitJoin bj : l)
			if (bj.getHrBenefitConfigId() != null)
				enrolls.add(bj);
			else
				declines.add(bj);

		l.clear();

		l.addAll(enrolls);

		if (includeDeclines)
			l.addAll(declines);

		return BHRBenefitJoin.makeArray(l);
	}

	@SuppressWarnings("unchecked")
	public BHRBenefitJoin[] listCurrentBenefitElectionsWithPhysicians(boolean includeDeclines) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);

		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());

		List<HrBenefitJoin> l = hcu.orderBy(HrBenefitJoin.APPROVED) //N will come before Y, so unapproved will go first and block approved - saving queries
				.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.HAS_PHYSICIANS, 'Y').list();

		List<HrBenefit> excludes = ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).list();

		List<HrBenefitJoin> removes = new ArrayList<HrBenefitJoin>();

		for (HrBenefitJoin bj : l) {
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

			if (bj.getBenefitApproved() == 'Y' && !bbj.isDecline())
				for (HrBenefit b : excludes)
					if (bbj.getBenefitConfig().getBenefit().equals(b))
						removes.add(bj);
		}

		l.removeAll(removes);

		List<HrBenefitJoin> enrolls = new ArrayList<HrBenefitJoin>();

		List<HrBenefitJoin> declines = new ArrayList<HrBenefitJoin>();

		for (HrBenefitJoin bj : l)
			if (bj.getHrBenefitConfigId() != null)
				enrolls.add(bj);
			else
				declines.add(bj);

		l.clear();

		l.addAll(enrolls);

		if (includeDeclines)
			l.addAll(declines);

		return BHRBenefitJoin.makeArray(l);
	}

	@SuppressWarnings("unchecked")
	public boolean alreadyEnrolledInConfig(String configId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		boolean ret;

		BHRBenefitConfig bConfig = new BHRBenefitConfig(configId);

		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());

		ret = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, configId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').exists();

		List<String> configIds = (List) hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(bConfig.getBenefit()).getBean()).list();

		if (!ret && !hsu.createCriteria(HrBenefitConfig.class).in(HrBenefitConfig.BENEFIT_CONFIG_ID, configIds).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').exists())
			ret = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, configId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').exists();

		return ret;
	}

	@SuppressWarnings("unchecked")
	public boolean enrolledInConfig(String benefitId) {
		//System.out.println(benefitId);
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());
		//REMOVED .eq(HrBenefitJoin.PROJECT, getChangeRequest().getProject())
		//because project id don't often match when there is an existing benefit that has not been approved
		//therefore, if there is existing benefit of the same config, we're not going to create a new benefit record
		//for it
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitId).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').exists();
	}

	public void loadRealPerson(String personId) {
		//this does not create a pending record,
		//we just want a copy of the real record and load it
		realPersonId = personId;
		person = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, personId).first();

//        if (bean == null) {
//            create();
//        }
//        HibernateSessionUtil.copyCorresponding(bean, p, Person.PERSONID);
	}

	public void filterByBenefitClassForBenefits(final HibernateCriteriaUtil<HrBenefit> hcu) throws ArahantException {
		if (isEmployee() && getBEmployee().getBenefitClass() != null) {
			//HibernateCriteriaUtil chcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.makeCriteria();
			HibernateCriteriaUtil<HrBenefit> classHcu = hcu.leftJoinTo("benefitClasses", "classes");
			HibernateCriterionUtil cri2 = classHcu.makeCriteria();
			cri1.sizeEq("benefitClasses", 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, getBEmployee().getBenefitClassId());
			orcri.or(cri1, cri2);
			orcri.add();
		}
	}

	public void filterByBenefitClassForConfigs(final HibernateCriteriaUtil<HrBenefitConfig> hcu) throws ArahantException {
		if (isEmployee() && getBEmployee().getBenefitClass() != null) {
			//HibernateCriteriaUtil chcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.makeCriteria();
			HibernateCriteriaUtil<HrBenefitConfig> classHcu = hcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");
			HibernateCriterionUtil cri2 = classHcu.makeCriteria();
			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, getBEmployee().getBenefitClassId());
			orcri.or(cri1, cri2);
			orcri.add();
		}
	}

	public static void main(String[] args) {

		for (CompanyDetail cd : ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class).list()) {
			HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class, "person1");
			Criteria crit = hcu.getCriteria();
			crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			//crit.add(Restrictions.eq("person1." + Person.RECORD_TYPE, 'R'));

			//		DetachedCriteria subquery = DetachedCriteria.forClass(HrEmplStatusHistory.class, "statusHistory2");
			//		subquery.setProjection(Projections.max("statusHistory2.effectiveDate"));
			//		subquery.add(Restrictions.eq("statusHistory2.employeeId", "person1.personId"));
			//		subquery.add(Restrictions.le("statusHistory2.effectiveDate", DateUtils.now()));

			crit.createCriteria("person1.hrEmplStatusHistories", "statusHistory1");
			//crit.add(Subqueries.propertyEq("statusHistory1.effectiveDate", subquery));
			crit.createCriteria("statusHistory1.hrEmployeeStatus", "status1");
			crit.add(Restrictions.eq("status1." + HrEmployeeStatus.ACTIVE, 'Y'));
			crit.add(Restrictions.eq("status1." + HrEmployeeStatus.ORG_GROUP, cd));
			crit.setProjection(Projections.countDistinct("person1.personId"));
			long count = (Long) crit.list().get(0);
			//hcu.setCriteriaWithoutOrder(crit);
			System.out.println(cd.getName() + ", " + count);
		}
	}

    /**
     * Find a specific note for a person
     *
     * @param cat_code  the cat_code of the note desired
     * @param person_id for this person
     * @return the note
     */
	public String getNote(String cat_code, String person_id) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        HibernateCriteriaUtil<PersonNote> crit = hsu.createCriteria(PersonNote.class);
        crit.joinTo(PersonNote.HRNOTECATEGORY).eq(HrNoteCategory.CATCODE, cat_code);
        crit.joinTo(PersonNote.PERSON).eq(Person.PERSONID, person_id);
        PersonNote pn = crit.first();
        return pn == null ? "" : pn.getNote();
    }

	public static String getNameLFM(Record rec) {
		try {
			final String fname = rec.getString("fname");
			final String mname = rec.getString("mname");
			final String lname = rec.getString("lname");
			String name = lname == null ? "" : lname;
			if (fname != null && !fname.isEmpty())
				if (name.isEmpty())
					name = fname;
				else
					name += ", " + fname;
			if (mname != null && !mname.isEmpty())
				if (name.isEmpty())
					name = mname;
				else
					name += " " + mname;
			return name;
		} catch (SQLException e) {
			return "";
		}
	}

	public static String getNameFML(Record rec) {
		try {
			final String fname = rec.getString("fname");
			final String mname = rec.getString("mname");
			final String lname = rec.getString("lname");
			String name = fname == null ? "" : fname;
			if (mname != null && !mname.isEmpty())
				if (name.isEmpty())
					name = mname;
				else
					name += " " + mname;
			if (lname != null && !lname.isEmpty())
				if (name.isEmpty())
					name = lname;
				else
					name += " " + lname;
			return name;
		} catch (SQLException e) {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public String enrollInConfigMultipleCoverages(String configId, String changeReasonId, int qualifyingEventDate, Enrollee[] enrollees, double coverageAmount, boolean doEnrollment, String explanation) throws Exception {

		//internal class for saving beneficiary info before it gets deleted (moved to history)
		//reassign beneficiaries in the same benefit if they exist
		class Beneficiary {

			public String name;
			public String address;
			public int dob;
			public String ssn;
			public String relationship;
			public String type;
			public int percent;

			Beneficiary(BHRBeneficiary b) {
				name = b.getBeneficiary();
				address = b.getAddress();
				dob = b.getDob();
				ssn = b.getSsn();
				relationship = b.getRelationship();
				type = b.getBeneficiaryType();
				percent = b.getPercentage();
			}
		}

		class Physician {

			public String name;
			public String address;
			public String code;
			public String changeDescription;
			public int changeDate;
			public boolean annualVisit;
			public String coveredPersonId;

			Physician(BHRPhysician p) {
				name = p.getPhysicianName();
				address = p.getAddress();
				code = p.getPhysicianCode();
				changeDate = p.getChangeDate();
				changeDescription = p.getChangeReason();
				annualVisit = p.getAnnualVisit();
				coveredPersonId = p.getBenefitJoin().getCoveredPersonId();
			}
		}

		HibernateSessionUtil hsu = ArahantSession.getHSU();
hsu.flush();
		List possibleIds = new ArrayList();
		possibleIds.add(person.getPersonId());
		if (getChangeRequest() != null)
			if (getChangeRequest().getRealRecord() != null)
				possibleIds.add(getChangeRequest().getRealRecord().getPersonId());
		String changeDescription = new BHRBenefitChangeReason(changeReasonId).getDescription();

		final BHRBenefitConfig bbc = new BHRBenefitConfig(configId);
		qualifyingEventDate = bbc.getEarliestStartDate(qualifyingEventDate);
		final HrBenefit bene = bbc.getBenefit();
		boolean coversEmployee = bbc.getCoversEmployee();

		HashSet<String> enrollIds = new HashSet<String>();

		for (Enrollee x : enrollees)
			try {
				BHREmplDependent pend = new BHREmplDependent(x.getRelationshipId());
//				if(pend.getRecordType()!='C')
//				{
//					pend = new BHREmplDependent(hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C').eq(HrEmplDependent.PERSON, x).first().getPerson().getPersonId());
//				}
				enrollIds.add(pend.getEnrollingPersonId());
			} catch (Exception e) {
				//we probably have a real record person here
				BHREmplDependent pend = new BHREmplDependent(x.getRelationshipId());
				enrollIds.add(pend.getPersonId());
			}
hsu.flush();
		if (doEnrollment) {
			//delete any pending declines
			for (HrBenefitJoin decBJ : hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, bene).list()) {

				decBJ.setCoverageEndDate(qualifyingEventDate);
				decBJ.setPolicyEndDate(qualifyingEventDate);
				hsu.update(decBJ);

				new BHRBenefitJoin(decBJ).delete();
			}
			HrBenefitJoin catDec = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, bene.getHrBenefitCategory()).first();
			if (catDec != null) {
				catDec.setCoverageEndDate(qualifyingEventDate);
				catDec.setPolicyEndDate(qualifyingEventDate);
				hsu.update(catDec);

				new BHRBenefitJoin(catDec).delete();
			}
			//delete pending declines in the category if it is mutually exclusive
			if (!bene.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
				for (HrBenefitJoin decBJ : hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.HRBENEFIT, bene.getHrBenefitCategory().getBenefits()).list()) {

					decBJ.setCoverageEndDate(qualifyingEventDate);
					decBJ.setPolicyEndDate(qualifyingEventDate);
					hsu.update(decBJ);

					new BHRBenefitJoin(decBJ).delete();
				}
		}
hsu.flush();

		List<Beneficiary> copyBeneficiaries = new ArrayList<Beneficiary>();
		if (bene.getHasBeneficiaries() == 'Y') {
			for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
				for (HrBeneficiary beneficiary : join.getBeneficiaries()) {
					BHRBeneficiary bbeneficiary = new BHRBeneficiary(beneficiary);
					if ((bene.getContingentBeneficiaries() == 'Y' && bbeneficiary.getBeneficiaryType().equals("C")) || bbeneficiary.getBeneficiaryType().equals("P"))
						copyBeneficiaries.add(new Beneficiary(bbeneficiary));
				}
			if (copyBeneficiaries.isEmpty())
				for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
					for (HrBeneficiary beneficiary : join.getBeneficiaries()) {
						BHRBeneficiary bbeneficiary = new BHRBeneficiary(beneficiary);
						if ((bene.getContingentBeneficiaries() == 'Y' && bbeneficiary.getBeneficiaryType().equals("C")) || bbeneficiary.getBeneficiaryType().equals("P"))
							copyBeneficiaries.add(new Beneficiary(bbeneficiary));
					}
		}
hsu.flush();

		List<Physician> copyPhysicians = new ArrayList<Physician>();
		if (bene.getHasPhysicians() == 'Y') {
			for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
				for (HrPhysician physician : join.getPhysicians()) {
					BHRPhysician bphysician = new BHRPhysician(physician);
					copyPhysicians.add(new Physician(bphysician));
				}
			if (copyPhysicians.isEmpty())
				for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bbc.getBenefitId()).list())
					for (HrPhysician physician : join.getPhysicians()) {
						BHRPhysician bphysician = new BHRPhysician(physician);
						copyPhysicians.add(new Physician(bphysician));
					}
		}
hsu.flush();

		int differenceInEnrollment = 0;
		List<String> existingApprovedPersons = new LinkedList<String>();
		//look to see if they have the benefit already with the same config
		if (!enrolledInApprovedConfig(configId))
			differenceInEnrollment = 1;
		else {
			//go through all dependents and check them

			for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').list())
				//        System.out.println("Found " + bj.getCoveredPersonId());
				//does add employee in the found
				if (!bj.getCoveredPersonId().equalsIgnoreCase(bj.getPayingPersonId()))
					existingApprovedPersons.add(bj.getCoveredPersonId());
			//now check if any person was removed or added
			//how do we check this???
			//if the size of the lists of enrollIds and existingApprovedPersons don't match
			//then someone was added or removed
			if (existingApprovedPersons.size() != enrollIds.size())
				differenceInEnrollment = 1;
			else
				for (String currentCoveredPerson : existingApprovedPersons)
					if (!enrollIds.contains(currentCoveredPerson)) {
						differenceInEnrollment = 1;
						break;
					}
		}
hsu.flush();

		HrBenefit benefit = hsu.get(HrBenefitConfig.class, configId).getHrBenefit();

		//delete any pending records for configs that are exclusive
		//BEFORE this was using project_id in the query, but it has been removed
		//AND replaced by HrBenefitJoin.PAYING_PERSON_ID, possibleIds because project_id change frequently
		//BUT we just want to delete this pending benefits
		if (!benefit.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean())
			for (HrBenefitJoin pendingBJ : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, benefit.getHrBenefitCategory()).list()) {
				//delete beneficiary if there is one
				hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, pendingBJ.getBenefitJoinId()).delete();

				pendingBJ.setCoverageEndDate(qualifyingEventDate);
				pendingBJ.setPolicyEndDate(qualifyingEventDate);

				hsu.flush();

				new BHRBenefitJoin(pendingBJ).delete();
			}
hsu.flush();

		//check for existing enrollments in this benefit, delete them
		for (HrBenefitJoin pendingBJ : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefit.getBenefitId()).list()) {
			//delete beneficiary if there is one
			hsu.createCriteria(HrBeneficiary.class).eq(HrBeneficiary.BENEFIT_JOIN_ID, pendingBJ.getBenefitJoinId()).delete();

			pendingBJ.setCoverageEndDate(qualifyingEventDate);
			pendingBJ.setPolicyEndDate(qualifyingEventDate);

			hsu.flush();

			new BHRBenefitJoin(pendingBJ).delete();
		}

		HrBenefitJoin approvedJoin = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).eq(HrBenefitJoin.APPROVED, 'Y').first();

		//could be same config but different coverage amount?
		if (approvedJoin != null && !Utils.doubleEqual(approvedJoin.getAmountCovered(), coverageAmount, 0.001))
			if (approvedJoin.getEmployeeCovered() == 'Y')
				differenceInEnrollment = 3;
			else //employee isn't covered, so need to check dep joins to see if coverage amount matches
			{
				BHRBenefitJoin bapprovedJoin = new BHRBenefitJoin(approvedJoin);
				for (HrBenefitJoin depBJ : bapprovedJoin.getDependentBenefitJoins())
					if (!Utils.doubleEqual(depBJ.getAmountCovered(), coverageAmount, 0.001))
						differenceInEnrollment = 4;
			}

		if (approvedJoin != null && new BHRBenefitJoin(approvedJoin).getBenefitCategoryType() == HrBenefitCategory.FLEX_TYPE)
			if (approvedJoin.getPolicyStartDate() != qualifyingEventDate)
				differenceInEnrollment = 1;
hsu.flush();

		//NOT JUST YET
		//is it the same as the approved benefit???
		if (differenceInEnrollment == 0) {
			//return approved benefit if there is one
			if (approvedJoin != null)
				return approvedJoin.getBenefitJoinId();
			//else return nothing
			return ""; //same config
		}
		HashMap<String, HrBenefitJoin> joinsCreated = new HashMap<String, HrBenefitJoin>();
		BHRBenefitJoin originalRecord = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.APPROVED, 'Y').first());

		BHRBenefitJoin masterRecord = new BHRBenefitJoin();
		masterRecord.create();
		masterRecord.setPayingPerson(getChangeRequest().getRealRecord());
		masterRecord.setCoveredPerson(getChangeRequest().getRealRecord());
		masterRecord.setBenefitConfigId(configId);
		BHRBenefitChangeReason bbcr = new BHRBenefitChangeReason(changeReasonId);

		//if the policy join already existed, no need to change the dates on it
		if (originalRecord.getBean() != null)
			if (originalRecord.getBenefitCategoryType() == HrBenefitCategory.FLEX_TYPE) {   //For flex type benefits only
				int termDate = DateUtils.addDays(qualifyingEventDate, -1);

				originalRecord.setPolicyEndDate(termDate);
				originalRecord.setCoverageEndDate(termDate);

				if (originalRecord.isPolicyBenefitJoin())
					for (HrBenefitJoin depJoin : originalRecord.getDependentBenefitJoins()) {
						depJoin.setPolicyEndDate(termDate);
						depJoin.setCoverageEndDate(termDate);
					}
				originalRecord.update();

				masterRecord.setPolicyStartDate(qualifyingEventDate);
				if (coversEmployee) {
					masterRecord.setCoverageStartDate(qualifyingEventDate);
					masterRecord.setAmountCovered(coverageAmount);
				} else
					masterRecord.setEmployeeCovered("N");
			} else {
				masterRecord.setPolicyStartDate(originalRecord.getPolicyStartDate());
				if (coversEmployee) {
					masterRecord.setCoverageStartDate(originalRecord.getCoverageStartDate());
					masterRecord.setAmountCovered(coverageAmount);
				} else
					masterRecord.setEmployeeCovered("N");
			}
		else {	//this is a new policy join

			if (bbcr.getType() == HrBenefitChangeReason.NEW_HIRE) //is this a new hire?  maybe we should calculate dates
			{
				BHRBenefit bhrb = new BHRBenefit(benefit);
				Calendar hireCal = DateUtils.getCalendar(this.getBEmployee().getCurrentHiredDate());
				switch (bhrb.getEligibilityType()) {
					case 2:
						hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
						if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
							hireCal.add(Calendar.MONTH, 1);
						hireCal.set(Calendar.DAY_OF_MONTH, 1);
						break;
					case 3:
						hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
						if (hireCal.get(Calendar.DAY_OF_MONTH) != 1)
							hireCal.add(Calendar.MONTH, 1);
						hireCal.set(Calendar.DAY_OF_MONTH, 1);
						break;
					case 4:
						hireCal.add(Calendar.DAY_OF_YEAR, bhrb.getEligibilityPeriod());
						break;
					case 5:
						hireCal.add(Calendar.MONTH, bhrb.getEligibilityPeriod());
						break;
					default:
						break;

				}
				//are you trying to enroll as of a date before you are eligible?
				if (qualifyingEventDate < DateUtils.getDate(hireCal))
					qualifyingEventDate = DateUtils.getDate(hireCal);
			}
			masterRecord.setPolicyStartDate(qualifyingEventDate);
			if (coversEmployee) {
				char benefitAmountModel = masterRecord.getBenefitConfig().getBenefit().getBenefitAmountModel();
				masterRecord.setCoverageStartDate(qualifyingEventDate);
				if (masterRecord.getBenefitConfig().getBenefit().getEmployeeCostModel() == 'R')
					masterRecord.setAmountPaid(coverageAmount);
				if (benefitAmountModel == 'R'  ||  benefitAmountModel == 'N')  // include N for FSA accounts (i.e. employee cost & employee benefit are the same)
					masterRecord.setAmountCovered(coverageAmount);
			} else
				masterRecord.setEmployeeCovered("N");
		}
hsu.flush();

		masterRecord.setChangeDescription(changeDescription);
		masterRecord.setBenefitApproved(false);
		masterRecord.setEmployeeExplanation(explanation);

		if (bbcr.getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
			masterRecord.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
			masterRecord.setChangeReason(null);
		} else
			masterRecord.setChangeReason(changeReasonId);
		masterRecord.setProject(getChangeRequest().getProject());
		masterRecord.insert(true);
		joinsCreated.put(masterRecord.getPayingPersonId(), masterRecord.getBean());
		//if we have beneficiaries to copy, that means they should be copied to the new enrollment (unapproved of course)
//		BHRBenefitJoin masterRecord = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class)
//																				.in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds)
//																				.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId)
//																				.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
//																				.eq(HrBenefitJoin.APPROVED, 'Y')
//																				.first());
		for (Beneficiary tempBeneficiary : copyBeneficiaries) {
			BHRBeneficiary newBeneficiary = new BHRBeneficiary();
			newBeneficiary.create();
			newBeneficiary.setAddress(tempBeneficiary.address);
			newBeneficiary.setRelationship(tempBeneficiary.relationship);
			newBeneficiary.setAssociatedBenefit(masterRecord);
			newBeneficiary.setBeneficiary(tempBeneficiary.name);
			newBeneficiary.setBeneficiaryType(tempBeneficiary.type);
			newBeneficiary.setDob(tempBeneficiary.dob);
			newBeneficiary.setPercentage(tempBeneficiary.percent);
			newBeneficiary.setSsn(tempBeneficiary.ssn);
			newBeneficiary.insert();
		}
hsu.flush();

		//since we're not creating pending records immediately anymore
		//we forced to get Real or Pending Record; therefore, when creating the join we have to use one of the Beans
		//consequently, we have duplicate codes here

		for (Enrollee ee : enrollees) {
			String x = ee.getRelationshipId();
			try {
				BHREmplDependent pend = new BHREmplDependent(x);
				if (pend.getPersonId().equals(getChangeRequest().getRealRecordId()) || pend.getPersonId().equals(getChangeRequest().getChangeRecordId())) //dont re-enroll the employee
					continue;
				BHRBenefitJoin bbj = new BHRBenefitJoin();
				BHRBenefitJoin bbjDeps = new BHRBenefitJoin();
				if (existingApprovedPersons.contains(pend.getPersonId())) {
					bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
					if (bbjDeps.getBean() == null)
						bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
//					if(bbjDeps.getBenefitApproved()) {
//						bbjDeps.setBenefitApproved(false);
//						bbjDeps.update();
//					}
//					joinsCreated.put(bbjDeps.getPayingPersonId(), bbjDeps.getBean());
				}
//				else {
				//need to make a benefit join record for this person
				String id = bbj.create();
				bbj.setBenefitApproved(false);
				bbj.setBenefitConfigId(configId);
				bbj.setPayingPerson(getChangeRequest().getRealRecord());
				//if (!pend.getEnrollingPersonId().equals(pend.getPersonId())) {
				bbj.setCoveredPersonId(pend.getEnrollingPersonId()); //TODO: may have to come up with some kind of double map to get this to work
				bbj.setRelationshipId(pend.getEnrollingRelationshipId());
				//} else {
				//    bbj.setCoveredPersonId(masterRecord.getPayingPersonId());
				//    bbj.setRelationshipId(null);
				//    forceNewUser = true;
				//}
				bbj.setPolicyStartDate(masterRecord.getPolicyStartDate());
				if (bbjDeps.getBean() == null)
					bbj.setCoverageStartDate(qualifyingEventDate);
				else
					bbj.setCoverageStartDate(bbjDeps.getCoverageStartDate());
				bbj.setAmountCovered(ee.getCoverageAmount());
				bbj.setChangeDescription(changeDescription);
				bbj.setEmployeeExplanation(explanation);
				if (new BHRBenefitChangeReason(changeReasonId).getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
					bbj.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
					bbj.setChangeReason(null);
				} else
					bbj.setChangeReason(changeReasonId);
				bbj.setProject(getChangeRequest().getProject());
				bbj.insert();
				joinsCreated.put(bbj.getPayingPersonId(), bbj.getBean());
//				}
				//no longer needed... got rid of
//                if (forceNewUser) {
//                    hsu.doSQL("update hr_benefit_join set covered_person='" + pend.getEnrollingPersonId() + "' , relationship_id='" + pend.getEnrollingRelationshipId() + "' where benefit_join_id='" + id + "'");
//                }
			} catch (Exception e) {
				//WE HAVE  a real record
				BHREmplDependent pend = new BHREmplDependent(x);
				if (pend.getPersonId().equals(getChangeRequest().getRealRecordId()) || pend.getPersonId().equals(getChangeRequest().getChangeRecordId())) //dont re-enroll the employee
					continue;
				BHRBenefitJoin bbj = new BHRBenefitJoin();
				BHRBenefitJoin bbjDeps = new BHRBenefitJoin();
				if (existingApprovedPersons.contains(pend.getPersonId())) {
					bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
					if (bbjDeps.getBean() == null)
						bbjDeps = new BHRBenefitJoin(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).in(HrBenefitJoin.PAYING_PERSON_ID, possibleIds).eq(HrBenefitJoin.COVERED_PERSON_ID, pend.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').first());
//					if(bbjDeps.getBenefitApproved()) {
//						bbjDeps.setBenefitApproved(false);
//						bbjDeps.update();
//					}
//					joinsCreated.put(bbjDeps.getPayingPersonId(), bbjDeps.getBean());
				}
//				else {
				//need to make a benefit join record for this person
				String id = bbj.create();
				bbj.setBenefitApproved(false);
				bbj.setBenefitConfigId(configId);
				bbj.setPayingPerson(getChangeRequest().getRealRecord());
				//if (!pend.getPerson().getPersonId().equals(pend.getPersonId())) {
				bbj.setCoveredPersonId(pend.getPerson().getPersonId()); //TODO: may have to come up with some kind of double map to get this to work
				bbj.setRelationshipId(pend.getRelationshipId());
				//} else {
				//    bbj.setCoveredPersonId(masterRecord.getPayingPersonId());
				//    bbj.setRelationshipId(null);
				//    forceNewUser = true;
				//}
				bbj.setChangeDescription(changeDescription);
				bbj.setEmployeeExplanation(explanation);
				if (new BHRBenefitChangeReason(changeReasonId).getType() == HrBenefitChangeReason.QUALIFYING_EVENT) {
					bbj.setLifeEvent(BLifeEvent.getLifeEvent(pcr.getRealRecord(), qualifyingEventDate, changeReasonId));
					bbj.setChangeReason(null);
				} else
					bbj.setChangeReason(changeReasonId);
				bbj.setPolicyStartDate(masterRecord.getPolicyStartDate());
				if (bbjDeps.getBean() == null)
					bbj.setCoverageStartDate(qualifyingEventDate);
				else
					bbj.setCoverageStartDate(bbjDeps.getCoverageStartDate());
				bbj.setAmountCovered(ee.getCoverageAmount());
				bbj.setProject(getChangeRequest().getProject());
				bbj.insert();
				joinsCreated.put(bbj.getPayingPersonId(), bbj.getBean());
				//                if (forceNewUser) {
				//                    hsu.doSQL("update hr_benefit_join set covered_person='" + pend.getPersonId() + "' , relationship_id='" + pend.getRelationshipId() + "' where benefit_join_id='" + id + "'");
				//                }
//				}
			}

		}
hsu.flush();

		//if we have PHYSICIANS to copy, that means they should be copied to the new enrollment (unapproved of course)
		for (Physician tempPhysician : copyPhysicians)
			if (joinsCreated.containsKey(tempPhysician.coveredPersonId)) {
				BHRPhysician newPhysician = new BHRPhysician();
				newPhysician.create();
				newPhysician.setAddress(tempPhysician.address);
				newPhysician.setBenefitJoin(joinsCreated.get(tempPhysician.coveredPersonId));
				newPhysician.setPhysicianCode(tempPhysician.code);
				newPhysician.setPhysicianName(tempPhysician.name);
				newPhysician.setChangeReason(tempPhysician.changeDescription);
				newPhysician.setChangeDate(tempPhysician.changeDate);
				newPhysician.setAnnualVisit(tempPhysician.annualVisit);
				newPhysician.insert();
			}
hsu.flush();

		//check for over coverage
		if (masterRecord.checkOverCoverage() && !BProperty.getBoolean("wizardAllowInapplicable"))
			throw new ArahantWarning("You have selected a coverage level that is excessive\ngiven the dependents you are enrolling.");
		return masterRecord.getBenefitJoinId();
	}
}
