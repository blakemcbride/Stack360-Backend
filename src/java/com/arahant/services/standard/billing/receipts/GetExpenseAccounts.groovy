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

import com.arahant.beans.ExpenseAccount
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/13/18
 */
class GetExpenseAccounts {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        List<ExpenseAccount> accts = hsu.createCriteria(ExpenseAccount.class).orderBy(ExpenseAccount.DESCRIPTION).list()
        JSONArray ary = new JSONArray()
        for (ExpenseAccount exp : accts) {
            JSONObject jobj = new JSONObject()

            jobj.put "expense_account_id", exp.getExpenseAccountId()
            jobj.put "expense_id", exp.getExpenseId()
            jobj.put "description", exp.getDescription()
            ary.put(jobj)
        }
        outjson.put("expense_accounts", ary)
    }

}
