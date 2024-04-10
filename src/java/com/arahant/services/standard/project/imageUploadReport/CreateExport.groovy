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
import com.arahant.utils.KissConnection
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 1/9/20
 */
class CreateExport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateExport().build(hsu, injson.getInt("beginning_date"), injson.getInt("ending_date"), injson.getString('date_type'))
        outjson.put("filename", fname)
    }

    private String build(HibernateSessionUtil hsu, int begdate, int enddate, String dateType) {
        File csvFile = FileSystemUtils.createTempFile("ProjectImageUploadExport-" + begdate + "-", ".csv")
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        writeColumnHeader dfw, begdate, enddate, dateType
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
            String reference = rec.getString("reference")
            if (reference == null)
                reference = ""
            String client = rec.getString("group_name")

            dfw.writeField project_name.trim()
            dfw.writeDate rec.getInt("estimated_first_date")
            dfw.writeDate rec.getInt("estimated_last_date")
            dfw.writeField description.trim()
            dfw.writeField reference
            dfw.writeField client
            dfw.writeField rec.getLong("count")
            dfw.endRecord()
        }
        dfw.close()
        return FileSystemUtils.getHTTPPath(csvFile);
    }

    private static void writeColumnHeader(DelimitedFileWriter dfw, int begdate, int enddate, String dateType) {
        dfw.writeField dateType == "project" ? "Projects active between" : "Images uploaded between"
        dfw.writeDate begdate
        dfw.writeDate enddate
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.writeField ""
        dfw.endRecord()

        dfw.writeField "Project"
        dfw.writeField "Start Date"
        dfw.writeField "End Date"
        dfw.writeField "Description"
        dfw.writeField "Reference"
        dfw.writeField "Client"
        dfw.writeField "Count"
        dfw.endRecord()
    }

}
