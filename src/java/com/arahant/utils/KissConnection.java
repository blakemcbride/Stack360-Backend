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

import org.kissweb.database.Connection;

/**
 * Author: Blake McBride
 * Date: 5/17/23
 *
 * The Arahant system was initially built with Hibernate.  Later Kiss was added.
 * Since Hibernate was first, its use is primary.  Kiss connections come from Hibernate connections.
 *
 * Commandline utilities most often use Kiss and no Hibernate.  (One reason being the difficulty of initializing the
 * system. It takes a lot of code and processing to initialize the system.)  So, we need a way of removing Kiss'
 * dependency on Hibernate when forming the database connection.  This class is used to do that - abstract away Hibernate.
 *
 * Database connections are global - but most often only global within a particular thread!
 */
public class KissConnection {

    private static final ThreadLocal<Connection> dbTS = new ThreadLocal<>();
    private static Connection db;  // in case it is global for all threads

    /**
     * This method is used to assure that the Kiss connection is globally available regardless of whether Hibernate is used or not.
     */
    public static void set(Connection c) {
        dbTS.set(c);
        db = c;
        c.setDeleteCallback(ExternalFile::deleteCallback);
    }

    /**
     * This method is used to assure that the Kiss connection is globally available regardless of whether Hibernate is used or not.
     */
    public static void set(java.sql.Connection sqlc) {
        final Connection c = new Connection(sqlc);
        dbTS.set(c);
        db = c;
        c.setDeleteCallback(ExternalFile::deleteCallback);
    }

    public static void delete() {
        dbTS.remove();
        db = null;
    }

    /**
     * Return the Kiss connection that is global to this particular thread.  If the thread has no Kiss connection,
     * then the global connection is returned.  This handles multi-threaded command line utilities.
     *
     * Generally, each web service has its own thread and database connection.  This method returns the Kiss connection
     * associated with the current particular thread.
     */
    public static Connection get() {
        final Connection c = dbTS.get();
        if (c != null) {
            c.setDeleteCallback(ExternalFile::deleteCallback);
            return c;
        }
        return db;
    }

}
