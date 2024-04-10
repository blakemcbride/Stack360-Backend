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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A set of formatting functions for the standard scalar data types and various object types such as Date. 
 * The purpose is primarily to provide reusable functions for these common operations and to also set a standard for the formatting of the types included. 
 * 
 * <a name="DateTimeFunctions"></a>
 * <h3>Date/Time Functions</h3>
 * Also included in this class are some standard Date/Time formatting strings and their corresponding classes derived from {@link java.text.SimpleDateFormat}. While
 * the {@link java.text.SimpleDateFormat SimpleDateFormat} class documentation documents the Date/Time Pattern string syntax, included here is a copy of that documentation
 * for conveinient reference. 
 
 	<pre>
  		G  	Era designator 	Text  	AD
	    y 	Year 	Year 	1996; 96
	    M 	Month in year 	Month 	July; Jul; 07
	    w 	Week in year 	Number 	27
	    W 	Week in month 	Number 	2
	    D 	Day in year 	Number 	189
	    d 	Day in month 	Number 	10
	    F 	Day of week in month 	Number 	2
	    E 	Day in week 	Text 	Tuesday; Tue
	    a 	Am/pm marker 	Text 	PM
	    H 	Hour in day (0-23) 	Number 	0
	    k 	Hour in day (1-24) 	Number 	24
	    K 	Hour in am/pm (0-11) 	Number 	0
	    h 	Hour in am/pm (1-12) 	Number 	12
	    m 	Minute in hour 	Number 	30
	    s 	Second in minute 	Number 	55
	    S 	Millisecond 	Number 	978
	    z 	Time zone 	General time zone 	Pacific Standard Time; PST; GMT-08:00
	    Z 	Time zone 	RFC 822 time zone 	-0800
	</pre>

	<a name="StringFormattingFunctions"></a>
	<h3>String Formatting Functions</h3>
	
	This class includes an implementation of the popular "C" language sprintf() function for string formatting. The calling syntax is somewhat
	different in this Java implementation than in the standard "C" language version mainly due to the lack of support for variable number of arguments in Java.
	However, it is the ability to specify the desired format in a "format string" that is the primary power of the sprintf() function. The syntax of the
	format string is retained in this implementation. Here is a summary of that syntax.

	<pre>	
	A format specification, which consists of optional and required fields, has the following form:
	
	%[flags] [width] [.precision] [{h | l | I | I32 | I64}]type
	
	Each field of the format specification is a single character or a number signifying a particular format option. 
	The simplest format specification contains only the percent sign and a type character (for example, %s). 
	If a percent sign is followed by a character that has no meaning as a format field, the character is copied to 
	stdout. For example, to print a percent-sign character, use %%.
	
	The optional fields, which appear before the type character, control other aspects of the formatting, as follows: 
	
	type - Required character that determines whether the associated argument is interpreted as a character, 
			a string, or a number (see the printf Type Field Characters table. 
	flags - Optional character or characters that control justification of output and printing of signs, blanks, 
			decimal points, and octal and hexadecimal prefixes (see the Flag Characters table). More than one flag 
			can appear in a format specification. 
	width - Optional number that specifies the minimum number of characters output (see printf Width Specification). 
	precision - Optional number that specifies the maximum number of characters printed for all or part of the output
			field, or the minimum number of digits printed for integer values (see the How Precision Values Affect 
			Type table). 
			
	flags
	-----
	The first optional field of the format specification is flags. A flag directive is a character that justifies 
	output and prints signs, blanks, decimal points, and octal and hexadecimal prefixes. More than one flag directive 
	may appear in a format specification. 
	
	Flag Characters
	
	Flag Meaning Default 
		- 	Left align the result within the given field width.  Right align.
		+ 	Prefix the output value with a sign (+ or -) if the output value is of a signed type.
			Sign appears only for negative signed values (-).
		0 	If width is prefixed with 0, zeros are added until the minimum width is reached. If 0 and  appear, 
			the 0 is ignored. If 0 is specified with an integer format (i, u, x, X, o, d) the 0 is ignored.  No padding. 
		blank (' ') Prefix the output value with a blank if the output value is signed and positive; the blank is 
			ignored if both the blank and + flags appear. No blank appears. 
		# 	When used with the o, x, or X format, the # flag prefixes any nonzero output value with 0, 0x, or 0X, 
			respectively. No blank appears. 
	  		
	  		When used with the e, E, or f format, the # flag forces the output value to contain a decimal point in all 
	  		cases.  Decimal point appears only if digits follow it. 
	  		
			When used with the g or G format, the # flag forces the output value to contain a decimal point in all 
			cases and prevents the truncation of trailing zeros. 
			
			Ignored when used with c, d, i, u, or s.
			 Decimal point appears only if digits follow it. Trailing zeros are truncated. 
	
	type
	----
	
	Character Type Output format 
	c 	int or wint_t When used with printf functions, specifies a single-byte 
		character; when used with wprintf functions, specifies a wide character. 
	C 	int or wint_t When used with printf functions, specifies a wide character; 
		when used with wprintf functions, specifies a single-byte character. 
	d 	int Signed decimal integer. 
	i 	int  Signed decimal integer. 
	o 	int  Unsigned octal integer. 
	u 	int  Unsigned decimal integer. 
	x 	int Unsigned hexadecimal integer, using "abcdef." 
	X 	int Unsigned hexadecimal integer, using "ABCDEF." 
	e  	double Signed value having the form [  ]d.dddd e [sign]ddd where d is a 
		single decimal digit, dddd is one or more decimal digits, ddd is exactly 
		three decimal digits, and sign is + or . 
	E 	double Identical to the e format except that E rather than e introduces the 
		exponent. 
	f 	double Signed value having the form [  ]dddd.dddd, where dddd is one or 
		more decimal digits. The number of digits before the decimal point depends 
		on the magnitude of the number, and the number of digits after the decimal 
		point depends on the requested precision. 
	g 	double Signed value printed in f or e format, whichever is more compact 
		for the given value and precision. The e format is used only when the 
		exponent of the value is less than 4 or greater than or equal to the
		precision argument. Trailing zeros are truncated, and the decimal point 
		appears only if one or more digits follow it. 
	G 	double Identical to the g format, except that E, rather than e, introduces 
		the exponent (where appropriate). 
	n  	Pointer to integer  Number of characters successfully written so far to 
		the stream or buffer; this value is stored in the integer whose address 
		is given as the argument. 
	p 	Pointer to void Prints the address of the argument in hexadecimal digits. 
	s 	String  When used with printf functions, specifies a single-byte character
		string; when used with wprintf functions, specifies a wide-character 
		string. Characters are printed up to the first null character or until 
		the precision value is reached. 
	S 	String When used with printf functions, specifies a wide-character string; 
		when used with wprintf functions, specifies a single-byte character string.
		Characters are printed up to the first null character or until the precision 
		value is reached. 
	
	Note   If the argument corresponding to %s or %S is a null pointer, "(null)" will be printed.
	
	Regular Expression To Be Used
	-----------------------------
	
	\%([\-\+\x20#]*)([0-9]+)?(\.[1-9]?[0-9]*)?([cCdiouxXeEfgGnpsS])
	</pre>	
	  	  	

 * Arahant
 */
public class Formatting
{
	/*-------------------------------------------------------------------------------------------------------------
	  
	  											Name
	  
	 -------------------------------------------------------------------------------------------------------------*/

	/**
	 * This method formats a name in a standard first middle last suffix format. If any name is missing, it is simply
	 * skipped.
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param suffix
	 * @return The formatted name
	 */
	public static String formatName(final String firstName, final String middleName, final String lastName, final String suffix)
	{
		final StringBuilder name = new StringBuilder();
		if (!(firstName == null || firstName.isEmpty() || firstName.trim().isEmpty()))
		{
			name.append(firstName);
			name.append(" ");
		}
		
		if (!(middleName == null || middleName.isEmpty() || middleName.trim().isEmpty()))
		{
			name.append(middleName);
			name.append(" ");
		}
		
		if (!(lastName == null || lastName.isEmpty() || lastName.trim().isEmpty()))
		{
			name.append(lastName);
			name.append(" ");
		}
		
		if (!(suffix == null || suffix.isEmpty() || suffix.trim().isEmpty()))
			name.append(suffix);
		
		return name.toString().trim();
	}

	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * 											Currency
	 * 
	 -------------------------------------------------------------------------------------------------------------*/

	
	
	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * 											Percentages
	 * 
	 -------------------------------------------------------------------------------------------------------------*/

	/**
	 * This method formats a value as a percentage with the trailing percent "%" sign. The target value is a decimal
	 * representation of the percentage. For example, .5 represents 50%. The number of decimal places can also be specified.
	 *   
	 * @param pct the target value to format
	 * @param decimalPlaces the number of places to fill to the right of the decimal sign. If this number is greater than the actual
	 * number of decimal digits, the vakue will be padded. If it is less, the value will be rounded.
	 * @return The formatted percentage value.
	 */
	public static String formatPercentage(double pct, final int decimalPlaces)
	{
		pct = pct * 100; // get it into the correct decimal alignment
		String formattedNumber = Formatting.formatNumber(pct, decimalPlaces);
		
		return formattedNumber + "%";
	}

	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * 											Numeric
	 * 
	 -------------------------------------------------------------------------------------------------------------*/
	/**
	 * This method strips all non-numeric characters from a string and then converts it to a simple string representation
	 * of a double. Formatting characters include, but are not limited to, a dollar sign, leading plus sign, surround parentheses
	 * to indicate a negative value, comma delimiters, etc.
	 * 
	 * @param value the target string value.
	 * @return a copy of the target string stripped of all non-numeric characters except an optional decimal point and an optional
	 * leading minus sign.
	 */
	public static String stripNumericFormatting(String value)
	{
		final StringBuilder ret = new StringBuilder();
		
		if (value.startsWith("-")) {
			ret.append("-");
			value = value.substring(1);
		}
		else if (value.startsWith("+"))
			value = value.substring(1);
		else if (value.startsWith("(") && value.endsWith(")")) {
			ret.append("-");
			value = value.substring(1, value.length() - 1);
		}

		boolean hasDecimal = false;
		for (int i = 0; i < value.length(); i++)
		{
			final char c = value.charAt(i);

			if (Character.isDigit(c))
				ret.append(c);
			else if (!hasDecimal && c == '.') {
				hasDecimal = true;
				ret.append('.');
			}
		}
		
		return ret.toString();
	}


	/**
	 * This method formats a number to a specified number of places.
	 *
	 * @param num the target value to format
	 * @param decimalPlaces the number of places to fill to the right of the decimal sign. If this number is greater than the actual
	 * number of decimal digits, the value will be padded. If it is less, the value will be rounded.
	 * @return The formatted number value.
	 */
	public static String formatNumberWithCommas(double num, final int decimalPlaces)
	{
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf("%,."+decimalPlaces+"f",num);
        pw.flush();
        sw.flush();
        return sw.toString();
		/* This was changing the value when it didn't need to be rounded.  THANKS JOHN!
		final String rounded = Conversions.round(num, decimalPlaces) + "";
		final int decimalOffset = rounded.indexOf('.');
		StringBuffer sb = new StringBuffer(rounded);

		if (decimalPlaces == 0)
			sb.setLength(decimalOffset);
		else {
			final int targetLength = decimalOffset + 1 + decimalPlaces;
			final int currentLength = rounded.length();

			if (currentLength > targetLength)
				sb.setLength(targetLength);
			else if (currentLength < targetLength)
				for (int cnt = 0; cnt < targetLength - currentLength; cnt++)
					sb.append("0");
		}

		return sb.toString();
		 * */
	}

	/**
	 * This method formats a number to a specified number of places.
	 *   
	 * @param num the target value to format
	 * @param decimalPlaces the number of places to fill to the right of the decimal sign. If this number is greater than the actual
	 * number of decimal digits, the value will be padded. If it is less, the value will be rounded.
	 * @return The formatted number value.
	 */
	public static String formatNumber(double num, final int decimalPlaces)
	{
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf("%."+decimalPlaces+"f",num);
        pw.flush();
        sw.flush();
        return sw.toString();
		/* This was changing the value when it didn't need to be rounded.  THANKS JOHN!
		final String rounded = Conversions.round(num, decimalPlaces) + "";
		final int decimalOffset = rounded.indexOf('.');
		StringBuffer sb = new StringBuffer(rounded);
		
		if (decimalPlaces == 0)
			sb.setLength(decimalOffset);
		else {
			final int targetLength = decimalOffset + 1 + decimalPlaces;
			final int currentLength = rounded.length();
			
			if (currentLength > targetLength)
				sb.setLength(targetLength);
			else if (currentLength < targetLength)
				for (int cnt = 0; cnt < targetLength - currentLength; cnt++)
					sb.append("0");
		}
		
		return sb.toString();
		 * */
	}

	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * 											Date/Time
	 *
	 -------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * The date format string for a two digit year. For example, 08/06/99. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_2DIGITYEAR
	 */
	public static final String DATE_2DIGITYEAR_PATTERN = "MM/dd/yy"; // 08/06/99

	/**
	 * The date formattter for a two digit year. For example, 08/06/99
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_2DIGITYEAR_PATTERN
	 */
	public static final SimpleDateFormat DATE_2DIGITYEAR = new SimpleDateFormat(DATE_2DIGITYEAR_PATTERN);
	
	/**
	 * The date format string for a four digit year. For example, 08/06/1999. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE
	 */
	public static final String DATE_PATTERN = "MM/dd/yyyy"; // 08/06/1999
	
	/**
	 * The date formatter for a four digit year. For example, 08/06/1999
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_PATTERN
	 */
	public static final SimpleDateFormat DATE = new SimpleDateFormat(DATE_PATTERN);

	/**
	 * The date format string for a four digit year and time. For example, 08/06/1999 18:30:00. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATETIME
	 */
	public static final String DATETIME_PATTERN = "MM/dd/yyyy HH:mm:ss"; // 08/06/1999

	/**
	 * The date formatter for a four digit year and time. For example, 08/06/1999 18:30:00.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATETIME_PATTERN
	 */
	public static final SimpleDateFormat DATETIME = new SimpleDateFormat(DATETIME_PATTERN);

	/**
	 * The date format string for a two digit year with hyphen separator. For example, 08-06-99. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_2DIGITYEAR
	 */
	public static final String DATE_DASH_2DIGITYEAR_PATTERN = "MM-dd-yy"; // 08-06-99

	/**
	 * The date formatter for a two digit year with hyphen separator. For example, 08-06-99
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_2DIGITYEAR_PATTERN
	 */
	public static final SimpleDateFormat DATE_DASH_2DIGITYEAR = new SimpleDateFormat(DATE_DASH_2DIGITYEAR_PATTERN);

	/**
	 * The date format string for a four digit year with hyphen separator. For example, 08-06-1999. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_DASH
	 */
	public static final String DATE_DASH_PATTERN = "MM-dd-yyyy"; // 08-06-1999

	/**
	 * The date formatter for a four digit year with hyphen separator. For example, 08-06-1999
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_DASH_PATTERN
	 */
	public static final SimpleDateFormat DATE_DASH = new SimpleDateFormat(DATE_DASH_PATTERN);

	/**
	 * A date format string. For example, August 6, 1999. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_FULL
	 */
	public static final String DATE_FULL_PATTERN = "MMMM d, yyyy"; // August 6, 1999

	/**
	 * A date formatter. For example, August 6, 1999
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_FULL_PATTERN
	 */
	public static final SimpleDateFormat DATE_FULL = new SimpleDateFormat(DATE_FULL_PATTERN);
	
	/**
	 * The date format string similar to that found using the MS-Windows "dir" command. For example, 08/06/1999 06:30 pm. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_DIRLISTING
	 */
	public static final String DATE_DIRLISTING_PATTERN = "MM/dd/yyyy hh:mm a";
	
	/**
	 * The date formatter similar to that found using the MS-Windows "dir" command. For example, 08/06/1999 06:30 pm
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_DIRLISTING_PATTERN
	 */
	public static final SimpleDateFormat DATE_DIRLISTING = new SimpleDateFormat(DATE_DIRLISTING_PATTERN);
	
	/**
	 * The date format string used by Integra for internal representations, usually as a long value. For example, 19990806. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_INTEGRA
	 */
	public static final String DATE_INTEGRA_PATTERN = "yyyyMMdd";
	
	/**
	 * The date formatter used by Integra for internal representations, usually as a long value. For example, 19990806
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_INTEGRA_PATTERN
	 */
	public static final SimpleDateFormat DATE_INTEGRA = new SimpleDateFormat(DATE_INTEGRA_PATTERN);

	/**
	 * The date format string that can be used as a literal in ANSI SQL statements. For example, 1999-08-06. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_SQL_LITERAL
	 */
	public static final String DATE_SQL_LITERAL_PATTERN = "yyyy-MM-dd";

	/**
	 * The date formatter that can be used as a literal in ANSI SQL statements. For example, 1999-08-06
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_SQL_LITERAL_PATTERN
	 */
	public static final SimpleDateFormat DATE_SQL_LITERAL = new SimpleDateFormat(DATE_SQL_LITERAL_PATTERN);

	/**
	 * The date format string used in HTTP headers. For example, Fri, 06 August 1999 18:30:00 CDT. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_HTTP
	 */
	public static final String DATE_HTTP_PATTERN = "EEE, d MMM yyyy HH:mm:ss z";

	/**
	 * The date formatter used in HTTP headers. For example, Fri, 06 August 1999 18:30:00 CDT
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_HTTP_PATTERN
	 */
	public static final SimpleDateFormat DATE_HTTP = new SimpleDateFormat(DATE_HTTP_PATTERN);

	/**
	 * The date format string used to represent time intervals. For example, 06:30:17.357. The pattern is <code>{@value}</code>.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_TIME_INTERVAL
	 */
	public static final String DATE_TIME_INTERVAL_PATTERN = "HH:mm:ss.SSS";

	/**
	 * The date formatter used to represent time intervals. For example, 06:30:17.357
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_TIME_INTERVAL_PATTERN
	 */
	public static final SimpleDateFormat DATE_TIME_INTERVAL = new SimpleDateFormat(DATE_TIME_INTERVAL_PATTERN);
	
	static
	{
		DATE_HTTP.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/**
	 * Formats a {@link java.util.Date Date} using the {@link #DATE} format.
	 * 
	 * @param date the target {@link java.util.Date Date}
	 * @return the formatted date string.
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 */
	public static String formatDate(final Date date)
	{
		if (Dates.isNull(date))
			return "";
		
		return DATE.format(date);
	}
	
	/**
	 * Formats a {@link java.util.Date Date} using the supplied Date/Time format String. In general, you should use one of Date/Format strings defined in this
	 * class. 
	 * @param date
	 * @param pattern
	 * @return the formatted date
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 * @see #DATE_2DIGITYEAR_PATTERN
	 * @see #DATE_DASH_2DIGITYEAR_PATTERN
	 * @see #DATE_DASH_PATTERN
	 * @see #DATE_DIRLISTING_PATTERN
	 * @see #DATE_FULL_PATTERN
	 * @see #DATE_HTTP_PATTERN
	 * @see #DATE_INTEGRA_PATTERN
	 * @see #DATE_SQL_LITERAL_PATTERN
	 */
	public static String formatDate(final Date date, final String pattern)
	{
		if (Dates.isNull(date))
			return "";
		
		if (pattern == null)
			return DATE.format(date);
		
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * Formats a date using the {@link #DATE_INTEGRA} format.
	 * @param date the target {@link java.util.Date Date}
	 * @return the formatted date string
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 */
	public static String formatIntegraDate(final Date date)
	{
		if (Dates.isNull(date))
			return "";
		
		return DATE_INTEGRA.format(date);
	}

	/**
	 * Formats a date using the {@link #DATE_SQL_LITERAL} format.
	 * @param date the target {@link java.util.Date Date}
	 * @return the formatted date string
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 */
	public static String formatSQLDate(final Date date)
	{
		if (Dates.isNull(date))
			return "";
		
		return DATE_SQL_LITERAL.format(date);
	}

	/**
	 * Formats a millisecond value as a time interval of the format...
	 * 
	 * <pre>
	 * if hours > 0
	 * 		HH:mm:ss.SSS
	 * 
	 * if minutes > 0
	 * 		mm:ss.SSS
	 * 
	 * if seconds > 0
	 * 		ss.SSS seconds
	 * 
	 * </pre>
	 * 
	 * @param milliseconds the number of milliseconds representing the interval to format
	 * @return the formatted string
	 * @see <a href="#DateTimeFunctions">Date/Time Functions</a>
	 */
	public static String formatDuration(long milliseconds)
	{
		final long hours = milliseconds / 3600000;
		long tmp = milliseconds - (hours * 3600000);
		final long minutes = tmp / 60000;
		tmp = tmp - (minutes * 60000);
		final long seconds = tmp / 1000;
		milliseconds = tmp - (seconds * 1000);
	
		if (hours > 0)
			return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
		else if (minutes > 0)
			return minutes + ":" + seconds + "." + milliseconds;
		else
			return seconds + "." + milliseconds + " seconds.";
	}    

	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * 											Javascript
	 * 
	 -------------------------------------------------------------------------------------------------------------*/

	/**
	 * Formats a string for inclusion in a Javascript script as a string literal. Whitespace characters are escaped as are
	 * single and double quote characters.
	 *  
	 * @param str the target string
	 * @return the formatted string
	 */
	public static String formatForJavascript(String str)
	{
		str = str.replaceAll( "\n", "\\n");
		str = str.replaceAll( "\r", "");
		str = str.replaceAll( "\t", "\\t");
		str = str.replaceAll( "'", "\\'");
		str = str.replaceAll( "\"", "\\\"");
		return str;
	}

	/*-------------------------------------------------------------------------------------------------------------

		String formatting

-------------------------------------------------------------------------------------------------------------*/
	
	
	
	/**
	 * Pads a string with spaces " " until the string reaches a specified length. If the target string before padding equals or exceeds the 
	 * specified length, no padding or truncating is performed.
	 * 
	 * @param value the target string
	 * @param maxLength the maximum length to pad the string
	 * @return the formatted string
	 * @see #pad(String, int, String, Boolean)
	 * @see #pad(String, int, String, boolean)
	 * @see #pad(String, int, String)
	 */
	public static String pad(final String value, final int maxLength)
	{
		return pad(value, maxLength, " ", Boolean.TRUE);
	}

	/**
	 * Pads a string with a specifed padding string until the string reaches a specified length. If the target string before padding equals or exceeds the 
	 * specified length, no padding or truncating is performed. The padding string may be of any length. It will be applied to the end of the target string
	 * repeatedly until the string length equals or exceeds the maxLength paramter. If the length exceeds maxLength, the string will then be
	 * truncated to equal maxLength.
	 * 
	 * @param value the target string
	 * @param maxLength the maximum length to pad the string
	 * @param paddingString the string used to pad.
	 * @return the formatted string
	 * @see #pad(String, int, String, Boolean)
	 * @see #pad(String, int, String, boolean)
	 * @see #pad(String, int)
	 */
	public static String pad(final String value, final int maxLength, final String paddingString)
	{
		return pad(value, maxLength, paddingString, Boolean.TRUE);
	}

	/**
	 * Pads a string with a specifed padding string until the string reaches a specified length. If the target string before padding equals or exceeds the 
	 * specified length, no padding or truncating is performed. The padding string may be of any length. Depending on the value of the append parameter, the 
	 * padding string may be applied to either the beginning or the end of the target string. It will be applied repeatedly until the string length equals or 
	 * exceeds the maxLength paramter. The padded portion of the string will then be truncated so that the total string length equals maxLength.
	 * 
	 * @param value the target string
	 * @param maxLength the maximum length to pad the string
	 * @param paddingString the string used to pad.
	 * @param append true to append the padding, false to prepend the padding
	 * @return the formatted string
	 * @see #pad(String, int, String, Boolean)
	 * @see #pad(String, int, String)
	 * @see #pad(String, int)
	 */
	public static String pad(final String value, final int maxLength, final String paddingString, final boolean append)
	{
		return pad(value, maxLength, paddingString, append);
	}
	
	/**
	 * Pads a string with a specifed padding string until the string reaches a specified length. If the target string before padding equals or exceeds the 
	 * specified length, no padding or truncating is performed. The padding string may be of any length. Depending on the value of the append parameter, the 
	 * padding string may be applied to either the beginning or the end of the target string or both. It will be applied repeatedly until the string length equals or 
	 * exceeds the maxLength paramter. The padded portion of the string will then be truncated so that the total string length equals maxLength.
	 * 
	 * @param value the target string
	 * @param maxLength the maximum length to pad the string
	 * @param paddingString the string used to pad.
	 * @param append true to append the padding, false to prepend the padding, null to pad evenly to both the beginning and the end of the string.
	 * @return the formatted string
	 * @see #pad(String, int, String, boolean)
	 * @see #pad(String, int, String)
	 * @see #pad(String, int)
	 */
	public static String pad(String value, final int maxLength, String paddingString, final Boolean append)
	{
		if (paddingString == null)
			paddingString = " ";
			
		int remaining = maxLength - value.length();
		int leftPad = 0;
		int rightPad = 0;
		if (append == null)
		{
			leftPad = remaining / 2;
			rightPad = remaining - leftPad;
		}
		else if (append == Boolean.FALSE)
			leftPad = remaining;
		else
			rightPad = remaining;

		StringBuffer padText = new StringBuffer();
		while (padText.length() < leftPad)
		{
			remaining = leftPad - padText.length();
			if (remaining < paddingString.length())
				padText.append(paddingString, 0, remaining);
			else
				padText.append(paddingString);
		}
		
		value = padText + value;
		
		padText = new StringBuffer();
		while (padText.length() < rightPad)
		{
			remaining = rightPad - padText.length();
			if (remaining < paddingString.length())
				padText.append(paddingString, 0, remaining);
			else
				padText.append(paddingString);
		}
		
		value += padText;
		return value;
	}

	/**
	 * Creates a string that is the result of repeating appending the target string n number of times. 
	 * @param str the target string
	 * @param times The number of times to repeat the target string
	 * @return the constructed string
	 */
	public static String repeat(final String str, final int times)
	{
		return String.valueOf(str).repeat(Math.max(0, times));
	}

	/**
	        Normalizing phone number format is important if you are to be able to search by phone numbers.

	        If the number is not recognized as a valid phone number, it is returned untouched.
	 */
	public static String formatPhoneNumber(final String number) {
		if (number == null)
			return number;  // invalid phone number
		String n = number.replaceAll("[^\\d]", "");
        if (n.length() < 10)
            return number;   // invalid phone number
		if (n.length() == 11 && n.charAt(0) == '1') // remove leading 1
			n = n.substring(1);
		if (n.length() == 10)
			return n.substring(0, 3) + "-" + n.substring(3, 6) + "-" + n.substring(6);
		return n.substring(0, 3) + "-" + n.substring(3, 6) + "-" + n.substring(6, 10) + " x" + n.substring(10);
    }

}


class StringFormat
{
	private final String format;
	private boolean isLeftAligned = false;
	private final String flags;
	private String positivePrefixChar = "";
	private String paddingChar = "";
	private final int width;
	private String prec = "";
	private String type = "";

	StringFormat(final String format, final String flags, final String width, final String prec, final String type)
	{
		this.format = format;
		isLeftAligned = format != null && format.contains("-");
		
		this.flags = flags;
		this.positivePrefixChar = "";
		if (flags != null && !flags.isEmpty() && !flags.trim().isEmpty())
		{
			if (flags.contains(" "))
				positivePrefixChar = " ";
				
			if (flags.contains("+"))
				positivePrefixChar = "+";
		}
		
		if (width != null && !width.isEmpty() && !width.trim().isEmpty())
		{
			if (width.startsWith("0") && width.length() > 1)
			{
				paddingChar = "0";
				this.width = Integer.parseInt(width.substring(1));
			}
			else
			{
				this.width = Integer.parseInt(width);
				paddingChar = " ";
			}
		}
		else
		{
			this.width = 1;
			paddingChar = " ";
		}
			
		this.prec = prec;
		this.type = type;
	}

	String formatValue(final Object value)
	{
		if (value == null)
			return "(null)";

		String ret = null;
		if (type.equalsIgnoreCase("c"))
			ret = formatChar(value);
		else if (type.equalsIgnoreCase("d"))
			ret = formatInt(value, 10);
		else if (type.equals("o"))
			ret = formatInt(value, 8);
		else if (type.equals("x"))
			ret = formatInt(value, 16).toLowerCase();
		else if (type.equals("X"))
		{
			ret = formatInt(value, 16);
			if (ret.startsWith("0x"))
				ret = "0x" + ret.substring(2).toUpperCase();
			else
				ret = ret.toUpperCase();
		}
		else if (type.equals("e"))
			ret = formatFloat(value, Boolean.TRUE).toLowerCase();
		else if (type.equals("E"))
			ret = formatFloat(value, Boolean.TRUE).toUpperCase();
		else if (type.equals("f"))
			ret = formatFloat(value, Boolean.FALSE).toUpperCase();
		else if (type.equals("g"))
			ret = formatFloat(value, null).toLowerCase();
		else if (type.equals("G"))
			ret = formatFloat(value, null).toUpperCase();
		else if (type.equalsIgnoreCase("s"))
			ret = formatString(value);
		else
			throw new UnsupportedOperationException("ValueFormatter.sprintf does not support the '" + type + "' type specifier.");
		
		ret = formatWidth(ret);
		return ret;
	}
	
	String formatInt(final Object value, final int radix)
	{
		int intValue = 0;
		StringBuilder ret = new StringBuilder();
		
		if (value instanceof Number)
			intValue = ((Number)value).intValue();
		else if (value instanceof String)
			intValue = Integer.parseInt((String)value);
		else
			throw new NumberFormatException("ValueFormatter.sprintf() expects either a String or Number parameter for integer type values.");
		
		if (radix == 10)
			ret = new StringBuilder("" + Math.abs(intValue));
		else if (radix == 8)
			ret = new StringBuilder(Integer.toOctalString(Math.abs(intValue)));
		else if (radix == 16)
			ret = new StringBuilder(Integer.toHexString(Math.abs(intValue)));

		int lprec = 1;
		if (this.prec != null && this.prec.length() > 1)
			lprec = Integer.parseInt(this.prec.substring(1));
			
		lprec = Math.max(0, lprec - ret.length());
		if (flags.contains("#"))
			if (type.equals("o"))
				lprec -= 1;
			else if (type.equals("x") || type.equals("X"))
				lprec -= 2;
		
		for (int i = 0; i < lprec; i++)
			ret.insert(0, "0");
			
		if (flags.contains("#"))
			if (type.equals("o"))
				ret.insert(0, "0");
			else if (type.equals("x") || type.equals("X"))
				ret.insert(0, "0x");
	
		if (intValue >= 0)
			ret.insert(0, positivePrefixChar);
		else
			ret.insert(0, "-");
			
		paddingChar = " ";
		
		return ret.toString();
	}

	// useExponential true, false or null which means to determine based on value
	String formatFloat(final Object value, Boolean useExponential)
	{
		double dValue = 0;
		
		if (value instanceof Number)
			dValue = ((Number)value).doubleValue();
		else if (value instanceof String)
			dValue = Double.parseDouble((String)value);
		else
			throw new NumberFormatException("ValueFormatter.sprintf() expects either a String or Number parameter for floating point type values.");
		
		int lprec = 6;
		if (!(this.prec == null || this.prec.isEmpty() || this.prec.trim().isEmpty()))
			if (this.prec.equals("."))
				lprec = 0;
			else
				lprec = Integer.parseInt(this.prec.substring(1));
			
		if (useExponential == null)
			if (Math.abs(dValue) < .0001 || Math.abs(dValue) >= Math.pow(10, lprec))
				useExponential = Boolean.TRUE;
			else
				useExponential = Boolean.FALSE;
	
		String ret;
		dValue = Math.floor(dValue * Math.pow(10, lprec)) / Math.pow(10, lprec);
		if (useExponential) {
			String pattern = "#0." + "0".repeat(Math.max(0, lprec)) + "E0";
			final StringBuffer result = new StringBuffer();
			ret = new DecimalFormat(pattern).format(dValue, result, new FieldPosition(0)).toString();
		} else {
			final StringBuffer result = new StringBuffer();
			ret = new DecimalFormat("#0." + "0".repeat(Math.max(0, lprec))).format(dValue, result, new FieldPosition(0)).toString();
		}
	
		if (dValue >= 0)
			ret = positivePrefixChar + ret;
		
		return ret;
	}

	String formatString(final Object value)
	{
		final String sValue = value.toString();
		String ret;
		
		if (prec != null && prec.length() > 1)
			ret = sValue.substring(0, Math.min(Integer.parseInt(prec.substring(1)), sValue.length()));
		else
			ret = sValue;
		
		return ret;
	}
			
	String formatWidth(final Object value)
	{
		String sValue = value.toString();
		if (sValue.length() < width)
			// always use space as padding char is left alignment is specified
			if (isLeftAligned)
				sValue = Formatting.pad(sValue, width, " ", Boolean.TRUE);
			else
				sValue = Formatting.pad(sValue, width, paddingChar, Boolean.FALSE);
		
		return sValue;
	}

	String formatChar(final Object value)
	{
		final String sValue = value.toString();
		if (sValue.isEmpty())
			return "";
		else
			return "" + sValue.charAt(0);
	}

	@Override
	public String toString()
	{
		return "format: " + this.format + "\n"
			+ "flags: " + this.flags + "\n"
			+ "width: " + this.width + "\n"
			+ "precision: " + this.prec + "\n"
			+ "type: " + this.type + "\n"
			+ "positivePrefixChar: '" + this.positivePrefixChar + "'" + "\n";
	}
	
}

