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

/*
*/


package com.arahant.utils.dbreset;

import com.arahant.sql.*;

/**
 * Fleshes out the schema on a new SQL database.
 *
 * SQL database must exist first.
 *
 * Set dbName to name the database.
 *
 * Set loadTestData to control if test data is loaded too.
 *
 * Defaults to PostgreQL database type.
 */
public class Reset extends ResetBase {

	public static void main(String args[]) {
		boolean loadTestData = false;
		String dbName = "DBNAME";

		Database db = Database.OpenDatabase("org.postgresql.Driver", "jdbc:postgresql://localhost/" + dbName, "postgres", "postgres");
		if (db == null) {
			System.err.println("Error opening database");
			return;
		}
		db.execute("drop schema public cascade");
		db.execute("create schema public");
		db.executeSQLScript("schema/ArahantSchema.sql", 1); // execute create statements only
		db.executeSQLScript("src/java/com/arahant/utils/dbreset/base_setup.sql");
		if (loadTestData)
			db.executeSQLScript("src/java/com/arahant/utils/dbreset/testLoad.sql");
		db.execute("delete from property where prop_name='Database Version'");  // cause the system to calculate it upon startup
		db.executeSQLScript("schema/ArahantSchema.sql", 3);  // execute constraints only
	}
}
