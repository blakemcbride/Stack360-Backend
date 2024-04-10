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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "project_view")
public class ProjectView extends ArahantBean implements Serializable {

	public static final String PERSON = "person";
	public static final String PROJECT = "project";
	public static final String ID = "projectViewId";
	public static final String VIEW_JOINS_WHERE_CHILD = "projectViewJoinsAsChild";
	public static final String VIEW_JOINS_WHERE_PARENT = "projectViewJoinsAsParent";
	private static final long serialVersionUID = -5046390603967510761L;
	private String projectViewId;
	private Person person;
	private String nodeTitle;
	private String nodeDescription;
	private Project project;
	private Set<ProjectViewJoin> projectViewJoinsAsChild = new HashSet<ProjectViewJoin>();
	private Set<ProjectViewJoin> projectViewJoinsAsParent = new HashSet<ProjectViewJoin>();

	@OneToMany(mappedBy = ProjectViewJoin.CHILD, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectViewJoin> getProjectViewJoinsAsChild() {
		return projectViewJoinsAsChild;
	}

	public void setProjectViewJoinsAsChild(Set<ProjectViewJoin> projectViewJoinsAsChild) {
		this.projectViewJoinsAsChild = projectViewJoinsAsChild;
	}

	@OneToMany(mappedBy = ProjectViewJoin.PARENT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectViewJoin> getProjectViewJoinsAsParent() {
		return projectViewJoinsAsParent;
	}

	public void setProjectViewJoinsAsParent(Set<ProjectViewJoin> projectViewJoinsAsParent) {
		this.projectViewJoinsAsParent = projectViewJoinsAsParent;
	}

	@Column(name = "node_description")
	public String getNodeDescription() {
		return nodeDescription;
	}

	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	@Column(name = "node_title")
	public String getNodeTitle() {
		return nodeTitle;
	}

	public void setNodeTitle(String nodeTitle) {
		this.nodeTitle = nodeTitle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Id
	@Column(name = "project_view_id")
	public String getProjectViewId() {
		return projectViewId;
	}

	public void setProjectViewId(String projectViewId) {
		this.projectViewId = projectViewId;
	}

	@Override
	public boolean equals(Object o) {
		if (projectViewId == null && o == null)
			return true;
		if (projectViewId != null && o instanceof ProjectView)
			return projectViewId.equals(((ProjectView) o).getProjectViewId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectViewId == null)
			return 0;
		return projectViewId.hashCode();
	}

	@Override
	public String tableName() {
		return "project_view";
	}

	@Override
	public String keyColumn() {
		return "project_view_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return projectViewId = IDGenerator.generate(this);
	}
}
