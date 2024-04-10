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

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/5/21
 */
class SaveBilling {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String projectId = injson.getString("projectId")
        Connection db = KissConnection.get()
        Record rec = db.fetchOne("""select *
                                    from project 
                                    where project_id = ?""", projectId)
        rec.set 'billable', injson.getCharacter('billable')
        rec.set 'billing_type', injson.getCharacter('billingType')
        rec.set 'billing_rate', injson.getFloat('billingRate')
        rec.set 'dollar_cap', injson.getDouble('dollarCap')
        rec.set 'fixed_price_amount', injson.getDouble('fixedPrice')
        rec.set 'rate_type_id', injson.getString('rateTypeId')
        rec.set 'purchase_order', injson.getString('purchaseOrder')
        rec.set 'product_id', injson.getString('serviceId')
        rec.set 'estimate_hours', injson.getDouble('estimateHours')
        rec.set 'project_days', injson.getInt('projectDays')
        rec.update()
    }

}
