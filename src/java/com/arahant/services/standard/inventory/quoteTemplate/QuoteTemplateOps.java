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
package com.arahant.services.standard.inventory.quoteTemplate;

import com.arahant.beans.Product;
import com.arahant.beans.ProductType;
import com.arahant.beans.ProductTypeChild;
import com.arahant.beans.QuoteTemplateProduct;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryQuoteTemplateOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class QuoteTemplateOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(QuoteTemplateOps.class);
	
	public QuoteTemplateOps() {
		super();
	}
	
    @WebMethod()
	public NewQuoteTemplateReturn newQuoteTemplate(/*@WebParam(name = "in")*/final NewQuoteTemplateInput in) {

		final NewQuoteTemplateReturn ret = new NewQuoteTemplateReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteTemplate x = new BQuoteTemplate();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) 
		{
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveQuoteTemplateReturn saveQuoteTemplate(/*@WebParam(name = "in")*/final SaveQuoteTemplateInput in) {

		final SaveQuoteTemplateReturn ret = new SaveQuoteTemplateReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteTemplate x = new BQuoteTemplate(in.getId());
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
	public ListQuoteTemplatesReturn listQuoteTemplates(/*@WebParam(name = "in")*/final ListQuoteTemplatesInput in) {

		final ListQuoteTemplatesReturn ret = new ListQuoteTemplatesReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BQuoteTemplate.list(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListQuoteTemplateProductsReturn listQuoteTemplateProducts(/*@WebParam(name = "in")*/final ListQuoteTemplateProductsInput in) {

		final ListQuoteTemplateProductsReturn ret = new ListQuoteTemplateProductsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BQuoteTemplateProduct.list(ret.getCap(), in.getQuoteTemplateId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveQuoteTemplateProductsReturn saveQuoteTemplateProducts(/*@WebParam(name = "in")*/final SaveQuoteTemplateProductsInput in) {

		final SaveQuoteTemplateProductsReturn ret = new SaveQuoteTemplateProductsReturn();

		try
		{
			checkLogin(in);
			
			short sequenceNumber = 10000;

			for (QuoteTemplateProductsInputItem item : in.getProducts())
			{
				if (!isEmpty(item.getTemplateProductId()))
				{
					BQuoteTemplateProduct bqtp = new BQuoteTemplateProduct(item.getTemplateProductId());
					bqtp.setSequenceNumber(sequenceNumber++);
					bqtp.update();
				}
			}

			hsu.flush();

			sequenceNumber = 0;

			BQuoteTemplate bqt = new BQuoteTemplate(in.getQuoteTemplateId());

			List<QuoteTemplateProduct> products = bqt.getQuoteTemplateProducts();

			List<QuoteTemplateProduct> keepers = new ArrayList<QuoteTemplateProduct>();

			for(QuoteTemplateProduct qtp : bqt.getQuoteTemplateProducts())
			{
				for (QuoteTemplateProductsInputItem qtpi : in.getProducts())
				{
					if (qtp.getProduct().getProductId().equals(qtpi.getProductId()))
						keepers.add(qtp);
				}
			}

			products.removeAll(keepers);

			for (QuoteTemplateProduct qtp : products)
			{
				BQuoteTemplateProduct bqtp = new BQuoteTemplateProduct(qtp);
				bqtp.delete();
			}

			for (QuoteTemplateProductsInputItem item : in.getProducts())
			{
				in.setData(item, bqt.getBean(), sequenceNumber++);
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteQuoteTemplateReturn deleteQuoteTemplate(/*@WebParam(name = "in")*/final DeleteQuoteTemplateInput in) {

		final DeleteQuoteTemplateReturn ret = new DeleteQuoteTemplateReturn();

		try
		{
			checkLogin(in);
			
			BQuoteTemplate.delete(in.getQuoteTemplateId());
			
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

				List<ListProductsAndTypesReturnItem> productsAndTypes = new ArrayList<ListProductsAndTypesReturnItem>();

				for (ProductTypeChild child : children)
				{
					if (child instanceof ProductType)
					{
						productsAndTypes.add(new ListProductsAndTypesReturnItem(new BProductType((ProductType)child)));
					}
				}

				for (ProductTypeChild child : children)
				{
					if (child instanceof Product)
					{
						Product p = (Product)child;

						boolean add = true;

						if (in.getExcludeIds() != null)
							for (String id : in.getExcludeIds())
								if (p.getProductId().equals(id))
									add = false;

						if (add)
							productsAndTypes.add(new ListProductsAndTypesReturnItem(new BProduct((Product)child)));
					}
				}

				ListProductsAndTypesReturnItem [] items=new ListProductsAndTypesReturnItem[productsAndTypes.size()];

				for (int i = 0; i < productsAndTypes.size(); i++)
				{
					items[i] = productsAndTypes.get(i);
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
}
