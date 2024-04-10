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
import com.arahant.exceptions.ArahantDuplicateSSNWarning;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.HRDependeeReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import jess.JessException;

/**
 *
 *
 *
 */
public class BHREmplDependent extends BPerson implements IDBFunctions, Comparable<BHREmplDependent> {

	public BHREmplDependent() {
		super();
	}

	/**
	 * @return @see com.arahant.beans.HrEmplDependent#getDependentId()
	 */
	public String getDependentId() {
		return bean.getRelationshipId();
	}

	public HrEmplDependent getEmplDependent() {
		return this.bean;
	}

	/**
	 * @return @see com.arahant.beans.HrEmplDependent#getRelationship()
	 */
	public String getOtherRelationshipDescription() {
		return bean.getRelationship();
	}

	public String getStudentTermType() {
		return bean.getPerson().getStudentCalendarType() + "";
	}

	public int getStudentVerificationDate() {
		StudentVerification sv = ArahantSession.getHSU().createCriteria(StudentVerification.class).eq(StudentVerification.PERSON, bean.getPerson()).orderByDesc(StudentVerification.YEAR).orderByDesc(StudentVerification.TERM).first();

		if (sv == null)
			return 0;

		Calendar cal = DateUtils.getNow();
		cal.set(Calendar.YEAR, sv.getSchoolYear());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		switch (sv.getCalendarPeriod()) {
			case StudentVerification.PERIOD_FALL:
				cal.set(Calendar.MONTH, 9);
				break;
			case StudentVerification.PERIOD_WINTER:
				cal.set(Calendar.MONTH, 12);
				break;
			case StudentVerification.PERIOD_SPRING:
				cal.set(Calendar.MONTH, 3);
				break;
			case StudentVerification.PERIOD_SUMMER:
				cal.set(Calendar.MONTH, 6);
				break;
		}

		return DateUtils.getDate(cal);
	}

	public String getTextRelationship() {
		switch (bean.getRelationshipType()) {
			case 'E':
				return "Employee"; //used for temporary returns
			case 'S':
				return "Spouse";
			case 'C':
				return "Child";
			case 'O':
				return getOtherRelationshipDescription();
			default:
				return "Unknown";
		}
	}

	/**
	 * @param relationship
	 * @see com.arahant.beans.HrEmplDependent#setRelationship(java.lang.String)
	 */
	public void setRelationship(final String relationship) {
		bean.setRelationship(relationship);
	}
	HrEmplDependent bean;

	/**
	 * @param dependent
	 * @throws ArahantException
	 */
	public BHREmplDependent(final HrEmplDependent dependent) throws ArahantException {
		super(dependent.getPerson());
		bean = dependent;
		oldRelationshipType = bean.getRelationshipType();
	}

	/**
	 * @param dependentId
	 * @throws ArahantException
	 */
	public BHREmplDependent(final String dependentId) throws ArahantException {
		super();

		load(dependentId);

		this.loadedDateInactive = bean.getDateInactive();
		oldRelationshipType = bean.getRelationshipType();
	}

