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
package com.arahant.utils.generators.authcode;

import com.arahant.utils.Crypto;
import java.io.*;

public class AuthCodeGenerator {

	private static boolean frontEndSeperate = false;
	private static String frontEndPath = "/Users/arahant/Documents/FlashBuilder4/Frontend_Flex";

	public static void generate(String filename, File fyle, BufferedWriter fw) throws Exception {
		final byte[] md5Hash = Crypto.getMD5(fyle);
		//logger.info("MD5 Hash is " + Crypto.bytesToHex(md5Hash));

		final byte[] encryptedMd5Hash = Crypto.encryptTripleDES(md5Hash);
		final String encryptedMd5HashAsHex = Crypto.bytesToHex(encryptedMd5Hash);
		//logger.info("Triple DES Encrypted MD5 Hash is " + encryptedMd5HashAsHex);

		fw.write("update screen set auth_code='" + encryptedMd5HashAsHex + "' where filename='" + filename + "';\n");
	}

	public static void recurseFiles(BufferedWriter fw, File fyle) throws Exception {
		if (fyle.isDirectory()) {
			File[] fyles = fyle.listFiles();
			for (File file : fyles)
				recurseFiles(fw, file);
		} else {
			String filename = fyle.getAbsolutePath();
			filename = filename.replace('\\', '/');
			if (fyle.getName().endsWith(".swf") && (filename.contains("/screen/") || filename.contains("/dynamicScreen/")))
				try {
					if (frontEndSeperate)
						filename = filename.substring(filename.indexOf("/bin-debug/") + 11);
					else
						filename = filename.substring(filename.indexOf("/web/") + 5);
					generate(filename, fyle, fw);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String args[]) {
		try {
			File startDir;

			if (frontEndSeperate)
				startDir = new File(frontEndPath + "/bin-debug");
			else
				startDir = new File("./web");

			BufferedWriter fw = new BufferedWriter(new FileWriter("./build/web/WEB-INF/classes/authcodes.sql"));

			recurseFiles(fw, startDir);

			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
