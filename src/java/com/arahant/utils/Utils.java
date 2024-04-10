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
 * Utils.java
 *
 * Created on October 28, 2007, 9:11 PM
 *
 */
package com.arahant.utils;

import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.io.*;
import java.sql.SQLException;

/**
 *
 * @author Blake McBride
 */
public class Utils {

	private static String errorString;
	private static Boolean demoCache = null;

	/** Creates a new instance of Utils */
	public Utils() {
	}

	public static String getStackTrace(Throwable t) {
		return getStackTrace(t, null);
	}

	public static String getStackTrace(Throwable t, String msg) {
		StringWriter sx = new StringWriter();
		PrintWriter sw = new PrintWriter(sx);
		if (msg != null)
			sw.println(msg);
		t.printStackTrace(sw);

		if (t instanceof SQLException) {
			SQLException se = (SQLException) t;
			while (se.getNextException() != null) {
				se = se.getNextException();
				sw.println("Nested exception :" + se.getMessage());
				se.printStackTrace(sw);
			}
		} else
			while (t.getCause() != null) {
				t = t.getCause();
				sw.println("Nested exception :" + t.getMessage());
				t.printStackTrace(sw);
			}

		return sx.toString();
	}

	/**
	 * This method opens a file and returns an InputStream.  It first attempts to open the file through the normal file system.
	 * If the file is not found it then attempts to open the file as a resource from within a jar file. This way a default file
	 * can be located in the jar file but this file can be overridden by creating a file external to the jar file.
	 *
	 * @param file the name of the file to be opened
	 * @return the InputStream or null if file not found
	 */
	public static InputStream openFromJar(String file) {
		//  Trys to open file external to the jar (as an override file) else attempt to open the file from the jar
		try {
			FileInputStream fi = new FileInputStream(file);
			return fi;
		} catch (Throwable e) {
			InputStream pf = Utils.class.getClassLoader().getResourceAsStream(file);
			return pf;
		}
	}

	public static void setErrorString(String str) {
		errorString = str;
		exitDisplayError();  //  For now.  Need a general way of setting this behavior
	}

	public static String getErrorString() {
		return errorString;
	}

	public static void clearErrorString() {
		errorString = null;
	}

	public static String formatKey(int dbid, int idx, boolean addQuotes, boolean addComma) {
		String res;
		if (addQuotes)
			res = String.format("'%05d-%010d'", dbid, idx);
		else
			res = String.format("%05d-%010d", dbid, idx);
		if (addComma)
			res = res + ", ";
		return res;
	}

	public static String formatKey(int dbid, int idx) {
		return formatKey(dbid, idx, false, false);
	}

	public static void exitDisplayError(String msg) {
		System.err.println(msg);
		System.exit(1);
	}

	public static void exitDisplayError() {
		if (errorString != null)
			System.err.println(errorString);
		throw new Error();
//        System.exit(1);
	}
	private static BufferedReader stdin = null;

