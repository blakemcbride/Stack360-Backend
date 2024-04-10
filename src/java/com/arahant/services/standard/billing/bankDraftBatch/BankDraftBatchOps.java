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
package com.arahant.services.standard.billing.bankDraftBatch;
import com.arahant.beans.BankDraftHistory;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.BillingBatchReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingBankDraftBatchOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BankDraftBatchOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BankDraftBatchOps.class);
	
	public BankDraftBatchOps() {
		super();
	}
	
        @WebMethod()
	public ListBatchesReturn listBatches(/*@WebParam(name = "in")*/final ListBatchesInput in)		
	{
		final ListBatchesReturn ret=new ListBatchesReturn();
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();


		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessHR"));
			ret.setCanSeeAllCompanies(BRight.checkRight("CanAccessAllCompanies") == BRight.ACCESS_LEVEL_WRITE);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
        @WebMethod()
	public SearchPersonsForBatchReturn searchPersonsForBatch(/*@WebParam(name = "in")*/final SearchPersonsForBatchInput in)		
	{
		final SearchPersonsForBatchReturn ret=new SearchPersonsForBatchReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BBillingBatch(in.getId()).searchInBatch(in.getFirstName(), in.getLastName(), in.getSearchType(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchPersonsNotForBatchReturn searchPersonsNotForBatch(/*@WebParam(name = "in")*/final SearchPersonsNotForBatchInput in)		
	{
		final SearchPersonsNotForBatchReturn ret=new SearchPersonsNotForBatchReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(new BBillingBatch(in.getId()).searchNotInBatch(in.getFirstName(),in.getLastName(),in.getSearchType(),ret.getCap()),in.getId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public AddPersonsToBatchReturn addPersonsToBatch(/*@WebParam(name = "in")*/final AddPersonsToBatchInput in)		
	{
		final AddPersonsToBatchReturn ret=new AddPersonsToBatchReturn();
		try
		{
			checkLogin(in);
			
			new BBillingBatch(in.getId()).addToBatch(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public RemovePersonsFromBatchReturn removePersonsFromBatch(/*@WebParam(name = "in")*/final RemovePersonsFromBatchInput in)		
	{
		final RemovePersonsFromBatchReturn ret=new RemovePersonsFromBatchReturn();
		try
		{
			checkLogin(in);
			
			new BBillingBatch(in.getId()).removeFromBatch(in.getIds());
			
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
			
			ret.setReportUrl(new BillingBatchReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteBatchesReturn deleteBatches(/*@WebParam(name = "in")*/final DeleteBatchesInput in)		
	{
		final DeleteBatchesReturn ret=new DeleteBatchesReturn();
		try
		{
			checkLogin(in);
			
			BBillingBatch.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewBatchReturn newBatch(/*@WebParam(name = "in")*/final NewBatchInput in)		
	{
		final NewBatchReturn ret=new NewBatchReturn();
		try
		{
			checkLogin(in);
			
			final BBillingBatch x=new BBillingBatch();
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
	public SaveBatchReturn saveBatch(/*@WebParam(name = "in")*/final SaveBatchInput in)		
	{
		final SaveBatchReturn ret=new SaveBatchReturn();
		try
		{
			checkLogin(in);
			
			final BBillingBatch x=new BBillingBatch(in.getId());
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
	public GetBatchAmountReturn getBatchAmount(/*@WebParam(name = "in")*/final GetBatchAmountInput in)		
	{
		final GetBatchAmountReturn ret=new GetBatchAmountReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BBillingBatch(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public PostBatchReturn postBatch(/*@WebParam(name = "in")*/final PostBatchInput in)		
	{
		final PostBatchReturn ret=new PostBatchReturn();
		try
		{
			checkLogin(in);
			
						
			//check history to see if this batch has been done
			if (hsu.createCriteria(BankDraftHistory.class)
					.eq(BankDraftHistory.DATE, in.getDate())
					.exists())
				throw new ArahantWarning("Batch has already been posted for that date.");
			
			final BBillingBatch x=new BBillingBatch(in.getId());
			x.postBatch(in.getAmountCheck(),in.getConfirmationNumber(),in.getDate());
			//x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListBatchHistoryReturn listBatchHistory(/*@WebParam(name = "in")*/final ListBatchHistoryInput in)		
	{
		final ListBatchHistoryReturn ret=new ListBatchHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBankDraftHistory.list(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
