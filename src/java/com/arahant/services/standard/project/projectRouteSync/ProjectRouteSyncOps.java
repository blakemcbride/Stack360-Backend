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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.project.projectRouteSync;

import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.business.BProject;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 * Arahant
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectRouteSyncOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectRouteSyncOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectRouteSyncOps.class);

	public ProjectRouteSyncOps() {
		super();
	}

	@WebMethod()
	public SyncProjectsReturn syncProjects(/*@WebParam(name = "in")*/final SyncProjectsInput in)	{
		final SyncProjectsReturn ret=new SyncProjectsReturn();

		try {
			checkLogin(in);
			HibernateSessionUtil hsu2 = ArahantSession.getHSU();

			HibernateScrollUtil<Project> scr =  hsu2.createCriteria(Project.class).isNull(Project.CURRENT_ROUTE_STOP).scroll();

			int count = 0;
			while(scr.next())
			{
				count++;
				BProject bp = new BProject(scr.get());
				String description = bp.getDescription();
				String lname = bp.getDoneForPersonLastName();
				String fname = bp.getDoneForPersonFirstName();
				String projectId = bp.getProjectId();

				try {
					bp.calculateRouteAndStatus();
					bp.update();
					hsu2.commitTransaction();
					hsu2.beginTransaction();
					System.out.println(count + ". Synching project " + description + " / " + fname + " " + lname + " (" + projectId + ") ");
				} catch (ArahantException arahantException) {
					System.out.println(count + ". ERROR Synching project " + description + " / " + fname + " " + lname + " (" + projectId + ") ");
				}
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	public static void main(String[] args) {
		if(!ArahantSession.getHSU().isOpen())
			ArahantSession.openHSU();

		HibernateScrollUtil<Project> scr =  ArahantSession.getHSU().createCriteria(Project.class).isNull(Project.CURRENT_ROUTE_STOP).scroll();

		ArahantSession.getHSU().setCurrentPerson(ArahantSession.getHSU().get(Person.class, "00001-0000005729"));

		int count = 0;
		while(scr.next())
		{
			count++;
			BProject bp = new BProject(scr.get());
			bp.calculateRouteAndStatus();
			bp.update();
			System.out.println(count + ". Synching project " + bp.getDescription() + " / " + bp.getDoneForPersonFirstName() + " " + bp.getDoneForPersonLastName() + " (" + bp.getProjectId() + ") ");
		}
		ArahantSession.getHSU().commitTransaction();
	}
}
