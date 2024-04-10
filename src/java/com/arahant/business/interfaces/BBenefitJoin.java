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
 * Created on Oct 3, 2007
 * 
 */
package com.arahant.business.interfaces;

import com.arahant.exceptions.ArahantException;


/**
 * 
 *
 * Created on Oct 3, 2007
 *
 */
public interface BBenefitJoin {
	/**
	 * @return
	 */
	public double getAmountCovered() ;

	/**
	 * @return
	 */
	public int getPolicyEndDate() ;

	/**
	 * @return
	 */
	public boolean getEnrolled();

	/**
	 * @return
	 */
	public String getCoveredFirstName();

	/**
	 * @return
	 */
	public String getCoveredLastName() ;

	/**
	 * @return
	 */
	public String getCoveredMiddleName() ;

	/**
	 * @return
	 */
	public String getCoveredPersonId() ;

	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String getRelationshipText() throws ArahantException ;
	/**
	 * @return
	 */
	public String getCoveredSsn();

	/**
	 * @return
	 */
	public int getPolicyStartDate() ;

	/**
	 * @return
	 */
	public boolean getIsEmployeeSpouse();

	/**
	 * @return
	 */
	public boolean getDependentActive();

	/**
	 * @return
	 */
	public int getCoveredEndDate();

	/**
	 * @return
	 */
	public int getCoveredStartDate();

	/**
	 * 
	 */
	public String getPayingPersonId();


}

	
