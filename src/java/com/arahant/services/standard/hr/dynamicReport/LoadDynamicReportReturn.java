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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.beans.ReportColumn;
import com.arahant.beans.ReportGraphic;
import com.arahant.beans.ReportSelection;
import com.arahant.beans.ReportTitle;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BReport;
import com.arahant.services.TransmitReturnBase;
import java.util.ArrayList;
import java.util.List;


public class LoadDynamicReportReturn extends TransmitReturnBase {

	private int reportType;
	private String reportName;
	private String description;
	private String pageOrientation;
	private double pageOffsetLeft;
	private double pageOffsetTop;
	private int defaultFontSize;
	private int linesInColumnTitle;
	private double defaultSpaceBetweenColumns;
	private SaveDynamicReportColumn[] column;
	private SaveDynamicReportSelection[] selection;
	private SaveDynamicReportTitle[] title;
	private SaveDynamicReportGraphic[] graphic;
	private ListCategoriesReturnItem[] category;
	private ListBenefitsReturnItem[] benefit;
	private ListConfigsReturnItem[] config;
	private boolean runTime;



	void setData(BReport bReport) {
		reportType = bReport.getReportType();
		reportName = bReport.getReportName();
		description = bReport.getDescription();
		pageOrientation = bReport.getPageOrientation();
		pageOffsetLeft = bReport.getPageOffsetLeft();
		pageOffsetTop = bReport.getPageOffsetTop();
		defaultFontSize = bReport.getDefaultFontSize();
		linesInColumnTitle = bReport.getLinesInColumnTitle();
		defaultSpaceBetweenColumns = bReport.getDefaultSpaceBetweenColumns();
		for(ReportSelection rs : bReport.getReportSelections())
			if(rs.getSelectionType() == 'R') {
				runTime = true;
				break;
			}
		setColumn(bReport.getReportColumns());
		setSelection(bReport.getReportSelections());
		setTitle(bReport.getReportTitles());
		setGraphic(bReport.getReportGraphics());
		setSelectionItems();
	}

	public boolean getRunTime() {
		return runTime;
	}

	public void setRunTime(boolean runTime) {
		this.runTime = runTime;
	}

	public SaveDynamicReportColumn[] getColumn() {
		return column;
	}

	public void setColumn(SaveDynamicReportColumn[] column) {
		this.column = column;
	}

	public void setColumn(List<ReportColumn> columns) {
		SaveDynamicReportColumn[] arr = new SaveDynamicReportColumn[columns.size()];
		for(int loop = 0; loop < columns.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportColumn(columns.get(loop));
		}
		this.column = arr;
	}

	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(int defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	public double getDefaultSpaceBetweenColumns() {
		return defaultSpaceBetweenColumns;
	}

	public void setDefaultSpaceBetweenColumns(double defaultSpaceBetweenColumns) {
		this.defaultSpaceBetweenColumns = defaultSpaceBetweenColumns;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLinesInColumnTitle() {
		return linesInColumnTitle;
	}

	public void setLinesInColumnTitle(int linesInColumnTitle) {
		this.linesInColumnTitle = linesInColumnTitle;
	}

	public double getPageOffsetLeft() {
		return pageOffsetLeft;
	}

	public void setPageOffsetLeft(double pageOffsetLeft) {
		this.pageOffsetLeft = pageOffsetLeft;
	}

	public double getPageOffsetTop() {
		return pageOffsetTop;
	}

	public void setPageOffsetTop(double pageOffsetTop) {
		this.pageOffsetTop = pageOffsetTop;
	}

	public String getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public SaveDynamicReportSelection[] getSelection() {
		return selection;
	}

	public void setSelection(SaveDynamicReportSelection[] selection) {
		this.selection = selection;
	}

	public void setSelection(List<ReportSelection> selections) {
		SaveDynamicReportSelection[] arr = new SaveDynamicReportSelection[selections.size()];
		for(int loop = 0; loop < selections.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportSelection(selections.get(loop));
		}
		this.selection = arr;
	}

	public SaveDynamicReportGraphic[] getGraphic() {
		return graphic;
	}

	public void setGraphic(SaveDynamicReportGraphic[] graphic) {
		this.graphic = graphic;
	}

	public void setGraphic(List<ReportGraphic> graphics) {
		SaveDynamicReportGraphic[] arr = new SaveDynamicReportGraphic[graphics.size()];
		for(int loop = 0; loop < graphics.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportGraphic(graphics.get(loop));
		}
		this.graphic = arr;
	}

	public SaveDynamicReportTitle[] getTitle() {
		return title;
	}

	public void setTitle(SaveDynamicReportTitle[] title) {
		this.title = title;
	}

	public void setTitle(List<ReportTitle> titles) {
		SaveDynamicReportTitle[] arr = new SaveDynamicReportTitle[titles.size()];
		for(int loop = 0; loop < titles.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportTitle(titles.get(loop));
		}
		this.title = arr;
	}

	public ListBenefitsReturnItem[] getBenefit() {
		return benefit;
	}

	public void setBenefit(ListBenefitsReturnItem[] benefit) {
		this.benefit = benefit;
	}

	public ListCategoriesReturnItem[] getCategory() {
		return category;
	}

	public void setCategory(ListCategoriesReturnItem[] category) {
		this.category = category;
	}

	public ListConfigsReturnItem[] getConfig() {
		return config;
	}

	public void setConfig(ListConfigsReturnItem[] config) {
		this.config = config;
	}

	private void setSelectionItems() {

		if(getReportType() == 1) //HrBenefitJoin
		{
			List<ListConfigsReturnItem> configs = new ArrayList<ListConfigsReturnItem>();
			List<ListBenefitsReturnItem> benefits = new ArrayList<ListBenefitsReturnItem>();
			List<ListCategoriesReturnItem> categories = new ArrayList<ListCategoriesReturnItem>();
			for(SaveDynamicReportSelection s : getSelection())
			{
				if(s.getSelectionColumn() == 7)
				{
					String[] sa = s.getSelectionValue().split("::");
					for (String ss : sa)
					{
						configs.add(new ListConfigsReturnItem(new BHRBenefitConfig(ss)));
					}
				}
				else if(s.getSelectionColumn() == 8)
				{
					String[] sa = s.getSelectionValue().split("::");
					for (String ss : sa)
					{
						benefits.add(new ListBenefitsReturnItem(new BHRBenefit(ss)));
					}
				}
				else if(s.getSelectionColumn() == 9)
				{
					String[] sa = s.getSelectionValue().split("::");
					for (String ss : sa)
					{
						categories.add(new ListCategoriesReturnItem(new BHRBenefitCategory(ss)));
					}
				}
			}
			setConfigs(configs);
			setBenefits(benefits);
			setCategories(categories);
		}
	}

	private void setConfigs(List<ListConfigsReturnItem> configs) {
		config = new ListConfigsReturnItem[configs.size()];
		for(int loop = 0; loop < configs.size(); loop++)
		{
			config[loop] = configs.get(loop);
		}
	}

	private void setBenefits(List<ListBenefitsReturnItem> benefits) {
		benefit = new ListBenefitsReturnItem[benefits.size()];
		for(int loop = 0; loop < benefits.size(); loop++)
		{
			benefit[loop] = benefits.get(loop);
		}
	}

	private void setCategories(List<ListCategoriesReturnItem> categories) {
		category = new ListCategoriesReturnItem[categories.size()];
		for(int loop = 0; loop < categories.size(); loop++)
		{
			category[loop] = categories.get(loop);
		}
	}


	
}

	
