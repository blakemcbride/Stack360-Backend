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
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.*;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Collections;
import org.hibernate.Query;
import org.hibernate.ScrollMode;

public class BHRBenefitCategory extends SimpleBusinessObjectBase<HrBenefitCategory> {

	public BHRBenefitCategory() {
	}

	/**
	 * @param categoryId
	 * @throws ArahantException
	 */
	public BHRBenefitCategory(final String categoryId) throws ArahantException {
		internalLoad(categoryId);
	}

	/**
	 * @param category
	 */
	public BHRBenefitCategory(final HrBenefitCategory category) {
		bean = category;
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitCategory();
		bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public String getId() {
		return bean.getBenefitCatId();
	}

	public BHRBenefit[] listActiveBenefits(int date) {
		return BHRBenefit.makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.orderBy(HrBenefit.NAME)
				.eq(HrBenefit.BENEFIT_CATEGORY, bean)
				.dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, date)
				.list());
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitCategory.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @return
	 */
	public String getCategoryId() {
		return bean.getBenefitCatId();
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	/**
	 * @return
	 */
	public static BHRBenefitCategory[] list() {
		List<String> restricted = AIProperty.getList("RestrictedBenefitCategory");
		HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).orderBy(HrBenefitCategory.SEQ).orderBy(HrBenefitCategory.DESCRIPTION);
		if (restricted.size() > 0)
			hcu.in(HrBenefitCategory.BENEFIT_CATEGORY_ID, restricted);

		return makeArray(hcu.list());
	}

	public static BHRBenefitCategory[] list(String[] excludeIds, int cap) {
		HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
				.orderBy(HrBenefitCategory.SEQ)
				.orderBy(HrBenefitCategory.DESCRIPTION)
				.notIn(HrBenefitCategory.BENEFIT_CATEGORY_ID, excludeIds)
				.setMaxResults(cap);

		return makeArray(hcu.list());
	}

	public BHRBenefit[] listBenefits(String[] excludeIds, int cap) {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.notIn(HrBenefit.BENEFITID, excludeIds)
				.eq(HrBenefit.BENEFIT_CATEGORY, getBean())
				.setMaxResults(cap);

		return BHRBenefit.makeArray(hcu.list());
	}

