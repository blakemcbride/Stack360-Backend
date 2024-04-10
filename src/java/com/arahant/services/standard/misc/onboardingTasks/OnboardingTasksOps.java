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
package com.arahant.services.standard.misc.onboardingTasks;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.services.standard.hr.benefitConfigAdvancedCost.DeleteConfigsInput;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscOnboardingTasksOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class OnboardingTasksOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			OnboardingTasksOps.class);
	
	public OnboardingTasksOps() {
		super();
	}
	
    @WebMethod()
	public ListOnboardingConfigsReturn listOnboardingConfigs(/*@WebParam(name = "in")*/final ListOnboardingConfigsInput in)		
	{
		final ListOnboardingConfigsReturn ret=new ListOnboardingConfigsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOnboardingConfig.makeArray(BOnboardingConfig.list(ret.getCap())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListTasksForConfigReturn listTasksForConfig(/*@WebParam(name = "in")*/final ListTasksForConfigInput in)		
	{
		final ListTasksForConfigReturn ret=new ListTasksForConfigReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOnboardingTask.makeArray(BOnboardingTask.list(in.getConfigId(), ret.getCap())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListOnboardingScreensReturn listOnboardingScreens(/*@WebParam(name = "in")*/final ListOnboardingScreensInput in)
	{
		final ListOnboardingScreensReturn ret=new ListOnboardingScreensReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOnboardingTask.listOnboardingScreens());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

    @WebMethod()
	public NewConfigReturn newConfig(/*@WebParam(name = "in")*/final NewConfigInput in)		
	{
		final NewConfigReturn ret=new NewConfigReturn();
		try
		{
			checkLogin(in);
			
			final BOnboardingConfig x=new BOnboardingConfig();
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
	public SaveConfigReturn saveConfig(/*@WebParam(name = "in")*/final SaveConfigInput in)		
	{
		final SaveConfigReturn ret=new SaveConfigReturn();
		try
		{
			checkLogin(in);
			
			final BOnboardingConfig x=new BOnboardingConfig(in.getConfigId());
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);

			ret.setCanAccessAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == BRight.ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	 @WebMethod()
	public NewTaskReturn newTask(/*@WebParam(name = "in")*/final NewTaskInput in)
	{
		final NewTaskReturn ret=new NewTaskReturn();
		try
		{
			checkLogin(in);

			final BOnboardingTask x=new BOnboardingTask();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		}
		catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}



    @WebMethod()
	public SaveTaskReturn saveTask(/*@WebParam(name = "in")*/final SaveTaskInput in)
	{
		final SaveTaskReturn ret=new SaveTaskReturn();
		try
		{
			checkLogin(in);

			final BOnboardingTask x=new BOnboardingTask(in.getTaskId());
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
    public DeleteConfigsReturn deleteConfigs(/*@WebParam(name = "in")*/final DeleteConfigsInput in)		
	{
		final DeleteConfigsReturn ret=new DeleteConfigsReturn();
		try
		{
			checkLogin(in);
			
			BOnboardingConfig.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
    public DeleteTasksReturn deleteTasks(/*@WebParam(name = "in")*/final DeleteTasksInput in)		
	{
		final DeleteTasksReturn ret=new DeleteTasksReturn();
		try
		{
			checkLogin(in);
			
			BOnboardingTask.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public ReorderTasksReturn reorderTasks(/*@WebParam(name = "in")*/final ReorderTasksInput in)
	{
		final ReorderTasksReturn ret=new ReorderTasksReturn();
		try
		{
			checkLogin(in);

			BOnboardingTask b;

			int i = 0;
			for (String s: in.getIds())
			{
				b = new BOnboardingTask(s);
				b.setSeqno(i);
				b.update();
				i++;
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
