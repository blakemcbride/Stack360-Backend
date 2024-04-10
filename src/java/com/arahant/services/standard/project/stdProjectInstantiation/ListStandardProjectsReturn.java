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
import com.arahant.business.BStandardProject;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class ListStandardProjectsReturn extends TransmitReturnBase {

	private StandardProjects []projects;
	
	public ListStandardProjectsReturn() {
		super();
	}

	/**
	 * @return Returns the projects.
	 */
	public StandardProjects[] getProjects() {
		return projects;
	}

	/**
	 * @param projects The projects to set.
	 */
	public void setProjects(final StandardProjects[] projects) {
		this.projects = projects;
	}

	/**
	 * @param projects2
	 */
	void setProjects(final BStandardProject[] p) {
		projects=new StandardProjects[p.length];
		for (int loop=0;loop<p.length;loop++)
			projects[loop]=new StandardProjects(p[loop]);
	}
}

	
