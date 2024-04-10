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
import com.arahant.reports.HRBenefitReport;
import com.arahant.reports.MissingBenefitCostReport;
import com.arahant.utils.*;
import java.util.*;
import jess.Fact;
import jess.JessException;
import org.kissweb.StringUtils;

public class BHRBenefit extends BusinessLogicBase implements IDBFunctions {
	private static final transient ArahantLogger logger = new ArahantLogger(BHRBenefit.class);
	private HrBenefit hrBenefit;

	public BHRBenefit() {
	}

	public HrBenefit getHrBenefit() {
		return hrBenefit;
	}

	public BHRBenefit(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHRBenefit(final HrBenefit account) {
		hrBenefit = account;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrBenefit = new HrBenefit();
		return hrBenefit.generateId();
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BHRBenefit(id).delete();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (HrBenefitConfig c : hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, hrBenefit).list()) {
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, c).exists())
				throw new ArahantWarning("You cannot delete benefits that are assigned to employees.");
			new BHRBenefitConfig(c).delete();
		}
		if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HRBENEFIT, hrBenefit).exists())
			throw new ArahantWarning("You cannot delete benefits that are assigned to employees.");
		hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, hrBenefit).delete();
		hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, hrBenefit).delete();
		for (WizardConfigurationBenefit wcb : hsu.createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.BENEFIT, hrBenefit).list())
			new BWizardConfigurationBenefit(wcb).delete();

		hsu.delete(hrBenefit);
	}

	public String getAddressRequired() {
		return hrBenefit.getAddressRequired() + "";
	}

	public void setAddressRequired(String addressRequired) {
		if (!StringUtils.isEmpty(addressRequired))
			hrBenefit.setAddressRequired(addressRequired.charAt(0));
	}

	public void setAddressRequired(char addressRequired) {
		hrBenefit.setAddressRequired(addressRequired);
	}

	public HrBenefit getBean() {
		return hrBenefit;
	}

	public String getId() {
		return hrBenefit.getBenefitId();
	}

	public String getGroupAccountId() {
		return hrBenefit.getGroupAccountId();
	}

	public void setGroupAccountId(String groupAccountId) {
		hrBenefit.setGroupAccountId(groupAccountId);
	}

	public String getInsuranceCode() {
		return hrBenefit.getInsuranceCode();
	}

	public String getPayerId() {
		return hrBenefit.getPayerId();
	}

	public String getPlanName() {
		return hrBenefit.getPlanName();
	}

	public String getSubGroupId() {
		return hrBenefit.getSubGroupId();
	}

	public String getUnderwriterId() {
		if (hrBenefit.getProvider() == null)
			return "";
		return hrBenefit.getProvider().getOrgGroupId();
	}

	public VendorCompany getProvider() {
		return hrBenefit.getProvider();
	}

	public String getWageTypeId() {
		if (hrBenefit.getWageType() == null)
			return "";
		return hrBenefit.getWageType().getWageTypeId();
	}

	public short getCoverageEndPeriod() {
		return hrBenefit.getCoverageEndPeriod();
	}

	public void setCoverageEndPeriod(int coverageEndPeriod) {
		hrBenefit.setCoverageEndPeriod((short) coverageEndPeriod);
	}

	public short getCoverageEndType() {
		return hrBenefit.getCoverageEndType();
	}

	public void setCoverageEndType(int coverageEndType) {
		hrBenefit.setCoverageEndType((short) coverageEndType);
	}

	public short getDependentMaxAge() {
		return hrBenefit.getDependentMaxAge();
	}

	public void setDependentMaxAge(int dependentMaxAge) {
		hrBenefit.setDependentMaxAge((short) dependentMaxAge);
	}

	public short getMaxAge() {
		return hrBenefit.getMaxAge();
	}

	public void setMaxAge(int maxAge) {
		hrBenefit.setMaxAge((short) maxAge);
	}

	public void setMaxAge(short maxAge) {
		hrBenefit.setMaxAge(maxAge);
	}

	public short getMinAge() {
		return hrBenefit.getMinAge();
	}

	public void setMinAge(int minAge) {
		hrBenefit.setMinAge((short) minAge);
	}

	public void setMinAge(short minAge) {
		hrBenefit.setMinAge(minAge);
	}

	public short getDependentMaxAgeStudent() {
		return hrBenefit.getDependentMaxAgeStudent();
	}

	public void setDependentMaxAgeStudent(int dependentMaxAgeStudent) {
		hrBenefit.setDependentMaxAgeStudent((short) dependentMaxAgeStudent);
	}

	public short getEligibilityPeriod() {
		return hrBenefit.getEligibilityPeriod();
	}

	public void setEligibilityPeriod(int eligibilityPeriod) {
		hrBenefit.setEligibilityPeriod((short) eligibilityPeriod);
	}

	public int getEligibilityType() {
		return (int) hrBenefit.getEligibilityType();
	}

	public void setEligibilityType(int eligibilityType) {
		hrBenefit.setEligibilityType((short) eligibilityType);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		checkData();
		ArahantSession.getHSU().insert(hrBenefit);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrBenefit = ArahantSession.getHSU().get(HrBenefit.class, key);
		if (hrBenefit == null)
			throw new ArahantException("Could not load benefit with key '" + key + "'.");
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setInsuranceCode(String insuranceCode) {
		hrBenefit.setInsuranceCode(insuranceCode);
	}

	public void setPayerId(String payerId) {
		hrBenefit.setPayerId(payerId);
	}

	public void setPlanName(String planName) {
		hrBenefit.setPlanName(planName);
	}

	public void setSubGroupId(String subGroupId) {
		hrBenefit.setSubGroupId(subGroupId);
	}

	public void setWageType(WageType wt) {
		hrBenefit.setWageType(wt);
	}

	public void setWageTypeId(String wageTypeId) {
		hrBenefit.setWageType(ArahantSession.getHSU().get(WageType.class, wageTypeId));
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		checkData();
		ArahantSession.getHSU().saveOrUpdate(hrBenefit);
	}

	private void checkData() throws ArahantWarning {
		if (hrBenefit.getRequiresDecline() == 'Y' && hrBenefit.getHrBenefitCategory() != null && hrBenefit.getHrBenefitCategory().getRequiresDecline() == 'Y')
			throw new ArahantWarning("Benefit can not be set to require a decline if category already requires the decline.");
	}

	/**
	 * @return @see com.arahant.beans.HrBenefit#getBenefitId()
	 */
	public String getBenefitId() {
		return hrBenefit.getBenefitId();
	}

	@SuppressWarnings("unchecked")
	public List<String> getConfigIds() {
		return (List) ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, hrBenefit).list();
	}

	public List<HrBenefitConfig> getConfigs() {
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, hrBenefit).list();
	}

	/**
	 * @return @see com.arahant.beans.HrBenefit#getName()
	 */
	public String getName() {
		return hrBenefit.getName();
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrBenefit#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrBenefit.setName(name);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRBenefit(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRBenefit[] list(final HibernateSessionUtil hsu) {

		final List l = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));
		return ret;
	}

	public static BHRBenefit[] listBenefits(final String catId) {

		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);

		if (!isEmpty(catId))
			hcu.eq(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(catId).getBean());
		else
			hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefitCategory.SEQ);

		hcu.orderBy(HrBenefit.SEQ);

		final List l = hcu.list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final boolean includeConfigs) throws Exception {

		final HRBenefitReport rep = new HRBenefitReport();
		rep.build(hsu, includeConfigs);

		return rep.getFilename();
	}

	/**
	 * @param ruleName
	 */
	public void setRuleName(final String ruleName) {
		hrBenefit.setRuleName(ruleName);
	}

	/**
	 * @return
	 */
	public String getRuleName() {
		return hrBenefit.getRuleName();
	}

	public int getSequence() {
		return hrBenefit.getSequence();
	}

	public void setSequence(int sequence) {
		hrBenefit.setSequence((short) sequence);
	}

	/**
	 * @param timeRelated
	 */
	public void setTimeRelated(final boolean timeRelated) {
		hrBenefit.setTimeRelated(timeRelated ? 'Y' : 'N');
	}

	/**
	 * @return
	 */
	public boolean getTimeRelated() {
		return hrBenefit.getTimeRelated() == 'Y';
	}

	/**
	 * @return @throws ArahantException
	 */
	public static String[] listRules() throws ArahantException {

		try {
			final JessBean jb = new JessBean(ArahantSession.getAI());

			final List rules = jb.queryForFacts("assignableRule");

			final String x[] = new String[rules.size()];

			for (int loop = 0; loop < x.length; loop++)
				x[loop] = jb.getFactStringValue((Fact) rules.get(loop), "name");


			return x;
		} catch (final JessException e) {
			throw new ArahantException(e.getMessage());
		}
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRBenefit[] listTimeRelatedBenefits(final HibernateSessionUtil hsu) {

		final List l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').orderBy(HrBenefit.NAME).list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));

		return ret;
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRBenefit[] listTimeRelatedPaidBenefits(final HibernateSessionUtil hsu, String personId) {
		final List l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y') //	.eq(HrBenefit.PAIDBENEFIT,'Y')  //show unpaid per request from Gates
				.orderBy(HrBenefit.NAME).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON_ID, personId).list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));

		return ret;
	}

	/**
	 * @param personId
	 * @return
	 */
	public boolean isValidForEmployee(final String personId) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, hrBenefit);
		hcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Person.PERSONID, personId);
		return hcu.first() != null;
	}

	public void setPaid(final boolean paid) {
		hrBenefit.setPaidBenefit(paid ? 'Y' : 'N');
	}

	/**
	 * @return
	 */
	public boolean getPaid() {
		return hrBenefit.getPaidBenefit() == 'Y';
	}

	/**
	 * @param l
	 * @return
	 */
	public static BHRBenefit[] makeArray(final List<HrBenefit> l) {
		final BHRBenefit[] ret = new BHRBenefit[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefit(l.get(loop));
		return ret;
	}

	/**
	 * @param preTax
	 */
	public void setPreTax(char preTax) {
		hrBenefit.setPreTax(preTax);
	}

	/**
	 * @param applytoSalary
	 */
	@Deprecated
	public void setApplyToSalary(final boolean applytoSalary) {
		hrBenefit.setEmployeeIsProvider(applytoSalary ? 'Y' : 'N');
	}

	/**
	 * @param underwriter
	 */
	public void setUnderwriter(final String underwriter) {
		hrBenefit.setProvider(ArahantSession.getHSU().get(VendorCompany.class, underwriter));
	}

	/**
	 * @return
	 */
	public String getUnderwriter() {
		if (hrBenefit.getProvider() == null)
			return "";
		return hrBenefit.getProvider().getName();
	}

	/**
	 * @return
	 */
	public char getPreTax() {
		return hrBenefit.getPreTax();
	}

	/**
	 * @return
	 */
	@Deprecated
	public boolean getApplytoSalary() {
		return hrBenefit.deprecatedGetEmployeeIsProvider() == 'Y';
	}

	public void setBenefitCategoryId(final String benefitCategoryCategoryId) {
		hrBenefit.setHrBenefitCategory(ArahantSession.getHSU().get(HrBenefitCategory.class, benefitCategoryCategoryId));
	}

	/**
	 * @return
	 */
	public String getBenefitCategoryId() {
		try {
			return hrBenefit.getHrBenefitCategory().getBenefitCatId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getCategoryName() {
		try {
			return hrBenefit.getHrBenefitCategory().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	public static BHRBenefit[] listByClassId(final String classId) {
		final List l = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).joinTo(HrBenefit.BENEFIT_CLASS).eq(BenefitClass.BENEFIT_CLASS_ID, classId).list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));
		return ret;
	}
	
	public static List<HrBenefit> listActiveByVendor(VendorCompany v) {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).eq(HrBenefit.BENEFIT_PROVIDER, v).gtOrEq(HrBenefit.END_DATE, DateUtils.today(), 0).list();
	}

	/**
	 * @param benefitCategoryId
	 * @return
	 */
	public static BHRBenefit[] list(final String benefitCategoryId) {
		final List<HrBenefit> l = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, benefitCategoryId).list();

		final BHRBenefit[] ret = new BHRBenefit[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefit((HrBenefit) l.get(loop));
		return ret;
	}
	private String assignedUnder = "";

	/**
	 * @return Returns the assignedUnder.
	 */
	public String getAssignedUnder() {
		return assignedUnder;
	}

	/**
	 * @param assignedUnder The assignedUnder to set.
	 */
	public void setAssignedUnder(final String assignedUnder) {
		this.assignedUnder = assignedUnder;
	}

	/**
	 * @return
	 */
	public HrBenefitCategory getBenefitCategory() {
		return hrBenefit.getHrBenefitCategory();
	}

	/**
	 * @return
	 */
	public String getActive() {
		if (hrBenefit.getEndDate() == 0 || hrBenefit.getEndDate() > DateUtils.now())
			return "Y";
		return "N";
	}

	/**
	 * @return
	 */
	public String getGroupId() {
		return hrBenefit.getGroupId();
	}

	@Deprecated
	public String getEmployeeIsProvider() {
		logger.deprecated();
		return "Y";
	}

	/**
	 * @param categoryId
	 */
	public void setCategoryId(final String categoryId) {
		hrBenefit.setHrBenefitCategory(ArahantSession.getHSU().get(HrBenefitCategory.class, categoryId));
	}

	/**
	 * @param groupId
	 */
	public void setGroupId(final String groupId) {
		hrBenefit.setGroupId(groupId);
	}

	/**
	 * @param additionalInfo
	 */
	public void setAdditionalInfo(final String additionalInfo) {
		hrBenefit.setAddInfo(additionalInfo);
	}

	/**
	 * @param employeeIsProvider
	 */
	@Deprecated
	public void setEmployeeIsProvider(final boolean employeeIsProvider) {
		logger.deprecated();
	}

	public void setContingentBeneficiaries(final boolean hasContingentBeneficiaries) {
		hrBenefit.setContingentBeneficiaries(hasContingentBeneficiaries ? 'Y' : 'N');
	}

	/**
	 * @param hasBeneficiaries
	 */
	public void setHasBeneficiaries(final boolean hasBeneficiaries) {
		hrBenefit.setHasBeneficiaries(hasBeneficiaries ? 'Y' : 'N');
	}

	/**
	 * @return
	 */
	public String getAdditionalInfo() {
		return hrBenefit.getAddInfo();
	}

	/**
	 * @return
	 */
	public String getCategoryId() {
		try {
			return hrBenefit.getHrBenefitCategory().getBenefitCatId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	@Deprecated
	public boolean getEmployeeIsProviderBool() {
		return hrBenefit.deprecatedGetEmployeeIsProvider() == 'Y';
	}

	public String getInternalId() {
		return hrBenefit.getInternalId();
	}

	public void setInternalId(String internalId) {
		hrBenefit.setInternalId(internalId);
	}

	public String getLispReference() {
		return hrBenefit.getLispReference();
	}

	public void setLispReference(String lispReference) {
		hrBenefit.setLispReference(lispReference);
	}

	public String getLispPackageName() {
		String lispReference = getLispReference();
		if (StringUtils.isEmpty(lispReference))
			return "";
		String[] lispSplit = lispReference.split(":");

		if (lispSplit.length == 3)
			return lispSplit[1];
		else
			return lispSplit[0];
	}

	public String getLispFileName() {
		String lispReference = getLispReference();
		if (StringUtils.isEmpty(lispReference))
			return "";
		String[] lispSplit = lispReference.split(":");

		return lispSplit[0];
	}

	public String getLispMethodName() {
		String lispReference = getLispReference();
		if (StringUtils.isEmpty(lispReference))
			return "";
		String[] lispSplit = lispReference.split(":");

		if (lispSplit.length == 3)
			return lispSplit[2];
		else
			return lispSplit[1];
	}

	/**
	 * @return
	 */
	public boolean getActiveBool() {
		return getActive().equals("Y");
	}

	/**
	 * @return
	 */
	public boolean getHasBeneficiariesBool() {
		return hrBenefit.getHasBeneficiaries() == 'Y';
	}

	/**
	 * @return boolean
	 */
	public boolean getHasContingentBeneficiariesBool() {
		return hrBenefit.getContingentBeneficiaries() == 'Y';
	}

	/**
	 * @param startDate
	 */
	public void setStartDate(final int startDate) {
		hrBenefit.setStartDate(startDate);
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(final int endDate) {
		hrBenefit.setEndDate(endDate);
	}

	/**
	 * @param plan
	 */
	public void setPlan(final String plan) {
		hrBenefit.setPlanId(plan);
	}

	/**
	 * @return
	 */
	public String getPlan() {
		return hrBenefit.getPlanId();
	}

	/**
	 * @return
	 */
	public int getStartDate() {
		return hrBenefit.getStartDate();
	}

	/**
	 * @return
	 */
	public int getEndDate() {
		return hrBenefit.getEndDate();
	}

	public char getCostCalcType() {
		return hrBenefit.getCostCalcType();
	}

	public String getCostCalcTypeString() {
		return hrBenefit.getCostCalcType() + "";
	}

	public void setCostCalcType(char costCalcType) {
		hrBenefit.setCostCalcType(costCalcType);
	}

	/*
	 * @param employeeId
	 * @param configId
	 * @return
	 * @throws ArahantException
	 *
	 * public BHRBenefitConfig[] listValidConfigs(final String employeeId, final
	 * String configId) throws ArahantException {
	 *
	 * final BEmployee bemp=new BEmployee(employeeId);
	 *
	 * //	get all that cover employee final
	 * HibernateCriteriaUtil<HrBenefitConfig>hcu=hsu.createCriteria(HrBenefitConfig.class);
	 * hcu.joinTo(HrBenefitConfig.HR_BENEFIT) .orderBy(HrBenefit.NAME)
	 * .le(HrBenefit.START_DATE, DateUtils.now()) .eq(HrBenefit.END_DATE, 0);
	 *
	 *
	 * hcu.distinct();
	 *
	 * boolean hasSpouse=bemp.getSpouse()!=null;
	 *
	 * boolean hasChildren=bemp.getChildren().size()>0;
	 *
	 * if (!hasSpouse && !hasChildren) { hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE,
	 * 'Y'); hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N');
	 * hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N');
	 * hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N');
	 * hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N');
	 * hcu.eq(HrBenefitConfig.COVERS_CHILDREN, 'N');
	 *
	 * }
	 *
	 * if (hasSpouse && !hasChildren) { hcu.eq(HrBenefitConfig.COVERS_CHILDREN,
	 * 'N'); hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N');
	 * hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N'); }
	 *
	 * final List <HrBenefitConfig> l=hcu.list();
	 *
	 * if (!isEmpty(configId)) { final HrBenefitConfig
	 * conf=hsu.get(HrBenefitConfig.class, configId);
	 *
	 * if (!l.contains(conf)) l.add(conf); }
	 *
	 * return BHRBenefitConfig.makeArray(hcu.list()); }
	 */
	public void setRequiresDecline(final boolean requiresDecline) {
		hrBenefit.setRequiresDecline(requiresDecline ? 'Y' : 'N');
	}

	public boolean getRequiresDecline() {
		return hrBenefit.getRequiresDecline() == 'Y';
	}

	public static BHRBenefitConfig[] listValidConfigs(String employeeId, short type, int effectiveDate) throws ArahantException {
		final BEmployee bemp = new BEmployee(employeeId);
		final HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT).orderBy(HrBenefit.NAME).dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, effectiveDate).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);

		BHRBenefitConfig[] configs = listConfigs(hcu, bemp, "", effectiveDate);

		List<BHRBenefitConfig> ret = new LinkedList<BHRBenefitConfig>();
		ret.addAll(Arrays.asList(configs));

		return ret.toArray(new BHRBenefitConfig[ret.size()]);
	}

	public BHRBenefitConfig[] listConfigs(String[] excludeIds, int cap) {
		HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).orderBy(HrBenefitConfig.SEQ).notIn(HrBenefitConfig.BENEFIT_CONFIG_ID, excludeIds).eq(HrBenefitConfig.HR_BENEFIT, getBean()).setMaxResults(cap);
		return BHRBenefitConfig.makeArray(hcu.list());
	}

	/**
	 * @param hcu
	 * @return
	 * @throws ArahantException
	 */
	private static BHRBenefitConfig[] listConfigs(HibernateCriteriaUtil<HrBenefitConfig> hcu, BPerson bper, String configId, int effectiveDate) throws ArahantException {
		hcu.distinct();

		boolean hasSpouse = false;
		boolean hasChildren = false;
		boolean spouseIsEmp = false;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bper.isEmployee()) {
			BEmployee bemp = new BEmployee(bper);
			hasSpouse = bemp.getActiveSpouse(effectiveDate) != null;

			if (hasSpouse) {
				Employee emp = hsu.get(Employee.class, bemp.getSpouse().getPerson().getPersonId());

				if (emp != null) {
					BEmployee spEmp = new BEmployee(emp);
					if (spEmp.isActive() < 1)
						spouseIsEmp = true;
				}
			}

			if (!hasSpouse)
				hasSpouse = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RELATIONSHIP_TYPE, HrEmplDependent.TYPE_SPOUSE).eq(HrEmplDependent.EMPLOYEE, bemp.getEmployee()).exists();

			hasChildren = bemp.getActiveChildren(effectiveDate) > 0;
			if (!hasChildren)
				hasChildren = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RELATIONSHIP_TYPE, HrEmplDependent.TYPE_CHILD).eq(HrEmplDependent.EMPLOYEE, bemp.getEmployee()).exists();
		}
		
		/*
		 * There is inconsistent interpretation about what the employee/spouse/children flags mean.
		 * Sometimes they are interpreted to mean the must have ALL of those specified (spouse/children/etc.),
		 * other times it is interpreted to mean ANY of them.
		 * 
		 */

		if (!hasSpouse && !hasChildren) {
			hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y');
			hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N');
			hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N');
			hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N');
			hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N');
			hcu.eq(HrBenefitConfig.COVERS_CHILDREN, 'N');

		}

		if (hasSpouse && !hasChildren)
			hcu.eq(HrBenefitConfig.COVERS_CHILDREN, 'N'); //	hcu.eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N');
		//	hcu.eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N');


		final List<HrBenefitConfig> l2 = hcu.list();


		List<HrBenefitConfig> l = new LinkedList<HrBenefitConfig>();

		for (HrBenefitConfig conf : l2) {

			if (!conf.getAvailable())
				continue;

			if (!hasSpouse && conf.getSpouseDeclinesOutside() == 'Y')
				continue;
			
			//  See comment about the flags above
			if (conf.getEmployee() == 'N') {
				if (!hasSpouse  && !hasChildren)
					continue;
				if (hasSpouse  && !hasChildren  &&  conf.getSpouseEmpOrChildren() == 'N'  &&  conf.getSpouseEmployee() == 'N'  &&
						conf.getSpouseNonEmployee() == 'N'  &&  conf.getSpouseNonEmpOrChildren() == 'N')
					continue;
				if (hasChildren  &&  !hasSpouse  &&  conf.getChildren() == 'N'  &&  conf.getSpouseEmpOrChildren() == 'N'  &&
						conf.getSpouseNonEmpOrChildren() == 'N')
					continue;
				if (conf.getSpouseEmpOrChildren() == 'N'  &&  conf.getSpouseEmployee() == 'N'  &&  conf.getChildren() == 'N'  &&
						conf.getSpouseNonEmployee() == 'N'  &&  conf.getSpouseNonEmpOrChildren() == 'N')
					continue;
			}

			if (hasSpouse && !hasChildren && conf.getSpouseEmpOrChildren() == 'Y' && conf.getMaxChildren() != 1) {
				boolean skip = false;
				for (HrBenefitConfig c2 : l2) {
					if (!c2.getHrBenefit().getBenefitId().equals(conf.getHrBenefit().getBenefitId()))
						continue;

					/*
					 * Handle this on insert, they may not be planning on
					 * enrolling the spouse if (c2.getSpouseEmployee()=='Y') {
					 * skip=true; break; }
					 */
					if (c2.getMaxChildren() == 1 && conf.getMaxChildren() == 0) {
						skip = true;
						break;
					}
				}

				if (skip)
					continue;
			}

			if (hasSpouse && !hasChildren && conf.getSpouseNonEmpOrChildren() == 'Y' && conf.getMaxChildren() != 1) {
				boolean skip = false;
				for (HrBenefitConfig c2 : l2) {
					if (!c2.getHrBenefit().getBenefitId().equals(conf.getHrBenefit().getBenefitId()))
						continue;

					if (c2.getSpouseNonEmployee() == 'Y') {
						skip = true;
						break;
					}
					if (c2.getMaxChildren() == 1 && conf.getMaxChildren() == 0) {
						skip = true;
						break;
					}
				}

				if (skip)
					continue;
			}


			if (!hasSpouse && hasChildren)
				if (conf.getChildren() == 'N' && conf.getSpouseEmpOrChildren() == 'N' && conf.getSpouseNonEmpOrChildren() == 'N'
						&& (conf.getSpouseEmployee() == 'Y' || conf.getSpouseNonEmployee() == 'Y'))
					continue;
			if (spouseIsEmp) {
				if (conf.getSpouseEmployee() == 'N' && conf.getSpouseNonEmployee() == 'Y')
					continue;
				/*
				 * Handle this on insert, they may not be planning on enrolling
				 * spouse if (conf.getSpouseEmployee()=='N' &&
				 * conf.getSpouseEmpOrChildren()=='N' &&
				 * conf.getSpouseNonEmpOrChildren()=='Y') continue;
				 */
			} else {
				if (conf.getSpouseEmployee() == 'Y' && conf.getSpouseNonEmployee() == 'N')
					continue;
				if (conf.getSpouseNonEmployee() == 'N' && conf.getSpouseEmpOrChildren() == 'Y' && conf.getSpouseNonEmpOrChildren() == 'N')
					continue;
			}

			l.add(conf);
		}

		if (!isEmpty(configId)) {
			final HrBenefitConfig conf = hsu.get(HrBenefitConfig.class, configId);

			if (!l.contains(conf))
				l.add(conf);
		}

		return BHRBenefitConfig.makeArray(l);
	}

	/**
	 * @param employeeId
	 * @param configId
	 * @param benefitId
	 * @return
	 * @throws ArahantException
	 */
	public BHRBenefitConfig[] listValidConfigs(String employeeId, String configId, String benefitId) throws ArahantException {
		final BPerson bemp = new BPerson(employeeId);

		//		get all that cover employee
		final HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);
		
		
