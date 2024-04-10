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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.Where;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Person implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "employee";
    public static final String PROSPECTS = "prospectCompanies";
    public static final String MARITAL_STATUS = "maritalStatus";
    public static final String INITIAL_STATUS_DATE = "initialStatusDate";
	public static final String AUTO_OVERTIME_LOGOUT="autoOvertimeLogout";
	public static final String AUTO_LOG_TIME="autoLogTime";

	public static final String HR_ADMIN ="hrAdmin";

    public static final char TYPE_ADDITIONAL_AMOUNT = 'A';
    // Fields
    private HrEeoRace hrEeoRace;
    public static final String HREEORACE = "hrEeoRace";
    private HrEeoCategory hrEeoCategory;
    public static final String HREEOCATEGORY = "hrEeoCategory";
    private int timesheetFinalDate;
    public static final String TIMESHEETFINALDATE = "timesheetFinalDate";
    private String extRef;
    public static final String EXTREF = "extRef";
    private char overtimePay = 'N';
    public static final String OVERTIMEPAY = "overtimePay";
    private Set<Project> projects = new HashSet<Project>(0);
    public static final String PROJECTS = "projects";
    private Set<HrEmployeeEvent> hrEmployeeEventsForEmployeeId = new HashSet<HrEmployeeEvent>(0);
    public static final String HREMPLOYEEEVENTSFOREMPLOYEEID = "hrEmployeeEventsForEmployeeId";
    private Set<HrTrainingDetail> hrTrainingDetails = new HashSet<HrTrainingDetail>(0);
    public static final String HRTRAININGDETAILS = "hrTrainingDetails";
    private Set<HrEmployeeEval> hrEmployeeEvalsForEmployeeId = new HashSet<HrEmployeeEval>(0);
    public static final String HREMPLOYEEEVALSFOREMPLOYEEID = "hrEmployeeEvalsForEmployeeId";
    private Set<HrEmployeeEval> hrEmployeeEvalsForSupervisorId = new HashSet<HrEmployeeEval>(0);
    public static final String HREMPLOYEEEVALSFORSUPERVISORID = "hrEmployeeEvalsForSupervisorId";
    private Set<HrChecklistDetail> hrChecklistDetailsForEmployeeId = new HashSet<HrChecklistDetail>(0);
    public static final String HRCHECKLISTDETAILSFOREMPLOYEEID = "hrChecklistDetailsForEmployeeId";
    private Set<HrEmployeeEvent> hrEmployeeEventsForSupervisorId = new HashSet<HrEmployeeEvent>(0);
    public static final String HREMPLOYEEEVENTSFORSUPERVISORID = "hrEmployeeEventsForSupervisorId";
    private Set<StandardProject> standardProjects = new HashSet<StandardProject>(0);
