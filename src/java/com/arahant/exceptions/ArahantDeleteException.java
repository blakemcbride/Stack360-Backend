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

public class ArahantDeleteException extends ArahantWarning {

	private static final long serialVersionUID = -7083846113007761337L;

	public ArahantDeleteException(final String msg) {
		super(msg);
	}

	public ArahantDeleteException() {
		super("Can not delete.  Item in use.");
	}

	/**
	 * @param e
	 */
	public ArahantDeleteException(final Exception e) {
		this(e.getMessage());
		setStackTrace(e.getStackTrace());
	}
}
