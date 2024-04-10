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

package com.arahant.services.standard.at.message

import com.arahant.utils.StandardProperty
import com.arahant.business.BMessage
import com.arahant.business.BPerson
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import com.arahant.utils.SQLUtils
import org.json.JSONArray
import org.json.JSONObject
import org.kissweb.DateTime
import org.kissweb.DateUtils
import org.kissweb.StringUtils
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 9/13/23
 */
class SearchMessages {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final Connection db = KissConnection.get()
        boolean includeBcc = true; // currentPersonId.equals(senderId);
        String currentPersonId = hsu.getCurrentPerson().getPersonId()

        final Command cmd = db.newCommand();
        final ArrayList<Object> args = new ArrayList<>();
        String stmt;

        stmt =  "with current_applicant_persons as ( " +
                "select p.* from person p " +
                "left join current_employee_status esn " +
                "  on p.person_id = esn.employee_id " +
                "left join hr_employee_status es " +
                "  on esn.status_id = es.status_id " +
                "join applicant a " +
                "  on a.person_id = p.person_id " +
                "where es.active = 'N' or es.active is null) " +

                "select m.message_id " +
                "from message m " +
                "left join message_to mt " +
                "  on m.message_id = mt.message_id " +
                "left join current_applicant_persons fp " +
                "  on m.from_person_id = fp.person_id " +
                "left join current_applicant_persons tp " +
                "  on mt.to_person_id = tp.person_id " +
                "where (fp.person_id is not null " +
                "       or tp.person_id is not null) ";

        if (!injson.getBoolean("includeHandled")) {
            stmt += " and (mt.to_show = 'Y' or m.from_show = 'Y') ";
        }

        if (injson.getInt("fromDate") > 0) {
            String op = SQLUtils.dateCompareOperator(injson.getInt("fromDateIndicator"));
            if (op != null) {
                stmt += "and m.created_date " + op + " ? ";
                args.add(Connection.toTimestamp(injson.getInt("fromDate")));
            }
        }

        if (injson.getInt("toDate") > 0) {
            String op = SQLUtils.dateCompareOperator(injson.getInt("toDateIndicator"));
            if (op != null) {
                stmt += "and m.created_date " + op + " ? ";
                args.add(Connection.toTimestamp(injson.getInt("toDate")));
            }
        }

        String str = injson.getString("subject")
        if (str != null && !str.isEmpty()) {
            stmt += "and lower(m.subject) like ? ";
            args.add(str.toLowerCase());
        }

        String senderId = injson.getString("senderId")
        if (isEmpty(senderId))
            senderId = injson.getString("receiverId")
        if (!isEmpty(senderId)) {
            stmt += "and (m.from_person_id = ? or ? in (select to_person_id from message_to mt where mt.message_id = m.message_id)) ";
            args.add(senderId);
            args.add(senderId);
        }

        if (injson.getString("sortType") == "L")
            stmt += "order by m.created_date desc, m.message_id";
        else
            stmt += "order by m.created_date, m.message_id";

        final List<Record> results = cmd.fetchAll(stmt, args)

        outjson.put("taskComplete", false);
        setMessages(outjson, results, currentPersonId, includeBcc);
    }

    static void setMessages(final JSONObject outjson, final List<Record> mrecs, String currentPersonId, boolean includeBcc) {
        final JSONArray messages = new JSONArray()
        int max = BProperty.getInt(StandardProperty.SEARCH_MAX)
        int n = 0
        String prevMessageId = null
        for (Record mrec : mrecs) {
            String messageId = mrec.getString("message_id")
            if (messageId == prevMessageId)
                continue
            else
                prevMessageId = messageId
            if (n++ >= max)
                break
            BMessage bm = new BMessage(messageId)
            JSONObject message = new JSONObject()
            message.put("messageId", messageId)
            Date dt = bm.getCreatedDate();
            message.put("messageDateTime", StringUtils.take(DateUtils.dayOfWeekName(dt), 3) + " " + DateTime.format(dt));
            String subject = bm.getSubject()
            if (subject == null  || subject.isEmpty())
                subject = bm.getMessage()
            message.put("subject", subject);

            JSONObject mp = new JSONObject()
            final String fromPersonId = bm.getFromPersonId();
            mp.put("personId", fromPersonId)
            mp.put("displayName", bm.getFromName())
            if (fromPersonId != null)
                mp.put("phoneNumber", new BPerson(fromPersonId).getMobilePhone());
            message.put("fromPerson", mp);
            boolean handled = currentPersonId == fromPersonId && bm.getFromShow() == (char) 'N'
            message.put("sendEmail", bm.getSendEmail() == (char) 'Y')
            message.put("sendText", bm.getSendText() == (char) 'Y')
            message.put("sendInternal", bm.getSendInternal() == (char) 'Y')

            final Connection db = KissConnection.get();
            JSONArray mpl = new JSONArray();
            List<Record> recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_show, mt.to_name " +
                    "from message_to mt " +
                    "join person p " +
                    "  on mt.to_person_id = p.person_id " +
                    "where mt.message_id=? and send_type='T'", messageId);
            for (Record rec : recs) {
                JSONObject tmp = new JSONObject()
                tmp.put("personId", rec.getString("to_person_id"))
                tmp.put("displayName", BMessage.getToName(rec))
                if (!handled)
                    handled = currentPersonId == rec.getString("to_person_id") && rec.getChar("to_show") == (char) 'N'
                tmp.put("handled", rec.getChar("to_show") == (char) 'N')
                tmp.put("phoneNumber", new BPerson(rec.getString("to_person_id")).getMobilePhone())
                mpl.put(tmp)
            }
            message.put("toPersons", mpl)

            mpl = new JSONArray();
            recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_show, mt.to_name " +
                    "from message_to mt " +
                    "join person p " +
                    "  on mt.to_person_id = p.person_id " +
                    "where mt.message_id=? and send_type='C'", messageId);
            for (Record rec : recs) {
                JSONObject tmp = new JSONObject()
                tmp.put("personId", rec.getString("to_person_id"))
                tmp.put("displayName", BMessage.getToName(rec))
                tmp.put("handled", rec.getChar("to_show") == (char) 'N')
                if (!handled)
                    handled = currentPersonId == rec.getString("to_person_id") && rec.getChar("to_show") == (char) 'N'
                tmp.put("phoneNumber", new BPerson(rec.getString("to_person_id")).getMobilePhone())
                mpl.put(tmp)
            }
            message.put("ccPersons", mpl)

            if (includeBcc) {
                mpl = new JSONArray();
                recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_show, mt.to_name " +
                        "from message_to mt " +
                        "join person p " +
                        "  on mt.to_person_id = p.person_id " +
                        "where mt.message_id=? and send_type='B'", messageId);
                for (Record rec : recs) {
                    JSONObject tmp = new JSONObject()
                    tmp.put("personId", rec.getString("to_person_id"))
                    tmp.put("displayName", BMessage.getToName(rec))
                    tmp.put("handled", rec.getChar("to_show") == (char) 'N')
                    if (!handled)
                        handled = currentPersonId == rec.getString("to_person_id") && rec.getChar("to_show") == (char) 'N'
                    tmp.put("phoneNumber", new BPerson(rec.getString("to_person_id")).getMobilePhone())
                    mpl.put(tmp)
                }
                message.put("bccPersons", mpl)
            }
            message.put("handled", handled)
            messages.put(message)
        }
        outjson.put("messages", messages);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
