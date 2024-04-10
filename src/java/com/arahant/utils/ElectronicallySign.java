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

import org.kissweb.FileUtils;
import org.kissweb.builder.BuildUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * Author: Blake McBride
 * Date: 3/27/23
 */
public class ElectronicallySign {

    /**
     * Electronically sign a file.  This assures that has not been modified.  It uses the following external programs:<br>
     * <ul>
     *     <li>zip</li>
     *     <li>gpg</li>
     * </ul>
     * It is assumed that GPG has been set up including a key.
     * <br><br>
     * The file is REPLACED with the signature file.  It's name will be the same as the original file (minus its extension) with
     * a <code>.zip.gpg</code> file extension.
     * <br><br>
     * This method returns the name of the signed file that contains the original file.  It will be in the same directory as the
     * original file. The original file will have been deleted.
     *
     * @param fullpath the absolute path of the file to be electronically signed
     * @param ip their IP address of the person signing the file
     * @param signature their signature text
     * @param gpgPassword the password to your gpg key file.  This was generated when you created your gpg key.
     * @return the final signed file name (without the path
     * @throws IOException
     */
    public static String electronicallySignDocument(String fullpath, String ip, String signature, String gpgPassword) throws IOException {
        final File fp = new File(fullpath);
        if (!fp.exists())
            throw new IOException("File not found: " + fullpath);
        if (!fp.isFile())
            throw new IOException("Not a file: " + fullpath);
        final String path = fp.getCanonicalFile().getParent();
        final String filename = fp.getName();
        String basename;
        int idx = filename.lastIndexOf('.');
        if (idx == -1)
            basename = filename;
        else
            basename = filename.substring(0, idx);
        final String signame = basename + ".sig";
        final String zipname = basename + ".zip";
        final String gpgname = zipname + ".gpg";

        BuildUtils.rm(path + "/" + signame);
        BuildUtils.rm(path + "/" + zipname);
        BuildUtils.rm(path + "/" + gpgname);

        createSigFile(path, filename, signame, ip, signature, new Date(fp.lastModified()));

        BuildUtils.run(false, true, false, false, path, "zip " + zipname + " " + filename + " " + signame);
        BuildUtils.run(false, true, false, false, path, "gpg -s --pinentry-mode loopback --passphrase " + gpgPassword + " " + zipname);

        BuildUtils.rm(path + "/" + filename);
        BuildUtils.rm(path + "/" + signame);
        BuildUtils.rm(path + "/" + zipname);
        return gpgname;
    }

    /**
     * Extract a signed document from a zipped GPG file and place a copy in the reports directory.
     * The File object of the extracted file is returned.
     *
     * @param zippedGpgFileName  The full path of the base file (e.g. /mnt/XXX/YYY/ZZZ/the-file-name.zip.gpg)
     * @param fileName  the name of the file to be extracted
     * @return
     */
    public static File extractSignedDocument(String zippedGpgFileName, String fileName) throws IOException {
        final File zippedGpgFile = new File(zippedGpgFileName);
        final String zippedGpgBaseName = zippedGpgFile.getName();
        final String basename = zippedGpgBaseName.substring(0, zippedGpgBaseName.lastIndexOf(".zip.gpg"));
        final File tempZipGpgFile = FileSystemUtils.createTempFile(basename, ".zip.gpg");
        final String tempDir = tempZipGpgFile.getParent();
        final File zipFile = new File(tempDir + "/" + basename + ".zip");
        FileUtils.copy(zippedGpgFile.getAbsolutePath(), tempZipGpgFile.getAbsolutePath());
        BuildUtils.run(false, true, false, false, tempDir, "gpg -q " + tempZipGpgFile.getName());
        tempZipGpgFile.delete();
        BuildUtils.run(false, true, false, false, tempDir, "unzip " + zipFile.getName());
        zipFile.delete();
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        File outputFile = FileSystemUtils.createReportFile(basename, ext);
        BuildUtils.move(tempDir + "/" + fileName, outputFile.getAbsolutePath());
        return outputFile;
    }

    private static void createSigFile(String path, String filename, String signame, String ip, String signature, Date dt) throws IOException {
        final String fullpath = path + "/" + signame;
        final StringBuilder sb = new StringBuilder();
        sb.append("File:  " + filename + "\n");
        sb.append("Signature:  " + signature + "\n");
        sb.append("Date:  " + dt + "\n");
        sb.append("IP Address:  " + ip + "\n");
        BuildUtils.rm(fullpath);
        Files.write(Paths.get(fullpath), sb.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void main(String[] args) throws IOException {
        /*
        if (args.length != 1) {
            System.out.println("Usage: java ElectronicallySign <file>");
            System.exit(1);
        }
        String filename = args[0];
         */
        String fullpath = "/home/blake/intellijProjects/ElectronicallySign/myfile.txt";
        electronicallySignDocument(fullpath, "192.192.44.55", "person's signature", "dogfish");
    }

}
