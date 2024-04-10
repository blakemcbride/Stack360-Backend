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
package com.arahant.services.standard.security.securityGroup;

import com.arahant.business.BSecurityGroup;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardSecuritySecurityGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class SecurityGroupOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(SecurityGroupOps.class);

	@WebMethod()
	public ListSecurityGroupsReturn listSecurityGroups(/*@WebParam(name = "in")*/final ListSecurityGroupsInput in) {
		final ListSecurityGroupsReturn ret = new ListSecurityGroupsReturn();
		try {
			checkLogin(in);

			ret.setItem(BSecurityGroup.list());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListMembersForSecurityGroupReturn listMembersForSecurityGroup(/*@WebParam(name = "in")*/final ListMembersForSecurityGroupInput in) {
		final ListMembersForSecurityGroupReturn ret = new ListMembersForSecurityGroupReturn();
		try {
			checkLogin(in);

			ret.setItem(new BSecurityGroup(in.getGroupId()).listMembers(ret.getCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListRightsForSecurityGroupReturn listRightsForSecurityGroup(/*@WebParam(name = "in")*/final ListRightsForSecurityGroupInput in) {
		final ListRightsForSecurityGroupReturn ret = new ListRightsForSecurityGroupReturn();
		try {
			checkLogin(in);

			ret.setItem(new BSecurityGroup(in.getGroupId()).listRights());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEffectiveRightsForSecurityGroupReturn listEffectiveRightsForSecurityGroup(/*@WebParam(name = "in")*/final ListEffectiveRightsForSecurityGroupInput in) {
		final ListEffectiveRightsForSecurityGroupReturn ret = new ListEffectiveRightsForSecurityGroupReturn();
		try {
			checkLogin(in);

			ret.setItem(new BSecurityGroup(in.getGroupId()).getEffectiveRightList());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewSecurityGroupReturn newSecurityGroup(/*@WebParam(name = "in")*/final NewSecurityGroupInput in) {
		final NewSecurityGroupReturn ret = new NewSecurityGroupReturn();
		try {
			checkLogin(in);

			final BSecurityGroup b = new BSecurityGroup();
			ret.setId(b.create());
			in.setData(b);
			b.insert();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		ArahantSession.storeSecurityRules();
		return ret;
	}

	@WebMethod()
	public SaveSecurityGroupReturn saveSecurityGroup(/*@WebParam(name = "in")*/final SaveSecurityGroupInput in) {
		final SaveSecurityGroupReturn ret = new SaveSecurityGroupReturn();
		try {
			checkLogin(in);

			final BSecurityGroup b = new BSecurityGroup(in.getGroupId());
			in.setData(b);
			b.update();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		ArahantSession.storeSecurityRules();
		return ret;
	}

	@WebMethod()
	public DeleteSecurityGroupReturn deleteSecurityGroup(/*@WebParam(name = "in")*/final DeleteSecurityGroupInput in) {
		final DeleteSecurityGroupReturn ret = new DeleteSecurityGroupReturn();
		try {
			checkLogin(in);

			BSecurityGroup.delete(in.getGroupIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		ArahantSession.storeSecurityRules();
		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();
		try {
			checkLogin(in);

			ret.setItem(BSecurityGroup.search(in.getName(), in.getParentGroupId(), in.getTypeIndicator(), ret.getCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public AssignRightsToSecurityGroupsReturn assignRightsToSecurityGroups(/*@WebParam(name = "in")*/final AssignRightsToSecurityGroupsInput in) {
		final AssignRightsToSecurityGroupsReturn ret = new AssignRightsToSecurityGroupsReturn();
		try {
			checkLogin(in);

			final BSecurityGroup bsg = new BSecurityGroup(in.getParentGroupId());
			bsg.assignRights(in.getTokenIds(), in.getTokenAccessLevel());
			bsg.assignGroups(in.getGroupIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		ArahantSession.storeSecurityRules();
		return ret;
	}

	@WebMethod()
	public UnassignRightsFromSecurityGroupsReturn unassignRightsFromSecurityGroups(/*@WebParam(name = "in")*/final UnassignRightsFromSecurityGroupsInput in) {
		final UnassignRightsFromSecurityGroupsReturn ret = new UnassignRightsFromSecurityGroupsReturn();
		try {
			checkLogin(in);

			final BSecurityGroup bsg = new BSecurityGroup(in.getParentGroupId());
			bsg.unassignRights(in.getTokenIds());
			bsg.unassignGroups(in.getGroupIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		ArahantSession.storeSecurityRules();
		return ret;
	}

	@WebMethod()
	public EditSecurityTokenAssignmentReturn editSecurityTokenAssignment(/*@WebParam(name = "in")*/final EditSecurityTokenAssignmentInput in) {
		final EditSecurityTokenAssignmentReturn ret = new EditSecurityTokenAssignmentReturn();
		try {
			checkLogin(in);

			final BSecurityGroup bsg = new BSecurityGroup(in.getParentGroupId());
			bsg.setAccessLevel(in.getTokenId(), in.getAccessLevel());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		ArahantSession.storeSecurityRules();
		return ret;
	}
}
/*
 * 
 * listSecurityGroups input: none output: name, description, groupId remarks:
 * get a list of all security groups
 * 
 * listMembersForSecurityGroup: input: groupId output: firstName, lastName,
 * loginName, companyName, cap remarks: returns the top cap members assigned to
 * the group
 * 
 * listRightsForSecurityGroup: input: groupId output: rightId, name,
 * description, type (Group, Token), tokenAccessLevel (None, Read, Write)
 * remarks: list a combination of the assigned groups and tokens to the
 * specified security group (show specific None token assignments here)
 * 
 * listEffectiveRightsForSecurityGroup: input: groupId output: name,
 * description, accessLevel (Read, Write) remarks: lists the effective token
 * access levels for the specified security group (tokens not assigned OR
 * assigned with None should NOT be returned)
 * 
 * newSecurityGroup: input: name (required), description (required)
 * 
 * saveSecurityGroup: input: name (required), description (required), groupId
 * (required)
 * 
 * deleteSecurityGroup: input: array of groupIds (min = 1) remarks: can't delete
 * if groups are associated, should provide a reasonable error message
 * 
 * searchSecurityGroups: input: parentGroupId (required), name (required),
 * nameSearchType (standard), typeIndicator (0 = Both, 1 = Token, 2 = Group)
 * output: rightId (either the tokenId or groupId), name, description, type
 * (Group, Token) remarks: returns a list of tokens or groups that are not
 * already DIRECTLY assigned to the parentGroupId (they may be indirectly
 * assigned through inheritance)
 * 
 * assignRightsToSecurityGroups input: groupIds array, tokenIds array,
 * tokenAccessLevel (1 = None, 2 = Read, 3 = Write .. NOTE please use these
 * numbers here, as 0 means no selection on the client side validation),
 * parentGroupId (required) remarks: assigns the groups and tokens to the
 * parent, the tokens are assigned with the specified access level (field not
 * required in validation
 * 
 * unassignRightsFromSecurityGroups input: groupIds array, tokenIds array,
 * parentGroupId (required)
 * 
 * editSecurityTokenAssignment input: tokenId (required), parentGroupId
 * (required), accessLevel (1 = None, 2 = Read, 3 = Write .. NOTE please use
 * these numbers here, as 0 means no selection on the client side validation)
 * 
 * 
 */
