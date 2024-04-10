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


import com.arahant.beans.GlAccount
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/3/18
 */
class GetGLAccounts {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        List<GlAccount> accts = hsu.createCriteria(GlAccount.class)
                .in(GlAccount.ACCOUNTTYPE, [24, 103])  //  expense, and other expense types
                .orderBy(GlAccount.ACCOUNTNAME)
                .list()
        JSONArray ary = new JSONArray()
        for (GlAccount acct in accts) {
            JSONObject jobj = new JSONObject()
            jobj.put("gl_account_id", acct.getGlAccountId())
            jobj.put("account_number", acct.getAccountNumber())
            jobj.put("account_name", acct.getAccountName())
            jobj.put("account_type", acct.getAccountType())
            ary.put(jobj)
        }
        outjson.put("gl_accounts", ary)
    }

}
