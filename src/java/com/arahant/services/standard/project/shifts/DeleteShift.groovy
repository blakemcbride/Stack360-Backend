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

package com.arahant.services.standard.project.shifts

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.FrontendException
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 3/29/22
 */
class DeleteShift {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String shiftId = injson.getString('shiftId')
        Connection db = hsu.getKissConnection()
        try {
            db.execute("delete from project_shift where project_shift_id = ?", shiftId)
        } catch (Exception e) {
            throw new FrontendException("Shift is being used.  Cannot delete.")
        }
    }
}
