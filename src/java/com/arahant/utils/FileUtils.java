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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Blake McBride
 */
public class FileUtils {

	public static String getcwd() {
		return new File(".").getAbsolutePath();
	}

	public static boolean fileExists(String fileName) {
		return (new File(fileName)).exists();
	}

	public static int copy(String fromFileName, String toFileName) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);
		int ret = 0;

		if (!fromFile.exists())
			return 1;

		if (!fromFile.isFile())
			return 2;
		if (!fromFile.canRead())
			return 3;

		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			if (!toFile.canWrite())
				return 4;
		} else {
			String parent = toFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists())
				return 5;
			if (dir.isFile())
				return 6;
			if (!dir.canWrite())
				return 7;
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead);
		} catch (Exception e) {
			ret = 8;
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
				}
		}
		return ret;
	}

	public static void main(String [] argv) {

	}
}
