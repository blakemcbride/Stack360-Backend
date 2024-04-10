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
import org.apache.log4j.Level;
import org.json.JSONObject;

import static java.lang.Thread.sleep;


class GetAccessToken {

    private static final ArahantLogger logger = new ArahantLogger(GetAccessToken.class);

    static String getAccessToken() throws Exception {
        logger.setLevel(Level.ALL);
        Globals.access_token = null;
        for (int n=1 ; n <= 3 ; n++) {
            JSONObject jo = Request.makeRequest("https://accounts.adp.com/auth/oauth/v2/token", Globals.POST, 0);
            if (jo == null) {
                logger.error("null returned from token request");
                sleep(5000);
                continue;
            }
            Globals.access_token = jo.getString("access_token");
           // logger.info("access_token (" + n + ") = " + Globals.access_token);
            if (Globals.access_token == null) {
                logger.error("returned from token request = " + jo.toString());
                sleep(5000);
            } else
                break;
        }
        return Globals.access_token;
    }

    public static void main(String [] args) {
        try {
            for (int i=0 ; i < 30 ; i++) {
                String at = getAccessToken();
                System.out.println("Access token = " + at);
                sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

