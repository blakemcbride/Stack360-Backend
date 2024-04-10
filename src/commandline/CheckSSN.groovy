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

import com.arahant.utils.Crypto
import org.kissweb.database.*


class CheckSSN {

    private static final String encKey = "2384658293468fac86876b65764ac987657fd8757b76c530";


    static void main(String[] args) {
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres")
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select person_id, ssn from person")
        Record rec1
        while ((rec1 = c.next()) != null) {
            String essn = rec1.getString("ssn")
            if (essn == null)
                continue
            String personId1 = rec1.getString("person_id")
            String ssn = decryptSsn(essn)
            if (ssn != null && (ssn.equals("999-99-9999") || ssn.startsWith("000-00"))) {
                db.execute("update person set ssn = null where person_id = ?", personId1)
                println "Making SSN null for " + personId1
                continue
            }
            if (essn.trim().isEmpty()) {
                db.execute("update person set ssn = null where person_id = ?", personId1)
                println "Making SSN null for " + personId1
                continue
            }
            List<Record> recs = db.fetchAll("select person_id from person where ssn = ?", essn)
            if (recs.size() == 1)
                continue
            Record er = db.fetchOne("select person_id from employee where person_id = ?", personId1)
            boolean isEmployee1 = er != null
            /*
            er = db.fetchOne("select person_id from applicant where person_id = ?", personId1)
            boolean isApplicant1 = er != null
             */
            for (Record rec2 : recs) {
                String personId2 = rec2.getString("person_id")
                if (personId1.equals(personId2))
                    continue
                er = db.fetchOne("select person_id from employee where person_id = ?", personId2)
                boolean isEmployee2 = er != null
                /*
                er = db.fetchOne("select person_id from applicant where person_id = ?", personId2)
                boolean isApplicant2 = er != null
                 */
                if (isEmployee1 && !isEmployee2) {
                    db.execute("update person set ssn = null where person_id = ?", personId2)
                    println "Eliminating applicant SSN for " + personId2 + " (" + personId1 + ")"
                    continue
                }
                if (isEmployee2 && !isEmployee1) {
                    db.execute("update person set ssn = null where person_id = ?", personId1)
                    println "Eliminating applicant SSN for " + personId1 + " (" + personId2 + ")"
                    personId1 = personId2
                    isEmployee1 = true
                    continue
                }
                if (!isEmployee2 && !isEmployee1) {
                    db.execute("update person set ssn = null where person_id = ?", personId2)
                    println "Eliminating applicant SSN for " + personId2 + " (" + personId1 + ")"
                    continue
                }
                if (isEmployee2 && isEmployee1) {
                    db.execute("update person set ssn = null where person_id = ?", personId1)
                    println "Eliminating employee SSN for " + personId1 + " (" + personId2 + ")"
                    db.execute("update person set ssn = null where person_id = ?", personId2)
                    println "Eliminating employee SSN for " + personId2 + " (" + personId1 + ")"
                    break
                }
            }
        }
        db.commit()
    }

    public static String decryptSsn(String ssn) {
        if (ssn != null && ssn.length() > 11)
            try {
                return Crypto.decryptTripleDES(encKey, ssn);
            } catch (Exception e) {
                return ssn;
            }
        return ssn;
    }
}