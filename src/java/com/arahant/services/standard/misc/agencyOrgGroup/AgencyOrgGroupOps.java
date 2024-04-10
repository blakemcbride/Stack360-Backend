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
package com.arahant.services.standard.misc.agencyOrgGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscAgencyOrgGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AgencyOrgGroupOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AgencyOrgGroupOps.class);
	
	public AgencyOrgGroupOps() {
		super();
	}
	

	@WebMethod()
	public SearchAssociatedOrgGroupsReturn searchAssociatedOrgGroups(/*@WebParam(name = "in")*/final SearchAssociatedOrgGroupsInput in)	{
		final SearchAssociatedOrgGroupsReturn ret=new SearchAssociatedOrgGroupsReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setOrgGroups(BOrgGroup.listAssociatedGroups(hsu, in.getGroupId(), AGENT_TYPE, in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public  SearchAgentsReturn searchAgents(/*@WebParam(name = "in")*/final SearchAgentsInput in)	{
		final SearchAgentsReturn ret=new SearchAgentsReturn();
		
		try
		{
			checkLogin(in);
			
			final BAgent []vcs=BAgent.list(hsu, in.getGroupId(),ret.getCap(), in.getName());
			
			ret.setPersons(vcs,in.getGroupId());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public AddGroupToGroupReturn addGroupToGroup(/*@WebParam(name = "in")*/final AddGroupToGroupInput in) {		
		final AddGroupToGroupReturn ret=new  AddGroupToGroupReturn();
		try
		{
			checkLogin(in);
			
			new BOrgGroup(in.getParentGroupID()).assignToThisGroup(in.getChildGroupID());
								
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public DeleteGroupReturn deleteGroup(/*@WebParam(name = "in")*/final DeleteGroupInput in)	{
		final DeleteGroupReturn ret=new DeleteGroupReturn();
		
		try {
			checkLogin(in);
			
			BOrgGroup.delete(hsu, in.getGroupIds());
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public RemoveGroupFromGroupReturn removeGroupFromGroup(/*@WebParam(name = "in")*/final RemoveGroupFromGroupInput in) {		
		final RemoveGroupFromGroupReturn ret=new RemoveGroupFromGroupReturn();
		try
		{
			checkLogin(in);
			
			new BOrgGroup(in.getParentGroupID()).removeGroups(in.getChildGroupIDs());
		
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public DeleteAgencyAgentReturn deleteAgencyAgent(/*@WebParam(name = "in")*/final DeleteAgencyAgentInput in) {
		final DeleteAgencyAgentReturn ret=new DeleteAgencyAgentReturn();

		try {
			checkLogin(in);
			
			BAgent.delete(hsu,in.getIds());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public  RemovePersonFromOrgGroupReturn removePersonFromOrgGroup (/*@WebParam(name = "in")*/final RemovePersonFromOrgGroupInput in) {	
		final RemovePersonFromOrgGroupReturn ret=new RemovePersonFromOrgGroupReturn();
		try
		{
			checkLogin(in);
			
			new BOrgGroup(in.getGroupId()).removePeopleFromGroup(in.getPersonIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchOrgGroupsGenericReturn searchOrgGroupsGeneric(/*@WebParam(name = "in")*/final SearchOrgGroupsGenericInput in)	{
		final SearchOrgGroupsGenericReturn ret=new SearchOrgGroupsGenericReturn();
		
		try {
			checkLogin(in);
			
			ret.setOrgGroups(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(),in.getAssociatedIndicator(), AGENT_TYPE,ret.getCap()));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public NewGroupReturn newGroup(/*@WebParam(name = "in")*/final NewGroupInput in)	{
		final NewGroupReturn ret=new NewGroupReturn();

		try {
			checkLogin(in);
			
			final BOrgGroup bog=new BOrgGroup();
			bog.create();
			bog.setOrgGroupType(AGENT_TYPE);
			bog.setName(in.getName());
			bog.setExternalId(in.getExternalId());
			bog.insert();
			
			if (!isEmpty(in.getParentGroupID()))
				new BOrgGroup(in.getParentGroupID()).assignToThisGroup(bog.getOrgGroupId());
			
			ret.setOrgGroupId(bog.getOrgGroupId());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SaveGroupReturn saveGroup(/*@WebParam(name = "in")*/final SaveGroupInput in)	{
		final SaveGroupReturn ret=new SaveGroupReturn();
		
		try {
			checkLogin(in);
			
			final BOrgGroup bog=new BOrgGroup(in.getOrgGroupId());
			bog.setName(in.getName());
			bog.setExternalId(in.getExternalId());
			bog.update();
				
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public LoadAgencyAgentReturn loadAgencyAgent(/*@WebParam(name = "in")*/final LoadAgencyAgentInput in) {
            final LoadAgencyAgentReturn ret = new LoadAgencyAgentReturn();

		try {
			checkLogin(in);
			
			ret.setData(new BAgent(in.getPersonId(),in.getGroupId()), in.getCompanyName());
			
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchAgencyAgentsReturn searchAgencyAgents(/*@WebParam(name = "in")*/final SearchAgencyAgentsInput in)	{
		final SearchAgencyAgentsReturn ret=new SearchAgencyAgentsReturn();
		
		try
		{
			checkLogin(in);
			
			final BAgent[] v=BAgent.search(hsu, in.getFirstName(), in.getLastName(), in.getOrgGroupId(), in.getAssociatedIndicator(),ret.getCap());
			
			ret.setContacts(v,in.getOrgGroupId());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}

	@WebMethod()
	public AssignPersonToOrgGroupReturn assignPersonToOrgGroup(/*@WebParam(name = "in")*/final AssignPersonToOrgGroupInput in)	{
		final AssignPersonToOrgGroupReturn ret=new AssignPersonToOrgGroupReturn();

		try {
			checkLogin(in);
			
			new BOrgGroup(in.getGroupId()).assignPeopleToGroup(in.getPersonIds());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;

	}

	@WebMethod()
	public NewAgencyAgentReturn newAgencyAgent(/*@WebParam(name = "in")*/final NewAgencyAgentInput in) {
		final NewAgencyAgentReturn ret= new NewAgencyAgentReturn();

		try {
			checkLogin(in);
			
			final BAgent a=new BAgent();
			ret.setId(a.create());
			in.makeAgent(a);
                        if(a.userNameTaken(in.getLogin()))
                            throw new ArahantWarning("That Username is Already Taken");
                        else
                        {
                            a.insert();
                            a.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());
                        }

			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}

	@WebMethod()
	public SaveAgencyAgentReturn saveAgencyAgent(/*@WebParam(name = "in")*/final SaveAgencyAgentInput in) {
		final SaveAgencyAgentReturn ret=new SaveAgencyAgentReturn();
		try {
			checkLogin(in);
		
			final BAgent a=new BAgent(in.getPersonId());
			in.makeAgent(a);
			a.update();
			a.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());
			finishService(ret);
		} catch (final Throwable e) {
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
			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(),in.getExtId(),in.getSearchTopLevelOnly()?2:0,null,2,ret.getCap()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in)	{
		final SearchSecurityGroupsReturn ret=new SearchSecurityGroupsReturn();
		
		try
		{
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(),ret.getCap()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
  
	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in)		
	{
		final SearchCompaniesReturn ret=new SearchCompaniesReturn();
		try
		{
			checkLogin(in);

			if (!isEmpty(in.getAgentId()))
				ret.setItem(new BAgent(in.getAgentId()).search(in.getName(), in.getAssociatedIndicator(), ret.getCap()));
			else
				ret.setItem(new BAgency(in.getAgencyId()).searchCompaniesForAgents(in.getName(), in.getAssociatedIndicator(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public AddCompanyToAgentReturn addCompanyToAgent(/*@WebParam(name = "in")*/final AddCompanyToAgentInput in)
	{
		final AddCompanyToAgentReturn ret=new AddCompanyToAgentReturn();
		try
		{
			checkLogin(in);
			
			new BAgent(in.getAgentId()).assignToThisGroup(in.getCompanyId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetMetaReturn getMeta(/*@WebParam(name = "in")*/final GetMetaInput in)		
	{
		final GetMetaReturn ret=new GetMetaReturn();
		try
		{
			checkLogin(in);
			
			ret.setMultipleCompanySupport(ArahantSession.multipleCompanySupport);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
