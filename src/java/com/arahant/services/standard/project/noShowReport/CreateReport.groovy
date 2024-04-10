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

package com.arahant.services.standard.project.noShowReport

import com.arahant.business.BHREmployeeEvent
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.Groff
import org.kissweb.NumberFormat
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/15/21
 */
class CreateReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int workDate = injson.getInt("workDate")
        String client = injson.getString('client')
        String subtype = injson.getString('subtype')
        Connection db = KissConnection.get()
        Groff rpt = new Groff("NoShowReport-", "Worker No-Show Report", true)
        rpt.setFooter('* = more workers scheduled in the future', '')
 //       rpt.dontDeleteGroffFile()  //  for debugging
        page_title rpt, workDate, client, subtype, db
        spin_records db, rpt, workDate, client, subtype
        outjson.put("filename", rpt.process(0.4f))
    }

    private static void spin_records(Connection db, Groff rpt, int workDate, String client, String subtype) {
        ArrayList args = new ArrayList()
        String projectSearch =  """select proj.project_id, proj.description, proj.project_name, proj.estimated_first_date,
                                          proj.estimated_last_date, ps.required_workers,
                                          pers.fname, pers.mname, pers.lname, phone.phone_number,
                                          proj.store_number, ps.shift_start,
                                          ad.city, ad.state, og.group_name client_name, pst.code subtype
                                   from project_shift ps
                                   join project proj
                                     on ps.project_id = proj.project_id
                                   join project_status pstat
                                     on proj.project_status_id = pstat.project_status_id
                                   join project_type ptype
                                     on proj.project_type_id = ptype.project_type_id
                                   left outer join person pers
                                     on proj.managing_employee = pers.person_id
                                   left outer join phone
                                     on pers.person_id = phone.person_join
                                   left outer join address ad
                                     on proj.address_id = ad.address_id
                                   join org_group og
                                     on proj.requesting_org_group = og.org_group_id
                                   left outer join project_subtype pst
                                     on proj.project_subtype_id = pst.project_subtype_id
                                   where (phone.phone_type = 3 or phone.phone_type is null) and
                                         (phone.record_type = 'R' or phone.record_type is null)
                                         and pstat.active = 'Y'
                                         and proj.estimated_first_date <= ?
                                """
        args.add(workDate)
        if (client != null && !client.isEmpty()) {
            projectSearch += " and proj.requesting_org_group = ? "
            args.add(client)
        }
        if (subtype != null && !subtype.isEmpty()) {
            projectSearch += " and proj.project_subtype_id = ? "
            args.add(subtype)
        }
        projectSearch += " order by og.group_name, estimated_first_date"
        List<Record> recs = db.fetchAll(projectSearch, args)
        recs.forEach(r -> {
            String desc = r.getString("description")

            String projectId = r.getString("project_id")
            int required
            String project = append(fix(r.getString("store_number")), fix(r.getString("shift_start"))) +
                    " " + fix(r.getString("city")) + " " + fix(r.getString("state"))
            String out = StringUtils.centerStrip(r.getString("project_name")) +
                    "\t" + StringUtils.take(r.getString("client_name"), 20) +
                    "\t" + fix(r.getString('subtype')) +
                    "\t" + project +
                    "\t" + DateUtils.format4(r.getInt("estimated_first_date")) +
                    "\t" + DateUtils.format4(r.getInt("estimated_last_date")) +
                    "\t" + fix(required=r.getShort("required_workers"))

            int scheduledNow = 0
            int scheduled = 0
            List<Record> srecs = db.fetchAll("select start_date from project_employee_join where project_id = ?", projectId)
            for (Record sr in srecs) {
                int dt = sr.getInt('start_date')
                if (dt <= workDate)
                    scheduledNow++
                scheduled++
            }
            if (scheduledNow == scheduled)
                out += "\t" + fix(scheduled)
            else
                out += "\t" + fix(scheduledNow) + '*'

            List<Record> nts = db.fetchAll("""
                                 select person_id, SUM(total_hours) total_hours
                                 from timesheet
                                 where project_id = ?
                                       and beginning_date = ?
                                       and billable = 'Y'
                                 group by person_id
                                 order by person_id
                              """, projectId, workDate)
            //  Once hours have been entered, we want to show people with zero hours (but checked in)
            //    as not being there.
            int zeroHourWorkers = 0
            nts.forEach(ts -> {
                Double th = ts.getDouble("total_hours")
                if (th == null || th < 0.1)
                    zeroHourWorkers++
            })
            int actual
            if (nts.size() == zeroHourWorkers)
                out += "\t" + fix(actual=nts.size())  // show who showed up
            else
                out += "\t" + fix(actual=nts.size()-zeroHourWorkers)   // show those with hours

            int net = actual - required
            out += "\t" + NumberFormat.Format(net, "PB", 0, 0)

            rpt.out ".T&"
            rpt.out("l l l l r r r r r r s.")
            rpt.out(out)

            boolean needNewFormat = true
            String manLname = r.getString('lname')
            if (manLname) {
                rpt.out ".T&"
                rpt.out("l l s s s s s s s s s.")
                rpt.out ".sp"
                needNewFormat = false

                String fname = r.getString('fname')
                String mname = r.getString('mname')
                String phone = r.getString('phone_number')
                fname = fname ? fname : ''
                fname = 'Manager: ' + fname
                if (mname)
                    fname += ' ' + mname
                fname += ' ' + manLname
                if (phone)
                    fname += ' ' + phone
                rpt.out "\t" + StringUtils.take(fname, 140)
                rpt.out('.sp')
            }

            nts = db.fetchAll("""
                            select pej.person_id, per.lname, per.fname, per.mname
                            from project_employee_join pej
                            join person per
                              on pej.person_id = per.person_id
                            where pej.project_id = ?
                                  and (pej.start_date = 0 or pej.start_date <= ?)
                            order by per.lname, per.fname, per.mname
                              """, projectId, workDate)
            nts.forEach(r2 -> {

                String personId = r2.getString("person_id")
                Record ts = db.fetchOne("""
                                            select total_hours
                                            from timesheet 
                                            where project_id = ?
                                                  and person_id = ?
                                                  and beginning_date = ?
                                                  and billable = 'Y'
                                        """, projectId, personId, workDate)
                if (ts == null) {
                    String name = r2.getString("lname") + ", " + r2.getString("fname")
                    String mname = r2.getString("mname")
                    if (mname != null && !mname.isEmpty())
                        name += " " + mname

                    List<Record> phones = db.fetchAll("select phone_number, phone_type from phone where person_join = ? and record_type = 'R'", personId)
                    String phoneNumber = null
                    for (Record phone : phones) {
                        int phoneType = phone.getInt('phone_type')
                        if (phoneType == 3) {
                            phoneNumber = phone.getString('phone_number')
                            break
                        }
                    }
                    if (phoneNumber == null  &&  !phones.isEmpty())
                        phoneNumber = phones.get(0).getString('phone_number')
                    String phone = phoneNumber == null ? '' : phoneNumber
                    if (phone == null)
                        phone = ""

                    String eventS = BHREmployeeEvent.getMobileEventString(db, personId, workDate)
                    String labels = getLabelString(db, personId)
                    if (needNewFormat) {
                        rpt.out ".T&"
                        rpt.out("l l s s s s s s s s s.")
                        rpt.out ".sp"
                        needNewFormat = false
                    }
                    String line = name + " " + phone + " " + eventS
                    if ((line + labels).size() > 120) {
                        rpt.out "\t" + StringUtils.take(line, 120)
                        rpt.out "\t        " + StringUtils.take(labels, 110)
                    } else
                        rpt.out "\t" + line + ' ' + labels
                }
            })
            if (!nts.isEmpty())
                rpt.out(".sp")
        })
        rpt.out ".TE"
    }

    private static String fix(String str) {
        return str == null ? "" : StringUtils.centerStrip(str)
    }

    private static String fix(int n) {
        return n == 0 ? "" : n + ""
    }

    private static String fix(long n) {
        return n == 0 ? "" : n + ""
    }

    private static String append(String a, String b) {
        if (a == null || a.isEmpty())
            return fix(b)
        if (b == null || b.isEmpty())
            return fix(a)
        return a + "-" + b
    }

    private static void page_title(Groff rpt, int workDate, String client, String subtype, Connection db) {
        rpt.out(".SP")
        rpt.out("Workers who didn't show up for work date:  " + DateUtils.format("EEE MMM d, yyyy", workDate))

        if (client != null && !client.isEmpty()) {
            Record rec = db.fetchOne("select group_name from org_group where org_group_id=?", client)
            rpt.out "Client: " + rec.getString("group_name")
        } else
            rpt.out "Clients: All"

        if (subtype != null  &&  !subtype.isEmpty()) {
            Record rec = db.fetchOne("select code from project_subtype where project_subtype_id=?", subtype)
            rpt.out "Sub-type: " + rec.getString("code")
        } else
            rpt.out "Sub-type: All"

        rpt.out(".SP")
        rpt.out(".TS H")
        rpt.out("l l l l r r r r r r s.")
        String cols = "ID\tClient\tSub-type\tProject\tStart\tEnd\tReq\tSch\tAct\tNet"
        rpt.out(cols)
        rpt.out("\\_\t\\_\t\\_\t\\_\t\\_\t\\_\t\\_\t\\_\t\\_\t\\_")
        rpt.out(".TH")
    }

    private static String getLabelString(Connection db, String personId) {
        List<Record> recs = db.fetchAll("""
                                        select el.name
                                        from employee_label_association ela
                                        join employee_label el
                                          on ela.employee_label_id = el.employee_label_id
                                        where ela.employee_id = ?
                                        order by el.name
                                        """, personId)
        String ret = ''
        recs.forEach(r -> {
            if (ret)
                ret += ' ' + r.getString('name')
            else
                ret = r.getString('name')
        })
        return ret
    }

}
