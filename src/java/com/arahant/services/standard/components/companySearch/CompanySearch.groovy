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

package com.arahant.services.standard.components.companySearch

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

class CompanySearch {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String name = injson.getString('name')
        String nameType = injson.getString('nameType')
        String active = injson.getString('active')
        Boolean includeClients = injson.getBoolean("includeClients")
        Boolean includeCompanies = injson.getBoolean("includeCompanies")
        Boolean includeVendors = injson.getBoolean("includeVendors")

        if (includeClients == null)
            includeClients = true
        if (includeCompanies == null)
            includeCompanies = true
        if (includeVendors == null)
            includeVendors = true

        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);

        outjson.put('lowCap', lowCap)
        outjson.put('highCap', highCap)

        Connection db = KissConnection.get()
        ArrayList<JSONObject> ary = new ArrayList<>()
        ArrayList<Object> args
        String select
        List<Record> recs

        if (includeClients) {
            // first get clients
            args = new ArrayList<>()
            select = """
            select c.org_group_id, og.group_name
            from client c
            join org_group og
              on  c.org_group_id = og.org_group_id
            where c.company_id = ? 
        """
            args.add(hsu.getCurrentCompany().getOrgGroupId())

            if (active == '1') {
                select += 'and (c.inactive_date = 0 or c.inactive_date > ?) '
                args.add(DateUtils.today())
            } else if (active == '2') {
                select += 'and (c.inactive_date <> 0 and c.inactive_date < ?) '
                args.add(DateUtils.today())
            }

            if (name != null && !name.isEmpty())
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
            recs = db.fetchAll(highCap, select, args.toArray())
            for (Record rec : recs) {
                JSONObject obj = new JSONObject()
                obj.put 'id', rec.getString('org_group_id')
                obj.put 'name', rec.getString('group_name')
                obj.put 'type', 'Client'
                ary.add(obj)
            }
        }

        // next get companies
        if (includeCompanies) {
            if (active != '2') {
                args = new ArrayList<>()
                select = """
            select c.org_group_id, og.group_name
            from company_detail c
            join org_group og
              on  c.org_group_id = og.org_group_id
            where c.org_group_id = ? 
        """
                args.add(hsu.getCurrentCompany().getOrgGroupId())

                if (name != null && !name.isEmpty())
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
                recs = db.fetchAll(highCap, select, args.toArray())
                for (Record rec : recs) {
                    JSONObject obj = new JSONObject()
                    obj.put 'id', rec.getString('org_group_id')
                    obj.put 'name', rec.getString('group_name')
                    obj.put 'type', 'Company'
                    ary.add(obj)
                }
            }
        }

        // next get vendors
        if (includeVendors) {
            if (active != '2') {
                args = new ArrayList<>()
                select = """
            select c.org_group_id, og.group_name
            from vendor c
            join org_group og
              on  c.org_group_id = og.org_group_id
            where c.company_id = ? 
        """
                args.add(hsu.getCurrentCompany().getOrgGroupId())

                if (name != null && !name.isEmpty())
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
                recs = db.fetchAll(highCap, select, args.toArray())
                for (Record rec : recs) {
                    JSONObject obj = new JSONObject()
                    obj.put 'id', rec.getString('org_group_id')
                    obj.put 'name', rec.getString('group_name')
                    obj.put 'type', 'Vendor'
                    ary.add(obj)
                }
            }
        }

        // sort
        Collections.sort(ary, new CompanySorter())

        // convert to JSON array
        JSONArray jary = new JSONArray()
        int len = ary.size()
        for (int i=0 ; i < len ; i++)
            jary.put(ary.get(i))

        outjson.put('companies', jary)
    }

    private static class CompanySorter implements Comparator<JSONObject> {

        @Override
        int compare(JSONObject o1, JSONObject o2) {
            return o1.getString('name') <=> o2.getString('name')
        }
    }

}

