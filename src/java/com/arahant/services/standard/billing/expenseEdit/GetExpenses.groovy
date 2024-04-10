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

package com.arahant.services.standard.billing.expenseEdit

import com.arahant.beans.Expense
import com.arahant.servlets.REST
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/3/18
 */
class GetExpenses {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String person_id = injson.getString("person_id")
        String project_id = injson.getString("project_id")
        HibernateCriteriaUtil<Expense> hcu = hsu.createCriteria(Expense.class)
                .eq(Expense.EMPLOYEE_ID, person_id)
        if (project_id.length() > 2)
            hcu.eq(Expense.PROJECT_ID, project_id)
        hcu.orderBy(Expense.DATE_PAID)
        List<Expense> expenses = hcu.list()
        JSONArray ary = new JSONArray()
        for (Expense expense : expenses) {
            JSONObject jobj = new JSONObject()
            jobj.put("expense_id", expense.getExpenseId())
            jobj.put("employee_id", expense.getEmployeeId())
            jobj.put("project_id", expense.getProjectId())
            jobj.put("project_name", expense.getProject().getDescription())
            jobj.put("date_paid", expense.getDatePaid())
            jobj.put("per_diem_amount", expense.getPerDiemAmount())
            jobj.put("expense_amount", expense.getExpenseAmount())
            jobj.put("hotel_amount", expense.getHotelAmount())
            jobj.put("advance_amount", expense.getAdvanceAmount())
            jobj.put("payment_method", expense.getPaymentMethod())
            jobj.put("comments", expense.getComments())
            jobj.put("per_diem_return", expense.getPerDiemReturn())
            jobj.put("auth_date", expense.getAuthDate())
            jobj.put("auth_employee_name", expense.getAuthPerson().getNameLFM())
            ary.put(jobj)
        }
        outjson.put("expenses", ary)
    }

}
