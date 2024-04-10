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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmployeeEval;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Report;
import com.arahant.beans.ReportColumn;
import com.arahant.beans.ReportGraphic;
import com.arahant.beans.ReportSelection;
import com.arahant.beans.ReportTitle;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.dynamic.reports.ColumnDisplayObject;
import com.arahant.dynamic.reports.DynamicJoinColumn;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.dynamic.reports.EmployeeColumns;
import com.arahant.dynamic.reports.HrBenefitJoinColumns;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
//import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 */
public class BReport extends SimpleBusinessObjectBase<Report> implements IDBFunctions {

	/**
	 */
	public BReport() {
	}
	private HibernateScrollUtil scr;

	/**
	 * @param reportId
	 * @throws ArahantException
	 */
	public BReport(final String reportId) throws ArahantException {
		internalLoad(reportId);
	}

	public BReport(final Report r) {
		bean = r;
	}

	public int getDefaultFontSize() {
		return bean.getDefaultFontSize();
	}

	public void setDefaultFontSize(short defaultFontSize) {
		bean.setDefaultFontSize(defaultFontSize);
	}

	public void setDefaultFontSize(int defaultFontSize) {
		bean.setDefaultFontSize((short) defaultFontSize);
	}

	public double getDefaultSpaceBetweenColumns() {
		return bean.getDefaultSpaceBetweenColumns();
	}

