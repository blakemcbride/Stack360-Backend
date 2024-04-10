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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.PersonCR;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;


/**
 *
 */
public class BenefitWizardStatusReport extends ReportBase {

	public BenefitWizardStatusReport() {
		super("BenefitWizardStatus", "Benefit Wizard Status");
	}

	public String build(String fname, String lname, int startDate, int endDate, String wizardStatus) {
		try {
			PdfPTable table = new PdfPTable(1);
			List<Employee> empList = BEmployee.searchEmployeesByWizardStatus(fname, lname, startDate, endDate, wizardStatus, 10000);

			if(wizardStatus.equals("N"))
				wizardStatus = "No Change";
			else if(wizardStatus.equals("U"))
				wizardStatus = "Unfinalized ";
			else if(wizardStatus.equals("F"))
				wizardStatus = "Finalized ";
			else if(wizardStatus.equals("P"))
				wizardStatus = "Processed ";

			if(startDate > 0)
				writeHeader(table, "   Start Date: " + DateUtils.getDateFormatted(startDate));
			if(endDate > 0)
				writeHeader(table, "     End Date: " + DateUtils.getDateFormatted(endDate));
			if(!isEmpty(wizardStatus))
				writeHeader(table, "Wizard Status: " + wizardStatus);
			
			addTable(table);
			addHeaderLine();

			

			table = makeTable(new int[]{24, 14, 14, 14, 14, 20});


			writeColHeader(table, "Employee");
			writeColHeader(table, "Demographic Change");
			writeColHeader(table, "Dependent Change");
			writeColHeader(table, "Benefit Change");
			writeColHeader(table, "Wizard Status");
			writeColHeader(table, "Status Date");


			boolean alternateRow = true;

			for (Employee e : empList) {
				BEmployee be = new BEmployee(e);

				// toggle the alternate row
				alternateRow = !alternateRow;

				boolean hasBenefitChange = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
											.eq(HrBenefitJoin.APPROVED, 'N')
											.eq(HrBenefitJoin.PAYING_PERSON, be.getPerson())
											.exists();
				boolean hasDependentChange = ArahantSession.getHSU().createCriteria(HrEmplDependent.class)
										.eq(HrEmplDependent.EMPLOYEE, be.getEmployee())
										.eq(HrEmplDependent.RECORD_TYPE, 'C')
										.exists();
				boolean hasDemographicChange = ArahantSession.getHSU().createCriteria(PersonCR.class)
													.eq(PersonCR.PERSON, be.getPerson())
													.eq(PersonCR.CHANGE_STATUS, PersonCR.STATUS_PENDING)
													.exists();

				String benefitWizardStatus = be.getBenefitWizardStatus();
				if(benefitWizardStatus.equals("N"))
					benefitWizardStatus = "No Change";
				else if(benefitWizardStatus.equals("U"))
					benefitWizardStatus = "Unfinalized ";
				else if(benefitWizardStatus.equals("F"))
					benefitWizardStatus = "Finalized ";
				else if(benefitWizardStatus.equals("P"))
					benefitWizardStatus = "Processed ";
				write(table, be.getNameLFM(), alternateRow);
				writeAlign(table, hasDemographicChange ? "Yes" : "--", Element.ALIGN_CENTER, alternateRow);
				writeAlign(table, hasDependentChange ? "Yes" : "--", Element.ALIGN_CENTER, alternateRow);
				writeAlign(table, hasBenefitChange ? "Yes" : "--", Element.ALIGN_CENTER, alternateRow);
				write(table, benefitWizardStatus, alternateRow);
				writeRight(table, DateUtils.getDateFormatted(be.getBenefitWizardDate()), alternateRow);

			}


			addTable(table);

		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}

		return getFilename();
	}

	public static void main(String[] args) {

		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().createCriteria(CompanyDetail.class).like(CompanyDetail.NAME, "%Mason%").first());
		BenefitWizardStatusReport rep = new BenefitWizardStatusReport();
		rep.build("", "%A", 0, 0, "N");
	}
}
	
