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
package com.arahant.services.quickbooksApp;

import com.arahant.beans.AIProperty;
import com.arahant.beans.ClientCompany;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.GlAccount;
import com.arahant.beans.HrBenefitProjectJoin;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.HrPosition;
import com.arahant.beans.Invoice;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.PersonH;
import com.arahant.beans.ProductService;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectType;
import com.arahant.beans.QuickbooksAccountChange;
import com.arahant.beans.QuickbooksChange;
import com.arahant.beans.QuickbooksClientChange;
import com.arahant.beans.QuickbooksCompanyChange;
import com.arahant.beans.QuickbooksPersonChange;
import com.arahant.beans.QuickbooksProductChange;
import com.arahant.beans.QuickbooksProjectChange;
import com.arahant.beans.QuickbooksProjectTypeChange;
import com.arahant.beans.QuickbooksSync;
import com.arahant.beans.QuickbooksTimesheetChange;
import com.arahant.beans.QuickbooksVendorChange;
import com.arahant.beans.QuickbooksWageTypeChange;
import com.arahant.beans.Route;
import com.arahant.beans.Service;
import com.arahant.beans.Timesheet;
import com.arahant.beans.VendorCompany;
import com.arahant.beans.WagePaid;
import com.arahant.beans.WageType;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "QuickbooksAppOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class QuickbooksAppOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(QuickbooksAppOps.class);
	private static final String EMPLOYEE = "Employee";
	private static final String INVOICE = "Invoice";
	private static final String GL_ACCOUNTS = "GL Accounts";
	private static final String PRODUCT = "Product/Service";
	private static final String CLIENTS = "Clients";
	private static final String VENDORS = "Vendors";
	private static final String COMPANY = "Company";
	private static final String TIMESHEETS = "Timesheets";
	private static final String WAGE_TYPES = "Wage Types";
	private static final String PROJECTS = "Projects";
	private static final String PROJECT_TYPES = "Project Types";
	private static final String PAYROLL_DATA = "Payroll Data";
	private static final String PAYROLL_ITEMS = "Payroll Items";
	private static final String downFields[] = {CLIENTS, VENDORS, EMPLOYEE, INVOICE, TIMESHEETS, PROJECT_TYPES, PROJECTS};
	private static final int downOrder[] = {};
	private static final String upFields[] = {GL_ACCOUNTS, WAGE_TYPES, PAYROLL_ITEMS, PROJECT_TYPES, PRODUCT, CLIENTS, VENDORS, COMPANY, EMPLOYEE, PROJECTS, PAYROLL_DATA};
	private static final int upOrder[] = {};

	public QuickbooksAppOps() {
	}

	@WebMethod()
	public ListCapabilitiesReturn listCapabilities(/*@WebParam(name = "in")*/final ListCapabilitiesInput in) {
		final ListCapabilitiesReturn ret = new ListCapabilitiesReturn();
		try {
			checkLogin(in);

			ListCapabilitiesReturnItem down[] = new ListCapabilitiesReturnItem[downFields.length];

			for (short loop = 0; loop < down.length; loop++) {
				down[loop] = new ListCapabilitiesReturnItem();
				down[loop].setName(downFields[loop]);
				down[loop].setId("down-" + downFields[loop]);
				if (loop < downOrder.length)
					down[loop].setOrder(downOrder[loop]);
				else
					down[loop].setOrder(loop);
				QuickbooksSync qs = hsu.createCriteria(QuickbooksSync.class)
						.eq(QuickbooksSync.DIRECTION, QuickbooksSync.TYPE_DOWN)
						.eq(QuickbooksSync.FIELD_CODE, loop)
						.first();

				if (qs != null) {
					down[loop].setTime(DateUtils.getTime(qs.getInterfaceTime()));
					down[loop].setDate(DateUtils.getDate(qs.getInterfaceTime()));
				} else {
					down[loop].setTime(-1);
					down[loop].setDate(0);
				}
			}

			ret.setDownloads(down);

			ListCapabilitiesReturnItem up[] = new ListCapabilitiesReturnItem[upFields.length];

			for (short loop = 0; loop < up.length; loop++) {
				up[loop] = new ListCapabilitiesReturnItem();
				up[loop].setName(upFields[loop]);
				up[loop].setId("up-" + upFields[loop]);
				if (loop < upOrder.length)
					up[loop].setOrder(upOrder[loop]);
				else
					up[loop].setOrder(loop);
				QuickbooksSync qs = hsu.createCriteria(QuickbooksSync.class)
						.eq(QuickbooksSync.DIRECTION, QuickbooksSync.TYPE_UP)
						.eq(QuickbooksSync.FIELD_CODE, loop)
						.first();

				if (qs != null) {
					up[loop].setTime(DateUtils.getTime(qs.getInterfaceTime()));
					up[loop].setDate(DateUtils.getDate(qs.getInterfaceTime()));
				} else {
					up[loop].setTime(-1);
					up[loop].setDate(0);
				}
			}

			ret.setUploads(up);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoginReturn login(/*@WebParam(name = "in")*/final LoginInput in) {
		final LoginReturn ret = new LoginReturn();
		try {
			checkLogin(in);

			ret.setData(BPerson.getCurrent());

			List<CompanyDetail> compList = BPerson.getCurrent().getAllowedCompanies();

			LoginReturnItem[] lri = new LoginReturnItem[compList.size()];
			for (int loop = 0; loop < lri.length; loop++)
				lri[loop] = new LoginReturnItem(compList.get(loop));

			ret.setCompany(lri);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetQBRequestReturn getQBRequest(/*@WebParam(name = "in")*/final GetQBRequestInput in) {
		final GetQBRequestReturn ret = new GetQBRequestReturn();
		try {
			checkLogin(in);
			
			hsu.dontAIIntegrate();

			if (in.getId().startsWith("up-"))
				ret.setMessage(getUpRequest(in.getId()));

			if (in.getId().startsWith("down-"))
				ret.setMessage(getDownRequest(in.getId()));


			//  System.out.println(ret.getMessage());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SetQBResponseReturn setQBResponse(/*@WebParam(name = "in")*/final SetQBResponseInput in) {
		final SetQBResponseReturn ret = new SetQBResponseReturn();
		try {
			checkLogin(in);
			
			hsu.dontAIIntegrate();

			Document doc = DOMUtils.createDocument(in.getMessage());
			NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/*[@statusSeverity=\"Error\"]");

			String error = "";
			for (int loop = 0; loop < nl.getLength(); loop++) {
				Node n = nl.item(loop);
				String msg = DOMUtils.getAttribute((Element) n, "statusMessage");
				if (msg.startsWith("Object") && msg.indexOf("specified in the request cannot be found.") != -1) {
					//I have a situation where something was deleted from QB and not Arahant
					String qbid = msg.substring(msg.indexOf('"') + 1, msg.lastIndexOf('"'));

					QuickbooksChange qc = hsu.createCriteria(QuickbooksChange.class)
							.eq(QuickbooksChange.QB_ID, qbid)
							.first();

					if (qc != null) {
						String table = qc.getArahantTableName().replace('_', ' ');

						if (table.startsWith("gl "))
							table = "GL account";

						String name = "Unknown";

						if (table.equals("person"))
							name = hsu.get(Person.class, qc.getArahantRecordId()).getNameLFM();

						if (table.equals("client"))
							name = hsu.get(ClientCompany.class, qc.getArahantRecordId()).getName();

						if (table.equals("vendor"))
							name = hsu.get(VendorCompany.class, qc.getArahantRecordId()).getName();

						if (table.equals("GL account"))
							name = hsu.get(GlAccount.class, qc.getArahantRecordId()).getAccountName();

						if (table.equals("product service"))
							name = hsu.get(ProductService.class, qc.getArahantRecordId()).getDescription();

						if (table.equals("project"))
							name = hsu.get(Project.class, qc.getArahantRecordId()).getProjectName();

						if (table.equals("project type"))
							name = hsu.get(ProjectType.class, qc.getArahantRecordId()).getCode();

						if (table.equals("wage type"))
							name = hsu.get(WageType.class, qc.getArahantRecordId()).getWageName();

						name = name.trim();

						ArahantSession.addReturnMessage("A " + table + " record (" + name + ") was deleted from Quickbooks, but still exists in Arahant.  Please either delete it from Arahant at this time, or re-run the synchronize to put it back into Quickbooks.");

						hsu.delete(qc);

						hsu.commitTransaction();
						hsu.beginTransaction();
					}
					continue;
				}

				if (msg.indexOf("QuickBooks error message: The list element is in use.") != -1)
					msg = "An item to update is currently open in QuickBooks.  Please close QuickBooks dialogs and retry.";

				error += msg + "\n";
			}

			if (!isEmpty(error))
				throw new ArahantException(error);

			if (in.getId().startsWith("up-"))
				handleUpResponses(in.getId(), in.getMessage());

			if (in.getId().startsWith("down-"))
				handleDownResponses(in.getId(), in.getMessage());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private String getCustomerBodyXML(String ret, BClientCompany client, boolean mod) {

		ret += "<Name >" + DOMUtils.escapeText(client.getName()) + "</Name>";
		if (client.getInactiveDate() != 0 && client.getInactiveDate() < DateUtils.now())
			ret += "<IsActive>false</IsActive>";
		else
			ret += "<IsActive>true</IsActive>";
		ret += "<CompanyName >" + DOMUtils.escapeText(client.getName()) + "</CompanyName> "
				+ "<FirstName >" + DOMUtils.escapeText(client.getMainContactFname()) + "</FirstName> "
				+ "<LastName >" + DOMUtils.escapeText(client.getMainContactLname()) + "</LastName> "
				+ "<BillAddress> "
				+ "<Addr1 >" + DOMUtils.escapeText(client.getStreet()) + "</Addr1> "
				+ "<Addr2 >" + DOMUtils.escapeText(client.getStreet2()) + "</Addr2>"
				+ "<City >" + DOMUtils.escapeText(client.getCity()) + "</City> "
				+ "<State >" + DOMUtils.escapeText(client.getState()) + "</State> "
				+ "<PostalCode >" + DOMUtils.escapeText(client.getZip()) + "</PostalCode> "
				+ "<Country >" + DOMUtils.escapeText(client.getCountry()) + "</Country> "
				+ "</BillAddress>"
				+ "<Phone >" + DOMUtils.escapeText(client.getMainPhoneNumber()) + "</Phone> "
				+ "<Fax >" + DOMUtils.escapeText(client.getMainFaxNumber()) + "</Fax> "
				+ "<Email >" + DOMUtils.escapeText(client.getMainContactPersonalEmail()) + "</Email>"
				+ "<Contact >" + DOMUtils.escapeText(client.getMainContactName()) + "</Contact>";

		return ret;
	}

	private String getProjectBodyXML(String ret, BProject proj, boolean mod) {
		BClientCompany client = new BClientCompany(proj.getRequestingOrgGroupId());

		QuickbooksClientChange qcc = hsu.createCriteria(QuickbooksClientChange.class)
				.eq(QuickbooksClientChange.ARAHANT_ID, client.getOrgGroupId())
				.first();
		if (qcc == null)
			throw new ArahantWarning("Please synchronize Clients first.");

		String qbParentId = qcc.getQbRecordId();


		QuickbooksProjectTypeChange qptc = hsu.createCriteria(QuickbooksProjectTypeChange.class)
				.eq(QuickbooksProjectTypeChange.ARAHANT_ID, proj.getProjectTypeId())
				.first();

		if (qptc == null)
			throw new ArahantWarning("Please synchronize Project Types first.");

		String qbJobTypeId = qptc.getQbRecordId();

		ret += "<Name >" + DOMUtils.escapeText(proj.getSummary()) + "</Name>"
				+ "<ParentRef><ListID>" + qbParentId + "</ListID></ParentRef>"
				+ "<CompanyName >" + DOMUtils.escapeText(proj.getCompanyName()) + "</CompanyName> "
				+ "<FirstName >" + DOMUtils.escapeText(client.getMainContactFname()) + "</FirstName> "
				+ "<LastName >" + DOMUtils.escapeText(client.getMainContactLname()) + "</LastName> "
				+ "<BillAddress> "
				+ "<Addr1 >" + DOMUtils.escapeText(client.getStreet()) + "</Addr1> "
				+ "<Addr2 >" + DOMUtils.escapeText(client.getStreet2()) + "</Addr2>"
				+ "<City >" + DOMUtils.escapeText(client.getCity()) + "</City> "
				+ "<State >" + DOMUtils.escapeText(client.getState()) + "</State> "
				+ "<PostalCode >" + DOMUtils.escapeText(client.getZip()) + "</PostalCode> "
				+ "<Country >" + DOMUtils.escapeText(client.getCountry()) + "</Country> "
				+ "</BillAddress>"
				+ "<Phone >" + DOMUtils.escapeText(client.getMainPhoneNumber()) + "</Phone> "
				+ "<Fax >" + DOMUtils.escapeText(client.getMainFaxNumber()) + "</Fax> "
				+ "<Email >" + DOMUtils.escapeText(client.getMainContactPersonalEmail()) + "</Email>"
				+ "<Contact >" + DOMUtils.escapeText(client.getMainContactName()) + "</Contact>"
				+ "<JobTypeRef><ListID>" + qbJobTypeId + "</ListID></JobTypeRef>";

		return ret;
	}

	private String getProjectTypeBodyXML(String ret, BProjectType pt, boolean isModify) {
		ret += "<Name>" + pt.getCode() + "</Name>";
		ret += "<IsActive>true</IsActive>";

		return ret;
	}

	private String getProjectTypeXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		//Find employees without qb id's

		for (QuickbooksProjectTypeChange pcs : hsu.createCriteria(QuickbooksProjectTypeChange.class).isNull(QuickbooksProjectTypeChange.QB_ID).list()) {
			ret += "<JobTypeAddRq requestID = \"" + (++count) + "\"><JobTypeAdd>";
			ret = getProjectTypeBodyXML(ret, new BProjectType(pcs.getProjectType()), false);

			ret += "</JobTypeAdd></JobTypeAddRq>";
		}

		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksProjectTypeChange.class)
				.selectFields(QuickbooksProjectTypeChange.ARAHANT_ID)
				.list();

		//add types that aren't in change table
		for (ProjectType emp : hsu.createCriteria(ProjectType.class).notIn(ProjectType.PROJECTTYPEID, ids).list()) {
			ret += "<JobTypeAddRq requestID = \"" + (++count) + "\"><JobTypeAdd>";
			ret = getProjectTypeBodyXML(ret, new BProjectType(emp), false);

			ret += "</JobTypeAdd></JobTypeAddRq>";
		}

		//No modify currently for Job Types

		for (QuickbooksProjectTypeChange pcs : hsu.createCriteria(QuickbooksProjectTypeChange.class)
				.isNotNull(QuickbooksProjectTypeChange.QB_ID)
				.eq(QuickbooksProjectTypeChange.RECORD_CHANGED, 'Y')
				.list())
			ArahantSession.addReturnMessage("You must make Job Type modifications in Quickbooks manually.  " + pcs.getProjectType().getCode() + " was changed.");

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";

		return ret;
	}

	private String getVendorBodyXML(String ret, BVendorCompany vendor, boolean mod) {
		ret += "<Name >" + DOMUtils.escapeText(vendor.getName()) + "</Name>"
				+ "<CompanyName >" + DOMUtils.escapeText(vendor.getName()) + "</CompanyName> "
				+ "<FirstName >" + DOMUtils.escapeText(vendor.getMainContactFname()) + "</FirstName> "
				+ "<LastName >" + DOMUtils.escapeText(vendor.getMainContactLname()) + "</LastName> "
				+ "<VendorAddress> "
				+ "<Addr1 >" + DOMUtils.escapeText(vendor.getStreet()) + "</Addr1> "
				+ "<Addr2 >" + DOMUtils.escapeText(vendor.getStreet2()) + "</Addr2>"
				+ "<City >" + DOMUtils.escapeText(vendor.getCity()) + "</City> "
				+ "<State >" + DOMUtils.escapeText(vendor.getState()) + "</State> "
				+ "<PostalCode >" + DOMUtils.escapeText(vendor.getZip()) + "</PostalCode> "
				+ "<Country >" + DOMUtils.escapeText(vendor.getCountry()) + "</Country> "
				+ "</VendorAddress>"
				+ "<Phone >" + DOMUtils.escapeText(vendor.getMainPhoneNumber()) + "</Phone> "
				+ "<Fax >" + DOMUtils.escapeText(vendor.getMainFaxNumber()) + "</Fax> "
				+ "<Email >" + DOMUtils.escapeText(vendor.getMainContactPersonalEmail()) + "</Email>"
				+ "<Contact >" + DOMUtils.escapeText(vendor.getMainContactName()) + "</Contact>";

		return ret;
	}

	private String getDownRequest(String id) {
		id = id.substring(5);

		if (id.equals(INVOICE))
			return getInvoiceXML();

		if (id.equals(EMPLOYEE))
			return getEmployeeXML();

		if (id.equals(TIMESHEETS)) {
			preProcessTimesheets();
			return getTimesheetsXML();
		}
		if (id.equals(CLIENTS))
			return getClientXML();

		if (id.equals(PROJECTS))
			return getProjectXML();

		if (id.equals(VENDORS))
			return getVendorXML();

		if (id.equals(PROJECT_TYPES))
			return getProjectTypeXML();

		return "";
	}

	private String getEmployeeBodyXML(String ret, BEmployee bemp, boolean mod) {

		if (mod) {
			QuickbooksSync qs = hsu.createCriteria(QuickbooksSync.class)
					.eq(QuickbooksSync.FIELD_CODE, getDownFieldCode(EMPLOYEE))
					.eq(QuickbooksSync.DIRECTION, QuickbooksSync.TYPE_DOWN)
					.first();
			//did this employee have a ssn or sex change since the last export?
			PersonH hist = null;
			if (qs != null)
				hist = hsu.createCriteria(PersonH.class)
						.eq(PersonH.PERSONID, bemp.getPersonId())
						.lt(PersonH.RECORD_CHANGE_DATE, qs.getInterfaceTime())
						.orderByDesc(PersonH.RECORD_CHANGE_DATE)
						.first();

			if (hist != null) {
				if (hist.getSex() != bemp.getSex().charAt(0))
					ArahantSession.addReturnMessage("Gender for " + bemp.getNameLFM() + " must be set in Quickbooks manually.");

				if (!hist.getSsn().equals(bemp.getSsn()))
					ArahantSession.addReturnMessage("SSN change for " + bemp.getNameLFM() + " must be set in Quickbooks manually.");
			}
		}

		ret += "<IsActive>" + (bemp.isActive() < 1) + "</IsActive>"
				+ "<FirstName>" + bemp.getFirstName() + "</FirstName>"
				+ "<MiddleName>" + bemp.getMiddleName() + "</MiddleName>"
				+ "<LastName>" + bemp.getLastName() + "</LastName>";
		if (!isEmpty(bemp.getState()))
			ret += "<EmployeeAddress>"
					+ "<Addr1>" + bemp.getStreet() + "</Addr1>"
					+ "<Addr2>" + bemp.getStreet2() + "</Addr2>"
					+ "<City>" + bemp.getCity() + "</City>"
					+ "<State>" + bemp.getState() + "</State>"
					+ "<PostalCode>" + bemp.getZip() + "</PostalCode>"
					+ "</EmployeeAddress>";
		ret += "<Phone>" + bemp.getHomePhone() + "</Phone>"
				+ "<Mobile>" + bemp.getMobilePhone() + "</Mobile>";
		if (!mod)
			ret += "<SSN>" + bemp.getSsn() + "</SSN>";
		ret += "<Email>" + bemp.getPersonalEmail() + "</Email>";
		if (!mod) {
			ret += "<Gender>" + (bemp.getSex().charAt(0) == 'F' ? "Female" : "Male") + "</Gender>";
			ret += "<HiredDate>" + formatDate(bemp.getHireDate()).substring(0, 10) + "</HiredDate>";
		}
		if (bemp.isActive() > 0)
			ret += "<ReleasedDate>" + formatDate(bemp.getTermDate()).substring(0, 10) + "</ReleasedDate>";
		ret += "<BirthDate>" + formatDate(bemp.getDob()).substring(0, 10) + "</BirthDate>"
				+ "<AccountNumber>" + bemp.getExtRef() + "</AccountNumber>";

		QuickbooksWageTypeChange wtc = hsu.createCriteria(QuickbooksWageTypeChange.class)
				.eq(QuickbooksWageTypeChange.ARAHANT_ID, bemp.getWageTypeId())
				.first();

		if (wtc == null)
			throw new ArahantWarning("Could not find wage type '" + bemp.getWageTypeName() + "' for employee " + bemp.getNameLFM() + " in Quickbooks.\nWage types need to come only from Quickbooks.\nPlease synchronize Wage Types from Quickbooks first and make sure all employee records are using Quickbooks wage types only.");
		//Payroll info goes here
		if (mod)
			ret += "<EmployeePayrollInfoMod>"
					+ "<Earnings>"
					+ "<PayrollItemWageRef>"
					+ "<ListID>" + wtc.getQbRecordId() + "</ListID>"
					+ "</PayrollItemWageRef>"
					+ "<Rate>" + bemp.getCurrentSalary() + "</Rate>"
					+ "</Earnings>"
					+ "<UseTimeDataToCreatePaychecks>UseTimeData</UseTimeDataToCreatePaychecks>"
					+ "</EmployeePayrollInfoMod>";
		else
			ret += "<EmployeePayrollInfo>"
					+ //NotSet, UseTimeData, DoNotUseTimeData
					"<Earnings>"
					+ "<PayrollItemWageRef>"
					+ "<ListID>" + wtc.getQbRecordId() + "</ListID>"
					+ "</PayrollItemWageRef>"
					+ "<Rate>" + bemp.getCurrentSalary() + "</Rate>"
					+ "</Earnings>"
					+ "<UseTimeDataToCreatePaychecks>UseTimeData</UseTimeDataToCreatePaychecks>"
					+ "</EmployeePayrollInfo>";
		return ret;
	}
	
	private static HashMap<Integer, String> timeMap = new HashMap<Integer, String>();

	private String getTimesheetsXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		for (QuickbooksTimesheetChange pcs : hsu.createCriteria(QuickbooksTimesheetChange.class).isNull(QuickbooksTimesheetChange.QB_ID).list()) {
			ret += "<TimeTrackingAddRq requestID = \"" + (++count) + "\">";
			ret += getTimeTrackingAdd(new BTimesheet(pcs.getTimesheet()));
			ret += "</TimeTrackingAddRq>";
			timeMap.put(count, pcs.getTimesheet().getTimesheetId());

		}

		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksTimesheetChange.class)
				.selectFields(QuickbooksTimesheetChange.ARAHANT_ID)
				.list();

		//add timehseets that aren't in change table
		for (Timesheet ts : hsu.createCriteria(Timesheet.class).notIn(Timesheet.TIMESHEETID, ids)
				.in(Timesheet.STATE, new char[]{ArahantConstants.TIMESHEET_APPROVED, ArahantConstants.TIMESHEET_INVOICED})
				.list()) {
			ret += "<TimeTrackingAddRq requestID = \"" + (++count) + "\">";
			ret += getTimeTrackingAdd(new BTimesheet(ts));
			ret += "</TimeTrackingAddRq>";
			timeMap.put(count, ts.getTimesheetId());
		}

		for (QuickbooksTimesheetChange pcs : hsu.createCriteria(QuickbooksTimesheetChange.class)
				.isNotNull(QuickbooksTimesheetChange.QB_ID)
				.eq(QuickbooksTimesheetChange.RECORD_CHANGED, 'Y')
				.list()) {
			ret += "<TimeTrackingModRq requestID = \"" + (++count) + "\">";
			ret += getTimeTrackingMod(new BTimesheet(pcs.getTimesheet()));
			ret += "</TimeTrackingModRq>";
		}

		ret += "</QBXMLMsgsRq></QBXML>";

		return ret;
	}

	private short getUpFieldCode(String id) {
		short field = -1;

		for (short loop = 0; loop < upFields.length; loop++)
			if (id.equals(upFields[loop])) {
				field = loop;
				break;
			}

		if (field == -1)
			throw new ArahantException("Unknown field requested " + id);

		return field;
	}

	private short getDownFieldCode(String id) {
		short field = -1;

		for (short loop = 0; loop < downFields.length; loop++)
			if (id.equals(downFields[loop])) {
				field = loop;
				break;
			}

		if (field == -1)
			throw new ArahantException("Unknown field requested " + id);

		return field;
	}

	private String getUpRequest(String id) {

		id = id.substring(3);

		short field = getUpFieldCode(id);

		QuickbooksSync qs = hsu.createCriteria(QuickbooksSync.class)
				.eq(QuickbooksSync.FIELD_CODE, field)
				.eq(QuickbooksSync.DIRECTION, QuickbooksSync.TYPE_UP)
				.first();

		Date lastTime = new Date(0);

		if (qs != null)
			lastTime = qs.getInterfaceTime();


		if (id.equals(GL_ACCOUNTS))
			return requestAccounts(lastTime);

		if (id.equals(PRODUCT))
			return requestServices(lastTime);

		if (id.equals(CLIENTS))
			return requestClients(lastTime);

		if (id.equals(VENDORS))
			return requestVendors(lastTime);

		if (id.equals(EMPLOYEE))
			return requestEmployees(lastTime);

		if (id.equals(COMPANY))
			return requestCompanies(lastTime);

		if (id.equals(WAGE_TYPES))
			return requestWageTypes(lastTime);

		if (id.equals(PAYROLL_ITEMS))
			return requestPayrollItems(lastTime);

		if (id.equals(PROJECTS))
			return requestClients(lastTime);

		if (id.equals(PROJECT_TYPES))
			return requestJobTypes(lastTime);

		if (id.equals(PAYROLL_DATA))
			return requestPayrollData(lastTime);

		return "";
	}

	private void handleDownResponses(String id, String message) throws Exception {
		id = id.substring(5);

		if (id.equals(EMPLOYEE))
			parseEmployeeDownResponse(message);

		if (id.equals(TIMESHEETS))
			parseTimesheetDownResponse(message);

		if (id.equals(CLIENTS))
			parseClientDownResponse(message);

		if (id.equals(PROJECTS))
			parseProjectDownResponse(message);

		if (id.equals(VENDORS))
			parseVendorDownResponse(message);

		if (id.equals(COMPANY))
			parseCompanyDownResponse(message);

		if (id.equals(PROJECT_TYPES))
			parseProjectTypeDownResponse(message);

		//if (id.equals(WAGE_TYPES))
		//  parseDownWageTypeResponse(message);

		updateQuickbooksSync(id, new Date(), QuickbooksSync.TYPE_DOWN);
	}

	private void handleUpResponses(String id, String message) throws Exception {
		id = id.substring(3);

		Date lastDate = new Date(0);

		if (id.equals(GL_ACCOUNTS))
			lastDate = parseAccounts(message);

		if (id.equals(PRODUCT))
			lastDate = parseServices(message);

		if (id.equals(CLIENTS))
			lastDate = parseClients(message);

		if (id.equals(PROJECTS))
			lastDate = parseProjects(message);

		if (id.equals(VENDORS))
			lastDate = parseVendors(message);

		if (id.equals(COMPANY))
			lastDate = parseCompanies(message);

		if (id.equals(EMPLOYEE))
			lastDate = parseEmployees(message);

		if (id.equals(WAGE_TYPES))
			lastDate = parseWageTypes(message);

		if (id.equals(PAYROLL_ITEMS))
			lastDate = parsePayrollItems(message);

		if (id.equals(PROJECT_TYPES))
			lastDate = parseJobTypes(message);

		if (id.equals(PAYROLL_DATA))
			lastDate = parsePayrollData(message);

		updateQuickbooksSync(id, lastDate, QuickbooksSync.TYPE_UP);
	}

	private void parseEmployeeDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/EmployeeAddRs/EmployeeRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String firstName = DOMUtils.unescapeText(DOMUtils.getValue(n, "FirstName"));
			String lastName = DOMUtils.unescapeText(DOMUtils.getValue(n, "LastName"));
			String ssn = DOMUtils.unescapeText(DOMUtils.getValue(n, "SSN"));

			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			Person p = hsu.createCriteria(Person.class)
					.eq(Person.FNAME, firstName)
					.eq(Person.LNAME, lastName)
					.eq(Person.SSN, ssn)
					.first();

			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksPersonChange qac = new QuickbooksPersonChange();
			qac.setPerson(p);
			qac.setQbRecordId(listId);
			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			qac.generateId();
			hsu.insert(qac);
		}

		nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/EmployeeModRs/EmployeeRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));


			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksPersonChange qac = hsu.createCriteria(QuickbooksPersonChange.class)
					.eq(QuickbooksPersonChange.QB_ID, listId)
					.first();

			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			hsu.saveOrUpdate(qac);
		}
	}

	private void parseClientDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerAddRs/CustomerRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));
			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			ClientCompany c = hsu.createCriteria(ClientCompany.class)
					.eq(ClientCompany.NAME, name)
					.first();

			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksClientChange qac = new QuickbooksClientChange();
			qac.setClient(c);
			qac.setQbRecordId(listId);
			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			qac.generateId();
			hsu.insert(qac);
			hsu.flush();
		}

		nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerModRs/CustomerRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			QuickbooksClientChange qac = hsu.createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksPersonChange.QB_ID, listId)
					.first();

			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			hsu.saveOrUpdate(qac);
			hsu.flush();
		}
	}

	private Date parsePayrollData(String message) throws Exception {
		Date lastDate = new Date(0);

		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/PayrollDetailReportQueryRs/ReportRet/ReportData/DataRow");

		BWagePaid currentWagePaid = null;

		String lastCheck = "";
		String lastCheckDate = "";
		String lastPerson = "";

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String checktype = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"3\"]"), "value");

			if (!isEmpty(checktype) && !"Paycheck".equals(checktype)) {
				while (!"Paycheck".equals(checktype) && ++loop < nl.getLength()) {
					n = nl.item(loop);
					checktype = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"3\"]"), "value");
				}

				if (loop >= nl.getLength())
					continue;
			}
			String date = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"1\"]"), "value");

			if (isEmpty(date))
				continue;

			String checkNo = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"2\"]"), "value");
			String name = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"4\"]"), "value");
			String type = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"5\"]"), "value");
			String base = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"6\"]"), "value");
			String amount = DOMUtils.getAttribute((Element) DOMUtils.getNode(n, "ColData[@colID=\"7\"]"), "value");


			if ((!isEmpty(checkNo) && !checkNo.equals(lastCheck)) || !name.equals(lastPerson) || !lastCheckDate.equals(date)) {
				if (!isEmpty(checkNo))
					lastCheck = checkNo;
				lastPerson = name;
				lastCheckDate = date;

				currentWagePaid = new BWagePaid();
				currentWagePaid.create();

				Date dt = sdfDateOnly.parse(date);
				if (dt.after(lastDate))
					lastDate = dt;
				currentWagePaid.setDate(dt);
				currentWagePaid.setCheckNo(checkNo);
				currentWagePaid.setPaymentMethod(WagePaid.METHOD_CHECK);

				//need to split the name
				String fname = name.trim().substring(0, name.indexOf(' ')).trim();
				String lname = name.trim().substring(name.lastIndexOf(' ')).trim();
				String mname = name.trim().substring(name.indexOf(' '), name.lastIndexOf(' ')).trim();

				String empId = hsu.createCriteria(Employee.class)
						.selectFields(Employee.PERSONID)
						.eq(Employee.FNAME, fname)
						.eq(Employee.LNAME, lname)
						.eq(Employee.MNAME, mname)
						.stringVal();

				if (isEmpty(empId))
					throw new ArahantWarning("Could not find employee " + name + ".  Please synchronise employees.");

				currentWagePaid.setEmployeeId(empId);
				currentWagePaid.insert();
			}

			if (currentWagePaid == null)
				continue;

			BWagePaidDetail wpd = new BWagePaidDetail();
			wpd.create();
			wpd.setBaseAmount(Double.parseDouble(base));
			WageType wt = hsu.createCriteria(WageType.class)
					.eq(WageType.NAME, type)
					.first();

			if (wt == null)
				throw new ArahantWarning("Please synchronise wage types and payroll items and try again.");

			wpd.setType(wt);
			wpd.setAmount(Double.parseDouble(amount));
			wpd.setWagePaid(currentWagePaid);
			wpd.insert();
		}

		return lastDate;
	}

	private void parseProjectDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerAddRs/CustomerRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));
			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			Project p = hsu.createCriteria(Project.class)
					.eq(Project.DESCRIPTION, name)
					.first();

			if (p == null)
				p = hsu.createCriteria(Project.class)
						.eq(Project.PROJECTNAME, name)
						.first();

			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksProjectChange qac = new QuickbooksProjectChange();
			qac.setProject(p);
			qac.setQbRecordId(listId);
			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			qac.generateId();
			hsu.insert(qac);
		}

		nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerModRs/CustomerRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			QuickbooksProjectChange qac = hsu.createCriteria(QuickbooksProjectChange.class)
					.eq(QuickbooksProjectChange.QB_ID, listId)
					.first();

			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			hsu.saveOrUpdate(qac);

		}


	}

	private void parseTimesheetDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/TimeTrackingAddRs");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);


			int req = Integer.parseInt(DOMUtils.getAttribute((Element) n, "requestID"));

			if (timeMap.containsKey(req)) {
				// sd.accountMap.put(listId, gla.getGlAccountId());
				QuickbooksTimesheetChange qac = new QuickbooksTimesheetChange();
				qac.setTimesheet(hsu.get(Timesheet.class, timeMap.get(req)));
				qac.setQbRecordId(DOMUtils.getString(n, "TimeTrackingRet/TxnID"));
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "TimeTrackingRet/EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "TimeTrackingRet/ListID"));

				QuickbooksTimesheetChange qac = hsu.createCriteria(QuickbooksTimesheetChange.class)
						.eq(QuickbooksTimesheetChange.QB_ID, listId)
						.first();

				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "TimeTrackingRet/EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		timeMap.clear();
	}

	private void parseProjectTypeDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/JobTypeAddRs/JobTypeRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String name = DOMUtils.getString(n, "Name");
			String listId = DOMUtils.getString(n, "ListID");

			ProjectType pt = hsu.createCriteria(ProjectType.class)
					.eq(ProjectType.CODE, name)
					.first();

			QuickbooksProjectTypeChange qptc = hsu.createCriteria(QuickbooksProjectTypeChange.class)
					.eq(QuickbooksProjectTypeChange.PROJECT_TYPE, pt)
					.first();

			if (qptc == null) {

				// sd.accountMap.put(listId, gla.getGlAccountId());
				QuickbooksProjectTypeChange qac = new QuickbooksProjectTypeChange();
				qac.setProjectType(pt);
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				QuickbooksProjectTypeChange qac = hsu.createCriteria(QuickbooksProjectTypeChange.class)
						.eq(QuickbooksVendorChange.QB_ID, listId)
						.first();

				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		timeMap.clear();
	}

	private void parseVendorDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/VendorAddRs/VendorRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));
			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			VendorCompany c = hsu.createCriteria(VendorCompany.class)
					.eq(VendorCompany.NAME, name)
					.first();

			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksVendorChange qac = new QuickbooksVendorChange();
			qac.setVendor(c);
			qac.setQbRecordId(listId);
			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			qac.generateId();
			hsu.insert(qac);

		}

		nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/VendorModRs/VendorRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			String listId = DOMUtils.unescapeText(DOMUtils.getValue(n, "ListID"));

			QuickbooksVendorChange qac = hsu.createCriteria(QuickbooksVendorChange.class)
					.eq(QuickbooksVendorChange.QB_ID, listId)
					.first();

			qac.setRecordChanged('N');
			qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
			hsu.saveOrUpdate(qac);
		}
	}

	private void parseCompanyDownResponse(String message) throws Exception {
		Document doc = DOMUtils.createDocument(message);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CompanyModRs/CompanyRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);
			QuickbooksCompanyChange qac = hsu.createCriteria(QuickbooksCompanyChange.class)
					.eq(QuickbooksCompanyChange.COMPANY, hsu.getCurrentCompany())
					.first();

			if (qac == null) {
				qac = new QuickbooksCompanyChange();
				qac.setCompany(hsu.getCurrentCompany());
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}

		}
	}

	private Date parseEmployees(String message) throws Exception {

		Document doc = DOMUtils.createDocument(message);

		//find all account rets

		Date lastDate = new Date(0);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/EmployeeQueryRs/EmployeeRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			Date lastModify = new Date();
			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				lastModify = sdf.parse(time);


				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String listId = DOMUtils.getValue(n, "ListID");

			//See if the person exists
			QuickbooksPersonChange qpc = hsu.createCriteria(QuickbooksPersonChange.class)
					.eq(QuickbooksPersonChange.QB_ID, listId)
					.first();

			BEmployee bemp;

			String ssn = DOMUtils.getValue(n, "SSN");

			if (isEmpty(ssn))
				throw new ArahantWarning("Can not find SSN for " + DOMUtils.getValue(n, "LastName") + ", " + DOMUtils.getValue(n, "FirstName")
						+ ".  All employees require a SSN.\nPlease make sure the Arahant Quickbooks Connector is granted permissions\n"
						+ "to import all data and that all employees have SSN's.");

			if (qpc != null)
				bemp = new BPerson(qpc.getPerson()).getBEmployee();
			else {
				//look via ssn
				Employee emp = hsu.createCriteria(Employee.class)
						.eq(Employee.SSN, ssn)
						.first();
				if (emp != null) {
					bemp = new BEmployee(emp);
					qpc = new QuickbooksPersonChange();
					qpc.setPerson(bemp.getEmployee());
					qpc.setQbRecordId(listId);
					qpc.setRecordChanged('N');
					qpc.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
					qpc.generateId();
					hsu.insert(qpc);
				} else {
					bemp = new BEmployee();
					bemp.create();
				}
			}

			//set employee data
			bemp.setFirstName(DOMUtils.getValue(n, "FirstName"));
			bemp.setLastName(DOMUtils.getValue(n, "LastName"));
			bemp.setMiddleName(DOMUtils.getValue(n, "MiddleName"));

			bemp.setStreet(DOMUtils.getValue(n, "EmployeeAddress/Addr1"));
			bemp.setStreet2(DOMUtils.getValue(n, "EmployeeAddress/Addr2"));
			bemp.setCity(DOMUtils.getValue(n, "EmployeeAddress/City"));
			bemp.setState(DOMUtils.getValue(n, "EmployeeAddress/State"));
			bemp.setZip(DOMUtils.getValue(n, "EmployeeAddress/PostalCode"));

			bemp.setHomePhone(DOMUtils.getValue(n, "Phone"));
			bemp.setMobilePhone(DOMUtils.getValue(n, "Mobile"));
			bemp.setWorkFax(DOMUtils.getValue(n, "Fax"));
			bemp.setPersonalEmail(DOMUtils.getValue(n, "Email"));
			bemp.setSex(DOMUtils.getValue(n, "Gender"));
			String dob = DOMUtils.getValue(n, "BirthDate");
			if (!isEmpty(dob))
				bemp.setDob(DateUtils.getDate(sdfDateOnly.parse(dob)));
			bemp.setSsn(ssn);

			//TODO: how do I handle these employee fields?


			/*

			 <EmployeePayrollInfo>
			 <PayPeriod>Biweekly</PayPeriod>

			 <SickHours>
			 <HoursAvailable>PT20H0M0S</HoursAvailable>
			 <AccrualPeriod>BeginningOfYear</AccrualPeriod>
			 <HoursAccrued>PT40H0M0S</HoursAccrued>
			 <MaximumHours>PT0H0M0S</MaximumHours>
			 <IsResettingHoursEachNewYear>true</IsResettingHoursEachNewYear>
			 <HoursUsed>PT0H0M0S</HoursUsed>
			 <AccrualStartDate>2011-11-01</AccrualStartDate>
			 </SickHours>
			 <VacationHours>
			 <HoursAvailable>PT255H30M0S</HoursAvailable>
			 <AccrualPeriod>EveryPaycheck</AccrualPeriod>
			 <HoursAccrued>PT6H45M0S</HoursAccrued>
			 <MaximumHours>PT0H0M0S</MaximumHours>
			 <IsResettingHoursEachNewYear>false</IsResettingHoursEachNewYear>
			 <HoursUsed>PT0H0M0S</HoursUsed>
			 <AccrualStartDate>2011-11-01</AccrualStartDate>
			 </VacationHours>
			 </EmployeePayrollInfo>

			 */


			//insert employee
			if (qpc == null) {
				bemp.insert();
				qpc = new QuickbooksPersonChange();
				qpc.setPerson(bemp.getEmployee());
				qpc.setQbRecordId(listId);
				qpc.setRecordChanged('N');
				qpc.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qpc.generateId();
				hsu.insert(qpc);

				if (DOMUtils.getNode(n, "EmployeePayrollInfo") != null) {
					BHRWage wage = new BHRWage();
					wage.create();
					wage.setEffectiveDate(DateUtils.getDate(sdfDateOnly.parse(DOMUtils.getString(n, "HiredDate"))));

					QuickbooksWageTypeChange qwtc = hsu.createCriteria(QuickbooksWageTypeChange.class)
							.eq(QuickbooksWageTypeChange.QB_ID, DOMUtils.getString(n, "EmployeePayrollInfo/Earnings/PayrollItemWageRef/ListID"))
							.first();

					if (qwtc == null)
						throw new ArahantWarning("There was a problem finding an Arahant Wage Type for an Employee being transferred from QuickBooks.\n\nPlease transfer Wage Types from QuickBooks to Arahant.");

					wage.setWageTypeId(qwtc.getArahantRecordId());

					//  System.out.println(DOMUtils.getDouble(n, "EmployeePayrollInfo/Earnings/Rate"));
					wage.setWageAmount(DOMUtils.getDouble(n, "EmployeePayrollInfo/Earnings/Rate"));

					HrPosition pos = hsu.createCriteria(HrPosition.class).eq(HrPosition.NAME, DOMUtils.getString(n, "EmployeeType")).first();


					if (pos == null) {
						BHRPosition bpos = new BHRPosition();
						bpos.create();
						bpos.setName(DOMUtils.getString(n, "EmployeeType"));
						bpos.insert();
						pos = bpos.getBean();
					}

					wage.setPositionId(pos.getPositionId());
					wage.setNotes("");
					wage.setEmployeeId(bemp.getPersonId());
					wage.insert();
				}
				BHREmplStatusHistory hist = new BHREmplStatusHistory();
				hist.create();
				hist.setEmployeeId(bemp.getPersonId());
				hist.setNotes("Hire Date");
				hist.setEffectiveDate(DateUtils.getDate(sdfDateOnly.parse(DOMUtils.getValue(n, "HiredDate"))));

				//get the first active status
				String statusId = hsu.createCriteria(HrEmployeeStatus.class)
						.selectFields(HrEmployeeStatus.STATUSID)
						.eq(HrEmployeeStatus.ACTIVE, 'Y')
						.orderBy(HrEmployeeStatus.STATUSID)
						.stringVal();

				hist.setStatusId(statusId);
				hist.insert();

				//check for term
				String termDate = DOMUtils.getValue(n, "ReleasedDate");
				if (!isEmpty(termDate)) {
					hist = new BHREmplStatusHistory();
					hist.create();
					hist.setEmployeeId(bemp.getPersonId());
					hist.setNotes("Released Date");
					hist.setEffectiveDate(DateUtils.getDate(sdfDateOnly.parse(DOMUtils.getValue(n, "ReleasedDate"))));

					//get the first inactive status
					statusId = hsu.createCriteria(HrEmployeeStatus.class)
							.selectFields(HrEmployeeStatus.STATUSID)
							.eq(HrEmployeeStatus.ACTIVE, 'N')
							.orderBy(HrEmployeeStatus.STATUSID)
							.stringVal();

					hist.setStatusId(statusId);
					hist.insert();
				}

			} else {
				bemp.update();
				qpc.setRecordChanged('N');
				qpc.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qpc);


				if (DOMUtils.getNode(n, "EmployeePayrollInfo") != null) {

					QuickbooksWageTypeChange qwtc = hsu.createCriteria(QuickbooksWageTypeChange.class)
							.eq(QuickbooksWageTypeChange.QB_ID, DOMUtils.getString(n, "EmployeePayrollInfo/Earnings/PayrollItemWageRef/ListID"))
							.first();

					if (qwtc == null)
						throw new ArahantWarning("There was a problem finding an Arahant Wage Type for an Employee being transferred from QuickBooks.\n\nPlease transfer Wage Types from QuickBooks to Arahant.");

					String wageTypeId = qwtc.getArahantRecordId();

					//  System.out.println(DOMUtils.getDouble(n, "EmployeePayrollInfo/Earnings/Rate"));
					double wageAmount = DOMUtils.getDouble(n, "EmployeePayrollInfo/Earnings/Rate");

					bemp.setWageAndPosition(bemp.getLastPositionId(), wageTypeId, wageAmount, DateUtils.getDate(lastModify));
				}
			}

		}

		return lastDate;

	}

	private String requestAccounts(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<AccountQueryRq requestID=\"1\">"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //          "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</AccountQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	/*
	 String getAccountMod(BGlAccount acct)
	 {
	 String ret="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
	 "<?qbxml version=\"7.0\"?>"+
	 "<QBXML>"+
	 "<QBXMLMsgsRq onError=\"stopOnErrror\"> "+
	 "<AccountModRq> " +
	 "<AccountMod>"+
	 "<ListID >"+acct.getAccountNumber()+"</ListID>"+
	 "<EditSequence >STRTYPE</EditSequence> "+  //This is a seqno I need to get via query
	 "<Name >"+acct.getAccountName()+"</Name>"+

	 //<!-- AccountType may have one of the following values: AccountsPayable, AccountsReceivable, Bank, CostOfGoodsSold, CreditCard, Equity, Expense, FixedAsset, Income, LongTermLiability, NonPosting, OtherAsset, OtherCurrentAsset, OtherCurrentLiability, OtherExpense, OtherIncome -->
	 "<AccountType >"+acct.getQuickbooksAccountType()+"</AccountType>"+
	 "<Desc >"+acct.getAccountName()+"</Desc>"+
	 "</AccountModRq>"+
	 "</QBXMLMsgsRq>"+
	 "/<QBXML>";

	 return ret;
	 }

	 String getAccountAdd(BGlAccount acct)
	 {
	 String ret="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
	 "<?qbxml version=\"7.0\"?>"+
	 "<QBXML>"+
	 "<QBXMLMsgsRq onError=\"stopOnErrror\"> "+
	 "<AccountAddRq> " +
	 "<AccountAdd>"+
	 "<ListID >"+acct.getAccountNumber()+"</ListID>"+
	 "<Name >"+acct.getAccountName()+"</Name>"+
	 "<AccountType >"+acct.getQuickbooksAccountType()+"</AccountType>"+
	 "<Desc >"+acct.getAccountName()+"</Desc>"+
	 "</AccountAdd>" +
	 "</AccountAddRq>"+
	 "</QBXMLMsgsRq>"+
	 "/<QBXML>";

	 return ret;
	 }
	 */
	String requestPayrollData(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<PayrollDetailReportQueryRq requestID=\"2\" >"
				+ "<PayrollDetailReportType>PayrollTransactionDetail </PayrollDetailReportType>"
				+ "<ReportPeriod><FromReportDate>" + sdfDateOnly.format(startDate) + "</FromReportDate></ReportPeriod>"
				+ "</PayrollDetailReportQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestWageTypes(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<PayrollItemWageQueryRq requestID=\"2\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //         "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</PayrollItemWageQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestPayrollItems(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<PayrollItemNonWageQueryRq requestID=\"2\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //        "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</PayrollItemNonWageQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestJobTypes(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<JobTypeQueryRq requestID=\"2\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //         "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</JobTypeQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestServices(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<ItemServiceQueryRq requestID=\"2\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //       "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</ItemServiceQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestClients(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<CustomerQueryRq requestID=\"3\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //       "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</CustomerQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestVendors(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<VendorQueryRq requestID=\"3\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //    "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</VendorQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestEmployees(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<EmployeeQueryRq requestID=\"3\" >"
				+ "<ActiveStatus>All</ActiveStatus>"
				+ //       "<FromModifiedDate>"+sdf.format(startDate).replace(' ', 'T')+"</FromModifiedDate>"+
				"</EmployeeQueryRq>"
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";
	}

	String requestCompanies(Date startDate) {
		return "<?xml version=\"1.0\" ?><?qbxml version=\"2.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError=\"stopOnError\">"
				+ "<CompanyQueryRq requestID=\"1\" />"
				+ //   "</CompanyQueryRq>" +
				"</QBXMLMsgsRq>"
				+ "</QBXML>";
	}
	
	final static String requestInvoices = "<?xml version=\"1.0\" ?><?qbxml version=\"7.0\"?>"
			+ "<QBXML>"
			+ "<QBXMLMsgsRq onError=\"stopOnError\">"
			+ "<InvoiceQueryRq requestID=\"3\" />"
			+ "</QBXMLMsgsRq>"
			+ "</QBXML>";

	private Date parseVendors(String vendorXML) throws Exception {
		//System.out.println(vendorXML);

		Date lastDate = new Date(0);
		Document doc = DOMUtils.createDocument(vendorXML);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/VendorQueryRs/VendorRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				Date lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			if (isEmpty(name))
				continue;

			//see if the account exists
			QuickbooksVendorChange chng = ArahantSession.getHSU().createCriteria(QuickbooksVendorChange.class)
					.eq(QuickbooksVendorChange.QB_ID, DOMUtils.getValue(n, "ListID"))
					.first();

			VendorCompany vendor = null;

			if (chng != null)
				vendor = chng.getVendor();

			BVendorCompany bcc = null;
			if (vendor == null) {
				vendor = hsu.createCriteria(VendorCompany.class)
						.eq(VendorCompany.NAME, name)
						.first();

				if (vendor != null) {
					bcc = new BVendorCompany(vendor);
					chng = new QuickbooksVendorChange();
					chng.setVendor(bcc.getBean());
					chng.setQbRecordId(DOMUtils.getValue(n, "ListID"));
					chng.setRecordChanged('N');
					chng.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
					chng.generateId();
					hsu.insert(chng);
				} else if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("true")) {
					bcc = new BVendorCompany();
					bcc.create();
				}
			} else
				bcc = new BVendorCompany(vendor);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				try {
					if (bcc != null)
						bcc.delete();
				} catch (Exception e) {
					//continue - must be being used
				}
				continue;

			}
			//bcc.setIdentifier(DOMUtils.getValue(n,"ListID"));

			bcc.setName(name);

			bcc.setStreet(DOMUtils.unescapeText(DOMUtils.getValue(n, "VendorAddress/Addr1")));
			bcc.setStreet2(DOMUtils.unescapeText(DOMUtils.getValue(n, "VendorAddress/Addr2")));
			bcc.setCity(DOMUtils.unescapeText(DOMUtils.getValue(n, "VendorAddress/City")));
			bcc.setState(DOMUtils.unescapeText(DOMUtils.getValue(n, "VendorAddress/State")));
			bcc.setZip(DOMUtils.getValue(n, "VendorAddress/PostalCode"));

			if (vendor == null)
				bcc.insert();
			else
				bcc.update();

			bcc.setMainContactFname(DOMUtils.unescapeText(DOMUtils.getValue(n, "FirstName")));
			bcc.setMainContactLname(DOMUtils.unescapeText(DOMUtils.getValue(n, "LastName")));

			if (isEmpty(bcc.getMainContactLname()))
				bcc.setMainContactLname(".");
			if (isEmpty(bcc.getMainContactFname()))
				bcc.setMainContactFname(".");

			bcc.setMainPhoneNumber(DOMUtils.getValue(n, "Phone"));
			bcc.setMainFaxNumber(DOMUtils.getValue(n, "Fax"));

			bcc.setMainContactWorkPhone(DOMUtils.getValue(n, "Phone"));
			bcc.setMainContactWorkFax(DOMUtils.getValue(n, "Fax"));
			bcc.setMainContactPersonalEmail(DOMUtils.unescapeText(DOMUtils.getValue(n, "Email")));

			bcc.update();

			QuickbooksVendorChange qac = hsu.createCriteria(QuickbooksVendorChange.class)
					.eq(QuickbooksVendorChange.VENDOR, bcc.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksVendorChange();
				qac.setVendor(bcc.getBean());
				qac.setQbRecordId(DOMUtils.getValue(n, "ListID"));
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		return lastDate;

	}

	private Date parseCompanies(String companyXML) throws Exception {
		// System.out.println(clientsXML);


		Date lastDate = new Date(0);

		Document doc = DOMUtils.createDocument(companyXML);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CompanyQueryRs/CompanyRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			lastDate = new Date();


			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "CompanyName"));



			// System.out.println(accountType);


			CompanyDetail company = hsu.getCurrentCompany();

			BCompany bcc = new BCompany(company);

			bcc.setName(name);

			bcc.setStreet(DOMUtils.unescapeText(DOMUtils.getValue(n, "Address/Addr1")));
			bcc.setStreet2(DOMUtils.unescapeText(DOMUtils.getValue(n, "Address/Addr2")));
			bcc.setCity(DOMUtils.unescapeText(DOMUtils.getValue(n, "Address/City")));
			bcc.setState(DOMUtils.unescapeText(DOMUtils.getValue(n, "Address/State")));
			bcc.setZip(DOMUtils.getValue(n, "Address/PostalCode"));

			bcc.setFederalEmployerId(DOMUtils.getValue(n, "EIN"));
			bcc.setMainPhoneNumber(DOMUtils.getValue(n, "Phone"));
			bcc.setMainFaxNumber(DOMUtils.getValue(n, "Fax"));

			bcc.update();

			QuickbooksCompanyChange qac = hsu.createCriteria(QuickbooksCompanyChange.class)
					.eq(QuickbooksCompanyChange.COMPANY, hsu.getCurrentCompany())
					.first();

			if (qac == null) {
				qac = new QuickbooksCompanyChange();
				qac.setCompany(bcc.getBean());
				qac.setQbRecordId(DOMUtils.getValue(n, "ListID"));
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		return lastDate;
	}

	private Date parseClients(String clientsXML) throws Exception {
		// System.out.println(clientsXML);

		Date lastDate = new Date(0);
		Document doc = DOMUtils.createDocument(clientsXML);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerQueryRs/CustomerRet");

		System.out.println(nl.getLength());

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			//if this is a project - to them a sub-client, skip it

			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "FullName"));

			System.out.println(loop + " " + name);

			if (DOMUtils.getElement(n, "ParentRef") != null)
				continue;

			Date lastModify = new Date();

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			// System.out.println(accountType);

			//see if the account exists
			QuickbooksClientChange chng = ArahantSession.getHSU().createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksClientChange.QB_ID, DOMUtils.getValue(n, "ListID"))
					.first();

			ClientCompany client = null;

			if (chng != null)
				client = chng.getClient();

			BClientCompany bcc;
			if (client == null) {
				//check by name
				client = hsu.createCriteria(ClientCompany.class)
						.eq(ClientCompany.NAME, name)
						.first();

				if (client != null) {
					bcc = new BClientCompany(client);
					chng = new QuickbooksClientChange();
					chng.setClient(bcc.getBean());
					chng.setQbRecordId(DOMUtils.getValue(n, "ListID"));
					chng.setRecordChanged('N');
					chng.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
					chng.generateId();
					hsu.insert(chng);
				} else {
					bcc = new BClientCompany();
					bcc.create();
				}
			} else
				bcc = new BClientCompany(client);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				if (bcc.getInactiveDate() == 0)
					bcc.setInactiveDate(DateUtils.getDate(lastModify));
			} else
				bcc.setInactiveDate(0);

			//  bcc.setIdentifier();

			bcc.setName(name);

			bcc.setStreet(DOMUtils.unescapeText(DOMUtils.getValue(n, "BillAddress/Addr1")));
			bcc.setStreet2(DOMUtils.unescapeText(DOMUtils.getValue(n, "BillAddress/Addr2")));
			bcc.setCity(DOMUtils.unescapeText(DOMUtils.getValue(n, "BillAddress/City")));
			bcc.setState(DOMUtils.unescapeText(DOMUtils.getValue(n, "BillAddress/State")));
			bcc.setZip(DOMUtils.getValue(n, "BillAddress/PostalCode"));

			if (client == null)
				bcc.insert();
			else
				bcc.update();

			//       System.out.println("Here "+ DOMUtils.unescapeText(DOMUtils.getValue(n, "FirstName")));

			//       if (name.startsWith("Ab"))
			//           System.out.println("Here "+ DOMUtils.unescapeText(DOMUtils.getValue(n, "FirstName")));

			if (!isEmpty(DOMUtils.unescapeText(DOMUtils.getValue(n, "LastName")))) {
				bcc.setMainContactFname(DOMUtils.unescapeText(DOMUtils.getValue(n, "FirstName")));
				bcc.setMainContactLname(DOMUtils.unescapeText(DOMUtils.getValue(n, "LastName")));
				bcc.setMainContactWorkPhone(DOMUtils.getValue(n, "Phone"));
				bcc.setMainContactPersonalEmail(DOMUtils.unescapeText(DOMUtils.getValue(n, "Email")));
				bcc.getMainContact().setRecordType('R');
				bcc.update();
			}
			QuickbooksClientChange qac = hsu.createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksClientChange.CLIENT, bcc.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksClientChange();
				qac.setClient(bcc.getBean());
				qac.setQbRecordId(DOMUtils.getValue(n, "ListID"));
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}


		}

		return lastDate;

	}

	private Date parseProjects(String clientsXML) throws Exception {
		// System.out.println(clientsXML);

		Date lastDate = new Date(0);
		Document doc = DOMUtils.createDocument(clientsXML);

		String defaultProjectCategoryId = BProperty.get("DefaultProjectCategoryId");
		String defaultRouteId = BProperty.get("DefaultProjectRouteId");
		String defaultProjectTypeId = BProperty.get("DefaultProjectTypeId");
		String defaultProjectStatusId = BProperty.get("DefaultProjectStatusId");

		if (isEmpty(defaultProjectCategoryId) && (hsu.createCriteria(ProjectCategory.class).count() == 1))
			defaultProjectCategoryId = hsu.getFirst(ProjectCategory.class).getProjectCategoryId();

		if (isEmpty(defaultProjectCategoryId)) {
			BProjectCategory cat = new BProjectCategory();
			defaultProjectCategoryId = cat.create();
			cat.setCode("Default");
			cat.setDescription("Default Project Category");
			cat.setScope(ProjectCategory.SCOPE_GLOBAL);
			cat.insert();
			BProperty prop = new BProperty("DefaultProjectCategoryId");
			prop.setDescription("Default Project Category");
			prop.setValue(defaultProjectCategoryId);
			prop.update();
			//  throw new ArahantWarning("Please set up a default Project Category in the System Properties - DefaultProjectCategoryId");
		}

		if (isEmpty(defaultProjectTypeId) && (hsu.createCriteria(ProjectType.class).count() == 1))
			defaultProjectTypeId = hsu.getFirst(ProjectType.class).getProjectTypeId();

		if (isEmpty(defaultProjectTypeId)) {
			ProjectType pt = hsu.createCriteria(ProjectType.class)
					.eq(ProjectType.CODE, "Default QB Type")
					.first();

			if (pt != null)
				defaultProjectTypeId = pt.getProjectTypeId();
			else {
				BProjectType type = new BProjectType();
				defaultProjectTypeId = type.create();
				type.setCode("Default QB Type");
				type.setDescription("Default Project Type");
				type.setScope(ProjectType.SCOPE_GLOBAL);
				type.insert();
			}
			BProperty prop = new BProperty("DefaultProjectTypeId");
			prop.setDescription("Default Project Type");
			prop.setValue(defaultProjectTypeId);
			prop.update();
			//throw new ArahantWarning("Please set up a default Project Type in the System Properties - DefaultProjectTypeId");
		}


		BRouteTypeAssoc rta = new BRouteTypeAssoc(defaultProjectCategoryId, defaultProjectTypeId);

		if (!isEmpty(rta.getRouteId())) {
			BRoute rt = new BRoute(rta.getRouteId());
			if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
				defaultRouteId = rt.getRouteId();
				defaultProjectStatusId = rt.getProjectStatusId();
			}
		}

		if (isEmpty(defaultProjectStatusId) && (hsu.createCriteria(ProjectStatus.class).count() == 1))
			defaultProjectStatusId = hsu.getFirst(ProjectStatus.class).getProjectStatusId();

		if (isEmpty(defaultProjectStatusId)) {
			BProjectStatus stat = new BProjectStatus();
			defaultProjectStatusId = stat.create();
			stat.setActive('Y');
			stat.setCode("Default");
			stat.setDescription("Default Project Status");
			stat.insert();
			BProperty prop = new BProperty("DefaultProjectStatusId");
			prop.setDescription("Default Project Status");
			prop.setValue(defaultProjectStatusId);
			prop.update();
			//   throw new ArahantWarning("Please set up a default Project Status in the System Properties - DefaultProjectStatusId");
		}


		if (isEmpty(defaultRouteId) && (hsu.createCriteria(Route.class).count() == 1))
			defaultRouteId = hsu.getFirst(Route.class).getRouteId();


		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/CustomerQueryRs/CustomerRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			//if this is a project - to them a sub-client, skip it

			if (DOMUtils.getElement(n, "ParentRef") == null)
				continue;

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				Date lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}

			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "FullName"));

			name = name.substring(name.indexOf(':') + 1);

			// System.out.println(accountType);

			//see if the project exists
			QuickbooksProjectChange chng = ArahantSession.getHSU().createCriteria(QuickbooksProjectChange.class)
					.eq(QuickbooksProjectChange.QB_ID, DOMUtils.getValue(n, "ListID"))
					.first();

			Project project = null;

			if (chng != null)
				project = chng.getProject();

			BProject bcc = null;
			if (project == null) {
				if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("true")) {
					bcc = new BProject();
					bcc.create();
				}
			} else
				bcc = new BProject(project);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				try {
					if (bcc != null)
						bcc.delete();
				} catch (Exception e) {
					//continue - must be being used
				}
				continue;

			}
			//  bcc.setIdentifier();


			bcc.setDescription(name);
			bcc.setDetailDesc(DOMUtils.getString(n, "JobDesc"));

			QuickbooksProjectTypeChange qptc = hsu.createCriteria(QuickbooksProjectTypeChange.class)
					.eq(QuickbooksProjectTypeChange.QB_ID, DOMUtils.getString(n, "JobTypeRef/ListID"))
					.first();

			String projectTypeId;

			if (qptc == null) {
				//is my parent ref a project?
				projectTypeId = defaultProjectTypeId;

				if (isEmpty(projectTypeId))
					continue;
			} else
				projectTypeId = qptc.getArahantRecordId();

			bcc.setProjectTypeId(projectTypeId);


			QuickbooksClientChange qcc = hsu.createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksClientChange.QB_ID, DOMUtils.getString(n, "ParentRef/ListID"))
					.first();

			if (qcc == null)
				throw new ArahantWarning("Please synchronize Clients first.");

			String orgid = qcc.getClient()
					.getOrgGroupId();

			bcc.setRequestingOrgGroupId(orgid);

			if (project == null) {
				bcc.setOrgGroupId(orgid);
				bcc.setProjectCategoryId(defaultProjectCategoryId);
				bcc.calculateRouteAndStatus();

				BRouteTypeAssoc rta2 = new BRouteTypeAssoc(bcc.getProjectCategoryId(), bcc.getProjectTypeId());

				if (!isEmpty(rta2.getRouteId())) {
					BRoute rt = new BRoute(rta2.getRouteId());
					if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
						bcc.setRouteId(rt.getRouteId());
						bcc.setProjectStatusId(rt.getProjectStatusId());
					}
				}

				if (isEmpty(defaultRouteId) && isEmpty(bcc.getRouteId()))
					throw new ArahantWarning("A Project Route could not be determined from the project information being transferred from QuickBooks.  This is usually because a Client job was not assigned a Job Type in QuickBooks.\n\nPlease identify a default Project Route in the Arahant System Properties (DefaultProjectRouteId) to get around this problem.");

				if (isEmpty(bcc.getRouteId()))
					bcc.setRouteId(defaultRouteId);

				if (isEmpty(bcc.getProjectStatusId()))
					bcc.setProjectStatusId(defaultProjectStatusId);

				bcc.setBillable(Project.BILLABLE_TYPE_BILLABLE);
				bcc.insert();
			} else
				bcc.update();


			bcc.update();

			QuickbooksProjectChange qac = hsu.createCriteria(QuickbooksProjectChange.class)
					.eq(QuickbooksProjectChange.PROJECT, bcc.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksProjectChange();
				qac.setProject(bcc.getBean());
				qac.setQbRecordId(DOMUtils.getValue(n, "ListID"));
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		return lastDate;

	}

	private String getInvoiceXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">";

		//for every non-exported invoice
		int request = 1;

		for (Invoice invoice : hsu.createCriteria(Invoice.class)
				.isNull(Invoice.EXPORTDATE)
				.list()) {

			BInvoice inv = new BInvoice(invoice);
			
			inv.setExportDate(new Date());

			String refNum = inv.getAccountingInvoiceId().trim();
			if (refNum.length() > 11)
				refNum = refNum.substring(refNum.length() - 11);

			ret += "<InvoiceAddRq requestID = \"" + (request++) + "\"><InvoiceAdd>";
			
			ret += "<CustomerRef><ListID>" + inv.getCustomerAcctId() + "</ListID></CustomerRef>";
			
			ret += "<TxnDate>" + formatDateOnly(inv.getDate()) + "</TxnDate>";
			
			ret += "<RefNumber>" + refNum + "</RefNumber>";
			
			String po = inv.getPurchaseOrder();
			if (po != null  &&  po.length() > 0)
				ret += "<PONumber >" + DOMUtils.escapeText(po) + "</PONumber>";
			
			int terms = inv.getPaymentTerms();
			ret += "<TermsRef><FullName>";
			if (terms == 0)
				ret += "Due on receipt";
			else
				ret += "Net " + terms;
			ret += "</FullName></TermsRef>";
			
			ret += "<Memo>" + DOMUtils.escapeText(inv.getDescription()) + "</Memo>";

			for (BInvoiceLineItem li : inv.getLineItems()) {
				ret += "<InvoiceLineAdd>"
						+ "<ItemRef>"
						+ "<ListID>" + li.getProductServiceAcctId() + "</ListID>"
						+ "</ItemRef>"
						+ "<Desc>" + DOMUtils.escapeText(li.getDescription()) + "</Desc>";

				if (li.isFlatAmount())
					ret += "<Quantity>1</Quantity>"
							+ "<Rate>" + li.getAmount() + "</Rate>";
				else
					ret += "<Quantity>" + Utils.Format(li.getUnits(), "", 0, 2) + "</Quantity>"
							+ "<Rate>" + Utils.Format(li.getUnitPrice(), "", 0, 4) + "</Rate>";
				ret += "</InvoiceLineAdd>";
			}

			ret += "</InvoiceAdd></InvoiceAddRq>";
		}

		ret += " </QBXMLMsgsRq>"
				+ "</QBXML>";

		//   System.out.println(ret);
		return ret;
	}

	private Date parseServices(String servicesXML) throws Exception {
		//System.out.println(servicesXML);
		Document doc = DOMUtils.createDocument(servicesXML);

		Date lastDate = new Date(0);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/ItemServiceQueryRs/ItemServiceRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				Date lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String serviceName = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			if (serviceName.length() > 30)
				serviceName = serviceName.substring(0, 30);



			String accountId = DOMUtils.getValue(n, "SalesOrPurchase/AccountRef/ListID");

			String listId = DOMUtils.getValue(n, "ListID");
			// System.out.println(accountType);

			QuickbooksProductChange qbp = hsu.createCriteria(QuickbooksProductChange.class)
					.eq(QuickbooksProductChange.QB_ID, listId)
					.first();

			//see if the account exists
			Service prod = null;

			if (qbp != null)
				prod = qbp.getProduct();

			BService p = null;
			if (prod == null) {
				if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("true")) {
					p = new BService();
					p.create();
				}
			} else
				p = new BService(prod);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				try {
					if (p != null)
						p.delete();
				} catch (Exception e) {
					//continue - must be being used
				}
				continue;
			}


			if (!isEmpty(accountId)) {
				QuickbooksAccountChange qbac = hsu.createCriteria(QuickbooksAccountChange.class)
						.eq(QuickbooksAccountChange.QB_ID, accountId)
						.first();

				if (qbac != null)
					p.setAccount(qbac.getAccount());
				//        else
				//            throw new ArahantWarning("Please synchronize GL Accounts first.");
			}

			p.setProductType(0); //TODO: do I need this?
			p.setDescription(serviceName);
			p.setAccsysId(listId);

			if (prod == null)
				p.insert();
			else
				p.update();

			QuickbooksProductChange qac = hsu.createCriteria(QuickbooksProductChange.class)
					.eq(QuickbooksProductChange.PRODUCT, p.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksProductChange();
				qac.setProduct(p.getBean());
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}

		}

		return lastDate;

	}

	private Date parseWageTypes(String servicesXML) throws Exception {
		//System.out.println(servicesXML);
		Document doc = DOMUtils.createDocument(servicesXML);

		Date lastDate = new Date(0);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/PayrollItemWageQueryRs/PayrollItemWageRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			Date lastModify = new Date();
			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String wageName = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			String accountId = DOMUtils.getValue(n, "ExpenseAccountRef/ListID");

			String listId = DOMUtils.getValue(n, "ListID");
			// System.out.println(accountType);

			QuickbooksWageTypeChange qbp = hsu.createCriteria(QuickbooksWageTypeChange.class)
					.eq(QuickbooksWageTypeChange.QB_ID, listId)
					.first();

			//see if the account exists
			WageType wt = null;

			if (qbp != null)
				wt = qbp.getWageType();


			if (wt == null) {
				//see if I already have it by name
				wt = hsu.createCriteria(WageType.class)
						.eq(WageType.NAME, wageName)
						.first();

				if (wt != null) {
					qbp = new QuickbooksWageTypeChange();
					qbp.setWageType(wt);
					qbp.setQbRecordId(listId);
					qbp.setRecordChanged('N');
					qbp.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
					qbp.generateId();
					hsu.insert(qbp);
				}
			}

			BWageType p;
			if (wt == null) {
				p = new BWageType();
				p.create();
			} else
				p = new BWageType(wt);


			QuickbooksAccountChange qbac = hsu.createCriteria(QuickbooksAccountChange.class)
					.eq(QuickbooksAccountChange.QB_ID, accountId)
					.first();

			if (qbac != null)
				p.setExpenseAccount(qbac.getAccount());

			p.setName(wageName);

			String type = DOMUtils.getValue(n, "WageType");

			if (type.equals("Bonus")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_BONUS);
			}

			if (type.equals("Commission")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_COMMISSION);
			}

			if (type.equals("HourlyOvertime")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_OVERTIME);
			}

			if (type.equals("HourlyRegular")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_HOURLY);
				p.setType(WageType.TYPE_REGULAR);
			}

			if (type.equals("HourlySick")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_HOURLY);
				p.setType(WageType.TYPE_SICK);
			}

			if (type.equals("HourlyVacation")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_HOURLY);
				p.setType(WageType.TYPE_VACATION);
			}

			if (type.equals("SalaryRegular")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_SALARY);
				p.setType(WageType.TYPE_REGULAR);
			}

			if (type.equals("SalarySick")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_SALARY);
				p.setType(WageType.TYPE_SICK);
			}

			if (type.equals("SalaryVacation")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_SALARY);
				p.setType(WageType.TYPE_VACATION);
			}

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				if (p.getLastActiveDate() == 0)
					p.setLastActiveDate(DateUtils.getDate(lastModify));
			} else
				p.setLastActiveDate(0);

			if (wt == null)
				p.insert();
			else
				p.update();

			QuickbooksWageTypeChange qac = hsu.createCriteria(QuickbooksWageTypeChange.class)
					.eq(QuickbooksWageTypeChange.WAGE_TYPE, p.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksWageTypeChange();
				qac.setWageType(p.getBean());
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}

		}

		return lastDate;

	}

	private Date parsePayrollItems(String servicesXML) throws Exception {
		//System.out.println(servicesXML);
		Document doc = DOMUtils.createDocument(servicesXML);

		Date lastDate = new Date(0);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/PayrollItemNonWageQueryRs/PayrollItemNonWageRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			Date lastModify = new Date();
			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String wageName = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			String listId = DOMUtils.getValue(n, "ListID");
			// System.out.println(accountType);

			QuickbooksWageTypeChange qbp = hsu.createCriteria(QuickbooksWageTypeChange.class)
					.eq(QuickbooksWageTypeChange.QB_ID, listId)
					.first();

			//see if the type exists
			WageType wt = null;

			if (qbp != null)
				wt = qbp.getWageType();

			BWageType p;
			if (wt == null) {
				p = new BWageType();
				p.create();
			} else
				p = new BWageType(wt);

			String accountId = DOMUtils.getValue(n, "ExpenseAccountRef/ListID");
			if (!isEmpty(accountId)) {
				QuickbooksAccountChange qbac = hsu.createCriteria(QuickbooksAccountChange.class)
						.eq(QuickbooksAccountChange.QB_ID, accountId)
						.first();

				if (qbac != null)
					p.setExpenseAccount(qbac.getAccount());
				else
					throw new ArahantWarning("Please synchronize GL Accounts and retry.");

			}

			accountId = DOMUtils.getValue(n, "LiabilityAccountRef/ListID");
			if (!isEmpty(accountId)) {
				QuickbooksAccountChange qbac = hsu.createCriteria(QuickbooksAccountChange.class)
						.eq(QuickbooksAccountChange.QB_ID, accountId)
						.first();

				if (qbac != null)
					p.setLiabilityAccount(qbac.getAccount());
				else
					throw new ArahantWarning("Please synchronize GL Accounts and retry.");

			}


			p.setName(wageName);

			String type = DOMUtils.getValue(n, "NonWageType");

			if (type.equals("Addition")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_ADDITION);
			}

			if (type.equals("Deduction")) {
				p.setIsDeduction(true);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_DEDUCTION);
			}

			if (type.equals("CompanyContribution")) {
				p.setIsDeduction(false);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_COMPANY_CONTRIBUTION);
			}

			if (type.equals("Tax")) {
				p.setIsDeduction(true);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_TAX);
			}

			if (type.equals("DirectDeposit")) {
				p.setIsDeduction(true);
				p.setPeriodType(WageType.PERIOD_ONE_TIME);
				p.setType(WageType.TYPE_DIRECT_DEPOSIT);
			}

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				if (p.getLastActiveDate() == 0)
					p.setLastActiveDate(DateUtils.getDate(lastModify));
			} else
				p.setLastActiveDate(0);

			if (wt == null)
				p.insert();
			else
				p.update();

			QuickbooksWageTypeChange qac = hsu.createCriteria(QuickbooksWageTypeChange.class)
					.eq(QuickbooksWageTypeChange.WAGE_TYPE, p.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksWageTypeChange();
				qac.setWageType(p.getBean());
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}

		}

		return lastDate;

	}

	private Date parseJobTypes(String servicesXML) throws Exception {
		//System.out.println(servicesXML);
		Document doc = DOMUtils.createDocument(servicesXML);

		Date lastDate = new Date(0);

		//find all account rets

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/JobTypeQueryRs/JobTypeRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				Date lastModify = sdf.parse(time);

				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}


			String name = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			String listId = DOMUtils.getValue(n, "ListID");
			// System.out.println(accountType);

			QuickbooksProjectTypeChange qbp = hsu.createCriteria(QuickbooksProjectTypeChange.class)
					.eq(QuickbooksProjectTypeChange.QB_ID, listId)
					.first();

			//see if the account exists
			ProjectType prod = null;

			if (qbp != null)
				prod = qbp.getProjectType();

			BProjectType p = null;
			if (prod == null) {
				prod = hsu.createCriteria(ProjectType.class)
						.eq(ProjectType.CODE, name)
						.first();

				if (prod != null) {
					p = new BProjectType(prod);
					qbp = new QuickbooksProjectTypeChange();
					qbp.setProjectType(p.getBean());
					qbp.setQbRecordId(listId);
					qbp.setRecordChanged('N');
					qbp.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
					qbp.generateId();
					hsu.insert(qbp);
				} else if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("true")) {
					p = new BProjectType();
					p.create();
				}
			} else
				p = new BProjectType(prod);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				try {
					if (p != null)
						p.delete();
				} catch (Exception e) {
					//continue - must be being used
				}
				continue;

			}
			p.setCode(name);
			p.setDescription(DOMUtils.unescapeText(DOMUtils.getValue(n, "FullName")));
			p.setScope("G");

			if (prod == null)
				p.insert();
			else
				p.update();

			QuickbooksProjectTypeChange qac = hsu.createCriteria(QuickbooksProjectTypeChange.class)
					.eq(QuickbooksProjectTypeChange.PROJECT_TYPE, p.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksProjectTypeChange();
				qac.setProjectType(p.getBean());
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}

		}
		return lastDate;
	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");

	private String formatDate(int date) {
		return sdf.format(DateUtils.getDate(date)).replace(' ', 'T');
	}
	
	private String formatDateOnly(int date) {
		return sdfDateOnly.format(DateUtils.getDate(date));
	}

	private Date parseAccounts(String accountsXML) throws Exception {
		Document doc = DOMUtils.createDocument(accountsXML);

		//find all account rets

		Date lastDate = new Date(0);

		NodeList nl = DOMUtils.getNodes(doc, "QBXML/QBXMLMsgsRs/AccountQueryRs/AccountRet");

		for (int loop = 0; loop < nl.getLength(); loop++) {
			Node n = nl.item(loop);

			try {
				String time = DOMUtils.getValue(n, "TimeModified");

				time = time.replace('T', ' ').substring(0, time.length() - 6);

				Date lastModify = sdf.parse(time);


				if (lastModify.after(lastDate))
					lastDate = lastModify;
			} catch (Exception e) {
				//just keep going
				logger.error(e);
			}



			String accountName = DOMUtils.unescapeText(DOMUtils.getValue(n, "Name"));

			if (accountName.length() > 30)
				accountName = accountName.substring(0, 30);

			String accountType = DOMUtils.unescapeText(DOMUtils.getValue(n, "AccountType"));
			String accountNumber = DOMUtils.getValue(n, "AccountNumber");
			boolean defaultFlag = false;

			// System.out.println(accountType);

			//see if the account exists
			GlAccount gl = ArahantSession.getHSU().createCriteria(GlAccount.class)
					.eq(GlAccount.ACCOUNTNUMBER, accountNumber)
					.first();

			BGlAccount gla = null;
			if (gl == null) {
				if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("true")) {
					gla = new BGlAccount();
					gla.create();
				}
			} else
				gla = new BGlAccount(gl);

			if (DOMUtils.getValue(n, "IsActive").equalsIgnoreCase("false")) {
				try {
					if (gla != null)
						gla.delete();
				} catch (Exception e) {
					//continue - must be being used
				}
				continue;

			}

			gla.setAccountName(accountName);
			gla.setAccountNumber(accountNumber);
			gla.setAccountType(accountType);
			gla.setDefaultFlag(defaultFlag);

			if (gl == null)
				gla.insert();
			else
				gla.update();

			String listId = DOMUtils.getValue(n, "ListID");

			// sd.accountMap.put(listId, gla.getGlAccountId());
			QuickbooksAccountChange qac = hsu.createCriteria(QuickbooksAccountChange.class)
					.eq(QuickbooksAccountChange.ACCOUNT, gla.getBean())
					.first();

			if (qac == null) {
				qac = new QuickbooksAccountChange();
				qac.setAccount(gla.getBean());
				qac.setQbRecordId(listId);
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				qac.generateId();
				hsu.insert(qac);
			} else {
				qac.setRecordChanged('N');
				qac.setQbRecordRevision(DOMUtils.getInt(n, "EditSequence"));
				hsu.saveOrUpdate(qac);
			}
		}

		return lastDate;

	}

	private void updateQuickbooksSync(String fieldName, Date lastDate, char direction) {
		short field = (direction == QuickbooksSync.TYPE_UP) ? getUpFieldCode(fieldName) : getDownFieldCode(fieldName);

		QuickbooksSync qs = hsu.createCriteria(QuickbooksSync.class)
				.eq(QuickbooksSync.FIELD_CODE, field)
				.eq(QuickbooksSync.DIRECTION, direction)
				.first();

		if (qs == null) {
			qs = new QuickbooksSync();
			qs.generateId();
			qs.setCompany(hsu.getCurrentCompany());
			qs.setDirection(direction);
			qs.setInterfaceCode(field);
			qs.setInterfaceTime(lastDate);
			hsu.insert(qs);
		} else {
			qs.setInterfaceTime(lastDate);
			hsu.saveOrUpdate(qs);
		}
	}

	private String getEmployeeXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		//Find employees without qb id's

		for (QuickbooksPersonChange pcs : hsu.createCriteria(QuickbooksPersonChange.class).isNull(QuickbooksPersonChange.QB_ID).list()) {

			BPerson bp = new BPerson(pcs.getPerson());

			if (!bp.isEmployee())
				continue;

			BEmployee bemp = bp.getBEmployee();
			ret += "<EmployeeAddRq requestID = \"" + (++count) + "\"><EmployeeAdd>";
			ret = getEmployeeBodyXML(ret, bemp, false);

			ret += "</EmployeeAdd></EmployeeAddRq>";
		}


		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksPersonChange.class)
				.selectFields(QuickbooksPersonChange.ARAHANT_ID)
				.list();

		//add employees that aren't in change table
		for (Employee emp : hsu.createCriteria(Employee.class).notIn(Employee.PERSONID, ids).list()) {
			BEmployee bemp = new BEmployee(emp);
			ret += "<EmployeeAddRq requestID = \"" + (++count) + "\"><EmployeeAdd>";
			ret = getEmployeeBodyXML(ret, bemp, false);

			ret += "</EmployeeAdd></EmployeeAddRq>";
		}

		for (QuickbooksPersonChange pcs : hsu.createCriteria(QuickbooksPersonChange.class)
				.isNotNull(QuickbooksPersonChange.QB_ID)
				.eq(QuickbooksPersonChange.RECORD_CHANGED, 'Y')
				.list()) {

			BPerson bp = new BPerson(pcs.getPerson());

			if (!bp.isEmployee())
				continue;

			BEmployee bemp = bp.getBEmployee();
			ret += "<EmployeeModRq requestID = \"" + (++count) + "\"><EmployeeMod>";
			ret += "<ListID>" + pcs.getQbRecordId() + "</ListID>"
					+ "<EditSequence>" + pcs.getQbRecordRevision() + "</EditSequence>";
			ret = getEmployeeBodyXML(ret, bemp, true);

			ret += "</EmployeeMod></EmployeeModRq>";
		}

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";


		return ret;
	}

	private String getVendorXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		//Find vendors without qb id's

		for (QuickbooksVendorChange pcs : hsu.createCriteria(QuickbooksVendorChange.class).isNull(QuickbooksVendorChange.QB_ID).list()) {


			ret += "<VendorAddRq requestID = \"" + (++count) + "\"><VendorAdd>";
			ret = getVendorBodyXML(ret, new BVendorCompany(pcs.getVendor()), false);
			ret += "</VendorAdd></VendorAddRq>";
		}


		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksVendorChange.class)
				.selectFields(QuickbooksVendorChange.ARAHANT_ID)
				.list();

		//add vendors that aren't in change table
		for (VendorCompany vndr : hsu.createCriteria(VendorCompany.class).notIn(VendorCompany.ORGGROUPID, ids).list()) {
			ret += "<VendorAddRq requestID = \"" + (++count) + "\"><VendorAdd>";
			ret = getVendorBodyXML(ret, new BVendorCompany(vndr), false);
			ret += "</VendorAdd></VendorAddRq>";
		}

		for (QuickbooksVendorChange pcs : hsu.createCriteria(QuickbooksVendorChange.class)
				.isNotNull(QuickbooksVendorChange.QB_ID)
				.eq(QuickbooksVendorChange.RECORD_CHANGED, 'Y')
				.list()) {
			ret += "<VendorModRq requestID = \"" + (++count) + "\"><VendorMod>";
			ret += "<ListID>" + pcs.getQbRecordId() + "</ListID>"
					+ "<EditSequence>" + pcs.getQbRecordRevision() + "</EditSequence>";
			ret = getVendorBodyXML(ret, new BVendorCompany(pcs.getVendor()), true);
			ret += "</VendorMod></VendorModRq>";
		}

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";


		return ret;
	}

	private String getClientXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"continueOnError\">"
				+ "";

		int count = 0;

		//Find employees without qb id's

		for (QuickbooksClientChange pcs : hsu.createCriteria(QuickbooksClientChange.class).isNull(QuickbooksClientChange.QB_ID).list()) {

			BClientCompany client = new BClientCompany(pcs.getClient());

			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getCustomerBodyXML(ret, client, false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}


		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksClientChange.class)
				.selectFields(QuickbooksClientChange.ARAHANT_ID)
				.list();

		//add clients that aren't in change table
		for (ClientCompany client : hsu.createCriteria(ClientCompany.class).notIn(ClientCompany.ORGGROUPID, ids).list()) {
			BClientCompany bclient = new BClientCompany(client);
			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getCustomerBodyXML(ret, bclient, false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}

		for (QuickbooksClientChange pcs : hsu.createCriteria(QuickbooksClientChange.class)
				.isNotNull(QuickbooksClientChange.QB_ID)
				.eq(QuickbooksClientChange.RECORD_CHANGED, 'Y')
				.list()) {

			BClientCompany client = new BClientCompany(pcs.getClient());

			ret += "<CustomerModRq requestID = \"" + (++count) + "\"><CustomerMod>";
			ret += "<ListID>" + pcs.getQbRecordId() + "</ListID>"
					+ "<EditSequence>" + pcs.getQbRecordRevision() + "</EditSequence>";
			ret = getCustomerBodyXML(ret, client, true);

			ret += "</CustomerMod></CustomerModRq>";
		}

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";


		return ret;
	}

	private String getProjectXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		//Find employees without qb id's

		for (QuickbooksProjectChange pcs : hsu.createCriteria(QuickbooksProjectChange.class)
				.isNull(QuickbooksProjectChange.QB_ID)
				.joinTo(QuickbooksProjectChange.PROJECT)
				.joinTo(Project.REQUESTING_ORG_GROUP)
				.eq(OrgGroup.ORGGROUPTYPE, CLIENT_TYPE)
				.list()) {

			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getProjectBodyXML(ret, new BProject(pcs.getProject()), false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}


		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksProjectChange.class)
				.selectFields(QuickbooksProjectChange.ARAHANT_ID)
				.list();

		//add clients that aren't in change table
		for (Project proj : hsu.createCriteria(Project.class).notIn(Project.PROJECTID, ids)
				.joinTo(Project.REQUESTING_ORG_GROUP)
				.eq(OrgGroup.ORGGROUPTYPE, CLIENT_TYPE)
				.list()) {
			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getProjectBodyXML(ret, new BProject(proj), false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}

		for (QuickbooksProjectChange pcs : hsu.createCriteria(QuickbooksProjectChange.class)
				.isNotNull(QuickbooksProjectChange.QB_ID)
				.eq(QuickbooksProjectChange.RECORD_CHANGED, 'Y')
				.list()) {
			ret += "<CustomerModRq requestID = \"" + (++count) + "\"><CustomerMod>";
			ret += "<ListID>" + pcs.getQbRecordId() + "</ListID>"
					+ "<EditSequence>" + pcs.getQbRecordRevision() + "</EditSequence>";
			ret = getProjectBodyXML(ret, new BProject(pcs.getProject()), true);

			ret += "</CustomerMod></CustomerModRq>";
		}

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";


		return ret;
	}

	private String getCompanyXML() {
		String ret = "<?xml version=\"1.0\" ?>"
				+ "<?qbxml version=\"8.0\"?>"
				+ "<QBXML>"
				+ "<QBXMLMsgsRq onError = \"stopOnError\">"
				+ "";

		int count = 0;

		//Find employees without qb id's

		for (QuickbooksClientChange pcs : hsu.createCriteria(QuickbooksClientChange.class).isNull(QuickbooksClientChange.QB_ID).list()) {

			BClientCompany client = new BClientCompany(pcs.getClient());

			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getCustomerBodyXML(ret, client, false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}


		@SuppressWarnings("unchecked")
		List<String> ids = (List) hsu.createCriteria(QuickbooksClientChange.class)
				.selectFields(QuickbooksClientChange.ARAHANT_ID)
				.list();

		//add clients that aren't in change table
		for (ClientCompany client : hsu.createCriteria(ClientCompany.class).notIn(ClientCompany.ORGGROUPID, ids).list()) {
			BClientCompany bclient = new BClientCompany(client);
			ret += "<CustomerAddRq requestID = \"" + (++count) + "\"><CustomerAdd>";
			ret = getCustomerBodyXML(ret, bclient, false);

			ret += "</CustomerAdd></CustomerAddRq>";
		}

		for (QuickbooksClientChange pcs : hsu.createCriteria(QuickbooksClientChange.class)
				.isNotNull(QuickbooksClientChange.QB_ID)
				.eq(QuickbooksClientChange.RECORD_CHANGED, 'Y')
				.list()) {

			BClientCompany client = new BClientCompany(pcs.getClient());

			ret += "<CustomerModRq requestID = \"" + (++count) + "\"><CustomerMod>";
			ret += "<ListID>" + pcs.getQbRecordId() + "</ListID>"
					+ "<EditSequence>" + pcs.getQbRecordRevision() + "</EditSequence>";
			ret = getCustomerBodyXML(ret, client, true);

			ret += "</CustomerMod></CustomerModRq>";
		}

		ret += ""
				+ "</QBXMLMsgsRq>"
				+ "</QBXML>";


		return ret;
	}

	private String getTimeTrackingAdd(BTimesheet ts) {
		//get the person list id
		QuickbooksPersonChange qpc = hsu.createCriteria(QuickbooksPersonChange.class)
				.eq(QuickbooksPersonChange.ARAHANT_ID, ts.getPersonId())
				.first();

		String ret = "<TimeTrackingAdd  >" + //defMacro=\"Tst"+ts.getTimesheetId()+"\"
				"<TxnDate>" + formatDate(ts.getWorkDate()).substring(0, 10) + "</TxnDate>"
				+ "<EntityRef>"
				+ "<ListID >" + qpc.getQbRecordId() + "</ListID>"
				+ "</EntityRef>";




		boolean setRef = false;
		QuickbooksProjectChange qprc = hsu.createCriteria(QuickbooksProjectChange.class)
				.eq(QuickbooksProjectChange.ARAHANT_ID, ts.getProject().getProjectId())
				.first();

		if (qprc != null) {
			//have to specify the customer ref
			ret += "<CustomerRef>"
					+ "<ListID >" + qprc.getQbRecordId() + "</ListID>"
					+ "</CustomerRef>";

			setRef = true;
		} else {
			QuickbooksClientChange qcc = hsu.createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksClientChange.ARAHANT_ID, ts.getProject().getCompanyId())
					.first();

			if (qcc != null) {
				//have to specify the customer ref
				ret += "<CustomerRef>"
						+ "<ListID >" + qcc.getQbRecordId() + "</ListID>"
						+ "</CustomerRef>";

				setRef = true;
			} else {
				QuickbooksVendorChange qvc = hsu.createCriteria(QuickbooksVendorChange.class)
						.eq(QuickbooksVendorChange.ARAHANT_ID, ts.getProject().getCompanyId())
						.first();

				if (qvc != null) {
					//have to specify the customer ref
					ret += "<CustomerRef>"
							+ "<ListID >" + qvc.getQbRecordId() + "</ListID>"
							+ "</CustomerRef>";
					setRef = true;
				}
			}
		}
		if (ts.getBillable() == 'Y')
			if (!isEmpty(ts.getProject().getProductId())) {
				BService prod = new BService(ts.getProject().getProductId());

				ret += "<ItemServiceRef>"
						+ "<ListID >" + prod.getAccsysId() + "</ListID>"
						+ "</ItemServiceRef>";
			} else
				throw new ArahantWarning("Project " + ts.getProject().getProjectName().trim() + " needs to have a product/service selected.");


		String wageId = ts.getWageTypeId();

		HrBenefitProjectJoin bpj = hsu.createCriteria(HrBenefitProjectJoin.class)
				.eq(HrBenefitProjectJoin.PROJECT, ts.getProject().getBean())
				.first();

		if (bpj != null)
			wageId = new AIProperty("WageIDOverride", bpj.getHrBenefitConfig().getHrBenefit().getRuleName()).getValue();

		QuickbooksWageTypeChange qwtc = hsu.createCriteria(QuickbooksWageTypeChange.class)
				.eq(QuickbooksWageTypeChange.ARAHANT_ID, wageId)
				.first();


		if (qwtc == null)
			throw new ArahantWarning("Wage types need to be synchronized before timesheets can be exported");

		String qbid = qwtc.getQbRecordId();



		int hours = (int) ts.getTotalHours();
		int min = (int) ((ts.getTotalHours() - hours) * 60);
		String minutes = min + "";

		char billSave = ts.getBillable();
		if (!setRef)
			ts.setBillable('N');

		ret += "<Duration >PT" + hours + "H" + minutes + "M</Duration>";

		ret += "<PayrollItemWageRef>"
				+ "<ListID >" + qbid + "</ListID>"
				+ "</PayrollItemWageRef>";

		ret += "<Notes >" + DOMUtils.escapeText(ts.getDescription()) + "</Notes>"
				+ //
				"<BillableStatus>" + (ts.getBillable() == 'Y' ? "Billable" : "NotBillable") + "</BillableStatus>"
				+ //       "<IsBillable >"+(ts.getBillable()=='Y'?"true":"false")+"</IsBillable>"+
				"</TimeTrackingAdd>";

		ts.setBillable(billSave);
		return ret;
	}

	private String getTimeTrackingMod(BTimesheet ts) {
		//get the person list id
		QuickbooksPersonChange qpc = hsu.createCriteria(QuickbooksPersonChange.class)
				.eq(QuickbooksPersonChange.ARAHANT_ID, ts.getPersonId())
				.first();

		QuickbooksTimesheetChange qttc = hsu.createCriteria(QuickbooksTimesheetChange.class)
				.eq(QuickbooksTimesheetChange.ARAHANT_ID, ts.getTimesheetId())
				.first();

		String ret = "<TimeTrackingMod  >"
				+ "<TxnID>" + qttc.getQbRecordId() + "</TxnID>"
				+ "<EditSequence>" + qttc.getQbRecordRevision() + "</EditSequence>"
				+ "<TxnDate>" + formatDate(ts.getWorkDate()).substring(0, 10) + "</TxnDate>"
				+ "<EntityRef>"
				+ "<ListID >" + qpc.getQbRecordId() + "</ListID>"
				+ "</EntityRef>";


		boolean setRef = false;

		QuickbooksProjectChange qprc = hsu.createCriteria(QuickbooksProjectChange.class)
				.eq(QuickbooksProjectChange.ARAHANT_ID, ts.getProject().getProjectId())
				.first();

		if (qprc != null) {
			//have to specify the customer ref
			ret += "<CustomerRef>"
					+ "<ListID >" + qprc.getQbRecordId() + "</ListID>"
					+ "</CustomerRef>";

			setRef = true;
		} else {

			QuickbooksClientChange qcc = hsu.createCriteria(QuickbooksClientChange.class)
					.eq(QuickbooksClientChange.ARAHANT_ID, ts.getProject().getCompanyId())
					.first();

			if (qcc != null) {
				//have to specify the customer ref
				ret += "<CustomerRef>"
						+ "<ListID >" + qcc.getQbRecordId() + "</ListID>"
						+ "</CustomerRef>";

				setRef = true;
			} else {

				QuickbooksVendorChange qvc = hsu.createCriteria(QuickbooksVendorChange.class)
						.eq(QuickbooksVendorChange.ARAHANT_ID, ts.getProject().getCompanyId())
						.first();

				if (qvc != null) {
					//have to specify the customer ref
					ret += "<CustomerRef>"
							+ "<ListID >" + qvc.getQbRecordId() + "</ListID>"
							+ "</CustomerRef>";
					setRef = true;
				}
			}
		}
		if (!isEmpty(ts.getProject().getProductId())) {
			BService prod = new BService(ts.getProject().getProductId());

			ret += "<ItemServiceRef>"
					+ "<ListID >" + prod.getAccsysId() + "</ListID>"
					+ "</ItemServiceRef>";
		}



		QuickbooksWageTypeChange qwtc = hsu.createCriteria(QuickbooksWageTypeChange.class)
				.eq(QuickbooksWageTypeChange.ARAHANT_ID, ts.getWageTypeId())
				.first();


		if (qwtc == null)
			throw new ArahantWarning("Please synchronize Wage Types first.");

		String qbid = qwtc.getQbRecordId();

		int hours = (int) ts.getTotalHours();
		int min = (int) ((ts.getTotalHours() - hours) * 60);
		String minutes = min + "";

		char billSave = ts.getBillable();
		if (!setRef)
			ts.setBillable('N');

		ret += "<Duration >PT" + hours + "H" + minutes + "M</Duration>";

		ret += "<PayrollItemWageRef>"
				+ "<ListID >" + qbid + "</ListID>"
				+ "</PayrollItemWageRef>";

		ret += "<Notes >" + DOMUtils.escapeText(ts.getDescription()) + "</Notes>"
				+ //
				"<BillableStatus>" + (ts.getBillable() == 'Y' ? "Billable" : "NotBillable") + "</BillableStatus>"
				+ //       "<IsBillable >"+(ts.getBillable()=='Y'?"true":"false")+"</IsBillable>"+
				"</TimeTrackingMod>";

		ts.setBillable(billSave);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private void preProcessTimesheets() {

		//find the overtime wage type
		WageType overtimeWage = hsu.createCriteria(WageType.class)
				.eq(WageType.WAGE_CAT, WageType.TYPE_OVERTIME)
				.first();

		//find timsheets for people that are regular time and timesheets are ready to be exported
		List<String> ids = (List) hsu.createCriteria(QuickbooksTimesheetChange.class)
				.selectFields(QuickbooksTimesheetChange.ARAHANT_ID)
				.eq(QuickbooksTimesheetChange.RECORD_CHANGED, 'Y')
				.joinTo(QuickbooksTimesheetChange.TIMESHEET)
				.in(Timesheet.STATE, new char[]{TIMESHEET_APPROVED, TIMESHEET_INVOICED})
				.list();



		ids.addAll((List) hsu.createCriteria(Timesheet.class).notIn(Timesheet.TIMESHEETID, ids)
				.selectFields(Timesheet.TIMESHEETID)
				.in(Timesheet.STATE, new char[]{ArahantConstants.TIMESHEET_APPROVED, ArahantConstants.TIMESHEET_INVOICED})
				.joinTo(Timesheet.WAGE_TYPE)
				.eq(WageType.WAGE_CAT, WageType.TYPE_REGULAR)
				.list());


		//ids now contains all timesheets new or modified that need to be exported

		List<Timesheet> tsl = hsu.createCriteria(Timesheet.class).in(Timesheet.TIMESHEETID, ids).orderByDesc(Timesheet.WORKDATE).list();

		for (Timesheet ts : tsl) {
			//if timesheet is for non-employee, skip it
			BPerson bp = new BPerson(ts.getPerson());
			if (!bp.isEmployee())
				continue;

			BEmployee bemp = bp.getBEmployee();

			//now I need to see if employee is salary or not
			if (bemp.getCurrentSalaryType() != WageType.PERIOD_HOURLY)
				continue;

			//so I now have a timesheet that falls in some week for an hourly person
			//Quickbooks says the workweek starts on Mondays
			//how many hours have we got in this week?
			Calendar workDate = DateUtils.getCalendar(ts.getWorkDate());

			Calendar monday = (Calendar) workDate.clone();
			Calendar sunday = (Calendar) workDate.clone();

			while (monday.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
				monday.add(Calendar.DAY_OF_YEAR, -1);

			while (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
				sunday.add(Calendar.DAY_OF_YEAR, 1);

			//how many hours have they worked regular time in this week?
			double hours = (float) hsu.createCriteria(Timesheet.class)
					.sum(Timesheet.TOTALHOURS)
					.eq(Timesheet.PERSON, ts.getPerson())
					.dateBetween(Timesheet.WORKDATE, DateUtils.getDate(monday), DateUtils.getDate(sunday))
					.joinTo(Timesheet.WAGE_TYPE)
					.eq(WageType.WAGE_CAT, WageType.TYPE_REGULAR)
					.doubleVal();

			if (hours <= 40)  //worked no overtime
				continue;

			if (overtimeWage == null)
				throw new ArahantWarning("No overtime wage type found.");

			//ok, so they have run over and need some overtime set for that week
			double overtime = hours - 40;

			//is my current timesheet time less than the overtime?
			if (ts.getTotalHours() <= overtime + .009) {
				//don't need to split or anything fancy, just make it overtime type
				ts.setWageType(overtimeWage);
				hsu.insert(ts);
			} else {
				//need to split the time
				ts.setTotalHours(ts.getTotalHours() - overtime);
				hsu.insert(ts);

				Timesheet nts = (Timesheet) ts.clone();
				nts.setWageType(overtimeWage);
				nts.setTotalHours(overtime);
				hsu.insert(nts);

			}
		}

	}

	private String tag(String tagName, String val) {
		return "<" + tagName + ">" + DOMUtils.escapeText(val) + "</" + tagName + "> ";
	}
}
