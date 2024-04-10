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

import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 5/16/23
 */
class Utils {

    static final String EXTERNAL_FILE_ROOT = "EXTERNAL_FILE_ROOT";
    private static Connection db;

    static void init(Connection dbv) {
        db = dbv
    }

	public static String makeExternalFilePath(String tableName, final String primaryKey, String fieldName, String extension) {
		tableName = tableName.toLowerCase();
		if (fieldName == null)
			fieldName = "";
		if (!fieldName.isEmpty() &&  fieldName.charAt(0) != '-' as char)
			fieldName = "-" + fieldName;
		if (extension == null)
			extension = "";
		if (!extension.isEmpty() &&  extension.charAt(0) != '.' as char)
			extension = "." + extension;
		final String dir = getExternalFileDir(tableName, primaryKey);
		return dir + "/" + primaryKey + fieldName + extension;
	}

    private static String getExternalFileDir(final String tableName, final String primaryKey) {
        String rootPath = getProperty(EXTERNAL_FILE_ROOT);

        if (rootPath == null  ||  rootPath.isEmpty())
            throw new Exception("System property " + EXTERNAL_FILE_ROOT + " not set.");

        if (rootPath.charAt(rootPath.length()-1) != (char) '/')
            rootPath += '/';
        final StringBuilder path = new StringBuilder(rootPath);
        path.append(tableName).append("/").append(primaryKey, 0, 5);
        final String tid = primaryKey.substring(6);
        for (int j=0 ; j < 4 ; j++)
            path.append("/").append(tid, j * 2, j * 2 + 2);
        String spath = path.toString();
        (new File(spath)).mkdirs();
        return spath;
    }

    static String getProperty(String prop) {
        Record rec = db.fetchOne("select prop_value from property where prop_name = ?", prop)
        if (rec == null)
            return null
        return rec.getString("prop_value")
    }

}
