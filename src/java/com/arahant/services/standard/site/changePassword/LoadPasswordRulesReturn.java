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
 */
package com.arahant.services.standard.site.changePassword;

import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class LoadPasswordRulesReturn extends TransmitReturnBase {

	
	private int minimumLength=BProperty.getInt("PasswordMinimumLength");
	private int minimumLetters=BProperty.getInt("PasswordMinimumLetters");
	private int minimumUpperCaseLetters=BProperty.getInt("PasswordMinimumUpperCase");
	private int minimumLowerCaseLetters=BProperty.getInt("PasswordMinimumLowerCase");
	private int minimumDigits=BProperty.getInt("PasswordMinimumDigits");
	private int minimumSpecialChars=BProperty.getInt("PasswordMinimumSpecialChars");
	

	public int getMinimumLength()
	{
		return minimumLength;
	}
	public void setMinimumLength(int minimumLength)
	{
		this.minimumLength=minimumLength;
	}
	public int getMinimumLetters()
	{
		return minimumLetters;
	}
	public void setMinimumLetters(int minimumLetters)
	{
		this.minimumLetters=minimumLetters;
	}
	public int getMinimumUpperCaseLetters()
	{
		return minimumUpperCaseLetters;
	}
	public void setMinimumUpperCaseLetters(int minimumUpperCaseLetters)
	{
		this.minimumUpperCaseLetters=minimumUpperCaseLetters;
	}
	public int getMinimumLowerCaseLetters()
	{
		return minimumLowerCaseLetters;
	}
	public void setMinimumLowerCaseLetters(int minimumLowerCaseLetters)
	{
		this.minimumLowerCaseLetters=minimumLowerCaseLetters;
	}
	public int getMinimumDigits()
	{
		return minimumDigits;
	}
	public void setMinimumDigits(int minimumDigits)
	{
		this.minimumDigits=minimumDigits;
	}
	public int getMinimumSpecialChars()
	{
		return minimumSpecialChars;
	}
	public void setMinimumSpecialChars(int minimumSpecialChars)
	{
		this.minimumSpecialChars=minimumSpecialChars;
	}

}

	
