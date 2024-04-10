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
package com.arahant.services.standard.hr.benefitParent;

import com.arahant.beans.HrBenefit;
import com.arahant.business.BBenefitClass;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BService;
import com.arahant.business.BRight;
import com.arahant.business.BScreen;
import com.arahant.business.BVendorCompany;
import com.arahant.business.BWageType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 *  
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitParentOps.class);
	
	public BenefitParentOps() {
		super();
	}
	
	@WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in)			{
		final ListBenefitsReturn ret=new ListBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listBenefits(in.getCategoryId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetBenefitReportReturn getBenefitReport(/*@WebParam(name = "in")*/final GetBenefitReportInput in)			{
		final GetBenefitReportReturn ret=new GetBenefitReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(BHRBenefit.getReport(hsu, in.getIncludeConfigurations()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public DeleteBenefitsReturn deleteBenefits(/*@WebParam(name = "in")*/final DeleteBenefitsInput in)			{
		final DeleteBenefitsReturn ret=new DeleteBenefitsReturn();
		try
		{
			checkLogin(in);
			
			BHRBenefit.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListRulesReturn listRules(/*@WebParam(name = "in")*/final ListRulesInput in)			{
		final ListRulesReturn ret=new ListRulesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listRules());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*@WebParam(name = "in")*/final ListBenefitCategoriesInput in)			{
		final ListBenefitCategoriesReturn ret=new ListBenefitCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewBenefitReturn newBenefit(/*@WebParam(name = "in")*/final NewBenefitInput in)	{
		final NewBenefitReturn ret=new NewBenefitReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefit x=new BHRBenefit();
			String id = x.create();
			ret.setId(id);
			in.setData(x);
			x.insert();

			if (in.getReplacingBenefitIds() != null)
			{
				for (String s : in.getReplacingBenefitIds())
				{
					BHRBenefit benefit = new BHRBenefit(s);
					benefit.setReplacingBenefit(new BHRBenefit(id).getBean());
					benefit.update();
				}
			}

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CopyBenefitReturn copyBenefit(/*@WebParam(name = "in")*/final CopyBenefitInput in)	{
		final CopyBenefitReturn ret = new CopyBenefitReturn();
		try
		{
			checkLogin(in);

			BHRBenefit bene = new BHRBenefit(hsu.get(HrBenefit.class, in.getBenefitId()));

			bene.deepCopy(in.getNewName());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
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
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in)	{
		final SearchServicesReturn ret=new SearchServicesReturn();

		try {
			checkLogin(in);
			
			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(), ret.getHighCap()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	


	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in)		
	{
		final ListWageTypesReturn ret=new ListWageTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BWageType.list(ret.getHighCap()));
			

			if (!isEmpty(in.getWageTypeId()))
				ret.setSelectedItem(new ListWageTypesReturnItem(new BWageType(in.getWageTypeId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReorderBenefitsReturn reorderBenefits(/*@WebParam(name = "in")*/final ReorderBenefitsInput in)
	{
		final ReorderBenefitsReturn ret=new ReorderBenefitsReturn();
		try
		{
			checkLogin(in);
			
			BHRBenefit b;

			int i = 0;
			for (String s: in.getIds())
			{
				b = new BHRBenefit(s);
				b.setSequence(i);
				b.update();
				i++;
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveBenefitReturn moveBenefit(/*@WebParam(name = "in")*/final MoveBenefitInput in)
	{
		final MoveBenefitReturn ret=new MoveBenefitReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefit x=new BHRBenefit(in.getId());
			x.moveUp(in.getMoveUp());
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in)		
	{
		final ListBenefitClassesReturn ret=new ListBenefitClassesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBenefitClass.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchScreensReturn searchScreens(/*@WebParam(name = "in")*/final SearchScreensInput in)	{
		final SearchScreensReturn ret=new SearchScreensReturn();

		try
		{
			checkLogin(in);
			
			ret.setData(BScreen.searchScreens(in.getName(),in.getExtId()));
					
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReplacingBenefitsReturn listReplacingBenefits(/*@WebParam(name = "in")*/final ListReplacingBenefitsInput in)		
	{
		final ListReplacingBenefitsReturn ret=new ListReplacingBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setReplacingBenefits(new BHRBenefit[0]);
  
			ret.setNotReplacingBenefits(BHRBenefit.getBenefitsNotBeingReplaced("", in.getCategoryId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
