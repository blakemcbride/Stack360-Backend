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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import org.kissweb.TimeUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BProject extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BProject.class);
    private Collection<ArahantBean> updates = new LinkedList<ArahantBean>();
    private String routeStopId = "";
    private String lastStatusId = "";
    Project project;


    public void addChildren(String[] subProjectIds) {
		int seq = 0;

		ProjectViewJoin p = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).orderByDesc(ProjectViewJoin.SEQ).joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).first();

		if (p != null)
			seq = p.getSeqNum() + 1;

		ProjectView pv = BProjectView.getForProjectId(project.getProjectId());

		for (String id : subProjectIds) {
			BProjectViewJoin bpvj = new BProjectViewJoin();

			bpvj.create();

			bpvj.setParent(pv);

			bpvj.setChild(BProjectView.getForProjectId(id));

			bpvj.setSeq(seq++);

			bpvj.insert();
		}
	}

	public void sendToAssignees(List<String> addPeople) {
		String names = "";
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		for (String pid : addPeople) {
			Person p = hsu.get(Person.class, pid);
			names += p.getNameLFM() + "\n";
		}
		sendToAssignees("Project " + project.getProjectName().trim() + " Assignments",
				"The following people were assigned to " + project.getProjectName().trim() + "\n" + names);
	}

	public void calculateRouteAndStatus() {
		//WMCO WORKFLOW

		String projectCategoryId;
		if (project.getProjectCategory() == null) {
			AIProperty prop = new AIProperty("CalculateDefaultCategory", project.getProjectTypeId(), project.getDoneForPersonId());
			projectCategoryId = prop.getValue();

			if (isEmpty(projectCategoryId))
				projectCategoryId = BProperty.get(StandardProperty.DefaultProjectCategoryId);

			project.setProjectCategory(ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
		} else
			projectCategoryId = project.getProjectCategory().getProjectCategoryId();

		BRouteTypeAssoc rta = new BRouteTypeAssoc(projectCategoryId, project.getProjectTypeId());

		if (!isEmpty(rta.getRouteId())) {
			BRoute rt = new BRoute(rta.getRouteId());
			if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
				if (project.getProjectStatus() == null)
					project.setProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, rt.getProjectStatusId()));
				project.setRouteStop(rt.getRouteStop().bean);
			}
		}
	}

	public void changePriority(String personId, int priority) {
		ProjectEmployeeJoin pej = ArahantSession.getHSU().createCriteria(ProjectEmployeeJoin.class).joinTo(ProjectEmployeeJoin.PROJECTSHIFT).eq(ProjectShift.PROJECT, project).joinTo(ProjectEmployeeJoin.PERSON).eq(Person.PERSONID, personId).first();

		pej.setPersonPriority((short) priority);

		updates.add(pej);
	}

	public String getBenefitName() {
		try {
			return ArahantSession.getHSU().createCriteria(HrBenefit.class).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, project).first().getName();

		} catch (Exception e) {
			return "";
		}
	}

	public String getProjectFormatted() {
		return getProjectName().trim() + " - " + getDescription().trim();
	}

	public String getRequestingCompanyId() {
		return project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId();
	}

	public String getRequestingOrgGroupId() {
		return project.getRequestingOrgGroup().getOrgGroupId();
	}

	public String getAssignedToOrgGroup(String orgGroupId) {
		try {
			if (orgGroupId.equals(project.getRouteStop().getOrgGroupId()))
				return "Yes";

			if (project.getRequestingOrgGroup().getOrgGroupId().equals(orgGroupId))
				if (project.getRouteStop().getOrgGroup() == null)
					return "Yes";
		} catch (Exception e) {
			//must not be assigned
		}

		return "No";
	}

	public boolean getBillableInsideEligible(BProject child) {
		return child.project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId().equals(project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId());
	}

	public String getId() {
		return project.getProjectId();
	}

	public String getName() {
		//pad to 30 characters right justified
		if (project.getProjectName() == null)
			project.setProjectName(makeProjectName());
		if (project.getProjectName().length() < 8)
			return String.format("%8s", project.getProjectName());

		return project.getProjectName();
	}

	public String getPhaseCode() {
		try {
			return project.getRouteStop().getProjectPhase().getCode();
		} catch (NullPointerException ne) {
			return "";
		}
	}

	public String getPhaseDescription() {
		try {
			return project.getRouteStop().getProjectPhase().getDescription();
		} catch (NullPointerException ne) {
			return "";
		}
	}

	public String getPrimaryParentId() {
		Project p = ArahantSession.getHSU().createCriteria(Project.class).joinTo(Project.VIEWS).joinTo(ProjectView.VIEW_JOINS_WHERE_PARENT).eq(ProjectViewJoin.PRIMARY_BILLING, 'Y').joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project).first();
		return (p == null) ? "" : p.getProjectId();
	}

	public String getProjectStatusDescription() {
		return project.getProjectStatus().getDescription();
	}

	public String getRequestingOrgGroupName() {
		return getOrgGroupName();
	}

	public String getRequestingPersonOrCreatedBy() {
		if (!isEmpty(project.getRequesterName()))
			return project.getRequesterName();
		if (project.getEnteredByPerson() != null)
			return project.getEnteredByPerson().getNameLFM();
		return "";
	}

	/**
	 * This is the person who created the project.
	 *
	 * @return
	 */
	public String getSponsorNameFormatted() {
		if (project.getEnteredByPerson() == null)
			return "";
		return project.getEnteredByPerson().getNameLFM();
	}

	public String getSummary() {
		return getDescription();
	}

	public String getTopBillingProjectId() {
		Project p = ArahantSession.getHSU().createCriteria(Project.class).joinTo(Project.VIEWS).joinTo(ProjectView.VIEW_JOINS_WHERE_PARENT).eq(ProjectViewJoin.PRIMARY_BILLING, 'Y').joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project).first();


		if (p == null)
			return project.getProjectId();

		if (p.getBillable() == 'U')
			return new BProject(p).getTopBillingProjectId();

		return p.getProjectId();
	}

	public String getProjectCategoryDescription() {
		return project.getProjectCategory().getDescription();
	}

	public String getWorkDoneForEmployeeFirstName() {
		try {
			return project.getDoneForPerson().getFname();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkDoneForEmployeeLastName() {
		try {
			return project.getDoneForPerson().getLname();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkDoneForEmployeePersonType() {
		if (project.getDoneForPerson() == null)
			return "";

		BPerson bp = new BPerson(project.getDoneForPerson());

		if (bp.isEmployee())
			return "Emp";
		else
			return "Dep";
	}

	public void removeChidren(String[] subProjectIds) {
		HibernateCriteriaUtil<ProjectViewJoin> hcu = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class);
		hcu.joinTo(ProjectViewJoin.CHILD).isNull(ProjectView.PERSON).joinTo(ProjectView.PROJECT).in(Project.PROJECTID, subProjectIds);
		hcu.joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).isNull(ProjectView.PERSON);

		for (BProjectViewJoin p : BProjectViewJoin.makeArray(hcu.list()))
			p.delete();

		for (String projId : subProjectIds) {
			BProject bp = new BProject(projId);
			if (bp.getBillable() == 'I') {
				bp.setBillable('Y');
				bp.update();
			}
		}
	}

	@SuppressWarnings({"unchecked", "unchecked"})
	public BProject[] search(String summary, String category, String status, String type, String companyId, String projectName, int cap) {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class);
		hcu.setMaxResults(cap);
		hcu.like(Project.PROJECTNAME, projectName);
		hcu.like(Project.DESCRIPTION, summary);

		if (!isEmpty(category))
			hcu.joinTo(Project.PROJECTCATEGORY).eq(ProjectCategory.PROJECTCATEGORYID, category);

		if (!isEmpty(status))
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.PROJECTSTATUSID, status);

		if (!isEmpty(type))
			hcu.joinTo(Project.PROJECTTYPE).eq(ProjectType.PROJECTTYPEID, type);

		if (!isEmpty(companyId))
			hcu.joinTo(Project.REQUESTING_ORG_GROUP).joinTo(OrgGroup.OWNINGCOMPANY).eq(CompanyBase.ORGGROUPID, companyId);


		//TODO: someday combine this into query
		HibernateCriteriaUtil children = ArahantSession.getHSU().createCriteria(Project.class).selectFields(Project.PROJECTID).joinTo(Project.VIEWS).isNull(ProjectView.PERSON).joinTo(ProjectView.VIEW_JOINS_WHERE_CHILD).joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).isNull(ProjectView.PERSON);

		@SuppressWarnings("unchecked")
		List<String> ids = children.list();
		ids.add(project.getProjectId());

		HibernateCriteriaUtil parents = ArahantSession.getHSU().createCriteria(Project.class).selectFields(Project.PROJECTID).joinTo(Project.VIEWS).isNull(ProjectView.PERSON).joinTo(ProjectView.VIEW_JOINS_WHERE_PARENT).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project).isNull(ProjectView.PERSON);

		ids.addAll(parents.list());

		hcu.notIn(Project.PROJECTID, ids);

		return makeArray(hcu.list());
	}

	/**
	 * @param projectId
	 * @throws ArahantException
	 */
	public BProject(final String projectId) throws ArahantException {
		internalLoad(projectId);
	}

	public BProject() {
	}

	private ProjectEmployeeJoin getSingleEmployeeJoin() {
		Connection db = ArahantSession.getKissConnection();
		Record rec;
		try {
			rec = db.fetchOne("select pej.project_employee_join_id " +
					"from project_employee_join pej " +
					"join project_shift ps " +
					"  on pej.project_shift_id = ps.project_shift_id " +
					"join project p " +
					"  on ps.project_id = p.project_id " +
					"where p.project_id = ?", getProjectId());
			if (rec == null)
				return null;
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
					.eq(ProjectEmployeeJoin.PROJECT_EMPLOYEE_JOIN_ID, rec.getString("project_employee_join_id"))
					.first();
			return pej;
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

	private Person getSingleCurrentAssignedPerson() {
		if (getSingleEmployeeJoin() != null)
			return getSingleEmployeeJoin().getPerson();

		return null;
	}

	public BProject(final Project proj) {
		super();
		project = proj;
		if (project.getRouteStop() != null)
			routeStopId = project.getRouteStop().getRouteStopId();
		if (project.getProjectStatus() != null)
			lastStatusId = project.getProjectStatus().getProjectStatusId();
		saveOriginalData(project);
	}

	@Override
	public String create() throws ArahantException {
		project = new Project();
		project.generateId();
		project.setDateReported(DateUtils.now());
		project.setEnteredByPerson(ArahantSession.getCurrentPerson());
		project.setTimeReported(TimeUtils.now() * 100000);
		return getProjectId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			if (!project.getTimesheets().isEmpty())
				throw new ArahantDeleteException("Cannot delete; time has been entered against this project");
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.delete(project.getHrBenefitProjectJoins());
			hsu.delete(project.getProjectComments());
			hsu.createCriteria(PersonCR.class).eq(PersonCR.PROJECT, project).delete();
			// hsu.delete(project.getProjectHistories());
			BProject.unassignAllAssignmentHistory(project.getProjectId());
			hsu.createCriteria(ProjectEmployeeJoin.class).joinTo(ProjectEmployeeJoin.PROJECTSHIFT).eq(ProjectShift.PROJECT, project).delete();
			hsu.createCriteria(ProjectHistory.class).eq(ProjectHistory.PROJECT, project).delete();
			hsu.createCriteria(QuickbooksProjectChange.class).eq(QuickbooksProjectChange.PROJECT, project).delete();
			hsu.createCriteria(ProjectShift.class).eq(ProjectShift.PROJECT, project).delete();
			hsu.delete(project);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public int getApprovalDate() {
		return DateUtils.getDate(project.getApprovingTimestamp());
	}

	public String getApprovalEnteredBy() {
		if (project.getApprovingPerson() == null)
			return "";
		return project.getApprovingPerson().getNameLFM();
	}

	public String getApprovalEnteredFormatted() {
		return DateUtils.getDateFormatted(project.getApprovingTimestamp());
	}

	public int getApprovalTime() {
		return DateUtils.getTime(project.getApprovingTimestamp());
	}

	public String getApprovedBy() {
		return project.getApprovingPersonTxt();
	}

	public String getAssignedPersonName() {
		if (project.getEmployee() == null)
			return "";
		return project.getEmployee().getNameLFM();
	}

	public int getClientPriority() {
		return project.getPriorityClient();
	}

	public int getCompanyPriority() {
		return project.getPriorityCompany();
	}

	public String getCurrentCompanyName() {
		try {
			if (project.getRouteStop() == null || project.getRouteStop().getOrgGroup() == null)
				return "Requesting Company";

			BOrgGroup borg = new BOrgGroup(project.getRouteStop().getOrgGroup());
			if (borg.isCompany())
				return borg.getName();

			return borg.getCompanyName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getDateReportedFormatted() {
		return DateUtils.getDateFormatted(project.getDateReported());
	}

	public float getCalculatedBillingRate(Employee emp) {
		if (project.getBillingRate() < .01)
			return getDefaultBillingRate(emp);
		return project.getBillingRate();
	}

	public float getDefaultBillingRate(Employee emp) {
		try {
			// get parent rate first
			if (project.getBillable() == 'I' && !"".equals(getPrimaryParentId()))
				return new BProject(getPrimaryParentId()).getDefaultBillingRate(emp);

			if (project.getRequestingOrgGroup() != null) {
				ClientCompany cc = ArahantSession.getHSU().get(ClientCompany.class, project.getRequestingOrgGroup().getOrgGroupId());
				if (cc == null)
					cc = ArahantSession.getHSU().get(ClientCompany.class, project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId());
				if (cc != null)
					if (cc.getBillingRate() != 0)
						return cc.getBillingRate();

			}
		} catch (Exception e) {
			//no client
		}
		if (emp != null) {
            EmployeeRate er = new BEmployee(emp).getBillingRate(project.getRateType().getRateTypeId());
            if (er != null) {
                float rate = (float) er.getRate();
                if (rate >= .01)
                    return rate;
            }
		}
		return BClientCompany.getDefaultBillingRate();
	}

	public String getDefaultBillingRateFormatted() {
		// get parent rate first
		if (project.getBillable() == 'I' && !"".equals(getPrimaryParentId()))
			return new BProject(getPrimaryParentId()).getDefaultBillingRateFormatted();

		//get client rate first
		try {
			if (project.getRequestingOrgGroup() != null) {
				ClientCompany cc = ArahantSession.getHSU().get(ClientCompany.class, project.getRequestingOrgGroup().getOrgGroupId());
				if (cc == null)
					cc = ArahantSession.getHSU().get(ClientCompany.class, project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId());
				if (cc != null)
					if (cc.getBillingRate() != 0)
						return MoneyUtils.formatMoney(cc.getBillingRate());

			}
		} catch (Exception e) {
			//no client
		}

		return BClientCompany.getDefaultBillingRateFormatted();

		/*
		 * //get company rate try {
		 *
		 * if (project.getOrgGroup()!=null) { CompanyDetail
		 * cd=hsu.get(CompanyDetail.class,
		 * project.getOrgGroup().getOrgGroupId()); if (cd==null)
		 * cd=hsu.get(CompanyDetail.class,
		 * project.getOrgGroup().getOwningCompany().getOrgGroupId()); if
		 * (cd!=null) if (cd.getBillingRate()!=0) return
		 * MoneyUtils.formatMoney(cd.getBillingRate()); } } catch (Exception e)
		 * { //no company? }
		 *
		 *
		 *
		 * return "Employee Rate";
         *
		 */
	}

	public int getEmployeePriority(Person p) {
		// XXYY
		throw new ArahantException("XXYY");
		/*
		try {
			ProjectEmployeeJoin pej = ArahantSession.getHSU().createCriteria(ProjectEmployeeJoin.class)
					.eq(ProjectEmployeeJoin.PERSON, p)
					.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
					.eq(ProjectShift.PROJECT, project)
					.first();
			if (pej != null)
				return pej.getPersonPriority();

			return project.getProjectEmployeeJoins().iterator().next().getPersonPriority();
		} catch (Exception e) {
			return 0;
		}
		 */
	}

	public int getEstimateOnDate() {
		return project.getDateOfEstimate();
	}

	public double getBillTreeEstimateHours() {
		//  XXYY
		throw new ArahantException("XXYY");
//		return ArahantSession.getHSU().createCriteria(Project.class).sum(Project.ESTIMATE_HOURS).in(Project.PROJECTID, getBillProjectsTree()).doubleVal();
	}

	public double getEstimateHours() {
		return project.getEstimateHours();
	}

	public double getEstimateTimeSpan() {
		return project.getEstimatedTimeSpan();
	}

	public double getInvoicedHours() {
		return ArahantSession.getHSU().createCriteria(Timesheet.class)
				.eq(Timesheet.STATE, TIMESHEET_INVOICED)
				.eq(Timesheet.BILLABLE, 'Y') //.eq(Timesheet.PROJECT, project)
				.sum(Timesheet.TOTALHOURS)
				.joinTo(Timesheet.PROJECTSHIFT)
				.joinTo(ProjectShift.PROJECT)
				.in(Project.PROJECTID, getBillProjectsTree())
				.doubleVal();
	}

	public int getOrgGroupPriority() {
		return project.getPriorityDepartment();
	}

	public int getPhaseSecurityLevel() {
		try {
			return project.getRouteStop().getProjectPhase().getSecurityLevel();
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPromisedDate() {
		return project.getDatePromised();
	}

	public String getRequesterNameFormatted() {
		try {
			if (project.getRequestingOrgGroup() == null)
				return "Requesting Company (Requesting Organizational Group)";
			BOrgGroup borg = new BOrgGroup(project.getRequestingOrgGroup());
			return borg.getNameFormatted();
		} catch (Exception e) {
			return "Requesting Company (Requesting Organizational Group)";
		}
	}

	public String getRequestingCompanyName() {
		return project.getRequestingOrgGroup().getOwningCompany().getName();
	}

	public String getRequestingGroup() {
		return new BOrgGroup(project.getRequestingOrgGroup()).getNameFormatted();
	}

	public String getRequestingGroupId() {
		return project.getRequestingOrgGroup().getOrgGroupId();
	}

	public String getRequestingNameFormatted() {
		return project.getRequesterName();
	}

	public String getStatusEffect(BProjectStatus bc) {

		final BRoutePath rp = checkForRouteStop(bc.getProjectStatusId());
		if (rp != null)
			return "Send to Next Route Stop";

		return "";
	}

	public String getWorkDoneForEmployeeId() {
		try {
			return project.getDoneForPerson().getPersonId();
		} catch (Exception e) {
			return "";
		}
	}

	public String getProjectCode() {
	    return project.getProjectCode();
    }

    public void setProjectCode(String pc) {
	    project.setProjectCode(pc);
    }

	@Override
	public void insert() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (isEmpty(project.getProjectName()))
			setProjectName(makeProjectName());

		// make sure there is a date/time assigned for person assignments
		if (getSingleCurrentAssignedPerson() != null && getSingleEmployeeJoin().getDateAssigned() == 0) {
			getSingleEmployeeJoin().setDateAssigned(DateUtils.now());
			getSingleEmployeeJoin().setTimeAssigned(DateUtils.nowTime());

			updates.add(getSingleEmployeeJoin());
		}

		/*
		//AutoAssignProject to employee who created the project
		//Jason asked for this feature 5/14/2010
		if (BProperty.getBoolean("AutoAssignProject"))
			if (getEmployeeId().equals("")) {
				setEmployeeId(hsu.getCurrentPerson().getPersonId());
				setBillable('Y');
				assignPerson(hsu.getCurrentPerson().getPersonId(), 10, true);
			}
		 */
		hsu.insert(project);
		hsu.insert(updates);


		// check for need of history record
		this.checkForHistory();
		sendNotifications();
	}

	public BProjectStatus[] searchStatuses(String code, String description, int max) {
		HibernateCriteriaUtil<ProjectStatus> hcu = ArahantSession.getHSU().createCriteria(ProjectStatus.class).like(ProjectStatus.CODE, code).like(ProjectStatus.DESCRIPTION, description).setMaxResults(max);

		hcu.joinTo(ProjectStatus.TOROUTEPATHS).joinTo(RoutePath.TO_ROUTE_STOP).eq(RouteStop.PROJECTS, project);

		return BProjectStatus.makeArray(hcu.list());
	}

	public void setApprovedBy(String approvedBy) {
		if (isEmpty(approvedBy)) //once set can't be made empty
			return;
		if (!approvedBy.equals(project.getApprovingPersonTxt())) {
			project.setApprovingPersonTxt(approvedBy);
			project.setApprovingTimestamp(new Date());
			project.setApprovingPerson(ArahantSession.getHSU().getCurrentPerson());
		}
	}

	public void setClientPriority(int clientPriority) {
		project.setPriorityClient((short) clientPriority);
	}

	public void setCompanyPriority(int companyPriority) {
		project.setPriorityCompany((short) companyPriority);
	}

	public void setEstimateHours(double estimate) {
		project.setEstimateHours((float) estimate);
	}

	public void setEstimateOnDate(int estimateOnDate) {
		project.setDateOfEstimate(estimateOnDate);
	}

	public void setEstimateTimeSpan(double hours) {
		project.setEstimatedTimeSpan((int) hours);
	}

	public void setOrgGroupPriority(int orgGroupPriority) {
		project.setPriorityDepartment((short) orgGroupPriority);
	}

	public void setPrimaryParentId(String primaryParentId) {

		if (isEmpty(primaryParentId))
			return;

		if (project.getBillable() != 'I')
			throw new ArahantWarning("Project may not have billable parent if being billed externally.");

		BProject parent = new BProject(primaryParentId);

		if (parent.getBillingRate() != getBillingRate())
			throw new ArahantWarning("Billing rate must match parent project's billing rate of " + MoneyUtils.formatMoney(parent.getBillingRate()) + ".");

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		//remove any old primary parent
		HibernateCriteriaUtil<ProjectViewJoin> hcu = hsu.createCriteria(ProjectViewJoin.class);
		hcu.eq(ProjectViewJoin.PRIMARY_BILLING, 'Y');
		hcu.joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project);

		ProjectViewJoin p = hcu.first();
		if (p != null) {
			p.setPrimaryBilling('N');
			hsu.saveOrUpdate(p);
		}

		hcu = hsu.createCriteria(ProjectViewJoin.class);
		hcu.joinTo(ProjectViewJoin.PARENT).joinTo(ProjectView.PROJECT).eq(Project.PROJECTID, primaryParentId);
		hcu.joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project);

		ProjectViewJoin pvj = hcu.first();

		if (pvj == null)
			throw new ArahantWarning("Parent project is missing!");

		pvj.setPrimaryBilling('Y');

		hsu.saveOrUpdate(pvj);
	}

	public void setPromisedDate(int promisedDate) {
		project.setDatePromised(promisedDate);
	}

	public void setRouteId(String routeId) {
		Route rt = ArahantSession.getHSU().get(Route.class, routeId);
		if (rt != null) {
			project.setRouteStop(rt.getInitalRouteStop());
			project.setProjectStatus(rt.getInitialProjectStatus());
		}
	}

	protected static String getNameFromId(String id) {
		id = IDGenerator.shrinkKey(id);
		if (id.indexOf('-') != -1)
			id = id.substring(id.indexOf('-') + 1);

		if (id.length() < 8)
			return String.format("%8s", id);
		return id;
	}

	protected String makeProjectName() {
		//		TODO: set up an extension point here for different generators
		return getNameFromId(project.getProjectId());
	}

	private void internalLoad(final String key) throws ArahantException {
		project = ArahantSession.getHSU().get(Project.class, key);
		if (project == null)
			throw new ArahantWarning("Bad project key passed to server '" + key + "'");
		if (project.getRouteStop() != null)
			routeStopId = project.getRouteStop().getRouteStopId();
		if (project.getProjectStatus() != null)
			lastStatusId = project.getProjectStatus().getProjectStatusId();
		saveOriginalData(project);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		if (isEmpty(project.getProjectName()))
			setProjectName(makeProjectName());

		checkForTransition();

		// make sure there is a date/time assigned for person assignments
		/*
		if (getSingleEmployeeJoin() != null && getSingleEmployeeJoin().getDateAssigned() == 0) {
			getSingleEmployeeJoin().setDateAssigned(DateUtils.now());
			getSingleEmployeeJoin().setTimeAssigned(DateUtils.nowTime());
		}
		 */

		sendNotifications();

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.saveOrUpdate(project);
		hsu.update(updates);

		checkForHistory();
	}

	private String fixName(String x) {
		StringBuilder ret = new StringBuilder();

		for (int loop = 0; loop < x.length(); loop++) {
			char c = x.charAt(loop);
			char u = x.toUpperCase().charAt(loop);
			if (c == u)
				ret.append(" ").append(c);
			else
				ret.append(c);
		}
		return ret.toString();
	}

	private void sendNotifications() {
		try {
			if (BProperty.getBoolean("ProjectNotifications")) {
				List<ChangedFields> fields = compareChangesReturnOldNewValues(project);

				String changes = "";
				for (ChangedFields cf : fields)
					if (!cf.getFieldName().equalsIgnoreCase("getProjectTypeId")) {
						String name = fixName(cf.getFieldName());

						if (cf.getFieldName().equalsIgnoreCase("getEmployee"))
							name = "Managing Employee";
						changes += "Field Name: " + name.substring(3).trim() + "\n";
						changes += "Old value: " + cf.getPrevValue() + "\n";
						changes += "New value: " + cf.getNewValue() + "\n";
						changes += "-------------------------------\n";
						changes += "\n";
					}

				changes = changes.trim();
				changes = changes.substring(0, changes.length() - 1) + ".";
				//if there are any messages with the subject line in the last 30 minutes, don't send

				Calendar recentTime = Calendar.getInstance();
				recentTime.add(Calendar.MINUTE, -30);
				String emailBody = "";
				emailBody += ("\n\nProject: " + project.getProjectName().trim() + " for " + project.getRequestingOrgGroup().getName());
				emailBody += ("\nSummary: " + project.getDescription());
				emailBody += ("\nDetail: " + project.getDetailDesc());
				emailBody += ("\n");
				emailBody += ("");
				emailBody += ("\n" + changes);
				emailBody += ("");
				emailBody += ("\n");
				//System.out.println("Email This " + emailBody);
				sendToAssignees("Project was updated " + project.getProjectName().trim(), "Project " + project.getProjectName().trim() + " was updated.  The items that changed were " + emailBody);
			}
		} catch (Exception ignored) {
		}
	}

	List<String> getAllInBillableTree() {
		List<String> l = new LinkedList<String>();
		l.add(project.getProjectId());
		for (Project p : ArahantSession.getHSU().createCriteria(Project.class).eq(Project.BILLABLE, 'I').joinTo(Project.VIEWS).joinTo(ProjectView.VIEW_JOINS_WHERE_CHILD).eq(ProjectViewJoin.PRIMARY_BILLING, 'Y').joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).list())
			l.addAll(new BProject(p).getAllInBillableTree());
		return l;
	}

	/**
	 * @throws ArahantException
	 *
	 */
	@SuppressWarnings({"unchecked", "element-type-mismatch"})
	private void checkForTransition() throws ArahantException {
		// check for route transition
		if (project.getRouteStop() != null && project.getRouteStop().getRouteStopId().equals(routeStopId)) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			// we currently have the same route stop as the one with which we were loaded, so
			// check for a route path transition
			final RoutePath rp = hsu.createCriteria(RoutePath.class).eq(RoutePath.FROM_ROUTE_STOP, project.getRouteStop()).eq(RoutePath.FROM_PROJECT_STATUS, project.getProjectStatus()).first();

			if (rp != null) {

				checkForHistory();

				// apply the new route stop
				project.setRouteStop(rp.getToRouteStop());
				project.setProjectStatus(rp.getToProjectStatus());

				List<ArahantBean> ups = new ArrayList<>(updates.size());
				ups.addAll(updates);

				//check the current person, if they aren't in the new org group, clear them
				if (project.getRouteStop().getOrgGroup() != null) {

					List<ProjectEmployeeJoin> pejList = hsu.createCriteria(ProjectEmployeeJoin.class)
							.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
							.eq(ProjectShift.PROJECT, project)
							.joinTo(ProjectEmployeeJoin.PERSON)
							.joinTo(Person.ORGGROUPASSOCIATIONS)
							.ne(OrgGroupAssociation.ORGGROUP, project.getRouteStop().getOrgGroup())
							.list();

					for (ProjectEmployeeJoin pej : pejList) {
						HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class);

						hcu.eq(OrgGroupAssociation.ORGGROUP, project.getRouteStop().getOrgGroup());
						hcu.eq(OrgGroupAssociation.PERSON, pej.getPerson());

						if (!hcu.exists()) {
//  XXYY							BProject.createAssignmentHistory(pej.getPerson().getPersonId(), pej.getProject().getProjectId(), "D");
							hsu.delete(pej);
						}
					}

					for (Object o : ups)
						if (o instanceof ProjectEmployeeJoin) {
							ProjectEmployeeJoin pej = (ProjectEmployeeJoin) o;
							HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class);

							hcu.eq(OrgGroupAssociation.ORGGROUP, project.getRouteStop().getOrgGroup());
							hcu.eq(OrgGroupAssociation.PERSON, pej.getPerson());

							if (!hcu.exists())
								updates.remove(o);
						}
				} else {
					List<ProjectEmployeeJoin> pejList = hsu.createCriteria(ProjectEmployeeJoin.class)
							.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
							.eq(ProjectShift.PROJECT, project)
							.joinTo(ProjectEmployeeJoin.PERSON)
							.joinTo(Person.ORGGROUPASSOCIATIONS)
							.ne(OrgGroupAssociation.ORGGROUP, project.getRequestingOrgGroup())
							.list();

					for (ProjectEmployeeJoin pej : pejList) {
						HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class);

						hcu.eq(OrgGroupAssociation.ORGGROUP, project.getRequestingOrgGroup());
						hcu.eq(OrgGroupAssociation.PERSON, pej.getPerson());

						if (!hcu.exists()) {
		//  XXYY					BProject.createAssignmentHistory(pej.getPerson().getPersonId(), pej.getProject().getProjectId(), "D");
							hsu.delete(pej);
						}
					}

					for (Object o : ups)
						if (o instanceof ProjectEmployeeJoin) {
							ProjectEmployeeJoin pej = (ProjectEmployeeJoin) o;
							HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class);

							hcu.eq(OrgGroupAssociation.ORGGROUP, project.getRequestingOrgGroup());
							hcu.eq(OrgGroupAssociation.PERSON, pej.getPerson());

							if (!hcu.exists())
								updates.remove(o);
						}
				}

				//autoAssignToSupervisors=true means that when a project is routed, current assignments should be dropped and all supervisors of the group should be assigned (10,000)

				if (project.getRouteStop().getAutoAssignSupervisor() == 'Y') {
					for (Object o : ups)
						if (o instanceof ProjectEmployeeJoin)
							updates.remove(o);

					BProject.unassignAllAssignmentHistory(project.getProjectId());
					hsu.createCriteria(ProjectEmployeeJoin.class)
							.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
							.eq(ProjectShift.PROJECT, project)
							.delete();
/* XXYY
					if (project.getRouteStop().getOrgGroup() != null) {
						for (OrgGroupAssociation oga : project.getRouteStop().getOrgGroup().getOrgGroupAssociations())
							if (oga.getPrimaryIndicator() == 'Y')
								assignPerson(oga.getPersonId(), 10000, true);
					} else
						for (OrgGroupAssociation oga : project.getRequestingOrgGroup().getOrgGroupAssociations())
							if (oga.getPrimaryIndicator() == 'Y')
								assignPerson(oga.getPersonId(), 10000, true);
 */
				}
			}
		}

		// check for need of history record
		//this.checkForHistory();
	}

	private void checkForHistory() throws ArahantException {
		boolean routeChanged =
				(routeStopId.length() > 0 && project.getRouteStop() == null)
				|| (routeStopId.length() == 0 && project.getRouteStop() != null)
				|| (project.getRouteStop() != null && !project.getRouteStop().getRouteStopId().equals(routeStopId));
		boolean statusChanged =
				(lastStatusId.length() > 0 && project.getProjectStatus() == null)
				|| (lastStatusId.length() == 0 && project.getProjectStatus() != null)
				|| (project.getProjectStatus() != null && !project.getProjectStatus().getProjectStatusId().equals(lastStatusId));

		int nowTime = DateUtils.nowTime();
		int date = DateUtils.now();
		// create a project history if needed
		if (routeChanged || statusChanged) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			final BProjectHistory hist = new BProjectHistory();

			hist.create();

			hist.setDateChanged(date);
			hist.setTimeChanged(nowTime);

			hist.setProject(project);
			hist.setAssignedBy(hsu.getCurrentPerson());

			// set route from/to
			RouteStop routeStop = null;
			if (routeStopId.length() > 0)
				routeStop = hsu.get(RouteStop.class, routeStopId);
			hist.setFromRouteStop(routeStop);
			hist.setToRouteStop(project.getRouteStop());

			// set status from/to
			//RKK Schema currently says must have a status, which seems wrong for history at inception but ...
			ProjectStatus projectStatus = project.getProjectStatus()/*
					 * null
					 */;
			if (lastStatusId.length() > 0)
				projectStatus = hsu.get(ProjectStatus.class, lastStatusId);
			hist.setFromProjectStatus(projectStatus);
			hist.setToProjectStatus(project.getProjectStatus());

			hist.insert();
		}
		lastStatusId = project.getProjectStatus().getProjectStatusId(); //reset last project status
	}

	/**
	 * @return @see com.arahant.beans.Project#getBillable()
	 */
	public char getBillable() {
		return project.getBillable();
	}

	public char getResolvedBillable() {
		if (project.getBillable() != 'I')
			return project.getBillable();

		//find the parent's billable
		return new BProject(getPrimaryParentId()).getResolvedBillable();
	}

	/**
	 * @return @see com.arahant.beans.Project#getBillingMethod()
	 */
	public Integer getBillingMethod() {
		return project.getBillingMethod();
	}

	/**
	 * @return @see com.arahant.beans.Project#getBillingRate()
	 */
	public float getBillingRate() {
		return project.getBillingRate();
	}

	/**
	 * @return @see com.arahant.beans.Project#getDateReported()
	 */
	public int getDateReported() {
		return project.getDateReported();
	}

	/**
	 * @return @see com.arahant.beans.Project#getDescription()
	 */
	public String getDescription() {
		return project.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.Project#getDetailDesc()
	 */
	public String getDetailDesc() {
		return project.getDetailDesc();
	}

	/**
	 * @return @see com.arahant.beans.Project#getDollarCap()
	 */
	public double getDollarCap() {
		return project.getDollarCap();
	}

	/**
	 * @return @see com.arahant.beans.Project#getNextBillingDate()
	 */
	public Integer getNextBillingDate() {
		return project.getNextBillingDate();
	}

	/**
	 * @return @see com.arahant.beans.Project#getProjectId()
	 */
	public String getProjectId() {
		return project.getProjectId();
	}

	/**
	 * @return @see com.arahant.beans.Project#getProjectName()
	 */
	public String getProjectName() {
		return getName();
	}

	/**
	 * @return @see com.arahant.beans.Project#getReference()
	 */
	public String getReference() {
		return project.getReference();
	}

	/**
	 * @return @see com.arahant.beans.Project#getRequesterName()
	 */
	public String getRequesterName() {
		return project.getRequesterName();
	}

	/**
	 * @param billable
	 * @see com.arahant.beans.Project#setBillable(char)
	 */
	public void setBillable(final char billable) {
		project.setBillable(billable);
	}

	/**
	 * @param billingMethod
	 */
	public void setBillingMethod(final Integer billingMethod) {
		project.setBillingMethod(billingMethod);
	}

	/**
	 * @param billingRate
	 * @see com.arahant.beans.Project#setBillingRate(float)
	 */
	public void setBillingRate(final float billingRate) {
		project.setBillingRate(billingRate);
	}

	/**
	 * @param dateReported
	 * @see com.arahant.beans.Project#setDateReported(int)
	 */
	public void setDateReported(final int dateReported) {
		project.setDateReported(dateReported);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.Project#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		project.setDescription(description);
	}

	/**
	 * @param detailDesc
	 * @see com.arahant.beans.Project#setDetailDesc(java.lang.String)
	 */
	public void setDetailDesc(final String detailDesc) {
		project.setDetailDesc(detailDesc);
	}

	/**
	 * @param dollarCap
	 * @see com.arahant.beans.Project#setDollarCap(double)
	 */
	public void setDollarCap(final double dollarCap) {
		project.setDollarCap(dollarCap);
	}

	public void setNextBillingDate(final Integer nextBillingDate) {
		project.setNextBillingDate(nextBillingDate);
	}

	/**
	 * @param projectId
	 * @see com.arahant.beans.Project#setProjectId(java.lang.String)
	 */
	public void setProjectId(final String projectId) {
		project.setProjectId(projectId);
	}

	/**
	 * @param projectName
	 * @see com.arahant.beans.Project#setProjectName(java.lang.String)
	 */
	public void setProjectName(final String projectName) {
		project.setProjectName(projectName);
	}

	/**
	 * @param reference
	 * @see com.arahant.beans.Project#setReference(java.lang.String)
	 */
	public void setReference(final String reference) {
		project.setReference(reference);
	}

	/**
	 * @param requesterName
	 * @see com.arahant.beans.Project#setRequesterName(java.lang.String)
	 */
	public void setRequesterName(final String requesterName) {
		project.setRequesterName(requesterName);
	}

	/**
	 * @param ongoing
	 * @see com.arahant.beans.Project#setOngoing(char)
	 */
	public void setOngoing(final char ongoing) {
		project.setOngoing(ongoing);
	}

	/**
	 * @param current
	 * @param comment
	 * @param internalFlag
	 * @throws ArahantException
	 */
	public String addComment(final BPerson current, final String comment, final boolean internalFlag, final String shiftId) throws ArahantException {
		final BProjectComment pc = new BProjectComment();

		pc.create();

		pc.setPerson(current);
		pc.setProjectShiftId(shiftId);

		pc.setCommentTxt(comment);
		pc.setInternal(internalFlag ? 'Y' : 'N');

		pc.insert();

		return pc.getCommentId();
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		if (ids != null)
			for (final String element : ids)
				new BProject(element).delete();
	}

	public BProjectComment[] listComments() {
		return BProjectComment.makeArray(ArahantSession.getHSU()
				.createCriteria(ProjectComment.class)
				.orderBy(ProjectComment.DATEENTERED)
				.joinTo(ProjectComment.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.list());
	}

	/**
	 * @return @see com.arahant.beans.Project#getAllEmployees()
	 */
	public char getAllEmployees() {
		return project.getAllEmployees();
	}

	/**
	 * @param allEmployees
	 * @see com.arahant.beans.Project#setAllEmployees(char)
	 */
	public void setAllEmployees(final char allEmployees) {
		project.setAllEmployees(allEmployees);
	}

	/**
	 * @param projectStatusId
	 */
	public void setProjectStatusId(final String projectStatusId) {
		project.setProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, projectStatusId));
	}

	/**
	 * @param projectTypeId
	 */
	public void setProjectTypeId(final String projectTypeId) {
		project.setProjectType(ArahantSession.getHSU().get(ProjectType.class, projectTypeId));
	}

	public String getProjectSubtypeId() {
		return project.getProjectSubtypeId();
	}

	public void setProjectSubtypeId(String projectSubtypeId) {
		project.setProjectSubtypeId(projectSubtypeId);
	}

	/**
	 * @param projectCategoryId
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		project.setProjectCategory(ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
	}

	/**
	 * @param requestingCompanyId
	 */
	public void setRequestingOrgGroupId(final String requestingCompanyId) {
		project.setRequestingOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, requestingCompanyId));
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		project.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param projectSponsorId
	 */
	public void setSponsorId(final String projectSponsorId) {
		project.setEnteredByPerson(ArahantSession.getHSU().get(Person.class, projectSponsorId));
	}

	/**
	 * @param productId
	 */
	public void setProductServiceId(final String productId) {
		project.setProductService(ArahantSession.getHSU().get(ProductService.class, productId));
	}

	public static BProject[] search(final HibernateSessionUtil hsu, final String summary, final String category,
			final String status, final String type, final String companyId, final String projectName, final int fromDate, final int toDate,
			final String personId, final boolean quickList, final String user, final String extReference, final String projectSummary, final int max) {
		return search(hsu, summary, category, status, type, companyId, projectName, fromDate,
				toDate, personId, quickList, user, extReference, projectSummary, max, false);
	}

	public static BProject[] search(final HibernateSessionUtil hsu, final String summary, final String category,
			final String status, final String type, final String companyId, final String projectName, final int fromDate, final int toDate,
			final String personId, final boolean quickList, final String user, final String extReference, final String projectSummary, final int max,
			final boolean excludeInternalBill) {
		int typeflag = 1;
		if (!isEmpty(status))
			typeflag = 3;
		return search(hsu, summary, category, status, type, companyId, projectName, fromDate,
				toDate, personId, quickList, user, extReference, projectSummary, typeflag, max, excludeInternalBill, Project.DESCRIPTION);
	}

    /**
     *
     * @param hsu
     * @param summary
     * @param category
     * @param status
     * @param type
     * @param companyId
     * @param projectName
     * @param fromDate
     * @param toDate
     * @param personId
     * @param quickList
     * @param user
     * @param extReference
     * @param projectSummary
     * @param statusType
     * @param max
     * @param excludeInternalBill
     * @param sortOrder
     * @return
     */
	public static BProject[] search(final HibernateSessionUtil hsu, final String summary, final String category,
			final String status, final String type, final String companyId, final String projectName, final int fromDate, final int toDate,
			final String personId, final boolean quickList, final String user, final String extReference, final String projectSummary,
			int statusType, final int max, final boolean excludeInternalBill, final String sortOrder) {
		List<Project> ptl;

		if (quickList) //DCM get all the quicklist projects
		
			ptl = getProjectQuickList(user, hsu, personId);
		else {
			final HibernateCriteriaUtil<Project> criteria = hsu.createCriteria(Project.class);

			criteria.orderBy(sortOrder);

			if (max > 0)
				criteria.setMaxResults(max);

			if (!isEmpty(extReference))
				criteria.like(Project.REFERENCE, extReference);
			if (!isEmpty(projectSummary))
				criteria.like(Project.DESCRIPTION, projectSummary);

			if (excludeInternalBill)
				criteria.ne(Project.BILLABLE, 'I');

			if (!isEmpty(summary))
				criteria.like(Project.DESCRIPTION, summary);

			if (!isEmpty(category))
				criteria.eq(Project.PROJECTCATEGORY, hsu.get(ProjectCategory.class, category));


			switch (statusType) {
				case 3:
					criteria.eq(Project.PROJECTSTATUS, hsu.get(ProjectStatus.class, status));
					break;
				case 1:
					criteria.joinTo(Project.PROJECTSTATUS).ne(ProjectStatus.ACTIVE, 'N');
					break;
				case 2:
					criteria.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'N');
					break;

			}

			if (!isEmpty(type))
				criteria.eq(Project.PROJECTTYPE, hsu.get(ProjectType.class, type));

			if (!isEmpty(companyId))
				criteria.joinTo(Project.REQUESTING_ORG_GROUP).in(OrgGroup.ORGGROUPID, new BOrgGroup(companyId).getAllGroupsForCompany());


			if (!isEmpty(projectName))
				criteria.like(Project.PROJECTNAME, projectName);

			if (fromDate > 0)
				criteria.ge(Project.DATEREPORTED, fromDate);

			if (toDate > 0)
				criteria.le(Project.DATEREPORTED, toDate);


			ptl = criteria.list();
		}

		return makeArray(ptl);
		/*
		 * final BProject[] ptt=new BProject[ptl.size()];
		 *
		 * final Iterator ptlItr=ptl.iterator();
		 *
		 * for (int loop=0;loop<ptt.length;loop++) ptt[loop]=new
		 * BProject((Project)ptlItr.next());
		 *
		 * return ptt;
         *
		 */
	}

	public static BSearchOutput<BProject> search(BSearchMetaInput metaInput, final String category,
			final String status, final String type, final String companyId, final String projectName, final int fromDate, final int toDate,
			final String user, final String extReference, final String projectSummary,
			int statusType, String excludeId, final int max) {


		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final HibernateCriteriaUtil<Project> criteria = hsu.createCriteria(Project.class);

		if (max > 0)
            criteria.setMaxResults(max);

		if (excludeId != null)
			criteria.ne(Project.PROJECTID, excludeId.trim());

		criteria.like(Project.REFERENCE, extReference);
		criteria.like(Project.DESCRIPTION, projectSummary);

		if (!isEmpty(category))
			criteria.eq(Project.PROJECTCATEGORY, hsu.get(ProjectCategory.class, category));


		switch (statusType) {
			case 3:
				criteria.eq(Project.PROJECTSTATUS, hsu.get(ProjectStatus.class, status));
				break;
			case 1:
				char[] chStatus = {'Y', 'O'};
				criteria.joinTo(Project.PROJECTSTATUS).in(ProjectStatus.ACTIVE, chStatus);
				break;
			case 2:
				criteria.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'N');
				break;

		}

		if (!isEmpty(type))
			criteria.eq(Project.PROJECTTYPE, hsu.get(ProjectType.class, type));

		if (!isEmpty(companyId))
			criteria.joinTo(Project.REQUESTING_ORG_GROUP).in(OrgGroup.ORGGROUPID, new BOrgGroup(companyId).getAllGroupsForCompany());


		if (!isEmpty(projectName))
			criteria.like(Project.PROJECTNAME, projectName);

		if (fromDate > 0)
			criteria.ge(Project.DATEREPORTED, fromDate);

		if (toDate > 0)
			criteria.le(Project.DATEREPORTED, toDate);

		//projectName, description, dateReportedFormatted, reference, requestingNameFormatted
		switch (metaInput.getSortType()) {
			case 1:
				criteria.orderBy(Project.PROJECTNAME);
				break;
			case 2:
				criteria.orderBy(Project.DESCRIPTION);
				break;
			case 3:
				criteria.orderBy(Project.DATEREPORTED);
				break;
			case 4:
				criteria.orderBy(Project.REFERENCE);
				break;
			case 5:
				criteria.orderBy(Project.REQUESTERNAME);
				break;
			case 6:
				criteria.orderBy(Project.PROJECTSTATUS);
				break;
			default:
				criteria.orderBy(Project.PROJECTNAME);

		}

		return makeSearchOutput(metaInput, criteria);
		/*
		 * final BProject[] ptt=new BProject[ptl.size()];
		 *
		 * final Iterator ptlItr=ptl.iterator();
		 *
		 * for (int loop=0;loop<ptt.length;loop++) ptt[loop]=new
		 * BProject((Project)ptlItr.next());
		 *
		 * return ptt;
         *
		 */
	}

	public static BSearchOutput<BProject> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Project> hcu) {
		BSearchOutput<BProject> ret = new BSearchOutput<BProject>(searchMeta);

		HibernateScrollUtil<Project> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BProject.makeArray(scr));

		return ret;
	}

	/**
	 * @param user
	 * @param hsu
	 * @return
	 */
	private static List<Project> getProjectQuickList(final String user, final HibernateSessionUtil hsu, final String personId) {

		//DCM get all the quicklist projects
		final List<Project> ptl = new LinkedList<Project>();

		final Date now = new Date();
		final long weekBack = now.getTime() - 7 * 24 * 60 * 60 * 1000;
		final Date lastWeek = new Date(weekBack);

		final int aWeekAgo = DateUtils.getDate(lastWeek);

		final Person p = hsu.get(Person.class, personId);

		HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class).distinct().orderBy(Project.PROJECTNAME).setMaxResults(15).ne(Project.ALLEMPLOYEES, 'Y');
		hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
		hcu.joinTo(Project.TIMESHEETS).eq(Timesheet.PERSON, p).ge(Timesheet.WORKDATE, aWeekAgo).orderByDesc(Timesheet.WORKDATE);
		ptl.addAll(hcu.list());


		//add the last set the user had
		final int max = 15 - ptl.size();

		if (max > 0) {
//					add the ones that are in the user's status			
			hcu = hsu.createCriteria(Project.class, "ProjectAlias").distinct().setMaxResults(max).orderBy(Project.PROJECTNAME);
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
			hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN).eq(ProjectEmployeeJoin.PERSON, p);
			ptl.addAll(hcu.list());

		}

		if (ptl.size() < 15) {

			final List<String> ids = new LinkedList<String>();
			for (int loop = 0; loop < ptl.size(); loop++)
				ids.add((ptl.get(loop)).getProjectId());
			hcu = hsu.createCriteria(Project.class).distinct().orderBy(Project.PROJECTNAME).notIn(Project.PROJECTID, ids).setMaxResults(15 - ptl.size()).ne(Project.ALLEMPLOYEES, 'Y');
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
			hcu.joinTo(Project.TIMESHEETS).eq(Timesheet.PERSON, p).orderByDesc(Timesheet.WORKDATE);

			ptl.addAll(hcu.list());
		}

		//add the sick, etc types
		ptl.addAll(hsu.createCriteria(Project.class).orderBy(Project.PROJECTNAME).setMaxResults(15).eq(Project.ALLEMPLOYEES, 'Y').joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y').list());
		return ptl;
	}

	public String getProjectStatusId() {
		if (project.getProjectStatus() == null)
			return "";
		return project.getProjectStatus().getProjectStatusId();
	}

	public String getProjectCategoryId() {
		return project.getProjectCategory().getProjectCategoryId();
	}

	public String getProjectTypeId() {
		return project.getProjectType().getProjectTypeId();
	}

	public String getProjectStatusCode() {
		return project.getProjectStatus().getCode();
	}

	public String getProjectCategoryCode() {
		return project.getProjectCategory().getCode();
	}

	public String getProjectTypeCode() {
		return project.getProjectType().getCode();
	}

	public String getCompanyId() {
		return project.getRequestingOrgGroup().getOrgGroupId();
	}

	public String getCompanyName() {
		try {
			return new BOrgGroup(project.getRequestingOrgGroup()).getCompanyName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getProductId() {
		if (project.getProductService() != null)
			return project.getProductService().getProductId();
		return "";
	}

	public String getProductName() {
		if (project.getProductService() != null)
			return project.getProductService().getDescription();
		return "";
	}

	public String getEmployeeName() {
		if (project.getEmployee() != null)
			return project.getEmployee().getNameLFM();
		return "";
	}

	public String getEmployeeId() {
		if (project.getEmployee() != null)
			return project.getEmployee().getPersonId();
		return "";
	}

	/**
	 * @param orgGroupId
	 */
	public void setOrgGroupId(final String orgGroupId) {
		project.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	/**
	 * @param companyId
	 * @return
	 */
	public static BProject[] list(final HibernateSessionUtil hsu, final String companyId) {
		return makeArray(hsu.createCriteria(Project.class).orderBy(Project.DESCRIPTION).joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.ORGGROUPID, companyId).list());
	}

	/**
	 * @param companyId
	 * @return
	 */
	public static BProject[] listCreatedStandardProjects(final HibernateSessionUtil hsu, final String companyId, final int max) {
		HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class).setMaxResults(max).orderBy(Project.DESCRIPTION);
		hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
		hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.ORGGROUPID, companyId);
		return makeArray(hcu.list());
	}

	/**
	 * This is used by the AQDev import and should go away when we are done with
	 * that It's a dangerous function to use, since the number of projects that
	 * could be loaded in a production system could blow out the memory.
	 *
	 * @param hsu
	 */
	public static BProject[] list(final HibernateSessionUtil hsu) {
		return makeArray(hsu.getAll(Project.class));
	}
	
	/**
	 * @param routeStopId
	 */
	public void setRouteStopId(final String routeStopId) {
		project.setRouteStop(ArahantSession.getHSU().get(RouteStop.class, routeStopId));
	}

	/**
	 * @param currentPersonId
	 */
	public void setCurrentPersonId(final String currentPersonId) {

		//This is assuming this is only person allowed
/*  XXYY
		if (!isEmpty(getCurrentPersonId()) && !getCurrentPersonId().equals(currentPersonId))
			removeAssignment(getCurrentPersonId());

		if (!isEmpty(currentPersonId))
			assignPerson(currentPersonId, 10000, true);

		BProject.unassignAllAssignmentHistory(project.getProjectId());

		ArahantSession.getHSU().createCriteria(ProjectEmployeeJoin.class)
				.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.delete();

		//needed to add a flush here so the delete is applied
		ArahantSession.getHSU().flush();
 */
	}

	public String getRouteId() {
		try {
			return project.getRouteStop().getRoute().getRouteId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getRouteName() {
		try {
			return project.getRouteStop().getRoute().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getRouteStopId() {
		try {
			return project.getRouteStop().getRouteStopId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getRouteStopName() {
		try {
			return project.getRouteStop().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getCurrentPersonId() {
		try {
			return getSingleCurrentAssignedPerson().getPersonId();
		} catch (final Exception e) {
			return "";
		}
	}

	public BPerson getCurrentPerson() {
		if (getSingleCurrentAssignedPerson() == null)
			return null;

		return new BPerson(getSingleCurrentAssignedPerson());
	}

	public String getCurrentPersonName() {
		try {
			return getSingleCurrentAssignedPerson().getNameLFM();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getOrgGroupName() {
		try {
			BOrgGroup borg = new BOrgGroup(project.getRequestingOrgGroup());
			if (borg.isCompany())
				return "";
			return borg.getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getDateTimeAssignedFormatted() {
		return DateUtils.getDateTimeFormatted(getSingleEmployeeJoin().getDateAssigned(), getSingleEmployeeJoin().getTimeAssigned());
	}

	/**
	 * @param newProjectStatusId
	 * @return
	 */
	public BRoutePath checkForRouteStop(final String newProjectStatusId) {
		final HibernateCriteriaUtil<RoutePath> hcu = ArahantSession.getHSU().createCriteria(RoutePath.class).eq(RoutePath.FROM_ROUTE_STOP, project.getRouteStop()).joinTo(RoutePath.FROM_PROJECT_STATUS).eq(ProjectStatus.PROJECTSTATUSID, newProjectStatusId);
		final RoutePath rp = hcu.first();
		if (rp != null)
			return new BRoutePath(rp);
		return null;
	}

	static BProject[] makeArray(HibernateScrollUtil<Project> scr) {
		List<Project> l = new ArrayList<Project>();
		while (scr.next())
			l.add(scr.get());
		return BProject.makeArray(l);
	}

	static public BProject[] makeArray(HibernateScrollUtil<Project> scr, int max) {
		List<Project> l = new ArrayList<Project>();
		while (scr.next() && max-- > 0)
			l.add(scr.get());
		return BProject.makeArray(l);
	}

	static public BProject[] makeArray(final Collection<Project> l) {
		final BProject[] ret = new BProject[l.size()];
		int i = 0;
		for (Project p : l)
			ret[i++] = new BProject(p);
		return ret;
	}

	public static BProject[] searchOrgGroupProjectsClient(final String orgGroupId, final String projectSummary,
			final String extReference, final int fromDate, final int toDate, final String[] projectCategoryId,
			final String[] projectTypeId, final String[] projectStatusId,
			final boolean showAssigned, boolean showUnassigned, final int max) {
		final HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class, "proj").setMaxResults(max);

		// or the following with the requested company
		BOrgGroup borg = new BOrgGroup(orgGroupId);

		if (showUnassigned && showAssigned)
			hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.ORGGROUPID, borg.orgGroup.getOwningCompany().getOrgGroupId());
		else if (showAssigned) {
			HibernateCriteriaUtil hc2 = hcu.leftJoinTo(Project.CURRENT_ROUTE_STOP).leftJoinTo(RouteStop.ORG_GROUP);
			HibernateCriterionUtil hcri1 = hc2.makeCriteria().eq(OrgGroup.ORGGROUPID, orgGroupId);
			HibernateCriterionUtil hcri2 = hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
			HibernateCriterionUtil hcri3 = hc2.makeCriteria().eq("proj." + Project.REQUESTING_ORG_GROUP, borg.orgGroup.getOwningCompany());
			HibernateCriterionUtil hcriand = hc2.makeCriteria().and(hcri2, hcri3);
			HibernateCriterionUtil hcrior = hc2.makeCriteria().or(hcriand, hcri1);
			hcrior.add();
		} else if (showUnassigned) {
			HibernateCriteriaUtil hc2 = hcu.joinTo(Project.CURRENT_ROUTE_STOP).isNotNull(RouteStop.ORG_GROUP).ne(RouteStop.ORG_GROUP_ID, orgGroupId).joinTo(RouteStop.ROUTE).joinTo(Route.ROUTE_STOPS).leftJoinTo(RouteStop.ORG_GROUP);
			HibernateCriterionUtil hcri1 = hc2.makeCriteria().eq(OrgGroup.ORGGROUPID, orgGroupId);
			HibernateCriterionUtil hcri2 = hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
			HibernateCriterionUtil hcri3 = hc2.makeCriteria().eq("proj." + Project.REQUESTING_ORG_GROUP, borg.orgGroup.getOwningCompany());
			HibernateCriterionUtil hcriand = hc2.makeCriteria().and(hcri2, hcri3);
			HibernateCriterionUtil hcrior = hc2.makeCriteria().or(hcriand, hcri1);
			hcrior.add();
		} else
			return new BProject[0];

		hcu.orderBy(Project.PRIORITY_CLIENT).orderBy(Project.PROJECTNAME);


		if (!isEmpty(projectSummary))
			hcu.like(Project.DESCRIPTION, projectSummary);

		if (!isEmpty(extReference))
			hcu.like(Project.REFERENCE, extReference);

		if (fromDate > 0)
			hcu.ge(Project.DATEREPORTED, fromDate);

		if (toDate > 0)
			hcu.le(Project.DATEREPORTED, toDate);

		if (projectCategoryId.length > 0)
			hcu.joinTo(Project.PROJECTCATEGORY).in(ProjectCategory.PROJECTCATEGORYID, projectCategoryId);

		if (projectTypeId.length > 0)
			hcu.joinTo(Project.PROJECTTYPE).in(ProjectType.PROJECTTYPEID, projectTypeId);

		if (projectStatusId.length > 0)
			hcu.joinTo(Project.PROJECTSTATUS).in(ProjectStatus.PROJECTSTATUSID, projectStatusId);
		else
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');

		//if (!isEmpty(companyId))
		//	hcu.eq(Project.REQUESTING_ORG_GROUP, ArahantSession.getHSU().get(CompanyBase.class,companyId));

		return BProject.makeArray(hcu.list());
	}

	public String getCurrentOrgGroupName() {
		try {
			if (project.getRouteStop() == null || project.getRouteStop().getOrgGroup() == null)
				return "Requesting Group";

			BOrgGroup borg = new BOrgGroup(project.getRouteStop().getOrgGroup());
			if (borg.isCompany())
				return "";

			return borg.getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getOrgGroupId() {
		try {
			return project.getRouteStop().getOrgGroup().getOrgGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkDoneForPersonName() {
		try {
			return project.getDoneForPerson().getNameLFM();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getWorkDoneForPersonSSN() {
		try {
			return project.getDoneForPerson().getUnencryptedSsn();
		} catch (final Exception e) {
			return "";
		}
	}

	public BProjectComment[] getProjectComments(String shiftId) {
		HibernateCriteriaUtil<ProjectComment> crit = ArahantSession.getHSU()
				.createCriteria(ProjectComment.class)
				.orderByDesc(ProjectComment.COMMENTID)
				.joinTo(ProjectComment.PROJECTSHIFT);
		if (shiftId != null && !shiftId.isEmpty())
			crit.eq(ProjectShift.PROJECTSHIFTID, shiftId);
		else
			crit.eq(ProjectShift.PROJECT, project);
		List<ProjectComment> lst = crit.list();
		return BProjectComment.makeArray(lst);
	}

	/**
	 * This is the time the project was created.
	 */
	public int getTimeReported() {
		return project.getTimeReported();
	}

	public String getCurrentPersonFirstName() {
		try {
			return getSingleCurrentAssignedPerson().getFname();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getCurrentPersonLastName() {
		try {
			return getSingleCurrentAssignedPerson().getLname();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @param dateCompleted
	 */
	public void setDateCompleted(final int dateCompleted) {
		project.setDateCompleted(dateCompleted);
	}

	/**
	 * @param timeCompleted
	 */
	public void setTimeCompleted(final int timeCompleted) {
		project.setTimeCompleted(timeCompleted);
	}

	/**
	 * @param timeRequested
	 */
	public void setTimeReported(final int timeRequested) {
		project.setTimeReported(timeRequested);
	}

	public String getDoneForPersonFirstName() {
		try {
			return new BPerson(project.getDoneForPersonId()).getFirstName();
		} catch (final Exception ee) {
			return "";
		}
	}

	public String getDoneForPersonLastName() {
		try {
			return new BPerson(project.getDoneForPersonId()).getLastName();
		} catch (final Exception ee) {
			return "";
		}
	}

	public void setDoneForPersonId(final String key) {
		project.setDoneForPerson(ArahantSession.getHSU().get(Person.class, key));
	}

	public static BProject[] search(final String employeeId, final String projectName, final String statusId, final int cap) {
		final HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).eq(Project.REQUESTING_ORG_GROUP, ArahantSession.getHSU().getCurrentCompany()).setMaxResults(cap).like(Project.PROJECTNAME, projectName);

		if (!isEmpty(statusId))
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.PROJECTSTATUSID, statusId);
		else
			hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');

		if (!isEmpty(employeeId))
			hcu.joinTo(Project.DONE_FOR_PERSON).eq(Person.PERSONID, employeeId);

		return makeArray(hcu.list());
	}

	public int getDateCompleted() {
		return project.getDateCompleted();
	}

	public int getTimeCompleted() {
		return project.getTimeCompleted();
	}

	public String getWorkDoneForEmployeeDisplayName() {
		try {
			return project.getDoneForPerson().getNameLFM();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * This is the Arahant company that the project is associated with (i.e. owns it),
	 * the company_detail record it is associated with
	 * 
	 * @return 
	 */
	public String getOwningCompanyId() {
		return project.getOwningCompanyId();
	}

	/**
	 * This is the Arahant company that the project is associated with (i.e. owns it),
	 * the company_detail record it is associated with
	 * 
	 * @return 
	 */
	public void setOwningCompanyId(String company_id) {
		project.setOwningCompanyId(company_id);
	}
	
	public String getWorkDoneForEmployeeSSN() {
		try {
			return project.getDoneForPerson().getUnencryptedSsn();
		} catch (final Exception e) {
			return "";
		}
	}

	public boolean getActive() {
		char active = project.getProjectStatus().getActive();
		return active == 'Y' || active == 'O';
	}

	public String getWorkDoneForEmployeeEmail() {
		try {
			if (project.getDoneForPerson().getPersonalEmail().indexOf('@') != 0)
				return project.getDoneForPerson().getPersonalEmail();
			return "";
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return @throws ArahantException
	 */
	public String getDoneForPersonStatusName() throws ArahantException {
		if (project.getDoneForPerson() == null)
			return "";
		return new BPerson(project.getDoneForPerson().getPersonId()).getStatus();
	}

	/**
	 * Create a project employee de-assignment history record for each assignment employee.
	 *
	 * @param projectId
	 */
	public static void unassignAllAssignmentHistory(String projectId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Connection db = hsu.getKissConnection();
		String change_person_id = hsu.getCurrentPerson().getPersonId();
		int today = DateUtils.today();
		int now = TimeUtils.now();

		try {
			List<Record> recs = db.fetchAll("select pej.person_id, ps.project_shift_id " +
					"                        from project_employee_join pej " +
					"                        join project_shift ps " +
					"                          on pej.project_shift_id = ps.project_shift_id " +
					"                        where ps.project_id = ?", projectId);
			for (Record r : recs) {
				Record rec = db.newRecord("project_employee_history");
				rec.set("project_employee_history_id", IDGenerator.generate("project_employee_history", "project_employee_history_id"));
				rec.set("person_id", r.getString("person_id"));
				rec.set("project_shift_id", r.getString("project_shift_id"));
				rec.set("change_person_id", change_person_id);
				rec.set("change_date", today);
				rec.set("change_time", now);
				rec.set("change_type", "D");
				rec.addRecord();
			}
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	public static void makeRecent(final String fromPerson, final String toPerson, final String subject, final String message, String typeId) throws ArahantException {
		if (true)
			throw new ArahantException("XXYY");
		try {
			//Don't send it to current user
			if (toPerson.equals(ArahantSession.getCurrentPerson().getPersonId()))
				return;

//			Only send if I haven't sent something recently between them with the same subject

			HibernateSessionUtil hsu = ArahantSession.getHSU();

			//trying to avoid breaking the flush - dropping to sql here
			PreparedStatement ps = hsu.getConnection().prepareStatement("select p.project_id from project p join project_employee_join pej on pej.project_id=p.project_id where p.subject_person=? and pej.person_id=? and p.description=? and p.date_reported>=? and p.time_reported>=?");


//			Lets say one every hour is enough...
			Calendar aWhileback = DateUtils.getNow();
			aWhileback.add(Calendar.HOUR_OF_DAY, -1);

			ps.setString(1, fromPerson);
			ps.setString(2, toPerson);
			ps.setString(3, subject);
			ps.setInt(4, DateUtils.getDate(aWhileback));
			ps.setInt(5, DateUtils.getTime(aWhileback));

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				rs.close();
				ps.close();
				return;
			}

			rs.close();
			ps.close();

			ps = hsu.getConnection().prepareStatement("insert into project (time_reported, " +//1
					"date_reported, " + //2
					"subject_person, " + //3
					"description, " + //4
					"detail_desc, " + //5
					"project_type_id, " + //6
					"project_category_id, " + //TODO: hard coded wmco specific - move to property or rules or something
					"project_status_id, " + //hard coded
					"requesting_org_group, " + //hard coded
					"project_id, " + //7
					"project_name, billable, company_id )" + //8
					"values (?,?,?,?,?,?,'00001-0000000000','00001-0000000000','00001-0000000000',?,?,'N',?)");

			String projectId = IDGenerator.generate(new Project());

			ps.setInt(1, DateUtils.nowTime());
			ps.setInt(2, DateUtils.now());
			//	ps.setString(3, toPerson);
			ps.setString(3, fromPerson);
			ps.setString(4, subject);
			ps.setString(5, message);
			ps.setString(6, typeId);
			ps.setString(7, projectId);
			ps.setString(8, Integer.parseInt(projectId.substring(projectId.indexOf('-') + 1)) + "");
			ps.setString(9, hsu.getCurrentCompany().getOrgGroupId());

			ps.executeUpdate();

			ps.close();

			ps = hsu.getConnection().prepareStatement("insert into project_employee_join (project_employee_join_id, person_id, project_id,date_assigned, time_assigned) values (?,?,?,?,?)");
			ps.setString(1, IDGenerator.generate(new ProjectEmployeeJoin()));
			ps.setString(2, toPerson);
			ps.setString(3, projectId);
			ps.setInt(4, DateUtils.now());
			ps.setInt(5, DateUtils.nowTime());
			ps.executeUpdate();
			ps.close();

//  XXYY			createAssignmentHistory(toPerson, projectId, "A");

		} catch (Exception e) {
			logger.error(e);
			throw new ArahantException(e);
		}
	}

	public static void makeProjectWithRoute(final String subjectPerson, final String subject, final String message, String categoryId, String typeId) throws ArahantException {
		if (true)
			throw new ArahantException("XXYY");
		PreparedStatement preparedStatement = null;
		ResultSet resultSet;
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			String projectId = IDGenerator.generate(new Project());
			String routeId;
			String routeStopId;
			String statusId;
			String orgGroupId;
			String autoAssignToSupervisor;

			// 1) get the route id for the category/type
			preparedStatement = hsu.getConnection().prepareStatement("select route_id from route_type_assoc where project_category_id=? and project_type_id=?");
			preparedStatement.setString(1, categoryId);
			preparedStatement.setString(2, typeId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			routeId = resultSet.getString(1);
			resultSet.close();
			preparedStatement.close();

			// 2) get the initial route stop id and initial status id
			preparedStatement = hsu.getConnection().prepareStatement("select route_stop_id, project_status_id from route where route_id=?");
			preparedStatement.setString(1, routeId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			routeStopId = resultSet.getString(1);
			statusId = resultSet.getString(2);
			resultSet.close();
			preparedStatement.close();

			// 3) get the initial org group and auto-assign flag
			preparedStatement = hsu.getConnection().prepareStatement("select org_group_id, auto_assign_supervisor from route_stop where route_stop_id=?");
			preparedStatement.setString(1, routeStopId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			orgGroupId = resultSet.getString(1);
			autoAssignToSupervisor = resultSet.getString(2);
			resultSet.close();
			preparedStatement.close();

			// 4) now insert the project
			preparedStatement = hsu.getConnection().prepareStatement("insert into project ("
					+ "time_reported, " + //1
					"date_reported, " + //2
					"subject_person, " + //3
					"description, " + //4
					"detail_desc, " + //5
					"project_category_id, " + //6
					"project_type_id, " + //7
					"project_status_id, " + //8
					"requesting_org_group, " + //9
					"project_id, " + //10
					"project_name, " + //11
					"route_id, " + //12
					"current_route_stop, " + //13
					"billable, company_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,'N',?)");
			preparedStatement.setInt(1, DateUtils.nowTime());
			preparedStatement.setInt(2, DateUtils.now());
			preparedStatement.setString(3, subjectPerson);
			preparedStatement.setString(4, subject);
			preparedStatement.setString(5, message);
			preparedStatement.setString(6, categoryId);
			preparedStatement.setString(7, typeId);
			preparedStatement.setString(8, statusId);
			preparedStatement.setString(9, orgGroupId);
			preparedStatement.setString(10, projectId);
			preparedStatement.setString(11, Integer.parseInt(projectId.substring(projectId.indexOf('-') + 1)) + "");
			preparedStatement.setString(12, routeId);
			preparedStatement.setString(13, routeStopId);
			preparedStatement.setString(14, hsu.getCurrentCompany().getOrgGroupId());
			preparedStatement.executeUpdate();
			preparedStatement.close();

			// 5) check if we auto-assign a person
			if (autoAssignToSupervisor.equals("Y")) {
				String assignedPersonId;

				// 1) first look up the supervisor
				preparedStatement = hsu.getConnection().prepareStatement("select person_id from org_group_association where org_group_id=? and primary_indicator='Y'");
				preparedStatement.setString(1, orgGroupId);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				assignedPersonId = resultSet.getString(1);
				resultSet.close();
				preparedStatement.close();

				// 2) insert the supervisor as the assigned
				preparedStatement = hsu.getConnection().prepareStatement("insert into project_employee_join (project_employee_join_id, person_id, project_id, date_assigned, time_assigned) values (?,?,?,?,?)");
				preparedStatement.setString(1, IDGenerator.generate(new ProjectEmployeeJoin()));
				preparedStatement.setString(2, assignedPersonId);
				preparedStatement.setString(3, projectId);
				preparedStatement.setInt(4, DateUtils.now());
				preparedStatement.setInt(5, DateUtils.nowTime());
				preparedStatement.executeUpdate();
				preparedStatement.close();

//  XXYY				BProject.createAssignmentHistory(assignedPersonId, projectId, "A");
			}
		} catch (Exception e) {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					Logger.getLogger(BProject.class.getName()).log(Level.SEVERE, null, ex);
				}
			logger.error(e);
			throw new ArahantException(e);
		}
	}

	public static void makeProject(final String fromPerson, final String toPerson, final String subject, final String message, String typeId) throws ArahantException {
		if (true)
			throw new ArahantException("XXYY");
		try {

			//Don't send it to current user - unless wizard
			if (!subject.equals("Change Request") && toPerson.equals(ArahantSession.getCurrentPerson().getPersonId()))
				return;

//			Only send if I haven't sent something recently between them with the same subject

			HibernateSessionUtil hsu = ArahantSession.getHSU();

			//trying to avoid breaking the flush - dropping to sql here
			PreparedStatement ps = hsu.getConnection().prepareStatement("insert into project (time_reported, date_reported, subject_person, description, detail_desc,"
					+ "project_type_id, project_category_id, project_status_id, requesting_org_group, project_id, project_name, route_id, current_route_stop,billable, company_id )"
					+ "values (?,?,?,?,?,?,'00001-0000000000','00001-0000000000','00001-0000000000',?,?,'00001-0000000018','00001-0000000027','N',?)");

			String projectId = IDGenerator.generate(new Project());

			ps.setInt(1, DateUtils.nowTime());
			ps.setInt(2, DateUtils.now());
			//ps.setString(3, toPerson);
			ps.setString(3, fromPerson);
			ps.setString(4, subject);
			ps.setString(5, message);

			ps.setString(6, typeId);
			ps.setString(7, projectId);
			ps.setString(8, getNameFromId(projectId));
			ps.setString(9, hsu.getCurrentCompany().getOrgGroupId());

			ps.executeUpdate();

			ps.close();

			ps = hsu.getConnection().prepareStatement("insert into project_employee_join (project_employee_join_id, person_id, project_id,date_assigned, time_assigned) values (?,?,?,?,?)");
			ps.setString(1, IDGenerator.generate(new ProjectEmployeeJoin()));
			ps.setString(2, toPerson);
			ps.setString(3, projectId);
			ps.setInt(4, DateUtils.now());
			ps.setInt(5, DateUtils.nowTime());
			ps.executeUpdate();
			ps.close();

//  XXYY			BProject.createAssignmentHistory(toPerson, projectId, "A");
		} catch (Exception e) {
			logger.error(e);
			throw new ArahantException(e);
		}
	}

	/**
	 * @param configIds
	 */
	public void associateBenefitConfigIds(String[] configIds) {
		HrBenefitProjectJoin bpj;
		HrBenefitProjectJoinId bpjid;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final String configId : configIds) {
			bpjid = new HrBenefitProjectJoinId();
			bpjid.setBenefitConfigId(configId);
			bpjid.setProjectId(project.getProjectId());

			bpj = new HrBenefitProjectJoin();
			bpj.setId(bpjid);
			bpj.setHrBenefitConfig(hsu.get(HrBenefitConfig.class, configId));
			bpj.setProject(project);

			hsu.insert(bpj);
		}
	}

	/**
	 * @param configIds
	 */
	public void dissassociateBenefitConfigIds(String[] configIds) throws ArahantDeleteException {
		HrBenefitProjectJoin bpj;
		HrBenefitProjectJoinId bpjid;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final String configId : configIds) {
			bpjid = new HrBenefitProjectJoinId();
			bpjid.setBenefitConfigId(configId);
			bpjid.setProjectId(project.getProjectId());

			bpj = hsu.get(HrBenefitProjectJoin.class, bpjid);

			hsu.delete(bpj);
		}
	}

	public BHRBenefitConfig[] getAssociatedBenefitConfigs() {
		final Set bpj = project.getHrBenefitProjectJoins();
		final Iterator bpjItr = bpj.iterator();
		final ArrayList<HrBenefitConfig> configs = new ArrayList<HrBenefitConfig>();
		while (bpjItr.hasNext())
			configs.add(((HrBenefitProjectJoin) bpjItr.next()).getHrBenefitConfig());

		java.util.Collections.sort(configs, new BenefitConfigComparator());

		final BHRBenefitConfig[] ret = new BHRBenefitConfig[configs.size()];
		for (int loop = 0; loop < configs.size(); loop++)
			ret[loop] = new BHRBenefitConfig(configs.get(loop));

		return ret;
	}

	/**
	 * Send an email to all people assigned to the project.
	 *
	 * @param subject
	 * @param message
	 */
	private void sendToAssignees(String subject, String message) {
		if (!BProperty.getBoolean("CommentMessages"))
			return;
		
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		subject = "[Project] " + subject;

		List<Person> l = hsu.createCriteria(Person.class)
				.joinTo(Person.PROJECT_PERSON_JOIN)
				.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.list();

		Set<Person> ps = new HashSet<>(l);
		//add any new ones from updates
		for (ArahantBean b : updates)
			if (b instanceof ProjectEmployeeJoin)
				ps.add(((ProjectEmployeeJoin) b).getPerson());

		//add the managing employee
		if (project.getEmployee() != null)
			ps.add(project.getEmployee());

		for (Person p : ps) {
			if (p.getPersonId().equals(hsu.getCurrentPerson().getPersonId()))
				continue;
			/*
			 * if(!hsu.createCriteria(Message.class) .eq(Message.SUBJECT,
			 * subject) .ge(Message.CREATEDDATE, DateUtils.now())
			 * .eq(Message.PERSONBYTOPERSONID, p) .exists())
			 */
			BMessage.send(hsu.getCurrentPerson(), p, subject, message);
		}

	}

	public void copyFromAndDelete(String deleteId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Project p = hsu.get(Project.class, deleteId);

		if (true) throw new ArahantException("XXYY");
		/*  XXYY

		for (ProjectForm f : p.getProjectForms()) {
			f.setProjectShift(project);
			hsu.saveOrUpdate(f);
		}

		for (ProjectComment f : p.getProjectComments()) {
			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		for (Timesheet f : p.getTimesheets()) {
			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		for (HrBenefitProjectJoin f : p.getHrBenefitProjectJoins()) {
			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		for (ProjectEmployeeJoin f : p.getProjectEmployeeJoins()) {
			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		for (ProjectHistory f : p.getProjectHistories()) {
			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		for (Timesheet f : p.getTimesheets()) {
//  XXYY			f.setProject(project);
			hsu.saveOrUpdate(f);
		}

		hsu.delete(p);

		 */
	}

	public int getTotalTimesheetEntries() {
		return ArahantSession.getHSU().createCriteria(Timesheet.class)
				.joinTo(Timesheet.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.count();
	}

	public double getTotalLoggedTime() {
		double total = 0.0;
		HibernateScrollUtil<Timesheet> s = ArahantSession.getHSU().createCriteria(Timesheet.class)
				.joinTo(Timesheet.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.scroll();
		while (s.next()) {
			Timesheet t = s.get();
			total += t.getTotalHours();
		}
		return total;
	}

	public int getTotalLogEntries() {
		return ArahantSession.getHSU().createCriteria(ProjectComment.class)
				.joinTo(ProjectComment.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.count();
	}

	private static class BenefitConfigComparator implements Comparator<HrBenefitConfig> {
		
		@Override
		public int compare(HrBenefitConfig c1, HrBenefitConfig c2) {
			String cat1 = c1.getHrBenefitCategory().getDescription();
			String cat2 = c2.getHrBenefitCategory().getDescription();
			String ben1 = c1.getHrBenefit().getName();
			String ben2 = c2.getHrBenefit().getName();
			String con1 = c1.getName();
			String con2 = c2.getName();

			if (cat1 == null)
				cat1 = "";
			if (cat2 == null)
				cat2 = "";
			if (ben1 == null)
				ben1 = "";
			if (ben2 == null)
				ben2 = "";
			if (con1 == null)
				con1 = "";

			if (cat1.equals(cat2))
				if (ben1.equals(ben2))
					return con1.compareToIgnoreCase(con2);
				else
					return ben1.compareToIgnoreCase(ben2);
			else
				return cat1.compareToIgnoreCase(cat2);
		}
	}

	public BHRBenefitConfig[] getDissassociatedBenefitConfigs(String benefitId) {
		final HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId);

		hcu.le(HrBenefit.START_DATE, DateUtils.now()).geOrEq(HrBenefit.END_DATE, DateUtils.now(), 0);

		hcu.le(HrBenefitConfig.START_DATE, DateUtils.now()).geOrEq(HrBenefit.END_DATE, DateUtils.now(), 0);

		final Set bpjs = project.getHrBenefitProjectJoins();
		final Iterator bpjsItr = bpjs.iterator();
		final ArrayList<String> configIds = new ArrayList<String>();
		while (bpjsItr.hasNext())
			configIds.add(((HrBenefitProjectJoin) bpjsItr.next()).getHrBenefitConfig().getBenefitConfigId());

		hcu.notIn(HrBenefitConfig.BENEFIT_CONFIG_ID, configIds);

		hcu.orderBy(HrBenefitConfig.NAME);

		final List<HrBenefitConfig> configs = hcu.list();

		final BHRBenefitConfig[] ret = new BHRBenefitConfig[configs.size()];
		for (int loop = 0; loop < configs.size(); loop++)
			ret[loop] = new BHRBenefitConfig(configs.get(loop));

		return ret;
	}

	/**
	 * This is the date & time the project was added.
	 *
	 * @return
	 */
	public String getDateTimeReportedFormatted() {
		return DateUtils.getDateTimeFormatted(project.getDateReported(), project.getTimeReported());
	}

	private List<String> getBillProjectsTree() {
		HibernateScrollUtil<Project> s = ArahantSession.getHSU().createCriteria(Project.class).joinTo(Project.VIEWS).joinTo(ProjectView.VIEW_JOINS_WHERE_CHILD).eq(ProjectViewJoin.PRIMARY_BILLING, 'Y').joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).scroll();
		List<String> ids = new LinkedList<String>();
		ids.add(project.getProjectId());
		while (s.next()) {
			Project p = s.get();
			ids.add(p.getProjectId());
			ids.addAll(new BProject(p).getBillProjectsTree());
		}
		return ids;
	}

	public double getBillTreeBillableHours() {
		HibernateScrollUtil<Timesheet> s = ArahantSession.getHSU().createCriteria(Timesheet.class)
				.eq(Timesheet.BILLABLE, 'Y')
				.joinTo(Timesheet.PROJECTSHIFT)
				.joinTo(ProjectShift.PROJECT)
				.in(Project.PROJECTID, getBillProjectsTree())
				.scroll();
		double ret = 0;
		while (s.next()) {
			Timesheet t = s.get();
			ret += t.getTotalHours();
		}
		return ret;
	}

	public double getBillableHours() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateScrollUtil<Timesheet> s = ArahantSession.getHSU().createCriteria(Timesheet.class)
				.eq(Timesheet.BILLABLE, 'Y')
				.joinTo(Timesheet.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.scroll();
		double ret = 0;
		while (s.next()) {
			Timesheet t = s.get();
			ret += t.getTotalHours();
		}
		return ret;
	}

	public double getBillTreeNonBillableHours() {
		HibernateScrollUtil<Timesheet> s = ArahantSession.getHSU()
				.createCriteria(Timesheet.class)
				.eq(Timesheet.BILLABLE, 'N')
				.joinTo(Timesheet.PROJECTSHIFT)
				.joinTo(ProjectShift.PROJECT)
				.in(Project.PROJECTID, getBillProjectsTree())
				.scroll();
		double ret = 0;
		while (s.next()) {
			Timesheet t = s.get();
			ret += t.getTotalHours();
		}
		return ret;
	}

	public double getNonBillableHours() {
		HibernateScrollUtil<Timesheet> s = ArahantSession.getHSU()
				.createCriteria(Timesheet.class)
				.eq(Timesheet.BILLABLE, 'N')
				.joinTo(Timesheet.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project)
				.scroll();
		double ret = 0;
		while (s.next()) {
			Timesheet t = s.get();
			ret += t.getTotalHours();
		}
		return ret;
	}

	public List<ProjectShift> getAllShifts() {
		return ArahantSession.getHSU()
				.createCriteria(ProjectShift.class)
				.eq(ProjectShift.PROJECT_ID, project.getProjectId())
				.list();
	}

	public BProjectStatus getProjectStatus() {
		ProjectStatus ps = project.getProjectStatus();
		if (ps == null)
			return null;
		return new BProjectStatus(ps);
	}

	public BEmployee getManagingEmployee() {
		Employee e = project.getEmployee();
		if (e == null)
			return null;
		return new BEmployee(e);
	}

	public BRateType getRateType() {
		RateType rt = project.getRateType();
		return rt == null ? null : new BRateType(rt);
	}

	public void setRateType(BRateType brt) {
		project.setRateType(brt == null ? null : brt.getRateType());
	}

	public String getProjectState () {
	    return project.getProjectState();
    }

    public void setProjectState(String state) {
	    project.setProjectState(state);
    }

	public BProjectCategory getProjectCategory() {
		return new BProjectCategory(project.getProjectCategory());
	}

	public BProjectType getProjectType() {
		return new BProjectType(project.getProjectType());
	}

	public BProject[] listSubProjects() {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).orderBy(Project.PROJECTNAME);

		hcu.joinTo(Project.VIEWS).isNull(ProjectView.PERSON).joinTo(ProjectView.VIEW_JOINS_WHERE_CHILD).joinTo(ProjectViewJoin.PARENT).eq(ProjectView.PROJECT, project).isNull(ProjectView.PERSON);

		return makeArray(hcu.list());
	}

	public BProject[] listParentProjects() {
		HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class).orderBy(Project.PROJECTNAME);

		hcu.joinTo(Project.VIEWS).isNull(ProjectView.PERSON).joinTo(ProjectView.VIEW_JOINS_WHERE_PARENT).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PROJECT, project).isNull(ProjectView.PERSON);

		return makeArray(hcu.list());
	}

	/**
	 * Get a list of all workers assigned to the project.  (Not just a particular shift.)
	 *
	 * @return
	 */
	public List<Person> getAssignedPersons2() {
		return getAssignedPersons2(99999999, null);
	}

	/**
	 * Get a list of all workers assigned to a shift.  If shift is null,
	 * get all workers associated with the project as a whole.
	 *
	 * @return
	 */
	public List<Person> getAssignedPersons2(String projectShiftId) {
		return getAssignedPersons2(99999999, projectShiftId);
	}

	/**
	 * Get a list of all workers who are assigned to a shift and start before or on workDate.
	 * If shift is null,
	 * get all workers associated with the project as a whole.
	 *
	 * @param workDate
	 * @return
	 */
	public List<Person> getAssignedPersons2(int workDate, String projectShiftId) {
		return getAssignedPersons2(workDate, true, projectShiftId);
	}

	/**
	 * If <code>projectShiftId</code> is <code>null</code> that means get workers for all schedules for project.
	 *
	 * @param workDate
	 * @param includeNotReportingHours
	 * @param projectShiftId
	 * @return
	 */
	public List<Person> getAssignedPersons2(int workDate, boolean includeNotReportingHours, String projectShiftId) {
		int firstDate = project.getEstimatedFirstDate();
		HibernateCriteriaUtil<ProjectEmployeeJoin> hcu = ArahantSession.getHSU().createCriteria(ProjectEmployeeJoin.class);
		HibernateCriteriaUtil<ProjectEmployeeJoin> hcu2 = hcu.joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
				.eq(ProjectShift.PROJECT, project);
		if (projectShiftId != null  && !projectShiftId.isEmpty())
			hcu2.eq(ProjectShift.PROJECTSHIFTID, projectShiftId);
		List<ProjectEmployeeJoin> pejs = hcu.joinTo(ProjectEmployeeJoin.PERSON)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME)
				.orderBy(Person.MNAME)
				.list();
		List<Person> pl = new ArrayList<>();
		if (firstDate < 20000101 || workDate >= firstDate)
			for (ProjectEmployeeJoin pej : pejs) {
				int startDate = pej.getStartDate();
				if (startDate <= workDate && (includeNotReportingHours || pej.getHours().equals("Y")))
					pl.add(pej.getPerson());
			}
		return pl;
	}

	public BRouteStop[] listRouteStops() {
		if (project.getRouteStop() == null)
			return new BRouteStop[0];

		return BRouteStop.makeArray(ArahantSession.getHSU().createCriteria(RouteStop.class).eq(RouteStop.ROUTE, project.getRouteStop().getRoute()).orderBy(RouteStop.NAME).list());
	}

	public String getPercentComplete() {
		if (getEstimateHours() < .01)
			return "N/A";

		if (getProjectStatus().getActive() == 0)
			return "100%";

		long val = Math.round(100 * getBillableHours() / getEstimateHours());
		if (val > 99)
			val = 99;

		return val + "%";
	}

	public double getBenefitAccrual(BEmployee bemp) {
		double ret = 0;

		for (HrBenefitProjectJoin bpj : project.getHrBenefitProjectJoins())
			ret += bemp.getHoursLeftOnBenefit(bpj.getHrBenefitConfig().getHrBenefit().getName());

		return ret;
	}

	public double getBenefitAccrual(BEmployee bemp, int startDate, int endDate) {
		double ret = 0;

		for (HrBenefitProjectJoin bpj : project.getHrBenefitProjectJoins())
			ret += bemp.getHoursLeftOnBenefit(bpj.getHrBenefitConfig().getHrBenefit().getName(), startDate, endDate);

		return ret;
	}

	public BCompanyBase getRequestingCompany() {
		return BCompanyBase.get(project.getRequestingOrgGroup().getOwningCompany().getOrgGroupId());
	}
	
	public String getPurchaseOrder() {
		return project.getPurchaseOrder();
	}
	
	public void setPurchaseOrder(String po) {
		project.setPurchaseOrder(po);
	}

	public Project getBean() {
		return project;
	}

	public Set<HrBenefitConfig> getBenefitConfigs() {
		return project.getBenefitConfigs();
	}

	public void setBenefitConfigs(Set<HrBenefitConfig> configs) {
		project.setBenefitConfigs(configs);
	}

	public static BProject[] listChangeRequests() {
		return makeArray(ArahantSession.getHSU().createCriteria(Project.class).joinTo(Project.CHANGE_REQUEST).list());
	}

	public BWizardProject[] getWizardProjects(boolean incompletedOnly) {
		HibernateCriteriaUtil<WizardProject> hcu = ArahantSession.getHSU().createCriteria(WizardProject.class).orderBy(WizardProject.COMPLETED).eq(WizardProject.PROJECT, this.project);

		if (incompletedOnly)
			hcu.eq(WizardProject.COMPLETED, 'N');

		return BWizardProject.makeArray(hcu.list());
	}

	/**
	 * Get all ProjectHistory records sorted by date and time.
	 *
	 * @return
	 */
	public List<ProjectHistory> getHistory() {
		return ArahantSession.getHSU().createCriteria(ProjectHistory.class)
				.eq(ProjectHistory.PROJECT, project)
				.orderBy(ProjectHistory.DATE)
				.orderBy(ProjectHistory.TIME)
				.list();
	}

	public ProjectStatus getCurrentStatus() {
		ProjectHistory ph = ArahantSession.getHSU().createCriteria(ProjectHistory.class)
				.eq(ProjectHistory.PROJECT, project)
				.le(ProjectHistory.DATE, DateUtils.today())
				.orderByDesc(ProjectHistory.DATE)
				.orderByDesc(ProjectHistory.TIME)
				.first();
		if (ph == null)
			return null;
		return ph.getToStatus();
	}

	/**
	 * Gets the start date of the project
	 *
	 * @return the start date YYYYMMDD
	 */
	public int getStartDate() {
		List<ProjectHistory> phlst = getHistory();
		if (phlst.isEmpty())
			return 0;
		for (ProjectHistory ph : phlst)
			if (ph.getToStatus().getActive() != 'N')
				return ph.getDateChanged();
		return 0;
	}

	/**
	 * Gets the date the project terminated or 0 if still active.
	 *
	 * @return YYYYMMDD
	 */
	public int getEndDate() {
		List<ProjectHistory> phlst = getHistory();
		if (phlst.isEmpty())
			return 0;
		ProjectHistory ph = phlst.get(phlst.size()-1);
		if (ph.getToStatus().getActive() == 'N')
			return ph.getDateChanged();
		return 0;
	}

	public boolean isPersonAssociateWithProject(Person p) {
	    String pid = p.getPersonId();
	    BPerson mp = getManagingEmployee();
	    if (mp != null  &&  mp.getPersonId().equals(pid))
	        return true;
	    List<Person> baps = getAssignedPersons2(null);
	    for (Person bap : baps) {
	        if (bap.getPersonId().equals(pid))
	            return true;
        }
	    return false;
    }

    public int getEstimatedFirstDate() {
        return project.getEstimatedFirstDate();
    }

    public BProject setEstimatedFirstDate(int dt) {
        project.setEstimatedFirstDate(dt);
        return this;
    }

    public int getEstimatedLastDate() {
        return project.getEstimatedLastDate();
    }

    public BProject setEstimatedLastDate(int dt) {
        project.setEstimatedLastDate(dt);
        return this;
    }

    public Address getAddress() {
	    return project.getAddress();
    }

    public BProject setAddress(Address address) {
	    project.setAddress(address);
	    return this;
    }

	public String getStoreNumber() {
		return project.getStoreNumber();
	}

	public BProject setStoreNumber(String storeNumber) {
		project.setStoreNumber(storeNumber);
		return this;
	}

	public String getLocationDescription() {
		return project.getLocationDescription();
	}

	public BProject setLocationDescription(String shiftStart) {
		project.setLocationDescription(shiftStart);
		return this;
	}

	public char getBillingType() {
		return project.getBillingType();
	}

	public BProject setBillingType(char type) {
		project.setBillingType(type);
		return this;
	}

	public double getFixedPriceAmount() {
		return project.getFixedPriceAmount();
	}

	public BProject setFixedPriceAmount(double a) {
		project.setFixedPriceAmount(a);
		return this;
	}

	public Date getLastReportDate() {
		return project.getLastReportDate();
	}

	public BProject setLastReportDate(Date a) {
		project.setLastReportDate(a);
		return this;
	}

	public short getProjectDays() {
		return project.getProjectDays();
	}

	public BProject setProjectDays(short a) {
		project.setProjectDays(a);
		return this;
	}

	public double amountPreviouslyInvoiced() {
		final String projectId = project.getProjectId();
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		double total = 0;
		final List<InvoiceLineItem> lis = hsu.createCriteria(InvoiceLineItem.class).eq(InvoiceLineItem.PROJECTID, projectId).list();
		for (InvoiceLineItem li : lis)
			total += li.getAmount();
		return total;
	}
}
