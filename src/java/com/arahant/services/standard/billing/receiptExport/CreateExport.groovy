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

package com.arahant.services.standard.billing.receiptExport

import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DelimitedFileWriter
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

import java.text.DecimalFormat

/**
 * Author: Blake McBride
 * Date: 5/18/18
 */
class CreateExport {

    private static int seq = 1

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString "project_id"
        String client_id = injson.getString "client_id"
        String expense_account_id = injson.getString "expense_account_id"
        String payment_method = injson.getString "payment_method"
        int receipt_start_date = injson.getInt "receipt_start_date"
        int receipt_end_date = injson.getInt "receipt_end_date"
        int upload_start_date = injson.getInt "upload_start_date"
        int upload_end_date = injson.getInt "upload_end_date"
        String worker_id = injson.getString "worker_id"

        DecimalFormat dateFmt = new DecimalFormat("00000000")
        String fname = "ReceiptExport-" + dateFmt.format(DateUtils.now()) + "-" + seq++
        File file = FileSystemUtils.createTempFile(fname, ".csv")
        doExport file, hsu,
                project_id,
                client_id,
                expense_account_id,
                payment_method,
                receipt_start_date,
                receipt_end_date,
                upload_start_date,
                upload_end_date,
                worker_id

        outjson.put("filename", FileSystemUtils.getHTTPPath(file))
    }

    private static void doExport(File file, HibernateSessionUtil hsu, project_id,
                                 client_id,
                                 expense_account_id,
                                 payment_method,
                                 receipt_start_date,
                                 receipt_end_date,
                                 upload_start_date,
                                 upload_end_date,
                                 worker_id) {
        DelimitedFileWriter dfw = new DelimitedFileWriter(file.getAbsolutePath())

        writeColumnHeaders(dfw)

        spinRecords(dfw, hsu, project_id,
                client_id,
                expense_account_id,
                payment_method,
                receipt_start_date,
                receipt_end_date,
                upload_start_date,
                upload_end_date,
                worker_id)

        dfw.close()
    }

    private static void spinRecords(DelimitedFileWriter dfw, HibernateSessionUtil hsu, project_id,
                                    client_id,
                                    expense_account_id,
                                    payment_method,
                                    receipt_start_date,
                                    receipt_end_date,
                                    upload_start_date,
                                    upload_end_date,
                                    worker_id) {
        String query = "select e.fname, e.mname, e.lname, p.description, a.description adesc, r.receipt_date, r.when_uploaded, " +
                "r.amount, r.payment_method, r.approved, r.who_approved, r.when_approved, r.business_purpose, " +
                "ap.fname ap_fname, ap.mname ap_mname, ap.lname ap_lname " +
                "from expense_receipt r " +
                "join project p " +
                "  on r.project_id = p.project_id " +
                "join person e " +
                "  on r.person_id = e.person_id " +
                "join expense_account a " +
                "  on r.expense_account_id = a.expense_account_id " +
                "left outer join person ap " +
                "  on r.who_approved = ap.person_id "
        boolean whereAdded = false
        boolean needAnd = false
        ArrayList<Object> args = new ArrayList<>()
        if (!project_id.isEmpty()) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.project_id = ? "
            args.add(project_id)
        }
        if (!client_id.isEmpty()) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "p.requesting_org_group = ? "
            args.add(client_id)
        }
        if (!expense_account_id.isEmpty()) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.expense_account_id = ? "
            args.add(expense_account_id)
        }
        if (!payment_method.isEmpty()  &&  payment_method != 'Z') {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.payment_method = ? "
            args.add(payment_method)
        }
        if (receipt_start_date > 0) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.receipt_date >= ? "
            args.add(receipt_start_date)
        }
        if (receipt_end_date > 0) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.receipt_date < ? "
            args.add(DateUtils.addDays(receipt_end_date, 1))
        }
        if (upload_start_date > 0) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.when_uploaded >= ? "
            args.add(DateUtils.getDate((int)upload_start_date))
        }
        if (upload_end_date > 0) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.when_uploaded < ? "
            args.add(DateUtils.getDate(DateUtils.addDays(upload_end_date, 1)))
        }
        if (!worker_id.isEmpty()) {
            if (!whereAdded) {
                query += "where "
                whereAdded = true
            }
            if (needAnd)
                query += " and "
            else
                needAnd = true
            query += "r.person_id = ? "
            args.add(worker_id)
        }
        query += "order by r.receipt_date"
        Connection con = KissConnection.get()
        Command cmd = con.newCommand()
        Cursor cursor = cmd.query(query, args)

        while (cursor.next) {
            Record rec = cursor.getRecord()
            String lname = rec.getString("lname")
            String fname = rec.getString("fname")
            String mname = rec.getString("mname")
            String name = lname + ", " + fname
            if (mname != null)
                mname = mname.trim()
            else
                mname = ""
            if (mname.length() > 0)
                name += " " + mname
            dfw.writeField name
            dfw.writeField rec.getString("description")
            dfw.writeField rec.getString("adesc")
            dfw.writeField DateUtils.getDateFormatted(rec.getInt("receipt_date"))
            dfw.writeField DateUtils.getDateFormatted(rec.getDateTime("when_uploaded"))
            dfw.writeField rec.getString("business_purpose")
            dfw.writeField rec.getDouble("amount")
            switch (rec.getChar("payment_method")) {
                case 'A':  dfw.writeField 'Company Debit Card'; break;
                case 'B':  dfw.writeField 'Employee Comdata Card'; break;
                case 'E':  dfw.writeField 'Employee Personal Account'; break;
                default:   dfw.writeField '';  break;
            }
            dfw.writeField(rec.getChar("approved") == (char) 'Y' ? 'Approved' : 'Not Approved')
            if (rec.getString("who_approved") != null) {
                String ap_fname = rec.getString("ap_fname")
                String ap_mname = rec.getString("ap_mname")
                String ap_lname = rec.getString("ap_lname")
                String ap_name = ap_lname + ", " + ap_fname
                if (ap_mname != null)
                    ap_mname = ap_mname.trim()
                else
                    ap_mname = ""
                if (ap_mname.length() > 0)
                    ap_name += " " + mname
                dfw.writeField ap_name
            } else
                dfw.writeField ''
            if (rec.get("when_approved") != null)
                dfw.writeField DateUtils.getDateFormatted(rec.getDateTime("when_approved"))

            dfw.endRecord()
        }
    }

    private static void writeColumnHeaders(DelimitedFileWriter dfw) {
        dfw.writeField "Worker Name"
        dfw.writeField "Project Summary"
        dfw.writeField "Account"
        dfw.writeField "Receipt Date"
        dfw.writeField "When Uploaded"
        dfw.writeField "Business Purpose"
        dfw.writeField "Amount"
        dfw.writeField "Payment Method"
        dfw.writeField "Approved"
        dfw.writeField "Who Approved"
        dfw.writeField "When Approved"
        dfw.endRecord()
    }

}
