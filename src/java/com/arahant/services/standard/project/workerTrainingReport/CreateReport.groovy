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

package com.arahant.services.standard.project.workerTrainingReport

import com.arahant.beans.HrTrainingDetail
import com.arahant.beans.Person
import com.arahant.beans.ProjectEmployeeJoin
import com.arahant.beans.ProjectShift
import com.arahant.beans.ProjectTraining
import com.arahant.business.BProject
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.Groff
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

class CreateReport {

    private HibernateSessionUtil hsu
    private JSONObject outJSON
    private BProject project
    private List<ProjectTraining> projectTrainings
    private List<Person> projectEmployees

    static void main(JSONObject inJSON, JSONObject outJSON, HibernateSessionUtil hsu, REST service) {
        CreateReport inst = new CreateReport()
        inst.hsu = hsu
        inst.outJSON = outJSON

        String projectId = inJSON.getString("project_id")

        // Get the project and if the project is null throw an ArahantWarning.
        inst.project = new BProject(projectId)
        if (inst.project == null)
            throw new ArahantWarning("Invalid project ID.")

        // Get the project trainings.
        inst.projectTrainings = hsu.createCriteria(ProjectTraining)
                .eq(ProjectTraining.PROJECT, inst.project.getBean()).orderBy(ProjectTraining.TRAINING_CATEGORY).list()

        // Get project projectEmployee join and filter out the employees.
        List<ProjectEmployeeJoin> projectEmployees = hsu.createCriteria(ProjectEmployeeJoin)
                .orderBy(ProjectEmployeeJoin.PERSON)
                .joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
                .eq(ProjectShift.PROJECT, inst.project.getBean())
                .list()
        inst.projectEmployees = projectEmployees.collect { it.person }
        inst.projectEmployees.sort { emp -> emp.lname + ", " + emp.fname }

        inst.doRpt()
    }

    private void doRpt() {
        Groff rpt = new Groff("WTReport-", "", true)

        rpt.out(".tl ''\\s14Worker Training Report for Project''\\s0")
        rpt.out(".tl ''" + project.name.trim() + " - " + project.description.trim() + "''")
        rpt.out(".SP 2")

        // Get a Arahant connection object from the hibernate session.
        Connection conn = KissConnection.get()
        Command cmd = conn.newCommand()

        // Start a groff table with multi-page heading support..
        rpt.out(".TS H")
        rpt.out("L L L L C L C.")
        rpt.out("Worker\t\tTraining\t\tRequired\t\tTaken")
        rpt.out("\\_\t\t\\_\t\t\\_\t\t\\_")
        rpt.out(".TH")

        this.projectEmployees.forEach { employee ->

            if (notActive(cmd, employee.personId))
                return

            boolean first = true
            String taken, required
            String name = employee.lname + ", " + employee.fname

            Set<HrTrainingDetail> empTrainings = hsu.createCriteria(HrTrainingDetail)
                    .eq(HrTrainingDetail.EMPLOYEE, employee).list()

            Set<HrTrainingDetail> activeTrainings = empTrainings.findResults { empTraining ->
                if (empTraining.expireDate == 0 || empTraining.expireDate > DateUtils.now())
                    return empTraining
            }

            this.projectTrainings.forEach { training ->
                Number count = activeTrainings.count { empTraining ->
                    (empTraining.hrTrainingCategory == training.trainingCategory)
                }

                taken = count >= 1 ? "Yes" : "No"
                required = training.required == (char) "Y" ? "Yes" : "No"
                name = !first ? "" : name

                rpt.out(name + "\t\t" + training.trainingCategory.name + "\t\t" + required + "\t\t" + taken)

                first = false
            }

            // Leave out some space before next entry.
            rpt.out(".SP 1")
        }

        // Close the table.
        rpt.out(".TE")

        outJSON.put("url", rpt.process())
    }

    /**
     * Get whether the given person Id is active or not.
     *
     * @param cmd Command object.
     * @param person_id Person Id.
     * @return True if the person is inactive.
     */
    private static boolean notActive(Command cmd, String person_id) {
        Record rec = cmd.fetchOne("select es.active from hr_empl_status_history esh " +
                "join hr_employee_status es " +
                "  on esh.status_id = es.status_id " +
                "where esh.employee_id=? and esh.effective_date <= ? " +
                "order by esh.effective_date desc",
                person_id, DateUtils.today())
        if (rec == null)
            return true
        return rec.getString("active") == "N"
    }
}
