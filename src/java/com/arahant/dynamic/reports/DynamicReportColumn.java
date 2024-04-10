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



package com.arahant.dynamic.reports;

import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DynamicReportColumn {

	private String columnName;
	private String displayName;
	private String criteriaField;
	private Class columnType;
	private Class ownerClass;
	private int[] joinColumns;
	private int index;
	private int formatType;
	private int controlType;
	private ISelectionList selectionList;
	private List<?> operators;
	private String breakLevelSortCompare;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_CHARACTER = 2;
	public static final int TYPE_DATE = 3;
	public static final int TYPE_TIME = 4;
	public static final int TYPE_BOOLEAN = 5;
	public static final int TYPE_PERCENTAGE = 6;
	public static final int TYPE_MONEY = 7;
	public static final int CONTROLTYPE_TEXT = 1;
	public static final int CONTROLTYPE_NUMERIC = 2;
	public static final int CONTROLTYPE_DATE = 3;
	public static final int CONTROLTYPE_TRI_SELECT = 4;
	public static final int CONTROLTYPE_LIST = 5;

	public DynamicReportColumn(int index, String columnName, String displayName, Class columnType, int formatType, int controlType, List<?> operators, ISelectionList selectionList, String breakLevelSortCompare, Class ownerClass, int[] joinColumn, String criteriaField) {
		this.columnName = columnName;
		this.displayName = displayName;
		this.columnType = columnType;
		this.formatType = formatType;
		this.controlType = controlType;
		this.ownerClass = ownerClass;
		this.index = index;
		this.joinColumns = joinColumn;
		this.criteriaField = criteriaField;
		this.selectionList = selectionList;
		this.operators = operators;
		this.breakLevelSortCompare = breakLevelSortCompare;
	}

	public DynamicReportColumn() {
	}

	public String getBreakLevelSortCompare() {
		if (StringUtils.isEmpty(breakLevelSortCompare))
			return this.columnName;
		return breakLevelSortCompare;
	}

	public void setBreakLevelSortCompare(String breakLevelSortCompare) {
		this.breakLevelSortCompare = breakLevelSortCompare;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Class getColumnType() {
		return columnType;
	}

	public void setColumnType(Class columnType) {
		this.columnType = columnType;
	}

	public Class getOwnerClass() {
		return ownerClass;
	}

	public void setOwnerClass(Class ownerClass) {
		this.ownerClass = ownerClass;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int[] getJoinColumns() {
		return joinColumns;
	}

	public void setJoinColumns(int[] joinColumn) {
		this.joinColumns = joinColumn;
	}

	public String getCriteriaField() {
		return criteriaField;
	}

	public void setCriteriaField(String criteriaField) {
		this.criteriaField = criteriaField;
	}

	public int getFormatType() {
		return formatType;
	}

	public void setFormatType(int formatType) {
		this.formatType = formatType;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public ISelectionList getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(ISelectionList selectionList) {
		this.selectionList = selectionList;
	}

	public List<?> getOperators() {
		return operators;
	}

	public void setOperators(List<?> operators) {
		this.operators = operators;
	}

	public String getAlias() {
		if (this.joinColumns == null || this.joinColumns.length == 0)
			return ownerClass.getSimpleName() + ".";
		else
			return DynamicReportBase.getJoinColumnByClass(ownerClass, joinColumns[joinColumns.length - 1]).getJoinClass().getSimpleName() + ".";
	}

	public Class getValueClass() {
		if (this.joinColumns == null || this.joinColumns.length == 0)
			return ownerClass;
		else
			return DynamicReportBase.getJoinColumnByClass(ownerClass, joinColumns[joinColumns.length - 1]).getJoinClass();
	}

	public List<DynamicJoinColumn> getJoinColumnList() {
		List<DynamicJoinColumn> ret = new ArrayList<DynamicJoinColumn>();
		for (int i = 0; i < joinColumns.length; i++)
			ret.add(DynamicReportBase.getJoinColumnByClass(ownerClass, joinColumns[i]));

		return ret;
	}

	public String getColumnNameForCriteria() {
		if (StringUtils.isEmpty(this.criteriaField))
			return this.columnName;
		else
			return this.criteriaField;
	}

	public Class getColumnTypeForCriteria() {
		if (getColumnType() == int.class)
			return Integer.class;
		else if (getColumnType() == short.class)
			return Short.class;
		else if (getColumnType() == char.class)
			return Character.class;
		else if (getColumnType() == boolean.class)
			return Boolean.class;
		else if (getColumnType() == double.class)
			return Double.class;
		return getColumnType();
	}

	public List<List<Object>> getRunTimeSelectionList() {
		List<List<Object>> ret = new ArrayList<List<Object>>();

		String methodName = getColumnNameForCriteria();
		String lowerCase = methodName.charAt(0) + "";
//		methodName = "get" + lowerCase.toUpperCase() + methodName.substring(1);
		if (selectionList != null)
			for (Object o : selectionList.list())
				try {
					if (getControlType() == CONTROLTYPE_LIST) {
						List<Object> r = new ArrayList<Object>();
						r.add(((Object[]) o)[0]);
						r.add(((Object[]) o)[1]);
						ret.add(r);
					} else if (getControlType() == CONTROLTYPE_TRI_SELECT) {
						List<Object> r = new ArrayList<Object>();
						r.add(o.toString().charAt(0) + "");
						r.add(o.toString());
						ret.add(r);
					}
				} catch (Exception e) {
					System.out.println("Run Time Selection List error for " + getDisplayName());
				}

		return ret;
	}
//	@Override
//	public int hashCode() {
//		final int PRIME = 31;
//		int result = 1;
//
//		result += this.columnName.hashCode();
//		result += this.displayName.hashCode();
//		result += this.index;
//		result *= PRIME;
//
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//       if (this == obj)
//           return true;
//       if (obj == null)
//           return false;
//       if (getClass() != obj.getClass())
//           return false;
//       final DynamicReportColumn other = (DynamicReportColumn) obj;
//       if (this.hashCode() != other.hashCode())
//           return false;
//       return true;
//   }
}
