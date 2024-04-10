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

package com.arahant.services.standard.hr.hrParent

import com.arahant.beans.Person
import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.Formatting
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DelimitedFileWriter
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

import java.sql.SQLException

/**
 * Author: Blake McBride
 * Date: 3/10/21
 */
class SearchPersons {

    private enum AssignedType {
        EITHER,
        ASSIGNED,
        UNASSIGNED
    }

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = hsu.getKissConnection()
        boolean export = injson.getBoolean("export")
        boolean needAnd = false;
        ArrayList<Object> args = new ArrayList<>();
        int today = DateUtils.today();
        File csvFile = null
        DelimitedFileWriter dfw = null
        int cap

        if (export) {
            csvFile = FileSystemUtils.createTempFile("HRExport-", ".csv")
            dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
            dfw.setDateFormat("MM/dd/yyyy")
            writeColumnHeader(dfw)
            cap = 100000
        } else {
            cap = BProperty.getInt(StandardProperty.SEARCH_MAX)
            if (cap == 0)
                cap = 100
        }

        args.add(today);

        String select =  "select p.person_id, p.lname, p.fname, p.mname, p.ssn, p.job_title, es.name status_name, " +
                "(case when esh.effective_date <= ? then es.active else 'N' end) active, esh.effective_date, " +
                " pos.position_name, p.i9_part1 " +
                "from employee e " +

                "join person p " +
                "  on e.person_id = p.person_id " +

                "left join current_employee_status esh " +
                "  on p.person_id = esh.employee_id " +

                "left join current_employee_wage w " +
                "  on p.person_id = w.employee_id " +

                "left join hr_position pos " +
                "  on w.position_id = pos.position_id " +

                "left join hr_employee_status es " +
                "  on esh.status_id = es.status_id ";

        if (injson.getString("activeIndicator") == "1") {
            select += " where es.active='Y' and esh.effective_date <= ? ";
            args.add(DateUtils.today());
            needAnd = true;
        } else if (injson.getString("activeIndicator") == "2") {
            select += " where (es.active='N' or esh.effective_date > ?) ";
            args.add(DateUtils.today());
            needAnd = true;
        }

        String phone = injson.getString('phone')
        if (phone != null && !phone.isEmpty()) {
            List<Record> recs = db.fetchAll("select person_join from phone where phone_number like ?", Formatting.formatPhoneNumber(phone) + '%')  // so numbers with extensions will be found
            if (recs.isEmpty()) {
                if (!needAnd) {
                    select += " where ";
                    needAnd = true;
                } else
                    select += " and ";
                select += " 1=0 ";  // none found
            } else {
                if (!needAnd) {
                    select += " where ";
                    needAnd = true;
                } else
                    select += " and ";
                String list = ""
                boolean needComma = false
                for (Record rec : recs) {
                    if (needComma)
                        list += ", "
                    else
                        needComma = true
                    list += "'" + rec.getString("person_join") + "'"
                }
                select += "p.person_id in (" + list + ")"
            }
        }

