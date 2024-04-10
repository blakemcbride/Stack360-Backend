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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.IDGenerator;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = Project.TABLE_NAME)
public class Project extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "project";
	public static final String VIEWS = "projectViews";
	public static final String PROJECT_EMPLOYEE_JOIN = "projectEmployeeJoin";
	public static final String PRIORITY_CLIENT = "priorityClient";
	public static final String ESTIMATE_HOURS = "estimateHours";
	public static final char BILLABLE_TYPE_BILLABLE = 'Y';
	public static final char BILLABLE_TYPE_NON_BILLABLE = 'N';
	public static final char BILLABLE_TYPE_UNKNOWN = 'U';
	// Fields
	private String projectId;
	public static final String PROJECTID = "projectId";
	private Person enteredByPerson;
	private Person doneForPerson;
	private String doneForPersonId;
	public static final String DONE_FOR_PERSON = "doneForPerson";
	public static final String ENTERED_BY_PERSON = "enteredByPerson";
	private OrgGroup orgGroup;
	public static final String ORGGROUP = "orgGroup";
	private ProjectStatus projectStatus;
	public static final String PROJECTSTATUS = "projectStatus";
	private ProjectCategory projectCategory;
	public static final String PROJECTCATEGORY = "projectCategory";
	public static final String SUBJECT_PERSON = "doneForPerson";
	private Employee employee;
	public static final String EMPLOYEE = "employee";
	private ProductService productService;
	public static final String PRODUCTSERVICE = "productService";
	private ProjectType projectType;
	public static final String PROJECTTYPE = "projectType";
	private String projectSubtypeId;
	private OrgGroup requestingOrgGroup;
	public static final String REQUESTING_ORG_GROUP = "requestingOrgGroup";
	private String reference;
	public static final String REFERENCE = "reference";
	private String description;
	public static final String DESCRIPTION = "description";
	private String detailDesc;
	public static final String DETAILDESC = "detailDesc";
	private double dollarCap;
	public static final String DOLLARCAP = "dollarCap";
	private float billingRate;
	public static final String BILLINGRATE = "billingRate";
	private char billable = BILLABLE_TYPE_UNKNOWN;
	public static final String BILLABLE = "billable";
	private String projectName;
	public static final String PROJECTNAME = "projectName";
	private String requesterName;
	public static final String REQUESTERNAME = "requesterName";
	private int dateReported;
	public static final String DATEREPORTED = "dateReported";
	private int billingMethod;
	public static final String BILLINGMETHOD = "billingMethod";
	private int nextBillingDate;
	public static final String NEXTBILLINGDATE = "nextBillingDate";
	private char allEmployees = 'N';
	public static final String ALLEMPLOYEES = "allEmployees";
	private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
	public static final String TIMESHEETS = "timesheets";
	private char ongoing = 'N';
	public static final String ONGOING = "ongoing";
	private Set<HrBenefitProjectJoin> hrBenefitProjectJoins = new HashSet<HrBenefitProjectJoin>(0);
	public static final String HRBENEFITPROJECTJOINS = "hrBenefitProjectJoins";
	public static final String CURRENT_ROUTE_STOP = "routeStop";
