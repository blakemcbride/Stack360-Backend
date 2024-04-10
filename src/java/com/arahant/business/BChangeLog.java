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

package com.arahant.business;

import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.KissConnection;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

/**
 * Author: Blake McBride
 * Date: 6/13/23
 */
public class BChangeLog {

    /**
     * For changes to applicant_application.applicant_app_status_id
     *
     * @param personId the applicant the change is to effect
     * @param oldApplicantAppStatusId
     * @param newApplicantAppStatusId
     * @throws Exception
     */
    public static void applicantAppStatusChange(String personId, String oldApplicantAppStatusId, String newApplicantAppStatusId, String description) throws Exception {
        if (description == null && (oldApplicantAppStatusId == null && newApplicantAppStatusId == null ||
                oldApplicantAppStatusId != null && oldApplicantAppStatusId.equals(newApplicantAppStatusId)))
            return;
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        final Connection db = KissConnection.get();
        final Record crec = db.newRecord("change_log");
        crec.set("change_person_id", hsu.getCurrentPerson().getPersonId());
        crec.set("person_id", personId);
        crec.set("table_changed", "applicant_application");
        crec.set("column_changed", "applicant_app_status_id");

        String oldVal;
        if (oldApplicantAppStatusId == null)
            oldVal = null;
        else {
            Record rec = db.fetchOne("select status_name from applicant_app_status where applicant_app_status_id =?", oldApplicantAppStatusId);
            oldVal = rec.getString("status_name");
        }
        crec.set("old_value", oldVal);

        String newVal;
        if (newApplicantAppStatusId == null)
            newVal = null;
        else {
            Record rec = db.fetchOne("select status_name from applicant_app_status where applicant_app_status_id =?", newApplicantAppStatusId);
            newVal = rec.getString("status_name");
        }
        crec.set("new_value", newVal);

        crec.set("description", description);

        crec.addRecord();
    }

    /**
     * For changes to applicant.applicant_status_id
     *
     * @param personId the applicant the change is to effect
     * @param oldApplicantStatusId
     * @param newApplicantStatusId
     * @throws Exception
     */
    public static void applicantStatusChange(String personId, String oldApplicantStatusId, String newApplicantStatusId, String description) throws Exception {
        if (description == null && (oldApplicantStatusId == null && newApplicantStatusId == null ||
                oldApplicantStatusId != null && oldApplicantStatusId.equals(newApplicantStatusId)))
            return;
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        final Connection db = KissConnection.get();
        final Record crec = db.newRecord("change_log");
        if (hsu.getCurrentPerson() == null)
            crec.set("change_person_id", personId);  //  person adding themselves
        else
            crec.set("change_person_id", hsu.getCurrentPerson().getPersonId());
        crec.set("person_id", personId);
        crec.set("table_changed", "applicant");
        crec.set("column_changed", "status_id");

        String oldVal;
        if (oldApplicantStatusId == null)
            oldVal = null;
        else {
            Record rec = db.fetchOne("select name from applicant_status where applicant_status_id =?", oldApplicantStatusId);
            oldVal = rec.getString("name");
        }
        crec.set("old_value", oldVal);

        String newVal;
        if (newApplicantStatusId == null)
            newVal = null;
        else {
            Record rec = db.fetchOne("select name from applicant_status where applicant_status_id =?", newApplicantStatusId);
            newVal = rec.getString("name");
        }
        crec.set("new_value", newVal);

        crec.set("description", description);

        crec.addRecord();
    }

    /**
     * Log changes about <code>aboutPersonId</code>
     *
     * @param aboutPersonId the person the log is about (not the person making the log) - if null use the current logged in person
     * @param description
     * @throws Exception
     */
    public static void personLog(String aboutPersonId, String description) throws Exception {
        if (description == null  ||  description.isEmpty())
            return;
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        final Connection db = KissConnection.get();
        final Record crec = db.newRecord("change_log");
        Person currenlyLoggrdInPerson = hsu.getCurrentPerson();
        if (currenlyLoggrdInPerson == null) {
            if (aboutPersonId == null)
                return;  //  don't know anything
            crec.set("change_person_id", aboutPersonId);  //  person adding themselves
        } else {
            String currentLoggedInPersonId = currenlyLoggrdInPerson.getPersonId();
            crec.set("change_person_id", currentLoggedInPersonId);
            if (aboutPersonId == null)
                aboutPersonId = currentLoggedInPersonId;  // default to a person making a change about themselves
        }
        crec.set("person_id", aboutPersonId);

        crec.set("description", description);

        crec.addRecord();
    }

}
