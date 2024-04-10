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
package com.arahant.services.standard.hr.benefitHistory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitHistoryOps.class);
	
	public BenefitHistoryOps() {
		super();
	}
	
	@WebMethod()
	public ListBenefitsWithBeneficiaryHistoryReturn listBenefitsWithBeneficiaryHistory(/*@WebParam(name = "in")*/final ListBenefitsWithBeneficiaryHistoryInput in)			{
		final ListBenefitsWithBeneficiaryHistoryReturn ret=new ListBenefitsWithBeneficiaryHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listBenefitsWithHistory(in.getPersonId()));
			
			
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListBeneficiaryHistoryReturn listBeneficiaryHistory(/*@WebParam(name = "in")*/final ListBeneficiaryHistoryInput in)			{
		final ListBeneficiaryHistoryReturn ret=new ListBeneficiaryHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeBeneficiaryH.list(in.getPersonId(),in.getBenefitId()));
			hsu.rollbackTransaction();
			hsu.beginTransaction();
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
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public LoadPersonBenefitHistoryReturn loadPersonBenefitHistory(/*@WebParam(name = "in")*/final LoadPersonBenefitHistoryInput in)			{
		final LoadPersonBenefitHistoryReturn ret=new LoadPersonBenefitHistoryReturn();
		try
		{
			checkLogin(in);

			if (in.getHistoryId().indexOf('-')!=-1)
			{
				ret.setData(new BHRBenefitJoin(in.getHistoryId()));
			}
			else
			{
				ret.setData(new BHRBenefitJoinH(in.getHistoryId()));
			}
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListDependentsReturn listDependents(/*@WebParam(name = "in")*/final ListDependentsInput in)			{
		final ListDependentsReturn ret=new ListDependentsReturn();
		try
		{
			checkLogin(in);
			if (new BPerson(in.getPersonId()).isEmployee())
				ret.setItem(new BEmployee(in.getPersonId()).listDependents());
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListDependentBenefitHistoryReturn listDependentBenefitHistory(/*@WebParam(name = "in")*/final ListDependentBenefitHistoryInput in)			{
		final ListDependentBenefitHistoryReturn ret=new ListDependentBenefitHistoryReturn();
		try
		{
			checkLogin(in);

			BHREmplDependent dep=new BHREmplDependent(in.getDependentId());
			ret.setItem(BHRBenefitJoinH.list(in.getCategoryId(),dep.getPersonId()));
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public LoadDependentBenefitHistoryReturn loadDependentBenefitHistory(/*@WebParam(name = "in")*/final LoadDependentBenefitHistoryInput in)			{
		final LoadDependentBenefitHistoryReturn ret=new LoadDependentBenefitHistoryReturn();
		try
		{
			checkLogin(in);
			if (in.getHistoryId().indexOf('-')!=-1) 
			{
				ret.setData(new BHRBenefitJoin(in.getHistoryId()));
			}
			else
			{
				ret.setData(new BHRBenefitJoinH(in.getHistoryId()));
			}
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public LoadBeneficiaryHistoryReturn loadBeneficiaryHistory(/*@WebParam(name = "in")*/final LoadBeneficiaryHistoryInput in)			{
		final LoadBeneficiaryHistoryReturn ret=new LoadBeneficiaryHistoryReturn();
		try
		{
			checkLogin(in);
			if (in.getHistoryId().indexOf('-')!=-1) 
			{
				ret.setData(new BHRBeneficiary(in.getHistoryId()));
			}
			else
			{
				ret.setData(new BHREmployeeBeneficiaryH(in.getHistoryId()));
			}
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListPersonBenefitHistoryReturn listPersonBenefitHistory(/*@WebParam(name = "in")*/final ListPersonBenefitHistoryInput in)			{
		final ListPersonBenefitHistoryReturn ret=new ListPersonBenefitHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitJoinH.list(in.getCategoryId(),in.getPersonId()));
			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
