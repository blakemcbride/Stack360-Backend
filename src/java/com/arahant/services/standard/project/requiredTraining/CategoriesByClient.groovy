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
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Arahant
 * Date: 22/12/2018
 */
class CategoriesByClient {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String projectId = inJSON.getString("project-id")
        Boolean isTenant = inJSON.getBoolean("is-tenant")

        // Set client Id to null if it is a tenant category.
        String clientId = isTenant ? null: new BProject(projectId).getRequestingOrgGroupId()

        BHRTrainingCategory[] trainingCategories = BHRTrainingCategory.list(1, clientId)
        JSONArray array = new JSONArray()

        for (BHRTrainingCategory trainingCategory : trainingCategories) {
            if (trainingCategory.getClientId() == clientId) {
                JSONObject jsonObject = new JSONObject()
                jsonObject.put("category_id", trainingCategory.getTrainingCategoryId())
                jsonObject.put("name", trainingCategory.getName())
                array.put(jsonObject)
            }
        }

        outJSON.put("training_categories", array)
    }
}
