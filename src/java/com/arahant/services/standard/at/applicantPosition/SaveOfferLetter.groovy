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

package com.arahant.services.standard.at.applicantPosition

import com.arahant.servlets.REST
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.FileUtils

/**
 * Author: Blake McBride
 * Date: 3/29/23
 */
class SaveOfferLetter {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String applicantPositionId = injson.getString("applicantPositionId")
        ExternalFile.saveData(ExternalFile.APPLICANT_POSITION_OFFER_LETTER, applicantPositionId, "html", injson.getString("html"))
    }

}
