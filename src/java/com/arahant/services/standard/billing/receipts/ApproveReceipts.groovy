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

package com.arahant.services.standard.billing.receipts

import com.arahant.business.BExpenseReceipt
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/20/18
 */
class ApproveReceipts {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        JSONArray ids = injson.getJSONArray("expense_receipt_ids")
        boolean approve = injson.getBoolean("approve")
        int len = ids.length()
        for (int i=0 ; i < len ; i++) {
            String id = ids.getString(i)
            BExpenseReceipt ber = new BExpenseReceipt(id)
            if (ber.getApproved().toString() == "Y"  &&  !approve) {
                ber.setApproved("N".charAt(0))
                ber.setWhoApproved(null)
                ber.setWhenApproved(null)
            } else if (ber.getApproved().toString() == "N"  &&  approve) {
                ber.setApproved("Y".charAt(0))
                ber.setWhoApproved(hsu.getCurrentPerson())
                ber.setWhenApproved(new Date())
            }
            ber.update()
        }
    }

}
