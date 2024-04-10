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
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
public class HREmployeesByStatusReport extends ReportBase {

	public HREmployeesByStatusReport() throws ArahantException {
		super("HREmpByStaRpt", "Employees by Status Report", false);
	}

	public class ReportData {

		private String orgLable;
		private String empName;
		private int hireDate;
		private String title;

		/**
		 * @return the orgLable
		 */
		public String getOrgLable() {
			return orgLable;
		}

		/**
		 * @param orgLable the orgLable to set
		 */
		public void setOrgLable(String orgLable) {
			this.orgLable = orgLable;
		}

		/**
		 * @return the empName
		 */
		public String getEmpName() {
			return empName;
		}

		/**
		 * @param empName the empName to set
		 */
		public void setEmpName(String empName) {
			this.empName = empName;
		}

		/**
		 * @return the hireDate
		 */
		public int getHireDate() {
			return hireDate;
		}

		/**
		 * @param hireDate the hireDate to set
		 */
		public void setHireDate(int hireDate) {
			this.hireDate = hireDate;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
	static List<ReportData> reportDataList = new ArrayList<ReportData>();

	public void setReportData(String orgLable, Person p, int effectDate, HrEmployeeStatus status) {
		ReportData rd = new ReportData();
		BEmployee emp = new BEmployee(p.getPersonId());

		//  if (emp.getLastStatusDate() <= effectDate && emp.getStatus().equalsIgnoreCase(status.getName()))
		{
//            System.out.println(orgLable + " " + p.getNameFL());
			rd.setOrgLable(orgLable);
			rd.setEmpName(p.getNameLFM());
			rd.setHireDate(emp.getHireDate());
			rd.setTitle(emp.getJobTitle());
			reportDataList.add(rd);
		}

	}

	public void getRestOfEmployees(BOrgGroup og, String orgLabel, int effectDate, HrEmployeeStatus status, int depthLevel) {
		Set<String> person = og.getAllPersonIdsForOrgGroupHierarchy(true);

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME);
		hcu.in(Person.PERSONID, person);
		hcu.employeeCurrentStatusIn(new String[]{status.getStatusId()}, effectDate);



		for (Person pp : hcu.list())
			setReportData(orgLabel, pp, effectDate, status); //System.out.println(orgLabel + " " + pp.getNameFL());
	}

	public void displayGroupsByOrgId(String orgId, String orgLabel, int effectDate, HrEmployeeStatus status, int depthLevel) {
		displayGroupsByOrgId(orgId, orgLabel, effectDate, status, depthLevel, 0);
	}

	public void displayGroupsByOrgId(String orgId, String orgLabel, int effectDate, HrEmployeeStatus status, int depthLevel, int currentLevel) {
		BOrgGroup borg = new BOrgGroup(orgId);
		HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class);

		hcu.employeeCurrentStatusIn(new String[]{status.getStatusId()}, effectDate);

		hcu.joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, orgId);

		List<Person> personAssociation = hcu.list();
		String label = (orgLabel + " - " + borg.getName()).trim();

		while (label.startsWith("-"))
			label = label.substring(1).trim();

		for (Person pp : personAssociation)
			//System.out.println("Set report");
			setReportData(label, pp, effectDate, status);

		//if we are at the top level, check for ppl NOT associated to any group in the company, not even the top level
		if (orgId.equals(hsu.getCurrentCompany().getCompanyId())) {
			HibernateCriteriaUtil<Person> hcu2 = hsu.createCriteria(Person.class);

			hcu2.employeeCurrentStatusIn(new String[]{status.getStatusId()}, effectDate);

			hcu2.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);

			List<Person> personAssociation2 = hcu2.list();
			String label2 = "(No Group Associations)";

			while (label.startsWith("-"))
				label = label.substring(1).trim();

