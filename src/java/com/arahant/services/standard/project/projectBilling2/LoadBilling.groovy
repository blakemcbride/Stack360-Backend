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

package com.arahant.services.standard.project.projectBilling2

import com.arahant.business.BProject
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/2/21
 */
class LoadBilling {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("projectId")
        Connection db = KissConnection.get()

        db.fetchOneJSON(outjson, """select billable, billing_type, fixed_price_amount, billing_rate, dollar_cap, rate_type_id,
                                           purchase_order, estimate_hours, project_days
                                    from project 
                                    where project_id = ?""", projectId)
        BProject bp = new BProject(projectId)
        outjson.put('default_billing_rate', bp.getDefaultBillingRateFormatted())
        outjson.put('billable_hours', bp.getBillableHours())
        outjson.put('nonbillable_hours', bp.getNonBillableHours())
        outjson.put('rate_type_id', bp.getRateType().getId())
        Record rec = db.fetchOne("select count(*) from invoice_line_item where project_id = ?", projectId)
        long count = rec.getLong("count")
        outjson.put("has_been_invoiced", count > 0)

        Double actualManHours = db.fetchOne("""select SUM(ts.total_hours) total_hours
                                               from timesheet ts
                                               join project_shift ps
                                                 on ts.project_shift_id = ps.project_shift_id
                                               where ps.project_id = ?
                                               and ts.billable = 'Y'""", projectId).getDouble("total_hours")
        if (actualManHours == null)
            actualManHours = 0.0
        outjson.put("actual_man_hours", actualManHours)
    }
}
