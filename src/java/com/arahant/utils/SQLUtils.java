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
package com.arahant.utils;

/**
 * Author: Blake McBride
 * Date: 3/19/20
 */
public class SQLUtils {


    final public static String dateCompareOperator(final int dateSearchType) {

        if (dateSearchType == SearchConstants.ALL_VALUE)
            return null;

        switch (dateSearchType) {
            case 1:
            case SearchConstants.EXACT_MATCH_VALUE:
            case SearchConstants.ON_VALUE:
                return "=";
            case 2:
            case SearchConstants.BEFORE_VALUE:
                return "<";
            case 3:
            case SearchConstants.AFTER_VALUE:
                return ">=";
            case 4:  // not equal
                return "<>";
            case SearchConstants.IS_SET_VALUE:
                return "<>0";
            case SearchConstants.IS_NOT_SET_VALUE:
                return "=0";
            default:
                return null;
        }
    }

}
