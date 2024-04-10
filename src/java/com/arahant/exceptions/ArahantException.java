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
package com.arahant.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ArahantExceptions cause two things:
 *
 *      1.  specify an error string for the front-end
 *      2.  print an error and stacktrace on the back-end log
 *
 * ArahantWarning does only one thing:
 *
 *      1.  specify an error string for the front-end
 *
 * ArahantWarning does not print an error or stacktrace in the back-end log
 */
public class ArahantException extends RuntimeException {

	private static final long serialVersionUID = -4001989355482535022L;

	public ArahantException(final String s) {
		super(s);
		final StringWriter sw = new StringWriter();
		printStackTrace(new PrintWriter(sw));
		try {
			//Mail.send("admin@arahant.com", "admin@arahant.com", "Exception", s+"\n"+sw.toString());
		} catch (final Throwable e) {
		}
	}

	public ArahantException(final String s, final boolean nomail) {
		super(s);
	}

	public ArahantException(final Exception e) {
		super(e);
	}

	/**
	 * @param string
	 * @param ex
	 */
	public ArahantException(final String string, final Exception ex) {
		super(string);
		try {
			setStackTrace(ex.getStackTrace());
			final StringWriter sw = new StringWriter();
			printStackTrace(new PrintWriter(sw));
			final StringWriter sw2 = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw2));
			String msg = string + "\n" + sw.toString();
			msg += "\n" + ex.getMessage() + "\n" + sw2;
			//	Mail.send("admin@arahant.com", "admin@arahant.com", "Exception", msg);
		} catch (final Throwable e) {
		}
	}
}
