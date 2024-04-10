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
package com.arahant.services.standard.inventory.lot;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.LotReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryLotOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class LotOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			LotOps.class);
	
	public LotOps() {
		super();
	}
	
	@WebMethod()
    public DeleteLotsReturn deleteLots(/*@WebParam(name = "in")*/final DeleteLotsInput in)		
	{
		final DeleteLotsReturn ret=new DeleteLotsReturn();
		try
		{
			checkLogin(in);
			
			BLot.delete(in.getIds());
			
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
			
			ret.fillFromSearchOutput(BLot.search(in.getSearchMetaInput(),in.getLotNumber(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber()));

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
				x.setQuantity(in.getOriginalQuantity());
				x.setDescription(in.getLotDescription());
				x.insert();
			}
			else
			{
				for (NewLotInputSerial serno : in.getSerialNumber())
				{
					final BItem x=new BItem();
					ret.setId(x.create());
					in.setData(x);
					x.setSerialNumber(serno.getSerialNumber());
					x.setDescription(serno.getDescription());
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
	public SaveLotReturn saveLot(/*@WebParam(name = "in")*/final SaveLotInput in)
	{
		final SaveLotReturn ret=new SaveLotReturn();
		try
		{
			checkLogin(in);

			final BLot x=new BLot(in.getId());
			in.setData(x);
			x.update();

			BItem []items=x.getItems();

			for (BItem i: items)
			{
				in.setData(i);
				i.update();
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}



    @WebMethod()
	public LoadLotReturn loadLot(/*@WebParam(name = "in")*/final LoadLotInput in)
	{
		final LoadLotReturn ret=new LoadLotReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BLot(in.getId()));

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

			//populate the return item
			if (!isEmpty(in.getSelectFromLotId()))
				ret.setSelectedItem(new SearchProductsReturnItem(new BLot(in.getSelectFromLotId()).getProduct()));

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


			ret.setReportUrl(new LotReport().build(in.getLotNumber(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber()));

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
}
