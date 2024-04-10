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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.business.BCompanyBase;

public class SearchCompanyByTypeReturnItem {
  
	private String orgGroupId;
	private String name;
	private int orgGroupType;
	private String orgGroupTypeName;
	private String identifier;
	private String mainPhone;
	private String mainFax;
	private String federalEmployerId;

	public SearchCompanyByTypeReturnItem() {
	}

	public SearchCompanyByTypeReturnItem(final BCompanyBase c) {
		if (c == null)
			return;
		orgGroupId = c.getOrgGroupId();
		name = c.getName();
		orgGroupTypeName = c.getOrgType();
		identifier = c.getIdentifier();
		mainPhone = c.getMainPhoneNumber();
		mainFax = c.getMainFaxNumber();
		federalEmployerId = c.getFederalEmployerId();
		orgGroupType = c.getOrgGroupType();
	}

//		 Property accessors
		/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getCompanyId()
	 */
	public String getCompanyId() {
		return this.orgGroupId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setCompanyId(java.lang.String)
	 */
	public void setCompanyId(final String companyId) {
		this.orgGroupId = companyId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getName()
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setName(java.lang.String)
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getOrgGroupType()
	 */
	public int getOrgGroupType() {
		return orgGroupType;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setOrgGroupType(int)
	 */
	public void setOrgGroupType(final int orgGroupType) {
		this.orgGroupType = orgGroupType;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getOrgGroupTypeName()
	 */
	public String getOrgGroupTypeName() {
		return orgGroupTypeName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setOrgGroupTypeName(java.lang.String)
	 */
	public void setOrgGroupTypeName(final String orgGroupTypeName) {
		this.orgGroupTypeName = orgGroupTypeName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getIdentifier()
	 */
	public String getIdentifier() {
		return identifier;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setIdentifier(java.lang.String)
	 */
	public void setIdentifier(final String accsysId) {
		this.identifier = accsysId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getMainFax()
	 */
	public String getMainFax() {
		return mainFax;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setMainFax(com.arahant.operations.transmit.PhoneTransmit)
	 */
	public void setMainFax(final String mainFax) {
		this.mainFax = mainFax;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getMainPhone()
	 */
	public String getMainPhone() {
		return mainPhone;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setMainPhone(com.arahant.operations.transmit.PhoneTransmit)
	 */
	public void setMainPhone(final String mainPhone) {
		this.mainPhone = mainPhone;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#getFederalEmployerId()
	 */
	public String getFederalEmployerId() {
		return federalEmployerId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ICompanyTransmit#setFederalEmployerId(java.lang.String)
	 */
	public void setFederalEmployerId(final String federalEmployerId) {
		this.federalEmployerId = federalEmployerId;
	}
}