	public void setDefaultSpaceBetweenColumns(double defaultSpaceBetweenColumns) {
		bean.setDefaultSpaceBetweenColumns(defaultSpaceBetweenColumns);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public int getLinesInColumnTitle() {
		return bean.getLinesInColumnTitle();
	}

	public void setLinesInColumnTitle(short linesInColumnTitle) {
		bean.setLinesInColumnTitle(linesInColumnTitle);
	}

	public void setLinesInColumnTitle(int linesInColumnTitle) {
		bean.setLinesInColumnTitle((short) linesInColumnTitle);
	}

	public double getPageOffsetLeft() {
		return bean.getPageOffsetLeft();
	}

	public void setPageOffsetLeft(double pageOffsetLeft) {
		bean.setPageOffsetLeft(pageOffsetLeft);
	}

	public double getPageOffsetTop() {
		return bean.getPageOffsetTop();
	}

	public void setPageOffsetTop(double pageOffsetTop) {
		bean.setPageOffsetTop(pageOffsetTop);
	}

	public String getPageOrientation() {
		return String.valueOf(bean.getPageOrientation());
	}

	public void setPageOrientation(char pageOrientation) {
		bean.setPageOrientation(pageOrientation);
	}

	public void setPageOrientation(String pageOrientation) {
		bean.setPageOrientation(pageOrientation.charAt(0));
	}

	public String getReportId() {
		return bean.getReportId();
	}

	public void setReportId(String reportId) {
		bean.setReportId(reportId);
	}

	public String getReportName() {
		return bean.getReportName();
	}

	public void setReportName(String reportName) {
		bean.setReportName(reportName);
	}

	public int getReportType() {
		return bean.getReportType();
	}

	public void setReportType(short reportType) {
		bean.setReportType(reportType);
	}

	public void setReportType(int reportType) {
		bean.setReportType((short) reportType);
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		insertChecks();

		if (isEmpty(bean.getCompanyId()))
			bean.setCompany(ArahantSession.getHSU().getCurrentCompany());

		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void insertChecks() {
		if (ArahantSession.getHSU().createCriteria(Report.class).eq(Report.REPORT_NAME, this.getReportName()).eq(Report.COMPANY, ArahantSession.getHSU().getCurrentCompany()).exists())
			throw new ArahantException("Report with identical name already exists.  Please try a different name.");
	}

	@Override
	public void updateChecks() {
		if (ArahantSession.getHSU().createCriteria(Report.class).eq(Report.REPORT_NAME, this.getReportName()).eq(Report.COMPANY, ArahantSession.getHSU().getCurrentCompany()).ne(Report.REPORT_ID, this.getReportId()).exists())
			throw new ArahantException("Report with identical name already exists.  Please try a different name.");
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().createCriteria(ReportTitle.class).eq(ReportTitle.REPORT, getBean()).delete();
			ArahantSession.getHSU().createCriteria(ReportColumn.class).eq(ReportColumn.REPORT, getBean()).delete();
			ArahantSession.getHSU().createCriteria(ReportSelection.class).eq(ReportSelection.REPORT, getBean()).delete();
			ArahantSession.getHSU().createCriteria(ReportGraphic.class).eq(ReportGraphic.REPORT, getBean()).delete();

			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for (String id : ids)
			new BReport(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new Report();
		bean.generateId();
		return getReportId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Report.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static BReport[] search(int reportType, String reportName, int cap) {
		HibernateCriteriaUtil<Report> hcu = ArahantSession.getHSU().createCriteria(Report.class).eq(Report.COMPANY, ArahantSession.getHSU().getCurrentCompany()).like(Report.REPORT_NAME, reportName);

		if (reportType > 0)
			hcu.eq(Report.REPORT_TYPE, reportType);

		return makeArray(hcu.list());
	}

	public static BReport[] makeArray(final Collection<Report> l) throws ArahantException {
		final BReport[] ret = new BReport[l.size()];

		int loop = 0;
		for (Report r : l)
			ret[loop++] = new BReport(r);

		return ret;
	}

	public List<DynamicReportColumn> getAvailableColumns() {
		//return ArahantSession.getHSU().createCriteria(ReportColumn.class).orderBy(ReportColumn.SEQ_NO).eq(ReportColumn.REPORT_ID, getReportId()).list();
		List<DynamicReportColumn> drcl = new ArrayList<DynamicReportColumn>();

		if (getReportType() == 1) {
			drcl = HrBenefitJoinColumns.getColumns();
			for (ReportColumn rc : getReportColumns()) {
				List<DynamicReportColumn> removeList = new ArrayList<DynamicReportColumn>();
				DynamicReportColumn drc = HrBenefitJoinColumns.getColumnByIndex(rc.getColumnId());
				for (DynamicReportColumn d : drcl)
					if (d.equals(drc))
						removeList.add(d);
				drcl.removeAll(removeList);
			}
		} else if (getReportType() == 2) //Employee
		{
			drcl = EmployeeColumns.getColumns();
			for (ReportColumn rc : getReportColumns()) {
				List<DynamicReportColumn> removeList = new ArrayList<DynamicReportColumn>();
				DynamicReportColumn drc = EmployeeColumns.getColumnByIndex(rc.getColumnId());
				for (DynamicReportColumn d : drcl)
					if (d.equals(drc))
						removeList.add(d);
				drcl.removeAll(removeList);
			}
		}

		return drcl;
	}

	public List<ReportColumn> getReportColumns() {
		return ArahantSession.getHSU().createCriteria(ReportColumn.class).orderBy(ReportColumn.SEQ_NO).eq(ReportColumn.REPORT_ID, getReportId()).list();
	}

	public List<ReportSelection> getReportSelections() {
		return ArahantSession.getHSU().createCriteria(ReportSelection.class).orderBy(ReportSelection.SEQ_NO).eq(ReportSelection.REPORT_ID, getReportId()).list();
	}

	public List<ReportTitle> getReportTitles() {
		return ArahantSession.getHSU().createCriteria(ReportTitle.class).orderBy(ReportTitle.SEQ_NO).eq(ReportTitle.REPORT_ID, getReportId()).list();
	}

	public List<ReportColumn> getReportSorts() {
		return ArahantSession.getHSU().createCriteria(ReportColumn.class).ne(ReportColumn.SORT_ORDER, (short) 0).orderBy(ReportColumn.SORT_ORDER).eq(ReportColumn.REPORT_ID, getReportId()).list();
	}

	public List<ReportGraphic> getReportGraphics() {
		return ArahantSession.getHSU().createCriteria(ReportGraphic.class).eq(ReportGraphic.REPORT_ID, getReportId()).list();
	}

	public Class getReportClassForCriteria() {
		return DynamicReportBase.getReportClassForCriteria(this.bean);
	}

	public HibernateCriteriaUtil applySelectionCriteria() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(getReportClassForCriteria(), getReportClassForCriteria().getSimpleName());
		System.out.println("Created Criteria on " + getReportClassForCriteria().getSimpleName());
		Criteria crit = hcu.getCriteria();
		List<String> aliases = new ArrayList<String>();
		aliases.add(getReportClassForCriteria().getSimpleName());
		List<Employee> empList;
		List<ReportColumn> sort = getReportSorts();

		Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
		Set<OrgGroup> ogs = new HashSet<OrgGroup>();
		for (OrgGroupAssociation o : oga)
			ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

		if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
			empList = ArahantSession.getHSU().createCriteria(Employee.class).joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs).selectFields(Employee.PERSONID).list();
		else
			empList = ArahantSession.getHSU().createCriteria(Employee.class).selectFields(Employee.PERSONID).list();

		crit.add(Restrictions.in(DynamicReportBase.getPersonFilterCriteria(getReportType()), empList));

		List<ReportSelection> reportSelections = getReportSelections();
		for (ReportSelection critSelection : reportSelections) {
			BReportSelection bSelection = new BReportSelection(critSelection);
			//Handle joining to new tables first
			for (int i = 0; i < bSelection.getDynamicJoinColumns().size(); i++) {
				DynamicJoinColumn joinColumn = bSelection.getDynamicJoinColumns().get(i);
				String previousAlias = (i == 0 ? aliases.get(0) : bSelection.getDynamicJoinColumns().get(i - 1).getJoinClass().getSimpleName()) + ".";
				//dont join if you already have
				if (aliases.contains(joinColumn.getJoinClass().getSimpleName()))
					continue;
				aliases.add(joinColumn.getJoinClass().getSimpleName());
				crit.createCriteria(previousAlias + joinColumn.getJoinColumn(), joinColumn.getJoinClass().getSimpleName());
				System.out.println("   Join to " + previousAlias + joinColumn.getJoinColumn() + " (" + joinColumn.getJoinClass().getSimpleName() + ")");
			}
			for (int i = 0; i < sort.size(); i++) {
				DynamicReportColumn drc = DynamicReportBase.getColumnByTypeIndex(bean.getReportType(), sort.get(i).getColumnId());
				List<DynamicJoinColumn> joins = drc.getJoinColumnList();
				String previousAlias = aliases.get(0) + ".";

				if (joins != null && !joins.isEmpty())
					if (!aliases.contains(drc.getAlias().replaceAll("\\.", "")))
						for (DynamicJoinColumn djc : joins)
							if (!aliases.contains(djc.getJoinClass().getSimpleName())) {
								aliases.add(djc.getJoinClass().getSimpleName());
								crit.createCriteria(previousAlias + djc.getJoinColumn(), djc.getJoinClass().getSimpleName());
								System.out.println("   Join to " + previousAlias + djc.getJoinColumn() + " (" + djc.getJoinClass().getSimpleName() + ")");
								previousAlias = djc.getJoinClass().getSimpleName() + ".";
							} else
								previousAlias = djc.getJoinClass().getSimpleName() + ".";
			}
		}

		while (index < reportSelections.size())
			crit.add(addToCriteria(crit, reportSelections, 0));

		for (ReportColumn c : sort) {
			DynamicReportColumn drc = DynamicReportBase.getColumnByTypeIndex(bean.getReportType(), c.getColumnId());

			final Order o = c.getSortDirection() == 'A' ? org.hibernate.criterion.Order.asc(drc.getAlias() + drc.getColumnNameForCriteria()).ignoreCase()
					: org.hibernate.criterion.Order.desc(drc.getAlias() + drc.getColumnNameForCriteria()).ignoreCase();
			crit.addOrder(o);
		}

		index = 0;
		System.out.println("   Criterion: " + hcu.getCriteria().toString());
		return hcu;
	}
	private int index = 0;
	private List<List<List<Method>>> columnMethods = new ArrayList<List<List<Method>>>();

	public Criterion addToCriteria(Criteria crit, List<ReportSelection> reportSelections, int parenthesisDepth) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BReportSelection bSelection = new BReportSelection(reportSelections.get(index));
		DynamicReportColumn column = DynamicReportBase.getColumnByTypeIndex(bean.getReportType(), bSelection.getSelectionColumn());

		//deal with this selection's criterion
		Method method = null;
		try {
			method = Restrictions.class.getMethod(bSelection.getSelectionOperatorForCriteria(), new Class[]{String.class, Object.class});
		} catch (NoSuchMethodException noSuchMethodException) {
			method = Restrictions.class.getMethod(bSelection.getSelectionOperatorForCriteria(), new Class[]{String.class, Collection.class});
		} catch (SecurityException securityException) {
		}
		Criterion criterion = (Criterion) method.invoke(null, new Object[]{column.getAlias() + column.getColumnNameForCriteria(), bSelection.getCompareToObjectForCriteria()});

		String nextLogicOperator = "N";
		if (reportSelections.size() > (index + 1)) //if there is a next selection
		
			nextLogicOperator = (new BReportSelection(reportSelections.get(index + 1)).getLogicOperator() + "");
		parenthesisDepth += bSelection.getLeftParens();
		parenthesisDepth -= bSelection.getRightParens();
		if (parenthesisDepth == 0) {
			index++;
			return criterion;
		} else
			if (nextLogicOperator.equals("A")) {
				index++;
				return Restrictions.and(criterion, addToCriteria(crit, reportSelections, parenthesisDepth));
			} else if (nextLogicOperator.equals("O")) {
				index++;
				return Restrictions.or(criterion, addToCriteria(crit, reportSelections, parenthesisDepth));
			} else if (nextLogicOperator.equals("N")) {
				index++;
				return criterion;
			} else
				return null; //this should never happen
	}

	public List<ColumnDisplayObject> next() {
		try {
			if (scr == null)
				scr = applySelectionCriteria().scroll();

			if (scr.next()) {
				List<ColumnDisplayObject> ret = new ArrayList<ColumnDisplayObject>();
				Class clazz = DynamicReportBase.getReportClassForCriteria(bean);

				for (List<List<Method>> methods : getReportColumnMethods()) {
					ColumnDisplayObject cdo = new ColumnDisplayObject();
					final Object args[] = new Object[1];
					args[0] = clazz.cast(scr.get());
					for (Method method : methods.get(0)) //get display value first
					
						if (args[0].getClass() == PersistentCollection.class) {
							Iterator setIterator = ((Set) args[0]).iterator();
							List<Object> setObjectList = new ArrayList<Object>();
							while (setIterator.hasNext()) {
								Object nextArg = setIterator.next();
								setObjectList.add(method.invoke(nextArg, (Object[]) null));
							}
							args[0] = setObjectList;
						} else if (args[0] instanceof Collection) {
							Iterator setIterator = ((Collection) args[0]).iterator();
							List<Object> setObjectList = new ArrayList<Object>();
							while (setIterator.hasNext()) {
								Object nextArg = setIterator.next();
								setObjectList.add(method.invoke(nextArg, (Object[]) null));
							}
							args[0] = setObjectList;
						} else {
							args[0] = method.invoke(args[0], (Object[]) null);
							//
							if (args[0] == null) {
								args[0] = "";
								break;
//								throw new ArahantException("Method invokation returned null object (" + method.getName() + ")");
							}
						}
					cdo.setDisplay(args[0]);
					args[0] = clazz.cast(scr.get());
					for (Method method : methods.get(1)) //get break level sort compare value next
					
						if (args[0].getClass() == PersistentCollection.class) {
							Iterator setIterator = ((Set) args[0]).iterator();
							List<Object> setObjectList = new ArrayList<Object>();
							while (setIterator.hasNext()) {
								Object nextArg = setIterator.next();
								setObjectList.add(method.invoke(nextArg, (Object[]) null));
							}
							args[0] = setObjectList;
						} else if (args[0] instanceof Collection) {
							Iterator setIterator = ((Collection) args[0]).iterator();
							List<Object> setObjectList = new ArrayList<Object>();
							while (setIterator.hasNext()) {
								Object nextArg = setIterator.next();
								setObjectList.add(method.invoke(nextArg, (Object[]) null));
							}
							args[0] = setObjectList;
						} else {
							args[0] = method.invoke(args[0], (Object[]) null);
							//
							if (args[0] == null) {
								args[0] = "";
								break;
//								throw new ArahantException("Method invokation returned null object (" + method.getName() + ")");
							}
						}
					cdo.setBreakCompareItem(args[0]);
					ret.add(cdo);
				}
				return ret;
			} else
				return null;
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(BReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(BReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(BReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(BReport.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<List<List<Method>>> getReportColumnMethods() throws NoSuchMethodException {
		if (columnMethods.isEmpty())
			for (ReportColumn rc : getReportColumns()) {
				List<List<Method>> colMethods = new ArrayList<List<Method>>();
				List<Method> displayMethods = new ArrayList<Method>();
				List<Method> breakCompareMethods = new ArrayList<Method>();
				DynamicReportColumn drc = DynamicReportBase.getColumnByTypeIndex(bean.getReportType(), rc.getColumnId());
				Class clazz = DynamicReportBase.getReportClassForCriteria(bean);
				String n;
				String n2;
				Method displayMethod;
				Method breakCompareMethod;
				final Class args[] = new Class[1];
				args[0] = clazz;

				if (drc.getJoinColumns() == null || drc.getJoinColumns().length == 0) //we didn't join to anything
				{
					n = "get" + drc.getColumnName().substring(0, 1).toUpperCase() + drc.getColumnName().substring(1);
					n2 = "get" + drc.getBreakLevelSortCompare().substring(0, 1).toUpperCase() + drc.getBreakLevelSortCompare().substring(1);
					displayMethod = args[0].getMethod(n, new Class[0]);
					breakCompareMethod = args[0].getMethod(n2, new Class[0]);
					args[0] = displayMethod.getReturnType();
					displayMethods.add(displayMethod);
					breakCompareMethods.add(breakCompareMethod);
				} else {	//do all the joins first
					for (DynamicJoinColumn djc : drc.getJoinColumnList()) {
						n = "get" + djc.getJoinColumn().substring(0, 1).toUpperCase() + djc.getJoinColumn().substring(1);
						displayMethod = args[0].getMethod(n, new Class[0]);
						Type type = displayMethod.getGenericReturnType();
						if (type instanceof ParameterizedType) {
							ParameterizedType pt = (ParameterizedType) type;
							System.out.println("Type: " + pt.getActualTypeArguments()[0]);
							args[0] = (Class) pt.getActualTypeArguments()[0];
						} else
							args[0] = displayMethod.getReturnType();
						displayMethods.add(displayMethod);
						breakCompareMethods.add(displayMethod);
					}

					n = "get" + drc.getColumnName().substring(0, 1).toUpperCase() + drc.getColumnName().substring(1);
					n2 = "get" + drc.getBreakLevelSortCompare().substring(0, 1).toUpperCase() + drc.getBreakLevelSortCompare().substring(1);
					displayMethod = args[0].getMethod(n, new Class[0]);
					breakCompareMethod = args[0].getMethod(n2, new Class[0]);
					args[0] = displayMethod.getReturnType();
					displayMethods.add(displayMethod);
					breakCompareMethods.add(breakCompareMethod);
				}
				colMethods.add(displayMethods);
				colMethods.add(breakCompareMethods);
				columnMethods.add(colMethods);
			}
		return columnMethods;
	}

	private static BReport[] makeArray(List<Report> l) {
		BReport[] ret = new BReport[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BReport(l.get(loop));
		return ret;
	}

//	private List<ReportSelection> orderSelectionsForCriteria() {
//
//		List<ReportSelection> ret = new ArrayList<ReportSelection>();
//		List<ReportSelection> loopList = getReportSelections();
//		int sizeCheck = 0;
//		while(ret.size() != loopList.size())
//		{
//			for(ReportSelection rs : loopList)
//			{
//				int[] joinColumns = HrBenefitJoinColumns.getColumnByIndex(rs.getSelectionColumn()).getJoinColumns();
//				if(joinColumns == null && sizeCheck == 0)
//				{
//					ret.add(rs);
//				}
//				else if(joinColumns != null && joinColumns.length == sizeCheck)
//				{
//					ret.add(rs);
//				}
//			}
//			sizeCheck++;
//		}
//		return ret;
//	}
	public static void main(String[] args) {

		for (OrgGroupAssociation oga : ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.ORGGROUPTYPE, BCompanyBase.COMPANY_TYPE).list()) {
			BOrgGroup orgGroup = new BOrgGroup(oga.getOrgGroup());
			int notifyDays = orgGroup.getEvalEmailNotifyDays();
			int today = DateUtils.getDate(new Date());
			BEmployee supervisor = new BEmployee(oga.getPersonId());
			HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class);
			Criteria tempCrit = hcu.getCriteria();
			tempCrit.add(Restrictions.ne(Employee.PERSONID, supervisor.getPersonId()));
			tempCrit.createCriteria(Employee.ORGGROUPASSOCIATIONS, "oga");
			tempCrit.add(Restrictions.eq("oga." + OrgGroupAssociation.ORG_GROUP_ID, oga.getOrgGroupId()));
			tempCrit.createCriteria(Employee.HREMPLOYEEEVALSFOREMPLOYEEID, "evals");
			tempCrit.add(Restrictions.or(Restrictions.and(Restrictions.le("evals." + HrEmployeeEval.NEXTEVALDATE, DateUtils.addDays(today, notifyDays)),
					Restrictions.gt("evals." + HrEmployeeEval.NEXTEVALDATE, today)),
					// Restrictions.and(
					Restrictions.and(
					Restrictions.le("evals." + HrEmployeeEval.EVALDATE, DateUtils.addDays(today, notifyDays)),
					//Restrictions.gt("evals." + HrEmployeeEval.EVALDATE, today)
					//),
					Restrictions.eq("evals." + HrEmployeeEval.FINALDATE, 0))));
			int x = 0;
		}

	}
}
