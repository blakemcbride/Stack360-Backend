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
 * Created on Dec 22, 2006
 * 
 */
package com.arahant.utils;

/**
 * 
 *
 * Created on Dec 22, 2006
 *
 */
public class MoneyUtils {

    public static String formatMoney(int x) {
        return formatMoney((double) x);
    }

    public static String formatMoney(double x) {
		//x+=.0005;
        final double y = Math.round(x * 100);
        x = y / 100;

        //now I'm rounded

        String res = "" + x;
        final boolean isNegative = res.startsWith("-");
        if (isNegative) {
            res = res.substring(1);
        }
        final int pt = res.indexOf('.');

        //check for decimal point
        if (pt == -1) {
            res += ".00";
        } else {
            final int len = res.length();
            if (pt == len - 2) {
                res += "0";
            }
        }

        //now yank off the cents and add commas

        final String cents = res.substring(res.length() - 3, res.length());

        String dollars = res.substring(0, res.length() - 3);

        String hold = "";

        while (dollars.length() > 3) {
            hold = "," + dollars.substring(dollars.length() - 3, dollars.length()) + hold;
            dollars = dollars.substring(0, dollars.length() - 3);
        }

        String t = dollars + hold + cents;

        if (t.startsWith(".")) {
            t = "0" + t;
        }

        if (isNegative) {
            t = "($" + t + ")";
        } else {
            t = "$" + t;
        }

        return t;
    }

    public static void main(final String args[]) {
        System.out.println(formatMoney(2));
        System.out.println(formatMoney(2.1));
        System.out.println(formatMoney(123332.1));
        System.out.println(formatMoney(123332.12233));
    }

    /**
     * @param calculatedCost
     * @return
     */
    public static float parseMoney(String calculatedCost) {

        try {

            if (calculatedCost == null || calculatedCost.trim().equals("")) {
                return 0;
            }

            calculatedCost = calculatedCost.trim();

            if (calculatedCost.charAt(0) == '$') {
                calculatedCost = calculatedCost.substring(1);
            }

            String num = "";
            for (int loop = 0; loop < calculatedCost.length(); loop++) {
                if (calculatedCost.charAt(loop) != ',') {
                    num = num + calculatedCost.charAt(loop);
                }
            }

            return Float.parseFloat(num);
        } catch (Exception ex) {
            return 0;
        }

    }
}

	
