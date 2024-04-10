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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.peachtreeApp;

import com.arahant.beans.ClientStatus;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;



/**
 * 
 *
 * Created on Feb 4, 2007
 *  
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="PeachtreeAppOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PeachtreeAppOps extends ServiceBase {
	
	static ArahantLogger logger = new ArahantLogger(PeachtreeAppOps.class);
	
	
	@WebMethod()
	public ListGlAccountsReturn listGlAccounts(/*@WebParam(name = "in")*/final ListGlAccountsInput in)	{
		

		final ListGlAccountsReturn ret=new ListGlAccountsReturn();	

		try {
			checkLogin(in);
			
			ret.setGLAccounts(BGlAccount.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	@WebMethod()
	public ListProductsAndServicesReturn listProductsAndServices(/*@WebParam(name = "in")*/final ListProductsAndServicesInput in)	{
		final ListProductsAndServicesReturn ret=new ListProductsAndServicesReturn();
		
		try {
			checkLogin(in);

			ret.setPst(BService.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}
	

	@WebMethod()
	public NewGLAccountReturn newGlAccount(/*@WebParam(name = "in")*/final NewGLAccountInput in)	{
		final NewGLAccountReturn ret=new NewGLAccountReturn();
		try {
			checkLogin(in);
			
			final BGlAccount gla=new BGlAccount();
			ret.setGlAccountId(gla.create());
			in.setData(gla);
			gla.insert();
			
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewProductServiceReturn newProductService(/*@WebParam(name = "in")*/final NewProductServiceInput in)	{
		final NewProductServiceReturn ret=new NewProductServiceReturn();

		try {
			checkLogin(in);
			
			final BService p=new BService();
			ret.setProductId(p.create());
			in.setData(p);
			p.insert();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SaveGLAccountReturn saveGlAccount(/*@WebParam(name = "in")*/final SaveGLAccountInput in)	{
		final SaveGLAccountReturn ret=new SaveGLAccountReturn();


		try {
			checkLogin(in);
			
			final BGlAccount gl=new BGlAccount(in.getGlAccountId());
			in.setData(gl);
			gl.update();

			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}

	@WebMethod()
	public SaveProductServiceReturn saveProductService(/*@WebParam(name = "in")*/final SaveProductServiceInput in)	{
		final SaveProductServiceReturn ret=new SaveProductServiceReturn();

		try {
			checkLogin(in);
			
			final BService p=new BService(in.getProductId());
			in.setData(p);
			p.update();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoginReturn login(/*@WebParam(name = "in")*/final LoginInput in)	{
		
		logger.info(in.getUser()+" tried to log in.");
		
		final LoginReturn ret=new LoginReturn();
		
		try
		{
			checkLogin(in);
		
			final BPerson bp=BPerson.getCurrent();
			if (bp.hasMessages())
				ret.setScreen("Message.swf");
		//		ret.setScreenTitle("Messages");
			else
				ret.setScreen("Blank.swf");
		//		ret.setScreenTitle("");
			
	//		ret.setPerson(bp.getPersonId());
			ret.setPersonFName(bp.getFirstName());
			ret.setPersonLName(bp.getLastName());
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
		
	}
	
	
	
	@WebMethod()
	public ListUnexportedInvoicesReturn listUnexportedInvoices(/*@WebParam(name = "in")*/final ListUnexportedInvoicesInput in)	{
		final ListUnexportedInvoicesReturn ret=new ListUnexportedInvoicesReturn();
		
		try {
			checkLogin(in);
			
			final BInvoice b[]=BInvoice.listUnexported(hsu);
			
			ret.setInvoices(b);
			finishService(ret);
			
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public LoadInvoiceReturn loadInvoice(/*@WebParam(name = "in")*/final LoadInvoiceInput in)	{
		final LoadInvoiceReturn ret=new LoadInvoiceReturn();

		try {
			checkLogin(in);
			
			final BInvoice inv=new BInvoice(in.getInvoiceId());

			ret.setData(inv);
			
			ret.setData(inv.getLineItems());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewClientCompanyReturn newClientCompany(/*@WebParam(name = "in")*/final NewClientCompanyInput in)	{
	
		final NewClientCompanyReturn ret=new NewClientCompanyReturn();


		try {
			checkLogin(in);
			
			final BClientCompany bcc=new BClientCompany();
			bcc.create();
			in.makeClientCompany(bcc);
                        ClientStatus cs=hsu.createCriteria(ClientStatus.class)
                                .eq(ClientStatus.SEQ,(short)0)
                                .first();
                        if (cs==null)
                        {
                            BClientStatus bcs=new BClientStatus();
                            bcs.create();
                            bcs.setCode("Default");
                            bcs.setDescription("Default Status");
                            bcs.setInitialSequence(0);
                            bcs.setSeqNo((short)0);
                            bcs.setLastActiveDate(0);
                            bcs.insert();
                            cs=bcs.getBean();
                        }
                        bcc.setClientStatus(cs);
			bcc.insert();
			
			ret.setOrgGroupId(bcc.getOrgGroupId());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}
	
	@WebMethod()
	public MarkInvoicesExportedReturn markInvoicesExported(/*@WebParam(name = "in")*/final MarkInvoicesExportedInput in)	{
		final MarkInvoicesExportedReturn ret=new MarkInvoicesExportedReturn();
		try {
			checkLogin(in);
			
			BInvoice.markExported(hsu,in.getInvoiceIds());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListClientCompaniesReturn listClientCompanies(/*@WebParam(name = "in")*/final ListClientCompaniesInput in)	{
		final ListClientCompaniesReturn ret=new ListClientCompaniesReturn();
		try {
			checkLogin(in);

			ret.setCompanies(BClientCompany.list(hsu, ret.getCap()));
			
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public SaveClientCompanyReturn saveClientCompany(/*@WebParam(name = "in")*/final SaveClientCompanyInput in) {
		final SaveClientCompanyReturn ret=new SaveClientCompanyReturn();
				
		try
		{
			checkLogin(in);
			
			final BCompanyBase bcc=new BClientCompany(in.getOrgGroupId());
			in.makeClientCompany(bcc);
			bcc.update();
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
		
	}
}

/*
 * Peachtree Application		Web Service Dependencies																																																																																																																																																																																																																																																													
																																																																																																																																																																																																																																																															
		AccountingManagementService.listGlAccountsByTypeObj																																																																																																																																																																																																																																																													
		AccountingManagementService.listProductsAndServicesObj																																																																																																																																																																																																																																																													
		AccountingManagementService.newGlAccount																																																																																																																																																																																																																																																													
		AccountingManagementService.newProductService																																																																																																																																																																																																																																																													
		AccountingManagementService.saveGlAccount																																																																																																																																																																																																																																																													
		AccountingManagementService.saveProductService																																																																																																																																																																																																																																																													
		CompanyEditorService.listCompanyByType																																																																																																																																																																																																																																																													
		CompanyEditorService.newCompanyObj																																																																																																																																																																																																																																																													
		CompanyEditorService.saveCompanyObj																																																																																																																																																																																																																																																													
		LoginService.login																																																																																																																																																																																																																																																													
		TimesheetManagementService.listUnexportedInvoices																																																																																																																																																																																																																																																													
		TimesheetManagementService.loadInvoice																																																																																																																																																																																																																																																													

 */	
