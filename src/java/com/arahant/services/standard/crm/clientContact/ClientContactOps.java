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
package com.arahant.services.standard.crm.clientContact;

import com.arahant.business.*;
import com.arahant.reports.ClientContactReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmClientContactOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ClientContactOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ClientContactOps.class);

	public ClientContactOps() {
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
	public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in) {
		final SearchOrgGroupsForCompanyReturn ret = new SearchOrgGroupsForCompanyReturn();
		try {
			checkLogin(in);

			BClientCompany cl = new BClientCompany(in.getCompanyId());

			ret.setItem(cl.searchOrgGroups(in.getOrgGroupName(), ret.getHighCap(), true));

			// set org group that matches the company
			ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(new BOrgGroup(cl.getOrgGroupId())));

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
	public LoadContactReturn loadContact(/*@WebParam(name = "in")*/final LoadContactInput in) {
		final LoadContactReturn ret = new LoadContactReturn();
		try {
			checkLogin(in);

			ret.setData(new BClientContact(in.getId()));

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
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap()));

			if (!isEmpty(in.getPersonId())) {
				BPerson p = new BPerson(in.getPersonId());
				if (p.getScreenGroup() != null)
					ret.setSelectedItem(new SearchScreenGroupsReturnItem(new BScreenGroup(p.getScreenGroup())));
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();

		try {
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getHighCap()));

			if (!isEmpty(in.getPersonId())) {
				BPerson p = new BPerson(in.getPersonId());
				if (!isEmpty(p.getSecurityGroupId()))
					ret.setSelectedItem(new SearchSecurityGroupsItem(new BSecurityGroup(p.getSecurityGroupId())));
			}
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
	public NewClientContactReturn newClientContact(/*@WebParam(name = "in")*/final NewClientContactInput in) {
		final NewClientContactReturn ret = new NewClientContactReturn();

		try {
			checkLogin(in);

			final BClientContact bcc = new BClientContact();
			bcc.create();
			in.makeClientContact(bcc);
			bcc.insert();
			bcc.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());

			ret.setPersonId(bcc.getPersonId());


			for (NewClientContactInputItem i : in.getItem()) {
				BContactQuestionDetail qd = new BContactQuestionDetail();
				qd.create();
				qd.setResponse(i.getResponse());
				qd.setPersonId(bcc.getPersonId());
				qd.setQuestionId(i.getQuestionId());
				qd.insert();
			}

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SaveClientContactReturn saveClientContact(/*@WebParam(name = "in")*/final SaveClientContactInput in) {
		final SaveClientContactReturn ret = new SaveClientContactReturn();
		try {
			checkLogin(in);

			final BClientContact bcc = new BClientContact(in.getPersonId());
			in.makeClientContact(bcc);
			bcc.update();
			bcc.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public DeleteClientContactReturn deleteClientContact(/*@WebParam(name = "in")*/final DeleteClientContactInput in) {
		final DeleteClientContactReturn ret = new DeleteClientContactReturn();

		try {
			checkLogin(in);
			BClientContact.delete(hsu, in.getIds());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new ClientContactReport().build(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
