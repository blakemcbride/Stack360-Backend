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

package com.arahant.db

import com.arahant.utils.KissConnection
import org.kissweb.database.Command
import org.kissweb.database.Connection
import org.kissweb.database.Cursor
import org.kissweb.database.Record

class P_5138 {

    public static run(Connection db) {
        int n = 0
        KissConnection.set(db)
        println "Begin updating screen paths"
        println "Reading records.  Please wait."
        Command cmd = db.newCommand()
        Cursor c = cmd.query("select * from screen")
        long size = c.size()
        while (c.isNext()) {
            Record rec = c.getRecord()
            rec.set("technology", "H")

            String filename = rec.getString("filename")
            filename = filename.replaceAll(/^com\/arahant\/app\/dynamicScreen\//, '')
            filename = filename.replaceAll(/^com\/arahant\/app\/screen\//, '')
            filename = filename.replaceAll(/^htmlFlex\//, '')
            filename = filename.replaceAll(/Screen\.swf$/, '')
            filename = filename.replaceAll(/\.swf$/, '')
            filename = filename.replaceAll(/^screens\//, '')
            filename = processString(filename)
            rec.set("filename", filename)

            rec.update()
            if (++n % 100 == 0) {
                println n + " of " + size
                db.commit()
            }
        }
        cmd.close()
        db.commit()
        println "Total records = " + n
    }

    private static String processString(String input) {
        // Split the string into segments
        String[] segments = input.split("/");

        // Check if there are at least two segments to compare
        if (segments.length < 2)
            return input;

        String lastSegment = segments[segments.length - 1];
        String secondLastSegment = segments[segments.length - 2];

        // Check if the last two segments are the same except for the case of the first character
        if (lastSegment.equalsIgnoreCase(secondLastSegment) &&
            Character.toUpperCase(lastSegment.charAt(0)) == Character.toUpperCase(secondLastSegment.charAt(0)) &&
            lastSegment.substring(1).equals(secondLastSegment.substring(1))) {
            // Remove the last segment
            String[] newSegments = Arrays.copyOf(segments, segments.length - 1);
            return String.join("/", newSegments);
        }

        return input;
    }
}
