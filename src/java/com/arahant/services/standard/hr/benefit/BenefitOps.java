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



package com.arahant.services.standard.hr.benefit;

import com.arahant.beans.HrBenefit;
import com.arahant.business.BBenefitRider;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BRight;
import com.arahant.business.BScreen;
import com.arahant.business.BService;
import com.arahant.business.BVendorCompany;
import com.arahant.business.BWageType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.Set;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrBenefitOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class BenefitOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitOps.class);

	public BenefitOps() {
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight(ACCESS_SETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			hsu.rollbackTransaction();
			logger.error(e);
			return new CheckRightReturn("Failed: Contact Administrator");
		}

		return ret;
	}

	@WebMethod()
	public ListRulesReturn listRules(/*@WebParam(name = "in")*/final ListRulesInput in) {
		final ListRulesReturn ret = new ListRulesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefit.listRules());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*@WebParam(name = "in")*/final ListBenefitCategoriesInput in) {
		final ListBenefitCategoriesReturn ret = new ListBenefitCategoriesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchVendorsReturn searchVendors(/*@WebParam(name = "in")*/final SearchVendorsInput in) {
		final SearchVendorsReturn ret = new SearchVendorsReturn();
		try {
			checkLogin(in);

			ret.setItem(BVendorCompany.searchVendors(in.getName(), ret.getHighCap()));

			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchVendorsReturnItem(new BVendorCompany(in.getId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in) {
		final SearchServicesReturn ret = new SearchServicesReturn();

		try {
			checkLogin(in);

			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getBenefitId())) {
				BHRBenefit b = new BHRBenefit(in.getBenefitId());

				try {
					ret.setSelectedItem(new SearchServicesReturnItem(new BService(b.getProductServiceId())));
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
	public LoadBenefitReturn loadBenefit(/*@WebParam(name = "in")*/final LoadBenefitInput in) {
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
	public SaveBenefitReturn saveBenefit(/*@WebParam(name = "in")*/final SaveBenefitInput in) {
		final SaveBenefitReturn ret = new SaveBenefitReturn();
		try {
			checkLogin(in);

			final BHRBenefit x = new BHRBenefit(in.getBenefitId());
			in.setData(x);
			x.update();

			BHRBenefit.clearReplacingBenefits(in.getBenefitId());

			if (in.getReplacingBenefitIds() != null)
				for (String s : in.getReplacingBenefitIds()) {
					BHRBenefit benefit = new BHRBenefit(s);
					benefit.setReplacingBenefit(new BHRBenefit(in.getBenefitId()).getBean());
					benefit.update();
				}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in) {
		final ListWageTypesReturn ret = new ListWageTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BWageType.list(ret.getHighCap()));


			if (!isEmpty(in.getWageTypeId()))
				ret.setSelectedItem(new ListWageTypesReturnItem(new BWageType(in.getWageTypeId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in) {
		final ListBenefitClassesReturn ret = new ListBenefitClassesReturn();
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
	public SearchScreensReturn searchScreens(/*@WebParam(name = "in")*/final SearchScreensInput in) {
		final SearchScreensReturn ret = new SearchScreensReturn();

		try {
			checkLogin(in);

			ret.setData(BScreen.searchScreens(in.getName(), in.getExtId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchBenefitsReturn searchBenefits(/*@WebParam(name = "in")*/final SearchBenefitsInput in) {
		final SearchBenefitsReturn ret = new SearchBenefitsReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getBenefitId())) {
				BHRBenefit b = BHRBenefit.getReplacingBenefit(in.getBenefitId());
				if (b != null)
					ret.setSelectedItem(new SearchBenefitsReturnItem(b));
				else
					ret.setSelectedItem(null);
			}

			if (isEmpty(in.getCategoryId()))
				in.setCategoryId(new BHRBenefit(in.getBenefitId()).getBenefitCategoryId());

			ret.setItem(BHRBenefit.searchBenefits(in.getBenefitId(), in.getCategoryId(), in.getName(), ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReplacingBenefitsReturn listReplacingBenefits(/*@WebParam(name = "in")*/final ListReplacingBenefitsInput in) {
		final ListReplacingBenefitsReturn ret = new ListReplacingBenefitsReturn();
		try {
			checkLogin(in);

			BHRBenefit benefit = new BHRBenefit(in.getBenefitId());

			Set<HrBenefit> replacedBenefits = benefit.getReplacedBenefits();

			BHRBenefit benefits[] = new BHRBenefit[replacedBenefits.size()];

			int loop = 0;
			for (HrBenefit hb : replacedBenefits)
				benefits[loop++] = new BHRBenefit(hb);

			ret.setReplacingBenefits(benefits);

			ret.setNotReplacingBenefits(BHRBenefit.getBenefitsNotBeingReplaced(in.getBenefitId(), in.getCategoryId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitRidersReturn listBenefitRiders(/*@WebParam(name = "in")*/final ListBenefitRidersInput in) {
		final ListBenefitRidersReturn ret = new ListBenefitRidersReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitRider.list(in.getBenefitId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewRiderReturn newRider(/*@WebParam(name = "in")*/final NewRiderInput in) {
		final NewRiderReturn ret = new NewRiderReturn();
		try {
			checkLogin(in);

			BBenefitRider bbr = new BBenefitRider();
			bbr.create();
			in.setData(bbr);
			bbr.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveRiderReturn saveRider(/*@WebParam(name = "in")*/final SaveRiderInput in) {
		final SaveRiderReturn ret = new SaveRiderReturn();
		try {
			checkLogin(in);

			BBenefitRider bbr = new BBenefitRider(in.getBenefitRiderId());
			in.setData(bbr);
			bbr.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteRidersReturn deleteRiders(/*@WebParam(name = "in")*/final DeleteRidersInput in) {
		final DeleteRidersReturn ret = new DeleteRidersReturn();
		try {
			checkLogin(in);

			for (String id : in.getIds())
				new BBenefitRider(id).delete();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchBenefitsForRiderReturn searchBenefitsForRider(/*@WebParam(name = "in")*/final SearchBenefitsForRiderInput in) {
		final SearchBenefitsForRiderReturn ret = new SearchBenefitsForRiderReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getBenefitRiderId())) {
				BBenefitRider b = new BBenefitRider(in.getBenefitRiderId());
				if (b != null)
					ret.setSelectedItem(new SearchBenefitsReturnItem(new BHRBenefit(b.getRiderBenefit())));
				else
					ret.setSelectedItem(null);
			}

			ret.setItem(BHRBenefit.searchBenefits(in.getBenefitId(), in.getName(), ret.getHighCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
