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
package com.arahant.services.standard.misc.onboardingHomePage;
import com.arahant.beans.HrChecklistDetail;
import com.arahant.beans.HrChecklistItem;
import com.arahant.beans.Person;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.business.BHRCheckListDetail;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscOnboardingHomePageOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OnboardingHomePageOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			OnboardingHomePageOps.class);
	
	public OnboardingHomePageOps() {
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
	public ListTasksReturn listTasks(/*@WebParam(name = "in")*/final ListTasksInput in)		
	{
		final ListTasksReturn ret=new ListTasksReturn();
		try
		{
			checkLogin(in);
			
			BEmployee be = BPerson.getCurrent().getBEmployee();

			int screenIndex = 1;

			List<HrChecklistItem> l = ArahantSession.getHSU().createCriteria(HrChecklistItem.class).eq(HrChecklistItem.HREMPLOYEESTATUS, be.getLastActiveStatusHistory().getHrEmployeeStatus()).eq(HrChecklistItem.RESPONSIBILITY, HrChecklistItem.RESPONSIBILITY_EMPLOYEE).list();

			ListTasksReturnItem[] ltri = new ListTasksReturnItem[l.size()];

			int i = 0;
			for (HrChecklistItem cl : l)
			{
				BHRCheckListDetail cld = new BHRCheckListDetail(ArahantSession.getHSU().createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.HRCHECKLISTITEM, cl).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, be.getPerson()).first());
				ltri[i] = new ListTasksReturnItem();
				ltri[i].setSelect(cld == null);
				ltri[i].setStatus(ltri[i].getSelect()?"Completed":"In Progress");
				ltri[i].setStatusDate(ltri[i].getSelect()?DateUtils.getDateFormatted(cld.getDateCompleted()):DateUtils.getDateFormatted(be.getLastActiveStatusHistory().getEffectiveDate()));
				ltri[i].setTaskDescription("");
				ltri[i].setTaskName(cl.getName());
				if (cl.getScreen() != null)
				{
					ltri[i].setIndex(screenIndex++);
				}
				else if (cl.getScreenGroup() != null)
				{
					ltri[i].setIndex(screenIndex++);
				}
				else
				{
					ltri[i].setIndex(-1);
				}

				i++;

			}
			
			ret.setItem(ltri);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}
