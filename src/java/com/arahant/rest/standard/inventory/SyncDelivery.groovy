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
class SyncDelivery {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        Connection db = KissConnection.get()
        Command cmd = db.newCommand()
        JSONArray records = injson.getJSONArray "records"
        int n = records.length()
        for (int i = 0; i < n; i++) {
            JSONObject nrec = records.getJSONObject i
            Record arec = cmd.fetchOne "select * from delivery where delivery_id=?", nrec.getString("delivery_id")
            if (arec == null) {
                arec = db.newRecord "delivery"
                copyString nrec, arec, "delivery_id"
                copyInt nrec, arec, "delivery_date"
                copyInt nrec, arec, "delivery_time"
                copyString nrec, arec, "who_accepted_delivery", 30
                copyString nrec, arec, "location", 50
                copyString nrec, arec, "project_po_id"
                arec.addRecord()
            } else {
                copyInt nrec, arec, "delivery_date"
                copyInt nrec, arec, "delivery_time"
                copyString nrec, arec, "who_accepted_delivery", 30
                copyString nrec, arec, "location", 50
                copyString nrec, arec, "project_po_id"
                arec.update()
            }
        }

        List<Record> arecs = cmd.fetchAll("select * from delivery where project_po_id in (select project_po_id from project_purchase_order where project_id=?)", injson.getString("project_id"))
        JSONArray ja = new JSONArray()
        for (Record rec in arecs) {
            JSONObject jo = new JSONObject()
            returnString jo, rec, "delivery_id"
            returnInt jo, rec, "delivery_date"
            returnInt jo, rec, "delivery_time"
            returnString jo, rec, "who_accepted_delivery"
            returnString jo, rec, "location"
            returnString jo, rec, "project_po_id"
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
