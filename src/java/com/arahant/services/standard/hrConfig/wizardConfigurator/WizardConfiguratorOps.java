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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrConfigWizardConfiguratorOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class WizardConfiguratorOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(WizardConfiguratorOps.class);

	public WizardConfiguratorOps() {
	}

	@WebMethod()
	public LoadWizardConfigurationReturn loadWizardConfiguration(/*@WebParam(name = "in")*/final LoadWizardConfigurationInput in) {

		final LoadWizardConfigurationReturn ret = new LoadWizardConfigurationReturn();

		try {
			checkLogin(in);

			ret.setData(new BWizardConfiguration(in.getWizardConfigurationId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReportsReturn listReports(/*@WebParam(name = "in")*/final ListReportsInput in) {

		final ListReportsReturn ret = new ListReportsReturn();

		try {
			checkLogin(in);

			ListReportsReturnItem[] reviewArray = new ListReportsReturnItem[2];
			reviewArray[0] = new ListReportsReturnItem("Generic Review Report", 0);
			reviewArray[1] = new ListReportsReturnItem("WmCo Review Report", 1);
			ret.setReviewReport(reviewArray);

			ListReportsReturnItem[] benefitArray = new ListReportsReturnItem[2];
			benefitArray[0] = new ListReportsReturnItem("Generic Benefit Report", 0);
			benefitArray[1] = new ListReportsReturnItem("WmCo Benefit Statement", 1);
			ret.setBenefitReport(benefitArray);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();
		try {
			checkLogin(in);

			//	ret.setItem(BProjectStatus.search(hsu, in.getCode(), in.getDescription(),in.getFromRouteStopId(), in.getStatusType(),ret.getHighCap()));

			//TODO: refactor into biz object
			BProject proj = null;
			if (!isEmpty(in.getProjectId()))
				proj = new BProject(in.getProjectId());
			//add to query, code and description
			RouteTypeAssoc rta;
			List<ProjectStatus> projectStatusList;
			if (!isEmpty(in.getProjectCategoryId()) && !isEmpty(in.getProjectTypeId())) {
				rta = hsu.createCriteria(RouteTypeAssoc.class).eq(RouteTypeAssoc.PROJECT_TYPE, new BProjectType(in.getProjectTypeId()).getBean())
						.eq(RouteTypeAssoc.PROJECT_CATEGORY, new BProjectCategory(in.getProjectCategoryId()).getBean())
						.joinTo(RouteTypeAssoc.ROUTE)
						.first();

				if (rta == null)
					throw new ArahantWarning("This is not a valid Category/Type combination.");

				HibernateCriteriaUtil<RouteStop> routeStopHcu = hsu.createCriteria(RouteStop.class).eq(RouteStop.ROUTE, rta.getRoute());
				projectStatusList = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription()).joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).in(RouteStop.ROUTE_STOP_ID, routeStopHcu.selectFields(RouteStop.ROUTE_STOP_ID).list()).list();

				ret.setItem(BProjectStatus.makeArray(projectStatusList), proj);
			} else {

				HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription());
				ret.setItem(BProjectStatus.makeArray(hcu.list()), proj);
			}
			if (!isEmpty(in.getWizardConfigurationId()))
				ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(new BWizardConfiguration(in.getWizardConfigurationId()).getProjectStatus()), proj));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();
		try {
			checkLogin(in);

			boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

			ret.setProjectCategories(BProjectCategory.search(all, in.getCode(), in.getDescription(), ret.getHighCap()));
			if (!isEmpty(in.getWizardConfigurationId()))
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(new BWizardConfiguration(in.getWizardConfigurationId()).getProjectCategory())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();
		try {
			checkLogin(in);

			boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

			ret.setProjectTypes(BProjectType.search(all, in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getWizardConfigurationId()))
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(new BWizardConfiguration(in.getWizardConfigurationId()).getProjectType())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListWizardConfigurationsReturn listWizardConfigurations(/*@WebParam(name = "in")*/final ListWizardConfigurationsInput in) {
		final ListWizardConfigurationsReturn ret = new ListWizardConfigurationsReturn();

		try {
			checkLogin(in);

			ret.setItem(BWizardConfiguration.list(ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewWizardConfigurationReturn newWizardConfiguration(/*@WebParam(name = "in")*/final NewWizardConfigurationInput in) {
		final NewWizardConfigurationReturn ret = new NewWizardConfigurationReturn();

		try {
			checkLogin(in);

			final BWizardConfiguration x = new BWizardConfiguration();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveWizardConfigurationReturn saveWizardConfiguration(/*@WebParam(name = "in")*/final SaveWizardConfigurationInput in) {
		final SaveWizardConfigurationReturn ret = new SaveWizardConfigurationReturn();

		try {
			checkLogin(in);

			final BWizardConfiguration x = new BWizardConfiguration(in.getWizardConfigurationId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteWizardConfigurationReturn deleteWizardConfiguration(/*@WebParam(name = "in")*/final DeleteWizardConfigurationInput in) {
		final DeleteWizardConfigurationReturn ret = new DeleteWizardConfigurationReturn();

		try {
			checkLogin(in);

			BWizardConfiguration.delete(in.getWizardConfigurationId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListCategoriesReturn listCategories(/*@WebParam(name = "in")*/final ListCategoriesInput in) {
		final ListCategoriesReturn ret = new ListCategoriesReturn();

		try {
			checkLogin(in);

			ret.setItem(BWizardConfigurationCategory.list(ret.getCap(), in.getWizardConfigurationId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in) {
		final ListBenefitsReturn ret = new ListBenefitsReturn();

		try {
			checkLogin(in);

			ret.setItem(BWizardConfigurationBenefit.list(ret.getCap(), in.getWizardCategoryId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListConfigsReturn listConfigs(/*@WebParam(name = "in")*/final ListConfigsInput in) {
		final ListConfigsReturn ret = new ListConfigsReturn();

		try {
			checkLogin(in);

			ret.setItem(BWizardConfigurationConfig.list(ret.getCap(), in.getWizardBenefitId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadWizardPreviewReturn loadWizardPreview(/*@WebParam(name = "in")*/final LoadWizardPreviewInput in) {
		final LoadWizardPreviewReturn ret = new LoadWizardPreviewReturn();

		try {
			checkLogin(in);

			ret.setItem((new BWizardConfiguration(in.getWizardConfigurationId())).loadWizard(null));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadWizardCategoryReturn loadWizardCategory(/*@WebParam(name = "in")*/final LoadWizardCategoryInput in) {
		final LoadWizardCategoryReturn ret = new LoadWizardCategoryReturn();

		try {
			checkLogin(in);

			ret.setData(new BWizardConfigurationCategory(in.getCategoryWizardId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveWizardCategoryReturn saveWizardCategory(/*@WebParam(name = "in")*/final SaveWizardCategoryInput in) {
		final SaveWizardCategoryReturn ret = new SaveWizardCategoryReturn();

		try {
			checkLogin(in);

			final BWizardConfigurationCategory x = new BWizardConfigurationCategory(in.getCategoryWizardId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewWizardCategoryReturn newWizardCategory(/*@WebParam(name = "in")*/final NewWizardCategoryInput in) {
		final NewWizardCategoryReturn ret = new NewWizardCategoryReturn();

		try {
			checkLogin(in);

			final BWizardConfigurationCategory x = new BWizardConfigurationCategory();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			if (in.getGenerateBenefits()) {
				ArahantSession.getHSU().flush();
				int countBenefits = 0;
				for (BHRBenefit b : BWizardConfiguration.listAvailableBenefits(50, x.getWizardConfigurationCategoryId())) {
					BWizardConfigurationBenefit wb = new BWizardConfigurationBenefit();
					wb.create();
					wb.setAvatarPath(b.getAvatarPath());
					wb.setBenefit(b.getBean());
					wb.setInstructions(b.getAdditionalInstructions());
					wb.setName(b.getName());
					wb.setSeqNo(countBenefits++);
					wb.setWizardConfigurationCategory(x.getBean());
					wb.insert();

					if (in.getGenerateConfigs()) {
						ArahantSession.getHSU().flush();
						int countConfigs = 0;
						for (BHRBenefitConfig c : BWizardConfiguration.listAvailableConfigs(50, wb.getWizardConfigurationBenefitId())) {
							BWizardConfigurationConfig wc = new BWizardConfigurationConfig();
							wc.create();
							wc.setBenefitConfig(c.getBean());
							wc.setName(c.getName());
							wc.setSeqNo(countConfigs++);
							wc.setWizardConfigurationBenefit(wb.getBean());
							wc.insert();
						}
					}
				}
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadWizardBenefitReturn loadWizardBenefit(/*@WebParam(name = "in")*/final LoadWizardBenefitInput in) {
		final LoadWizardBenefitReturn ret = new LoadWizardBenefitReturn();

		try {
			checkLogin(in);

			ret.setData(new BWizardConfigurationBenefit(in.getBenefitWizardId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReorderBenefitsReturn reorderBenefits(/*@WebParam(name = "in")*/final ReorderBenefitsInput in) {
		final ReorderBenefitsReturn ret = new ReorderBenefitsReturn();
		try {
			checkLogin(in);

			BWizardConfigurationBenefit b;

			int i = 16000;
			for (String s : in.getIds()) {
				b = new BWizardConfigurationBenefit(s);
				b.setSeqNo(i);
				b.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			for (String s : in.getIds()) {
				b = new BWizardConfigurationBenefit(s);
				b.setSeqNo(i);
				b.update();
				i++;
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReorderCategoriesReturn reorderCategories(/*@WebParam(name = "in")*/final ReorderCategoriesInput in) {
		final ReorderCategoriesReturn ret = new ReorderCategoriesReturn();
		try {
			checkLogin(in);

			BWizardConfigurationCategory bc;

			int i = 16000;
			for (String s : in.getIds()) {
				bc = new BWizardConfigurationCategory(s);
				bc.setSeqNo(i);
				bc.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			for (String s : in.getIds()) {
				bc = new BWizardConfigurationCategory(s);
				bc.setSeqNo(i);
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
	public LoadProposedBenefitReturn loadProposedBenefit(/*@WebParam(name = "in")*/final LoadProposedBenefitInput in) {
		final LoadProposedBenefitReturn ret = new LoadProposedBenefitReturn();
		try {
			checkLogin(in);

			ret.setData(in.getBenefitId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProposedCategoryReturn loadProposedCategory(/*@WebParam(name = "in")*/final LoadProposedCategoryInput in) {
		final LoadProposedCategoryReturn ret = new LoadProposedCategoryReturn();
		try {
			checkLogin(in);

			ret.setData(in.getCategoryId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAvailableBenefitsReturn listAvailableBenefits(/*@WebParam(name = "in")*/final ListAvailableBenefitsInput in) {
		final ListAvailableBenefitsReturn ret = new ListAvailableBenefitsReturn();
		try {
			checkLogin(in);

			ret.setItem(BWizardConfiguration.listAvailableBenefits(ret.getCap(), in.getCategoryWizardId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveWizardBenefitReturn saveWizardBenefit(/*@WebParam(name = "in")*/final SaveWizardBenefitInput in) {
		final SaveWizardBenefitReturn ret = new SaveWizardBenefitReturn();

		try {
			checkLogin(in);

			final BWizardConfigurationBenefit x = new BWizardConfigurationBenefit(in.getBenefitWizardId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteCategoriesFromWizardReturn deleteCategoriesFromWizard(/*@WebParam(name = "in")*/final DeleteCategoriesFromWizardInput in) {
		final DeleteCategoriesFromWizardReturn ret = new DeleteCategoriesFromWizardReturn();
		try {
			checkLogin(in);

			BWizardConfigurationCategory.delete(in.getIds());

			int i = 16000;
			HibernateScrollUtil<WizardConfigurationCategory> scr = hsu.createCriteria(WizardConfigurationCategory.class).notIn("wizardConfigurationCategoryId", in.getIds()).scroll();
			while (scr.next()) {
				BWizardConfigurationCategory bc = new BWizardConfigurationCategory(scr.get());
				bc.setSeqNo(i);
				bc.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			scr = hsu.createCriteria(WizardConfigurationCategory.class).notIn("wizardConfigurationCategoryId", in.getIds()).scroll();
			while (scr.next()) {
				BWizardConfigurationCategory bc = new BWizardConfigurationCategory(scr.get());
				bc.setSeqNo(i);
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
	public DeleteBenefitsFromWizardReturn deleteBenefitsFromWizard(/*@WebParam(name = "in")*/final DeleteBenefitsFromWizardInput in) {
		final DeleteBenefitsFromWizardReturn ret = new DeleteBenefitsFromWizardReturn();
		try {
			checkLogin(in);

			BWizardConfigurationBenefit.delete(in.getIds());

			int i = 16000;
			HibernateScrollUtil<WizardConfigurationBenefit> scr = hsu.createCriteria(WizardConfigurationBenefit.class).scroll();
			while (scr.next()) {
				BWizardConfigurationBenefit b = new BWizardConfigurationBenefit(scr.get());
				b.setSeqNo(i);
				b.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			scr = hsu.createCriteria(WizardConfigurationBenefit.class).scroll();
			while (scr.next()) {
				BWizardConfigurationBenefit b = new BWizardConfigurationBenefit(scr.get());
				b.setSeqNo(i);
				b.update();
				i++;
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteConfigsFromWizardReturn deleteConfigsFromWizard(/*@WebParam(name = "in")*/final DeleteConfigsFromWizardInput in) {
		final DeleteConfigsFromWizardReturn ret = new DeleteConfigsFromWizardReturn();
		try {
			checkLogin(in);

			BWizardConfigurationConfig.delete(in.getIds());

			int i = 16000;
			HibernateScrollUtil<WizardConfigurationConfig> scr = hsu.createCriteria(WizardConfigurationConfig.class).scroll();
			while (scr.next()) {
				BWizardConfigurationConfig bc = new BWizardConfigurationConfig(scr.get());
				bc.setSeqNo(i);
				bc.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			scr = hsu.createCriteria(WizardConfigurationConfig.class).scroll();
			while (scr.next()) {
				BWizardConfigurationConfig bc = new BWizardConfigurationConfig(scr.get());
				bc.setSeqNo(i);
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
	public ListAvailableCategoriesReturn listAvailableCategories(/*@WebParam(name = "in")*/final ListAvailableCategoriesInput in) {

		final ListAvailableCategoriesReturn ret = new ListAvailableCategoriesReturn();

		try {
			checkLogin(in);

			ret.setItem(BWizardConfiguration.listAvailableCategories(ret.getCap(), in.getWizardConfigurationId()));

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
	public NewWizardBenefitReturn newWizardBenefit(/*@WebParam(name = "in")*/final NewWizardBenefitInput in) {

		final NewWizardBenefitReturn ret = new NewWizardBenefitReturn();

		try {
			checkLogin(in);

			final BWizardConfigurationBenefit x = new BWizardConfigurationBenefit();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			if (in.getGenerateConfigs()) {
				ArahantSession.getHSU().flush();
				int countConfigs = 0;
				for (BHRBenefitConfig c : BWizardConfiguration.listAvailableConfigs(50, x.getWizardConfigurationBenefitId())) {
					BWizardConfigurationConfig wc = new BWizardConfigurationConfig();
					wc.create();
					wc.setBenefitConfig(c.getBean());
					wc.setName(c.getName());
					wc.setSeqNo(countConfigs++);
					wc.setWizardConfigurationBenefit(x.getBean());
					wc.insert();
				}
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAvailableConfigsReturn listAvailableConfigs(/*@WebParam(name = "in")*/final ListAvailableConfigsInput in) {
		final ListAvailableConfigsReturn ret = new ListAvailableConfigsReturn();
		try {
			checkLogin(in);

			ret.setItem(BWizardConfiguration.listAvailableConfigs(ret.getCap(), in.getBenefitWizardId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewWizardConfigReturn newWizardConfig(/*@WebParam(name = "in")*/final NewWizardConfigInput in) {
		final NewWizardConfigReturn ret = new NewWizardConfigReturn();
		try {
			checkLogin(in);

			final BWizardConfigurationConfig x = new BWizardConfigurationConfig();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveWizardConfigReturn saveWizardConfig(/*@WebParam(name = "in")*/final SaveWizardConfigInput in) {
		final SaveWizardConfigReturn ret = new SaveWizardConfigReturn();
		try {
			checkLogin(in);

			final BWizardConfigurationConfig x = new BWizardConfigurationConfig(in.getConfigWizardId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadWizardConfigReturn loadWizardConfig(/*@WebParam(name = "in")*/final LoadWizardConfigInput in) {
		final LoadWizardConfigReturn ret = new LoadWizardConfigReturn();
		try {
			checkLogin(in);

			ret.setData(new BWizardConfigurationConfig(in.getConfigWizardId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProposedConfigReturn loadProposedConfig(/*@WebParam(name = "in")*/final LoadProposedConfigInput in) {
		final LoadProposedConfigReturn ret = new LoadProposedConfigReturn();
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
	public ReorderConfigsReturn reorderConfigs(/*@WebParam(name = "in")*/final ReorderConfigsInput in) {
		final ReorderConfigsReturn ret = new ReorderConfigsReturn();
		try {
			checkLogin(in);

			BWizardConfigurationConfig c;

			int i = 1000;
			for (String s : in.getIds()) {
				c = new BWizardConfigurationConfig(s);
				c.setSeqNo(i);
				c.update();
				i++;
			}
			ArahantSession.getHSU().flush();

			i = 0;
			for (String s : in.getIds()) {
				c = new BWizardConfigurationConfig(s);
				c.setSeqNo(i);
				c.update();
				i++;
			}

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

			if (isEmpty(in.getWizardConfigurationId())) {
				ret.setOnboardingItem(BBenefitClass.listAvailableByWizardType('O', ret.getCap()));
				ret.setEnrollmentItem(BBenefitClass.listAvailableByWizardType('E', ret.getCap()));
			} else {
				ret.setOnboardingItem(BBenefitClass.list());
				ret.setEnrollmentItem(BBenefitClass.list());
			}
			ret.setOnboardingHasNull(ArahantSession.getHSU().createCriteria(WizardConfiguration.class).eqOrNull(WizardConfiguration.COMPANY_ID, ArahantSession.getHSU().getCurrentCompany().getCompanyId()).eq(WizardConfiguration.WIZARD_TYPE, 'O').isNull(WizardConfiguration.BENEFIT_CLASS).exists());
			ret.setEnrollmentHasNull(ArahantSession.getHSU().createCriteria(WizardConfiguration.class).eqOrNull(WizardConfiguration.COMPANY_ID, ArahantSession.getHSU().getCurrentCompany().getCompanyId()).eq(WizardConfiguration.WIZARD_TYPE, 'E').isNull(WizardConfiguration.BENEFIT_CLASS).exists());

			if (!isEmpty(in.getWizardConfigurationId()))
				ret.setSelectedId(new BWizardConfiguration(in.getWizardConfigurationId()).getBenefitClass() != null ? new BWizardConfiguration(in.getWizardConfigurationId()).getBenefitClass().getBenefitClassId() : "");
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListHrContactsReturn listHrContacts(/*@WebParam(name = "in")*/final ListHrContactsInput in) {
		final ListHrContactsReturn ret = new ListHrContactsReturn();
		try {
			checkLogin(in);

			List<Employee> admins = hsu.createCriteria(Employee.class).eq(Employee.HR_ADMIN, 'Y').list();
			ret.setItem(BEmployee.makeArray(admins));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitChangeReasonsReturn listBenefitChangeReasons(/*@WebParam(name = "in")*/final ListBenefitChangeReasonsInput in) {
		final ListBenefitChangeReasonsReturn ret = new ListBenefitChangeReasonsReturn();
		try {
			checkLogin(in);

			List<HrBenefitChangeReason> bcrs = hsu.createCriteria(HrBenefitChangeReason.class).orderBy(HrBenefitChangeReason.DESCRIPTION).list();

			ret.setItem(BHRBenefitChangeReason.makeArray(bcrs));

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

			ret.setItem(BHRBenefitCategory.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.EXCLUSIVE, 'Y').orderBy(HrBenefitCategory.SEQ).orderBy(HrBenefitCategory.DESCRIPTION).list()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitRestrictionsReturn listBenefitRestrictions(/*@WebParam(name = "in")*/final ListBenefitRestrictionsInput in) {
		final ListBenefitRestrictionsReturn ret = new ListBenefitRestrictionsReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitRestriction.list(null, null));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteBenefitRestrictionsReturn deleteBenefitRestrictions(/*@WebParam(name = "in")*/final DeleteBenefitRestrictionsInput in) {
		final DeleteBenefitRestrictionsReturn ret = new DeleteBenefitRestrictionsReturn();
		try {
			checkLogin(in);

			BBenefitRestriction.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewBenefitRestrictionReturn newBenefitRestriction(/*@WebParam(name = "in")*/final NewBenefitRestrictionInput in) {
		final NewBenefitRestrictionReturn ret = new NewBenefitRestrictionReturn();
		try {
			checkLogin(in);

			BBenefitRestriction br = new BBenefitRestriction();
			ret.setId(br.create());
			br.setBenefitCategory(ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).first());
			br.setBenefitChangeReason(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.ID, in.getBcrId()).first());
			br.insert();

			finishService(ret);
		} catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveBenefitRestrictionReturn saveBenefitRestriction(/*@WebParam(name = "in")*/final SaveBenefitRestrictionInput in) {
		final SaveBenefitRestrictionReturn ret = new SaveBenefitRestrictionReturn();
		try {
			checkLogin(in);

			BBenefitRestriction br = new BBenefitRestriction(in.getBenefitRestrictionId());
			br.setBenefitCategory(ArahantSession.getHSU().createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, in.getCategoryId()).first());
			br.setBenefitChangeReason(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.ID, in.getBcrId()).first());
			br.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewBenefitDependencyReturn newBenefitDependency(/*@WebParam(name = "in")*/final NewBenefitDependencyInput in) {
		final NewBenefitDependencyReturn ret = new NewBenefitDependencyReturn();
		try {
			checkLogin(in);

			BBenefitDependency bd = new BBenefitDependency();
			ret.setId(bd.create());
			bd.setRequiredBenefit(ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFITID, in.getRequiredBenefitId()).first());
			bd.setDependentBenefit(ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFITID, in.getDependentBenefitId()).first());
			bd.setRequired(in.getRequired());
			bd.setHidden(in.getHidden());
			bd.insert();

			finishService(ret);
		} catch (final Exception e) {
			ret.setId("");
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveBenefitDependencyReturn saveBenefitDependency(/*@WebParam(name = "in")*/final SaveBenefitDependencyInput in) {
		final SaveBenefitDependencyReturn ret = new SaveBenefitDependencyReturn();
		try {
			checkLogin(in);

			BBenefitDependency bd = new BBenefitDependency(in.getBenefitDependencyId());
			bd.setRequiredBenefit(ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFITID, in.getRequiredBenefitId()).first());
			bd.setDependentBenefit(ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFITID, in.getDependentBenefitId()).first());
			bd.setRequired(in.getRequired());
			bd.setHidden(in.getHidden());
			bd.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAvailableDependencyBenefitsForRequiredReturn listAvailableDependencyBenefitsForRequired(/*@WebParam(name = "in")*/final ListAvailableDependencyBenefitsForRequiredInput in) {
		final ListAvailableDependencyBenefitsForRequiredReturn ret = new ListAvailableDependencyBenefitsForRequiredReturn();
		try {
			checkLogin(in);

			HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);
			if (!isEmpty(in.getExcludeId()))
				hcu.ne(HrBenefit.BENEFITID, in.getExcludeId());
			if (!isEmpty(in.getName()))
				hcu.like(HrBenefit.NAME, in.getName());

			ret.setItem(BHRBenefit.makeArray(hcu.list()));

			if (!isEmpty(in.getBenefitDependencyId()))
				ret.setSelectedItem(new ListAvailableDependencyBenefitsReturnItem(new BHRBenefit(new BBenefitDependency(in.getBenefitDependencyId()).getRequiredBenefit())));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAvailableDependencyBenefitsForDependentReturn listAvailableDependencyBenefitsForDependent(/*@WebParam(name = "in")*/final ListAvailableDependencyBenefitsForDependentInput in) {
		final ListAvailableDependencyBenefitsForDependentReturn ret = new ListAvailableDependencyBenefitsForDependentReturn();
		try {
			checkLogin(in);

			HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);
			if (!isEmpty(in.getExcludeId()))
				hcu.ne(HrBenefit.BENEFITID, in.getExcludeId());
			if (!isEmpty(in.getName()))
				hcu.like(HrBenefit.NAME, in.getName());
			hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.EXCLUSIVE, 'N');

			ret.setItem(BHRBenefit.makeArray(hcu.list()));

			if (!isEmpty(in.getBenefitDependencyId()))
				ret.setSelectedItem(new ListAvailableDependencyBenefitsReturnItem(new BHRBenefit(new BBenefitDependency(in.getBenefitDependencyId()).getDependentBenefit())));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitDependenciesReturn listBenefitDependencies(/*@WebParam(name = "in")*/final ListBenefitDependenciesInput in) {
		final ListBenefitDependenciesReturn ret = new ListBenefitDependenciesReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitDependency.list(null, null));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteBenefitDependenciesReturn deleteBenefitDependencies(/*@WebParam(name = "in")*/final DeleteBenefitDependenciesInput in) {

		final DeleteBenefitDependenciesReturn ret = new DeleteBenefitDependenciesReturn();

		try {
			checkLogin(in);

			BBenefitDependency.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in) {
		final ListAssociatedOrgGroupsReturn ret = new ListAssociatedOrgGroupsReturn();
		try {
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListVendorGroupsReturn listVendorGroups(/*@WebParam(name = "in")*/final ListVendorGroupsInput in) {
		final ListVendorGroupsReturn ret = new ListVendorGroupsReturn();
		try {
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedGroups(hsu, in.getGroupId(), VENDOR_TYPE));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEmployeesForOrgGroupReturn listEmployeesForOrgGroup(/*@WebParam(name = "in")*/final ListEmployeesForOrgGroupInput in) {
		final ListEmployeesForOrgGroupReturn ret = new ListEmployeesForOrgGroupReturn();

		try {
			checkLogin(in);

			BEmployee[] emps = BEmployee.listEmployees(hsu, in.getGroupId(), in.getLastName(), in.getSupervisor(), ret.getCap());
			List<BEmployee> goodEmps = new ArrayList<BEmployee>();
			List<String> excludeIds = new ArrayList<String>();
//			excludeIds.addAll((List)hsu.createCriteria(ProjectTemplateBCRAssignment.class).eq(ProjectTemplateBCRAssignment.PROJECT_TYPE_ID, in.getProjectTypeId())
//																				   .joinTo(ProjectTemplateBCRAssignment.PERSON)
//																				   .isEmployee()
//																				   .selectFields(Person.PERSONID)
//																				   .list());

			if (in.getExcludeIds() != null && in.getExcludeIds().length != 0)
				excludeIds.addAll(Arrays.asList(in.getExcludeIds()));

			for (BEmployee be : emps)
				if (!excludeIds.contains(be.getPersonId()))
					goodEmps.add(be);
			emps = new BEmployee[goodEmps.size()];
			for (int i = 0; i < goodEmps.size(); i++)
				emps[i] = goodEmps.get(i);

			ret.setEmployees(emps);

			if (!isEmpty(in.getGroupId())) {
				BOrgGroup grp = new BOrgGroup(in.getGroupId());
				ret.setCanAddOrEdit(grp.getCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListContactsReturn listContacts(/*@WebParam(name = "in")*/final ListContactsInput in) {
		final ListContactsReturn ret = new ListContactsReturn();

		try {
			checkLogin(in);

			final BVendorContact[] vcs = BVendorContact.list(hsu, in.getGroupId(), in.getLastNameStartsWith(), in.getPrimary(), ret.getCap(), in.getExcludeIds());

			ret.setPersons(vcs, in.getGroupId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public AssignToDefaultProjectReturn assignToDefaultProject(/*@WebParam(name = "in")*/final AssignToDefaultProjectInput in) {
		final AssignToDefaultProjectReturn ret = new AssignToDefaultProjectReturn();
		try {
			checkLogin(in);

			BWizardConfiguration wc = new BWizardConfiguration(in.getWizardConfigurationId());

			ArahantSession.getHSU().createCriteria(WizardConfigurationProjectAssignment.class).eq(WizardConfigurationProjectAssignment.WIZARD_CONFIG, wc.getBean()).delete();

			for (String id : in.getEmployeeIds()) {
				BWizardConfigurationProjectAssignment wcpa = new BWizardConfigurationProjectAssignment();
				wcpa.create();
				wcpa.setPerson(new BPerson(id).getPerson());
				wcpa.setWizardConfig(wc.getBean());
				wcpa.insert();
			}

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
				if (b.getBean() != null)
					ret.setSelectedItem(new SearchBenefitsForRiderReturnItem(new BHRBenefit(b.getRiderBenefit())));
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

	@WebMethod()
	public ListBenefitQuestionsReturn listBenefitQuestions(/*@WebParam(name = "in")*/final ListBenefitQuestionsInput in) {
		final ListBenefitQuestionsReturn ret = new ListBenefitQuestionsReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitQuestion.list(in.getBenefitId(), null, false));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteQuestionsReturn deleteQuestions(/*@WebParam(name = "in")*/final DeleteQuestionsInput in) {
		final DeleteQuestionsReturn ret = new DeleteQuestionsReturn();
		try {
			checkLogin(in);

			for (String id : in.getIds())
				new BBenefitQuestion(id).delete();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewQuestionReturn newQuestion(/*@WebParam(name = "in")*/final NewQuestionInput in) {
		final NewQuestionReturn ret = new NewQuestionReturn();
		try {
			checkLogin(in);

			final BBenefitQuestion x = new BBenefitQuestion();
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
	public SaveQuestionReturn saveQuestion(/*@WebParam(name = "in")*/final SaveQuestionInput in) {
		final SaveQuestionReturn ret = new SaveQuestionReturn();
		try {
			checkLogin(in);

			final BBenefitQuestion x = new BBenefitQuestion(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReorderQuestionReturn reorderQuestion(/*@WebParam(name = "in")*/final ReorderQuestionInput in) {
		final ReorderQuestionReturn ret = new ReorderQuestionReturn();
		try {
			checkLogin(in);

			short count = 1000;
			String benefitId = null;
			for (String id : in.getIds()) {
				final BBenefitQuestion bq = new BBenefitQuestion(id);
				bq.getBean().setQuestionOrder(count++);
				benefitId = bq.getBenefitId();
				bq.update();
			}
			if (!isEmpty(benefitId))
				for (BenefitQuestion q : ArahantSession.getHSU().createCriteria(BenefitQuestion.class).eq(BenefitQuestion.BENEFIT_ID, benefitId).notIn(BenefitQuestion.ID, in.getIds()).list()) {
					final BBenefitQuestion bq = new BBenefitQuestion(q);
					bq.getBean().setQuestionOrder(count++);
					bq.update();
				}

			ArahantSession.getHSU().flush();

			count = 0;
			for (String id : in.getIds()) {
				final BBenefitQuestion bq = new BBenefitQuestion(id);
				bq.getBean().setQuestionOrder(count++);
				bq.update();
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadQuestionReturn loadQuestion(/*@WebParam(name = "in")*/final LoadQuestionInput in) {
		final LoadQuestionReturn ret = new LoadQuestionReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitQuestionChoice.list(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
