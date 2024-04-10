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
package com.arahant.services.standard.inventory.assemblyTemplate;
import com.arahant.beans.Product;
import com.arahant.beans.ProductType;
import com.arahant.beans.ProductTypeChild;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.AssemblyTemplateFillInReport;
import com.arahant.reports.AssemblyTemplateGeneralReport;
import com.arahant.reports.AssemblyTemplateReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryAssemblyTemplateOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AssemblyTemplateOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AssemblyTemplateOps.class);
	
	public AssemblyTemplateOps() {
		super();
	}
	
    @WebMethod()
	public ListTemplatesReturn listTemplates(/*@WebParam(name = "in")*/final ListTemplatesInput in)		
	{
		final ListTemplatesReturn ret=new ListTemplatesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAssemblyTemplate.listTopLevel());
			
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

			if (in.getIsDetail())
				BAssemblyTemplateDetail.delete(in.getIds());
			else
				BAssemblyTemplate.delete(in.getIds());
			
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
	public ListTemplateDetailsReturn listTemplateDetails(/*@WebParam(name = "in")*/final ListTemplateDetailsInput in)		
	{
		final ListTemplateDetailsReturn ret=new ListTemplateDetailsReturn();
		try
		{
			checkLogin(in);

			if (!isEmpty(in.getParentId()))
				ret.setItem(new BAssemblyTemplateDetail(in.getParentId()).listChildren());
			else
				ret.setItem(new BAssemblyTemplate(in.getTemplateParentId()).listChildren());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	


    @WebMethod()
	public ListProductsAndTypesReturn listProductsAndTypes(/*@WebParam(name = "in")*/final ListProductsAndTypesInput in)
	{
		final ListProductsAndTypesReturn ret=new ListProductsAndTypesReturn();
		try
		{
			checkLogin(in);


			if (isEmpty(in.getProductTypeId()))
			{
				BProductType []types=BProductType.getTopLevelTypes();

				ListProductsAndTypesReturnItem [] items=new ListProductsAndTypesReturnItem[types.length];

				for (int loop=0;loop<types.length;loop++)
					items[loop]=new ListProductsAndTypesReturnItem(types[loop]);

				ret.setData(in.getSearchMetaInput().getPagingFirstItemIndex(), types.length,in.getSearchMetaInput().getItemsPerPage());

				ret.setItem(items);

			}
			else
			{
				BProductType pt=new BProductType(in.getProductTypeId());

				List<ProductTypeChild> children=pt.getChildren(in.getSearchMetaInput());
				int total=hsu.createCriteria(ProductType.class).eq(ProductTypeChild.PRODUCT_TYPE, pt.getBean()).count();
				total+=hsu.createCriteria(Product.class).eq(ProductTypeChild.PRODUCT_TYPE, pt.getBean()).count();
				ListProductsAndTypesReturnItem [] items=new ListProductsAndTypesReturnItem[children.size()];

				int count=0;
				for (ProductTypeChild child : children)
				{
					if (child instanceof ProductType)
					{
						items[count++]=new ListProductsAndTypesReturnItem(new BProductType((ProductType)child));
					}
				}

				for (ProductTypeChild child : children)
				{
					if (child instanceof Product)
					{
						items[count++]=new ListProductsAndTypesReturnItem(new BProduct((Product)child));
					}
				}

				ret.setData(in.getSearchMetaInput().getPagingFirstItemIndex(), total,in.getSearchMetaInput().getItemsPerPage());

				ret.setItem(items);

			}


			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


    @WebMethod()
	public NewDetailReturn newDetail(/*@WebParam(name = "in")*/final NewDetailInput in)		
	{
		final NewDetailReturn ret=new NewDetailReturn();
		try
		{
			checkLogin(in);
			
			final BAssemblyTemplateDetail x=new BAssemblyTemplateDetail();
			ret.setId(x.create());
			in.setData(x);
			if (isEmpty(in.getParentId()))
				x.setTemplateId(in.getTemplateId());
			else
				x.setParentId(in.getParentId());
			x.insert();
			
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
	public SaveDetailReturn saveDetail(/*@WebParam(name = "in")*/final SaveDetailInput in)		
	{
		final SaveDetailReturn ret=new SaveDetailReturn();
		try
		{
			checkLogin(in);
			
			final BAssemblyTemplateDetail x=new BAssemblyTemplateDetail(in.getId());
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
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new AssemblyTemplateReport().build());
			
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

                        if (in.getReportNumber()==1)
                            ret.setReportUrl(new AssemblyTemplateGeneralReport().build(in.getId()));

                        if (in.getReportNumber()==2)
                            ret.setReportUrl(new AssemblyTemplateFillInReport().build(in.getId()));
			
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

			ret.setItem(BProduct.search(in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
