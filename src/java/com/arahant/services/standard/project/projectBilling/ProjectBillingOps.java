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
package com.arahant.services.standard.project.projectBilling;

import com.arahant.beans.ProjectPhase;
import com.arahant.business.BProject;
import com.arahant.business.BRateType;
import com.arahant.business.BRight;
import com.arahant.business.BService;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectBillingOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectBillingOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(ProjectBillingOps.class);

	@WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in) {
		final SearchServicesReturn ret = new SearchServicesReturn();

		try {
			checkLogin(in);

			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getProjectId())) {
				BProject p = new BProject(in.getProjectId());
				try {
					ret.setSelectedItem(new SearchServicesReturnItem(new BService(p.getProductId())));
				} catch (Exception e) {
				}
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SaveBillingReturn saveBilling(/*@WebParam(name = "in")*/final SaveBillingInput in) {
		logger.debug("In saveProject");

		final SaveBillingReturn ret = new SaveBillingReturn();

		try {
			checkLogin(in);

			final BProject bp = new BProject(in.getProjectId());

			in.makeProject(bp);

			bp.update();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		logger.debug("return from saveProject");
		return ret;
	}

	@WebMethod()
	public LoadBillingReturn loadBilling(/*@WebParam(name = "in")*/final LoadBillingInput in) {
		final LoadBillingReturn ret = new LoadBillingReturn();

		try {
			checkLogin(in);

			final BProject bp = new BProject(in.getProjectId());

			ret.setData(bp);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadBillingRateTypesReturn loadBillingRateTypes(/*@WebParam(name = "in")*/final LoadBillingRateTypesInput in) {
		final LoadBillingRateTypesReturn ret = new LoadBillingRateTypesReturn();

		try {
			checkLogin(in);

			ret.setItem(BRateType.list());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessOtherBillableLevel"));
			ret.setBillableAccessLevel(BRight.checkRight("AccessBillableLevel"));
			ret.setDefaultServiceAccessLevel(BRight.checkRight("AccessDefaultProductService"));

			BProject bp = new BProject(in.getProjectId());
			int phase = bp.getPhaseSecurityLevel();

			ret.setEstimateAccessLevel(BRight.checkRight("AccessEstimates"));

			if (BRight.checkRight("AccessProjectApproval") == ACCESS_LEVEL_WRITE
					|| (BRight.checkRight("AccessProjectApprovalInEst") == ACCESS_LEVEL_WRITE && phase == ProjectPhase.ESTIMATE && bp.getEstimateHours() > 0))
				ret.setEstimateApprovalAccessLevel(2);
			else
				ret.setEstimateApprovalAccessLevel(1);

			if (BRight.checkRight("AccessEstimates") == ACCESS_LEVEL_WRITE
					|| (BRight.checkRight("AccessProjectEstimateInEst") == ACCESS_LEVEL_WRITE && phase == ProjectPhase.ESTIMATE && bp.getEstimateHours() > 0))
				ret.setEstimateAccessLevel(2);
			else
				ret.setEstimateAccessLevel(1);



			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListParentProjectsReturn listParentProjects(/*@WebParam(name = "in")*/final ListParentProjectsInput in) {
		final ListParentProjectsReturn ret = new ListParentProjectsReturn();
		try {
			checkLogin(in);

			ret.setItem(new BProject(in.getId()).listParentProjects(), new BProject(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetUpdatedEstimateAndActualReturn getUpdatedEstimateAndActual(/*@WebParam(name = "in")*/final GetUpdatedEstimateAndActualInput in) {
		final GetUpdatedEstimateAndActualReturn ret = new GetUpdatedEstimateAndActualReturn();
		try {
			checkLogin(in);

			BProject bp = new BProject(in.getId());

			ret.setEstimate(DateUtils.getMeasuredValue(bp.getEstimateHours(), in.getNewEstimateMeasurement()));
			ret.setActualNonBillable(DateUtils.getMeasuredValue(bp.getNonBillableHours(), in.getNewEstimateMeasurement()));
			ret.setActualBillable(DateUtils.getMeasuredValue(bp.getBillableHours(), in.getNewEstimateMeasurement()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
