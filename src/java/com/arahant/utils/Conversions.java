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
 * Created on Jul 28, 2004
 *
 * 
 * 
 */
package com.arahant.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * A standard and reusable method for converting data from one datatype to another. 
 * The data types that can be converted are
 * 
 * <ul>
 * 		<li>boolean</li>
 * 		<li>byte</li>
 * 		<li>byte []</li>
 * 		<li>char</li>
 * 		<li>short</li>
 * 		<li>int</li>
 * 		<li>long</li>
 * 		<li>float</li>
 * 		<li>double</li>
 * 		<li>{@link java.lang.String String}</li>
 * 		<li>{@link java.util.Date Date}</li>
 * 		<li>{@link java.lang.Object Object}</li>
 * </ul>
 *
 * Using this class, you can convert from any to any of the supported datatypes. The methods that deal with {@link java.lang.Object Object}
 * also support the wrapper classes such as {@link java.lang.Boolean Boolean}, {@link java.lang.Short Short}, {@link java.lang.Number Number}, etc.
 * 
 * Arahant
 * 
 */
public class Conversions
{
	/**
	 * Rounds a double to the nearest decimal place. While the {@link java.lang.Math Math} class provides a flexible
	 * set of rounding methods, they do not handle the most common rounding operation in the simplest possible way.
	 * This method provides a simple way to round a double to the nearest decimal place.
	 *    
	 * @param value the target value
	 * @param decimalPlaces the number of decimal places to round to.
	 * @return the rounded number
	 */
	public static final double round(final double value, final int decimalPlaces)
	{
		double factor = Math.pow(10, decimalPlaces);
		long roundedValue = (long)(value * factor);
		
		return roundedValue / factor;
		//return (value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
	}

	/**
	 * Converts an object to a boolean. If the object is a numeric value, the result is value != 0. If the object is
	 * a String, true is returned if the value is a case insensitive match for "t", "y", "true" or "yes". 
	 * @param value the target object
	 * @return the converted value
	 */
	public static final boolean toBoolean(final Object value)
	{
		if (value == null)
			return false;
		else if (value instanceof Number)
			return ((Number)value).doubleValue() != 0;
		else if (value instanceof Boolean)
			return ((Boolean)value).booleanValue();
		else if (value instanceof String)
		{
			final String strValue = (String)value;
			if (strValue == null || strValue.length() == 0 || strValue.trim().length() == 0)
				return false;
			
			if (Comparisons.equals(strValue, "t", false) ||
							Comparisons.equals(strValue, "y", false) ||
							Comparisons.equals(strValue, "true", false) ||
							Comparisons.equals(strValue, "yes", false))
				return true;
			else
			{
				try
				{
					final int iValue = Integer.parseInt(strValue);
					return iValue != 0;
				}
				catch (final NumberFormatException e)
				{
				}

				return false;
			}
		}
		else 
			return false;
	}




	/**
	 * Converts a boolean value to a byte. If the value is true, 1 is returned, otherwise 0 is returned. 
	 * @param value the target value
	 * @return the converted value.
	 */
	public static final byte toByte(final boolean value)
	{
		return (byte)(value ? 1 : 0);
	}

