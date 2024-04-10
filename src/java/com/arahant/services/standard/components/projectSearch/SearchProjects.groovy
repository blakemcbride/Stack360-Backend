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
 * Date: 5/9/20
 */

package com.arahant.services.standard.components.projectSearch

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

class SearchProjects {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);

        outjson.put('lowCap', lowCap)
        outjson.put('highCap', highCap)

        String select = """
            select p.project_id, p.reference, p.description, p.requesting_org_group, og2.group_name, cat.code cat_code,
            typ.code typ_code, s.code stat_code, p.billable, ps.project_shift_id, ps.shift_start
            from project_shift ps
            join project p
              on ps.project_id = p.project_id
            join org_group og
              on p.requesting_org_group = og.org_group_id
            join org_group og2
              on og.owning_entity_id = og2.org_group_id
            join project_category cat
              on p.project_category_id = cat.project_category_id
            join project_type typ
              on p.project_type_id = typ.project_type_id
            join project_status s
              on p.project_status_id = s.project_status_id
            where 1=1
        """
        ArrayList<Object> args = new ArrayList<>()

        String spid = injson.getString('projectId')
        if (spid != null  &&  !spid.isEmpty()) {
            spid = '00001-' + StringUtils.take('000000000' + spid, -10)
            select += 'and p.project_id = ? '
            args.add spid
        }

        String clientId = injson.getString('companyId')
        if (clientId != null  &&  !clientId.isEmpty()) {
            select += 'and p.requesting_org_group = ? '
            args.add(clientId)
        }

        String summary = injson.getString('summary')
        if (summary != null  &&  !summary.isEmpty()) {
            switch (injson.getString('summarySearchType')) {
                case '1':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(p.description) like lower(?)"
                    else
                        select += 'and p.description like ? '
                    args.add(summary + '%')
                    break
                case '2':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(p.description) like lower(?)"
                    else
                        select += 'and p.description like ? '
                    args.add('%' + summary)
                    break
                case '3':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(p.description) like lower(?)"
                    else
                        select += 'and p.description like ? '
                    args.add('%' + summary + '%')
                    break
                case '4':
                    if (db.getDBType() == Connection.ConnectionType.PostgreSQL)
                        select += "and lower(p.description) = lower(?)"
                    else
                        select += 'and p.description = ? '
                    args.add(summary)
                    break
            }
        }

        String catId = injson.getString('categoryId')
        if (catId != null  &&  !catId.isEmpty()) {
            select += 'and p.project_category_id = ? '
            args.add(catId)
        }

        String typeId = injson.getString('typeId')
        if (typeId != null  &&  !typeId.isEmpty()) {
            select += 'and p.project_type_id = ? '
            args.add(typeId)
        }

        String statusId = injson.getString('statusId')
        if (statusId != null  &&  !statusId.isEmpty()) {
            select += 'and p.project_status_id = ? '
            args.add(statusId)
        }

        String active = injson.getString('active')
        if (active == '1')
            select += "and (s.active='Y' or s.active = 'O') "
        else if (active == 2)
            select += "and s.active='N' "

        select += 'order by og.group_name, p.description'
        List<Record> recs = db.fetchAll(highCap, select, args.toArray())
        JSONArray ary = new JSONArray();
        for (Record rec : recs) {
            JSONObject obj = new JSONObject()
            String projectId = rec.getString('project_id')
            obj.put 'project_id', projectId
            obj.put 'shift_id', rec.getString('project_shift_id')
            obj.put 'shift_start', rec.getString('shift_start')
            obj.put 'pid', getPid(projectId)
            obj.put 'reference', rec.getString('reference')
            obj.put 'description', rec.getString('description')
            obj.put 'company_name', rec.getString('group_name')
            obj.put 'cat_code', rec.getString('cat_code')
            obj.put 'typ_code', rec.getString('typ_code')
            obj.put 'stat_code', rec.getString('stat_code')
            obj.put 'billable', rec.getString('billable')
            ary.put(obj)
        }
        outjson.put('projects', ary)
    }

    private static String getPid(String pid) {
        pid = StringUtils.drop(pid, 6)
        int len = pid.length()
        int i = 0
        for ( ; i < len ; i++)
            if (pid.charAt(i) != (char) '0')
                break
        return StringUtils.drop(pid, i)
    }

}
