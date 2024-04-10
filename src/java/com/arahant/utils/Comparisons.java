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
 * Created on Oct 23, 2004
 *
 * 
 * 
 */
package com.arahant.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Two sets of static overloaded methods for comparing two values, equals and compare. 
 * The equals method compares two values and returns true if the values are equal and false if they are not. compare() compares two values and returns -1 if the
 * first parameter is less than the second, 0 if both parameters are equal, 1 if the 2nd parameter is greater than the first.
 * 
 * Arahant
 */
public class Comparisons 
{
	/**
	 * This is a general purpose {@link java.util.Comparator} that delegates the comparison operations to this class.
	 */
	public static final Comparator COMPARATOR = new ComparisionsComparator();
	

	/**
	 * Compares to doubles
	 * 
	 * @param d1 the first value
	 * @param d2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final double d1, final double d2)
	{
		return d1 == d2;
	}
	
	/**
	 * Compares two longs
	 * @param l1 the first value
	 * @param l2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final long l1, final long l2)
	{
		return l1 == l2;
	}

	/**
	 * Compares two booleans
	 * @param b1 the first value
	 * @param b2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final boolean b1, final boolean b2)
	{
		return b1 == b2;
	}

	/**
	 * Compares two byte arrays. This method uses {@link java.util.Arrays#equals(byte[], byte[])} to compare the two byte arrays.
	 *   
	 * @param b1 the first value
	 * @param b2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final byte [] b1, final byte [] b2)
	{
		return Arrays.equals(b1, b2);
	}

	/**
	 * Compares two byte arrays. If both arrays are null, true is returned. 
	 * 
	 * @param b1 the first value
	 * @param offset1 the offset to start in the first array
	 * @param b2 the second value
	 * @param offset2 the offset to start in the second array
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final byte [] b1, final int offset1, final byte [] b2, final int offset2)
	{
		if (b1 == null && b2 == null)
			return true;
		else if (b1 == b2 && offset1 == offset2)
			return true;
		else if (b1 == null)
			return false;
		else if (b2 == null)
			return false;
		
		final int size = Math.min(b1.length - offset1, b2.length - offset2);
		int i1 = offset1;
		int i2 = offset2;
		for (int i = 0; i < size; i++, i1++, i2++)
		{
			final byte byte1 = b1[i1];
			final byte byte2 = b2[i2];
			
			if (byte1 == byte2)
				continue;
			
			return false;
		}
		
		return b1.length == b2.length;
	}
	
	/**
	 * Compares two strings using {@link java.lang.String#equals(java.lang.Object)}. If either string is null, false is returned. If both parameters
	 * are null, true is returned.
	 * 
	 * @param str1 the first value
	 * @param str2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final String str1, final String str2)
	{
		if (str1 == str2)
			return true;
		else if (str1 == null)
			return false;
		else if (str2 == null)
			return false;
		
		return str1.equals(str2);
	}
	
	/**
	 * Compares two strings using {@link java.lang.String#equalsIgnoreCase(java.lang.String)} if <code>caseSensitive</code> is true and
	 * {@link java.lang.String#equals(java.lang.Object)} if <code>caseSensitive</code> is false
	 * If either string is null, false is returned. If both parameters
	 * are null, true is returned.
	 * 
	 * @param str1 the first value
	 * @param str2 the second value
	 * @param caseSensitive
	 * @return true if they are equal and false if they are not.
	 */
	public static final boolean equals(final String str1, final String str2, final boolean caseSensitive)
	{
		if (str1 == str2)
			return true;
		else if (str1 == null)
			return false;
		else if (str2 == null)
			return false;
		
		if (caseSensitive)
			return str1.equals(str2);
		else
			return str1.equalsIgnoreCase(str2);	
	}
	
	/**
	 * Compares two {@link java.util.Date} objects. If both parameters
	 * are null, true is returned. Otherwise, {@link java.util.Date#equals(java.lang.Object)} is used.
	 * @param date1 the first value
	 * @param date2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final Date date1, final Date date2)
	{
		if (Dates.isNull(date1) && Dates.isNull(date2))
			return true;
		else if (Dates.isNull(date1))
			return false;
		else if (Dates.isNull(date2))
			return false;
		
		return date1.equals(date2);
	}
	
	/**
	 * Copmpares two objects using {@link #equals(Object, Object, boolean)} with false as the third parameter.
	 * @param obj1 the first value
	 * @param obj2 the second value
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final Object obj1, final Object obj2)
	{
		return equals(obj1, obj2, false);
	}

	/**
	 * Compares two objects. If both are strings, they are compared in a case sensitive manner according to the value of the <code>caseSenstive</code>
	 * parameter. If both objects are of the same class but they are NOT strings, then the <code>caseSenstive</code> parameter is ignored
	 * and the two objects are compared using {@link java.lang.Object#equals(java.lang.Object)}. If the objects are of different classes,
	 * then they are each converted to a String. Both strings are then
	 * compared using {@link #equals(String, String, boolean)}. 
	 *  
	 * @param obj1 the first value
	 * @param obj2 the second value
	 * @param caseSensitive true to perform a case sensitive comparison. False to perform a case -nsenstive comparison.
	 * @return true if they are equal and false if they are not.
	 */
	public static boolean equals(final Object obj1, final Object obj2, final boolean caseSensitive)
	{
		if (obj1 == obj2)
			return true;
		else if (obj1 == null)
			return false;
		else if (obj2 == null)
			return false;
		
		if (obj1.getClass() == obj2.getClass())
		{
			if (obj1 instanceof String)
			{
				final String str1 = (String)obj1;
				final String str2 = (String)obj2;
				if (caseSensitive)
					return str1.equals(str2);
				else
					return str1.equalsIgnoreCase(str2);
			} else
				return obj1.equals(obj2);
		}
		else
		{
			final String str1 = Conversions.toString(obj1);
			final String str2 = Conversions.toString(obj2);
			return equals(str1, str2, caseSensitive);
		}
	}

