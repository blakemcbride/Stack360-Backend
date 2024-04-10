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
import org.kissweb.NumberUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record


/**
 * Author: Blake McBride
 * Date: 10/23/20
 */
class AutoGenerateWeekly {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final int startDate = injson.getInt("currentWeek")
        final int lastDate = DateUtils.addDays(startDate, 6)
        final boolean testing = false
        int nworkers = 0
        double total = 0
        int today = DateUtils.today()
        int fewDaysAgo = DateUtils.addDays(today, -3)
        int sevenDaysAgo = DateUtils.addDays(today, -7)
        final String standardComment = "Automated per diem allocation"
        HashMap<String,Float> alreadyPaid = new HashMap<>()

        Connection db = KissConnection.get()

        //  Delete any prior auto-allocations for this week
        db.execute("""delete from expense 
                      where week_paid_for=?
                      and expense_amount < .01 and advance_amount < .01 and per_diem_return < .01 and hotel_amount < .01
                      and payment_method='O'
                      and comments=?""", startDate, standardComment)

        List<Record> projRecs = db.fetchAll("""
                              select p.project_id, a.zip, p.estimated_first_date, p.estimated_last_date 
                              from project p
                              join project_status s
                                on p.project_status_id = s.project_status_id
                              join address a
                                on p.address_id = a.address_id
                              where p.estimated_first_date > 0 and p.estimated_last_date > 0 and p.estimated_last_date > p.estimated_first_date
                                    and p.estimated_first_date < ?
                                    and p.estimated_last_date >= ?
                                    and s.active = 'Y'
                                    and a.zip is not null and a.zip <> ''
                      """, DateUtils.addDays(startDate, 7), startDate)
        for (Record pr : projRecs) {
            int fdate = pr.getInt("estimated_first_date")
            int ldate = pr.getInt("estimated_last_date")
            fdate = fdate < startDate ? startDate : fdate
            ldate = ldate > lastDate ? lastDate : ldate
            int ndays = DateUtils.daysBetween(ldate, fdate) + 1
            String projectId = pr.getString("project_id")
            String pzip = pr.getString("zip")
            if (pzip == null || pzip.length() != 5)
                continue
            List<Record> erecs = db.fetchAll("""
                      select ej.person_id, p.weekly_per_diem, wa.zip
                      from project_employee_join ej
                      inner join (
                          -- this creates an hr_wage table that has only one record per employee - their current wage
                          select t4.position_id, t4.employee_id from hr_wage t4
                          inner join

                              (select distinct t2.employee_id, t2.effective_date, t2.wage_id
                              from hr_wage t2
                              inner join

                                  (select distinct t0.employee_id, max(t0.effective_date) effective_date
                                  from hr_wage t0
                                  where t0.effective_date <= ?
                                  group by t0.employee_id) t1

                              on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date) t3

                          on t4.wage_id = t3.wage_id

                      ) wh
                      on ej.person_id = wh.employee_id
                      
                      inner join hr_position p
                      on wh.position_id = p.position_id
                      
                      inner join address wa
                      on ej.person_id = wa.person_join
                      
                      where ej.project_id = ?
                            and p.weekly_per_diem > 0
                            and wa.address_type = 2 and wa.zip is not null and wa.zip <> ''
                      """, DateUtils.today(), projectId)
            for (Record erec : erecs) {
                float weeklyPerDiem = erec.getFloat("weekly_per_diem")
                if (ndays < 5)
                    weeklyPerDiem = NumberUtils.round(weeklyPerDiem * ndays / 5, 2)
                if (weeklyPerDiem < 10)
                    continue
                if (!testing) {
                    String wzip = erec.getString("zip")
                    if (wzip == null  ||  wzip.length() != 5)
                        continue
                    int miles = ZipCodeDistance.distance(wzip, pzip);
                    if (miles <= 50)
                        continue
                }
                String personId = erec.getString("person_id")

                //  See if worker has any prior hours for the project.  Don't pay if not.
                //List<Record> priorHours = db.fetchAll("select timesheet_id from timesheet where project_id=? and person_id=?", projectId, personId)

                //  See if worker has any prior hours for any project in the prior week.  Don't pay if not.
                List<Record> priorHours = db.fetchAll("select timesheet_id from timesheet where person_id=? and beginning_date >= ?", personId, sevenDaysAgo)
                if (priorHours == null  ||  priorHours.isEmpty())
                    continue;

                List<Record> exprecs = db.fetchAll("select expense_id from expense where employee_id=? and date_paid >= ?", personId, fewDaysAgo)
                if (!exprecs.isEmpty())
                    continue  // already got per diem

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
                exp.set("week_paid_for", startDate)
                exp.addRecord()

                alreadyPaid.put(personId, (float) (perDiemAmount + prevPaid))

                nworkers++
                total += perDiemAmount
            }
        }
    }
}
