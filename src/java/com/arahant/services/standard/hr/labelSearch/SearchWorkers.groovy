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

package com.arahant.services.standard.hr.labelSearch

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/25/20
 */
class SearchWorkers {

    private static final int MAX_RECORDS_TO_SEND = 400

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        String lbl1 = injson.getString("label1")
        String lbl2 = injson.getString("label2")
        String lbl3 = injson.getString("label3")
        String projectId = injson.getString("projectId")
        String i9 = injson.getString("i9")
        List<Object> args = new ArrayList<>()
        String query = """select distinct p.person_id, p.fname, p.mname, p.lname, p.i9_part1, p.i9_part2, el.name lbl
                          from person p
                          left outer join employee_label_association ela1
                            on p.person_id = ela1.employee_id
                          join employee_label el
                            on ela1.employee_label_id = el.employee_label_id
                          where ela1.completed = 'N' and
                                p.person_id in (select ela.employee_id
                                                from employee_label_association ela
                                                where ela.completed = 'N'
                                                    and (
                       """
        boolean needAnd = false
        if (lbl1 != null  &&  !lbl1.isEmpty()) {
            query += " employee_label_id = ? "
            args.add(lbl1)
            needAnd = true
        }
        if (lbl2 != null  &&  !lbl2.isEmpty()) {
            if (needAnd)
                query += " and "
            query += " employee_label_id = ? "
            args.add(lbl2)
            needAnd = true
        }
        if (lbl3 != null  &&  !lbl3.isEmpty()) {
            if (needAnd)
                query += " and "
            query += " employee_label_id = ? "
            args.add(lbl3)
        }
        query += "))"
        if (projectId != null &&  !projectId.isEmpty()) {
            query += """ and p.person_id in (select person_id 
                                               from project_employee_join
                                               where project_id = ?)
                     """
            args.add(projectId)
        }
        switch (i9) {
            case "0":  //  any
                break
            case "1":  //  Part I and II incomplete
                query += " and p.i9_part1 = 'N' and p.i9_part2 = 'N' "
                break
            case "2":  //  Part I complete, Part II incomplete
                query += " and p.i9_part1 = 'Y' and p.i9_part2 = 'N' "
                break
            case "3":  //  Parts I and II complete
                query += " and p.i9_part1 = 'Y' and p.i9_part2 = 'Y' "
                break
            case "4":  //  Part I incomplete but Part II complete (??)
                query += " and p.i9_part1 = 'N' and p.i9_part2 = 'Y' "
                break
        }
        query += " order by p.lname, p.fname, p.mname, p.person_id"

        // combine like persons
        int totalRecords = 0
        JSONArray recs2 = new JSONArray()
        JSONObject obj = null
        Command cmd = db.newCommand()
        Cursor c = cmd.query(query, args)
        while (c.isNext()) {
            Record r = c.getRecord()
            if (obj == null)
                obj = createObject(r)
            else if (r.getString("person_id") == obj.getString("person_id")) {
                JSONArray labels = obj.getJSONArray("labels")
                labels.put(r.getString("lbl"))
            } else {
                if (totalRecords++ < MAX_RECORDS_TO_SEND)
                    recs2.put(obj)
                obj = createObject(r)
            }
        }
        if (obj && totalRecords++ < MAX_RECORDS_TO_SEND)
            recs2.put(obj)

        outjson.put "workers", recs2
        outjson.put "totalRecords", totalRecords
    }

    private static JSONObject createObject(Record r) {
        JSONObject obj = new JSONObject()
        obj.put("person_id", r.getString("person_id"))
        obj.put("fname", r.getString("fname"))
        obj.put("mname", r.getString("mname"))
        obj.put("lname", r.getString("lname"))
        obj.put("i9_part1", r.getString("i9_part1"))
        obj.put("i9_part2", r.getString("i9_part2"))
        JSONArray labels = new JSONArray()
        labels.put(r.getString("lbl"))
        obj.put("labels", labels)
        return obj
    }

}
