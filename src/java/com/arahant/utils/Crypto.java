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
 *
 * Created on Aug 10, 2006
 */
package com.arahant.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Iterator;


public class Crypto {

	// Default password.  Used to encrypt user passwords.
    private static final String keyInHex = "f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8f8";

    public static String bytesToHex(final byte[] bytes) {
		final StringBuilder s = new StringBuilder();
		for (final byte element : bytes)
			s.append(Integer.toHexString(0x0100 + (element & 0x00FF)).substring(1));
		return s.toString();
    }

    public static byte[] hexToBytes(final String s) {
		final byte[] bytes = new byte[s.length() / 2];
		for (int idx = 0; idx < bytes.length; idx++)
			bytes[idx] = (byte) Integer.parseInt(s.substring(2 * idx, 2 * idx + 2), 16);
		return bytes;
    }
	
	public static long hexToLong(final String s) {
		BigInteger big = new BigInteger(s, 16);
		return big.longValue();
	}

    public static byte[] getMD5(final File f) throws Exception {
		final FileInputStream fis = new FileInputStream(f);
		final byte[] bytes = new byte[(int) f.length()];
		fis.read(bytes);
		final MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hashBytes = md.digest(bytes);
		return hashBytes;
    }

    public static String encryptTripleDES(final String decrypted) throws Exception {
		return encryptTripleDES(keyInHex, decrypted);
	}
	
    public static String encryptTripleDES(String hexKey, final String decrypted) throws Exception {
		return Crypto.bytesToHex(Crypto.encryptTripleDES(hexKey, decrypted.getBytes()));
    }

    public static byte[] encryptTripleDES(final byte[] decrypted) throws Exception {
		return encryptTripleDES(keyInHex, decrypted);
	}
	
    public static byte[] encryptTripleDES(String hexKey, final byte[] decrypted) throws Exception {
		final SecretKey key = Crypto.getPrivateKeyTripleDES(hexKey);
		Cipher c;

		// Create and initialize the encryption engine
		// Attempt to load the TripleDES provider (ecb version, not cbc)
		// The JCE provider needs to have been permanently installed on this
		// system by listing it in the $JAVA_HOME/jre/lib/security/java.security file.
		c = Cipher.getInstance("DESede");
		c.init(Cipher.ENCRYPT_MODE, key);

		// actually perform the encryption
		byte[] encrypted = c.doFinal(decrypted);

		return encrypted;
    }

    public static String decryptTripleDES(final String encrypted) throws Exception {
		return decryptTripleDES(keyInHex, encrypted);
	}
	
    public static String decryptTripleDES(String hexKey, final String encrypted) throws Exception {
		return new String(Crypto.decryptTripleDES(hexKey, hexToBytes(encrypted)));
    }

    public static byte[] decryptTripleDES(final byte[] encrypted) throws Exception {
		return decryptTripleDES(keyInHex, encrypted);
	}
	
    public static byte[] decryptTripleDES(String hexKey, final byte[] encrypted) throws Exception {
		final SecretKey key = Crypto.getPrivateKeyTripleDES(hexKey);
		Cipher c;

		// Create and initialize the encryption engine
		// Attempt to load the TripleDES provider (ecb version, not cbc)
		// The JCE provider needs to have been permanently installed on this
		// system by listing it in the $JAVA_HOME/jre/lib/security/java.security file.
		// Starting in 1.4(?) the SunJCE provider came as part of the JRE.
		c = Cipher.getInstance("DESede");
		c.init(Cipher.DECRYPT_MODE, key);

		// actually perform the decryption
		byte[] decrypted = c.doFinal(encrypted);

		return decrypted;
    }
	
