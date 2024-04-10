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

package com.arahant.services.standard.hr.hrTraining;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created on Feb 25, 2007
 * Modified on Dec 12, 2018
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrTrainingOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HRTrainingOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            HRTrainingOps.class);

    public HRTrainingOps() {
        super();
    }

    @WebMethod()
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

        try {
            checkLogin(in);

            ret.setAccessLevel(BRight.checkRight("HRTraining"));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public DeleteTrainingDetailsReturn deleteTrainingDetails(/*@WebParam(name = "in")*/final DeleteTrainingDetailsInput in) {
        final DeleteTrainingDetailsReturn ret = new DeleteTrainingDetailsReturn();
        try {
            checkLogin(in);

            BHRTrainingDetail.delete(hsu, in.getIds());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public GetTrainingDetailsReportReturn getTrainingDetailsReport(/*@WebParam(name = "in")*/final GetTrainingDetailsReportInput in) {
        final GetTrainingDetailsReportReturn ret = new GetTrainingDetailsReportReturn();
        try {
            checkLogin(in);

            ret.setFileName(BHRTrainingDetail.getReport(hsu, in.getEmployeeId()));

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

            // Select only the active training categories for the given client.
            List<BHRTrainingCategory> trainingCategories = new ArrayList<>();

            // Check for tenant records. They are saved with a null clientIds in the database.
            String effectiveClientId = null;
            if (!hsu.getCurrentCompany().getOrgGroupId().equals(in.getClientId()))
                effectiveClientId = in.getClientId();
            Collections.addAll(trainingCategories, BHRTrainingCategory.list(1, effectiveClientId));

//            if (!isEmpty(in.getId())) {
//                BHRTrainingDetail trainingDetail = new BHRTrainingDetail(in.getId());
//                boolean add = true;
//
//                String checkId = trainingDetail.getTrainingCategoryId();
//                for (BHRTrainingCategory trainingCategory : trainingCategories) {
//                    if (trainingCategory.getTrainin   gCategoryId().equals(checkId))
//                        add = false;
//                }
//
//                if (add)
//                    trainingCategories.add(new BHRTrainingCategory(checkId));
//            }

            ret.setItem(trainingCategories.toArray(new BHRTrainingCategory[0]));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public ListTrainingDetailsReturn listTrainingDetails(/*@WebParam(name = "in")*/final ListTrainingDetailsInput in) {
        final ListTrainingDetailsReturn ret = new ListTrainingDetailsReturn();

        try {
            checkLogin(in);

            // Get the training details for the employee.
            BHRTrainingDetail[] trainingDetails = new BEmployee(in.getEmployeeId()).listTrainingDetails();
            List<ListTrainingDetailsItem> trainingDetailsItems = new ArrayList<>();

            Arrays.asList(trainingDetails).forEach(detail -> {
                BHRTrainingCategory category = new BHRTrainingCategory(detail.getTrainingCategoryId());

                if (category.getClient() == null)
                    trainingDetailsItems.add(new ListTrainingDetailsItem(detail, hsu.getCurrentCompany()));
                else
                    trainingDetailsItems.add(new ListTrainingDetailsItem(detail, category.getClient()));
            });

            ret.setItem(trainingDetailsItems.toArray(new ListTrainingDetailsItem[0]));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public LoadTrainingDetailReturn loadTrainingDetail(/*@WebParam(name = "in")*/final LoadTrainingDetailInput in) {
        final LoadTrainingDetailReturn ret = new LoadTrainingDetailReturn();
        try {
            checkLogin(in);

            ret.setData(new BHRTrainingDetail(in.getId()));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public NewTrainingDetailReturn newTrainingDetail(/*@WebParam(name = "in")*/final NewTrainingDetailInput in) {
        final NewTrainingDetailReturn ret = new NewTrainingDetailReturn();
        try {
            checkLogin(in);
            final BHRTrainingDetail td = new BHRTrainingDetail();
            ret.setId(td.create());
            in.setData(td);
            td.insert();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public SaveTrainingDetailReturn saveTrainingDetail(/*@WebParam(name = "in")*/final SaveTrainingDetailInput in) {
        final SaveTrainingDetailReturn ret = new SaveTrainingDetailReturn();
        try {
            checkLogin(in);
            final BHRTrainingDetail td = new BHRTrainingDetail(in.getTrainingDetailId());
            in.setData(td);
            td.update();

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

            ret.setClients(BCompanyBase.searchCompanySpecific(in.getName(), false, ret.getHighCap()));

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
HrTraining		HumanResourcesManagementService.deleteTrainingDetailsObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.getTrainingDetailsReportObj	*** MISSING																																																																																																																																																																																																																																																												
HumanResourcesManagementService.listTrainingCategoriesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
HumanResourcesManagementService.listTrainingDetailsObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
HumanResourcesManagementService.loadTrainingDetailObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.newTrainingDetailObj																																																																																																																																																																																																																																																													
HumanResourcesManagementService.saveTrainingDetailObj																																																																																																																																																																																																																																																													
SecurityManagementService.checkRights	*** MISSING AccessHR																																																																																																																																																																																																																																																												
*/
