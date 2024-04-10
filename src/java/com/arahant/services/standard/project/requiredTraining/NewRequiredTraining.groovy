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

package com.arahant.services.standard.project.requiredTraining

import com.arahant.business.BHRTrainingCategory
import com.arahant.business.BProject
import com.arahant.business.BProjectTraining
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * @Author Arahant
 * @Created Dec 21, 2018
 * @Copyright Arahant LLC.
 */
class NewRequiredTraining {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String projectId = inJSON.getString("project_id")
        String categoryId = inJSON.getString("category_id")
        Boolean required = inJSON.getBoolean("required")

        // If the category ID is already in the database, return.
        Connection con = KissConnection.get()
        Record record = con.fetchOne("SELECT * FROM public.project_training WHERE (project_id = ?) AND (cat_id = ?)", projectId, categoryId)

        if (record != null) {
            throw new ArahantWarning("Category already exists.")
        }

        BProjectTraining bpt = new BProjectTraining()
        bpt.create()
        bpt.setProject(new BProject(projectId))
        bpt.setTrainingCategory(new BHRTrainingCategory(categoryId))
        bpt.setRequired(required ? (char) 'Y' : (char) 'N')
        bpt.insert()
    }
}
