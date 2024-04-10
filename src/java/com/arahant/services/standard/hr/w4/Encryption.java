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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.services.standard.hr.w4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * Arahant
 */
public class Encryption {

    private static final int KEYSIZE = 1024;
    private static final String PUBLIC_KEY = "public.key";
    private static final String PRIVATE_KEY = "private.key";
    private static final String RSA = "RSA";
    private static final String AES = "AES";

    public void generatePublicAndPrivateKeys(boolean overwite) throws Exception {
        //generate new key or use existing
        if (!overwite){
            //do we already have the keys generated?
            if (new File(PUBLIC_KEY).exists()) return;
        }
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance(RSA);
        SecureRandom random = new SecureRandom();
        pairgen.initialize(KEYSIZE, random);
        KeyPair keyPair = pairgen.generateKeyPair();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY));
        out.writeObject(keyPair.getPublic());
        out.close();
        out = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY));
        out.writeObject(keyPair.getPrivate());
        out.close();
    }

    public final byte[] encryptBytes(byte[] bytes) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        SecretKey key = keygen.generateKey();

        // wrap with RSA public key
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(PRIVATE_KEY));
        Key publicKey = (Key) keyIn.readObject();
        keyIn.close();

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.WRAP_MODE, publicKey);
        byte[] wrappedKey = cipher.wrap(key);

        ByteArrayOutputStream baStream = new ByteArrayOutputStream(4 + wrappedKey.length + bytes.length);
        DataOutputStream out = new DataOutputStream(baStream);
        out.writeInt(wrappedKey.length);
        out.write(wrappedKey);

        cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        bytes = cipher.doFinal(bytes);
        out.write(bytes);
        out.close();
        return baStream.toByteArray();
    }

    public final byte[] decryptBytes(byte[] bytes) throws Exception {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);

        // Unwrap the RSA private key
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(PUBLIC_KEY));
        Key privateKey = (Key) keyIn.readObject();
        keyIn.close();

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        Key key = cipher.unwrap(wrappedKey, AES, Cipher.SECRET_KEY);

        cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] outBytes = new byte[in.available()];
        in.readFully(outBytes);
        in.close();
        return cipher.doFinal(outBytes);
    }

    public static void main(String[] args) {
        try {
            Encryption ec = new Encryption();
            ec.generatePublicAndPrivateKeys(false);
            String toBeEncrypted = "hello word";
            byte[] edata = ec.encryptBytes(toBeEncrypted.getBytes());
            System.out.println("Encrypted " + edata);
            byte[] ddata = ec.decryptBytes(edata);
            System.out.println("Decrypted " + new String(ddata));
        } catch (Exception ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
