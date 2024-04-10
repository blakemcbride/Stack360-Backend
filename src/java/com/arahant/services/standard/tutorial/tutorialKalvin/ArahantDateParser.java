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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class ArahantDateParser {

    public static final String DATE_FORMAT_MM_DD_YY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/(\\d\\d)";
    public static final String DATE_FORMAT_MM_DD_YYYY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
    public static final String DATE_FORMAT_DOT_MM_DD_YY = "(0?[1-9]|1[012]).(0?[1-9]|[12][0-9]|3[01]).(\\d\\d)";
    public static final String DATE_FORMAT_DASH_MM_DD_YY = "(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(\\d\\d)";

    private static String normalizeDate(String date) {

        int length = date.length();

        String dateYear = date.substring(length - 2);
        if (Integer.parseInt(dateYear) > 20) {
            date = date.substring(0, length - 2) + "19" + dateYear;
        } else {
            date = date.substring(0, length - 2) + "20" + dateYear;
        }
        return date;
    }

    public static int getDate(String date) {
        try {

            //date format does not like dash, so convert it to dot if found
            date = date.replaceAll("-", ".");

            if (date.matches(DATE_FORMAT_MM_DD_YY)) {
                date = normalizeDate(date);
                return DateUtils.getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_MM_DD_YYYY)) {
                return DateUtils.getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_DOT_MM_DD_YY)) {
                date = normalizeDate(date);
                return DateUtils.getDate(new SimpleDateFormat("MM.dd.yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_DASH_MM_DD_YY)) {
                date = normalizeDate(date);
                return DateUtils.getDate(new SimpleDateFormat("MM-dd-yyyy").parse(date));
            }

        } catch (Exception ex) {
            Logger.getLogger(ArahantDateParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static void main(String[] args) {
//        12/22/10  (assume 2010)
//        12/22/2010
//        12/22/82  (assume 1982)  (break point is 20)
//        2/22/10
//        2/4/10
//        2-4-10
//        2.4.10
        List<String> dates = new ArrayList<String>();
        dates.add("12/22/10");
        dates.add("12/22/2010");
        dates.add("12/22/82");
        dates.add("2/22/10");
        dates.add("2/4/10");
        dates.add("2-4-10");
        dates.add("2.4.10");
        dates.add("02/4/10");
        dates.add("02-24-10");
        dates.add("02.04.10");

        for (String thisDate : dates) {
            System.out.println("Date new " + thisDate + " is " + DateUtils.getDate(thisDate));
        }

    }
}
