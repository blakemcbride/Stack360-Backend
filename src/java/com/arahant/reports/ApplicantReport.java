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
import com.arahant.business.BApplicant;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;

public class ApplicantReport extends ReportBase {

	public ApplicantReport() {
		super("AppLoc", "Applicant Report");
	}

	public String build(int searchType, String applicationSource, String applicationStatus, String applicantSource, String applicantStatus, String firstName, String lastName, String jobTypeId, String positionId, boolean includeEmployees) {
		try {
			PdfPTable table = new PdfPTable(1);

			addTable(table);
			addHeaderLine();

			if (!isEmpty(applicantSource))
				writeHeaderLine("Applicant Source", hsu.get(ApplicantSource.class, applicantSource).getDescription());
			if (!isEmpty(applicantStatus))
				writeHeaderLine("Applicant Status", hsu.get(ApplicantStatus.class, applicantStatus).getName());
			if (!isEmpty(applicationSource))
				writeHeaderLine("Applicant Source", hsu.get(ApplicantSource.class, applicationSource).getDescription());
			if (!isEmpty(applicationStatus))
				writeHeaderLine("Application Status", hsu.get(ApplicantAppStatus.class, applicationStatus).getStatusName());
			if (!isEmpty(applicantStatus))
				writeHeaderLine("Applicant Status", hsu.get(ApplicantStatus.class, applicantStatus).getName());
			/*
			if (!isEmpty(jobTypeId))
				writeHeaderLine("Job Type", hsu.get(JobType.class, jobTypeId).getDescription());
			if (!isEmpty(positionId))
				writeHeaderLine("Position", hsu.get(ApplicantPosition.class, positionId).getPosition());
*/
			BApplicant[] apps = BApplicant.search(applicantSource, applicantStatus, applicationSource, applicationStatus, firstName, lastName, jobTypeId, positionId, searchType, 0, includeEmployees);


			table = makeTable(new int[]{40, 20, 20, 20});


			writeColHeader(table, "Name");
			writeColHeader(table, "Status");
			writeColHeader(table, "Source");
			writeColHeader(table, "First Aware");


			boolean alternateRow = true;

			for (BApplicant cc : apps) {


				// toggle the alternate row
				alternateRow = !alternateRow;


				write(table, cc.getNameLFM(), alternateRow);
				write(table, cc.getApplicantStatus(), alternateRow);
				write(table, cc.getApplicantSource(), alternateRow);
				write(table, DateUtils.getDateFormatted(cc.getFirstAwareDate()), alternateRow);

			}


			addTable(table);

		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}

		return getFilename();
	}
}
