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


package com.arahant.reports;

import com.arahant.beans.*;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.standard.hr.salaryReport.GetReportInput;
import com.arahant.utils.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.ScrollMode;

public class SalaryReport extends PdfPageEventHelper {

	private Font baseFont;
	private Document document;
	protected BaseFont watermarkFont;
	protected PdfGState gstate;

	public SalaryReport() {
		this.baseFont = new Font(FontFamily.COURIER, 10F, Font.NORMAL);
	}
	/*
	 * public static void main (String args[]) { SalaryReport rep=new
	 * SalaryReport(); GetReportInput in=new GetReportInput();
	 * in.setCurrentSalary(true); in.setEmployeeName(true);
	 * in.setLastEvaluationDate(true); in.setLastRaiseAmount(true);
	 * in.setLastRaiseDate(true); in.setSortAsc(true); in.setSortType(3);
	 * in.setSsn(true); in.setStartDate(true); in.setTimeSinceLastRaise(true);
	 * in.setTimeSinceLastReview(true); in.setTimeWithCompany(true); try {
	 * rep.build(new File("c:/sal.pdf"), in); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

	public void build(final File fileName, final GetReportInput in) throws FileNotFoundException, DocumentException, ArahantException {

		try {
			in.setLastRaiseDate(false);  //TODO: turned these off for now because
			in.setLastRaiseAmount(false); //these no longer work because we added other wages and types and so report wasn't coming up at all

			PdfWriter pdfWriter;
			String sortBy;
			final String sortDirection = in.isSortAsc() ? "Ascending" : "Descending";

			switch (in.getSortType()) {
				case 0:
					sortBy = "Last Name";
					break;
				case 1:
					sortBy = "Current Salary";
					break;
				case 2:
					sortBy = "Last Raise Date";
					break;
				case 3:
					sortBy = "Start Date";
					break;
				case 4:
					sortBy = "Last Evaluation Date";
					break;
				default:
					sortBy = "Unknown";
					break;

			}

			this.document = new Document(PageSize.LETTER.rotate(), 50F, 50F, 50F, 50F);

			pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
			pdfWriter.setPageEvent(this);

			this.document.open();

			// write out the parts of our report
			this.writeHeader(sortBy, sortDirection);
			this.writeEmployees(in);
		} finally {
			try {
				if (this.document != null) {
					this.document.close();
					this.document = null;
				}
			} catch (final Exception ignored) {
			}

		}
	}

	protected void writeHeader(final String sortBy, final String sortDirection) throws DocumentException {
		PdfPTable table;
		PdfPCell cell;

		// report header
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setTotalWidth(225F);
		table.setLockedWidth(true);

		cell = new PdfPCell(new Paragraph("Employee Salary List", new Font(FontFamily.COURIER, 12F, Font.NORMAL)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8F);
		cell.setPaddingTop(5F);
		table.addCell(cell);

		this.document.add(table);


		// run date
		table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(5F);

		cell = new PdfPCell(new Paragraph("Run Date: " + DateUtils.getDateTimeFormatted(new Date()), this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Sort By: " + sortBy, this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Sort Direction: " + sortDirection, this.baseFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);


		// header line
		table = new PdfPTable(1);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.setBackgroundColor(new BaseColor(192, 192, 192));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.document.add(table);
	}

	/*
	 * protected HibernateScrollUtil<Employee> getEmployees(GetReportInput in) {
	 * HibernateCriteriaUtil<Employee>
	 * hcu=ArahantSession.getHSU().createCriteria(Employee.class);
	 * hcu.activeEmployee(); final int sortType = in.getSortType(); final
	 * boolean asc = in.isSortAsc();
	 *
	 * if (asc) { switch (sortType) { case 0:
	 * hcu.orderBy(Employee.LNAME).orderBy(Employee.FNAME); break; case 1:
	 * query+=" order by wage."+HrWage.WAGEAMOUNT+", emp.lname, emp.fname ";
	 * break; case 2:	query+=" order by wage."+HrWage.EFFECTIVEDATE+",
	 * emp.lname, emp.fname "; break; case 3:	query+=" order by
	 * stat."+HrEmplStatusHistory.EFFECTIVEDATE+", emp.lname, emp.fname ";
	 * break; case 4: query+=" order by eval."+HrEmployeeEval.EVALDATE+",
	 * emp.lname, emp.fname "; break; }
	 *
	 * }
	 * else { switch (sortType) { case 0: query+=" order by emp.lname, emp.fname
	 * desc "; break; case 1: query+=" order by wage."+HrWage.WAGEAMOUNT+",
	 * emp.lname, emp.fname desc "; break; case 2:	query+=" order by
	 * wage."+HrWage.EFFECTIVEDATE+", emp.lname, emp.fname desc "; break; case
	 * 3:	query+=" order by stat."+HrEmplStatusHistory.EFFECTIVEDATE+",
	 * emp.lname, emp.fname desc "; break; case 4: query+=" order by
	 * eval."+HrEmployeeEval.EVALDATE+", emp.lname, emp.fname desc "; break; } }
	 *
	 *
	 * }
	 *
	 */
	protected Query getEmployees(final GetReportInput in) {
		// last name = 0, current salary=1, last raise date=2, start date=3, last evaluation date=4
		final int sortType = in.getSortType();
		final boolean asc = in.isSortAsc();
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
		Set<OrgGroup> ogs = new HashSet<OrgGroup>();
		for (OrgGroupAssociation o : oga)
			ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

		//get all the employees

		String query = "select distinct emp." + Employee.LNAME + ", emp." + Employee.FNAME + ", emp." + Employee.SSN + ", wage." + HrWage.EFFECTIVEDATE + ", "
				+ "wage." + HrWage.WAGEAMOUNT + ", stat." + HrEmplStatusHistory.EFFECTIVEDATE + ", eval." + HrEmployeeEval.EVALDATE +/*
				 * ", pwage."+HrWage.WAGEAMOUNT +
				 */ " "
				+ "from Employee emp left join emp." + Employee.HREMPLSTATUSHISTORIES + " stat "
				+ "left join emp." + Employee.HRWAGES + " wage ";
		if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
			query += "join emp." + Employee.ORGGROUPASSOCIATIONS + " oga ";
		else
			query += "join emp." + Employee.COMPANYBASE + " oga ";
		query += "left join emp." + Employee.HREMPLOYEEEVALSFOREMPLOYEEID + " eval " // +
				//"left join emp."+Employee.HRWAGES+" pwage "
				;

		query += " where ";

		query += "stat." + HrEmplStatusHistory.EFFECTIVEDATE + " = (select max(stat2." + HrEmplStatusHistory.EFFECTIVEDATE
				+ ") from HrEmplStatusHistory stat2 where stat2." + HrEmplStatusHistory.EMPLOYEE + " = emp)"
				+ "and stat." + HrEmplStatusHistory.HREMPLOYEESTATUS + "." + HrEmployeeStatus.ACTIVE + "='Y' ";

		if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
			int i = 0;
			query += " and (";
			for (OrgGroup og : ogs) {
				if (i != 0)
					query += " or ";
				query += "oga." + OrgGroup.ORGGROUPID + " = '" + og.getOrgGroupId() + "'";
				i++;
			}
			query += ")";
		} else
			query += " and oga." + OrgGroup.ORGGROUPID + "='" + hsu.getCurrentCompany().getOrgGroupId() + "' ";