	/**
	 * Compares two values
	 * @param d1 the first value
	 * @param d2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static final int compare(final double d1, final double d2)
	{
		if (d1 == d2)
			return 0;
		else if (d1 < d2)
			return -1;
		else
			return 1;
	}
	
	/**
	 * Compares two values
	 * @param l1 the first value
	 * @param l2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final long l1, final long l2)
	{
		if (l1 == l2)
			return 0;
		else if (l1 < l2)
			return -1;
		else
			return 1;
	}

	/**
	 * Compares two values
	 * @param b1 the first value
	 * @param b2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final boolean b1, final boolean b2)
	{
		if (b1 == b2)
			return 0;
		else if (b1 == false)
			return -1;
		else
			return 1;
	}

	/**
	 * Compares two byte arrays by calling {@link #compare(byte[], int, byte[], int)} using zero as the offset for both arrays.
	 * 
	 * @param b1 the first value
	 * @param b2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final byte [] b1, final byte [] b2)
	{
		return compare(b1, 0, b2, 0);
	}
	
	/**
	 * Compares two arrays starting at a given offset. The corresponding values in both arrays are compared. Once two unequal values are found, the result of that
	 * single comparison is used as the return value of the method. If the first array is a subset of the second, -1 is returned. If the second array is a subset of the
	 * first, 1 is returned.  
	 * 
	 * @param b1 the first value
	 * @param offset1 the starting offset of the first value
	 * @param b2 the second value
	 * @param offset2 the starting offset of the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final byte [] b1, final int offset1, final byte [] b2, final int offset2)
	{
		if (b1 == null && b2 == null)
			return 0;
		else if (b1 == b2 && offset1 == offset2)
			return 0;
		else if (b1 == null)
			return -1;
		else if (b2 == null)
			return 1;
		
		final int size = Math.min(b1.length, b2.length);
		int i1 = offset1;
		int i2 = offset2;
		for (int i = 0; i < size; i++, i1++, i2++)
		{
			final byte byte1 = b1[i1];
			final byte byte2 = b2[i2];
			
			if (byte1 < byte2)
				return -1;
			else if (byte1 > byte2)
				return 1;
		}
		
		return compare(b1.length, b2.length);
	}
	
	/**
	 * Compares two strings in a case sensitive manner
	 * @param str1 the first value
	 * @param str2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final String str1, final String str2)
	{
		if (str1 == null)
			return -1;
		else if (str2 == null)
			return 1;
		
		return str1.compareTo(str2);
	}
	
	/**
	 * Compares two strings in either a case sensitive or insensitive manner according to the value of the <code>caseSensitive</code> parameter.
	 * @param str1 the first value
	 * @param str2 the second value
	 * @param caseSensitive
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(String str1, String str2, final boolean caseSensitive)
	{
		if (str1 == null)
			return -1;
		else if (str2 == null)
			return 1;

		if (caseSensitive)
			return str1.compareTo(str2);
		else
		{
			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();
			return str1.compareTo(str2);
		}
	}
	
	/**
	 * Compares two {@link java.util.Date} objects.
	 * 
	 * @param date1 the first value
	 * @param date2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final Date date1, final Date date2)
	{
		if (date1 == date2)
			return 0;
		else if (date1 == null)
			return -1;
		else if (date2 == null)
			return 1;
		
		return compare(date1.getTime(), date2.getTime());
	}
	
	/**
	 * Compares two objects by calling {@link #compare(Object, Object, boolean)}.
	 * 
	 * @param obj1 the first value
	 * @param obj2 the second value
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final Object obj1, final Object obj2)
	{
		return compare(obj1, obj2, false);
	}

	/**
	 * Compares two objects. If both are strings, they are compared in a case sensitive manner according to the value of the <code>caseSenstive</code>
	 * parameter. If both objects are of the same class but they are NOT strings, then the <code>caseSenstive</code> parameter is ignored
	 * and the two objects are compared using {@link java.lang.Object#equals(java.lang.Object)}. If the objects are of different classes,
	 * then they are each converted to a String. Both strings are then
	 * compared using {@link #equals(String, String, boolean)}. 
	 *  
	 * @param obj1 the first value
	 * @param obj2 the second value
	 * @param caseSensitive true to perform a case sensitive comparison. False to perform a case -nsenstive comparison.
	 * @return -1 if the first value is less than the second value. 0 if both values are equal. 1 if the first value is greater than the second value.
	 */
	public static int compare(final Object obj1, final Object obj2, final boolean caseSensitive)
	{
		if (obj1 == obj2)
			return 0;
		else if (obj1 == null)
			return -1;
		else if (obj2 == null)
			return 1;
		
		if (obj1.getClass() == obj2.getClass())
		{
			if (obj1 instanceof String)
			{
				final String str1 = (String)obj1;
				final String str2 = (String)obj2;
				return compare(str1, str2, caseSensitive);
			}
			else if (obj1 instanceof Number)
			{
				final Number n1 = (Number)obj1;
				final Number n2 = (Number)obj2;
				return compare(n1.doubleValue(), n2.doubleValue());
			}
		}
		else
		{
			final String str1 = Conversions.toString(obj1);
			final String str2 = Conversions.toString(obj2);
			return compare(str1, str2, caseSensitive);
		}
		return 0;
	}
	
}

class ComparisionsComparator implements Comparator
{
	public int compare(final Object arg0, final Object arg1)
	{
		return Comparisons.compare(arg0, arg1);
	}
}
