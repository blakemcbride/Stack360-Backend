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

package com.arahant.services.standard.project.projectParent

import com.arahant.beans.Person
import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.DelimitedFileWriter
import org.kissweb.database.ArrayListString
import org.kissweb.database.Connection
import org.kissweb.database.Record


/**
 * User: Blake McBride
 * Date: 1/16/21
 *
 * This class does both the search and export functions.
 */
class SearchProjects {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int workDate = injson.getInt('workDate')
        boolean export = injson.getBoolean("export")
        int cap
        File csvFile = null
        DelimitedFileWriter dfw = null
        final Connection db = KissConnection.get()

        final Person person = hsu.getCurrentPerson()
        final boolean isClient = person.isClient()
        ArrayListString orgGroups = null
        if (isClient) {
            int today = DateUtils.today()
            orgGroups = new ArrayListString()
            List<Record> recs = db.fetchAll("""select org_group_id 
                                               from org_group_association
                                               where person_id = ?
                                                     and (start_date = 0 or start_date >= ?)
                                                     and (final_date = 0 or final_date <= ?)""", person.getPersonId(), today, today)
            for (Record r in recs)
                orgGroups.add(r.getString("org_group_id"))
        }

        if (export) {
            cap = 5000
            csvFile = FileSystemUtils.createTempFile("ProjectListExport-", ".csv")
            dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
            dfw.setDateFormat("MM/dd/yyyy")
            writeColumnHeader(dfw)
        } else {
            cap = BProperty.getInt(StandardProperty.SEARCH_MAX)
            if (cap == 0)
                cap = 100
        }
        ArrayList args = new ArrayList()
        String projectSearch =  """select proj.project_id, proj.description, proj.project_name, proj.estimated_first_date,
                                          proj.estimated_last_date, psch.required_workers, proj.estimate_hours, proj.project_days,
                                          pers.fname, pers.mname, pers.lname, pers.person_id,
                                          proj.store_number, psch.shift_start,
                                          ad.city, ad.state, og.group_name client_name, pst.code subtype,
                                          proj.location_description, psch.project_shift_id
                                   from project proj
                                   left outer join project_shift psch
                                     on proj.project_id = psch.project_id
                                   join project_status pstat
                                     on proj.project_status_id = pstat.project_status_id
                                   join project_type ptype
                                     on proj.project_type_id = ptype.project_type_id
                                   left outer join person pers
                                     on proj.managing_employee = pers.person_id
                                   left outer join address ad
                                     on proj.address_id = ad.address_id
                                   join org_group og
                                     on proj.requesting_org_group = og.org_group_id
                                   left outer join project_subtype pst
                                     on proj.project_subtype_id = pst.project_subtype_id
                                   where 1=1
                                """

        String summary = injson.getString("projectSummary")
        if (summary != null &&  !summary.isEmpty())
            switch (injson.getInt("projectSummarySearchType")) {
                case 2: // starts with
                    projectSearch += " and UPPER(proj.description) like ? "
                    args.add(summary.toUpperCase() + "%")
                    break
                case 3: // ends with
                    projectSearch += " and UPPER(proj.description) like ? "
                    args.add("%" + summary.toUpperCase())
                    break
                case 4: // contains
                    projectSearch += " and UPPER(proj.description) like ? "
                    args.add("%" + summary.toUpperCase() + "%")
                    break
                case 5: // exact match
                    projectSearch += " and UPPER(proj.description) = ? "
                    args.add(summary.toUpperCase())
                    break
            }


        String id = injson.getString("projectName")
        if (id != null &&  !id.isEmpty())
            switch (injson.getInt("projectNameSearchType")) {
                case 2: // starts with
                    projectSearch += " and TRIM(UPPER(proj.project_name)) like ? "
                    args.add(id.toUpperCase() + "%")
                    break
                case 3: // ends with
                    projectSearch += " and TRIM(UPPER(proj.project_name)) like ? "
                    args.add("%" + id.toUpperCase())
                    break
                case 4: // contains
                    projectSearch += " and TRIM(UPPER(proj.project_name)) like ? "
                    args.add("%" + id.toUpperCase() + "%")
                    break
                case 5: // exact match
                    projectSearch += " and TRIM(UPPER(proj.project_name)) = ? "
                    args.add(id.toUpperCase())
                    break
            }

        switch (injson.getInt("statusType")) {
            case 0: // any
                break;
            case 1: // active
                projectSearch += " and pstat.active = 'Y' "
                break
            case 2: // inactive
                projectSearch += " and pstat.active = 'N' "
                break
            case 3: // specific
                projectSearch += " and pstat.project_status_id = ? "
                args.add(injson.getString("status"))
                break
        }

        String client = injson.getString("companyId")
        if (client != null && !client.isEmpty()) {
            projectSearch += " and proj.requesting_org_group = ? "
            args.add(client)
        }

        String category = injson.getString("category")
        if (category != null && !category.isEmpty()) {
            projectSearch += " and proj.project_category_id = ? "
            args.add(category)
        }

        String type = injson.getString("type")
        if (type != null && !type.isEmpty()) {
            projectSearch += " and proj.project_type_id = ? "
            args.add(type)
        }

