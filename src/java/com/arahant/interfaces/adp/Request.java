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
 * Author: Blake McBride
 * Date: 5/2020
 */

package com.arahant.interfaces.adp;

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import org.apache.log4j.Level;
import org.json.JSONObject;
import org.kissweb.RestClient;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


class Request {

    private static final ArahantLogger logger = new ArahantLogger(Request.class);
    private static final boolean debugging = false;

    /**
     * Make request with paging.  Page size is 50.  If page is zero then
     * don't use paging.  Pages are numbered from 1.
     *
     * @param serviceURL
     * @param type  POST or GET
     * @param page
     * @return
     * @throws Exception
     */
    static JSONObject makeRequest(String serviceURL, String type, int page) throws Exception {
        String urlString;
        RestClient rc = new RestClient();

        if (debugging) {
            logger.setLevel(Level.ALL);
            rc.setDebugFileName("ADPIntergfaceDebugLog.txt");
        }

        if (serviceURL.contains("?"))
            urlString = serviceURL + "&preventCache=" + System.currentTimeMillis();
        else
            urlString = serviceURL + "?preventCache=" + System.currentTimeMillis();
        if (page != 0) {
            urlString += "&$top=50";
            if (page > 1)
                urlString += "&$skip=" + ((page-1) * 50);
        }

        String requestParams;
        if (Globals.access_token == null) {
            requestParams = "client_id=" + Globals.client_id + "&client_secret=" + URLEncoder.encode(Globals.client_secret, StandardCharsets.UTF_8.toString());
            if (Globals.grant_type != null && !Globals.grant_type.isEmpty())
                requestParams += "&grant_type=" + Globals.grant_type;
        } else {
            requestParams = null;
        }

        JSONObject headers = new JSONObject();
        headers.put("Connection", "close");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json;masked=false");
        if (Globals.access_token != null)
            headers.put("Authorization", "Bearer " + Globals.access_token);

        if (Globals.proxyServerURL != null  &&  !Globals.proxyServerURL.isEmpty())
            rc.setProxy(Globals.proxyServerURL, Globals.proxyServerPort);

        File workingDir = FileSystemUtils.getWorkingDirectory();
        File pKeyFile = new File(workingDir, "WEB-INF/keys/ADP/" + Globals.CLIENT_KEYSTORE_PATH);
        rc.setTLSKey(Globals.CLIENT_KEYSTORE_TYPE, pKeyFile.getAbsolutePath(), Globals.CLIENT_KEYSTORE_PASS);

        return rc.jsonCall(type, urlString, requestParams, headers);
    }

 }

