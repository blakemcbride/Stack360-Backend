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
package com.arahant.services.standard.time.overtimeHours;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.DateUtils;
import java.util.LinkedList;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeOvertimeHoursOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OvertimeHoursOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			OvertimeHoursOps.class);
	
	public OvertimeHoursOps() {
		super();
	}
	
     @WebMethod()
	public ListOvertimeHoursReturn listOvertimeHours(/*@WebParam(name = "in")*/final ListOvertimeHoursInput in)
	{
		final ListOvertimeHoursReturn ret=new ListOvertimeHoursReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOvertimeApproval.list(in.getPersonId(),in.getFromDate(),in.getToDate()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}



    @WebMethod()
	public LoadDateReturn loadDate(/*@WebParam(name = "in")*/final LoadDateInput in)
	{
		final LoadDateReturn ret=new LoadDateReturn();
		try
		{
			checkLogin(in);

			ret.setFromDate(DateUtils.addDays(DateUtils.now(), -14));
			ret.setToDate(DateUtils.addDays(DateUtils.now(), 14));

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
			boolean includeUser=false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES)==ACCESS_LEVEL_WRITE)
				includeUser=true;

			if (hsu.currentlySuperUser())
				includeUser=false;


			BEmployee []emps=BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser);

			if (in.getHasTimeReadyForApproval())
			{
				final List <BEmployee> be=new LinkedList<BEmployee>();
				for (BEmployee element : emps)
					if (element.getHasTimeReadyForApproval())
						be.add(element);

				emps=be.toArray(new BEmployee[be.size()]);
			}

			ret.setEmployees(emps);
			if (in.getIncludeSelected() && !hsu.currentlySuperUser())
				ret.setSelectedItem(new Employees(new BEmployee(hsu.getCurrentPerson().getPersonId())));
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
    public RemoveOvertimeHoursReturn removeOvertimeHours(/*@WebParam(name = "in")*/final RemoveOvertimeHoursInput in)		
	{
		final RemoveOvertimeHoursReturn ret=new RemoveOvertimeHoursReturn();
		try
		{
			checkLogin(in);
			
			BOvertimeApproval.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewOvertimeHoursReturn newOvertimeHours(/*@WebParam(name = "in")*/final NewOvertimeHoursInput in)		
	{
		final NewOvertimeHoursReturn ret=new NewOvertimeHoursReturn();
		try
		{
			checkLogin(in);

			for (int loop=0;loop<in.getObj().length;loop++)
			{
				NewOvertimeHoursInputItem item=in.getObj()[loop];
				if (isEmpty(item.getId()))
				{
					final BOvertimeApproval x=new BOvertimeApproval();
					x.create();
					x.setPersonId(in.getPersonId());
					x.setDate(item.getDate());
					x.setHours(item.getHours());
					x.insert();
				}
				else
				{
					final BOvertimeApproval x=new BOvertimeApproval(item.getId());
					x.setDate(item.getDate());
					x.setHours(item.getHours());
					x.update();
				}
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
