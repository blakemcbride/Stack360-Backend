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
package com.arahant.services.standard.crm.salesActivity;

import com.arahant.beans.Right;
import com.arahant.beans.SalesActivity;
import com.arahant.business.BRight;
import com.arahant.business.BSalesActivity;
import com.arahant.business.BSalesActivityResult;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
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
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmSalesActivityOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class SalesActivityOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            SalesActivityOps.class);

    public SalesActivityOps() {
        super();
    }

    @WebMethod()
    public ListActivitiesReturn listActivities(/*@WebParam(name = "in")*/final ListActivitiesInput in) {
        final ListActivitiesReturn ret = new ListActivitiesReturn();
        try {
            checkLogin(in);
			
            ret.setItem(BSalesActivity.list(ret.getCap(), in.getShowActive(), in.getShowInactive()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public NewActivityReturn newActivity(/*@WebParam(name = "in")*/final NewActivityInput in) {
        final NewActivityReturn ret = new NewActivityReturn();
        try {
            checkLogin(in);
            BSalesActivity ba = new BSalesActivity();
            ba.create();
            ba.setActivityCode(in.getCode());
            ba.setDescription(in.getDescription());
            ba.setSalesPoints(in.getPoints());
			ba.setLastActiveDate(in.getLastActiveDate());
			ba.setSeqno((short)ArahantSession.getHSU().createCriteria(SalesActivity.class).count());
			if (in.getAllCompanies())
				ba.setCompanyId("");
			else
				ba.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());

            ba.insert();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SaveActivityReturn saveActivity(/*@WebParam(name = "in")*/final SaveActivityInput in) {
        final SaveActivityReturn ret = new SaveActivityReturn();
        try {
            checkLogin(in);

            BSalesActivity ba = new BSalesActivity(in.getId());
            ba.setActivityCode(in.getCode());
            ba.setDescription(in.getDescription());
            ba.setSalesPoints(in.getPoints());
			ba.setLastActiveDate(in.getLastActiveDate());
			if (in.getAllCompanies())
				ba.setCompanyId("");
			else
				ba.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
            ba.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DeleteActivitiesReturn deleteActivities(/*@WebParam(name = "in")*/final DeleteActivitiesInput in) {
        final DeleteActivitiesReturn ret = new DeleteActivitiesReturn();
        try {
            checkLogin(in);

			try {
				BSalesActivity.delete(in.getIds());
				finishService(ret);
			} catch (Exception ex) {
				throw new ArahantWarning("Cannot delete activity because it is being used elsewhere.");
			}

        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
	public ReorderActivitiesReturn reorderActivities(/*@WebParam(name = "in")*/final ReorderActivitiesInput in)		
	{
		final ReorderActivitiesReturn ret=new ReorderActivitiesReturn();
		try
		{
			checkLogin(in);
			
			BSalesActivity bsa;

			List<String> ids = new ArrayList();
			
			for (String s: in.getIds())
			{
				ids.add(s);
			}

			for (BSalesActivity sa : BSalesActivity.list(ret.getCap(), false, true))
			{
				ids.add(sa.getSalesActivityId());
			}

			int i = 0;
			for (String s: ids)
			{
				bsa = new BSalesActivity(s);
				bsa.setSeqno((short)i);
				bsa.update();
				i++;
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public MoveActivityReturn moveActivity(/*@WebParam(name = "in")*/final MoveActivityInput in)		
	{
		final MoveActivityReturn ret=new MoveActivityReturn();
		try
		{
			checkLogin(in);
			
			final BSalesActivity x=new BSalesActivity(in.getId());
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);

			if ((BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==BRight.ACCESS_LEVEL_WRITE) && ArahantSession.multipleCompanySupport)
			{
				ret.setAllCompanyAccess(true);
			}
			else
				ret.setAllCompanyAccess(false);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
    @WebMethod()
	public ListResultsReturn listResults(/*@WebParam(name = "in")*/final ListResultsInput in)		
	{
		final ListResultsReturn ret=new ListResultsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BSalesActivityResult.list(ret.getCap(), in.getShowActive(), in.getShowInactive(), in.getActivityId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
 
    @WebMethod()
	public NewResultReturn newResult(/*@WebParam(name = "in")*/final NewResultInput in)		
	{
		final NewResultReturn ret=new NewResultReturn();
		try
		{
			checkLogin(in);  
			
			final BSalesActivityResult x=new BSalesActivityResult();
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
	public SaveResultReturn saveResult(/*@WebParam(name = "in")*/final SaveResultInput in)		
	{
		final SaveResultReturn ret=new SaveResultReturn();
		try
		{
			checkLogin(in);
			
			final BSalesActivityResult x=new BSalesActivityResult(in.getId());
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
    public DeleteResultsReturn deleteResults(/*@WebParam(name = "in")*/final DeleteResultsInput in)		
	{
		final DeleteResultsReturn ret=new DeleteResultsReturn();
		try
		{
			checkLogin(in);
			
			BSalesActivityResult.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public MoveResultReturn moveResult(/*@WebParam(name = "in")*/final MoveResultInput in)		
	{
		final MoveResultReturn ret=new MoveResultReturn();
		try
		{
			checkLogin(in);
			
			final BSalesActivityResult x=new BSalesActivityResult(in.getId());
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
	public ReorderResultsReturn reorderResults(/*@WebParam(name = "in")*/final ReorderResultsInput in)		
	{
		final ReorderResultsReturn ret=new ReorderResultsReturn();
		try
		{
			checkLogin(in);
			
			BSalesActivityResult bsar;

			List<String> ids = new ArrayList();

			for (String s: in.getIds())
			{
				ids.add(s);
			}

			for (BSalesActivityResult sar : BSalesActivityResult.listInactives(ret.getCap()))
			{
				ids.add(sar.getSalesActivityResultId());
			}

			int i = 0;
			for (String s: ids)
			{
				bsar = new BSalesActivityResult(s);
				bsar.setSequence((short)(i + 99999));
				bsar.update();
				i++;
			}
			
			hsu.flush();

			i = 0;
			for (String s: ids)
			{
				bsar = new BSalesActivityResult(s);
				bsar.setSequence((short)i);
				bsar.update();
				i++;
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public LoadResultReturn loadResult(/*@WebParam(name = "in")*/final LoadResultInput in)		
	{
		final LoadResultReturn ret=new LoadResultReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BSalesActivityResult(in.getResultId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
