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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class ArahantLogger {
	private static boolean displayStackForDepricatedColumns = false;

	Logger logger;

	public Level setLevel(Level lvl) {
		Level old = logger.getLevel();
		logger.setLevel(lvl);
		return old;
	}

	public ArahantLogger(final Class cls) {
		try {
			logger = Logger.getLogger(cls);
		} catch (Exception e) {
			//keep quiet, probably in gen
		}
	}

	@SuppressWarnings("CallToThreadDumpStack")
	public void error(final Throwable e) {
		logger.error(e);
		e.printStackTrace();
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		logger.error(time() + sw.toString());
	}

	@SuppressWarnings("CallToThreadDumpStack")
	public void error(final String str, final Throwable e) {
		logger.error(time() + str, e);
		e.printStackTrace();
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		logger.error(sw.toString());
	}

	public void debug(final Object o) {
		logger.debug(time() + o);
	}

	public void debug(final Throwable e) {
		logger.debug(e);
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		logger.debug(time() + sw.toString());
	}

	public void info(final Object o) {
		logger.info(time() + o);
	}

	public void info(final String s) {
		logger.info(s);
	}

	private String time() {
		return "";
//		return DateUtils.getDateTimeFormatted(new Date()) + " ";
	}

	/**
	 * @param string
	 */
	public void error(final String string) {
		logger.error(string);
	}

	/**
	 * @param string
	 */
	public void warn(final String string) {
		logger.warn(string);
	}
	
	public void deprecated() {
		StackTraceElement [] st = Thread.currentThread().getStackTrace();
//		String msg = "Deprecated method called:  " + st[2].getClassName() + "." + st[2].getMethodName() + "() (line " + st[2].getLineNumber() + ")";
		String msg = "Deprecated method called:  " + st[2].getMethodName() + " (line " + st[2].getLineNumber() + ")";
		if (displayStackForDepricatedColumns)
			logger.error(msg, new Exception());
		else
			logger.error(msg);
	}
	
}
