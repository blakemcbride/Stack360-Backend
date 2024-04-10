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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;


public class FileSums {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		md5sum("out.md5", "build.xml");
	}

	static boolean md5sum(String to_file_name, String path) {
		return calcSum("MD5", to_file_name, path);
	}

	private static boolean calcSum(String algo, String to_file_name, String path) {
		boolean res = true;
		BufferedWriter bw = null;
		// Valid options for algo are:  MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512

		try {
			bw = new BufferedWriter(new FileWriter(to_file_name));
			visitAllDirsAndFiles(algo, bw, new File(path));
		} catch (Exception e) {
			res = false;
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (Exception e) {
				}
			return res;
		}
	}

	private static void visitAllDirsAndFiles(String algo, BufferedWriter bw, File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
				visitAllDirsAndFiles(algo, bw, new File(dir, children[i]));
		} else
			genSum(algo, bw, dir);
	}

	private static void genSum(String algo, BufferedWriter bw, File f) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algo);
			InputStream is = new FileInputStream(f);
			byte[] buffer = new byte[8192];
			int read = 0;
			try {
				while ((read = is.read(buffer)) > 0)
					digest.update(buffer, 0, read);
				byte[] md5sum = digest.digest();
				bw.write(asHex(md5sum) + "  " + f.getPath());
				bw.newLine();
			} catch (IOException e) {
			} finally {
				is.close();
			}
		} catch (Exception e) {
		}
	}

	private static final char[] HEX_CHARS = {'0', '1', '2', '3',
		'4', '5', '6', '7',
		'8', '9', 'a', 'b',
		'c', 'd', 'e', 'f',};

	private static String asHex(byte hash[]) {
		char buf[] = new char[hash.length * 2];
		for (int i = 0, x = 0; i < hash.length; i++) {
			buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
			buf[x++] = HEX_CHARS[hash[i] & 0xf];
		}
		return new String(buf);
	}
}

