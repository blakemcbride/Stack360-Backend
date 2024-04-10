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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.IAuditedBean;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.WizardProject;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BWizardProject extends SimpleBusinessObjectBase<WizardProject> {

	public BWizardProject() {
	}

	public BWizardProject(String key) {
		super(key);
	}

	public BWizardProject(WizardProject o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardProject();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(WizardProject.class, key);
	}

	public String getBenefitJoinId() {
		return bean.getBenefitJoinId();
	}

	public HrBenefitJoin getBenefitJoin() {
		return bean.getBenefitJoin();
	}

	public void setBenefitJoin(HrBenefitJoin benefitJoin) {
		bean.setBenefitJoin(benefitJoin);
	}

	public Project getProject() {
		return bean.getProject();
	}

	public void setProject(Project project) {
		bean.setProject(project);
	}

	public char getProjectAction() {
		return bean.getProjectAction();
	}

	public void setProjectAction(char projectAction) {
		bean.setProjectAction(projectAction);
	}

	public void setProjectAction(String projectAction) {
		bean.setProjectAction(projectAction.charAt(0));
	}

	public String getProjectId() {
		return bean.getProjectId();
	}

	public String getWizardProjectId() {
		return bean.getWizardProjectId();
	}

	static BWizardProject[] makeArray(List<WizardProject> l) {
		BWizardProject[] ret = new BWizardProject[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardProject(l.get(loop));
		return ret;
	}

	public void approve() {
		if (getProjectAction() == 'A') {
			BHRBenefitJoin bj = new BHRBenefitJoin(getBenefitJoinId());
			BHRBenefitCategory bcat = new BHRBenefitCategory(bj.getBenefitCategoryId());
			if (!bcat.getAllowsMultipleBenefits()) {
				HrBenefitJoin tbj = bj.getPayingPerson().getApprovedBenefitJoinOf(bcat);
				BHRBenefitJoin btbj = new BHRBenefitJoin(tbj);
				if (tbj != null && tbj.getHrBenefitConfig() != null) {
					btbj.setPolicyEndDate(DateUtils.addDays(bj.getPolicyStartDate(), -1));
					btbj.setCoverageEndDate(DateUtils.addDays(bj.getPolicyStartDate(), -1));
					btbj.delete();
				}
			}
			bj.setPolicyApproved(true);

			ArahantSession.getHSU().commitTransaction();
			ArahantSession.getHSU().beginTransaction();


			BEmployee emp = new BEmployee(bj.getPayingPersonId());
			boolean hasUnapproved = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
					.in(HrBenefitJoin.PAYING_PERSON, emp.getRealAndChangePerson())
					.eq(HrBenefitJoin.APPROVED, 'N')
					.exists();

			ArahantSession.getHSU().rollbackTransaction();
			ArahantSession.getHSU().beginTransaction();

			if (!hasUnapproved) {
				emp.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_PROCESSED + "");
				emp.update();
			}
		} else if (getProjectAction() == 'C') {
			//do nothing?
		} else if (getProjectAction() == 'D') {
			BPerson bp = new BPerson(getProject().getSubjectPerson());
			bp.apply(true);
		}
		markCompleted();
	}

	public void reject() {
		if (getProjectAction() == 'A') {
			BHRBenefitJoin bj = new BHRBenefitJoin(getBenefitJoinId());
			bj.setBenefitApproved('R');

			setBenefitJoin(null);
			update();

			bj.delete();
		} else if (getProjectAction() == 'C') {
			//do nothing?
		} else if (getProjectAction() == 'D') {
			BPerson bp = new BPerson(getProject().getSubjectPerson());
			bp.reject();
		}
		markCompleted();
	}

	boolean isApproved() {
		if (getProjectAction() == 'A')
			return (getBenefitJoin().getBenefitApproved() == 'Y');
		else if (getProjectAction() == 'C')
			return (getProject().getProjectStatus().getActive() == 'N');
		else
			return (!new BPerson(getProject().getSubjectPerson()).hasPending(getProject().getSubjectPerson().getPersonId()));
	}

	public char getCompleted() {
		return bean.getCompleted();
	}

	public void setCompleted(char completed) {
		bean.setCompleted(completed);
	}

	public void setCompleted(String completed) {
		bean.setCompleted(completed.charAt(0));
	}

	public Date getDateComplated() {
		return bean.getDateComplated();
	}

	public void setDateComplated(Date dateComplated) {
		bean.setDateComplated(dateComplated);
	}

	public Person getPersonCompleted() {
		return bean.getPersonCompleted();
	}

	public void setPersonCompleted(Person personCompleted) {
		bean.setPersonCompleted(personCompleted);
	}

	public void markCompleted() {
		markCompleted(true);
	}

	public void markCompleted(boolean doGroup) throws ArahantException {
		this.setCompleted("Y");
		this.setDateComplated(new Date());
		this.setPersonCompleted(ArahantSession.getCurrentPerson());

		if (getBenefitJoin() != null && doGroup)
			for (WizardProject wp : ArahantSession.getHSU().createCriteria(WizardProject.class).eq(WizardProject.BENEFIT_JOIN, getBenefitJoin()).eq(WizardProject.PROJECT_ACTION, getProjectAction()).ne(WizardProject.WIZARD_PROJECT_ID, this.getWizardProjectId()).list()) {
				BWizardProject bwp = new BWizardProject(wp);
				bwp.setCompleted("Y");
				bwp.setDateComplated(new Date());
				bwp.setPersonCompleted(ArahantSession.getCurrentPerson());
				bwp.setBenefitJoin(null);
				bwp.update();
			}
		if (doGroup)
			this.update();
	}

	public String getBenefitJoinHId() {
		return bean.getBenefitJoinHId();
	}

	public HrBenefitJoinH getBenefitJoinH() {
		return bean.getBenefitJoinH();
	}

	public void setBenefitJoinH(HrBenefitJoinH bjh) {
		bean.setBenefitJoinH(bjh);
	}

	public static List<WizardProject> adjustWizardProjects(IAuditedBean originalBean) {
		List<WizardProject> ret = new ArrayList<WizardProject>();
		for (WizardProject wp : ArahantSession.getHSU().createCriteria(WizardProject.class).eq(WizardProject.BENEFIT_JOIN, (HrBenefitJoin) originalBean).list()) {
			BWizardProject bwp = new BWizardProject(wp);
			bwp.setBenefitJoin(null);
			bwp.getBean().setBenefitJoinId(null);
			//bwp.setBenefitJoinH((HrBenefitJoinH)history);
			ret.add(bwp.getBean());
		}
		return ret;
	}
}
