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

package com.arahant.services.standard.components.searchCompany

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 4/1/20
 */
class SearchCompany {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String search_type = inJSON.getString("search_type")
        String search_term = inJSON.getString("search_term")
        int max = BProperty.getInt(StandardProperty.SEARCH_MAX)

        Connection db = KissConnection.get()
        String currentCompany = hsu.getCurrentCompany().getOrgGroupId()
        String select = "select og.org_group_id id, og.group_name, cd.org_group_id cd_id,\n" +
                "       c.org_group_id client_id, v.org_group_id vendor_id, a.agency_id a_id\n" +
                "from company_base cb\n" +
                "join org_group og\n" +
                "  on cb.org_group_id = og.org_group_id\n" +
                "left outer join company_detail cd\n" +
                "  on cb.org_group_id = cd.org_group_id\n" +
                "left outer join client c\n" +
                "  on cb.org_group_id = c.org_group_id\n" +
                "left outer join vendor v\n" +
                "  on cb.org_group_id = v.org_group_id\n" +
                "left outer join agency a\n" +
                "  on cb.org_group_id = a.agency_id\n" +
                "where og.org_group_id = og.owning_entity_id\n" +
                "    and (c.company_id = ? or c.company_id is null)\n" +
                "    and (v.company_id = ? or v.company_id is null)\n"
        if (search_term != null  &&  !search_term.isEmpty()) {
            search_term = search_term.toUpperCase()
            switch (search_type) {
                case '1': // starts with
                    search_term = search_term.toUpperCase() + "%"
                    select += "    and upper(og.group_name) like ?\n"
                    break
                case '2': // ends with
                    search_term = "%" + search_term
                    select += "    and upper(og.group_name) like ?\n"
                    break
                case '3': // contains
                    search_term = "%" + search_term + "%"
                    select += "    and upper(og.group_name) like ?\n"
                    break
                case '4': // exact match
                    select += "    and upper(og.group_name) = ?\n"
                    break
            }
        } else {
            search_term = "%"
            select += "    and og.group_name like ? "
        }
        List<Record> comps = db.fetchAll(max+1, select + "order by og.group_name", currentCompany, currentCompany, search_term)
        JSONArray ca = new JSONArray()
        for (int i=0 ; i < comps.size() && i < max ; i++) {
            Record comp = comps.get(i);
            JSONObject c = new JSONObject();
            c.put("id", comp.getString("id"))
            c.put("name", comp.getString("group_name"))
            if (comp.getString("cd_id") != null)
                c.put("type", "Company")
            else if (comp.getString("client_id") != null)
                c.put("type", "Client")
            else if (comp.getString("vendor_id") != null)
                c.put("type", "Vendor")
            else if (comp.getString("a_id") != null)
                c.put("type", "Agency")
            else
                c.put("type", "")
            ca.put(c)
        }
        outJSON.put("companies", ca)
        outJSON.put("isMore", comps.size() > max)
    }
}