	/**
	 * This method reads a line of text from stdin.
	 *
	 * @return A String containing the contents of the line, not including the line terminator.
	 * null is returned on eof.
	 */
	public static String readLine() {
		if (stdin == null)
			stdin = new BufferedReader(new InputStreamReader(System.in));
		try {
			return stdin.readLine();
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * Correctly compare two doubles.
	 * 
	 * @param d1
	 * @param d2
	 * @param maxdiff
	 * @return true if the two doubles are different by less than maxdiff
	 */
	public static boolean doubleEqual(double d1, double d2, double maxdiff) {
		double diff = d1 - d2;
		if (diff < 0.0)
			diff = -diff;
		return diff < maxdiff;
	}

	/**
	 * Round a number to the nearest p decimal places.
	 * 
	 * @param n the number to be rounded
	 * @param p the number of decimal places
	 * @return the rounded number
	 */
	public static double round(double n, int p) {
		double r = Math.pow(10.0, (double) p);
		r = Math.floor(0.5 + Math.abs(n * r)) / r;
		return n < 0.0 ? -r : r;
	}

	/**
	 * Round a number up to the nearest p decimal places.
	 * 
	 * @param n the number to be rounded
	 * @param p the number of decimal places
	 * @return the rounded number
	 */
	public static double roundUp(double n, int p) {
		double r = Math.pow(10.0, (double) p);
		r = Math.ceil(Math.abs(n * r)) / r;
		return n < 0.0 ? -r : r;
	}

	/**
	 * Round a number down to the nearest p decimal places.
	 *
	 * @param n the number to be rounded
	 * @param p the number of decimal places
	 * @return the rounded number
	 */
	public static double roundDown(double n, int p) {
		double r = Math.pow(10.0, (double) p);
		r = Math.floor(Math.abs(n * r)) / r;
		return n < 0.0 ? -r : r;
	}

	private static final char[] alpha = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 *  Numeric	formatter.  Takes a double and converts it to a nicely formatted String in a specified number base.
	 *
	 *	@param num		number to be formatted
	 *	@param base		numeric base (like base 2 = binary, 16=hex...)
	 *	@param msk		format mask - any combination of the following:<br>
	 *  <ul>
	 *     <li>B = blank if zero</li>
	 *     <li>C = add commas</li>
	 *     <li>L = left justify number</li>
	 *     <li>P = put parenthesis around negative numbers</li>
	 *     <li>Z = zero fill</li>
	 *     <li>D = floating dollar sign</li>
	 *     <li>U = uppercase letters in conversion</li>
	 *     <li>R = add a percent sign to the end of the number</li>
	 *  </ul>
	 *	@param  wth	total field width (0 means auto)
	 *	@param  dp		number of decimal places (-1 means auto)
	 *  @return the formatted String
	 *<p>
	 *	example:<br><br>
	 *
	 *		String r = Formatb(-12345.348, 10, "CP", 12, 2);<br><br>
	 *
	 *		result in r:&nbsp;&nbsp;&nbsp;"(12,345.35)"
	 * </p>
	 */
	public static String Formatb(double num, int base, String msk, int wth, int dp) {
		int si, i;
		int sign, blnk, comma, left, paren, zfill, nd, dol, tw, dl, ez, ucase, cf, percent;
		double dbase;

		if (base < 2 || base > alpha.length)
			base = 10;
		dbase = base;

		if (num < 0.0) {
			num = -num;
			sign = 1;
		} else
			sign = 0;

		/*  round number  */

		if (dp >= 0) {
			double r = Math.pow(dbase, (double) dp);
//		n = Math.floor(base/20.0 + n * r) / r;
			num = Math.floor(.5 + num * r) / r;
		}
		switch (base) {
			case 10:
				cf = 3;
				dl = num < 1.0 ? 0 : 1 + (int) Math.log10(num);	/* # of	digits left of .  */
				break;
			case 2:
				cf = 4;
				dl = num < 1.0 ? 0 : 1 + (int) (Math.log(num) / .6931471806);  /* # of digits left	of .  */
				break;
			case 8:
				cf = 3;
				dl = num < 1.0 ? 0 : 1 + (int) (Math.log(num) / 2.079441542);  /* # of digits left	of .  */
				break;
			case 16:
				cf = 4;
				dl = num < 1.0 ? 0 : 1 + (int) (Math.log(num) / 2.772588722);  /* # of digits left	of .  */
				break;
			default:
				cf = 3;
				dl = num < 1.0 ? 0 : 1 + (int) (Math.log(num) / Math.log(dbase));  /* # of digits left of .  */
				break;
		}

		if (dp < 0) {   /* calculate the number of digits right of decimal point */
			double n = num < 0.0 ? -num : num;
			dp = 0;
			while (dp < 20) {
				n -= Math.floor(n);
				if (1E-5 >= n)
					break;
				dp++;
				/*  round n to 5 places	 */
				double r = Math.pow(10.0, 5.0);
				r = Math.floor(.5 + Math.abs(n * base * r)) / r;
				n = n < 0.0 ? -r : r;
			}
		}
		blnk = comma = left = paren = zfill = dol = ucase = percent = 0;
		if (msk != null) {
			char [] t2 = msk.toCharArray();
			for (char c : t2)
				switch (c) {
					case 'B':  // blank if zero
						blnk = 1;
						break;
					case 'C':  //  add commas
						comma = (dl - 1) / cf;
						break;
					case 'L':  //  left justify
						left = 1;
						break;
					case 'P':  //  parens around negative numbers
						paren = 1;
						break;
					case 'Z':  //  zero fill
						zfill = 1;
						break;
					case 'D':  //  dollar sign
						dol = 1;
						break;
					case 'U':  //  upper case letters
						ucase = 1;
						break;
					case 'R':  //  add percent
						percent = 1;
						break;
				}
		}
		/*  calculate what the number should take up	*/

		ez = num < 1.0 ? 1 : 0;
		tw = dol + paren + comma + sign + dl + dp + (dp == 0 ? 0 : 1) + ez + percent;
		if (wth < 1)
			wth = tw;
		else if (tw > wth) {
			if (ez != 0)
				tw -= ez--;
			if ((i = dol) != 0 && tw > wth)
				tw -= dol--;
			if (tw > wth && comma != 0) {
				tw -= comma;
				comma = 0;
			}
			if (tw < wth && i != 0) {
				tw++;
				dol = 1;
			}
			if (tw > wth && paren != 0)
				tw -= paren--;
			if (tw > wth && percent != 0)
				tw -= percent--;
			if (tw > wth) {
				final char[] tbuf = new char[wth];
				for (i = 0; i < wth;)
					tbuf[i++] = '*';
				return new String(tbuf);
			}
		}
		final char[] buf = new char[wth];
		num = Math.floor(.5 + num * Math.floor(.5 + Math.pow(dbase, (double) dp)));
		if (blnk != 0 && num == 0.0) {
			for (i = 0; i < wth;)
				buf[i++] = ' ';
			return new String(buf);
		}
		si = wth;

		if (left != 0 && wth > tw) {
			i = wth - tw;
			while (i-- != 0)
				buf[--si] = ' ';
		}
		if (paren != 0)
			buf[--si] = (sign != 0 ? ')' : ' ');
        if (percent != 0)
            buf[--si] = '%';
		for (nd = 0; nd < dp && si != 0; nd++) {
			num /= dbase;
			i = (int) Math.floor(dbase * (num - Math.floor(num)) + .5);
			num = Math.floor(num);
			buf[--si] = ucase != 0 && i > 9 ? Character.toUpperCase(alpha[i]) : alpha[i];
		}
		if (dp != 0)
			if (si != 0)
				buf[--si] = '.';
			else
				num = 1.0;
		if (ez != 0 && si > sign + dol)
			buf[--si] = '0';
		nd = 0;
		while (num > 0.0 && si != 0)
			if (comma != 0 && nd == cf) {
				buf[--si] = ',';
				nd = 0;
			} else {
				num /= dbase;
				i = (int) Math.floor(dbase * (num - Math.floor(num)) + .5);
				num = Math.floor(num);
				if (ucase != 0 && i > 9)
					buf[--si] = Character.toUpperCase(alpha[i]);
				else
					buf[--si] = alpha[i];
				nd++;
			}
		if (zfill != 0) {
			i = sign + dol;
			while (si > i)
				buf[--si] = '0';
		}
		if (dol != 0 && si != 0)
			buf[--si] = '$';
		if (sign != 0)
			if (si != 0)
				buf[--si] = (paren != 0 ? '(' : '-');
			else
				num = 1.0;	/*  signal error condition	*/

		while (si != 0)
			buf[--si] = ' ';

		if (num != 0.0)         /*  should never happen. but just in case	*/
			for (i = 0; i < wth;)
				buf[i++] = '*';

		return new String(buf);
	}

	/**
	 *  Numeric	formatter.  Takes a double and converts it to a nicely formatted String (for number in base 10).
	 *
	 *	@param num		number to be formatted
	 *	@param msk		format mask - any combination of the following:<br>
	 *  <ul>
	 *     <li>B = blank if zero</li>
	 *     <li>C = add commas</li>
	 *     <li>L = left justify number</li>
	 *     <li>P = put parenthesis around negative numbers</li>
	 *     <li>Z = zero fill</li>
	 *     <li>D = floating dollar sign</li>
	 *     <li>U = uppercase letters in conversion</li>
	 *     <li>R = add a percent sign to the end of the number</li>
	 *  </ul>
	 *	@param  wth	total field width (0 means auto)
	 *	@param  dp		number of decimal places (-1 means auto)
	 *  @return the formatted String
	 *<p>
	 *	example:<br><br>
	 *
	 *		String r = Format(-12345.348, "CP", 12, 2);<br><br>
	 *
	 *		result in r:&nbsp;&nbsp;&nbsp;"(12,345.35)"
	 * </p>
	 */
	public static String Format(double num, String msk, int wth, int dp) {
		return Formatb(num, 10, msk, wth, dp);
	}

	/**
	 * For now, this is how we're determining if we're on a demo system.
	 * Basically if the primary, administrative user is named "demo" then we're treating it like a demo system.
	 * There is no way a user can do this.
	 *
	 * @return true if this is a demo system, false if a normal system
	 */
	public static boolean isDemo() {
		if (demoCache != null)
			return demoCache;
		final Connection db = KissConnection.get();
		final Command cmd = db.newCommand();
		try {
			final Record rec = cmd.fetchOne("select user_login from prophet_login where person_id = '00000-0000000000'");
			final boolean ret = rec != null && "demo".equals(rec.getString("user_login"));
			cmd.close();
			return demoCache = ret;
		} catch (Exception e) {
			return false;
		}
	}

	private static void print(double num, String msk, int wth, int dp) {
		System.out.println("\"" + Format(num, msk, wth, dp) + "\"");
	}

	public static void main(String[] argv) {
		print(-12345.146, "CDP", 12, 2);
		print( 12345.146, "CDP", 12, 2);
		print(-12345.146, "CP", 12, 2);
		print( 12345.146, "CP", 12, 2);
		print(-12345.146, "CD", 12, 2);
		print( 12345.146, "CD", 12, 2);
        print( 55.0, "PRC", 12, 0);
        print( -58.0, "PRC", 12, 0);
        print( 55.0, "RC", 12, 0);
        print( -58.0, "RC", 12, 0);
	}
}
