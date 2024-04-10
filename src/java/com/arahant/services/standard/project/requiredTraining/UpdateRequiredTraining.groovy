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
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * @Author Arahant
 * @Created Dec 21, 2018
 * @Copyright Arahant LLC.
 */
class UpdateRequiredTraining {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String projectTrainingId = inJSON.getString("project_training_id")
        String projectId = inJSON.getString("project_id")
        String categoryId = inJSON.getString("category_id")
        Boolean required = inJSON.getBoolean("required")

        BProjectTraining bpt = new BProjectTraining(projectTrainingId)
        bpt.setProject(new BProject(projectId))
        bpt.setTrainingCategory(new BHRTrainingCategory(categoryId))
        bpt.setRequired(required ? (char) 'Y' : (char) 'N')
        bpt.update()
    }
}
