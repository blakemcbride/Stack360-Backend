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



package com.arahant.utils.dynamicclassloader;

/**
 *
 * @author Blake McBride
 */


import com.arahant.utils.FileSystemUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Blake McBride
 * @version 2010-3-13
 */
public class ArahantClassLoader extends ClassLoader {

    private static final String algorithm = "AES";
    private static SecretKeySpec key;
    private static final char encryptionVersion = '1';

	public ArahantClassLoader() {
		super(ArahantClassLoader.class.getClassLoader());
	}

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage:  java ArahantClassLoader classFile");
            System.exit(1);
        }
        encryptClass(args[0]);
    }

	/**
	 * Load a class from a file.
	 *
	 * @param encrypted
	 * @param fileName the full file name including the absolute or relative path, the file name, and the extension
	 * @return the loaded class object
	 */
    public final Class<?> loadArahantClassFromFile(boolean encrypted, String fileName) {
        Class<?> cls = null;
        try {
            byte[] classBytes = loadClassBytes(encrypted, fileName);
            cls = defineClass(null, classBytes, 0, classBytes.length);
            if (cls != null)
                resolveClass(cls);
            else
                return null;
        } catch (Exception ex) {
            return null;
        }
        return cls;
    }

	/**
	 * Load a class from a file.
	 *
	 * @param encrypted
	 * @param baseDir the base path to the packages
	 * @param name the full class name including the package e.g. "java.lang.String".
	 * @return the loadad class object
	 * @throws ClassNotFoundException
	 */
    public final Class<?> loadArahantClass(boolean encrypted, String baseDir, String className) throws ClassNotFoundException {
        Class<?> cls = null;
		if (baseDir == null)
			baseDir = "";
		else {
			int len = baseDir.length();
			if (len > 2  &&  baseDir.charAt(len-1) != '/'  && baseDir.charAt(len-1) != '\\')
					baseDir += "/";
		}
        try {
            byte[] classBytes = loadClassBytes(encrypted, baseDir + createFileName(encrypted, className));
            cls = defineClass(className, classBytes, 0, classBytes.length);
            if (cls != null)
                resolveClass(cls);
            else
                throw new ClassNotFoundException(className);
        } catch (Exception ex) {
            throw new ClassNotFoundException(className);
        }
        return cls;
    }

	private String createFileName(boolean encrypted, String className) {
        if (encrypted)
            return className.replace('.', '/') + ".arahant";
        else
            return className.replace('.', '/') + ".class";
	}

	/**
	 * Read in the class bytes and decrypt if necessary.
	 * 
	 * @param encrypted true or false
	 * @param name the full (relative or absolute) path with the full file name
	 * @return the (decrypted) class bytes
	 * @throws Exception
	 */
    private final byte[] loadClassBytes(boolean encrypted, String name) throws Exception {
        File file = new File(name);
        int fileLength = (int) file.length();
        FileInputStream in = new FileInputStream(file);
        char ev;
        if (encrypted) {
            ev = (char) in.read();
            if (ev != encryptionVersion)
                throw new ClassNotFoundException(name);
            fileLength--;
        }
        byte[] classBytes = new byte[fileLength];
        int len = 0;
        while (len < fileLength)
            len += in.read(classBytes, len, fileLength - len);
        in.close();
        if (encrypted) {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            classBytes = cipher.doFinal(classBytes);
        }
        return classBytes;
    }

    public static final void encryptClass(String name) {
        if (name.endsWith(".class"))
            name = name.substring(0, name.length() - 6);
        String inFile = name + ".class";
        String outFile = name + ".arahant";
        try {
            File ifo = new File(inFile);
            FileInputStream in = new FileInputStream(ifo);
            int fileLength = (int) ifo.length();
            byte[] bytes = new byte[fileLength];
            int len = 0;
            while (len < fileLength)
                len += in.read(bytes, len, fileLength - len);
            in.close();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            bytes = cipher.doFinal(bytes);

            FileOutputStream out = new FileOutputStream(outFile);
            out.write(encryptionVersion);
            out.write(bytes);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /*
    public static final byte[] encryptBytes(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }

    public static final byte[] decryptBytes(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }
    */

    /* The methods below this line are not used and, in fact, not tested.
     * They are kept here for two reasons.  The first is a good starting point for
     * public key encryption in case we decide to use it.  The second is to help
     * make it difficult to understand disassembled code.
     * */


    private static final int KEYSIZE = 512;

    public static void genkey() throws Exception {
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        pairgen.initialize(KEYSIZE, random);
        KeyPair keyPair = pairgen.generateKeyPair();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("public.key"));
        out.writeObject(keyPair.getPublic());
        out.close();
        out = new ObjectOutputStream(new FileOutputStream("private.key"));
        out.writeObject(keyPair.getPrivate());
        out.close();
    }

    public final byte[] encryptBytes2(byte[] bytes) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        SecretKey key = keygen.generateKey();

        // wrap with RSA public key
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("public.key"));
        Key publicKey = (Key) keyIn.readObject();
        keyIn.close();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, publicKey);
        byte[] wrappedKey = cipher.wrap(key);

        ByteArrayOutputStream baStream = new ByteArrayOutputStream(4 + wrappedKey.length + bytes.length);
        DataOutputStream out = new DataOutputStream(baStream);
        out.writeInt(wrappedKey.length);
        out.write(wrappedKey);

        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        bytes = cipher.doFinal(bytes);
        out.write(bytes);
        out.close();
        return baStream.toByteArray();
    }

    public final byte[] decryptBytes2(byte[] bytes) throws Exception {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);

        // Unwrap the RSA private key
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("private.key"));
        Key privateKey = (Key) keyIn.readObject();
        keyIn.close();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

        OutputStream out = new ByteArrayOutputStream(bytes.length);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] outBytes = new byte[in.available()];
        in.readFully(outBytes);
        in.close();
        return cipher.doFinal(outBytes);
    }

	public static void genkey2(String arg) {
		String pubkeyfile = FileSystemUtils.createTempDirFile("public.key").getAbsolutePath();
		String privkeyfile = FileSystemUtils.createTempDirFile("private.key").getAbsolutePath();
		try {
			KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			pairgen.initialize(KEYSIZE, random);
			KeyPair keyPair = pairgen.generateKeyPair();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pubkeyfile));
			ArahantClassLoader.key = new SecretKeySpec(arg.getBytes(), algorithm);
			out.writeObject(keyPair.getPublic());
			out.close();
			out = new ObjectOutputStream(new FileOutputStream(privkeyfile));
			out.writeObject(keyPair.getPrivate());
			out.close();
			new File(pubkeyfile).delete();
			new File(privkeyfile).delete();
		} catch (Exception e) {
			//  nothing
		} finally {
			new File(pubkeyfile).delete();
			new File(privkeyfile).delete();
		}
	}

}
