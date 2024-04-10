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

import com.arahant.utils.FileSystemUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class SignAndEncrypt {

    private String ZIP = "DigitalSignatureZipFiles";

    public SignAndEncrypt(String pdfFile, DigitalSignatureInfo dsInfo){
        
        FileInputStream fis = null;
        try {
            String dsXMLFile = dsInfo.writeXML();//create an xml of the digital signature information
            File zipFile = createZipFile();//create the zip file where the xml and pdf will be zipped in
            CreateJarFile cj = new CreateJarFile();
            File xml = new File(dsXMLFile);
            File pdf = new File(pdfFile);
            File[] tobeJared = new File[2];
            tobeJared[0] = xml;
            tobeJared[1] = pdf;

            //zip the files and delete them
            cj.createJarArchive(zipFile, tobeJared);
            xml.delete();
            pdf.delete();

            //encrypt the zip and save it to database
            fis = new FileInputStream(zipFile);
            byte[] zipData = new byte[(int) zipFile.length()];
            fis.read(zipData);

            Encryption ec = new Encryption();
            ec.generatePublicAndPrivateKeys(false);
            byte[] zipDataEncrypted = ec.encryptBytes(zipData);
            
        } catch (Exception ex) {
            Logger.getLogger(SignAndEncrypt.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(SignAndEncrypt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private File createZipFile() {
        try {
            //create the directory to put the filled in Zip files
            File reportDir = new File(FileSystemUtils.getWorkingDirectory(), ZIP);
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
            //create the output zip
            File outputXML = File.createTempFile("digital", ".zip", reportDir);
            return outputXML;
        } catch (IOException ex) {
            Logger.getLogger(SignAndEncrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        String pdf = "/home/kag/NetBeansProjects/Arahant/build/web/pdf/w4.pdf8625389314770075300.pdf";
        DigitalSignatureInfo ds = new DigitalSignatureInfo();
        ds.setDateTime("20100932");
        ds.setDocument("w4.pdf8625389314770075300.pdf");
        ds.setIpAdddress("192.168.6.45");
        ds.setLoginName("kalvink");
        ds.setSignature("Kalvin Khetsavanh");
        SignAndEncrypt sae = new SignAndEncrypt(pdf,ds);
    }
}
