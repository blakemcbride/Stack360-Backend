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
package com.arahant.utils;

import com.arahant.beans.*;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BSearchMetaInput;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.exception.GenericJDBCException;

@SuppressWarnings("unchecked")
final public class HibernateCriteriaUtil<T extends IArahantBean> {

	private final static transient ArahantLogger logger = new ArahantLogger(HibernateCriteriaUtil.class);
	private final Criteria criteria;
	private Criteria criteriaWithoutOrder;
//	private Session session;
	private String className;
	private Class clazz;
	private String alias = "";
	private boolean aiIntegrate;
	private boolean noCompanyFilter;
	private int cap = 0;
	private static int numberOfReports = 0;

	/**
	 * This method applies the company specific filters for multiple company
	 * support.
	 *
	 * @param clazz The class of the bean being queried for
	 */
	private void applyFilters(Class clazz) {
		if (noCompanyFilter)
			return;

		if (clazz.equals(Employee.class))
			filterToCurrentCompanyEmployees();
		if (clazz.equals(Person.class))
			filterToCurrentCompanyPerson();
		if (clazz.equals(Applicant.class))
			filterToCurrentCompanyApplicant();
		if (clazz.equals(PersonH.class))
			filterToCurrentCompanyPersonHistory();
		if (clazz.equals(OrgGroup.class))
			filterToCurrentCompanyOrgGroups();
		if (clazz.equals(CompanyDetail.class))
			filterToCurrentCompanyOrgGroups();
		if (clazz.equals(Timesheet.class))
			filterToCurrentCompanyTimesheets();
		if (clazz.equals(HrBenefitCategory.class))
			filterToCurrentCompanyBenefitCategories();
		if (clazz.equals(HrBenefit.class))
			filterToCurrentCompanyBenefits();
		if (clazz.equals(HrBenefitConfig.class))
			filterToCurrentCompanyBenefitConfigs();
		if (clazz.equals(VendorCompany.class))
			filterToCurrentCompanyClientOrVendors();
		if (clazz.equals(ClientCompany.class))
			filterToCurrentCompanyClientOrVendors();
		if (clazz.equals(ProductType.class))
			filterToCurrentCompanyProductTypes();
		if (clazz.equals(ProductService.class))
			filterToCurrentCompanyProductServices();
		if (clazz.equals(HrEmplStatusHistory.class))
			filterToCurrentCompanyStatusHistories();
		if (clazz.equals(PersonForm.class))
			filterToCurrentCompanyPersonForms();
		if (clazz.equals(ApplicantSource.class))
			filterToCurrentCompanyApplicantSources();
		if (clazz.equals(ApplicantStatus.class))
			filterToCurrentCompanyApplicantStatuses();
		if (clazz.equals(ApplicantAppStatus.class))
			filterToCurrentCompanyApplicantAppStatuses();
		if (clazz.equals(ApplicantQuestion.class))
			filterToCurrentCompanyApplicantQuestions();
		if (clazz.equals(AppointmentLocation.class))
			filterToCurrentCompanyAppointmentLocations();
		if (clazz.equals(CompanyQuestion.class))
			filterToCurrentCompanyCompanyQuestions();
		if (clazz.equals(ContactQuestion.class))
			filterToCurrentCompanyContactQuestions();
		if (clazz.equals(ClientStatus.class))
			filterToCurrentCompanyClientStatuses();
		if (clazz.equals(ProspectStatus.class))
			filterToCurrentCompanyProspectStatuses();
		if (clazz.equals(ProspectSource.class))
			filterToCurrentCompanyProspectSources();
		if (clazz.equals(ProspectType.class))
			filterToCurrentCompanyProspectTypes();
		if (clazz.equals(RateType.class))
			filterToCurrentCompanyRateTypes();
		if (clazz.equals(ProspectCompany.class))
			filterToCurrentCompanyProspectCompanies();
		if (clazz.equals(BankDraftBatch.class))
			filterToCurrentCompanyBankDraftBatch();
		if (clazz.equals(GlAccount.class))
			filterToCurrentCompanyGlAccount();
		if (clazz.equals(Service.class))
			filterToCurrentCompanyService();
		if (clazz.equals(BankAccount.class))
			filterToCurrentCompanyBankAccount();
		if (clazz.equals(PaySchedule.class))
			filterToCurrentCompanyPaySchedule();
		if (clazz.equals(ProjectCategory.class))
			filterToCurrentCompanyProjectCategory();
		if (clazz.equals(ProjectStatus.class))
			filterToCurrentCompanyProjectStatus();
		if (clazz.equals(ProjectType.class))
			filterToCurrentCompanyProjectType();
		if (clazz.equals(ProjectPhase.class))
			filterToCurrentCompanyProjectPhase();
		if (clazz.equals(Route.class))
			filterToCurrentCompanyRoute();
		if (clazz.equals(Project.class))
			filterToCurrentCompanyProject();
		if (clazz.equals(StandardProject.class))
			filterToCurrentCompanyStandardProject();


		for (Class interf : clazz.getInterfaces())
			if (interf.equals(CompanyFiltered.class)) {
				filterToCurrentCompanyFiltered();
				break;
			}
		while (clazz.getSuperclass() != null) {
			if (clazz.equals(Filtered.class)) {
				filterToCurrentCompanyFiltered();
				break;
			}
			clazz = clazz.getSuperclass();
		}
	}

	HibernateCriteriaUtil(final Session session, final Class clazz, boolean aiIntegrate) {
		//	this.session=session;
		criteria = session.createCriteria(clazz);
		className = clazz.getName();
		this.clazz = clazz;
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //I think I always use this
		criteriaWithoutOrder = session.createCriteria(clazz);
		criteriaWithoutOrder.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		this.aiIntegrate = aiIntegrate;
		noCompanyFilter = !ArahantSession.multipleCompanySupport;
		applyFilters(clazz);
	}

	HibernateCriteriaUtil(final Session session, final Class clazz, boolean aiIntegrate, boolean noCompanyFilter) {
		//	this.session=session;
		criteria = session.createCriteria(clazz);
		className = clazz.getName();
		this.clazz = clazz;
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //I think I always use this
		criteriaWithoutOrder = session.createCriteria(clazz);
		criteriaWithoutOrder.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		this.aiIntegrate = aiIntegrate;
		this.noCompanyFilter = noCompanyFilter;

		applyFilters(clazz);
	}

	HibernateCriteriaUtil(final Session session, final Class clazz, final String alias, boolean aiIntegrate, boolean noCompanyFilter) {
		//	this.session=session;
		criteria = session.createCriteria(clazz, alias);
		className = clazz.getName();
		this.clazz = clazz;
		this.alias = alias;
		criteriaWithoutOrder = session.createCriteria(clazz);
		criteriaWithoutOrder.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		//	addReturnField(alias);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //I think I always use this
		this.aiIntegrate = aiIntegrate;
		this.noCompanyFilter = noCompanyFilter;

		applyFilters(clazz);

	}

	HibernateCriteriaUtil(final Session session, final Class clazz, final String alias, boolean aiIntegrate) {
		//	this.session=session;
		criteria = session.createCriteria(clazz, alias);
		className = clazz.getName();
		this.clazz = clazz;
		this.alias = alias;
		criteriaWithoutOrder = session.createCriteria(clazz, alias);
		criteriaWithoutOrder.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		//	addReturnField(alias);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //I think I always use this
		this.aiIntegrate = aiIntegrate;
		noCompanyFilter = !ArahantSession.multipleCompanySupport;
		applyFilters(clazz);

	}

	private HibernateCriteriaUtil(final Criteria crit, boolean aiIntegrate) {
		criteria = crit;
		criteriaWithoutOrder = crit;
		this.aiIntegrate = aiIntegrate;
	}

	/**
	 * Allows the criteria returned to be sorted by a particular field.
	 *
	 * @param field The field to be sorted
	 * @param sortAsc Set to true for ascending order, false for descending
	 * order.
	 */
	public HibernateCriteriaUtil<T> orderBy(String field, boolean sortAsc) {
		if (sortAsc)
			return orderBy(field);
		else
			return orderByDesc(field);
	}

