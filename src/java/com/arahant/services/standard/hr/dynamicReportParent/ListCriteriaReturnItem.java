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


/**
 * 
 */
package com.arahant.services.standard.hr.dynamicReportParent;

import com.arahant.business.BReport;
import com.arahant.business.BReportSelection;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.services.standard.hr.dynamicReport.ListObject;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
import java.util.List;


public class ListCriteriaReturnItem {

	private String name;
	private int reportType;
	private String selectionId;
	private String description;
	private String value = "";
	private String label = "";
	private ListObject[] listItems;
	private String firstText = "";
	private String secondText = "";
	private String thirdText = "";
	private String operator = "";
	
	public ListCriteriaReturnItem() {
		
	}

	ListCriteriaReturnItem(BReportSelection rs) {
		BReport bp = new BReport(rs.getReportId());
		DynamicReportColumn rc = DynamicReportBase.getColumnByTypeIndex(bp.getReportType(), rs.getSelectionColumn());

		name = rc.getDisplayName();
		reportType = rc.getControlType();
		selectionId = rs.getReportSelectionId();
		value = rs.getSelectionValue();
		label = rs.getSelectionValue();
		description = rs.getDescription();
		operator = rs.getSelectionOperator();

		if(rc.getControlType() == DynamicReportColumn.CONTROLTYPE_TRI_SELECT) {
			firstText = (String)(rc.getRunTimeSelectionList().get(0).get(1));
			secondText = (String)(rc.getRunTimeSelectionList().get(1).get(1));
			thirdText = (String)(rc.getRunTimeSelectionList().get(2).get(1));
			if(firstText.charAt(0) == rs.getSelectionValue().charAt(0))
				label = firstText;
			else if(secondText.charAt(0) == rs.getSelectionValue().charAt(0))
				label = secondText;
			else if(thirdText.charAt(0) == rs.getSelectionValue().charAt(0))
				label = thirdText;
		}
		else if(rc.getControlType() == DynamicReportColumn.CONTROLTYPE_LIST) {
			List<List<Object>> selectionList = rc.getRunTimeSelectionList();
			int length = selectionList.size();
			listItems = new ListObject[length];
			
			for(int i = 0; i < length; i++) {
				ListObject o = new ListObject();
				o.setVal((String)(selectionList.get(i).get(0)));
				o.setLabel((String)(selectionList.get(i).get(1)));
				listItems[i] = o;
			}
		}
		else if(rc.getControlType() == DynamicReportColumn.CONTROLTYPE_DATE) {
			label = StringUtils.isEmpty(value) ? "" : DateUtils.getDateFormatted(Integer.valueOf(value).intValue());
		}

		if(!rc.getColumnName().equals(rc.getCriteriaField()) || StringUtils.isEmpty(rc.getCriteriaField())) {
			for(List<Object> o : rc.getRunTimeSelectionList()) {
				if(rs.getSelectionValue().equals((String)(o.get(0))))
					label = (String)o.get(1);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(String selectionId) {
		this.selectionId = selectionId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFirstText() {
		return firstText;
	}

	public void setFirstText(String firstText) {
		this.firstText = firstText;
	}

	public String getSecondText() {
		return secondText;
	}

	public void setSecondText(String secondText) {
		this.secondText = secondText;
	}

	public String getThirdText() {
		return thirdText;
	}

	public void setThirdText(String thirdText) {
		this.thirdText = thirdText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ListObject[] getListItems() {
		return listItems;
	}

	public void setListItems(ListObject[] listItems) {
		this.listItems = listItems;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}

	
