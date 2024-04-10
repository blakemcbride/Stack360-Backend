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
 * Date: 5/11/20
 */

package com.arahant.services.standard.components.prospectSearch

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

class ProspectSearch {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String name = injson.getString('name')
        String nameType = injson.getString('nameType')

        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);

        outjson.put('lowCap', lowCap)
        outjson.put('highCap', highCap)

        Connection db = hsu.getKissConnection()

        // first get clients
        ArrayList<Object> args = new ArrayList<>()
        String select = """
            select p.org_group_id, og.group_name
            from prospect p
            join org_group og
              on  p.org_group_id = og.org_group_id
            where 1=1  
        """

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
        List<Record> recs = db.fetchAll(highCap, select, args.toArray())
        ArrayList<JSONObject> ary = new ArrayList<>();
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            obj.put 'id', rec.getString('org_group_id')
            obj.put 'name', rec.getString('group_name')
            obj.put 'type', 'Prospect'
            ary.add(obj)
        }
        outjson.put('prospects', ary)
    }

}

