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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashMap;
import org.hibernate.HibernateException;

/**
 *
 *
 * Created on Aug 12, 2007
 *
 */
public final class EmployeeGroupBenefitStatement extends EmployeeBenefitStatement {

	/**
	 * @param reportFileNameStart
	 * @param title
	 * @throws ArahantException
	 */
	public EmployeeGroupBenefitStatement() throws ArahantException {
		super();

		for (HrBenefit bene : ArahantSession.getHSU().getAll(HrBenefit.class))
			benefitMap.put(bene.getBenefitId(), bene);

		setNoPageNumbers();
	}
	
	final private HashMap<String, HrBenefit> benefitMap = new HashMap<String, HrBenefit>();
	private int date;

	public String build(String[] statusIdArray, boolean includeCredentials, int date) throws HibernateException, DocumentException, ArahantException {
		{
			try {
				this.date = date;
				HibernateScrollUtil<Employee> sr = ArahantSession.getHSU().createCriteria(Employee.class).employeeCurrentStatusIn(statusIdArray, date).orderBy(Employee.LNAME).orderBy(Employee.FNAME).scroll();

				int count = 0;

				while (sr.next()) {
					if (++count % 50 == 0) {
						logger.info(count);
						logger.info((long) (((new java.util.Date()).getTime() - x) / 1000) + " seconds.");
						x = (new java.util.Date()).getTime();
					}

					try {
						super.writeEmployee(new BEmployee(sr.get()), includeCredentials, date);

						newPage();


					} catch (Exception ex) {
						continue;
					}
				}

				sr.close();

			} finally {
				close();
			}
			return getFilename();
		}
	}
	
	private static long x = (new java.util.Date()).getTime();

	public static void main(String args[]) {
		try {

			ArahantSession.getHSU().setCurrentPersonToArahant();
			EmployeeGroupBenefitStatement egbs = new EmployeeGroupBenefitStatement();
			egbs.build(new String[]{"00001-0000000001"}, false, 20080101);
			logger.info((long) (((new java.util.Date()).getTime() - x) / 1000) + " seconds.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void writeHeaderText(PdfPTable table) {
		/*
		 * String headerText="This Benefit Statement reflects the benefits you
		 * are enrolled and the per pay period cost as of January 1, 2008.\n" +
		 * "It is your responsibility to review this statement and notify the
		 * Benefits Department prior to January 11, 2008 " + "if you feel any of
		 * the information is incorrect. If the Benefits Department is not
		 * notified by January 11, 2008," + " no changes, corrections or
		 * reimbursement of payroll deductions can be made. \n" + "Please note:
		 * If a benefit reflects cost not available, you have elected coverage
		 * but the carrier has not notified" + " the Benefits Department of
		 * approval and/or cost of this benefit. Once the Benefits Department
		 * has received proper" + " notification a revised Benefit Statement
		 * will be issued to you.";
		 *
		 * writeLeft(table, headerText, false);
		 */

		Font boldUnderlinedFont = new Font(FontFamily.COURIER, 10F, Font.BOLD | Font.UNDERLINE);
		Paragraph p;
		Chunk c;
		PdfPCell cell;

		this.write(table, "This Benefit Statement reflects the benefits in which you are enrolled and their per pay period costs as of " + DateUtils.getDateFormatted(date) + ".");

		this.write(table, "");

		p = new Paragraph();
		c = new Chunk("It is your responsibility to review this statement and notify the Benefits Department, ", this.baseFont);
		//	p.add(c);			
		//	c = new Chunk("January 11, 2008", boldFont);
		p.add(c);
		c = new Chunk(" if you feel any of the information is incorrect.", this.baseFont);
		p.add(c);
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);

		this.write(table, "");

		p = new Paragraph();
		c = new Chunk("Please note:", boldUnderlinedFont);
		p.add(c);
		c = new Chunk(" If a benefit reflects \"cost not available\", you have elected coverage but the carrier has not notified the Benefits Department of approval and/or cost of this benefit.  Once the Benefits Department has received proper notification a revised Benefit Statement will be issued to you.\n", baseFont);
		p.add(c);
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.disableBorderSide(1 | 2 | 4 | 8);
		table.addCell(cell);
		this.write(table, " ");

	}
}
