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
@Table(name = "hr_benefit")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrBenefit extends ArahantBean implements java.io.Serializable {
	
	private static final transient ArahantLogger logger = new ArahantLogger(HrBenefit.class);
	
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_benefit";
	public static final String START_DATE = "startDate";
	public static final String BENEFIT_PROVIDER = "provider";
	public static final String INSURANCE_CODE = "insuranceCode";
	public static final String WAGE_TYPE = "wageType";
	public static final String HAS_PHYSICIANS = "hasPhysicians";
	public static final String ADDRESS_REQUIRED = "addressRequired";
	private char addressRequired = 'N';
	public static final char PROCESS_EMPLOYEE = 'E';
	public static final char PROCESS_HRSP = 'H';
	public static final char PROCESS_VENDOR = 'V';
	public static final char PROCESS_NORMAL = 'N';
	// Fields
	private String benefitId;
	public static final String BENEFITID = "benefitId";
	private String name;
	public static final String NAME = "name";
	private String ruleName;
	public static final String RULENAME = "ruleName";
	private char timeRelated = 'N';
	public static final String TIMERELATED = "timeRelated";
	private char paidBenefit = 'N';
	public static final String PAIDBENEFIT = "paidBenefit";
	private Set<HrBenefitJoin> hrBenefitJoins = new HashSet<HrBenefitJoin>(0);
	public static final String HREMPLOYEEBENEFITJOINS = "hrBenefitJoins";
	private Set<HrAccrual> hrAccruals = new HashSet<HrAccrual>(0);
	public static final String HRACCRUALS = "hrAccruals";
	private Set<HrBenefitPackageJoin> hrBenefitPackageJoins = new HashSet<HrBenefitPackageJoin>(0);
	public static final String HRBENEFITPACKAGEJOINS = "hrBenefitPackageJoins";
	public static final String BENEFIT_CATEGORY = "hrBenefitCategory";
	public static final String BENEFIT_CATEGORY_ID = "hrBenefitCategoryId";
	public static final String BENEFIT_CLASS = "benefitClasses";
	private HrBenefitCategory hrBenefitCategory;
	private String groupId;
	public static final String GROUPID = "groupId";
	private char preTax = 'N';
	private VendorCompany provider;
	private char requiresDecline = 'N';
	private char hasBeneficiaries = 'N';
	private char contingentBeneficiaries = 'N';
	public static final String HAS_BENEFICIARIES = "hasBeneficiaries";
	public static final String CONTINGENT_BENEFICIARIES = "contingentBeneficiaries";
	public static final String END_DATE = "endDate";
	public static final String HR_BENEFIT_CONFIGS = "benefitConfigs";
	public static final String REQUIRES_DECLINE = "requiresDecline";
	public static final String COVERED_UNDER_COBRA = "coveredUnderCOBRA";
	public static final String PRODUCTSERVICE = "productService";
	public static final String SEQ = "sequence";
	private Set<HrBenefitConfig> benefitConfigs = new HashSet<HrBenefitConfig>();
	private int startDate, endDate;
	private String addInfo;
	private String instructions;
	private char coveredUnderCOBRA = 'N';
	private String hrBenefitCategoryId;
	private String planId;
	private String planName;
	private String payerId;
	private String insuranceCode;
	private String subGroupId;
	private ProductService productService;
	private WageType wageType;
	private String description;
	private short eligibilityType = 1;
	private short eligibilityPeriod;
	private short dependentMaxAge;
	private short dependentMaxAgeStudent;
	private short coverageEndType = 2;
	private short coverageEndPeriod;
	private char processType = PROCESS_NORMAL;
	public static final String PROCESS_TYPE = "processType";
	private short sequence;
	private String avatarPath;
	public static final String OPEN_ENROLLMENT_WIZARD = "openEnrollmentWizard";
	private char openEnrollmentWizard = 'N';
	private char onboardingWizard = 'N';
	private Set<BenefitClass> benefitClasses = new HashSet<BenefitClass>();
	private Screen openEnrollmentScreen;
	private Screen onboardingScreen;
	private String avatarLocation;
	private int lastEnrollmentDate;
	public static final String LAST_ENROLLMENT_DATE = "lastEnrollmentDate";
	public static final String REPLACING_BENEFIT = "replacingBenefit";
	private HrBenefit replacingBenefit;
	public static final String REPLACED_BENEFITS = "replacedBenefits";
	private Set<HrBenefit> replacedBenefits = new HashSet<HrBenefit>();
	private char hasPhysicians = 'N';
	private String ediFieldValue1;
	private String ediFieldValue2;
	private String ediFieldValue3;
	private String ediFieldValue4;
	private String ediFieldValue5;
	private String ediFieldDescription1;
	private String ediFieldDescription2;
	private String ediFieldDescription3;
	private String ediFieldDescription4;
	private String ediFieldDescription5;
	public static final String WIZARD_CONF_BENEFITS = "wizardConfigurationBenefits";
	private String groupAccountId;
	public static final String GROUP_ACCOUNT_ID = "groupAccountId";
	private char autoAssign = 'N';
	public static final String AUTO_ASSIGN = "autoAssign";
	private Set<WizardConfigurationBenefit> wizardConfigurationBenefits = new HashSet<WizardConfigurationBenefit>(0);
	public static final String INTERNAL_ID = "internalId";
	private String internalId;
	public static final String LISP_REFERENCE = "lispReference";
	private String lispReference;
	public static final String RIDERS_ON_BASE_BENEFIT = "ridersOnBaseBenefit";
	private Set<BenefitRider> ridersOnBaseBenefit;
	public static final String RIDERS_ON_RIDER_BENEFIT = "ridersOnRiderBenefit";
	private Set<BenefitRider> ridersOnRiderBenefit;
	public static final String MINIMUM_AGE = "minAge";
	private short minAge;
	public static final String MAXIMUM_AGE = "maxAge";
	private short maxAge;
	public static final String SHOW_ALL_COVERAGES = "showAllCoverages";
	private char showAllCoverages = 'N';
	private char costCalcType = 'C';
	public static final String COST_CALC_TYPE = "costCalcType";
	private char employerCostModel = 'Z';
	private char employeeCostModel = 'Z';
	private char benefitAmountModel = 'N';
	private double minPay;
	private double maxPay;
	private float minHoursPerWeek;

	public HrBenefit() {
	}

	@Column(name = "address_required")
	public char getAddressRequired() {
		return addressRequired;
	}

	public void setAddressRequired(char addressRequired) {
		this.addressRequired = addressRequired;
	}

	@Column(name = "cost_calc_type")
	public char getCostCalcType() {
		return costCalcType;
	}

	public void setCostCalcType(char costCalcType) {
		this.costCalcType = costCalcType;
	}

	@Column(name = "show_all_coverages")
	public char getShowAllCoverages() {
		return showAllCoverages;
	}

	public void setShowAllCoverages(char showAllCoverages) {
		this.showAllCoverages = showAllCoverages;
	}

	@Column(name = "auto_assign")
	public char getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(char autoAssign) {
		this.autoAssign = autoAssign;
	}

	@OneToMany(mappedBy = WizardConfigurationBenefit.BENEFIT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<WizardConfigurationBenefit> getWizardConfigurationBenefits() {
		return wizardConfigurationBenefits;
	}

	public void setWizardConfigurationBenefits(final Set<WizardConfigurationBenefit> wizardConfigurationBenefits) {
		this.wizardConfigurationBenefits = wizardConfigurationBenefits;
	}

	@Column(name = "has_physicians")
	public char getHasPhysicians() {
		return hasPhysicians;
	}

	public void setHasPhysicians(char hasPhysicians) {
		this.hasPhysicians = hasPhysicians;
	}

	@Column(name = "group_account_id")
	public String getGroupAccountId() {
		return groupAccountId;
	}

	public void setGroupAccountId(String groupAccountId) {
		this.groupAccountId = groupAccountId;
	}

	@Column(name = "edi_field_value_1")
	public String getEdiFieldValue1() {
		return ediFieldValue1;
	}

	public void setEdiFieldValue1(String ediFieldValue1) {
		this.ediFieldValue1 = ediFieldValue1;
	}

	@Column(name = "edi_field_value_2")
	public String getEdiFieldValue2() {
		return ediFieldValue2;
	}

	public void setEdiFieldValue2(String ediFieldValue2) {
		this.ediFieldValue2 = ediFieldValue2;
	}

	@Column(name = "edi_field_value_3")
	public String getEdiFieldValue3() {
		return ediFieldValue3;
	}

	public void setEdiFieldValue3(String ediFieldValue3) {
		this.ediFieldValue3 = ediFieldValue3;
	}

	@Column(name = "edi_field_value_4")
	public String getEdiFieldValue4() {
		return ediFieldValue4;
	}

	public void setEdiFieldValue4(String ediFieldValue4) {
		this.ediFieldValue4 = ediFieldValue4;
	}

	@Column(name = "edi_field_value_5")
	public String getEdiFieldValue5() {
		return ediFieldValue5;
	}

	public void setEdiFieldValue5(String ediFieldValue5) {
		this.ediFieldValue5 = ediFieldValue5;
	}

	@Column(name = "edi_field_descrption_1")
	public String getEdiFieldDescription1() {
		return ediFieldDescription1;
	}

	public void setEdiFieldDescription1(String ediFieldDescription1) {
		this.ediFieldDescription1 = ediFieldDescription1;
	}

	@Column(name = "edi_field_descrption_2")
	public String getEdiFieldDescription2() {
		return ediFieldDescription2;
	}

	public void setEdiFieldDescription2(String ediFieldDescription2) {
		this.ediFieldDescription2 = ediFieldDescription2;
	}

	@Column(name = "edi_field_descrption_3")
	public String getEdiFieldDescription3() {
		return ediFieldDescription3;
	}

	public void setEdiFieldDescription3(String ediFieldDescription3) {
		this.ediFieldDescription3 = ediFieldDescription3;
	}

	@Column(name = "edi_field_descrption_4")
	public String getEdiFieldDescription4() {
		return ediFieldDescription4;
	}

	public void setEdiFieldDescription4(String ediFieldDescription4) {
		this.ediFieldDescription4 = ediFieldDescription4;
	}

	@Column(name = "edi_field_descrption_5")
	public String getEdiFieldDescription5() {
		return ediFieldDescription5;
	}

	public void setEdiFieldDescription5(String ediFieldDescription5) {
		this.ediFieldDescription5 = ediFieldDescription5;
	}

	@ManyToMany
	@JoinTable(name = "benefit_group_class_join",
	joinColumns = {
		@JoinColumn(name = "benefit_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "benefit_class_id")})
	public Set<BenefitClass> getBenefitClasses() {
		return benefitClasses;
	}

	public void setBenefitClasses(Set<BenefitClass> benefitClasses) {
		this.benefitClasses = benefitClasses;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}

	@Column(name = "insurance_code")
	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	@Column(name = "payer_id")
	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	@Column(name = "plan_name")
	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	@Column(name = "sub_group_id")
	public String getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public ProductService getProductService() {
		return this.productService;
	}

	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}

	@Column(name = "coverage_end_period")
	public short getCoverageEndPeriod() {
		return coverageEndPeriod;
	}

	public void setCoverageEndPeriod(short coverageEndPeriod) {
		this.coverageEndPeriod = coverageEndPeriod;
	}

	@Column(name = "coverage_end_type")
	public short getCoverageEndType() {
		return coverageEndType;
	}

	public void setCoverageEndType(short coverageEndType) {
		this.coverageEndType = coverageEndType;
	}

	@Column(name = "dependent_max_age")
	public short getDependentMaxAge() {
		return dependentMaxAge;
	}

	public void setDependentMaxAge(short dependentMaxAge) {
		this.dependentMaxAge = dependentMaxAge;
	}

	@Column(name = "dependent_max_age_student")
	public short getDependentMaxAgeStudent() {
		return dependentMaxAgeStudent;
	}

	public void setDependentMaxAgeStudent(short dependentMaxAgeStudent) {
		this.dependentMaxAgeStudent = dependentMaxAgeStudent;
	}

	@Column(name = "eligibility_period")
	public short getEligibilityPeriod() {
		return eligibilityPeriod;
	}

	public void setEligibilityPeriod(short eligibilityPeriod) {
		this.eligibilityPeriod = eligibilityPeriod;
	}

	@Column(name = "eligibility_type")
	public short getEligibilityType() {
		return eligibilityType;
	}

	public void setEligibilityType(short eligibilityType) {
		this.eligibilityType = eligibilityType;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "benefit_id";
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
		return benefitId = IDGenerator.generate(this);
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
	 * @return Returns the benefitConfigs.
	 */
	@OneToMany(mappedBy = HrBenefitConfig.HR_BENEFIT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitConfig> getBenefitConfigs() {
		return benefitConfigs;
	}

	/**
	 * @param benefitConfigs The benefitConfigs to set.
	 */
	public void setBenefitConfigs(Set<HrBenefitConfig> benefitConfigs) {
		this.benefitConfigs = benefitConfigs;
	}

	/**
	 * @return Returns the benefitId.
	 */
	@Id
	@Column(name = "benefit_id")
	public String getBenefitId() {
		return benefitId;
	}

	/**
	 * @param benefitId The benefitId to set.
	 */
	public void setBenefitId(String benefitId) {
		firePropertyChange("benefitId", this.benefitId, benefitId);
		this.benefitId = benefitId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id")
	public VendorCompany getProvider() {
		return provider;
	}

	public void setProvider(VendorCompany provider) {
		this.provider = provider;
	}

	@Deprecated
	public char deprecatedGetEmployeeIsProvider() {
		logger.deprecated();
		return 'Y';
	}

	@Deprecated
	public void setEmployeeIsProvider(char employeeIsProvider) {
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
	 * @return Returns the groupId.
	 */
	@Column(name = "group_id")
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId The groupId to set.
	 */
	public void setGroupId(String groupId) {
		firePropertyChange("groupId", this.groupId, groupId);
		this.groupId = groupId;
	}

	/**
	 * @return Returns the contingentBeneficiaries.
	 */
	@Column(name = "contingent_beneficiaries")
	public char getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}

	/**
	 * @param contingentBeneficiaries The contingentBeneficiaries to set.
	 */
	public void setContingentBeneficiaries(char contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	/**
	 * @return Returns the hasBeneficiaries.
	 */
	@Column(name = "has_beneficiaries")
	public char getHasBeneficiaries() {
		return hasBeneficiaries;
	}

	/**
	 * @param hasBeneficiaries The hasBeneficiaries to set.
	 */
	public void setHasBeneficiaries(char hasBeneficiaries) {
		firePropertyChange("hasBeneficiaries", this.hasBeneficiaries,
				hasBeneficiaries);
		this.hasBeneficiaries = hasBeneficiaries;
	}

	/**
	 * @return Returns the hrAccruals.
	 */
	@OneToMany(mappedBy = HrAccrual.HRBENEFIT, fetch = FetchType.LAZY)
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
	 * @return Returns the hrBenefitCategory.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_cat_id")
	public HrBenefitCategory getHrBenefitCategory() {
		return hrBenefitCategory;
	}

	/**
	 * @param hrBenefitCategory The hrBenefitCategory to set.
	 */
	public void setHrBenefitCategory(HrBenefitCategory hrBenefitCategory) {
		this.hrBenefitCategory = hrBenefitCategory;
	}

	/**
	 * @return Returns the hrBenefitPackageJoins.
	 */
	@OneToMany(mappedBy = HrBenefitPackageJoin.HRBENEFIT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitPackageJoin> getHrBenefitPackageJoins() {
		return hrBenefitPackageJoins;
	}

	/**
	 * @param hrBenefitPackageJoins The hrBenefitPackageJoins to set.
	 */
	public void setHrBenefitPackageJoins(Set<HrBenefitPackageJoin> hrBenefitPackageJoins) {
		this.hrBenefitPackageJoins = hrBenefitPackageJoins;
	}

	/**
	 * @return Returns the hrEmployeeBenefitJoins.
	 */
	@OneToMany(mappedBy = HrBenefitJoin.HRBENEFIT, fetch = FetchType.LAZY)
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
	 * @return Returns the name.
	 */
	@Column(name = "benefit_name")
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
	 * @return Returns the paidBenefit.
	 */
	@Column(name = "paid_benefit")
	public char getPaidBenefit() {
		return paidBenefit;
	}

	/**
	 * @param paidBenefit The paidBenefit to set.
	 */
	public void setPaidBenefit(char paidBenefit) {
		firePropertyChange("paidBenefit", this.paidBenefit, paidBenefit);
		this.paidBenefit = paidBenefit;
	}

	/**
	 * @return Returns the planId.
	 */
	@Column(name = "plan_id")
	public String getPlanId() {
		return planId;
	}

	/**
	 * @param planId The planId to set.
	 */
	public void setPlanId(String plan) {
		firePropertyChange("planId", this.planId, plan);
		this.planId = plan;
	}

	/**
	 * @return Returns the preTax.
	 */
	@Column(name = "pre_tax")
	public char getPreTax() {
		return preTax;
	}

	/**
	 * @param preTax The preTax to set.
	 */
	public void setPreTax(char preTax) {
		firePropertyChange("preTax", this.preTax, preTax);
		this.preTax = preTax;
	}

	/**
	 * @return Returns the requiresDecline.
	 */
	@Column(name = "requires_decline")
	public char getRequiresDecline() {
		return requiresDecline;
	}

	/**
	 * @param requiresDecline The requiresDecline to set.
	 */
	public void setRequiresDecline(char requiresDecline) {
		firePropertyChange("requiresDecline", this.requiresDecline, requiresDecline);
		this.requiresDecline = requiresDecline;
	}

	/**
	 * @return Returns the ruleName.
	 */
	@Column(name = "rule_name")
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName The ruleName to set.
	 */
	public void setRuleName(String ruleName) {
		firePropertyChange("ruleName", this.ruleName, ruleName);
		this.ruleName = ruleName;
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
	 * @return Returns the timeRelated.
	 */
	@Column(name = "time_related")
	public char getTimeRelated() {
		return timeRelated;
	}

	/**
	 * @param timeRelated The timeRelated to set.
	 */
	public void setTimeRelated(char timeRelated) {
		firePropertyChange("timeRelated", this.timeRelated, timeRelated);
		this.timeRelated = timeRelated;
	}

	/**
	 * @return Returns the additionalInstructions.
	 */
	@Column(name = "instructions")
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param additionalInstructions The additionalInstructions to set.
	 */
	public void setInstructions(String additionalInstructions) {
		firePropertyChange("instructions", this.instructions, additionalInstructions);
		this.instructions = additionalInstructions;
	}

	/**
	 * @return Returns the employeeChoosesAmount.
	 */
	@Deprecated
	public char deprecatedGetEmployeeChoosesAmount() {
		logger.deprecated();
		return 'Y';
	}

	/**
	 * @param employeeChoosesAmount The employeeChoosesAmount to set.
	 */
	@Deprecated
	public void setEmployeeChoosesAmount(char employeeChoosesAmount) {
		logger.deprecated();
	}

	/**
	 * @return Returns the employeeChoosesAmount.
	 */
	@Column(name = "cobra")
	public char getCoveredUnderCOBRA() {
		return coveredUnderCOBRA;
	}

	@Column(name = "min_pay")
	public double getMinPay() {
		return minPay;
	}

	public void setMinPay(double minPay) {
		this.minPay = minPay;
	}

	@Column(name = "max_pay")
	public double getMaxPay() {
		return maxPay;
	}

	public void setMaxPay(double maxPay) {
		this.maxPay = maxPay;
	}

	@Column(name = "min_hours_per_week")
	public float getMinHoursPerWeek() {
		return minHoursPerWeek;
	}

	public void setMinHoursPerWeek(float minHoursPerWeek) {
		this.minHoursPerWeek = minHoursPerWeek;
	}

	/**
	 * @param coveredUnderCOBRA The coveredUnderCOBRA to set.
	 */
	public void setCoveredUnderCOBRA(char coveredUnderCOBRA) {
		firePropertyChange("coveredUnderCOBRA", this.coveredUnderCOBRA, coveredUnderCOBRA);
		this.coveredUnderCOBRA = coveredUnderCOBRA;
	}

	@Override
	public boolean equals(Object o) {
		if (benefitId == null && o == null)
			return true;
		if (benefitId != null && o instanceof HrBenefit)
			return benefitId.equals(((HrBenefit) o).getBenefitId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitId == null)
			return 0;
		return benefitId.hashCode();
	}

	@Column(name = "benefit_cat_id", insertable = false, updatable = false)
	public String getHrBenefitCategoryId() {
		return hrBenefitCategoryId;
	}

	public void setHrBenefitCategoryId(String hrBenefitCategoryId) {
		firePropertyChange("hrBenefitCategoryId", this.hrBenefitCategoryId,
				hrBenefitCategoryId);
		this.hrBenefitCategoryId = hrBenefitCategoryId;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "process_type")
	public char getProcessType() {
		return processType;
	}

	public void setProcessType(char processType) {
		this.processType = processType;
	}

	@Column(name = "seqno")
	public short getSequence() {
		return sequence;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	@Column(name = "avatar_path")
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	@Column(name = "open_enrollment_wizard")
	public char getOpenEnrollmentWizard() {
		return openEnrollmentWizard;
	}

	public void setOpenEnrollmentWizard(char openEnrollmentWizard) {
		this.openEnrollmentWizard = openEnrollmentWizard;
	}

	@Column(name = "onboarding_wizard")
	public char getOnboarding() {
		return onboardingWizard;
	}

	public void setOnboarding(char onboarding) {
		this.onboardingWizard = onboarding;
	}

	@Column(name = "employer_cost_model")
	public char getEmployerCostModel() {
		return employerCostModel;
	}

	public void setEmployerCostModel(char employerCostModel) {
		this.employerCostModel = employerCostModel;
	}

	@Column(name = "employee_cost_model")
	public char getEmployeeCostModel() {
		return employeeCostModel;
	}

	public void setEmployeeCostModel(char employeeCostModel) {
		this.employeeCostModel = employeeCostModel;
	}

	@Column(name = "benefit_amount_model")
	public char getBenefitAmountModel() {
		return benefitAmountModel;
	}

	public void setBenefitAmountModel(char benefitAmountModel) {
		this.benefitAmountModel = benefitAmountModel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "open_enrollment_screen_id")
	public Screen getOpenEnrollmentScreen() {
		return openEnrollmentScreen;
	}

	public void setOpenEnrollmentScreen(Screen openEnrollmentScreen) {
		this.openEnrollmentScreen = openEnrollmentScreen;
	}

	@Column(name = "avatar_location")
	public String getAvatarLocation() {
		return avatarLocation;
	}

	public void setAvatarLocation(String avatarLocation) {
		this.avatarLocation = avatarLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_screen_id")
	public Screen getOnboardingScreen() {
		return onboardingScreen;
	}

	public void setOnboardingScreen(Screen onboardingScreen) {
		this.onboardingScreen = onboardingScreen;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id_replaced_by")
	public HrBenefit getReplacingBenefit() {
		return replacingBenefit;
	}

	public void setReplacingBenefit(HrBenefit replacingBenefit) {
		this.replacingBenefit = replacingBenefit;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id_replaced_by")
	public Set<HrBenefit> getReplacedBenefits() {
		return replacedBenefits;
	}

	public void setReplacedBenefits(Set<HrBenefit> replacedBenefits) {
		this.replacedBenefits = replacedBenefits;
	}

	@Column(name = "last_enrollment_date")
	public int getLastEnrollmentDate() {
		return lastEnrollmentDate;
	}

	public void setLastEnrollmentDate(int lastEnrollmentDate) {
		this.lastEnrollmentDate = lastEnrollmentDate;
	}

	@Column(name = "internal_id")
	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	@Column(name = "lisp_reference")
	public String getLispReference() {
		return lispReference;
	}

	public void setLispReference(String lispReference) {
		this.lispReference = lispReference;
	}

	@OneToMany(mappedBy = BenefitRider.BASE_BENEFIT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<BenefitRider> getRidersOnBaseBenefit() {
		return ridersOnBaseBenefit;
	}

	public void setRidersOnBaseBenefit(Set<BenefitRider> ridersOnBaseBenefit) {
		this.ridersOnBaseBenefit = ridersOnBaseBenefit;
	}

	@OneToMany(mappedBy = BenefitRider.RIDER_BENEFIT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<BenefitRider> getRidersOnRiderBenefit() {
		return ridersOnRiderBenefit;
	}

	public void setRidersOnRiderBenefit(Set<BenefitRider> ridersOnRiderBenefit) {
		this.ridersOnRiderBenefit = ridersOnRiderBenefit;
	}

	@Column(name = "max_age")
	public short getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(short maxAge) {
		this.maxAge = maxAge;
	}

	@Column(name = "min_age")
	public short getMinAge() {
		return minAge;
	}

	public void setMinAge(short minAge) {
		this.minAge = minAge;
	}
}
