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

package com.arahant.services.standard.at.applicantProfile

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/17/23
 */
class SaveQuestions {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final positionId = injson.getString("positionId")
        final String personId = injson.getString("personId")
        final JSONArray questions = injson.getJSONArray("questions")
        final Connection db = hsu.getKissConnection()
        Record rec
        boolean newrec
        if (questions != null)
            for (int i = 0; i < questions.length() ; i++) {
                JSONObject q = questions.getJSONObject(i)
                rec = db.fetchOne("select * from applicant_answer where person_id = ? and applicant_question_id = ?", personId, q.getString("applicant_question_id"))
                if (newrec = rec == null) {
                    rec = db.newRecord("applicant_answer")
                    IDGenerator.generate(rec, "applicant_answer_id")
                    rec.set("person_id", personId)
                    rec.set("applicant_question_id", q.getString("applicant_question_id"))
                }
                switch (q.getString("data_type")) {
                    case "D":  // Date
                        rec.set("date_answer", q.getInt("date_answer"))
                        break
                    case 'N':  // numeric
                        rec.set("numeric_answer", q.getDouble("numeric_answer"))
                        break
                    case 'S':  // string
                        rec.set("string_answer", q.getString("string_answer"))
                        break
                    case 'Y':  // yes/no
                        rec.set("string_answer", q.getString("string_answer"))
                        break
                    case 'L':  //  list
                        rec.set("applicant_question_choice_id", q.getString("applicant_question_choice_id"))
                        break;
                }
                if (newrec)
                    rec.addRecord()
                else
                    rec.update()
            }
    }

}
