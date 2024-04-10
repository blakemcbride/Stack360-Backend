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

class Globals {

    static final String CLIENT_KEYSTORE_TYPE = "PKCS12";
    static final String CLIENT_KEYSTORE_PATH = "keystore.pkcs12";
    static final String CLIENT_KEYSTORE_PASS = "password";

    /*REQUEST PARAMS*/
    /* Client ID and Secret for WayToGo.  Need to make a parameter.  */
    static final String client_id = "b823550f-76c1-4a68-bee0-99b850315066";
    static final String client_secret = "4dcde6e9-91d8-4059-a360-7e8acff4dd68";
    static final String grant_type = "client_credentials";
    // client_id + client_secret + grant_type = access_token
    static String access_token = null;  //  obtained from ADP
    /*REQUEST PARAMS*/
    static final String proxyServerURL = "";
    static final int proxyServerPort = 80;

    static final String GET = "GET";
    static final String POST = "POST";
}
