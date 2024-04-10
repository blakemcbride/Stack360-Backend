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

package com.arahant.services.standard.hr.billingRate;

import com.arahant.beans.EmployeeRate;
import com.arahant.beans.Person;
import com.arahant.beans.RateType;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.util.List;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBillingRateOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BillingRateOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BillingRateOps.class);

	public BillingRateOps() {
	}
	
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("EmployeeBillingRates"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public DeleteRatesReturn deleteRates(/*@WebParam(name = "in")*/final DeleteRatesInput in)	{
		final DeleteRatesReturn ret=new DeleteRatesReturn();
	
		try {
			checkLogin(in);

			BEmployeeRate.delete(hsu, in.getIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public LoadEmployeeRatesReturn loadEmployeeRates(/*@WebParam(name = "in")*/final LoadEmployeeRatesInput in)	{
		final LoadEmployeeRatesReturn ret=new LoadEmployeeRatesReturn();

		try { 
			checkLogin(in);

			BEmployee be = new BEmployee(in.getPersonId());
			List<EmployeeRate> recs = hsu.createCriteria(EmployeeRate.class).eq(EmployeeRate.PERSON, be.getPerson())
							.joinTo(EmployeeRate.PERSON).orderBy(Person.LNAME)
							.orderBy(Person.FNAME).list();
			
			ret.setItem(be, recs);
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	} 
	
	@WebMethod()
	public ListAllRatesReturn listAllRates(/*@WebParam(name = "in")*/final ListAllRatesInput in) {
		final ListAllRatesReturn ret = new ListAllRatesReturn();
		try {
			checkLogin(in);
			ret.setData(hsu, hsu.createCriteria(RateType.class).ne(RateType.TYPE, 'P').orderBy(RateType.RATE_CODE).list(), in.getPersonId(), in.getAllowedRateTypeId());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewRateReturn newRate(/*@WebParam(name = "in")*/final NewRateInput in) {
		final NewRateReturn ret = new NewRateReturn();

		try {
			checkLogin(in);
			final BEmployeeRate n = new BEmployeeRate();
			ret.setId(n.create());
			in.setData(n);
			n.insert();
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SaveRateReturn saveRate(/*@WebParam(name = "in")*/final SaveRateInput in) {
		final SaveRateReturn ret = new SaveRateReturn();

		try {
			checkLogin(in);
			final BEmployeeRate n = new BEmployeeRate(in.getEmployeeRateId());
			in.setData(n);
			n.update();
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}

