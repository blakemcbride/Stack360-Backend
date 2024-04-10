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
class SyncProjectPurchaseOrder {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        Connection db = KissConnection.get()
        Command cmd = db.newCommand()
        JSONArray records = injson.getJSONArray "records"
        int n = records.length()
        for (int i = 0; i < n; i++) {
            JSONObject nrec = records.getJSONObject i
            Record arec = cmd.fetchOne "select * from project_purchase_order where project_po_id=?", nrec.getString("project_po_id")
            if (arec == null) {
                arec = db.newRecord "project_purchase_order"
                copyString nrec, arec, "project_po_id"
                copyString nrec, arec, "project_id"
                copyString nrec, arec, "po_reference", 20
                copyString nrec, arec, "po_number", 20
                copyString nrec, arec, "vendor_name", 30
                arec.addRecord()
            } else {
                copyString nrec, arec, "po_reference", 20
                copyString nrec, arec, "po_number", 20
                copyString nrec, arec, "vendor_name", 30
                arec.update()
            }
        }

        List<Record> arecs = cmd.fetchAll("select * from project_purchase_order where project_id=?", injson.getString("project_id"))
        JSONArray ja = new JSONArray()
        for (Record rec in arecs) {
            JSONObject jo = new JSONObject()
            returnString jo, rec, "project_po_id"
            returnString jo, rec, "project_id"
            returnString jo, rec, "po_reference"
            returnString jo, rec, "po_number"
            returnString jo, rec, "vendor_name"
            ja.put jo
        }
        outjson.put "records", ja
    }

    private static void copyString(JSONObject frec, Record trec, String fname) {
        trec.set fname, frec.getString(fname)
    }

    private static void copyString(JSONObject frec, Record trec, String fname, int maxlen) {
        String data = frec.getString(fname)
        if (data != null  &&  data.length() > maxlen)
            data = data.substring(0, maxlen-1)
        trec.set fname, data
    }

    private static void copyInt(JSONObject frec, Record trec, String fname) {
        trec.set fname, frec.getInt(fname)
    }

    private static void returnString(JSONObject jo, Record rec, String fname) {
        jo.put fname, rec.getString(fname)
    }

    private static void returnInt(JSONObject jo, Record rec, String fname) {
        jo.put fname, rec.getInt(fname)
    }

}