	/**
	 * Converts an object to a byte. If the object is a String, the value is assumed to be a string representation of a numeric value
	 * which is then converted to a byte value.
	 * @param value the target value
	 * @return the converted value.
	 */
	public static final byte toByte(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).byteValue();
		else if (value instanceof String)
		{
			final String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			
			return Byte.parseByte(Formatting.stripNumericFormatting((String)value));
		}
		else if (value instanceof Boolean)
			return toByte(((Boolean)value).booleanValue());
		else 
			return 0;
	}

	/**
	 * Converts a Object to a {@link java.util.Date}. Depending upon the actual datatype of the <code>obj</code> parameter,
	 * a different method is used. If the <code>obj</code> parameter is a {@link java.util.Date}, it is returned directly.
	 * If the <code>obj</code> parameter is a {@link java.lang.Number} then the <code>obj</code> parameter is converted to
	 * a long and that value is used as a time value to construct the return value. If the <code>obj</code> parameter is
	 * a String, then it must match the DATETIME_PATTERN pattern.
	 * If none of the above types is matched, null is returned. 
	 *  
	 * @param obj the target object
	 * @return the converted Date
	 */
	public static final Date toDate(final Object obj)
	{
		if (obj instanceof Date)
			return (Date)obj;
		else if (obj instanceof Number)
			return new Date(((Number)obj).longValue());
		else if (obj instanceof String)
		{
			final String str = (String)obj;

			try
			{
				if (!(str == null || str.length() == 0 || str.trim().length() == 0))
					return Formatting.DATETIME.parse(str);
				
				return null;
			}
			catch (final ParseException e)
			{
				try
				{
					return Formatting.DATE.parse(str);
				}
				catch (final ParseException e1)
				{
					try
					{
						return Formatting.DATE_DASH.parse(str);
					}
					catch (final ParseException e2)
					{
						return null;
					}
				}
			}
		} else
			return null;
	}


    public static final Date toDate(final String str)
	{
        try
        {
            return DateFormat.getDateTimeInstance().parse(str);
        }
        catch (Exception e)
        {
            return null;
        }
	}

	/**
	 * Converts an object to a double. If the <code>value</code> parameter is null, 0 is returned. If the <code>value</code> parameter
	 * is an instance of a {@link java.lang.Number} class, then the value returned is the result of {@link java.lang.Number#doubleValue()}.
	 * If the <code>value</code> parameter is a {@link java.lang.Boolean}, 0 or 1 is returned. If the <code>value</code> parameter
	 * is a {@link java.util.Date}, the result of {@link java.util.Date#getTime()} is returned. If none of the above tests passes, 0 is returned.
	 *     
	 * @param value the target object
	 * @return the converted value.
	 */
	public static final double toDouble(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).doubleValue();
		else if (value instanceof String)
		{
			final String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			
			return Double.parseDouble(Formatting.stripNumericFormatting((String)value));
		}
		else if (value instanceof Boolean)
			return toDouble(((Boolean)value).booleanValue());
		else if (value instanceof Date)
			return ((Date)value).getTime();
		else 
			return 0;
	}

	/**
	 * Converts a boolean value to a double. If the <code>value</code> parameter is true, 1 is returned. Otherwise, 0 is returned.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final double toDouble(final boolean value)
	{
		return value ? 1 : 0;
	}



	/**
	 * Converts an object to a float. If the <code>value</code> parameter is null, 0 is returned. If the <code>value</code> parameter
	 * is an instance of a {@link java.lang.Number} class, then the value returned is the result of {@link java.lang.Number#floatValue()}.
	 * If the <code>value</code> parameter is a {@link java.lang.Boolean}, 0 or 1 is returned. If the <code>value</code> parameter
	 * is a {@link java.util.Date}, the result of {@link java.util.Date#getTime()} is returned. If none of the above tests passes, 0 is returned.
	 *     
	 * @param value the target object
	 * @return the converted value.
	 */
	public static final float toFloat(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).floatValue();
		else if (value instanceof String)
		{
			String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			str=Formatting.stripNumericFormatting((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			return Float.parseFloat(str);
		}
		else if (value instanceof Boolean)
			return toFloat(((Boolean)value).booleanValue());
		else if (value instanceof Date)
			return ((Date)value).getTime();
		else 
			return 0;
	}
	
	/**
	 * Converts a boolean value to a float. If the <code>value</code> parameter is true, 1 is returned. Otherwise, 0 is returned.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final float toFloat(final boolean value)
	{
		return value ? 1 : 0;
	}
	
	/**
	 * Converts an array of bytes to a float. The array must be at least 4 bytes long starting from the <code>offset</code> value.
	 * 
	 * @param bytes the target array
	 * @param offset the starting offset for the conversion
	 * @return the converted value.
	 */
	public static final float toFloat(final byte [] bytes, final int offset)
	{
		final int i = toInt(bytes, offset);
		return Float.intBitsToFloat(i);
	}
	
	/**
	 * Converts an object to an int. If the <code>value</code> parameter is null, 0 is returned. If the <code>value</code> parameter
	 * is an instance of a {@link java.lang.Number} class, then the value returned is the result of {@link java.lang.Number#intValue()}.
	 * If the <code>value</code> parameter is a {@link java.lang.Boolean}, 0 or 1 is returned. If none of the above tests passes, 0 is returned.
	 *     
	 * @param value the target object
	 * @return the converted value.
	 */
	public static final int toInt(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).intValue();
		else if (value instanceof String)
		{
			final String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			
			return Integer.parseInt(Formatting.stripNumericFormatting((String)value));
		}
		else if (value instanceof Boolean)
			return toInt(((Boolean)value).booleanValue());
		else 
			return 0;
	}
	
	/**
	 * Converts a boolean value to an int. If the <code>value</code> parameter is true, 1 is returned. Otherwise, 0 is returned.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final int toInt(final boolean value)
	{
		return value ? 1 : 0;
	}

	/**
	 * Converts an array of bytes to an int. The array must be at least 4 bytes long starting from the <code>offset</code> value.
	 * 
	 * @param bytes the target array
	 * @param offset the starting offset for the conversion
	 * @return the converted value.
	 */
	public static final int toInt(final byte [] bytes, final int offset)
	{
		int tmp = 0;
		
		tmp += bytes[offset]     << 24 & 0xFF000000;
		tmp += bytes[offset + 1] << 16 & 0x00FF0000;
		tmp += bytes[offset + 2] << 8  & 0x0000FF00;
		tmp += bytes[offset + 3]       & 0x000000FF;
		
		return tmp;
	}
	
	/**
	 * Converts an object to a long. If the <code>value</code> parameter is null, 0 is returned. If the <code>value</code> parameter
	 * is an instance of a {@link java.lang.Number} class, then the value returned is the result of {@link java.lang.Number#longValue()}.
	 * If the <code>value</code> parameter is a {@link java.lang.Boolean}, 0 or 1 is returned. If the <code>value</code> parameter
	 * is a {@link java.util.Date}, the result of {@link java.util.Date#getTime()} is returned. If none of the above tests passes, 0 is returned.
	 *     
	 * @param value the target object
	 * @return the converted value.
	 */
	public static final long toLong(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).longValue();
		else if (value instanceof String)
		{
			final String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			
			return Long.parseLong(Formatting.stripNumericFormatting((String)value));
		}
		else if (value instanceof Boolean)
			return toLong(((Boolean)value).booleanValue());
		else if (value instanceof Date)
			return ((Date)value).getTime();
		else 
			return 0;
	}
	
	/**
	 * Converts a boolean value to a long. If the <code>value</code> parameter is true, 1 is returned. Otherwise, 0 is returned.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final long toLong(final boolean value)
	{
		return value ? 1 : 0;
	}
	
	/**
	 * Converts an array of bytes to a long. The array must be at least 8 bytes long starting from the <code>offset</code> value.
	 * 
	 * @param bytes the target array
	 * @param offset the starting offset for the conversion
	 * @return the converted value.
	 */
	public static final long toLong(final byte [] bytes, final int offset)
	{
		long tmp = 0;
		
		tmp += (long)bytes[offset]     << 56 & 0xFF00000000000000L;
		tmp += (long)bytes[offset + 1] << 48 & 0x00FF000000000000L;
		tmp += (long)bytes[offset + 2] << 40 & 0x0000FF0000000000L;
		tmp += (long)bytes[offset + 3] << 32 & 0x000000FF00000000L;
		tmp += (long)bytes[offset + 4] << 24 & 0x00000000FF000000L;
		tmp += (long)bytes[offset + 5] << 16 & 0x0000000000FF0000L;
		tmp += (long)bytes[offset + 6] << 8  & 0x000000000000FF00L;
		tmp +=       bytes[offset + 7]       & 0x00000000000000FFL;
		
		return tmp;
	}
	
	/**
	 * Converts an object to a short. If the <code>value</code> parameter is null, 0 is returned. If the <code>value</code> parameter
	 * is an instance of a {@link java.lang.Number} class, then the value returned is the result of {@link java.lang.Number#shortValue()}.
	 * If the <code>value</code> parameter is a {@link java.lang.Boolean}, 0 or 1 is returned. If none of the above tests passes, 0 is returned.
	 *     
	 * @param value the target object
	 * @return the converted value.
	 */
	public static final short toShort(final Object value)
	{
		if (value == null)
			return 0;
		else if (value instanceof Number)
			return ((Number)value).shortValue();
		else if (value instanceof String)
		{
			final String str = ((String)value);
			if (str == null || str.length() == 0 || str.trim().length() == 0)
				return 0;
			
			return Short.parseShort(Formatting.stripNumericFormatting((String)value));
		}
		else if (value instanceof Boolean)
			return toShort(((Boolean)value).booleanValue());
		else 
			return 0;
	}
	
	/**
	 * Converts a boolean value to a short. If the <code>value</code> parameter is true, 1 is returned. Otherwise, 0 is returned.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final short toShort(final boolean value)
	{
		return (short)(value ? 1 : 0);
	}
	
	/**
	 * Converts an array of bytes to a short. The array must be at least 2 bytes long starting from the <code>offset</code> value.
	 * 
	 * @param bytes the target array
	 * @param offset the starting offset for the conversion
	 * @return the converted value.
	 */
	public static final short toShort(final byte [] bytes, final int offset)
	{
		short value = 0;
		
		value += bytes[offset]		<< 8	& 0xFF00;
		value += bytes[offset + 1]  		& 0x00FF;
		
		return value;
	}

	/**
	 * Converts an object to a string. If the <code>value</code> parameter is null, an empty string is returned. If the object is an instance of
	 * {@link java.util.Date Date}, it is converted using {@link #toString(Date)}. Otherwise the result of <code>value.toString()</code> is returned.
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(Object)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toString(final Object value)
	{
		if (value == null)
			return "";
		else if (value instanceof Date)
			return toString((Date)value);
		
		return value.toString();
	}
	
	/**
	 * Converts a byte array to a string. The byte array must be an array of character codes. 
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(byte[], int, int)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 * 
	 * @param bytes The target byte array
	 * @param offset the offset to start the conversion
	 * @param size the number of bytes to convert
	 * @return the converted value.
	 */
	public static final String toString(final byte [] bytes, final int offset, final int size)
	{
		return new String(bytes, offset, size).trim();
	}
	
	/**
	 * Converts a long to a string using the {@link java.lang.String#valueOf(long)} method.
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(long)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toString(final long value)
	{
		return String.valueOf(value);
	}
	
	/**
	 * Converts a double to a string using the {@link java.lang.String#valueOf(double)} method.
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(double)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toString(final double value)
	{
		return String.valueOf(value);
	}
	
	/**
	 * Converts a {@link java.util.Date Date} to a string using the {@link com.integra.util.Formatting#formatDate(Date, String)} method and applying the
	 * {@link com.integra.util.Formatting#DATE_PATTERN} pattern.
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(Date)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 * 
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toString(final Date value)
	{
		return Formatting.formatDate(value, Formatting.DATE_PATTERN);
	}

	/**
	 * Converts a boolean value to a string. If the value is true, "true" is returned. If the value is false, "false" is returned. 
	 * 
	 * Do not use this method from Javascript. Instead use {@link #toStr(boolean)} instead. This is because the toString() method is special in Javascript
	 * and the overloading of the method name as is done in this class does not work correctly in Javascript. The toStr() methods are simple wrappers
	 * around the toString() methods.
	 * 
	 * @param value the target value
	 * @return the converted value.
	 */
	public static final String toString(final boolean value)
	{
		return (value ? "true" : "false");
	}
	
	/**
	 * This method is designed to provide Javascript access to the {@link #toString(Object)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toStr(final Object value)
	{
		return toString(value);
	}

	/**
	 * This method is designed to provide Javascript access to the {@link #toString(byte[], int, int)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param bytes the target array
	 * @param offset the starting offset
	 * @param size the number of bytes to use
	 * @return the converted value
	 */
	public static final String toStr(final byte [] bytes, final int offset, final int size)
	{
		return toString(bytes, offset, size);
	}
	
	/**
	 * This method is designed to provide Javascript access to the {@link #toString(long)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toStr(final long value)
	{
		return toString(value);
	}
	
	/**
	 * This method is designed to provide Javascript access to the {@link #toString(double)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toStr(final double value)
	{
		return toString(value);
	}
	
	/**
	 * This method is designed to provide Javascript access to the {@link #toString(java.util.Date)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toStr(final Date value)
	{
		return toString(value);
	}
	
	/**
	 * This method is designed to provide Javascript access to the {@link #toString(boolean)} method. Since toString() is a special method in Javascript, using the
	 * static overloaded toString() methods of this class results in problems. Instead use the toStr() methods from Javascript.
	 *  
	 * @param value the target value
	 * @return the converted value
	 */
	public static final String toStr(final boolean value)
	{
		return toString(value);
	}
}
