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
package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Crypto;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.IDGenerator;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = Person.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
//@Where(clause="record_type='R'")
public class Person extends AuditedBean implements IPerson, java.io.Serializable, Comparable<Person>, ArahantSaveNotify {

	private static final transient ArahantLogger logger = new ArahantLogger(Person.class);

	// The following key is used to encrypt social security numbers in the database
	// Put something unique here
	private static final String encKey = "f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8";
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "person";
	public static final String PROJECT_HISTORY_FROM_PERSON = "projectHistoriesFrom";
	public static final String PROJECT_HISTORY_TO_PERSON = "projectHistoriesTo";
	public static final String PROJECT_PERSON_JOIN = "projectEmployeeJoin";
	public static final String APPOINTMENT_PERSON_JOIN = "appointments";
	public static final String BILLING_BATCHES = "bankDraftBatches";
	public static final String SORT_ORDER = "sortOrder";
	public static final String EMPLOYEE_CHANGES = "employeeChanges";
	public static final String INVOICES = "invoices";
	public static final String TIME_OFF_REQUESTS = "timeOffRequests";
	public static final String RECIEPTS = "receipts";
	public static final String STUDENT_CALENDAR_TYPE = "studentCalendarType";
	public static final String STUDENT_VERIFICATIONS = "studentVerifications";
	public static final String CHANGE_REQUESTS = "changeRequests";
	public static final String CHANGE_REQS = "changeReqs";
	public static final String RECORD_TYPE = "recordType";

