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

import org.kissweb.DateUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 4/24/23
 *
 * This program updates applicant.applicant_status_id and applicant_application.applicant_app_status_id
 * for those that are active and prior employees.
 */

class CorrectApplicantStatus {

    private static final String APPLICANT_STATUS_HIRED = "00001-0000000003"
    private static final String APPLICANT_STATUS_IN_PROCESS = "00001-0000000001"
    private static final String APPLICANT_STATUS_PAST_EMPLOYEE = "00001-0000000004"
    private static final String APPLICATION_STATUS_HIRED = "00001-0000000038"
    private static int today = DateUtils.today()

    public static void main(String[] args) {
        int numberOfChanges = 0, totalChanges = 0
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from applicant")
        Record applicant
        while (c.isNext()) {
            applicant = c.getRecord()
            String personId = applicant.getString("person_id")
            if (personId == "00001-0000021164")
                personId = personId
            int actualEmploymentStatus = getActualEmploymentStatus(db, applicant)
            int reportedEmplomentStatus = getReportedEmploymentStatus(applicant)
            switch(actualEmploymentStatus) {
                case 0: // is not and never was an employee
                    if (reportedEmplomentStatus == 1) {
                        applicant.set("applicant_status_id", APPLICANT_STATUS_IN_PROCESS)
                        numberOfChanges++
                    }
                    break
                case 1: // is an employee
                    if (reportedEmplomentStatus == 0) {
                        applicant.set("applicant_status_id", APPLICANT_STATUS_HIRED)
                        numberOfChanges++
                    }
                    break
                case 2: // is a former employee
                    if (reportedEmplomentStatus == 1) {
                        applicant.set("applicant_status_id", APPLICANT_STATUS_PAST_EMPLOYEE)
                        numberOfChanges++
                    }
                    break
            }
            applicant.update()
            List<Record> applications = db.fetchAll("select * from applicant_application where person_id = ?", applicant.getString("person_id"))
            for (Record application : applications) {
                String reportedApplicationStatus = application.getString("applicant_app_status_id")
                switch(actualEmploymentStatus) {
                    case 0: // is not and never was an employee
                        if (reportedApplicationStatus == APPLICATION_STATUS_HIRED) {
                            application.set("applicant_app_status_id", "00001-0000000001")  // new application
                            numberOfChanges++
                        }
                        break;
                    case 1: // is an employee
                        if (reportedApplicationStatus != APPLICATION_STATUS_HIRED) {
                            application.set("applicant_app_status_id", APPLICATION_STATUS_HIRED)  // new application
                            numberOfChanges++
                        }
                        break;
                    case 2: // is a former employee
                        if (reportedApplicationStatus == APPLICATION_STATUS_HIRED) {
                            application.set("applicant_app_status_id", "00001-0000000001")  // new application
                            numberOfChanges++
                        }
                        break;
                }
                application.update()
            }
            if (numberOfChanges > 10) {
                db.commit()
                totalChanges += numberOfChanges
                numberOfChanges = 0
            }
        }
        db.commit()
        db.close()
        totalChanges += numberOfChanges
        println("Total number of changes = " + totalChanges)
    }

    /**
     * Actual employee status as of today.
     *
     *  0 = is not and never was an employee
     *  1 = is an employee
     *  2 = is a former employee
     */
    private static int getActualEmploymentStatus(Connection db, Record applicant) {
        Record esh = db.fetchOne("""select es.active
                                    from hr_empl_status_history esh
                                    join hr_employee_status es
                                      on esh.status_id = es.status_id
                                    where esh.employee_id = ? 
                                          and esh.effective_date <= ?
                                    order by esh.effective_date desc""", applicant.getString("person_id"), today)
        if (esh == null)
            return 0
        return esh.getString("active") == "Y" ? 1 : 2
    }

    /**
     * Reported by the applicant record.
     *
     *  0 = not current employee
     *  1 = is an employee
     */
    private static int getReportedEmploymentStatus(Record applicant) {
        String statusId = applicant.getString("applicant_status_id")
        return statusId == APPLICANT_STATUS_HIRED ? 1 : 0
    }
}