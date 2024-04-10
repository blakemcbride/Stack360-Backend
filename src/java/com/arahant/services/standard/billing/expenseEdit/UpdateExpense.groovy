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
import com.arahant.business.BExpense
import com.arahant.business.BProject
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/4/18
 */
class UpdateExpense {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        BExpense bexp = new BExpense(injson.getString("expense_id"))
        Expense exp = bexp.getExpense();
        exp.setProject((new BProject(injson.getString("project_id"))).getBean())
        exp.setDatePaid(injson.getInt('date_paid'))
        exp.setPerDiemAmount(injson.getFloat('per_diem_amount'))
        exp.setExpenseAmount(injson.getFloat('expense_amount'))
        exp.setHotelAmount(injson.getFloat('hotel_amount'))
        exp.setAdvanceAmount(injson.getFloat('advance_amount'))
        exp.setComments(injson.getString('comments'))
        exp.setPerDiemReturn(injson.getFloat('per_diem_return'))
        exp.setPaymentMethod(injson.getString('payment_method').charAt(0))
        bexp.update()
    }
}