	public static BHRBenefitCategory[] makeArray(final List<HrBenefitCategory> l) {
		final BHRBenefitCategory[] ret = new BHRBenefitCategory[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitCategory(l.get(loop));

		return ret;
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (String element : ids)
			new BHRBenefitCategory(element).delete();
	}

	@Override
	public void delete() {

		for (HrBenefit b : ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.eq(HrBenefit.BENEFIT_CATEGORY, bean)
				.list())
			new BHRBenefit(b).delete();

		super.delete();
	}

	/**
	 * @return @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String getReport() throws FileNotFoundException, DocumentException, ArahantException {
		return new HRBenefitCategoryReport().build(list());
	}

	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	/**
	 * @return
	 */
	public static List<CategoryType> listTypes() {
		final List<CategoryType> l = new ArrayList<CategoryType>(HrBenefitCategory.types.length);

		for (int loop = 0; loop < HrBenefitCategory.types.length; loop++)
			l.add(new CategoryType(loop, HrBenefitCategory.types[loop]));

		Collections.sort(l);

		return l;
	}

	public List<HrBenefit> getBenefits() {
		return ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.eq(HrBenefit.BENEFIT_CATEGORY, getBean())
				.orderBy(HrBenefit.SEQ)
				.list();
	}

	public static class CategoryType implements Comparable<CategoryType> {
		private int id;
		private String name;

		/**
		 * @param loop
		 * @param string
		 */
		public CategoryType(final int loop, final String string) {
			id = loop;
			name = string;
		}

		/**
		 * @return Returns the id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id The id to set.
		 */
		public void setId(final int id) {
			this.id = id;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(final String name) {
			this.name = name;
		}
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */

		@Override
		public int compareTo(final CategoryType o) {
			return name.compareTo(o.name);
		}
	}

	/**
	 * @param typeId
	 */
	public void setTypeId(final int typeId) {
		bean.setBenefitType((short) typeId);
	}

	/**
	 * @return
	 */
	public int getTypeId() {

		return bean.getBenefitType();
	}

	/**
	 * @return
	 */
	public String getTypeName() {
		return HrBenefitCategory.types[bean.getBenefitType()];
	}

	@SuppressWarnings("unchecked")
	public static String getDependentReport(final int minAge, final String[] benefitCategoryCategoryIds, final int year, int inactiveAsOf, boolean excludeHandicap, boolean excludeStudent) throws ArahantException, FileNotFoundException, DocumentException {
		Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
		Set<OrgGroup> ogs = new HashSet<OrgGroup>();
		for (OrgGroupAssociation o : oga)
			ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

		final Calendar startOfYear = DateUtils.getNow();
		startOfYear.set(Calendar.YEAR, year - minAge);
		startOfYear.set(Calendar.MONTH, Calendar.JANUARY);
		startOfYear.set(Calendar.DAY_OF_MONTH, 1);

		final Calendar endOfYear = DateUtils.getNow();
		endOfYear.set(Calendar.YEAR, year - minAge);
		endOfYear.set(Calendar.MONTH, Calendar.DECEMBER);
		endOfYear.set(Calendar.DAY_OF_MONTH, 31);

		String q = "select distinct per.fname, per.lname, per.ssn, per.dob, cat.description, emp.lname, emp.fname, emp.ssn, per.personId "
				+ " from HrEmplDependent dep "
				+ " join dep." + HrEmplDependent.EMPLOYEE + " emp "
				+ " join emp." + Employee.HREMPLSTATUSHISTORIES + " hist "
				+ " join hist." + HrEmplStatusHistory.HREMPLOYEESTATUS + " stat "
				+ " join dep." + HrEmplDependent.PERSON + " per "
				+ " join emp." + Employee.ORGGROUPASSOCIATIONS + " oga "
				+ " join per." + Person.HR_BENEFIT_JOINS_WHERE_COVERED + " ben2 "
				+ " join ben2." + HrBenefitJoin.HR_BENEFIT_CONFIG + "." + HrBenefitConfig.HR_BENEFIT + " ben3 "
				+ " join ben3." + HrBenefit.BENEFIT_CATEGORY + " cat "
				+ "where dep." + HrEmplDependent.RELATIONSHIP_TYPE + "='C' and "
				+ " per." + Person.DOB + " >= " + DateUtils.getDate(startOfYear) + " and "
				+ " per." + Person.DOB + " <= " + DateUtils.getDate(endOfYear);

		if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
			int i = 0;
			q += " and (";
			for (OrgGroup og : ogs) {
				if (i != 0)
					q += " or ";
				q += "oga." + OrgGroup.ORGGROUPID + " = '" + og.getOrgGroupId() + "'";
				i++;
			}
			q += ")";
		}

		q += " and ben2." + HrBenefitJoin.COVERAGE_END_DATE + " = 0 "
				+ " and (";

		for (final String id : benefitCategoryCategoryIds)
			q += " cat." + HrBenefitCategory.BENEFIT_CATEGORY_ID + " = '" + id + "' or ";

		q = q.substring(0, q.length() - 3);

		q += ")";

		if (inactiveAsOf != 0)
			q += " and (dep.dateInactive = 0 or dep.dateInactive > " + inactiveAsOf + ")";
		if (excludeHandicap)
			q += " and per." + Person.HANDICAP + "='N' ";

		if (excludeStudent)
			q += " and per." + Person.STUDENT + "='N' ";

		// add status filter (inactive statuses)
		//per laurie status doesn't matter
		//	q+= " and hist.effectiveDate=(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<="+DateUtils.now()+") and stat.active='Y' ";

		q += " order by per.dob, per.personId";


		final Query qry = ArahantSession.getHSU().createQuery(q);

		final List benes = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
				.selectFields(HrBenefitCategory.DESCRIPTION)
				.orderBy(HrBenefitCategory.DESCRIPTION)
				.in(HrBenefitCategory.BENEFIT_CATEGORY_ID, benefitCategoryCategoryIds)
				.list();

		final List<String> names = benes;

		String benefitCategoryNames[] = new String[names.size()];
		benefitCategoryNames = names.toArray(benefitCategoryNames);


		final HRDependentsAgeReport report = new HRDependentsAgeReport();
		return report.build(benefitCategoryNames, minAge, qry.scroll(ScrollMode.FORWARD_ONLY), year, inactiveAsOf);
	}

	/**
	 * @param benefitId
	 * @param orgGroupId
	 * @param statusId
	 * @return
	 * @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String getReport(final String benefitId, final String orgGroupId, final String statusId, int enrolledAsOf) throws FileNotFoundException, DocumentException, ArahantException {
		return new HREnrollmentListReport().build(bean.getBenefitCatId(), benefitId, statusId, orgGroupId, enrolledAsOf);
	}

	/**
	 * @param benefitId
	 * @param orgGroupId
	 * @param statusId
	 * @param includeDeclines
	 * @return
	 * @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String getMissingReport(String benefitId, String orgGroupId, String statusId, boolean includeDeclines, int notEnrolledAsOf) throws FileNotFoundException, DocumentException, ArahantException {
		/*
		 if includeDeclines is true, people that have declines should show up
		 if includeDeclines is false, people that have declines should not show up
		 should show ssn, name, and if includeDeclines is true, a Yes/No column that shows that
		 break down by org group with counts, very similar to enrollment list report
		 */
		return new HRMissingEnrollmentListReport().build(bean.getBenefitCatId(), benefitId, statusId, orgGroupId, includeDeclines, notEnrolledAsOf);
	}