	/**
	 * @param employeeId
	 * @param string
	 */
	public BHREmplDependent(String employeeId, String personId) {
		HibernateCriteriaUtil<HrEmplDependent> hcu = ArahantSession.getHSU().createCriteria(HrEmplDependent.class);
		hcu.joinTo(HrEmplDependent.EMPLOYEE).eq(Employee.PERSONID, employeeId);
		hcu.joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, personId);
		bean = hcu.first();
		if (bean == null)
			throw new ArahantException("Relationship not found for " + employeeId + " " + personId);
		oldRelationshipType = bean.getRelationshipType();
		person = bean.getPerson();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrEmplDependent();
		super.create();
		bean.setPerson(person);
		bean.setDateAdded(DateUtils.now());
		return bean.generateId();
	}
	boolean didInactivate = false;

	@Override
	public void delete() throws ArahantDeleteException {
		delete(DateUtils.now());
	}

	public void delete(int terminateDate) throws ArahantDeleteException {

		int termDate = DateUtils.add(terminateDate, -1);
		if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.RELATIONSHIP, bean).exists() || ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class) //check history like above too
				.eq(HrBenefitJoinH.RELATIONSHIP_ID, bean.getRelationshipId()).exists()) {
			//They were in use, so just deactivate them
			bean.setDateInactive(termDate);
			termBenefits(termDate);
			didInactivate = true;
		} else
			try {
				ArahantSession.getHSU().delete(bean);
			} catch (ArahantDeleteException e) {
				//They were in use, so just deactivate them
				bean.setDateInactive(termDate);
				termBenefits(termDate);
				didInactivate = true;
			}

		//don't delete person if it's an employee
		final Person emp = ArahantSession.getHSU().get(Employee.class, bean.getPerson().getPersonId());
		if (emp != null)
			//I'm an employee that is a dependent
			//this relationship may be two way, so wipe the other way
			try {
				BHREmplDependent bdep = new BHREmplDependent(emp.getPersonId(), bean.getEmployee().getPersonId());
				bdep.setDateInactive(termDate);
				bdep.termBenefits(termDate);
				bdep.update();
			} catch (Exception e) //apparently not two way, so just go on
			{
			}
		else
			//delete the person if nobody else points to it
			if (!didInactivate && !ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, person).exists())
				super.delete();
	}

	/**
	 * @param termDate
	 */
	public void setDateInactive(int termDate) {
		bean.setDateInactive(termDate);
	}

	public void setStudentTermType(String studentTermType) {
		if (isEmpty(studentTermType))
			studentTermType = " ";
		bean.getPerson().setStudentCalendarType(studentTermType.charAt(0));
	}
	/*
	 * public void deleteWithNotify(int effectiveDate) throws
	 * ArahantDeleteException { delete(effectiveDate); if (!didInactivate) {
	 * boolean notify=!hsu.createCriteria(OrgGroupAssociation.class)
	 * .eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
	 * .joinTo(OrgGroupAssociation.ORGGROUP) .eq(OrgGroup.ORGGROUPID,
	 * "00001-0000000004") .exists();
	 *
	 * // WMCO specific if (notify) { BProject bp=new BProject(); bp.create();
	 * bp.setRequestingOrgGroupId(hsu.getFirst(CompanyDetail.class).getOrgGroupId());
	 * bp.setTimeReported(DateUtils.nowTime());
	 * bp.setDateReported(DateUtils.now());
	 * bp.setCurrentPersonId("00001-0000005729"); //Laurie
	 * bp.setDoneForPersonId(bean.getEmployee().getPersonId());
	 * bp.setDescription("Dependent Terminated"); bp.setDetailDesc("A dependent
	 * was termed - "+bean.getPerson().getNameLFM()+".");
	 * bp.setProjectTypeId("00001-0000000001");
	 * bp.setProjectCategoryId(hsu.getFirst(ProjectCategory.class).getProjectCategoryId());
	 * bp.setProjectStatusId(hsu.createCriteria(ProjectStatus.class).eq(ProjectStatus.ACTIVE,
	 * 'Y').first().getProjectStatusId());
	 *
	 * bp.insert(); } } }
     *
	 */

	private void termBenefits(final int termDate) {
		for (final HrBenefitJoin dbj : bean.getBenefitJoins())
			if (dbj.getCoverageEndDate() == 0 && dbj.getCoverageStartDate() != 0) {
				dbj.setCoverageEndDate(termDate);
				dbj.setChangeDescription("Dependent Ineligible");
				ArahantSession.getHSU().saveOrUpdate(dbj);
			}
	}

	@Override
	public void insert() throws ArahantException {

		//if this is a spouse, make sure there isn't one already

		if (bean.getRelationshipType() == 'S')
			if (ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bean.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).exists())
				throw new ArahantWarning("Only one spouse allowed.  Please remove prior spouse before adding a new one.");

		super.insert();

		bean.setPerson(person);


		//if this is a child, and the parent has a married employee, duplicate the bean for the other employee
		if (bean.getRelationshipType() == 'C') {
			final HrEmplDependent dep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bean.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').first();
			if (dep != null) {
				final Employee spouse = ArahantSession.getHSU().get(Employee.class, dep.getPerson().getPersonId());
				if (spouse != null) {
					final HrEmplDependent newdep = new HrEmplDependent();
					newdep.generateId();
					newdep.setDependentId(bean.getDependentId());
					newdep.setEmployee(spouse);
					newdep.setRelationship(bean.getRelationship());
					newdep.setRelationshipType(bean.getRelationshipType());
					newdep.setPerson(bean.getPerson());
					ArahantSession.getHSU().insert(newdep);
				}

			}

		}

		if (bean.getDateAdded() == 0)
			bean.setDateAdded(DateUtils.now());

		ArahantSession.getHSU().insert(bean);

		this.warnIfActiveAndInactive();
		this.loadedDateInactive = bean.getDateInactive();
	}
	private char oldRelationshipType;

	@Override
	public void update() {
		update(false);
	}

	public void update(boolean skip) throws ArahantException {


		if (!skip) {
			//if this is a spouse, make sure there isn't one already
			if (bean.getRelationshipType() == 'S')
				if (ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bean.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').joinTo(HrEmplDependent.PERSON).ne(Person.PERSONID, bean.getDependentId()).exists())
					throw new ArahantWarning("Only one spouse allowed.  Please remove prior spouse before adding a new one.");

			// should only check vs. benefits that are active
			boolean hasActiveBenefit = false;

			for (HrBenefitJoin dbj : bean.getBenefitJoins())
				if (!dbj.getPayingPersonId().equals(dbj.getCoveredPersonId())
						&& dbj.getCoveredPersonId().equals(bean.getPerson().getPersonId())
						&& dbj.getCoverageStartDate() != 0 && (dbj.getCoverageEndDate() == 0 || dbj.getCoverageEndDate() > DateUtils.now()))
					hasActiveBenefit = true;

			if (hasActiveBenefit && oldRelationshipType != bean.getRelationshipType())
				if (bean.getBenefitJoins().size() > 0)
					throw new ArahantWarning("You may not change relationship type while benefits are assigned.");

			super.update();
			ArahantSession.getHSU().saveOrUpdate(bean);

			final Employee emp = ArahantSession.getHSU().get(Employee.class, bean.getPerson().getPersonId());

			if (emp != null) {

				//should only check vs. benefits that are active

				//is the employee in the relationship providing me some benefits?
				if (hasActiveBenefit && ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).joinTo(HrBenefitJoin.RELATIONSHIP).eq(HrEmplDependent.EMPLOYEE, emp).eq(HrEmplDependent.PERSON, bean.getEmployee()).exists())
					throw new ArahantWarning("You may not change the relationship type while that dependent (who is also an employee) is providing benefits to this employee.");


				//Depedent became spouse
				if (oldRelationshipType != 'S' && bean.getRelationshipType() == 'S') {
					//add reverse link and move kides
					final HrEmplDependent dep = new HrEmplDependent();
					dep.generateId();
					dep.setDependentId(bean.getEmployee().getPersonId());
					dep.setEmployee(emp);
					dep.setPerson(bean.getEmployee());
					dep.setRelationshipType('S');
					ArahantSession.getHSU().saveOrUpdate(dep);

					person = emp;
					moveKidsOver();
				}


				//dependent stopped being spouse
				if (oldRelationshipType == 'S' && bean.getRelationshipType() != 'S')
					//drop all dependents, this will break the
					//reverse link and remove any kids that were moved over
					ArahantSession.getHSU().delete(emp.getHrEmplDependents());
			}

			if (bean.getDateInactive() != 0) {
				termBenefits(bean.getDateInactive()); //if I have any non-termed benefits, term them
				this.warnIfActiveAndInactive();
			}
			this.loadedDateInactive = bean.getDateInactive();
		} else {
			if (!isEmpty(person.getUnencryptedSsn()) && ArahantSession.getHSU().createCriteriaNoCompanyFilter(Person.class).ne(Person.PERSONID, person.getPersonId()).eq(Person.SSN, person.getUnencryptedSsn()).exists())
				throw new ArahantDuplicateSSNWarning("Social Security Number must be unique.  There is already a person in the system with that number.");

			ArahantSession.getHSU().saveOrUpdate(bean);
		}

	}

	public void updateNoCheck() throws ArahantException {


		super.update();
		ArahantSession.getHSU().saveOrUpdate(bean);


	}
	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrEmplDependent.class, key);
		if (bean == null)
			throw new ArahantWarning("Dependent not found.");
		super.load(bean.getPerson().getPersonId());
		oldRelationshipType = bean.getRelationshipType();
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param dependentIds
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static boolean delete(final String[] dependentIds) throws ArahantDeleteException, ArahantException {

		boolean retVal = false;
		for (final String key : dependentIds) {
			final BHREmplDependent d = new BHREmplDependent(key);
			d.delete();
			if (d.didInactivate)
				retVal = true;
		}
		return retVal;
	}

	/**
	 * @param l
	 * @return
	 * @throws ArahantException
	 */
	static BHREmplDependent[] makeArray(final List<HrEmplDependent> l) throws ArahantException {

		try {
			if (l.size() > 1)
				Collections.sort(l);
		} catch (Exception e) {
			//why the exception here?
		}
		final BHREmplDependent[] ret = new BHREmplDependent[l.size()];

		int loop = 0;

		for (final HrEmplDependent dependent : l)
			ret[loop++] = new BHREmplDependent(dependent);

		return ret;
	}

	/**
	 * @param relationshipType
	 * @throws ArahantException
	 */
	public void setRelationshipType(final String relationshipType) throws ArahantException {
		if (isEmpty(relationshipType))
			throw new ArahantException("Relationship type is required.");
		bean.setRelationshipType(relationshipType.charAt(0));
	}

	public void setRelationshipType(final char relationshipType) throws ArahantException {
		bean.setRelationshipType(relationshipType);
	}

	/**
	 * @return
	 */
	public String getRelationshipType() {

		return bean.getRelationshipType() + "";
	}

	/**
	 * @return
	 */
	public String getEnrolled() {

		return enrolled ? "Yes" : "No";
	}
	private boolean enrolled = false;
	private int benefitStartDate = 0;
	private int benefitEndDate = 0;
	private double amountCovered;

	/**
	 * @return Returns the benefitEndDate.
	 */
	public int getBenefitEndDate() {
		return benefitEndDate;
	}

	/**
	 * @param benefitEndDate The benefitEndDate to set.
	 */
	public void setBenefitEndDate(final int benefitEndDate) {
		this.benefitEndDate = benefitEndDate;
	}

	/**
	 * @return Returns the benefitStartDate.
	 */
	public int getBenefitStartDate() {
		return benefitStartDate;
	}

	/**
	 * @param benefitStartDate The benefitStartDate to set.
	 */
	public void setBenefitStartDate(final int benefitStartDate) {
		this.benefitStartDate = benefitStartDate;
	}

	/**
	 * @param b
	 */
	public void setEnrolled(final boolean b) {
		enrolled = b;
	}

	/**
	 * @param bl1
	 * @return
	 */
	public static BHREmplDependent[] sort(final List<BHREmplDependent> bl1) {

		Collections.sort(bl1);

		final BHREmplDependent[] ret = new BHREmplDependent[bl1.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = bl1.get(loop);

		return ret;
	}

	public static BHREmplDependent[] sort(final BHREmplDependent[] bl1) {

		final List<BHREmplDependent> l = new ArrayList<BHREmplDependent>(bl1.length);
		Collections.addAll(l, bl1);
		return sort(l);

	}

	/*
	 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final BHREmplDependent o) {

		if (getRelationshipType().equals(o.getRelationshipType()) && getRelationshipType().trim().equals("C"))
			return o.getDob() - getDob();

		if (getRelationshipType().equals(o.getRelationshipType()))
			if (getLastName().compareTo(o.getLastName()) == 0)
				return getFirstName().compareTo(getFirstName());
			else
				return getLastName().compareTo(o.getLastName());


		if (getRelationshipType().trim().equals("E"))
			return -1;

		if (o.getRelationshipType().trim().equals("E"))
			return 1;

		if (getRelationshipType().trim().equals("S"))
			return -1;

		if (getRelationshipType().equals("O"))
			return 1;

		if (o.getRelationshipType().trim().equals("S"))
			return 1;

		if (o.getRelationshipType().equals("O"))
			return -1;


		return 0;

	}

	/**
	 * @return
	 */
	public String getEmployeeSSN() {

		try {
			return bean.getEmployee().getUnencryptedSsn();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getEmployeeFName() {

		try {
			return bean.getEmployee().getFname();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getEmployeeLName() {

		try {
			return bean.getEmployee().getLname();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getEmployeeMName() {

		try {
			return bean.getEmployee().getMname();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public int getEmployeeDob() {

		try {
			return bean.getEmployee().getDob();
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * @return
	 */
	public char getEmployeeSex() {

		try {
			return bean.getEmployee().getSex();
		} catch (final Exception e) {
			return ' ';
		}
	}

	/**
	 * @return
	 */
	public String getEmployeeNameLFM() {

		try {
			return bean.getEmployee().getNameLFM();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public String hasBenefitCategoryByName(final String desc) {

		if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.RELATIONSHIP, bean).dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.DESCRIPTION, desc).exists())
			return "Y";

		return "N";
	}

	/**
	 * @return
	 */
	@Override
	public boolean getStudent() {

		return person.getStudent() == 'Y';
	}

	/**
	 * @return
	 */
	@Override
	public boolean getHandicap() {

		return person.getHandicap() == 'Y';
	}

	/**
	 * @param handicap
	 */
	@Override
	public void setHandicap(final boolean handicap) {
		person.setHandicap(handicap ? 'Y' : 'N');
	}

	/**
	 * @param student
	 */
	@Override
	public void setStudent(final boolean student) {
		person.setStudent(student ? 'Y' : 'N');
	}

	/**
	 * @param dependentEmployeeId
	 * @param employeeId
	 * @param relationshipType
	 * @throws ArahantException
	 */
	public void create(final String dependentEmployeeId, final String employeeId, final String relationshipType) throws ArahantException {
		bean = new HrEmplDependent();
		bean.generateId();
		person = ArahantSession.getHSU().get(Person.class, dependentEmployeeId);
		bean.setDependentId(person.getPersonId());
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
		bean.setPerson(person);
		bean.setRelationship("");
		if (isEmpty(relationshipType))
			bean.setRelationshipType('S');
		else
			bean.setRelationshipType(relationshipType.charAt(0));


		if (bean.getRelationshipType() == 'S' && ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.PERSONID, dependentEmployeeId).exists()) {
			checkForSpouse(dependentEmployeeId);

			//need to do the reverse too
			final BHREmplDependent dep = new BHREmplDependent();
			dep.createSpouseBackLink(employeeId, dependentEmployeeId, bean.getDateInactive());
			dep.insert();

			moveKidsOver();
		}
	}

	/**
	 * @throws ArahantException
	 */
	private void moveKidsOver() throws ArahantException {
		//move kids over
		for (final HrEmplDependent mdep : bean.getEmployee().getHrEmplDependents()) {
			if (mdep.getRelationshipType() == 'S')
				continue;

			final HrEmplDependent d = new HrEmplDependent();
			d.generateId();
			d.setPerson(mdep.getPerson());
			d.setEmployee((Employee) person);
			d.setRelationship(mdep.getRelationship());
			d.setRelationshipType(mdep.getRelationshipType());
			d.setDateAdded(mdep.getDateAdded());
			d.setDateInactive(mdep.getDateInactive());
			ArahantSession.getHSU().insert(d);
		}
	}

	/**
	 * @param dependentEmployeeId
	 * @param employeeId
	 * @throws ArahantException
	 */
	void createSpouseBackLink(final String dependentEmployeeId, final String employeeId, int dateInactive) throws ArahantException {
		bean = new HrEmplDependent();
		bean.generateId();
		person = ArahantSession.getHSU().get(Employee.class, dependentEmployeeId);
		bean.setDependentId(person.getPersonId());
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
		bean.setPerson(person);
		bean.setRelationship("");
		bean.setRelationshipType('S');
		bean.setDateInactive(dateInactive);
		bean.setDateAdded(DateUtils.now());
		//checkForSpouse(dependentEmployeeId);

		if (ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bean.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').joinTo(HrEmplDependent.PERSON).ne(Person.PERSONID, bean.getDependentId()).exists())
			throw new ArahantWarning("The person you are linking to has a spouse already assigned.");

	}

	private void checkForSpouse(final String dependentEmployeeId) throws ArahantWarning {
		if (ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bean.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').joinTo(HrEmplDependent.PERSON).ne(Person.PERSONID, bean.getDependentId()).exists())
			throw new ArahantWarning("This person has a spouse already assigned.");

		if (ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, dependentEmployeeId).exists())
			throw new ArahantWarning("The person you are linking to has a spouse already assigned.");
		/*
		 * if (hsu.createCriteria(HrEmplDependent.class)
		 * .eq(HrEmplDependent.EMPLOYEE, hsu.get(Employee.class,
		 * dependentEmployeeId)) .eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S')
		 * .exists()) throw new ArahantWarning("That person has a spouse already
		 * assigned.");
		 */
	}

	/**
	 * @param dependentEmployeeId
	 * @throws ArahantException
	 */
	public void moveTo(final String dependentEmployeeId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final Person oldPerson = person;

		person = hsu.get(Person.class, dependentEmployeeId);

		bean.setPerson(person);

		hsu.saveOrUpdate(bean);

		//if they are a depdendent anywhere else, move that too
		for (HrEmplDependent dep : hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, oldPerson).list()) {
			dep.setPerson(person);
			hsu.saveOrUpdate(dep);
		}

		if (bean.getRelationshipType() == 'S' && hsu.createCriteria(Employee.class).eq(Employee.PERSONID, dependentEmployeeId).exists()) {
			//	need to do the reverse too
			final BHREmplDependent dep = new BHREmplDependent();
			dep.createSpouseBackLink(bean.getEmployee().getPersonId(), dependentEmployeeId, bean.getDateInactive());
			dep.insert();

			moveKidsOver();
		}

		//move benefits over
		for (HrBenefitJoin bj : oldPerson.getHrBenefitJoinsWhereCovered()) {
			bj.setCoveredPerson(person);
			hsu.saveOrUpdate(bj);
		}

		//move benefits over
		for (HrBenefitJoin bj : oldPerson.getHrBenefitJoinsWherePaying()) {
			bj.setPayingPerson(person);
			hsu.saveOrUpdate(bj);
		}

		//move notes over
		for (PersonNote pn : oldPerson.getPersonNotes()) {
			pn.setPerson(person);
			hsu.saveOrUpdate(pn);
		}

		//move forms over
		for (PersonForm pf : oldPerson.getPersonForms()) {
			pf.setPerson(person);
			hsu.saveOrUpdate(pf);
		}

		//move student verifications over
		for (StudentVerification sv : oldPerson.getStudentVerifications()) {
			sv.setPerson(person);
			hsu.saveOrUpdate(sv);
		}

		new BPerson(oldPerson).delete();
		//hsu.delete(oldPerson);
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	@SuppressWarnings("unchecked")
	public static BPerson[] searchDependents(String firstName, String lastName, String ssn, final int cap) throws ArahantException {

		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).setMaxResults(cap);

		if (!isEmpty(ssn))
			hcu.eq(Person.SSN, ssn);

		hcu.sizeNe(Person.DEP_JOINS_AS_DEPENDENT, 0);

		hcu.isNotEmployee();

		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);

		return BPerson.makeArray(hcu.list());
	}

	private String getBenefitStartDate(final short type) {
		for (final HrBenefitJoin dbj : bean.getBenefitJoins())
			if (dbj.getHrBenefitConfig().getHrBenefitCategory().getBenefitType() == type)
				return DateUtils.getDateFormatted(dbj.getCoverageStartDate());
		return "No";
	}

	/**
	 * @return
	 */
	public String getMedicalStartDate() {
		return getBenefitStartDate(HrBenefitCategory.HEALTH);
	}

	/**
	 * @return
	 */
	public String getDentalStartDate() {
		return getBenefitStartDate(HrBenefitCategory.DENTAL);
	}

	public String getVisionStartDate() {
		return getBenefitStartDate(HrBenefitCategory.VISION);
	}

	public String getLongTermCareStartDate() {
		return getBenefitStartDate(HrBenefitCategory.LONG_TERM_CARE);
	}

	/**
	 * @param personId
	 * @return
	 * @throws ArahantException
	 */
	public static BHREmplDependent[] loadDependentsFor(final String personId) throws ArahantException {

		return sort(makeArray(ArahantSession.getHSU().createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.EMPLOYEE).eq(Person.PERSONID, personId).list()));

	}

	/**
	 * @return Returns the amountCovered.
	 */
	public double getAmountCovered() {
		return amountCovered;
	}

	/**
	 * @param amountCovered The amountCovered to set.
	 */
	public void setAmountCovered(final double amountCovered) {
		this.amountCovered = amountCovered;
	}

	/**
	 *
	 */
	public void reactivate() {
		bean.setDateInactive(0);
	}

	/**
	 * @return
	 */
	public boolean getIsEmployee() {

		return ArahantSession.getHSU().exists(Employee.class, getPersonId());
	}

	/**
	 * @return
	 */
	public int getInactiveDate() {

		return bean.getDateInactive();
	}

	public boolean isCurrentlyActive() {
		return bean.getDateInactive() == 0 || DateUtils.now() <= bean.getDateInactive();
	}

	/**
	 * @param inactiveDate
	 */
	public void setInactiveDate(final int inactiveDate) {
		bean.setDateInactive(inactiveDate);
	}

	/**
	 * @param bene
	 * @return
	 */
	public boolean isEnrolled(final HrBenefitConfig bene) {

		for (final HrBenefitJoin dbj : bean.getBenefitJoins())
			if (dbj.getHrBenefitConfig().equals(bene))
				return true;

		return false;
	}

	/**
	 * @param hbc
	 * @return
	 */
	public boolean getCovered(HrBenefitConfig hbc) {

		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERAGE_END_DATE, 0).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).eq(HrBenefitJoin.RELATIONSHIP, bean).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, hbc).exists();
	}

	/**
	 * @return
	 */
	public char getRelationship() {
		if (bean == null)
			return 'U';
		return bean.getRelationshipType();
	}

	/**
	 * @return
	 */
	public String getEmployeeId() {

		return bean.getEmployee().getPersonId();
	}

	/**
	 * @return
	 */
	@Override
	public Person getPerson() {
		return bean.getPerson();
	}

	/**
	 * @param ids
	 * @param effectiveDate
	 * @throws ArahantException
	 *
	 * public static boolean delete(String[] dependentIds, int effectiveDate)
	 * throws ArahantException { boolean retVal=false; for (final String key :
	 * dependentIds) { final BHREmplDependent d=new BHREmplDependent(key);
	 * d.deleteWithNotify(effectiveDate); if (d.didInactivate) retVal=true; }
	 * return retVal; }
	 *
	 * /
	 **
	 * @return
	 */
	public String getDependentNameLFM() {


		return getPerson().getNameLFM();
	}

	/**
	 *
	 */
	public void unregisterWithAIEngine() {
		try {
			ArahantSession.getAI().remove(bean);
			ArahantSession.getAI().remove(person);
		} catch (JessException e) {
			 // I don't care
		}
	}

	/**
	 * @return
	 */
	public String getRelationshipId() {
		return bean.getRelationshipId();
	}

	/**
	 * Determines if this depedent is active anywhere
	 *
	 * @param dependentId
	 * @return
	 */
	public static boolean isActiveDependent(String dependentId) {
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.PERSONID, dependentId);

		hcu.joinTo(Person.DEP_JOINS_AS_DEPENDENT).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0);

		return hcu.exists();
	}

	/**
	 *
	 */
	@Override
	public String getStatus() {
		if (bean.getDateInactive() == 0)
			return "Active";

		if (bean.getDateInactive() >= DateUtils.now())
			return "Active (until " + DateUtils.getDateFormatted(bean.getDateInactive()) + ")";

		return "Inactive (as of " + DateUtils.getDateFormatted(bean.getDateInactive()) + ")";
	}

	/**
	 * @param personId
	 * @return
	 */
	public static BHREmplDependent[] listDependees(String personId) throws ArahantException {
		return makeArray(ArahantSession.getHSU().createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, personId).list());
	}

	/**
	 * @param personId
	 * @return
	 */
	public static String getDependeeReport(String personId) throws ArahantException {
		return new HRDependeeReport().build(new BPerson(personId), BHREmplDependent.listDependees(personId));
	}

	/**
	 * Searches for all dependent sponsors that match a benefit config and start
	 * date either the dependency flag must be inactive prior to the start date,
	 * or the employee must have an inactive employee status prior to the start
	 * date
	 *
	 * @param dependentId
	 * @param configId
	 * @param startDate
	 * @return
	 */
	public static BHREmplDependent[] getDependentSponsors(String dependentId, String configId, int startDate) throws ArahantException {
		BPerson person = new BPerson(dependentId);

		// look for all configs related to this person as the paying person, ordring by policy start date
		List<HrBenefitJoin> candidateBenefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, person.getPerson()).eq(HrBenefitJoin.PAYING_PERSON, person.getPerson()).orderBy(HrBenefitJoin.POLICY_START_DATE).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, configId).list();
		if (candidateBenefitJoins.size() > 0) {
			HrEmplDependent candidateDependentSponsor = candidateBenefitJoins.get(0).getRelationship();
			ArrayList<HrEmplDependent> list = new ArrayList<HrEmplDependent>();

			// we got one, so this the associated dependent sponsor is definately the one we will use,
			// but we still need to check the start date
			if (BHREmplDependent.checkDependentSponsorAgainstStartDate(candidateDependentSponsor, startDate))
				list.add(candidateDependentSponsor);

			return makeArray(list);
		}

		// get all dependent sponsors (we should have at least one dependent sponsor, or we were not given a dependent!!)
		List<HrEmplDependent> allDependentSponsors = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.PERSON).eq(Person.PERSONID, dependentId).orderBy(Person.LNAME).orderBy(Person.FNAME).list();

		// check for possibles match on the inactive matching
		ArrayList<HrEmplDependent> eligibleDependentSponsors = new ArrayList<HrEmplDependent>();
		for (HrEmplDependent candidateDependentSponsor : allDependentSponsors)
			if (BHREmplDependent.checkDependentSponsorAgainstStartDate(candidateDependentSponsor, startDate))
				eligibleDependentSponsors.add(candidateDependentSponsor);

		return makeArray(eligibleDependentSponsors);
	}

	protected static boolean checkDependentSponsorAgainstStartDate(HrEmplDependent dependentSponsor, int startDate) {
		// check if the relationship is inactive before the start date
		if (dependentSponsor.getDateInactive() != 0 && dependentSponsor.getDateInactive() <= startDate)
			return true;

		// check if the last status is an inactive employee status that starts before the start date
		HrEmplStatusHistory employeeStatusHistory = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.EMPLOYEE).eq(Employee.PERSONID, dependentSponsor.getEmployee().getPersonId()).first();
		if (employeeStatusHistory.getHrEmployeeStatus().getActive() == 'N' && employeeStatusHistory.getEffectiveDate() <= startDate)
			return true;

		return false;
	}
	private int loadedDateInactive = 0;

	protected void warnIfActiveAndInactive() {
		if (this.loadedDateInactive != bean.getDateInactive()) {
			List<HrEmplDependent> otherRelationships = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).ne(HrEmplDependent.DEP_KEY, bean.getRelationshipId()).eq(HrEmplDependent.PERSON, bean.getPerson()).eq(HrEmplDependent.DATE_INACTIVE, 0).list();

			String sponsors = "";
			for (HrEmplDependent otherRelationship : otherRelationships) {
				if (sponsors.length() > 0)
					sponsors += ",";
				sponsors += otherRelationship.getEmployee().getNameLFM();
				sponsors += " (";
				sponsors += otherRelationship.getEmployee().getUnencryptedSsn();
				sponsors += ")";
			}

			if (sponsors.length() > 0)
				ArahantSession.addReturnMessage("The following Employee Sponsors still have Active relationships to this dependent: " + sponsors);
		}
	}

	/**
	 * @return
	 */
	@Override
	public String getMiddleName() {

		return person.getMname();
	}

	public Employee getEmployee() {
		return bean.getEmployee();
	}

	public BHRBenefitJoin[] getBenefitJoins() {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bean.getPerson()).list());
	}

	public boolean enrolledInBenefit(String benefitId) {

		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPerson().getPersonId()).exists();
	}

	public boolean enrolledInApprovedBenefit(String benefitId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPerson().getPersonId()).exists();
	}

	public boolean enrolledInCategory(String categoryId) {
		List possibleIds = new ArrayList();
		possibleIds.add(bean.getPerson().getPersonId());

		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'N').in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).exists();
	}

	public boolean enrolledInApprovedCategory(String categoryId) {
		List possibleIds = new ArrayList();
		possibleIds.add(bean.getPerson().getPersonId());

		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).ne(HrBenefitJoin.COVERAGE_START_DATE, 0).gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0).eq(HrBenefitJoin.APPROVED, 'Y').in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).exists();
	}

	@Override
	public boolean alreadyEnrolledInConfig(String benefitId) {

		if (!isEmpty(benefitId)) {
			boolean ret;

			List possibleIds = new ArrayList();

			possibleIds.add(bean.getPerson().getPersonId());

			ret = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'N').exists();

			List<String> configIds = (List) ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).list();

			if (!ret && !ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).in(HrBenefitConfig.BENEFIT_CONFIG_ID, configIds).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, new BPerson(bean.getEmployee().getPersonId()).getPerson()).eq(HrBenefitJoin.APPROVED, 'N').exists())
				ret = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean()).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).in(HrBenefitJoin.COVERED_PERSON_ID, possibleIds).eq(HrBenefitJoin.APPROVED, 'Y').exists();

			return ret;
		}

		return false;
	}

	public String getEnrollingPersonId() {
		if (getChangeRequest().getRealRecord() != null)
			return getChangeRequest().getRealRecord().getPerson().getPersonId();

		return getChangeRequest().getChangeRecord().getDependentId();
	}
	
	private HrEmplDependentChangeRequest pcr;

	private HrEmplDependentChangeRequest getChangeRequest() {
		if (pcr != null)
			return pcr;

		//System.out.println("looking for "+bean.getRelationshipId());

		pcr = ArahantSession.getHSU().createCriteria(HrEmplDependentChangeRequest.class).eq(HrEmplDependentChangeRequest.DEP_PENDING, bean).eq(HrEmplDependentChangeRequest.CHANGE_STATUS, HrEmplDependentChangeRequest.STATUS_PENDING).first();
		if (pcr == null)
			throw new ArahantException("Could not find pending dep relationship with relationship id of " + bean.getRelationshipId());

		return pcr;
	}

	public String getEnrollingRelationshipId() {
		if (getChangeRequest().getRealRecord() != null)
			return getChangeRequest().getRealRecord().getRelationshipId();

		return getChangeRequest().getChangeRecord().getRelationshipId();
	}

	public static void terminate(String[] ids, int effectiveDate) {
		for (String id : ids)
			new BHREmplDependent(id).terminate(effectiveDate);
	}

	public void terminate(final int effectiveDate) {
		//if I have no real record, just get rid of me and my change record

		//we don't actually delete, need to mark inactive
		if (effectiveDate == 0)
			bean.setDateInactive(DateUtils.addDays(DateUtils.now(), -1));
		else
			bean.setDateInactive(DateUtils.addDays(effectiveDate, -1));
		update();
		ArahantSession.getHSU().flush();

	}
}
