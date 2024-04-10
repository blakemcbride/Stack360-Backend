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

package com.arahant.utils;

import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

/**
 * Author: Blake McBride
 * Date: 9/5/21
 */
public class Email {

    public static boolean canSendEmail(String ad) {
        if (!isValidEmailAddress(ad))
            return false;
        Connection db = ArahantSession.getKissConnection();
        Command cmd = db.newCommand();
        try {
            String domain = getDomain(ad);
            if (domain == null)
                return false;
            Record rec = cmd.fetchOne("select count(*) from authenticated_senders where address_type='D' and LOWER(address)=?", domain.toLowerCase());
            if (rec != null  &&  rec.getLong("count") > 0L)
                return true;
            rec = cmd.fetchOne("select count(*) from authenticated_senders where address_type='E' and LOWER(address)=?", ad.toLowerCase());
            return rec != null && rec.getLong("count") > 0;
        } catch (Exception e) {
            throw new ArahantException(e);
        } finally {
            cmd.close();
        }
    }

    public static boolean isValidDomain(String d) {
        if (d == null || d.length() < 3)
            return false;
        if (!d.replaceAll("[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.]", "").isEmpty())
            return false;
        if (d.charAt(0) == '.' ||  d.charAt(d.length()-1) == '.')
            return false;
        if (d.contains(".."))
            return false;
        if (!d.contains("."))
            return false;
        return true;
    }

    public static boolean isValidEmailAddress(String add) {
        if (add == null || add.length() < 5)
            return false;
        if (!add.replaceAll("[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.]", "").equals("@"))
            return false;
        final int idx = add.indexOf("@");
        final String dom = add.substring(idx+1);
        if (!isValidDomain(dom))
            return false;
        final String user = add.substring(0, idx);
        if (user.length() < 1  ||  user.charAt(0) == '.' ||  user.charAt(user.length()-1) == '.')
            return false;
        if (user.contains(".."))
            return false;
        return true;
    }

    private static String getDomain(String ad) {
        int i = ad.indexOf("@");
        if (i == -1)
            return null;
        String ret = ad.substring(i+1);
        if (ret.isEmpty())
            return null;
        return ret;
    }
}
