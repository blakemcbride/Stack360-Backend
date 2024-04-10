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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "hr_benefit_config")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrBenefitConfig extends ArahantBean implements java.io.Serializable {
	
	private static final transient ArahantLogger logger = new ArahantLogger(HrBenefitConfig.class);

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_benefit_config";
	public static final String BENEFIT_CLASSES = "benefitClasses";
	public static final String INSURANCE_CODE = "insuranceCode";
	// Fields
	private String benefitConfigId;
	public static final String BENEFIT_CONFIG_ID = "benefitConfigId";
	private String name;
	public static final String NAME = "name";
	private Set<HrBenefitJoin> hrBenefitJoins = new HashSet<HrBenefitJoin>(0);
	private Set<HrBenefitJoinH> hrBenefitJoinsH = new HashSet<HrBenefitJoinH>(0);
	public static final String HR_BENEFIT_JOINS = "hrBenefitJoins";
	public static final String HR_BENEFIT_JOINS_H = "hrBenefitJoinsH";
	public static final String COVERS_EMPLOYEE = "employee";
	private char spouseNonEmpOrChildren = 'N';
	private char employee = 'N';
	private char spouseNonEmployee = 'N';
	private char children = 'N';
	private short maxChildren;
	private float employeeCOBRACost;
	private String addInfo;
	private int startDate, endDate;
	private char autoAssign = 'N';
	public static final String AUTO_ASSIGN = "autoAssign";
	public static final String COVERS_NON_EMP_SPOUSE = "spouseNonEmployee";
	public static final String COVERS_NON_EMP_SPOUSE_CHILDREN = "spouseNonEmpOrChildren";
	public static final String COVERS_CHILDREN = "children";
	public static final String START_DATE = "startDate";
	public static final String COVERS_EMPLOYEE_SPOUSE_CHILDREN = "spouseEmpOrChildren";
	public static final String END_DATE = "endDate";
	public static final String HR_BENEFIT = "hrBenefit";
	public static final String HR_BENEFIT_ID = "benefitId";
	private HrBenefit hrBenefit;
	private char spouseDeclinesOutside = 'N';
	private char spouseEmployee = 'N';
	private char spouseEmpOrChildren = 'N';
//	private Set<HrEmployeeStatus> statuses=new HashSet<HrEmployeeStatus>();
	private Set<HrBenefitProjectJoin> hrBenefitProjectJoins = new HashSet<HrBenefitProjectJoin>(0);
	public static final String HRBENEFITPROJECTJOINS = "hrBenefitProjectJoins";
	public static final String COVERS_EMPLOYEE_SPOUSE = "spouseEmployee";
	public static final String SPOUSAL = "spouseDeclinesOutside";
	public static final String MAX_DEPENDENTS = "maxChildren";
	private String generatedCost;
	private String benefitId;
	private boolean available = true;
	private char onBilling = 'Y';
	private Set<BenefitClass> benefitClasses = new HashSet<BenefitClass>();
	private String insuranceCode;
	private Set<Project> projects = new HashSet<Project>(0);
	public static String SEQ = "sequence";
	private short sequence;

	@ManyToMany
	@JoinTable(name = "benefit_class_join",
	joinColumns = {
		@JoinColumn(name = "benefit_config_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "benefit_class_id")})
	public Set<BenefitClass> getBenefitClasses() {
		return benefitClasses;
	}

	public void setBenefitClasses(Set<BenefitClass> benefitClasses) {
		this.benefitClasses = benefitClasses;
	}

	@Column(name = "on_billing")
	public char getOnBilling() {
		return onBilling;
	}

	public void setOnBilling(char onBilling) {
		this.onBilling = onBilling;
	}

	@Transient
	public boolean getAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		firePropertyChange("available", this.available, available);
		this.available = available;
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		firePropertyChange("benefitId", this.benefitId, benefitId);
		this.benefitId = benefitId;
	}

	/**
	 * @return Returns the maxEmployeeAmount.
	 */
	@Deprecated
	public double deprecatedGetMaxEmployeeAmount() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @param maxEmployeeAmount The maxEmployeeAmount to set.
	 */
	@Deprecated
	public void setMaxEmployeeAmount(double maxEmployeeAmount) {
//		firePropertyChange("maxEmployeeAmount", this.maxEmployeeAmount,
//				maxEmployeeAmount);
//		this.maxEmployeeAmount = maxEmployeeAmount;
		logger.deprecated();
	}

	/**
	 * @return Returns the calculatedCost.
	 *
	 * public String getCalculatedCost() { return calculatedCost; }
	 *
	 * /
	 **
	 * @param calculatedCost The calculatedCost to set.
	 *
	 * public void setCalculatedCost(String calculatedCost) {
	 * firePropertyChange("calculatedCost", this.calculatedCost,
	 * calculatedCost); this.calculatedCost = calculatedCost; }
	 *
	 * /** default constructor
	 */
	public HrBenefitConfig() {
	}

	/**
	 * @return Returns the addInfo.
	 */
	@Column(name = "add_info")
	public String getAddInfo() {
		return addInfo;
	}

	/**
	 * @param addInfo The addInfo to set.
	 */
	public void setAddInfo(String addInfo) {
		firePropertyChange("addInfo", this.addInfo, addInfo);
		this.addInfo = addInfo;
	}

	/**
	 * @return Returns the autoAssign.
	 */
	@Column(name = "auto_assign")
	public char getAutoAssign() {
		return autoAssign;
	}

	/**
	 * @param autoAssign The autoAssign to set.
	 */
	public void setAutoAssign(char autoAssign) {
		firePropertyChange("autoAssign", this.autoAssign, autoAssign);
		this.autoAssign = autoAssign;
	}

	/**
	 * @return Returns the benefitConfigId.
	 */
	@Id
	@Column(name = "benefit_config_id")
	public String getBenefitConfigId() {
		return benefitConfigId;
	}

	/**
	 * @param benefitConfigId The benefitConfigId to set.
	 */
	public void setBenefitConfigId(String benefitConfigId) {
		firePropertyChange("benefitConfigId", this.benefitConfigId, benefitConfigId);
		this.benefitConfigId = benefitConfigId;
	}

	/**
	 * @return Returns the children.
	 */
	@Column(name = "children")
	public char getChildren() {
		return children;
	}

	/**
	 * @param children The children to set.
	 */
	public void setChildren(char children) {
		firePropertyChange("children", this.children, children);
		this.children = children;
	}

	/**
	 * @return Returns the employee.
	 */
	@Column(name = "employee")
	public char getEmployee() {
		return employee;
	}

	/**
	 * @param employee The employee to set.
	 */
	public void setEmployee(char employee) {
		firePropertyChange("employee", this.employee, employee);
		this.employee = employee;
	}

	/**
	 * @return Returns the employeeCost.
	 */
	@Deprecated
	public float deprecatedGetEmployeeCost() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @return Returns the employeeCOBRACost.
	 */
	@Column(name = "employee_cobra_cost")
	public float getEmployeeCOBRACost() {
		return employeeCOBRACost;
	}

	/**
	 * @param employeeCost The employeeCost to set.
	 */
	@Deprecated
	public void setEmployeeCost(float employeeCost) {
//		firePropertyChange("employeeCost", this.employeeCost, employeeCost);
//		this.employeeCost = employeeCost;
		logger.deprecated();
	}

	/**
	 * @param employeeCOBRACost The employeeCOBRACost to set.
	 */
	public void setEmployeeCOBRACost(float employeeCOBRACost) {
		firePropertyChange("employeeCOBRACost", this.employeeCOBRACost, employeeCOBRACost);
		this.employeeCOBRACost = employeeCOBRACost;
	}

	/**
	 * @return Returns the employerCost.
	 */
	@Deprecated
	public float deprecatedGetEmployerCost() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @param employerCost The employerCost to set.
	 */
	@Deprecated
	public void setEmployerCost(float employerCost) {
//		firePropertyChange("employerCost", this.employerCost, employerCost);
//		this.employerCost = employerCost;
		logger.deprecated();
	}

	/**
	 * @return Returns the endDate.
	 */
	@Column(name = "end_date")
	public int getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(int endDate) {
		firePropertyChange("endDate", this.endDate, endDate);
		this.endDate = endDate;
	}

	/**
	 * @return Returns the hrBenefit.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getHrBenefit() {
		return hrBenefit;
	}

	/**
	 * @param hrBenefit The hrBenefit to set.
	 */
	public void setHrBenefit(HrBenefit hrBenefit) {
		this.hrBenefit = hrBenefit;
	}

	/**
	 * @return Returns the hrBenefitProjectJoins.
	 */
	@OneToMany(mappedBy = HrBenefitProjectJoin.HR_BENEFIT_CONFIG, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitProjectJoin> getHrBenefitProjectJoins() {
		return hrBenefitProjectJoins;
	}

	/**
	 * @param hrBenefitProjectJoins The hrBenefitProjectJoins to set.
	 */
	public void setHrBenefitProjectJoins(Set<HrBenefitProjectJoin> hrBenefitProjectJoins) {

		this.hrBenefitProjectJoins = hrBenefitProjectJoins;
	}

	/**
	 * @return Returns the hrEmployeeBenefitJoins.
	 */
	@OneToMany(mappedBy = HrBenefitJoin.HR_BENEFIT_CONFIG, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoin> getHrBenefitJoins() {
		return hrBenefitJoins;
	}

	/**
	 * @param hrEmployeeBenefitJoins The hrEmployeeBenefitJoins to set.
	 */
	public void setHrBenefitJoins(Set<HrBenefitJoin> hrEmployeeBenefitJoins) {

		this.hrBenefitJoins = hrEmployeeBenefitJoins;
	}

	/**
	 * @return Returns the hrEmployeeBenefitJoins.
	 */
	@OneToMany(mappedBy = HrBenefitJoinH.HR_BENEFIT_CONFIG, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoinH> getHrBenefitJoinsH() {
		return hrBenefitJoinsH;
	}

	/**
	 * @param hrEmployeeBenefitJoins The hrEmployeeBenefitJoins to set.
	 */
	public void setHrBenefitJoinsH(Set<HrBenefitJoinH> hrEmployeeBenefitJoinsH) {

		this.hrBenefitJoinsH = hrEmployeeBenefitJoinsH;
	}

	/**
	 * @return Returns the maxChildren.
	 */
	@Column(name = "max_children")
	public short getMaxChildren() {
		return maxChildren;
	}

	/**
	 * @param maxChildren The maxChildren to set.
	 */
	public void setMaxChildren(short maxChildren) {
		firePropertyChange("maxChildren", this.maxChildren, maxChildren);
		this.maxChildren = maxChildren;
	}

	/**
	 * @return Returns the name.
	 */
	@Column(name = "config_name")
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		firePropertyChange("name", this.name, name);
		this.name = name;
	}

	/**
	 * @return Returns the spouseDeclinesOutside.
	 */
	@Column(name = "spouse_declines_outside")
	public char getSpouseDeclinesOutside() {
		return spouseDeclinesOutside;
	}

	/**
	 * @param spouseDeclinesOutside The spouseDeclinesOutside to set.
	 */
	public void setSpouseDeclinesOutside(char spouseDeclinesOutside) {
		firePropertyChange("spouseDeclinesOutside", this.spouseDeclinesOutside,
				spouseDeclinesOutside);
		this.spouseDeclinesOutside = spouseDeclinesOutside;
	}

	/**
	 * @return Returns the spouseEmployee.
	 */
	@Column(name = "spouse_employee")
	public char getSpouseEmployee() {
		return spouseEmployee;
	}

	/**
	 * @param spouseEmployee The spouseEmployee to set.
	 */
	public void setSpouseEmployee(char spouseEmployee) {
		firePropertyChange("spouseEmployee", this.spouseEmployee, spouseEmployee);
		this.spouseEmployee = spouseEmployee;
	}

	/**
	 * @return Returns the spouseEmpOrChildren.
	 */
	@Column(name = "spouse_emp_or_children")
	public char getSpouseEmpOrChildren() {
		return spouseEmpOrChildren;
	}

	/**
	 * @param spouseEmpOrChildren The spouseEmpOrChildren to set.
	 */
	public void setSpouseEmpOrChildren(char spouseEmpOrChildren) {
		firePropertyChange("spouseEmpOrChildren", this.spouseEmpOrChildren,
				spouseEmpOrChildren);
		this.spouseEmpOrChildren = spouseEmpOrChildren;
	}

	/**
	 * @return Returns the spouseNonEmployee.
	 */
	@Column(name = "spouse_non_employee")
	public char getSpouseNonEmployee() {
		return spouseNonEmployee;
	}

	/**
	 * @param spouseNonEmployee The spouseNonEmployee to set.
	 */
	public void setSpouseNonEmployee(char spouseNonEmployee) {
		firePropertyChange("spouseNonEmployee", this.spouseNonEmployee,
				spouseNonEmployee);
		this.spouseNonEmployee = spouseNonEmployee;
	}

	/**
	 * @return Returns the spouseNonEmpOrChildren.
	 */
	@Column(name = "spouse_non_emp_or_children")
	public char getSpouseNonEmpOrChildren() {
		return spouseNonEmpOrChildren;
	}

	/**
	 * @param spouseNonEmpOrChildren The spouseNonEmpOrChildren to set.
	 */
	public void setSpouseNonEmpOrChildren(char spouseNonEmpOrChildren) {
		firePropertyChange("spouseNonEmpOrChildren", this.spouseNonEmpOrChildren,
				spouseNonEmpOrChildren);
		this.spouseNonEmpOrChildren = spouseNonEmpOrChildren;
	}

	/**
	 * @return Returns the startDate.
	 */
	@Column(name = "start_date")
	public int getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(int startDate) {
		firePropertyChange("startDate", this.startDate, startDate);
		this.startDate = startDate;
	}

	/**
	 * @return Returns the statuses.
	 *
	 * public Set<HrEmployeeStatus> getStatuses() { return statuses; }
	 *
	 * /
	 **
	 * @param statuses The statuses to set.
	 *
	 * public void setStatuses(Set<HrEmployeeStatus> statuses) { this.statuses =
	 * statuses; }
	 *
	 * /* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {

		return "benefit_config_id";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {

		return TABLE_NAME;
	}


	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setBenefitConfigId(IDGenerator.generate(this));
		return benefitConfigId;
	}

	@Transient
	public String getGroupId() {
		return hrBenefit.getGroupId();
	}

	/**
	 * @return
	 */
	@Transient
	public HrBenefitCategory getHrBenefitCategory() {

		return hrBenefit.getHrBenefitCategory();
	}

	/**
	 * @return Returns the maxAmount.
	 */
	@Deprecated
	public float deprecatedGetMaxAmount() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @param maxAmount The maxAmount to set.
	 */
	@Deprecated
	public void setMaxAmount(float maxEmployeeAmount) {
//		firePropertyChange("maxAmount", this.maxEmployeeAmount, maxEmployeeAmount);
//		this.maxEmployeeAmount = maxEmployeeAmount;
		logger.deprecated();
	}

	/**
	 * Returns whether or not the config's parent benefit is covered under COBRA
	 *
	 * @return
	 */
	@Transient
	public boolean getCoveredUnderCOBRA() {
		return hrBenefit.getCoveredUnderCOBRA() == 'Y';
	}

	@Column(name = "insurance_code")
	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hr_benefit_project_join",
	joinColumns = {
		@JoinColumn(name = "benefit_config_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "project_id")})
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@Transient
	public String getGeneratedCost() {
		return generatedCost;
	}

	public void setGeneratedCost(String generatedCost) {
		firePropertyChange("generatedCost", this.generatedCost, generatedCost);
		this.generatedCost = generatedCost;
	}

	@Column(name = "seqno")
	public short getSequence() {
		return sequence;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	@Deprecated
	public double deprecatedGetMinValue() {
		logger.deprecated();
		return 0;
	}

	@Deprecated
	public void setMinValue(double minValue) {
		logger.deprecated();
	}

	@Deprecated
	public int deprecatedGetStepValue() {
		logger.deprecated();
		return 0;
	}

	@Deprecated
	public void setStepValue(int stepValue) {
		logger.deprecated();
	}

	@Override
	public boolean equals(Object o) {
		if (benefitConfigId == null && o == null)
			return true;
		if (benefitConfigId != null && o instanceof HrBenefitConfig)
			return benefitConfigId.equals(((HrBenefitConfig) o).getBenefitConfigId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitConfigId == null)
			return 0;
		return benefitConfigId.hashCode();
	}
}
