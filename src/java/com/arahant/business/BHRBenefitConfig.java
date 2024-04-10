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
import com.arahant.reports.HRBenefitsUsingCOBRAReport;
import com.arahant.reports.HRUnapprovedBenefitsReport;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jess.Fact;
import jess.JessException;

public class BHRBenefitConfig extends SimpleBusinessObjectBase<HrBenefitConfig> implements IDBFunctions {
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BHRBenefitConfig.class);

	public static BHRBenefitConfig[] list(String benefitId, String[] excludeConfigIds) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.notIn(HrBenefitConfig.BENEFIT_CONFIG_ID, excludeConfigIds)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID, benefitId)
				.list());
	}

	public static BHRBenefitConfig[] list(int asOfDate) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, asOfDate)
				.list());
	}

	public static BHRBenefitConfig[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.list());
	}

	public static BHRBenefitConfig[] listTimeRelated() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.TIMERELATED, 'Y')
				.list());
	}

	public BHRBenefitConfig() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHRBenefitConfig(final String key) throws ArahantException {
		super(key);
	}

	/**
	 * @param account
	 */
	public BHRBenefitConfig(final HrBenefitConfig account) {
		bean = account;
	}

	public void addCost(BBenefitConfigCost c) {
		addPendingInsert(c);
	}

	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitConfig();
		bean.setBenefitConfigId(IDGenerator.generate(bean));

		return getBenefitConfigId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		deleteCosts();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.createCriteria(HrBenefitProjectJoin.class)
				.eq(HrBenefitProjectJoin.HR_BENEFIT_CONFIG, bean)
				.delete();
		hsu.createCriteria(WizardConfigurationConfig.class)
				.eq(WizardConfigurationConfig.BENEFIT_CONFIG, bean)
				.delete();
		hsu.delete(bean);
	}

	public void deleteCosts() {
		for (BBenefitConfigCost cc : makeCostArray(ArahantSession.getHSU().createCriteria(BenefitConfigCost.class)
				.eq(BenefitConfigCost.BENEFIT_CONFIG, bean).list()))
			cc.delete();
	}

	public BBenefitClass[] getBenefitClasses() {
		return BBenefitClass.makeArray(bean.getBenefitClasses());
	}

	public BBenefitConfigCost[] getBenefitCosts() {
		return makeCostArray(ArahantSession.getHSU().createCriteria(BenefitConfigCost.class)
				.eq(BenefitConfigCost.BENEFIT_CONFIG, bean)
				.list());
	}

	public BenefitConfigCost getActiveBenefitCost(int date) {
		return ArahantSession.getHSU().createCriteria(BenefitConfigCost.class)
				.eq(BenefitConfigCost.BENEFIT_CONFIG, bean)
				.dateInside(BenefitConfigCost.FIRSTACTIVEDATE, BenefitConfigCost.LASTACTIVEDATE, date)
				.first();
	}

	public String getDescription() {
		return bean.getName();
	}

	public String getId() {
		return bean.getBenefitConfigId();
	}

	public boolean getIncludeInBilling() {
		return bean.getOnBilling() == 'Y';
	}

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitConfig.class, key);
	}

	public void setBenefitClasses(String[] ids) {
		bean.getBenefitClasses().clear();
		for (String id : ids)
			bean.getBenefitClasses().add(ArahantSession.getHSU().get(BenefitClass.class, id));
	}

	public void setIncludeInBilling(boolean includeInBilling) {
		bean.setOnBilling(includeInBilling ? 'Y' : 'N');
	}

	public void setInsuranceCode(String code) {
		bean.setInsuranceCode(code);
	}

	@Override
	public void update() throws ArahantException {
//		don't allow the change if people are associated
		if (changedApplicability
				&& ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.BENEFIT_CONFIG_ID, bean.getBenefitConfigId())
				.sizeNe(HrBenefitConfig.HR_BENEFIT_JOINS, 0)
				.exists())
			throw new ArahantWarning("You can not edit who a benefit applies to, after people have been assigned to it.");
		super.update();
	}
	
	private boolean changedApplicability = false;

	/**
	 * @return @see com.arahant.beans.HrBenefit#getName()
	 */
	public String getName() {
		return bean.getName();
	}

	/**
	 * @param BenefitCategoryId
	 * @see com.arahant.beans.HrBenefit#setBenefitConfigId(java.lang.String)
	 */
	public void setBenefitCategoryId(final String BenefitCategoryId) {
		bean.setBenefitConfigId(BenefitCategoryId);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrBenefit#setName(java.lang.String)
	 */
	public void setName(final String name) {
		bean.setName(name);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids) {
			if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, element).exists())
				throw new ArahantException("Unable to delete benefit configuration " + new BHRBenefitConfig(element).getConfigName() + " because there are existing enrollments.");
			{
				new BHRBenefitConfig(element).delete();
			}
		}
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRBenefitConfig[] list(final HibernateSessionUtil hsu) {
		final List<HrBenefitConfig> l = hsu.createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.list();

		final BHRBenefitConfig[] ret = new BHRBenefitConfig[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefitConfig(l.get(loop));
		return ret;
	}

	public static BHRBenefitConfig[] listNonTimeRelated(final HibernateSessionUtil hsu) {
		final List<HrBenefitConfig> l = hsu.createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.NAME)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.TIMERELATED, 'N')
				.list();

		final BHRBenefitConfig[] ret = new BHRBenefitConfig[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefitConfig(l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
//	public static String getReport(final HibernateSessionUtil hsu ) throws Exception {
//
//		final HRBenefitReport rep=new HRBenefitReport();
//		rep.build(hsu);
//
//		return rep.getFilename();
//	}

	public String getRuleName() {
		return bean.getHrBenefit().getRuleName();
	}

	public boolean getTimeRelated() {
		return bean.getHrBenefit().getTimeRelated() == 'Y';
	}

	/**
	 * @return @throws ArahantException
	 */
	public static String[] listRules() throws ArahantException {
		try {
			final JessBean jb = new JessBean();
			jb.loadScript("BusinessRules.jess");

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
	 * @param personId
	 * @return
	 */
	public boolean isValidForEmployee(final String personId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.HRBENEFIT, bean)
				.joinTo(HrBenefitJoin.COVERED_PERSON)
				.eq(Person.PERSONID, personId)
				.first() != null;
	}

	/**
	 * @param l
	 * @return
	 */
	public static BHRBenefitConfig[] makeArray(final List<HrBenefitConfig> l) {
		final BHRBenefitConfig[] ret = new BHRBenefitConfig[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitConfig(l.get(loop));
		return ret;
	}
	
	private static BBenefitConfigCost[] makeCostArray(List<BenefitConfigCost> l) {
		BBenefitConfigCost[] ret = new BBenefitConfigCost[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBenefitConfigCost(l.get(loop));
		return ret;
	}
	/**
	 * @param coversChildren
	 */
	public void setCoversChildren(final boolean coversChildren) {
		final char newVal = coversChildren ? 'Y' : 'N';
		changedApplicability = bean.getChildren() != newVal;
		bean.setChildren(newVal);
	}

	/**
	 * @param coversSpouse
	 */
	public void setSpouseNonEmployee(final boolean coversSpouse) {
		final char newVal = coversSpouse ? 'Y' : 'N';
		changedApplicability = bean.getSpouseNonEmployee() != newVal;
		bean.setSpouseNonEmployee(newVal);
	}

	/**
	 * @param coversEmployee
	 */
	public void setCoversEmployee(final boolean coversEmployee) {
		final char newVal = coversEmployee ? 'Y' : 'N';
		changedApplicability = bean.getEmployee() != newVal;
		bean.setEmployee(newVal);
	}

	/**
	 * @param employeeCost
	 */
	@Deprecated
	public void setEmployeeCost(final double employeeCost) {
		logger.deprecated();
	}

	/**
	 * @param employeeCOBRACost
	 */
	public void setEmployeeCOBRACost(final double employeeCOBRACost) {
		bean.setEmployeeCOBRACost((float) employeeCOBRACost);
	}

	/**
	 * @param employerCost
	 */
	@Deprecated
	public void setEmployerCost(final double employerCost) {
		logger.deprecated();
	}

	/**
	 * @param additionalInfo
	 */
	public void setAdditionalInfo(final String additionalInfo) {
		bean.setAddInfo(additionalInfo);
	}

	/**
	 * @param maxChildren
	 */
	public void setMaxChildren(final int maxChildren) {
		bean.setMaxChildren((short) maxChildren);
	}

	public String getGroupId() {
		return bean.getGroupId();
	}

	/**
	 * @return
	 *
	 * public String getUnderwriter() {
	 *
	 * return bean.getHrBenefit().getCarrier(); }
	 *
	 * /
	 **
	 * @return
	 */
	public String getActive() {
		return (bean.getEndDate() == 0 || bean.getEndDate() > DateUtils.now()) ? "Yes" : "No";
	}

	public boolean getCoversChildren() {
		return bean.getChildren() == 'Y';
	}

	public boolean getSpouseNonEmployee() {
		return bean.getSpouseNonEmployee() == 'Y';
	}

	public boolean getCoversEmployee() {
		return bean.getEmployee() == 'Y';
	}

	@Deprecated
	public double getEmployeeCost() {
		logger.deprecated();
		return 0;
	}

	public double getEmployeeCOBRACost() {
		return bean.getEmployeeCOBRACost();
	}

	@Deprecated
	public double getEmployerCost() {
		logger.deprecated();
		return 0;
	}

	public boolean getPreTax() {
		return bean.getHrBenefit().getPreTax() == 'Y';
	}

	public String getAdditionalInfo() {
		return bean.getAddInfo();
	}

	public boolean getApplytoSalary() {
		return bean.getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y';
	}

	public int getMaxChildren() {
		return bean.getMaxChildren();
	}

	public boolean getActiveBool() {
		return getActive().startsWith("Y");
	}

	public String getBenefitCategoryCategoryId() {
		try {
			return bean.getHrBenefit().getHrBenefitCategory().getBenefitCatId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getCategoryName() {
		try {
			return bean.getHrBenefit().getHrBenefitCategory().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	public boolean getSpouseNonEmpOrChildren() {
		return bean.getSpouseNonEmpOrChildren() == 'Y';
	}

	/**
	 * @param coversSpouseOrChildren
	 */
	public void setSpouseNonEmpOrChildren(final boolean coversSpouseOrChildren) {
		bean.setSpouseNonEmpOrChildren(coversSpouseOrChildren ? 'Y' : 'N');
	}

	/**
	 * @param benefitCategoryCategoryId
	 * @return
	 */
	public static BHRBenefitConfig[] list(final String benefitId) {
		final List<HrBenefitConfig> l = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.orderBy(HrBenefitConfig.SEQ)
				.orderBy(HrBenefitConfig.NAME)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID, benefitId)
				.list();

		final BHRBenefitConfig[] ret = new BHRBenefitConfig[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRBenefitConfig(l.get(loop));
		return ret;
	}

	public boolean getAutoAssign() {
		return bean.getAutoAssign() == 'Y';
	}

	/**
	 * @param autoAssign
	 */
	public void setAutoAssign(final boolean autoAssign) {
		bean.setAutoAssign(autoAssign ? 'Y' : 'N');
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

	public HrBenefitCategory getBenefitCategory() {
		return bean.getHrBenefitCategory();
	}

	public boolean getPaid() {
		return bean.getHrBenefit().getPaidBenefit() == 'Y';
	}

	/**
	 * @param benefitId
	 */
	public void setBenefitId(final String benefitId) {
		if (isEmpty(benefitId))
			throw new ArahantException("Empty benefit ID passed to benefit config.");
		bean.setHrBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
		if (bean.getHrBenefit() == null)
			throw new ArahantException("Requested benefit not found for benefit config.");
	}

	public boolean getSpouseDeclinesExternalCoverage() {
		return bean.getSpouseDeclinesOutside() == 'Y';
	}

	public boolean getSpouseIsEmployee() {
		return bean.getSpouseEmployee() == 'Y';
	}

	/**
	 * @param maxDependents
	 */
	public void setMaxDependents(final int maxDependents) {
		bean.setMaxChildren((short) maxDependents);
	}

	/**
	 * @param spouseDeclinesExternalCoverage
	 */
	public void setSpouseDeclinesExternalCoverage(final boolean spouseDeclinesExternalCoverage) {
		bean.setSpouseDeclinesOutside(spouseDeclinesExternalCoverage ? 'Y' : 'N');
	}

	/**
	 * @param spouseIsEmployee
	 */
	public void setSpouseEmployee(final boolean spouseIsEmployee) {
		bean.setSpouseEmployee(spouseIsEmployee ? 'Y' : 'N');
	}

	/**
	 * @param statusIds
	 *
	 * public void setStatusIds(final String[] statusIds) {
	 * bean.getStatuses().clear(); bean.getStatuses().addAll(
	 * hsu.createCriteria(HrEmployeeStatus.class) .in(HrEmployeeStatus.STATUSID,
	 * statusIds).list()); }
	 *
	 * /
	 **
	 * @return
	 */
	public String getCoverage() {
		/*
		 * Emp

		 Emp, Sp (Emp)

		 Emp, Sp (Non-Emp)

		 Emp, Sp (Non-Emp), Child (All)

		 Emp, Sp (Non-Emp) - Declines Outside Coverage, Child (All) 

		 Emp, Sp (Non-Emp), Child (2)

		 Emp, Sp (Non-Emp) - Declines Outside Coverage, Child (2)

		 Emp, Dep (2)

		 */
		String coverage = "";
		if (bean.getEmployee() == 'Y')
			coverage += "Emp, ";
		if (bean.getSpouseEmployee() == 'Y')
			coverage += "Sp (Emp), ";
		if (bean.getSpouseNonEmployee() == 'Y') {
			coverage += "Sp (Non-Emp)";
			if (bean.getSpouseDeclinesOutside() == 'Y')
				coverage += " - Declines Outside Coverage";
			coverage += ", ";
		}

		if (bean.getChildren() == 'Y') {
			coverage += "Child ";
			if (bean.getMaxChildren() == 0)
				coverage += "(All)";
			else
				coverage += "(" + bean.getMaxChildren() + ")";
			coverage += ", ";
		}

		if (bean.getSpouseEmpOrChildren() == 'Y') {
			coverage += "Sp (Emp) or Dep ";
			if (bean.getMaxChildren() == 0)
				coverage += "(All)";
			else
				coverage += "(" + bean.getMaxChildren() + ")";
			coverage += ", ";
		}


		if (bean.getSpouseNonEmpOrChildren() == 'Y') {
			coverage += "Sp (Non-Emp)";

			if (bean.getSpouseDeclinesOutside() == 'Y')
				coverage += " - Declines Outside Coverage";

			coverage += " or Dep ";

			if (bean.getMaxChildren() == 0)
				coverage += "(All)";
			else
				coverage += "(" + bean.getMaxChildren() + ")";
			coverage += ", ";
		}
		if (coverage.length() == 0)
			return coverage;

		return coverage.substring(0, coverage.length() - 2);
	}

	/**
	 * @return
	 *
	 * public String[] getStatusIds() {
	 *
	 * final String ret[]=new String[bean.getStatuses().size()];
	 *
	 * int index=0;
	 *
	 * for (final HrEmployeeStatus st : bean.getStatuses())
	 * ret[index++]=st.getStatusId();
	 *
	 * return ret; }
	 *
	 * /
	 **
	 * @return
	 */
	public String getBenefitConfigId() {
		return bean.getBenefitConfigId();
	}

	/**
	 * @return
	 *
	 * public String getStatusCodes() {
	 *
	 * String ret="";
	 *
	 * for (final HrEmployeeStatus st : bean.getStatuses()) ret+=st.getName()+",
	 * ";
	 *
	 * if (ret.length()==0) return "";
	 *
	 * return ret.substring(0,ret.length()-2); }
	 *
	 * /
	 **
	 * @param startDate
	 */
	public void setStartDate(final int startDate) {
		bean.setStartDate(startDate);
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(final int endDate) {
		bean.setEndDate(endDate);
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public int getEndDate() {
		return bean.getEndDate();
	}

	public boolean getCoversEmployeeSpouse() {
		return bean.getSpouseEmployee() == 'Y';
	}

	public boolean getCoversEmployeeSpouseOrChildren() {
		return bean.getSpouseEmpOrChildren() == 'Y';
	}

	/**
	 * @param coversEmployeeSpouseOrChildren
	 */
	public void setCoversEmployeeSpouseOrChildren(final boolean coversEmployeeSpouseOrChildren) {
		final char newVal = coversEmployeeSpouseOrChildren ? 'Y' : 'N';
		changedApplicability = bean.getSpouseEmpOrChildren() != newVal;
		bean.setSpouseEmpOrChildren(newVal);
	}

	public String getCategoryId() {
		return bean.getHrBenefit().getHrBenefitCategory().getBenefitCatId();
	}

	public String getBenefitName() {
		return bean.getHrBenefit().getName();
	}

	public String getConfigId() {
		return bean.getBenefitConfigId();
	}

	public String getConfigName() {
		return bean.getName();
	}

	public int getPolicyStartDate() {
		return bean.getStartDate();
	}

	public boolean getSupportsBeneficiaries() {
		return bean.getHrBenefit().getHasBeneficiaries() == 'Y';
	}

	/**
	 * @return
	 *
	 * public String getStatusNames() {
	 *
	 * String ret="";
	 *
	 *
	 * for (final HrEmployeeStatus st : bean.getStatuses()) ret+=st.getName()+",
	 * ";
	 *
	 * if (ret.length()==0) return "(all)";
	 *
	 * return ret.substring(0,ret.length()-2); }
	 *
	 * /
	 **
	 * @return
	 */
	public boolean getNeedsBeneficiaries() {
		return bean.getHrBenefit().getHasBeneficiaries() == 'Y';
	}

	/**
	 * @return
	 */
	public boolean getNeedsEnrollees() {
		return bean.getSpouseNonEmployee() == 'Y'
				|| bean.getSpouseEmployee() == 'Y'
				|| bean.getChildren() == 'Y'
				|| bean.getSpouseEmpOrChildren() == 'Y'
				|| bean.getSpouseNonEmpOrChildren() == 'Y';
		//TODO: figure out if I can figure this out to a finer detail with auto assign
	}

	/**
	 * @return
	 */
	public boolean getUsesEmployeeAmount() {
		return bean.getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y';
	}
	
	private boolean isDecline = false;

	public boolean getIsDecline() {
		return isDecline;
	}

	/**
	 * @param isDecline The isDecline to set.
	 */
	public void setDecline(boolean isDecline) {
		this.isDecline = isDecline;
	}

	public String getMutuallyExclusiveId() {
		return bean.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean() ? "" : bean.getHrBenefitCategory().getBenefitCatId();
	}

	/**
	 * @param emp
	 * @return
	 */
	public static BHRBenefitConfig[] listAutoAssignsThatRequireBeneficiaries(BEmployee emp) {

		HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.AUTO_ASSIGN, 'Y');
		hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.HAS_BENEFICIARIES, 'Y');
		hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.eq(HrBenefitJoin.COVERED_PERSON, emp.employee);
		return makeArray(hcu.list());
	}

	public boolean getBenefitCategoryRequiresDecline() {
		return !bean.getHrBenefitCategory().getAllowsMultipleBenefitsBoolean();
	}

	public boolean getBenefitRequiresDecline() {
		return bean.getHrBenefit().getRequiresDecline() == 'Y';
	}

	public String getBenefitId() {
		return bean.getHrBenefit().getBenefitId();
	}

	/**
	 * @return
	 *
	 * public String getCalculatedCost() {
	 *
	 * return bean.getCalculatedCost(); }
	 *
	 * /
	 **
	 * @return
	 */
	@Deprecated
	public double getMaxEmployeeAmount() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @param maxAmount
	 */
	@Deprecated
	public void setMaxAmount(double maxAmount) {
		logger.deprecated();
	}

	@Deprecated
	public double getMaxAmount() {
		logger.deprecated();
		return 0;
	}

	/**
	 * @return
	 */
	public boolean getEmployeeSpecificCost() {
		return bean.getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y';
	}

	/**
	 * @return
	 */
	public String getInstructions() {
		return bean.getHrBenefit().getInstructions();
	}

	/**
	 * @return
	 */
	public int getBenefitCategoryType() {
		return bean.getHrBenefit().getHrBenefitCategory().getBenefitType();
	}

	/**
	 * @param config
	 * @return
	 */
	public boolean spousalCoverageCheck(BHRBenefitConfig config) {
		if (!config.getSpouseDeclinesExternalCoverage())
			return false;

		if (config.bean.equals(bean))
			return false;

		return getCoversChildren() == config.getCoversChildren()
				&& getCoversEmployee() == config.getCoversEmployee()
				&& (getCoversEmployeeSpouse() == config.getCoversEmployeeSpouse()
				|| (getCoversEmployeeSpouse() && config.getCoversEmployeeSpouseOrChildren() && config.getMaxChildren() == 1))
				&& getCoversEmployeeSpouseOrChildren() == config.getCoversEmployeeSpouseOrChildren()
				&& (getSpouseNonEmployee() == config.getSpouseNonEmployee()
				|| (getSpouseNonEmployee() && config.getSpouseNonEmpOrChildren() && config.getMaxChildren() == 1))
				&& getSpouseNonEmpOrChildren() == config.getSpouseNonEmpOrChildren();

	}

	/**
	 * @return
	 */
	public boolean getEmployeeIsProviderBool() {
		return bean.getHrBenefit().deprecatedGetEmployeeIsProvider() == 'Y';
	}

	/**
	 * @return
	 */
	public BHRBenefitConfig cloneFake() {
		HrBenefitConfig fake = new HrBenefitConfig();
		HibernateSessionUtil.copyCorresponding(fake, bean);
		fake.setBenefitConfigId("FAKE" + fake.getBenefitConfigId());
		return new BHRBenefitConfig(fake);
	}

	/**
	 * @return
	 */
	public boolean getEmployeeOnly() {
		return bean.getChildren() == 'N' && bean.getEmployee() == 'Y'
				&& bean.getSpouseEmpOrChildren() == 'N' && bean.getSpouseNonEmpOrChildren() == 'N'
				&& bean.getSpouseEmployee() == 'N' && bean.getSpouseNonEmployee() == 'N';
	}

	/**
	 * @return
	 */
	public boolean somebodyHasSpousal() {
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
				.eq(HrBenefitConfig.SPOUSAL, 'Y')
				.exists();
	}

	/**
	 * @return
	 */
	public static String getUnapprovedBenefitsReport() throws Exception {
		return new HRUnapprovedBenefitsReport().build();
	}

	/**
	 * @return
	 */
	public boolean getCoveredUnderCOBRA() {
		return bean.getHrBenefit().getCoveredUnderCOBRA() == 'Y';
	}

	/**
	 *
	 * @param policyStartDateFrom
	 * @param policyStartDateTo
	 * @param includeCoverageDetail
	 * @param limitToNoPolicyEndDate
	 * @return
	 */
	public static String getBenefitsUsingCOBRAReport(int policyStartDateFrom, int policyStartDateTo, boolean includeCoverageDetail, boolean limitToNoPolicyEndDate) throws Exception {
		return new HRBenefitsUsingCOBRAReport().build(policyStartDateFrom, policyStartDateTo, includeCoverageDetail, limitToNoPolicyEndDate);
	}

	/**
	 * @return
	 */
	public String getGeneratedCost() {
		return bean.getGeneratedCost();
	}

	/**
	 * @return
	 */
	public HrBenefit getBenefit() {
		return bean.getHrBenefit();
	}

	public Set<Project> getProjects() {
		return bean.getProjects();
	}

	public List<Project> listProjects() {
		List<Project> l = new ArrayList<Project>();
		l.addAll(bean.getProjects());
		return l;
	}

	public void setProjects(Set<Project> projects) {
		bean.setProjects(projects);
	}

	public int getSequence() {
		return bean.getSequence();
	}

	public void setSequence(int sequence) {
		bean.setSequence((short) sequence);
	}

	@Deprecated
	public double getMinValue() {
		logger.deprecated();
		return 0;
	}

	@Deprecated
	public void setMinValue(double minValue) {
		logger.deprecated();
	}

	@Deprecated
	public int getStepValue() {
		logger.deprecated();
		return 1;
	}

	@Deprecated
	public void setStepValue(int stepValue) {
		logger.deprecated();
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}
	
	public int getEarliestStartDate(int qualifyingEventDate) {
		int date = qualifyingEventDate;
		if (getPolicyStartDate() > date)
			date = getPolicyStartDate();
		HrBenefit ben = getBenefit();
		if (ben.getStartDate() > date)
			date = ben.getStartDate();
		return date;
	}

	public void moveUp() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean.getSequence() > 0) {
			HrBenefitConfig bc = hsu.createCriteria(HrBenefitConfig.class)
					.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
					.eq(HrBenefitConfig.SEQ, (short) (bean.getSequence() - 1))
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 99999);
			hsu.saveOrUpdate(bc);
			hsu.flush();
			bean.setSequence((short) (bean.getSequence() - 1));
			hsu.saveOrUpdate(bean);
			hsu.flush();
			bc.setSequence(temp);
			hsu.saveOrUpdate(bc);
		} else {  //shift them all
			List<HrBenefitConfig> l = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
					.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
					.orderBy(HrBenefitConfig.SEQ)
					.list();

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
		if (bean.getSequence() != hsu.createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
				.count() - 1) {
			HrBenefitConfig bc = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
					.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
					.eq(HrBenefitConfig.SEQ, (short) (bean.getSequence() + 1))
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 999999);
			hsu.saveOrUpdate(bc);
			hsu.flush();
			bean.setSequence((short) (bean.getSequence() + 1));
			hsu.saveOrUpdate(bean);
			bc.setSequence(temp);
			hsu.saveOrUpdate(bc);
		} else {  //shift them all
			List<HrBenefitConfig> l = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
					.eq(HrBenefitConfig.HR_BENEFIT, bean.getHrBenefit())
					.orderBy(HrBenefitConfig.SEQ)
					.list();

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

	public List<IPerson> estimateCoveredPeople(BEmployee employee) {
		return estimateCoveredPeople(employee, false);
	}

	public List<IPerson> estimateCoveredPeople(BEmployee employee, boolean includeChangeRecords) {
		BHRBenefitConfig bc = this;
		Set<IPerson> covered = new HashSet<IPerson>();

		// when a dependent is promoted to getting their oen COBRA they're not an employee
		if (employee == null) {
			List<IPerson> p2 = new ArrayList<IPerson>();
			p2.addAll(covered);
			return p2;
		}

		//if not, get the coverages from the benefit config
		boolean emp = bc.getCoversEmployee();
		if (emp)
			covered.add(employee.getPerson());
		boolean sp = bc.getCoversEmployeeSpouse() || bc.getSpouseNonEmployee();

		if (sp && employee.hasSpouse())
			covered.add(employee.getCurrentSpouse().getPerson());
		else if (includeChangeRecords) {
			Person spouse = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').eq(HrEmplDependent.EMPLOYEE, employee.getEmployee()).eq(HrEmplDependent.RECORD_TYPE, 'C').first();
			if (spouse != null)
				covered.add(spouse);
		}
		boolean kids = bc.getCoversChildren();

		//now add variable coverage
		if ((bc.getCoversEmployeeSpouseOrChildren() || bc.getSpouseNonEmpOrChildren()))
			//add all wife and kids
			if (bc.getMaxChildren() == 0) {
				for (BHREmplDependent bhd : employee.getDependents())
					if (bhd.isCurrentlyActive())
						covered.add(bhd.getPerson());
			} else {
				//start with wife, then add kids up to bc.getMaxChildren()
				int done = 0;
				if (employee.hasSpouse()) {
					covered.add(employee.getCurrentSpouse().getPerson());
					done = 1;
				}

				BHREmplDependent[] bhd = employee.getDependents();

				for (int i = 0; i < bc.getMaxChildren() - done && i < bhd.length; i++)
					if (bhd[i].getRelationship() == HrEmplDependent.TYPE_CHILD && bhd[i].isCurrentlyActive())
						covered.add(bhd[i].getPerson());
			}

		if (kids)
			//add all kids
			if (bc.getMaxChildren() == 0) {
				for (BHREmplDependent bhd : employee.getDependents())
					if (bhd.getRelationship() == HrEmplDependent.TYPE_CHILD && bhd.isCurrentlyActive())
						covered.add(bhd.getPerson());
			} else {
				// add kids up to bc.getMaxChildren()
				BHREmplDependent[] bhd = employee.getDependents();

				for (int i = 0; i < bhd.length && i < bhd.length; i++) {
					if (bhd[i].getRelationship() == HrEmplDependent.TYPE_CHILD && bhd[i].isCurrentlyActive())
						covered.add(bhd[i].getPerson());
					int max = (bc.getMaxChildren() + (emp ? 1 : 0));
					if (covered.size() == max)
						break;
				}
			}
		List<IPerson> p = new ArrayList<IPerson>();
		p.addAll(covered);
		return p;
	}
	


}