        String subtype = injson.getString("subType")
        if (subtype != null && !subtype.isEmpty()) {
            projectSearch += " and proj.project_subtype_id = ? "
            args.add(subtype)
        }

        if (isClient) {
            projectSearch += " and proj.requesting_org_group = ANY(?) "
            args.add(orgGroups)
        }

        Integer fromDate = injson.getInt("fromDate")
        Integer toDate = injson.getInt("toDate")
        if (fromDate == null)
            fromDate = 0
        if (toDate == null)
            toDate = 0
        if ("A" == injson.getString("dateType")) {
            if (fromDate > 19000101 && toDate > 19000101) {
                projectSearch += ''' and (? >= proj.estimated_first_date and ? <= proj.estimated_last_date or 
                                      ? >= proj.estimated_first_date and ? <= proj.estimated_last_date)
                             '''
                args.add(fromDate)
                args.add(fromDate)
                args.add(toDate)
                args.add(toDate)
            } else if (fromDate > 19000101) {
                projectSearch += ' and ? >= proj.estimated_first_date and ? <= proj.estimated_last_date '
                args.add(fromDate)
                args.add(fromDate)
            } else if (toDate > 19000101) {
                projectSearch += ' and ? >= proj.estimated_first_date and ? <= proj.estimated_last_date '
                args.add(toDate)
                args.add(toDate)
            }
        } else {
            if (fromDate > 19000101 && toDate > 19000101) {
                projectSearch += ''' and proj.estimated_first_date >= ?  and 
                                         proj.estimated_first_date <= ?
                             '''
                args.add(fromDate)
                args.add(toDate)
            } else if (fromDate > 19000101) {
                projectSearch += ' and proj.estimated_first_date >= ? '
                args.add(fromDate)
            } else if (toDate > 19000101) {
                projectSearch += ' and proj.estimated_first_date <= ? '
                args.add(toDate)
            }
        }

        // SPEED NOTE:  changing from "UPPER(x) like ?" to "x ilike ?" will make it a lot faster!
        // See:  com/arahant/rest/standard/hr/hrParent/SearchPersons.groovy

        String ref = injson.getString("extReference")
        if (ref != null &&  !ref.isEmpty())
            switch (injson.getInt("extReferenceSearchType")) {
                case 2: // starts with
                    projectSearch += " and TRIM(UPPER(proj.reference)) like ? "
                    args.add(ref.toUpperCase() + "%")
                    break
                case 3: // ends with
                    projectSearch += " and TRIM(UPPER(proj.reference)) like ? "
                    args.add("%" + ref.toUpperCase())
                    break
                case 4: // contains
                    projectSearch += " and TRIM(UPPER(proj.reference)) like ? "
                    args.add("%" + ref.toUpperCase() + "%")
                    break
                case 5: // exact match
                    projectSearch += " and TRIM(UPPER(proj.reference)) = ? "
                    args.add(ref.toUpperCase())
                    break
            }

