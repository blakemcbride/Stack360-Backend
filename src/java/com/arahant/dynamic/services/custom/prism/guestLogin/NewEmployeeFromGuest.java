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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.dynamic.services.custom.prism.guestLogin;

import com.arahant.annotation.DynamicValidation;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BProperty;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

/**
 *
 */
public class NewEmployeeFromGuest {

	@DynamicValidation(required = false, max = 20, min = 0)
    public String homePhone;
    @DynamicValidation(required = false, max = 20, min = 0)
    public String mobilePhone;
    @DynamicValidation(required = false, max = 60, min = 0)
    public String personalEmail;
    @DynamicValidation(required = true, max = 30, min = 1)
    public String lname;
    @DynamicValidation(required = true, max = 30, min = 1)
    public String fname;
    @DynamicValidation(required = true, max = 50, min = 1)
    public String userName;
    @DynamicValidation(required = true, max = 22, min = 1)
    public String userPassword;
    @DynamicValidation(required = true, max = 11, min = 9)
    public String ssn;
    @DynamicValidation(required = true, type = "date")
    public int dob;
    @DynamicValidation(required = true, max = 60, min = 1)
    public String street1;
    @DynamicValidation(required = false, max = 60, min = 0)
    public String street2;
    @DynamicValidation(required = true, max = 60, min = 1)
    public String city;
    @DynamicValidation(required = true, max = 25, min = 1)
    public String state;
    @DynamicValidation(required = true, max = 10, min = 5)
    public String zip;

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		BEmployee be=new BEmployee(hsu);
		be.create();

        be.setPersonalEmail(in.getString("personalEmail"));
        be.setLastName(in.getString("lastName"));
        be.setFirstName(in.getString("firstName"));
        be.setUserLogin(in.getString("userName"));
        be.setUserPassword(in.getString("userPassword"), true);
        be.setScreenGroupId(BProperty.get("DefaultScreenGroupId"));
        be.setSecurityGroupId(BProperty.get("DefaultSecurityGroupId"));
        be.setSsn(in.getString("ssn"));
        be.setHomePhone(in.getString("homePhone"));
        be.setMobilePhone(in.getString("mobilePhone"));
        be.setCompanyId(hsu.getCurrentCompany().getOrgGroupId());
        be.setDob(in.getInt("dob"));
        be.setStreet(in.getString("street1"));
        be.setStreet2(in.getString("street2"));
        be.setCity(in.getString("city"));
        be.setState(in.getString("state"));
        be.setZip(in.getString("zip"));
		be.setStatusId(BHREmployeeStatus.findOrMake("Active", true), DateUtils.now());
		be.insert();
	}
}
