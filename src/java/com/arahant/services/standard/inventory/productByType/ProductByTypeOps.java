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
package com.arahant.services.standard.inventory.productByType;
import com.arahant.beans.Product;
import com.arahant.beans.ProductType;
import com.arahant.beans.ProductTypeChild;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProductTypeReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryProductByTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProductByTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProductByTypeOps.class);
	
	public ProductByTypeOps() {
		super();
	}
	
    @WebMethod()
	public NewProductTypeReturn newProductType(/*@WebParam(name = "in")*/final NewProductTypeInput in)		
	{
		final NewProductTypeReturn ret=new NewProductTypeReturn();
		try
		{
			checkLogin(in);
			
			final BProductType x=new BProductType();
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
	public SaveProductTypeReturn saveProductType(/*@WebParam(name = "in")*/final SaveProductTypeInput in)		
	{
		final SaveProductTypeReturn ret=new SaveProductTypeReturn();
		try
		{
			checkLogin(in);

			final BProductType x=new BProductType(in.getId());
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
	public NewProductReturn newProduct(/*@WebParam(name = "in")*/final NewProductInput in)		
	{
		final NewProductReturn ret=new NewProductReturn();
		try
		{
			checkLogin(in);
			
			final BProduct x=new BProduct();
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
	public SaveProductReturn saveProduct(/*@WebParam(name = "in")*/final SaveProductInput in)		
	{
		final SaveProductReturn ret=new SaveProductReturn();
		try
		{
			checkLogin(in);
			
			final BProduct x=new BProduct(in.getId());
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
    public DeleteProductsAndTypesReturn deleteProductsAndTypes(/*@WebParam(name = "in")*/final DeleteProductsAndTypesInput in)		
	{
		final DeleteProductsAndTypesReturn ret=new DeleteProductsAndTypesReturn();
		try
		{
			checkLogin(in);
			
			BProduct.delete(in.getProductIds());
			BProductType.delete(in.getTypeIds());
			
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

			ret.setItem(BVendorCompany.searchVendors(in.getName(),ret.getHighCap()));
			
			// populate the return item
			if (!isEmpty(in.getProductId()))
			{
				BProduct bp=new BProduct(in.getProductId());
				if (!isEmpty(bp.getVendorId()))
					ret.setSelectedItem(new SearchVendorsReturnItem(new BVendorCompany(bp.getVendorId())));
			}
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
			
			ret.setReportUrl(new ProductTypeReport().build(in.getProductTypeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadProductReturn loadProduct(/*@WebParam(name = "in")*/final LoadProductInput in)		
	{
		final LoadProductReturn ret=new LoadProductReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProduct(in.getId()));
			
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

			BProductType [] dat=BProductType.search(in.getDescription(),in.getExcludeProductTypeId(),ret.getHighCap());
			ret.setItem(dat);
			
			//populate the return item
			if (!isEmpty(in.getSelectProductTypeId()))
			{
				ret.setSelectedItem(new SearchProductTypesReturnItem(new BProductType(in.getSelectProductTypeId())));
			}
			else
				if (!isEmpty(in.getSelectParentProductTypeId()))
				{
					BProductType pt=new BProductType(in.getSelectParentProductTypeId());
					String pid=pt.getParentId();
					if (!isEmpty(pid))
						ret.setSelectedItem(new SearchProductTypesReturnItem(new BProductType(pid)));
				}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
