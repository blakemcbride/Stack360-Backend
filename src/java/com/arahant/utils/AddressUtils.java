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


/**
 *
 *
 */

package com.arahant.utils;

import com.arahant.exceptions.ArahantException;

public class AddressUtils {

	public static String getState(String state)
	{
            if (state.equalsIgnoreCase("Alabama"))
                return "AL";
            if (state.equalsIgnoreCase("Alaska"))
                return "AK";
            if (state.equalsIgnoreCase("Arizona"))
                return "AZ";
            if (state.equalsIgnoreCase("Arkansas"))
                return "AR";
            if (state.equalsIgnoreCase("California"))
                return "CA";
            if (state.equalsIgnoreCase("Colorado"))
                return "CO";
            if (state.equalsIgnoreCase("Connecticut"))
                return "CT";
            if (state.equalsIgnoreCase("Delaware"))
                return "DE";
            if (state.equalsIgnoreCase("District of Columbia"))
                return "DC";
            if (state.equalsIgnoreCase("Florida"))
                return "FL";
            if (state.equalsIgnoreCase("Georgia"))
                return "GA";
            if (state.equalsIgnoreCase("Hawaii"))
                return "HI";
            if (state.equalsIgnoreCase("Idaho"))
                return "ID";
            if (state.equalsIgnoreCase("Illinois"))
                return "IL";
            if (state.equalsIgnoreCase("Indiana"))
                return "IN";
            if (state.equalsIgnoreCase("Iowa"))
                return "IA";
            if (state.equalsIgnoreCase("Kansas"))
                return "KS";
            if (state.equalsIgnoreCase("Kentucky"))
                return "KY";
            if (state.equalsIgnoreCase("Louisiana"))
                return "LA";
            if (state.equalsIgnoreCase("Maine"))
                return "ME";
            if (state.equalsIgnoreCase("Maryland"))
                return "MD";
            if (state.equalsIgnoreCase("Massachusetts"))
                return "MA";
            if (state.equalsIgnoreCase("Michigan"))
                return "MI";
            if (state.equalsIgnoreCase("Minnesota"))
                return "MN";
            if (state.equalsIgnoreCase("Mississippi"))
                return "MS";
            if (state.equalsIgnoreCase("Missouri"))
                return "MO";
            if (state.equalsIgnoreCase("Montana"))
                return "MT";
            if (state.equalsIgnoreCase("Nebraska"))
                return "NE";
            if (state.equalsIgnoreCase("Nevada"))
                return "NV";
            if (state.equalsIgnoreCase("New Hampshire"))
                return "NH";
            if (state.equalsIgnoreCase("New Jersey"))
                return "NJ";
            if (state.equalsIgnoreCase("New Mexico"))
                return "NM";
            if (state.equalsIgnoreCase("New York"))
                return "NY";
            if (state.equalsIgnoreCase("North Carolina"))
                return "NC";
            if (state.equalsIgnoreCase("North Dakota"))
                return "ND";
            if (state.equalsIgnoreCase("Ohio"))
                return "OH";
            if (state.equalsIgnoreCase("Oklahoma"))
                return "OK";
            if (state.equalsIgnoreCase("Oregon"))
                return "OR";
            if (state.equalsIgnoreCase("Pennsylvania"))
                return "PA";
            if (state.equalsIgnoreCase("Rhode Island"))
                return "RI";
            if (state.equalsIgnoreCase("South Carolina"))
                return "SC";
            if (state.equalsIgnoreCase("South Dakota"))
                return "SD";
            if (state.equalsIgnoreCase("Tennessee"))
                return "TN";
            if (state.equalsIgnoreCase("Texas"))
                return "TX";
            if (state.equalsIgnoreCase("Utah"))
                return "UT";
            if (state.equalsIgnoreCase("Vermont"))
                return "VT";
            if (state.equalsIgnoreCase("Virginia"))
                return "VA";
            if (state.equalsIgnoreCase("Washington"))
                return "WA";
            if (state.equalsIgnoreCase("West Virginia"))
                return "WV";
            if (state.equalsIgnoreCase("Wisconsin"))
                return "WI";
            if (state.equalsIgnoreCase("Wyoming"))
                return "WY";

            throw new ArahantException("State code not found - " + state);
	}
}
