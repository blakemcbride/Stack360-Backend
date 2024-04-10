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
import com.arahant.beans.ReportGraphic;
import com.arahant.beans.ReportTitle;
import com.arahant.business.BReport;
import com.arahant.business.BReportColumn;
import com.arahant.business.BReportTitle;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;

/**
 *
 */
public class ReportGenerator extends DynamicReportBase {

	public ReportGenerator(Report report) throws ArahantException {
		super(report);
	}

	public String build(Report report) {
		String reportId = report.getReportId();
		titles = hsu.createCriteria(ReportTitle.class).eq(ReportTitle.REPORT_ID, reportId).list();
		graphics = hsu.createCriteria(ReportGraphic.class).eq(ReportGraphic.REPORT_ID, reportId).list();
		selectedCols = hsu.createCriteria(ReportColumn.class).eq(ReportColumn.REPORT_ID, reportId).list();

		try {
			if (titles == null || titles.isEmpty()) {
				BReportTitle generic1 = new BReportTitle();
				generic1.create();
				generic1.setPageLocation('A');
				generic1.setSequenceNumber(1);
				generic1.setFieldType('R');
				generic1.setFontSize(20);
				generic1.setFontBold('Y');
				generic1.setEndLine('Y');
				generic1.setJustification('C');

				BReportTitle generic2 = new BReportTitle();
				generic2.create();
				generic2.setPageLocation('A');
				generic2.setSequenceNumber(2);
				generic2.setFieldType('L');
				generic2.setFontSize(14);
				generic2.setFontBold('N');
				generic2.setEndLine('Y');
				generic2.setJustification('C');

				BReportTitle generic3 = new BReportTitle();
				generic3.create();
				generic3.setPageLocation('A');
				generic3.setSequenceNumber(3);
				generic3.setFieldType('D');
				generic3.setFontSize(14);
				generic3.setFontBold('N');
				generic3.setEndLine('Y');
				generic3.setJustification('C');

				BReportTitle footer = new BReportTitle();
				footer.create();
				footer.setPageLocation('B');
				footer.setSequenceNumber(4);
				footer.setFieldType('P');
				footer.setFontSize(8);
				footer.setFontBold('N');
				footer.setEndLine('N');
				footer.setJustification('C');

				titles.add(generic1.getBean());
				titles.add(generic2.getBean());
				titles.add(generic3.getBean());
				titles.add(footer.getBean());
			}

			createHeader(titles);
			createColumns(selectedCols);
//			createFooter(titles);
		} catch (DocumentException ex) {
			System.out.println("Error in ReportGenerator.");
		} finally {
//			getDocument().close();
			tempDocument.close();
			close();
		}
		return getFilename();
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
		report.setReportType(1);
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

		new ReportGenerator(r).build(r);
	}
}