        if ("employee" == injson.getString("workerType")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += " e.employment_type = 'E' ";
        } else if ("contractor" == injson.getString("workerType")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += " e.employment_type = 'C' ";
        }
        if (injson.getString("firstName").trim()) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            switch (injson.getString("firstNameSearchType")) {
                case "2":  // starts with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " fname ilike ? ";
                    else
                        select += " fname like ? ";
                    args.add(injson.getString("firstName") + '%');
                    break;
                case "3":  // ends with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " fname ilike ? ";
                    else
                        select += " fname like ? ";
                    args.add('%' + injson.getString("firstName"));
                    break;
                case "4":  // contains
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " fname ilike ? ";
                    else
                        select += " fname like ? ";
                    args.add('%' + injson.getString("firstName") + '%');
                    break;
                case "5":  // exact match
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " fname ilike ? ";
                    else
                        select += " fname = ? ";
                    args.add(injson.getString("firstName"));
                    break;
            }
        }

        if (injson.getString("lastName")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            switch (injson.getString("lastNameSearchType")) {
                case "2":  // starts with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " lname ilike ? ";
                    else
                        select += " lname like ? ";
                    args.add(injson.getString("lastName") + '%');
                    break;
                case "3":  // ends with
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " lname ilike ? ";
                    else
                        select += " lname like ? ";
                    args.add('%' + injson.getString("lastName"));
                    break;
                case "4":  // contains
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " lname ilike ? ";
                    else
                        select += " lname like ? ";
                    args.add('%' + injson.getString("lastName") + '%');
                    break;
                case "5":  // exact match
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += " lname ilike ? ";
                    else
                        select += " lname = ? ";
                    args.add(injson.getString("lastName"));
                    break;
            }
        }

        if (injson.getString("ssn")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += "ssn = ?";
            args.add(Person.encryptSsn(injson.getString("ssn")));
        }
        if (injson.getString("employeeStatusId")) {
            if (!needAnd) {
                select += " where ";
                needAnd = true;
            } else
                select += " and ";
            select += "esh.status_id = ?";
            args.add(injson.getString("employeeStatusId"));
        }

        select += " order by lower(p.lname), lower(p.fname), lower(p.mname), p.person_id ";

        Cursor c = db.newCommand().query(select, args.toArray());
        setEmployees(export, dfw, outjson, db, c, injson.getString("assigned"), injson.getInt("assignedFrom"), injson.getInt("assignedTo"), cap,
                injson.getInt("searchType"), injson.getString("firstPerson"), injson.getString("lastPerson"), injson.getJSONArray("labels"), injson.getJSONArray("negativeLabels"));
        c.close()
        if (export) {
            dfw.close()
            outjson.put("reportUrl", FileSystemUtils.getHTTPPath(csvFile))
        }
    }

    private static void setEmployees(boolean export, DelimitedFileWriter dfw, JSONObject outjson, Connection db, Cursor c, String assigned, Integer assignedFrom, Integer assignedTo, int cap,
                                     Integer searchType, String firstPerson, String lastPerson, JSONArray labels, JSONArray negativeLabels) throws ArahantException, SQLException {
        JSONArray persons = new JSONArray();
        int today = org.kissweb.DateUtils.today();
        Command lblcmd = db.newCommand();
        int groupNumber = 1
        Command cmd = db.newCommand()

        AssignedType assignedType;
        switch (assigned) {
            case "assigned":
                assignedType = AssignedType.ASSIGNED;
                break;
            case "unassigned":
                assignedType = AssignedType.UNASSIGNED;
                break;
            case "either":
            default:
                assignedType = AssignedType.EITHER;
                break;
        }

        int nDisplayed = 0;
        int numberSkipped = 0;
        if (searchType == 1 && (firstPerson == null || firstPerson.isEmpty()))
            searchType = 0;
        if (searchType == 2 && (lastPerson == null || lastPerson.isEmpty()))
            searchType = 0;
        boolean inCorrectPage = searchType == 0;
        int total = 0
        while (c.isNext()) {
            Record rec = c.getRecord();
            String personId = rec.getString("person_id");
            List<Record> ap
            if (assignedType == AssignedType.EITHER) {
                assignedFrom = assignedTo = today
                /*
                ap = db.fetchAll("select pej.project_employee_join_id, p.description, psch.shift_start " +
                        "from project_employee_join pej " +
                        "join project_shift psch " +
                        "  on pej.project_shift_id = psch.project_shift_id " +
                        "join project p " +
                        "  on psch.project_id = p.project_id " +
                        "join project_status ps " +
                        " on p.project_status_id = ps.project_status_id " +
                        "where pej.person_id = ? " +
                        "    and (p.estimated_first_date <= ? and p.estimated_last_date >= ?" +
                        "    or p.estimated_first_date <= ? and p.estimated_last_date >= ? " +
                        "or p.estimated_first_date > ? and p.estimated_last_date < ?) " +
                        "and ps.active = 'Y'",
                        personId, assignedFrom, assignedFrom, assignedTo, assignedTo, assignedFrom, assignedTo);
                 */
                ap = db.fetchAll("select pej.project_employee_join_id, p.description, psch.shift_start " +
                        "from project_employee_join pej " +
                        "join project_shift psch " +
                        "  on pej.project_shift_id = psch.project_shift_id " +
                        "join project p " +
                        "  on psch.project_id = p.project_id " +
                        "join project_status ps " +
                        " on p.project_status_id = ps.project_status_id " +
                        "where pej.person_id = ? " +
                        "      and (p.estimated_last_date >= ? or p.estimated_last_date = 0) " +
                        "      and ps.active = 'Y'",
                        personId, assignedFrom);

            } else
                ap = db.fetchAll("select project_employee_join_id, p.description, psch.shift_start " +
                        "from project_employee_join pej " +
                        "join project_shift psch " +
                        "  on pej.project_shift_id = psch.project_shift_id " +
                        "join project p " +
                        "  on psch.project_id = p.project_id " +
                        "join project_status ps " +
                        " on p.project_status_id = ps.project_status_id " +
                        "where pej.person_id = ? " +
                        "    and (p.estimated_first_date <= ? and p.estimated_last_date >= ?" +
                        "    or p.estimated_first_date <= ? and p.estimated_last_date >= ? " +
                        "or p.estimated_first_date > ? and p.estimated_last_date < ?) " +
                        "and ps.active = 'Y'",
                        personId, assignedFrom, assignedFrom, assignedTo, assignedTo, assignedFrom, assignedTo);

            if (assignedFrom > 20100101  &&  assignedTo > 20100101 && assignedType != AssignedType.EITHER) {
                if (assignedType == AssignedType.ASSIGNED) {
                    if (ap.isEmpty())
                        continue;
                } else /* if (assignedType == AssignedType.UNASSIGNED) */ {
                    if (!ap.isEmpty())
                        continue;
                }
            }

            // Label search
            int nLabels = labels.size()
            if (nLabels > 0) {
                StringBuilder select = new StringBuilder("select employee_label_id " +
                        "from employee_label_association " +
                        "where employee_id = ? and completed = 'N' and (");
                boolean needOr = false;
                for (int i=0 ; i < nLabels ; i++) {
                    String lblid = labels.getString(i)
                    if (needOr)
                        select.append(" or ");
                    else
                        needOr = true;
                    select.append(" employee_label_id = '");
                    select.append(lblid);
                    select.append("' ");
                }
                select.append(')');
                List<Record> lblrecs = lblcmd.fetchAll(select.toString(), personId);
                if (lblrecs.size() != nLabels)
                    continue;
            }

            // Negative label search
            int nnLabels = negativeLabels.size()
            if (nnLabels > 0) {
                StringBuilder select = new StringBuilder("select employee_label_id " +
                        "from employee_label_association " +
                        "where employee_id = ? and completed = 'N' and (")
                boolean needOr = false
                for (int i=0 ; i < nnLabels ; i++) {
                    String lblid = negativeLabels.getString(i)
                    if (needOr)
                        select.append(" or ")
                    else
                        needOr = true
                    select.append(" employee_label_id = '");
                    select.append(lblid)
                    select.append("' ")
                }
                select.append(')');
                List<Record> lblrecs = lblcmd.fetchAll(select.toString(), personId);
                if (lblrecs.size() > 0)
                    continue
            }

            total++
            if (nDisplayed < cap  &&  inCorrectPage) {
                nDisplayed++
                if (export)
                    exportRow(dfw, SearchPersonsReturnItem(db, rec, ap, cmd, assignedFrom, assignedTo))
                else
                    persons.put(SearchPersonsReturnItem(db, rec, ap, cmd, assignedFrom, assignedTo))
            } else
                numberSkipped++
            if (searchType == 2 && !inCorrectPage && personId == lastPerson) {
                inCorrectPage = true
                groupNumber = 1 + numberSkipped.intdiv(cap)
            }
        }
        cmd.close()
        if (!export) {
            outjson.put("cap", cap)
            outjson.put("total", total)
            outjson.put("groupNumber", groupNumber)
            outjson.put("persons", persons)
        }
    }

    private static JSONObject SearchPersonsReturnItem(Connection db, final Record rec, List<Record> ap, Command cmd, int assignedFrom, int assignedTo) throws ArahantException, SQLException {
        JSONObject obj = new JSONObject()
        String personId = rec.getString("person_id")
        String lname = rec.getString("lname")
        String fname = rec.getString("fname")

        String middleName = rec.getString("mname")
        String jobTitle = rec.getString("job_title")
        String ssn = Person.decryptSsn(rec.getString("ssn"))
        String positionName = rec.getString("position_name")

        String statusName = rec.getString("status_name")
        String active = rec.getString("active")
        String type = "Emp"
        boolean dirty = false
        String assignedProject
        String labels
        String i9p1

        if (ap.isEmpty())
            assignedProject = ""
        else if (ap.size() > 1)
            assignedProject = "(" + ap.size() + " different projects)"
        else {
            Record r = ap.get(0);
            assignedProject = r.getString("description") + " (" + r.getString("shift_start") + ")"
        }

        if (assignedFrom > 20100101  &&  assignedTo > 20100101) {
            Record enacount = db.fetchOne("""select count(*) 
                                                 from employee_not_available 
                                                 where employee_id=? and 
                                                       (start_date <= ? and end_date >= ? or
                                                        start_date <= ? and end_date >= ?)
                                                 """,
                    personId, assignedFrom, assignedFrom, assignedTo, assignedTo)
            obj.put("available", enacount.getLong('count') == 0)
        } else {
            int today = org.kissweb.DateUtils.today()
            Record enacount = db.fetchOne("""select count(*) 
                                                 from employee_not_available 
                                                 where employee_id=? and 
                                                       (start_date <= ? and end_date >= ? or
                                                        start_date <= ? and end_date >= ?)
                                                 """,
                    personId, today, today, today, today)
            obj.put("available", enacount.getLong('count') == 0)
        }

        StringBuilder sb = new StringBuilder(30);
        List<Record> lbls = cmd.fetchAll("select el.name " +
                "from employee_label_association ela " +
                "join employee_label el " +
                "  on ela.employee_label_id = el.employee_label_id " +
                "where ela.employee_id = ? and ela.completed = 'N'" +
                "order by el.name", personId);
        for (Record lbl : lbls) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(lbl.getString("name"));
        }
        labels = sb.toString();
        i9p1 = rec.getString("i9_part1") == "Y" ? "Yes" : ""

        obj.put("personId", personId)
        obj.put("lname", lname)
        obj.put("fname", fname)
        obj.put("active", active)
        obj.put("middleName", middleName)
        obj.put("jobTitle", jobTitle)
        obj.put("ssn", ssn)
        obj.put("type", type)
        obj.put("statusName", statusName)
        obj.put("dirty", dirty)
        obj.put("positionName", positionName)
        obj.put("assignedProject", assignedProject)
        obj.put("labels", labels)
        obj.put("i9p1", i9p1)

        return obj
    }

    private static void exportRow(DelimitedFileWriter dfw, JSONObject obj) {
        dfw.writeField obj.getString("lname")
        dfw.writeField obj.getString("fname")
        dfw.writeField obj.getString("middleName")
        dfw.writeField obj.getString("positionName")
        dfw.writeField obj.getString("assignedProject")
        String i9p1 = obj.getString("i9p1")
        dfw.writeField i9p1 ? i9p1 : "No"
        dfw.writeField obj.getString("labels")
        dfw.endRecord()
    }

    private static void writeColumnHeader(DelimitedFileWriter dfw) throws Exception {
        dfw.writeField("Last Name");
        dfw.writeField("First Name");
        dfw.writeField("Middle Name");
        dfw.writeField("Position");
        dfw.writeField("Assignment");
        dfw.writeField("I9 Part 1");
        dfw.writeField("Labels");
        dfw.endRecord();
    }
}
