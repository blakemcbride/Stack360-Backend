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

package com.arahant.services.standard.project.workerAssignmentReport

import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.KissConnection
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 2/10/20
 */
class CreateExport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String fname = new CreateExport().build(hsu)
        outjson.put("filename", fname)
    }

    private String build(HibernateSessionUtil hsu) {
        File csvFile = FileSystemUtils.createTempFile("WorkerAssignmentExport-" + DateUtils.today() + "-", ".csv")
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        writeColumnHeader dfw
        Connection con = KissConnection.get()
        Command cmd = con.newCommand();
        Cursor c = cmd.query(
                "select p.project_id, p.project_name, p.estimated_first_date, p.estimated_last_date, og.group_name, p.description, " +
                        "  per.lname, per.fname, per.mname, " +
                        "  psh.required_workers, psh.project_shift_id, per.person_id " +
                        "from project p " +
                        "join project_shift psh " +
                        "  on p.project_id = psh.project_id " +
                        "join project_status ps " +
                        "  on p.project_status_id = ps.project_status_id " +
                        "join org_group og " +
                        "  on p.requesting_org_group = og.org_group_id " +
                        "left outer join project_employee_join ej " +
                        "  on psh.project_shift_id = ej.project_shift_id " +
                        "left outer join person per " +
                        "  on ej.person_id = per.person_id " +
                        "where ps.active = 'Y' " +
                        "order by og.group_name, p.description ")
        String lastProject = ""
        while (c.isNext()) {
            String projectId = c.getString "project_id"
            String fname = c.getString("fname")
            String lname = c.getString("lname")
            String mname = c.getString("mname")
            if (fname == null)
                fname = ""
            if (lname == null)
                lname = ""
            if (mname == null)
                mname = ""
            String name = lname
            if (name.length() > 0  &&  (fname.length() > 0  ||  mname.length() > 0))
                name += ", "
            name += fname + ' ' + mname
            if (lastProject != projectId) {
                dfw.writeField c.getString("group_name")
                dfw.writeField c.getString("project_name")
                dfw.writeField c.getString("description")
                dfw.writeDate c.getInt("estimated_first_date")
                dfw.writeDate c.getInt("estimated_last_date")
                dfw.endRecord()
                lastProject = projectId
                if (lname.length() > 0  ||  fname.length() > 0) {
                    dfw.writeField ""
                    dfw.writeField ""
                    dfw.writeField name
                    dfw.endRecord()
                }
            } else {
                dfw.writeField ""
                dfw.writeField ""
                dfw.writeField name
                dfw.endRecord()
            }
        }
        dfw.close()
        return FileSystemUtils.getHTTPPath(csvFile);
    }

    private static void writeColumnHeader(DelimitedFileWriter dfw) {
        dfw.writeField "Client"
        dfw.writeField "Project ID"
        dfw.writeField "Description / Worker Name"
        dfw.writeField "First Date"
        dfw.writeField "Last Date"
        dfw.endRecord()
    }

}
