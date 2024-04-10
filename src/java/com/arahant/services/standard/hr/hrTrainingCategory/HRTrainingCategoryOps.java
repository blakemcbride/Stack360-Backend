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

package com.arahant.services.standard.hr.hrTrainingCategory;

import com.arahant.business.*;
import com.arahant.reports.HRTrainingCategoryReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created on Feb 22, 2007
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrTrainingCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HRTrainingCategoryOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(HRTrainingCategoryOps.class);

    public HRTrainingCategoryOps() {
        super();
    }

    @WebMethod()
    public DeleteTrainingCategoriesReturn deleteTrainingCategories(/*@WebParam(name = "in")*/final DeleteTrainingCategoriesInput in) {
        final DeleteTrainingCategoriesReturn ret = new DeleteTrainingCategoriesReturn();
        try {
            checkLogin(in);

            BHRTrainingCategory.delete(hsu, in.getIds());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public GetTrainingCategoriesReportReturn getTrainingCategoriesReport(/*@WebParam(name = "in")*/final GetTrainingCategoriesReportInput in) {
        final GetTrainingCategoriesReportReturn ret = new GetTrainingCategoriesReportReturn();
        try {
            checkLogin(in);

            ret.setFileName(new HRTrainingCategoryReport().build(hsu, in.getActiveType()));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListTrainingCategoriesReturn listTrainingCategories(/*@WebParam(name = "in")*/final ListTrainingCategoriesInput in) {
        final ListTrainingCategoriesReturn ret = new ListTrainingCategoriesReturn();
        try {
            checkLogin(in);

            ret.setItem(BHRTrainingCategory.list(in.getActiveType(), in.getClientId()));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public NewTrainingCategoryReturn newTrainingCategory(/*@WebParam(name = "in")*/final NewTrainingCategoryInput in) {
        final NewTrainingCategoryReturn ret = new NewTrainingCategoryReturn();

        try {
            checkLogin(in);

            final BHRTrainingCategory trainingCategory = new BHRTrainingCategory();

            ret.setId(trainingCategory.create());

            // If the company is the tenant, set it to null.
            if (in.getClientId().equals(hsu.getCurrentCompany().getOrgGroupId()))
                in.setData(trainingCategory, null);
            else {
                final BCompanyBase company = BClientCompany.get(in.getClientId());
                if (company != null)
                    in.setData(trainingCategory, company.getBean());
                else
                    in.setData(trainingCategory, null);
            }

            trainingCategory.insert();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public SaveTrainingCategoryReturn saveTrainingCategory(/*@WebParam(name = "in")*/final SaveTrainingCategoryInput in) {
        final SaveTrainingCategoryReturn ret = new SaveTrainingCategoryReturn();

        try {
            checkLogin(in);

            final BHRTrainingCategory trainingCategory = new BHRTrainingCategory(in.getId());

            // If the company is the tenant, set it to null.
            if (in.getClientId().equals(hsu.getCurrentCompany().getOrgGroupId()))
                in.setData(trainingCategory, null);
            else {
                final BCompanyBase company = BClientCompany.get(in.getClientId());
                if (company != null)
                    in.setData(trainingCategory, company.getBean());
                else
                    in.setData(trainingCategory, null);
            }

            trainingCategory.update();

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
    public GetClientsListReturn getClientsList(final GetClientsListInput in) {
        final GetClientsListReturn ret = new GetClientsListReturn();

        try {
            checkLogin(in);

            // Get the companies array.
            BClientCompany[] clientCompanies = BClientCompany.listActiveClients(1000);

            // Sort the companies by name.
            Arrays.sort(clientCompanies, Comparator.comparing(BCompanyBase::getName));

            ret.setGetClientsListItems(hsu.getCurrentCompany(),clientCompanies);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public SearchClientByNameReturn searchClientByName(SearchClientByNameInput in) {
        final SearchClientByNameReturn ret = new SearchClientByNameReturn();

        try {
            checkLogin(in);

            ret.setClients(BCompanyBase.searchCompanySpecific(in.getName(), false, true, ret.getHighCap()));

            if (in.getClientId().equals("")) {
                ret.setSelectedItem(new SearchClientByNameReturnItem(BCompanyBase.get(hsu.getCurrentCompany().getOrgGroupId())));
            } else {
                ret.setSelectedItem(new SearchClientByNameReturnItem(BCompanyBase.get(in.getClientId())));
            }

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }
}

/*
 * HrTrainingCategory		HumanResourcesManagementService.deleteTrainingCategoriesObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.getTrainingCategoryReportObj	*** MISSING																																																																																																																																																																																																																																																												
HumanResourcesManagementService.listTrainingCategoriesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
HumanResourcesManagementService.newTrainingCategoryObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.saveTrainingCategoryObj																																																																																																																																																																																																																																																													
SecurityManagementService.checkRights	*** MISSING AccessHRSetups																																																																																																																																																																																																																																																												

 */	
