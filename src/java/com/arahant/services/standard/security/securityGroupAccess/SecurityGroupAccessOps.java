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
package com.arahant.services.standard.security.securityGroupAccess;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSecuritySecurityGroupAccessOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class SecurityGroupAccessOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			SecurityGroupAccessOps.class);
	
	public SecurityGroupAccessOps() {
		super();
	}
	
    @WebMethod()
	public ListAssociatedScreenGroupsReturn listAssociatedScreenGroups(/*@WebParam(name = "in")*/final ListAssociatedScreenGroupsInput in)		
	{
		final ListAssociatedScreenGroupsReturn ret=new ListAssociatedScreenGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setScreenGroups(new BSecurityGroup(in.getSecurityGroupId()).listAssociatedScreenGroups());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public ListSecurityGroupsReturn listSecurityGroups(/*@WebParam(name = "in")*/final ListSecurityGroupsInput in)		
	{
		final ListSecurityGroupsReturn ret=new ListSecurityGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setSecurityGroups(BSecurityGroup.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListAssociatedSecurityGroupsReturn listAssociatedSecurityGroups(/*@WebParam(name = "in")*/final ListAssociatedSecurityGroupsInput in)		
	{
		final ListAssociatedSecurityGroupsReturn ret=new ListAssociatedSecurityGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setSecurityGroups(new BSecurityGroup(in.getSecurityGroupId()).listAssociatedSecurityGroups());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public AssociateScreenGroupsReturn associateScreenGroups(/*@WebParam(name = "in")*/final AssociateScreenGroupsInput in)		
	{
		final AssociateScreenGroupsReturn ret=new AssociateScreenGroupsReturn();
		try
		{
			checkLogin(in);
			
			new BSecurityGroup(in.getSecurityGroupId()).associateToScreenGroups(in.getScreenGroupIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public AssociateSecurityGroupsReturn associateSecurityGroups(/*@WebParam(name = "in")*/final AssociateSecurityGroupsInput in)		
	{
		final AssociateSecurityGroupsReturn ret=new AssociateSecurityGroupsReturn();
		try
		{
			checkLogin(in);
			
			new BSecurityGroup(in.getSecurityGroupId()).associateToSecurityGroups(in.getSecurityGroupIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListAvailableSecurityGroupsReturn listAvailableSecurityGroups(/*@WebParam(name = "in")*/final ListAvailableSecurityGroupsInput in)		
	{
		final ListAvailableSecurityGroupsReturn ret=new ListAvailableSecurityGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setSecurityGroups(new BSecurityGroup().listAvailableSecurityGroups(in.getExcludeIds()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListAvailableScreenGroupsReturn listAvailableScreenGroups(/*@WebParam(name = "in")*/final ListAvailableScreenGroupsInput in)		
	{
		final ListAvailableScreenGroupsReturn ret=new ListAvailableScreenGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setScreenGroups(new BScreenGroup().listAvailableScreenGroups(in.getExcludeIds()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);
			
			ret.setScreenGroups(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", ret.getHighCap(), in.getExcludeIds()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();

		try {

			checkLogin(in);

			ret.setSecurityGroups(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getHighCap(), in.getExcludeIds()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}
