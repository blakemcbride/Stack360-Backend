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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrCheckList;

import com.arahant.beans.CompanyForm;
import com.arahant.business.BCompanyForm;
import com.arahant.business.BFormType;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRCheckListItem;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BRight;
import com.arahant.business.BScreenGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.ArrayList;
import java.util.List;
 

/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrCheckListOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrCheckListOps extends ServiceBase{

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrCheckListOps.class);

	public HrCheckListOps() {
		super();
	}
	
	
	
	@WebMethod()
	public DeleteChecklistItemsReturn deleteChecklistItems(/*@WebParam(name = "in")*/final DeleteChecklistItemsInput in)	{
		final DeleteChecklistItemsReturn ret=new DeleteChecklistItemsReturn();
		
		try
		{
			checkLogin(in);
			
			BHRCheckListItem.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetChecklistItemsReportReturn getChecklistItemsReport(/*@WebParam(name = "in")*/final GetChecklistItemsReportInput in)	{
		final GetChecklistItemsReportReturn ret=new GetChecklistItemsReportReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setFileName(BHRCheckListItem.getReport(hsu, in.getStatusId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public ListChecklistItemsReturn listChecklistItems(/*@WebParam(name = "in")*/final ListChecklistItemsInput in)	{
		final ListChecklistItemsReturn ret=new ListChecklistItemsReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setItem(BHRCheckListItem.list(hsu,in.getEmployeeStatusId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)	{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHREmployeeStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HR));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	@WebMethod()
	public NewChecklistItemReturn newChecklistItem(/*@WebParam(name = "in")*/final NewChecklistItemInput in)	{
		final NewChecklistItemReturn ret=new NewChecklistItemReturn();
		
		try
		{
			checkLogin(in);
			final BHRCheckListItem cli=new BHRCheckListItem();
			ret.setId(cli.create());
			in.setData(cli);
			cli.insert();
			
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveChecklistItemReturn saveChecklistItem(/*@WebParam(name = "in")*/final SaveChecklistItemInput in)	{
		final SaveChecklistItemReturn ret=new SaveChecklistItemReturn();
		
		try
		{
			checkLogin(in);
			final BHRCheckListItem cli=new BHRCheckListItem(in.getChecklistItemId());
			in.setData(cli);
			cli.update();
			
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchScreensAndGroupsReturn searchScreensAndGroups(/*@WebParam(name = "in")*/final SearchScreensAndGroupsInput in)		
	{
		final SearchScreensAndGroupsReturn ret=new SearchScreensAndGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BScreenGroup.searchScreensAndGroupsForTasks(in.getName(), in.getScreenOrGroupId(), in.getIncludeScreens(), in.getIncludeScreenGroups(), in.getIncludeWizards(), ret.getCap()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

    @WebMethod()
	public ListAvailableDocumentsReturn listAvailableDocuments(/*@WebParam(name = "in")*/final ListAvailableDocumentsInput in) {

		final ListAvailableDocumentsReturn ret = new ListAvailableDocumentsReturn();

		try
		{
			checkLogin(in);

			List<CompanyForm> forms = hsu.createCriteria(CompanyForm.class).notIn(CompanyForm.COMPANY_FORM_ID, in.getExcludeIds())
																		   .like(CompanyForm.COMMENTS, in.getName()).list();
			if(!isEmpty(in.getChecklistItemId()))
			{
				BHRCheckListItem bcli = new BHRCheckListItem(in.getChecklistItemId());
				if(!isEmpty(bcli.getCompanyFormId()))
					ret.setSelectedItem(new ListAvailableDocumentsReturnItem(new BCompanyForm(bcli.getCompanyFormId())));
			}
			if(!isEmpty(in.getCompanyFormId()))
				ret.setSelectedItem(new ListAvailableDocumentsReturnItem(new BCompanyForm(in.getCompanyFormId())));
			ret.setItem(BCompanyForm.makeArray(forms));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in)	{
		final ListFormTypesReturn ret=new ListFormTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BFormType.list('E'));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
