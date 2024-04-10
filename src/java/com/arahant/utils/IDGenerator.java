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

package com.arahant.utils;

import com.arahant.beans.ArahantBean;
import com.arahant.exceptions.ArahantException;
import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.kissweb.database.Record;

public class IDGenerator implements IdentifierGenerator {

	private static final Logger logger = Logger.getLogger(IDGenerator.class);

	static {
		try {
			Connection con = ArahantSession.getConnection();
			Statement s = con.createStatement();
//  The following line causes old versions of Microsoft SQL to hang!
//			s.executeUpdate("update property set prop_name='DBID' where prop_name='dbid'"); //convert from lower case
			ResultSet rs = s.executeQuery("select prop_value from property where prop_name='DBID'");

			if (rs.next()) {
				IDGeneratorKiss.dbid = String.format("%5s", rs.getString(1)).replace(' ', '0');
				rs.close();
			} else {
				rs.close();
				IDGeneratorKiss.dbid = "00001";
				s.executeUpdate("insert into property (prop_name, prop_value, prop_desc) values ('DBID','00001','database id for keys')");
			}
			s.close();
		} catch (Exception e) {
			throw new ArahantException("Can't get database id from property table", e);
		}
	}

	//TODO: make this more automatic
	@Override
	public Serializable generate(final SessionImplementor arg0, final Object arg1) throws HibernateException {
		arg1.getClass().getName();
		arg0.bestGuessEntityName(arg1);

		logger.info(arg0.bestGuessEntityName(arg1));
		return null;
	}

    /**
	 * Hibernate API key generator.
	 *
	 * Generates a primary key used for the next new record.
	 * The new key gets assigned to the record and returned.
     *
     * @param bean
     * @return
     * @throws ArahantException
     */
	public static String generate(final ArahantBean bean) throws ArahantException {
		if (bean != null)
			bean.linkToEngine();
	    return generate(bean.tableName(), bean.keyColumn());
	}

	/**
	 * Generates a primary key used for the next new record.
	 * The new key gets assigned to the record and returned.
	 *
	 * @param rec
	 * @param colName
	 * @return
	 */
	public static String generate(Record rec, String colName) {
        String key;
		rec.set(colName, key=IDGenerator.generate(rec.getTableName(), colName));
        return key;
	}

	/**
	 * Generates and returns a primary key used for the next new record.
	 *
	 * This version only works if Arahant is the only single process changing the database.
	 * If other processes are to be changing the database, this method should be its own server.
	 *
	 * @param tableName
	 * @param id_column
	 * @return
	 * @throws ArahantException
	 */
	public static String generate(final String tableName, final String id_column) throws ArahantException {
		synchronized (IDGeneratorKiss.idCache) {
			if (tableName.endsWith("_h")  &&  id_column.equals("history_id")) {
				UUID uuid = UUID.randomUUID();
				return (Long.toHexString(uuid.getLeastSignificantBits()) + Long.toHexString(uuid.getMostSignificantBits()));
			}
			int top;
			Integer otop = IDGeneratorKiss.idCache.get(tableName);
			if (otop != null)
				top = otop;
			else {
				if (id_column == null)
					throw new ArahantException("generate: missing column name");

				Connection con = ArahantSession.getConnection();
				Statement st;
				try {
					st = con.createStatement();
				} catch (SQLException e) {
					throw new ArahantException(e);
				}

				try {
					/* The following line works for PostreSQL, MySQL, and SQLite but does not work for Microsoft Server or Oracle
					   because of the limit 1.  See org.kissweb.database.Connection.limit()
					*/
					ResultSet rs = st.executeQuery("select " + id_column + " from " + tableName + " where " + id_column + " like '" + IDGeneratorKiss.dbid + "-%' order by " + id_column + " desc limit 1");
					if (rs.next()) {
						String val = rs.getString(1);
						top = Integer.parseInt(val.substring(val.indexOf('-') + 1));
					} else
						top = 0;
					rs.close();
					st.close();
				} catch (final Exception e) {
					throw new ArahantException(e);
				}
			}
			IDGeneratorKiss.idCache.put(tableName, ++top);
			/*
			logger.setLevel(Level.INFO);
			logger.info("Generated key for " + tableName + "." + id_column + " = " + dbid + "-" + String.format("%010d", top));
			 */
			return IDGeneratorKiss.dbid + "-" + String.format("%010d", top);
		}
	}
	/**
	 * Remove leading zeros in record key.  Turns 00001-0000000082 into 1-82
	 *
	 * @param id i.e. "00001-0000000082"
	 * @return i.e.  "1-82"
	 */
	public static String shrinkKey(String id) {
		return IDGeneratorKiss.shrinkKey(id);
	}

	/**
	 * Converts a db record key from short format to long format.
	 *
	 * @param extId i.e. "1-82"
	 * @return i.e. "00001-0000000082"
	 */
	public static String expandKey(String extId) {
		return IDGeneratorKiss.expandKey(extId);
	}
}

	