	/**
	 * @return
	 */
	public boolean getAllowsMultipleBenefits() {
		return bean.getMutuallyExclusive() != 'Y';
	}

	/**
	 * @return
	 */
	public String getAllowsMultipleBenefitsFormatted() {

		return bean.getMutuallyExclusive() != 'Y' ? "Yes" : "No";
	}

	/**
	 * @param allowsMultipleBenefits
	 */
	public void setAllowsMultipleBenefits(boolean allowsMultipleBenefits) {
		bean.setMutuallyExclusive(!allowsMultipleBenefits ? 'Y' : 'N');
	}

	/**
	 * @return
	 */
	public String getCategoryName() {
		return bean.getDescription();
	}

	public int getSequence() {
		return bean.getSequence();
	}

	public void setSequence(int seqno) {
		bean.setSequence((short) seqno);
	}

	/**
	 * @param requiresDecline
	 * @throws ArahantWarning
	 */
	public void setRequiresDecline(final boolean requiresDecline) throws ArahantWarning {
		bean.setRequiresDecline(requiresDecline ? 'Y' : 'N');

		if (requiresDecline)
			//see if there is a benefit in this category with decline set
			if (ArahantSession.getHSU().createCriteria(HrBenefit.class)
					.eq(HrBenefit.BENEFIT_CATEGORY, bean)
					.eq(HrBenefit.REQUIRES_DECLINE, 'Y')
					.exists())
				throw new ArahantWarning("You can't set category to requires decline while a benefit in that category is set to requires decline.");

	}

	/**
	 * @return
	 */
	public boolean getRequiresDecline() {
		return bean.getRequiresDecline() == 'Y';
	}

	/**
	 * @param benefitId
	 * @param forDecline
	 * @return
	 */
	public BHRBenefit[] listActiveBenefits(String benefitId, boolean forDecline, int effectiveDate) {
		final HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);

		if (forDecline)
			hcu.eq(HrBenefit.REQUIRES_DECLINE, 'Y');

		final List<HrBenefit> l = hcu.eq(HrBenefit.BENEFIT_CATEGORY, bean)
				.le(HrBenefit.START_DATE, effectiveDate)
				.geOrEq(HrBenefit.END_DATE, effectiveDate, 0)
				.list();

		if (!isEmpty(benefitId))
			l.add(ArahantSession.getHSU().get(HrBenefit.class, benefitId));

