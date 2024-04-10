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

package com.arahant.services.standard.hr.ADPInterface

import com.arahant.interfaces.adp.ADPInterfaceDown
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/*
 * Author: Blake McBride
 * Date: 6/11/20
 */

class GetStatus {
    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        if (ADPInterfaceDown.isRunning()) {
            outjson.put("isRunning", true)
        } else {
            outjson.put("isRunning", false)
            Date lastComleted = ADPInterfaceDown.getLastCompletionDate()
            if (lastComleted != null) {
                outjson.put("lastCompletionDate", lastComleted.toString())
                outjson.put("totalWorkersReceived", ADPInterfaceDown.getTotalWorkersReceived())
                outjson.put("totalNewWorkers", ADPInterfaceDown.getTotalNewWorkers())
                outjson.put("totalUpdatedWorkers", ADPInterfaceDown.getTotalUpdatedWorkers())
                outjson.put("skippedOfficeWorkers", ADPInterfaceDown.getSkippedOfficeWorkers())
                outjson.put("hiredApplicants", ADPInterfaceDown.getHiredApplicants())
            } else
                outjson.put ("lastCompletionDate", null)
        }
    }
}
