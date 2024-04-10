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
package com.arahant.services.standard.inventory.productInventory;
import com.arahant.beans.Item;
import com.arahant.beans.Lot;
import com.arahant.beans.OrgGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProductInventoryReport;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.Collections;
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
 * This screen has been split into other screens
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryProductInventoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProductInventoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProductInventoryOps.class);
	
	public ProductInventoryOps() {
		super();
	}
	
    @WebMethod()
	public SearchProductInventoryReturn searchProductInventory(/*@WebParam(name = "in")*/final SearchProductInventoryInput in)		
	{
		final SearchProductInventoryReturn ret=new SearchProductInventoryReturn();
		try
		{
			checkLogin(in);


			ret.setItem(BItem.search (in.getLotNumber(),in.getOrgGroupId(), in.getProductAvailable(),
					in.getProductId(),in.getProductTypeIds(),in.getSerialNumber(),
					in.getVendorIds(),
					new String[]{in.getExcludeId()},
					in.getSearchMetaInput()));

			if (!isEmpty(in.getSelectParentOfId()))
			{
				BItem item=new BItem(in.getSelectParentOfId());
				if (item.getParent()!=null)
					ret.setSelectedItem(new SearchProductInventoryReturnItem(item.getParent()));
			}
			else
				if (!isEmpty(in.getSelectFromId()))
				{
					ret.setSelectedItem(new SearchProductInventoryReturnItem(new BItem(in.getSelectFromId())));
				}
			
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

			if (!isEmpty(in.getSelectFromProductInventoryId()))
			{
				BItem inv=new BItem(in.getSelectFromProductInventoryId());
				ret.setSelectedItem(new SearchProductsReturnItem(inv.getProduct()));
			}
			
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
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)		
	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOrgGroup.search(in.getName(),COMPANY_TYPE,ret.getHighCap()));

			if (!isEmpty(in.getSelectFromProductInventoryId()))
			{
				BItem inv=new BItem(in.getSelectFromProductInventoryId());
				if (inv.getLocation()!=null)
					ret.setSelectedItem(new SearchOrgGroupsReturnItem(inv.getLocation()));

			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchProductTypesReturn searchProductTypes(/*@WebParam(name = "in")*/final SearchProductTypesInput in)		
	{
		final SearchProductTypesReturn ret=new SearchProductTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProductType.search(in.getDescription(),in.getExcludeIds(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchVendorsReturn searchVendors(/*@WebParam(name = "in")*/final SearchVendorsInput in)		
	{
		final SearchVendorsReturn ret=new SearchVendorsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BVendorCompany.searchVendors(in.getName(), in.getExcludeIds(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
    public DeleteProductInventoryReturn deleteProductInventory(/*@WebParam(name = "in")*/final DeleteProductInventoryInput in)		
	{
		final DeleteProductInventoryReturn ret=new DeleteProductInventoryReturn();
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
	public LoadProductInventoryReturn loadProductInventory(/*@WebParam(name = "in")*/final LoadProductInventoryInput in)		
	{
		final LoadProductInventoryReturn ret=new LoadProductInventoryReturn();
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
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new ProductInventoryReport().build(in.getLotNumber(),in.getOrgGroupId(),
					in.getProductId(),in.getProductTypeIds(),in.getSerialNumber(),in.getVendorIds(),
					in.getProductAvailable()));
			
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
	public SearchProductInventoryHistoryReturn searchProductInventoryHistory(/*@WebParam(name = "in")*/final SearchProductInventoryHistoryInput in)		
	{
		final SearchProductInventoryHistoryReturn ret=new SearchProductInventoryHistoryReturn();
		try
		{
			checkLogin(in);

			if (in.getStartDate()==0)
				in.setStartDate(19000101);
			if (in.getEndDate()==0)
				in.setEndDate(30000101);

			ArrayList <SearchProductInventoryHistoryReturnItem> items=new ArrayList<SearchProductInventoryHistoryReturnItem>();

			Item item=hsu.createCriteria(Item.class)
				.eq(Item.ID,in.getId())
				.dateBetween(Item.HISTORY_DATE, DateUtils.getDate(in.getStartDate()), DateUtils.getDate(in.getEndDate()))
				.first();

			int cap=ret.getCap();
			
			if (item!=null)
			{
				cap--;
				items.add(new SearchProductInventoryHistoryReturnItem(new BItem(item)));
			}

			BItemH [] hists=BItemH.search(in.getId(),in.getStartDate(),in.getEndDate(),cap);

			for (int loop=0;loop<hists.length;loop++)
				items.add(new SearchProductInventoryHistoryReturnItem(hists[loop]));

			ret.setItem(items.toArray(new SearchProductInventoryHistoryReturnItem[items.size()]));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadProductInventoryHistoryReturn loadProductInventoryHistory(/*@WebParam(name = "in")*/final LoadProductInventoryHistoryInput in)		
	{
		final LoadProductInventoryHistoryReturn ret=new LoadProductInventoryHistoryReturn();
		try
		{
			checkLogin(in);
			
			if (in.getId().indexOf('-')!=-1)
				ret.setData(new BItem(in.getId()));
			else
				ret.setData(new BItemH(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListProductInventoryLocationReturn listProductInventoryLocation(/*@WebParam(name = "in")*/final ListProductInventoryLocationInput in)		
	{
		final ListProductInventoryLocationReturn ret=new ListProductInventoryLocationReturn();
		try
		{
			checkLogin(in);

			if (isEmpty(in.getId()))
			{
				ret.setItem(new ListProductInventoryLocationReturnItem[0]);
			}
			else
			{
				BItem bi=new BItem(in.getId());


				ArrayList<ListProductInventoryLocationReturnItem> l=new ArrayList<ListProductInventoryLocationReturnItem>();

				while (bi.getParent()!=null)
				{
					l.add(new ListProductInventoryLocationReturnItem(bi.getParent()));
					bi=bi.getParent();
				}

				l.add(new ListProductInventoryLocationReturnItem(bi.getLocation()));

				//need to reverse the list now
				Collections.reverse(l);

				l.add(new ListProductInventoryLocationReturnItem(new BItem(in.getId())));

				ret.setItem(l.toArray(new ListProductInventoryLocationReturnItem[l.size()]));
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public AssociateProductInventoryReturn associateProductInventory(/*@WebParam(name = "in")*/final AssociateProductInventoryInput in)		
	{
		final AssociateProductInventoryReturn ret=new AssociateProductInventoryReturn();
		try
		{
			checkLogin(in);

			BItem child=new BItem(in.getChildId());

			if (in.getBreakQuantity()==0)
			{
				child.setParentItemId(in.getParentId());
				child.setLocation("");
			}
			else
			{
				child.splitTo(in.getParentId(),in.getBreakQuantity());
			}

			child.update();
			
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

			if (isEmpty(in.getLotNumberId()))
			{
				BLot l=new BLot();
				in.setLotNumberId(l.create());
				in.setData(l);
				l.insert();
			}

			final BItem x=new BItem();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			new BLot(in.getLotNumberId()).update();
			
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

			ret.setItem(BItem.search(in.getProductId(),in.getLotNumber() ,ret.getHighCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewLotReturn newLot(/*@WebParam(name = "in")*/final NewLotInput in)		
	{
		final NewLotReturn ret=new NewLotReturn();
		try
		{
			checkLogin(in);

			BLot l=new BLot();
			l.create();
			in.setData(l);
			l.insert();


			if (in.getSerialNumber().length==0)
			{
				final BItem x=new BItem();
				ret.setId(x.create());
				in.setData(x);
				x.setLot(l);
				x.insert();
			}
			else
			{
				for (String serno : in.getSerialNumber())
				{
					final BItem x=new BItem();
					ret.setId(x.create());
					in.setData(x);
					x.setSerialNumber(serno);
					x.setLot(l);
					x.insert();
				}
			}

			l.update();

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
	public SaveLotReturn saveLot(/*@WebParam(name = "in")*/final SaveLotInput in)		
	{
		final SaveLotReturn ret=new SaveLotReturn();
		try
		{
			checkLogin(in);
			
			final BLot x=new BLot(in.getId());
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
	public DisassociateProductInventoryItemReturn disassociateProductInventoryItem(/*@WebParam(name = "in")*/final DisassociateProductInventoryItemInput in)		
	{
		final DisassociateProductInventoryItemReturn ret=new DisassociateProductInventoryItemReturn();
		try
		{
			checkLogin(in);
			
			/*
			 *             - removing a child's link to it's parent, and putting it back in to a location
            - should you recombine them with items out there that have same org group id if they are in same lot ? YES
			 * */

			for (String id : in.getChildIds())
			{
				BItem i=new BItem(id);
		
				HibernateCriteriaUtil<Item> hcu=hsu.createCriteria(Item.class);
				hcu.joinTo(Item.LOT).eq(Lot.LOT_ID, i.getLotId());
				hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, in.getOrgGroupId());

				Item loki=hcu.first();
				if (loki!=null)
				{
					loki.setQuantity(i.getQuantity()+loki.getQuantity());
					hsu.saveOrUpdate(loki);
					i.delete();
				}
				else
				{
					i.setParentItemId(null);
					i.setLocation(in.getOrgGroupId());
					i.update();
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
