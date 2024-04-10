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

import com.arahant.beans.ExpenseReceipt
import com.arahant.business.BPerson
import com.arahant.servlets.REST
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/13/18
 */
class SearchReceipts {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String person_id = injson.getString "person_id"
        String project_id = injson.getString "project_id"
        int start_date = injson.getInt "start_date"
        int end_date = injson.getInt "end_date"
        String expense_account_id = injson.getString "expense_account_id"


        HibernateCriteriaUtil<ExpenseReceipt> crit = hsu.createCriteria(ExpenseReceipt.class)
        if (person_id)
            crit.eq(ExpenseReceipt.PERSON_ID, person_id)
        if (project_id)
            crit.eq(ExpenseReceipt.PROJECT_ID, project_id)
        if (start_date)
            crit.ge(ExpenseReceipt.RECEIPT_DATE, start_date)
        if (end_date)
            crit.le(ExpenseReceipt.RECEIPT_DATE, end_date)
        if (expense_account_id)
            crit.eq(ExpenseReceipt.EXPENSE_ACCOUNT_ID, expense_account_id)
        crit.orderBy(ExpenseReceipt.RECEIPT_DATE)
        crit.setMaxResults(100)
        List<ExpenseReceipt> expreceipts = crit.list()

        JSONArray ary = new JSONArray()
        for (ExpenseReceipt exp : expreceipts) {
            JSONObject jobj = new JSONObject()
            jobj.put "expense_receipt_id", exp.getExpenseReceiptId()
            jobj.put "person_name", (new BPerson(exp.getPersonId())).getNameLFM()
            jobj.put "person_id", exp.getPersonId()
            jobj.put "project_id", exp.getProjectId()
            jobj.put "project_description", exp.getProject().getDescription()
            jobj.put "expense_account", exp.getExpenseAccount().getDescription()
            jobj.put "expense_account_id", exp.getExpenseAccountId()
            jobj.put "receipt_date", exp.getReceiptDate()
            jobj.put "expense_amount", exp.getAmount()
            jobj.put "business_purpose", exp.getBusinessPurpose()
            jobj.put "who_uploaded", (new BPerson(exp.getWhoUploadedId())).getNameLFM()
            jobj.put "when_uploaded", exp.getWhenUploaded()
            jobj.put "payment_method", exp.getPaymentMethod()
            jobj.put "approved", exp.getApproved()
            jobj.put "who_approved", exp.getWhoApproved()?.getNameLFM()
            jobj.put "when_approved", exp.getWhenApproved()
            ary.put(jobj)
        }
        outjson.put("expense_receipts", ary)
    }

}
