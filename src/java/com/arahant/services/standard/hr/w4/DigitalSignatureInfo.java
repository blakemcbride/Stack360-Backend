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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class DigitalSignatureInfo {

    private String ipAdddress = "";
    private String loginName = "";
    private String document = "";
    private String dateTime = "";
    private String signature = "";

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public DigitalSignatureInfo(){

    }
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getIpAdddress() {
        return ipAdddress;
    }

    public void setIpAdddress(String ipAdddress) {
        this.ipAdddress = ipAdddress;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String writeXML() {
        try {
            String xml = "<digitalsignature>";
            xml += "<document>" + this.getDocument() + "</document>";
            xml += "<datetime>" + this.getDateTime() + "</datetime>";
            xml += "<ipaddress>" + this.getIpAdddress() + "</ipaddress>";
            xml += "<login>" + this.getLoginName() + "</login>";
            xml += "</digitalsignature>";
            //create the directory to put the filled in PDF files
            String XML_OutputDirectory = "digitalSignatureXml";
            File reportDir = new File(FileSystemUtils.getWorkingDirectory(), XML_OutputDirectory);
            if (!reportDir.exists()) {
                reportDir.mkdir();
            }
            //create the output PDF
            File outputXML = File.createTempFile("dsxml", ".xml", reportDir);
            FileOutputStream fout = new FileOutputStream(outputXML);
            fout.write(xml.getBytes());
            fout.close();
            return outputXML.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(DigitalSignatureInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    public static void main(String[] args) {
        DigitalSignatureInfo ds = new DigitalSignatureInfo();
        ds.setDateTime("20100932");
        ds.setDocument("c:/home/pdf.pdf");
        ds.setIpAdddress("192.168.6.45");
        ds.setLoginName("kalvink");
        ds.writeXML();
    }
}
