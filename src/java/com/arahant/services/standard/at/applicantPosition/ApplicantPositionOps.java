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


/**
 */
package com.arahant.services.standard.at.applicantPosition;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ApplicantPositionReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.List;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardAtApplicantPositionOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ApplicantPositionOps extends ServiceBase {

    private static final transient ArahantLogger logger = new ArahantLogger(
            ApplicantPositionOps.class);

    public ApplicantPositionOps() {
        super();
    }

    @WebMethod()
    public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in) {
        final ListPositionsReturn ret = new ListPositionsReturn();
        try {
            checkLogin(in);
            BApplicantPosition[] items = BApplicantPosition.search(in.getAcceptingFrom(), in.getAcceptingTo(),
                    in.getJobStartFrom(), in.getJobStartTo(), in.getIncludeAccepting(), in.getIncludeCancelled(),
                    in.getIncludeFilled(), in.getIncludeNew(), in.getIncludeSuspended(), in.getOrgGroupId(),
                    in.getPositionId(), 0);
            ret.setItem(items);

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListHrPositionsReturn listHrPositions(/*@WebParam(name = "in")*/final ListHrPositionsInput in) {
        final ListHrPositionsReturn ret = new ListHrPositionsReturn();
        try {
            checkLogin(in);

            final int today = DateUtils.today();

            Connection db = hsu.getKissConnection();
            List<Record> recs = db.fetchAll("select p.position_id, p.position_name " +
                    "from hr_position p " +
                    "where (p.first_active_date = 0 or p.first_active_date <= ?) " +
                    "      and (p.last_active_date = 0 or p.last_active_date >=?) " +
                    "order by p.seqno, p.position_name", today, today);
            ListHrPositionsReturnItem[] items = new ListHrPositionsReturnItem[recs.size()];
            for (int i = 0; i < recs.size(); i++) {
                Record rec = recs.get(i);
                items[i] = new ListHrPositionsReturnItem();
                items[i].setPositionId(rec.getString("position_id"));
                items[i].setPositionName(rec.getString("position_name"));
            }
            ret.setItem(items);

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

            ret.setRights();

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in) {
        final SearchOrgGroupsReturn ret = new SearchOrgGroupsReturn();
        try {
            checkLogin(in);

            int type = COMPANY_TYPE;

            if (BProperty.getBoolean("ShowClientsInApplicantGroups"))
                type |= CLIENT_TYPE;

            ret.setItem(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), 0, type, ret.getHighCap()));
            if (!isEmpty(in.getId())) {
                if (ret.getItem().length <= ret.getLowCap()) {
                    //if it's in the search, set selected item
                    for (SearchOrgGroupsReturnItem ogri : ret.getItem())
                        if (in.getId().equals(ogri.getId()))
                            ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
                } else {
                    for (BOrgGroup borg : BPerson.getCurrent().searchSubordinateGroups(hsu, in.getName(), 0))
                        if (in.getId().equals(borg.getOrgGroupId()))
                            ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getId())));
                }

            }

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public NewPositionReturn newPosition(/*@WebParam(name = "in")*/final NewPositionInput in) {
        final NewPositionReturn ret = new NewPositionReturn();
        try {
            checkLogin(in);

            final BApplicantPosition x = new BApplicantPosition();
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
    public SavePositionReturn savePosition(/*@WebParam(name = "in")*/final SavePositionInput in) {
        final SavePositionReturn ret = new SavePositionReturn();
        try {
            checkLogin(in);

            final BApplicantPosition x = new BApplicantPosition(in.getId());
            in.setData(x);
            x.update();

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public DeletePositionsReturn deletePositions(/*@WebParam(name = "in")*/final DeletePositionsInput in) {
        final DeletePositionsReturn ret = new DeletePositionsReturn();
        try {
            checkLogin(in);

            BApplicantPosition.delete(in.getIds());

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

            ret.setReportUrl(new ApplicantPositionReport().build(in.getAcceptingFrom(), in.getAcceptingTo(),
                    in.getIncludeAccepting(), in.getIncludeCancelled(), in.getIncludeFilled(), in.getIncludeNew(),
                    in.getIncludeSuspended(), in.getJobStartFrom(), in.getJobStartTo(), in.getJobTypeId(),
                    in.getOrgGroupId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListPositionInformationReturn listPositionInformation(/*@WebParam(name = "in")*/final ListPositionInformationInput in) {
        final ListPositionInformationReturn ret = new ListPositionInformationReturn();
        try {
            checkLogin(in);

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }
}
