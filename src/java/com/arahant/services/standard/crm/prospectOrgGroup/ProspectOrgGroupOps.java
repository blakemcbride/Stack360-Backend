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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.crm.prospectOrgGroup;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectOrgGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectOrgGroupOps extends ServiceBase {
	
	static ArahantLogger logger = new ArahantLogger(ProspectOrgGroupOps.class);
	

	@WebMethod()
	public DeleteProspectContactReturn deleteProspectContact(/*@WebParam(name = "in")*/final DeleteProspectContactInput in) {
		final DeleteProspectContactReturn ret=new DeleteProspectContactReturn();
	
		try {
			checkLogin(in);
			BProspectContact.delete(in.getIds());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListProspectContactsReturn listProspectContacts(/*@WebParam(name = "in")*/final ListProspectContactsInput in)	{
		final ListProspectContactsReturn ret=new ListProspectContactsReturn();
				
		try
		{
			checkLogin(in);
			
			ret.setPersons(BProspectContact.listContacts(in.getGroupId(),in.getLastNameStartsWith(),in.getPrimary(),ret.getCap()),in.getGroupId());
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	
	@WebMethod()
	public LoadProspectContactReturn loadProspectContact(/*@WebParam(name = "in")*/final LoadProspectContactInput in) {
		LoadProspectContactReturn ret = new LoadProspectContactReturn();

		try {
			checkLogin(in);

			ret=new LoadProspectContactReturn(new BProspectContact(in.getPersonId()),in.getGroupId());
			
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	@WebMethod()
	public NewProspectContactReturn newProspectContact(/*@WebParam(name = "in")*/final NewProspectContactInput in) {
		final NewProspectContactReturn ret= new NewProspectContactReturn();

		try {
			checkLogin(in);
			
			final BProspectContact bcc=new BProspectContact();
			bcc.create();
			in.makeProspectContact(bcc);
			bcc.insert();
			bcc.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());
			
			ret.setPersonId(bcc.getPersonId());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveProspectContactReturn saveProspectContact(/*@WebParam(name = "in")*/final SaveProspectContactInput in) {		
		final SaveProspectContactReturn ret=new SaveProspectContactReturn();
		try {
			checkLogin(in);
			
			final BProspectContact bcc=new BProspectContact(in.getPersonId());
			in.makeProspectContact(bcc);
			bcc.update();
			bcc.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	@WebMethod()
	public SearchProspectContactsReturn searchProspectContacts(/*@WebParam(name = "in")*/final SearchProspectContactsInput in)	{
		final SearchProspectContactsReturn ret=new SearchProspectContactsReturn();
		
		try
		{
			checkLogin(in);
			BProspectContact[] r=BProspectContact.searchProspectContacts(in.getFirstName(), in.getLastName(), in.getOrgGroupId(), in.getAssociatedIndicator(), ret.getCap());
			ret.setContacts(r);
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	
	@WebMethod()
	public AddGroupToGroupReturn addGroupToGroup(/*@WebParam(name = "in")*/final AddGroupToGroupInput in) {		
		final AddGroupToGroupReturn ret=new AddGroupToGroupReturn();
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
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in)	{
		final ListAssociatedOrgGroupsReturn ret=new ListAssociatedOrgGroupsReturn();
		
		try
		{
			checkLogin(in);
			
			if (isEmpty(in.getGroupId()))
				ret.setOrgGroups(BProspectCompany.search("", "", 0));
			else
				ret.setOrgGroups(BOrgGroup.listAssociatedGroups(hsu, in.getGroupId(), PROSPECT_TYPE));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
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
			bog.setOrgGroupType(PROSPECT_TYPE);
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

	/*
	 * 1. search org groups generically (used so I can find an org group in the system and navigate it, edit the tree)
 
	- name filter
	- dissassociated means all org groups in any company or no company that do not have a parent
	- associated means all org groups in in any company or no company that do have a parent
	 */
	@WebMethod()
	public SearchOrgGroupsGenericReturn searchOrgGroupsGeneric(/*@WebParam(name = "in")*/final SearchOrgGroupsGenericInput in)	{

		final SearchOrgGroupsGenericReturn ret=new SearchOrgGroupsGenericReturn();
		
		try {
			checkLogin(in);
			
			ret.setOrgGroups(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), in.getAssociatedIndicator(),PROSPECT_TYPE,ret.getCap()));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}

	
	/*2. search org groups for purpose of associating
	 
	- pass the current org group the user is in or "" for no current org group (top)
	 
	assuming the org group passed in was a company org group (is or has a parent that is company)
	 
	- name filter
	- dissassociated means all org groups in the company that do not have a parent
	- associated means all org groups in the company that do have a parent
	- should never see in the results the current org group the user is in
	- should never see in the results the parents of the org group the user is in
	- should never see in the results the org groups that are or have as a child the current org group
	 
	assuming the org group passed in was not a company org group
	 
	- name filter
	- dissassociated means all org groups in any company or no company that do not have a parent
	- associated means all org groups in in any company or no company that do have a parent
	- should never see in the results the current org group the user is in
	- should never see in the results the parents of the org group the user is in
	- should never see in the results the org groups that are or have as a child the current org group
	 */
	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		
		try {
			checkLogin(in);
			ret.setOrgGroups(BOrgGroup.searchOrgGroups(hsu, in.getName(),in.getOrgGroupId(), in.getAssociatedIndicator(), PROSPECT_TYPE, ret.getCap()));
			
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
	
}
