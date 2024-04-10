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
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 3/15/23
 */
class LoadQuestions {
    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final positionId = injson.getString("positionId")
        final String personId = injson.getString("personId")
        final Connection db = hsu.getKissConnection()

        JSONArray questions = db.fetchAllJSON("""select aq.*, aa.applicant_answer_id, aa.string_answer, 
                                                       aa.date_answer, aa.numeric_answer, aa.applicant_question_choice_id,
                                                       qc.description
                                                from applicant_question aq
                                                left join applicant_answer aa
                                                  on aq.applicant_question_id = aa.applicant_question_id
                                                     and aa.person_id = ? 
                                                left join applicant_question_choice qc
                                                  on aa.applicant_question_choice_id = qc.applicant_question_choice_id
                                                where (aq.position_id = ? or aq.position_id is null)
                                                      and (aq.last_active_date = 0 or aq.last_active_date >= ?
                                                      or aa.applicant_answer_id is not null)
                                                order by aq.question_order;
                                                """, personId, positionId, DateUtils.today())
        for (int i=0 ; i < questions.length() ; i++) {
            JSONObject q = questions.getJSONObject(i)
            if (q.getString("data_type") == "L")
                q.put("choices", db.fetchAllJSON("select * from applicant_question_choice where applicant_question_id = ? order by applicant_question_choice_id", q.getString("applicant_question_id")))
        }
        outjson.put("questions", questions)
    }
}
