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
package com.arahant.services.standard.misc.onboardingMonitoring;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscOnboardingMonitoringOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OnboardingMonitoringOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			OnboardingMonitoringOps.class);
	
	public OnboardingMonitoringOps() {
		super();
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
			BMessage.createToRecord(bm.getMessageId(), getToPersonId());
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
	public ListOnboardingEmployeesReturn listOnboardingEmployees(/*@WebParam(name = "in")*/final ListOnboardingEmployeesInput in)		
	{
		final ListOnboardingEmployeesReturn ret=new ListOnboardingEmployeesReturn();
		try
		{
			checkLogin(in);

			List<ListOnboardingEmployeesReturnItem> l = new ArrayList<ListOnboardingEmployeesReturnItem>();

			for (BHREmplStatusHistory sh : BHREmplStatusHistory.listOnboardingEmps())
			{
				l.add(new ListOnboardingEmployeesReturnItem(sh));
			}

			Collections.sort(l, new Comparator<ListOnboardingEmployeesReturnItem>() {

				@Override
				public int compare(ListOnboardingEmployeesReturnItem arg0, ListOnboardingEmployeesReturnItem arg1) {
					return arg1.LoginDate().compareTo(arg0.LoginDate());
				}
			});

			ListOnboardingEmployeesReturnItem[] onboardEmps = new ListOnboardingEmployeesReturnItem[l.size()];

			int i = 0;
			for (ListOnboardingEmployeesReturnItem ri : l)
			{
				onboardEmps[i] = new ListOnboardingEmployeesReturnItem();
				onboardEmps[i] = ri;
				i++;
			}

			ret.setItem(onboardEmps);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListOnboardingTasksReturn listOnboardingTasks(/*@WebParam(name = "in")*/final ListOnboardingTasksInput in)		
	{
		final ListOnboardingTasksReturn ret=new ListOnboardingTasksReturn();
		try
		{
			checkLogin(in);

			String statusId = BHREmployeeStatus.findOrMake("Onboarding", true);

			if (!isEmpty(in.getPersonId()))
			{
				ret.setItem(BHRCheckListDetail.list(in.getPersonId(), statusId), in.getPersonId());
			}
			else
			{
				HibernateCriteriaUtil<HrEmplStatusHistory> hcu = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class)
						.eq(HrEmplStatusHistory.STATUS_ID, statusId)
						.selectFields(HrEmplStatusHistory.EMPLOYEE_ID);

				HashSet<String> empIds=new HashSet<String>();
				empIds.addAll((List)hcu.list());

				List<ListOnboardingTasksReturnItem> l = new ArrayList<ListOnboardingTasksReturnItem>();

				for (String s: empIds)
				{
					for (BHRCheckListDetail cld : BHRCheckListDetail.list(s, statusId))
					{
						l.add(new ListOnboardingTasksReturnItem(cld, s));
					}
				}

				Collections.sort(l,new Comparator<ListOnboardingTasksReturnItem>() {

					@Override
					public int compare(ListOnboardingTasksReturnItem arg0, ListOnboardingTasksReturnItem arg1) {
						return arg1.Date().compareTo(arg0.Date());
					}
				});

				ListOnboardingTasksReturnItem[] lotri = new ListOnboardingTasksReturnItem[l.size()];

				int i = 0;
				for (ListOnboardingTasksReturnItem ri : l)
				{
					lotri[i] = new ListOnboardingTasksReturnItem();
					
					if (in.getShowCompleted() && ri.getStatus().equals("Completed"))
					{
						lotri[i] = ri;
						i++;
					}
					else if (in.getShowInProgress() && ri.getStatus().equals("In Progress"))
					{
						lotri[i] = ri;
						i++;
					}
				}

				ret.setItem(lotri);
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
