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
package com.arahant.services.standard.inventory.productAssemblyTemplate;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
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
 * This is an old screen that probably isn't being used
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryProductAssemblyTemplateOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProductAssemblyTemplateOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProductAssemblyTemplateOps.class);
	
	public ProductAssemblyTemplateOps() {
		super();
	}
	
    @WebMethod()
	public ListTemplatesReturn listTemplates(/*@WebParam(name = "in")*/final ListTemplatesInput in)		
	{
		final ListTemplatesReturn ret=new ListTemplatesReturn();
		try
		{
			checkLogin(in);

			if (isEmpty(in.getId()))
				ret.setItem(BAssemblyTemplate.listTopLevel());
			else
				ret.setItem(new BAssemblyTemplate(in.getId()).list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewTemplateReturn newTemplate(/*@WebParam(name = "in")*/final NewTemplateInput in)		
	{
		final NewTemplateReturn ret=new NewTemplateReturn();
		try
		{
			checkLogin(in);
			
			final BAssemblyTemplate x=new BAssemblyTemplate();
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
	public SaveTemplateReturn saveTemplate(/*@WebParam(name = "in")*/final SaveTemplateInput in)		
	{
		final SaveTemplateReturn ret=new SaveTemplateReturn();
		try
		{
			checkLogin(in);
			
			final BAssemblyTemplate x=new BAssemblyTemplate(in.getId());
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
    public DeleteTemplatesReturn deleteTemplates(/*@WebParam(name = "in")*/final DeleteTemplatesInput in)		
	{
		final DeleteTemplatesReturn ret=new DeleteTemplatesReturn();
		try
		{
			checkLogin(in);
			
			BAssemblyTemplate.delete(in.getIds());
			
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
			
		//TODO:	ret.setReportUrl(new BAssemblyTemplate().getReport());
			
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

			if (!isEmpty(in.getSelectFromTemplateId()))
			{
				BAssemblyTemplate inv=new BAssemblyTemplate(in.getSelectFromTemplateId());
		//		ret.setSelectedItem(new SearchProductsReturnItem(inv.getProduct()));
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
    @WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(EDIT_TEMPLATE_ITEMS));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public GetFillnReportReturn getFillnReport(/*@WebParam(name = "in")*/final GetFillnReportInput in)		
	{
		final GetFillnReportReturn ret=new GetFillnReportReturn();
		try
		{
			checkLogin(in);
			
			//TODO: ret.setReportUrl(new BAnnouncement().getReport());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
