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
 * Created on Mar 1, 2007
 * 
 */
package com.arahant.services.standard.hr.hrStatusHistory;

import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.ProjectEmployeeJoin;
import com.arahant.beans.ProphetLogin;
import com.arahant.business.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;

import com.arahant.services.main.UserCache;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.reports.TerminationReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 
 *
 * Created on Mar 1, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrStatusHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRStatusHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRStatusHistoryOps.class);

	public HRStatusHistoryOps() {
		super();
	}
	
	@WebMethod()
	public DeleteStatusHistoryItemReturn deleteStatusHistoryItem(/*@WebParam(name = "in")*/final DeleteStatusHistoryItemInput in)	{
		final DeleteStatusHistoryItemReturn ret=new DeleteStatusHistoryItemReturn();
		try
		{
			checkLogin(in);
			
			if (!in.getConfirmed()) {
				// should be safe assumption here that all ids are from same employee
				BEmployee bEmployee = new BHREmplStatusHistory(in.getIds()[0]).getEmployee();
				
				// we need to delete without cascaded status changes first
				BHREmplStatusHistory.delete(hsu,in.getIds(), true);
				
				ret.setConfirmations(bEmployee.getConfirmationsForStatusChange());

				// rollback and start the transaction again as we will either do it
				// for real with cascades or we don't want to commit yet
				hsu.rollbackTransaction();
				hsu.beginTransaction();
			}
			
			// check if we should make the changes with cascaded status changes
			if (ret.getConfirmations()==null || ret.getConfirmations().length==0) {
				// no confirmations, so delete again with cascaded
				BHREmplStatusHistory.delete(hsu,in.getIds());
			}
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public GetStatusHistoryItemReportReturn getStatusHistoryItemReport(/*@WebParam(name = "in")*/final GetStatusHistoryItemReportInput in)	{
		final GetStatusHistoryItemReportReturn ret=new GetStatusHistoryItemReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setFileName(BHREmplStatusHistory.getReport(hsu,in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public ListStatusHistoryItemsReturn listStatusHistoryItems(/*@WebParam(name = "in")*/final ListStatusHistoryItemsInput in)	{
		final ListStatusHistoryItemsReturn ret=new ListStatusHistoryItemsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHREmplStatusHistory.list(hsu,in.getEmployeeId()),BRight.checkRight(IS_OWNER)==ACCESS_LEVEL_WRITE);
			
			BEmployee bemp=new BEmployee(in.getEmployeeId());
			
			ret.setYearsOfServiceFormatted(bemp.getSeniority(DateUtils.now()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public NewStatusHistoryItemReturn newStatusHistoryItem(/*@WebParam(name = "in")*/final NewStatusHistoryItemInput in)			{
		final NewStatusHistoryItemReturn ret=new NewStatusHistoryItemReturn();
		try
		{
			checkLogin(in);
			
			if (!in.getConfirmed()) {
				// we need to insert without cascaded status changes first
				final BHREmplStatusHistory bStatusHistoryTest=new BHREmplStatusHistory();
				bStatusHistoryTest.create();
				in.setData(bStatusHistoryTest);
				bStatusHistoryTest.insert(true);
				
				ret.setConfirmations(bStatusHistoryTest.getEmployee().getConfirmationsForStatusChange());

				// rollback and start the transaction again as we will either do it
				// for real with cascades or we don't want to commit yet
				hsu.rollbackTransaction();
				hsu.beginTransaction();
			}
			
			// check if we should make the changes with cascaded status changes
			if (ret.getConfirmations()==null || ret.getConfirmations().length==0) {
				// no confirmations, so insert again with cascaded
				final BHREmplStatusHistory bStatusHistory=new BHREmplStatusHistory();
				ret.setId(bStatusHistory.create());
				in.setData(bStatusHistory);
				bStatusHistory.insert();

				/*   If it is an inactive status, de-associate them with all current and future projects. */
				BPerson bp = new BPerson(in.getEmployeeId());
				final HrEmployeeStatus s = bStatusHistory.getHrEmployeeStatus();
				if (s.getActive() == 'N') {
					List<ProjectEmployeeJoin> assignments = hsu.createCriteria(ProjectEmployeeJoin.class).eq(ProjectEmployeeJoin.PERSON, bp.getPerson()).list();
					for (ProjectEmployeeJoin pej : assignments) {
	//  XXYY					BProject tbp = new BProject(pej.getProject());
	// XXYY					tbp.removeAssignment(pej.getPerson().getPersonId());
	// XXYY					tbp.update();
					}

					//   make sure their login is disabled
					ProphetLogin pl = hsu.get(ProphetLogin.class, in.getEmployeeId());
					if (pl != null && pl.getCanLogin() == 'Y')
						pl.setCanLogin('N');
					UserCache.removePerson(in.getEmployeeId(), null);
				}
			}
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SaveStatusHistoryItemReturn saveStatusHistoryItem(/*@WebParam(name = "in")*/final SaveStatusHistoryItemInput in)	{
		final SaveStatusHistoryItemReturn ret=new SaveStatusHistoryItemReturn();
		try
		{
			checkLogin(in);
			
			if (!in.getConfirmed()) {
				// we need to insert without cascaded status changes first
				final BHREmplStatusHistory bStatusHistoryTest=new BHREmplStatusHistory(in.getStatusHistoryItemId());
				in.setData(bStatusHistoryTest);
				bStatusHistoryTest.update(true);
				
				ret.setConfirmations(bStatusHistoryTest.getEmployee().getConfirmationsForStatusChange());

				// rollback and start the transaction again as we will either do it
				// for real with cascades or we don't want to commit yet
				hsu.rollbackTransaction();
				hsu.beginTransaction();
			}

			final BHREmplStatusHistory bStatusHistory=new BHREmplStatusHistory(in.getStatusHistoryItemId());

			// check if we should make the changes with cascaded status changes
			if (ret.getConfirmations()==null || ret.getConfirmations().length==0) {
				// no confirmations, so insert (again) with cascaded
				in.setData(bStatusHistory);
				bStatusHistory.update();
			}

			/*   If it is an inactive status, de-associate them with all current and future projects. */

			String personId = bStatusHistory.getEmployee().getPersonId();
			BPerson bp = new BPerson(personId);
			final HrEmployeeStatus s = bStatusHistory.getHrEmployeeStatus();
			if (s.getActive() == 'N') {
				List<ProjectEmployeeJoin> assignments = hsu.createCriteria(ProjectEmployeeJoin.class).eq(ProjectEmployeeJoin.PERSON, bp.getPerson()).list();
				for (ProjectEmployeeJoin pej : assignments)
					hsu.delete(pej);

				//   make sure their login is disabled
				ProphetLogin pl = hsu.get(ProphetLogin.class, personId);
				if (pl != null && pl.getCanLogin() == 'Y')
					pl.setCanLogin('N');
				UserCache.removePerson(personId, null);
			}

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public LoadStatusHistoryReturn loadStatusHistory(/*@WebParam(name = "in")*/final LoadStatusHistoryInput in)	{
		final LoadStatusHistoryReturn ret=new LoadStatusHistoryReturn();
		try {
			checkLogin(in);
			
			ret.setData(new BHREmplStatusHistory(in.getId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessStatusHistory"));

			finishService(ret);
		} catch (final Throwable e) {
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

			ArrayList<BHREmployeeStatus> l=new ArrayList<BHREmployeeStatus>();
			Collections.addAll(l, BHREmployeeStatus.list(hsu));

			if (!isEmpty(in.getHistoryId()))
			{
				BHREmplStatusHistory hist=new BHREmplStatusHistory(in.getHistoryId());
				boolean add=true;

				for (BHREmployeeStatus s :l)
				{
					if (s.getEmployeeStatusId().equals(hist.getStatusId()))
						add=false;
				}

				if (add)
					l.add(new BHREmployeeStatus(hist.getHrEmployeeStatus()));
			}


			ret.setItem(l.toArray(new BHREmployeeStatus[l.size()]));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetTerminationReportReturn getTerminationReport(/*@WebParam(name = "in")*/final GetTerminationReportInput in)	{
		final GetTerminationReportReturn ret=new GetTerminationReportReturn();
		try
		{
			checkLogin(in);

			ret.setFileName(new TerminationReport().buildEmployee(in.getEmployeeId()));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
	
