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

package com.arahant.services.standard.components.searchClient

import com.arahant.beans.ClientCompany
import com.arahant.beans.CompanyDetail
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateCriteriaUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 5/17/18
 * Modified: 12/22/2018
 */
class SearchClients {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String search_type = inJSON.getString("search_type")
        String client_starts_with = inJSON.getString("client_starts_with")
        Boolean addTenant = inJSON.getBoolean("add_tenant")

        HibernateCriteriaUtil<ClientCompany> hcu = hsu.createCriteria(ClientCompany.class)
                .orderBy(CompanyDetail.NAME)

        if (client_starts_with != null && !client_starts_with.isEmpty())
            hcu.like(CompanyDetail.NAME, inJSON.getString("client_starts_with") + "%")

        List<ClientCompany> clients = hcu.list()
        int today = DateUtils.today()
        int n = 0
        JSONArray array = new JSONArray()

        // Add the tenant if requested by client.
        if (addTenant) {
            JSONObject jsonObject = new JSONObject()
            jsonObject.put("client_id", "")
            jsonObject.put("name", hsu.getCurrentCompany().name)
            array.put(jsonObject)
        }

        for (ClientCompany client : clients) {
            int inactiveDate = client.getInactiveDate()

            if (search_type == "active" && inactiveDate != 0 && inactiveDate < today)
                continue
            if (search_type == "inactive" && (inactiveDate == 0 || inactiveDate > today))
                continue

            JSONObject jsonObject = new JSONObject()
            jsonObject.put("client_id", client.getOrgGroupId())
            jsonObject.put("name", client.getName())
            array.put(jsonObject)
            if (++n > 40)
                break
        }

        outJSON.put("clients", array)
    }
}
