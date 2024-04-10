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
package com.arahant.services.standard.crm.prospectContact;

import com.arahant.business.*;
import com.arahant.reports.ProspectContactReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmProspectContactOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProspectContactOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ProspectContactOps.class);

	public ProspectContactOps() {
		super();
	}

	@WebMethod()
	public ListContactsReturn listContacts(/*@WebParam(name = "in")*/final ListContactsInput in) {
		final ListContactsReturn ret = new ListContactsReturn();
		try {
			checkLogin(in);

			ret.setItem(new BOrgGroup(in.getOrgGroupId()).listPeople(ret.getCap()), in.getOrgGroupId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();
		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessCRM"));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in) {
		final SearchOrgGroupsForCompanyReturn ret = new SearchOrgGroupsForCompanyReturn();
		try {
			checkLogin(in);


			BProspectCompany pc = new BProspectCompany(in.getCompanyId());

			ret.setItem(pc.searchOrgGroups(in.getOrgGroupName(), ret.getHighCap(), true));

			// set org group that matches the company
			ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(new BOrgGroup(pc.getOrgGroupId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadContactReturn loadContact(/*@WebParam(name = "in")*/final LoadContactInput in) {
		final LoadContactReturn ret = new LoadContactReturn();
		try {
			checkLogin(in);

			ret.setData(new BProspectContact(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListContactQuestionsReturn listContactQuestions(/*@WebParam(name = "in")*/final ListContactQuestionsInput in) {
		final ListContactQuestionsReturn ret = new ListContactQuestionsReturn();
		try {
			checkLogin(in);

			ret.setItem(BContactQuestionDetail.list(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadQuestionDetailReturn loadQuestionDetail(/*@WebParam(name = "in")*/final LoadQuestionDetailInput in) {
		final LoadQuestionDetailReturn ret = new LoadQuestionDetailReturn();
		try {
			checkLogin(in);

			ret.setData(new BContactQuestionDetail(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewContactReturn newContact(/*@WebParam(name = "in")*/final NewContactInput in) {
		final NewContactReturn ret = new NewContactReturn();
		try {
			checkLogin(in);

			final BProspectContact bpc = new BProspectContact();
			bpc.create();
			in.setData(bpc);
			bpc.insert();
			bpc.assignToOrgGroup(in.getOrgGroupId(), in.getPrimaryIndicator());

			ret.setPersonId(bpc.getPersonId());


			for (NewContactInputItem i : in.getItem()) {
				BContactQuestionDetail qd = new BContactQuestionDetail();
				qd.create();
				qd.setResponse(i.getResponse());
				qd.setPersonId(bpc.getPersonId());
				qd.setQuestionId(i.getQuestionId());
				qd.insert();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveContactReturn saveContact(/*@WebParam(name = "in")*/final SaveContactInput in) {
		final SaveContactReturn ret = new SaveContactReturn();
		try {
			checkLogin(in);

			final BProspectContact bpc = new BProspectContact(in.getPersonId());
			in.setData(bpc);
			bpc.update();
			bpc.assignToOrgGroup(in.getOrgGroupId(), in.getPrimaryIndicator());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteContactsReturn deleteContacts(/*@WebParam(name = "in")*/final DeleteContactsInput in) {
		final DeleteContactsReturn ret = new DeleteContactsReturn();
		try {
			checkLogin(in);

			BProspectContact.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new ProspectContactReport().build(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
