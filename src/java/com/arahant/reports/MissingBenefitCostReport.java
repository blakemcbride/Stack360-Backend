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
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class MissingBenefitCostReport extends ReportBase {

	public MissingBenefitCostReport() {
		super("MisBenCost", "Missing Benefit Cost");
	}

	public String build(String benefitCategoryId, String benefitId)  {

        try {

            PdfPTable table;

            addHeaderLine();
			
			

            HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
			
			if (isEmpty(benefitId))
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.joinTo(HrBenefit.BENEFIT_CATEGORY)
						.eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, benefitCategoryId);
			else
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.eq(HrBenefit.BENEFITID,benefitId);
			
			hcu.eq(HrBenefitJoin.AMOUNT_PAID, (double)0.0);
			
			hcu.propEq(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON);
			
			hcu.joinTo(HrBenefitJoin.PAYING_PERSON)
					.orderBy(Person.LNAME).orderBy(Person.FNAME);
			
            HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

            int count = 0;

            table = makeTable(new int[]{33,33,33});

            writeColHeader(table, "Employee", Element.ALIGN_LEFT);
            writeColHeader(table, "SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit", Element.ALIGN_LEFT);
            
            boolean alternateRow = true;

            while (scr.next()) {
                count++;
//logger.info(scr.get().getPayingPerson().getNameLFM());
                // toggle the alternate row
                alternateRow = !alternateRow;
				write(table, scr.get().getPayingPerson().getNameLFM(), alternateRow);
                write(table, scr.get().getPayingPerson().getUnencryptedSsn(), alternateRow);
                write(table, scr.get().getHrBenefitConfig().getName(), alternateRow);

            }
            
            scr.close();

            write(table, "Total: " + count);

            addTable(table);

        }
		catch (Exception e)
		{
			throw new ArahantException(e);
		}
		finally {
            close();

        }

        return getFilename();
    }
	
	public static void main (String args[])
	{
		ArahantSession.getHSU().setCurrentPersonToArahant();
		new MissingBenefitCostReport().build("00001-0000000009", "");
	}
	
}
