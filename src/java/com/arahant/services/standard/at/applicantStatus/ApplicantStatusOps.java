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

package com.arahant.services.standard.at.applicantStatus;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ApplicantStatusReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardAtApplicantStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ApplicantStatusOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            ApplicantStatusOps.class);

    public ApplicantStatusOps() {
        super();
    }

    @WebMethod()
    public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in) {
        final ListStatusesReturn ret = new ListStatusesReturn();
        try {
            checkLogin(in);

            ret.setItem(BApplicantStatus.list(in.getActiveType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
        final GetReportReturn ret = new GetReportReturn();
        try {
            checkLogin(in);

            ret.setReportUrl(new ApplicantStatusReport().build(in.getActiveType()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public NewStatusReturn newStatus(/*@WebParam(name = "in")*/final NewStatusInput in) {
        final NewStatusReturn ret = new NewStatusReturn();
        try {
            checkLogin(in);

            final BApplicantStatus x = new BApplicantStatus();
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
    public SaveStatusReturn saveStatus(/*@WebParam(name = "in")*/final SaveStatusInput in) {
        final SaveStatusReturn ret = new SaveStatusReturn();
        try {
            checkLogin(in);

            final BApplicantStatus x = new BApplicantStatus(in.getId());
            in.setData(x);
            x.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public DeleteStatusesReturn deleteStatuses(/*@WebParam(name = "in")*/final DeleteStatusesInput in) {
        final DeleteStatusesReturn ret = new DeleteStatusesReturn();
        try {
            checkLogin(in);

            BApplicantStatus.delete(in.getIds());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public MoveStatusReturn moveStatus(/*@WebParam(name = "in")*/final MoveStatusInput in) {
        final MoveStatusReturn ret = new MoveStatusReturn();
        try {
            checkLogin(in);

            final BApplicantStatus x = new BApplicantStatus(in.getId());
            in.setData(x);
            x.update();

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

            ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }
}
