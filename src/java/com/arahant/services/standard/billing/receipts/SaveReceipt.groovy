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

import com.arahant.business.BExpenseAccount
import com.arahant.business.BExpenseReceipt
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/16/18
 */
class SaveReceipt {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        BExpenseReceipt ber = new BExpenseReceipt(injson.getString("expense_receipt_id"))
        ber.setExpenseAccount((new BExpenseAccount(injson.getString("expense_account_id"))).getBean())
        ber.setReceiptDate(injson.getInt("receipt_date"))
        ber.setAmount(injson.getDouble("expense_amount"))
        ber.setBusinessPurpose(injson.getString("business_purpose"))
        ber.setPaymentMethod(injson.getString("payment_method").charAt(0))
        if (ber.getApproved().toString() == "Y"  &&  injson.getString("approved") == "N") {
            ber.setApproved("N".charAt(0))
            ber.setWhoApproved(null)
            ber.setWhenApproved(null)
        } else if (ber.getApproved().toString() == "N"  &&  injson.getString("approved") == "Y") {
            ber.setApproved("Y".charAt(0))
            ber.setWhoApproved(hsu.getCurrentPerson())
            ber.setWhenApproved(new Date())
        }

        ber.update()
    }

}
