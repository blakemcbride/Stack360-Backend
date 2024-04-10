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
package com.arahant.services.standard.hr.employeeProfile;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;

public class SaveEmployeeInput extends TransmitInputBase {

	void setData(BPerson bc)
	{
		
		bc.setFirstName(firstName);
		bc.setMiddleName(middleName);
		bc.setLastName(lastName);
		bc.setNickName(nickName);
		bc.setSsn(ssn);
		bc.setPersonalEmail(email);
		bc.setSex(sex);
		bc.setStreetPending(address1);
		bc.setStreet2Pending(address2);
		bc.setCityPending(city);
		bc.setZipPending(zip);
		bc.setCountyPending(county);
		bc.setHomePhonePending(homePhone);
		bc.setWorkPhonePending(workPhone);
		bc.setMobilePhonePending(mobilePhone);
		bc.setWorkFaxPending(fax);
		bc.setDob(dob);
	

	}

	@Validation (required=true)
	private String employeeId;
	@Validation(table = "person", column = "fname", required = true)
	private String firstName;
	@Validation(table = "person", column = "mname", required = false)
	private String middleName;
	@Validation(table = "person", column = "lname", required = true)
	private String lastName;
	@Validation(table = "person", column = "nickname", required = false)
	private String nickName;
	@Validation(table = "employee", column = "ssn", required = false)
	private String ssn;
	@Validation(table = "employee", column = "ext_ref", required = false)
	private String externalId;
	@Validation(table = "person", column = "personal_email", required = false)
	private String email;
	@Validation(table = "employee", column = "sex", required = false)
	private String sex;
	@Validation(table = "address", column = "street", required = false)
	private String address1;
	@Validation(table = "address", column = "street2", required = false)
	private String address2;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(table = "address", column = "zip", required = false)
	private String zip;
	@Validation(table = "address", column = "county", required = false)
	private String county;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String homePhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String workPhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mobilePhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String fax;
	@Validation (type="date", required=false)
	private int dob;
	

	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(String employeeId)
	{
		this.employeeId=employeeId;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(String middleName)
	{
		this.middleName=middleName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName=nickName;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(String ssn)
	{
		this.ssn=ssn;
	}
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId=externalId;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex=sex;
	}
	public String getAddress1()
	{
		return address1;
	}
	public void setAddress1(String address1)
	{
		this.address1=address1;
	}
	public String getAddress2()
	{
		return address2;
	}
	public void setAddress2(String address2)
	{
		this.address2=address2;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city=city;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip=zip;
	}
	public String getCounty()
	{
		return county;
	}
	public void setCounty(String county)
	{
		this.county=county;
	}
	public String getHomePhone()
	{
		return homePhone;
	}
	public void setHomePhone(String homePhone)
	{
		this.homePhone=homePhone;
	}
	public String getWorkPhone()
	{
		return workPhone;
	}
	public void setWorkPhone(String workPhone)
	{
		this.workPhone=workPhone;
	}
	public String getMobilePhone()
	{
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone=mobilePhone;
	}
	public String getFax()
	{
		return fax;
	}
	public void setFax(String fax)
	{
		this.fax=fax;
	}
	public int getDob()
	{
		return dob;
	}
	public void setDob(int dob)
	{
		this.dob=dob;
	}

	public String toXML()
    {
       String ret="";

       ret+=tag("PERSONCHANGEDATA",
            tag("employeeId", DOMUtils.escapeText(employeeId))+
            tag("firstName", DOMUtils.escapeText(firstName))+
            tag("middleName", DOMUtils.escapeText(middleName))+
            tag("lastName", DOMUtils.escapeText(lastName))+
            tag("nickName", DOMUtils.escapeText(nickName))+
            tag("ssn", DOMUtils.escapeText(ssn))+
            tag("email", DOMUtils.escapeText(email))+
            tag("sex", DOMUtils.escapeText(sex))+
            tag("address1", DOMUtils.escapeText(address1))+
            tag("address2", DOMUtils.escapeText(address2))+
            tag("city", DOMUtils.escapeText(city))+
            tag("zip", DOMUtils.escapeText(zip))+
            tag("county", DOMUtils.escapeText(county))+
            tag("homePhone", DOMUtils.escapeText(homePhone))+
            tag("workPhone", DOMUtils.escapeText(workPhone))+
            tag("mobilePhone", DOMUtils.escapeText(mobilePhone))+
            tag("fax", DOMUtils.escapeText(fax))+
            tag("dob", dob)
            );

       return ret;
    }

	@Override
	public String toString()
    {
       String ret="";


		ret+="First Name:"+firstName+"\n";
		ret+="Middle Name:"+middleName+"\n";
		ret+="Last Name:"+lastName+"\n";
		ret+="Nick Name:"+nickName+"\n";
		ret+="SSN:"+ssn+"\n";
		ret+="Email:"+email+"\n";
		ret+="Gender:"+sex+"\n";
		ret+="Street 1:"+address1+"\n";
		ret+="Street 2:"+address2+"\n";
		ret+="City:"+city+"\n";
		ret+="Zip:"+zip+"\n";
		ret+="County:"+county+"\n";
		ret+="Home Phone:"+homePhone+"\n";
		ret+="Work Phone:"+workPhone+"\n";
		ret+="Mobile Phone:"+mobilePhone+"\n";
		ret+="Fax:"+fax+"\n";
		ret+="DOB"+ DateUtils.getDateFormatted(dob)+"\n";


       return ret;
    }

    public String tag(String tagName, String value)
    {
        return "<"+tagName+">"+value+"</"+tagName+">\n";
    }
    public String tag(String tagName, int value)
    {
        return "<"+tagName+">"+value+"</"+tagName+">\n";
    }

}

	
