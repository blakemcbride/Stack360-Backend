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
package com.arahant.services.standard.inventory.quoteParent;

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


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardInventoryQuoteParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class QuoteParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(QuoteParentOps.class);
	
	public QuoteParentOps() {
		super();
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

			final BQuoteTable x = new BQuoteTable();
			in.setData(x);
			ret.setId(x.getQuoteId());
			ret.setDescription(x.getQuoteDescription());
			ret.setName(x.getQuoteName());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewQuoteFromEmptyReturn newQuoteFromEmpty(/*@WebParam(name = "in")*/final NewQuoteFromEmptyInput in) {

		final NewQuoteFromEmptyReturn ret = new NewQuoteFromEmptyReturn();

		try
		{
			checkLogin(in);

			final BQuoteTable x = new BQuoteTable();
			in.setData(x);
			ret.setId(x.getQuoteId());
			ret.setDescription(x.getQuoteDescription());
			ret.setName(x.getQuoteName());

			finishService(ret);
		}
		catch (final Exception e) {
			ret.setId("");
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
    public DeleteQuoteReturn deleteQuote(/*@WebParam(name = "in")*/final DeleteQuoteInput in) {

		final DeleteQuoteReturn ret = new DeleteQuoteReturn();

		try
		{
			checkLogin(in);

			for (String id : in.getQuoteIds())
				BQuoteTable.delete(id);

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
}