	/**
	 * Restricts the query to only those OrgGroups the Employee is allowed to
	 * see.
	 */
	public HibernateCriteriaUtil<T> restrictEmployeeOrgGroups() {
		List<String> orgs = AIProperty.getList("RestrictedOrgGroups");


		if (orgs.size() > 0) {
			Set<String> orgSet = new HashSet<String>(orgs);
			orgSet.addAll(orgs);

			for (String o : orgs)
				orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());

			joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgSet);
		}

		return this;
	}

	/**
	 * Restricts the query to only OrgGroups the user currently logged in is
	 * allowed to see.
	 */
	public HibernateCriteriaUtil<T> restrictOrgGroups() {

		List<String> orgs = AIProperty.getList("RestrictedOrgGroups");

		if (orgs.size() > 0) {
			Set<String> orgSet = new HashSet<String>(orgs);
			orgSet.addAll(orgs);

			for (String o : orgs)
				orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());

			in(OrgGroup.ORGGROUPID, orgSet);
		}

		return this;
	}

	private void addCriteria(Criterion crit) {
		criteria.add(crit);
		criteriaWithoutOrder.add(crit);
	}

	/**
	 * Returns a query of all active dependents for the current employee.
	 */
	public HibernateCriteriaUtil<T> activeDependent() {
		filterToCurrentCompanyPerson();
		return sql("{alias}.person_id in (select dep.dependent_id from hr_empl_dependent dep where dep.date_inactive = 0 or dep.date_inactive > " + DateUtils.now() + ") and {alias}.person_id not in (select person_id from employee)");
	}

	/**
	 * Returns a query of whether the current person is an employee or a
	 * dependent.
	 */
	public HibernateCriteriaUtil<T> employeeOrDependent() {
		filterToCurrentCompanyPerson();
		return sql("({alias}.person_id in (select dependent_id from hr_empl_dependent) or {alias}.person_id in (select person_id from employee))");
	}

	public HibernateScrollUtil<T> getPage(BSearchMetaInput bSearchMetaInput) {
		if (bSearchMetaInput.isUsingPaging())
			return getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			return scroll();
	}

	/**
	 * Returns a query of all inactive dependents for the current employee.
	 */
	public HibernateCriteriaUtil<T> inactiveDependent() {
		return sql("{alias}.person_id in (select dep.dependent_id from hr_empl_dependent dep) and {alias}.person_id not in (select person_id from employee)"
				+ " and {alias}.person_id not in (select dep.dependent_id from hr_empl_dependent dep where dep.date_inactive = 0 or dep.date_inactive > " + DateUtils.now() + ")");
	}

	/**
	 * Returns the integer value of a number field in the query.
	 */
	public int intValue() {
		try {
			Object o = criteria.list().get(0);
			if (o instanceof Short)
				return (Short) o;
			if (o instanceof Integer)
				return (Integer) o;
			if (o instanceof Number)
				return ((Number) o).intValue();
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the short value of a number field in the query.
	 */
	public int shortValue() {
		try {
			Object obj = criteria.list().get(0);
			if (obj instanceof Short)
				return (Short) obj;
			if (obj instanceof Integer)
				return (Integer) obj;
			if (obj instanceof Number)
				return ((Number) obj).shortValue();
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns a query of whether or not the current person is a dependent.
	 */
	public HibernateCriteriaUtil<T> isDependent() {
		return sql("{alias}.person_id in (select dep.dependent_id from hr_empl_dependent dep) and {alias}.person_id not in (select person_id from employee)");
	}

	/**
	 * Returns a query where the given field is not null.
	 *
	 * @param propName Field to do the null check on.
	 */
	public HibernateCriteriaUtil<T> isNotNull(String propName) {
		return notNull(propName);
	}

	private String addAlias(final String propName) {
		if (alias.equals(""))
			return propName;
		return alias + "." + propName;
	}

	/**
	 * Returns a query that determines if any of the values of a collection are
	 * equal to a given field.
	 *
	 * @param propName The field compared to the collection of values.
	 * @param values A collection of values to be compared to propName.
	 */
	final public HibernateCriteriaUtil<T> in(String propName, final Collection values) {
		propName = addAlias(propName);

		if (values.isEmpty()) {
			//there is no way it could be in, so I need to force a failure 
			//without messing up the query
			addCriteria(Restrictions.isNotNull(propName));
			addCriteria(Restrictions.isNull(propName));
		} else {
			//if the in clause is too big, the database chokes, so lets try breaking big ones up)
			final int max = 500;
			if (values.size() > max) {
				List<HibernateCriterionUtil> crits = new LinkedList<HibernateCriterionUtil>();
				List<Object> x = new ArrayList<>(max);

				while (values.size() > 0) {

					int count = 0;

					for (Object o : values) {
						x.add(o);
						if (++count > max)
							break;
					}

					values.removeAll(x);

					crits.add(makeCriteria().in(propName, x));
					x.clear();
				}
				HibernateCriterionUtil or = makeCriteria();
				or.or(crits);
				or.add();
			} else
				addCriteria(Restrictions.in(propName, values));
		}
		return this;
	}

	/**
	 * Returns a query that determines if any of the values of a collection are
	 * not equal to a given field.
	 *
	 * @param propName The field compared to the collection of values.
	 * @param values The collection of values compared to propName;
	 */
	final public HibernateCriteriaUtil<T> notIn(String propName, final Collection values) {
		propName = addAlias(propName);

		//if there are no values, then I don't need the criterion
		if (!values.isEmpty())
			addCriteria(Restrictions.not(Restrictions.in(propName, values)));
		return this;
	}

	/**
	 * Returns a query that determines if a particular number field is between a
	 * high and low value.
	 */
	final public HibernateCriteriaUtil<T> between(String propName, final int low, final int high) {
		propName = addAlias(propName);
		addCriteria(Restrictions.between(propName, low, high));
		return this;
	}

	/**
	 * Returns a query that determines if a field is equal to a given String
	 * value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, String value) {
		String origName = propName;
		propName = addAlias(propName);
		//INDEX: change when index changes
		//	addCriteria(Restrictions.eq(propName,value).ignoreCase());
		if (value == null)
			value = "";
		if (origName.equals(Person.SSN))
			try {
				value = Crypto.encryptTripleDES(Person.encKey(), value);
			} catch (Exception e) {
				logger.error("Error encrypting a SSN", e);
			}
		if (origName.equals(Project.PROJECTNAME))
			addCriteria(Restrictions.or(
					Restrictions.or(
					Restrictions.or(
					Restrictions.eq(propName, "" + value.toLowerCase()).ignoreCase(),
					Restrictions.eq(propName, " " + value.toLowerCase()).ignoreCase()),
					Restrictions.or(
					Restrictions.eq(propName, "  " + value.toLowerCase()).ignoreCase(),
					Restrictions.eq(propName, "   " + value.toLowerCase()).ignoreCase())),
					Restrictions.or(
					Restrictions.or(
					Restrictions.eq(propName, "    " + value.toLowerCase()).ignoreCase(),
					Restrictions.eq(propName, "     " + value.toLowerCase()).ignoreCase()),
					Restrictions.or(
					Restrictions.eq(propName, "      " + value.toLowerCase()).ignoreCase(),
					Restrictions.eq(propName, "       " + value.toLowerCase()).ignoreCase()))));
		else
			addCriteria(Restrictions.eq(propName, value.toLowerCase()).ignoreCase());
		logSearch(propName);
		return this;
	}

	/**
	 * Returns a query that determines if a field is equal to a given Object
	 * value.
	 */
	private HibernateCriteriaUtil<T> eqPrivate(String propName, final Object value) {
		if (value == null)
			return isNull(propName);

		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	final public HibernateCriteriaUtil<T> eq(String propName, final IArahantBean value) {
		return eqPrivate(propName, value);
	}

	final public HibernateCriteriaUtil<T> eq(String propName, final Date value) {
		return eqPrivate(propName, value);
	}

	final public HibernateCriteriaUtil<T> eq(String propName, final Number value) {
		return eqPrivate(propName, value);
	}

	/**
	 * Returns a query that determines if a field is equal to a given long
	 * value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, final long value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is equal to the Object value
	 * OR equal to null
	 */
	final public HibernateCriteriaUtil<T> eqOrNull(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.or(Restrictions.isNull(propName), Restrictions.eq(propName, value)));
		return this;
	}

	final public HibernateCriteriaUtil<T> eqJoinedFieldOrNull(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		addCriteria(Restrictions.or(Restrictions.eqProperty(prop1, prop2), Restrictions.isNull(prop1)));
		return this;
	}

	/**
	 * Sorts the query by a particular field in ascending order.
	 */
	final public HibernateCriteriaUtil<T> orderBy(String propName) {
		if (propName.equals(Person.FNAME)
				|| propName.equals(Person.MNAME)
				|| propName.equals(Person.LNAME)
				|| propName.equals(OrgGroup.NAME)
				|| propName.equals(Project.DESCRIPTION)
				|| propName.equals(Project.REFERENCE)
				|| propName.equals(HrBenefit.NAME)
				|| propName.equals(HrBenefitConfig.NAME)
				|| propName.equals(HrBenefitCategory.DESCRIPTION)) {
			propName = addAlias(propName);
			final Order o = org.hibernate.criterion.Order.asc(propName).ignoreCase();
			criteria.addOrder(o);
		} else if (propName.equals(Person.SSN)) {
			//  ignore
		} else {
			propName = addAlias(propName);
			final Order o = org.hibernate.criterion.Order.asc(propName);
			criteria.addOrder(o);
		}
		return this;
	}

	/**
	 * Sorts the query by a particular field in descending order.
	 */
	final public HibernateCriteriaUtil<T> orderByDesc(String propName) {
		if (propName.equals(Person.FNAME)
				|| propName.equals(Person.MNAME)
				|| propName.equals(Person.LNAME)
				|| propName.equals(OrgGroup.NAME)
				|| propName.equals(Project.DESCRIPTION)
				|| propName.equals(Project.REFERENCE)
				|| propName.equals(HrBenefit.NAME)
				|| propName.equals(HrBenefitConfig.NAME)
				|| propName.equals(HrBenefitCategory.DESCRIPTION)) {
			propName = addAlias(propName);
			final Order o = org.hibernate.criterion.Order.desc(propName).ignoreCase();
			criteria.addOrder(o);
		} else if (propName.equals(Person.SSN)) {
			// ignore
		} else {
			propName = addAlias(propName);
			final Order o = org.hibernate.criterion.Order.desc(propName);
			criteria.addOrder(o);
		}

		return this;
	}

	/**
	 * Returns a query that determines if a particular field is equal another
	 * field.
	 */
	final public HibernateCriteriaUtil<T> propEq(String propName1, String propName2) {
		propName1 = addAlias(propName1);
		propName2 = addAlias(propName2);
		addCriteria(Restrictions.eqProperty(propName1, propName2));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is null.
	 */
	final public HibernateCriteriaUtil<T> isNull(String propName) {
		propName = addAlias(propName);
		addCriteria(Restrictions.isNull(propName));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is not null.
	 */
	final public HibernateCriteriaUtil<T> notNull(String propName) {
		propName = addAlias(propName);
		addCriteria(Restrictions.isNotNull(propName));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is not equal to a
	 * given Object value.
	 */
	final public HibernateCriteriaUtil<T> ne(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.ne(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is equal to a given
	 * char value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, final char value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is equal to a given
	 * char value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, final Character value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	/**
	 * Returns the number of items returned by the query in no particular order.
	 */
	final public int countNoOrder() {
		try {
			criteriaWithoutOrder.setProjection(Projections.countDistinct("id"));
			long count = (Long) criteriaWithoutOrder.list().get(0);
			return (int) count;
		} catch (Exception e) {
			logger.error(e);
			return 0;
		}
	}

	/**
	 * Returns the number of items returned by the current query.
	 */
	final public int count() {
		try {
			criteria.setProjection(Projections.count("id"));
			long count = (Long) criteria.list().get(0);
			if (count > Integer.MAX_VALUE)
				throw new ArahantException("count() returning long value greater than max int value: " + count);
			return (int) count;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the number of items returned by the current query for a given
	 * field.
	 */
	final public int count(String propName) {
		try {
			propName = addAlias(propName);
			criteria.setProjection(Projections.count(propName));
			long count = (Long) criteria.list().get(0);
			if (count > Integer.MAX_VALUE)
				throw new ArahantException("count(propName) returning long value greater than max int value: " + count);
			return (int) count;
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * Returns the float value of a number field in the query.
	 */
	final public float floatVal() {
		try {
			return ((Number) criteria.list().get(0)).floatValue();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the double value of a number field in the query.
	 */
	final public double doubleVal() {
		try {
			return ((Number) criteria.list().get(0)).doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the String value of a field in the query.
	 */
	final public String stringVal() {
		try {
			return (String) criteria.list().get(0);
		} catch (Exception e) {
			return "";
		}
	}

	final public Character charVal() {
		try {
			return (Character) criteria.list().get(0);
		} catch (Exception e) {
			return ' ';
		}
	}

	/**
	 * Returns a query that determines whether a field is equal to a given
	 * integer value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, final int value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines whether a field is equal to a given short
	 * value.
	 */
	final public HibernateCriteriaUtil<T> eq(String propName, final short value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.eq(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field contains any part of the given
	 * String value.
	 */
	final public HibernateCriteriaUtil<T> like(String propName, String value) {
		if (value == null || value.equals("") || value.equals("*") || value.equals("%"))
			return this;
		if (propName.equals(Person.SSN))
			return eq(propName, value);

		String origName = propName;
		propName = addAlias(propName);

		if (value.indexOf('%') == -1 && value.indexOf('*') == -1)
			return eq(propName, value);

		value = value.replaceAll("\\*", "%");
		if (origName.equals(Project.PROJECTNAME) && value.endsWith("%") && !value.startsWith("%"))
			addCriteria(Restrictions.or(
					Restrictions.or(
					Restrictions.or(
					Restrictions.like(propName, "" + value.toLowerCase()).ignoreCase(),
					Restrictions.like(propName, " " + value.toLowerCase()).ignoreCase()),
					Restrictions.or(
					Restrictions.like(propName, "  " + value.toLowerCase()).ignoreCase(),
					Restrictions.like(propName, "   " + value.toLowerCase()).ignoreCase())),
					Restrictions.or(
					Restrictions.or(
					Restrictions.like(propName, "    " + value.toLowerCase()).ignoreCase(),
					Restrictions.like(propName, "     " + value.toLowerCase()).ignoreCase()),
					Restrictions.or(
					Restrictions.like(propName, "      " + value.toLowerCase()).ignoreCase(),
					Restrictions.like(propName, "       " + value.toLowerCase()).ignoreCase()))));
		else if (origName.equals(Project.PROJECTNAME) && value.startsWith("%")) {
			value = value.substring(1);
			addCriteria(
					Restrictions.or(
					Restrictions.like(propName, "%" + value.toLowerCase()).ignoreCase(),
					Restrictions.like(propName, "% " + value.toLowerCase()).ignoreCase()));
		} else
			addCriteria(Restrictions.like(propName, value).ignoreCase());
		//addCriteria(Restrictions.like(propName,value.toUpperCase().replaceAll("\\*", "%")).ignoreCase());
		logSearch(propName);
		return this;
	}
	private boolean didTimesheetCompanyFilter = false;

	private HibernateCriteriaUtil<T> filterToCurrentCompanyTimesheets() {
		if (!noCompanyFilter && !didTimesheetCompanyFilter) {
			//check to see if they are employee in company or dependent in company
			didTimesheetCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("({alias}.person_id in (select person_id from person where company_id='" + currentCompanyId + "')"
					+ " or {alias}.person_id in (select person_id from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "'))");
		}
		return this;
	}

	private void logSearch(final String propName) {
		/*	try
		 {
		 FileWriter fw=new FileWriter("c:\\searchlog.csv",true);
		 fw.write(className+","+propName+"\n");
		 fw.flush();
		 fw.close();

		 }
		 catch (Exception e)
		 {
		 e.printStackTrace();
		 }
		 */
	}

	/**
	 * Returns a query that determines if a field is greater than or equal a
	 * given String value.
	 */
	final public HibernateCriteriaUtil<T> ge(String propName, final String value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.ge(propName, value).ignoreCase());
		return this;
	}

	/**
	 * Returns a query that determines if a field is greater than a given String
	 * value.
	 */
	final public HibernateCriteriaUtil<T> gt(String propName, final String value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.gt(propName, value).ignoreCase());
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than a given String
	 * value.
	 */
	final public HibernateCriteriaUtil<T> lt(String propName, final String value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.lt(propName, value).ignoreCase());
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than or equal to a
	 * given String value.
	 */
	final public HibernateCriteriaUtil<T> le(String propName, final String value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.le(propName, value).ignoreCase());
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than or equal to a
	 * given String value.
	 */
	final public HibernateCriteriaUtil<T> le(String propName, final Date value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.le(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than a given String
	 * value.
	 */
	final public HibernateCriteriaUtil<T> lt(String propName, final Date value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.lt(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is greater than or equal to a
	 * given Object value.
	 */
	final public HibernateCriteriaUtil<T> ge(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.ge(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is greater than a given Object
	 * value.
	 */
	final public HibernateCriteriaUtil<T> gt(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.gt(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than a given Object
	 * value.
	 */
	final public HibernateCriteriaUtil<T> lt(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.lt(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a field is less than or equal to a
	 * given Object value.
	 */
	final public HibernateCriteriaUtil<T> le(String propName, final Object value) {
		propName = addAlias(propName);
		addCriteria(Restrictions.le(propName, value));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is equal to a given
	 * size.
	 */
	final public HibernateCriteriaUtil<T> sizeEq(String propName, final int size) {
		//If you have a sizeEq or Ne that is giving Unknown collection role: it may be because
		//you can't do the size from a derived class, like check the size on a person collection
		//from a prospect
		propName = addAlias(propName);
		addCriteria(Restrictions.sizeEq(propName, size));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than a
	 * given size.
	 */
	public HibernateCriteriaUtil<T> sizeGt(String propName, final int size) {
		propName = addAlias(propName);
		addCriteria(Restrictions.sizeGt(propName, size));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is not equal to a
	 * given size.
	 */
	final public HibernateCriteriaUtil<T> sizeNe(String propName, final int size) {
		propName = addAlias(propName);
		addCriteria(Restrictions.sizeNe(propName, size));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than or
	 * equal to a given integer value.
	 */
	final public HibernateCriteriaUtil<T> ge(String propName, final int x) {
		//propName = addAlias(propName);
		ge(propName, (Integer) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than or
	 * equal to a given short value.
	 */
	final public HibernateCriteriaUtil<T> ge(String propName, final short x) {
		//propName = addAlias(propName);
		ge(propName, (Short) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than or
	 * equal to a given short value.
	 */
	final public HibernateCriteriaUtil<T> ge(String propName, final Date x) {
		propName = addAlias(propName);
		addCriteria(Restrictions.ge(propName, x));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is less than or
	 * equal to a given integer value.
	 */
	final public HibernateCriteriaUtil<T> le(String propName, final int x) {
		//propName = addAlias(propName);
		le(propName, (Integer) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is less than or
	 * equal to a given short value.
	 */
	final public HibernateCriteriaUtil<T> le(String propName, final short x) {
		//propName = addAlias(propName);
		le(propName, (Short) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is less than a
	 * given integer value.
	 */
	final public HibernateCriteriaUtil<T> lt(String propName, final int x) {
		//propName = addAlias(propName);
		lt(propName, (Integer) x);
		return this;
	}

	final public HibernateCriteriaUtil<T> lt(String propName, final short x) {
		//propName = addAlias(propName);
		lt(propName, (Short) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than a
	 * given integer value.
	 */
	final public HibernateCriteriaUtil<T> gt(String propName, final int x) {
		//propName = addAlias(propName);
		gt(propName, (Integer) x);
		return this;
	}

	/**
	 * Returns a query that determines if a field is greater than a given double
	 * value.
	 */
	final public HibernateCriteriaUtil<T> gt(String propName, final double value) {
		//propName = addAlias(propName);
		gt(propName, (Double) value);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is greater than a
	 * given short value.
	 */
	final public HibernateCriteriaUtil<T> gt(String propName, final short x) {
		//propName = addAlias(propName);
		gt(propName, (Short) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field not equal to a
	 * given integer value.
	 */
	final public HibernateCriteriaUtil<T> ne(String propName, final int x) {
		//propName = addAlias(propName);
		ne(propName, (Integer) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field not equal to a
	 * given short value.
	 */
	final public HibernateCriteriaUtil<T> ne(String propName, final short x) {
		//propName = addAlias(propName);
		ne(propName, (Short) x);
		return this;
	}

	/**
	 * Returns a query that determines if a particular field not equal to a
	 * given char value.
	 */
	final public HibernateCriteriaUtil<T> ne(String propName, final char x) {
		//propName = addAlias(propName);
		ne(propName, (Character) x);
		return this;
	}

	/**
	 * Sets the maximum number of results a query can return.
	 */
	final public HibernateCriteriaUtil<T> setMaxResults(final int max) {
		if (max > 0)
			criteria.setMaxResults(max);

		cap = max;
		return this;
	}

	/**
	 * Returns the current query as a list.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	final public List<T> list() {

		if (clazz == IAuditedBean.class) {
		}
		try {
			List<T> ret = criteria.list();

			if (numberOfReports < 20 && ret.size() > 1000) {
				logger.error("hcu.list(" + className + ") is returning " + ret.size() + " elements", new Throwable());
				numberOfReports++;
			}

			//logger.debug("Query for "+className+" took "+((end.getTime()-start.getTime())/1000)+" seconds");
			if (aiIntegrate)
				ArahantSession.runAI();


			if (cap > 0 && ret.size() > cap)
				ret = ret.subList(0, cap);

			if (ret.size() > 0 && ret.get(0) instanceof AuditedBean)
				for (T ab : ret)
					((AuditedBean) ab).saveOriginalBean();

			return ret;
		} /*
		 catch (org.hibernate.exception.JDBCConnectionException e)
		 {
		 logger.error(e);
		 throw new ArahantException("Error communicating with database.");
		 }
		 catch (HibernateException e)
		 {
		 if ((e.getCause() instanceof IOException) || (e.getCause() instanceof SocketException))
		 {
		 logger.error(e);
		 throw new ArahantException("Error communicating with database.");
		 }
		 else
		 {
		 logger.error(e);
		 e.printStackTrace();
		 return new LinkedList<T>();
		 }
		 }
		 * */ catch (Exception e) {
			logger.error(e);
			return new LinkedList<T>();
		}
	}

	/**
	 * Returns the maximum value of a particular field.
	 */
	final public HibernateCriteriaUtil<T> max(String propName) {
		propName = addAlias(propName);
		final Projection p = Projections.max(propName);
		criteria.setProjection(p);
		return this;
	}

	/**
	 * Returns the maximum value of a particular field using an alias.
	 */
	final public HibernateCriteriaUtil<T> max(String propName, final String alias) {
		propName = addAlias(propName);
		final Projection p = Projections.max(propName);
		Projections.alias(p, alias);
		criteria.setProjection(p);
		return this;
	}

	/**
	 * Returns the minimum value of a particular field using an alias.
	 */
	final public HibernateCriteriaUtil<T> min(String propName, final String alias) {
		propName = addAlias(propName);
		final Projection p = Projections.min(propName);
		Projections.alias(p, alias);
		criteria.setProjection(p);
		return this;
	}

	/**
	 * Returns the current query as a set.
	 */
	@SuppressWarnings("unchecked")
	final public Set<T> set() {
		final List l = criteria.list();
		final HashSet<T> ret = new HashSet<T>(l.size());
		ret.addAll(l);
		return ret;
	}

	final public void setExample(final Object example) {
		final Example ex = Example.create(example);
		ex.excludeZeroes();
		addCriteria(ex);
	}

	final public HibernateCriterionUtil makeCriteria() {
		return new HibernateCriterionUtil(criteria, alias);
	}

	final public Criteria getCriteria() {
		return criteria;
	}

	final public HibernateCriteriaUtil<T> joinTo(String propName) {
		propName = addAlias(propName);
		HibernateCriteriaUtil<T> h = new HibernateCriteriaUtil<T>(criteria.createCriteria(propName), aiIntegrate);
		h.criteriaWithoutOrder = criteriaWithoutOrder.createCriteria(propName);
		return h;
		//return new HibernateCriteriaUtil<T>(criteria.setFetchMode(propName,FetchMode.LAZY),aiIntegrate);
	}
	/*
	 final public HibernateCriteriaUtil<T> joinToEager(String propName)
	 {
	 propName=addAlias(propName);
	 HibernateCriteriaUtil<T> h= new HibernateCriteriaUtil<T>(criteria.setFetchMode(propName, FetchMode.JOIN),aiIntegrate);
	 h.criteriaWithoutOrder=criteriaWithoutOrder.createCriteria(propName);
	 return h;
	 //return new HibernateCriteriaUtil<T>(criteria.setFetchMode(propName,FetchMode.LAZY),aiIntegrate);
	 }
	 */

	final public HibernateCriteriaUtil<T> joinTo(String propName, final String alias) {
		if (alias.indexOf('-') != -1)
			throw new Error("Bad join alias " + alias);
		propName = addAlias(propName);
		HibernateCriteriaUtil<T> h = new HibernateCriteriaUtil<T>(criteria.createCriteria(propName, alias), aiIntegrate);
		h.criteriaWithoutOrder = criteriaWithoutOrder.createCriteria(propName, alias);
		return h;
	}

	final public HibernateScrollUtil<T> scroll() {
        if (cap > 0)
            criteria.setMaxResults(cap);
		HibernateScrollUtil<T> scr = new HibernateScrollUtil<T>(criteria.scroll(ScrollMode.FORWARD_ONLY));
		return scr;
	}

	private HibernateScrollUtil<T> scrollMS() {
		//Microsoft SQL Server has a problem with forward only scrolling in circumstances
		//where you are not starting with the very first result (AKA Paging).
		//We only verified this with Microsoft SQL Server 2008 R2
        if (cap > 0)
            criteria.setMaxResults(cap);
		HibernateScrollUtil<T> scr = new HibernateScrollUtil<T>(criteria.scroll());
		return scr;
	}

	final public HibernateScrollUtil<T> scroll(int startRow) {
        if (cap > 0)
            criteria.setMaxResults(cap);
        criteria.setFirstResult(startRow);
		ScrollableResults sr = criteria.scroll();
		HibernateScrollUtil<T> scr = new HibernateScrollUtil<T>(sr);
		return scr;
	}

	final public HibernateScrollUtil<T> getPage(int start, int size) {
		cap = size;
		if (cap == 0)
			cap = 50; //never load all on a page
		criteria.setFirstResult(start);
        criteria.setMaxResults(cap);
		//use scrollMS instead of scroll because Microsoft SQL Server has a problem
		if (StartupListener.DB_TYPE == StartupListener.DB_MSSQL)
			return scrollMS();
		else
			return scroll();
	}

	final public HibernateScrollUtil<T> scrollPage(int start, int size) {
		criteria.setFirstResult(start);
		criteria.setMaxResults(size);
		if (StartupListener.DB_TYPE == StartupListener.DB_MSSQL)
			return scrollMS();
		else
			return scroll();
	}

	final public HibernateCriteriaUtil<T> distinct() {
		//I do it all the time	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return this;
	}

	final public HibernateCriteriaUtil<T> leftJoinTo(String propName) {
		propName = addAlias(propName);
		HibernateCriteriaUtil<T> h = new HibernateCriteriaUtil<T>(criteria.createCriteria(propName, CriteriaSpecification.LEFT_JOIN), aiIntegrate);
		h.criteriaWithoutOrder = criteriaWithoutOrder.createCriteria(propName, CriteriaSpecification.LEFT_JOIN);
		return h;
	}

	final public HibernateCriteriaUtil<T> leftJoinTo(String propName, String alias) {
		propName = addAlias(propName);
		HibernateCriteriaUtil<T> h = new HibernateCriteriaUtil<T>(criteria.createCriteria(propName, alias, CriteriaSpecification.LEFT_JOIN), aiIntegrate);
		h.criteriaWithoutOrder = criteriaWithoutOrder.createCriteria(propName, CriteriaSpecification.LEFT_JOIN);
		return h;
	}

	/**
	 * Returns a query that determines if a given field is empty.
	 */
	final public HibernateCriteriaUtil<T> isEmpty(String propertyName) {
		propertyName = addAlias(propertyName);
		addCriteria(Restrictions.or(Restrictions.eq(propertyName, ""), Restrictions.isNull(propertyName)));
		return this;
	}

	/**
	 * Returns a query that determines if a given field is not empty.
	 */
	final public HibernateCriteriaUtil<T> isNotEmpty(String propertyName) {
		propertyName = addAlias(propertyName);
		addCriteria(Restrictions.and(Restrictions.ne(propertyName, ""), Restrictions.isNotNull(propertyName)));
		return this;
	}

	/**
	 * Returns a query that determines if either one of the given property names
	 * is equal to the given Object value.
	 */
	final public HibernateCriteriaUtil<T> orEq(String propertyName1, String propertyName2, final Object val) {
		propertyName1 = addAlias(propertyName1);
		propertyName2 = addAlias(propertyName2);
		addCriteria(Restrictions.or(Restrictions.eq(propertyName1, val), Restrictions.eq(propertyName2, val)));
		return this;
	}

	/**
	 * Returns a query that determines if either one of the given property names
	 * is null.
	 */
	final public HibernateCriteriaUtil<T> orNull(String propertyName1, String propertyName2) {
		propertyName1 = addAlias(propertyName1);
		propertyName2 = addAlias(propertyName2);
		addCriteria(Restrictions.or(Restrictions.isNull(propertyName1), Restrictions.isNull(propertyName2)));
		return this;
	}

	final public HibernateCriteriaUtil<T> leJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		addCriteria(Restrictions.leProperty(prop1, prop2));
		return this;
	}

	final public HibernateCriteriaUtil<T> ltJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		addCriteria(Restrictions.ltProperty(prop1, prop2));
		return this;
	}

	final public HibernateCriteriaUtil<T> eqJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		addCriteria(Restrictions.eqProperty(prop1, prop2));
		return this;
	}

	final public HibernateCriteriaUtil<T> neqField(String prop1, String prop2) {
		prop1 = addAlias(prop1);
		prop2 = addAlias(prop2);
		addCriteria(Restrictions.neProperty(prop1, prop2));
		return this;
	}

	final public HibernateCriteriaUtil<T> gtJoinedField(String prop1, final String prop2) {
		prop1 = addAlias(prop1);
		addCriteria(Restrictions.gtProperty(prop1, prop2));
		return this;
	}

	/**
	 * Returns a query that compares a particular field to a given date in
	 * integer format.
	 *
	 * @param dateSearchType Determines which type of comparison to perform.
	 */
	final public HibernateCriteriaUtil<T> dateCompare(final String propName, final int date, final int dateSearchType) {

		if (dateSearchType == SearchConstants.ALL_VALUE)
			return this;

		switch (dateSearchType) {
			case 1:
			case SearchConstants.EXACT_MATCH_VALUE:
			case SearchConstants.ON_VALUE:
				eq(propName, date);
				break;
			case 2:
			case SearchConstants.BEFORE_VALUE:
				lt(propName, date);
				break;
			case 3:
			case SearchConstants.AFTER_VALUE:
				gt(propName, date);
				break;
			case 4:  // not equal
				ne(propName, date);
				break;
			case SearchConstants.IS_SET_VALUE:
				ne(propName, 0);
				break;
			case SearchConstants.IS_NOT_SET_VALUE:
				eq(propName, 0);
				break;
			default:
				break;
		}
		return this;
	}

	/**
	 * Returns a query where a particular field is compared to a date.
	 *
	 * @param dateSearchType Determines which comparison to perform.
	 */
	final public HibernateCriteriaUtil<T> dateCompare(final String propName, final Date date, final int dateSearchType) {

		if (dateSearchType == SearchConstants.ALL_VALUE)
			return this;

		switch (dateSearchType) {
			case 1:
			case SearchConstants.EXACT_MATCH_VALUE:
			case SearchConstants.ON_VALUE:
				eq(propName, date);
				break;
			case 2:
			case SearchConstants.BEFORE_VALUE:
				lt(propName, date);
				break;
			case 3:
			case SearchConstants.AFTER_VALUE:
				gt(propName, date);
				break;
			case 4:  // not equal
				ne(propName, date);
				break;
			case SearchConstants.IS_SET_VALUE:
				ne(propName, 0);
				break;
			case SearchConstants.IS_NOT_SET_VALUE:
				eq(propName, 0);
				break;
			default:
				break;
		}
		return this;
	}

	public HibernateCriteriaUtil<T> dateTimeSpanCompare(String startDateField, String startTimeField, String endDateField, String endTimeField, int startDate, int startTime, int endDate, int endTime) {
		startDateField = addAlias(startDateField);
		endDateField = addAlias(endDateField);
		startTimeField = addAlias(startTimeField);
		endTimeField = addAlias(endTimeField);


		List<HibernateCriterionUtil> ors = new LinkedList<HibernateCriterionUtil>();

		//start and end day are the same - start is inside
		ors.add(makeCriteria().and(makeCriteria().and(makeCriteria().eq(startDateField, startDate),
				makeCriteria().le(startTimeField, startTime)),
				makeCriteria().and(makeCriteria().eq(endDateField, startDate),
				makeCriteria().gt(endTimeField, startTime))));

		//start and end day are the same - end is inside
		ors.add(makeCriteria().and(makeCriteria().and(makeCriteria().eq(startDateField, endDate),
				makeCriteria().lt(startTimeField, endTime)),
				makeCriteria().and(makeCriteria().eq(endDateField, endDate),
				makeCriteria().ge(endTimeField, endTime))));

		//start and end day are the same times totally surround existing timesheet
		ors.add(makeCriteria().and(makeCriteria().and(makeCriteria().eq(startDateField, startDate),
				makeCriteria().ge(startTimeField, startTime)),
				makeCriteria().and(makeCriteria().eq(endDateField, endDate),
				makeCriteria().and(makeCriteria().le(endTimeField, endTime), makeCriteria().ne(endTimeField, -1)))));

		//falls on start day but not same end day
		ors.add(makeCriteria().and(makeCriteria().and(makeCriteria().eq(startDateField, startDate),
				makeCriteria().le(startTimeField, startTime)),
				makeCriteria().gt(endDateField, startDate)));

		//falls on end day but not same start day
		ors.add(makeCriteria().and(makeCriteria().and(makeCriteria().eq(endDateField, endDate),
				makeCriteria().ge(endTimeField, endTime)),
				makeCriteria().lt(startDateField, endDate)));


		//if start date falls inside start and end of fields
		ors.add(makeCriteria().and(makeCriteria().lt(startDateField, startDate),
				makeCriteria().gt(endDateField, startDate)));


		//if start date comes after start field and has no end field
		ors.add(makeCriteria().and(makeCriteria().lt(startDateField, startDate),
				makeCriteria().eq(endDateField, 0)));

		if (endDate == 0) {
			//if start field comes after start date and there is no end date
			ors.add(makeCriteria().gt(startDateField, startDate));

			//if end field comes after start date and there is no end date
			ors.add(makeCriteria().gt(endDateField, startDate));
		} else {
			//if start field comes after start date, but before end date
			ors.add(makeCriteria().and(makeCriteria().gt(startDateField, startDate),
					makeCriteria().lt(startDateField, endDate)));

			//if end field comes after start date, but before end date
			ors.add(makeCriteria().and(makeCriteria().gt(endDateField, startDate),
					makeCriteria().lt(endDateField, endDate)));
		}

		HibernateCriterionUtil or = makeCriteria();
		or.or(ors);
		or.add();

		return this;
	}

	public HibernateCriteriaUtil<T> dateSpanCompare(String startField, String endField, int startDate, int endDate) {
		startField = addAlias(startField);
		endField = addAlias(endField);

		List<HibernateCriterionUtil> ors = new LinkedList<HibernateCriterionUtil>();

		//if start date falls inside start and end of fields

		ors.add(makeCriteria().and(makeCriteria().le(startField, startDate),
				makeCriteria().ge(endField, startDate)));


		//if start date comes after start field and has no end field
		ors.add(makeCriteria().and(makeCriteria().le(startField, startDate),
				makeCriteria().eq(endField, 0)));

		if (endDate == 0) {
			//if start field comes after start date and there is no end date
			ors.add(makeCriteria().ge(startField, startDate));

			//if end field comes after start date and there is no end date
			ors.add(makeCriteria().ge(endField, startDate));
		} else {
			//if start field comes after start date, but before end date
			ors.add(makeCriteria().and(makeCriteria().ge(startField, startDate),
					makeCriteria().le(startField, endDate)));

			//if end field comes after start date, but before end date
			ors.add(makeCriteria().and(makeCriteria().ge(endField, startDate),
					makeCriteria().le(endField, endDate)));
		}

		HibernateCriterionUtil or = makeCriteria();
		or.or(ors);
		or.add();

		return this;
	}

	/**
	 * Returns the first result of the current query.
	 */
	@SuppressWarnings("unchecked")
	final public T first() {
		try {
			setMaxResults(1);
			final List<T> l = list();
			if (l.isEmpty())
				return null;

			if (aiIntegrate)
				ArahantSession.runAI();
			return (T) l.get(0);
		} catch (final GenericJDBCException e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Returns a query that determines if a particular field is equal to any of
	 * a group of type Object.
	 */
	final public HibernateCriteriaUtil<T> in(String propName, final Object[] ids) {
		propName = addAlias(propName);
		final ArrayList<Object> al = new ArrayList<Object>(ids.length);
		Collections.addAll(al, ids);
		return in(propName, al);
	}

	/**
	 * Returns a query that determines if a particular field is equal to any of
	 * a group of type char.
	 */
	final public HibernateCriteriaUtil<T> in(String propName, final char[] c) {
		propName = addAlias(propName);
		final Character [] car = new Character[c.length];
		for (int loop = 0; loop < c.length; loop++)
			car[loop] = c[loop];
		return in(propName, car);
	}

	/**
	 * Returns a query that determines if a particular field is equal to any of
	 * a group of type short.
	 */
	final public HibernateCriteriaUtil<T> in(String propName, final short[] s) {
		propName = addAlias(propName);
		final Short [] sar = new Short[s.length];
		for (int loop = 0; loop < s.length; loop++)
			sar[loop] = s[loop];
		return in(propName, sar);
	}

	final public HibernateCriteriaUtil<T> in(String propName, final int[] s) {
		propName = addAlias(propName);
		final Integer [] sar = new Integer[s.length];
		for (int loop = 0; loop < s.length; loop++)
			sar[loop] = s[loop];
		return in(propName, sar);
	}

	/**
	 * Returns a query that determines if a particular field is not equal to any
	 * of a group of type char.
	 */
	final public HibernateCriteriaUtil<T> notIn(String propName, char[] c) {
		propName = addAlias(propName);
		final Character [] car = new Character[c.length];
		for (int loop = 0; loop < c.length; loop++)
			car[loop] = c[loop];
		return notIn(propName, car);
	}

	/**
	 * Returns a query that determines if a particular field exists.
	 */
	final public boolean exists() {
		setMaxResults(1);
		return first() != null;
	}

	/**
	 * Returns a query that determines the sum total of a particular field.
	 */
	final public HibernateCriteriaUtil<T> sum(String propName) {
		propName = addAlias(propName);
		criteria.setProjection(Projections.projectionList().add(Projections.sum(propName)));
		return this;
	}

	final public HibernateCriteriaUtil<T> groupBy(String propName) {
		propName = addAlias(propName);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty(propName)));
		return this;
	}

	final public HibernateCriteriaUtil<T> sum(String sumCol, String groupByCol) {
		sumCol = addAlias(sumCol);
		groupByCol = addAlias(groupByCol);
		criteria.setProjection(Projections.projectionList().add(Projections.sum(sumCol)).add(Projections.groupProperty(groupByCol)));
		return this;
	}

	final public HibernateCriteriaUtil<T> sum(String sumCol, String groupByCol, String groupByCol2) {
		sumCol = addAlias(sumCol);
		groupByCol = addAlias(groupByCol);
		groupByCol2 = addAlias(groupByCol2);
		criteria.setProjection(Projections.projectionList().add(Projections.sum(sumCol)).add(Projections.groupProperty(groupByCol)).add(Projections.groupProperty(groupByCol2)));
		return this;
	}

	final public HibernateCriteriaUtil<T> sum(String sumCol, String groupByCol, String groupByCol2, String groupByCol3) {
		sumCol = addAlias(sumCol);
		groupByCol = addAlias(groupByCol);
		addAlias(groupByCol2);  // what the hell?
		groupByCol2 = addAlias(groupByCol3);
		criteria.setProjection(Projections.projectionList().add(Projections.sum(sumCol)).add(Projections.groupProperty(groupByCol)).add(Projections.groupProperty(groupByCol2)).add(Projections.groupProperty(groupByCol3)));
		return this;
	}
	/*
	 final public HibernateCriteriaUtil<T> addReturnField(String propName) {
	 propName=addAlias(propName);
	 return selectFields(new String[]{propName});
	 }
	 */

	/**
	 * Returns any number of specified fields from the current query.
	 */
	final public HibernateCriteriaUtil<T> selectFields(String... propNames) {
		ProjectionList pl = Projections.projectionList();

		for (String name : propNames)
			pl.add(Projections.property(name));

		criteria.setProjection(pl);
		return this;
	}

	/**
	 * Returns only the specified fields, as a collection, from the current
	 * query.
	 */
	final public HibernateCriteriaUtil<T> selectFields(Collection<String> propNames) {
		ProjectionList pl = Projections.projectionList();

		for (String name : propNames)
			pl.add(Projections.property(name));

		criteria.setProjection(pl);
		return this;
	}

	/**
	 * Deletes the rows returned by the current query.
	 */
	final public void delete() throws ArahantDeleteException {
		//ArahantSession.getHSU().delete(list());
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateScrollUtil<T> scr = scroll();
		while (scr.next())
			hsu.delete(scr.get());
	}

	final public HibernateCriteriaUtil<T> eqPropertySubquery(String propName, final HibernateDetachedCriteriaUtil hdcu) {
		propName = addAlias(propName);
		addCriteria(Subqueries.propertyEq(propName, hdcu.criteria));
		return this;
	}

	final public HibernateCriteriaUtil<T> orEqPropertySubqueries(String propName, final HibernateDetachedCriteriaUtil sub1, final HibernateDetachedCriteriaUtil sub2) {
		propName = addAlias(propName);
		addCriteria(Restrictions.or(Subqueries.propertyEq(propName, sub1.criteria), Subqueries.propertyEq(propName, sub2.criteria)));
		return this;
	}

	/**
	 * Returns a query that determines if a given column value is greater than
	 * or equal to the first integer value or equal to the second integer value.
	 */
	final public HibernateCriteriaUtil<T> geOrEq(String propertyName, final int i, final int j) {
		propertyName = addAlias(propertyName);

		addCriteria(Restrictions.or(Restrictions.ge(propertyName, i), Restrictions.eq(propertyName, j)));
		return this;
	}

	/**
	 * Returns a query that determines if a given column value is greater than
	 * the first integer value or equal to the second integer value.
	 */
	final public HibernateCriteriaUtil<T> gtOrEq(String propertyName, final int i, final int j) {
		propertyName = addAlias(propertyName);

		addCriteria(Restrictions.or(Restrictions.gt(propertyName, i), Restrictions.eq(propertyName, j)));
		return this;
	}

	/**
	 * Returns a query that determines if a given column value is less than the
	 * first integer value or equal to the second integer value.
	 */
	final public HibernateCriteriaUtil<T> leOrEq(String propertyName, final int i, final int j) {
		propertyName = addAlias(propertyName);

		addCriteria(Restrictions.or(Restrictions.le(propertyName, i), Restrictions.eq(propertyName, j)));
		return this;
	}

	/**
	 * use {alias} for table alias
	 */
	final public HibernateCriteriaUtil<T> sql(String sql) {
		addCriteria(Restrictions.sqlRestriction(sql));
		return this;
	}

	/**
	 * Returns a query that returns all the entries before the given date.
	 */
	final public HibernateCriteriaUtil<T> dateBefore(String propName, Date date1) {
		propName = addAlias(propName);
		lt(propName, date1);
		return this;
	}

	final public HibernateCriteriaUtil<T> dateOnOrBefore(String propName, Date date1) {
		propName = addAlias(propName);
		le(propName, date1);
		return this;
	}

	final public HibernateCriteriaUtil<T> dateAfter(String propName, Date date1) {
		propName = addAlias(propName);
		gt(propName, date1);
		return this;
	}

	final public HibernateCriteriaUtil<T> dateAfter(String propName, int date1) {
		propName = addAlias(propName);
		gt(propName, date1);
		return this;
	}

	final public HibernateCriteriaUtil<T> dateOnOrAfter(String propName, Date date1) {
		propName = addAlias(propName);
		ge(propName, date1);
		return this;
	}

	/**
	 * Returns a query that determines if a given date is inside a start and end
	 * date.
	 */
	final public HibernateCriteriaUtil<T> dateInside(String startDate, String endDate, int effectiveDate) {
		le(startDate, effectiveDate);
		geOrEq(endDate, effectiveDate, 0);
		return this;
	}

	/**
	 * Returns a query that determines if a given date is outside a start and
	 * end date.
	 */
	public HibernateCriteriaUtil<T> dateOutside(String startDate, String endDate, int effectiveDate) {

		HibernateCriterionUtil cri1 = makeCriteria();
		HibernateCriterionUtil cri2 = makeCriteria();
		HibernateCriterionUtil cri3 = makeCriteria();
		HibernateCriterionUtil andcri = makeCriteria();
		HibernateCriterionUtil orcri = makeCriteria();

		cri1.gt(startDate, effectiveDate);
		//or
		cri2.lt(endDate, effectiveDate);
		cri3.ne(endDate, 0);

		andcri.and(cri2, cri3);
		orcri.or(cri1, andcri);

		orcri.add();

		return this;
	}

	/**
	 * Returns a query that determines if a particular field is between
	 * startDate and endDate.
	 */
	final public HibernateCriteriaUtil<T> between(String propName, java.util.Date startDate, java.util.Date endDate) {
		propName = addAlias(propName);
		addCriteria(Restrictions.between(propName, startDate, endDate));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is between an
	 * integer value startDate and an integer value endDate.
	 */
	final public HibernateCriteriaUtil<T> dateBetween(String propName, int date1, int date2) {
		propName = addAlias(propName);
		if (date2 > 0)
			addCriteria(Restrictions.and(Restrictions.ge(propName, date1), Restrictions.le(propName, date2)));
		else
			addCriteria(Restrictions.ge(propName, date1));
		return this;
	}

	/**
	 * Returns a query that determines if a particular field is between date1
	 * and date2.
	 */
	final public HibernateCriteriaUtil<T> dateBetween(String propName, Date date1, Date date2) {
		propName = addAlias(propName);

		addCriteria(Restrictions.and(Restrictions.ge(propName, date1), Restrictions.le(propName, date2)));

		return this;
	}

	/**
	 * Returns a query that determines if a particualr field is less than the
	 * first integer value and not equal to the second integer value.
	 */
	public HibernateCriteriaUtil<T> ltAndNeq(String propName, int i, int j) {
		propName = addAlias(propName);
		addCriteria(Restrictions.and(Restrictions.lt(propName, i), Restrictions.ne(propName, j)));
		return this;
	}
	private boolean didEmployeeCompanyFilter = false;

	/**
	 * Filters the current query to only contain the employees for the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyEmployees() {
		if (!noCompanyFilter && !didEmployeeCompanyFilter) {
			didEmployeeCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("({alias}.person_id in (select person_id from person where company_id='" + currentCompanyId + "')"
					+ " or "
					+ "{alias}.person_id in (select person_id from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "'))");
		}
		return this;
	}
	private boolean didPersonCompanyFilter = false;

	/**
	 * Filters the current query to only conatin the person in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyPerson() {
		if (!noCompanyFilter && !didPersonCompanyFilter) {
			//check to see if they are employee in company or dependent in company
			didPersonCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("("
					+ "{alias}.org_group_type!=" + ArahantConstants.COMPANY_TYPE
					+ " or "
					+ " {alias}.person_id in (select person_id from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "')"
					+ " or "
					+ " {alias}.person_id in (select person_id from person where company_id='" + currentCompanyId + "')"
					+ " or "
					+ //	"{alias}.person_id not in (select person_id from org_group_association) " +
					//	" or "+
					"{alias}.person_id in (select dependent_id from hr_empl_dependent where"
					+ " employee_id in (select person_id "
					+ "from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "'))"
					+ ")");
		}
		return this;
	}
	private boolean didPersonHistoryCompanyFilter = false;

	/**
	 * Filters the current query to only conatin the person in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyPersonHistory() {
		if (!noCompanyFilter && !didPersonHistoryCompanyFilter) {
			//check to see if they are employee in company or dependent in company
			didPersonHistoryCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("("
					+ "{alias}.org_group_type!=" + ArahantConstants.COMPANY_TYPE
					+ " or "
					+ " {alias}.person_id in (select person_id from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "')"
					+ " or "
					+ " {alias}.person_id in (select person_id from person where company_id='" + currentCompanyId + "')"
					+ " or "
					+ //	"{alias}.person_id not in (select person_id from org_group_association) " +
					//	" or "+
					"{alias}.person_id in (select dependent_id from hr_empl_dependent where"
					+ " employee_id in (select person_id "
					+ "from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "'))"
					+ ")");
		}
		return this;
	}
	private boolean didApplicantCompanyFilter = false;

	/**
	 * Filters the current query to only conatin the applicant in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyApplicant() {
		if (!noCompanyFilter && !didApplicantCompanyFilter) {
			//check to see if they are employee in company or dependent in company
			didApplicantCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("{alias}.person_id in (select person_id from person where person.company_id='" + currentCompanyId + "' or person.company_id is null)");
		}
		return this;
	}
	private boolean didProductServiceCompanyFilter = false;

	/**
	 * Filters the current query to only contain those product services in the
	 * current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProductServices() {
		if (!noCompanyFilter && !didProductServiceCompanyFilter) {
			didProductServiceCompanyFilter = true;
			sql("{alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'");
		}
		return this;
	}
	private boolean didProductTypeCompanyFilter = false;

	/**
	 * Filters the current query to only contain those product types in the
	 * current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProductTypes() {
		if (!noCompanyFilter && !didProductTypeCompanyFilter) {
			didProductTypeCompanyFilter = true;
			sql("{alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'");
		}
		return this;
	}
	private boolean didVendorCompanyFilter = false;

	/**
	 * Filters the current query to only contain clients for vendors in the
	 * current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyClientOrVendors() {
		if (!noCompanyFilter && !didVendorCompanyFilter) {
			didVendorCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'"
					+ " or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didOrgGroupCompanyFilter = false;

	/**
	 * Filters the current query to only contain orggroups in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyOrgGroups() {
		if (!noCompanyFilter && !didOrgGroupCompanyFilter) {
			didOrgGroupCompanyFilter = true;
			sql("{alias}.org_group_id in (select org_group_id from org_group "
					+ " where owning_entity_id='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'"
					+ " or org_group_type!=" + ArahantConstants.COMPANY_TYPE + ""
					+ " )");
		}
		return this;
	}
	private boolean didBenefitCategoryCompanyFilter = false;

	/**
	 * Filters the current query to only contain benefit categories in the
	 * current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyBenefitCategories() {
		if (!noCompanyFilter && !didBenefitCategoryCompanyFilter) {
			didBenefitCategoryCompanyFilter = true;
			sql("{alias}.org_group_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'");
		}
		return this;
	}
	private boolean didBenefitCompanyFilter = false;

	/**
	 * Filters the current query to only contain benefits in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyBenefits() {
		if (!noCompanyFilter && !didBenefitCompanyFilter) {
			didBenefitCompanyFilter = true;
			sql("{alias}.benefit_cat_id in "
					+ "(select benefit_cat_id from hr_benefit_category where org_group_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')"
					+ "");
		}
		return this;
	}
	private boolean didEvalCompanyFilter = false;

	/**
	 * Restricts the current query to only the current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyFiltered() {
		if (!noCompanyFilter && !didEvalCompanyFilter) {
			didEvalCompanyFilter = true;

			String field = addAlias("orgGroup");

			addCriteria(Restrictions.or(Restrictions.isNull(field),
					Restrictions.eq(field, ArahantSession.getHSU().getCurrentCompany())));

		}
		return this;
	}
	private boolean didStatusHistoryCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyStatusHistories() {
		if (!noCompanyFilter && !didStatusHistoryCompanyFilter) {
			didStatusHistoryCompanyFilter = true;
			sql("{alias}.status_id in (select status_id from hr_employee_status where org_group_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')");
		}
		return this;
	}
	private boolean didBenefitConfigCompanyFilter = false;

	/**
	 * Filters the current query to only contain benefit configurations in the
	 * current company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyBenefitConfigs() {
		if (!noCompanyFilter && !didBenefitConfigCompanyFilter) {
			didBenefitConfigCompanyFilter = true;
			sql("{alias}.benefit_id in (select benefit_id from hr_benefit where benefit_cat_id in "
					+ "(select benefit_cat_id from hr_benefit_category where org_group_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'))"
					+ "");
		}
		return this;
	}

	public HibernateCriteriaUtil<T> filterToCurrentCompanyOrgGroupsOrMeta() {
		if (!noCompanyFilter && !didOrgGroupCompanyFilter) {
			didOrgGroupCompanyFilter = true;
			sql("{alias}.org_group_id in (select org_group_id from org_group "
					+ " where owning_entity_id='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'"
					+ " or org_group_type!=" + ArahantConstants.COMPANY_TYPE + ""
					+ " or owning_entity_id is null)");
		}
		return this;
	}
	private boolean didPersonFormCompanyFilter = false;

	/**
	 * Filters the current query to only contain person forms in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyPersonForms() {
		if (!noCompanyFilter && !didPersonFormCompanyFilter) {
			didPersonFormCompanyFilter = true;
			String currentCompanyId = ArahantSession.getHSU().getCurrentCompany().getOrgGroupId();
			sql("("
					+ //"{alias}.org_group_type!="+ArahantConstants.COMPANY_TYPE+
					//" or "+
					" {alias}.person_id in (select person_id from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "')"
					+ " or "
					+ " {alias}.person_id in (select person_id from person where company_id='" + currentCompanyId + "')"
					+ " or "
					+ //	"{alias}.person_id not in (select person_id from org_group_association) " +
					//	" or "+
					"{alias}.person_id in (select dependent_id from hr_empl_dependent where"
					+ " employee_id in (select person_id "
					+ "from org_group_association "
					+ "join org_group on org_group_association.org_group_id=org_group.org_group_id "
					+ "and owning_entity_id='" + currentCompanyId + "'))"
					+ ")");
		}
		return this;
	}
	private boolean didApplicantSourceCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyApplicantSources() {
		if (!noCompanyFilter && !didApplicantSourceCompanyFilter) {
			didApplicantSourceCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didApplicantStatusCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyApplicantStatuses() {
		if (!noCompanyFilter && !didApplicantStatusCompanyFilter) {
			didApplicantStatusCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didApplicantAppStatusCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyApplicantAppStatuses() {
		if (!noCompanyFilter && !didApplicantAppStatusCompanyFilter) {
			didApplicantAppStatusCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didApplicantQuestionCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyApplicantQuestions() {
		if (!noCompanyFilter && !didApplicantQuestionCompanyFilter) {
			didApplicantQuestionCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didJobTypeCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyJobTypes() {
		if (!noCompanyFilter && !didJobTypeCompanyFilter) {
			didJobTypeCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didAppointmentLocationCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyAppointmentLocations() {
		if (!noCompanyFilter && !didAppointmentLocationCompanyFilter) {
			didAppointmentLocationCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didCompanyQuestionCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyCompanyQuestions() {
		if (!noCompanyFilter && !didCompanyQuestionCompanyFilter) {
			didCompanyQuestionCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didContactQuestionCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyContactQuestions() {
		if (!noCompanyFilter && !didContactQuestionCompanyFilter) {
			didContactQuestionCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didClientStatusCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyClientStatuses() {
		if (!noCompanyFilter && !didClientStatusCompanyFilter) {
			didClientStatusCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProspectStatusCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProspectStatuses() {
		if (!noCompanyFilter && !didProspectStatusCompanyFilter) {
			didProspectStatusCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProspectSourceCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProspectSources() {
		if (!noCompanyFilter && !didProspectSourceCompanyFilter) {
			didProspectSourceCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProspectTypeCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProspectTypes() {
		if (!noCompanyFilter && !didProspectTypeCompanyFilter) {
			didProspectTypeCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
    private boolean didRateTypeCompanyFilter = false;

    /**
     * Filters the current query to only contain status histories in the current
     * company.
     */
    public HibernateCriteriaUtil<T> filterToCurrentCompanyRateTypes() {
        if (!noCompanyFilter && !didRateTypeCompanyFilter) {
            didRateTypeCompanyFilter = true;
            sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
        }
        return this;
    }
	private boolean didProspectCompanyCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProspectCompanies() {
		if (!noCompanyFilter && !didProspectCompanyCompanyFilter) {
			didProspectCompanyCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didBankDraftBatchCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyBankDraftBatch() {
		if (!noCompanyFilter && !didBankDraftBatchCompanyFilter) {
			didBankDraftBatchCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didGlAccountCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyGlAccount() {
		if (!noCompanyFilter && !didGlAccountCompanyFilter) {
			didGlAccountCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didServiceCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyService() {
		if (!noCompanyFilter && !didServiceCompanyFilter) {
			didServiceCompanyFilter = true;
			sql("({alias}.service_id in (select product_service_id from product_service where company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or company_id is null))");
		}
		return this;
	}
	private boolean didBankAccountCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyBankAccount() {
		if (!noCompanyFilter && !didBankAccountCompanyFilter) {
			didBankAccountCompanyFilter = true;
			sql("{alias}.org_group_id in (select org_group_id from org_group "
					+ " where owning_entity_id='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')");
		}
		return this;
	}
	private boolean didPayScheduleCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyPaySchedule() {
		if (!noCompanyFilter && !didPayScheduleCompanyFilter) {
			didPayScheduleCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProjectCategoryCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProjectCategory() {
		if (!noCompanyFilter && !didProjectCategoryCompanyFilter) {
			didProjectCategoryCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProjectStatusCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProjectStatus() {
		if (!noCompanyFilter && !didProjectStatusCompanyFilter) {
			didProjectStatusCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProjectTypeCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProjectType() {
		if (!noCompanyFilter && !didProjectTypeCompanyFilter) {
			didProjectTypeCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProjectPhaseCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProjectPhase() {
		if (!noCompanyFilter && !didProjectPhaseCompanyFilter) {
			didProjectPhaseCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didRouteCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyRoute() {
		if (!noCompanyFilter && !didRouteCompanyFilter) {
			didRouteCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}
	private boolean didProjectCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyProject() {
		if (!noCompanyFilter && !didProjectCompanyFilter) {
			didProjectCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')");
//			sql("({alias}.project_id in (select p.project_id from project p, project_category pc, project_status ps, project_type pt " +
//					"where p.project_category_id = pc.project_category_id and (pc.company_id = '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or pc.company_id is null) " +
//					"and p.project_status_id = ps.project_status_id and (ps.company_id = '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or ps.company_id is null) " +
//					"and p.project_type_id = pt.project_type_id and (pt.company_id = '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or pt.company_id is null)))" );
		}
		return this;
	}
	private boolean didStandardProjectCompanyFilter = false;

	/**
	 * Filters the current query to only contain status histories in the current
	 * company.
	 */
	public HibernateCriteriaUtil<T> filterToCurrentCompanyStandardProject() {
		if (!noCompanyFilter && !didStandardProjectCompanyFilter) {
			didStandardProjectCompanyFilter = true;
			sql("({alias}.company_id ='" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "' or {alias}.company_id is null)");
		}
		return this;
	}

	/**
	 * Returns a query that determines if the current person is an employee.
	 */
	public HibernateCriteriaUtil<T> isEmployee() {
		sql("{alias}.person_id in (select person_id from employee)");
		filterToCurrentCompanyEmployees();
		return this;
	}

	/**
	 * Returns a query that determines if the current person is not an employee.
	 */
	public HibernateCriteriaUtil<T> isNotEmployee() {
		sql("{alias}.person_id not in (select person_id from employee)");
		return this;
	}

	/**
	 * Returns a query that determines if the current person is an active
	 * employee.
	 */
	public HibernateCriteriaUtil<T> activeEmployee() {
		filterToCurrentCompanyEmployees();
		sql("{alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status "
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id and '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'=hr_employee_status.org_group_id "
				+ "		and hr_employee_status.active='Y' and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date<= " + DateUtils.now()
				+ " and hist2.status_id in (select status_id from hr_employee_status where org_group_id="
				+ "'" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')) ) ");
		return this;
	}

	/**
	 * Returns a query that determines if the current employee is active between
	 * the two specified dates.
	 */
	public HibernateCriteriaUtil<T> activeEmployee(int startDate, int finalDate) {
		filterToCurrentCompanyEmployees();
		sql("({alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status "
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id and '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'=hr_employee_status.org_group_id "
				+ "		and hr_employee_status.active='Y' and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date<= " + startDate
				+ " and hist2.status_id in (select status_id from hr_employee_status where org_group_id="
				+ "'" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')))  "
				+ "or "
				+ "{alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status "
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id and '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'=hr_employee_status.org_group_id "
				+ "		and hr_employee_status.active='Y' and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date>= " + startDate + " or hist2.effective_date<= " + finalDate + ") ) )");
		return this;
	}

	/**
	 * Returns a query that determines if the current employee has been active
	 * for at least 60 days.
	 */
	public HibernateCriteriaUtil<T> activeEmployee60Days() {
		filterToCurrentCompanyEmployees();
		sql("(({alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status "
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id and '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'=hr_employee_status.org_group_id "
				+ "		and hr_employee_status.active='Y' and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date<= " + DateUtils.getDate(DateUtils.getSixtyDaysAgo()) + ") ))"
				+ " or (({alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status"
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id "
				+ "		and hr_employee_status.active='Y' and {alias}.company_id=hr_employee_status.org_group_id  and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id "
				+ " and hist2.status_id in (select status_id from hr_employee_status where org_group_id="
				+ "'" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')) )))) ");

		return this;
	}

	/**
	 * Returns a query that determines if the current employee is inactive.
	 */
	public HibernateCriteriaUtil<T> inactiveEmployee() {
		filterToCurrentCompanyEmployees();
		sql("{alias}.person_id in (select employee_id from hr_empl_status_history join hr_employee_status "
				+ "		on hr_employee_status.status_id=hr_empl_status_history.status_id and '" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "'=hr_employee_status.org_group_id "
				+ "		and hr_employee_status.active='N' and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date<= " + DateUtils.now()
				+ " and hist2.status_id in (select status_id from hr_employee_status where org_group_id="
				+ "'" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')) ) ");
		return this;
	}

	public HibernateCriteriaUtil<T> employeeCurrentStatus(String statusId) {
		filterToCurrentCompanyEmployees();
		sql("{alias}.person_id in (select employee_id from hr_empl_status_history  "
				+ "	where hr_empl_status_history.status_id ='" + statusId + "'"
				+ "	 and "
				+ "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where "
				+ "				hist2.employee_id={alias}.person_id and hist2.effective_date<= " + DateUtils.now()
				+ " and hist2.status_id in (select status_id from hr_employee_status where org_group_id="
				+ "'" + ArahantSession.getHSU().getCurrentCompany().getOrgGroupId() + "')) ) ");


		return this;
	}

	/**
	 * Looking at this method, it seems totally messed up.  I won't touch until I find a need to. -- blake
	 */
	public HibernateCriteriaUtil<T> orEq(String propertyName1, String propertyName2, String propertyName3, String propertyName4, String propertyName5, Object val) {
		propertyName1 = addAlias(propertyName1);
//		addAlias(propertyName2);
//		addAlias(propertyName3);
//		addAlias(propertyName4);
		propertyName2 = addAlias(propertyName5);
		addCriteria(Restrictions.or(Restrictions.eq(propertyName4, val),
				Restrictions.or(Restrictions.eq(propertyName5, val),
				Restrictions.or(Restrictions.eq(propertyName3, val),
				Restrictions.or(Restrictions.eq(propertyName1, val),
				Restrictions.eq(propertyName2, val))))));

		return this;
	}

	public HibernateCriteriaUtil<T> orEq(String[] properties, Object val) {
		List<HibernateCriterionUtil> rlist = new LinkedList<HibernateCriterionUtil>();

		for (String prop : properties)
			rlist.add(makeCriteria().eq(prop, val));

		makeCriteria().or(rlist).add();

		return this;
	}

	public HibernateCriteriaUtil<T> employeeCurrentStatusIn(String[] statusIdArray, int date) {
		StringBuilder q = new StringBuilder("{alias}.person_id in (select employee_id from hr_empl_status_history  "
				+ "	where hr_empl_status_history.status_id in (");
		for (String s : statusIdArray)
			q.append("'").append(s).append("',");
		q = new StringBuilder(q.substring(0, q.length() - 1));
		q.append(")" + "	 and " + "hr_empl_status_history.effective_date=(select max(effective_date) from hr_empl_status_history hist2 where " + "				hist2.employee_id={alias}.person_id and hist2.effective_date<= ").append(date).append(") ) ");
		return sql(q.toString());
	}

	/**
	 * Returns a query that determines if any of the supplied Object values are
	 * equal to a particular field.
	 */
	public HibernateCriteriaUtil<T> notIn(String propName, Object[] vals) {
		if (vals == null)
			return this;

		if (vals.length == 0)
			return this;

		ArrayList<Object> l = new ArrayList<Object>(vals.length);

		Collections.addAll(l, vals);

		return notIn(propName, l);
	}
	/*
	 public HibernateCriteriaUtil<T> inOrgGroup(OrgGroup og)
	 {
	 return in(Person.PERSONID,new BOrgGroup(og).getAllPersonIdsForOrgGroupHierarchy(false));
	 }
	
	 public HibernateCriteriaUtil<T> inOrgGroup(String orgGroupId)
	 {
	 return inOrgGroup(ArahantSession.getHSU().get(OrgGroup.class,orgGroupId));
	 }
	 */

	/**
	 * Checks the current query to see if a given field is equal to a specified
	 * month.
	 */
	public HibernateCriteriaUtil<T> monthIs(String propName, int month) {
		if (month < 0 || month > 11)
			return this;

		propName = addAlias(propName);

		// assume no one working is older than 100 years old, and gather up OR clauses
		// for the last 100 of the specified months
		Calendar fromCalendarDate = Calendar.getInstance();
		Calendar toCalendarDate = Calendar.getInstance();

		fromCalendarDate.set(Calendar.MONTH, month);
		fromCalendarDate.set(Calendar.DAY_OF_MONTH, 1);

		ArrayList<HibernateCriterionUtil> criterions = new ArrayList<HibernateCriterionUtil>();
		for (int idx = 0; idx < 100; idx++) {
			toCalendarDate.set(Calendar.YEAR, fromCalendarDate.get(Calendar.YEAR));
			toCalendarDate.set(Calendar.MONTH, fromCalendarDate.get(Calendar.MONTH));
			toCalendarDate.set(Calendar.DAY_OF_MONTH, fromCalendarDate.getActualMaximum(Calendar.DAY_OF_MONTH));

			HibernateCriterionUtil hcou = makeCriteria();
			hcou.between(propName, DateUtils.getDate(fromCalendarDate), DateUtils.getDate(toCalendarDate));
			criterions.add(hcou);

			fromCalendarDate.roll(Calendar.YEAR, -1);
		}

		HibernateCriterionUtil hcou = makeCriteria();
		hcou.or(criterions);
		hcou.add();

		return this;
	}

	/**
	 * Checks the current query to see if a given field is: (11)greater than,
	 * (12)less than, (13)equal to, or (14)not equal to a specified double value
	 * d. Set the comparator to the value next to what comparison you wish to
	 * make.
	 */
	public HibernateCriteriaUtil<T> compare(String field, int comparator, double d) {
		switch (comparator) {
			case SearchConstants.GREATER_THAN_VALUE:
				return gt(field, d);
			case SearchConstants.LESS_THAN_VALUE:
				return lt(field, d);
			case SearchConstants.EQUAL_TO_VALUE:
				return eq(field, d);
			case SearchConstants.NOT_EQUAL_TO_VALUE:
				return ne(field, d);

			default:
				throw new ArahantException("Unknown comparator passed to compare - double " + comparator);
		}
	}

	/**
	 * Checks the current query to see if a given field is: (11)greater than,
	 * (12)less than, (13)equal to, or (14)not equal to a specified integer
	 * value d. Set the comparator to the value next to what comparison you wish
	 * to make.
	 */
	public HibernateCriteriaUtil<T> compare(String field, int comparator, int d) {
		switch (comparator) {
			case SearchConstants.GREATER_THAN_VALUE:
				return gt(field, d);
			case SearchConstants.LESS_THAN_VALUE:
				return lt(field, d);
			case SearchConstants.EQUAL_TO_VALUE:
				return eq(field, d);
			case SearchConstants.NOT_EQUAL_TO_VALUE:
				return ne(field, d);

			default:
				throw new ArahantException("Unknown comparator passed to compare - int " + comparator);
		}
	}
//	public static HibernateCriteriaUtil or(HibernateCriteriaUtil hcu1, HibernateCriteriaUtil hcu2) {
//		HibernateCriteriaUtil hcu = hcu1;
//
//		HibernateCriterionUtil crit1 = hcu1.makeCriteria();
//		HibernateCriterionUtil crit2 = hcu2.makeCriteria();
//		HibernateCriterionUtil criterion = hcu.makeCriteria();
//		criterion.or(crit1, crit2);
//		criterion.add();
//
//		return hcu;
//	}
//
//	public void or(HibernateCriteriaUtil hcu2) {
//		Criteria crit1 = this.getCriteria();
//		List critList1 = crit1.list();
//
//		Criteria crit2 = this.getCriteria();
//		List critList2 = crit2.list();
//
//		int i = 0;
//		while(critList1.get(i).equals(critList2.get(i))) {
//			critList1.remove(critList1.get(i));
//			critList2.remove(critList2.get(i));
//			this.getCriteria().list().remove(critList1.get(i));
//			i++;
//		}
//
//		for(Object c : critList1) {
//			Criterion cr = (Criterion)c;
//			crit1.add(cr);
//		}
//		for(Object c : critList2) {
//			Criterion cr = (Criterion)c;
//			crit2.add(cr);
//		}
//		HibernateCriterionUtil criterion1 = new HibernateCriterionUtil(crit1, alias);
//		HibernateCriterionUtil criterion2 = new HibernateCriterionUtil(crit2, alias);
//
//		HibernateCriterionUtil hcu = new HibernateCriterionUtil(this.getCriteria(), alias);
//		hcu.or(criterion1, criterion2);
//		hcu.add();
//	}
}
