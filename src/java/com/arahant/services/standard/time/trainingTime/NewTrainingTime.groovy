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

import com.arahant.business.BHRTrainingDetail
import com.arahant.business.BTimesheet
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

class NewTrainingTime {

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        String employeeId = inJSON.get('employee-id')
        if (employeeId == null || employeeId == "") {
            throw new ArahantWarning("Employee ID is required.")
        }

        int trainingDate = inJSON.getInt('training-date')
        if (trainingDate == 0) {
            throw new ArahantWarning("Training date is required.")
        }

        String beginTime = inJSON.get('begin-time')
        if (beginTime == "null" || Integer.parseInt(beginTime) == 0) {
        //    throw new ArahantWarning("Begin time is required.")
            beginTime = '0'
        }

        String endTime = inJSON.get('end-time')
        if (endTime == "null" || Integer.parseInt(endTime) == 0) {
        //    throw new ArahantWarning("End time is required.")
            endTime = '0'
        }

        String hours = inJSON.get('hours')
        if (hours == "null" || Double.parseDouble(hours) == 0.0d) {
            throw new ArahantWarning("Hours is required.")
        }

        String projectId = inJSON.get('project-id')
        if (projectId == "null" || projectId == "") {
            throw new ArahantWarning("Project is required.")
        }

        String trainingCategoryId = inJSON.get('training-category-id')
        if (trainingCategoryId == "null" || trainingCategoryId == "") {
            throw new ArahantWarning("Training category is required.")
        }

        String billable = inJSON.get('billable')
        if (billable == '' || (billable != 'Y' && billable != 'N' && billable != 'U')) {
            throw new ArahantWarning("Billable is required.")
        }

        int expiration = inJSON.getInt('expiration')

        try {
            hsu.beginTransaction()

            // Create and insert a training detail record.
            BHRTrainingDetail trainingDetail = new BHRTrainingDetail()
            trainingDetail.create()
            trainingDetail.setEmployeeId(employeeId)
            trainingDetail.setTrainingCategoryId(trainingCategoryId)
            trainingDetail.setTrainingHours(Float.parseFloat(hours))
            trainingDetail.setTrainingDate(trainingDate)
            trainingDetail.setExpireDate(expiration)
            trainingDetail.insert()

            // Create and insert a timesheet record.
            BTimesheet timesheet = new BTimesheet()
            timesheet.create()
            timesheet.setPersonId(employeeId)
            timesheet.setProjectId(projectId)
            timesheet.setStartDate(trainingDate)
            timesheet.setBeginningTime(Integer.parseInt(beginTime) * 100000)
            timesheet.setEndTime(Integer.parseInt(endTime) * 100000)
            timesheet.setTotalHours(Double.parseDouble(hours))
            timesheet.setBillable(billable.toCharArray()[0])
            timesheet.insert()

            hsu.commitTransaction()
        } catch (Exception e) {
            throw e
        }
    }
}