	/**
	 * Verifies a PGP public key block can be opened and contains the specified
	 * public key.
	 * 
	 * @param publicKeyText the ASCII PGP public key text
	 * @param publicKeyId the public key id
	 * @return 0 is no error, 1 is invalid public key text, 2 is invalid key id,
	 *         3 key id specified was found but key is not an encryption key
	 */
	public static short verifyPGPPublicKeyText(final String publicKeyText, final long publicKeyId) {
		byte[] publicKeyBytes = publicKeyText.getBytes();
		ByteArrayInputStream publicKeyStream = new ByteArrayInputStream(publicKeyBytes);
		short error = 1;
		
		try {
			InputStream is = PGPUtil.getDecoderStream(publicKeyStream);
			PGPPublicKeyRingCollection pgpPubKeyRingCollection = new PGPPublicKeyRingCollection(is);
			error = 2;
			PGPPublicKey publicKey = pgpPubKeyRingCollection.getPublicKey(publicKeyId);
			if (publicKey != null) {
				if (!publicKey.isEncryptionKey())
					error = 3;
				else
					error = 0;
			}
		} catch (Exception ignored) {
		}
		return error;
	}

	/**
	 * 
	 * To get the long key ID with GPG do:
	 * 
	 * Blake-Mac-17:web blake$ gpg --list-keys --with-colons ihcgroup
     * tru::1:1331825759:0:3:1:5
     * pub:f:2048:1:269B5644BE36E2D8:1276280433:::-:::escaESCA:
     * uid:f::::1276280433::42291F61C93D0924E3E54522AA9B6A76D2BCDD06::IHC Administrative Services <EDI_Alert@ihcgroup.com>:
     * sub:f:2048:1:614403B059090FAB:1276280435::::::esca:
	 * 
	 * The long key for the above is:  269B5644BE36E2D8
	 * 
	 * @param keyText a String containing the entire PGP PUBLIC KEY BLOCK (including the leading and trailing ---BEGIN....BLOCK---- text)
	 * @param keyIdInHex a 16 character String containing the LONG key ID in hex format.
	 * @return 0 is no error, 1 is invalid public key text, 2 is invalid key id,
	 *         3 key id specified was found but key is not an encryption key
	 */
	public static short verifyPGPPublicKeyText(String keyText, String keyIdInHex) {
		return Crypto.verifyPGPPublicKeyText(keyText, hexToLong(keyIdInHex));
	}

	/**
	 * Encrypts a file using PGP and the specified public key.
	 * 
	 * If you are using JDK 1.4, or later, you must download the unrestricted 
	 * policy files for the Sun JCE if you want the provider to work properly. 
	 * The policy files can be found at the same place as the JDK download. 
	 * Further information on this can be found in the Sun documentation on the
	 * JCE. If you have not installed the policy files you will see something
	 * like:
	 * 
	 * java.lang.SecurityException: Unsupported keysize or algorithm parameters
	 * 
	 * @param decryptedFile the unencrypted file to encrypt
	 * @param encryptedFile the encrypted file
	 * @param publicKeyStream the public key (or keyring) stream
	 * @param publicKeyId the public key id from the public key file
	 * @throws Exception
	 */
    public static void encryptPGP(final File decryptedFile, final File encryptedFile, final InputStream publicKeyStream, final long publicKeyId) throws Exception {
		PGPPublicKey publicKey = getPublicKeyPGP(publicKeyStream, publicKeyId);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PGPCompressedDataGenerator compressedData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

		PGPUtil.writeFileToLiteralData(compressedData.open(bos), PGPLiteralData.BINARY, decryptedFile);

		compressedData.close();

		Security.addProvider(new BouncyCastleProvider());
		PGPEncryptedDataGenerator encryptedData = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, false, new SecureRandom(), "BC");

		encryptedData.addMethod(publicKey);

		byte[] bytes = bos.toByteArray();
		FileOutputStream fos = new FileOutputStream(encryptedFile);

