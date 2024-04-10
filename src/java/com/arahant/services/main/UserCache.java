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

package com.arahant.services.main;

import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Author: Blake McBride
 * Date: 3/23/18
 */
public class UserCache {

    private static final Hashtable<String, UserData> uuidTable = new Hashtable<>();
    private static LocalDateTime lastPurge = LocalDateTime.now();

    public enum LoginType {
        NORMAL,
        WORKER,
        APPLICANT
    }

    public static class UserData {
        public String username;
        public String password;
        public String personId;
        public String companyId;
        public LoginType loginType;
        private final String uuid;
        private LocalDateTime lastAccessDate;

        UserData(String user, String pw, String personId, String companyId, LoginType loginType) {
            username = user;
            password = pw;
            uuid = UUID.randomUUID().toString();
            lastAccessDate = LocalDateTime.now();
            this.personId = personId;
            this.companyId = companyId;
            this.loginType = loginType;
        }
    }

    public static String newUser(String user, String pw, String personId, String companyId, LoginType loginType) {
        UserData ud = new UserData(user, pw, personId, companyId, loginType);
        synchronized (uuidTable) {
            uuidTable.put(ud.uuid, ud);
        }
        return ud.uuid;
    }

    public static UserData findUuid(String uuid) {
        UserData ud;
        synchronized (uuidTable) {
            purgeOld();
            ud = uuidTable.get(uuid);
            if (ud != null)
                ud.lastAccessDate = LocalDateTime.now();
        }
        return ud;
    }

    public static void removeUuid(String uuid) {
        synchronized (uuidTable) {
            uuidTable.remove(uuid);
        }
    }

    /**
     * Find all of the instances of people logged in with a given name and invalidate them
     * except for user specified by uuid.
     *
     * @param personId the person to delete from the cache
     * @param uid except this uuid
     */
    public static void removePerson(String personId, String uid) {
        final ArrayList<String> found = new ArrayList<>();
        synchronized (uuidTable) {
            uuidTable.forEach((uuid,ud) -> {
                if (ud.personId.equals(personId) && !uuid.equals(uid))
                    found.add(uuid);
            });
            found.forEach(uuidTable::remove);
        }
    }

    private static void purgeOld() {
        // Don't purge more than once per minute
        if (60 > ChronoUnit.SECONDS.between(lastPurge, LocalDateTime.now()))
            return;
        ArrayList<String> purge = new ArrayList<>();
        LocalDateTime old = LocalDateTime.now();
        int maxSeconds = BProperty.getInt(StandardProperty.InactiveUserMaxSeconds, 1800);
        if (maxSeconds == 0)
            maxSeconds = 3600;  // no more than 60 minutes
        old = old.minusSeconds(maxSeconds);
        for (UserData ud : uuidTable.values())
            if (ud.lastAccessDate.isBefore(old))
                purge.add(ud.uuid);
        for (String s : purge)
            uuidTable.remove(s);
        lastPurge = LocalDateTime.now();
    }
}
