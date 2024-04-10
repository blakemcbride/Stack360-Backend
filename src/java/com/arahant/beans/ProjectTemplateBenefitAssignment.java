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
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "project_template_benefit_a")
public class ProjectTemplateBenefitAssignment extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "project_template_benefit_a";
	public static final String ID = "id";
	private String id;
	public static final String PERSON = "person";
	private Person person;
	public static final String PERSON_ID = "personId";
	private String personId;
	public static final String PROJECT_TEMPLATE_BENEFIT = "projectTemplateBenefit";
	private ProjectTemplateBenefit projectTemplateBenefit;
	public static final String PROJECT_TEMPLATE_BENEFIT_ID = "projectTemplateBenefitId";
	private String projectTemplateBenefitId;

	//Default constructor
	public ProjectTemplateBenefitAssignment() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_template_id")
	public ProjectTemplateBenefit getProjectTemplateBenefit() {
		return projectTemplateBenefit;
	}

	public void setProjectTemplateBenefit(ProjectTemplateBenefit projectTemplateBenefit) {
		this.projectTemplateBenefit = projectTemplateBenefit;
	}

	@Column(name = "project_template_id", insertable = false, updatable = false)
	public String getProjectTemplateBenefitId() {
		return projectTemplateBenefitId;
	}

	public void setProjectTemplateBenefitId(String setProjectTemplateBenefitId) {
		this.projectTemplateBenefitId = setProjectTemplateBenefitId;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Id
	@Column(name = "template_assignment_id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public ProjectTemplateBenefitAssignment clone() {
		ProjectTemplateBenefitAssignment p = new ProjectTemplateBenefitAssignment();
		p.generateId();
		p.setPersonId(personId);
		p.setProjectTemplateBenefit(projectTemplateBenefit);

		return p;
	}

	@Override
	public boolean equals(Object o) {
		if (id == null && o == null)
			return true;
		if (id != null && o instanceof ProjectTemplateBenefitAssignment)
			return id.equals(((ProjectTemplateBenefitAssignment) o).getId());

		return false;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}

	@Override
	public String generateId() throws ArahantException {
		setId(IDGenerator.generate(this));
		return getId();
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "template_assignment_id";
	}
}
