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


package com.arahant.services.standard.crm.prospectType;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProspectCompany;
import com.arahant.beans.ProspectType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProspectTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ProspectTypeOps.class);

	public ProspectTypeOps() {
		super();
	}



	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessCRMSetup"));
			ret.setCanSeeAllCompanies(BRight.checkRight("CanAccessAllCompanies") == BRight.ACCESS_LEVEL_WRITE);

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListProspectTypesReturn listProspectTypes(/*@WebParam(name = "in")*/final ListProspectTypesInput in) {

		final ListProspectTypesReturn ret = new ListProspectTypesReturn();

		try
		{
			checkLogin(in);
			
			List<ProspectType> types = hsu.createCriteria(ProspectType.class).list();

			ret.setItem(BProspectType.makeArray(types));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public NewProspectTypeReturn newProspectType(/*@WebParam(name = "in")*/final NewProspectTypeInput in) {

		final NewProspectTypeReturn ret = new NewProspectTypeReturn();

		try
		{
			checkLogin(in);

			if(in.getAllCompanies()) {
				BProspectType pt = new BProspectType();
				pt.create();
				pt.setLastActiveDate(in.getLastActiveDate());
				pt.setTypeCode(in.getTypeCode());
				if(!StringUtils.isEmpty(in.getDescription()))
					pt.setDescription(in.getDescription());
				pt.setCompany(null);
				pt.insert();
			}
			else {
				BProspectType pt = new BProspectType();
				pt.create();
				pt.setTypeCode(in.getTypeCode());
				pt.setLastActiveDate(in.getLastActiveDate());
				if(!StringUtils.isEmpty(in.getDescription()))
					pt.setDescription(in.getDescription());
				pt.setCompany(hsu.getCurrentCompany());
				pt.insert();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveProspectTypeReturn saveProspectType(/*@WebParam(name = "in")*/final SaveProspectTypeInput in) {

		final SaveProspectTypeReturn ret = new SaveProspectTypeReturn();

		try
		{
			checkLogin(in);
			
			final BProspectType x = new BProspectType(in.getProspectTypeId());
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
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in) {

		final SearchCompanyReturn ret = new SearchCompanyReturn();

		try
		{
			checkLogin(in);

			if(!isEmpty(in.getCompanyName()))
				ret.setItem(BCompany.search(in.getCompanyName(), ret.getHighCap()));
			else
				ret.setItem(BCompany.makeArray(hsu.createCriteria(CompanyDetail.class).list()));

			if(!isEmpty(in.getProspectTypeId()))
				ret.setSelectedItem(new SearchCompanyReturnItem(new BProspectType(in.getProspectTypeId())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteProspectTypesReturn deleteProspectTypes(/*@WebParam(name = "in")*/final DeleteProspectTypesInput in)
	{
		final DeleteProspectTypesReturn ret = new DeleteProspectTypesReturn();
		try
		{
			checkLogin(in);

			List<ProspectType> associatedTypes = (List)hsu.createCriteria(ProspectCompany.class).in(ProspectCompany.PROSPECT_TYPE_ID, in.getProspectTypeIds())
																								.selectFields(ProspectCompany.PROSPECT_TYPE)
																								.list();

			for(String id : in.getProspectTypeIds()) {
				BProspectType bpt = new BProspectType(id);
				if(associatedTypes.contains(bpt.getBean())) {
					if(bpt.getLastActiveDate() == 0)
						bpt.setLastActiveDate(DateUtils.now());
				}
				else
					bpt.delete();
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
