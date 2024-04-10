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

package com.arahant.services.standard.crm.prospectParent;

import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
import org.kissweb.database.Record;

import java.sql.SQLException;

public class SearchProspectsReturnItem {
	
	private String id;
	private String name;
	private String identifier;
	private String status;
	private String prospectType;
	private int statusDate;
	private int firstContactDate;
	private int lastContactDate;
	private int nextContactDate;
	private String mainContactFirstName;
	private String mainContactLastName;
	private String source;
	private String street1;
	private String street2;
	private String country;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String fax;
	private int firstContact;
	private int certainty;
	private String salesPerson;
	private String companyPhone;
	private String companyFax;
	private String mainContactPhone;
	private String otherContactPhone;
	private String mainContactName;
	private String otherContactName;
	private String otherContactEmail;
	private String email;
	private short timeZone;
	private int lastLogDate;
	private double opportunity;
	private double weightedOpportunity;
	private int whenAdded;

	public SearchProspectsReturnItem()
	{
	}

	SearchProspectsReturnItem(final Record rec) throws SQLException {
		id = rec.getString("org_group_id");
		name = rec.getString("group_name");
		/*  problem bellow this line  */
		identifier = rec.getString("external_id");
		status = rec.getString("project_status_code");
		prospectType = rec.getString("type_code");
		firstContactDate = nullToZero(rec.getInt("first_contact_date"));
		mainContactFirstName = rec.getString("fname");
		mainContactLastName = rec.getString("lname");
		source = rec.getString("source_code");
		street1 = rec.getString("street");
		street2 = rec.getString("street2");
		city = rec.getString("city");
		state = rec.getString("state");
		zip = rec.getString("zip");
		country = rec.getString("country_code");
		source = rec.getString("source_code");
		firstContact = nullToZero(rec.getInt("first_contact_date"));
		certainty = nullToZero(rec.getShort("certainty"));
		opportunity = rec.getDouble("opportunity_value");
		weightedOpportunity = opportunity * (double) certainty / 100.0;
		String mname = rec.getString("mname");
		String name = rec.getString("lname") + ", " + rec.getString("fname");
		if (mname != null && !mname.isEmpty())
			name += " " + mname;
		salesPerson = name;
		timeZone = nullToZero(rec.getShort("time_zone_offset"));
		statusDate = nullToZero(rec.getDateAsInt("status_change_date"));
		lastContactDate = nullToZero(rec.getInt("last_contact_date"));
		nextContactDate = nullToZero(rec.getInt("next_contact_date"));
		lastLogDate = nullToZero(rec.getInt("last_log_date"));
		whenAdded = rec.getDateAsInt("when_added");

//		final BPerson mc = bc.getMainContact();
//		email = mc == null ? "" : mc.getPersonalEmail();
		/*  problem bellow this line  */
//		phone = mc == null ? "" : mc.getWorkPhone();
//		fax = mc == null ? "" : mc.getWorkFaxNumber();
//		companyPhone = bc.getMainPhoneNumber();
//		companyFax = bc.getMainFaxNumber();
//		mainContactPhone = mc == null ? "" : mc.getWorkPhone();
//		if (!StringUtils.isEmpty(mainContactLastName) && !StringUtils.isEmpty(mainContactFirstName))
//			mainContactName = mainContactLastName + ", " + mainContactFirstName;
//		BPerson[] bp = new BOrgGroup(bc.getOrgGroupId()).listPeople2(1);
//		if (bp.length > 0) {
//			otherContactName = StringUtils.isEmpty(bp[0].getNameLFM()) ? "" : bp[0].getNameLFM();
//			otherContactPhone = StringUtils.isEmpty(bp[0].getWorkPhone()) ? "" : bp[0].getWorkPhone();
//			otherContactEmail = StringUtils.isEmpty(bp[0].getPersonalEmail()) ? "" : bp[0].getPersonalEmail();
//		}

	}

	private static int nullToZero(Integer i) {
		return i == null ? 0 : i;
	}

	private static short nullToZero(Short i) {
		return i == null ? 0 : i;
	}

