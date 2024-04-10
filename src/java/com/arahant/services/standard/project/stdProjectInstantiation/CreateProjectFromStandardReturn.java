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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.project.stdProjectInstantiation;
import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class CreateProjectFromStandardReturn extends TransmitReturnBase {


	private CreatedProjects [] projects;

	public CreateProjectFromStandardReturn() {
		super();
	}

	/**
	 * @return Returns the projects.
	 */
	public CreatedProjects[] getProjects() {
		return projects;
	}

	/**
	 * @param projects The projects to set.
	 */
	public void setProjects(final CreatedProjects[] projects) {
		this.projects = projects;
	}

	/**
	 * @param projects2
	 */
	void setProjects(final BProject[] p) {
		projects=new CreatedProjects[p.length];
		for (int loop=0;loop<p.length;loop++)
			projects[loop]=new CreatedProjects(p[loop]);
	}
}

	
