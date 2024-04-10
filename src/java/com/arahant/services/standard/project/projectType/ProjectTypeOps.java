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


package com.arahant.services.standard.project.projectType;

import com.arahant.beans.Right;
import com.arahant.business.BProjectType;
import com.arahant.business.BRight;
import com.arahant.reports.ProjectTypeReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import java.io.File;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectTypeOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(ProjectTypeOps.class);

	@WebMethod()
	public DeleteProjectTypeReturn deleteProjectType(/*
			 * @WebParam(name = "in")
			 */final DeleteProjectTypeInput in) {
		final DeleteProjectTypeReturn ret = new DeleteProjectTypeReturn();
		try {
			checkLogin(in);

			BProjectType.delete(hsu, in.getProjectTypeIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*
			 * @WebParam(name = "in")
			 */final ListProjectTypesInput in) {
		final ListProjectTypesReturn ret = new ListProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.list(hsu));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewProjectTypeReturn newProjectType(/*
			 * @WebParam(name = "in")
			 */final NewProjectTypeInput in) {
		final NewProjectTypeReturn ret = new NewProjectTypeReturn();
		try {
			checkLogin(in);

			final BProjectType bp = new BProjectType();
			ret.setProjectTypeId(bp.create());
			in.setData(bp);
			bp.insert();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;

	}

	@WebMethod()
	public SaveProjectTypeReturn saveProjectType(/*
			 * @WebParam(name = "in")
			 */final SaveProjectTypeInput in) {
		final SaveProjectTypeReturn ret = new SaveProjectTypeReturn();

		try {
			checkLogin(in);
			final BProjectType bp = new BProjectType(in.getProjectTypeId());
			in.setData(bp);
			bp.update();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*
			 * @WebParam(name = "in")
			 */final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetProjectTypeReportReturn getProjectTypeReport(/*
			 * @WebParam(name = "in")
			 */final GetProjectTypeReportInput in) {
		final GetProjectTypeReportReturn ret = new GetProjectTypeReportReturn();

		try {
			checkLogin(in);

			final File fyle = FileSystemUtils.createTempFile("PTRept", ".pdf");

			// configure a finalize report and build it
			new ProjectTypeReport().build(hsu, fyle);

			// construct a transmit return
			ret.setReportUrl(FileSystemUtils.getHTTPPath(fyle));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}

/*
 * ProjectManagementService.deleteProjectTypeObj
 * ProjectManagementService.listProjectTypeesObj
 * ProjectManagementService.newProjectTypeObj
 * ProjectManagementService.saveProjectTypeObj
 * SecurityManagementService.checkRights	 *
 */
