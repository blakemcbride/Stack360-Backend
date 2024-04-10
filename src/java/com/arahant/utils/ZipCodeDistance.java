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

import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 11/15/20
 */
public class ZipCodeDistance {

    private static final int maxCacheSize = 500;
    private static final Hashtable<String, Integer> cache = new Hashtable<>();
    private static final List<String> cacheList = new ArrayList<>(maxCacheSize);

    public static int distance(String zip1, String zip2) {
        if (zip1 == null || zip2 == null || zip1.length() < 5 || zip2.length() < 5)
            return -1;
        if (zip1.length() > 5)
            zip1 = zip1.substring(0, 5);
        if (zip2.length() > 5)
            zip2 = zip2.substring(0, 5);
        if (zip1.equals(zip2))
            return 0;
        final String key = zip1.compareTo(zip2) < 0 ? zip1 + ":" + zip2 : zip2 + ":" + zip1;
        if (cache.contains(key))
            return cache.get(key);
        final Connection db = ArahantSession.getKissConnection();
        final Command cmd = db.newCommand();
        try {
            final List<Record> recs = cmd.fetchAll("select latitude, longitude from zipcode_location where zipcode=? or zipcode=?", zip1, zip2);
            if (recs.size() != 2)
                return -1;  // not found
            final Record rec1 = recs.get(0);
            final Record rec2 = recs.get(1);
            final double dist = distance(rec1.getDouble("latitude"), rec1.getDouble("longitude"),
                    rec2.getDouble("latitude"), rec2.getDouble("longitude"), "M");
            synchronized (cacheList) {
                if (cache.size() > maxCacheSize) {
                    String fkey = cacheList.remove(0);
                    cache.remove(fkey);
                }
                cacheList.add(key);
            }
            cache.put(key, (int) dist);
            return (int) dist;
        } catch (Exception ex) {
            throw new ArahantException(ex);
        }
    }

    public static int distance(String zipcode, double longitude, double latitude) {
        if (zipcode == null || zipcode.length() < 5)
            return -1;
        if (zipcode.length() > 5)
            zipcode = zipcode.substring(0, 5);
        if (longitude < .000001  &&  longitude > -.000001  ||  latitude < .000001  &&  latitude > -.000001)
            return -1;
        final Connection db = ArahantSession.getKissConnection();
        final Command cmd = db.newCommand();
        try {
            final List<Record> recs = cmd.fetchAll("select latitude, longitude from zipcode_location where zipcode=?", zipcode);
            if (recs.size() != 1)
                return -1;  // not found
            final Record rec = recs.get(0);
            final double dist = distance(rec.getDouble("latitude"), rec.getDouble("longitude"), latitude, longitude, "M");
            return (int) dist;
        } catch (Exception ex) {
            throw new ArahantException(ex);
        }
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::                                                                         :*/
    /*::  This routine calculates the distance between two points (given the     :*/
    /*::  latitude/longitude of those points). It is being used to calculate     :*/
    /*::  the distance between two locations using GeoDataSource (TM) products   :*/
    /*::                                                                         :*/
    /*::  Definitions:                                                           :*/
    /*::    Southern latitudes are negative, eastern longitudes are positive     :*/
    /*::                                                                         :*/
    /*::  Function parameters:                                                   :*/
    /*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
    /*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
    /*::    unit = the unit you desire for results                               :*/
    /*::           where: 'M' is statute miles (default)                         :*/
    /*::                  'K' is kilometers                                      :*/
    /*::                  'N' is nautical miles                                  :*/
    /*::  Worldwide cities and other features databases with latitude longitude  :*/
    /*::  are available at https://www.geodatasource.com                         :*/
    /*::                                                                         :*/
    /*::                                                                         :*/
    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double distance(final double lat1, final double lon1, final double lat2, final double lon2, final String unit) {
        if (lat1 == lat2 && lon1 == lon2)
            return 0;
        else {
            final double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K"))
                dist = dist * 1.609344;
            else if (unit.equals("N"))
                dist = dist * 0.8684;
            return dist;
        }
    }

}
