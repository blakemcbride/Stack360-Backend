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
package com.arahant.services.standard.hr.eeo1Survey;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.FileSystemUtils;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEeo1SurveyOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class Eeo1SurveyOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			Eeo1SurveyOps.class);
	
	public Eeo1SurveyOps() {
		super();
	}
	
    @WebMethod()
	public ListSurveysReturn listSurveys(/*@WebParam(name = "in")*/final ListSurveysInput in)		
	{
		final ListSurveysReturn ret=new ListSurveysReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREEO1.list(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteSurveysReturn deleteSurveys(/*@WebParam(name = "in")*/final DeleteSurveysInput in)		
	{
		final DeleteSurveysReturn ret=new DeleteSurveysReturn();
		try
		{
			checkLogin(in);
			
			BHREEO1.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public OpenSurveyReturn openSurvey(/*@WebParam(name = "in")*/final OpenSurveyInput in)		
	{
		final OpenSurveyReturn ret=new OpenSurveyReturn();
		try
		{
			checkLogin(in);

			ret.setSurveyUrl(new BHREEO1(in.getId()).getDataFile());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListDefaultEstablishmentsReturn listDefaultEstablishments(/*@WebParam(name = "in")*/final ListDefaultEstablishmentsInput in)
	{
		final ListDefaultEstablishmentsReturn ret=new ListDefaultEstablishmentsReturn();
		try
		{
			checkLogin(in);

            ret.setItem(BOrgGroup.listEEO1Establishments());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewSurveyReturn newSurvey(/*@WebParam(name = "in")*/final NewSurveyInput in)		
	{
		final NewSurveyReturn ret=new NewSurveyReturn();
		try
		{
			checkLogin(in);

            final BHREEO1 eeo1=new BHREEO1();
			eeo1.create();
			in.setData(eeo1);
			eeo1.insert();

			ret.setEeo1Id(eeo1.getId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}



    @WebMethod()
	public SaveSurveyReturn saveSurvey(/*@WebParam(name = "in")*/final SaveSurveyInput in)
	{
		final SaveSurveyReturn ret=new SaveSurveyReturn();
		try
		{
			checkLogin(in);

            final BHREEO1 eeo1=new BHREEO1(in.getId());
			in.setData(eeo1);
			eeo1.update();

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
