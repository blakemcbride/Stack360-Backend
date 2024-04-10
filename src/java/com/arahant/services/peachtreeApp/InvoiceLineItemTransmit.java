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
 * Created on Oct 13, 2006
 * 
 */
package com.arahant.services.peachtreeApp;

import com.arahant.business.BInvoiceLineItem;



/**
 * 
 *
 * Created on Oct 13, 2006
 *
 */
public class InvoiceLineItemTransmit  {

	 private String itemId;
	 private String description;
	 private double origHours;
	 private double origRate;
	 private double adjHours;
	 private double adjRate;
	 private String glAccountId;

	 public InvoiceLineItemTransmit()
	 {
		 ;
	 }
	/**
	 * @param item
	 */
	InvoiceLineItemTransmit(final BInvoiceLineItem item) {
		super();
		itemId=item.getAcctSysInvoiceLineItemId();
		description=item.getDescription();
		adjHours=item.getAdjHours();
		adjRate=item.getAdjRate();
		glAccountId=item.getAccSysGLId();
		origHours=item.getOrigHours();
		origRate=item.getOrigRate();
	}

	/**
	 * @return Returns the adjHours.
	 */
	public double getAdjHours() {
		return adjHours;
	}

	/**
	 * @param adjHours The adjHours to set.
	 */
	public void setAdjHours(final double adjHours) {
		this.adjHours = adjHours;
	}

	/**
	 * @return Returns the adjRate.
	 */
	public double getAdjRate() {
		return adjRate;
	}

	/**
	 * @param adjRate The adjRate to set.
	 */
	public void setAdjRate(final double adjRate) {
		this.adjRate = adjRate;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the glAccountId.
	 */
	public String getGlAccountId() {
		return glAccountId;
	}

	/**
	 * @param glAccountId The glAccountId to set.
	 */
	public void setGlAccountId(final String glAccountId) {
		this.glAccountId = glAccountId;
	}

	/**
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(final String itemId) {
		this.itemId = itemId;
	}


	/**
	 * @return Returns the origHours.
	 */
	public double getOrigHours() {
		return origHours;
	}

	/**
	 * @param origHours The origHours to set.
	 */
	public void setOrigHours(final double origHours) {
		this.origHours = origHours;
	}
	/**
	 * @return Returns the origRate.
	 */
	public double getOrigRate() {
		return origRate;
	}
	/**
	 * @param origRate The origRate to set.
	 */
	public void setOrigRate(final double origRate) {
		this.origRate = origRate;
	}


}

	
