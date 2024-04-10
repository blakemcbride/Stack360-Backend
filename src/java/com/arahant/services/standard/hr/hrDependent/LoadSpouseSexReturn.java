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
package com.arahant.services.standard.hr.hrDependent;

import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadSpouseSexReturn extends TransmitReturnBase {

	void setData(final BEmployee bc)
	{
		
		defaultSex=bc.getSpouseDefaultSex();

	}
	
	private String defaultSex;

	public String getDefaultSex()
	{
		return defaultSex;
	}
	public void setDefaultSex(final String defaultSex)
	{
		this.defaultSex=defaultSex;
	}

}

	
