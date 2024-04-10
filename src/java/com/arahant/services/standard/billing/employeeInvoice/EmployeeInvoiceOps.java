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
package com.arahant.services.standard.billing.employeeInvoice;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.EmployeeInvoiceReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingEmployeeInvoiceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeInvoiceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeInvoiceOps.class);
	
	public EmployeeInvoiceOps() {
		super();
	}
	

	@WebMethod()
	public SearchInvoicesReturn searchInvoices(/*@WebParam(name = "in")*/final SearchInvoicesInput in)		
	{
		final SearchInvoicesReturn ret=new SearchInvoicesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BInvoice.searchPersonInvoices(in.getFromDate(), in.getToDate(), in.getExcludeZeroBalance(), null, null, ret.getCap()));
			
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
			
			ret.setReportUrl(new EmployeeInvoiceReport().build(in.getIds(),in.getFromDate(),in.getToDate(),in.getExcludeZeroBalance()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GenerateInvoicesReturn generateInvoices(/*@WebParam(name = "in")*/final GenerateInvoicesInput in)		
	{
		final GenerateInvoicesReturn ret=new GenerateInvoicesReturn();
		try
		{
			checkLogin(in);
			
			new InvoicesThread(hsu.getCurrentPerson().getPersonId(), in.getDate()).start();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
		
	private static class InvoicesThread extends AsynchRequest
	{
		private int date;
		
		public InvoicesThread (String currentPersonId, int date)
		{

			super(currentPersonId);
			this.date=date;
			
		}

		public void doRequest()
		{

			BInvoice.makeInvoicesForPersons(date);
			
			BMessage.send(hsu.getCurrentPerson(), hsu.getCurrentPerson(), "Invoices created","Your invoices have been created.");

		}
		
	}
	

}
