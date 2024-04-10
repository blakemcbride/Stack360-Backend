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

package com.arahant.utils;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 */
public class EmailAttachment {
    private String attachmentName;
    private String diskFileName;
    private byte [] byteArray;
    private String mimeType;

    public EmailAttachment(String diskFileName, String attachementName) {
        this.diskFileName = diskFileName;
        this.attachmentName = attachementName;
        this.mimeType = org.kissweb.FileUtils.getMimeType(attachementName);
    }

    EmailAttachment(byte [] data, String attachementName, String mimeType) {
        this.byteArray = data;
        this.attachmentName = attachementName;
        this.mimeType = mimeType;
    }

    public EmailAttachment(byte [] data, String attachementName) {
        this.byteArray = data;
        this.attachmentName = attachementName;
        this.mimeType = org.kissweb.FileUtils.getMimeType(attachementName);
    }

    public String getFileExtension() {
        return org.kissweb.FileUtils.getExtension(attachmentName);
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String type) {
        this.mimeType = type;
    }
}
