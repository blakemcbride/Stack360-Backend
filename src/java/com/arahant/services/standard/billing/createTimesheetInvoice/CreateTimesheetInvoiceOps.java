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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.business.*;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.utils.DateUtils;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardBillingCreateTimesheetInvoiceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class CreateTimesheetInvoiceOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(CreateTimesheetInvoiceOps.class);

	@WebMethod()
	public ListARAccountsReturn listARAccounts(/*@WebParam(name = "in")*/final ListARAccountsInput in) {
		final ListARAccountsReturn ret = new ListARAccountsReturn();

		try {
			checkLogin(in);

			ret.setGLAccounts(BGlAccount.listByType(hsu, 1));

			// TODO needs to be changed to determine correct company
			BCompany company = BCompany.listCompanies(hsu)[0];
			ret.setCompanyUsesAR(company.getAccountingBasis() == 'A');
			if (ret.getCompanyUsesAR())
				ret.setDefaultAccountId(company.getARAccountId());
			
			BClientCompany bcc = new BClientCompany(in.getClientCompanyId());
			ret.setPaymentTerms(bcc.getPaymentTerms());

			finishService(ret);

		} catch (final Throwable e) {
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

			if (!isEmpty(in.getServiceId()))
				try {
					ret.setSelectedItem(new SearchServicesReturnItem(new BService(in.getServiceId())));
				} catch (Exception e) {
				}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewCompleteInvoiceReturn newCompleteInvoice(/*@WebParam(name = "in")*/final NewCompleteInvoiceInput in) {
		final NewCompleteInvoiceReturn ret = new NewCompleteInvoiceReturn();

		try {
			checkLogin(in);

			final BInvoice bi = new BInvoice();
			ret.setInvoiceId(bi.create(DateUtils.getDate(in.getInvoiceDate())));

			in.makeInvoice(bi);

			bi.update();

			finishService(ret);

		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public RejectTimeReturn rejectTime(/*@WebParam(name = "in")*/final RejectTimeInput in) {
		final RejectTimeReturn ret = new RejectTimeReturn();
		try {
			checkLogin(in);

			BTimesheet.rejectTimesheets(in.getTimesheetId(), in.getMessage());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in) {
		final SearchCompaniesReturn ret = new SearchCompaniesReturn();
		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.searchBillingState(hsu, in.getName(), in.getIdentifier(), in.getBillableItemsIndicator(), ret.getHighCap(), in.getBillableFromDate()),
					in.getBillableFromDate());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchBillableTimesheetsReturn searchBillableTimesheets(/*@WebParam(name = "in")*/final SearchBillableTimesheetsInput in) {
		final SearchBillableTimesheetsReturn ret = new SearchBillableTimesheetsReturn();
		try {
			checkLogin(in);

			BSearchMetaInput sm = in.getSearchMetaInput();
			if (in.getGetAll())
				sm.setUsingPaging(false);
			ret.setItem(BTimesheet.search(sm, in.getBillableItemsIndicator(), in.getCompanyId(), in.getExcludedTimesheetIds(),
					in.getPersonId(), in.getProjectId(), in.getTimesheetEndDate(), in.getTimesheetStartDate(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();
		try {
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(), in.getLastName(), "", ret.getHighCap()));

			if (!isEmpty(in.getPersonId()))
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getPersonId())));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, null, false, in.getUser(), null, null, ret.getCap()));

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

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public MarkTimesheetsDeferredReturn markTimesheetsDeferred(/*@WebParam(name = "in")*/final MarkTimesheetsDeferredInput in) {
		final MarkTimesheetsDeferredReturn ret = new MarkTimesheetsDeferredReturn();
		try {
			checkLogin(in);

			BTimesheet.markDeferred(in.getTimesheetIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private int shareParent(String parentId, String[] projectIds) {
		//2 means all match
		//1 means some match
		//0 means none match

		int count = 0;

		for (String id : projectIds) {
			BProject obp = new BProject(id);
			if (parentId.equals(obp.getTopBillingProjectId()))
				count++;
		}


		if (count == 0)
			return 0;

		if (count == projectIds.length)
			return 2;

		return 1;
	}

	@WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in) {
		final GetProjectDetailReturn ret = new GetProjectDetailReturn();
		try {
			checkLogin(in);

			//find a parent

			final BProject bp = new BProject(in.getProjectIds()[0]);

			// For fixed-priced projects
			ret.setFixedPrice(bp.getFixedPriceAmount());
			ret.setInvoicedAmount(bp.amountPreviouslyInvoiced());

			String parentId = bp.getTopBillingProjectId();


			double adjustment = 0;

			if (shareParent(parentId, in.getProjectIds()) < 2)
				ret.setFound(false);
			else {

				//if some of them are part of the same billable tree as the original projectIds and some of them are not,
				//stop and return found = false with no hours (just like if original projectIds array is "mixed-mode")
				boolean found = true;
				for (GetProjectDetailInputItem i : in.getLineItemDetails())
					if (shareParent(parentId, i.getProjectIds()) == 1) {
						found = false;
						break;
					}

				if (!found)
					ret.setFound(found);
				else {

					for (GetProjectDetailInputItem i : in.getLineItemDetails())
						if (shareParent(parentId, i.getProjectIds()) == 2)
							//f all of them are part of the same billable project tree as 
							//the original projectIds array, deduct corresponding line 
							//item's invoicedHours from main invoicedHours return at top level
							adjustment += i.getInvoicedHours();


					double estHours = 0;
					double invoicedHours = adjustment;
					double remainHours = -adjustment;

					for (String id : in.getProjectIds()) {
						BProject obp = new BProject(id);

						estHours += obp.getEstimateHours();
						invoicedHours += obp.getInvoicedHours();

						if (estHours > 0)
							remainHours += obp.getEstimateHours() - obp.getInvoicedHours();
					}

					ret.setFound(true);

					if (remainHours < 0)
						remainHours = 0;

					ret.setEstimatedHours(estHours);
					ret.setInvoicedHours(invoicedHours);
					ret.setParentProjectName(new BProject(parentId).getProjectName());
					ret.setRemainingEstimatedHours(remainHours);

					ret.setRemainingEstimatedRate(bp.getCalculatedBillingRate(null));
					ret.setRemainingEstimatedAmount(remainHours * bp.getCalculatedBillingRate(null));
				}
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetProjectSummaryReturn getProjectSummary(/*@WebParam(name = "in")*/final GetProjectSummaryInput in) {
		final GetProjectSummaryReturn ret = new GetProjectSummaryReturn();
		try {
			checkLogin(in);

			ret.setData(new BProject(in.getProjectId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetProjectDetailForToolTipReturn getProjectDetailForToolTip(/*@WebParam(name = "in")*/final GetProjectDetailForToolTipInput in) {
		final GetProjectDetailForToolTipReturn ret = new GetProjectDetailForToolTipReturn();
		try {
			checkLogin(in);

			ret.setData(new BProject(in.getProjectId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
