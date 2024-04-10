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
package com.arahant.business;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectTemplateBenefit;
import com.arahant.beans.ProjectTemplateBenefitAssignment;
import com.arahant.beans.ProjectType;
import com.arahant.beans.RouteStop;
import com.arahant.beans.RouteTypeAssoc;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BProjectTemplateBenefit extends SimpleBusinessObjectBase<ProjectTemplateBenefit> implements IDBFunctions {

	/**
	 */
	public BProjectTemplateBenefit() {
	}

	/**
	 * @param benefitTemplateId
	 * @throws ArahantException
	 */
	public BProjectTemplateBenefit(final String benefitTemplateId) throws ArahantException {
		internalLoad(benefitTemplateId);
	}

	/**
	 * @param ProjectTemplateBenefit
	 */
	public BProjectTemplateBenefit(final ProjectTemplateBenefit pb) {
		bean = pb;
	}

	public String getBenefitTemplateId() {
		return bean.getProjectTemplateId();
	}

	public void setBcrTemplateId(String benefitTemplateId) {
		bean.setProjectTemplateId(benefitTemplateId);
	}

	public String getBenefitId() {
		return bean.getBenefitId();
	}

	public void setBenefitId(String benefitId) {
		bean.setBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
	}

	public HrBenefit getBenefit() {
		return bean.getBenefit();
	}

	public void setBenefit(HrBenefit benefit) {
		bean.setBenefit(benefit);
	}

	public String getBcrId() {
		return bean.getBcrId();
	}

	public void setBcrId(String bcrId) {
		bean.setBcr(ArahantSession.getHSU().get(HrBenefitChangeReason.class, bcrId));
	}

	public HrBenefitChangeReason getBcr() {
		return bean.getBcr();
	}

	public void setBcr(HrBenefitChangeReason bcr) {
		bean.setBcr(bcr);
	}

	public String getProjectCategoryId() {
		return bean.getProjectCategory().getProjectCategoryId();
	}

	public void setProjectCategoryId(String projectCategoryId) {
		bean.setProjectCategory(ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
	}

	public ProjectCategory getProjectCategory() {
		return bean.getProjectCategory();
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		bean.setProjectCategory(projectCategory);
	}

	public String getProjectDescription() {
		return bean.getProjectDescription();
	}

	public void setProjectDescription(String projectDescription) {
		bean.setProjectDescription(projectDescription);
	}

	public String getProjectStatusId() {
		return bean.getProjectStatusId();
	}

	public void setProjectStatusId(String projectStatusId) {
		bean.setProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, projectStatusId));
	}

	public ProjectStatus getProjectStatus() {
		return bean.getProjectStatus();
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		bean.setProjectStatus(projectStatus);
	}

	public String getProjectTypeId() {
		return bean.getProjectTypeId();
	}

	public void setProjectTypeId(String projectTypeId) {
		bean.setProjectType(ArahantSession.getHSU().get(ProjectType.class, projectTypeId));
	}

	public ProjectType getProjectType() {
		return bean.getProjectType();
	}

	public void setProjectType(ProjectType projectType) {
		bean.setProjectType(projectType);
	}

	public HrEmployeeStatus getEmployeeStatus() {
		return bean.getEmployeeStatus();
	}

	public void setEmployeeStatus(HrEmployeeStatus employeeStatus) {
		bean.setEmployeeStatus(employeeStatus);
	}

	public String getEmployeeStatusId() {
		return bean.getEmployeeStatusId();
	}

	public OrgGroup getOrgGroup() {
		return bean.getOrgGroup();
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		bean.setOrgGroup(orgGroup);
	}

	public String getOrgGroupId() {
		return bean.getOrgGroupId();
	}

	@Override
	public void update() throws ArahantException {

		updateChecks();

		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {

		insertChecks();

		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void insertChecks() {
//		ProjectTemplateBenefit btp = ArahantSession.getHSU().createCriteria(ProjectTemplateBenefit.class)
//															.eq(ProjectTemplateBenefit.BENEFIT, getBenefit())
//															.eq(ProjectTemplateBenefit.PROJECT_CATEGORY, getProjectCategory())
//															.eq(ProjectTemplateBenefit.PROJECT_STATUS, getProjectStatus())
//															.eq(ProjectTemplateBenefit.PROJECT_TYPE, getProjectType())
//															.first();
//
//		if(btp != null)
//			throw new ArahantException("An identical project template already exists with this Category, Status, and Type combination.");
	}

	@Override
	public void updateChecks() {
//		ProjectTemplateBenefit btp = ArahantSession.getHSU().createCriteria(ProjectTemplateBenefit.class)
//															.eq(ProjectTemplateBenefit.BENEFIT, getBenefit())
//															.eq(ProjectTemplateBenefit.PROJECT_CATEGORY, getProjectCategory())
//															.eq(ProjectTemplateBenefit.PROJECT_STATUS, getProjectStatus())
//															.eq(ProjectTemplateBenefit.PROJECT_TYPE, getProjectType())
//															.ne(ProjectTemplateBenefit.BENEFIT_TEMPLATE_ID, getBenefitTemplateId())
//															.first();
//
//		if(btp != null)
//			throw new ArahantException("An identical project template already exists with this Category, Status, and Type combination.");
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {

		for (String id : ids) {
			ArahantSession.getHSU().createCriteria(ProjectTemplateBenefitAssignment.class).eq(ProjectTemplateBenefitAssignment.PROJECT_TEMPLATE_BENEFIT_ID, id).delete();
			new BProjectTemplateBenefit(id).delete();
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectTemplateBenefit();
		bean.generateId();
		return getBenefitTemplateId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectTemplateBenefit.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param name
	 * @return
	 * @throws ArahantException
	 */
	public static BProjectTemplateBenefit[] makeArray(final List<ProjectTemplateBenefit> l) throws ArahantException {

		final BProjectTemplateBenefit[] ret = new BProjectTemplateBenefit[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectTemplateBenefit(l.get(loop));

		return ret;
	}

	public static void createWizardProjects(BEmployee emp) {
		//get all unapproved enrollments (policy joins)
	}

	private static String getBenefitJoinDescription(BHRBenefitJoin[] l) {
		String policyMessage = "";
		String dependentMessage = "";
		String benefitMessage = "";
		String endline = "\n";
		for (BHRBenefitJoin bj : l) {
			BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
			//List personList = info.getPersons().get(info.getBenefitJoinList().indexOf(j));
			String coveredPersonId = bj.getCoveredPersonId();
			String payingPersonId = bj.getPayingPersonId();
			int bcrDate = bj.getChangeReasonDate();
			BPerson coveredPerson = new BPerson(coveredPersonId);

			if (coveredPersonId.equals(payingPersonId))
				policyMessage = "Employee: " + coveredPerson.getNameLFM() + endline
						+ "Employee SSN: " + coveredPerson.getSsn() + endline
						+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
						+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
						+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
						+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
						+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
						+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
						+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + //QE date
						"";
			else
				dependentMessage += "Covered Person: " + coveredPerson.getNameLFM() + endline
						+ "Covered SSN: " + coveredPerson.getSsn() + endline
						+ "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
						+ "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
						+ "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
						+ "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
						+ "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
						+ "Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
						+ "Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + endline + //QE date
						"";
			if (StringUtils.isEmpty(benefitMessage)) {
				BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
				BEmployee be = new BEmployee(payingPersonId);


				String sb = new String();
				sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
				sb += ("Transaction Type: " + bj.getRecordChangeType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
				if (bh.getBean().getProvider() != null)
					sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline; //Carrier
				else
					sb += ("Carrier: (Not Specified)") + endline;
				sb += ("Policy Owner: " + be.getNameFML()) + endline;
				sb += ("Policy Owner SSN: " + be.getSsn()) + endline;
				sb += ("Benefit: " + bj.getBenefitConfig().getBenefitName()) + endline;
				sb += ("Level: " + bj.getBenefitConfigName()) + endline;
				if (bj.getUsingCOBRA()) {
					sb += ("Cobra: " + "Yes") + endline;
					sb += ("Accepted Cobra Date: " + DateUtils.getDateFormatted(bj.getAcceptedDateCOBRA())) + endline;
					sb += ("Max Cobra Months:" + bj.getMaxMonthsCOBRA()) + endline;
				}
				sb += ("Policy: " + bh.getGroupId()) + endline;
				sb += ("Plan: " + bh.getPlan()) + endline;
				sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
				sb += ("Plan Name: " + bh.getPlanName()) + endline;
				sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
				sb += ("Email: " + be.getPersonalEmail()) + endline;
				if (ArahantSession.multipleCompanySupport)
					sb += ("Company: " + be.getCompanyName()) + endline;

				benefitMessage = sb;
			}
		}
		return benefitMessage + "\n---------------------------  \n" + policyMessage + "\n" + dependentMessage;
	}

	public static void main(String[] args) {
		String sql = "";
		String sql2 = "";

		List<String> benefits = new ArrayList<String>();
		benefits.add("00001-0000000047"); //'Long Term Care - Employee'
		benefits.add("00001-0000000049"); //'Voluntary - Cancer Policy'
		benefits.add("00001-0000000048"); //'Voluntary - Heart/Stroke Coverage'
		benefits.add("00001-0000000038"); //'Life - Supplemental Life Spouse'
		benefits.add("00001-0000000036"); //'Life - AD&D'
		benefits.add("00001-0000000035"); //'Life - Supplemental Employee Life'
		benefits.add("00001-0000000037"); //'Life - Basic Dependent Life'
		benefits.add("00001-0000000040"); //'Short Term Disability'
		benefits.add("00001-0000000053"); //'Long Term Care - Dependent'
		benefits.add("00001-0000000070"); //'Group Critical Illness'
		benefits.add("00001-0000000041"); //'Long Term Disabilty'
		benefits.add("00001-0000000050"); //'Voluntary - Major Illness Policy'
		benefits.add("00001-0000000039"); //'Life - Supplemental Life Child(ren)'
		benefits.add("00001-0000000051"); //'Voluntary - Universal Life'


//		List<String> bcrs = new ArrayList<String>();
//		bcrs.add("00001-0000000007"); //birth adoption --PAM
//		bcrs.add("00001-0000000004"); //marraige --PAM
//		bcrs.add("00001-0000000010"); //loss of other coverage --PAM
//		bcrs.add("00001-0000000006"); //death --LAURIE
//		bcrs.add("00001-0000000009"); //divorce --LAURIE
//		bcrs.add("00001-0000000005"); //gaining other coverage --LAURIE
//		bcrs.add("00001-0000000012"); //Dep Inelig - married --LAURIE
//		bcrs.add("00001-0000000014"); //Dep Inelig - college grad --LAURIE
//		bcrs.add("00001-0000000013"); //Dep Inelig - not FT or no FTSS --LAURIE
//		bcrs.add("00001-0000000011"); //Dep Inelig - turning 19 and not a student --LAURIE
//		bcrs.add("00001-0000000008"); //Dep Inelig - turning 25 --LAURIE
//		bcrs.add("00001-0000000018"); //Dep Inelig - returned to college --LAURIE

		//project categories
//		'00001-0000000018','DruryGroupBOEActive'
//		'00001-0000000019','DruryGroupWCGActive'
//		'00001-0000000020','DruryGroupMisc'
//		'00001-0000000021','DruryGroupBOELOA'
//		'00001-0000000022','DruryGroupWCGLOA'
//		'00001-0000000023','PartTimeEmployment'
//		'00001-0000000024','DeceasedEmployee'
//		'00001-0000000025','Human Resources'


		List<String> statuses = new ArrayList<String>();
		statuses.add("00001-0000000001"); //active
		statuses.add("00001-0000000006"); //interim
		statuses.add("00001-0000000003"); //LOA


		List<String> orgGroups = new ArrayList<String>();
		orgGroups.add("00001-0000000001"); //BOE P
		orgGroups.add("00001-0000000002"); //BOE 10
		orgGroups.add("00001-0000000007"); //BOE 11
		orgGroups.add("00001-0000000008"); //BOE 12
		orgGroups.add("00001-0000000011"); //School Board

		orgGroups.add("00001-0000000003"); //General WCG
		orgGroups.add("00001-0000000005"); //HW WCG
		orgGroups.add("00001-0000000006"); //Sanitation
		orgGroups.add("00001-0000000010"); //County Commissioner

		Integer ptCount = 1262;
		Integer ptCount2 = 1264;

		for (HrBenefit b : ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefits).list())
			for (HrEmployeeStatus s : ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).list())
				for (OrgGroup og : ArahantSession.getHSU().createCriteria(OrgGroup.class).in(OrgGroup.ORGGROUPID, orgGroups).list()) {
					BProjectCategory projCat = new BProjectCategory("00001-0000000020"); //Misc
					BProjectType projType = new BProjectType("00001-0000000029");
					RouteTypeAssoc rta;
					ProjectStatus projectStatus;

					if (s.getStatusId().equals("00001-0000000003")) //LOA
					
						if (og.getOrgGroupId().equals("00001-0000000003") || //County
								og.getOrgGroupId().equals("00001-0000000005")
								|| og.getOrgGroupId().equals("00001-0000000006")
								|| og.getOrgGroupId().equals("00001-0000000010"))
							projCat = new BProjectCategory("00001-0000000022");
						else
							projCat = new BProjectCategory("00001-0000000021");
					else if (s.getActive() == 'Y') //all others, actives
					
						if (og.getOrgGroupId().equals("00001-0000000003") || //County
								og.getOrgGroupId().equals("00001-0000000005")
								|| og.getOrgGroupId().equals("00001-0000000006")
								|| og.getOrgGroupId().equals("00001-0000000010"))
							projCat = new BProjectCategory("00001-0000000019");
						else
							projCat = new BProjectCategory("00001-0000000018");


					rta = ArahantSession.getHSU().createCriteria(RouteTypeAssoc.class).eq(RouteTypeAssoc.PROJECT_TYPE, projType.getBean())
							.eq(RouteTypeAssoc.PROJECT_CATEGORY, projCat.getBean())
							.joinTo(RouteTypeAssoc.ROUTE)
							.first();

					if (rta == null) {
						System.out.println("FAILED TO MAKE PROJECT FOR:  " + b.getName() + " / " + "(All BCRs)" + " / " + s.getName() + " / " + og.getName());
						continue;
					}

					HibernateCriteriaUtil<RouteStop> routeStopHcu = ArahantSession.getHSU().createCriteria(RouteStop.class).eq(RouteStop.ROUTE, rta.getRoute());
					projectStatus = ArahantSession.getHSU().createCriteria(ProjectStatus.class)
							.orderBy(ProjectStatus.CODE)
							.like(ProjectStatus.CODE, "%Open%")
							.joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS)
							.in(RouteStop.ROUTE_STOP_ID, routeStopHcu.selectFields(RouteStop.ROUTE_STOP_ID)
							.list()).first();

					String projectTemplateId = "00001-000000";
					if (ptCount.toString().length() == 3)
						projectTemplateId += "0" + ptCount.toString();
					else if (ptCount.toString().length() == 4)
						projectTemplateId += ptCount.toString();
					String templateAssignmentId = "00001-000000";
					if (ptCount2.toString().length() == 3)
						templateAssignmentId += "0" + ptCount2.toString();
					else if (ptCount2.toString().length() == 4)
						templateAssignmentId += ptCount2.toString();

					sql += "insert into project_template_benefit values('" + b.getBenefitId() + "','"
							+ projectStatus.getProjectStatusId() + "','"
							+ projCat.getProjectCategoryId() + "','"
							+ projType.getProjectTypeId() + "','"
							+ projType.getCode() + " - " + s.getName() + " - " + b.getName() + "','"
							+ s.getStatusId() + "','"
							+ og.getOrgGroupId() + "','"
							+ projectTemplateId + "',"
							+ "null" + ");";
					sql2 += "insert into project_template_benefit_a values('00002-0000000001','" + templateAssignmentId + "','" + projectTemplateId + "');";
//					System.out.println("insert into project_template_benefit values('" + b.getBenefitId() + "','" +
//							projectStatus.getProjectStatusId() + "','" +
//							projCat.getProjectCategoryId() + "','" +
//							projType.getProjectTypeId() + "','" +
//							projType.getCode() + " - " + s.getName() + " - " + b.getName() + "','" +
//							s.getStatusId() + "','" +
//							og.getOrgGroupId() + "','" +
//							projectTemplateId + "'," +
//							"null" + "); ");
					ptCount++;
					ptCount2++;
				}

		System.out.println("FINAL SQL STATEMENT\n");
		System.out.println(sql);
		System.out.print("FINAL ASSIGNMENT STATEMENT");
		System.out.println(sql2);
	}
//	public static void main(String[] args) {
//		String sql = "";
//
//		List<String> benefits = new ArrayList<String>();
//		benefits.add("00001-0000000023"); //copay
//		benefits.add("00001-0000000019"); //deductible
//		benefits.add("00001-0000000026"); //in hospital supplemental
//		benefits.add("00001-0000000062"); //reimbursement plan
//		benefits.add("00001-0000000061"); //dental
//
//
//		List<String> bcrs = new ArrayList<String>();
//		bcrs.add("00001-0000000007"); //birth adoption --PAM
//		bcrs.add("00001-0000000004"); //marraige --PAM
//		bcrs.add("00001-0000000010"); //loss of other coverage --PAM
//		bcrs.add("00001-0000000006"); //death --LAURIE
//		bcrs.add("00001-0000000009"); //divorce --LAURIE
//		bcrs.add("00001-0000000005"); //gaining other coverage --LAURIE
//		bcrs.add("00001-0000000012"); //Dep Inelig - married --LAURIE
//		bcrs.add("00001-0000000014"); //Dep Inelig - college grad --LAURIE
//		bcrs.add("00001-0000000013"); //Dep Inelig - not FT or no FTSS --LAURIE
//		bcrs.add("00001-0000000011"); //Dep Inelig - turning 19 and not a student --LAURIE
//		bcrs.add("00001-0000000008"); //Dep Inelig - turning 25 --LAURIE
//		bcrs.add("00001-0000000018"); //Dep Inelig - returned to college --LAURIE
//
//
//		List<String> statuses = new ArrayList<String>();
//		statuses.add("00001-0000000001"); //active
//		statuses.add("00001-0000000006"); //interim
//
//
//		List<String> orgGroups = new ArrayList<String>();
//		orgGroups.add("00001-0000000001"); //BOE P
//		orgGroups.add("00001-0000000002"); //BOE 10
//		orgGroups.add("00001-0000000007"); //BOE 11
//		orgGroups.add("00001-0000000008"); //BOE 12
//		orgGroups.add("00001-0000000011"); //School Board
//		orgGroups.add("00001-0000000003"); //General WCG
//		orgGroups.add("00001-0000000005"); //HW WCG
//		orgGroups.add("00001-0000000006"); //Sanitation
//		orgGroups.add("00001-0000000010"); //County Commissioner
//
//		Integer ptCount = 176;
//
//		for (HrBenefit b : ArahantSession.getHSU().createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefits).list())
//		{
//
//			for (HrBenefitChangeReason bcr : ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).in(HrBenefitChangeReason.ID, bcrs).list())
//			{
//
//				for (HrEmployeeStatus s : ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).in(HrEmployeeStatus.STATUSID, statuses).list())
//				{
//
//					for (OrgGroup og : ArahantSession.getHSU().createCriteria(OrgGroup.class).in(OrgGroup.ORGGROUPID, orgGroups).list())
//					{
//						BProjectCategory projCat;
//						BProjectType projType;
//						RouteTypeAssoc rta;
//						ProjectStatus projectStatus;
//
//
//
//						if(bcr.getHrBenefitChangeReasonId().equals("00001-0000000007") ||
//						   bcr.getHrBenefitChangeReasonId().equals("00001-0000000004") ||
//						   bcr.getHrBenefitChangeReasonId().equals("00001-0000000010"))
//						{
//							if(og.getOrgGroupId().equals("00001-0000000011") ||
//							   og.getOrgGroupId().equals("00001-0000000003") ||
//							   og.getOrgGroupId().equals("00001-0000000005") ||
//							   og.getOrgGroupId().equals("00001-0000000006") ||
//							   og.getOrgGroupId().equals("00001-0000000010"))
//							{
//								projCat = new BProjectCategory("00001-0000000016"); //AIAddDepCounty
//							}
//							else
//							{
//								projCat = new BProjectCategory("00001-0000000002"); //AIAddDepSchool
//							}
//							projType = new BProjectType("00001-0000000002"); //add dep
//						}
//						else
//						{
//							projCat = new BProjectCategory("00001-0000000004"); //drop dep
//							projType = new BProjectType("00001-0000000003"); //drop dep
//						}
//						rta = ArahantSession.getHSU().createCriteria(RouteTypeAssoc.class).eq(RouteTypeAssoc.PROJECT_TYPE, projType.getBean())
//																  .eq(RouteTypeAssoc.PROJECT_CATEGORY, projCat.getBean())
//																  .joinTo(RouteTypeAssoc.ROUTE)
//																  .first();
//
//						if(rta == null)
//						{
//							System.out.println("FAILED TO MAKE PROJECT FOR:  " + b.getName() + " / " + bcr.getDescription() + " / " + s.getName() + " / " + og.getName());
//							continue;
//						}
//
//						HibernateCriteriaUtil<RouteStop> routeStopHcu = ArahantSession.getHSU().createCriteria(RouteStop.class).eq(RouteStop.ROUTE, rta.getRoute());
//						projectStatus = ArahantSession.getHSU().createCriteria(ProjectStatus.class)
//																   .orderBy(ProjectStatus.CODE)
//																   .like(ProjectStatus.CODE, "%Open%")
//																   .joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS)
//																   .in(RouteStop.ROUTE_STOP_ID, routeStopHcu.selectFields(RouteStop.ROUTE_STOP_ID)
//																   .list()).first();
//
//						String projectTemplateId = "00001-000000";
//						if(ptCount.toString().length() == 3)
//							projectTemplateId += "0" + ptCount.toString();
//						else if(ptCount.toString().length() == 4)
//							projectTemplateId += ptCount.toString();
//
//						sql += "insert into project_template_benefit values('" + b.getBenefitId() + "','" +
//								projectStatus.getProjectStatusId() + "','" +
//								projCat.getProjectCategoryId() + "','" +
//								projType.getProjectTypeId() + "','" +
//								projType.getCode() + " - " + s.getName() + " - " + b.getName() + "','" +
//								s.getStatusId() + "','" +
//								og.getOrgGroupId() + "','" +
//								projectTemplateId + "','" +
//								bcr.getHrBenefitChangeReasonId() + "');";
//						System.out.println("insert into project_template_benefit values('" + b.getBenefitId() + "','" +
//								projectStatus.getProjectStatusId() + "','" +
//								projCat.getProjectCategoryId() + "','" +
//								projType.getProjectTypeId() + "','" +
//								projType.getCode() + " - " + s.getName() + " - " + b.getName() + "','" +
//								s.getStatusId() + "','" +
//								og.getOrgGroupId() + "','" +
//								projectTemplateId + "','" +
//								bcr.getHrBenefitChangeReasonId() + "'); ");
//						ptCount++;
//					}
//
//				}
//
//			}
//
//		}
//
//		System.out.println("FINAL SQL STATEMENT\n");
//		System.out.println(sql);
//	}
}
