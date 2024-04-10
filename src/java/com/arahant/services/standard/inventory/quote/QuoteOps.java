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
package com.arahant.services.standard.inventory.quote;

import com.arahant.beans.Product;
import com.arahant.beans.ProductType;
import com.arahant.beans.ProductTypeChild;
import com.arahant.beans.QuoteAdjustment;
import com.arahant.beans.QuoteProduct;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.QuoteReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryQuoteOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class QuoteOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(QuoteOps.class);
	
	public QuoteOps() {
		super();
	}
	
	@WebMethod()
    public DeleteQuoteReturn deleteQuote(/*@WebParam(name = "in")*/final DeleteQuoteInput in) {

		final DeleteQuoteReturn ret = new DeleteQuoteReturn();

		try
		{
			checkLogin(in);
			
			BQuoteTable.delete(in.getQuoteId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public NewQuoteFromTemplateReturn newQuoteFromTemplate(/*@WebParam(name = "in")*/final NewQuoteFromTemplateInput in) {

		final NewQuoteFromTemplateReturn ret = new NewQuoteFromTemplateReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteTable x = new BQuoteTable();
			in.setData(x);
			ret.setId(x.getQuoteId());
			ret.setDescription(x.getQuoteDescription());
			ret.setName(x.getQuoteName());
			ret.setItem(x.getQuoteProducts());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewQuoteFromExistingReturn newQuoteFromExisting(/*@WebParam(name = "in")*/final NewQuoteFromExistingInput in) {

		final NewQuoteFromExistingReturn ret = new NewQuoteFromExistingReturn();

		try
		{
			checkLogin(in);

			BQuoteTable bqt = new BQuoteTable(in.getQuoteId());

			double total = 0;
			for (QuoteAdjustment qa : bqt.getQuoteAdjustments())
			{
				total += qa.getQuantity() * qa.getAdjustedCost();
			}

			ret.setAdjustmentsTotalCost(total);

			final BQuoteTable x = new BQuoteTable();

			in.setData(x);
			ret.setId(x.getQuoteId());
			ret.setDescription(x.getQuoteDescription());
			ret.setName(x.getQuoteName());
			ret.setItem(x.getQuoteProducts());
			ret.setAdditionalCost(x.getAdditionalCost());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveQuoteAndProductsReturn saveQuoteAndProducts(/*@WebParam(name = "in")*/final SaveQuoteAndProductsInput in) {

		final SaveQuoteAndProductsReturn ret = new SaveQuoteAndProductsReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteTable bqt = new BQuoteTable(in.getQuoteId());

			List<QuoteProduct> products = bqt.getQuoteProducts();

			List<QuoteProduct> keepers = new ArrayList<QuoteProduct>();

			for(QuoteProduct qtp : bqt.getQuoteProducts())
			{
				if(in.getProducts() != null)
				{
					for (SaveQuoteProductInputItem sqpi : in.getProducts())
					{
						if (qtp.getProduct().getProductId().equals(sqpi.getProductId()))
							keepers.add(qtp);
					}
				}
			}

			products.removeAll(keepers);

			for (QuoteProduct qtp : products)
			{
				BQuoteProduct bqp = new BQuoteProduct(qtp);
				bqp.delete();
			}

			hsu.flush();

			in.setData(bqt);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
    public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in) {
        final SearchCompanyByTypeReturn ret = new SearchCompanyByTypeReturn();
        try {
            checkLogin(in);

            if (hsu.getCurrentPerson().getOrgGroupType() == CLIENT_TYPE) {
                BPerson bp = new BPerson(hsu.getCurrentPerson());
                BCompanyBase[] ar = new BCompanyBase[1];
                ar[0] = bp.getCompany();
                ret.setCompanies(ar);
            } else {
                ret.setCompanies(BCompanyBase.searchByCompanyType(in.getName(), false, ret.getHighCap(), BOrgGroup.CLIENT_TYPE));
            }


            if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(new BProject(in.getProjectId()).getRequestingCompanyId())));
            } else if (in.getAutoDefault()) {
                ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BPerson(hsu.getCurrentPerson()).getCompany()));
            } else if (!isEmpty(in.getId())) {
                if (ret.getCompanies().length <= ret.getLowCap()) {
                    //if it's in the list, set selected item
                    for (SearchCompanyByTypeReturnItem ogri : ret.getCompanies()) {
                        if (in.getId().equals(ogri.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }
                } else {
                    for (BCompanyBase bp : BCompanyBase.search(in.getName(), false, 0)) {
                        if (in.getId().equals(bp.getOrgGroupId())) {
                            ret.setSelectedItem(new SearchCompanyByTypeReturnItem(BCompanyBase.get(in.getId())));
                        }
                    }


                }
            }
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
	public SearchLocationsReturn searchLocations(/*@WebParam(name = "in")*/final SearchLocationsInput in) {

		final SearchLocationsReturn ret = new SearchLocationsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BLocationCost.searchLocations(ret.getHighCap(), in.getName()));

			if (!isEmpty(in.getLocationId()))
				ret.setSelectedItem(new SearchLocationsReturnItem(new BLocationCost(in.getLocationId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SearchQuoteTemplatesReturn searchQuoteTemplates(/*@WebParam(name = "in")*/final SearchQuoteTemplatesInput in) {

		final SearchQuoteTemplatesReturn ret = new SearchQuoteTemplatesReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BQuoteTemplate.searchQuoteTemplates(ret.getCap(), in.getName(), in.getDescription()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public LoadQuoteReturn loadQuote(/*@WebParam(name = "in")*/final LoadQuoteInput in) {

		final LoadQuoteReturn ret = new LoadQuoteReturn();

		try
		{
			checkLogin(in);

			BQuoteTable bqt = new BQuoteTable(in.getQuoteId());

			double total = 0;
			for (QuoteAdjustment qa : bqt.getQuoteAdjustments())
			{
				total += qa.getQuantity() * qa.getAdjustedCost();
			}

			ret.setAdjustmentsTotalCost(total);
			
			ret.setData(new BQuoteTable(in.getQuoteId()));
			
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

    @WebMethod()
	public SearchQuotesReturn searchQuotes(/*@WebParam(name = "in")*/final SearchQuotesInput in) {

		final SearchQuotesReturn ret = new SearchQuotesReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BQuoteTable.searchQuotes(ret.getCap(), in.getName(), in.getDescription(), in.getCreatedFromDate(), in.getCreatedToDate(), in.getFinalizedFromDate(), in.getFinalizedToDate(), in.getClientId(), in.getLocationId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
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
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {

		final GetReportReturn ret = new GetReportReturn();

		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new QuoteReport().build(in.getQuoteId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public FinalizeQuoteReturn finalizeQuote(/*@WebParam(name = "in")*/final FinalizeQuoteInput in) {

		final FinalizeQuoteReturn ret = new FinalizeQuoteReturn();

		try
		{
			checkLogin(in);
			
			BQuoteTable bqt = new BQuoteTable(in.getQuoteId());
			bqt.setFinalizedByPerson(ArahantSession.getCurrentPerson());
			bqt.setFinalizedDate(DateUtils.now());
			bqt.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListGLSalesAccountsReturn listGLSalesAccounts(/*@WebParam(name = "in")*/final ListGLSalesAccountsInput in)	{
		final ListGLSalesAccountsReturn ret=new ListGLSalesAccountsReturn();	

		try {
			checkLogin(in);
			
			ret.setGLAccounts(BGlAccount.listByType(hsu, 21));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in)	{
		final SearchScreenGroupsReturn ret=new SearchScreenGroupsReturn();
		
		try
		{
			checkLogin(in);
			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(),in.getExtId(),in.getSearchTopLevelOnly()?2:0,"",2,ret.getHighCap()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in)	{
		final SearchSecurityGroupsReturn ret=new SearchSecurityGroupsReturn();
		
		try
		{
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(),ret.getHighCap()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public GetDefaultsReturn getDefaults(/*@WebParam(name = "in")*/final GetDefaultsInput in)		
	{
		final GetDefaultsReturn ret=new GetDefaultsReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BCompany(in.getContextCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewClientCompanyReturn newClientCompany(/*@WebParam(name = "in")*/final NewClientCompanyInput in)	{
		final NewClientCompanyReturn ret=new NewClientCompanyReturn();

		try {
			checkLogin(in);
			
			if (!isEmpty(in.getId()))
			{
				BClientCompany bcc=BClientCompany.convertFromProspect(in.getId());
				in.makeClientCompany(bcc);
				bcc.update();
				ret.setOrgGroupId(bcc.getOrgGroupId());
			}
			else
			{
				final BClientCompany bcc=new BClientCompany();
				bcc.create();
				in.makeClientCompany(bcc);
				bcc.insert();
				ret.setOrgGroupId(bcc.getOrgGroupId());
			}
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}

	@WebMethod()
	public LoadProspectReturn loadProspect(/*@WebParam(name = "in")*/final LoadProspectInput in)		
	{
		final LoadProspectReturn ret=new LoadProspectReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProspectCompany(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListQuoteAdjustmentsReturn listQuoteAdjustments(/*@WebParam(name = "in")*/final ListQuoteAdjustmentsInput in) {

		final ListQuoteAdjustmentsReturn ret = new ListQuoteAdjustmentsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BQuoteAdjustment.listQuoteAdjustments(ret.getCap(), in.getQuoteId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public NewQuoteAdjustmentReturn newQuoteAdjustment(/*@WebParam(name = "in")*/final NewQuoteAdjustmentInput in) {

		final NewQuoteAdjustmentReturn ret = new NewQuoteAdjustmentReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteAdjustment x = new BQuoteAdjustment();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveQuoteAdjustmentReturn saveQuoteAdjustment(/*@WebParam(name = "in")*/final SaveQuoteAdjustmentInput in) {

		final SaveQuoteAdjustmentReturn ret = new SaveQuoteAdjustmentReturn();

		try
		{
			checkLogin(in);
			
			final BQuoteAdjustment x = new BQuoteAdjustment(in.getQuoteAdjustmentId());
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
    public DeleteQuoteAdjustmentsReturn deleteQuoteAdjustments(/*@WebParam(name = "in")*/final DeleteQuoteAdjustmentsInput in) {

		final DeleteQuoteAdjustmentsReturn ret = new DeleteQuoteAdjustmentsReturn();

		try
		{
			checkLogin(in);
			
			BQuoteAdjustment.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ReorderQuoteAdjustmentsReturn reorderQuoteAdjustments(/*@WebParam(name = "in")*/final ReorderQuoteAdjustmentsInput in) {

		final ReorderQuoteAdjustmentsReturn ret = new ReorderQuoteAdjustmentsReturn();

		try
		{
			checkLogin(in);

			short sequence = 0;
			for (String s : in.getIds())
			{
				BQuoteAdjustment bqa = new BQuoteAdjustment(s);
				bqa.setSequenceNumber(sequence++);
				bqa.update();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
