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
package com.arahant.services.standard.hr.employeeBilling;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.EmployeeBillingAdjustmentReport;
import com.arahant.reports.EmployeeBillingInvoiceReport;
import com.arahant.reports.EmployeeBillingPaymentReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import java.util.Date;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmployeeBillingOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeBillingOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeBillingOps.class);
	
	public EmployeeBillingOps() {
		super();
	}
	
	
	
	/*
	 * loadBilling
    input: personId (string - required), fromDate (number - optional), toDate (number - optional)
    output: balance (number), item (array of id - string, date - number, amount - number, type - string, accountingInvoiceId - string, paymentType - string, detail - string)
    remarks:
        - sorted by most recent
        - capped at 50, but should return 50 most recent
      
	 */ 
        @WebMethod()
	public LoadBillingReturn loadBilling(/*@WebParam(name = "in")*/final LoadBillingInput in)		
	{
		final LoadBillingReturn ret=new LoadBillingReturn();
		try
		{
			checkLogin(in);
/*
			BInvoice []invoices=BInvoice.searchPersonInvoices(in.getFromDate(), in.getToDate(), false, in.getPersonId(),50);
			BReceipt []receipts=BReceipt.searchPersonInvoices(in.getFromDate(), in.getToDate(), in.getPersonId(), 50);
			
			List <LoadBillingReturnItem> l=new ArrayList<LoadBillingReturnItem>(invoices.length+receipts.length);
			for (int loop=0;loop<invoices.length;loop++)
				l.add(new LoadBillingReturnItem(invoices[loop]));
			for (int loop=0;loop<receipts.length;loop++)
				l.add(new LoadBillingReturnItem(receipts[loop]));
			
			Collections.sort(l);
			
			LoadBillingReturnItem []bilRet=new LoadBillingReturnItem[(l.size()<50)?l.size():50];
			
			for (int loop=0; loop<bilRet.length; loop++)
				bilRet[loop]=l.get(loop);
			
			ret.setItem(bilRet);
			
			*/
			BPerson subjectPerson=new BPerson(in.getPersonId());
			ret.setBankDraftBatchId(subjectPerson.getBankDraftBatchId());
			ret.setBankDraftBatchDescription(subjectPerson.getBankDraftBatchDescription());
			ret.setBillingStatusName(subjectPerson.getBillingStatusName());
			ret.setBalance(subjectPerson.getBalance());
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
       
        @WebMethod()
	public NewPaymentReturn newPayment(/*@WebParam(name = "in")*/final NewPaymentInput in)		
	{
		final NewPaymentReturn ret=new NewPaymentReturn();
		try
		{
			checkLogin(in);
			
			final BReceipt x=new BReceipt();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			for (int loop=0;loop<in.getItem().length;loop++)
			{
				BInvoice bi=new BInvoice(in.getItem()[loop].getId());
				bi.applyPayment(x, in.getItem()[loop].getAmount());
				bi.update();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SavePaymentReturn savePayment(/*@WebParam(name = "in")*/final SavePaymentInput in)		
	{
		final SavePaymentReturn ret=new SavePaymentReturn();
		try
		{
			checkLogin(in);
			
			final BReceipt x=new BReceipt(in.getId());
			in.setData(x);
			x.update();
			
			x.deleteAppliedPayments();//I'll add them right back
			
			for (int loop=0;loop<in.getItem().length;loop++)
			{
				BInvoice bi=new BInvoice(in.getItem()[loop].getId());
				bi.applyPayment(x, in.getItem()[loop].getAmount());
				bi.update();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	

        @WebMethod()
	public ListBankDraftBatchesReturn listBankDraftBatches(/*@WebParam(name = "in")*/final ListBankDraftBatchesInput in)		
	{
		final ListBankDraftBatchesReturn ret=new ListBankDraftBatchesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBillingBatch.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
        @WebMethod()
	public ListBillingStatusHistoryReturn listBillingStatusHistory(/*@WebParam(name = "in")*/final ListBillingStatusHistoryInput in)		
	{
		final ListBillingStatusHistoryReturn ret=new ListBillingStatusHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBillingStatusHistory.list(in.getPersonId(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewBillingStatusHistoryReturn newBillingStatusHistory(/*@WebParam(name = "in")*/final NewBillingStatusHistoryInput in)		
	{
		final NewBillingStatusHistoryReturn ret=new NewBillingStatusHistoryReturn();
		try
		{
			checkLogin(in);
			
			final BBillingStatusHistory x=new BBillingStatusHistory();
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
	public SaveBillingStatusHistoryReturn saveBillingStatusHistory(/*@WebParam(name = "in")*/final SaveBillingStatusHistoryInput in)		
	{
		final SaveBillingStatusHistoryReturn ret=new SaveBillingStatusHistoryReturn();
		try
		{
			checkLogin(in);
			
			final BBillingStatusHistory x=new BBillingStatusHistory(in.getId());
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
	public SearchBillingStatusesReturn searchBillingStatuses(/*@WebParam(name = "in")*/final SearchBillingStatusesInput in)		
	{
		final SearchBillingStatusesReturn ret=new SearchBillingStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBillingStatus.search(in.getName(),ret.getHighCap()));
			
			if (!isEmpty(in.getId()))
			{
				BBillingStatusHistory hist=new BBillingStatusHistory(in.getId());
				BBillingStatus stat=hist.getBillingStatus();
				ret.setSelectedItem(new SearchBillingStatusesReturnItem(stat));
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteBillingStatusHistoriesReturn deleteBillingStatusHistories(/*@WebParam(name = "in")*/final DeleteBillingStatusHistoriesInput in)		
	{
		final DeleteBillingStatusHistoriesReturn ret=new DeleteBillingStatusHistoriesReturn();
		try
		{
			checkLogin(in);
			
			BBillingStatusHistory.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchPaymentsReturn searchPayments(/*@WebParam(name = "in")*/final SearchPaymentsInput in)		
	{
		final SearchPaymentsReturn ret=new SearchPaymentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BReceipt.search(in.getFromDate(), in.getToDate(), in.getPersonId(), in.getExcludeZeroBalance(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchInvoicesReturn searchInvoices(/*@WebParam(name = "in")*/final SearchInvoicesInput in)		
	{
		final SearchInvoicesReturn ret=new SearchInvoicesReturn();
		try
		{
			checkLogin(in);

			String []persons;
			if (!isEmpty(in.getPersonId()))
				persons=new String[]{in.getPersonId()};
			else
				persons=new String[0];
			ret.setItem(BInvoice.searchPersonInvoices(in.getFromDate(), in.getToDate(), in.getExcludeZeroBalance(), persons, null,ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchAdjustmentsReturn searchAdjustments(/*@WebParam(name = "in")*/final SearchAdjustmentsInput in)		
	{
		final SearchAdjustmentsReturn ret=new SearchAdjustmentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BReceipt.searchAdjustments(in.getFromDate(),in.getToDate(),in.getPersonId(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetInvoiceReportReturn getInvoiceReport(/*@WebParam(name = "in")*/final GetInvoiceReportInput in)		
	{
		final GetInvoiceReportReturn ret=new GetInvoiceReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new EmployeeBillingInvoiceReport().getReport(in.getFromDate(),in.getToDate(),in.getPersonId(),in.getIds(),in.getExcludeZeroBalance()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetPaymentReportReturn getPaymentReport(/*@WebParam(name = "in")*/final GetPaymentReportInput in)		
	{
		final GetPaymentReportReturn ret=new GetPaymentReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new EmployeeBillingPaymentReport().getReport(in.getFromDate(),in.getToDate(),in.getPersonId(),in.getIds(),in.getExcludeZeroBalance()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetAdjustmentReportReturn getAdjustmentReport(/*@WebParam(name = "in")*/final GetAdjustmentReportInput in)		
	{
		final GetAdjustmentReportReturn ret=new GetAdjustmentReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new EmployeeBillingAdjustmentReport().getReport(in.getFromDate(),in.getToDate(),in.getPersonId(),in.getIds()));

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public SearchProductsAndServicesReturn searchProductsAndServices(/*@WebParam(name = "in")*/final SearchProductsAndServicesInput in)	{
		final SearchProductsAndServicesReturn ret=new SearchProductsAndServicesReturn();

		try {
			checkLogin(in);
			
			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(),ret.getHighCap()));
			
			if (!isEmpty(in.getId()))
			{
				try
				{
					ret.setSelectedItem(new SearchProductsAndServicesReturnItem(new BService(in.getId())));
				}
				catch (Exception e)
				{
				
				}
			}
			finishService(ret);
	
		} catch (final Throwable e) {
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
			
			if (!isEmpty(in.getSelectFromId()))
				ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(in.getSelectFromId())));
			
			if (!isEmpty(in.getSelectFromInvoiceId()))
			{
				BInvoice inv=new BInvoice(in.getSelectFromInvoiceId());
				ret.setSelectedItem(new SearchGLAccountsReturnItem(inv.getGLAccount()));
			}
			
			if (in.getSelectFromCompanyDefault())
			{
				BCompany company = new BCompany(hsu.getCurrentCompany());
				if (isEmpty(company.getEmployeeAdvanceAccountId()))
					throw new ArahantException("Company employee advance GL account must be set up.");
				ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(company.getEmployeeAdvanceAccountId())));
			}
			if (!isEmpty(in.getSelectFromProductServiceId()))
			{
				try {
					ret.setSelectedItem(new SearchGLAccountsReturnItem(new BService(in.getSelectFromProductServiceId()).getGlAccount()));
				} catch (Exception e) {
				}
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public LoadInvoiceReturn loadInvoice(/*@WebParam(name = "in")*/final LoadInvoiceInput in)		
	{
		final LoadInvoiceReturn ret=new LoadInvoiceReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BInvoice(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewInvoiceReturn newInvoice(/*@WebParam(name = "in")*/final NewInvoiceInput in)		
	{
		final NewInvoiceReturn ret=new NewInvoiceReturn();
		try
		{
			checkLogin(in);
			
			final BInvoice x=new BInvoice();
			ret.setId(x.create(new Date()));
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
	public SaveInvoiceReturn saveInvoice(/*@WebParam(name = "in")*/final SaveInvoiceInput in)		
	{
		final SaveInvoiceReturn ret=new SaveInvoiceReturn();
		try
		{
			checkLogin(in);
			
			final BInvoice x=new BInvoice(in.getId());
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
	public LoadPaymentReturn loadPayment(/*@WebParam(name = "in")*/final LoadPaymentInput in)		
	{
		final LoadPaymentReturn ret=new LoadPaymentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BReceipt(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


        @WebMethod()
	public SearchInvoicesForPaymentReturn searchInvoicesForPayment(/*@WebParam(name = "in")*/final SearchInvoicesForPaymentInput in)		
	{
		final SearchInvoicesForPaymentReturn ret=new SearchInvoicesForPaymentReturn();
		try
		{
			checkLogin(in);

			/*
			 *             - sort by date
            - capped at 50
            - always exclude invoices with a zero balance, unless they are associated to the paymentId (if passed), then they do need to get returned
            - exclude invoices specified in excludeIds (even if they got include from results of previous line)
            - total is how much they will be billed for (calculated by looking at line items, adjustments)
            - balance is how much of the invoice is unpaid, if any (calculated by looking at invoice total, adjustments, payments)
			 **/
			BInvoice [] ar=BInvoice.search(in.getExcludeIds(), in.getFromDate(), in.getToDate(),in.getPersonId(),in.getPaymentId(),ret.getCap());
			ret.setItem(ar);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeletePaymentsReturn deletePayments(/*@WebParam(name = "in")*/final DeletePaymentsInput in)		
	{
		final DeletePaymentsReturn ret=new DeletePaymentsReturn();
		try
		{
			checkLogin(in);
			
			BReceipt.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteInvoicesReturn deleteInvoices(/*@WebParam(name = "in")*/final DeleteInvoicesInput in)		
	{
		final DeleteInvoicesReturn ret=new DeleteInvoicesReturn();
		try
		{
			checkLogin(in);
			
			BInvoice.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
