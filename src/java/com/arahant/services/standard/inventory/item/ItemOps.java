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
package com.arahant.services.standard.inventory.item;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ItemReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryItemOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ItemOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ItemOps.class);
	
	public ItemOps() {
		super();
	}
	
    @WebMethod()
	public SearchItemsReturn searchItems(/*@WebParam(name = "in")*/final SearchItemsInput in)		
	{
		final SearchItemsReturn ret=new SearchItemsReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(BItem.search(in.getSearchMetaInput(),in.getLotId(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewItemReturn newItem(/*@WebParam(name = "in")*/final NewItemInput in)		
	{
		final NewItemReturn ret=new NewItemReturn();
		try
		{
			checkLogin(in);
			
			final BItem x=new BItem();
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
	public SaveItemReturn saveItem(/*@WebParam(name = "in")*/final SaveItemInput in)		
	{
		final SaveItemReturn ret=new SaveItemReturn();
		try
		{
			checkLogin(in);
			
			final BItem x=new BItem(in.getId());
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
	public LoadItemReturn loadItem(/*@WebParam(name = "in")*/final LoadItemInput in)		
	{
		final LoadItemReturn ret=new LoadItemReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BItem(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadItemHistoryReturn loadItemHistory(/*@WebParam(name = "in")*/final LoadItemHistoryInput in)		
	{
		final LoadItemHistoryReturn ret=new LoadItemHistoryReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BItemH(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchItemHistoryReturn searchItemHistory(/*@WebParam(name = "in")*/final SearchItemHistoryInput in)		
	{
		final SearchItemHistoryReturn ret=new SearchItemHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BItemH.search(in.getId(),in.getStartDate(),in.getEndDate(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteItemsReturn deleteItems(/*@WebParam(name = "in")*/final DeleteItemsInput in)		
	{
		final DeleteItemsReturn ret=new DeleteItemsReturn();
		try
		{
			checkLogin(in);
			
			BItem.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchLotsReturn searchLots(/*@WebParam(name = "in")*/final SearchLotsInput in)		
	{
		final SearchLotsReturn ret=new SearchLotsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BLot.search(in.getLotNumber(),in.getProductId(),ret.getHighCap()));
			
			// populate the return item
			if (!isEmpty(in.getSelectFromItemId()))
			{
				BLot blot=new BItem(in.getSelectFromItemId()).getLot();
				if (!isEmpty(blot.getLotNumber()))
					ret.setSelectedItem(new SearchLotsReturnItem(blot));

			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)
	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOrgGroup.search(in.getName(),COMPANY_TYPE,ret.getHighCap()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProductsReturn searchProducts(/*@WebParam(name = "in")*/final SearchProductsInput in)
	{
		final SearchProductsReturn ret=new SearchProductsReturn();
		try
		{
			checkLogin(in);


			ret.setItem(BProduct.search(in.getDescription(), in.getSku(), ret.getHighCap()));

			// populate the return item
			if (!isEmpty(in.getSelectFromLotId()))
				ret.setSelectedItem(new SearchProductsReturnItem(new BLot(in.getSelectFromLotId()).getProduct()));
			if (!isEmpty(in.getSelectFromItemId()))
				ret.setSelectedItem(new SearchProductsReturnItem(new BItem(in.getSelectFromItemId()).getProduct()));
			if (!isEmpty(in.getSelectFromProductId()))
				ret.setSelectedItem(new SearchProductsReturnItem(new BProduct(in.getSelectFromProductId())));
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)		
	{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(),in.getLastName(),"",ret.getHighCap()));
			
			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchPersonsReturnItem(new BEmployee(in.getId())));

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


			ret.setReportUrl(new ItemReport().build(in.getLotId(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
