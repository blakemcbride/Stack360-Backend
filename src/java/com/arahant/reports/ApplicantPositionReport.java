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

import com.arahant.beans.OrgGroup;
import com.arahant.business.BApplicantPosition;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;

public class ApplicantPositionReport extends ReportBase {

	public ApplicantPositionReport() {
		super("apppos", "Applicant Position Report");
	}

	public String build(int acceptingFrom, int acceptingTo, boolean includeAccepting, boolean includeCancelled, boolean includeFilled, boolean includeNew, boolean includeSuspended, int jobStartFrom, int jobStartTo, String jobTypeId, String orgGroupId) {

		try {
			PdfPTable table;

			if (acceptingFrom != 0)
				writeHeaderLine("Accepting From", DateUtils.getDateFormatted(acceptingFrom));
			if (acceptingTo != 0)
				writeHeaderLine("Accepting To", DateUtils.getDateFormatted(acceptingTo));
			if (jobStartFrom != 0)
				writeHeaderLine("Job Start From", DateUtils.getDateFormatted(jobStartFrom));
			if (jobStartTo != 0)
				writeHeaderLine("Job Start To", DateUtils.getDateFormatted(jobStartTo));
			writeHeaderLine("Include Accepting", includeAccepting);
			writeHeaderLine("Include Cancelled", includeCancelled);
			writeHeaderLine("Include Filled", includeFilled);
			writeHeaderLine("Include New", includeNew);
			writeHeaderLine("Include Suspended", includeSuspended);
			if (!isEmpty(orgGroupId))
				writeHeaderLine("Org Group", hsu.get(OrgGroup.class, orgGroupId).getName());

			addHeaderLine();

			table = makeTable(20, 20, 10, 20, 15, 15);

			writeColHeader(table, "Job Type");
			writeColHeader(table, "Position");
			writeColHeader(table, "Status Type");
			writeColHeader(table, "Org Group");
			writeColHeader(table, "Job Start");
			writeColHeader(table, "Accept Applications");

			BApplicantPosition[] positions = BApplicantPosition.search(acceptingFrom, acceptingTo, jobStartFrom, jobStartTo, includeAccepting, includeCancelled, includeFilled, includeNew, includeSuspended, orgGroupId, jobTypeId, 0);


			boolean alternateRow = true;

			//   while (scr.next()) {
			for (BApplicantPosition pos : positions) {
				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, pos.getJobTitle(), alternateRow);
				write(table, pos.getStatusType(), alternateRow);
				write(table, pos.getOrgGroupName(), alternateRow);
				write(table, DateUtils.getDateFormatted(pos.getJobStartDate()), alternateRow);
				write(table, DateUtils.getDateFormatted(pos.getAcceptApplicationDate()), alternateRow);
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
