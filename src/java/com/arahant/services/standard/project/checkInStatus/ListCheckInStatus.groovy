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

package com.arahant.services.standard.project.checkInStatus

import com.arahant.servlets.REST
import com.arahant.utils.ArahantConstants
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.ZipCodeDistance
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.GoogleDistance
import org.kissweb.GoogleReverseGeocode
import org.kissweb.NumberFormat
import org.kissweb.NumberUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 2/24/22
 */
class ListCheckInStatus {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        GoogleDistance.setAPIKey(ArahantConstants.GOOGLE_API_KEY)
        GoogleReverseGeocode.setAPIKey(ArahantConstants.GOOGLE_API_KEY)
        String project_id = injson.getString("project_id")
        String worker = injson.getString("worker")
        String checkedIn = injson.getString("checkedIn")
        int today = DateUtils.today()
        int later = DateUtils.addDays(today, 5)  // show future project too
        Connection db = hsu.getKissConnection()
        List<Object> args = new ArrayList<Object>()
        args.add(later)
        args.add(today)
        args.add(later)
        String select = """select proj.project_id, proj.description, proj.estimated_first_date, per.person_id, per.fname, 
                                                 per.mname, per.lname, ad.zip, pshft.project_shift_id,
                                                 pej.start_date, ph.phone_number, per.personal_email 
                                           from project_employee_join pej
                                           join project_shift pshft
                                             on pej.project_shift_id = pshft.project_shift_id
                                           join project proj
                                             on pshft.project_id = proj.project_id
                                           join project_status ps
                                             on proj.project_status_id = ps.project_status_id
                                           join person per
                                             on pej.person_id = per.person_id
                                           left join address ad
                                             on proj.address_id = ad.address_id
                                           left join phone ph
                                             on per.person_id = ph.person_join
                                           where ps.active = 'Y'
                                                 and proj.estimated_first_date <= ?
                                                 and proj.estimated_last_date >= ?
                                                 and pej.start_date <= ? 
                                                 and (ad.record_type = 'R' or ad.record_type is null)
                                                 and (ph.phone_type = 3 or ph.phone_type is null)
                                                 and (ph.record_type = 'R' or ph.record_type is null)"""
        if (project_id != null  &&  !project_id.isEmpty()) {
            select += "and proj.project_id = ? "
            args.add(project_id)
        }
        if (worker != null  &&  !worker.isEmpty()) {
            select += "and per.person_id = ? "
            args.add(worker)
        }
        select += "order by proj.description, per.lname, per.fname"
        List<Record> recs = db.fetchAll(select, args)
        JSONArray ja = new JSONArray()
        String prevProjId = ""
        String prevPersId = ""
        boolean samePerson
        for (Record rec in recs) {
            String projId = rec.getString("project_id")
            String persId = rec.getString("person_id")
            if (!(samePerson = prevPersId == persId && prevProjId == projId)) {
                prevPersId = persId
                prevProjId = projId
            }

            Record cirec = db.fetchOne("""select wc.*, per.fname, per.mname, per.lname
                                          from worker_confirmation wc
                                          join project_shift ps
                                            on wc.project_shift_id = ps.project_shift_id
                                          left join person per
                                            on wc.who_added = per.person_id
                                          where wc.person_id = ?
                                                and ps.project_id = ?
                                          order by wc.confirmation_time desc""", persId, projId)
            if (cirec == null && checkedIn == "Y")
                continue
            if (cirec != null && checkedIn == "N")
                continue

            Record trec = db.fetchOne("""select ts.timesheet_id 
                                         from timesheet ts
                                         join project_shift ps
                                           on ts.project_shift_id = ps.project_shift_id
                                         where ps.project_id = ?
                                               and ts.person_id = ? 
                                               and ts.billable = 'Y'
                                               and ts.total_hours > .009""", projId, persId)
            if (trec != null)
                continue  // they've already worked

            JSONObject jo = new JSONObject()
            jo.put("project_id", projId)
            jo.put("project_description", samePerson ? '' : rec.getString("description"))
            jo.put("person_id", persId)
            jo.put("lname", samePerson ? '' : rec.getString("lname"))
            jo.put("fname", samePerson ? '' : rec.getString("fname"))
            jo.put("project_start_date", samePerson ? 0 : rec.getInt("estimated_first_date"))
            jo.put("employee_start_date", samePerson ? 0 : rec.getInt("start_date"))
            jo.put("phone_number", rec.getString("phone_number"))
            jo.put("personal_email", rec.getString("personal_email"))

            if (cirec != null && !samePerson) {
                final String pzip = rec.getString("zip")
                final double lat = cirec.getDouble("latitude")
                final double lon = cirec.getDouble("longitude")
                int distance = cirec.getInt("distance")
                int minutes = cirec.getInt("minutes")

                /*
                Sometimes Google, for no apparent reason, does not give a valid response.
                This code was an attempt to "try again".  It was abandoned because it was discovered that
                Google consistently returns no result for certain valid queries.  Bug report 240762125 was
                reported to Google.

                boolean b1 = NumberUtils.doubleEqual(lat, 41.9767, .1) && NumberUtils.doubleEqual(lon, -87.91, .1)

                if (b1 || !NumberUtils.doubleEqual(lat, 0, .01)  &&
                    !NumberUtils.doubleEqual(lon, 0, .01)  &&
                    distance == -1  &&  minutes == -1) {
                    //  We have lat and long but for some reason we didn't calculate the distance.  Try again.
                    Record prec = db.fetchOne("""select a.street, a.city, a.state, a.zip
                                         from project p
                                         inner join address a
                                           on p.address_id = a.address_id
                                         where p.project_id = ?""", projId)
                    if (prec != null) {
                        GoogleReverseGeocode grg = new GoogleReverseGeocode(lat, lon)
                        String location = grg.getCityState()

                        String add1 = prec.getString("street") + ", " + prec.getString("city") + ", " + prec.getString("state") + " " + prec.getString("zip")
                        String add2 = NumberFormat.Format(lat, "", 0, 3) + " " + NumberFormat.Format(lon, "", 0, 3)

                        GoogleDistance gd = new GoogleDistance(add1, add2)
                        distance = gd.miles()

                        //  address not found.  Try zip code alone.
                        if (distance == -1) {
                            add1 = prec.getString("zip")
                            add2 = NumberFormat.Format(lat, "", 0, 3) + " " + NumberFormat.Format(lon, "", 0, 3)
                            // gd = new GoogleDistance(add1, add2)
                            gd = new GoogleDistance(add1, add2)
                            distance = gd.miles()
                        }




                        minutes = gd.minutes()
                        if (distance != 0 || minutes != 0) {
                            //  save what we discovered
                            Record wcrec = db.fetchOne("select * from worker_confirmation where worker_confirmation_id = ?", cirec.getString("worker_confirmation_id"))
                            if (wcrec != null) {
                                wcrec.set("distance", distance)
                                wcrec.set("minutes", minutes)
                                wcrec.set("location", location)
                                wcrec.update()
                            }
                        }
                    }
                }

                 */

                jo.put("distance", distance < 0 ? 0 : distance)

                jo.put("confirmation_time", cirec.getDateTimeMS("confirmation_time"))
                jo.put("latitude", lat)
                jo.put("longitude", lon)
                jo.put("location", cirec.getString("location"))

                jo.put("minutes", minutes < 0 ? 0 : minutes)
                jo.put("notes", cirec.getString("notes"))
                jo.put("who_added", cirec.getString("who_added"))
                jo.put("checkin_fname", cirec.getString("fname"))
                jo.put("checkin_mname", cirec.getString("mname"))
                jo.put("checkin_lname", cirec.getString("lname"))
            }
            ja.put(jo)
        }
        outjson.put("checkins", ja)
    }

}
