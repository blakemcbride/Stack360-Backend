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
public class ArahantWarning extends ArahantException {

	private static final long serialVersionUID = -6401392132905555830L;

	/**
	 * @param string
	 */
	public ArahantWarning(final String string) {
		super(string, true);
	}

	public ArahantWarning(final Exception e) {
		super(e);
	}
}
