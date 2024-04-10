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
package com.arahant.services.standard.crm.prospectAssignment;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectAssignmentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectAssignmentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProspectAssignmentOps.class);
	
	public ProspectAssignmentOps() {
		super();
	}
	
    @WebMethod()
	public SearchProspectsReturn searchProspects(/*@WebParam(name = "in")*/final SearchProspectsInput in)		
	{
		final SearchProspectsReturn ret=new SearchProspectsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProspectCompany.searchProspects(in.getName(), in.getEmployeeId(), in.getExcludeIds(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)		
	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(),in.getLastName(),"",ret.getHighCap()));

			if(!isEmpty(in.getId()))  //prospect id
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BProspectCompany(in.getId()).getSalesPerson()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public AssignProspectsReturn assignProspects(/*@WebParam(name = "in")*/final AssignProspectsInput in)		
	{
		final AssignProspectsReturn ret=new AssignProspectsReturn();
		try
		{
			checkLogin(in);
			
			for (String s : in.getProspectIds())
			{
				BProspectCompany bpc = new BProspectCompany(s);
				bpc.setSalesPersonId(in.getEmployeeId());
				bpc.setNextContactDate(in.getNextContactDate());
				bpc.update();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
