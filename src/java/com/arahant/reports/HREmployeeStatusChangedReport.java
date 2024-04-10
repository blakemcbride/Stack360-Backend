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
 * Created on Jul 12, 2007
 *
 */
package com.arahant.reports;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.kissweb.StringUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 *
 */
public class HREmployeeStatusChangedReport extends ReportBase {

	public HREmployeeStatusChangedReport() throws ArahantException {
		super("HREmpStaChRpt", "Employee Status Changed Report", true);
	}
	static List<ReportData> reportDataList = new ArrayList<ReportData>();

	public void setReportData(String orgLabel, Person p, int startDate, int endDate, String status) {
		ReportData rd = new ReportData();
		BEmployee emp = new BEmployee(p.getPersonId());


//        System.out.println("\t" + orgLabel + "-" + emp.getNameFML());
		rd.setOrgLabel(orgLabel);
		rd.setSSN(emp.getSsn());
		rd.setNameLFM(emp.getNameLFM());
		rd.setLastStatusDateFormatted(emp.getLastStatusDateFormatted());
		rd.setPriorStatus(emp.getPriorStatus(startDate, endDate, status));
		rd.setHasDental(emp.getHasDental());
		rd.setHasMedical(emp.getHasMedical());
		rd.setHasVision(emp.getHasVision());
		reportDataList.add(rd);
	}

