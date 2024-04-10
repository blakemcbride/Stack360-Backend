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

package com.arahant.services.standard.project.workerAvailabilityReport

import com.arahant.beans.HrPosition
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 7/23/18
 * Modified: 1/2/19
 */
class GetPositions {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        List<HrPosition> poss = hsu.createCriteria(HrPosition.class)
                .le(HrPosition.FIRSTACTIVEDATE, DateUtils.today())
                .gtOrEq(HrPosition.LASTACTIVEDATE, DateUtils.today(), 0)
                .orderBy(HrPosition.NAME).list()
        JSONArray ary = new JSONArray()
        for (HrPosition pos : poss) {
            JSONObject jsonObject = new JSONObject()
            jsonObject.put "position_id", pos.getPositionId()
            jsonObject.put "position_name", pos.getName()
            ary.put(jsonObject)
        }
        outJSON.put("positions", ary)
    }
}
