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
 * Created on Feb 20, 2007
 * 
 */
package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;

/**
 * 
 *
 * Created on Feb 20, 2007
 *
 */
public class LoadBillingReturn extends TransmitReturnBase {

	public LoadBillingReturn() {
		super();
	}

	private float billingRate;
	private String defaultBillingRateFormatted;


	/**
	 * @param bp
	 */
	void setData(final BProject bp) {

		billingRate = bp.getBillingRate();

		defaultBillingRateFormatted = bp.getDefaultBillingRateFormatted();

	}

	public String getDefaultBillingRateFormatted() {
		return defaultBillingRateFormatted;
	}

	public void setDefaultBillingRateFormatted(String defaultBillingRateFormatted) {
		this.defaultBillingRateFormatted = defaultBillingRateFormatted;
	}
	

	/**
	 * @return Returns the billingRate.
	 */
	public float getBillingRate() {
		return billingRate;
	}

	/**
	 * @param billingRate The billingRate to set.
	 */
	public void setBillingRate(final float billingRate) {
		this.billingRate = billingRate;
	}
    
}

	
