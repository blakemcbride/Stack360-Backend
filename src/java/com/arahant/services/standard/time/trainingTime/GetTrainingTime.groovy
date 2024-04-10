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

import com.arahant.business.BEmployee
import com.arahant.business.BHRTrainingCategory
import com.arahant.business.BHRTrainingDetail
import com.arahant.business.BTimesheet
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

class GetTrainingTime {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String workerId = inJSON.getString("worker_id")

        // Get the current worker object.
        BEmployee worker = new BEmployee(workerId)

        // Get worker trainings.
        List<BHRTrainingDetail> workerTrainings = Arrays.asList(worker.listTrainingDetails())

        // Store the findings.
        Map<BHRTrainingDetail, BTimesheet> timesheetPerTraining = new LinkedHashMap<>()

        workerTrainings.forEach { training ->
            List<BTimesheet> timesheets = BTimesheet.search(workerId, training.trainingDate, training.trainingDate, 100)

            // Get the non-billable matching timesheet if there is any.
            BTimesheet matchingNBTimesheet = timesheets.find { timesheet ->
                if (timesheet.totalHours == training.trainingHours && timesheet.billable == (char) 'N')
                    return timesheet
            }

            if (matchingNBTimesheet != null) {
                timesheetPerTraining.put(training, matchingNBTimesheet)
            } else {
                // Get any matching timesheet.
                BTimesheet matchingTimesheet = timesheets.find { timesheet ->
                    if (timesheet.totalHours == training.trainingHours)
                        return timesheet
                }

                timesheetPerTraining.put(training, matchingTimesheet)
            }
        }

        JSONArray workerTrainingTimesheets = new JSONArray()

        timesheetPerTraining.forEach { training, timesheet ->
            JSONObject workerTrainingTimesheet = new JSONObject()

            if (timesheet != null) {
                workerTrainingTimesheet.put("timesheetId", timesheet.timesheetId)
                workerTrainingTimesheet.put("begin", timesheet.beginningTime / 100000)
                workerTrainingTimesheet.put("end", timesheet.endTime / 100000)
                workerTrainingTimesheet.put("hours", timesheet.totalHours)
                workerTrainingTimesheet.put("state", timesheet.state)
                workerTrainingTimesheet.put("billable", timesheet.billable)
                workerTrainingTimesheet.put('projectId', timesheet.project.id)
                workerTrainingTimesheet.put('projectName', timesheet.project.name)
                workerTrainingTimesheet.put('summary', timesheet.project.summary)
                workerTrainingTimesheet.put('client', timesheet.project.companyName)
            }

            workerTrainingTimesheet.put("trainingDate", training.trainingDate)
            workerTrainingTimesheet.put("expires", training.expireDate)

            // Get the training category.
            BHRTrainingCategory trainingCategory = new BHRTrainingCategory(training.trainingCategoryId)

            workerTrainingTimesheet.put("trainingId", training.trainingId)

            // Check the type of the training category and display the company accordingly.
            if (trainingCategory.client != null)
                workerTrainingTimesheet.put("company", trainingCategory.client.name)
            else
                workerTrainingTimesheet.put("company", hsu.currentCompany.name)

            workerTrainingTimesheet.put("training", trainingCategory.name)
            workerTrainingTimesheet.put("trainingCategoryId", trainingCategory.trainingCategoryId)

            workerTrainingTimesheets.put(workerTrainingTimesheet)
        }

        outJSON.put("workerTrainingTimesheets", workerTrainingTimesheets)
    }
}
