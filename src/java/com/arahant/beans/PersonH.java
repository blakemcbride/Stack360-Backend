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
 *
 * Created on Oct 9, 2007
*/

package com.arahant.beans;

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Crypto;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = PersonH.TABLE_NAME)
public class PersonH extends ArahantHistoryBean implements Serializable, Comparable<PersonH> {
	
	private static final transient ArahantLogger logger = new ArahantLogger(PersonH.class);
    public static final String TABLE_NAME = "person_h";
    public static final String PROJECT_HISTORY_FROM_PERSON = "projectHistoriesFrom";
    public static final String PROJECT_HISTORY_TO_PERSON = "projectHistoriesTo";
    public static final String PROJECT_PERSON_JOIN = "projectEmployeeJoin";
    public static final String APPOINTMENT_PERSON_JOIN = "appointments";
    public static final String BILLING_BATCHES = "bankDraftBatches";
    public static final String SORT_ORDER = "sortOrder";
    public static final String EMPLOYEE_CHANGES = "employeeChanges";
    private static final long serialVersionUID = -2215686606754182240L;
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
    private Set<Message> messagesForFromPersonId = new HashSet<Message>(0);
    public static final String MESSAGESFORFROMPERSONID = "messagesForFromPersonId";
    private Set<ProjectComment> projectComments = new HashSet<ProjectComment>(0);
    public static final String PROJECTCOMMENTS = "projectComments";
    private Set<Project> requestedProjects = new HashSet<Project>(0);
    public static final String REQUESTEDPROJECTS = "requestedProjects";
    private Set<Project> enteredProjects = new HashSet<Project>(0);
    public static final String ENTERED_PROJECTS = "entereedProjects";
    //private Set <VendorContact> vendorContacts  = new HashSet<VendorContact>(0);
    public static final String VENDORCONTACTS = "vendorContacts";
    private Set<Message> messagesForToPersonId = new HashSet<Message>(0);
    public static final String MESSAGESFORTOPERSONID = "messagesForToPersonId";
    private Set<StandardProject> requestedStandardProjects = new HashSet<StandardProject>(0);
    public static final String STANDARDPROJECTS = "requestedStandardProjects";
    //private Set<Employee> employees = new HashSet<Employee>(0);
    //public static final String EMPLOYEES = "employees";
//	private ProphetLogin prophetLogin;
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
    public static final String DEP_JOINS_AS_DEPENDENT = "depJoinsWhereDependent";
    public static final String MNAME = "mname";
    public static final String HR_BENEFIT_JOINS_WHERE_COVERED = "hrBenefitJoinsWhereCovered";
    protected Set<HrBenefitJoin> hrBenefitJoinsWhereCovered = new HashSet<HrBenefitJoin>(0);
    public static final String HR_BENEFIT_JOINS_WHERE_PAYING = "hrBenefitJoinsWherePaying";
    protected Set<HrBenefitJoin> hrBenefitJoinsWherePaying = new HashSet<HrBenefitJoin>(0);
    private Set<UserAttribute> userAttributes = new HashSet<UserAttribute>(0);
    private Set<PersonNote> personNotes = new HashSet<PersonNote>(0);
    private Set<ProjectView> projectViews = new HashSet<ProjectView>(0);
    public static final String PERSON_NOTES = "personNotes";
    public static final String REPLACE_EMPLOYER_PLAN = "replaceEmployerPlan";
    private char replaceEmployerPlan = ' ';
    public static final String NOT_MISSED_FIVE_DAYES = "notMissedFiveDays";
    private char notMissedFiveDays = ' ';
    public static final String DRUG_ALCOHOL_ABUSE = "drugAlcoholAbust";
    private char drugAlcoholAbust = ' ';
    public static final String TWO_FAMILY_HEART_COND = "twoFamilyHeartCond";
    private char twoFamilyHeartCond =  ' ';
    public static final String TWO_FAMILY_CANCER = "twoFamilyCancer";
    private char twoFamilyCancer = ' ';
    public static final String TWO_FAMILY_DIABETER = "twoFamilyDiabetes";
    private char twoFamilyDiabetes = ' ';
    public static final String HAS_OTHER_MEDICAL = "hasOtherMedical";
    private char hasOtherMedical = ' ';
    private Set<ProjectEmployeeJoin> projectEmployeeJoin = new HashSet<ProjectEmployeeJoin>(0);
    private Set<ContactQuestionDetail> questionDetails = new HashSet<ContactQuestionDetail>(0);
    private Set<AppointmentPersonJoin> appointments = new HashSet<AppointmentPersonJoin>(0);
    private Set<HrBillingStatusHistory> billingStatusHistories = new HashSet<HrBillingStatusHistory>(0);
    private Set<BankDraftBatch> bankDraftBatches = new HashSet<BankDraftBatch>(0);
    private Set<ElectronicFundTransfer> electronicFundTransfers = new HashSet<ElectronicFundTransfer>(0);
    private Set<EmployeeChanged> employeeChanges = new HashSet<EmployeeChanged>(0);
    private int sortOrder;
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
    private String defaultProjectId;
    private char studentCalendarType = ' ';
    private char recordType='R';
    private char smokingProgram = 'U';
    private char militaryBranch = 'U';
    private int militaryStartDate;
    private int militaryEndDate;
    private String militaryRank;
    private char militaryDischargeType = 'U';
    private String militaryDischargeExplain;
    private short height;
    private short weight;
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

