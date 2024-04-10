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
package com.arahant.services.standard.inventory.assemblyTemplateTree;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryAssemblyTemplateTreeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AssemblyTemplateTreeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AssemblyTemplateTreeOps.class);
	
	public AssemblyTemplateTreeOps() {
		super();
	}
	
    @WebMethod()
	public ListTemplatesReturn listTemplates(/*@WebParam(name = "in")*/final ListTemplatesInput in)		
	{
		final ListTemplatesReturn ret=new ListTemplatesReturn();
		try
		{
			checkLogin(in);

			ret.setTemplate(BAssemblyTemplate.list(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadTemplateTreeReturn loadTemplateTree(/*@WebParam(name = "in")*/final LoadTemplateTreeInput in)
	{
		final LoadTemplateTreeReturn ret=new LoadTemplateTreeReturn();
		try
		{
			checkLogin(in);
			
                        ret.setTree(new BAssemblyTemplate(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListLotsReturn listLots(/*@WebParam(name = "in")*/final ListLotsInput in)
	{
		final ListLotsReturn ret=new ListLotsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BLot.list(in.getProductId(),in.getLocationId()), in.getLocationId());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListLocationsReturn listLocations(/*@WebParam(name = "in")*/final ListLocationsInput in)
	{
		final ListLocationsReturn ret=new ListLocationsReturn();
		try
		{
			checkLogin(in);

			ret.setLocations(new BProduct(in.getProductId()).getLocations(), in.getProductId());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveFillInTemplateReturn saveFillInTemplate(/*@WebParam(name = "in")*/final SaveFillInTemplateInput in)		
	{
		final SaveFillInTemplateReturn ret=new SaveFillInTemplateReturn();
		try
		{
			checkLogin(in);

			for (int loop=0;loop<in.getTree().length;loop++)
			{
				in.getTree()[loop].build(null, in.getFinalLocationId());
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*@WebParam(name = "in")*/final ListOrgGroupsForGroupInput in)		
	{
		final ListOrgGroupsForGroupReturn ret=new ListOrgGroupsForGroupReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BOrgGroup().listTopLevelOrgGroups(in.getOrgGroupId(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
