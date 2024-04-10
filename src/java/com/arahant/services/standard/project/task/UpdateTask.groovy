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

package com.arahant.services.standard.project.task

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/9/22
 *
 * This is used to add and update a task.
 */
class UpdateTask {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String shiftId = injson.getString("shiftId")   // used for add
        final String projectTaskDetailId = injson.getString("projectTaskDetailId")  // used for update
        final int status = injson.getInt("status")
        final int workDate = injson.getInt("workDate")
        int seqno = 1 // used for add
        boolean newRecord
        final Connection db = hsu.getKissConnection()
        Record rec
        rec = projectTaskDetailId == null ? null : db.fetchOne("select * from project_task_detail where project_task_detail_id = ?", projectTaskDetailId)
        if (newRecord = rec == null) {
            rec = db.newRecord("project_task_detail")
            IDGenerator.generate(rec, "project_task_detail_id")

            Record srec = db.fetchOne("select seqno from project_task_detail where project_shift_id = ? order by seqno desc", shiftId)
            if (srec != null)
                seqno = srec.getInt("seqno") + 1
            rec.set("seqno", seqno)

            rec.set("project_shift_id", shiftId)
            rec.set("task_date", workDate)
        }
        rec.set("title", injson.getString("title"))
        rec.set("description", injson.getString("description"))
        rec.set("comments", injson.getString("comments"))
        rec.set("missing_items", injson.getString("missingItems"))
        rec.set("status", status)
        rec.set("recurring_schedule_id", null)
        rec.set("completion_date", 0)
        if (injson.getString("recurring") == "Y") {
            rec.set('recurring', "Y")
            String recurringScheduleId = rec.getString("recurring_schedule_id")
            if (recurringScheduleId == null) {
                Record srec = db.newRecord("recurring_schedule")
                recurringScheduleId = IDGenerator.generate(srec, "recurring_schedule_id")
                setRecurrenceFields(srec, injson)
                srec.addRecord()
                rec.set("recurring_schedule_id", recurringScheduleId)
            } else {
                Record srec = db.fetchOne("select * from recurring_schedule where recurring_schedule_id = ?", recurringScheduleId)
                setRecurrenceFields(srec, injson)
                srec.update()
            }
        } else {
            rec.set('recurring', "N")
            String recurringScheduleId = rec.getString("recurring_schedule_id")
            if (recurringScheduleId != null) {
                rec.set("recurring_schedule_id", null)
                rec.update()
                db.execute("delete from recurring_schedule where recurring_schedule_id = ?", recurringScheduleId)
            }
        }

