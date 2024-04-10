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
package com.arahant.services.standard.hrConfig.timeRelatedBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.Project;
import com.arahant.beans.RateType;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigTimeRelatedBenefitOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimeRelatedBenefitOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			TimeRelatedBenefitOps.class);
	
	public TimeRelatedBenefitOps() {
		super();
	}
	
    @WebMethod()
	public NewTimeRelatedBenefitReturn newTimeRelatedBenefit(/*@WebParam(name = "in")*/final NewTimeRelatedBenefitInput in)		
	{
		final NewTimeRelatedBenefitReturn ret=new NewTimeRelatedBenefitReturn();
		try
		{
			checkLogin(in);

			final BHRBenefitCategory bc = new BHRBenefitCategory(BHRBenefitCategory.findOrMake("Time Related"));

			final BHRBenefit hrb=new BHRBenefit();
			ret.setId(hrb.create());
			hrb.setPaid(false);
			hrb.setTimeRelated(true);
			hrb.setName(in.getName());
			hrb.setBenefitCategoryId(bc.getCategoryId());
			hrb.setWageTypeId(BWageType.findOrMake("Paid Time Off"));
			hrb.setEligibilityType(1);
			hrb.setEligibilityPeriod(0);
			hrb.setDependentMaxAge(0);
			hrb.setDependentMaxAgeStudent(0);
			hrb.setCoverageEndType(2);
			hrb.setCoverageEndPeriod(0);
			hrb.setProcessType("H");
			hrb.insert();

			final BHRBenefitConfig hrbc = new BHRBenefitConfig();
			hrbc.create();
			hrbc.setBenefitId(hrb.getBenefitId());
			hrbc.setCoversChildren(false);
			hrbc.setName(in.getName());
			hrbc.setCoversEmployee(true);
			hrbc.insert();

			final BProjectCategory pc = new BProjectCategory(BProjectCategory.findOrMake("Time Related"));

			final BProjectType pt = new BProjectType(BProjectType.findOrMake("Time Related"));

			final BProjectStatus ps = new BProjectStatus();
			ps.create();
			ps.setActive('Y');
			ps.setCode(in.getName());
			ps.insert();

			final BProject bp = new BProject();
			bp.create();
			bp.setProjectCategoryId(pc.getProjectCategoryId());
			bp.setProjectTypeId(pt.getProjectTypeId());
			bp.setProjectStatusId(ps.getProjectStatusId());
			bp.setRequestingOrgGroupId(hsu.getCurrentCompany().getCompanyId());
			bp.setDescription(in.getName());
			Set<HrBenefitConfig> cset = new HashSet<HrBenefitConfig>(0);
			cset.add(hrbc.getBean());
			bp.setBenefitConfigs(cset);

			List<RateType> rtl = hsu.getAll(RateType.class);
			if (rtl.isEmpty())
				throw new ArahantWarning("At least one Rate Type must first be defined.");
			bp.setRateType(new BRateType(rtl.get(0)));

			bp.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveTimeRelatedBenefitReturn saveTimeRelatedBenefit(/*@WebParam(name = "in")*/final SaveTimeRelatedBenefitInput in)		
	{
		final SaveTimeRelatedBenefitReturn ret=new SaveTimeRelatedBenefitReturn();
		try
		{
			checkLogin(in);
			
			final BHRBenefit x=new BHRBenefit(in.getBenefitId());
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
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in)	{
		final ListAssociatedOrgGroupsReturn ret=new ListAssociatedOrgGroupsReturn();

		try
		{
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEmployeesForOrgGroupReturn listEmployeesForOrgGroup(/*@WebParam(name = "in")*/final ListEmployeesForOrgGroupInput in) {
            final ListEmployeesForOrgGroupReturn ret=new ListEmployeesForOrgGroupReturn();

		try
		{
			checkLogin(in);

			BEmployee[] emps = BEmployee.listEmployees(hsu, in.getGroupId(),in.getLastName(),in.getSupervisor(),ret.getCap());
			List<BEmployee> goodEmps = new ArrayList<BEmployee>();
			List<String> excludeIds = new ArrayList<String>();
			if(in.getExcludeIds() != null && in.getExcludeIds().length != 0) {
				for(String id : in.getExcludeIds())
					excludeIds.add(id);
				for(BEmployee be : emps)
					if(!excludeIds.contains(be.getPersonId()))
						goodEmps.add(be);
				emps = new BEmployee[goodEmps.size()];
				for(int i = 0; i < goodEmps.size(); i++)
					emps[i] = goodEmps.get(i);
			}
			ret.setEmployees(emps);

			if (!isEmpty(in.getGroupId()))
			{
				BOrgGroup grp=new BOrgGroup(in.getGroupId());
				ret.setCanAddOrEdit(grp.getCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;

	}
	
    @WebMethod()
	public ListTimeRelatedBenefitsReturn listTimeRelatedBenefits(/*@WebParam(name = "in")*/final ListTimeRelatedBenefitsInput in)		
	{
		final ListTimeRelatedBenefitsReturn ret=new ListTimeRelatedBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listTimeRelatedBenefits(hsu));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteTimeRelatedBenefitsReturn deleteTimeRelatedBenefits(/*@WebParam(name = "in")*/final DeleteTimeRelatedBenefitsInput in)
	{
		final DeleteTimeRelatedBenefitsReturn ret=new DeleteTimeRelatedBenefitsReturn();
		try
		{  
			checkLogin(in);

			for (String s : in.getIds())
			{
				BHRBenefit bc = new BHRBenefit(s);

				for (HrBenefitConfig hc : bc.getConfigs())
				{
					BHRBenefitConfig hrbc = new BHRBenefitConfig(hc);

					for (Project p : hrbc.listProjects())
					{
						BProject bp = new BProject(p);
						bp.delete();

						BProjectStatus ps = new BProjectStatus(bp.getProjectStatusId());
						ps.delete();
					}
				}
			}

			BHRBenefit.delete(in.getIds());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public EnrollEmployeesInBenefitReturn enrollEmployeesInBenefit(/*@WebParam(name = "in")*/final EnrollEmployeesInBenefitInput in) {

		final EnrollEmployeesInBenefitReturn ret = new EnrollEmployeesInBenefitReturn();

		try
		{
			checkLogin(in);
			
			String configId = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.COVERS_EMPLOYEE, 'Y')
																	   .eq(HrBenefitConfig.COVERS_CHILDREN, 'N')
																	   .eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE, 'N')
																	   .eq(HrBenefitConfig.COVERS_EMPLOYEE_SPOUSE_CHILDREN, 'N')
																	   .eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE, 'N')
																	   .eq(HrBenefitConfig.COVERS_NON_EMP_SPOUSE_CHILDREN, 'N')
																	   .eq(HrBenefitConfig.HR_BENEFIT_ID, in.getBenefitId())
																	   .first()
																	   .getBenefitConfigId();
			for(String id : in.getEmployeeIds()) {
				BEmployee be = new BEmployee(id);
				try {
					new BHRBenefitJoin(id, configId);
				}
				catch(ArahantException e) {
					BHRBenefitJoin bj = new BHRBenefitJoin();

					bj.create();
					bj.setCoveredPerson(be.getPerson());
					bj.setPayingPerson(be.getPerson());
					bj.setPolicyStartDate(DateUtils.now());
					bj.setCoverageStartDate(DateUtils.now());
					bj.setHrBenefitConfig(new BHRBenefitConfig(configId).getBean());
					bj.setChangeReason(BHRBenefitChangeReason.findOrMake("Miscellaneous").getId());
					bj.insert();
				}
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
