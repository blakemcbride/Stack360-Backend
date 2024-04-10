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
package com.arahant.services.standard.misc.companySearch;
import com.arahant.beans.Right;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscCompanySearchOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class CompanySearchOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			CompanySearchOps.class);
	
	public CompanySearchOps() {
		super();
	}
	
    @WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in)		
	{
		final SearchCompanyReturn ret=new SearchCompanyReturn();
		try
		{
			checkLogin(in);

                        BCompanyBase [] r=BCompanyBase.search(in.getId(),in.getMainContactFirstName(),in.getMainContactLastName(), in.getName(),ret.getCap(),in.getSortOn(),in.getSortAsc());

			ret.setItem(r);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRM"));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteCompanyReturn deleteCompany(/*@WebParam(name = "in")*/final DeleteCompanyInput in) {		final DeleteCompanyReturn ret=new DeleteCompanyReturn();
		try {
			checkLogin(in);
			
			BCompany.delete(hsu, in.getCompanyIds());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public ListCompanyReturn listCompany(/*@WebParam(name = "in")*/final ListCompanyInput in)	{
		final ListCompanyReturn ret=new ListCompanyReturn();

		try {
			checkLogin(in);

			if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==ACCESS_LEVEL_WRITE)
				ret.setCompanies(BCompany.listCompaniesNoFilter(hsu));
			else
				ret.setCompanies(BCompany.listCompanies(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadCompanyReturn loadCompany(/*@WebParam(name = "in")*/final LoadCompanyInput in) {
		LoadCompanyReturn ret=new LoadCompanyReturn();
		
		try {
			checkLogin(in);
			
			ret=new LoadCompanyReturn(new BCompany(in.getCompanyId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}	
		
		return ret;
	}

	@WebMethod()
	public LoadDefaultBillingRateReturn loadDefaultBillingRate(/*@WebParam(name = "in")*/final LoadDefaultBillingRateInput in)		
	{
		final LoadDefaultBillingRateReturn ret=new LoadDefaultBillingRateReturn();
		try
		{
			checkLogin(in);
			
			ret.setDefaultBillingRateFormatted(BCompany.getDefaultBillingRateFormatted());
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewCompanyReturn newCompany(/*@WebParam(name = "in")*/final NewCompanyInput in)	{
		//make a company detail record
			
		final NewCompanyReturn ret=new NewCompanyReturn();

		try {
			checkLogin(in);
			
			final BCompany bc=new BCompany();
			bc.create();
			in.makeCompany(bc);
			bc.insert();
			
			ret.setCompanyId(bc.getOrgGroupId());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveCompanyReturn saveCompany(/*@WebParam(name = "in")*/final SaveCompanyInput in) {		
		final SaveCompanyReturn ret=new SaveCompanyReturn();
		
		try
		{
			checkLogin(in);
			final BCompany bc=new BCompany(in.getOrgGroupId());
			in.makeCompany(bc);
			bc.update();

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchGLAccountsReturn searchGLAccounts(/*@WebParam(name = "in")*/final SearchGLAccountsInput in)		
	{
		final SearchGLAccountsReturn ret=new SearchGLAccountsReturn();
		try
		{
			checkLogin(in);
			BGlAccount accts[]=BGlAccount.search(in.getAccountName(),in.getAccountNumber(),in.getShowOnlyARAccounts(),ret.getHighCap());
			ret.setItem(accts);
			
			if (!isEmpty(in.getSelectUsingCompanyId())) {
				BCompany company = new BCompany(in.getSelectUsingCompanyId());
				
				if (in.getSelectUsingType() == 1 && !isEmpty(company.getARAccountId())) { // AR
					ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(company.getARAccountId())));
				} else if (in.getSelectUsingType() == 2 && !isEmpty(company.getEmployeeAdvanceAccountId())) { // Emp Adv
					ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(company.getEmployeeAdvanceAccountId())));
				} else if (in.getSelectUsingType() == 3 && !isEmpty(company.getCashAccountId())) { // Cash
					ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(company.getCashAccountId())));
				}
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
