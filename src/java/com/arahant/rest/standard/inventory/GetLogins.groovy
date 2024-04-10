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

package com.arahant.rest.standard.inventory

import com.arahant.beans.ProphetLogin
import com.arahant.business.BPerson
import com.arahant.servlets.REST
import com.arahant.utils.HibernateScrollUtil
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONArray
import org.json.JSONObject

import javax.xml.bind.DatatypeConverter
import java.security.MessageDigest

/**
 * Author: Blake McBride
 * Date: 6/6/18
 */
class GetLogins {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        HibernateScrollUtil<ProphetLogin> scr = hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.CANLOGIN, 'Y'.charAt(0)).scroll()
        JSONArray ary = new JSONArray()
        MessageDigest md = MessageDigest.getInstance("SHA1")
        while (scr.next()) {
            ProphetLogin pl = scr.get()
            BPerson bp = new BPerson(pl.getPersonId())
            JSONObject jobj = new JSONObject()
            jobj.put("person_id", pl.getPersonId())
            jobj.put("user_login", pl.getUserLogin().toLowerCase())

            String pw = pl.getPersonId() + bp.getUserPassword()  // seed with person_id
            byte[] bytes = pw.getBytes()
            byte[] hash = md.digest(bytes)
            String epw = DatatypeConverter.printHexBinary(hash).toLowerCase()

            jobj.put("user_password", epw)
            ary.put(jobj)
        }
        scr.close()
        outjson.put("logins", ary)
    }

}
