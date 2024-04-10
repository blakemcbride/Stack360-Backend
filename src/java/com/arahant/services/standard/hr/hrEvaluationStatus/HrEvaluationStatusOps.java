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
package com.arahant.services.standard.hr.hrEvaluationStatus;

import com.arahant.beans.HrEmployeeEval;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEvaluationStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrEvaluationStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HrEvaluationStatusOps.class);
	
	public HrEvaluationStatusOps() {
		super();
	}
	
    @WebMethod()
	public SearchEvaluationsReturn searchEvaluations(/*@WebParam(name = "in")*/final SearchEvaluationsInput in) {

		final SearchEvaluationsReturn ret = new SearchEvaluationsReturn();

		try
		{
			checkLogin(in);

			HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrEmployeeEval.class).orderByDesc(HrEmployeeEval.EVALDATE);

			if(!isEmpty(in.getSupervisorId()))
			{
				hcu.eq(HrEmployeeEval.EMPLOYEEBYSUPERVISORID, new BEmployee(in.getSupervisorId()).getEmployee());
			}
			
			hcu.dateBetween(HrEmployeeEval.EVALDATE, in.getFromDate(), in.getToDate());
			
			if(!in.getFinalized())
			{
				hcu.eq(HrEmployeeEval.FINALDATE, 0);
			}

			ret.setItem(BHREmployeeEval.makeArray(hcu.list()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();

		try
		{
			checkLogin(in);
			boolean includeUser=true;

			if (hsu.currentlySuperUser())
				includeUser=false;

			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser));

			if (in.getAutoDefault() && includeUser && BPerson.getCurrent().isEmployee())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public FinalizeEvaluationReturn finalizeEvaluation (/*@WebParam(name = "in")*/final FinalizeEvaluationInput in)	{
		final FinalizeEvaluationReturn ret=new FinalizeEvaluationReturn();
		try
		{
			checkLogin(in);

			for (String id : in.getIds())
			{
				new BHREmployeeEval(id).finalizeEvaluation();
			}

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

}
