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


/**
 *
 *
 */

package com.arahant.reports;

import com.arahant.beans.ApplicantApplication;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class EEOReport extends ReportBase {

	public EEOReport() throws ArahantException {
        super("EEORpt", "EEO Report", false);
    }

    public String build(int startDate, int endDate) throws DocumentException {

        try {

            PdfPTable table = null;

			writeHeaderLine("Accepting Applications", DateUtils.getDateFormatted(startDate) + " - " + DateUtils.getDateFormatted(endDate));

            addHeaderLine();

			HibernateCriteriaUtil<ApplicantApplication> hcu = ArahantSession.getHSU().createCriteria(ApplicantApplication.class);

			//Get all positions that have been filled
//			hcu.joinTo(ApplicantApplication.POSITION).eq(ApplicantPosition.POSITION_STATUS, ApplicantPosition.STATUS_FILLED).dateBetween(ApplicantPosition.ACCEPT_APPLICANT_DATE, startDate, endDate).orderBy(ApplicantPosition.NAME);

			HibernateScrollUtil<ApplicantApplication> scr = hcu.scroll();

            table = makeTable(30, 30, 30, 10);

            writeColHeader(table, "Position Applied For", Element.ALIGN_LEFT);
            writeColHeader(table, "Job Group", Element.ALIGN_LEFT);
            writeColHeader(table, "Race", Element.ALIGN_LEFT);
            writeColHeader(table, "Sex", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            while (scr.next()) {

                // toggle the alternate row
                alternateRow = !alternateRow;
				
				String race;

				if (scr.get().getApplicant().getHrEeoRace() != null)
				{
					if (!isEmpty(scr.get().getApplicant().getHrEeoRace().getName()))
						race = scr.get().getApplicant().getHrEeoRace().getName();
					else
						race = "Not Specified";
				}
				else
						race = "Not Specified";

     //           write(table, scr.get().getPosition().getPosition(), alternateRow);
      //          write(table, scr.get().getPosition().getJobType().getDescription(), alternateRow);
                write(table, race, alternateRow);
                write(table, scr.get().getApplicant().getPerson().getSex() == 'M'? "Male" : "Female", alternateRow);

            }

            scr.close();
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String[] args) {
        try {
            new EEOReport().build(20100101, 20101201);
        } catch (Exception e) {
            e.printStackTrace();
        }

		


    }

}
