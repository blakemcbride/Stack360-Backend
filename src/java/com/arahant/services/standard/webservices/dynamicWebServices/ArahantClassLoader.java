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


package com.arahant.services.standard.webservices.dynamicWebServices;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Blake McBride (modified by Kalvin)
 * @version 2010-3-13
 */
public class ArahantClassLoader extends ClassLoader {

	private static final String algorithm = "AES";
	private static final byte[] encryptionBytes = "dsfdk2uhfkjni@17g$$@voffa76goubg".getBytes();
	private static final SecretKeySpec key = new SecretKeySpec(encryptionBytes, algorithm);
	private static final char encryptionVersion = '1';

	public ArahantClassLoader() {
		super(ArahantClassLoader.class.getClassLoader());
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage:  java ArahantClassLoader classFile");
			System.exit(1);
		}
		//encryptClass(args[0]);
	}

	public final Class<?> loadArahantClass(boolean encrypted, String path, String name) throws ClassNotFoundException, Exception {
		Class<?> cls;

		String className = name;
		try {
			if (!encrypted)
				className = path + className;

			byte[] classBytes;
			if (encrypted)
				classBytes = loadClassBytes(encrypted, className);
			else
				classBytes = loadClassBytesDevelopment(className);
			if (classBytes == null)
				throw new ClassNotFoundException(name);
			cls = defineClass(null, classBytes, 0, classBytes.length);
			if (cls != null)
				resolveClass(cls);
			else
				throw new ClassNotFoundException(name);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return cls;
	}

	private final byte[] loadClassBytes(boolean encrypted, String name) throws Exception {
		String cname;
		if (encrypted)
			cname = name.replace('.', '/') + ".arahant";
		else
			cname = name.replace('.', '/') + ".class";

		InputStream input = this.getResourceAsStream(cname);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int data = input.read();

		while (data != -1) {
			buffer.write(data);
			data = input.read();
		}
		input.close();
		byte[] classData = buffer.toByteArray();
		if (encrypted) {
			//System.out.println("DUR " + System.getProperty("user.dir"));
			com.arahant.encryptor.Encrypt ec = new com.arahant.encryptor.Encrypt();
			classData = ec.decryptBytes(classData, "public.key"); //cipher.doFinal(classBytes);
		}
		return classData;
	}

	private final byte[] loadClassBytesDevelopment(String fullpath) {
		try {
			fullpath = fullpath.replace('.', '/') + ".class";
			InputStream input = new FileInputStream(fullpath);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();

			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			//System.out.println("Class Bytes: " + classData.length);
			return classData;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
}

