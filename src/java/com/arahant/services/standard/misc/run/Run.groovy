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

package com.arahant.services.standard.misc.run

import com.arahant.beans.Person
import com.arahant.business.BPerson
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.SendSMSTwilio
import org.json.JSONObject
import org.kissweb.database.*

/**
 * Author: Blake McBride
 * Date: 8/14/23
 *
 *      This screen and the corresponding code on the frontend are used for testing purposes only.
 *      Basically, this code can be edited to execute any code when the run button is clicked in the context
 *      of a fully running system.  This is useful for testing and debugging.
 */
class Run {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final Connection db = hsu.getKissConnection()
        if (false) {
            String r = new SendSMSTwilio().sendSMS("615-394-6760", "Please contact Blake 15", null)
            int x = 1
        } else if (true){
            db.execute("delete from message_to")
            db.execute("delete from message")
            return
            Person p = hsu.getCurrentPerson()
            BPerson bp = new BPerson(p)
            String personId = p.getPersonId()
            String personName = bp.getNameFML()
            String lets = "ABCDEFGHIJKLMNOPQRSTUVW"
            int n = 1
            for (int i = 0; i < lets.length(); i++) {
                char c = lets.charAt(i)
                List<Record> recs = db.fetchAll(2, "select person_id, lname, fname from person where lname like '" + c + "%'")
                for (Record rec : recs) {
                    String toPersonId = rec.getString("person_id")
                    println rec.getString("lname")
                    Record mrec, mtrec
                    String messageId

                    // Sent messages
                    mrec = db.newRecord('message')
                    messageId = IDGenerator.generate(mrec, "message_id")
                    mrec.set("message", "From Message " + n)
                    mrec.set("from_person_id", personId)
                    mrec.setDateTime("created_date", new Date())
                    mrec.set("subject", "From Subject " + n)
                    mrec.set("from_name", personName)
                    mrec.set("send_internal", "Y")
                    mrec.addRecord()

                    mtrec = db.newRecord('message_to')
                    IDGenerator.generate(mtrec, "message_to_id")
                    mtrec.set("message_id", messageId)
                    mtrec.set("to_person_id", toPersonId)
                    mtrec.set("send_type", generateRandomType())
                    mtrec.set("sent", "Y")
                    mtrec.set("to_name", rec.getString("fname") + " " + rec.getString("lname"))
                    mtrec.addRecord()
                    n++


                    // Received messages
                    mrec = db.newRecord('message')
                    messageId = IDGenerator.generate(mrec, "message_id")
                    mrec.set("message", "From Message " + n)
                    mrec.set("from_person_id", toPersonId)
                    mrec.setDateTime("created_date", new Date())
                    mrec.set("subject", "From Subject " + n)
                    mrec.set("send_internal", "Y")
                    mrec.set("from_name", rec.getString("fname") + " " + rec.getString("lname"))
                    mrec.addRecord()

                    mtrec = db.newRecord('message_to')
                    IDGenerator.generate(mtrec, "message_to_id")
                    mtrec.set("message_id", messageId)
                    mtrec.set("to_person_id", personId)
                    mtrec.set("send_type", generateRandomType())
                    mtrec.set("sent", "Y")
                    mtrec.set("to_name", personName)
                    mtrec.addRecord()
                    n++


                }
            }
        } else {
            Command cmd = db.newCommand()
            Cursor c = cmd.query("select * from message")
            while (c.isNext()) {
                Record rec = c.getRecord()
                String msg = rec.getString("message")
                int x = 1
            }
        }
    }

    private static String generateRandomType() {
        Random random = new Random();
        int randomNumber = random.nextInt(3); // Generate a random number between 0 and 2
        switch (randomNumber) {
            case 0:
                return 'T';
            case 1:
                return 'B';
            case 2:
                return 'C';
            default:
                return 'T'; // Default case (should never be reached)
        }
    }


}
