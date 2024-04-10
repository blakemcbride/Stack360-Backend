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

package com.arahant.services.standard.time.trainingTime

import com.arahant.beans.ProjectTraining
import com.arahant.business.BHRTrainingCategory
import com.arahant.business.BProject
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

class GetProjectTrainings {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String projectId = inJSON.get("project_id")
        BProject project = new BProject(projectId)

        List<ProjectTraining> projectTrainings = hsu.createCriteria(ProjectTraining)
                .eq(ProjectTraining.PROJECT, project.bean)
                .list()

        JSONArray trainingCategories = new JSONArray()

        projectTrainings.forEach { training ->
            BHRTrainingCategory bTrainingCategory = new BHRTrainingCategory(training.trainingCategory.catId)

            JSONObject trainingCategory = new JSONObject()
            trainingCategory.put("categoryId", bTrainingCategory.trainingCategoryId)
            trainingCategory.put("categoryName", bTrainingCategory.name)

            trainingCategories.put(trainingCategory)
        }

        outJSON.put("trainingCategories", trainingCategories)
    }
}
