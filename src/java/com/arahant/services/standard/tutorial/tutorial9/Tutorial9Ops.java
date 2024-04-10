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
package com.arahant.services.standard.tutorial.tutorial9;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTutorialTutorial9Ops")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class Tutorial9Ops extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			Tutorial9Ops.class);
	
	public Tutorial9Ops() {
		super();
	}
	

	@WebMethod()
	public ListQuotableShowsReturn listQuotableShows(/*@WebParam(name = "in")*/final ListQuotableShowsInput in)	{
		final ListQuotableShowsReturn ret=new ListQuotableShowsReturn();
		try
		{
			checkLogin(in);
			
			final ListQuotableShowsItem []ar=new ListQuotableShowsItem[3];
			ar[0]=new ListQuotableShowsItem();
			ar[0].setId("1");
			ar[0].setName("Seinfeld");
			ar[1]=new ListQuotableShowsItem();
			ar[1].setId("2");
			ar[1].setName("SNL");
			ar[2]=new ListQuotableShowsItem();
			ar[2].setId("3");
			ar[2].setName("Married With Children");
			
			ret.setItem(ar);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
