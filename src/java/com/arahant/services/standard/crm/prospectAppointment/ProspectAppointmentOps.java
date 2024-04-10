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
package com.arahant.services.standard.crm.prospectAppointment;
import com.arahant.beans.Appointment;
import com.arahant.beans.AppointmentPersonJoin;
import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.AppointmentReport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectAppointmentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectAppointmentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProspectAppointmentOps.class);
	
	public ProspectAppointmentOps() {
		super();
	}


	@WebMethod()
	public ListAppointmentsReturn listAppointments(/*@WebParam(name = "in")*/final ListAppointmentsInput in)		
	{
		final ListAppointmentsReturn ret=new ListAppointmentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAppointment.list(in.getId(),in.getFromDate(),in.getToDate(),in.getType(),in.getStatus(),in.getAttendeeOnly(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchLocationsReturn searchLocations(/*@WebParam(name = "in")*/final SearchLocationsInput in)		
	{
		final SearchLocationsReturn ret=new SearchLocationsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BAppointmentLocation.search(in.getCode(),in.getDescription(),ret.getHighCap()));
			
			if (!isEmpty(in.getAppointmentId()))
				ret.setSelectedItem(new SearchLocationsReturnItem(new BAppointmentLocation(new BAppointment(in.getAppointmentId()).getLocationId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewAppointmentReturn newAppointment(/*@WebParam(name = "in")*/final NewAppointmentInput in)		
	{
		final NewAppointmentReturn ret=new NewAppointmentReturn();
		try
		{
			checkLogin(in);
			
			final BAppointment x=new BAppointment();
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
	public SaveAppointmentReturn saveAppointment(/*@WebParam(name = "in")*/final SaveAppointmentInput in)		
	{
		final SaveAppointmentReturn ret=new SaveAppointmentReturn();
		try
		{
			checkLogin(in);
			
			final BAppointment x=new BAppointment(in.getId());
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
        public DeleteAppointmentsReturn deleteAppointments(/*@WebParam(name = "in")*/final DeleteAppointmentsInput in)		
	{
		final DeleteAppointmentsReturn ret=new DeleteAppointmentsReturn();
		try
		{
			checkLogin(in);
			
			BAppointment.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadAppointmentReturn loadAppointment(/*@WebParam(name = "in")*/final LoadAppointmentInput in)		
	{
		final LoadAppointmentReturn ret=new LoadAppointmentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BAppointment(in.getId()));
			
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
			
			ret.setReportUrl(new AppointmentReport().build(in.getId(),in.getFromDate(),in.getToDate(),in.getType(),in.getStatus(),in.getAttendeeOnly()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRM"));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)		
	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(),in.getLastName(),in.getExcludeIds(),ret.getHighCap()));
			
			if (!isEmpty(in.getPersonId()))
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getPersonId())));
			
			if (!isEmpty(in.getArrangementId()))
			{
				try
				{
					ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(new BAppointment(in.getArrangementId()).getEmployees()[0])));
				}
				catch (Exception e)
				{
					//there may not be one
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
	public SearchContactsReturn searchContacts(/*@WebParam(name = "in")*/final SearchContactsInput in)		
	{
		final SearchContactsReturn ret=new SearchContactsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.search3(in.getId(),in.getFirstName(),in.getLastName(),in.getExcludeIds(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	

	@WebMethod()
	public LoadArrangementReturn loadArrangement(/*@WebParam(name = "in")*/final LoadArrangementInput in)		
	{
		final LoadArrangementReturn ret=new LoadArrangementReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BAppointment(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewArrangementReturn newArrangement(/*@WebParam(name = "in")*/final NewArrangementInput in)		
	{
		final NewArrangementReturn ret=new NewArrangementReturn();
		try
		{
			checkLogin(in);
			
			final BAppointment x=new BAppointment();
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
	public SaveArrangmentReturn saveArrangment(/*@WebParam(name = "in")*/final SaveArrangmentInput in)		
	{
		final SaveArrangmentReturn ret=new SaveArrangmentReturn();
		try
		{
			checkLogin(in);
			
			final BAppointment x=new BAppointment(in.getId());
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
	public ListAttendeeAppointmentsReturn listAttendeeAppointments(/*@WebParam(name = "in")*/final ListAttendeeAppointmentsInput in)		
	{
		final ListAttendeeAppointmentsReturn ret=new ListAttendeeAppointmentsReturn();
		try
		{
			checkLogin(in);

			List<String> ids;
			
			if (!isEmpty(in.getId()) && in.getFillFromId())
			{
				ids=(List<String>)(List)hsu.createCriteria(Person.class)
						.orderBy(Person.LNAME)
						.orderBy(Person.FNAME)
						.selectFields(Person.PERSONID)
						.joinTo(Person.APPOINTMENT_PERSON_JOIN)
						.joinTo(AppointmentPersonJoin.APPOINTMENT)
						.eq(Appointment.APPOINTMENT_ID,in.getId())
						.list();
				
			}
			else
			{
				ids=(List<String>)(List)hsu.createCriteria(Person.class)
						.orderBy(Person.LNAME)
						.orderBy(Person.FNAME)
						.in(Person.PERSONID,in.getItem())
						.selectFields(Person.PERSONID)
						.list();
			}
			
			ListAttendeeAppointmentsReturnItem []items=new ListAttendeeAppointmentsReturnItem[ids.size()];
				for (int loop=0;loop<items.length;loop++)
					items[loop]=new ListAttendeeAppointmentsReturnItem(new BPerson(ids.get(loop)),in.getDate(),in.getId(),in.getPrimaryEmployeeId(),in.getPrimaryContactId());
				ret.setItem(items);
				
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
