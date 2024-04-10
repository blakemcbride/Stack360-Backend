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
package com.arahant.services.standard.inventory.locationCost;

import com.arahant.beans.LocationCost;
import com.arahant.beans.QuoteTable;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryLocationCostOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class LocationCostOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(LocationCostOps.class);
	
	public LocationCostOps() {
		super();
	}
	
    @WebMethod()
	public NewLocationCostReturn newLocationCost(/*@WebParam(name = "in")*/final NewLocationCostInput in) {

		final NewLocationCostReturn ret = new NewLocationCostReturn();

		try
		{
			checkLogin(in);
			
			final BLocationCost x = new BLocationCost();
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
	public ListLocationCostsReturn listLocationCosts(/*@WebParam(name = "in")*/final ListLocationCostsInput in) {

		final ListLocationCostsReturn ret = new ListLocationCostsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BLocationCost.searchLocations(ret.getCap(), ""));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveLocationCostReturn saveLocationCost(/*@WebParam(name = "in")*/final SaveLocationCostInput in) {

		final SaveLocationCostReturn ret = new SaveLocationCostReturn();

		try
		{
			checkLogin(in);
			
			final BLocationCost x = new BLocationCost(in.getLocationCostId());
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
    public DeleteLocationCostsReturn deleteLocationCosts(/*@WebParam(name = "in")*/final DeleteLocationCostsInput in) {

		final DeleteLocationCostsReturn ret = new DeleteLocationCostsReturn();

		try
		{
			checkLogin(in);

			List<LocationCost> llc = new ArrayList();

			for(String s : in.getIds())
			{
				llc.add(new BLocationCost(s).getBean());
			}

			if(ArahantSession.getHSU().createCriteria(QuoteTable.class).in(QuoteTable.LOCATION_COST, llc).exists())
			{
				throw new ArahantWarning("You may not delete a Location Cost that has been associated to a Quote.");
			}

			BLocationCost.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
