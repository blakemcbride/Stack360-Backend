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

package com.arahant.services.standard.misc.employeeHomePage;

import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.DateUtils;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscEmployeeHomePageOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeHomePageOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(EmployeeHomePageOps.class);
	
	public EmployeeHomePageOps() {
	}

	@WebMethod()
	public CreateMessageReturn createMessage(/*@WebParam(name = "in")*/final CreateMessageInput in)	{
		final CreateMessageReturn ret=new CreateMessageReturn();

		try {
			checkLogin(in);


			ret.setId(BMessage.send(hsu.getCurrentPerson(), hsu.get(Person.class,in.getToPersonId()), in.getSubject(), in.getMessage()));
	/*		final BMessage bm=new BMessage();
			ret.setId(bm.create());
			in.makeMessage(bm);
			final BPerson bp=BPerson.getCurrent();
			bm.setFromPerson(bp);
			bm.insert();
	*/
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}

	@WebMethod()
	public DeleteMessageReturn deleteMessage(/*@WebParam(name = "in")*/final DeleteMessageInput in)	{
		final DeleteMessageReturn ret=new DeleteMessageReturn();
		try {
			checkLogin(in);
			
			BMessage.delete(hsu, in.getMessageIds(), BPerson.getCurrent());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}

	@WebMethod()
	public LoadMessageReturn loadMessage(/*@WebParam(name = "in")*/final LoadMessageInput in)	{
		LoadMessageReturn ret=new LoadMessageReturn();
		
		try {
			checkLogin(in);
			
			ret=new LoadMessageReturn(new BMessage(in.getMessageId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public LoadPreferencesReturn loadPreferences(/*@WebParam(name = "in")*/final LoadPreferencesInput in)		
	{
		final LoadPreferencesReturn ret=new LoadPreferencesReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BPerson(hsu.getCurrentPerson()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePreferencesReturn savePreferences(/*@WebParam(name = "in")*/final SavePreferencesInput in)		
	{
		final SavePreferencesReturn ret=new SavePreferencesReturn();
		try
		{
			checkLogin(in);
			
			final BPerson x=new BPerson(hsu.getCurrentPerson());
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
	public SearchLoginsReturn searchLogins(/*@WebParam(name = "in")*/final SearchLoginsInput in) {		
		final SearchLoginsReturn ret=new SearchLoginsReturn();
		try	{
			checkLogin(in);

			ret.setLogins(BPerson.searchLogins2(hsu, in.getUserLogin(), in.getLastName(), in.getFirstName(), in.getContextCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchMessagesReturn searchMessages(/*@WebParam(name = "in")*/final SearchMessagesInput in)	{
		final SearchMessagesReturn ret=new SearchMessagesReturn();

		try
		{
			checkLogin(in);
			
			ret.setMessages(BMessage.search(hsu, BPerson.getCurrent(), in, ret.getCap(), true));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger); 
		}
		return ret;
	}

    @WebMethod()
	public ListBenefitCostsForEmployeeReturn listBenefitCostsForEmployee(/*@WebParam(name = "in")*/final ListBenefitCostsForEmployeeInput in)		
	{
		final ListBenefitCostsForEmployeeReturn ret=new ListBenefitCostsForEmployeeReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(hsu.getCurrentPerson().getPersonId()).getApprovedBenefitJoinsNonDeclines(DateUtils.now()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListProjectsForEmployeeReturn listProjectsForEmployee(/*@WebParam(name = "in")*/final ListProjectsForEmployeeInput in)		
	{
		final ListProjectsForEmployeeReturn ret=new ListProjectsForEmployeeReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BPerson(hsu.getCurrentPerson()).searchProjects(0,0,null,ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
