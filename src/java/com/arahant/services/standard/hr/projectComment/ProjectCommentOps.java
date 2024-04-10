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
package com.arahant.services.standard.hr.projectComment;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrProjectCommentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectCommentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectCommentOps.class);
	
	public ProjectCommentOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public LoadCommentSummaryReturn loadCommentSummary(/*@WebParam(name = "in")*/final LoadCommentSummaryInput in)			{
		final LoadCommentSummaryReturn ret=new LoadCommentSummaryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewCommentReturn newComment(/*@WebParam(name = "in")*/final NewCommentInput in)			{
		final NewCommentReturn ret=new NewCommentReturn();
		try
		{
			checkLogin(in);
			
			final BProjectComment x=new BProjectComment();
			ret.setId(x.create());
			in.setData(x);
			x.setPerson(new BPerson(hsu.getCurrentPerson()));
			x.insert();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveCommentReturn saveComment(/*@WebParam(name = "in")*/final SaveCommentInput in)			{
		final SaveCommentReturn ret=new SaveCommentReturn();
		try
		{
			checkLogin(in);
			
			final BProjectComment x=new BProjectComment(in.getCommentId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
