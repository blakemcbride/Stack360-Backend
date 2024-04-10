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
package com.arahant.services.standard.crm.clientStatus;
import com.arahant.beans.ClientStatus;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ClientStatusList;
import com.arahant.utils.ArahantConstants;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmClientStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ClientStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ClientStatusOps.class);
	
	public ClientStatusOps() {
		super();
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRMSetup"));
			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
    public DeleteClientStatusesReturn deleteClientStatuses(/*@WebParam(name = "in")*/final DeleteClientStatusesInput in)
	{
		final DeleteClientStatusesReturn ret=new DeleteClientStatusesReturn();
		try
		{
			checkLogin(in);
			
			BClientStatus.delete(in.getIds());
			
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
			
			ret.setReportUrl(new ClientStatusList().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListClientStatusesReturn listClientStatuses(/*@WebParam(name = "in")*/final ListClientStatusesInput in)
	{
		final ListClientStatusesReturn ret=new ListClientStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BClientStatus.list(false));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveClientStatusReturn moveClientStatus(/*@WebParam(name = "in")*/final MoveClientStatusInput in)
	{
		final MoveClientStatusReturn ret=new MoveClientStatusReturn();
		try
		{
			checkLogin(in);
			
			final BClientStatus x=new BClientStatus(in.getId());
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
	public NewClientStatusReturn newClientStatus(/*@WebParam(name = "in")*/final NewClientStatusInput in)
	{
		final NewClientStatusReturn ret=new NewClientStatusReturn();
		try
		{
			checkLogin(in);
			
			final BClientStatus x=new BClientStatus();
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
	public ReorderStatusesReturn reorderStatuses(/*@WebParam(name = "in")*/final ReorderStatusesInput in)		
	{
		final ReorderStatusesReturn ret=new ReorderStatusesReturn();
		try
		{
			checkLogin(in);

			// RKK - copied this code from screen group re-order
			
			// adjust them all upwards so as to have no constraint violations
			List<ClientStatus> clientStatuses = hsu.createCriteria(ClientStatus.class).list();
			for (ClientStatus clientStatus : clientStatuses) {
				clientStatus.setSeqNo((short)(1000 + clientStatus.getSeqNo()));
				hsu.saveOrUpdate(clientStatus);
			}
			hsu.flush();
			
			// now update the ones we got in order
			for (short idx = 0; idx < in.getIds().length; idx++) {
				ClientStatus clientStatus = hsu.get(ClientStatus.class, in.getIds()[idx]);
				clientStatus.setSeqNo(idx);
				hsu.saveOrUpdate(clientStatus);
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
	public SaveClientStatusReturn saveClientStatus(/*@WebParam(name = "in")*/final SaveClientStatusInput in)
	{
		final SaveClientStatusReturn ret=new SaveClientStatusReturn();
		try
		{
			checkLogin(in);
			
			final BClientStatus x=new BClientStatus(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
