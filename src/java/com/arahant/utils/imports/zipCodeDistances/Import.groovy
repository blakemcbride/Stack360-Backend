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

package com.arahant.utils.imports.zipCodeDistances

import org.kissweb.DelimitedFileReader;
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 11/15/20
 *
 * See the "run" shell script in this directory.
 */
class Import {

    public static void main(String [] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage:  ./run  input-csv-file-name");
            return;
        }
        String csvfname = args[0];
        System.out.println("Importing for zip code locations from " + csvfname);
        int num = 0;
        Connection db = new Connection(Connection.ConnectionType.PostgreSQL, "localhost", "waytogo", "postgres", "postgres");
        DelimitedFileReader dfr = new DelimitedFileReader(csvfname);
        dfr.readHeader();
        while (dfr.nextLine()) {
            Record rec = db.newRecord("zipcode_location");
            rec.set("zipcode", dfr.getString("zip code"));
            rec.set("state_abr", dfr.getString("state abbreviation"));
            rec.set("city", dfr.getString("city"));
            rec.set("state", dfr.getString("state"));
            rec.set("latitude", dfr.getDouble("latitude"));
            rec.set("longitude", dfr.getDouble("longitude"));
            rec.addRecord();
            if ((++num % 100) == 0)
                System.out.println("Processed record " + num);
        }
        dfr.close();
        db.close();
        System.out.println("Done. Added " + num + " zip codes.");
    }
}