	SearchProspectsReturnItem(final BProspectCompany bc) {
		id = bc.getId();
		name = bc.getName();
		/*  problem bellow this line  */
		identifier = bc.getIdentifier();
		status = bc.getStatusCode();
		prospectType = bc.getTypeCode();
		firstContactDate = bc.getFirstContactDate();
		final BPerson mc = bc.getMainContact();
		mainContactFirstName = mc == null ? "" : mc.getFirstName();
		mainContactLastName = mc == null ? "" : mc.getLastName();
		email = mc == null ? "" : mc.getPersonalEmail();
		source = bc.getSourceCode();
		street1 = bc.getStreet();
		street2 = bc.getStreet2();
		country = bc.getCountry();
		city = bc.getCity();
		state = bc.getState();
		zip = bc.getZip();
		/*  problem bellow this line  */
		phone = mc == null ? "" : mc.getWorkPhone();
		fax = mc == null ? "" : mc.getWorkFaxNumber();
		source = bc.getSourceCode();
		firstContact = bc.getFirstContactDate();
		certainty = bc.getCertainty();
		if (certainty == 0)
			certainty = bc.getStatus().getCertainty();
		opportunity = bc.getOpportunityValue();
		weightedOpportunity = opportunity * (double) certainty / 100.0;
		salesPerson = bc.getSalesPerson().getNameLFM();
		companyPhone = bc.getMainPhoneNumber();
		companyFax = bc.getMainFaxNumber();
		mainContactPhone = mc == null ? "" : mc.getWorkPhone();
		timeZone = bc.getTimeZone();
		if (!StringUtils.isEmpty(mainContactLastName) && !StringUtils.isEmpty(mainContactFirstName))
			mainContactName = mainContactLastName + ", " + mainContactFirstName;
		BPerson[] bp = new BOrgGroup(bc.getOrgGroupId()).listPeople2(1);
		if (bp.length > 0) {
			otherContactName = StringUtils.isEmpty(bp[0].getNameLFM()) ? "" : bp[0].getNameLFM();
			otherContactPhone = StringUtils.isEmpty(bp[0].getWorkPhone()) ? "" : bp[0].getWorkPhone();
			otherContactEmail = StringUtils.isEmpty(bp[0].getPersonalEmail()) ? "" : bp[0].getPersonalEmail();
		}
		statusDate = DateUtils.getDate(bc.getStatusChangeDate());
		lastContactDate = bc.getLastContactDate();
		nextContactDate = bc.getNextContactDate();
		lastLogDate = bc.getLastLogDate();
	}
	
	public int getLastLogDate() {
		return lastLogDate;
	}

	public void setLastLogDate(int lastLogDate) {
		this.lastLogDate = lastLogDate;
	}

	public int getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(int lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	public int getNextContactDate() {
		return nextContactDate;
	}

	public void setNextContactDate(int nextContactDate) {
		this.nextContactDate = nextContactDate;
	}

	public int getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(int statusDate) {
		this.statusDate = statusDate;
	}

	public short getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(short timeZone) {
		this.timeZone = timeZone;
	}

	public String getOtherContactEmail() {
		return otherContactEmail;
	}

	public void setOtherContactEmail(String otherContactEmail) {
		this.otherContactEmail = otherContactEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getCertainty() {
		return certainty;
	}

	public void setCertainty(int certainty) {
		this.certainty = certainty;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public int getFirstContact() {
		return firstContact;
	}

	public void setFirstContact(int firstContact) {
		this.firstContact = firstContact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id=id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	public void setIdentifier(String identifier)
	{
		this.identifier=identifier;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status=status;
	}
	
	public int getFirstContactDate()
	{
		return firstContactDate;
	}
	
	public void setFirstContactDate(int firstContactDate)
	{
		this.firstContactDate=firstContactDate;
	}
	
	public String getMainContactFirstName()
	{
		return mainContactFirstName;
	}
	
	public void setMainContactFirstName(String mainContactFirstName)
	{
		this.mainContactFirstName=mainContactFirstName;
	}
	
	public String getMainContactLastName()
	{
		return mainContactLastName;
	}
	
	public void setMainContactLastName(String mainContactLastName)
	{
		this.mainContactLastName=mainContactLastName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getMainContactName() {
		return mainContactName;
	}

	public void setMainContactName(String mainContactName) {
		this.mainContactName = mainContactName;
	}

	public String getMainContactPhone() {
		return mainContactPhone;
	}

	public void setMainContactPhone(String mainContactPhone) {
		this.mainContactPhone = mainContactPhone;
	}

	public String getOtherContactName() {
		return otherContactName;
	}

	public void setOtherContactName(String otherContactName) {
		this.otherContactName = otherContactName;
	}

	public String getOtherContactPhone() {
		return otherContactPhone;
	}

	public void setOtherContactPhone(String otherContactPhone) {
		this.otherContactPhone = otherContactPhone;
	}

	public String getProspectType() {
		return prospectType;
	}

	public void setProspectType(String prospectType) {
		this.prospectType = prospectType;
	}

	public double getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(double opportunity) {
		this.opportunity = opportunity;
	}

	public double getWeightedOpportunity() {
		return weightedOpportunity;
	}

	public void setWeightedOpportunity(double weightedOpportunity) {
		this.weightedOpportunity = weightedOpportunity;
	}

	public int getWhenAdded() {
		return whenAdded;
	}

	public void setWhenAdded(int whenAdded) {
		this.whenAdded = whenAdded;
	}
}

	
