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
 */
package com.arahant.services.standard.hr.employeeAndDependentImport;

import com.arahant.business.BPerson;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BSecurityGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrEmployeeAndDependentImportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class EmployeeAndDependentImportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(EmployeeAndDependentImportOps.class);

	public EmployeeAndDependentImportOps() {}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*
			 * @WebParam(name = "in")
			 */final SearchScreenGroupsInput in) {
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
	public SearchSecurityGroupsReturn searchSecurityGroups(/*
			 * @WebParam(name = "in")
			 */final SearchSecurityGroupsInput in) {
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
}
