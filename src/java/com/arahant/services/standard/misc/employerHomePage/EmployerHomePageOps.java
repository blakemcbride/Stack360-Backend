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
package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.TerminationReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscEmployerHomePageOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployerHomePageOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployerHomePageOps.class);
	
	public EmployerHomePageOps() {
		super();
	}
	
    @WebMethod()
	public TerminateEmployeeReturn terminateEmployee(/*@WebParam(name = "in")*/final TerminateEmployeeInput in)		
	{
		final TerminateEmployeeReturn ret=new TerminateEmployeeReturn();
		try
		{
			checkLogin(in);

			BEmployee be = new BEmployee(in.getPersonId());

			in.setData(be);

			createProject(be, "Completed", "Completed", false, "Terminate Employee: ", "Terminate Employee: " + be.getNameFML(), "An Employee was Terminated in the System.");

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewDependentReturn newDependent (/*@WebParam(name = "in")*/final NewDependentInput in)	{
		final NewDependentReturn ret=new NewDependentReturn();
		try
		{
			checkLogin(in);
			
			final BHREmplDependent d=new BHREmplDependent();
			
			if (!isEmpty(in.getPersonId()))
			{
				d.create(in.getPersonId(),in.getEmployeeId(),in.getRelationshipType());
				ret.setDependentId(d.getDependentId());
				
				if (new BPerson(in.getPersonId()).getBEmployee()==null)
				{
					in.setData(d);
				}
				else
				{
					in.setDataSubset(d);
				}
			}
			else
			{	
				ret.setDependentId(d.create());
				in.setData(d);
			}
			d.insert();

			BEmployee be = new BEmployee(in.getEmployeeId());

			createProject(be, "Completed", "Completed", false, "Add Dependent", "Add Dependent: " + d.getNameFML(), "A Dependent was Added in the System.");
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public TerminateDependentReturn terminateDependent(/*@WebParam(name = "in")*/final TerminateDependentInput in)		
	{
		final TerminateDependentReturn ret=new TerminateDependentReturn();
		try
		{
			checkLogin(in);

			BHREmplDependent hrd = new BHREmplDependent(in.getDependentId());

			in.setData(hrd);
			
			createProject(new BEmployee(hrd.getEmployee()), "Completed", "Completed", false, "Cancel Dependent Benefit", "Cancel Dependent Benefit: " + hrd.getNameFML(), "A Benefit was Canceled for the Dependent in the System.");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListDependentsReturn listDependents (/*@WebParam(name = "in")*/final ListDependentsInput in)	{
		final ListDependentsReturn ret=new ListDependentsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(new BEmployee(in.getEmployeeId()).listDependents());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitChangeReasonsReturn listBenefitChangeReasons(/*@WebParam(name = "in")*/final ListBenefitChangeReasonsInput in)		
	{
		final ListBenefitChangeReasonsReturn ret=new ListBenefitChangeReasonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitChangeReason.listActives(in.getPersonConfigId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)	{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHREmployeeStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CreateMessageReturn createMessage(/*@WebParam(name = "in")*/final CreateMessageInput in)	{
		final CreateMessageReturn ret=new CreateMessageReturn();

		try {
			checkLogin(in);


			ret.setId(BMessage.send(hsu.getCurrentPerson(), hsu.get(Person.class,in.getToPersonId()), in.getSubject(), in.getMessage()));
	/*		final BMessage bm=new BMessage();
			ret.setId(bm.create());
			in.makeMessage(bm);
			final BPerson bp=BPerson.getCurrent();
			bm.setFromPerson(bp);
			bm.insert();
			BMessage.createToRecord(bm.getMessageId(), getToPersonId());
	*/
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}

	@WebMethod()
	public DeleteMessageReturn deleteMessage(/*@WebParam(name = "in")*/final DeleteMessageInput in)	{
		final DeleteMessageReturn ret=new DeleteMessageReturn();
		try {
			checkLogin(in);
			
			BMessage.delete(hsu, in.getMessageIds(), BPerson.getCurrent());
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}

	@WebMethod()
	public LoadMessageReturn loadMessage(/*@WebParam(name = "in")*/final LoadMessageInput in)	{
		LoadMessageReturn ret=new LoadMessageReturn();
		
		try {
			checkLogin(in);
			
			ret=new LoadMessageReturn(new BMessage(in.getMessageId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public LoadPreferencesReturn loadPreferences(/*@WebParam(name = "in")*/final LoadPreferencesInput in)		
	{
		final LoadPreferencesReturn ret=new LoadPreferencesReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BPerson(hsu.getCurrentPerson()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePreferencesReturn savePreferences(/*@WebParam(name = "in")*/final SavePreferencesInput in)		
	{
		final SavePreferencesReturn ret=new SavePreferencesReturn();
		try
		{
			checkLogin(in);
			
			final BPerson x=new BPerson(hsu.getCurrentPerson());
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
	public SearchLoginsReturn searchLogins(/*@WebParam(name = "in")*/final SearchLoginsInput in) {		
		final SearchLoginsReturn ret=new SearchLoginsReturn();
		try	{
			checkLogin(in);

			ret.setLogins(BPerson.searchLogins2(hsu, in.getUserLogin(), in.getLastName(), in.getFirstName(), in.getContextCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public SearchMessagesReturn searchMessages(/*@WebParam(name = "in")*/final SearchMessagesInput in)	{
		final SearchMessagesReturn ret=new SearchMessagesReturn();

		try
		{
			checkLogin(in);
			
			ret.setMessages(BMessage.search(hsu, BPerson.getCurrent(), in, ret.getCap(), true));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewEmployeeReturn newEmployee(/*@WebParam(name = "in")*/final NewEmployeeInput in) {
		final NewEmployeeReturn ret= new NewEmployeeReturn();

		try {
			checkLogin(in);
			final BEmployee be;

			be=new BEmployee(hsu);
			be.create();
			in.makeEmployee(be);
			be.insert();
			be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());

			ret.setPersonId(be.getPersonId());

			be.setWageAndPosition(in.getPositionId(),in.getWageTypeId(),in.getWageAmount(),in.getEmployeeStatusDate());
			if(!isEmpty(in.getOrgGroupId()))
				be.assignToSingleOrgGroup(in.getOrgGroupId(), false);
			hsu.flush();
			//ArahantSession.getAI().watchAll();

			ArahantSession.AIEval("(assert (InsertedNewEmployee \""+be.getPersonId()+"\" ))");

			createProject(be, "Completed", "Completed", false, "Add New Employee", "Add New Employee: " + be.getNameFML(), "A New Employee was Added to the System.");

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public ListEEOCategoriesReturn listEEOCategories(/*@WebParam(name = "in")*/final ListEEOCategoriesInput in)	{
		final ListEEOCategoriesReturn ret=new ListEEOCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREEOCategory.list(hsu));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEEORacesReturn listEEORaces(/*@WebParam(name = "in")*/final ListEEORacesInput in)	{
		final ListEEORacesReturn ret=new ListEEORacesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREEORace.list(hsu));

			finishService(ret);
		}
		catch (final Throwable e) {
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
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(),ret.getHighCap()));

			if(!isEmpty(in.getSecurityGroupId()))
				ret.setSelectedItem(new SearchSecurityGroupsItem(new BSecurityGroup(in.getSecurityGroupId())));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in)
	{
		final ListPositionsReturn ret=new ListPositionsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRPosition.list(hsu));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadMetaReturn loadMeta(/*@WebParam(name = "in")*/final LoadMetaInput in)
	{
		final LoadMetaReturn ret=new LoadMetaReturn();
		try
		{
			checkLogin(in);

			ret.setNewPersonDefaultExternalId(BProperty.getInt("DefaultEmployeeIDType"));
			ret.setNewEmpOpenScreenGroupId(BProperty.get("NewEmpOpenScreenGroupId"));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in)	{
		final SearchCompanyReturn ret=new SearchCompanyReturn();

		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(),false,ret.getHighCap()));

			if (!isEmpty(in.getCompanyId()))
				ret.setSelectedItem(new SearchCompanyReturnItem(BCompanyBase.get(in.getCompanyId())));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)	{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in)	{
		final SearchProjectStatusesReturn ret=new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(),ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)	{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in)	{
		final SearchProjectsReturn ret=new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(),in.getStatus(), in.getType(),in.getCompanyId(),in.getProjectName(),
				0,0,null,false,in.getUser(),null,null,ret.getHighCap(), true));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public GetInheritedDefaultProjectReturn getInheritedDefaultProject(/*@WebParam(name = "in")*/final GetInheritedDefaultProjectInput in)
	{
		final GetInheritedDefaultProjectReturn ret=new GetInheritedDefaultProjectReturn();
		try
		{
			checkLogin(in);

			BOrgGroup company=new BOrgGroup(hsu.getCurrentCompany());

			if (!isEmpty(company.getDefaultProjectId()))
				ret.setData(new BProject(company.getDefaultProjectId()));

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

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getHighCap()));
			if (!isEmpty(in.getScreenGroupId()))
				ret.setSelectedItem(new SearchScreenGroupsReturnItem(new BScreenGroup(in.getScreenGroupId())));

			finishService(ret);
		} catch (final Exception e) {
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

			ret.setItem(BWageType.listActiveNonDedutionsPlus(null));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in)
	{
		final ListBenefitClassesReturn ret=new ListBenefitClassesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBenefitClass.listActive());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GenerateLoginAndPasswordReturn generateLoginAndPassword(/*@WebParam(name = "in")*/final GenerateLoginAndPasswordInput in)
	{
		final GenerateLoginAndPasswordReturn ret=new GenerateLoginAndPasswordReturn();
		try
		{
			checkLogin(in);

			ret.setLogin(BEmployee.makeUserLogin(in.getFirstName(), in.getLastName()));
			ret.setPassword(BEmployee.makeUserPassword());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();

		try
		{
			checkLogin(in);
			boolean includeUser=true;

			if (hsu.currentlySuperUser())
				includeUser=false;

			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser));

			if (in.getAutoDefault() && includeUser && BPerson.getCurrent().isEmployee())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

    @WebMethod()
	public SaveEmployeeReturn saveEmployee(/*@WebParam(name = "in")*/final SaveEmployeeInput in)		
	{
		final SaveEmployeeReturn ret=new SaveEmployeeReturn();
		try {
			checkLogin(in);
			final BEmployee be = new BEmployee(in.getEmployeeId());

			if (profileChanged(new BPerson(in.getEmployeeId()), in))
            {
				String message = emailChanges(new BPerson(in.getEmployeeId()), in); //get the changes
				createProject(be, "Completed", "Completed", false, "Modify Employee: ", "Modify Employee: " + be.getNameFML(), message);
			}
			else
			{
				createProject(be, "Completed", "Completed", false, "Modify Employee: ", "Modify Employee: " + be.getNameFML(), "An Employee was Modified in the System");
			}

			in.makeEmployee(be);
			be.update();

			be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());
			ret.setPersonId(be.getPersonId());

			be.setWageAndPosition(in.getPositionId(),in.getWageTypeId(),in.getWageAmount(),in.getEmployeeStatusDate());
			if(!isEmpty(in.getOrgGroupId()))
				be.assignToSingleOrgGroup(in.getOrgGroupId(), false);
			hsu.flush();
			//ArahantSession.getAI().watchAll();

			ArahantSession.AIEval("(assert (InsertedNewEmployee \""+be.getPersonId()+"\" ))");

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;

		/*
		try
		{
			checkLogin(in);
			
			final BEmployee x=new BEmployee(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
		*/
	}

	private String emailChanges(BPerson per, SaveEmployeeInput in) {
		String message = "";
		final String endline = "\r\n";
		message += "Field Name: Old Value --> New Value" + endline;
		message += "-----------------------------------" + endline;
		message += "    Company: " + per.getCompanyName() + endline;
		message += " First Name: " + per.getFirstName() + " --> " + in.getFname() + endline;
		message += "  Last Name: " + per.getLastName() + " --> " + in.getLname() + endline;
		message += "Middle Name: " + per.getMiddleName() + " --> " + in.getMiddleName() + endline;
		message += "  Nick Name: " + per.getNickName() + " --> " + in.getNickName() + endline;
		message += "        SSN: " + per.getSsn() + " --> " + in.getSsn() + endline;
		message += "      Email: " + per.getPersonalEmail() + " --> " + in.getPersonalEmail() + endline;
		message += "        DOB: " + per.getDob() + " --> " + in.getDob() + endline;
		message += "     Gender: " + per.getSex() + " --> " + in.getSex() + endline;
		message += "    Street1: " + per.getStreet() + " --> " + in.getAddressLine1() + endline;
		message += "    Street2: " + per.getStreet2() + " --> " + in.getAddressLine2() + endline;
		message += "       City: " + per.getCity() + " --> " + in.getCity() + endline;
		message += "      State: " + per.getState() + " --> " + in.getStateProvince() + endline;
		message += "        Zip: " + per.getZip() + " --> " + in.getZipPostalCode() + endline;
		message += "     County: " + per.getCounty() + " --> " + in.getCounty() + endline;

		message += "  Home Phone: " + per.getHomePhone() + " --> " + in.getHomePhone() + endline;
		message += "  Work Phone: " + per.getWorkPhone() + " --> " + in.getWorkPhone() + endline;
		message += "Mobile Phone: " + per.getMobilePhone() + " --> " + in.getMobilePhone() + endline;
		message += "         Fax: " + per.getWorkFax() + " --> " + in.getWorkFax() + endline;
		return message;

	}
	
    @WebMethod()
	public LoadEmployeeReturn loadEmployee(/*@WebParam(name = "in")*/final LoadEmployeeInput in)		
	{
		final LoadEmployeeReturn ret=new LoadEmployeeReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadDependentReturn loadDependent(/*@WebParam(name = "in")*/final LoadDependentInput in)			{
		final LoadDependentReturn ret=new LoadDependentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHREmplDependent(in.getDependentId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveDependentReturn saveDependent (/*@WebParam(name = "in")*/final SaveDependentInput in)	{
		final SaveDependentReturn ret=new SaveDependentReturn();
		try
		{
			checkLogin(in);
			
			final BHREmplDependent d=new BHREmplDependent(in.getDependentId());
			
			if (!isEmpty(in.getPersonId()))
			{
				d.moveTo(in.getPersonId());
				
			}
			else
			{
				if (new BPerson(d.getPerson()).getBEmployee()!=null)
				{
					in.setDataSubset(d);
					d.update();
				}
				else
				{
					in.setData(d);
					d.update();
				}
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)			{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).searchEmployeesForDependent(in.getFirstName(),in.getLastName(),in.getSsn(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSpouseSexReturn loadSpouseSex(/*@WebParam(name = "in")*/final LoadSpouseSexInput in)			{
		final LoadSpouseSexReturn ret=new LoadSpouseSexReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListInactiveStatusesReturn listInactiveStatuses(/*@WebParam(name = "in")*/final ListInactiveStatusesInput in)		
	{
		final ListInactiveStatusesReturn ret=new ListInactiveStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.listInactiveStatuses());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public LoadEmployeeStatusReturn loadEmployeeStatus(/*@WebParam(name = "in")*/final LoadEmployeeStatusInput in)		
	{
		final LoadEmployeeStatusReturn ret=new LoadEmployeeStatusReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListEmployeeDependentsReturn listEmployeeDependents(/*@WebParam(name = "in")*/final ListEmployeeDependentsInput in)		
	{
		final ListEmployeeDependentsReturn ret=new ListEmployeeDependentsReturn();
		try
		{
			checkLogin(in);

			BEmployee be = new BEmployee(in.getEmployeeId());
			ret.setDependents(be.getDependents());
			ret.setEmployeeName(be.getNameFML());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListEmployeeBenefitsReturn listEmployeeBenefits(/*@WebParam(name = "in")*/final ListEmployeeBenefitsInput in)		
	{
		final ListEmployeeBenefitsReturn ret=new ListEmployeeBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).getApprovedBenefitJoins());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public CancelBenefitsReturn cancelBenefits(/*@WebParam(name = "in")*/final CancelBenefitsInput in)
	{
		final CancelBenefitsReturn ret=new CancelBenefitsReturn();
		try
		{
			checkLogin(in);

			boolean projectCreated = false;
			for(int i = 0; i < in.getIds().length; i++)
			{
				BHRBenefitJoin hrbj = new BHRBenefitJoin(in.getIds()[i]);

				in.setData(hrbj, in.getEffectiveDates()[i]);
				if(!projectCreated)
				{
					createProject(hrbj.getPayingPerson().getBEmployee(), "Completed", "Completed", false, "Cancel Employee Benefit(s)", "Cancel Employee Benefit(s): " + hrbj.getPayingPerson().getBEmployee().getNameFML(), "One or more benefits were Canceled for the Employee in the System.");
					projectCreated = true;
				}
			}

			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public InviteEmployeeToEnrollReturn inviteEmployeeToEnroll(/*@WebParam(name = "in")*/final InviteEmployeeToEnrollInput in)		
	{
		final InviteEmployeeToEnrollReturn ret=new InviteEmployeeToEnrollReturn();
		try
		{
			checkLogin(in);

			createProject(new BEmployee(in.getEmployeeId()), "Initial", "Requested", true, "Benefit Enrollment", "Benefit Enrollment", "Please take the time to go to the Benefit Wizard and Enroll in any Benefits you may want.");

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListOpenProjectsForCompanyReturn listOpenProjectsForCompany(/*@WebParam(name = "in")*/final ListOpenProjectsForCompanyInput in)		
	{
		final ListOpenProjectsForCompanyReturn ret=new ListOpenProjectsForCompanyReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BCompany(ArahantSession.getHSU().getCurrentCompany()).listActiveProjects(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListClosedProjectsForCompanyReturn listClosedProjectsForCompany(/*@WebParam(name = "in")*/final ListClosedProjectsForCompanyInput in)
	{
		final ListClosedProjectsForCompanyReturn ret=new ListClosedProjectsForCompanyReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BCompany(ArahantSession.getHSU().getCurrentCompany()).listInactiveProjects(ret.getCap()));

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

			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			//if this is a vendor, return 1, otherwise return 2
			if (hsu.getCurrentPerson().getOrgGroupType()==VENDOR_TYPE)
				ret.setTypeAccessLevel(1);
			else
				ret.setTypeAccessLevel(2);

			ret.setDetailAccessLevel(1); // WMCO

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetAssignedEmployeeReturn getAssignedEmployee(/*@WebParam(name = "in")*/final GetAssignedEmployeeInput in)
	{
		final GetAssignedEmployeeReturn ret=new GetAssignedEmployeeReturn();
		try
		{
			checkLogin(in);

			ret.setEmployeeId(new BProjectType(in.getProjectTypeId()).getRoutedEmployee(in.getPersonId()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeesForAssignmentReturn listEmployeesForAssignment(/*@WebParam(name = "in")*/final ListEmployeesForAssignmentInput in)			{
		final ListEmployeesForAssignmentReturn ret=new ListEmployeesForAssignmentReturn();
		try
		{
			checkLogin(in);

			BPerson []emps=BEmployee.listEmployeesInPersonsGroup(hsu.getCurrentPerson(), ret.getCap());

			if (emps.length==0)
			{
				if (!isEmpty(in.getProjectId()))
				{
					BProject proj=new BProject(in.getProjectId());

					List<Person> bap=proj.getAssignedPersons2(null);
					final int sz = bap.size();
					emps=new BPerson[sz];

					for (int loop=0;loop<sz;loop++)
						emps[loop]=new BPerson(bap.get(loop).getPersonId());

				}
			}

			ret.setItem(emps);

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*@WebParam(name = "in")*/final ListProjectTypesInput in)	{
		final ListProjectTypesReturn ret=new ListProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.list(hsu));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in)			{
		final ListStatusesReturn ret=new ListStatusesReturn();
		try
		{
			checkLogin(in);

			BProject proj=new BProject(in.getProjectId()); //WMCO WORKFLOW

			if (!isEmpty(proj.getRouteStopId()))
			{
				BRouteStop brs=new BRouteStop(proj.getRouteStopId());
				ret.setItem(brs.listProjectStatuses(ret.getCap()));
			}
			else
				ret.setItem(BProjectStatus.list(hsu));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSummaryReturn loadSummary(/*@WebParam(name = "in")*/final LoadSummaryInput in)			{
		final LoadSummaryReturn ret=new LoadSummaryReturn();
		try
		{
			checkLogin(in);

			ret.setData(new BProject(in.getProjectId()));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProjectReturn saveProject(/*@WebParam(name = "in")*/final SaveProjectInput in)			{
		final SaveProjectReturn ret=new SaveProjectReturn();
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

	private void createProject(final BEmployee be, final String projectStatusCode, final String projectStatusDesc, final boolean active, final String projectName, final String projectDesc, final String projectDetailDesc)
	{
			ProjectStatus ps=hsu.createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, projectStatusCode).first();

			String psid;
			if (ps==null)
			{
				BProjectStatus bps = new BProjectStatus();
				psid=bps.create();
				bps.setCode(projectStatusCode);
				if (active)
					bps.setActive('Y');
				else
					bps.setActive('N');
				bps.setDescription(projectStatusDesc);
				bps.insert();
			}
			else
				psid=ps.getProjectStatusId();

			ProjectType pt=hsu.createCriteria(ProjectType.class).eq(ProjectType.CODE, "Employee").first();

			String ptid;
			if (pt==null)
			{
				BProjectType bpt = new BProjectType();
				ptid=bpt.create();
				bpt.setScope(ProjectType.SCOPE_GLOBAL);
				bpt.setCode("Employee");
				bpt.setDescription("Employee");
				bpt.insert();
			}
			else
				ptid=pt.getProjectTypeId();

			ProjectCategory pc = hsu.createCriteria(ProjectCategory.class).eq(ProjectCategory.CODE, "Employee").first();

			String pcid;
			if (pc==null)
			{
				BProjectCategory bpc = new BProjectCategory();
				pcid=bpc.create();
				bpc.setCode("Employee");
				bpc.setDescription("Employee");
				bpc.setScope(ProjectCategory.SCOPE_GLOBAL);
				bpc.insert();
			}
			else
				pcid=pc.getProjectCategoryId();

			BProject bp = new BProject();
			bp.create();
			if (!active)
			{
				bp.setDateCompleted(DateUtils.now());
				bp.setTimeCompleted(DateUtils.nowTime());
			}
			bp.setBillable('N');
			bp.setProjectName(projectName);
			bp.setDescription(projectDesc);
			bp.setDetailDesc(projectDetailDesc);
			bp.setProjectTypeId(ptid);
			bp.setProjectCategoryId(pcid);
			bp.setRequestingOrgGroupId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
			String routeStopId = BRouteStop.findOrMakeByName("Misc");
			bp.setRouteStopId(routeStopId);
			bp.setRouteId(new BRouteStop(routeStopId).getRouteId());
			bp.setProjectStatusId(psid);
			bp.setCurrentPersonId(be.getPersonId());
			bp.setDoneForPersonId(be.getPersonId());
			bp.insert();
	}

	private boolean profileChanged(BPerson per, SaveEmployeeInput in)
    {
        try
		{
			if (!per.getFirstName().equals(in.getFname()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getFirstName()) && !isEmpty(in.getFname()))
				return true;
		}
		try
		{
			if (!per.getLastName().equals(in.getLname()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getLastName()) && !isEmpty(in.getLname()))
				return true;
		}
        try
		{
			if (!per.getMiddleName().equals(in.getMiddleName()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getMiddleName()) && !isEmpty(in.getMiddleName()))
				return true;
		}
		try
		{
			if (!per.getNickName().equals(in.getNickName()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getNickName()) && !isEmpty(in.getNickName()))
				return true;
		}
        try
		{
			if (!per.getSsn().equals(in.getSsn()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getSsn()) && !isEmpty(in.getSsn()))
				return true;
		}
        try
		{
			if (!per.getPersonalEmail().equals(in.getPersonalEmail()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getPersonalEmail()) && !isEmpty(in.getPersonalEmail()))
				return true;
		}

        if (per.getDob() != in.getDob())
            return true;
        try
		{
			if (!per.getSex().equals(in.getSex()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getSex()) && !isEmpty(in.getSex()))
				return true;
		}
        try
		{
			if (!per.getStreet().equals(in.getAddressLine1()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getStreet()) && !isEmpty(in.getAddressLine1()))
				return true;
		}
        try
		{
			if (!per.getStreet2().equals(in.getAddressLine2()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getStreet2()) && !isEmpty(in.getAddressLine2()))
				return true;
		}
        try
		{
			if (!per.getCity().equals(in.getCity()))
				return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getCity()) && !isEmpty(in.getCity()))
				return true;
		}
        try
		{
        if (!per.getState().equals(in.getStateProvince()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getState()) && !isEmpty(in.getStateProvince()))
				return true;
		}
        try
		{
        if (!per.getZip().equals(in.getZipPostalCode()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getZip()) && !isEmpty(in.getZipPostalCode()))
				return true;
		}
        try
		{
        if (!per.getCounty().equals(in.getCounty()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getCounty()) && !isEmpty(in.getCounty()))
				return true;
		}
        try
		{
        if (!per.getHomePhone().equals(in.getHomePhone()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getHomePhone()) && !isEmpty(in.getHomePhone()))
				return true;
		}
        try
		{
        if (!per.getWorkPhone().equals(in.getWorkPhone()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getWorkPhone()) && !isEmpty(in.getWorkPhone()))
				return true;
		}
        try
		{
        if (!per.getMobilePhone().equals(in.getMobilePhone()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getMobilePhone()) && !isEmpty(in.getMobilePhone()))
				return true;
		}
        try
		{
        if (!per.getWorkFax().equals(in.getWorkFax()))
            return true;
		}
		catch(Exception e)
		{
			if(isEmpty(per.getWorkFax()) && !isEmpty(in.getWorkFax()))
				return true;
		}

        return false;
    }

    @WebMethod()
	public ListTerminationChangeReasonsReturn listTerminationChangeReasons(/*@WebParam(name = "in")*/final ListTerminationChangeReasonsInput in) {

		final ListTerminationChangeReasonsReturn ret = new ListTerminationChangeReasonsReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitChangeReason.listCobraTermChangeReasons(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	


	@WebMethod()
	public GetTerminationReportReturn getTerminationReport(/*@WebParam(name = "in")*/final GetTerminationReportInput in)	{
		final GetTerminationReportReturn ret=new GetTerminationReportReturn();
		try
		{
			checkLogin(in);

			ret.setFileName(new TerminationReport().buildEmployee(in.getEmployeeId()));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)			{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), 0, COMPANY_TYPE, ret.getHighCap()));

			if(!isEmpty(in.getPersonId()))
			{
				BPerson bp = new BPerson(in.getPersonId());
				Iterator<OrgGroupAssociation> i = bp.getOrgGroupAssociations().iterator();
				if(i.hasNext())
				{
					ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(i.next().getOrgGroup())));
				}
			}
			else if(ret.getItem().length == 1)
				ret.setSelectedItem(ret.getItem()[0]);


			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