        //int ndate  = getNextDate(injson, workDate) // for testing only
        final int completionDate = rec.getInt("completion_date")
        if (status == 1 || status == 2) {
            // completed or cancelled
            if (completionDate == 0) {
                rec.set("completion_date", workDate)
                if (rec.getString("recurring") == "Y") {
                    int nextDate = getNextDate(injson, workDate)
                    if (nextDate <= injson.getInt("ending_date")) {
                        Record rec2 = db.fetchOne("select * from project_task_detail where project_shift_id = ? and task_date = ? and title = ?", shiftId, nextDate, injson.getString("title"))
                        if (rec2 == null) {
                            int seqno2 = 1
                            rec2 = db.newRecord("project_task_detail")
                            String newProjectTaskDetailId = IDGenerator.generate(rec2, "project_task_detail_id")

                            Record srec = db.fetchOne("select seqno from project_task_detail where project_shift_id = ? order by seqno desc", shiftId)
                            if (srec != null)
                                seqno2 = srec.getInt("seqno") + 1
                            rec2.set("seqno", seqno2)

                            rec2.set("title", rec.getString("title"))
                            rec2.set("description", rec.getString("description"))
                            rec2.set("status", 0)  // open
                            rec2.set("project_shift_id", shiftId)
                            rec2.set("task_date", nextDate)
                            rec2.set('recurring', "Y")
                            rec2.set("completion_date", 0)
                            String recurringScheduleId = rec.getString("recurring_schedule_id")
                            if (recurringScheduleId != null) {
                                Record rsrec = db.fetchOne("select * from recurring_schedule where recurring_schedule_id = ?", recurringScheduleId)
                                Record rsrec2 = db.newRecord("recurring_schedule")
                                recurringScheduleId = IDGenerator.generate(rsrec2, "recurring_schedule_id")
                                rsrec2.copyCorresponding(rsrec)
                                rsrec2.set("recurring_schedule_id", recurringScheduleId)  // put it back
                                rsrec2.addRecord()
                                rec2.set("recurring_schedule_id", recurringScheduleId)
                            }
                            rec2.addRecord()

                            List<Record> arecs = db.fetchAll("select * from project_task_assignment where project_task_detail_id = ?", projectTaskDetailId)
                            for (Record arec : arecs) {
                                Record arec2 = db.newRecord("project_task_assignment")
                                IDGenerator.generate(arec2, "project_task_assignment_id")
                                arec2.set("project_task_detail_id", newProjectTaskDetailId)
                                arec2.set("person_id", arec.getString("person_id"))
                                arec2.set("team_lead", arec.getString("team_lead"))
                                arec2.addRecord()
                            }
                        }
                    }
                }
            }
        } else {
            rec.set("completion_date", 0)
        }
        newRecord ? rec.addRecord() : rec.update()
    }

    private static void setRecurrenceFields(Record srec, JSONObject injson) {
        final int recurrenceType = injson.getInt("type")
        srec.set("type", recurrenceType)
        switch (recurrenceType) {
            case 1:
                srec.set("month", injson.getInt("month"))
                srec.set("day", injson.getInt("day"))
                break;
            case 2:
                srec.set("day", injson.getInt("day"))
                break;
            case 3:
                break;
            case 4:
                srec.set("n", injson.getInt("n"))
                srec.set("day_of_week", injson.getInt("day_of_week"))
                break;
            case 5:
                srec.set("day_of_week", injson.getInt("day_of_week"))
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                srec.set("n", injson.getInt("n"))
                break;
        }
        srec.set("ending_date", injson.getInt("ending_date"))
    }

    private static int getNextDate(JSONObject injson, int workDate) {
        final int recurrenceType = injson.getInt("type")
        final int today = DateUtils.today()
        int ret   //  YYYYMMDD
        switch (recurrenceType) {
            case 1:
                ret = DateUtils.toInt(DateUtils.year(today), injson.getInt("month"), injson.getInt("day"))
                if (ret < today)
                    ret = DateUtils.addYears(ret, 1)
                break;
            case 2:
                ret = DateUtils.addMonths(workDate, 1)
                int y = DateUtils.year(ret)
                int m = DateUtils.month(ret)
                ret = DateUtils.toInt(y, m, injson.getInt("day"))
                break;
            case 3:  // last day of following month
                ret = DateUtils.addMonths(workDate, 2)
                int y = DateUtils.year(ret)
                int m = DateUtils.month(ret)
                ret = DateUtils.toInt(y, m, 1)
                ret = DateUtils.addDays(ret, -1)
                break;
            case 4:
                int nextMonth = DateUtils.addMonths(workDate, 1)
                int month = DateUtils.month(nextMonth)
                int year = DateUtils.year(nextMonth)
                ret = getWeekdayInMonth(year, month, injson.getInt("n"), injson.getInt("day_of_week"))
               // ret = getDate(year, month, injson.getInt("n"), injson.getInt("day_of_week"))
                break;
            case 5: // same day each week
                ret = DateUtils.addDays(workDate, 7)
                ret = setWeekDay(ret, injson.getInt("day_of_week"))
                break;
            case 6:
                ret = getNextWeekday(workDate)
                break;
            case 7:
                ret = DateUtils.addDays(workDate, 1)
                break;
            case 8:
                ret = DateUtils.addDays(workDate, injson.getInt("n"))
                break;
            default:
                ret = 0;
                break;
        }
        return ret
    }

    private static int getWeekdayInMonth(int year, int month, int weekOfMonth, int day) {
        List<Integer> dates = new ArrayList<>()
        Calendar calendar = new GregorianCalendar(year, --month, 1); // month is 0-based

        while (calendar.get(Calendar.MONTH) == month) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == day) {
                int tuesday = calendar.get(Calendar.YEAR) * 10000 +
                        (calendar.get(Calendar.MONTH) + 1) * 100 +
                        calendar.get(Calendar.DAY_OF_MONTH);
                dates.add(tuesday);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return --weekOfMonth >= dates.size() ? 0 : dates[weekOfMonth]
    }

    private static int setWeekDay(int date, int targetDayOfWeek) {
        int year = date.intdiv(10000)
        int month = (date % 10000).intdiv(100)
        int day = date % 100

        Calendar calendar = new GregorianCalendar(year, month - 1, day) // month is 0-based
        calendar.set(Calendar.DAY_OF_WEEK, targetDayOfWeek)

        int adjustedDate = calendar.get(Calendar.YEAR) * 10000 +
                (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
        return adjustedDate
    }

    private static int getNextWeekday(int date) {
        int year = date.intdiv(10000)
        int month = (date % 10000).intdiv(100)
        int day = date % 100

        Calendar calendar = new GregorianCalendar(year, month - 1, day) // month is 0-based
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek >= Calendar.MONDAY && dayOfWeek < Calendar.FRIDAY) {
            // If it's Monday to Thursday, add one day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        } else {
            // If it's Friday or weekend, add appropriate days to reach next Monday
            calendar.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY + (7 - dayOfWeek))
        }

        int nextWeekdayDate = calendar.get(Calendar.YEAR) * 10000 +
                (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
        return nextWeekdayDate
    }

}
