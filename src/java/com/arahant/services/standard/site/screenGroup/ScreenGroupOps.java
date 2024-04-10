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



package com.arahant.services.standard.site.screenGroup;

import com.arahant.beans.Screen;
import com.arahant.beans.ScreenGroup;
import com.arahant.beans.ScreenGroupHierarchy;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.HibernateCriteriaUtil;
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
Arahant: so i can 1. associate a screen which becomes a sgh record with screen filled in  
... 2. associate a group which becomes a sgq record with group filled in ... 
3.  associate a group with parent which becomes a sgh record with group filled in ... 
4. add a new group which becomes both a group with a parent and a sqh with a group filled in
Arahant: and as we said i can't associate a parent screen, 
so i can't create a sgh with a screen filled in that is a parent
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSiteScreenGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ScreenGroupOps extends ServiceBase {
	
	private static final ArahantLogger logger = new ArahantLogger(ScreenGroupOps.class);
	
	@WebMethod()
	public DeleteScreensAndGroupsReturn deleteScreensAndGroups (/*@WebParam(name = "in")*/final DeleteScreensAndGroupsInput in) {
		final DeleteScreensAndGroupsReturn ret=new DeleteScreensAndGroupsReturn();
		try {
			checkLogin(in);
			
			if (!in.getDeepDelete())
			{
				// groups are deleted, screens are dissassociated only
				BScreenGroup.delete(hsu, in.getScreenGroupIds());
            
			}
			else
			{
				BScreenGroup.deepDelete(in.getScreenGroupIds());
			}
            if (!isEmpty(in.getParentScreenGroupId()))
                new BScreenGroup(in.getParentScreenGroupId()).removeScreensAndGroups(new String[0], in.getScreenIds());
            
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public ListScreensAndGroupsForScreenGroupReturn listScreensAndGroupsForScreenGroup(/*@WebParam(name = "in")*/final ListScreensAndGroupsForScreenGroupInput in)	{
		final ListScreensAndGroupsForScreenGroupReturn ret=new ListScreensAndGroupsForScreenGroupReturn();
		
		try
		{
			checkLogin(in);
			
			if (!isEmpty(in.getScreenGroupId()))
				ret.setScreenDefs(new BScreenGroup(in.getScreenGroupId()).listChildren(),in.getScreenGroupId());
			else
				ret.setScreenDefs(BScreenGroup.listWithoutChildren(hsu),null);
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in)	{
		final SearchScreenGroupsReturn ret=new SearchScreenGroupsReturn();

		try
		{
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroupsWithParents(hsu, in.getName(),in.getExtId(),in.getAssociatedIndicator(),in.getScreenGroupId(),in.getTypeIndicator(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public MoveScreenUpReturn moveScreenUp(/*@WebParam(name = "in")*/final MoveScreenUpInput in)	{
		final MoveScreenUpReturn ret=new MoveScreenUpReturn();
		try {
			checkLogin(in);

			new BScreenGroup(in.getScreenGroupId()).moveScreenUp(in.getScreenId());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public MoveScreenDownReturn moveScreenDown(/*@WebParam(name = "in")*/final MoveScreenDownInput in)	{
		final MoveScreenDownReturn ret=new MoveScreenDownReturn();
		try {
			checkLogin(in);
			
			new BScreenGroup(in.getScreenGroupId()).moveScreenDown(in.getScreenId());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public MoveScreenGroupUpReturn moveScreenGroupUp(/*@WebParam(name = "in")*/final MoveScreenGroupUpInput in)	{
		final MoveScreenGroupUpReturn ret=new MoveScreenGroupUpReturn();

		try {
			checkLogin(in);
			
			new BScreenGroup(in.getParentScreenGroupId()).moveScreenGroupUp(in.getChildScreenGroupId());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public MoveScreenGroupDownReturn moveScreenGroupDown(/*@WebParam(name = "in")*/final MoveScreenGroupDownInput in)	{
		final MoveScreenGroupDownReturn ret=new MoveScreenGroupDownReturn();

		try {
			checkLogin(in);
			
			new BScreenGroup(in.getParentScreenGroupId()).moveScreenGroupDown(in.getChildScreenGroupId());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public RemoveScreensAndGroupsFromGroupReturn removeScreensAndGroupsFromGroup(/*@WebParam(name = "in")*/final RemoveScreensAndGroupsFromGroupInput in) {
		final RemoveScreensAndGroupsFromGroupReturn ret=new RemoveScreensAndGroupsFromGroupReturn();
		
		try {
			checkLogin(in);

			new BScreenGroup(in.getParentScreenGroupId()).removeScreensAndGroups(in.getScreenGroupIds(),in.getScreenIds());
							
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}
	
	@WebMethod()
	public SaveScreenGroupReturn saveScreenGroup(/*@WebParam(name = "in")*/final SaveScreenGroupInput in)	{
		final SaveScreenGroupReturn ret=new SaveScreenGroupReturn();
		
		try
		{
			checkLogin(in);
			
			final BScreenGroup s=new BScreenGroup(in.getScreenGroupId());
			
			in.setData(s);
			s.update();
			
			// if we have a parent screen group, set the label and the default screen flag
			if (!isEmpty(in.getParentScreenGroupId()))
			{
				final BScreenGroup parentScreenGroup = new BScreenGroup(in.getParentScreenGroupId());
				parentScreenGroup.setScreenHierarchyData(s.getScreenGroupId(), null, in.getDefaultScreen(),in.getLabel());
			}
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewScreenGroupReturn newScreenGroup(/*@WebParam(name = "in")*/final NewScreenGroupInput in)	{
		final NewScreenGroupReturn ret=new NewScreenGroupReturn();
		
		try
		{
			checkLogin(in);
			
			final BScreenGroup s=new BScreenGroup();
			s.create();
			in.setData(s);
			s.insert();
			
			final String []screenIds=new String[0];
			final String []groupIds=new String[1];
			groupIds[0]=s.getScreenGroupId();
			
			if (!isEmpty(in.getParentScreenGroupId()))
			{
				final BScreenGroup parentScreenGroup = new BScreenGroup(in.getParentScreenGroupId());
				
				parentScreenGroup.add(groupIds, screenIds);
				
				parentScreenGroup.setScreenGroupButtonName(s.getScreenGroupId(), in.getLabel());
				parentScreenGroup.setScreenDefault(s.getScreenGroupId(), null, in.getDefaultScreen());
			}
			
			ret.setScreenGroupId(s.getScreenGroupId());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public AddScreensAndGroupsToGroupReturn addScreensAndGroupsToGroup(/*@WebParam(name = "in")*/final AddScreensAndGroupsToGroupInput in)	{
		final AddScreensAndGroupsToGroupReturn ret=new AddScreensAndGroupsToGroupReturn();
		
		try
		{
			checkLogin(in);
			
			new BScreenGroup(in.getParentGroupId()).add(in.getGroupIds(),in.getScreenIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchParentScreensReturn searchParentScreens(/*@WebParam(name = "in")*/final SearchParentScreensInput in)	{
		final SearchParentScreensReturn ret=new SearchParentScreensReturn();

		try
		{
			checkLogin(in);
			
			ret.setData(BScreen.searchParentScreens(hsu,in.getName(),in.getExtId()));
					
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SaveScreenReturn saveScreen(/*@WebParam(name = "in")*/final SaveScreenInput in)			{
		final SaveScreenReturn ret=new SaveScreenReturn();
		try
		{
			checkLogin(in);
			
			final BScreenGroup x=new BScreenGroup(in.getParentScreenGroupId());
			x.setScreenHierarchyData(null, in.getScreenId(), in.getDefaultScreen(), in.getLabel());	
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CopyScreenGroupReturn copyScreenGroup(/*@WebParam(name = "in")*/final CopyScreenGroupInput in)			{
		final CopyScreenGroupReturn ret=new CopyScreenGroupReturn();
		try
		{
			checkLogin(in);
			
			final BScreenGroup s=new BScreenGroup(in.getScreenGroupId());
			ret.setScreenGroupId(s.copyScreenGroup(in.getScreenGroupName(), in.getAssociate(), in.getParentScreenGroupId(), in.getShallowCopy()));
			
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
			
			ret.setReportUrl(BScreenGroup.getReport(in.getStartFromScreenGroupId(), in.getShowIds(), in.getShowLabels(), in.getShowSubHeaders()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	public static void main(String args[]) throws Exception
	{
		CopyScreenGroupInput in=new CopyScreenGroupInput();
		in.setAssociate(false);
		in.setParentScreenGroupId("");
		in.setScreenGroupId("00000-0000000000");
		in.setScreenGroupName("Copied");
		in.setShallowCopy(false);
		CopyScreenGroupReturn value=new ScreenGroupOps().copyScreenGroup(in);
	}

	@WebMethod()
	public ReorderScreensAndScreenGroupsReturn reorderScreensAndScreenGroups(/*@WebParam(name = "in")*/final ReorderScreensAndScreenGroupsInput in)		
	{
		final ReorderScreensAndScreenGroupsReturn ret=new ReorderScreensAndScreenGroupsReturn();
		try
		{
			checkLogin(in);
			
			final BScreenGroup x=new BScreenGroup(in.getParentScreenGroupId());
			
			ScreenGroup sg=hsu.get(ScreenGroup.class, in.getParentScreenGroupId());
			
			for (ScreenGroupHierarchy sgh : hsu.createCriteria(ScreenGroupHierarchy.class)
				.eq(ScreenGroupHierarchy.PARENTSCREENGROUP, sg).list())
			{
				sgh.setSeqNo((short) (sgh.getSeqNo() + 1000));
				hsu.saveOrUpdate(sgh);
			}
				
			hsu.flush();
			
			ReorderScreensAndScreenGroupsInputItem []i=in.getItem();
			
			for (short loop=0;loop<i.length;loop++)
			{
				HibernateCriteriaUtil<ScreenGroupHierarchy> hcu=hsu.createCriteria(ScreenGroupHierarchy.class)
					.eq(ScreenGroupHierarchy.PARENTSCREENGROUP, sg);
				
				if (i[loop].getType().equals("Group"))
					hcu.joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP)
						.eq(ScreenGroup.SCREENGROUPID, i[loop].getId());
				else
					hcu.joinTo(ScreenGroupHierarchy.SCREEN)
						.eq(Screen.SCREENID, i[loop].getId());
				
				ScreenGroupHierarchy sgh=hcu.first();
				
				sgh.setSeqNo(loop);
				
				hsu.saveOrUpdate(sgh);
				
				hsu.flush();
			}
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}
