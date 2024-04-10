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


package com.arahant.services.standard.project.projectComment;

import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.ProjectPhase;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectCommentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectCommentOps extends ServiceBase {
	
	static ArahantLogger logger = new ArahantLogger(ProjectCommentOps.class);
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjectComments"));

			ret.setInternalCommentAccessLevel(BRight.checkRight("AccessProjectInternalComments"));

			BProject bp=new BProject(in.getProjectId());
			int phase=bp.getPhaseSecurityLevel();
	
			BRouteStop brs=new BRouteStop(bp.getRouteStopId());
		//	logger.info(brs.getOrgGroup().getName());
			boolean atLocation=brs.getOrgGroup()==null || hsu.createCriteria(OrgGroupAssociation.class)
					.eq(OrgGroupAssociation.PERSON,hsu.getCurrentPerson())
					.eq(OrgGroupAssociation.ORGGROUP,brs.getOrgGroup())
					.exists();
			//if I'm a client and it's in my court and it's in estimate phase, then write level
			if (phase==ProjectPhase.ESTIMATE &&((hsu.getCurrentPerson().getOrgGroupType()==EMPLOYEE_TYPE) 
					||(hsu.getCurrentPerson().getOrgGroupType()==CLIENT_TYPE && atLocation))) //estimate
				ret.setDetailAccessLevel(ACCESS_LEVEL_WRITE);
			else	
				ret.setDetailAccessLevel(BRight.checkRight("AccessProjectDetail"));
			
			
			// add is only available if project is active
			BProject project = new BProject(in.getProjectId());
			int addAccessLevel = project.getProjectStatus().getActive()==1 ? ACCESS_LEVEL_WRITE : ACCESS_LEVEL_READ_ONLY;
			if (BRight.checkRight("AccessProjectComments") < addAccessLevel)
				addAccessLevel = BRight.checkRight("AccessProjectComments");
			ret.setAddAccessLevel(addAccessLevel);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	

	@WebMethod()
	public AddProjectCommentReturn addProjectComment(/*@WebParam(name = "in")*/final AddProjectCommentInput in) {
		final AddProjectCommentReturn ret = new AddProjectCommentReturn();

		try {
			checkLogin(in);

			final BProject bp = new BProject(in.getProjectId());

			ret.setId(bp.addComment(BPerson.getCurrent(), in.getComment(), in.getInternalFlag(), in.getShiftId()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	
	@WebMethod()
	public LoadCommentsSummaryReturn loadCommentsSummary (/*@WebParam(name = "in")*/final LoadCommentsSummaryInput in)	{
		final LoadCommentsSummaryReturn ret=new LoadCommentsSummaryReturn();
		
		try {
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()), in.getShiftId());
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;	
	}
	
	@WebMethod()
	public LoadCommentReturn loadComment(/*@WebParam(name = "in")*/final LoadCommentInput in)	{
		LoadCommentReturn ret=new LoadCommentReturn();

		try {
			checkLogin(in);

			ret=new LoadCommentReturn(new BProjectComment(in.getCommentId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SaveProjectCommentReturn saveProjectComment(/*@WebParam(name = "in")*/final SaveProjectCommentInput in)	{
		final SaveProjectCommentReturn ret=new SaveProjectCommentReturn();

		try {
			checkLogin(in);
			
			final BProjectComment bp=new BProjectComment(in.getCommentId());
			
			if (!hsu.currentlySuperUser() && (DateUtils.getDate(bp.getDateEntered())!=DateUtils.now() ||
					!hsu.getCurrentPerson().getPersonId().equals(bp.getPersonId())))
				throw new ArahantException("Can't change comment detail.  Must be original author on same day entered.");
				
			
			bp.setInternal(in.getInternalFlag()?'Y':'N');
			bp.setCommentTxt(in.getComment());
			bp.update();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		

		return ret;
	}
	

	

	@WebMethod()
	public SaveCommentsSummaryReturn saveCommentsSummary(/*@WebParam(name = "in")*/final SaveCommentsSummaryInput in)			{
		final SaveCommentsSummaryReturn ret=new SaveCommentsSummaryReturn();
		try
		{
			checkLogin(in);
			
			final BProject x=new BProject(in.getProjectId());
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
    public DeleteProjectCommentsReturn deleteProjectComments(/*@WebParam(name = "in")*/final DeleteProjectCommentsInput in)		
	{
		final DeleteProjectCommentsReturn ret=new DeleteProjectCommentsReturn();
		try
		{
			checkLogin(in);
			for (String id : in.getIds())
			{
				BProjectComment bp=new BProjectComment(id);
				if (!hsu.currentlySuperUser() && (DateUtils.getDate(bp.getDateEntered())!=DateUtils.now() ||
					!hsu.getCurrentPerson().getPersonId().equals(bp.getPersonId())))
					throw new ArahantException("Can't delete comment detail.  Must be original author on same day entered.");
			}
			
			BProjectComment.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