//		HibernateCriteriaUtil<HrBenefitConfig> hcu2 = hcu.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, DateUtils.now())
//				.joinTo(HrBenefitConfig.HR_BENEFIT)
//				.eq(HrBenefit.BENEFITID, benefitId)
//				.orderBy(HrBenefit.NAME)
//				.dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, DateUtils.now());
		
		//  Benefit wizard must allow enrollment in future applicable benefits (liek during open enrollment)
		HibernateCriteriaUtil<HrBenefitConfig> hcu2 = hcu.gtOrEq(HrBenefitConfig.END_DATE, DateUtils.now(), 0)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID, benefitId)
				.orderBy(HrBenefit.NAME)
				.gtOrEq(HrBenefit.END_DATE, DateUtils.now(), 0)
				.gtOrEq(HrBenefit.LAST_ENROLLMENT_DATE, DateUtils.now(), 0);

		if (!bemp.isEmployee())
			hcu2.eq(HrBenefit.COVERED_UNDER_COBRA, 'Y');
		else //check benefit classes
		if (bemp.getBEmployee().employee.getBenefitClass() != null) {
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.makeCriteria();

			HibernateCriteriaUtil<HrBenefitConfig> classHcu = hcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bemp.getBEmployee().employee.getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}
		return listConfigs(hcu, bemp, configId, DateUtils.now());
	}

	/**
	 * @param additionalInstructions
	 */
	public void setAdditionalInstructions(String additionalInstructions) {
		hrBenefit.setInstructions(additionalInstructions);
	}

	public String getAdditionalInstructions() {
		return hrBenefit.getInstructions();
	}

	/**
	 * @param employeeChoosesAmount
	 */
	@Deprecated
	public void setEmployeeChoosesAmount(boolean employeeChoosesAmount) {
		logger.deprecated();
	}

	@Deprecated
	public boolean getEmployeeChoosesAmount() {
		logger.deprecated();
		return true;
	}

	public String getProductServiceId() {
		if (hrBenefit.getProductService() != null)
			return hrBenefit.getProductService().getProductId();
		return "";
	}

	public String getProductServiceName() {
		if (hrBenefit.getProductService() != null)
			return hrBenefit.getProductService().getDescription();
		return "";
	}

	public void setProductServiceId(final String productId) {
		hrBenefit.setProductService(ArahantSession.getHSU().get(ProductService.class, productId));
	}

	public double getMinPay() {
		return hrBenefit.getMinPay();
	}

	public void setMinPay(double minPay) {
		hrBenefit.setMinPay(minPay);
	}

	public double getMaxPay() {
		return hrBenefit.getMaxPay();
	}

	public void setMaxPay(double maxPay) {
		hrBenefit.setMaxPay(maxPay);
	}

	public float getMinHoursPerWeek() {
		return hrBenefit.getMinHoursPerWeek();
	}

	public void setMinHoursPerWeek(float minHoursPerWeek) {
		hrBenefit.setMinHoursPerWeek(minHoursPerWeek);
	}

	/**
	 * @param employeeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static BHRBenefit[] listBenefitsWithHistory(String employeeId) {

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		List<HrBenefitJoinH> benefitConfigIds = hsu.createCriteria(HrBenefitJoinH.class).selectFields(HrBenefitJoinH.BENEFIT_CONFIG_ID).distinct().eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId).list();

		HibernateCriteriaUtil hcu = hsu.createCriteria(HrBenefitJoin.class);
		hcu.selectFields(HrBenefitJoin.HR_BENEFIT_CONFIG_ID);
		hcu.distinct();
		hcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Employee.PERSONID, employeeId);

		benefitConfigIds.addAll(hcu.list());


		// add current stuff too

		List<HrBenefit> l = hsu.createCriteria(HrBenefit.class).distinct().orderBy(HrBenefit.NAME).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).in(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigIds).list();


		List<HrBenefitJoinH> benefitIds = hsu.createCriteria(HrBenefitJoinH.class).selectFields(HrBenefitJoinH.BENEFIT_ID).distinct().eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId).list();

		hcu = hsu.createCriteria(HrBenefitJoin.class);
		hcu.selectFields(HrBenefitJoin.HR_BENEFIT_ID);
		hcu.distinct();
		hcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Employee.PERSONID, employeeId);

		benefitIds.addAll(hcu.list());

		l.addAll(hsu.createCriteria(HrBenefit.class).distinct().in(HrBenefit.BENEFITID, benefitIds).list());


		//remove duplicates

		Set<HrBenefit> bset = new HashSet<HrBenefit>(l.size());

		for (HrBenefit b : l)
			if (b.getHasBeneficiaries() == 'Y')
				bset.add(b);
		l.clear();

		l.addAll(bset);

		return makeArray(l);
	}

	public void setCoveredUnderCOBRA(boolean coveredUnderCOBRA) {
		hrBenefit.setCoveredUnderCOBRA(coveredUnderCOBRA ? 'Y' : 'N');
	}

	public boolean getCoveredUnderCOBRA() {
		return hrBenefit.getCoveredUnderCOBRA() == 'Y';
	}

	public String getDescription() {
		return hrBenefit.getDescription();
	}

	public void setDescription(String description) {
		hrBenefit.setDescription(description);
	}

	public String getProcessType() {
		return hrBenefit.getProcessType() + "";
	}

	public void setProcessType(String processType) {
		hrBenefit.setProcessType(processType.charAt(0));
	}
	
	public char getEmployerCostModel() {
		return hrBenefit.getEmployerCostModel();
	}

	public void setEmployerCostModel(char employerCostModel) {
		hrBenefit.setEmployerCostModel(employerCostModel);
	}

	public char getEmployeeCostModel() {
		return hrBenefit.getEmployeeCostModel();
	}

	public void setEmployeeCostModel(char employeeCostModel) {
		hrBenefit.setEmployeeCostModel(employeeCostModel);
	}

	public char getBenefitAmountModel() {
		return hrBenefit.getBenefitAmountModel();
	}

	public void setBenefitAmountModel(char benefitAmountModel) {
		hrBenefit.setBenefitAmountModel(benefitAmountModel);
	}

	public static String getMissingCostReport(String catId, String benId) {
		return new MissingBenefitCostReport().build(catId, benId);
	}

	public static BHRBenefit[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).list());
	}

	public static BHRBenefit[] listByCategory() {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);
		hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefitCategory.DESCRIPTION);
		hcu.orderBy(HrBenefit.NAME);

		return makeArray(hcu.list());
	}

	public boolean getHasMultipleConfigs() {
		try {
			return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, hrBenefit).count() > 1;

			/*
			 *
			 * if (hsu.createCriteria(HrBenefitConfig.class)
			 * .eq(HrBenefitConfig.HR_BENEFIT,hrBenefit)
			 * .eq(HrBenefitConfig.COVERS_CHILDREN,'Y') .exists()) return true;
			 * if (hsu.createCriteria(HrBenefitConfig.class)
			 * .eq(HrBenefitConfig.HR_BENEFIT,hrBenefit)
			 * .eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE,'Y') .exists()) return
			 * true; if (hsu.createCriteria(HrBenefitConfig.class)
			 * .eq(HrBenefitConfig.HR_BENEFIT,hrBenefit)
			 * .eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE,'Y') .exists()) return
			 * true; if (hsu.createCriteria(HrBenefitConfig.class)
			 * .eq(HrBenefitConfig.HR_BENEFIT,hrBenefit)
			 * .eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN,'Y')
			 * .exists()) return true; if
			 * (hsu.createCriteria(HrBenefitConfig.class)
			 * .eq(HrBenefitConfig.HR_BENEFIT,hrBenefit)
			 * .eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN,'Y')
			 * .exists()) return true;
			 */
		} catch (Exception e) {
			logger.error(e);
		}
		return false;

	}

	public String deepCopy(String name) {
		BHRBenefit bene = new BHRBenefit();
		String ret = bene.create();
		HibernateSessionUtil.copyCorresponding(bene.hrBenefit, hrBenefit, HrBenefit.BENEFITID);
		bene.setName(name);
		bene.insert();

		for (HrBenefitConfig c : hrBenefit.getBenefitConfigs()) {
			BHRBenefitConfig config = new BHRBenefitConfig();
			String cid = config.create();
			HibernateSessionUtil.copyCorresponding(config.bean, c, HrBenefitConfig.BENEFIT_CONFIG_ID);
			config.setBenefitId(ret);
			config.insert();
		}

		for (BenefitRider r : hrBenefit.getRidersOnBaseBenefit()) {
			BBenefitRider rider = new BBenefitRider(r);
			String rId = rider.create();
			HibernateSessionUtil.copyCorresponding(rider.bean, r, BenefitRider.BENEFIT_RIDER_ID);
			rider.setBaseBenefitId(ret);
			rider.insert();
		}

		return ret;
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void moveUp() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hrBenefit.getSequence() > 0) {
			HrBenefit bc = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, hrBenefit.getHrBenefitCategory()).eq(HrBenefit.SEQ, (short) (hrBenefit.getSequence() - 1)).first();

			short temp = hrBenefit.getSequence();
			bc.setSequence((short) 99999);
			hsu.saveOrUpdate(bc);
			hsu.flush();
			hrBenefit.setSequence((short) (hrBenefit.getSequence() - 1));
			hsu.saveOrUpdate(hrBenefit);
			hsu.flush();
			bc.setSequence(temp);
			hsu.saveOrUpdate(bc);
		} else { //shift them all
			List<HrBenefit> l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, hrBenefit.getHrBenefitCategory()).orderBy(HrBenefit.SEQ).list();

			l.get(0).setSequence((short) 99999);
			hsu.saveOrUpdate(l.get(0));
			hsu.flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSequence((short) (l.get(loop).getSequence() - 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(0).setSequence((short) (l.size() - 1));
			hsu.saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hrBenefit.getSequence() != hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, hrBenefit.getHrBenefitCategory()).count() - 1) {
			HrBenefit bc = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.SEQ, (short) (hrBenefit.getSequence() + 1)).first();

			short temp = hrBenefit.getSequence();
			bc.setSequence((short) 999999);
			hsu.saveOrUpdate(bc);
			hsu.flush();
			hrBenefit.setSequence((short) (hrBenefit.getSequence() + 1));
			hsu.saveOrUpdate(hrBenefit);
			bc.setSequence(temp);
			hsu.saveOrUpdate(bc);
		} else //shift them all
		{
			List<HrBenefit> l = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, hrBenefit.getHrBenefitCategory()).orderBy(HrBenefit.SEQ).list();

			l.get(l.size() - 1).setSequence((short) 99999);
			hsu.saveOrUpdate(l.get(l.size() - 1));
			hsu.flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setSequence((short) (loop + 1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}

			l.get(l.size() - 1).setSequence((short) 0);
			hsu.saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public String getAvatarPath() {
		return hrBenefit.getAvatarPath();
	}

	public void setAvatarPath(String avatarPath) {
		hrBenefit.setAvatarPath(avatarPath);
	}

	public Set<HrBenefit> getBaseBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_BASE_BENEFIT).eq(BenefitRider.RIDER_BENEFIT, hrBenefit).set();
	}

	public Set<HrBenefit> getRiderBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_RIDER_BENEFIT).eq(BenefitRider.BASE_BENEFIT, hrBenefit).set();
	}

	public Set<BenefitRider> getBenefitRiders() {
		return ArahantSession.getHSU().createCriteria(BenefitRider.class).eq(BenefitRider.BASE_BENEFIT, hrBenefit).set();
	}

	public Set<BenefitRider> getRequiredHiddenBenefitRiders() {
		return ArahantSession.getHSU().createCriteria(BenefitRider.class).eq(BenefitRider.BASE_BENEFIT, hrBenefit).eq(BenefitRider.REQUIRED, 'Y').eq(BenefitRider.HIDDEN, 'Y').set();
	}

	public List<HrBenefit> getRequiredRiderBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_RIDER_BENEFIT).eq(BenefitRider.BASE_BENEFIT, hrBenefit).eq(BenefitRider.REQUIRED, 'Y').list();
	}

	public List<HrBenefit> getRequiredHiddenRiderBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_RIDER_BENEFIT).eq(BenefitRider.BASE_BENEFIT, hrBenefit).eq(BenefitRider.REQUIRED, 'Y').eq(BenefitRider.HIDDEN, 'Y').list();
	}

	public Set<HrBenefit> getOptionalRiderBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.RIDERS_ON_RIDER_BENEFIT).eq(BenefitRider.BASE_BENEFIT, hrBenefit).eq(BenefitRider.REQUIRED, 'N').set();
	}

	@SuppressWarnings("unchecked")
	public Set<HrBenefit> getDependentBenefits() {
		return (Set) ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, hrBenefit).selectFields((BenefitDependency.REQUIRED_BENEFIT)).set();
	}

	public String getOpenEnrollmentWizard() {
		return hrBenefit.getOpenEnrollmentWizard() + "";
	}

	public void setOpenEnrollmentWizard(String openEnrollmentWizard) {
		hrBenefit.setOpenEnrollmentWizard(openEnrollmentWizard.charAt(0));
	}

	public String getOnboarding() {
		return hrBenefit.getOnboarding() + "";
	}

	public void setOnboarding(String onboarding) {
		hrBenefit.setOnboarding(onboarding.charAt(0));
	}

	public void associateBenefitClass(String benefitClassId) {
		hrBenefit.getBenefitClasses().add(ArahantSession.getHSU().get(BenefitClass.class, benefitClassId));
	}

	public void disassociateBenefitClass(String benefitClassId) {
		hrBenefit.getBenefitClasses().remove(ArahantSession.getHSU().get(BenefitClass.class, benefitClassId));
	}

	public Screen getOpenEnrollmentScreen() {
		return hrBenefit.getOpenEnrollmentScreen();
	}

	public void setOpenEnrollmentScreen(Screen openEnrollmentScreen) {
		hrBenefit.setOpenEnrollmentScreen(openEnrollmentScreen);
	}

	public String getAvatarLocation() {
		return hrBenefit.getAvatarLocation();
	}

	public void setAvatarLocation(String avatarLocation) {
		hrBenefit.setAvatarLocation(avatarLocation);
	}

	public Screen getOnboardingScreen() {
		return hrBenefit.getOnboardingScreen();
	}

	public void setOnboardingScreen(Screen onboardingScreen) {
		hrBenefit.setOnboardingScreen(onboardingScreen);
	}

	public HrBenefit getReplacingBenefit() {
		return hrBenefit.getReplacingBenefit();
	}

	public void setReplacingBenefit(HrBenefit replacingBenefit) {
		hrBenefit.setReplacingBenefit(replacingBenefit);
	}

	public Set<HrBenefit> getReplacedBenefits() {
		return hrBenefit.getReplacedBenefits();
	}

	public void setReplacedBenefits(Set<HrBenefit> replacedBenefits) {
		hrBenefit.setReplacedBenefits(replacedBenefits);
	}

	public int getLastEnrollmentDate() {
		return hrBenefit.getLastEnrollmentDate();
	}

	public void setLastEnrollmentDate(int lastEnrollmentDate) {
		hrBenefit.setLastEnrollmentDate(lastEnrollmentDate);
	}

	public static BHRBenefit getReplacingBenefit(String benefitId) {
		HrBenefit b = ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.REPLACED_BENEFITS).eq(HrBenefit.BENEFITID, benefitId).first();

		if (b == null)
			return null;

		return new BHRBenefit(b);
	}

	public static BHRBenefit[] getBenefitsNotBeingReplaced(String excludeId, String categoryId) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class).ne(HrBenefit.BENEFITID, excludeId).eq(HrBenefit.TIMERELATED, 'N').gt(HrBenefit.END_DATE, 0).isNull(HrBenefit.REPLACING_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list());
	}

	public static BHRBenefit[] searchBenefits(String benefitId, String categoryId, String benefitName, int cap) {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).setMaxResults(cap);

		if (!isEmpty(benefitId))
			hcu.ne(HrBenefit.BENEFITID, benefitId);

		hcu.ne(HrBenefit.REPLACING_BENEFIT, new BHRBenefit(benefitId).getBean());

		hcu.like(HrBenefit.NAME, benefitName);

		if (!isEmpty(categoryId))
			hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return makeArray(hcu.list());
	}

	@SuppressWarnings("unchecked")
	public static BHRBenefit[] searchBenefits(String benefitId, String benefitName, int cap) {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).setMaxResults(cap);

		if (!isEmpty(benefitId)) {
			List<String> notInBenefits = new ArrayList<String>();
			notInBenefits.add(benefitId);
			notInBenefits.addAll((List) ArahantSession.getHSU().createCriteria(BenefitRider.class).selectFields(BenefitRider.RIDER_BENEFIT_ID).eq(BenefitRider.BASE_BENEFIT_ID, benefitId).list());
			hcu.notIn(HrBenefit.BENEFITID, notInBenefits);
		}

		hcu.like(HrBenefit.NAME, benefitName);

		return makeArray(hcu.list());
	}

	public static void clearReplacingBenefits(String benefitId) {
		for (HrBenefit hb : ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.REPLACING_BENEFIT, new BHRBenefit(benefitId).getBean()).list()) {
			BHRBenefit bhb = new BHRBenefit(hb);
			bhb.setReplacingBenefit(null);
			bhb.update();
		}
	}

	public static String findOrMake(String name, String categoryId, String wageTypeId) {
		HrBenefit bene = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.NAME, name).eq(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(categoryId).getBean()).first();

		if (bene != null)
			return bene.getBenefitId();

		BHRBenefit benefit = new BHRBenefit();
		String ret = benefit.create();
		benefit.setBenefitCategoryId(categoryId);
		benefit.setName(name);
		benefit.setWageTypeId(wageTypeId);
		benefit.insert();

		return ret;
	}

	public static String findOrMakeCASBenefit(String name, String categoryId, String wageTypeId, String groupId) {
		HrBenefit bene;
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.NAME, name).eq(HrBenefit.BENEFIT_CATEGORY, new BHRBenefitCategory(categoryId).getBean());
		if (isEmpty(groupId))
			bene = hcu.first();
		else
			bene = hcu.like(HrBenefit.GROUPID, "MM-" + groupId).first();

		if (bene != null)
			return bene.getBenefitId();

		BHRBenefit benefit = new BHRBenefit();
		String ret = benefit.create();
		benefit.setBenefitCategoryId(categoryId);
		benefit.setName(name);
		benefit.setWageTypeId(wageTypeId);
		benefit.setGroupId("MM-" + groupId);
		benefit.insert();
		System.out.println("New Benefit Created: " + name + " - MM-" + groupId);

		return ret;
	}

	public boolean activeConfigsCoverEmployeeOnly() {
		boolean ret = true;
		for (HrBenefitConfig config : getConfigs()) {
			BHRBenefitConfig bc = new BHRBenefitConfig(config);
			if (bc.getCoversEmployee()
					&& !bc.getCoversChildren()
					&& !bc.getCoversEmployeeSpouse()
					&& !bc.getCoversEmployeeSpouseOrChildren()
					&& !bc.getSpouseNonEmpOrChildren()
					&& !bc.getSpouseNonEmployee())
				continue;
			else {
				ret = false;
				break;
			}
		}
		return ret;
	}

	public String getEdiFieldValue1() {
		return hrBenefit.getEdiFieldValue1();
	}

	public void setEdiFieldValue1(String ediFieldValue1) {
		hrBenefit.setEdiFieldValue1(ediFieldValue1);
	}

	public String getEdiFieldValue2() {
		return hrBenefit.getEdiFieldValue2();
	}

	public void setEdiFieldValue2(String ediFieldValue2) {
		hrBenefit.setEdiFieldValue2(ediFieldValue2);
	}

	public String getEdiFieldValue3() {
		return hrBenefit.getEdiFieldValue3();
	}

	public void setEdiFieldValue3(String ediFieldValue3) {
		hrBenefit.setEdiFieldValue3(ediFieldValue3);
	}

	public String getEdiFieldValue4() {
		return hrBenefit.getEdiFieldValue4();
	}

	public void setEdiFieldValue4(String ediFieldValue4) {
		hrBenefit.setEdiFieldValue4(ediFieldValue4);
	}

	public String getEdiFieldValue5() {
		return hrBenefit.getEdiFieldValue5();
	}

	public void setEdiFieldValue5(String ediFieldValue5) {
		hrBenefit.setEdiFieldValue5(ediFieldValue5);
	}

	public String getEdiFieldDescription1() {
		return hrBenefit.getEdiFieldDescription1();
	}

	public void setEdiFieldDescription1(String ediFieldDescription1) {
		hrBenefit.setEdiFieldDescription1(ediFieldDescription1);
	}

	public String getEdiFieldDescription2() {
		return hrBenefit.getEdiFieldDescription2();
	}

	public void setEdiFieldDescription2(String ediFieldDescription2) {
		hrBenefit.setEdiFieldDescription2(ediFieldDescription2);
	}

	public String getEdiFieldDescription3() {
		return hrBenefit.getEdiFieldDescription3();
	}

	public void setEdiFieldDescription3(String ediFieldDescription3) {
		hrBenefit.setEdiFieldDescription3(ediFieldDescription3);
	}

	public String getEdiFieldDescription4() {
		return hrBenefit.getEdiFieldDescription4();
	}

	public void setEdiFieldDescription4(String ediFieldDescription4) {
		hrBenefit.setEdiFieldDescription4(ediFieldDescription4);
	}

	public String getEdiFieldDescription5() {
		return hrBenefit.getEdiFieldDescription5();
	}

	public void setEdiFieldDescription5(String ediFieldDescription5) {
		hrBenefit.setEdiFieldDescription5(ediFieldDescription5);
	}

	public void setHasPhysicians(Boolean hasPhysicians) {
		hrBenefit.setHasPhysicians(hasPhysicians ? 'Y' : 'N');
	}

	public Boolean getHasPhysicians() {
		return hrBenefit.getHasPhysicians() == 'Y';
	}

	public List<BenefitDocument> getRequiredDocuments() {
		return ArahantSession.getHSU().createCriteria(BenefitDocument.class).eq(BenefitDocument.BENEFIT, getBean()).list();
	}

	public char getAutoAssign() {
		return hrBenefit.getAutoAssign();
	}

	public void setAutoAssign(char autoAssign) {
		hrBenefit.setAutoAssign(autoAssign);
	}

	public boolean getAutoAssignBoolean() {
		return hrBenefit.getAutoAssign() == 'Y';
	}

	public void setAutoAssignBoolean(Boolean a) {
		if (a)
			hrBenefit.setAutoAssign('Y');
		else
			hrBenefit.setAutoAssign('N');
	}

	public void setAutoAssign(String autoAssign) {
		hrBenefit.setAutoAssign(autoAssign.charAt(0));
	}

	public HrBenefitConfig getCorrespondingBenefitConfig(HrBenefitConfig hrBenefitConfig) {
		HrBenefitConfig match = null;
		List<HrBenefitConfig> hrbcl = this.getConfigs();
		if (hrbcl.size() == 1)
			match = hrbcl.get(0); //if there is only one option, return it (probably employee only)
		else
			for (HrBenefitConfig newConfig : hrbcl)
				if (hrBenefitConfig.getEmployee() == newConfig.getEmployee() && hrBenefitConfig.getChildren() == newConfig.getChildren() && hrBenefitConfig.getSpouseEmployee() == newConfig.getSpouseEmployee() && hrBenefitConfig.getSpouseNonEmployee() == newConfig.getSpouseNonEmployee() && hrBenefitConfig.getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren() && hrBenefitConfig.getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren() && hrBenefitConfig.getMaxChildren() == newConfig.getMaxChildren()) {
					match = newConfig;
					break;
				}
		return match;
	}

	public BenefitRider getRiderEnrollment(Person payingPerson) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (BenefitRider br : hsu.createCriteria(BenefitRider.class).eq(BenefitRider.RIDER_BENEFIT_ID, getBenefitId()).list())
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).exists())
				return br;
			//is there an approved enrollment and no unapproved decline?
			else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getBaseBenefit()).exists()
					&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, br.getBaseBenefit()).exists())
				return br;
		return null;
	}

	public BenefitDependency getDependencyEnrollment(Person payingPerson) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (BenefitDependency br : hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT_ID, getBenefitId()).list())
			if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getRequiredBenefit()).exists())
				return br;
			//is there an approved enrollment and no unapproved decline?
			else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, br.getRequiredBenefit()).exists()
					&& !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, payingPerson).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, br.getRequiredBenefit()).exists())
				return br;
		return null;
	}

	public void adjustDependencyRequirements(String employeeId) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<BenefitDependency> bds = hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, getBean()).list();
		//for all the benefits that depend on the one we declined
		for (BenefitDependency bd : bds) {
			//check to see that they meet another one of their requirements
			List<BenefitDependency> otherRequirements = hsu.createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, bd.getDependentBenefit()).ne(BenefitDependency.REQUIRED_BENEFIT, bd.getRequiredBenefit()).list();
			boolean meetsRequirement = false;
			for (BenefitDependency bd2 : otherRequirements)
				//do they have an unapproved enrollment?
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getRequiredBenefit()).exists())
					meetsRequirement = true;
				else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getRequiredBenefit()).exists() && !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, bd2.getRequiredBenefit().getBenefitId()).exists())
					meetsRequirement = true;
			//if they don't meet their requirement,
			//delete their unapproved enrollments in those dependent benefits
			if (!meetsRequirement) {
				//enrollments
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getDependentBenefit()).delete();
				//declines
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, bd.getDependentBenefit()).delete();
			}
		}
	}

	public void adjustRiderRequirements(String employeeId) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<BenefitRider> bds = hsu.createCriteria(BenefitRider.class).eq(BenefitRider.BASE_BENEFIT, getBean()).list();
		//for all the benefits that depend on the one we declined
		for (BenefitRider bd : bds) {
			//check to see that they meet another one of their requirements
			List<BenefitRider> otherRequirements = hsu.createCriteria(BenefitRider.class).eq(BenefitRider.RIDER_BENEFIT, bd.getRiderBenefit()).ne(BenefitRider.BASE_BENEFIT, bd.getBaseBenefit()).list();
			boolean meetsRequirement = false;
			for (BenefitRider bd2 : otherRequirements)
				//do they have an unapproved enrollment?
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getBaseBenefit()).exists())
					meetsRequirement = true;
				else if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd2.getBaseBenefit()).exists() && !hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, bd2.getBaseBenefit().getBenefitId()).exists())
					meetsRequirement = true;
			//if they don't meet their requirement,
			//delete their unapproved enrollments in those dependent benefits
			if (!meetsRequirement) {
				//enrollments
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRiderBenefit()).delete();
				//declines
				hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, bd.getRiderBenefit()).delete();
			}
		}
	}
}
