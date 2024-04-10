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

package com.arahant.services.standard.project.imageUploadReport

import com.arahant.servlets.REST
import com.arahant.utils.*
import org.kissweb.Groff
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 1/7/20
 */
class CreateReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateReport().build(hsu, injson.getInt("beginning_date"), injson.getInt("ending_date"), injson.getString('date_type'))
        outjson.put("filename", fname)
    }

    private String build(HibernateSessionUtil hsu, int begdate, int enddate, String dateType) {
        Groff rpt = new Groff("ProjectImageUpload-", "Project Image Upload Report", true)
        page_title rpt, begdate, enddate, dateType
        Connection con = KissConnection.get()
        List<Record> recs

        if (dateType == 'project')
            recs = con.fetchAll('''
                    select p.project_name, p.description, og.group_name, count(pf.project_form_id), p.estimated_first_date, p.estimated_last_date, p.reference
                    from project p
                    left join project_form pf
                      on p.project_id = pf.project_id
                    join org_group og
                      on p.requesting_org_group = og.org_group_id
                    where (p.estimated_first_date >= ? and p.estimated_first_date <= ?) OR
                           (p.estimated_last_date >= ? and p.estimated_last_date <= ?) OR
                           (pf.form_date >= ? and pf.form_date <= ?)
                    group by og.group_name, p.estimated_first_date, p.description, p.project_id
                    order by og.group_name, p.estimated_first_date, p.description, p.project_id
                    ''', begdate, enddate, begdate, enddate, begdate, enddate)
        else
            recs = con.fetchAll('''
                    select p.project_name, p.description, og.group_name, count(pf.project_form_id), p.estimated_first_date, p.estimated_last_date, p.reference
                    from project p
                    left join project_form pf
                      on p.project_id = pf.project_id
                    join org_group og
                      on p.requesting_org_group = og.org_group_id
                    where pf.form_date >= ? and pf.form_date <= ?
                    group by og.group_name, p.estimated_first_date, p.description, p.project_id
                    order by og.group_name, p.estimated_first_date, p.description, p.project_id
                    ''', begdate, enddate)

        for (Record rec : recs) {
            String project_name = rec.getString("project_name")
            if (project_name == null)
                project_name = ""
            String description = rec.getString("description")
            if (description == null)
                description = ""
            else
                description = StringUtils.take(description, 45)
            String reference = rec.getString("reference")
            if (reference == null)
                reference = ""
            else
                reference = StringUtils.take(reference, 20).trim()
            String client = rec.getString("group_name")
            client = StringUtils.take(client, 20)
            rpt.out project_name.trim() + "~" +
                    DateUtils.getDateFormatted(rec.getInt("estimated_first_date")) + "~" +
                    DateUtils.getDateFormatted(rec.getInt("estimated_last_date")) + "~" +
                    description.trim() + "~" +
                    reference + "~" +
                    client + "~" +
                    rec.getLong("count")
        }
        rpt.out(".TE");
        return rpt.process(0.75f)
    }

    private static void page_title(Groff rpt, int begdate, int enddate, String dateType) {
        rpt.out(".SP")
        if (dateType == "project")
            rpt.out("Project date range:  " + DateUtils.getDateFormatted(begdate) + " and " + DateUtils.getDateFormatted(enddate))
        else
            rpt.out("Image upload date range:  " + DateUtils.getDateFormatted(begdate) + " and " + DateUtils.getDateFormatted(enddate))
        rpt.out(".SP")
        rpt.out(".TS H")
        rpt.out("tab(~);")
        rpt.out("L L L Lw(1i) L Lw(1i) N .");
        String cols = "Id~Start~End~Summary~Reference~Client~Uploads"
        rpt.out(cols)
        rpt.out("\\_~\\_~\\_~\\_~\\_~\\_~\\_")
        rpt.out(".TH")
    }

}
