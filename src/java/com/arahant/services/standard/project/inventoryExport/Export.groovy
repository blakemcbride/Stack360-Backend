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

package com.arahant.services.standard.project.inventoryExport

import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.KissConnection
import org.kissweb.DelimitedFileWriter
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

import java.text.DecimalFormat

/**
 * Author: Blake McBride
 * Date: 9/5/18
 */
class Export {

    private static int seq = 1

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString "project_id"

        DecimalFormat dateFmt = new DecimalFormat("00000000")
        String fname = "ProjectInventoryExport-" + projectId(project_id) + "-" + dateFmt.format(DateUtils.now()) + "-" + seq++
        File file = FileSystemUtils.createTempFile(fname, ".csv")
        doExport file, hsu, project_id

        outjson.put("filename", FileSystemUtils.getHTTPPath(file))
    }

    private static String projectId(String project_id) {
        String s = StringUtils.drop(project_id, 6)
        while (s.startsWith("0"))
            s = StringUtils.drop(s, 1)
        return s
    }

    private static void doExport(File file, HibernateSessionUtil hsu, project_id) {
        DelimitedFileWriter dfw = new DelimitedFileWriter(file.getAbsolutePath())

        writeColumnHeaders(dfw)

        spinRecords(dfw, hsu, project_id)

        dfw.close()
    }

    private static void outBeginning(DelimitedFileWriter dfw, Record erec) {
        dfw.writeField erec.getString("vendor")
        dfw.writeField erec.getString("po_reference")
        dfw.writeField ""
        dfw.writeField erec.getString("po_number")
        dfw.writeField erec.getInt("po_line_number")
        dfw.writeField erec.getString("client_item_code")
        dfw.writeField erec.getString("vendor_item_code")
        dfw.writeField erec.getString("description")
        dfw.writeField erec.getString("item_category")
        dfw.writeField erec.getInt("quantity_ordered")
        dfw.writeField erec.getString("unit_of_measure")
        dfw.writeField DateUtils.getDateFormatted(erec.getInt("when_due_date"))
    }

    private static void spinRecords(DelimitedFileWriter dfw, HibernateSessionUtil hsu, String project_id) {
        Connection db = KissConnection.get()
        List<Record> erecs = db.fetchAll("select podet.project_po_detail_id, po.project_po_id, inv.vendor, po.po_reference, po.po_number, podet.po_line_number, " +
                "       inv.client_item_code, inv.vendor_item_code, inv.description, inv.item_category, podet.quantity_ordered, " +
                "       inv.unit_of_measure, podet.when_due_date " +
                "from project_po_detail podet " +
                "join project_purchase_order po " +
                "  on podet.project_po_id = po.project_po_id " +
                "left join client_item_inventory inv " +
                "  on podet.client_item_inventory_id = inv.client_item_inventory_id " +
                "where po.project_id = ? " +
                "order by podet.when_due_date ", project_id)
        for (Record erec in erecs) {
            List<Record> drecs = db.fetchAll("select d.delivery_date, dd.units_received, dd.condition " +
                    "from delivery_detail dd " +
                    "join delivery d " +
                    "  on dd.delivery_id = d.delivery_id " +
                    "where dd.project_po_detail_id = dd.project_po_detail_id and dd.project_po_detail_id = ?",
                    erec.getString("project_po_detail_id"))
            if (drecs.isEmpty()) {
                outBeginning dfw, erec
                dfw.endRecord()
            } else
                for (Record drec in drecs) {
                    outBeginning dfw, erec
                    dfw.writeDate drec.getInt("delivery_date")
                    dfw.writeField drec.getInt("units_received")
                    dfw.writeField  drec.getInt("units_received") - erec.getInt("quantity_ordered")
                    dfw.writeField drec.getString("condition")
                    dfw.endRecord()
                }
        }

    }

    private static void writeColumnHeaders(DelimitedFileWriter dfw) {
        dfw.writeField "Vendor Name"
        dfw.writeField "PO Ref"
        dfw.writeField "Buyer"
        dfw.writeField "PO No."
        dfw.writeField "PO Line Number"
        dfw.writeField "Item ID"
        dfw.writeField "Vnd Itm ID"
        dfw.writeField "Item Descr"
        dfw.writeField "Category"
        dfw.writeField "PO Qty"
        dfw.writeField "UOM"
        dfw.writeField "Due"
        dfw.writeField "Arrived"
        dfw.writeField "Qty Rec"
        dfw.writeField "Variance"
        dfw.writeField "Condition"
        dfw.endRecord()
    }

}