//	public static final String STANDARDPROJECTS = "standardProjects";
    private Set<HrChecklistDetail> hrChecklistDetailsForSupervisorId = new HashSet<HrChecklistDetail>(0);
    public static final String HRCHECKLISTDETAILSFORSUPERVISORID = "hrChecklistDetailsForSupervisorId";
    private Set<HrWage> hrWages = new HashSet<HrWage>(0);
    public static final String HRWAGES = "hrWages";
    private Set<HrAccrual> hrAccruals = new HashSet<HrAccrual>(0);
    public static final String HRACCRUALS = "hrAccruals";
    private Set<HrEmplStatusHistory> hrEmplStatusHistories = new HashSet<HrEmplStatusHistory>(0);
    public static final String HREMPLSTATUSHISTORIES = "hrEmplStatusHistories";
    private Set<HrEmplDependent> hrEmplDependents = new HashSet<HrEmplDependent>(0);
    public static final String HREMPLDEPENDENTS = "hrEmplDependents";
    private short payPeriodsPerYear;
    private Set<ProspectCompany> prospectCompanies = new HashSet<ProspectCompany>(0);
    private float expectedHoursPerPeriod = 40;
    private char maritalStatus = ' ';
    private String localTaxCode;
    private char earnedIncomeCreditStatus = ' ';
    private char addFederalIncomeTaxType = 'A';
    private float addFederalIncomeTax;
    private char addStateIncomeTaxType = 'A';
    private float addStateIncomeTax;
    private char addLocalIncomeTaxType = 'A';
    private float addLocalIncomeTax;
    private char addStateDisabilityTaxType = 'A';
    private float addStateDisabilityTax;
    private short numberFederalExemptions;
    private short numberStateExemptions;
    private BankAccount payrollBankAccount;
    private float federalExtraWithhold;
    private float stateExtraWithhold;
    private String taxState;
    private String unemploymentState;
    //private Integer initialStatusDate;
	public static final String BENEFIT_CLASS = "benefitClass";
    private BenefitClass benefitClass;
    private char autoTimeLog = 'N';  //used by clock in clock out
    private char autoOvertimeLogout = 'N';
    private float lengthOfWorkDay;
    private float lengthOfBreaks;
	private String workersCompCode;
	private char autoLogTime = 'N';  //used by salaried employees to auto enter time
	private char w4status='U';
	private char w4exempt='N';
	private char w4nameDiffers='N';
    public static final String MEDICARE = "medicare";
	private char medicare = 'N';
	private char hrAdmin = 'N';
	private String adpId;

	public static final String BENEFIT_WIZARD_STATUS = "benefitWizardStatus";
	public static final String BENEFIT_WIZARD_DATE = "benefitWizardDate";
	public static final Character BENEFIT_WIZARD_STATUS_NO_CHANGE = 'C';
	public static final Character BENEFIT_WIZARD_STATUS_UNFINALIZED = 'U';
	public static final Character BENEFIT_WIZARD_STATUS_FINALIZED = 'F';
	public static final Character BENEFIT_WIZARD_STATUS_PROCESSED = 'P';
	private char benefitWizardStatus = 'N';
	private int benefitWizardDate = 0;

	private HrEmployeeStatus status;
	private String statusId;
	private int statusEffectiveDate = 0;
	private short hours_per_week;
	private char employmentType = 'E';  //  Employee or Contractor
	
    public Employee() {
    }

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "status_id")
	public HrEmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(HrEmployeeStatus status) {
		this.status = status;
	}

	@Column(name = "status_effective_date")
	public int getStatusEffectiveDate() {
		return statusEffectiveDate;
	}

	public void setStatusEffectiveDate(int statusEffectiveDate) {
		this.statusEffectiveDate = statusEffectiveDate;
	}

	@Column(name = "status_id", updatable=false, insertable=false)
	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}



	@Column(name = "benefit_wizard_date")
	public int getBenefitWizardDate() {
		return benefitWizardDate;
	}

	public void setBenefitWizardDate(int benefitWizardDate) {
		this.benefitWizardDate = benefitWizardDate;
	}

	@Column(name = "benefit_wizard_status")
	public char getBenefitWizardStatus() {
		return benefitWizardStatus;
	}

	public void setBenefitWizardStatus(char benefitWizardStatus) {
		this.benefitWizardStatus = benefitWizardStatus;
	}


	@Column(name = "hr_admin")
	public char getHrAdmin() {
		return hrAdmin;
	}

	public void setHrAdmin(char hrAdmin) {
		this.hrAdmin = hrAdmin;
	}

    @Column(name = "auto_overtime_logout")
    public char getAutoOvertimeLogout() {
        return autoOvertimeLogout;
    }

    public void setAutoOvertimeLogout(char autoOvertimeLogout) {
        this.autoOvertimeLogout = autoOvertimeLogout;
    }

    @Column(name = "auto_time_log")
    public char getClockInTimeLog() {
        return autoTimeLog;
    }

    public void setClockInTimeLog(char autoTimeLog) {
        this.autoTimeLog = autoTimeLog;
    }

	@Column(name = "auto_log_time")
	public char getAutoLogTime() {
		return autoLogTime;
	}

	public void setAutoLogTime(char autoLogTime) {
		this.autoLogTime = autoLogTime;
	}

    @Column(name = "length_of_work_day")
    public float getLengthOfWorkDay() {
        return lengthOfWorkDay;
    }

    public void setLengthOfWorkDay(float lengthOfWorkDay) {
        this.lengthOfWorkDay = lengthOfWorkDay;
    }

    @Column(name = "length_of_breaks")
    public float getLengthOfBreaks() {
        return lengthOfBreaks;
    }

    public void setLengthOfBreaks(float lengthOfBreaks) {
        this.lengthOfBreaks = lengthOfBreaks;
    }

    // Constructors
    /**
     * @return Returns the payPeriodsPerYear.
     */
    @Column(name = "pay_periods_per_year")
    public short getPayPeriodsPerYear() {
        return payPeriodsPerYear;
    }

    /**
     * @param payPeriodsPerYear The payPeriodsPerYear to set.
     */
    public void setPayPeriodsPerYear(final short payPeriodsPerYear) {
        firePropertyChange("payPeriodsPerYear", this.payPeriodsPerYear, payPeriodsPerYear);
        this.payPeriodsPerYear = payPeriodsPerYear;
    }

    /**
     * @return Returns the extRef.
     */
    @Column(name = "ext_ref")
    public String getExtRef() {
        return extRef;
    }

    /**
     * @param extRef The extRef to set.
     */
    public void setExtRef(String extRef) {
        firePropertyChange("extRef", this.extRef, extRef);
        this.extRef = extRef;
    }

    /**
     * @return Returns the hrAccruals.
     */
    @OneToMany(mappedBy = HrAccrual.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrAccrual> getHrAccruals() {
        return hrAccruals;
    }

    /**
     * @param hrAccruals The hrAccruals to set.
     */
    public void setHrAccruals(Set<HrAccrual> hrAccruals) {
        this.hrAccruals = hrAccruals;
    }

    /**
     * @return Returns the hrChecklistDetailsForEmployeeId.
     */
    @OneToMany(mappedBy = HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrChecklistDetail> getHrChecklistDetailsForEmployeeId() {
        return hrChecklistDetailsForEmployeeId;
    }

    /**
     * @param hrChecklistDetailsForEmployeeId The hrChecklistDetailsForEmployeeId to set.
     */
    public void setHrChecklistDetailsForEmployeeId(
            Set<HrChecklistDetail> hrChecklistDetailsForEmployeeId) {
        this.hrChecklistDetailsForEmployeeId = hrChecklistDetailsForEmployeeId;
    }

    /**
     * @return Returns the hrChecklistDetailsForSupervisorId.
     */
    @OneToMany(mappedBy = HrChecklistDetail.EMPLOYEEBYSUPERVISORID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrChecklistDetail> getHrChecklistDetailsForSupervisorId() {
        return hrChecklistDetailsForSupervisorId;
    }

    /**
     * @param hrChecklistDetailsForSupervisorId The hrChecklistDetailsForSupervisorId to set.
     */
    public void setHrChecklistDetailsForSupervisorId(
            Set<HrChecklistDetail> hrChecklistDetailsForSupervisorId) {

        this.hrChecklistDetailsForSupervisorId = hrChecklistDetailsForSupervisorId;
    }

    /**
     * @return Returns the hrEeoCategory.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "eeo_category_id")
    public HrEeoCategory getHrEeoCategory() {
        return hrEeoCategory;
    }

    /**
     * @param hrEeoCategory The hrEeoCategory to set.
     */
    public void setHrEeoCategory(HrEeoCategory hrEeoCategory) {
        this.hrEeoCategory = hrEeoCategory;
    }

    /**
     * @return Returns the hrEeoRace.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "eeo_race_id")
    public HrEeoRace getHrEeoRace() {
        return hrEeoRace;
    }

    /**
     * @param hrEeoRace The hrEeoRace to set.
     */
    public void setHrEeoRace(HrEeoRace hrEeoRace) {
        this.hrEeoRace = hrEeoRace;
    }

    /**
     * @return Returns the hrEmplDependents.  Only Real records are included.
     */
    @OneToMany(mappedBy = HrEmplDependent.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	@Where(clause="record_type='R'")
    public Set<HrEmplDependent> getHrEmplDependents() {
        return hrEmplDependents;
    }

    /**
     * @param hrEmplDependents The hrEmplDependents to set.
     */
    public void setHrEmplDependents(Set<HrEmplDependent> hrEmplDependents) {
        this.hrEmplDependents = hrEmplDependents;
    }

    /**
     * @return Returns the hrEmployeeEvalsForEmployeeId.
     */
    @OneToMany(mappedBy = HrEmployeeEval.EMPLOYEEBYEMPLOYEEID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmployeeEval> getHrEmployeeEvalsForEmployeeId() {
        return hrEmployeeEvalsForEmployeeId;
    }

    /**
     * @param hrEmployeeEvalsForEmployeeId The hrEmployeeEvalsForEmployeeId to set.
     */
    public void setHrEmployeeEvalsForEmployeeId(
            Set<HrEmployeeEval> hrEmployeeEvalsForEmployeeId) {
        this.hrEmployeeEvalsForEmployeeId = hrEmployeeEvalsForEmployeeId;
    }

    /**
     * @return Returns the hrEmployeeEvalsForSupervisorId.
     */
    @OneToMany(mappedBy = HrEmployeeEval.EMPLOYEEBYSUPERVISORID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmployeeEval> getHrEmployeeEvalsForSupervisorId() {
        return hrEmployeeEvalsForSupervisorId;
    }

    /**
     * @param hrEmployeeEvalsForSupervisorId The hrEmployeeEvalsForSupervisorId to set.
     */
    public void setHrEmployeeEvalsForSupervisorId(Set<HrEmployeeEval> hrEmployeeEvalsForSupervisorId) {
        this.hrEmployeeEvalsForSupervisorId = hrEmployeeEvalsForSupervisorId;
    }

    /**
     * @return Returns the hrEmployeeEventsForEmployeeId.
     */
    @OneToMany(mappedBy = HrEmployeeEvent.EMPLOYEEID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmployeeEvent> getHrEmployeeEventsForEmployeeId() {
        return hrEmployeeEventsForEmployeeId;
    }

    /**
     * @param hrEmployeeEventsForEmployeeId The hrEmployeeEventsForEmployeeId to set.
     */
    public void setHrEmployeeEventsForEmployeeId(
            Set<HrEmployeeEvent> hrEmployeeEventsForEmployeeId) {
        this.hrEmployeeEventsForEmployeeId = hrEmployeeEventsForEmployeeId;
    }

    /**
     * @return Returns the hrEmployeeEventsForSupervisorId.
     */
    @OneToMany(mappedBy = HrEmployeeEvent.SUPERVISORID, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmployeeEvent> getHrEmployeeEventsForSupervisorId() {
        return hrEmployeeEventsForSupervisorId;
    }

    /**
     * @param hrEmployeeEventsForSupervisorId The hrEmployeeEventsForSupervisorId to set.
     */
    public void setHrEmployeeEventsForSupervisorId(
            Set<HrEmployeeEvent> hrEmployeeEventsForSupervisorId) {
        this.hrEmployeeEventsForSupervisorId = hrEmployeeEventsForSupervisorId;
    }

    /**
     * @return Returns the hrEmplStatusHistories.
     */
    @OneToMany(mappedBy = HrEmplStatusHistory.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	//@OrderBy(HrEmplStatusHistory.EFFECTIVEDATE+" DESC")
    public Set<HrEmplStatusHistory> getHrEmplStatusHistories() {
        return hrEmplStatusHistories;
    }

    /**
     * @param hrEmplStatusHistories The hrEmplStatusHistories to set.
     */
    public void setHrEmplStatusHistories(
            Set<HrEmplStatusHistory> hrEmplStatusHistories) {
        this.hrEmplStatusHistories = hrEmplStatusHistories;
    }

    /**
     * @return Returns the hrTrainingDetails.
     */
    @OneToMany(mappedBy = HrTrainingDetail.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrTrainingDetail> getHrTrainingDetails() {
        return hrTrainingDetails;
    }

    /**
     * @param hrTrainingDetails The hrTrainingDetails to set.
     */
    public void setHrTrainingDetails(Set<HrTrainingDetail> hrTrainingDetails) {
        this.hrTrainingDetails = hrTrainingDetails;
    }

    /**
     * @return Returns the hrWages.
     */
    @OneToMany(mappedBy = HrWage.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrWage> getHrWages() {
        return hrWages;
    }

    /**
     * @param hrWages The hrWages to set.
     */
    public void setHrWages(Set<HrWage> hrWages) {
        this.hrWages = hrWages;
    }

    /**
     * @return Returns the overtimePay.
     */
    @Column(name = "overtime_pay")
    public char getOvertimePay() {
        return overtimePay;
    }

    /**
     * @param overtimePay The overtimePay to set.
     */
    public void setOvertimePay(char overtimePay) {
        firePropertyChange("overtimePay", this.overtimePay, overtimePay);
        this.overtimePay = overtimePay;
    }

    /**
     * @return Returns the projects.
     */
    @OneToMany(mappedBy = Project.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Project> getProjects() {
        return projects;
    }

    /**
     * @param projects The projects to set.
     */
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    /**
     * @return Returns the standardProjects.
     */
    @OneToMany(mappedBy = StandardProject.EMPLOYEE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<StandardProject> getStandardProjects() {
        return standardProjects;
    }

    /**
     * @param standardProjects The standardProjects to set.
     */
    public void setStandardProjects(Set<StandardProject> standardProjects) {
        this.standardProjects = standardProjects;
    }

    /**
     * @return Returns the timesheetFinalDate.
     */
    @Column(name = "timesheet_final_date")
    public int getTimesheetFinalDate() {
        return timesheetFinalDate;
    }

    /**
     * @param timesheetFinalDate The timesheetFinalDate to set.
     */
    public void setTimesheetFinalDate(int timesheetFinalDate) {
        this.timesheetFinalDate = timesheetFinalDate;
    }

    /**
     * @return
     */
    @Transient
    public String getNameLF() {

        return getLname() + ", " + getFname();
    }

    @OneToMany(mappedBy = ProspectCompany.SALESPERSON, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProspectCompany> getProspectCompanies() {
        return prospectCompanies;
    }

    public void setProspectCompanies(Set<ProspectCompany> prospectCompanies) {
        this.prospectCompanies = prospectCompanies;
    }

    @Column(name = "add_federal_income_tax")
    public float getAddFederalIncomeTax() {
        return addFederalIncomeTax;
    }

    public void setAddFederalIncomeTax(float addFederalIncomeTax) {
        this.addFederalIncomeTax = addFederalIncomeTax;
    }

    @Column(name = "add_federal_income_tax_type")
    public char getAddFederalIncomeTaxType() {
        return addFederalIncomeTaxType;
    }

    public void setAddFederalIncomeTaxType(char addFederalIncomeTaxType) {
        this.addFederalIncomeTaxType = addFederalIncomeTaxType;
    }

    @Column(name = "add_local_income_tax")
    public float getAddLocalIncomeTax() {
        return addLocalIncomeTax;
    }

    public void setAddLocalIncomeTax(float addLocalIncomeTax) {
        this.addLocalIncomeTax = addLocalIncomeTax;
    }

    @Column(name = "add_local_income_tax_type")
    public char getAddLocalIncomeTaxType() {
        return addLocalIncomeTaxType;
    }

    public void setAddLocalIncomeTaxType(char addLocalIncomeTaxType) {
        this.addLocalIncomeTaxType = addLocalIncomeTaxType;
    }

    @Column(name = "add_state_disability_tax")
    public float getAddStateDisabilityTax() {
        return addStateDisabilityTax;
    }

    public void setAddStateDisabilityTax(float addStateDisabilityTax) {
        this.addStateDisabilityTax = addStateDisabilityTax;
    }

    @Column(name = "add_state_disability_tax_type")
    public char getAddStateDisabilityTaxType() {
        return addStateDisabilityTaxType;
    }

    public void setAddStateDisabilityTaxType(char addStateDisabilityTaxType) {
        this.addStateDisabilityTaxType = addStateDisabilityTaxType;
    }

    @Column(name = "add_state_income_tax")
    public float getAddStateIncomeTax() {
        return addStateIncomeTax;
    }

    public void setAddStateIncomeTax(float addStateIncomeTax) {
        this.addStateIncomeTax = addStateIncomeTax;
    }

    @Column(name = "add_state_income_tax_type")
    public char getAddStateIncomeTaxType() {
        return addStateIncomeTaxType;
    }

    public void setAddStateIncomeTaxType(char addStateIncomeTaxType) {
        this.addStateIncomeTaxType = addStateIncomeTaxType;
    }

    @Column(name = "earned_income_credit_status")
    public char getEarnedIncomeCreditStatus() {
        return earnedIncomeCreditStatus;
    }

    public void setEarnedIncomeCreditStatus(char earnedIncomeCreditStatus) {
        this.earnedIncomeCreditStatus = earnedIncomeCreditStatus;
    }

    @Column(name = "expected_hours_per_period")
    public float getExpectedHoursPerPeriod() {
        return expectedHoursPerPeriod;
    }

    public void setExpectedHoursPerPeriod(float expectedHoursPerPeriod) {
        firePropertyChange("expectedHoursPerPeriod", this.expectedHoursPerPeriod, expectedHoursPerPeriod);
        this.expectedHoursPerPeriod = expectedHoursPerPeriod;
    }

    @Column(name = "federal_extra_withhold")
    public float getFederalExtraWithhold() {
        return federalExtraWithhold;
    }

    public void setFederalExtraWithhold(float federalExtraWithhold) {
        this.federalExtraWithhold = federalExtraWithhold;
    }

    @Column(name = "local_tax_code")
    public String getLocalTaxCode() {
        return localTaxCode;
    }

    public void setLocalTaxCode(String localTaxCode) {
        this.localTaxCode = localTaxCode;
    }

    @Column(name = "marital_status")
    public char getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(char maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Column(name = "number_federal_exemptions")
    public short getNumberFederalExemptions() {
        return numberFederalExemptions;
    }

    public void setNumberFederalExemptions(short numberFederalExemptions) {
        this.numberFederalExemptions = numberFederalExemptions;
    }

    @Column(name = "number_state_exemptions")
    public short getNumberStateExemptions() {
        return numberStateExemptions;
    }

    public void setNumberStateExemptions(short numberStateExemptions) {
        this.numberStateExemptions = numberStateExemptions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_bank_code")
    public BankAccount getPayrollBankAccount() {
        return payrollBankAccount;
    }

    public void setPayrollBankAccount(BankAccount payrollBankAccount) {
        this.payrollBankAccount = payrollBankAccount;
    }

    @Column(name = "state_extra_withhold")
    public float getStateExtraWithhold() {
        return stateExtraWithhold;
    }

    public void setStateExtraWithhold(float stateExtraWithhold) {
        this.stateExtraWithhold = stateExtraWithhold;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "benefit_class_id")
    public BenefitClass getBenefitClass() {
        return benefitClass;
    }

    public void setBenefitClass(BenefitClass benefitClass) {
        this.benefitClass = benefitClass;
    }

    @Column(name = "tax_state")
    public String getTaxState() {
        return taxState;
    }

    public void setTaxState(String taxState) {
        this.taxState = taxState;
    }

    @Column(name = "unemployement_state")
    public String getUnemploymentState() {
        return unemploymentState;
    }

    public void setUnemploymentState(String unemploymentState) {
        this.unemploymentState = unemploymentState;
    }

	@Column(name="workers_comp_code")
	public String getWorkersCompCode() {
		return workersCompCode;
	}

	public void setWorkersCompCode(String workersCompCode) {
		this.workersCompCode = workersCompCode;
	}




    /*
    public int getInitialStatusDate() {
    if (initialStatusDate==null)
    return 0;
    return initialStatusDate;
    }

    public void setInitialStatusDate(Integer initialStatusDate) {
    this.initialStatusDate = initialStatusDate;
    }
     */
    @Override
    public String notifyClassName() {
        return "Employee";
    }
         @Override
    public String toString(){
        return this.getNameFML();
    }

	@Column(name="w4_exempt")
	public char getW4exempt() {
		return w4exempt;
	}

	public void setW4exempt(char w4exempt) {
		this.w4exempt = w4exempt;
	}

	@Column(name="w4_name_differs")
	public char getW4nameDiffers() {
		return w4nameDiffers;
	}

	public void setW4nameDiffers(char w4nameDiffers) {
		this.w4nameDiffers = w4nameDiffers;
	}

	@Column(name="w4_status")
	public char getW4status() {
		return w4status;
	}

	public void setW4status(char w4status) {
		this.w4status = w4status;
	}

	@Column(name="medicare")
	public char getMedicare() {
		return medicare;
	}

	public void setMedicare(char medicare) {
		this.medicare = medicare;
	}

	@Column(name="hours_per_week")
	public short getHours_per_week() {
		return hours_per_week;
	}

	public void setHours_per_week(short hours_per_week) {
		this.hours_per_week = hours_per_week;
	}

	@Column(name="employment_type")
	public char getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(char employmentType) {
		this.employmentType = employmentType;
	}

    @Column(name="adp_id")
    public String getAdpId() {
        return adpId;
    }

    public void setAdpId(String adpId) {
        this.adpId = adpId;
    }
}
