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
 * Created on Mar 13, 2007
 * 
 */
package com.arahant.services.standard.hr.employeeProfile;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.business.BWagePaid;
import com.arahant.reports.WagePaidDetailReport;
import com.arahant.reports.WagePaidReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmployeeProfileOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeProfileOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeProfileOps.class);

	public EmployeeProfileOps() {
		super();
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
	public LoadEmployeeProfileReturn loadEmployeeProfile(/*@WebParam(name = "in")*/final LoadEmployeeProfileInput in)	{
		final LoadEmployeeProfileReturn ret=new LoadEmployeeProfileReturn();

		try {
			checkLogin(in);

			ret.setData(new BEmployee(in.getEmployeeId()));

			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

/*
 * 
list employee subordinates
 
input: login id (to get person id)
output: "last name, first name", employee id
 
 
 
load employee profile
 
input: employee id
output: first, last, email, position, title, status, w4 status, wage type, street, city, state, zip, home phone, work phone, mobile phone, fax, login id (if exists), login status (if exists), accrual account amounts (maybe list with name, hours)
 

 */

	@WebMethod()
	public LoadPayDetailReturn loadPayDetail(/*@WebParam(name = "in")*/final LoadPayDetailInput in)
	{
		final LoadPayDetailReturn ret=new LoadPayDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(new BWagePaid(in.getId()).listDetail());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchPayHistoryReturn searchPayHistory(/*@WebParam(name = "in")*/final SearchPayHistoryInput in)		
	{
		final SearchPayHistoryReturn ret=new SearchPayHistoryReturn();
		try
		{
			checkLogin(in);

			BWagePaid [] ar=BWagePaid.search(in.getPersonId(),in.getCheckNumber(), in.getFromPayDate(), in.getToPayDate(), ret.getCap());
			ret.setItem(ar);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportDetailReturn getReportDetail(/*@WebParam(name = "in")*/final GetReportDetailInput in)		
	{
		final GetReportDetailReturn ret=new GetReportDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new WagePaidDetailReport().build(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


    @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new WagePaidReport().build(in.getPersonId(),in.getCheckNumber(),in.getFromPayDate(),in.getToPayDate()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeeDependentsReturn listEmployeeDependents(/*@WebParam(name = "in")*/final ListEmployeeDependentsInput in)		
	{
		final ListEmployeeDependentsReturn ret=new ListEmployeeDependentsReturn();
		try
		{
			checkLogin(in);

			BEmployee be = new BEmployee(in.getEmployeeId());
			ret.setDependents(be.getDependents());
			ret.setEmployeeName(be.getNameFML());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDependentReturn loadDependent(/*@WebParam(name = "in")*/final LoadDependentInput in)			{
		final LoadDependentReturn ret=new LoadDependentReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BHREmplDependent(in.getDependentId()));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewDependentReturn newDependent (/*@WebParam(name = "in")*/final NewDependentInput in)	{
		final NewDependentReturn ret=new NewDependentReturn();
		try
		{
			checkLogin(in);

			final BHREmplDependent d=new BHREmplDependent();

			if (!isEmpty(in.getPersonId()))
			{
				d.create(in.getPersonId(),in.getEmployeeId(),in.getRelationshipType());
				ret.setDependentId(d.getDependentId());

				if (new BPerson(in.getPersonId()).getBEmployee()==null)
				{
					in.setData(d);
				}
				else
				{
					in.setDataSubset(d);
				}
			}
			else
			{
				ret.setDependentId(d.create());
				in.setData(d);
			}
			d.insert();
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveDependentReturn saveDependent (/*@WebParam(name = "in")*/final SaveDependentInput in)	{
		final SaveDependentReturn ret=new SaveDependentReturn();
		try
		{
			checkLogin(in);

			final BHREmplDependent d=new BHREmplDependent(in.getDependentId());

			if (!isEmpty(in.getPersonId()))
			{
				d.moveTo(in.getPersonId());

			}
			else
			{
				if (new BPerson(d.getPerson()).getBEmployee()!=null)
				{
					in.setDataSubset(d);
					d.update();
				}
				else
				{
					in.setData(d);
					d.update();
				}
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSpouseSexReturn loadSpouseSex(/*@WebParam(name = "in")*/final LoadSpouseSexInput in)			{
		final LoadSpouseSexReturn ret=new LoadSpouseSexReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BEmployee(in.getEmployeeId()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public SaveEmployeeReturn saveEmployee(/*@WebParam(name = "in")*/final SaveEmployeeInput in)		
	{
		final SaveEmployeeReturn ret=new SaveEmployeeReturn();
		try
		{
			checkLogin(in);

			BPerson bpp=new BPerson();
			bpp.loadPending(BPerson.getCurrent().getPersonId(), BPerson.getCurrent().getPersonId(), null);

			in.setData(bpp);

			bpp.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
