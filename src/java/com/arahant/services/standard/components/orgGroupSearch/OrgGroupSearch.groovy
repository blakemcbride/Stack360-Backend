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

/*
 * All rights reserved.
 *
 * Author: Blake McBride
 * Date: 6/25/21
 */

package com.arahant.services.standard.components.orgGroupSearch

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

class OrgGroupSearch {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String name = injson.getString('name')
        String nameType = injson.getString('nameType')
        final String companyId = hsu.getCurrentCompany().getCompanyId()
        int active = 1

        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);

        outjson.put('lowCap', lowCap)
        outjson.put('highCap', highCap)

        Connection db = KissConnection.get()

        ArrayList<Object> args = new ArrayList<>()
        String select = """select org_group_id, group_name
                           from org_group og
                           where owning_entity_id=?"""
        args.add(companyId)

        if (name != null  &&  !name.isEmpty())
            switch (nameType) {
                case '1':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(og.group_name) like lower(?)"
                    else
                        select += 'and og.group_name like ? '
                    args.add(name + '%')
                    break
                case '2':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(og.group_name) like lower(?)"
                    else
                        select += 'and og.group_name like ? '
                    args.add('%' + name)
                    break
                case '3':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(og.group_name) like lower(?)"
                    else
                        select += 'and og.group_name like ? '
                    args.add('%' + name + '%')
                    break
                case '4':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(og.group_name) = lower(?)"
                    else
                        select += 'and og.group_name = ? '
                    args.add(name)
                    break
            }
        select += " order by group_name"
        List<Record> recs = db.fetchAll(highCap, select, args.toArray())
        JSONArray jary = new JSONArray()
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            obj.put 'id', rec.getString('org_group_id')
            obj.put 'name', rec.getString('group_name')
            jary.put(obj)
        }

        outjson.put('orgGroups', jary)
    }

}

