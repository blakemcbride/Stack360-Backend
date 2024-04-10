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
package com.arahant.services.standard.hr.payHistory;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.WagePaidDetailReport;
import com.arahant.reports.WagePaidReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrPayHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PayHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			PayHistoryOps.class);
	
	public PayHistoryOps() {
		super();
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
	
	

}
