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




package com.arahant.services.standard.misc.utils;

/**
 *
 * @author Blake McBride
 */
class FileSumThread extends Thread {
	private String fileName;
	private String path;

	public FileSumThread(String fileName, String path) {
		this.fileName = fileName;
		this.path = path;
	}

	@Override
	public void run() {
		FileSums.md5sum(fileName, path);
	}
}
