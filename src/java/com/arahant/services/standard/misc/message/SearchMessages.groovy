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

package com.arahant.services.standard.misc.message

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
import org.kissweb.database.*

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

        stmt = "select m.message_id " +
                "from message m " +
                "left join message_to mt " +
                "  on m.message_id = mt.message_id " +
                "where  (mt.to_person_id = ? or m.from_person_id = ?)";

        args.add(currentPersonId);
        args.add(currentPersonId);
        if (!injson.getBoolean("includeHandled")) {
            stmt += " and (mt.to_show = 'Y' and mt.to_person_id = ? or m.from_show = 'Y' and m.from_person_id = ?) ";
            args.add(currentPersonId);
            args.add(currentPersonId);
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
        String prevMessageId = null
        int max = BProperty.getInt(StandardProperty.SEARCH_MAX)
        int n = 0
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
