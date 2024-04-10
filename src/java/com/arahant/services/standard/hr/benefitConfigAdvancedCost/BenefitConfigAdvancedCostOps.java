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


package com.arahant.services.standard.hr.benefitConfigAdvancedCost;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitConfigAdvancedCostOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitConfigAdvancedCostOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitConfigAdvancedCostOps.class);

	public BenefitConfigAdvancedCostOps() {}

	@WebMethod()
	public ListConfigsReturn listConfigs(/*
			 * @WebParam(name = "in")
			 */final ListConfigsInput in) {
		final ListConfigsReturn ret = new ListConfigsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefitConfig.list(in.getBenefitId()));
			
			ret.setBenefitType((new BHRBenefit(in.getBenefitId())).getBenefitCategory().getBenefitType());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewConfigReturn newConfig(/*
			 * @WebParam(name = "in")
			 */final NewConfigInput in) {
		final NewConfigReturn ret = new NewConfigReturn();
		try {
			checkLogin(in);

			final BHRBenefitConfig x = new BHRBenefitConfig();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveConfigReturn saveConfig(/*
			 * @WebParam(name = "in")
			 */final SaveConfigInput in) {
		final SaveConfigReturn ret = new SaveConfigReturn();
		try {
			checkLogin(in);
			//ArahantSession.getAI().watchAll();
			final BHRBenefitConfig x = new BHRBenefitConfig(in.getConfigId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteConfigsReturn deleteConfigs(/*
			 * @WebParam(name = "in")
			 */final DeleteConfigsInput in) {
		final DeleteConfigsReturn ret = new DeleteConfigsReturn();
		try {
			checkLogin(in);

			BHRBenefitConfig.delete(hsu, in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadConfigReturn loadConfig(/*
			 * @WebParam(name = "in")
			 */final LoadConfigInput in) {
		final LoadConfigReturn ret = new LoadConfigReturn();
		try {
			checkLogin(in);

			ret.setData(new BHRBenefitConfig(in.getConfigId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadBenefitReturn loadBenefit(/*
			 * @WebParam(name = "in")
			 */final LoadBenefitInput in) {
		final LoadBenefitReturn ret = new LoadBenefitReturn();
		try {
			checkLogin(in);

			ret.setData(new BHRBenefit(in.getBenefitId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*
			 * @WebParam(name = "in")
			 */final ListBenefitClassesInput in) {
		final ListBenefitClassesReturn ret = new ListBenefitClassesReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitClass.list(in.getExcludeIds()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*
			 * @WebParam(name = "in")
			 */final ListOrgGroupsForGroupInput in) {
		final ListOrgGroupsForGroupReturn ret = new ListOrgGroupsForGroupReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getOrgGroupId()))
				ret.setItem(new BOrgGroup(in.getOrgGroupId()).getChildren(in.getExcludeIds()));
			else
				ret.setItem(new BOrgGroup[]{new BOrgGroup(hsu.getCurrentCompany())});
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*
			 * @WebParam(name = "in")
			 */final ListEmployeeStatusesInput in) {
		final ListEmployeeStatusesReturn ret = new ListEmployeeStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(in.getExcludeIds()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReorderConfigsReturn reorderConfigs(/*
			 * @WebParam(name = "in")
			 */final ReorderConfigsInput in) {
		final ReorderConfigsReturn ret = new ReorderConfigsReturn();
		try {
			checkLogin(in);

			BHRBenefitConfig bc;

			int i = 0;
			for (String s : in.getIds()) {
				bc = new BHRBenefitConfig(s);
				bc.setSequence(i);
				bc.update();
				i++;
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveConfigReturn moveConfig(/*
			 * @WebParam(name = "in")
			 */final MoveConfigInput in) {
		final MoveConfigReturn ret = new MoveConfigReturn();
		try {
			checkLogin(in);

			final BHRBenefitConfig x = new BHRBenefitConfig(in.getId());
			x.moveUp(in.getMoveUp());
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
