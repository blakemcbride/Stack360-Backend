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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Author: Blake McBride
 * Date: 3/30/18
 */
public class SourceCodeRevision {

    private static final ArahantLogger logger = new ArahantLogger(SourceCodeRevision.class);

    public static String getSourceCodeRevisionNumber() {
        InputStream is = null;
        try {
            is = SourceCodeRevision.class.getClassLoader().getResourceAsStream("VersionData.txt");
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("SourceCodeRevisionNumber");
        } catch (IOException ex) {
            logger.error("Error loading VersionData.txt", ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        return null;
    }

    public static int getBuildYear() {
        int year = 0;
        BufferedReader br = null;
        try {
            InputStream is = SourceCodeRevision.class.getClassLoader().getResourceAsStream("VersionData.txt");
            br = new BufferedReader(new InputStreamReader(is));

            String data = "";

            while (data != null) {
                if (data.startsWith("BuildDate")) {
                    data = data.trim();
                    String syear = data.substring(data.length() - 4, data.length());
                    year = Integer.parseInt(syear);
                }
                data = br.readLine();
            }
        } catch (final Exception e) {
            logger.error(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException ex) {
                }
        }
        return year;
    }
}
