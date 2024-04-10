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

import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.IHrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class NewEmployeeSignUp extends ReportBase {


    public NewEmployeeSignUp() throws ArahantException {
        super("Report", "New Employee Sign Up", true);
    }

	/*
	 * A report screen should be created that allow a date range to be specified.
	 The report includes people that are newly signed up during that date range,
	 and prints out the employee's name, last four digits of their SSN,
	 the benefit, benefit coverage level, benefit cost, and whether the
	 benefit is pre-tax or post-tax.  The report should be able to filter
	 to include all or only specific benefits.  The report will be in PDF format.
	 */

    public String build(int startDate, int endDate, String []benefitConfigIds) throws DocumentException {

        try {

            PdfPTable table;

			if (startDate>0)
				writeHeaderLine("From", DateUtils.getDateFormatted(startDate));
			if (endDate>0)
				writeHeaderLine("To", DateUtils.getDateFormatted(endDate));

			if (benefitConfigIds.length==0)
				writeHeaderLine("Benefits", "All");
			else
			{
				String benefits="";
				for (String id : benefitConfigIds)
				{
					BHRBenefitConfig config=new BHRBenefitConfig(id);
					benefits+=config.getName()+", ";
				}
				benefits=benefits.substring(0,benefits.length()-2);
				writeHeaderLine("Benefits", benefits);
			}

            addHeaderLine();


            HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(IHrBenefitJoin.class)
					.distinct()
					.selectFields(IHrBenefitJoin.BENEFIT_JOIN_ID)
					.eqJoinedField(IHrBenefitJoin.PAYING_PERSON, IHrBenefitJoin.COVERED_PERSON)
					.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'N')
					.dateBetween(IHrBenefitJoin.POLICY_START_DATE, startDate, endDate);
			if (benefitConfigIds.length>0)
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.in(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigIds);
		//	hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);


			HibernateCriteriaUtil <HrBenefitJoin> bjHcu=hsu.createCriteria(HrBenefitJoin.class)
				.in(HrBenefitJoin.BENEFIT_JOIN_ID, hcu.list())
				.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);


			HibernateScrollUtil<HrBenefitJoin> scr=bjHcu.scroll();

            table = makeTable(new int[]{22, 7, 28, 35, 8, 10});

            
            writeColHeader(table, "Name", Element.ALIGN_LEFT);
            writeColHeader(table, "SSN", Element.ALIGN_LEFT);
			writeColHeader(table, "Benefit", Element.ALIGN_LEFT);
            writeColHeader(table, "Coverage", Element.ALIGN_LEFT);
            writeColHeader(table, "Cost", Element.ALIGN_RIGHT);
            writeColHeader(table, "Tax", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            while (scr.next()) {

                // toggle the alternate row
                alternateRow = !alternateRow;


				HrBenefitJoin bj=scr.get();

				write(table, bj.getPayingPerson().getNameLFM(), alternateRow);
                write(table, bj.getPayingPerson().getUnencryptedSsn().substring(7), alternateRow);
                
                write(table, bj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);
                write(table, bj.getHrBenefitConfig().getName(), alternateRow);
                write(table, bj.getCalculatedCost(), alternateRow);
                write(table, bj.getHrBenefitConfig().getHrBenefit().getPreTax()=='Y'?"Pre Tax":"Post Tax", alternateRow);

				ArahantSession.resetAI();
            }

            scr.close();
            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
			ArahantSession.getHSU().setCurrentPersonToArahant();
            new NewEmployeeSignUp().build(20090501,0,new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
