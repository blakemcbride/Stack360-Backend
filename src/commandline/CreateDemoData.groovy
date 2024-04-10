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
 * This program converts a customer's database into a demo database.
 *
 * Main login:
 *     username:  demo
 *     password:  password
 *
 * Author: Blake McBride
 * Date: 10/4/23
 */
class CreateDemoData {

    public static void main(String[] args) {

        final String fromDBname = "waytogo"
        final String toDBname = "demo"
        final String loginPassword = 'password'
        final String externalFileRoot = "/home/blake/Stack360ExternalFiles"

        // Create database toDBname as a copy of fromDBname
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "postgres", "postgres", "postgres")
        db.executeImmediate("drop database if exists " + toDBname)
        db.executeImmediate("create database " + toDBname + " template " + fromDBname + " owner postgres")
        db.close()


        db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", toDBname, "postgres", "postgres")
        Command cmd = db.newCommand()
        Record rec
        Cursor c
        int n

        println "Obfuscating data for the " + toDBname + " database"

        println "person table"
        db.execute("update person set ssn = null") // to allow the system to run more than once
        db.commit()
        c = cmd.query("select person_id, ssn, lname from person")
        n = 1
        long ssn = 111111111
        String plname = null
        while ((rec = c.next()) != null) {
            rec.set("ssn", formatSSN(ssn++))
            String lname = rec.getString("lname")
            if (plname != null)
                rec.set("lname", plname)
            plname = lname
            rec.update()
            if (++n % 50 === 0)
                db.commit()
        }
        db.commit()

        db.execute("""update person set 
                                    personal_email = 'abc@email.com', 
                                    dob = 20000101, 
                                    drivers_license_number = null, 
                                    auto_insurance_policy = null, 
                                    convicted_of_what = null, 
                                    hic_number = null,
                                    i9p1_confirmation = null,
                                    i9p2_confirmation = null,
                                    linkedin = null
                   """)
        db.commit()

        db.execute("delete from person_change_request")
        db.commit()

        db.execute("delete from person_changed")
        db.commit()

        db.execute("delete from person_cr")
        db.commit()

        println "org_group table"
        c = cmd.query("select * from org_group")
        n = 1
        while ((rec = c.next()) != null) {
            rec.set("group_name", "Organization " + n)
            rec.update()
            if (++n % 50 === 0)
                db.commit()
        }
        db.commit()

        List<Record> recs = db.fetchAll("select project_status_id from project_status where active = 'Y'")
        Set<String> activeProjectStatuses = new HashSet<String>()
        for (Record r : recs)
            activeProjectStatuses.add(r.getString("project_status_id"))

        println "project table"
        c = cmd.query("select * from project")
        n = 1
        while ((rec = c.next()) != null) {
            rec.set("description", "Project " + n)
            rec.set("project_name", "Proj-" + n)
            rec.set("detail_desc", "Project detailed description")
            rec.set("location_description", n.toString())
            rec.set("estimated_last_date", 0)
            if (activeProjectStatuses.contains(rec.getString("project_status_id")))
                rec.set("estimated_first_date", DateUtils.addDays(DateUtils.today(), -10))
            rec.update()
            if (++n % 50 === 0)
                db.commit()
        }
        db.commit()

        db.execute("""update project_task_detail set
                      task_date = ?,
                      status = 0,
                      completion_date = 0
                   """, DateUtils.today())
        db.commit()


        println "Misc tables"
        db.execute("""update address set 
                                    street = '1234 Main Street', 
                                    city = 'My City', 
                                    state = 'OK', 
                                    zip = '12345', 
                                    street2 = null, 
                                    county = null
                   """)
        db.commit()

        db.execute("delete from address_cr")
        db.commit()

        db.execute("""update phone set
                      phone_number = '123-456-7890'
                   """)
        db.commit()

        db.execute("delete from phone_cr")
        db.commit()

        db.execute("""update message set 
                             message = 'The message',
                             subject = 'The subject',
                             from_address = 'abc@email.com'
                   """)
        db.commit()

        db.execute("""update company_detail set 
                             logo = null
                   """)
        db.commit()

        // This makes the main login "demo" with password "password"
        db.execute("""update prophet_login set 
                             user_login = 'demo'
                             where person_id = '00000-0000000000'
                   """)
        db.commit()

        db.execute("""update person set 
                             fname = 'John',
                             lname = 'Smith'
                             where person_id = '00000-0000000000'
                   """)
        db.commit()

        db.execute("""update org_group set 
                             group_name = 'Your Company'
                             where org_group_id = '00000-0000000005'
                   """)
        db.commit()

        db.execute("""update invoice_line_item set 
                             amount = 0,
                             adj_rate = 12.34
                   """)
        db.commit()


        db.execute("update prophet_login set user_password='" + loginPassword + "'")
        setProp(db, "PasswordExpiresAfterDays", "")
        setProp(db, "PasswordMinimumLength", "")
        setProp(db, "PasswordMinimumDigits", "")
        setProp(db, "PasswordMinimumLetters", "")
        setProp(db, "PasswordMinimumLowerCase", "")
        setProp(db, "PasswordMinimumSpecialChars", "")
        setProp(db, "PasswordMinimumUpperCase", "")
        setProp(db, "PasswordMinimumLowerCase", "")
        setProp(db, "RunADPImport", "")
        setProp(db, "InactiveUserMaxSeconds", "28800")
        setProp(db, "InactiveUserAlertMaxSeconds", "28800")
        setProp(db, "SMTP_HOST", "")
        setProp(db, "SMTP_PASSWORD", "")
        setProp(db, "SMTP_SENDER_EMAIL", "")
        setProp(db, "SMTP_SENDER_NAME", "")
        setProp(db, "SMTP_USERNAME", "")
        setProp(db, "EXTERNAL_FILE_ROOT", externalFileRoot)
        setProp(db, "TestEnvironment", "TRUE")
        setProp(db, "Announcement", "Test")
        setProp(db, "POSTMARK_API_KEY", "")
        setProp(db, "DoWmCoNotifyLateBilling", "FALSE")
        setProp(db, "EDIProduction", "FALSE")
        setProp(db, "SendTimesheetReminders", "FALSE")
        setProp(db, "RunCobraGuardInterface", "FALSE")
        setProp(db, "RunHRInvoicing", "FALSE")
        setProp(db, "SendBcbsEdi", "FALSE")
        setProp(db, "SendBcnEdi", "FALSE")
        setProp(db, "SendEbcEdi", "FALSE")
        setProp(db, "SendHumanaEdi", "FALSE")
        setProp(db, "InvoiceFormat", "1")
        db.commit()

        db.execute("update invoice set description = 'Invoice description'")
        db.commit()

        db.close()
        println "Done"
    }

    private static void setProp(Connection db, String prop, String val) {
        db.execute("insert into property (prop_name, prop_value, prop_desc) VALUES ('" + prop + "', '" + val + "', '" + prop + "') " +
                "on conflict (prop_name) " +
                "do update set prop_value = '" + val + "' where excluded.prop_name = '" + prop + "'")
    }

    private static String formatSSN(long ssn) {
        long firstPart = (long) (ssn / (long) 10000000)
        long secondPart = (ssn / 10000) % 100
        long thirdPart = ssn % 10000
        return String.format("%03d-%02d-%04d", firstPart as Integer, secondPart as Integer, thirdPart as Integer)
    }

}