        if (injson.getString('sortType') == "L")
            projectSearch += " order by proj.estimated_last_date, proj.requesting_org_group, proj.project_id"
        else
            projectSearch += " order by proj.estimated_first_date, proj.requesting_org_group, proj.project_id"
        List<Record> recs = db.fetchAll(cap, projectSearch, args)
        JSONArray ary = new JSONArray()
        recs.forEach(r -> {
            JSONObject obj = new JSONObject()
            String desc = r.getString("description")
            String projectId = r.getString("project_id")
            String projectShiftId = r.getString("project_shift_id")
            if (projectShiftId != null  ||  true) {
                obj.put("project_id", projectId)
                obj.put("shift_id", projectShiftId)
                obj.put("project_name", r.getString("project_name"))
                obj.put("description", r.getString("description"))
                obj.put("location_description", r.getString("location_description"))
                obj.put("estimated_first_date", r.getInt("estimated_first_date"))
                obj.put("estimated_last_date", r.getInt("estimated_last_date"))
                obj.put("required_workers", projectShiftId != null ? r.getInt("required_workers") : 0)
                obj.put("store_number", r.getString("store_number"))
                obj.put("shift_start", r.getString("shift_start"))
                obj.put("city", r.getString("city"))
                obj.put("state", r.getString("state"))
                obj.put("client_name", r.getString("client_name"))
                obj.put('subtype', r.getString('subtype'))
                double budgetedProjectHours = r.getFloat("estimate_hours")
                obj.put("estimate_hours", budgetedProjectHours)

                int scheduledNow = 0
                int scheduled = 0
                List<Record> srecs = projectShiftId != null ? db.fetchAll("""select start_date 
                                                from project_employee_join 
                                                where project_shift_id = ?""", projectShiftId) : null
                if (srecs != null)
                    for (Record sr in srecs) {
                        int dt = sr.getInt('start_date')
                        if (dt <= workDate)
                            scheduledNow++
                        scheduled++
                    }
                obj.put("scheduled", scheduled)
                obj.put("scheduledNow", scheduledNow)

                String fname = r.getString("fname")
                String lname = r.getString("lname")
                if (lname != null && fname != null) {
                    String n = lname + ", " + fname
                    String m = r.getString("mname")
                    if (m != null && !m.isEmpty())
                        n += ' ' + m
                    obj.put("site_manager", n)

                    String personId = r.getString("person_id") // the manager
                    if (personId != null && !personId.isEmpty()) {
                        List<Record> phones = db.fetchAll("select phone_number, phone_type from phone where person_join = ? and record_type = 'R'", personId)
                        String phoneNumber = null
                        for (Record phone : phones) {
                            int phoneType = phone.getInt('phone_type')
                            if (phoneType == 3) {
                                phoneNumber = phone.getString('phone_number')
                                break
                            }
                        }
                        if (phoneNumber == null && !phones.isEmpty())
                            phoneNumber = phones.get(0).getString('phone_number')
                        obj.put("manager_phone", phoneNumber == null ? '' : phoneNumber)
                    } else
                        obj.put("manager_phone", '')
                }

                List<Record> nts = db.fetchAll("""
                                 select person_id, SUM(total_hours) total_hours
                                 from timesheet
                                 where project_shift_id = ?
                                       and beginning_date = ?
                                       and billable = 'Y'
                                 group by person_id
                                 order by person_id
                              """, projectShiftId, workDate)
                /*  Once hours have been entered, we want to show people with zero hours (but checked in)
                as not being there.
             */
                int zeroHourWorkers = 0
                nts.forEach(ts -> {
                    Double th = ts.getDouble("total_hours")
                    if (th == null || th < 0.1)
                        zeroHourWorkers++
                })
                if (nts.size() == zeroHourWorkers)
                    obj.put("actual_workers", nts.size())  // show who showed up
                else
                    obj.put("actual_workers", nts.size() - zeroHourWorkers)  // show those with hours

                Record rec = db.fetchOne("""
                                 select SUM(ts.total_hours) total_hours
                                 from timesheet ts
                                 join project_shift ps
                                   on ts.project_shift_id = ps.project_shift_id
                                 where ps.project_id = ?
                                       and ts.billable = 'Y'
                              """, projectId)
                Double actualHoursWorked = rec.getDouble("total_hours")
                if (actualHoursWorked == null)
                    actualHoursWorked = 0.0
                obj.put("total_hours", actualHoursWorked)

                int budgetedProjectDays = r.getShort("project_days")
                rec = db.fetchOne("""select count(*) num
                                     from (select distinct ts.beginning_date 
                                           from timesheet ts
                                           join project_shift ps
                                             on ts.project_shift_id = ps.project_shift_id
                                           where ts.billable = 'Y' and ps.project_id = ?) xx""", projectId)
                final int actualDaysWorked = rec.getLong("num")

                double budgetedHoursPerDay = budgetedProjectDays > 0 ? budgetedProjectHours / budgetedProjectDays : 0
                double hoursShouldHaveWorkedSoFar = actualDaysWorked * budgetedHoursPerDay
                int statusOfHours = (int)(actualHoursWorked - hoursShouldHaveWorkedSoFar)
                obj.put("statusHours", statusOfHours)

                int statusPercent = budgetedProjectHours > 1 ? statusOfHours * 100 / budgetedProjectHours : 0
                obj.put("statusPercent", statusPercent)

                if (export)
                    exportRow(dfw, obj)
                else
                    ary.put(obj)
            }
        })
        if (export) {
            dfw.close()
            outjson.put("reportUrl", FileSystemUtils.getHTTPPath(csvFile))
        } else {
            outjson.put('projects', ary)
            outjson.put("cap", cap)
        }
    }

    private static void exportRow(DelimitedFileWriter dfw, JSONObject obj) {
        dfw.writeField obj.getString("project_name")
        dfw.writeField obj.getString("client_name")
        dfw.writeField obj.getString("location_description")
        dfw.writeField obj.getString("subtype")
        dfw.writeField obj.getString("store_number")
        dfw.writeField obj.getString("shift_start")
        dfw.writeField obj.getString("city")
        dfw.writeField obj.getString("state")
        dfw.writeField DateUtils.format4(obj.getInt("estimated_first_date"))
        dfw.writeField DateUtils.format4(obj.getInt("estimated_last_date"))
        int req = obj.getInt("required_workers")
        int act = obj.getInt("actual_workers")
        int sch = obj.getInt("scheduled")
        int schNow = obj.getInt("scheduledNow")
        dfw.writeField req
        dfw.writeField sch == schNow ? sch + "" : schNow + "*"
        dfw.writeField act
        dfw.writeField act - req

        dfw.endRecord()
    }

    private static void writeColumnHeader(DelimitedFileWriter dfw) throws Exception {
        dfw.writeField("ID");
        dfw.writeField("Client");
        dfw.writeField("Retailer");
        dfw.writeField("Sub-Type");
        dfw.writeField("Store");
        dfw.writeField("Shift");
        dfw.writeField("City");
        dfw.writeField("State");
        dfw.writeField("Start Date");
        dfw.writeField("End Date");
        dfw.writeField("Required");
        dfw.writeField("Scheduled");
        dfw.writeField("Actual");
        dfw.writeField("Net");
        dfw.endRecord();
    }
}
