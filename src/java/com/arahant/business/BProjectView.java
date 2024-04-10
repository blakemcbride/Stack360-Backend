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

import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectView;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

public class BProjectView extends SimpleBusinessObjectBase<ProjectView> {

	static ProjectView getForProjectId(String projectId) {

		ProjectView pv = ArahantSession.getHSU().createCriteria(ProjectView.class).isNull(ProjectView.PERSON).joinTo(ProjectView.PROJECT).eq(Project.PROJECTID, projectId).first();

		if (pv == null) {
			BProjectView bpv = new BProjectView();
			bpv.create();
			bpv.setProjectId(projectId);
			bpv.insert();
			pv = bpv.bean;
		}

		return pv;
	}

	public BProjectView(String id) {
		internalLoad(id);
	}

	public BProjectView() {
	}

	BProjectView(ProjectView pv) {
		bean = pv;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectView();
		return bean.generateId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		if (bean.getProject() != null) {
			bean.setNodeDescription(null);
			bean.setNodeTitle(null);
		}
		super.insert();
	}

	public String getDescription() {
		if (bean.getProject() == null)
			return bean.getNodeDescription();
		return bean.getProject().getDetailDesc();
	}

	public String getId() {
		return bean.getProjectViewId();
	}

	public String getProjectId() {
		if (bean.getProject() == null)
			return "";
		return bean.getProject().getProjectId();
	}

	public String getSummary() {
		if (bean.getProject() == null)
			return bean.getNodeTitle();
		return bean.getProject().getDescription();
	}

	public int getType() {
		return bean.getProject() == null ? 1 : 0;
	}

	public String getTypeFormatted() {
		return bean.getProject() == null ? "Folder" : "Project";
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectView.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public void setDescription(String description) {
		bean.setNodeDescription(description);

	}

	public void setSummary(String summary) {
		bean.setNodeTitle(summary);
	}

	void setPerson(Person person) {
		bean.setPerson(person);
	}

	void setProjectId(String projectId) {
		bean.setProject(ArahantSession.getHSU().get(Project.class, projectId));
	}
}
