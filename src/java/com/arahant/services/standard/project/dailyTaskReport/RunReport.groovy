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

package com.arahant.services.standard.project.dailyTaskReport

import com.arahant.rest.GroovyService
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 11/10/23
 */
class RunReport {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String shiftId = injson.getString('shiftId')
        Integer workDate = injson.getInt('workDate')
        Object dayComplete = GroovyService.constructor("com.arahant.services.custom.waytogo.mobileapp", // package
                    "DayComplete"    // class
        )  //  args
        /*
        try {
            DayComplete dc = (DayComplete) dayComplete
        } catch (Exception e) {
            outjson.put("error", e.message)
            return
        }
         */
        String reportName = (String) GroovyService.run("com.arahant.services.custom.waytogo.mobileapp",  // package
                    "DayComplete",    // class
                    "taskReport",  // method
                    dayComplete,        // instance or null of class method
                    shiftId, workDate)  // args
        outjson.put("reportName", reportName)
    }
}
