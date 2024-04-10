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

import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EmployeeTimesheetReport extends ReportBase {

	public EmployeeTimesheetReport() {
		super("emptime", "Employee Time");
	}

	public String build(int fromDate, int toDate, int type, int sort, String orgGroupId) throws DocumentException {
		try {
			BTimesheet[] times = BTimesheet.search(fromDate, toDate, type, orgGroupId, 0);

			Map<String, ReportDataItem> retData = new HashMap<String, ReportDataItem>();


			for (BTimesheet t : times) {
				ReportDataItem ri = retData.get(t.getPersonId());
				if (ri == null) {
					ri = new ReportDataItem();
					retData.put(t.getPersonId(), ri);
					BPerson bp = new BPerson(t.getPersonId());
					ri.setFirstName(bp.getFirstName());
					ri.setLastName(bp.getLastName());
					ri.sortType = sort;
				}

				ri.setTotalHours(ri.getTotalHours() + t.getTotalHours());

				if (t.getBillable() == 'Y') {
					ri.setBillableHours(ri.getBillableHours() + t.getTotalHours());
				} else {
					ri.setNonBillableHours(ri.getNonBillableHours() + t.getTotalHours());
				}
			}

			ArrayList<ReportDataItem> rl = new ArrayList<ReportDataItem>(retData.values().size());
			rl.addAll(retData.values());
			Collections.sort(rl);

			if (fromDate > 0) {
				writeHeaderLine("From", DateUtils.getDateFormatted(fromDate));
			}
			if (toDate > 0) {
				writeHeaderLine("To", DateUtils.getDateFormatted(toDate));
			}
			if (!isEmpty(orgGroupId)) {
				writeHeaderLine("Org Group", new BOrgGroup(orgGroupId).getName());
			}
			switch (type) {
				case 1:
					writeHeaderLine("Approval", "Not Approved");
					break;
				case 2:
					writeHeaderLine("Approval", "Approved or Invoiced");
					break;
			}
			addHeaderLine();
			PdfPTable table = makeTable(new int[]{40, 20, 20, 20});

			writeColHeader(table, "Name");
			writeColHeader(table, "Billable");
			writeColHeader(table, "Non Billable");
			writeColHeader(table, "Total");

			double billTotal = 0;
			double nonBillTotal = 0;
			for (ReportDataItem i : rl) {
				write(table, i.lastName + ", " + i.firstName);
				write(table, roundToTenths(i.billableHours));
				write(table, roundToTenths(i.nonBillableHours));
				write(table, roundToTenths(i.totalHours));
				billTotal += i.billableHours;
				nonBillTotal += i.nonBillableHours;
			}

			for (int loop = 0; loop < 4; loop++) {
				writeColHeader(table, "");
			}
			write(table, "TOTAL");
			write(table, roundToTenths(billTotal));
			write(table, roundToTenths(nonBillTotal));
			write(table, roundToTenths(billTotal + nonBillTotal));

			addTable(table);


		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}

		return getFilename();
	}

	public class ReportDataItem implements Comparable<ReportDataItem> {

		public ReportDataItem() {
		
		}
		int sortType;
		private String lastName;
		private String firstName;
		private double billableHours;
		private double nonBillableHours;
		private double totalHours;

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public double getBillableHours() {
			return billableHours;
		}

		public void setBillableHours(double billableHours) {
			this.billableHours = billableHours;
		}

		public double getNonBillableHours() {
			return nonBillableHours;
		}

		public void setNonBillableHours(double nonBillableHours) {
			this.nonBillableHours = nonBillableHours;
		}

		public double getTotalHours() {
			return totalHours;
		}

		public void setTotalHours(double totalHours) {
			this.totalHours = totalHours;
		}

		public int compareTo(ReportDataItem o) {
			//sort: 1=lastName, firstName (asc), 2=billable hours (desc), 3=non-billable hours (desc), 4=total hours (desc)
			switch (sortType) {
				case 2:
					return (int) (o.billableHours - billableHours);
				case 3:
					return (int) (o.getNonBillableHours() - nonBillableHours);
				case 4:
					return (int) (o.getTotalHours() - getTotalHours());

			}

			if (lastName.compareTo(o.lastName) == 0) {
				return firstName.compareTo(o.firstName);
			}
			return lastName.compareTo(o.lastName);
		}
	}
}
