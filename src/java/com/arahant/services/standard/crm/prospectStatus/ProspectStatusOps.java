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
package com.arahant.services.standard.crm.prospectStatus;
import com.arahant.beans.ProspectStatus;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProspectStatusReport;
import com.arahant.utils.ArahantConstants;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProspectStatusOps.class);
	
	public ProspectStatusOps() {
		super();
	}
	
    @WebMethod()
	public ListProspectStatusesReturn listProspectStatuses(/*@WebParam(name = "in")*/final ListProspectStatusesInput in)		
	{
		final ListProspectStatusesReturn ret=new ListProspectStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProspectStatus.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteProspectStatusesReturn deleteProspectStatuses(/*@WebParam(name = "in")*/final DeleteProspectStatusesInput in)		
	{
		final DeleteProspectStatusesReturn ret=new DeleteProspectStatusesReturn();
		try
		{
			checkLogin(in);
			
			BProspectStatus.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public NewProspectStatusReturn newProspectStatus(/*@WebParam(name = "in")*/final NewProspectStatusInput in)		
	{
		final NewProspectStatusReturn ret=new NewProspectStatusReturn();
		try
		{
			checkLogin(in);
			
			final BProspectStatus x=new BProspectStatus();
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
	public SaveProspectStatusReturn saveProspectStatus(/*@WebParam(name = "in")*/final SaveProspectStatusInput in)		
	{
		final SaveProspectStatusReturn ret=new SaveProspectStatusReturn();
		try
		{
			checkLogin(in);
			
			final BProspectStatus x=new BProspectStatus(in.getId());
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
	public MoveProspectStatusReturn moveProspectStatus(/*@WebParam(name = "in")*/final MoveProspectStatusInput in)		
	{
		final MoveProspectStatusReturn ret=new MoveProspectStatusReturn();
		try
		{
			checkLogin(in);
			
			final BProspectStatus x=new BProspectStatus(in.getId());
			x.moveUp(in.getMoveUp());
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ReorderStatusesReturn reorderStatuses(/*@WebParam(name = "in")*/final ReorderStatusesInput in)		
	{
		final ReorderStatusesReturn ret=new ReorderStatusesReturn();
		try
		{
			checkLogin(in);

			// RKK - copied this code from screen group re-order
			
			// adjust them all upwards so as to have no constraint violations
			List<ProspectStatus> prospectStatuses = hsu.createCriteria(ProspectStatus.class).list();
			for (ProspectStatus prospectStatus : prospectStatuses) {
				prospectStatus.setSequence((short)(1000 + prospectStatus.getSequence()));
				hsu.saveOrUpdate(prospectStatus);
			}
			hsu.flush();
			
			// now update the ones we got in order
			for (short idx = 0; idx < in.getIds().length; idx++) {
				ProspectStatus prospectStatus = hsu.get(ProspectStatus.class, in.getIds()[idx]);
				prospectStatus.setSequence(idx);
				hsu.saveOrUpdate(prospectStatus);
				hsu.flush();
			}
			
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
			
			ret.setReportUrl(new ProspectStatusReport().build()); 
			
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
			
			ret.setAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

			ret.setAccessLevel(BRight.checkRight("AccessCRMSetup"));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}
