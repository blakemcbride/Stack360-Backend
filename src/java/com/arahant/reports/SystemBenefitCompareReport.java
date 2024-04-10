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

import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;

/*
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *  
 */
public class SystemBenefitCompareReport extends ReportBase {


    public SystemBenefitCompareReport() throws ArahantException {
        super("Bencomp", "System Benefit Compare", true);
    }

	/*
    public String build(com.arahant.operations.CompareBenefitsReturn in) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			
			for (com.arahant.operations.CompareBenefitsReturnBenefitConfig configRet : in.getConfigs())
			{

				writeCentered(configRet.getName());

				int count = 0;

				table = makeTable(new int[]{15, 22, 22, 22, 22, 19, 20});

				writeColHeader(table, "SSN", Element.ALIGN_LEFT);
				writeColHeader(table, "Name", Element.ALIGN_LEFT);
				writeColHeader(table, "Discrepancy Type", Element.ALIGN_LEFT);
				writeColHeader(table, "Policy Start Date", Element.ALIGN_LEFT);
				writeColHeader(table, "Policy End Date", Element.ALIGN_LEFT);
				writeColHeader(table, "Amount Paid", Element.ALIGN_LEFT);
				writeColHeader(table, "Amount Covered", Element.ALIGN_LEFT);


				boolean alternateRow = true;


				for (com.arahant.operations.CompareBenefitsReturnPerson p : configRet.getItem()) {
					count++;
					// toggle the alternate row
					alternateRow = !alternateRow;

					write(table, p.getSsn(), alternateRow);
					write(table, p.getLname()+", "+p.getFname(), alternateRow);
					if (p.getDiffType()=='M')
						write(table, "Missing on Client", alternateRow);
					if (p.getDiffType()=='N')
						write(table, "No Benefit on Client", alternateRow);
					if (p.getDiffType()=='F')
						write(table, "Only on Client", alternateRow);
					if (p.getDiffType()=='D')
						write(table, "Values Differ", alternateRow);
					write(table, DateUtils.getDateFormatted(p.getPolicyStart()), alternateRow);
					write(table, DateUtils.getDateFormatted(p.getPolicyEnd()), alternateRow);
					write(table, MoneyUtils.formatMoney(p.getPaid()), alternateRow);
					write(table, MoneyUtils.formatMoney(p.getCovered()), alternateRow);

					if (p.getDiffType()=='D')
					{
						write(table, "****", alternateRow);
						write(table, p.getOlname()+", "+p.getOfname(), alternateRow);
						write(table, "", alternateRow);
						write(table, DateUtils.getDateFormatted(p.getOpolicyStart()), alternateRow);
						write(table, DateUtils.getDateFormatted(p.getOpolicyEnd()), alternateRow);
						write(table, MoneyUtils.formatMoney(p.getPaid()), alternateRow);
						write(table, MoneyUtils.formatMoney(p.getCovered()), alternateRow);
					}

				}

				addTable(table);

				table=makeTable(new int[]{100});

				write(table, "Total: " + count);

				addTable(table);
			}

        } finally {
            close();

        }

        return getFilename();
    }
*/

}
