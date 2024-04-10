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
package com.arahant.services.standard.hr.hrWageHistory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.beans.HrWage;
import com.arahant.exceptions.ArahantException;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 
 *
 * Created on Mar 1, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrWageHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRWageHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HRWageHistoryOps.class);

	public HRWageHistoryOps() {
		super();
	}
	
	@WebMethod()
	public DeleteWageHistoryItemReturn deleteWageHistoryItem(/*@WebParam(name = "in")*/final DeleteWageHistoryItemInput in)	{
		final DeleteWageHistoryItemReturn ret=new DeleteWageHistoryItemReturn();
		try
		{
			checkLogin(in);
			
			BHRWage.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	} 
	@WebMethod()
	public GetWageHistoryItemReportReturn getWageHistoryItemReport(/*@WebParam(name = "in")*/final GetWageHistoryItemReportInput in)	{
		final GetWageHistoryItemReportReturn ret=new GetWageHistoryItemReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setFileName(BHRWage.getReport(hsu,in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public ListWageHistoryItemsReturn listWageHistoryItems(/*@WebParam(name = "in")*/final ListWageHistoryItemsInput in)	{
		final ListWageHistoryItemsReturn ret=new ListWageHistoryItemsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHRWage.list(hsu,in.getEmployeeId()),BRight.checkRight(IS_OWNER)==ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public NewWageHistoryItemReturn newWageHistoryItem(/*@WebParam(name = "in")*/final NewWageHistoryItemInput in)			{
		final NewWageHistoryItemReturn ret=new NewWageHistoryItemReturn();
		try
		{
			checkLogin(in);

			List<HrWage> recs = hsu.createCriteria(HrWage.class).eq(HrWage.EMPLOYEEID, in.getEmployeeId()).eq(HrWage.EFFECTIVEDATE, in.getEffectiveDate()).list();
			if (recs != null  &&  !recs.isEmpty())
				throw new ArahantException("Employee already has an entry for the selected effective date.");

			final BHRWage x=new BHRWage();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SaveWageHistoryItemReturn saveWageHistoryItem(/*@WebParam(name = "in")*/final SaveWageHistoryItemInput in)	{
		final SaveWageHistoryItemReturn ret=new SaveWageHistoryItemReturn();
		try
		{
			checkLogin(in);
			
			final BHRWage x=new BHRWage(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public LoadWageHistoryReturn loadWageHistory(/*@WebParam(name = "in")*/final LoadWageHistoryInput in)	{
		final LoadWageHistoryReturn ret=new LoadWageHistoryReturn();
		try {
			checkLogin(in);
			
			ret.setData(new BHRWage(in.getId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in)	{
		final ListPositionsReturn ret=new ListPositionsReturn();
		try
		{
			checkLogin(in);



			ArrayList<BHRPosition> l=new ArrayList<BHRPosition>();
			Collections.addAll(l, BHRPosition.list(hsu));

			if (!isEmpty(in.getHistoryId()))
			{
				BHRWage hist=new BHRWage(in.getHistoryId());
				boolean add=true;

				for (BHRPosition s :l)
				{
					if (s.getPositionId().equals(hist.getPositionId()))
						add=false;
				}

				if (add)
					l.add(new BHRPosition(hist.getPositionId()));
			}


			ret.setItem(l.toArray(new BHRPosition[l.size()]));
			
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
	public GetLastWageReturn getLastWage(/*@WebParam(name = "in")*/final GetLastWageInput in)	{
		final GetLastWageReturn ret=new GetLastWageReturn();
		
		try {
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getPersonId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
 
    @WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in)		
	{
		final ListWageTypesReturn ret=new ListWageTypesReturn();
		try
		{
			checkLogin(in);

			String id=null;

			if (!isEmpty(in.getHistoryId()))
				id=new BHRWage(in.getHistoryId()).getWageTypeId();

			ret.setItem(BWageType.listActiveNonDedutionsPlus(id));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