		return BHRBenefit.makeArray(l);
	}

	/**
	 * @param benefitId
	 * @param forDecline
	 * @return
	 */
	public BHRBenefit[] listActiveBenefitsByClass(BPerson person, String benefitId, boolean forDecline, int effectiveDate) {
		final HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);

		if (person.isEmployee() && person.getBEmployee().getBenefitClass() != null) {
			//HibernateCriteriaUtil chcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.makeCriteria();

			HibernateCriteriaUtil classHcu = hcu.leftJoinTo("benefitClasses", "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq("benefitClasses", 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, person.getBEmployee().getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}
		if (forDecline)
			hcu.eq(HrBenefit.REQUIRES_DECLINE, 'Y');

		final List<HrBenefit> l = hcu.eq(HrBenefit.BENEFIT_CATEGORY, bean)
				//			.le(HrBenefit.START_DATE, effectiveDate)  //  allow assignment to future available benefits
				.geOrEq(HrBenefit.END_DATE, effectiveDate, 0)
				.list();

		if (!isEmpty(benefitId))
			l.add(ArahantSession.getHSU().get(HrBenefit.class, benefitId));

		return BHRBenefit.makeArray(l);
	}

	public BHRBenefit[] listBenefits() {
		return BHRBenefit.makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.eq(HrBenefit.BENEFIT_CATEGORY, bean)
				.list());
	}

	public static String findOrMake(String code) {
		HrBenefitCategory bc = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
				.eq(HrBenefitCategory.DESCRIPTION, code)
				.first();

		if (bc != null)
			return bc.getBenefitCatId();

		BHRBenefitCategory bbc = new BHRBenefitCategory();
		bbc.create();
		bbc.setDescription(code);
		bbc.setTypeId(0);
		bbc.insert();

		return bbc.getCategoryId();
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void moveUp() {
		if (bean.getSequence() > 0) {
			HrBenefitCategory bc = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
					.eq(HrBenefitCategory.SEQ, (short) (bean.getSequence() - 1))
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSequence((short) (bean.getSequence() - 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			bc.setSequence(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<HrBenefitCategory> l = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
					.orderBy(HrBenefitCategory.SEQ)
					.list();

			l.get(0).setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSequence((short) (l.get(loop).getSequence() - 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setSequence((short) (l.size() - 1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		if (bean.getSequence() != ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
				.count() - 1) {
			HrBenefitCategory bc = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
					.eq(HrBenefitCategory.SEQ, (short) (bean.getSequence() + 1))
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 999999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSequence((short) (bean.getSequence() + 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			bc.setSequence(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<HrBenefitCategory> l = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
					.orderBy(HrBenefitCategory.SEQ)
					.list();

			l.get(l.size() - 1).setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setSequence((short) (loop + 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setSequence((short) 0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public String getAvatarPath() {
		return bean.getAvatarPath();
	}

	public void setAvatarPath(String avatarPath) {
		bean.setAvatarPath(avatarPath);
	}

	public String getOpenEnrollmentWizard() {
		return bean.getOpenEnrollmentWizard() + "";
	}

	public void setOpenEnrollmentWizard(String openEnrollmentWizard) {
		bean.setOpenEnrollmentWizard(openEnrollmentWizard.charAt(0));
	}

	public String getOnboarding() {
		return bean.getOnboarding() + "";
	}

	public void setOnboarding(String onboarding) {
		bean.setOnboarding(onboarding.charAt(0));
	}

	public String getInstructions() {
		return bean.getInstructions();
	}

	public void setInstructions(String instructions) {
		bean.setInstructions(instructions);
	}

	public Screen getOpenEnrollmentScreen() {
		return bean.getOpenEnrollmentScreen();
	}

	public void setOpenEnrollmentScreen(Screen openEnrollmentScreen) {
		bean.setOpenEnrollmentScreen(openEnrollmentScreen);
	}

	public String getAvatarLocation() {
		return bean.getAvatarLocation();
	}

	public void setAvatarLocation(String avatarLocation) {
		bean.setAvatarLocation(avatarLocation);
	}

	public Screen getOnboardingScreen() {
		return bean.getOnboardingScreen();
	}

	public void setOnboardingScreen(Screen onboardingScreen) {
		bean.setOnboardingScreen(onboardingScreen);
	}

	public static String findOrMake(String name, boolean multipleBenefits, boolean requiresDecline, short type) {
		HrBenefitCategory bc = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
				.eq(HrBenefitCategory.DESCRIPTION, name)
				.eq(HrBenefitCategory.COMPANY, ArahantSession.getHSU().getCurrentCompany())
				.first();

		if (bc != null)
			return bc.getBenefitCatId();

		BHRBenefitCategory benefitCategory = new BHRBenefitCategory();
		String ret = benefitCategory.create();
		benefitCategory.setAllowsMultipleBenefits(multipleBenefits);
		benefitCategory.setDescription(name);
		benefitCategory.setRequiresDecline(requiresDecline);
		benefitCategory.setTypeId(type);
		benefitCategory.insert();

		return ret;
	}
}
