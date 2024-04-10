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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.scanner;

import com.arahant.business.BFormType;
import com.arahant.business.BPerson;
import com.arahant.business.BPersonForm;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.Base64;
import com.arahant.utils.DateUtils;
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
 * Arahant
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="ScannerOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ScannerOps extends ServiceBase {
    
    static ArahantLogger logger = new ArahantLogger(ScannerOps.class);
    
    
        @WebMethod()
	public ListPersonFormTypesReturn listPersonFormTypes(/*@WebParam(name = "in")*/final ListPersonFormTypesInput in)		
	{
		final ListPersonFormTypesReturn ret=new ListPersonFormTypesReturn();
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
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)		
	{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPerson.search2(in.getFirstName(),in.getLastName(),in.getSsn(),"A",ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	

        @WebMethod()
	public AddPersonFormTypeReturn addPersonFormType(/*@WebParam(name = "in")*/final AddPersonFormTypeInput in)		
	{
		final AddPersonFormTypeReturn ret=new AddPersonFormTypeReturn();
		try
		{
			checkLogin(in);
			
			final BFormType x=new BFormType();
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
	public NewPersonFormReturn newPersonForm(/*@WebParam(name = "in")*/final NewPersonFormInput in)		
	{
		final NewPersonFormReturn ret=new NewPersonFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm();
			ret.setFormId(x.create());
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
	public ListFormsForPersonReturn listFormsForPerson(/*@WebParam(name = "in")*/final ListFormsForPersonInput in)		
	{
		final ListFormsForPersonReturn ret=new ListFormsForPersonReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPersonForm.list(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetPersonFormReturn getPersonForm(/*@WebParam(name = "in")*/final GetPersonFormInput in)		
	{
		final GetPersonFormReturn ret=new GetPersonFormReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPersonForm(in.getFormId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public AppendPersonFormReturn appendPersonForm(/*@WebParam(name = "in")*/final AppendPersonFormInput in)		
	{
		final AppendPersonFormReturn ret=new AppendPersonFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm(in.getFormId());
			x.prepend(Base64.decode(in.getFormData()));
			ret.setFormId(in.getFormId());
			x.setFormDate(DateUtils.now());
			if (!isEmpty(in.getFormComment()))
				x.setComments(in.getFormComment());
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ReplacePersonFormReturn replacePersonForm(/*@WebParam(name = "in")*/final ReplacePersonFormInput in)		
	{
		final ReplacePersonFormReturn ret=new ReplacePersonFormReturn();
		try
		{
			checkLogin(in);
			
			final BPersonForm x=new BPersonForm(in.getFormId());
			in.setData(x);
			x.update();
			ret.setFormId(in.getFormId());
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