		query += " and ";

		query += "(wage." + HrWage.EFFECTIVEDATE + " is null or wage." + HrWage.EFFECTIVEDATE + " = (select max(wage2." + HrWage.EFFECTIVEDATE + ") from HrWage wage2 where wage2." + HrWage.EMPLOYEE + " = emp))";

		//	query+=" and ";

		//	query+="(pwage."+HrWage.EFFECTIVEDATE+" is null or pwage."+HrWage.EFFECTIVEDATE+" = " +
		//			"(select max(wage3."+HrWage.EFFECTIVEDATE+") from HrWage wage3 where wage3."+HrWage.EMPLOYEE+" = emp " +
		//					"and wage3."+HrWage.EFFECTIVEDATE+"!=wage."+HrWage.EFFECTIVEDATE+"))";


		query += " and ";

		query += "(eval." + HrEmployeeEval.EVALDATE + " is null or eval." + HrEmployeeEval.EVALDATE + " = (select max(eval2." + HrEmployeeEval.EVALDATE + ") from "
				+ "HrEmployeeEval eval2 where eval2." + HrEmployeeEval.EMPLOYEEBYEMPLOYEEID + " = emp))";


		if (asc)
			switch (sortType) {
				case 0:
					query += " order by emp.lname, emp.fname desc ";
					break;
				case 1:
					query += " order by wage." + HrWage.WAGEAMOUNT + ", emp.lname, emp.fname ";
					break;
				case 2:
					query += " order by wage." + HrWage.EFFECTIVEDATE + ", emp.lname, emp.fname ";
					break;
				case 3:
					query += " order by stat." + HrEmplStatusHistory.EFFECTIVEDATE + ", emp.lname, emp.fname ";
					break;
				case 4:
					query += " order by eval." + HrEmployeeEval.EVALDATE + ", emp.lname, emp.fname ";
					break;
			}
		else
			switch (sortType) {
				case 0:
					query += " order by emp.lname, emp.fname desc ";
					break;
				case 1:
					query += " order by wage." + HrWage.WAGEAMOUNT + ", emp.lname, emp.fname desc ";
					break;
				case 2:
					query += " order by wage." + HrWage.EFFECTIVEDATE + ", emp.lname, emp.fname desc ";
					break;
				case 3:
					query += " order by stat." + HrEmplStatusHistory.EFFECTIVEDATE + ", emp.lname, emp.fname desc ";
					break;
				case 4:
					query += " order by eval." + HrEmployeeEval.EVALDATE + ", emp.lname, emp.fname desc ";
					break;
			}


//		System.out.println(query);

