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
import org.kissweb.database.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * User: Blake McBride
 * Date: 6/20/18
 */
class SyncClientItemInventory {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        Connection db = KissConnection.get()
        Command cmd = db.newCommand()
        JSONArray records = injson.getJSONArray "records"
        int n = records.length()
        for (int i = 0; i < n; i++) {
            JSONObject nrec = records.getJSONObject i
            Record arec = cmd.fetchOne "select * from client_item_inventory where client_item_inventory_id=?", nrec.getString("client_item_inventory_id")
            if (arec == null) {
                arec = db.newRecord "client_item_inventory"
                copyString nrec, arec, "client_item_inventory_id"
                copyString nrec, arec, "location", 50
                copyString nrec, arec, "project_id"
                copyString nrec, arec, "item_category", 20
                copyString nrec, arec, "description", 150
                copyString nrec, arec, "model", 15
                copyString nrec, arec, "vendor", 45
                copyString nrec, arec, "vendor_item_code", 25
                copyString nrec, arec, "client_item_code", 12
                copyString nrec, arec, "unit_of_measure"
                copyInt nrec, arec, "quantity_on_hand"
                copyInt nrec, arec, "usable_quantity_on_hand"
                copyInt nrec, arec, "quantity_discarded"
                arec.addRecord()
            } else {
                copyString nrec, arec, "location", 50
                copyString nrec, arec, "item_category", 20
                copyString nrec, arec, "description", 150
                copyString nrec, arec, "model", 15
                copyString nrec, arec, "vendor", 45
                copyString nrec, arec, "vendor_item_code", 25
                copyString nrec, arec, "client_item_code", 12
                copyString nrec, arec, "unit_of_measure"
                copyInt nrec, arec, "quantity_on_hand"
                copyInt nrec, arec, "usable_quantity_on_hand"
                copyInt nrec, arec, "quantity_discarded"
                arec.update()
            }
        }

        List<Record> arecs = cmd.fetchAll("select * from client_item_inventory where project_id=?", injson.getString("project_id"))
        JSONArray ja = new JSONArray()
        for (Record rec in arecs) {
            JSONObject jo = new JSONObject()
            returnString jo, rec, "client_item_inventory_id"
            returnString jo, rec, "location"
            returnString jo, rec, "project_id"
            returnString jo, rec, "item_category"
            returnString jo, rec, "description"
            returnString jo, rec, "model"
            returnString jo, rec, "vendor"
            returnString jo, rec, "vendor_item_code"
            returnString jo, rec, "client_item_code"
            returnString jo, rec, "unit_of_measure"
            returnInt jo, rec, "quantity_on_hand"
            returnInt jo, rec, "usable_quantity_on_hand"
            returnInt jo, rec, "quantity_discarded"
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
