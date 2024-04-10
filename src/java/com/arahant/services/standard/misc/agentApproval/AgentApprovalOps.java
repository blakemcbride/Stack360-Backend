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
package com.arahant.services.standard.misc.agentApproval;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscAgentApprovalOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AgentApprovalOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AgentApprovalOps.class);
	
	public AgentApprovalOps() {
		super();
	}
	
    @WebMethod()
	public ListAgentsForCompanyReturn listAgentsForCompany(/*@WebParam(name = "in")*/final ListAgentsForCompanyInput in)		
	{
		final ListAgentsForCompanyReturn ret=new ListAgentsForCompanyReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAgentJoin.list(ArahantSession.getHSU().getCurrentCompany().getCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ApproveAgentsForCompanyReturn approveAgentsForCompany(/*@WebParam(name = "in")*/final ApproveAgentsForCompanyInput in)		
	{
		final ApproveAgentsForCompanyReturn ret=new ApproveAgentsForCompanyReturn();
		try
		{
			checkLogin(in);

			BAgentJoin agentJoin = null ;
                        //Kalvin 3/22/2001:
                        //For each company_id received, get the joined data of the agent and company and set whether the agent
                        //is approved for this company or not
                        for (String companyId : in.getIds()){
                            agentJoin = new BAgentJoin(companyId);
                            agentJoin.getBean().setApprovedByPersonId(hsu.getCurrentPerson().getPersonId());
                            char isApproved = agentJoin.getBean().getApproved();
                            //toggle between Y and N
                            if (isApproved=='N'){
                                agentJoin.getBean().setApproved('Y');
                            } else {
                                agentJoin.getBean().setApproved('N');
                            }
                            agentJoin.getBean().setApprovedDate(DateUtils.getDate(DateUtils.now()));
                            agentJoin.update();
                            agentJoin = null ;
                        }
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
