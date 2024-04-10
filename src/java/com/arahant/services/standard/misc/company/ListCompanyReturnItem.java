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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.misc.company;
import com.arahant.business.BCompany;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class ListCompanyReturnItem   {
	
	private String companyId;
	private String companyName;
	private String address;
	private String phone;
	private String fax;


	public ListCompanyReturnItem()
	{

	}


	/**
	 * @param company
	 */
	public ListCompanyReturnItem(final BCompany company) {
		super();
		companyId=company.getOrgGroupId();
		companyName=company.getName();
		address=company.getStreet();
        if (company.getStreet2().length() > 0) {
            if (address.length() > 0) {
                address += " ";
            }
            address += company.getStreet2();
        }
        if (company.getCity().length() > 0) {
            if (address.length() > 0) {
                address += ", ";
            }
            address += company.getCity();
        }
        if (company.getState().length() > 0) {
            if (address.length() > 0) {
                address += ", ";
            }
            address += company.getState();
        }
        if (company.getZip().length() > 0) {
            if (address.length() > 0) {
                address += ", ";
            }
            address += company.getZip();
        }
        if (company.getCountry().length() > 0) {
            if (address.length() > 0) {
                address += ", ";
            }
            address += company.getCountry();
        }
		phone=company.getMainPhoneNumber();
		fax=company.getMainFaxNumber();

	}
	/**
	 * @return Returns the companyId.
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId The companyId to set.
	 */
	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName The companyName to set.
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax The fax to set.
	 */
	public void setFax(final String fax) {
		this.fax = fax;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

	
