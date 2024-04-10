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

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import com.arahant.utils.ZipCodeDistance
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record


/**
 * Author: Blake McBride
 * Date: 1/4/21
 */
class AutoGenerate {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final int workDate = injson.getInt("currentWeek")  // actually a day and not a week!
        final boolean testing = false
        final float mealPerDiem = 20f
        final String standardComment = "Automated per diem allocation"
        HashMap<String,Float> alreadyPaid = new HashMap<>()
        int today = DateUtils.today()

        Connection db = KissConnection.get()

        //  Delete any prior auto-allocations for this week
        db.execute("""delete from expense 
                      where week_paid_for=?
                      and expense_amount < .01 and advance_amount < .01 and per_diem_return < .01 and hotel_amount < .01 and hotel_amount < .01
                      and payment_method='O'
                      and comments=?""", workDate, standardComment)

        List<Record> precs = db.fetchAll("""select distinct ps.project_id 
                                            from timesheet ts
                                            join project_shift ps
                                              on ts.project_shift_id = ps.project_shift_id
                                            where ts.beginning_date = ?
                                                  and ts.total_hours > 3.99
                                                  and ts.billable = 'Y'""", workDate)
        for (int i=0, s=precs.size() ; i < s ; i++) {
            Record prec = precs.get(i)
            List<Record> tsrecs = db.fetchAll("""
                                        -- Create an hr_wage table that only includes their current status as
                                        -- of a specific date - a single record for each employee.
                                        with wage_now as (
                                        select t2.* from hr_wage t2
                                        inner join
                                            (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                             from hr_wage t0
                                             where t0.effective_date <= ?
                                             group by t0.employee_id) t1
                                        on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date)

                                        select ts.person_id, ps.project_id, ts.total_hours, a.zip pzip, a2.zip wzip, pos.weekly_per_diem
                                        from timesheet ts
                                        join project_shift ps
                                          on ts.project_shift_id = ps.project_shift_id
                                        join project p
                                          on ps.project_id = p.project_id
                                        left join address a
                                          on p.address_id = a.address_id

                                        left join address a2
                                          on ts.person_id = a2.person_join
                                
                                        inner join wage_now wh
                                          on ts.person_id = wh.employee_id
                                          
                                        inner join hr_position pos
                                          on wh.position_id = pos.position_id
                                          
                                        where ts.beginning_date = ?
                                          and ps.project_id = ?
                                          and ts.total_hours > 3.999
                                          and (a.record_type = 'R' or a.record_type is null)
                                          and (a2.record_type = 'R' or a2.record_type is null)
                                        """, workDate, workDate, prec.getString("project_id"))
            double hoursInFullDay = getFullDay(tsrecs)
            for (Record rec : tsrecs) {
                if (rec.getDouble('total_hours') + 0.001 < hoursInFullDay)
                    continue
                String pzip = rec.getString("pzip")
                if (pzip == null)
                    continue
                if (pzip.length() > 5)
                    pzip = StringUtils.take(pzip, 5)
                if (pzip.length() != 5)
                    continue
                float weeklyPerDiem = rec.getFloat("weekly_per_diem")
                if (weeklyPerDiem < 0.01)
                    continue
                if (!testing) {
                    String wzip = rec.getString("wzip")
                    if (wzip == null)
                        continue
                    if (wzip.length() > 5)
                        wzip = StringUtils.take(wzip, 5)
                    if (wzip.length() != 5)
                        continue
                    int miles = ZipCodeDistance.distance(wzip, pzip);
                    if (miles <= 50)
                        continue
                }
                String personId = rec.getString("person_id")
                Record exprec = db.fetchOne("select * from expense where employee_id=? and week_paid_for = ?", personId, workDate)
                String projectId = rec.getString("project_id")
                if (exprec != null) {
                    if (exprec.getFloat("hotel_amount") > 1f) {
                        Record exception = db.fetchOne("select exception_type, exception_amount from per_diem_exception where person_id=? and project_id=?",
                                personId, projectId)
                        if (exception != null) {
                            switch (exception.getString("exception_type")) {
                                case "A":
                                    exprec.set("per_diem_amount", mealPerDiem + exception.getFloat("exception_amount"))
                                    break;
                                case "S":
                                    float perDiemAmount = mealPerDiem - exception.getFloat("exception_amount")
                                    if (perDiemAmount > 0.01f)
                                        exprec.set("per_diem_amount", perDiemAmount)
                                    break;
                                case "D":
                                    break;
                                case "R":
                                    exprec.set("per_diem_amount", exception.getFloat("exception_amount"))
                                    break;
                            }
                        } else
                            exprec.set("per_diem_amount", mealPerDiem)
                        exprec.update()
                    }
                    continue  // per diem already done
                }

                float perDiemAmount = weeklyPerDiem
                Record exception = db.fetchOne("select exception_type, exception_amount from per_diem_exception where person_id=? and project_id=?",
                        personId, projectId)
                if (exception != null) {
                    switch (exception.getString("exception_type")) {
                        case "A":
                            perDiemAmount += exception.getFloat("exception_amount")
                            break;
                        case "S":
                            perDiemAmount -= exception.getFloat("exception_amount")
                            if (perDiemAmount < 0)
                                perDiemAmount = 0
                            break;
                        case "D":
                            perDiemAmount = 0
                            break;
                        case "R":
                            perDiemAmount = exception.getFloat("exception_amount")
                            break;
                    }
                }
                if (perDiemAmount < 0.01)
                    continue  //  no per diem
                Float prevPaid = alreadyPaid.containsKey(personId) ? alreadyPaid.get(personId) : 0
                if (prevPaid + 0.001 >= perDiemAmount)
                    continue // already paid
                perDiemAmount -= prevPaid

                Record exp = db.newRecord("expense")
                exp.set("expense_id", IDGenerator.generate("expense", "expense_id"))
                exp.set("employee_id", personId);
                exp.set("project_id", projectId)
                exp.set("date_paid", today)
                exp.set("per_diem_amount", perDiemAmount)
                exp.set("expense_amount", 0)
                exp.set("hotel_amount", 0)
                exp.set("advance_amount", 0)
                exp.set("payment_method", "O")
                exp.set("comments", standardComment)
                exp.set("auth_date", today)
                exp.set("auth_person_id", hsu.getCurrentPerson().getPersonId())
                exp.set("week_paid_for", workDate)
                exp.addRecord()

                alreadyPaid.put(personId, (float) (perDiemAmount + prevPaid))
            }
        }
    }

    /**
     * Round to the nearest quarter hour.
     * Doesn't work with negative numbers.
     *
     * @param th
     * @return
     */
    private static double round(double th) {
        double nearest = 0.25
        double d2 = nearest / 2
        return th + d2 - (th+d2) % nearest
    }

    private static double getFullDay(List<Record> tsrecs) {
        int n = tsrecs.size()
        if (n < 4)
            return 8.0
        // create a map [hours * 100 :  number of people]
        def freq = [:]
        for (int i=0 ; i < n ; i++) {
            Record r = tsrecs.get(i)
            double th = r.getDouble("total_hours")
            int ith = (int) Math.floor(round(th) * 100)
            if (ith > 800)  // more than 8 hours is irrelevant
                ith = 800
            if (freq[ith] == null)
                freq[ith] = 1
            else
                freq[ith] += 1
        }

        //  find the hours with the most employees
        int maxHours = 0
        int maxEmployees = 0
        freq.each { entry ->
            if (entry.value > maxEmployees) {
                maxEmployees = entry.value
                maxHours = entry.key
            }
        }
        return maxHours / 100.0
    }
}
