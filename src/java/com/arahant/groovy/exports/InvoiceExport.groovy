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

import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/12/19
 */
class InvoiceExport {

    static void main(String [] argv) {

        if (argv.length != 0) {
            println "Usage:  ./run  InvoiceExport.groovy"
            println "\nExample:   ./run  InvoiceExport.groovy"
            return
        }

        File csvFile = FileSystemUtils.createTempFile("WalmartInvoiceExport-", ".csv")
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("MM/dd/yyyy")

        writeHeader dfw

        println "Sending output to " + csvFile.getAbsolutePath()

        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")

        List<Record> recs = db.fetchAll("""
                                            select i.invoice_id, i.description, i.create_date, g.group_name from invoice i
                                            join org_group g
                                              on i.customer_id = g.org_group_id
                                            where create_date >= '2016-10-01' and create_date <= '2019-09-30' 
                                            order by create_date
                                        """)
        for (Record rec : recs) {
            List<Record> drecs = db.fetchAll("select * from invoice_line_item where invoice_id=?", rec.getString("invoice_id"))
            for (Record drec : drecs) {
                dfw.writeField rec.getDateTime('create_date')
                dfw.writeField rec.getString('group_name')
                dfw.writeField rec.getString('description')

                dfw.writeField drec.getString("description")
                float hours = drec.getFloat("adj_hours")
                dfw.writeField hours
                double rate = drec.getDouble("adj_rate")
                dfw.writeField rate
                dfw.writeField rate * hours

                dfw.endRecord()
            }
        }
        db.close()
        dfw.close()
    }

    private static void writeHeader(DelimitedFileWriter dfw) {
        dfw.writeField "Invoice Date"
        dfw.writeField "Customer"
        dfw.writeField "Invoice Description"
        dfw.writeField "Line Description"
        dfw.writeField "Hours"
        dfw.writeField "Rate"
        dfw.writeField "Line Total"
        dfw.endRecord()
    }
}
