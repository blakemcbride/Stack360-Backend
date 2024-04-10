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
package com.arahant.services.standard.billing.bankAccount;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingBankAccountOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BankAccountOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BankAccountOps.class);
	
	public BankAccountOps() {
		super();
	}
	
        @WebMethod()
	public ListCompanyOrgGroupsReturn listCompanyOrgGroups(/*@WebParam(name = "in")*/final ListCompanyOrgGroupsInput in)		
	{
		final ListCompanyOrgGroupsReturn ret=new ListCompanyOrgGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BCompany.listCompanies(hsu));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewBankAccountReturn newBankAccount(/*@WebParam(name = "in")*/final NewBankAccountInput in)		
	{
		final NewBankAccountReturn ret=new NewBankAccountReturn();
		try
		{
			checkLogin(in);
			
			final BBankAccount x=new BBankAccount();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SaveBankAccountReturn saveBankAccount(/*@WebParam(name = "in")*/final SaveBankAccountInput in)		
	{
		final SaveBankAccountReturn ret=new SaveBankAccountReturn();
		try
		{
			checkLogin(in);
			
			final BBankAccount x=new BBankAccount(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteBankAccountsReturn deleteBankAccounts(/*@WebParam(name = "in")*/final DeleteBankAccountsInput in)		
	{
		final DeleteBankAccountsReturn ret=new DeleteBankAccountsReturn();
		try
		{
			checkLogin(in);
			
			BBankAccount.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
		//TODO:	ret.setReportUrl(new BBankAccount().getReport());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListBankAccountsReturn listBankAccounts(/*@WebParam(name = "in")*/final ListBankAccountsInput in)		
	{
		final ListBankAccountsReturn ret=new ListBankAccountsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBankAccount.list(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();


		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessHR"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
       
	

}
