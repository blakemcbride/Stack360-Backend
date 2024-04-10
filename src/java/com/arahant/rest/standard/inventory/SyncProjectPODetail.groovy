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

package com.arahant.rest.standard.inventory

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONArray
import org.json.JSONObject

/**
 * User: Blake McBride
 * Date: 6/20/18
 */
class SyncProjectPODetail {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        Connection db = KissConnection.get()
        Command cmd = db.newCommand()
        JSONArray records = injson.getJSONArray "records"
        int n = records.length()
        for (int i = 0; i < n; i++) {
            JSONObject nrec = records.getJSONObject i
            Record arec = cmd.fetchOne "select * from project_po_detail where project_po_detail_id=?", nrec.getString("project_po_detail_id")
            if (arec == null) {
                arec = db.newRecord "project_po_detail"
                copyString nrec, arec, "project_po_detail_id"
                copyString nrec, arec, "project_po_id"
                copyInt nrec, arec, "po_line_number"
                copyString nrec, arec, "client_item_inventory_id"
                copyInt nrec, arec, "quantity_ordered"
                copyFloat nrec, arec, "price_each"
                copyInt nrec, arec, "when_due_date"
                copyInt nrec, arec, "when_due_time"
                arec.addRecord()
            } else {
                copyString nrec, arec, "project_po_id"
                copyInt nrec, arec, "po_line_number"
                copyString nrec, arec, "client_item_inventory_id"
                copyInt nrec, arec, "quantity_ordered"
                copyFloat nrec, arec, "price_each"
                copyInt nrec, arec, "when_due_date"
                copyInt nrec, arec, "when_due_time"
                arec.update()
            }
        }

        List<Record> arecs = cmd.fetchAll("select * from project_po_detail where project_po_id in (select project_po_id from project_purchase_order where project_id=?)", injson.getString("project_id"))
        JSONArray ja = new JSONArray()
        for (Record rec in arecs) {
            JSONObject jo = new JSONObject()
            returnString jo, rec, "project_po_detail_id"
            returnString jo, rec, "project_po_id"
            returnInt jo, rec, "po_line_number"
            returnString jo, rec, "client_item_inventory_id"
            returnInt jo, rec, "quantity_ordered"
            returnFloat jo, rec, "price_each"
            returnInt jo, rec, "when_due_date"
            returnInt jo, rec, "when_due_time"
            ja.put jo
        }
        outjson.put "records", ja
    }

    private static void copyString(JSONObject frec, Record trec, String fname) {
        trec.set fname, frec.getString(fname)
    }

    private static void copyInt(JSONObject frec, Record trec, String fname) {
        Object val = frec.opt(fname)
        if (val != null  &&  val != JSONObject.NULL)
            trec.set fname, frec.getInt(fname)
        else if (fname.equals("when_due_time"))
            trec.set fname, -1
        else
            trec.set fname, 0
    }

    private static void copyFloat(JSONObject frec, Record trec, String fname) {
        trec.set fname, frec.getFloat(fname)
    }

    private static void returnString(JSONObject jo, Record rec, String fname) {
        jo.put fname, rec.getString(fname)
    }

    private static void returnInt(JSONObject jo, Record rec, String fname) {
        jo.put fname, rec.getInt(fname)
    }

    private static void returnFloat(JSONObject jo, Record rec, String fname) {
        jo.put fname, rec.getFloat(fname)
    }

}
