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


package com.arahant.services.standard.project.projectCategory;

import com.arahant.beans.Right;
import com.arahant.business.BProjectCategory;
import com.arahant.business.BRight;
import com.arahant.reports.ProjectCategoryReport;
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

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectCategoryOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(ProjectCategoryOps.class);

	@WebMethod()
	public DeleteProjectCategoryReturn deleteProjectCategory(/*
			 * @WebParam(name = "in")
			 */final DeleteProjectCategoryInput in) {
		final DeleteProjectCategoryReturn ret = new DeleteProjectCategoryReturn();

		try {
			checkLogin(in);

			BProjectCategory.delete(hsu, in.getProjectCategoryIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListProjectCategoriesReturn listProjectCategories(/*
			 * @WebParam(name = "in")
			 */final ListProjectCategoriesInput in) {
		final ListProjectCategoriesReturn ret = new ListProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.list(hsu));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewProjectCategoryReturn newProjectCategory(/*
			 * @WebParam(name = "in")
			 */final NewProjectCategoryInput in) {
		final NewProjectCategoryReturn ret = new NewProjectCategoryReturn();
		try {
			checkLogin(in);

			final BProjectCategory bpc = new BProjectCategory();
			ret.setProjectCategoryId(bpc.create());
			in.setData(bpc);
			bpc.insert();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;

	}

	@WebMethod()
	public SaveProjectCategoryReturn saveProjectCategory(/*
			 * @WebParam(name = "in")
			 */final SaveProjectCategoryInput in) {
		final SaveProjectCategoryReturn ret = new SaveProjectCategoryReturn();

		try {
			checkLogin(in);

			final BProjectCategory bpc = new BProjectCategory(in.getProjectCategoryId());
			in.setData(bpc);
			bpc.update();

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
	public GetProjectCategoryReportReturn getProjectCategoryReport(/*
			 * @WebParam(name = "in")
			 */final GetProjectCategoryReportInput in) {
		final GetProjectCategoryReportReturn ret = new GetProjectCategoryReportReturn();

		try {
			checkLogin(in);

			final File fyle = FileSystemUtils.createTempFile("PCRept", ".pdf");

			// configure a finalize report and build it
			new ProjectCategoryReport().build(hsu, fyle);

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
 * ProjectManagementService.deleteProjectCategoryObj
 * ProjectManagementService.listProjectCategoriesObj
 * ProjectManagementService.newProjectCategoryObj
 * ProjectManagementService.saveProjectCategoryObj
 * SecurityManagementService.checkRights	 *
 */