	// Fields
	private char sex = 'U';
	public static final String SEX = "sex";
	private int dob = 0;
	public static final String DOB = "dob";
	private String ssn;
	private String personId;
	public static final String PERSONID = "personId";
	private CompanyBase companyBase;
	public static final String COMPANYBASE = "companyBase";
	private String lname;
	public static final String LNAME = "lname";
	private String fname;
	public static final String FNAME = "fname";
	private short height;
	public static final String HEIGHT = "height";
	private short weight;
	public static final String WEIGHT = "weight";
	private String personalEmail;
	public static final String PERSONALEMAIL = "personalEmail";
	private String title;
	public static final String TITLE = "title";
	private int orgGroupType;
	public static final String ORGGROUPTYPE = "orgGroupType";
	private Set<ClientContact> clientContacts = new HashSet<ClientContact>(0);
	public static final String CLIENTCONTACTS = "clientContacts";
	private Set<OrgGroupAssociation> orgGroupAssociations = new HashSet<OrgGroupAssociation>(0);
	public static final String ORGGROUPASSOCIATIONS = "orgGroupAssociations";
	private Set<LoginLog> loginLogs = new HashSet<LoginLog>(0);
	public static final String LOGIN_LOGS = "loginLogs";
	private Set<Message> messagesForFromPersonId = new HashSet<Message>(0);
	public static final String MESSAGESFORFROMPERSONID = "messagesForFromPersonId";
	private Set<ProjectComment> projectComments = new HashSet<ProjectComment>(0);
	public static final String PROJECTCOMMENTS = "projectComments";
	private Set<Project> requestedProjects = new HashSet<Project>(0);
	public static final String REQUESTEDPROJECTS = "requestedProjects";
	private Set<Project> enteredProjects = new HashSet<Project>(0);
	public static final String ENTERED_PROJECTS = "entereedProjects";
	private Set<VendorContact> vendorContacts = new HashSet<VendorContact>(0);
	public static final String VENDORCONTACTS = "vendorContacts";
	private Set<Message> messagesForToPersonId = new HashSet<Message>(0);
	public static final String MESSAGESFORTOPERSONID = "messagesForToPersonId";
	private Set<StandardProject> requestedStandardProjects = new HashSet<StandardProject>(0);
	public static final String STANDARDPROJECTS = "requestedStandardProjects";
	//private Set<Employee> employees = new HashSet<Employee>(0);
	//public static final String EMPLOYEES = "employees";
	private ProphetLogin prophetLogin;
	public static final String PROPHETLOGINS = "prophetLogin";
	private Set<TimeReject> timeRejects = new HashSet<TimeReject>(0);
	public static final String TIMEREJECTS = "timeRejects";
	private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
	public static final String TIMESHEETS = "timesheets";
	private Set<Phone> phones = new HashSet<Phone>(0);
	public static final String PHONES = "phones";
	private Set<Address> addresses = new HashSet<Address>(0);
	public static final String ADDRESSES = "addresses";
	public static final String SSN = "ssn";
	private Set<PersonForm> personForms = new HashSet<PersonForm>(0);
	private Set<Invoice> invoices = new HashSet<Invoice>(0);
	private Set<Receipt> receipts = new HashSet<Receipt>(0);
	public static final String HANDICAP = "handicap";
	public static final String STUDENT = "student";
	private char handicap = 'N';
	private char student = 'N';
	private String citizenship = "";
	private String visa = "";
	private int visaStatusDate = 0;
	private int visaExpirationDate = 0;
	private char i9Part1 = 'N';
	private char i9Part2 = 'N';
	private String mname = "";
	//private Set<Project>currentProjects=new HashSet<Project>(0);
	private Set<ProjectHistory> projectHistoriesChanged = new HashSet<ProjectHistory>(0);
	private Set<HrEmplDependent> depJoinsWhereDependent = new HashSet<HrEmplDependent>(0);
	private Set<TimeOffRequest> timeOffRequests = new HashSet<TimeOffRequest>(0);
	public static final String DEP_JOINS_AS_DEPENDENT = "depJoinsWhereDependent";
	public static final String MNAME = "mname";
	public static final String HR_BENEFIT_JOINS_WHERE_COVERED = "hrBenefitJoinsWhereCovered";
	protected Set<HrBenefitJoin> hrBenefitJoinsWhereCovered = new HashSet<HrBenefitJoin>(0);
	public static final String HR_BENEFIT_JOINS_WHERE_PAYING = "hrBenefitJoinsWherePaying";
	public static final String HR_BENEFIT_JOINS_DELETED_WHERE_PAYING = "hrBenefitJoinDeletesWherePaying";
	protected Set<HrBenefitJoin> hrBenefitJoinsWherePaying = new HashSet<HrBenefitJoin>(0);
	protected Set<HrBenefitJoinHDeletes> hrBenefitJoinDeletesWherePaying = new HashSet<HrBenefitJoinHDeletes>(0);
	private Set<UserAttribute> userAttributes = new HashSet<UserAttribute>(0);
	private Set<PersonNote> personNotes = new HashSet<PersonNote>(0);
	private Set<ProjectView> projectViews = new HashSet<ProjectView>(0);
	public static final String PERSON_NOTES = "personNotes";
	private Set<ProjectEmployeeJoin> projectEmployeeJoin = new HashSet<ProjectEmployeeJoin>(0);
	private Set<ContactQuestionDetail> questionDetails = new HashSet<ContactQuestionDetail>(0);
	private Set<AppointmentPersonJoin> appointments = new HashSet<AppointmentPersonJoin>(0);
	private Set<HrBillingStatusHistory> billingStatusHistories = new HashSet<HrBillingStatusHistory>(0);
	private Set<BankDraftBatch> bankDraftBatches = new HashSet<BankDraftBatch>(0);
	private Set<Alert> alerts = new HashSet<Alert>(0);
	private Set<ElectronicFundTransfer> electronicFundTransfers = new HashSet<ElectronicFundTransfer>(0);
	private Set<EmployeeChanged> employeeChanges = new HashSet<EmployeeChanged>(0);
	private Set<StudentVerification> studentVerifications = new HashSet<StudentVerification>(0);
	public static final String REPLACE_EMPLOYER_PLAN = "replaceEmployerPlan";
	private char replaceEmployerPlan = ' ';
	public static final String NOT_MISSED_FIVE_DAYES = "notMissedFiveDays";
	private char notMissedFiveDays = ' ';
	public static final String DRUG_ALCOHOL_ABUSE = "drugAlcoholAbuse";
	private char drugAlcoholAbuse = ' ';
	public static final String TWO_FAMILY_HEART_COND = "twoFamilyHeartCond";
	private char twoFamilyHeartCond = ' ';
	public static final String HAS_OTHER_MEDICAL = "hasOtherMedical";
	private char hasOtherMedical = ' ';
	public static final String TWO_FAMILY_CANCER = "twoFamilyCancer";
	private char twoFamilyCancer = ' ';
	public static final String TWO_FAMILY_DIABETER = "twoFamilyDiabetes";
	private char twoFamilyDiabetes = ' ';
	private int sortOrder;
	public static final String NICKNAME = "nickName";
	private String nickName;
	private String driversLicenseState;
	private String driversLicenseNumber;
	private int driversLicenseExp;
	private char smoker = 'U';
	private String autoInsuranceCarrier;
	private String autoInsurancePolicy;
	private int autoInsuranceExp;
	private int autoInsuranceStart;
	private String autoInsuranceCoverage;
	private Project defaultProject;
	private char studentCalendarType = ' ';
	private char recordType = ' ';
	private char smokingProgram = 'U';
	private char militaryBranch = 'U';
	private int militaryStartDate;
	private int militaryEndDate;
	private String militaryRank;
	private char militaryDischargeType = 'U';
	private String militaryDischargeExplain;
	private char convictedOfCrime = 'U';
	private String convictedOfWhat;
	private char workedForCompanyBefore = 'N';
	private String workedForCompanyWhen;
	private char activelyAtWork = ' ';
	private char unableToPerform = ' ';
	private char hasAids = ' ';
	private char hasCancer = ' ';
	private char hasHeartCondition = ' ';
	private String hicNumber;
	public static final String HIC_NUMBER = "hicNumber";
	private Date agreementDate;
	private String agreementAddressIp;
	private String agreementAddressUrl;
	private int agreementRevision = 0;
	private String personGuid;
	public static final String PERSON_GUID = "personGuid";
	private String authenticatedEmail;
	private String defaultEmailSender = "N";
	public static final String DEFAULT_EMAIL_SENDER = "defaultEmailSender";
	private String i9p1Confirmation;
	private String i9p2Confirmation;
	private String i9p1Person;
	private String i9p2Person;
	private Date i9p1When;
	private Date i9p2When;
	private String linkedin;

