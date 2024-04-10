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

package com.arahant.services.standard.billing.expenseAccounts

import com.arahant.business.BExpenseAccount
import com.arahant.business.BGlAccount
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/4/18
 */
class UpdateExpenseAccount {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String expenseAccountId = injson.getString("expense_account_id")
        String expenseId = injson.getString("expense_id")
        String description = injson.getString("description")
        String glAccountId = injson.getString("gl_account_id")

        BExpenseAccount bea = new BExpenseAccount(expenseAccountId)
        bea.setExpenseId(expenseId)
        bea.setDescription(description)
        bea.setGlAccount((new BGlAccount(glAccountId)).getBean())
        bea.update()
    }

}
