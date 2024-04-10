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
package com.arahant.services.standard.hr.hrOrgGroupHistory;

import com.arahant.beans.OrgGroupAssociationH;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroupAssociation;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrOrgGroupHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrOrgGroupHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrOrgGroupHistoryOps.class);
	
	public HrOrgGroupHistoryOps() {
		super();
	}
	
	@WebMethod()
	public ListOrgGroupHistoryReturn listOrgGroupHistory(/*@WebParam(name = "in")*/final ListOrgGroupHistoryInput in)			{
		final ListOrgGroupHistoryReturn ret=new ListOrgGroupHistoryReturn();
		try
		{
			checkLogin(in);

			BOrgGroupAssociation.deleteExpiredAssocations();

            ret.setItem(new BEmployee(in.getPersonId()).listOrgGroupHistory(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	/*
	public static void main(String args[])
	{
		ListOrgGroupHistoryInput in=new ListOrgGroupHistoryInput();
		in.setUser("laurieg");
		in.setPassword("bummer");
		in.setPerson("00001-0000015501");
		
		HrOrgGroupHistoryOps ops=new HrOrgGroupHistoryOps();
		ListOrgGroupHistoryReturn ret=ops.listOrgGroupHistory(in);
		
		logger.info(ret.item.length);
		
	}
*/
	@WebMethod()
    public DeleteHistoryReturn deleteHistory(/*@WebParam(name = "in")*/final DeleteHistoryInput in)		
	{
		final DeleteHistoryReturn ret=new DeleteHistoryReturn();
		try
		{
			checkLogin(in);
			
			try {
				ArahantSession.getHSU().createCriteria(OrgGroupAssociationH.class).in(OrgGroupAssociationH.HISTORY_ID, in.getIds()).delete();
			} catch (ArahantDeleteException d) {
				throw new ArahantDeleteException("Unable to delete history.");
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