	@Column(name = "record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column(name = "record_person_id")
	@Override
	public String getRecordPersonId() {
		if (recordPersonId == null) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			if (hsu.getCurrentPerson() != null)
				recordPersonId = hsu.getCurrentPerson().getPersonId();
			else
				recordPersonId = hsu.getArahantPersonId();
		}
		return recordPersonId;
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Column(name = "person_guid")
	public String getPersonGuid() {
		return personGuid;
	}

	public void setPersonGuid(String personGuid) {
		this.personGuid = personGuid;
	}

	@Column(name = "agreement_address_ip")
	public String getAgreementAddressIp() {
		return agreementAddressIp;
	}

	public void setAgreementAddressIp(String agreementAddressIp) {
		this.agreementAddressIp = agreementAddressIp;
	}

	@Column(name = "agreement_address_url")
	public String getAgreementAddressUrl() {
		return agreementAddressUrl;
	}

	public void setAgreementAddressUrl(String agreementAddressUrl) {
		this.agreementAddressUrl = agreementAddressUrl;
	}

	@Column(name = "agreement_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	@Column(name = "agreement_revision")
	public int getAgreementRevision() {
		return agreementRevision;
	}

	public void setAgreementRevision(int agreementRevision) {
		this.agreementRevision = agreementRevision;
	}

	@Column(name = "hic_number")
	public String getHicNumber() {
		return hicNumber;
	}

	public void setHicNumber(String hicNumber) {
		this.hicNumber = hicNumber;
	}

	@Column(name = "record_type")
	public char getRecordType() {
		return recordType;
	}

	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	private Set<PersonCR> changeReqs = new HashSet<PersonCR>();

	private Set<PersonChangeRequest> changeRequests = new HashSet<PersonChangeRequest>();

	// Constructors
	/**
	 * @return Returns the mi.
	 */
	/*public char getMi() {
	 if (mname.length()>0)
	 return mname.charAt(0);

	 return ' ';
	 }*/
	/**
	 * default constructor
	 */
	public Person() {
	}

	@Transient
	public String getNameFM() {

		String displayName = fname;

		if (this.getMname() != null && this.getMname().trim().length() > 0)
			displayName += " " + getMname();

		return displayName;
	}

	// Property accessors
	@Id
	@Column(name = "person_id")
	@Override
	public String getPersonId() {
		return this.personId;
	}

	@Transient
	public boolean isClient() {
		return this.orgGroupType == ArahantConstants.CLIENT_TYPE;
	}

	public void setPersonId(final String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyBase getCompanyBase() {
		return this.companyBase;
	}

	public void setCompanyBase(final CompanyBase companyBase) {
		if (companyBase == null) //can't unset the company base
		
			return;
		this.companyBase = companyBase;
	}

	@Column(name = "lname")
	public String getLname() {
		return this.lname;
	}

	public void setLname(final String lname) {
		firePropertyChange("lname", this.lname, lname);
		this.lname = lname;
	}

	@Column(name = "fname")
	public String getFname() {
		return this.fname;
	}

	public void setFname(final String fname) {
		firePropertyChange("fname", this.fname, fname);
		this.fname = fname;
	}

	@Column(name = "height")
	public short getHeight() {
		return this.height;
	}

	public void setHeight(final short height) {
		firePropertyChange("height", this.height, height);
		this.height = height;
	}

	@Column(name = "weight")
	public short getWeight() {
		return this.weight;
	}

	public void setWeight(final short weight) {
		firePropertyChange("weight", this.weight, weight);
		this.weight = weight;
	}

	@Column(name = "personal_email")
	public String getPersonalEmail() {
		return this.personalEmail;
	}

	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
	}

	@Column(name = "job_title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Column(name = "org_group_type")
	public int getOrgGroupType() {
		return this.orgGroupType;
	}

	public void setOrgGroupType(final int orgGroupType) {
		this.orgGroupType = orgGroupType;
	}
	/*
	 public Set<ClientContact> getClientContacts() {
	 return this.clientContacts;
	 }

	 public void setClientContacts(final Set<ClientContact> clientContacts) {
	 this.clientContacts = clientContacts;
	 }
	 */

	@Column(name = "i9_part1")
	public char getI9Part1() {
		return i9Part1;
	}

	public void setI9Part1(char i9Part1) {
		this.i9Part1 = i9Part1;
	}

	@Column(name = "i9_part2")
	public char getI9Part2() {
		return i9Part2;
	}

	public void setI9Part2(char i9Part2) {
		this.i9Part2 = i9Part2;
	}

	@OneToMany(mappedBy = OrgGroupAssociation.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<OrgGroupAssociation> getOrgGroupAssociations() {
		return this.orgGroupAssociations;
	}

	public void setOrgGroupAssociations(final Set<OrgGroupAssociation> orgGroupAssociations) {
		this.orgGroupAssociations = orgGroupAssociations;
	}

	@OneToMany(mappedBy = LoginLog.PERSONID, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<LoginLog> getLoginLogs() {
		return this.loginLogs;
	}

	public void setLoginLogs(final Set<LoginLog> loginLogs) {
		this.loginLogs = loginLogs;
	}

	@OneToMany(mappedBy = Message.PERSONBYFROMPERSONID, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Message> getMessagesForFromPersonId() {
		return this.messagesForFromPersonId;
	}

	public void setMessagesForFromPersonId(final Set<Message> messagesForFromPersonId) {
		this.messagesForFromPersonId = messagesForFromPersonId;
	}

	@OneToMany(mappedBy = ProjectComment.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectComment> getProjectComments() {
		return this.projectComments;
	}

	public void setProjectComments(final Set<ProjectComment> projectComments) {
		this.projectComments = projectComments;
	}

	@OneToMany(mappedBy = Project.DONE_FOR_PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Project> getRequestedProjects() {
		return this.requestedProjects;
	}

	public void setRequestedProjects(final Set<Project> projects) {
		this.requestedProjects = projects;
	}
	/*
	 public Set<VendorContact> getVendorContacts() {
	 return this.vendorContacts;
	 }

	 public void setVendorContacts(final Set<VendorContact> vendorContacts) {
	 this.vendorContacts = vendorContacts;
	 }
	 */

	@OneToMany(mappedBy = StandardProject.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<StandardProject> getRequestedStandardProjects() {
		return this.requestedStandardProjects;
	}

	public void setRequestedStandardProjects(final Set<StandardProject> standardProjects) {
		this.requestedStandardProjects = standardProjects;
	}


	/*
	 public Set<Employee> getEmployees() {
	 return this.employees;
	 }

	 public void setEmployees(final Set<Employee> employees) {
	 this.employees = employees;
	 }
	 */
	@OneToMany(mappedBy = TimeReject.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<TimeReject> getTimeRejects() {
		return this.timeRejects;
	}

	public void setTimeRejects(final Set<TimeReject> timeRejects) {
		this.timeRejects = timeRejects;
	}

	@OneToMany(mappedBy = Timesheet.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Timesheet> getTimesheets() {
		return this.timesheets;
	}

	public void setTimesheets(final Set<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	@OneToMany(mappedBy = Phone.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(final Set<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany(mappedBy = Address.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(final Set<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return Returns the prophetLogin.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public ProphetLogin getProphetLogin() {
		return prophetLogin;
	}

	/**
	 * @param prophetLogin The prophetLogin to set.
	 */
	public void setProphetLogin(final ProphetLogin prophetLogin) {
		this.prophetLogin = prophetLogin;
	}

	@Override
	public String keyColumn() {
		return "person_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setPersonId(IDGenerator.generate(this));
		return personId;
	}

	/*
	 * @return Returns the currentProjects.
	 *
	 public Set<Project> getCurrentProjects() {
	 return currentProjects;
	 }

	 /*
	 * @param currentProjects The currentProjects to set.
	 *
	 public void setCurrentProjects(final Set<Project> currentProjects) {
	 this.currentProjects = currentProjects;
	 }

	 /*
	 * @return Returns the projectHistoriesChanged.
	 */
	@OneToMany(mappedBy = ProjectHistory.CHANGED_BY, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectHistory> getProjectHistoriesChanged() {
		return projectHistoriesChanged;
	}

	/**
	 * @param projectHistoriesChanged The projectHistoriesChanged to set.
	 */
	public void setProjectHistoriesChanged(
			final Set<ProjectHistory> projectHistoriesChanged) {
		this.projectHistoriesChanged = projectHistoriesChanged;
	}

	/**
	 * @return Returns the enteredProjects.
	 */
	@OneToMany(mappedBy = Project.ENTERED_BY_PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Project> getEnteredProjects() {
		return enteredProjects;
	}

	/**
	 * @param enteredProjects The enteredProjects to set.
	 */
	public void setEnteredProjects(final Set<Project> enteredProjects) {
		this.enteredProjects = enteredProjects;
	}

	@Transient
	public String getNameLFM() {
		String displayName = lname + ", " + fname;

		if (this.getMname() != null && this.getMname().trim().length() > 0)
			displayName += " " + getMname();

		return displayName;
	}

	@Transient
	public String getNameFL() {
		String displayName = fname == null ? "" : fname;
		return lname == null ? displayName : displayName + " " + lname;
	}

	@Transient
	public String getNameFML() {
		String displayName = fname == null ? "" : fname;
		if (mname != null && !mname.trim().isEmpty())
			displayName += " " + mname;
		return lname == null ? displayName : displayName + " " + lname;
	}

	/**
	 * @return Returns the unencrypted ssn.
	 */
	@Transient
	public String getUnencryptedSsn() {
		if (ssn != null && ssn.length() > 11)
			try {
				return Crypto.decryptTripleDES(encKey, ssn);
			} catch (Exception e) {
				return ssn;
			}
		return ssn;
	}

	/**
	 * Converts an encrypted SSN into an unencrypted SSN
	 */
	public static String decryptSsn(String ssn) {
		if (ssn != null && ssn.length() > 11)
			try {
				return Crypto.decryptTripleDES(encKey, ssn);
			} catch (Exception e) {
				return ssn;
			}
		return ssn;
	}

	/**
	 * Converts an unencrypted SSN into an encrypted SSN
	 */
	public static String encryptSsn(String ssn) {
		if (ssn == null || ssn.isEmpty())
			return "";
		else
			try {
				return Crypto.encryptTripleDES(encKey, ssn);
			} catch (Exception e) {
				return "";
			}
	}

	/**
	 * Set the ssn. Pass in the unencrypted SSN. This method encrypts the SSN
	 * and sets the database column.
	 *
	 * @param ssn The ssn to set.
	 */
	public void setUnencryptedSsn(String ssn) {
		if (ssn == null || ssn.length() == 0)
			setSsn(null);
		else
			try {
				setSsn(Crypto.encryptTripleDES(encKey, ssn));
			} catch (Exception e) {
				setSsn(null);
				logger.error("error encrypting an SSN", e);
			}
	}

	/**
	 * This method should only be used internally by Hibernate. Use
	 * getUnencryptedSsn() instead of this method.
	 *
	 * @return Returns the encrypted ssn.
	 */
	@Column(name = "ssn")
	public String getSsn() {
		return ssn;
	}

	/**
	 * This method should only be used internally by Hibernate and in this
	 * class. Use setUnencryptedSsn() instead of this method.
	 *
	 * @param ssn The ssn to set. ssn must already be encrypted at this point!
	 */
	public void setSsn(String ssn) {
		String oldSSN = getUnencryptedSsn();
		try {
			String newSSN;
			if (ssn == null || ssn.length() == 0)
				newSSN = null;
			else
				newSSN = Crypto.decryptTripleDES(encKey, ssn);
			firePropertyChange("ssn", oldSSN, newSSN);
		} catch (Exception e) {
			//  do nothing
		}
		this.ssn = ssn;
	}

	/**
	 * @return Returns the dob.
	 */
	@Column(name = "dob")
	public int getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(final int dob) {
		firePropertyChange("dob", this.dob, dob);
		this.dob = dob;
	}

	@OneToMany(mappedBy = Invoice.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	/**
	 * @return Returns the sex.
	 */
	@Column(name = "sex")
	public char getSex() {
		return sex;
	}

	/**
	 * @param sex The sex to set.
	 */
	public void setSex(final char sex) {
		this.sex = sex;
	}

	/**
	 * @return Returns the personForms.
	 */
	@OneToMany(mappedBy = PersonForm.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<PersonForm> getPersonForms() {
		return personForms;
	}

	/**
	 * @param personForms The personForms to set.
	 */
	public void setPersonForms(final Set<PersonForm> personForms) {
		this.personForms = personForms;
	}

	/**
	 * @return Returns the depJoins.
	 */
	@OneToMany(mappedBy = HrEmplDependent.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrEmplDependent> getDepJoinsWhereDependent() {
		return depJoinsWhereDependent;
	}

	/**
	 * @param depJoins The depJoins to set.
	 */
	public void setDepJoinsWhereDependent(final Set<HrEmplDependent> depJoins) {
		this.depJoinsWhereDependent = depJoins;
	}

	/**
	 * @return Returns the handicap.
	 */
	@Column(name = "handicap")
	public char getHandicap() {
		return handicap;
	}

	/**
	 * @param handicap The handicap to set.
	 */
	public void setHandicap(final char handicap) {
		this.handicap = handicap;
	}

	/**
	 * @return Returns the student.
	 */
	@Column(name = "student")
	public char getStudent() {
		return student;
	}

	/**
	 * @param student The student to set.
	 */
	public void setStudent(final char student) {
		this.student = student;
	}

	@Column(name = "citizenship")
	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(final String citizenship) {
		this.citizenship = citizenship;
	}

	@Column(name = "visa")
	public String getVisa() {
		return visa;
	}

	public void setVisa(final String visa) {
		this.visa = visa;
	}

	@Column(name = "visa_status_date")
	public int getVisaStatusDate() {
		return visaStatusDate;
	}

	public void setVisaStatusDate(final int visaStatusDate) {
		this.visaStatusDate = visaStatusDate;
	}

	@Column(name = "visa_exp_date")
	public int getVisaExpirationDate() {
		return visaExpirationDate;
	}

	public void setVisaExpirationDate(final int visaExpirationDate) {
		this.visaExpirationDate = visaExpirationDate;
	}

	/**
	 * @return Returns the userAttributes.
	 */
	@OneToMany(mappedBy = UserAttribute.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<UserAttribute> getUserAttributes() {
		return userAttributes;
	}

	@OneToMany(mappedBy = ContactQuestionDetail.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ContactQuestionDetail> getQuestionDetails() {
		return questionDetails;
	}

	public void setQuestionDetails(Set<ContactQuestionDetail> questionDetails) {
		this.questionDetails = questionDetails;
	}

	/**
	 * @param userAttributes The userAttributes to set.
	 */
	public void setUserAttributes(final Set<UserAttribute> userAttributes) {
		this.userAttributes = userAttributes;
	}

	/**
	 * @return
	 */
	@Transient
	public String getNameWithLogin() {

		String ret = lname + ", " + fname;

		if (getProphetLogin() != null)
			ret += " (" + getProphetLogin().getUserLogin() + ")";

		return ret;
	}

	/**
	 * @return Returns the hrEmployeeBenefitJoins.
	 */
	@OneToMany(mappedBy = HrBenefitJoin.COVERED_PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoin> getHrBenefitJoinsWhereCovered() {
		return hrBenefitJoinsWhereCovered;
	}

	/**
	 * @param hrEmployeeBenefitJoins The hrEmployeeBenefitJoins to set.
	 */
	public void setHrBenefitJoinsWhereCovered(Set<HrBenefitJoin> hrEmployeeBenefitJoins) {
		this.hrBenefitJoinsWhereCovered = hrEmployeeBenefitJoins;
	}

	@OneToMany(mappedBy = HrBenefitJoin.PAYING_PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoin> getHrBenefitJoinsWherePaying() {
		return hrBenefitJoinsWherePaying;
	}

	public void setHrBenefitJoinsWherePaying(
			Set<HrBenefitJoin> hrBenefitJoinsWherePaying) {
		this.hrBenefitJoinsWherePaying = hrBenefitJoinsWherePaying;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	@JoinColumn(name = "paying_person")
//	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoinHDeletes> getHrBenefitJoinDeletesWherePaying() {
		return hrBenefitJoinDeletesWherePaying;
	}

	public void setHrBenefitJoinDeletesWherePaying(Set<HrBenefitJoinHDeletes> hrBenefitJoinDeletesWherePaying) {
		this.hrBenefitJoinDeletesWherePaying = hrBenefitJoinDeletesWherePaying;
	}

	/**
	 * @return Returns the hrEmployeeNotes.
	 */
	@OneToMany(mappedBy = PersonNote.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<PersonNote> getPersonNotes() {
		return personNotes;
	}

	/**
	 * @param personNotes
	 */
	public void setPersonNotes(Set<PersonNote> personNotes) {
		this.personNotes = personNotes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Person o) {

		if (o == null)
			return -1;
		if (lname == null)
			return 1;
		if (o.lname == null)
			return 1;
		if (lname.compareTo(o.lname) != 0)
			return lname.compareTo(o.lname);

		if (fname.compareTo(o.fname) != 0)
			return fname.compareTo(o.fname);

		if (mname.compareTo(o.mname) != 0)
			return mname.compareTo(o.mname);

		if (ssn == null && o.ssn == null)
			return 0;

		if (ssn == null)
			return 1;

		if (o.ssn == null)
			return -1;

		return ssn.compareTo(o.ssn);
	}

	@Override
	public boolean equals(Object o) {
		if (personId == null && o == null)
			return true;
		if (personId != null && o instanceof Person)
			return personId.equals(((Person) o).getPersonId());

		return false;
	}

	@Override
	public int hashCode() {
		if (personId == null)
			return 0;
		return personId.hashCode();
	}

	@Column(name = "mname")
	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		if (mname == null)
			mname = "";
		firePropertyChange("mname", this.mname, mname);
		this.mname = mname;
	}

	/**
	 * @return
	 */
	@Transient
	public String getInitials() {
		String initials = "";
		try {
			if (fname.length() > 0)
				initials += fname.charAt(0);
			if (mname.length() > 0)
				initials += mname.charAt(0);
			if (lname.length() > 0)
				initials += lname.charAt(0);
		} catch (RuntimeException re) {
			//ignore and return what I have
		}
		return initials;
	}

	@OneToMany(mappedBy = ProjectView.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectView> getProjectViews() {
		return projectViews;
	}

	public void setProjectViews(Set<ProjectView> projectViews) {
		this.projectViews = projectViews;
	}

	@OneToMany(mappedBy = ProjectEmployeeJoin.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectEmployeeJoin> getProjectEmployeeJoin() {
		return projectEmployeeJoin;
	}

	public void setProjectEmployeeJoin(Set<ProjectEmployeeJoin> projectEmployeeJoin) {
		this.projectEmployeeJoin = projectEmployeeJoin;
	}

	@OneToMany(mappedBy = AppointmentPersonJoin.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<AppointmentPersonJoin> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<AppointmentPersonJoin> appointments) {
		this.appointments = appointments;
	}

	@OneToMany(mappedBy = HrBillingStatusHistory.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBillingStatusHistory> getBillingStatusHistories() {
		return billingStatusHistories;
	}

	public void setBillingStatusHistories(Set<HrBillingStatusHistory> billingStatusHistories) {
		this.billingStatusHistories = billingStatusHistories;
	}

	@ManyToMany
	@JoinTable(name = "bank_draft_detail",
			joinColumns = {
				@JoinColumn(name = "person_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "bank_draft_id")})
	public Set<BankDraftBatch> getBankDraftBatches() {
		return bankDraftBatches;
	}

	public void setBankDraftBatches(Set<BankDraftBatch> bankDraftBatches) {
		this.bankDraftBatches = bankDraftBatches;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "alert_person_join",
			joinColumns = {
				@JoinColumn(name = "person_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "alert_id")})
	public Set<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(Set<Alert> alerts) {
		this.alerts = alerts;
	}

	@OneToMany(mappedBy = ElectronicFundTransfer.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ElectronicFundTransfer> getElectronicFundTransfers() {
		return electronicFundTransfers;
	}

	public void setElectronicFundTransfers(Set<ElectronicFundTransfer> electronicFundTransfers) {
		this.electronicFundTransfers = electronicFundTransfers;
	}

	@OneToMany(mappedBy = EmployeeChanged.EMPLOYEE, fetch = FetchType.LAZY)
	// @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<EmployeeChanged> getEmployeeChanges() {
		return employeeChanges;
	}

	public void setEmployeeChanges(Set<EmployeeChanged> employeeChanges) {
		this.employeeChanges = employeeChanges;
	}

	@Column(name = "sort_order")
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String notifyId() {
		return personId;
	}

	@Column(name = "auto_insurance_carrier")
	public String getAutoInsuranceCarrier() {
		return autoInsuranceCarrier;
	}

	public void setAutoInsuranceCarrier(String autoInsuranceCarrier) {
		this.autoInsuranceCarrier = autoInsuranceCarrier;
	}

	@Column(name = "auto_insurance_exp")
	public int getAutoInsuranceExp() {
		return autoInsuranceExp;
	}

	public void setAutoInsuranceExp(int autoInsuranceExp) {
		this.autoInsuranceExp = autoInsuranceExp;
	}

	@Column(name = "auto_insurance_policy")
	public String getAutoInsurancePolicy() {
		return autoInsurancePolicy;
	}

	public void setAutoInsurancePolicy(String autoInsurancePolicy) {
		this.autoInsurancePolicy = autoInsurancePolicy;
	}

	@Column(name = "drivers_license_exp")
	public int getDriversLicenseExp() {
		return driversLicenseExp;
	}

	public void setDriversLicenseExp(int driversLicenseExp) {
		this.driversLicenseExp = driversLicenseExp;
	}

	@Column(name = "drivers_license_number")
	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber) {
		this.driversLicenseNumber = driversLicenseNumber;
	}

	@Column(name = "drivers_license_state")
	public String getDriversLicenseState() {
		return driversLicenseState;
	}

	public void setDriversLicenseState(String driversLicenseState) {
		this.driversLicenseState = driversLicenseState;
	}

	@Column(name = "nickname")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		if (nickName != null && nickName.length() > 15)
			nickName = nickName.substring(0, 15);
		this.nickName = nickName;
	}

	@Column(name = "smoker")
	public char getSmoker() {
		return smoker;
	}

	public void setSmoker(char smoker) {
		this.smoker = smoker;
	}

	@Column(name = "auto_insurance_coverage")
	public String getAutoInsuranceCoverage() {
		return autoInsuranceCoverage;
	}

	public void setAutoInsuranceCoverage(String autoInsuranceCoverage) {
		this.autoInsuranceCoverage = autoInsuranceCoverage;
	}

	@Column(name = "auto_insurance_start")
	public int getAutoInsuranceStart() {
		return autoInsuranceStart;
	}

	public void setAutoInsuranceStart(int autoInsuranceStart) {
		this.autoInsuranceStart = autoInsuranceStart;
	}

	@Override
	public ArahantHistoryBean historyObject() {
		return new PersonH();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "default_project_id")
	public Project getDefaultProject() {
		return defaultProject;
	}

	public void setDefaultProject(Project defaultProject) {
		this.defaultProject = defaultProject;
	}

	@OneToMany(mappedBy = TimeOffRequest.REQUESTING_PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<TimeOffRequest> getTimeOffRequests() {
		return timeOffRequests;
	}

	public void setTimeOffRequests(Set<TimeOffRequest> timeOffRequests) {
		this.timeOffRequests = timeOffRequests;
	}

	@Override
	public String notifyClassName() {
		return "Person";
	}

	@OneToMany(mappedBy = Receipt.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(Set<Receipt> receipts) {
		this.receipts = receipts;
	}

	@Column(name = "student_calendar_type")
	public char getStudentCalendarType() {
		return studentCalendarType;
	}

	public void setStudentCalendarType(char studentCalendarType) {
		this.studentCalendarType = studentCalendarType;
	}

	@OneToMany(mappedBy = StudentVerification.PERSON, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<StudentVerification> getStudentVerifications() {
		return studentVerifications;
	}

	public void setStudentVerifications(Set<StudentVerification> studentVerifications) {
		this.studentVerifications = studentVerifications;
	}

	@OneToMany(mappedBy = PersonChangeRequest.PERSON, fetch = FetchType.LAZY)
	// @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<PersonChangeRequest> getChangeRequests() {
		return changeRequests;
	}

	public void setChangeRequests(Set<PersonChangeRequest> changeRequests) {
		this.changeRequests = changeRequests;
	}

	@OneToMany
	@JoinColumn(name = "change_record_id")
	public Set<PersonCR> getChangeReqs() {
		return changeReqs;
	}

	public void setChangeReqs(Set<PersonCR> changeReqs) {
		this.changeReqs = changeReqs;
	}

	@Column(name = "smoking_program")
	public char getSmokingProgram() {
		return smokingProgram;
	}

	public void setSmokingProgram(char smokingProgram) {
		this.smokingProgram = smokingProgram;
	}

	@Column(name = "military_branch")
	public char getMilitaryBranch() {
		return militaryBranch;
	}

	public void setMilitaryBranch(char militaryBranch) {
		this.militaryBranch = militaryBranch;
	}

	@Column(name = "military_discharge_explain")
	public String getMilitaryDischargeExplain() {
		return militaryDischargeExplain;
	}

	public void setMilitaryDischargeExplain(String militaryDischargeExplain) {
		this.militaryDischargeExplain = militaryDischargeExplain;
	}

	@Column(name = "military_discharge_type")
	public char getMilitaryDischargeType() {
		return militaryDischargeType;
	}

	public void setMilitaryDischargeType(char militaryDischargeType) {
		this.militaryDischargeType = militaryDischargeType;
	}

	@Column(name = "military_end_date")
	public int getMilitaryEndDate() {
		return militaryEndDate;
	}

	public void setMilitaryEndDate(int militaryEndDate) {
		this.militaryEndDate = militaryEndDate;
	}

	@Column(name = "military_rank")
	public String getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(String militaryRank) {
		this.militaryRank = militaryRank;
	}

	@Column(name = "military_start_date")
	public int getMilitaryStartDate() {
		return militaryStartDate;
	}

	public void setMilitaryStartDate(int militaryStartDate) {
		this.militaryStartDate = militaryStartDate;
	}

	@Column(name = "convicted_of_crime")
	public char getConvictedOfCrime() {
		return convictedOfCrime;
	}

	public void setConvictedOfCrime(char convictedOfCrime) {
		this.convictedOfCrime = convictedOfCrime;
	}

	@Column(name = "convicted_of_what")
	public String getConvictedOfWhat() {
		return convictedOfWhat;
	}

	public void setConvictedOfWhat(String convictedOfWhat) {
		this.convictedOfWhat = convictedOfWhat;
	}

	@Column(name = "worked_for_company_before")
	public char getWorkedForCompanyBefore() {
		return workedForCompanyBefore;
	}

	public void setWorkedForCompanyBefore(char workedForCompanyBefore) {
		this.workedForCompanyBefore = workedForCompanyBefore;
	}

	@Column(name = "worked_for_company_when")
	public String getWorkedForCompanyWhen() {
		return workedForCompanyWhen;
	}

	public void setWorkedForCompanyWhen(String workedForCompanyWhen) {
		this.workedForCompanyWhen = workedForCompanyWhen;
	}

	@Column(name = "actively_at_work")
	public char getActivelyAtWork() {
		return activelyAtWork;
	}

	public void setActivelyAtWork(char activelyAtWork) {
		this.activelyAtWork = activelyAtWork;
	}

	@Column(name = "unable_to_perform")
	public char getUnableToPerform() {
		return unableToPerform;
	}

	public void setUnableToPerform(char unableToPerform) {
		this.unableToPerform = unableToPerform;
	}

	@Column(name = "has_aids")
	public char getHasAids() {
		return hasAids;
	}

	public void setHasAids(char hasAids) {
		this.hasAids = hasAids;
	}

	@Column(name = "has_cancer")
	public char getHasCancer() {
		return hasCancer;
	}

	public void setHasCancer(char hasCancer) {
		this.hasCancer = hasCancer;
	}

	@Column(name = "has_heart_condition")
	public char getHasHeartCondition() {
		return hasHeartCondition;
	}

	public void setHasHeartCondition(char hasHeartCondition) {
		this.hasHeartCondition = hasHeartCondition;
	}

	@Column(name = "drug_alcohol_abuse")
	public char getDrugAlcoholAbuse() {
		return drugAlcoholAbuse;
	}

	public void setDrugAlcoholAbuse(char drugAlcoholAbuse) {
		this.drugAlcoholAbuse = drugAlcoholAbuse;
	}

	@Column(name = "not_missed_five_days")
	public char getNotMissedFiveDays() {
		return notMissedFiveDays;
	}

	public void setNotMissedFiveDays(char notMissedFiveDays) {
		this.notMissedFiveDays = notMissedFiveDays;
	}

	@Column(name = "replace_employer_plan")
	public char getReplaceEmployerPlan() {
		return replaceEmployerPlan;
	}

	public void setReplaceEmployerPlan(char replaceEmployerPlan) {
		this.replaceEmployerPlan = replaceEmployerPlan;
	}

	@Column(name = "two_family_cancer")
	public char getTwoFamilyCancer() {
		return twoFamilyCancer;
	}

	public void setTwoFamilyCancer(char twoFamilyCancer) {
		this.twoFamilyCancer = twoFamilyCancer;
	}

	@Column(name = "two_family_diabetes")
	public char getTwoFamilyDiabetes() {
		return twoFamilyDiabetes;
	}

	public void setTwoFamilyDiabetes(char twoFamilyDiabetes) {
		this.twoFamilyDiabetes = twoFamilyDiabetes;
	}

	@Column(name = "two_family_heart_cond")
	public char getTwoFamilyHeartCond() {
		return twoFamilyHeartCond;
	}

	public void setTwoFamilyHeartCond(char twoFamilyHeartCond) {
		this.twoFamilyHeartCond = twoFamilyHeartCond;
	}

	@Column(name = "has_other_medical")
	public char getHasOtherMedical() {
		return hasOtherMedical;
	}

	public void setHasOtherMedical(char hasOtherMedical) {
		this.hasOtherMedical = hasOtherMedical;
	}

	@Column(name = "default_email_sender")
	public String getDefaultEmailSender() {
		return defaultEmailSender;
	}

	public void setDefaultEmailSender(String defaultEmailSender) {
		this.defaultEmailSender = defaultEmailSender;
	}

	@Column(name = "i9p1_confirmation")
	public String getI9p1Confirmation() {
		return i9p1Confirmation;
	}

	public void setI9p1Confirmation(String i9p1Confirmation) {
		this.i9p1Confirmation = i9p1Confirmation;
	}

	@Column(name = "i9p2_confirmation")
	public String getI9p2Confirmation() {
		return i9p2Confirmation;
	}

	public void setI9p2Confirmation(String i9p2Confirmation) {
		this.i9p2Confirmation = i9p2Confirmation;
	}

	@Column(name = "i9p1_person")
	public String getI9p1Person() {
		return i9p1Person;
	}

	public void setI9p1Person(String i9p1Person) {
		this.i9p1Person = i9p1Person;
	}

	@Column(name = "i9p2_person")
	public String getI9p2Person() {
		return i9p2Person;
	}

	public void setI9p2Person(String i9p2Person) {
		this.i9p2Person = i9p2Person;
	}

	@Column(name = "i9p1_when")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getI9p1When() {
		return i9p1When;
	}

	public void setI9p1When(Date i9p1When) {
		this.i9p1When = i9p1When;
	}

	@Column(name = "i9p2_when")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getI9p2When() {
		return i9p2When;
	}

	public void setI9p2When(Date i9p2When) {
		this.i9p2When = i9p2When;
	}

	@Column(name = "linkedin")
	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	@Override
	public String keyValue() {
		return getPersonId();
	}

	public short calcAgeAsOf(int asOfDate) {
		if (dob < 1)
			return 0;

		final Calendar dob2 = DateUtils.getCalendar(dob);
		final Calendar now = DateUtils.getCalendar(asOfDate);

		short age = -1;

		while (dob2.before(now)) {
			age++;
			dob2.add(Calendar.YEAR, 1);
		}

		return age;
	}

	public static String encKey() {
		return encKey;
	}

}
