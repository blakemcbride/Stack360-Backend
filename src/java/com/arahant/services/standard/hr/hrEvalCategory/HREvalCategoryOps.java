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
 *
 *  Created on Feb 22, 2007
*/

package com.arahant.services.standard.hr.hrEvalCategory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHREvalCategory;
import com.arahant.business.BRight;
import com.arahant.reports.HREvalCategoryReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEvalCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREvalCategoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HREvalCategoryOps.class);

	public HREvalCategoryOps() {
		super();
	}

	@WebMethod()
	public DeleteEvalCategoriesReturn deleteEvalCategories(/*@WebParam(name = "in")*/final DeleteEvalCategoriesInput in) {
		final DeleteEvalCategoriesReturn ret = new DeleteEvalCategoriesReturn();
		try {
			checkLogin(in);

			BHREvalCategory.delete(hsu, in.getIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetEvalCategoriesReportReturn getEvalCategoriesReport(/*@WebParam(name = "in")*/final GetEvalCategoriesReportInput in) {
		final GetEvalCategoriesReportReturn ret = new GetEvalCategoriesReportReturn();
		try {
			checkLogin(in);

			ret.setFileName(new HREvalCategoryReport().build(in.getActiveType()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEvalCategoriesReturn listEvalCategories(/*@WebParam(name = "in")*/final ListEvalCategoriesInput in) {
		final ListEvalCategoriesReturn ret = new ListEvalCategoriesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREvalCategory.list(in.getActiveType()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewEvalCategoryReturn newEvalCategory(/*@WebParam(name = "in")*/final NewEvalCategoryInput in) {
		final NewEvalCategoryReturn ret = new NewEvalCategoryReturn();
		try {
			checkLogin(in);

			final BHREvalCategory x = new BHREvalCategory();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveEvalCategoryReturn saveEvalCategory(/*@WebParam(name = "in")*/final SaveEvalCategoryInput in) {
		final SaveEvalCategoryReturn ret = new SaveEvalCategoryReturn();
		try {
			checkLogin(in);

			final BHREvalCategory x = new BHREvalCategory(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadEvalDescriptionReturn loadEvalDescription(/*@WebParam(name = "in")*/final LoadEvalDescriptionInput in) {
		final LoadEvalDescriptionReturn ret = new LoadEvalDescriptionReturn();
		try {
			checkLogin(in);

			ret.setData(new BHREvalCategory(in.getEvalCatId()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}
/*
 * HrEvalCategory		HumanResourcesManagementService.deleteEvalCategoriesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getEvalCategoriesReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEvalCategoriesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.newEvalCategoryObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveEvalCategoryObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHRSetups																																																																																																																																																																																																																																																												

 */
