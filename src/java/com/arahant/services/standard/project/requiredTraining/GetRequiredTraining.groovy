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

import com.arahant.beans.ProjectTraining
import com.arahant.business.BProject
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * @Author Arahant
 * @Created Dec 19, 2018
 * @Copyright Arahant LLC.
 */
class GetRequiredTraining {

    /**
     * Main entry point to the micro-service.
     *
     * @param inJSON Input parameters as a JSON object.
     * @param outJSON Output parameters as a JSON object.
     * @param hsu Hibernate session utility to be used.
     * @param service <code>REST</code> servlet instance.
     */
    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String projectId = inJSON.get("project_id")
        List<ProjectTraining> projectTrainings = hsu.createCriteria(ProjectTraining.class)
                .eq(ProjectTraining.PROJECT, (new BProject(projectId)).getBean())
                .orderBy(ProjectTraining.PROJECT)
                .list()

        JSONArray array = new JSONArray()
        for (ProjectTraining training : projectTrainings) {
            JSONObject jsonObject = new JSONObject()

            jsonObject.put("project_training_id", training.projectTrainingId)

            // Filter tenant training categories.
            String clientId
            String isCompany
            if (training.getTrainingCategory().client == null) {
                clientId = null
                isCompany = 'Yes'
            } else {
                clientId = training.getTrainingCategory().clientId
                isCompany = ''
            }

            jsonObject.put("client_id", clientId)
            jsonObject.put("is_company", isCompany)

            jsonObject.put("category_id", training.getTrainingCategory().getCatId())
            jsonObject.put("category_name", training.getTrainingCategory().getName())
            jsonObject.put("category_type", training.getTrainingCategory().getTrainingType())
            jsonObject.put("hours", training.getTrainingCategory().hours)
            jsonObject.put("required", training.getRequired())
            array.put(jsonObject)
        }

        outJSON.put("required_trainings", array)
    }
}
