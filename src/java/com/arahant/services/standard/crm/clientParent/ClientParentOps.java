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


package com.arahant.services.standard.crm.clientParent;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmClientParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ClientParentOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(ClientParentOps.class);

	@WebMethod()
	public ListGLSalesAccountsReturn listGLSalesAccounts(/*@WebParam(name = "in")*/final ListGLSalesAccountsInput in) {
		final ListGLSalesAccountsReturn ret = new ListGLSalesAccountsReturn();

		try {
			checkLogin(in);

			ret.setGLAccounts(BGlAccount.listByType(hsu, 21));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListClientStatusesReturn listClientStatuses(/*@WebParam(name = "in")*/final ListClientStatusesInput in) {
		final ListClientStatusesReturn ret = new ListClientStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BClientStatus.list(true));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteCompanyReturn deleteCompany(/*@WebParam(name = "in")*/final DeleteCompanyInput in) {
		final DeleteCompanyReturn ret = new DeleteCompanyReturn();

		try {
			checkLogin(in);

			BClientCompany.deleteCompanies(hsu, in.getClientCompanyId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	/*
	 @WebMethod()
	 public LoadClientCompanyReturn loadClientCompany(/*@WebParam(name = "in")*final LoadClientCompanyInput in) {	
	 LoadClientCompanyReturn ret=new LoadClientCompanyReturn();
		
	 try
	 {
	 checkLogin(in);

	 ret=new LoadClientCompanyReturn(new BClientCompany(in.getClientCompanyId()));
			
	 finishService(ret);
	 }
	 catch (final Exception e)
	 {
	 handleError(hsu, e, ret, logger);
	 }
		
	 return ret;
	 }
	 */

	@WebMethod()
	public NewClientCompanyReturn newClientCompany(/*@WebParam(name = "in")*/final NewClientCompanyInput in) {
		final NewClientCompanyReturn ret = new NewClientCompanyReturn();

		try {
			checkLogin(in);

			if (!isEmpty(in.getId())) {
				BClientCompany bcc = BClientCompany.convertFromProspect(in.getId());
//				in.makeClientCompany(bcc);
				bcc.update();
				ret.setOrgGroupId(bcc.getOrgGroupId());
			} else {
				BClientCompany bcc = new BClientCompany();
				ret.setOrgGroupId(bcc.create());
				in.makeClientCompany(bcc);
				bcc.insert();
//				ret.setOrgGroupId(bcc.getOrgGroupId());
			}

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchClientCompanyReturn searchClientCompany(/*@WebParam(name = "in")*/final SearchClientCompanyInput in) {
		final SearchClientCompanyReturn ret = new SearchClientCompanyReturn();

		try {
			checkLogin(in);
			ret.setClientList(BClientCompany.searchClientCompanies(hsu, in, ret.getCap()));

			if (!isEmpty(in.getSortOn())) {
				ClientSearchComparator csc = new ClientSearchComparator();
				csc.asc = in.getSortAsc();
				csc.field = in.getSortOn();

				List<ClientList> cl = new ArrayList<ClientList>(ret.getClientList().length);
				Collections.addAll(cl, ret.getClientList());
				Collections.sort(cl, csc);
				ret.setClientList(cl.toArray(ret.getClientList()));
			}

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

	//empty or one of clientName, clientIdentifier, clientPhone, clientContactLastName, clientContactFirstName, clientContactPhone, clientStatus
	private class ClientSearchComparator implements Comparator<ClientList> {

		private String field;
		private boolean asc;

		@Override
		public int compare(ClientList o1, ClientList o2) {

			if (!asc) {
				ClientList t = o2;
				o2 = o1;
				o1 = t;
			}

			if ("clientIdentifier".equals(field))
				return o1.getClientIdentifier().compareTo(o2.getClientIdentifier());

			if ("clientPhone".equals(field))
				return o1.getClientPhone().compareTo(o2.getClientPhone());

			if ("clientContactLastName".equals(field))
				return o1.getClientContactLastName().compareTo(o2.getClientContactLastName());

			if ("clientContactFirstName".equals(field))
				return o1.getClientContactFirstName().compareTo(o2.getClientContactFirstName());

			if ("clientContactPhone".equals(field))
				return o1.getClientContactPhone().compareTo(o2.getClientContactPhone());

			if ("clientStatus".equals(field))
				return o1.getClientStatus().compareTo(o2.getClientStatus());

			//must be the client name
			return o1.getClientName().compareTo(o2.getClientName());
		}
	}

	@WebMethod()
	public SearchProspectsReturn searchProspects(/*@WebParam(name = "in")*/final SearchProspectsInput in) {
		final SearchProspectsReturn ret = new SearchProspectsReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectCompany.search(in.getName(), in.getIdentifier(), ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProspectReturn loadProspect(/*@WebParam(name = "in")*/final LoadProspectInput in) {
		final LoadProspectReturn ret = new LoadProspectReturn();
		try {
			checkLogin(in);

			ret.setData(new BProspectCompany(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetDefaultsReturn getDefaults(/*@WebParam(name = "in")*/final GetDefaultsInput in) {
		final GetDefaultsReturn ret = new GetDefaultsReturn();
		try {
			checkLogin(in);

			ret.setData(new BCompany(in.getContextCompanyId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
