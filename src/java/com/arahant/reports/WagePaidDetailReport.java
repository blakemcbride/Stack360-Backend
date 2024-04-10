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

import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BWagePaid;
import com.arahant.business.BWagePaidDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class WagePaidDetailReport extends ReportBase{

	 public WagePaidDetailReport() throws ArahantException {
        super("Report", "Wages Paid Detail", true);
    }

    public String build(String wagePaidId) throws DocumentException {

        try {

            PdfPTable table;

			BWagePaid bwp=new BWagePaid(wagePaidId);

			writeHeaderLine("Employee", bwp.getEmployeeNameLFM());
			writeHeaderLine("Pay Date", DateUtils.getDateFormatted(bwp.getPayDate()));
			writeHeaderLine("Check Number", bwp.getCheckNumber()+"");
			writeHeaderLine("Payment Method", bwp.getPaymentMethodString());
            addHeaderLine();


            HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).distinct().eq(HrBenefitJoin.USING_COBRA, "Y").dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);

            HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

            table = makeTable(new int[]{34, 33, 33});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Wage Base", Element.ALIGN_RIGHT);
            writeColHeader(table, "Amount", Element.ALIGN_RIGHT);
            boolean alternateRow = true;

			BWagePaidDetail[] detarr=new BWagePaid(wagePaidId).listDetail();

            for (BWagePaidDetail det : detarr) {
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, det.getTypeName(), alternateRow);
                write(table, MoneyUtils.formatMoney(det.getBase()), alternateRow);
                write(table, MoneyUtils.formatMoney(det.getAmount()), alternateRow);
              
            }

            scr.close();
            addTable(table);

			table=makeTable(new int[]{100});

			write(table,"Total: "+MoneyUtils.formatMoney(bwp.getTotalAmount()));

			addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }


	public static void main(String args[])
	{
		try
		{
			ArahantSession.getHSU().setCurrentPersonToArahant();
			new WagePaidDetailReport().build("00001-0000000001");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