		OutputStream os = encryptedData.open(fos, bytes.length);
		os.write(bytes);
		os.close();
		os.close();
    }

	public static void encryptPGP(File decryptedFile, File encryptedFile, String publicEncryptionKey, String encryptionKeyId) throws Exception {
		Crypto.encryptPGP(decryptedFile, encryptedFile, new ByteArrayInputStream(publicEncryptionKey.getBytes()), hexToLong(encryptionKeyId));
	}
	
	public static void decryptPGP(final File encryptedFile, final File decryptedFile, final File privateKeyFile, final String privateKeyPassphrase) throws Exception {
		FileInputStream fis = new FileInputStream(encryptedFile);
		InputStream is = PGPUtil.getDecoderStream(fis);
		PGPObjectFactory pgpF = new PGPObjectFactory(is);
        PGPEncryptedDataList encryptedDataList;
		Object o = pgpF.nextObject();
		
		// the first object might be a PGP marker packet, skip over it if so
		if (o instanceof PGPEncryptedDataList) {
			encryptedDataList = (PGPEncryptedDataList)o;
		} else {
			encryptedDataList = (PGPEncryptedDataList)pgpF.nextObject();
		}
	
		// find the private key - should be under the key id that corresponds to
		// the key id of the public key used to encrypt the data originally
		Iterator<PGPEncryptedData> itr = encryptedDataList.getEncryptedDataObjects();
		PGPPrivateKey privateKey = null;
		PGPPublicKeyEncryptedData publicKeyEncryptedData = null;
		PGPSecretKeyRingCollection pgpPriKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(new FileInputStream(privateKeyFile)));

		while (privateKey == null && itr.hasNext()) {
			publicKeyEncryptedData = (PGPPublicKeyEncryptedData)itr.next();
			privateKey = Crypto.getPrivateKeyPGP(pgpPriKeyRingCollection, publicKeyEncryptedData.getKeyID(), privateKeyPassphrase);
		}
            
		if (privateKey == null) {
			throw new IllegalArgumentException("private key for message not found.");
		}

		// PGP encrypts the data with a session key, and only this session key is encrypted
		// with the receiver's public key, so overall time to encrypt is faster
    
		// do actual decryption of data
		InputStream clear = publicKeyEncryptedData.getDataStream(privateKey, "BC");
		PGPObjectFactory plainFact = new PGPObjectFactory(clear);
		Object message = plainFact.nextObject();

		if (message instanceof PGPCompressedData) {
			PGPCompressedData compressedData = (PGPCompressedData)message;
			PGPObjectFactory pgpFact = new PGPObjectFactory(compressedData.getDataStream());
			message = pgpFact.nextObject();
		}

		if (message instanceof PGPLiteralData) {
			PGPLiteralData literalData = (PGPLiteralData)message;
			//String outFileName = literalData.getFileName();
			FileOutputStream    fOut = new FileOutputStream(decryptedFile);
			InputStream unc = literalData.getInputStream();
			int ch;

			while ((ch = unc.read()) >= 0)
				fOut.write(ch);
		} else if (message instanceof PGPOnePassSignatureList) {
			throw new Exception("encrypted message contains a signed message - not literal data.");
		} else {
			throw new Exception("message is not a simple encrypted file - type unknown.");
		}

		if (publicKeyEncryptedData.isIntegrityProtected()) {
			if (!publicKeyEncryptedData.verify())
				System.err.println("message failed integrity check");
			else
				System.err.println("message integrity check passed");
		} else {
			System.err.println("no message integrity check");
		}
    }

    private static SecretKey getPrivateKeyTripleDES(String hexKey) throws Exception {
		final byte[] keyBytes = Crypto.hexToBytes(hexKey);
		final DESedeKeySpec keyspec = new DESedeKeySpec(keyBytes);
		final SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		final SecretKey key = keyfactory.generateSecret(keyspec);
		return key;
    }

    private static PGPPublicKey getPublicKeyPGP(final InputStream publicKeyStream, final long keyId) throws Exception {
		InputStream is = PGPUtil.getDecoderStream(publicKeyStream);
		PGPPublicKeyRingCollection pgpPubKeyRingCollection = new PGPPublicKeyRingCollection(is);
		PGPPublicKey publicKey = pgpPubKeyRingCollection.getPublicKey(keyId);
		return publicKey;
    }
	
	private static PGPPrivateKey getPrivateKeyPGP(final PGPSecretKeyRingCollection pgpPriKeyRingCollection, final long keyId, String passPhrase) throws Exception {
		PGPSecretKey privateKey = pgpPriKeyRingCollection.getSecretKey(keyId);
        if (privateKey == null)
            return null;
        else
			return privateKey.extractPrivateKey(passPhrase.toCharArray(), "BC");
	}
	
	private static void dumpKeyFile(File publicKeyFile) throws Exception {
		FileInputStream fis = new FileInputStream(publicKeyFile);
		InputStream is = PGPUtil.getDecoderStream(fis);
		PGPPublicKeyRingCollection pgpPubKeyRingCollection = new PGPPublicKeyRingCollection(is);
		Iterator<PGPPublicKeyRing> rIt = pgpPubKeyRingCollection.getKeyRings();

		while (rIt.hasNext()) {
			PGPPublicKeyRing pgpPub = (PGPPublicKeyRing)rIt.next();

			try {
				pgpPub.getPublicKey();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Iterator it = pgpPub.getPublicKeys();
			boolean first = true;
			while (it.hasNext()) {
				PGPPublicKey pgpKey = (PGPPublicKey)it.next();

				if (first) {
					System.out.println("Key ID: " + pgpKey.getKeyID());
					first = false;
				} else {
					System.out.println("Key ID: " + pgpKey.getKeyID() + " (subkey)");
				}
			}
		}
	}

	public static void main(final String[] args) throws Exception {
		final byte[] md5Hash = Crypto.getMD5(new File("/Users/arahant/Desktop/DeleteMe/ara3up.sql.txt"));
		System.out.println("MD5 Hash is " + Crypto.bytesToHex(md5Hash));

		final byte[] encryptedMd5Hash = Crypto.encryptTripleDES(md5Hash);
		final String encryptedMd5HashAsHex = Crypto.bytesToHex(encryptedMd5Hash);
		System.out.println("Triple DES Encrypted MD5 Hash is " + encryptedMd5HashAsHex);

		final byte[] encryptedMd5Hash_2 = Crypto.hexToBytes(encryptedMd5HashAsHex);
		final byte[] md5Hash_2 = Crypto.decryptTripleDES(encryptedMd5Hash_2);

		if (Arrays.equals(md5Hash, md5Hash_2))
			System.out.println("The original and decrypted are the same.");
		else
			System.out.println("ERROR - The original and decrypted are NOT the same.");

		final String encryptedPassword = Crypto.encryptTripleDES("password");
		System.out.println(Crypto.decryptTripleDES(encryptedPassword));
		
		// install pgp and use pgp --gen-key to test this
		File originalFile = new File("/Users/arahant/Desktop/DeleteMe/ara3up.sql.txt");
		File encryptedFile = new File("/Users/arahant/Desktop/DeleteMe/pgp-encrypt.out");
		File decryptedFile = new File("/Users/arahant/Desktop/DeleteMe/pgp-decrypt.out");
		File publicKeyFile = new File("/Users/arahant/.gnupg/pubring.gpg");
		Crypto.dumpKeyFile(publicKeyFile);
		File privateKeyFile = new File("/Users/arahant/.gnupg/secring.gpg");
		long keyId = -3588682832617370328L;
		Crypto.encryptPGP(originalFile, encryptedFile, new FileInputStream(publicKeyFile), keyId);
		Crypto.decryptPGP(encryptedFile, decryptedFile, privateKeyFile, "password");
		
		// test public key text in memory
		File memoryKeyFile = new File("/Users/arahant/Desktop/DeleteMe/delta.asc");
		Crypto.dumpKeyFile(memoryKeyFile);
		long memoryKeyId = Crypto.hexToLong("E335B7934AB31A94");//-2074550210092918124L
		FileInputStream fis = new FileInputStream(memoryKeyFile);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int bytesRead = fis.read(bytes);
		do {
			bos.write(bytes, 0, bytesRead);
			bytesRead = fis.read(bytes);
		} while (bytesRead != -1);
		fis.close();
		String memoryPublicKeyText = bos.toString();
		short verification = Crypto.verifyPGPPublicKeyText(memoryPublicKeyText, memoryKeyId);
		System.out.println(verification);
		
		
		// test david's file
		File davidOriginalFile = new File("/Users/arahant/Desktop/DeleteMe/EDI076334622_17.edi.txt");
		File davidEncryptedFile = new File("/Users/arahant/Desktop/DeleteMe/edi-pgp-encrypt.out");
		File davidPublicKeyFile = new File("/Users/arahant/.gnupg/pubring.gpg");
		Crypto.dumpKeyFile(davidPublicKeyFile);
		long davidKeyId = -3588682832617370328L;
		Crypto.encryptPGP(davidOriginalFile, davidEncryptedFile, new FileInputStream(davidPublicKeyFile), davidKeyId);
    }
}

	
