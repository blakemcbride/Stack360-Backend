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
package com.arahant.services.peachtreeApp;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class LoginReturn extends TransmitReturnBase {

	public LoginReturn() {
		super();
	}
	
	//private String personId;
	private String personFName;
	private String personLName;
	private String screen;
	//private String screenTitle;
	
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#getPersonFName()
	 */
	public String getPersonFName() {
		return personFName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#setPersonFName(java.lang.String)
	 */
	public void setPersonFName(final String personFName) {
		this.personFName = personFName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#getPersonId()
	 *
	public String getPersonId() {
		return personId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#setPerson(java.lang.String)
	 *
	public void setPerson(final String personId) {
		this.personId = personId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#getPersonLName()
	 */
	public String getPersonLName() {
		return personLName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#setPersonLName(java.lang.String)
	 */
	public void setPersonLName(final String personLName) {
		this.personLName = personLName;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#getScreen()
	 */
	public String getScreen() {
		return screen;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#setScreen(java.lang.String)
	 */
	public void setScreen(final String screen) {
		this.screen = screen;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#getScreenTitle()
	 *
	public String getScreenTitle() {
		return screenTitle;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ILoginResultTransmit#setScreenTitle(java.lang.String)
	 *
	public void setScreenTitle(final String screenTitle) {
		this.screenTitle = screenTitle;
	}
	*/
}

	
