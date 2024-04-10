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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.misc.employeeHomePage;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 8, 007
 *
 */
public class CreateMessageReturn extends TransmitReturnBase  {


	public CreateMessageReturn() {
		super();
	}

	/**
	 * @param string
	 */
	public CreateMessageReturn(final String string) {
		super(string);
	}
	
	String id;


	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IIdTransmit#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IIdTransmit#setId(java.lang.String)
	 */
	public void setId(final String id) {
		this.id = id;
	}
}

	
