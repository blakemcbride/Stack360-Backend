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
 *
 */
package com.arahant.services.standard.site.companySecurityOverride;
import com.arahant.beans.CompanyDetail;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSiteCompanySecurityOverrideOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class CompanySecurityOverrideOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			CompanySecurityOverrideOps.class);
	
	public CompanySecurityOverrideOps() {
		super();
	}

        @WebMethod()
        public DeleteCompanyOverridesReturn deleteCompanyOverrides(/*@WebParam(name = "in")*/final DeleteCompanyOverridesInput in)
	{
		final DeleteCompanyOverridesReturn ret=new DeleteCompanyOverridesReturn();
		try
		{
			checkLogin(in);

			BProphetLoginOverride.delete(in.getIds());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public SearchCompaniesForEmployeeReturn searchCompaniesForEmployee(/*@WebParam(name = "in")*/final SearchCompaniesForEmployeeInput in)
	{
		final SearchCompaniesForEmployeeReturn ret=new SearchCompaniesForEmployeeReturn();
		try
		{
			checkLogin(in);

                        //name removed because it was removed from the screen
                        BPerson bp = new BPerson(in.getEmployeeId());
                        ret.setScreenGroupName(bp.getScreenGroupExtId() + " - " + bp.getScreenGroupName());
                        ret.setSecurityGroupName(bp.getSecurityGroupName());
			ret.setItem(BProphetLoginOverride.searchCompaniesForEmployee(in.getEmployeeId(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);  

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap(), false, in.getCompanyId()));

			if (!isEmpty(in.getPersonId()) && !isEmpty(in.getCompanyId()))
			{
				BPerson p = new BPerson(in.getPersonId());

                                if(!isEmpty(p.getScreenOverrideId(in.getCompanyId())))
                                    ret.setSelectedItem(new SearchScreenGroupsReturnItem(new BScreenGroup(p.getScreenOverrideId(in.getCompanyId()))));

			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();
  
		try {
			checkLogin(in);

			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getHighCap(), in.getCompanyId()));

			if (!isEmpty(in.getPersonId()) && !isEmpty(in.getCompanyId()))
			{
				BPerson p = new BPerson(in.getPersonId());
				if(!isEmpty(p.getSecurityOverrideId(in.getCompanyId())))
                                    ret.setSelectedItem(new SearchSecurityGroupsItem(new BSecurityGroup(p.getSecurityOverrideId(in.getCompanyId()))));
				
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

    @WebMethod()
	public SaveCompanyOverrideReturn saveCompanyOverride(/*@WebParam(name = "in")*/final SaveCompanyOverrideInput in)		
	{
		final SaveCompanyOverrideReturn ret=new SaveCompanyOverrideReturn();
		try
		{
			checkLogin(in);
			
			final BProphetLoginOverride x=new BProphetLoginOverride(in.getLoginExceptionId());
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
	public NewCompanyOverrideReturn newCompanyOverride(/*@WebParam(name = "in")*/final NewCompanyOverrideInput in)		
	{
		final NewCompanyOverrideReturn ret=new NewCompanyOverrideReturn();
		try
		{
			checkLogin(in);
			
			final BProphetLoginOverride x=new BProphetLoginOverride();
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
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in)		
	{
		final SearchCompaniesReturn ret=new SearchCompaniesReturn();
		try
		{
			checkLogin(in);

			BPerson bp = new BPerson(in.getEmployeeId());

			List<CompanyDetail> l = bp.getAllowedCompanies(in.getExcludeIds());

			BCompany[] bc = new BCompany[l.size()];

			for (int i = 0; i < bc.length; i++)
				bc[i] = new BCompany(l.get(i));

			ret.setItem(bc);

			if (!isEmpty(in.getCompanyId()))
				ret.setSelectedItem(new SearchCompaniesReturnItem(new BCompany(in.getCompanyId())));
			else
				ret.setSelectedItem(null);

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
