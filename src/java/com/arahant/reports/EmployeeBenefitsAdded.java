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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.reports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 * 
 * Due to the down-turn of the economy, we are adding several people per week to our insurance.  
 * We need a way to track who we have added within a date range, along with the reason.  
 * So, it could be called a "Benefit Reason Report" or  ?????.  Anyway, we would need to be able to enter a date range & 
 * the report would list the employee, benefits added & reason.  I think it would be good to be able to run reports for 
 * each reason, even though right now we are interested in the "Loss of Coverage" reason most.  
 * I figured while you were programming, might as well include all of them.  I thought about adding the reason
 * to the "Employee Enrollment" report, but we are not able to enter a date range.  Only an "as of" date.
 */
public class EmployeeBenefitsAdded extends ReportBase {
   public EmployeeBenefitsAdded() throws ArahantException {
        super("report", "Benefit Reason Report", true);
    }

    public String build(int startDate, int finalDate, String []reasonIds, String []benefitIds) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			hsu.dontAIIntegrate();

            HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU()
					.createCriteria(HrBenefitJoin.class);
			
			hcu.orderBy(HrBenefitJoin.COVERAGE_START_DATE);
			hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);
			
			hcu.dateBetween(HrBenefitJoin.COVERAGE_START_DATE, startDate, finalDate);
	
			
			if (benefitIds.length>0)
			{
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.joinTo(HrBenefitConfig.HR_BENEFIT)
					.in(HrBenefit.BENEFITID, benefitIds);
			}
			
			if (reasonIds.length>0)
			{
				//search by reason ids
				List <String> reasons=(List)hsu.createCriteria(HrBenefitChangeReason.class)
					.selectFields(HrBenefitChangeReason.DESCRIPTION)
					.in(HrBenefitChangeReason.ID, reasonIds)
					.list();
				
				if (reasons.contains("Internal Staff Edit"))
					reasons.add("Internal Edit");
				
				hcu.in(HrBenefitJoin.CHANGE_REASON_STRING, reasons);
			}

			
            int count = 0;

            table = makeTable(new int[]{13, 18, 18, 15, 13, 25, 20});

            writeColHeader(table, "Paying SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Paying Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Covered Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Covered Start", Element.ALIGN_LEFT);
            writeColHeader(table, "Covered End", Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit Name", Element.ALIGN_LEFT);
			writeColHeader(table, "Reason", Element.ALIGN_LEFT);
            boolean alternateRow = true;
			
			HibernateScrollUtil<HrBenefitJoin> scr=hcu.scroll();

            while (scr.next()) {
                count++;

				if (count%50==0)
					System.out.println(count);
				
				HrBenefitJoin bj=scr.get();
				
				if (bj.getHrBenefitConfig()==null)
					continue;
				
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, bj.getPayingPerson().getUnencryptedSsn(), alternateRow);
                write(table, bj.getPayingPerson().getNameLFM(), alternateRow);
                write(table, bj.getCoveredPerson().getNameLFM(), alternateRow);
                write(table, DateUtils.getDateFormatted(bj.getCoverageStartDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(bj.getCoverageEndDate()), alternateRow);
                write(table, bj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);
				write(table, bj.getChangeDescription(),alternateRow);

            }
            
			scr.close();
			
			hsu.useAIIntegrate();
			
            addTable(table);
			
			table=makeTable(new int[]{100});

            write(table, "Total: " + count);

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new EmployeeBenefitsAdded().build(20090101,20090606,new String[]{"00001-0000000010","00001-0000000001"},new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
