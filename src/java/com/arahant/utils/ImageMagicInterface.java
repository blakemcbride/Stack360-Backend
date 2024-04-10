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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Author: Blake McBride
 * Date: 4/1/21
 *
 * While this is a good example of calling an external program, it doesn't do what was intended at all.
 * Use Image.resizeImage() instead.
 */
public class ImageMagicInterface {

    private static Boolean isMac;
    private static Boolean isWindows;

    /**
     * Resize image supplied in 'in' to maxSice (in K) if it is type "jpg" or "jpeg".
     * For other types or error, 'in' is returned.
     *
     * @param in the input image
     * @param type Only works with "jpg" or "jpeg"
     * @param maxSize in 1,000 bytes
     * @return the modified image
     */
    public static byte [] resizeJPG(byte [] in, String type, int maxSize) {
        byte[] out = in;
        File tmpFile1 = null;
        File tmpFile2 = null;
        type = type.toLowerCase();
        if (in == null ||  in.length <= maxSize || !type.equals("jpg") && !type.equals("jpeg"))
            return out;
        try {
            tmpFile1 = File.createTempFile("temp1-", ".jpg");
            tmpFile2 = File.createTempFile("temp2-", ".jpg");
            Files.write(tmpFile1.toPath(), in);
            int rc = ImageMagicInterface.resizeJPG(tmpFile1.getAbsolutePath(), tmpFile2.getAbsolutePath(), maxSize);
            if (rc == 0) {
                out = Files.readAllBytes(tmpFile2.toPath());
                if (out.length == 0)
                    out = in;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return in;
        } finally {
            if (tmpFile1 != null)
                tmpFile1.delete();
            if (tmpFile2 != null)
                tmpFile2.delete();
        }
        return out;
    }

    public static int resizeJPG(String fromFile, String toFile, int kbSize) {
        int rc = -1;

        if (isMac == null) {
            String os = System.getProperty("os.name").toLowerCase();
            isMac = os.startsWith("mac ");
            isWindows = os.startsWith("windows");
        }

        ProcessBuilder builder;

        //  Linux only for now
        builder = new ProcessBuilder("magick", fromFile, "-define", "jpeg:extent=" + kbSize + "KB", toFile);
        Map<String, String> env = builder.environment();
        env.put("LD_LIBRARY_PATH", "/usr/local/lib");
//        builder.redirectError(new File("error.out"));
        try {
            Process p = builder.start();
            p.waitFor();
            rc = p.exitValue();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rc;
    }

    // test program
    public static void main(String[] args) throws IOException {
        /*
        byte [] in = Files.readAllBytes((new File("abc.jpg")).toPath());
        byte [] out = ImageMagicInterface.resizeJPG(in, "jpg", 200);
        Files.write((new File("abc2.jpg")).toPath(), out);
         */
        ImageMagicInterface.resizeJPG("abc.jpg", "abc2.jpg", 200);
        ImageMagicInterface.resizeJPG("abc.jpg", "abc4.jpg", 400);
        ImageMagicInterface.resizeJPG("abc.jpg", "abc1M.jpg", 1000);
    }
}