			for (Person pp : personAssociation2)
				//System.out.println("Set report");
				setReportData(label2, pp, effectDate, status);
		}

		for (OrgGroupHierarchy o : hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, orgId).list())
			//do I have anymore under me?
			if (depthLevel == currentLevel)
				getRestOfEmployees(new BOrgGroup(o.getOrgGroupByChildGroupId()), label, effectDate, status, depthLevel);
			else
				displayGroupsByOrgId(o.getChildGroupId(), label, effectDate, status, depthLevel, currentLevel + 1);

	}

	public String build(final String orgGroupId, final HrEmployeeStatus status, int date, int depth, boolean subGroups) throws FileNotFoundException, DocumentException, ArahantException {

		reportDataList = new ArrayList<ReportData>();
		try {
			if (!subGroups)
				depth = 0;

			// if (depth==0)
			//   depth=100000;

			if (!isEmpty(orgGroupId))
				displayGroupsByOrgId(orgGroupId, "", date, status, depth);
			else {
				String compId;
				if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					compId = hsu.getCurrentPerson().getOrgGroupAssociations().iterator().next().getOrgGroupId();
				else
					compId = hsu.getCurrentCompany().getOrgGroupId();
				displayGroupsByOrgId(compId, "", date, status, depth);
			}

			final PdfPTable table = makeTable(new int[]{1});
			writeHeader(table, "Employee Status: " + status.getName());
			writeHeader(table, "Organizational Group: " + (isEmpty(orgGroupId) ? "(any)" : hsu.get(OrgGroup.class, orgGroupId).getName()));
			writeHeader(table, "Effective Date: " + DateUtils.getDateFormatted(date));

			addTable(table);

			addHeaderLine();
			writeEmployees(orgGroupId, status, date);
		} finally {
			close();

		}

		return getFilename();
	}

	/*
	 * @SuppressWarnings("unchecked") protected void writeEmployeesOld(final
	 * String orgGroupId, final HrEmployeeStatus status, int date) throws
	 * DocumentException, ArahantException { PdfPTable table;
	 *
	 *
	 * List<OrgGroup> ogList; if (isEmpty(orgGroupId)) { ogList =
	 * hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE,
	 * ArahantConstants.COMPANY_TYPE).orderBy(OrgGroup.NAME).list(); } else {
	 * ogList = new ArrayList<OrgGroup>(1); ogList.add(hsu.get(OrgGroup.class,
	 * orgGroupId)); } // THIS NEEDS TO BE BROKEN DOWN BY ORG GROUP ...
	 * multi-dimensional or something
	 *
	 * for (final OrgGroup og : ogList) {
	 *
	 * int currentOrgGroupTotal = 0;
	 *
	 * table = makeTable(new int[]{17, 20, 13, 50});
	 *
	 * writeColHeader(table, "Employee SSN", Element.ALIGN_LEFT);
	 * writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
	 * writeColHeader(table, "Status Date", Element.ALIGN_RIGHT);
	 * writeColHeader(table, "Notes", Element.ALIGN_LEFT);
	 *
	 * table.setHeaderRows(1);
	 *
	 * Query q = null;
	 *
	 * //TODO: someday change this from HQL - correlated subquery
	 *
	 * q = hsu.createQuery("select distinct emp.ssn, emp.fname, emp.lname,
	 * hist.notes, startHist.effectiveDate from Employee emp join
	 * emp.hrEmplStatusHistories hist join emp.hrEmplStatusHistories startHist
	 * join emp.orgGroupAssociations oga " + "where hist.hrEmployeeStatus =
	 * :stat and hist.effectiveDate = " + "(select max(effectiveDate) from
	 * HrEmplStatusHistory hist2 where hist2.employee=emp and
	 * hist2.effectiveDate<=" + date + ")" + "\n and oga.orgGroup = :og " + "
	 * and startHist.effectiveDate =(select min(effectiveDate) from
	 * HrEmplStatusHistory hist3 where hist3.employee=emp and
	 * hist3.hrEmployeeStatus.active='Y' )" + " order by emp.lname, emp.fname
	 * ");
	 *
	 *
	 * q.setEntity("stat", status); q.setEntity("og", og);
	 *
	 * final ScrollableResults res = q.scroll();
	 *
	 *
	 * boolean alternateRow = true; while (res.next()) {
	 *
	 *
	 * // toggle the alternate row alternateRow = !alternateRow;
	 *
	 * write(table, res.getString(0), alternateRow); write(table,
	 * res.getString(2) + ", " + res.getString(1), alternateRow);
	 * writeRight(table, DateUtils.getDateFormatted(res.getInteger(4)),
	 * alternateRow); write(table, res.getString(3), alternateRow); // use
	 * status notes
	 *
	 * currentOrgGroupTotal++;
	 *
	 *
	 *
	 * }
	 *
	 * res.close(); this.writeTotal(table, og.getName(), currentOrgGroupTotal);
	 * addTable(table); }
	 *
	 * }
	 */
	@SuppressWarnings("unchecked")
	protected void writeEmployees(final String orgGroupId, final HrEmployeeStatus status, int date) throws DocumentException, ArahantException {
		PdfPTable table;

//        List<OrgGroup> ogList;
//        if (isEmpty(orgGroupId)) {
//            ogList = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, ArahantConstants.COMPANY_TYPE).orderBy(OrgGroup.NAME).list();
//        } else {
//            ogList = new ArrayList<OrgGroup>(1);
//            ogList.add(hsu.get(OrgGroup.class, orgGroupId));
//        }

		addColumnHeader("Name", Element.ALIGN_LEFT, 35);
		addColumnHeader("Date " + status.getName().trim(), Element.ALIGN_LEFT, 24);
		addColumnHeader("Title", Element.ALIGN_LEFT, 37);

		table = makeTable(getColHeaderWidths());

		writeColHeaders(table);

		int currentOrgGroupTotal = 0;

		boolean alternateRow = false;

		String prevLabel = "";

		for (ReportData rd : reportDataList) {
			if (!rd.getOrgLable().equals(prevLabel)) {
				if (currentOrgGroupTotal != 0) {
					this.writeTotal(table, prevLabel, currentOrgGroupTotal);
					currentOrgGroupTotal = 0;
				}

				setSubHeader(rd.getOrgLable());
				writeLocation(table, rd.getOrgLable());

			}


			write(table, rd.getEmpName(), alternateRow);
			write(table, DateUtils.getDateFormatted(rd.getHireDate()), alternateRow);
			write(table, rd.getTitle(), alternateRow); // use status notes

			currentOrgGroupTotal++;

			prevLabel = rd.getOrgLable();
		}

		addTable(table);

		setSubHeader(null);

		table = makeTable(getColHeaderWidths());

		this.writeTotal(table, prevLabel, currentOrgGroupTotal);
		addTable(table);

	}

	protected void writeTotal(final PdfPTable table, final String orgGroup, final int total) {
		PdfPCell cell;

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(4);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("Total Employees for " + orgGroup + ": " + total, this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	protected void writeLocation(final PdfPTable table, final String location) {
		PdfPCell cell;

		Font underline = new Font(FontFamily.COURIER, 10F, Font.UNDERLINE);

		cell = new PdfPCell(new Paragraph("", this.baseFont));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(4);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(location, underline));
		cell.disableBorderSide(1 | 2 | 4 | 8);
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(cell);
	}

	public static void main(String[] args) {
		try {
			String test = "00001-0000000024";

			HrEmployeeStatus es = new BHREmployeeStatus("00000-0000000001").getBean();
			new HREmployeesByStatusReport().build(test, es, 20000101, 100, true);

		} catch (FileNotFoundException ex) {
			Logger.getLogger(HREmployeesByStatusReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (DocumentException ex) {
			Logger.getLogger(HREmployeesByStatusReport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ArahantException ex) {
			Logger.getLogger(HREmployeesByStatusReport.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