	public void getRestOfEmployees(BOrgGroup og, String orgLabel, int startDate, int endDate, String status, int depthLevel) {
		Set<String> person = og.getAllPersonIdsForOrgGroupHierarchy(true);

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME);
		hcu.in(Person.PERSONID, person);
		hcu.joinTo(Employee.HREMPLSTATUSHISTORIES).dateBetween(HrEmplStatusHistory.EFFECTIVEDATE, startDate, endDate).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmplStatusHistory.STATUS_ID, status);


		for (Person pp : hcu.list())
			setReportData(orgLabel, pp, startDate, endDate, status); //System.out.println(orgLabel + " " + pp.getNameFL());
	}

	public void displayGroupsByOrgId(String orgId, String orgLabel, int startDate, int endDate, String status, int depthLevel) {
		displayGroupsByOrgId(orgId, orgLabel, startDate, endDate, status, depthLevel, 0);
	}

	public void displayGroupsByOrgId(String orgId, String orgLabel, int startDate, int endDate, String status, int depthLevel, int currentLevel) {
		BOrgGroup borg = new BOrgGroup(orgId);
		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME);
		hcu.joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, orgId);

		hcu.joinTo(Employee.HREMPLSTATUSHISTORIES).dateBetween(HrEmplStatusHistory.EFFECTIVEDATE, startDate, endDate).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmplStatusHistory.STATUS_ID, status);

		List<Person> personAssociation = hcu.list();

		String label = (orgLabel + " - " + borg.getName()).trim();
		//   System.out.println("Lable " + label);
		while (label.startsWith("-"))
			label = label.substring(1).trim();

		for (Person pp : personAssociation)
			//System.out.println("Set report");
			setReportData(label, pp, startDate, endDate, status);

		for (OrgGroupHierarchy o : hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, orgId).list())
			//do I have anymore under me?
			if (depthLevel == currentLevel)
				getRestOfEmployees(new BOrgGroup(o.getOrgGroupByChildGroupId()), label, startDate, endDate, status, depthLevel);
			else
				displayGroupsByOrgId(o.getChildGroupId(), label, startDate, endDate, status, depthLevel, currentLevel + 1);

	}

	public String build(final int startDate, final int endDate, String orgGroupIds, String statusId, int depth, boolean subGroup) throws FileNotFoundException, DocumentException, ArahantException {

		try {
			final BHREmployeeStatus bEmployeeStatus = new BHREmployeeStatus(statusId);
			final PdfPTable table = makeTable(new int[]{1});

			writeHeader(table, "Employee Status: " + bEmployeeStatus.getName());
			writeHeader(table, "Status Changed: " + DateUtils.getDateFormatted(startDate) + " - " + DateUtils.getDateFormatted(endDate));
			if (StringUtils.isEmpty(orgGroupIds))
				writeHeader(table, "Organizational Group: (any)");
			else {
				String ogNames = "";

				for (OrgGroup og : hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPID, orgGroupIds).list())
					ogNames = ogNames + og.getName() + ", ";

				if (!isEmpty(ogNames))
					ogNames = ogNames.substring(0, ogNames.length() - 2);

				writeHeader(table, "Organizational Group: " + ogNames);
			}
			addTable(table);
			addHeaderLine();

			writeEmployeesDepth(startDate, endDate, orgGroupIds, statusId, depth, subGroup);
		} finally {
			close();
		}

		return getFilename();

	}

	public String build(final int startDate, final int endDate, String[] orgGroupIds, String statusId) throws FileNotFoundException, DocumentException, ArahantException {
		try {
			final BHREmployeeStatus bEmployeeStatus = new BHREmployeeStatus(statusId);
			final PdfPTable table = makeTable(new int[]{1});

			writeHeader(table, "Employee Status: " + bEmployeeStatus.getName());
			writeHeader(table, "Status Changed: " + DateUtils.getDateFormatted(startDate) + " - " + DateUtils.getDateFormatted(endDate));
			if (orgGroupIds.length == 0)
				writeHeader(table, "Organizational Group: (any)");
			else {
				String ogNames = "";

				for (OrgGroup og : hsu.createCriteria(OrgGroup.class).in(OrgGroup.ORGGROUPID, orgGroupIds).list())
					ogNames = ogNames + og.getName() + ", ";

				if (!isEmpty(ogNames))
					ogNames = ogNames.substring(0, ogNames.length() - 2);

				writeHeader(table, "Organizational Group: " + ogNames);
			}
			addTable(table);
			addHeaderLine();

			writeEmployees(startDate, endDate, orgGroupIds, statusId);
		} finally {
			close();
		}

		return getFilename();
	}

	protected String getMonthAndYear(final int month, final int year) {
		String formatted = "";

		switch (month) {
			case 1:
				formatted = "January";
				break;
			case 2:
				formatted = "February";
				break;
			case 3:
				formatted = "March";
				break;
			case 4:
				formatted = "April";
				break;
			case 5:
				formatted = "May";
				break;
			case 6:
				formatted = "June";
				break;
			case 7:
				formatted = "July";
				break;
			case 8:
				formatted = "August";
				break;
			case 9:
				formatted = "September";
				break;
			case 10:
				formatted = "October";
				break;
			case 11:
				formatted = "November";
				break;
			case 12:
				formatted = "December";
				break;
		}

		formatted += ", ";
		formatted += year;

		return formatted;
	}

	private void writeEmptyLines(int howmany, PdfPTable table) {
		for (int i = 0; i < howmany; i++)
			write(table, "", false);
	}

	protected void writeEmployeesDepth(int startDate, int endDate, String orgGroupId, String status, int depth, boolean subGroups) throws DocumentException, ArahantException {

		reportDataList = new ArrayList<ReportData>();

		if (!subGroups)
			depth = 0;


		if (!isEmpty(orgGroupId))
			displayGroupsByOrgId(orgGroupId, "", startDate, endDate, status, depth);
		else {
			String compId;
			if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
				compId = hsu.getCurrentPerson().getOrgGroupAssociations().iterator().next().getOrgGroupId();
			else
				compId = hsu.getCurrentCompany().getOrgGroupId();
			displayGroupsByOrgId(compId, "", startDate, endDate, status, depth);
		}



		PdfPTable table;

		table = makeTable(new int[]{12, 30, 11, 17, 30});
		writeColHeader(table, "Employee SSN\n ", Element.ALIGN_LEFT);
		writeColHeader(table, "Employee Name\nMedical", Element.ALIGN_LEFT);
		writeColHeader(table, "Status Date\nDental", Element.ALIGN_LEFT);
		writeColHeader(table, "Prior Status\n ", Element.ALIGN_LEFT);
		writeColHeader(table, " \nVision", Element.ALIGN_LEFT);

		// spin through org groups by which we are breaking this report down
		boolean alternateRow = false;
		int currentOrgGroupTotal = 0;
		String prevOrg = "";
		String orgLevelName = "";
		for (ReportData rd : reportDataList) {
			orgLevelName = rd.getOrgLabel();

//            System.out.println("loop label " + orgLevelName);

			if (!prevOrg.equalsIgnoreCase(rd.getOrgLabel()) && !prevOrg.equals("")) {
				if (currentOrgGroupTotal > 0)
					this.writeTotal(table, rd.getOrgLabel(), currentOrgGroupTotal);
				currentOrgGroupTotal = 0;
				writeEmptyLines(5, table);
				// table = makeTable(new int[]{12, 30, 11, 17, 30});
				writeColHeader(table, "Employee SSN\n ", Element.ALIGN_LEFT);
				writeColHeader(table, "Employee Name\nMedical", Element.ALIGN_LEFT);
				writeColHeader(table, "Status Date\nDental", Element.ALIGN_LEFT);
				writeColHeader(table, "Prior Status\n ", Element.ALIGN_LEFT);
				writeColHeader(table, " \nVision", Element.ALIGN_LEFT);

				table.setHeaderRows(1);
				// toggle the alternate row
				// alternateRow = !alternateRow;
				//prevOrg = rd.getOrgLabel();
				//
			}
			write(table, rd.getSSN(), alternateRow);
			write(table, rd.getNameLFM(), alternateRow);
			write(table, rd.getLastStatusDateFormatted(), alternateRow);
			write(table, rd.getPriorStatus(), alternateRow);
			write(table, " ", alternateRow);
			write(table, " ", alternateRow);
			write(table, rd.getHasMedical(), alternateRow);
			writeLeft(table, rd.getHasDental(), alternateRow, 2);
			write(table, rd.getHasVision(), alternateRow);

			currentOrgGroupTotal++;
			prevOrg = rd.getOrgLabel();
		}
		this.writeTotal(table, orgLevelName, currentOrgGroupTotal);
		addTable(table);

	}

	protected void writeEmployees(int startDate, int endDate, String[] orgGroupIds, String statusId) throws DocumentException, ArahantException {


		PdfPTable table;

		List<OrgGroup> ogList;
		if (orgGroupIds.length == 0)
			ogList = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, ArahantConstants.COMPANY_TYPE).orderBy(OrgGroup.NAME).list();
		else
			ogList = hsu.createCriteria(OrgGroup.class).in(OrgGroup.ORGGROUPID, orgGroupIds).list();

		// spin through org groups by which we are breaking this report down
		for (final OrgGroup og : ogList) {
			boolean alternateRow = true;
			int currentOrgGroupTotal = 0;

			table = makeTable(new int[]{12, 30, 11, 17, 30});
			writeColHeader(table, "Employee SSN\n ", Element.ALIGN_LEFT);
			writeColHeader(table, "Employee Name\nMedical", Element.ALIGN_LEFT);
			writeColHeader(table, "Status Date\nDental", Element.ALIGN_LEFT);
			writeColHeader(table, "Prior Status\n ", Element.ALIGN_LEFT);
			writeColHeader(table, " \nVision", Element.ALIGN_LEFT);

			table.setHeaderRows(1);

			Set ids = null;
			if (og.getOrgGroupHierarchiesForChildGroupId().size() > 0)
				ids = new BOrgGroup(og).getAllPersonIdsForOrgGroupHierarchy(false);

			HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME);

			hcu.joinTo(Employee.HREMPLSTATUSHISTORIES).dateBetween(HrEmplStatusHistory.EFFECTIVEDATE, startDate, endDate).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmplStatusHistory.STATUS_ID, statusId);

			if (ids != null)
				hcu.in(Employee.PERSONID, ids);

			final HibernateScrollUtil<Employee> employees = hcu.scroll();
			// write out each employee
			while (employees.next()) {
				BEmployee bEmployee = new BEmployee(employees.get());

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, bEmployee.getSsn(), alternateRow);
				write(table, bEmployee.getNameLFM(), alternateRow);
				write(table, bEmployee.getLastStatusDateFormatted(), alternateRow);
				write(table, bEmployee.getPriorStatus(startDate, endDate, statusId), alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, bEmployee.getHasMedical(), alternateRow);
				writeLeft(table, bEmployee.getHasDental(), alternateRow, 2);
				write(table, bEmployee.getHasVision(), alternateRow);

				currentOrgGroupTotal++;
			}

			this.writeTotal(table, og.getName(), currentOrgGroupTotal);
			employees.close();


			addTable(table);
		}
	}

	protected void writeTotal(final PdfPTable table, final String orgGroup, final int total) {
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(7);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Total for Organizational Group " + orgGroup + ": " + total, this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(7);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	public class ReportData {

		private String nameLFM;
		private String lastStatusDateFormatted;
		private String priorStatus;
		private String hasMedical;
		private String hasDental;
		private String hasVision;
		private String orgLevelName;
		private String SSN;

		/**
		 * @return the nameLFM
		 */
		public String getNameLFM() {
			return nameLFM;
		}

		/**
		 * @param nameLFM the nameLFM to set
		 */
		public void setNameLFM(String nameLFM) {
			this.nameLFM = nameLFM;
		}

		/**
		 * @return the lastStatusDateFormatted
		 */
		public String getLastStatusDateFormatted() {
			return lastStatusDateFormatted;
		}

		/**
		 * @param lastStatusDateFormatted the lastStatusDateFormatted to set
		 */
		public void setLastStatusDateFormatted(String lastStatusDateFormatted) {
			this.lastStatusDateFormatted = lastStatusDateFormatted;
		}

		/**
		 * @return the priorStatus
		 */
		public String getPriorStatus() {
			return priorStatus;
		}

		/**
		 * @param priorStatus the priorStatus to set
		 */
		public void setPriorStatus(String priorStatus) {
			this.priorStatus = priorStatus;
		}

		/**
		 * @return the hasMedical
		 */
		public String getHasMedical() {
			return hasMedical;
		}

		/**
		 * @param hasMedical the hasMedical to set
		 */
		public void setHasMedical(String hasMedical) {
			this.hasMedical = hasMedical;
		}

		/**
		 * @return the hasDental
		 */
		public String getHasDental() {
			return hasDental;
		}

		/**
		 * @param hasDental the hasDental to set
		 */
		public void setHasDental(String hasDental) {
			this.hasDental = hasDental;
		}

		/**
		 * @return the hasVision
		 */
		public String getHasVision() {
			return hasVision;
		}

		/**
		 * @param hasVision the hasVision to set
		 */
		public void setHasVision(String hasVision) {
			this.hasVision = hasVision;
		}

		/**
		 * @return the orgLevelName
		 */
		public String getOrgLabel() {
			return orgLevelName;
		}

		/**
		 * @param orgLevelName the orgLevelName to set
		 */
		public void setOrgLabel(String orgLevelName) {
			this.orgLevelName = orgLevelName;
		}

		/**
		 * @return the SSN
		 */
		public String getSSN() {
			return SSN;
		}

		/**
		 * @param SSN the SSN to set
		 */
		public void setSSN(String SSN) {
			this.SSN = SSN;
		}
	}

	public static void main(String[] args) {
		try {
			String test = "00001-0000000024";
			new HREmployeeStatusChangedReport().writeEmployeesDepth(20000101, 20100505, test, "001", 100, true);
		} catch (DocumentException ex) {
			Logger.getLogger(HREmployeeStatusChangedReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ArahantException ex) {
			Logger.getLogger(HREmployeeStatusChangedReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
