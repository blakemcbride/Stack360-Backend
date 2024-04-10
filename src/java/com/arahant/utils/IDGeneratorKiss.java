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
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Blake McBride
 * Date: 6/9/19
 *
 * This generated new Arahant keys like IDGenerator except that is works without Hibernate.
 * This version only works if Arahant is the only single process changing the database.
 * If other processes are to be changing the database, this method should be its own server.
 *
 */
public class IDGeneratorKiss {
    static String dbid;
    static final HashMap<String, Integer> idCache = new HashMap<>();


    /**
     * Generates and returns a primary key used for the next new record.
     *
     * @param rec
     * @param keyCol
     * @return
     */
    public static String generate(Record rec, String keyCol) {
        final String key = generate(rec.getConnection(), rec.getTableName(), keyCol);
        rec.set(keyCol, key);
        return key;
    }

    /**
     * Generates and returns a primary key used for the next new record.
     *
     * @param db
     * @param tableName
     * @param id_column
     * @return
     */
    public static String generate(final Connection db, final String tableName, final String id_column) {
        synchronized (idCache) {
            if (tableName.endsWith("_h")  &&  id_column.equals("history_id")) {
                UUID uuid = UUID.randomUUID();
                return (Long.toHexString(uuid.getLeastSignificantBits()) + Long.toHexString(uuid.getMostSignificantBits()));
            }
            if (dbid == null) {
                try {
                    Record r = db.fetchOne("select prop_value from property where prop_name='DBID'");
                    if (r == null)
                        throw new Exception("");
                    dbid = String.format("%5s", r.getString("prop_value")).replace(' ', '0');
                    r.close();
                } catch (Exception e) {
                    dbid = "00001";
                }
            }

            int top;
            Integer otop = idCache.get(tableName);
            if (otop != null)
                top = otop;
            else {
                try {
                    Record r = db.fetchOne("select " + id_column + " from " + tableName + " where " + id_column + " like '" + dbid + "-%' order by " + id_column + " desc");
                    if (r == null)
                        top = 0;
                    else {
                        String key = r.getString(id_column);
                        top = Integer.parseInt(key.substring(6));
                    }
                } catch (Exception e) {
                    throw new ArahantException(e);
                }
            }
            idCache.put(tableName, ++top);
            return dbid + "-" + String.format("%010d", top);
        }
    }

    /**
     * Remove leading zeros in record key.  Turns 00001-0000000082 into 1-82
     *
     * @param id i.e. "00001-0000000082"
     * @return i.e.  "1-82"
     */
    public static String shrinkKey(String id) {
        int dashIdx = id.indexOf('-');
        int dbId = Integer.parseInt(id.substring(0, dashIdx));
        int recordId = Integer.parseInt(id.substring(dashIdx + 1));

        return dbId + "-" + recordId;
    }

    /**
     * Converts a db record key from short format to long format.
     *
     * @param extId i.e. "1-82"
     * @return i.e. "00001-0000000082"
     */
    public static String expandKey(String extId) {
        if (extId == null || extId.isEmpty())
            return extId;
        int dashIdx = extId.indexOf('-');
        StringBuilder sb = new StringBuilder();
        String dbId = "";
        String recordId = "";

        if (dashIdx != -1) {
            dbId = extId.substring(0, dashIdx);
            recordId = extId.substring(dashIdx + 1);
        }

        for (int idx = 0; idx < (5 - dbId.length()); idx++)
            sb.append("0");
        sb.append(dbId);

        sb.append("-");

        for (int idx = 0; idx < (10 - recordId.length()); idx++)
            sb.append("0");
        sb.append(recordId);

        return sb.toString();
    }
}
