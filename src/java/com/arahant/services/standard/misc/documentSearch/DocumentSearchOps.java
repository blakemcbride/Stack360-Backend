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
package com.arahant.services.standard.misc.documentSearch;

import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantConstants;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardMiscDocumentSearchOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class DocumentSearchOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			DocumentSearchOps.class);

	public DocumentSearchOps() {
		super();
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();
		try {
			checkLogin(in);

			if (BRight.checkRight("employeeDocuments") == ArahantConstants.ACCESS_LEVEL_WRITE)
			{
				ret.setItem(BEmployee.searchEmployees(in.getFirstName(), in.getLastName(), ret.getCap()));
			} 
			else
			{
				List<Person> subs = BEmployee.getSubordinateList(true, true);

				BEmployee[] emps = new BEmployee[subs.size()];

				for (int loop = 0; loop < subs.size(); loop++) {
					emps[loop] = new BEmployee(subs.get(loop).getPersonId());
				}

				ret.setItem(emps);
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in) {
		final SearchCompaniesReturn ret = new SearchCompaniesReturn();
		try {
			checkLogin(in);

			if (BRight.checkRight("companyDocuments") == ArahantConstants.ACCESS_LEVEL_WRITE) {
				ret.setItem(BCompany.search(in.getCompanyName(), ret.getCap()));
			} else {
				ret.setItem(BCompany.searchWithFilter(in.getCompanyName(), ret.getCap()));
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeeDocumentsReturn searchEmployeeDocuments(/*@WebParam(name = "in")*/final SearchEmployeeDocumentsInput in) {
		final SearchEmployeeDocumentsReturn ret = new SearchEmployeeDocumentsReturn();
		try {
			checkLogin(in);

			if (isEmpty(in.getEmployeeId()))
			{
				if (BRight.checkRight("employeeDocuments") == ArahantConstants.ACCESS_LEVEL_WRITE)
				{
					ret.setItem(BPersonForm.search(in.getName(), "", in.getTypeId(), in.getFromDate(), in.getToDate(), ret.getCap()));
				} 
				else
				{
					List<String> l = BEmployee.getSubordinateIds(true, true);
					String[] empIds = new String[l.size()];

					for (int loop = 0; loop < l.size(); loop++) {
						empIds[loop] = l.get(loop);
					}

					ret.setItem(BPersonForm.search(in.getName(), empIds, in.getTypeId(), in.getFromDate(), in.getToDate(), ret.getCap()));

				}
			} else {
				ret.setItem(BPersonForm.search(in.getName(), in.getEmployeeId(), in.getTypeId(), in.getFromDate(), in.getToDate(), ret.getCap()));
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDocumentTypesReturn listDocumentTypes(/*@WebParam(name = "in")*/final ListDocumentTypesInput in) {
		final ListDocumentTypesReturn ret = new ListDocumentTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BFormType.listTypes());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public OpenEmployeeDocumentReturn openEmployeeDocument(/*@WebParam(name = "in")*/final OpenEmployeeDocumentInput in) {
		final OpenEmployeeDocumentReturn ret = new OpenEmployeeDocumentReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new BPersonForm(in.getDocumentId()).getReport());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public OpenCompanyDocumentReturn openCompanyDocument(/*@WebParam(name = "in")*/final OpenCompanyDocumentInput in) {
		final OpenCompanyDocumentReturn ret = new OpenCompanyDocumentReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new BCompanyForm(in.getDocumentId()).getForm());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchGroupsForCompanyReturn searchGroupsForCompany(/*@WebParam(name = "in")*/final SearchGroupsForCompanyInput in) {
		final SearchGroupsForCompanyReturn ret = new SearchGroupsForCompanyReturn();
		try {
			checkLogin(in);

			BCompany company = new BCompany(in.getCompanyId());
			BOrgGroup[] orgs = company.searchOrgGroups(in.getOrgGroupName(), ret.getCap());

			ret.setItem(orgs);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompanyDocumentsReturn searchCompanyDocuments(/*@WebParam(name = "in")*/final SearchCompanyDocumentsInput in) {
		final SearchCompanyDocumentsReturn ret = new SearchCompanyDocumentsReturn();
		try {
			checkLogin(in);

			ret.setItem(BCompanyForm.searchDocuments(in));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
