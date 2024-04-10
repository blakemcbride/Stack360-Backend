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

import com.arahant.beans.Report;
import com.arahant.beans.ReportSelection;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.dynamic.reports.DynamicJoinColumn;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class BReportSelection extends SimpleBusinessObjectBase<ReportSelection> implements IDBFunctions {

	/**
	 */
	public BReportSelection() {
	}

	/**
	 * @param reportTitleId
	 * @throws ArahantException
	 */
	public BReportSelection(final String reportSelectionId) throws ArahantException {
		internalLoad(reportSelectionId);
	}

	/**
	 * @param ReportTitle
	 */
	public BReportSelection(final ReportSelection rs) {
		bean = rs;
	}

	public String getReportSelectionId() {
		return bean.getReportSelectionId();
	}

	public void setReportSelectionId(String reportSelectionId) {
		bean.setReportSelectionId(reportSelectionId);
	}

	public Report getReport() {
		return bean.getReport();
	}

	public void setReport(Report report) {
		bean.setReport(report);
	}

	public String getReportId() {
		return bean.getReportId();
	}

	public void setReportId(String reportId) {
		bean.setReport(ArahantSession.getHSU().get(Report.class, reportId));
	}

	public int getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	public void setSequenceNumber(int sequenceNumber) {
		bean.setSequenceNumber((short) sequenceNumber);
	}

	public String getSelectionType() {
		return String.valueOf(bean.getSelectionType());
	}

	public void setSelectionType(char selectionType) {
		bean.setSelectionType(selectionType);
	}

	public void setSelectionType(String selectionType) {
		bean.setSelectionType(selectionType.charAt(0));
	}

	public int getSelectionColumn() {
		return bean.getSelectionColumn();
	}

	public void setSelectionColumn(short selectionColumn) {
		bean.setSelectionColumn(selectionColumn);
	}

	public void setSelectionColumn(int selectionColumn) {
		bean.setSelectionColumn((short) selectionColumn);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public String getSelectionOperator() {
		return bean.getSelectionOperator();
	}

	public void setSelectionOperator(String selectionOperator) {
		bean.setSelectionOperator(selectionOperator);
	}

	public String getSelectionValueList() {
		return bean.getSelectionValueList();
	}

	public void setSelectionValueList(String selectionValueList) {
		bean.setSelectionValueList(selectionValueList);
	}

	public String getSelectionValue() {
		if (!isEmpty(bean.getSelectionValue()))
			return bean.getSelectionValue();
		else if (!isEmpty(bean.getSelectionValueList()))
			return bean.getSelectionValueList();
		else
			return "";
	}

	public void setSelectionValue(String selectionValue) {
		if (selectionValue.length() > 60 || selectionValue.contains("::")) {
			bean.setSelectionValue(null);
			bean.setSelectionValueList(selectionValue);
		} else {
			bean.setSelectionValue(selectionValue);
			bean.setSelectionValueList(null);
		}
	}

	public int getSelectionColumn2() {
		return bean.getSelectionColumn();
	}

	public void setSelectionColumn2(short selectionColumn2) {
		bean.setSelectionColumn2(selectionColumn2);
	}

	public void setSelectionColumn2(int selectionColumn2) {
		bean.setSelectionColumn2((short) selectionColumn2);
	}

	public String getLogicOperator() {
		return String.valueOf(bean.getLogicOperator());
	}

	public void setLogicOperator(char logicOperator) {
		bean.setLogicOperator(logicOperator);
	}

	public void setLogicOperator(String logicOperator) {
		bean.setLogicOperator(logicOperator.charAt(0));
	}

	public int getLeftParens() {
		return bean.getLeftParens();
	}

	public void setLeftParens(short leftParens) {
		bean.setLeftParens(leftParens);
	}

	public void setLeftParens(int leftParens) {
		bean.setLeftParens((short) leftParens);
	}

	public int getRightParens() {
		return bean.getRightParens();
	}

	public void setRightParens(short rightParens) {
		bean.setRightParens(rightParens);
	}

	public void setRightParens(int rightParens) {
		bean.setRightParens((short) rightParens);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for (String id : ids)
			new BReportSelection(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ReportSelection();
		bean.generateId();
		return getReportSelectionId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ReportSelection.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static BReportSelection[] makeArray(final Collection<ReportSelection> l) throws ArahantException {
		final BReportSelection[] ret = new BReportSelection[l.size()];
		int loop = 0;

		for (ReportSelection p : l)
			ret[loop++] = new BReportSelection(p);

		return ret;
	}

	public String getSelectionOperatorForCriteria() {
		String selectionOperator = getSelectionOperator();
		if (selectionOperator.equals("EQ"))
			selectionOperator = "eq";
		else if (selectionOperator.equals("GT"))
			selectionOperator = "gt";
		else if (selectionOperator.equals("GE"))
			selectionOperator = "ge";
		else if (selectionOperator.equals("LT"))
			selectionOperator = "lt";
		else if (selectionOperator.equals("LE"))
			selectionOperator = "le";
		else if (selectionOperator.equals("NE"))
			selectionOperator = "ne";
		else if (selectionOperator.equals("NA"))
			selectionOperator = "eq";
		else if (selectionOperator.equals("IN"))
			selectionOperator = "in";
		else if (selectionOperator.equals("LK"))
			selectionOperator = "like";

		return selectionOperator;
	}

	public Object getCompareToObjectForCriteria() {
		Object ret = new Object();
		BReport report = new BReport(getReport());
		DynamicReportColumn column = DynamicReportBase.getColumnByTypeIndex(report.getReportType(), getSelectionColumn());
		DynamicReportColumn column2 = DynamicReportBase.getColumnByTypeIndex(report.getReportType(), getSelectionColumn2());
		if (getSelectionType().equals("V") || getSelectionType().equals("R")) {
			if (column.getColumnType() == String.class) {
				ret = getSelectionValue();
				String[] retArr = ret.toString().split("::");
				if (retArr.length > 1)
					ret = Arrays.asList(retArr);
			} else if (column.getColumnType() == int.class)
				ret = Integer.parseInt(getSelectionValue());
			else if (column.getColumnType() == double.class)
				ret = Double.parseDouble(getSelectionValue());
			else if (column.getColumnType() == Date.class)
				ret = DateUtils.getDate(Integer.parseInt(getSelectionValue()));
			else if (column.getColumnType() == char.class && getSelectionValue().length() > 0)
				ret = getSelectionValue().charAt(0);
		} else if (getSelectionType().equals("C"))
			ret = column2.getColumnName();
		else if (getSelectionType().equals("D"))
			if (column.getColumnType() == Date.class)
				ret = new Date();
			else if (column.getColumnType() == int.class)
				ret = DateUtils.now();

		return ret;
	}

//	public DynamicReportColumn getDynamicReportColumn()
//	{
//		if(getReport().getReportType() == 1)
//		{
//			return HrBenefitJoinColumns.getColumnByIndex(getSelectionColumn());
//		}
//		else if(getReport().getReportType() == 2)
//		{
//			return EmployeeColumns.getColumnByIndex(getSelectionColumn());
//		}
//		return null;
//	}
	public List<DynamicJoinColumn> getDynamicJoinColumns() {
		int reportType = getReport().getReportType();

		List<DynamicJoinColumn> ret = new ArrayList<DynamicJoinColumn>();
		for (int i : DynamicReportBase.getColumnByTypeIndex(reportType, getSelectionColumn()).getJoinColumns())
			ret.add(DynamicReportBase.getJoinColumnByTypeIndex(reportType, i));
		return ret;
	}
}
