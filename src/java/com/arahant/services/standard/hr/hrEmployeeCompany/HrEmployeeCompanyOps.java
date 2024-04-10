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
package com.arahant.services.standard.hr.hrEmployeeCompany;
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


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEmployeeCompanyOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrEmployeeCompanyOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrEmployeeCompanyOps.class);
	
	public HrEmployeeCompanyOps() {
		super();
	}
	
    @WebMethod()
	public ListAssCompaniesReturn listAssCompanies(/*@WebParam(name = "in")*/final ListAssCompaniesInput in)		
	{
		final ListAssCompaniesReturn ret=new ListAssCompaniesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BCompany.listAssCompanies(hsu, in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in)		
	{
		final SearchCompaniesReturn ret=new SearchCompaniesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).searchCompaniesNotIn(in.getName(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public AddCompanyToEmployeeReturn addCompanyToEmployee(/*@WebParam(name = "in")*/final AddCompanyToEmployeeInput in)
	{
		final AddCompanyToEmployeeReturn ret=new AddCompanyToEmployeeReturn();
		try
		{
			checkLogin(in);
			
			new BEmployee(in.getEmployeeId()).assignToOrgGroupWithStatus(in.getCompanyId(), in.getCompanyStatus(), in.getCompanyStatusDate(), in.getCompanyStatusNote(), false, 0, 0);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
        public RemoveCompaniesFromEmployeeReturn removeCompaniesFromEmployee(/*@WebParam(name = "in")*/final RemoveCompaniesFromEmployeeInput in)
	{
		final RemoveCompaniesFromEmployeeReturn ret=new RemoveCompaniesFromEmployeeReturn();
		try
		{
			checkLogin(in);
			
			new BEmployee(in.getEmployeeId()).removeCompaniesFromEmployee(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

        @WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)	{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(in.getCompanyId(), hsu));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
