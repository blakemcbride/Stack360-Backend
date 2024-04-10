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

import org.json.JSONArray;
import org.json.JSONObject;
import org.kissweb.RestClient;
import org.kissweb.URLBuilder;

import java.io.IOException;

/**
 * Author: Blake McBride
 * Date: 12/20/21
 */
public class GoogleMaps {

    private static final ArahantLogger logger = new ArahantLogger(GoogleMaps.class);
    private static final String googleKey = "AIzaSyBdjnFuLvJpF85dh-Ir4w_rj7FoY874p0I";

    private boolean success = false;
    private int miles;
    private int minutes;
    private double hours;

    public GoogleMaps(String origin, String destination) {
        final double noResult = -1.0;
        RestClient client = new RestClient();
        try {
            URLBuilder url = new URLBuilder("https://maps.googleapis.com/maps/api/distancematrix/json");
            url.addParameter("origins", origin);
            url.addParameter("destinations", destination);
            url.addParameter("key", googleKey);
            JSONObject ret = client.jsonCall("GET", url.build());
            if (ret == null)
                return;
            String status = ret.getString("status");
            if (!"OK".equals(status))
                return;
            JSONArray rows = ret.getJSONArray("rows");
            if (rows == null)
                return;
            int nrows = rows.length();
            if (nrows < 1)
                return;
            JSONObject row = rows.getJSONObject(0);
            JSONArray elements = row.getJSONArray("elements");
            int nelements = elements.length();
            if (nelements < 1)
                return;
            JSONObject element = elements.getJSONObject(0);
            status = element.getString("status");
            if (!"OK".equals(status))
                return;
            JSONObject dobj = element.getJSONObject("duration");
            int duration = dobj.getInt("value");
            JSONObject distObj = element.getJSONObject("distance");
            int distance = distObj.getInt("value");
            miles = (int) (distance * .0006214);
            minutes = duration / 60;
            hours = minutes / 60.0;
            success = true;
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public int getMiles() {
        return miles;
    }

    public int getMinutes() {
        return minutes;
    }

    public double getHours() {
        return hours;
    }

    public static void main(String [] argv) {
        GoogleMaps gm = new GoogleMaps("4535 Scenic Hills Drive, Franklin, TN", "905 Cherry Street, North Wilkesboro, NC");
        if (gm.isSuccess()) {
            int miles = gm.getMiles();
            int minutes = gm.getMinutes();
            double hours = gm.getHours();
            int x = 1;
        }
    }

}