    private Date agreementDate;
    private String agreementAddressIp;
    private String agreementAddressUrl;
    private int agreementRevision = 0;
    private String personGuid;

    public static final String PERSON_GUID = "personGuid";

    @Id
    @Override
    public String getHistory_id() {
        return history_id;
    }

    /**
     * @return Returns the recordChangeType.
     */
    @Column(name = "record_change_type")
    @Override
    public char getRecordChangeType() {
        return recordChangeType;
    }

    @Column(name = "record_person_id")
    @Override
    public String getRecordPersonId() {
        return recordPersonId;
    }

    @Column(name = "record_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Override
    public Date getRecordChangeDate() {
        return recordChangeDate;
    }
   	@Column(name="person_guid")
	public String getPersonGuid() {
		return personGuid;
	}

	public void setPersonGuid(String personGuid) {
		this.personGuid = personGuid;
	}

	@Column(name="agreement_address_ip")
	public String getAgreementAddressIp() {
		return agreementAddressIp;
	}

	public void setAgreementAddressIp(String agreementAddressIp) {
		this.agreementAddressIp = agreementAddressIp;
	}

	@Column(name="agreement_address_url")
	public String getAgreementAddressUrl() {
		return agreementAddressUrl;
	}

	public void setAgreementAddressUrl(String agreementAddressUrl) {
		this.agreementAddressUrl = agreementAddressUrl;
	}

	@Column(name="agreement_date")
    @Temporal(TemporalType.TIMESTAMP)
	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	@Column(name="agreement_revision")
	public int getAgreementRevision() {
		return agreementRevision;
	}

	public void setAgreementRevision(int agreementRevision) {
		this.agreementRevision = agreementRevision;
	}

	@Column(name="hic_number")
	public String getHicNumber() {
		return hicNumber;
	}

	public void setHicNumber(String hicNumber) {
		this.hicNumber = hicNumber;
	}

    public PersonH() {
    }

    @Override
    public String keyColumn() {
        return null;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public int compareTo(PersonH o) {
        return o.getRecordChangeDate().compareTo(getRecordChangeDate());
    }

    @Override
    public void copy(IAuditedBean ab) {
        super.copy(ab);
    }

    @Override
    public boolean alreadyThere() {
        try {
            PreparedStatement stmt = ArahantSession.getHSU().getConnection().prepareStatement("select * from person_h where person_id=? and record_change_date=?");

            stmt.setString(1, personId);
            stmt.setTimestamp(2, new java.sql.Timestamp(getRecordChangeDate().getTime()));
            ResultSet rs = stmt.executeQuery();
            boolean found = rs.next();

            rs.close();
            stmt.close();

            return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_join")
    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    @OneToMany(mappedBy = AppointmentPersonJoin.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<AppointmentPersonJoin> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<AppointmentPersonJoin> appointments) {
        this.appointments = appointments;
    }

    @Column(name = "auto_insurance_carrier")
    public String getAutoInsuranceCarrier() {
        return autoInsuranceCarrier;
    }

    public void setAutoInsuranceCarrier(String autoInsuranceCarrier) {
        this.autoInsuranceCarrier = autoInsuranceCarrier;
    }

    @Column(name = "auto_insurance_coverage")
    public String getAutoInsuranceCoverage() {
        return autoInsuranceCoverage;
    }

    public void setAutoInsuranceCoverage(String autoInsuranceCoverage) {
        this.autoInsuranceCoverage = autoInsuranceCoverage;
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

    @Column(name = "auto_insurance_start")
    public int getAutoInsuranceStart() {
        return autoInsuranceStart;
    }

    public void setAutoInsuranceStart(int autoInsuranceStart) {
        this.autoInsuranceStart = autoInsuranceStart;
    }

    @ManyToMany
    @JoinTable(name = "bank_draft_detail",
    joinColumns = {@JoinColumn(name = "person_id")},
    inverseJoinColumns = {@JoinColumn(name = "bank_draft_id")})
    public Set<BankDraftBatch> getBankDraftBatches() {
        return bankDraftBatches;
    }

    public void setBankDraftBatches(Set<BankDraftBatch> bankDraftBatches) {
        this.bankDraftBatches = bankDraftBatches;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_id")
    public Set<HrBillingStatusHistory> getBillingStatusHistories() {
        return billingStatusHistories;
    }

    public void setBillingStatusHistories(Set<HrBillingStatusHistory> billingStatusHistories) {
        this.billingStatusHistories = billingStatusHistories;
    }

    @Column(name = "citizenship")
    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }
    /*
    public Set<ClientContact> getClientContacts() {
    return clientContacts;
    }

    public void setClientContacts(Set<ClientContact> clientContacts) {
    this.clientContacts = clientContacts;
    }
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public CompanyBase getCompanyBase() {
        return companyBase;
    }

    public void setCompanyBase(CompanyBase companyBase) {
        this.companyBase = companyBase;
    }

    @OneToMany(mappedBy = HrEmplDependent.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmplDependent> getDepJoinsWhereDependent() {
        return depJoinsWhereDependent;
    }

    public void setDepJoinsWhereDependent(Set<HrEmplDependent> depJoinsWhereDependent) {
        this.depJoinsWhereDependent = depJoinsWhereDependent;
    }

    @Column(name = "dob")
    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
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

    @OneToMany(mappedBy = ElectronicFundTransfer.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ElectronicFundTransfer> getElectronicFundTransfers() {
        return electronicFundTransfers;
    }

    public void setElectronicFundTransfers(Set<ElectronicFundTransfer> electronicFundTransfers) {
        this.electronicFundTransfers = electronicFundTransfers;
    }

    @OneToMany(mappedBy = EmployeeChanged.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<EmployeeChanged> getEmployeeChanges() {
        return employeeChanges;
    }

    public void setEmployeeChanges(Set<EmployeeChanged> employeeChanges) {
        this.employeeChanges = employeeChanges;
    }

    @OneToMany(mappedBy = Project.ENTERED_BY_PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Project> getEnteredProjects() {
        return enteredProjects;
    }

    public void setEnteredProjects(Set<Project> enteredProjects) {
        this.enteredProjects = enteredProjects;
    }

    @Column(name = "fname")
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Column(name = "handicap")
    public char getHandicap() {
        return handicap;
    }

    public void setHandicap(char handicap) {
        this.handicap = handicap;
    }

    @OneToMany(mappedBy = HrBenefitJoin.COVERED_PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrBenefitJoin> getHrBenefitJoinsWhereCovered() {
        return hrBenefitJoinsWhereCovered;
    }

    public void setHrBenefitJoinsWhereCovered(Set<HrBenefitJoin> hrBenefitJoinsWhereCovered) {
        this.hrBenefitJoinsWhereCovered = hrBenefitJoinsWhereCovered;
    }

    @OneToMany(mappedBy = HrBenefitJoin.PAYING_PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrBenefitJoin> getHrBenefitJoinsWherePaying() {
        return hrBenefitJoinsWherePaying;
    }

    public void setHrBenefitJoinsWherePaying(Set<HrBenefitJoin> hrBenefitJoinsWherePaying) {
        this.hrBenefitJoinsWherePaying = hrBenefitJoinsWherePaying;
    }

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

    @OneToMany(mappedBy = Invoice.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Column(name = "lname")
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @OneToMany(mappedBy = Message.PERSONBYFROMPERSONID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Message> getMessagesForFromPersonId() {
        return messagesForFromPersonId;
    }

    public void setMessagesForFromPersonId(Set<Message> messagesForFromPersonId) {
        this.messagesForFromPersonId = messagesForFromPersonId;
    }

    @Column(name = "mname")
    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    @Column(name = "nickname")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

	@Transient
    public String getNameLFM() {
        String displayName = lname + ", " + fname;

        if (this.getMname() != null && this.getMname().trim().length() > 0) {
            displayName += " " + getMname();
        }

        return displayName;
    }

    @Transient
    public String getNameFL() {
        String displayName = fname + " " + lname;

        return displayName;
    }

    @Transient
    public String getNameFML() {
        String displayName = fname;

        if (this.getMname() != null && this.getMname().trim().length() > 0) {
            displayName += " " + getMname();
        }

        return displayName + " " + lname;
    }

    @OneToMany(mappedBy = OrgGroupAssociation.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<OrgGroupAssociation> getOrgGroupAssociations() {
        return orgGroupAssociations;
    }

    public void setOrgGroupAssociations(Set<OrgGroupAssociation> orgGroupAssociations) {
        this.orgGroupAssociations = orgGroupAssociations;
    }

    @Column(name = "org_group_type")
    public int getOrgGroupType() {
        return orgGroupType;
    }

    public void setOrgGroupType(int orgGroupType) {
        this.orgGroupType = orgGroupType;
    }

    @OneToMany(mappedBy = PersonForm.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<PersonForm> getPersonForms() {
        return personForms;
    }

    public void setPersonForms(Set<PersonForm> personForms) {
        this.personForms = personForms;
    }

    @Column(name = "person_id")
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_id")
    public Set<PersonNote> getPersonNotes() {
        return personNotes;
    }

    public void setPersonNotes(Set<PersonNote> personNotes) {
        this.personNotes = personNotes;
    }

    @Column(name = "personal_email")
    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    @OneToMany(mappedBy = Phone.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    @OneToMany(mappedBy = ProjectComment.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProjectComment> getProjectComments() {
        return projectComments;
    }

    public void setProjectComments(Set<ProjectComment> projectComments) {
        this.projectComments = projectComments;
    }

    @OneToMany(mappedBy = ProjectEmployeeJoin.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProjectEmployeeJoin> getProjectEmployeeJoin() {
        return projectEmployeeJoin;
    }

    public void setProjectEmployeeJoin(Set<ProjectEmployeeJoin> projectEmployeeJoin) {
        this.projectEmployeeJoin = projectEmployeeJoin;
    }

    @OneToMany(mappedBy = ProjectHistory.CHANGED_BY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProjectHistory> getProjectHistoriesChanged() {
        return projectHistoriesChanged;
    }

    public void setProjectHistoriesChanged(Set<ProjectHistory> projectHistoriesChanged) {
        this.projectHistoriesChanged = projectHistoriesChanged;
    }

    @OneToMany(mappedBy = ProjectView.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProjectView> getProjectViews() {
        return projectViews;
    }

    public void setProjectViews(Set<ProjectView> projectViews) {
        this.projectViews = projectViews;
    }

    /*
    public ProphetLogin getProphetLogin() {
    return prophetLogin;
    }

    public void setProphetLogin(ProphetLogin prophetLogin) {
    this.prophetLogin = prophetLogin;
    }
     */
    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_id")
    public Set<ContactQuestionDetail> getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(Set<ContactQuestionDetail> questionDetails) {
        this.questionDetails = questionDetails;
    }

    @OneToMany(mappedBy = Project.DONE_FOR_PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Project> getRequestedProjects() {
        return requestedProjects;
    }

    public void setRequestedProjects(Set<Project> requestedProjects) {
        this.requestedProjects = requestedProjects;
    }

    @OneToMany(mappedBy = StandardProject.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<StandardProject> getRequestedStandardProjects() {
        return requestedStandardProjects;
    }

    public void setRequestedStandardProjects(Set<StandardProject> requestedStandardProjects) {
        this.requestedStandardProjects = requestedStandardProjects;
    }

    @Column(name = "sex")
    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    @Column(name = "smoker")
    public char getSmoker() {
        return smoker;
    }

    public void setSmoker(char smoker) {
        this.smoker = smoker;
    }

    @Column(name = "sort_order")
    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
	
    @Column(name = "ssn")
    public String getSsn() {
		if (ssn != null  &&  ssn.length() > 11) {
			try {
				return Crypto.decryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				return ssn;
			}
		}
        return ssn;
    }

    public void setSsn(String ssn) {
		if (ssn == null  ||  ssn.length() == 0)
			this.ssn = ssn;
		else {
			try {
				this.ssn = Crypto.encryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				this.ssn = null;
				logger.error("error encrypting an SSN", e);
			}
		}
    }

    @Column(name = "student")
    public char getStudent() {
        return student;
    }

    public void setStudent(char student) {
        this.student = student;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_id")
    public Set<TimeReject> getTimeRejects() {
        return timeRejects;
    }

    public void setTimeRejects(Set<TimeReject> timeRejects) {
        this.timeRejects = timeRejects;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "person_id")
    public Set<Timesheet> getTimesheets() {
        return timesheets;
    }

    public void setTimesheets(Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    @Column(name = "job_title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @OneToMany(mappedBy = UserAttribute.PERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<UserAttribute> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(Set<UserAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }
    /*
    public Set<VendorContact> getVendorContacts() {
    return vendorContacts;
    }

    public void setVendorContacts(Set<VendorContact> vendorContacts) {
    this.vendorContacts = vendorContacts;
    }
     */

    @Column(name = "visa")
    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    @Column(name = "visa_exp_date")
    public int getVisaExpirationDate() {
        return visaExpirationDate;
    }

    public void setVisaExpirationDate(int visaExpirationDate) {
        this.visaExpirationDate = visaExpirationDate;
    }

    @Column(name = "visa_status_date")
    public int getVisaStatusDate() {
        return visaStatusDate;
    }

    public void setVisaStatusDate(int visaStatusDate) {
        this.visaStatusDate = visaStatusDate;
    }

	@Column(name = "default_project_id")
    public String getDefaultProjectId() {
        return defaultProjectId;
    }

	public void setDefaultProjectId(String projectId) {
        this.defaultProjectId = projectId;
    }

	@Column(name = "student_calendar_type")
    public char getStudentCalendarType() {
        return studentCalendarType;
    }

    public void setStudentCalendarType(char studentCalendarType) {
        this.studentCalendarType = studentCalendarType;
    }

    @Column(name="record_type")
    public char getRecordType() {
            return recordType;
    }

    public void setRecordType(char recordType) {
            this.recordType = recordType;
    }

    @Column(name="smoking_program")
    public char getSmokingProgram() {
            return smokingProgram;
    }

    public void setSmokingProgram(char smokingProgram) {
            this.smokingProgram = smokingProgram;
    }

    @Column(name="military_branch")
    public char getMilitaryBranch() {
            return militaryBranch;
    }

    public void setMilitaryBranch(char militaryBranch) {
            this.militaryBranch = militaryBranch;
    }

    @Column(name="military_discharge_explain")
    public String getMilitaryDischargeExplain() {
            return militaryDischargeExplain;
    }

    public void setMilitaryDischargeExplain(String militaryDischargeExplain) {
            this.militaryDischargeExplain = militaryDischargeExplain;
    }

    @Column(name="military_discharge_type")
    public char getMilitaryDischargeType() {
            return militaryDischargeType;
    }

    public void setMilitaryDischargeType(char militaryDischargeType) {
            this.militaryDischargeType = militaryDischargeType;
    }

    @Column(name="military_end_date")
    public int getMilitaryEndDate() {
            return militaryEndDate;
    }

    public void setMilitaryEndDate(int militaryEndDate) {
            this.militaryEndDate = militaryEndDate;
    }

    @Column(name="military_rank")
    public String getMilitaryRank() {
            return militaryRank;
    }

    public void setMilitaryRank(String militaryRank) {
            this.militaryRank = militaryRank;
    }

    @Column(name="military_start_date")
    public int getMilitaryStartDate() {
            return militaryStartDate;
    }

    public void setMilitaryStartDate(int militaryStartDate) {
            this.militaryStartDate = militaryStartDate;
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

    @Column(name="convicted_of_crime")
    public char getConvictedOfCrime() {
            return convictedOfCrime;
    }

    public void setConvictedOfCrime(char convictedOfCrime) {
            this.convictedOfCrime = convictedOfCrime;
    }

    @Column(name="convicted_of_what")
    public String getConvictedOfWhat() {
            return convictedOfWhat;
    }

    public void setConvictedOfWhat(String convictedOfWhat) {
            this.convictedOfWhat = convictedOfWhat;
    }

    @Column(name="worked_for_company_before")
    public char getWorkedForCompanyBefore() {
            return workedForCompanyBefore;
    }

    public void setWorkedForCompanyBefore(char workedForCompanyBefore) {
            this.workedForCompanyBefore = workedForCompanyBefore;
    }

    @Column(name="worked_for_company_when")
    public String getWorkedForCompanyWhen() {
            return workedForCompanyWhen;
    }

    public void setWorkedForCompanyWhen(String workedForCompanyWhen) {
            this.workedForCompanyWhen = workedForCompanyWhen;
    }

    @Column(name="actively_at_work")
    public char getActivelyAtWork() {
            return activelyAtWork;
    }

    public void setActivelyAtWork(char activelyAtWork) {
            this.activelyAtWork = activelyAtWork;
    }

    @Column(name="unable_to_perform")
    public char getUnableToPerform() {
        return unableToPerform;
    }

    public void setUnableToPerform(char unableToPerform) {
        this.unableToPerform = unableToPerform;
    }

	@Column(name="has_aids")
	public char getHasAids() {
		return hasAids;
	}

	public void setHasAids(char hasAids) {
		this.hasAids = hasAids;
	}

	@Column(name="has_cancer")
	public char getHasCancer() {
		return hasCancer;
	}

	public void setHasCancer(char hasCancer) {
		this.hasCancer = hasCancer;
	}

	@Column(name="has_heart_condition")
	public char getHasHeartCondition() {
		return hasHeartCondition;
	}

	public void setHasHeartCondition(char hasHeartCondition) {
		this.hasHeartCondition = hasHeartCondition;
	}

	@Column(name = "drug_alcohol_abuse")
	public char getDrugAlcoholAbust() {
		return drugAlcoholAbust;
	}

	public void setDrugAlcoholAbust(char drugAlcoholAbust) {
		this.drugAlcoholAbust = drugAlcoholAbust;
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
}

	
