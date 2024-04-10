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

package com.arahant.groovy.exports

import com.arahant.utils.DateUtils
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/12/19
 */
class InvoiceExport2 {

    static void main(String [] argv) {

        if (argv.length != 0) {
            println "Usage:  ./run  InvoiceExport2.groovy"
            println "\nExample:   ./run  InvoiceExport2.groovy"
            return
        }

        File csvFile = FileSystemUtils.createTempFile("InvoiceExport-", ".csv")
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("MM/dd/yyyy")

        writeHeader dfw

        println "Sending output to " + csvFile.getAbsolutePath()

        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        Command cmd = db.newCommand()
        Cursor cur = cmd.query("""
                                            select  i.accounting_invoice_identifier, i.invoice_id, i.description, i.create_date, g.group_name, s.description ldesc,
                                                    s.adj_hours, s.adj_rate,
                                                    pr.description pdesc,
                                                    p.lname, p.fname, p.mname,
                                                    t.end_date, t.total_hours
                                            from timesheet t
                                            join invoice_line_item s
                                              on t.invoice_line_item_id = s.invoice_line_item_id
                                            join invoice i
                                              on s.invoice_id = i.invoice_id
                                            join org_group g
                                              on i.customer_id = g.org_group_id
                                            join person p
                                              on t.person_id = p.person_id
                                            join project pr
                                              on t.project_id = pr.project_id
                                            where i.create_date >= '2016-10-01' and i.create_date <= '2019-09-30'
                                              and i.customer_id <> '00001-0000000230'
                                              and i.customer_id <> '00001-0000000218'
                                              and i.customer_id <> '00001-0000000193'
                                              and i.customer_id <> '00001-0000000221'
                                              and i.customer_id <> '00001-0000000197'
                                              and i.customer_id <> '00001-0000000202'
                                              and i.customer_id <> '00001-0000000232'
                                              and i.customer_id <> '00001-0000000237'
                                              and i.customer_id <> '00001-0000000210'
                                            order by i.create_date, t.end_date
                                        """)
        while (cur.isNext()) {
            Record rec = cur.getRecord()
            dfw.writeField StringUtils.rightStrip(rec.getString('accounting_invoice_identifier'))
            dfw.writeField rec.getDateTime('create_date')
            dfw.writeField rec.getString('group_name')
            dfw.writeField rec.getString('description')

            dfw.writeField rec.getString("ldesc")
            float hours = rec.getFloat("adj_hours")
            dfw.writeField hours
            double rate = rec.getDouble("adj_rate")
            dfw.writeField rate
            dfw.writeField rate * hours

            dfw.writeField rec.getString('pdesc')
            String name = rec.getString("fname")
            name += " " + rec.getString("lname");
            dfw.writeField name
            dfw.writeField DateUtils.getDate(rec.getInt("end_date"))
            dfw.writeField rec.getDouble("total_hours")

            dfw.endRecord()
        }
        db.close()
        dfw.close()
    }

    private static void writeHeader(DelimitedFileWriter dfw) {
        dfw.writeField "Invoice Number"
        dfw.writeField "Invoice Date"
        dfw.writeField "Customer"
        dfw.writeField "Invoice Description"
        dfw.writeField "Line Description"
        dfw.writeField "Hours"
        dfw.writeField "Rate"
        dfw.writeField "Line Total"
        dfw.writeField "Project Description"
        dfw.writeField "Worker Name"
        dfw.writeField "Date Worked"
        dfw.writeField "Hours Worked"
        dfw.endRecord()
    }
}
