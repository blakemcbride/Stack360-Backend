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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.misc.vendor;
import com.arahant.business.BVendorCompany;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class VendorList  {


	public VendorList() {
		super();
	}

	private String vendorId;
	private String vendorName;
	private String vendorIdentifier;
	private String vendorPhone;
	private String vendorContactLastName;
	private String vendorContactFirstName;
	private String vendorContactPhone;
	/**
	 * @param company
	 */
	VendorList(final BVendorCompany v) {
		super();
		vendorId=v.getOrgGroupId();
		vendorName=v.getName();
		vendorIdentifier=v.getIdentifier();
		vendorPhone=v.getMainPhoneNumber();
		vendorContactFirstName=v.getMainContactFname();
		vendorContactLastName=v.getMainContactLname();
		vendorContactPhone=v.getMainContactWorkPhone();
	}
	/**
	 * @return Returns the vendorContactFirstName.
	 */
	public String getVendorContactFirstName() {
		return vendorContactFirstName;
	}
	/**
	 * @param vendorContactFirstName The vendorContactFirstName to set.
	 */
	public void setVendorContactFirstName(final String vendorContactFirstName) {
		this.vendorContactFirstName = vendorContactFirstName;
	}
	/**
	 * @return Returns the vendorContactLastName.
	 */
	public String getVendorContactLastName() {
		return vendorContactLastName;
	}
	/**
	 * @param vendorContactLastName The vendorContactLastName to set.
	 */
	public void setVendorContactLastName(final String vendorContactLastName) {
		this.vendorContactLastName = vendorContactLastName;
	}
	/**
	 * @return Returns the vendorContactPhone.
	 */
	public String getVendorContactPhone() {
		return vendorContactPhone;
	}
	/**
	 * @param vendorContactPhone The vendorContactPhone to set.
	 */
	public void setVendorContactPhone(final String vendorContactPhone) {
		this.vendorContactPhone = vendorContactPhone;
	}
	/**
	 * @return Returns the vendorId.
	 */
	public String getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId The vendorId to set.
	 */
	public void setVendorId(final String vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return Returns the vendorIdentifier.
	 */
	public String getVendorIdentifier() {
		return vendorIdentifier;
	}
	/**
	 * @param vendorIdentifier The vendorIdentifier to set.
	 */
	public void setVendorIdentifier(final String vendorIdentifier) {
		this.vendorIdentifier = vendorIdentifier;
	}
	/**
	 * @return Returns the vendorName.
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName The vendorName to set.
	 */
	public void setVendorName(final String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return Returns the vendorPhone.
	 */
	public String getVendorPhone() {
		return vendorPhone;
	}
	/**
	 * @param vendorPhone The vendorPhone to set.
	 */
	public void setVendorPhone(final String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	
}

	
