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
package com.arahant.services.standard.hr.hrForm;

import com.arahant.beans.PersonForm;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BFormType;
import com.arahant.business.BPerson;
import com.arahant.business.BPersonForm;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import java.util.List;
 
    
/**
 *    
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrFormOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrFormOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrFormOps.class);
	
	public HrFormOps() {
		super();
	}
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("HREvents"));
			ret.setCanDeleteDocuments(BRight.checkRight("CanDeletePersonForms") == BRight.ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	@WebMethod()
	public ListFormsForPersonReturn listFormsForPerson(/*@WebParam(name = "in")*/final ListFormsForPersonInput in)			{
		final ListFormsForPersonReturn ret=new ListFormsForPersonReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPersonForm.list(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetFormReturn getForm(/*@WebParam(name = "in")*/final GetFormInput in)			{
		final GetFormReturn ret=new GetFormReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BPersonForm(in.getId()).getReport());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	


	@WebMethod()
	public DeleteFormsReturn deleteForms(/*@WebParam(name = "in")*/final DeleteFormsInput in)			{
		final DeleteFormsReturn ret=new DeleteFormsReturn();
		try
		{
			checkLogin(in);
			
			BPersonForm.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SaveFormReturn saveForm(/*@WebParam(name = "in")*/final SaveFormInput in)			{
		final SaveFormReturn ret=new SaveFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm(in.getId());
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
	public LoadFormReturn loadForm(/*@WebParam(name = "in")*/final LoadFormInput in)			{
		final LoadFormReturn ret=new LoadFormReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPersonForm(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in)			{
		final ListFormTypesReturn ret=new ListFormTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BFormType.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ReassignFormReturn reassignForm(/*@WebParam(name = "in")*/final ReassignFormInput in)		
	{
		final ReassignFormReturn ret=new ReassignFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm(in.getFormId());
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
	public CombineFormsReturn combineForms(/*@WebParam(name = "in")*/final CombineFormsInput in)		
	{
		final CombineFormsReturn ret=new CombineFormsReturn();
		try
		{
			checkLogin(in);
			
			
			//need to sort the form id's so we get newest first
			List <PersonForm> pfl=hsu.createCriteria(PersonForm.class)
					.in(PersonForm.FORM_ID, in.getFormIds())
					.orderByDesc(PersonForm.DATE)
					.list();
			
			final BPersonForm x=new BPersonForm(pfl.get(0));
			x.setFormDate(DateUtils.now());
			x.setFormTypeId(in.getFormTypeId());
			
			for (int loop=1;loop<pfl.size();loop++)
			{
				x.append(pfl.get(loop));
				x.update();
				new BPersonForm(pfl.get(loop)).delete();
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public DeletePagesFromFormReturn deletePagesFromForm(/*@WebParam(name = "in")*/final DeletePagesFromFormInput in)		
	{
		final DeletePagesFromFormReturn ret=new DeletePagesFromFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm(in.getFormId());
			x.deletePages(in.getPageNumbers());
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public CopyFormReturn copyForm(/*@WebParam(name = "in")*/final CopyFormInput in)		
	{
		final CopyFormReturn ret=new CopyFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm();
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
	public ExtractPagesFromFormReturn extractPagesFromForm(/*@WebParam(name = "in")*/final ExtractPagesFromFormInput in)		
	{
		final ExtractPagesFromFormReturn ret=new ExtractPagesFromFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm();
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
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)			{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.search2(in.getFirstName(),in.getLastName(),in.getSsn(),in.getSearchType(), ret.getHighCap()));
			
			if (!isEmpty(in.getPersonId()))
				ret.setSelectedItem(new SearchPersonsReturnItem(new BPerson(in.getPersonId())));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
