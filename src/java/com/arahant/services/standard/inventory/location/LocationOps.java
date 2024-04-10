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
package com.arahant.services.standard.inventory.location;
import com.arahant.beans.Item;
import com.arahant.beans.Lot;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Product;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.LocationItemReport;
import com.arahant.reports.LocationProductReport;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryLocationOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class LocationOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			LocationOps.class);
	
	public LocationOps() {
		super();
	}
	
    @WebMethod()
	public SearchLocationsReturn searchLocations(/*@WebParam(name = "in")*/final SearchLocationsInput in)		
	{
		final SearchLocationsReturn ret=new SearchLocationsReturn();
		try
		{
			checkLogin(in);

			HibernateCriteriaUtil<Item> hcu=hsu.createCriteria(Item.class);

			if (!isEmpty(in.getProductId()))
				hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID,in.getProductId());

			if (!isEmpty(in.getOrgGroupId()))
				hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, in.getOrgGroupId());

			hcu.isNull(Item.PARENT);

			HibernateScrollUtil <Item> scr=hcu.scroll();

			HashMap<Product,HashMap<OrgGroup,Integer>> map=new HashMap<Product, HashMap<OrgGroup, Integer>>();

			while (scr.next())
			{
				Item i=scr.get();

				HashMap<OrgGroup,Integer> pmap=map.get(i.getProduct());

				if (pmap==null)
				{
					pmap=new HashMap<OrgGroup, Integer>();
					pmap.put(i.getLocation(), i.getQuantity());
					map.put(i.getProduct(), pmap);
				}
				else
				{
					Integer x=pmap.get(i.getLocation());
					if (x==null)
						pmap.put(i.getLocation(), i.getQuantity());
					else
						pmap.put(i.getLocation(), x+i.getQuantity());
				}

			}

			List<Product> prods=new ArrayList<Product>(map.keySet().size());
			prods.addAll(map.keySet());
			Collections.sort(prods);

			ArrayList<SearchLocationsReturnItem> rl=new ArrayList<SearchLocationsReturnItem>();

			for (Product p : prods)
			{
				HashMap<OrgGroup,Integer> m=map.get(p);
				List<OrgGroup> orgs=new ArrayList<OrgGroup>(m.keySet().size());
				orgs.addAll(m.keySet());
				Collections.sort(orgs);

				for (OrgGroup o : orgs)
				{
					SearchLocationsReturnItem ri=new SearchLocationsReturnItem();
					ri.setOrgGroupId(o.getOrgGroupId());
					ri.setOrgGroupName(o.getName());
					ri.setProductDescription(p.getDescription());
					ri.setProductId(p.getProductId());
					ri.setQuantity(m.get(o));
					rl.add(ri);
				}
			}

			ret.setItem(rl.toArray(new SearchLocationsReturnItem[rl.size()]));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchItemsReturn searchItems(/*@WebParam(name = "in")*/final SearchItemsInput in)		
	{
		final SearchItemsReturn ret=new SearchItemsReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(BItem.search(in.getSearchMetaInput(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber(),ret.getCap(),in.getExcludeId(), in.getLotId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public GetItemReportReturn getItemReport(/*@WebParam(name = "in")*/final GetItemReportInput in)		
	{
		final GetItemReportReturn ret=new GetItemReportReturn();
		try
		{
			checkLogin(in);


			ret.setReportUrl(new LocationItemReport().build(in.getProductId(),in.getOrgGroupId(),in.getSerialNumber()));

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public GetLocationReportReturn getLocationReport(/*@WebParam(name = "in")*/final GetLocationReportInput in)		
	{
		final GetLocationReportReturn ret=new GetLocationReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new LocationProductReport().build(in.getProductId(),in.getOrgGroupId()));
			
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
	public ListOrgGroupsAndItemsReturn listOrgGroupsAndItems(/*@WebParam(name = "in")*/final ListOrgGroupsAndItemsInput in)		
	{
		final ListOrgGroupsAndItemsReturn ret=new ListOrgGroupsAndItemsReturn();
		try
		{
			checkLogin(in);

			/*
			 *    - type is 0 for empty, 1 for org group, 2 for inventory item
                - 0 - list all top level org groups
                - 1 - list all sub org groups of the specified org group and inventory items in the specified org group
                - 2 - list all child inventory items of the specified inventory item
			 *
			 */

			switch (in.getType())
			{
				case 0 : ret.setItem(BOrgGroup.listTopLevel(new String[0]));
					break;
				case 1 :
					{
						ArrayList<ListOrgGroupsAndItemsReturnItem> l=new ArrayList<ListOrgGroupsAndItemsReturnItem>(50);
						BOrgGroup borg=new BOrgGroup(in.getId());
						for (BOrgGroup b :borg.getChildren(new String[0]))
							l.add(new ListOrgGroupsAndItemsReturnItem(b));
						
						for (BItem i : BItem.getItemsAt(in.getId()))
							l.add(new ListOrgGroupsAndItemsReturnItem(i));

						ret.setItem(l.toArray(new ListOrgGroupsAndItemsReturnItem[l.size()]));
					}
					break;
				case 2 : ret.setItem(new BItem(in.getId()).getChildren());
					break;
				default : throw new ArahantException("Unknown level requested "+in.getType());
			}

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public MoveItemsReturn moveItems(/*@WebParam(name = "in")*/final MoveItemsInput in)		
	{
		final MoveItemsReturn ret=new MoveItemsReturn();
		try
		{
			checkLogin(in);
		/*
		 *             - fromProductId & fromOrgGroupId & moveQuantity will be passed ... OR... moveItemId will be passed
            - toOrgGroupId will be passed ... OR ... toItemId will be passed
            - moves a single item (moveItemId) or quantity of items (fromProductId, fromOrgGroupId, moveQuantity) from one location to another location (toOrgGroupId) OR as a child of an item (toItemId)
            - you would need to "extract" items out of the from location (verify there are still enough and throw error if not)
            - you may also need to take the new item and "combine" it in to an existing item if it gets moved to a location or item with the same product that is not a serial'd item (?)
		 **/

			if (!isEmpty(in.getMoveItemId()))
			{
				BItem bi=new BItem(in.getMoveItemId());
				if (!isEmpty(in.getToItemId()))
				{
					bi.setParentItemId(in.getToItemId());
					bi.setLocation("");
				}
				else
				{
					if (isEmpty(bi.getSerialNumber()))
					{
						HibernateCriteriaUtil<Item> hcu=hsu.createCriteria(Item.class);
						hcu.joinTo(Item.LOT).eq(Lot.LOT_ID, bi.getLotId());
						hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, in.getToOrgGroupId());

						Item loki=hcu.first();
						if (loki!=null)
						{
							loki.setQuantity(bi.getQuantity()+loki.getQuantity());
							hsu.saveOrUpdate(loki);
							bi.delete();
						}
						else
						{
							bi.setParentItemId(null);
							bi.setLocation(in.getToOrgGroupId());
							bi.update();
						}
					}
					else
					{
						bi.setParentItemId(null);
						bi.setLocation(in.getToOrgGroupId());
						bi.update();
					}
				}
			}
			else
			{
				HibernateCriteriaUtil<Item> hcu=hsu.createCriteria(Item.class);
				hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID,in.getFromOrgGroupId());
				hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID, in.getFromProductId());
				Item i=hcu.first();
				if (i==null)
					throw new ArahantException("Item not found");
				if (i.getQuantity()<in.getMoveQuantity())
					throw new ArahantException("Insufficient items at that location.  Requested to move "+in.getMoveQuantity()+", but there were only "+i.getQuantity());

				BItem bi=new BItem(i);
				if (!isEmpty(in.getToItemId()))
				{
					bi.splitTo(in.getToItemId(), in.getMoveQuantity());
				}
				else
				{
					HibernateCriteriaUtil<Item> dhcu=hsu.createCriteria(Item.class);
					dhcu.joinTo(Item.LOT).eq(Lot.LOT_ID, bi.getLotId());
					dhcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, in.getToOrgGroupId());

					Item loki=dhcu.first();
					if (loki!=null)
					{
						loki.setQuantity(in.getMoveQuantity()+loki.getQuantity());
						hsu.saveOrUpdate(loki);

						if (bi.getQuantity()==in.getMoveQuantity())
							bi.delete();
						else
						{
							bi.setQuantity(bi.getQuantity()-in.getMoveQuantity());
							bi.update();
						}

					}
					else
					{
						if (bi.getQuantity()==in.getMoveQuantity())
						{
							bi.setParentItemId(null);
							bi.setLocation(in.getToOrgGroupId());
							bi.update();
						}
						else
						{
							bi.setQuantity(bi.getQuantity()-in.getMoveQuantity());
							bi.update();

							BItem n=new BItem();
							n.create();
							n.setDescription(bi.getDescription());
							n.setLocation(in.getToOrgGroupId());
							n.setLot(bi.getLot());
							n.setQuantity(in.getMoveQuantity());
							n.setSerialNumber(bi.getSerialNumber());
							n.setProductId(bi.getProductId());

							n.insert();
						}

					}
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
	public ListPathForOrgGroupOrItemReturn listPathForOrgGroupOrItem(/*@WebParam(name = "in")*/final ListPathForOrgGroupOrItemInput in)		
	{
		final ListPathForOrgGroupOrItemReturn ret=new ListPathForOrgGroupOrItemReturn();
		try
		{
			checkLogin(in);

			ArrayList<ListPathForOrgGroupOrItemReturnItem> l=new ArrayList<ListPathForOrgGroupOrItemReturnItem>();

			BOrgGroup borg;
			if (in.getType()==2)
			{
				BItem bi=new BItem(in.getId());

				l.add(new ListPathForOrgGroupOrItemReturnItem(bi));

				while (bi.getParent()!=null)
				{
					l.add(new ListPathForOrgGroupOrItemReturnItem(bi.getParent()));
					bi=bi.getParent();
				}

				borg=bi.getLocation();
			}
			else
			{
				borg=new BOrgGroup(in.getId());
			}

			l.add(new ListPathForOrgGroupOrItemReturnItem(borg));

			BOrgGroup parent;
			while ((parent=borg.getParent()) != null)
			{
				l.add(new ListPathForOrgGroupOrItemReturnItem(parent));
				borg=parent;
			}

			ListPathForOrgGroupOrItemReturnItem top=new ListPathForOrgGroupOrItemReturnItem();
			top.setId("");
			top.setName("(top)");
			top.setType(0);
			l.add(top);
			//need to reverse the list now
			Collections.reverse(l);

			ret.setItem(l.toArray(new ListPathForOrgGroupOrItemReturnItem[l.size()]));
			
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
	public AssociateItemsReturn associateItems(/*@WebParam(name = "in")*/final AssociateItemsInput in)
	{
		final AssociateItemsReturn ret=new AssociateItemsReturn();
		try
		{
			checkLogin(in);

			BItem child=new BItem(in.getChildId());

			if (in.getBreakQuantity()==0 || in.getBreakQuantity()==child.getQuantity())
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
	public SearchLotsReturn searchLots(/*@WebParam(name = "in")*/final SearchLotsInput in)		
	{
		final SearchLotsReturn ret=new SearchLotsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BLot.search(in.getLotNumber() ,in.getProductId(),ret.getHighCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