		return hsu.createQuery(query);
	}

	protected void writeEmployees(final GetReportInput in) throws DocumentException, ArahantException {
		PdfPTable table;
		PdfPCell cell;
		boolean alternateRow = true;
		final BaseColor alternateRowColor = new BaseColor(192, 192, 192);

		table = new PdfPTable(this.getColumnCount(in));
		table.setWidthPercentage(100F);
		table.setSpacingBefore(15F);
		table.setWidths(this.getColumnWidths(in));
		table.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);

		if (in.isEmployeeName()) {
			cell = new PdfPCell(new Paragraph("Employee Name", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isSsn()) {
			cell = new PdfPCell(new Paragraph("SNN", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isStartDate()) {
			cell = new PdfPCell(new Paragraph("Start Date", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isLastRaiseDate()) {
			cell = new PdfPCell(new Paragraph("Last Raise Date", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isCurrentSalary()) {
			cell = new PdfPCell(new Paragraph("Current Salary", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isLastRaiseAmount()) {
			cell = new PdfPCell(new Paragraph("Last Raise Amount", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isLastEvaluationDate()) {
			cell = new PdfPCell(new Paragraph("Last Evaluation Date", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isTimeWithCompany()) {
			cell = new PdfPCell(new Paragraph("Time With Company", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isTimeSinceLastRaise()) {
			cell = new PdfPCell(new Paragraph("Time Since Last Raise", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		if (in.isTimeSinceLastReview()) {
			cell = new PdfPCell(new Paragraph("Time Since Last Review", this.baseFont));
			cell.disableBorderSide(1 | 4 | 8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell(cell);
		}
		table.setHeaderRows(1);

		HibernateScrollUtil hsu = new HibernateScrollUtil(this.getEmployees(in).scroll(ScrollMode.FORWARD_ONLY));
		while (hsu.next()) {

			// what was this for?    	if (BRight.checkRight(BEmployee.IS_OWNER)== BEmployee.ACCESS_LEVEL_WRITE)
			//			continue;

			// toggle the alternate row
			alternateRow = !alternateRow;

			if (in.isEmployeeName()) {
				cell = new PdfPCell(new Paragraph(hsu.getString(0) + ", " + hsu.getString(1), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isSsn()) {
				cell = new PdfPCell(new Paragraph(hsu.getString(2), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isStartDate()) {
				cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(hsu.getInt(5)), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isLastRaiseDate()) {
				cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(hsu.getInt(3)), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isCurrentSalary()) {
				cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(hsu.getDouble(4)), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isLastRaiseAmount()) {
				cell = new PdfPCell(new Paragraph(MoneyUtils.formatMoney(hsu.getDouble(4) - hsu.getDouble(7)) + "", this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isLastEvaluationDate()) {
				cell = new PdfPCell(new Paragraph(DateUtils.getDateFormatted(hsu.getInt(6)), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isTimeWithCompany()) {
				cell = new PdfPCell(new Paragraph(DateUtils.daysToYearMonth(1+DateUtils.getDaysSince(hsu.getInt(5))), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isTimeSinceLastRaise()) {
				cell = new PdfPCell(new Paragraph(DateUtils.daysToYearMonth(1+DateUtils.getDaysSince(hsu.getInt(3))), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
			if (in.isTimeSinceLastReview()) {
				cell = new PdfPCell(new Paragraph(DateUtils.daysToYearMonth(1+DateUtils.getDaysSince(hsu.getInt(6))), this.baseFont));
				if (alternateRow)
					cell.setBackgroundColor(alternateRowColor);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.disableBorderSide(1 | 2 | 4 | 8);
				table.addCell(cell);
			}
		}
		this.document.add(table);
	}

	private int getColumnCount(final GetReportInput in) {
		int columnCount = 0;

		if (in.isEmployeeName())
			columnCount++;
		if (in.isStartDate())
			columnCount++;
		if (in.isLastRaiseDate())
			columnCount++;
		if (in.isCurrentSalary())
			columnCount++;
		if (in.isLastRaiseAmount())
			columnCount++;
		if (in.isLastEvaluationDate())
			columnCount++;
		if (in.isTimeWithCompany())
			columnCount++;
		if (in.isTimeSinceLastRaise())
			columnCount++;
		if (in.isTimeSinceLastReview())
			columnCount++;
		if (in.isSsn())
			columnCount++;

		return columnCount;

	}

	private int[] getColumnWidths(final GetReportInput in) {
		final int columnCount = this.getColumnCount(in);
		final boolean isNamePresent = in.isEmployeeName();
		final int[] columnWidths = new int[columnCount];
		int percentage = (int) Math.floor(100 / columnCount);
		int total = 0;
		final int nameGetsAtLeastPercent = 15;
		int startFromIdx = 0;

		// name must get "nameGetsAtLeastPercent" percent of the table at least
		if (isNamePresent && percentage < nameGetsAtLeastPercent) {
			// recalculate the remaining column widths with 
			// name getting "nameGetsAtLeastPercent" percent
			percentage = (int) Math.floor((100 - nameGetsAtLeastPercent) / (columnCount - 1));

			// fill out name's percent and adjust the rest of the calculation starting index
			startFromIdx = 1;
			total += columnWidths[0] = nameGetsAtLeastPercent;
		}

		// spin through the columns and assign the widths
		for (int idx = startFromIdx; idx < columnCount; idx++) {
			total += columnWidths[idx] = percentage;

			// if this is the last column, adjust to total 100%
			if (idx == columnCount - 1)
				columnWidths[idx] += 100 - total;
		}

		return columnWidths;

	}

	@Override
	public void onOpenDocument(final PdfWriter writer, final Document document) {
		try {
			this.watermarkFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new ExceptionConverter(e);
		}

		this.gstate = new PdfGState();
		this.gstate.setFillOpacity(0.2f);
		this.gstate.setStrokeOpacity(0.2f);
	}

	@Override
	public void onEndPage(final PdfWriter writer, final Document document) {
		final int pageNum = writer.getPageNumber();

		if (pageNum > 1)
			try {
				final Rectangle page = document.getPageSize();
				final Font defaultFont = new Font(FontFamily.COURIER, 10F, Font.ITALIC);
				final PdfPTable footer = new PdfPTable(1);
				footer.getDefaultCell().disableBorderSide(1 | 2 | 4 | 8);
				footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				footer.addCell(new Paragraph("Page " + pageNum, defaultFont));
				footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
			} catch (final Exception e) {
				throw new ExceptionConverter(e);
			}

		try {
			final PdfContentByte contentUnder = writer.getDirectContentUnder();

			contentUnder.saveState();
			contentUnder.setGState(gstate);
			contentUnder.beginText();
			contentUnder.setFontAndSize(this.watermarkFont, 68);
			contentUnder.showTextAligned(Element.ALIGN_CENTER, "CONFIDENTIAL", document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
			contentUnder.endText();
			contentUnder.restoreState();
		} catch (final Exception e) {
			throw new ExceptionConverter(e);
		}
	}
}
