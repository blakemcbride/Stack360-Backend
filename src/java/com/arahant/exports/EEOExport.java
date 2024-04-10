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
 */

package com.arahant.exports;

import com.arahant.beans.ApplicantApplication;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import org.kissweb.StringUtils;
import java.io.File;


public class EEOExport
{
	//private static final ArahantLogger logger=new ArahantLogger(EEOExport.class);

    public String build(int startDate, int endDate) throws Exception
	{
		File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "EEOExport" + DateUtils.now() + ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

        try {

			writer.writeField("Position Applied For");
            writer.writeField("Job Group");
            writer.writeField("Race");
            writer.writeField("Sex");

			writer.endRecord();

			HibernateCriteriaUtil<ApplicantApplication> hcu = ArahantSession.getHSU().createCriteria(ApplicantApplication.class);

			//Get all positions that have been filled
//			hcu.joinTo(ApplicantApplication.POSITION).eq(ApplicantPosition.POSITION_STATUS, ApplicantPosition.STATUS_FILLED).dateBetween(ApplicantPosition.ACCEPT_APPLICANT_DATE, startDate, endDate).orderBy(ApplicantPosition.NAME);

			HibernateScrollUtil<ApplicantApplication> scr = hcu.scroll();

            while (scr.next())
			{
				String race;

				if (scr.get().getApplicant().getHrEeoRace() != null)
				{
					if (!StringUtils.isEmpty(scr.get().getApplicant().getHrEeoRace().getName()))
						race = scr.get().getApplicant().getHrEeoRace().getName();
					else
						race = "Not Specified";
				}
				else
						race = "Not Specified";

  //              writer.writeField(scr.get().getPosition().getPosition());
  //              writer.writeField(scr.get().getPosition().getJobType().getDescription());
                writer.writeField(race);
                writer.writeField(scr.get().getApplicant().getPerson().getSex() == 'M'? "Male" : "Female");
				writer.endRecord();
            }

            scr.close();
        } finally {
            writer.close();
        }

		System.out.println(csvFile.getName());
        return csvFile.getName();
    }

    public static void main(String[] args) {
        try {
            new EEOExport().build(20100101, 20101201);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
