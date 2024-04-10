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

import com.arahant.beans.Person
import com.arahant.servlets.REST
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 3/2/18
 */
class SearchWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
                .orderBy(Person.LNAME)
                .orderBy(Person.FNAME)
                .ge(Person.LNAME, injson.getString("beg_lname"))
                .eq(Person.RECORD_TYPE, 'R'.charAt(0))
                .setMaxResults(40)

        String st = injson.getString("search_type")
        if ("all".equals(st))
            hcu.isEmployee();
        else if ("active".equals(st))
            hcu.activeEmployee();
        else if ("inactive".equals(st))
            hcu.inactiveEmployee();
        List<Person> emps = hcu.list()
        JSONArray ary = new JSONArray()
        for (Person emp : emps) {
            JSONObject jobj = new JSONObject()
            jobj.put("employeeid", emp.getPersonId())
            jobj.put("pdfname", emp.getFname())
            jobj.put("mname", emp.getMname())
            jobj.put("lname", emp.getLname())
            jobj.put("ssn", emp.getSsn())
            jobj.put("title", emp.getTitle())
            ary.put(jobj)
        }
        outjson.put("projectEmployees", ary)
    }

}