//	public static final String CURRENT_PERSON = "currentPerson";
	public static final String TIME_REPORTED = "timeReported";
	private RouteStop routeStop;
	private Set<ProjectComment> projectComments = new HashSet<ProjectComment>(0);
	private Set<ProjectHistory> projectHistories = new HashSet<ProjectHistory>(0);
	private Set<ProjectForm> projectForms = new HashSet<ProjectForm>(0);
	//private Person currentPerson;
	private int timeReported = DateUtils.nowTime();//-1; set default time
	private int timeCompleted = -1;
	private int dateCompleted;
	private Person approvingPerson;
	private Date approvingTimestamp;
	private String approvingPersonTxt;
	private short priorityCompany = 10000;
	private short priorityDepartment = 10000;
	private short priorityClient = 10000;
	final static public String PRIORITY_ORGGROUP = "priorityDepartment";
	final static public String PRIORITY_COMPANY = "priorityCompany";
	private float estimateHours;
	private int datePromised;
	private int estimatedTimeSpan;
	private int dateOfEstimate;
	private Set<ProjectView> projectViews = new HashSet<ProjectView>(0);
	private Set<ProjectEmployeeJoin> projectEmployeeJoins = new HashSet<ProjectEmployeeJoin>(0);
	private Set<PersonCR> personChangeRequest = new HashSet<PersonCR>(0);
	public static final String CHANGE_REQUEST = "personChangeRequest";
	private Set<HrBenefitConfig> configs = new HashSet<HrBenefitConfig>(0);
	private String companyId;
	public static final String COMPANY_ID = "companyId";
	private String purchaseOrder;
	private RateType rateType;
	private String projectCode;
	private String projectState;
	private int estimatedFirstDate = 0;
	public static final String ESTIMATED_FIRST_DATE = "estimatedFirstDate";
	private int estimatedLastDate = 0;
	public static final String ESTIMATED_LAST_DATE = "estimatedLastDate";
	private Address address;
    public static final String ADDRESS = "address";
    private String storeNumber;
    private String locationDescription;
	private char billingType = 'H';
	private double fixedPriceAmount = 0;
	private Date lastReportDate;
	private short projectDays = 0;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<PersonCR> getPersonChangeRequest() {
		return personChangeRequest;
	}

	public void setPersonChangeRequest(Set<PersonCR> personChangeRequest) {
		this.personChangeRequest = personChangeRequest;
	}

	@Column(name = "date_of_estimate")
	public int getDateOfEstimate() {
		return dateOfEstimate;
	}

	public void setDateOfEstimate(int dateOfEstimate) {
		this.dateOfEstimate = dateOfEstimate;
	}

	@Column(name = "date_promised")
	public int getDatePromised() {
		return datePromised;
	}

	public void setDatePromised(int datePromised) {
		this.datePromised = datePromised;
	}

	@Column(name = "estimate_hours")
	public float getEstimateHours() {
		return estimateHours;
	}

	public void setEstimateHours(float estimateHours) {
		this.estimateHours = estimateHours;
	}

	@Column(name = "estimated_time_span")
	public int getEstimatedTimeSpan() {
		return estimatedTimeSpan;
	}

	public void setEstimatedTimeSpan(int estimatedTimeSpan) {
		this.estimatedTimeSpan = estimatedTimeSpan;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approving_person")
	public Person getApprovingPerson() {
		return approvingPerson;
	}

	public void setApprovingPerson(Person approvingPerson) {
		this.approvingPerson = approvingPerson;
	}

	@Column(name = "approving_person_txt")
	public String getApprovingPersonTxt() {
		return approvingPersonTxt;
	}

	public void setApprovingPersonTxt(String approvingPersonTxt) {
		this.approvingPersonTxt = approvingPersonTxt;
	}

	@Column(name = "approving_timestamp")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getApprovingTimestamp() {
		return approvingTimestamp;
	}

	public void setApprovingTimestamp(Date approvingTimestamp) {
		this.approvingTimestamp = approvingTimestamp;
	}

	@Column(name = "priority_client")
	public short getPriorityClient() {
		return priorityClient;
	}

	public void setPriorityClient(short priorityClient) {
		this.priorityClient = priorityClient;
	}

	@Column(name = "priority_company")
	public short getPriorityCompany() {
		return priorityCompany;
	}

	public void setPriorityCompany(short priorityCompany) {
		this.priorityCompany = priorityCompany;
	}

	@Column(name = "priority_department")
	public short getPriorityDepartment() {
		return priorityDepartment;
	}

	public void setPriorityDepartment(short priorityDepartment) {
		this.priorityDepartment = priorityDepartment;
	}

	@Transient
	public String getProjectTypeId() {
		if (projectType == null)
			return "";
		return projectType.getProjectTypeId();
	}

	public void setProjectTypeId(String projectTypeId) {
		projectType = ArahantSession.getHSU().get(ProjectType.class, projectTypeId);
	}

	@Column(name = "project_subtype_id")
	public String getProjectSubtypeId() {
		return projectSubtypeId;
	}

	public void setProjectSubtypeId(String projectSubtypeId) {
		this.projectSubtypeId = projectSubtypeId;
	}

	/**
	 * This is the time the project was created.
	 */
	@Column(name = "time_reported")
	public int getTimeReported() {
		return timeReported;
	}

	/**
	 * @param timeReported The timeReported to set.
	 */
	public void setTimeReported(final int timeReported) {
		this.timeReported = timeReported;
	}

	/**
	 * @return Returns the currentPerson.
	 *
	 * public Person getCurrentPerson() { return currentPerson; }
	 *
	 * public String getCurrentPersonId() { if (currentPerson==null) return "";
	 * return currentPerson.getPersonId(); } public void
	 * setCurrentPersonId(String id) {
	 * setCurrentPerson(ArahantSession.getHSU().get(Person.class,id)); }
    /
	 **
	 * param currentPerson The currentPerson to set.
	 *
	 * public void setCurrentPerson(final Person currentPerson) { String
	 * oldId=""; String newId=""; if (currentPerson!=null)
	 * newId=currentPerson.getPersonId(); if (this.currentPerson!=null)
	 * oldId=this.currentPerson.getPersonId();
	 * firePropertyChange("currentPersonId", oldId, newId); this.currentPerson =
	 * currentPerson; }
	 *
	 * /
	 **
	 * @return Returns the routeStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_route_stop")
	public RouteStop getRouteStop() {
		return routeStop;
	}

	/**
	 * @param routeStop The routeStop to set.
	 */
	public void setRouteStop(final RouteStop routeStop) {
		this.routeStop = routeStop;
	}

	/**
	 * default constructor
	 */
	public Project() {
		/*
		 * String bill=new BProperty("DefaultBillableState").getValue(); if
		 * (bill==null || "".equals(bill)) billable='U'; else
		 * billable=bill.charAt(0);
         *
		 */
	}

	// Property accessors
	@Id
	@Column(name = "project_id")
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final String projectId) {
		firePropertyChange("projectId", this.projectId, projectId);
		setOwningCompanyId(ArahantSession.getHSU().getCurrentCompany().getOrgGroupId());
		this.projectId = projectId;
	}

	/**
	 * This is the person who added the project.
	 *
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_sponsor_id")
	public Person getEnteredByPerson() {
		return this.enteredByPerson;
	}

	public void setEnteredByPerson(final Person person) {
		this.enteredByPerson = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bill_at_org_group")
	public OrgGroup getOrgGroup() {
		return this.orgGroup;
	}

	public void setOrgGroup(final OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_status_id")
	public ProjectStatus getProjectStatus() {
		return this.projectStatus;
	}

	public void setProjectStatus(final ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_category_id")
	public ProjectCategory getProjectCategory() {
		return this.projectCategory;
	}

	public void setProjectCategory(final ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managing_employee")
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Employee employee) {
		this.employee = employee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public ProductService getProductService() {
		return this.productService;
	}

	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_type_id")
	public ProjectType getProjectType() {
		return this.projectType;
	}

	public void setProjectType(final ProjectType projectType) {
		String oldId = "";
		String newId = "";
		if (projectType != null)
			newId = projectType.getProjectTypeId();
		if (this.projectType != null)
			oldId = this.projectType.getProjectTypeId();
		firePropertyChange("projectTypeId", oldId, newId);
		this.projectType = projectType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requesting_org_group")
	public OrgGroup getRequestingOrgGroup() {
		return this.requestingOrgGroup;
	}

	public void setRequestingOrgGroup(final OrgGroup requestingOrgGroup) {
		this.requestingOrgGroup = requestingOrgGroup;
	}

	@Column(name = "reference")
	public String getReference() {
		return this.reference;
	}

	public void setReference(final String reference) {
		this.reference = reference;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Column(name = "detail_desc")
	public String getDetailDesc() {
		return this.detailDesc;
	}

	public void setDetailDesc(final String detailDesc) {
		this.detailDesc = detailDesc;
	}

	@Column(name = "dollar_cap")
	public double getDollarCap() {
		return this.dollarCap;
	}

	public void setDollarCap(final double dollarCap) {
		this.dollarCap = dollarCap;
	}

	@Column(name = "billing_rate")
	public float getBillingRate() {
		return this.billingRate;
	}

	public void setBillingRate(final float billingRate) {
		this.billingRate = billingRate;
	}

	@Column(name = "billable")
	public char getBillable() {
		return this.billable;
	}

	public void setBillable(final char billable) {
		this.billable = billable;
	}

	@Column(name = "project_name")
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "requester_name")
	public String getRequesterName() {
		return this.requesterName;
	}

	public void setRequesterName(final String requesterName) {
		this.requesterName = requesterName;
	}

	/**
	 * This is the date the project was added.
	 *
	 * @return
	 */
	@Column(name = "date_reported")
	public int getDateReported() {
		return this.dateReported;
	}

	public void setDateReported(final int dateReported) {
		this.dateReported = dateReported;
	}

	@Column(name = "billing_method")
	public int getBillingMethod() {
		return this.billingMethod;
	}

	public void setBillingMethod(final int billingMethod) {
		this.billingMethod = billingMethod;
	}

	@Column(name = "next_billing_date")
	public int getNextBillingDate() {
		return this.nextBillingDate;
	}

	public void setNextBillingDate(final int nextBillingDate) {
		this.nextBillingDate = nextBillingDate;
	}

	@Column(name = "all_employees")
	public char getAllEmployees() {
		return this.allEmployees;
	}

	public void setAllEmployees(final char allEmployees) {
		this.allEmployees = allEmployees;
	}

	@Column(name = "ongoing")
	public char getOngoing() {
		return this.ongoing;
	}

	public void setOngoing(final char ongoing) {
		this.ongoing = ongoing;
	}

    @Column(name = "project_state")
    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    @OneToMany(mappedBy = Timesheet.PROJECTSHIFT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Timesheet> getTimesheets() {
		return this.timesheets;
	}

	public void setTimesheets(final Set<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	@OneToMany(mappedBy = HrBenefitProjectJoin.PROJECT, fetch = FetchType.LAZY)
	//  @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitProjectJoin> getHrBenefitProjectJoins() {
		return this.hrBenefitProjectJoins;
	}

	public void setHrBenefitProjectJoins(final Set<HrBenefitProjectJoin> hrBenefitProjectJoins) {
		this.hrBenefitProjectJoins = hrBenefitProjectJoins;
	}

	@Override
	public String keyColumn() {
		return "project_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setProjectId(IDGenerator.generate(this));
		return projectId;
	}

	@OneToMany(mappedBy = ProjectComment.PROJECTSHIFT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectComment> getProjectComments() {
		return projectComments;
	}

	/**
	 * @param projectComments The projectComments to set.
	 */
	public void setProjectComments(final Set<ProjectComment> projectComments) {
		this.projectComments = projectComments;
	}

	/**
	 * @return Returns the projectHistories.
	 */
	@OneToMany(mappedBy = ProjectHistory.PROJECT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectHistory> getProjectHistories() {
		return projectHistories;
	}

	/**
	 * @param projectHistories The projectHistories to set.
	 */
	public void setProjectHistories(final Set<ProjectHistory> projectHistories) {
		this.projectHistories = projectHistories;
	}

	/**
	 * @return Returns the subjectPerson.
	 */
	@Transient
	public Person getSubjectPerson() {
		return doneForPerson;
	}

	/**
	 * @param subjectPerson The subjectPerson to set.
	 */
	public void setSubjectPerson(final Person subjectPerson) {
		this.doneForPerson = subjectPerson;
	}

	/**
	 * @return Returns the dateCompleted.
	 */
	@Column(name = "completed_date")
	public int getDateCompleted() {
		return dateCompleted;
	}

	/**
	 * @param dateCompleted The dateCompleted to set.
	 */
	public void setDateCompleted(final int dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	/**
	 * @return Returns the timeCompleted.
	 */
	@Column(name = "completed_time")
	public int getTimeCompleted() {
		return timeCompleted;
	}

	/**
	 * @param timeCompleted The timeCompleted to set.
	 */
	public void setTimeCompleted(final int timeCompleted) {
		this.timeCompleted = timeCompleted;
	}

	/**
	 * This is the Arahant company that the project is associated with (i.e. owns it),
	 * the company_detail record it is associated with
	 * 
	 * @return 
	 */
	@Column(name = "company_id")
	public String getOwningCompanyId() {
		return companyId;
	}

	public void setOwningCompanyId(String company_id) {
		this.companyId = company_id;
	}

	@Column(name = "purchase_order")
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "rate_type_id")
	public RateType getRateType() {
		return rateType;
	}

	public void setRateType(RateType rateType) {
		this.rateType = rateType;
	}

    @Column(name = "project_code")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
	 * @return Returns the doneForPerson.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_person")
	public Person getDoneForPerson() {
		return doneForPerson;
	}

	@Column(name = "subject_person", insertable = false, updatable = false)
	public String getDoneForPersonId() {
		String ret = doneForPersonId;
		if (ret == null)
			return "";
		return ret;
	}

	public void setDoneForPersonId(String id) {
		doneForPersonId = id;
		setDoneForPerson(ArahantSession.getHSU().get(Person.class, id));
	}

	/**
	 * @param doneForPerson The doneForPerson to set.
	 */
	public void setDoneForPerson(final Person doneForPerson) {
		String oldId = "";
		String newId = "";
		if (doneForPerson != null)
			newId = doneForPerson.getPersonId();
		if (this.doneForPerson != null)
			oldId = this.doneForPerson.getPersonId();
		firePropertyChange("doneForPersonId", oldId, newId);
		this.doneForPerson = doneForPerson;
		this.doneForPersonId = doneForPerson == null ? "" : doneForPerson.getPersonId();
	}

	@OneToMany(mappedBy = ProjectView.PROJECT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectView> getProjectViews() {
		return projectViews;
	}

	public void setProjectViews(Set<ProjectView> projectViews) {
		this.projectViews = projectViews;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hr_benefit_project_join",
	joinColumns = {
		@JoinColumn(name = "project_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "benefit_config_id")})
	public Set<HrBenefitConfig> getBenefitConfigs() {
		return configs;
	}

	public void setBenefitConfigs(Set<HrBenefitConfig> configs) {
		this.configs = configs;
	}

	@Column(name="estimated_first_date")
    public int getEstimatedFirstDate() {
        return estimatedFirstDate;
    }

    public void setEstimatedFirstDate(int estimatedFirstDate) {
        this.estimatedFirstDate = estimatedFirstDate;
    }

    @Column(name="estimated_last_date")
    public int getEstimatedLastDate() {
        return estimatedLastDate;
    }

    public void setEstimatedLastDate(int estimatedLastDate) {
        this.estimatedLastDate = estimatedLastDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

	@Column(name="store_number")
	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	@Column(name="location_description")
	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	@Column(name = "billing_type")
	public char getBillingType() {
		return billingType;
	}

	public void setBillingType(char billingType) {
		this.billingType = billingType;
	}

	@Column(name = "fixed_price_amount")
	public double getFixedPriceAmount() {
		return fixedPriceAmount;
	}

	public void setFixedPriceAmount(double fixedPriceAmount) {
		this.fixedPriceAmount = fixedPriceAmount;
	}

	@Column(name = "last_report_date")
	public Date getLastReportDate() {
		return lastReportDate;
	}

	public void setLastReportDate(Date lastReportDate) {
		this.lastReportDate = lastReportDate;
	}

	@Column(name = "project_days")
	public short getProjectDays() {
		return projectDays;
	}

	public void setProjectDays(short projectDays) {
		this.projectDays = projectDays;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Project other = (Project) obj;
		return Objects.equals(this.projectId, other.getProjectId()) || (this.projectId != null && this.projectId.equals(other.getProjectId()));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.projectId != null ? this.projectId.hashCode() : 0);
		return hash;
	}

	@Override
	public String notifyId() {
		return projectId;
	}

	@Override
	public String notifyClassName() {
		return "Project";
	}
}
