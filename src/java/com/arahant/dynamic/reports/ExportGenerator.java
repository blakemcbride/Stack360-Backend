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

import com.arahant.beans.Report;
import com.arahant.beans.ReportColumn;
import com.arahant.business.BReport;
import com.arahant.business.BReportColumn;
import com.arahant.business.BReportTitle;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 */
public class ExportGenerator {

	private Report report;
	private File csvFile;
	private DelimitedFileWriter dfw;

	public ExportGenerator() throws ArahantException {
	}

	public String build(Report report) throws Exception {
		this.report = report;
		csvFile = new File(FileSystemUtils.getWorkingDirectory(), report.getReportName() + (new Date().getTime()) + ".csv");
		dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);
		String reportId = report.getReportId();
		List<ReportColumn> cols = ArahantSession.getHSU().createCriteria(ReportColumn.class).eq(ReportColumn.REPORT_ID, reportId).list();

		try {
			exportColumns(cols);
		} catch (IOException ex) {
			System.out.println("Error in ExportGenerator.");
		} finally {
			dfw.close();
		}
		return csvFile.getName();
	}

	protected void exportColumns(final ReportColumn[] columns) throws Exception {
		List<ReportColumn> colList = new ArrayList<ReportColumn>();
		colList.addAll(Arrays.asList(columns));

		Collections.sort(colList);
		exportColumns(colList);
	}

	protected void exportColumns(final List<ReportColumn> columns) throws Exception {
		BReport br = new BReport(report);

		Collections.sort(columns);
		exportColumnHeaders(columns);
		HashMap<ReportColumn, Double> columnTotals = new HashMap<ReportColumn, Double>();

		for (ReportColumn c : columns)
			columnTotals.put(c, 0.0);

		List<ColumnDisplayObject> row = br.next();
		while (row != null) {
			for (int i = 0; i < row.size(); i++) {
				ReportColumn column = columns.get(i);
				Object cell = row.get(i).getDisplay();
				int columnId = column.getColumnId();
				double currentTotal = columnTotals.get(column);
				DynamicReportColumn col = DynamicReportBase.getColumnByTypeIndex(report.getReportType(), columnId);

				String pattern = "0";
				if (column.getNumericDigits() != 0) {
					pattern += ".";
					for (int j = 0; j < column.getNumericDigits(); j++)
						pattern += "0";
				}

				DecimalFormat df = new DecimalFormat(pattern);
				String formattedCode = DynamicReportBase.formatCode(column, cell, df);

				Class clazz = col.getColumnType();

				dfw.writeField(formattedCode);

				if (column.getDisplayTotals() == 'Y')
					if (clazz.equals(int.class) || clazz.equals(Integer.class))
						columnTotals.put(column, currentTotal + ((Integer) cell).intValue());
					else if (clazz.equals(double.class) || clazz.equals(Double.class))
						columnTotals.put(column, currentTotal + ((Double) cell).doubleValue());
					else if (clazz.equals(float.class) || clazz.equals(Float.class))
						columnTotals.put(column, currentTotal + ((Float) cell).floatValue());
					else
						columnTotals.put(column, currentTotal + 1);
			}
			dfw.endRecord();
			row = br.next();
		}
	}

	private void exportColumnHeaders(List<ReportColumn> columns) throws Exception {
		for (ReportColumn c : columns)
			dfw.writeField(c.getTitle());
		dfw.endRecord();
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BReport report = new BReport();
		BReportTitle title1 = new BReportTitle();
		BReportTitle title2 = new BReportTitle();
		BReportTitle title3 = new BReportTitle();
		BReportTitle title4 = new BReportTitle();
		BReportTitle titleFooter1 = new BReportTitle();
		BReportTitle titleFooter2 = new BReportTitle();
		BReportTitle titleFooter3 = new BReportTitle();
		BReportColumn column1 = new BReportColumn();
		BReportColumn column2 = new BReportColumn();
		BReportColumn column3 = new BReportColumn();
		BReportColumn column4 = new BReportColumn();

		hsu.beginTransaction();

		String reportId = report.create();
		report.setLinesInColumnTitle((short) 2);
		report.setDescription("Used for testing headers");
		report.setReportName("Test Report" + (ArahantSession.getHSU().createCriteria(Report.class).count() + 1));
		report.setDefaultSpaceBetweenColumns(0);
		report.insert();
		hsu.commitTransaction();
		hsu.beginTransaction();

		title1.create();
		title1.setPageLocation('A');
		title1.setSequenceNumber(1);
		title1.setFieldType('R');
		title1.setFontSize(24);
		title1.setFontBold('Y');
		title1.setEndLine('Y');
		title1.setJustification('C');
		title1.setReportId(reportId);
		title1.insert();

		title2.create();
		title2.setPageLocation('A');
		title2.setSequenceNumber(2);
		title2.setFieldType('T');
		title2.setFontSize(14);
		title2.setFontBold('N');
		title2.setEndLine('Y');
		title2.setJustification('C');
		title2.setVerbiage(report.getDescription());
		title2.setReportId(reportId);
		title2.insert();

		title3.create();
		title3.setPageLocation('A');
		title3.setSequenceNumber(3);
		title3.setFieldType('C');
		title3.setFontSize(10);
		title3.setFontBold('N');
		title3.setEndLine('N');
		title3.setJustification('C');
		title3.setReportId(reportId);
		title3.insert();

		title4.create();
		title4.setPageLocation('A');
		title4.setSequenceNumber(4);
		title4.setFieldType('D');
		title4.setFontSize(10);
		title4.setFontBold('N');
		title4.setEndLine('Y');
		title4.setJustification('C');
		title4.setReportId(reportId);
		title4.insert();

		titleFooter1.create();
		titleFooter1.setPageLocation('B');
		titleFooter1.setSequenceNumber(6);
		titleFooter1.setFieldType('T');
		titleFooter1.setFontSize(6);
		titleFooter1.setFontBold('N');
		titleFooter1.setEndLine('N');
		titleFooter1.setJustification('R');
		titleFooter1.setVerbiage("Page: ");
		titleFooter1.setReportId(reportId);
		titleFooter1.insert();

		titleFooter2.create();
		titleFooter2.setPageLocation('B');
		titleFooter2.setSequenceNumber(7);
		titleFooter2.setFieldType('P');
		titleFooter2.setFontSize(6);
		titleFooter2.setFontBold('N');
		titleFooter2.setEndLine('N');
		titleFooter2.setJustification('L');
		titleFooter2.setReportId(reportId);
		titleFooter2.insert();

		titleFooter3.create();
		titleFooter3.setPageLocation('B');
		titleFooter3.setSequenceNumber(5);
		titleFooter3.setFieldType('R');
		titleFooter3.setFontSize(6);
		titleFooter3.setFontBold('N');
		titleFooter3.setEndLine('Y');
		titleFooter3.setJustification('C');
		titleFooter3.setReportId(reportId);
		titleFooter3.insert();

		column1.create();
		column1.setTitle("Top Aligned");
		column1.setColumnId(1);
		column1.setSequenceNumber(1);
		column1.setTitleJustification('C');
		column1.setVerticalJustification('T');
		column1.setColumnJustification('L');
		column1.setLines(1);
		column1.setReportId(reportId);
		column1.insert();

		column2.create();
		column2.setTitle("Middle Aligned");
		column2.setColumnId(2);
		column2.setSequenceNumber(2);
		column2.setTitleJustification('C');
		column2.setVerticalJustification('M');
		column2.setColumnJustification('C');
		column2.setLines(2);
		column2.setReportId(reportId);
		column2.insert();

		column3.create();
		column3.setTitle("Bottom Aligned");
		column3.setColumnId(3);
		column3.setSequenceNumber(3);
		column3.setTitleJustification('C');
		column3.setVerticalJustification('B');
		column3.setColumnJustification('R');
		column3.setLines(3);
		column3.setReportId(reportId);
		column3.insert();

		column4.create();
		column4.setTitle("Fourth Column");
		column4.setColumnId(3);
		column4.setSequenceNumber(4);
		column4.setTitleJustification('C');
		column4.setVerticalJustification('B');
		column4.setColumnJustification('R');
		column4.setLines(1);
		column4.setReportId(reportId);
		column4.insert();

		Report r = new BReport(reportId).getBean();
		hsu.commitTransaction();

		try {
			new ExportGenerator().build(r);
		} catch (Exception ex) {
		}
	}
}
