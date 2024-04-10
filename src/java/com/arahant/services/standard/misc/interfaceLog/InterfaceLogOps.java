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
package com.arahant.services.standard.misc.interfaceLog;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscInterfaceLogOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class InterfaceLogOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			InterfaceLogOps.class);
	
	public InterfaceLogOps() {
		super();
	}
	
    @WebMethod()
	public ListInterfaceLogsReturn listInterfaceLogs(/*@WebParam(name = "in")*/final ListInterfaceLogsInput in)		
	{
		final ListInterfaceLogsReturn ret=new ListInterfaceLogsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BInterfaceLog.list(in.getCompanyId(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
    public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in) {
        final SearchCompanyByTypeReturn ret = new SearchCompanyByTypeReturn();
        try {
            checkLogin(in);

            if (hsu.getCurrentPerson().getOrgGroupType() == CLIENT_TYPE) {
                BPerson bp = new BPerson(hsu.getCurrentPerson());
                BCompanyBase[] ar = new BCompanyBase[1];
                ar[0] = bp.getCompany();
                ret.setCompanies(ar);
            } else {
                ret.setCompanies(BCompanyBase.searchByCompanyType(in.getName(), false, ret.getHighCap(),BCompanyBase.COMPANY_TYPE));
            }


            if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(new BProject(in.getProjectId()).getRequestingCompanyId())));
            } else if (in.getAutoDefault()) {
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BPerson(hsu.getCurrentPerson()).getCompany()));
            } else if (!isEmpty(in.getId())) {
                if (ret.getCompanies().length <= ret.getLowCap()) {
                    //if it's in the list, set selected item
                    for (SearchCompanyByTypeReturnItem ogri : ret.getCompanies()) {
                        if (in.getId().equals(ogri.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }
                } else {
                    for (BCompanyBase bp : BCompanyBase.search(in.getName(), false, 0)) {
                        if (in.getId().equals(bp.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }


                }
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

	

}
