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



package com.arahant.services.standard.misc.utils;

import com.arahant.services.TransmitInputBase;

/**
 *
 * @author Blake McBride
 */
public class WebUtilInput extends TransmitInputBase {
	private static final String authCode2 = "iunwr8734o*G&^FJKBKU&F&iubUYgu7gturY%S34769(hp9uhu*7yf75f7fig78656546548tfiuvi76r7f934587(*&^&%$JHV";
	private String authCode;
	private int funCode;
	private int intArg1;

	public boolean isValid() {
		return authCode2.equals(authCode);
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public int getFunCode() {
		return funCode;
	}

	public void setFunCode(int funCode) {
		this.funCode = funCode;
	}

	public int getIntArg1() {
		return intArg1;
	}

	public void setIntArg1(int intArg1) {
		this.intArg1 = intArg1;
	}


}
