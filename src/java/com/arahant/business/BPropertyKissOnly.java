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

package com.arahant.business;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

/**
 * Author: Blake McBride
 * Date: 5/17/23
 *
 * Use this class directly on code that doesn't use Hibernate like command line programs.
 */
public class BPropertyKissOnly {

    private static final ArahantLogger logger = new ArahantLogger(BPropertyKissOnly.class);

    public static String get(String name, String defaultVal) {
        String val;
        final Connection db = KissConnection.get();
        final Command cmd = db.newCommand();
        try {
            Record rec = cmd.fetchOne("select prop_value from property where prop_name = ?", name);
            if (rec == null) {
                val = defaultVal;
                rec = db.newRecord("property");
                rec.set("prop_name", name);
                rec.set("prop_value", val);
                rec.addRecord();
            } else {
                val = rec.getString("prop_value");
                if (isEmpty(val))
                    val = defaultVal;
            }
        } catch (Exception e) {
            throw new ArahantException(e);
        }
        cmd.close();
        return val;
    }

    public static String get(String name) {
        return get(name, null);
    }

    public static int getInt(String name, int defaultVal) {
        final String sval = get(name, String.valueOf(defaultVal));
        if (isEmpty(sval))
            return defaultVal;
        else
            try {
                return Integer.parseInt(sval);
            } catch (NumberFormatException e) {
                return defaultVal;
            }
    }

    public static int getInt(String name) {
        return getInt(name, 0);
    }

    public static float getFloat(String name, float defaultVal) {
        final String sval = get(name, String.valueOf(defaultVal));
        if (isEmpty(sval))
            return defaultVal;
        else
            try {
                return Float.parseFloat(sval);
            } catch (NumberFormatException e) {
                return defaultVal;
            }
    }

    public static float getFloat(String name) {
        return getFloat(name, 0f);
    }

    public static boolean getBoolean(String name, boolean defaultVal) {
        final String sval = get(name, String.valueOf(defaultVal));
        if (isEmpty(sval))
            return defaultVal;
        else
            return sval.equalsIgnoreCase("TRUE") || sval.equalsIgnoreCase("YES");
    }

    public static boolean getBoolean(String name) {
        return getBoolean(name, false);
    }
    public static String getID(String name) {
        final String id = get(name);
        if (id == null || id.isEmpty())
            return null;
        return IDGeneratorKiss.expandKey(id);
    }

    public static String getIDWithCheck(final String name) {
        final String id = get(name);
        if (id == null || id.isEmpty())
            throw new ArahantException("Property " + name + " is not set.");
        if (!id.replaceAll("[0-9-]", "").isEmpty() ||                      // must have only characters 0-9 and -
                id.replaceAll("[^-]", "").length() != 1 ||                 // must have a single -
                id.length() < 3 || id.length() > 16 ||                     // must be between 3 and 16 characters in length
                id.charAt(0) == '-' || id.charAt(id.length()-1) == '-')    // - must be in the middle
            throw new ArahantException("Property " + name + " has an invalid value.");
        return IDGeneratorKiss.expandKey(id);
    }

    static boolean isEmpty(final String str) {
        return str == null || str.trim().isEmpty();
    }

}
