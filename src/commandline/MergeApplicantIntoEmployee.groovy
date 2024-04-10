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

import com.arahant.business.BPerson
import com.arahant.utils.IDGeneratorKiss
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 3/8/23
 */


class MergeApplicantIntoEmployee {

    public static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        moveApplicantToEmployee(db, "00001-0000023100", "00001-0000023175")
        db.commit()
        db.close()
    }

    /**
     * Merge applicant appPersonId with employee empPersonId.
     * The end result is empPersonId.  appPersonId is no longer present.
     *
     * @param db
     * @param appPersonId personId of applicant
     * @param empPersonId personId of employee
     */
    private static void moveApplicantToEmployee(Connection db, String appPersonId, String empPersonId) {
        moveRecords(db, appPersonId, empPersonId, "education", "education_id", "person_id")
        moveRecords(db, appPersonId, empPersonId, "phone", "phone_id", "person_join")
        moveRecords(db, appPersonId, empPersonId, "hr_emergency_contact", "contact_id", "person_id")
        moveRecords(db, appPersonId, empPersonId, "address", "address_id", "person_join")
        moveRecords(db, appPersonId, empPersonId, "previous_employment", "employment_id", "person_id")
        moveRecords(db, appPersonId, empPersonId, "person_form", "person_form_id", "person_id")
        db.execute("delete from applicant_answer where person_id = ?", appPersonId)
        db.execute("delete from appointment_person_join where person_id = ?", appPersonId)
        db.execute("delete from applicant_contact where person_id = ?", appPersonId)
        db.execute("delete from applicant_application where person_id = ?", appPersonId)
        db.execute("delete from applicant where person_id =?", appPersonId)
        Record oldLogin = db.fetchOne("select * from prophet_login where person_id = ?", empPersonId)
        Record newLogin = db.fetchOne("select * from prophet_login where person_id = ?", appPersonId)
        if (oldLogin != null && newLogin != null) {
            oldLogin.set("can_login", "Y")
            oldLogin.set("user_login", newLogin.getString("user_login"))
            oldLogin.set("user_password", BPerson.encryptPassword(empPersonId, BPerson.decryptPassword(newLogin.getString("user_password"))))
            oldLogin.set("password_effective_date", newLogin.getInt("password_effective_date"))
            oldLogin.setDateTime("when_created", newLogin.getDateTime("when_created"))
            oldLogin.set("authentication_code", newLogin.getString("authentication_code"))
            oldLogin.set("number_of_resends", newLogin.getShort("number_of_resends"))
            oldLogin.set("number_of_authentications", newLogin.getShort("number_of_authentications"))
            oldLogin.setDateTime("reset_password_date", newLogin.getDateTime("reset_password_date"))
            oldLogin.set("user_type", newLogin.getString("user_type"))
            db.execute("delete from prophet_login where person_id = ?", appPersonId)
            oldLogin.update()
        } else
            db.execute("delete from prophet_login where person_id = ?", appPersonId)
        db.execute("delete from person where person_id =?", appPersonId)
    }

    private static void moveRecords(Connection db, String appPersonId, String empPersonId, String table, String pkeyColumn, String personCol) {
        List<Record> recs = db.fetchAll("select * from " + table + " where " + personCol + " = ?", appPersonId)
        for (Record rec : recs) {
            Record nrec = db.newRecord(table)
            nrec.copyCorresponding(rec)
            IDGeneratorKiss.generate(nrec, pkeyColumn)
            nrec.set(personCol, empPersonId)
            nrec.addRecord()
            rec.delete()
        }
    }


}
