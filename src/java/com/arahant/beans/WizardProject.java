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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "wizard_project")
public class WizardProject extends ArahantBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "wizard_project";
	private String wizardProjectId;
	private String projectId;
	private Project project;
	private char projectAction;
	private String benefitJoinId;
	private HrBenefitJoin benefitJoin;
	private Date dateComplated;
	private Person personCompleted;
	private char completed = 'N';
	public static final String WIZARD_PROJECT_ID = "wizardProjectId";
	public static final String PROJECT = "project";
	public static final String PROJECT_ID = "projectId";
	public static final String PROJECT_ACTION = "projectAction";
	public static final String BENEFIT_JOIN_ID = "benefitJoinId";
	public static final String BENEFIT_JOIN = "benefitJoin";
	public static final String COMPLETED = "completed";
	public static final String DATE_COMPLETED = "dateCompleted";
	public static final String PERSON_COMPLETED = "personCompleted";
	private String benefitJoinHId;
	private HrBenefitJoinH benefitJoinH;
	public static final String BENEFIT_JOIN_H_ID = "benefitJoinHId";
	public static final String BENEFIT_JOIN_H = "benefitJoinH";

	public WizardProject() {
	}

	@ManyToOne
	@JoinColumn(name = "hr_benefit_join_h_id")
	public HrBenefitJoinH getBenefitJoinH() {
		return benefitJoinH;
	}

	public void setBenefitJoinH(HrBenefitJoinH benefitJoinH) {
		this.benefitJoinH = benefitJoinH;
	}

	@Column(name = "hr_benefit_join_h_id", insertable = false, updatable = false)
	public String getBenefitJoinHId() {
		return benefitJoinHId;
	}

	public void setBenefitJoinHId(String benefitJoinHId) {
		this.benefitJoinHId = benefitJoinHId;
	}

	@Column(name = "completed")
	public char getCompleted() {
		return completed;
	}

	public void setCompleted(char completed) {
		this.completed = completed;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_completed")
	public Date getDateComplated() {
		return dateComplated;
	}

	public void setDateComplated(Date dateComplated) {
		this.dateComplated = dateComplated;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_completed")
	public Person getPersonCompleted() {
		return personCompleted;
	}

	public void setPersonCompleted(Person personCompleted) {
		this.personCompleted = personCompleted;
	}

	@Column(name = "benefit_join_id", insertable = false, updatable = false)
	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_join_id")
	public HrBenefitJoin getBenefitJoin() {
		return benefitJoin;
	}

	public void setBenefitJoin(HrBenefitJoin benefitJoin) {
		this.benefitJoin = benefitJoin;
	}

	@ManyToOne
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "project_action")
	public char getProjectAction() {
		return projectAction;
	}

	public void setProjectAction(char projectAction) {
		this.projectAction = projectAction;
	}

	@Column(name = "project_id", insertable = false, updatable = false)
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Id
	@Column(name = "wizard_project_id")
	public String getWizardProjectId() {
		return wizardProjectId;
	}

	public void setWizardProjectId(String wizardProjectId) {
		this.wizardProjectId = wizardProjectId;
	}

	@Override
	public String tableName() {

		return TABLE_NAME;
	}


	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setWizardProjectId(IDGenerator.generate(this));
		return wizardProjectId;
	}

	@Override
	public boolean equals(Object o) {
		if (wizardProjectId == null && o == null)
			return true;
		if (wizardProjectId != null && o instanceof HrAccrual)
			return wizardProjectId.equals(((HrAccrual) o).getAccrualId());

		return false;
	}

	@Override
	public int hashCode() {
		if (wizardProjectId == null)
			return 0;
		return wizardProjectId.hashCode();
	}

	@Override
	public String keyColumn() {
		return "wizard_project_id";
	}
}
