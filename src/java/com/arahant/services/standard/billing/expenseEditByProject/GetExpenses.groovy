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

package com.arahant.services.standard.billing.expenseEditByProject

import com.arahant.beans.Expense
import com.arahant.beans.Person
import com.arahant.business.BEmployee
import com.arahant.business.BProject
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
        final String project_id = injson.getString("project_id")
        final int project_day = injson.getInt("project_week")  //  this is now used for day not week!

        // first get the existing expense records
        HibernateCriteriaUtil<Expense> hcu = hsu.createCriteria(Expense.class).eq(Expense.PROJECT_ID, project_id)
                .eq(Expense.WEEK_PAID_FOR, project_day)
        List<Expense> expenses = hcu.list()

        // get rid of those who have zero records
        List<Expense> e2 = new ArrayList<>()
        for (Expense e : expenses)
            if (e.getPerDiemAmount() > 0.009 || e.getExpenseAmount() > 0.009 || e.getAdvanceAmount() > 0.009 || e.getHotelAmount() > 0.009)
                e2.add(e);
        expenses = e2;

        // now add list of people associated with the project if not already there
        BProject proj = new BProject(project_id)
        List<Person> pers = proj.getAssignedPersons2(null)
        for (Person per : pers) {
            String person_id1 = per.getPersonId()
            boolean found = false;
            for (Expense exp : expenses) {
                String person_id2 = exp.getEmployeeId()
                if (person_id2.equals(person_id1)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Expense exp = new Expense()
                exp.setEmployee((new BEmployee(person_id1)).getEmployee())
                exp.setProject((new BProject(project_id)).getBean())
                expenses.add(exp)
            }
        }

        // sort by lname, pdfname
        expenses.sort{it.employee.getNameLFM()}


        JSONArray ary = new JSONArray()
        for (Expense expense : expenses) {
            JSONObject jobj = new JSONObject()
            jobj.put("expense_id", expense.getExpenseId())
            jobj.put("employee_id", expense.getEmployee().getPersonId())
            jobj.put("worker_name", (new BEmployee(expense.getEmployee())).getNameLFM())
            jobj.put("project_id", expense.getProject().getProjectId())
            jobj.put("project_name", expense.getProject()?.getDescription())
            jobj.put("date_paid", expense.getDatePaid())
            jobj.put("week_paid_for", expense.getWeekPaidFor())
            jobj.put("per_diem_amount", expense.getPerDiemAmount())
            jobj.put("expense_amount", expense.getExpenseAmount())
            jobj.put("hotel_amount", expense.getHotelAmount())
            jobj.put("advance_amount", expense.getAdvanceAmount())
            char pm = expense.getPaymentMethod()
            jobj.put("payment_method", pm ? pm : '')
            jobj.put("comments", expense.getComments())
            jobj.put("scheduling_comments", expense.getSchedulingComments())
            jobj.put("per_diem_return", expense.getPerDiemReturn())
            jobj.put("auth_date", expense.getAuthDate())
            jobj.put("auth_employee_name", expense.getAuthPerson()?.getNameLFM())
            ary.put(jobj)
        }
        outjson.put("expenses", ary)
    }

}
