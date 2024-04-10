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
package com.arahant.services.standard.hrConfig.benefitConfigVariant;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigBenefitConfigVariantOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitConfigVariantOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitConfigVariantOps.class);
	
	public BenefitConfigVariantOps() {
		super();
	}
	
    @WebMethod()
	public ListBenefitConfigsReturn listBenefitConfigs(/*@WebParam(name = "in")*/final ListBenefitConfigsInput in)		
	{
		final ListBenefitConfigsReturn ret=new ListBenefitConfigsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitConfig.listTimeRelated());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewBenefitConfigVariantReturn newBenefitConfigVariant(/*@WebParam(name = "in")*/final NewBenefitConfigVariantInput in)		
	{
		final NewBenefitConfigVariantReturn ret=new NewBenefitConfigVariantReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffAccrualCalc x=new BTimeOffAccrualCalc();
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
	public SaveBenefitConfigVariantReturn saveBenefitConfigVariant(/*@WebParam(name = "in")*/final SaveBenefitConfigVariantInput in)		
	{
		final SaveBenefitConfigVariantReturn ret=new SaveBenefitConfigVariantReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffAccrualCalc x=new BTimeOffAccrualCalc(in.getId());
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
	public ListBenefitConfigVariantsReturn listBenefitConfigVariants(/*@WebParam(name = "in")*/final ListBenefitConfigVariantsInput in)		
	{
		final ListBenefitConfigVariantsReturn ret=new ListBenefitConfigVariantsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BTimeOffAccrualCalc.listBenefitConfigTimeOff(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListBenefitConfigsVariantsSeniorityReturn listBenefitConfigsVariantsSeniority(/*@WebParam(name = "in")*/final ListBenefitConfigsVariantsSeniorityInput in)
	{
		final ListBenefitConfigsVariantsSeniorityReturn ret=new ListBenefitConfigsVariantsSeniorityReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BTimeOffAccrualCalcSeniority.listSeniority(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteBenefitConfigVariantsReturn deleteBenefitConfigVariants(/*@WebParam(name = "in")*/final DeleteBenefitConfigVariantsInput in)
	{
		final DeleteBenefitConfigVariantsReturn ret=new DeleteBenefitConfigVariantsReturn();
		try
		{
			checkLogin(in);
			
			BTimeOffAccrualCalc.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteBenefitConfigVariantSeniorityReturn deleteBenefitConfigVariantSeniority(/*@WebParam(name = "in")*/final DeleteBenefitConfigVariantSeniorityInput in)
	{
		final DeleteBenefitConfigVariantSeniorityReturn ret=new DeleteBenefitConfigVariantSeniorityReturn();
		try
		{
			checkLogin(in);
			
			BTimeOffAccrualCalcSeniority.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public NewBenefitConfigVariantSeniorityReturn newBenefitConfigVariantSeniority(/*@WebParam(name = "in")*/final NewBenefitConfigVariantSeniorityInput in)
	{
		final NewBenefitConfigVariantSeniorityReturn ret=new NewBenefitConfigVariantSeniorityReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffAccrualCalcSeniority x=new BTimeOffAccrualCalcSeniority();
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
	public SaveBenefitConfigVariantSeniorityReturn saveBenefitConfigVariantSeniority(/*@WebParam(name = "in")*/final SaveBenefitConfigVariantSeniorityInput in)		
	{
		final SaveBenefitConfigVariantSeniorityReturn ret=new SaveBenefitConfigVariantSeniorityReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffAccrualCalcSeniority x=new BTimeOffAccrualCalcSeniority(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
