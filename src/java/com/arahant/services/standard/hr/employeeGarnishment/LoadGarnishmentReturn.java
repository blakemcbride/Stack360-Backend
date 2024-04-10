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
package com.arahant.services.standard.hr.employeeGarnishment;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BGarnishment;


/**
 * 
 *
 *
 */
public class LoadGarnishmentReturn extends TransmitReturnBase {

	void setData(BGarnishment bc)
	{
		
		issueState=bc.getIssueState();
		fipsCode=bc.getFipsCode();
		collectingState=bc.getCollectingState();
		remitToAddressLine1=bc.getRemitToStreet();
		remitToAddressLine2=bc.getRemitToStreet2();
		remitToCity=bc.getRemitToCity();
		remitToStateProvince=bc.getRemitToState();
		remitToZipPostalCode=bc.getRemitToZip();
		remitToCounty=bc.getRemitToCounty();
		remitToCountry=bc.getRemitToCountry();
		remitToName=bc.getRemitToName();

	}
	
	private String issueState;
	private String fipsCode;
	private String collectingState;
	private String remitToAddressLine1;
	private String remitToAddressLine2;
	private String remitToCity;
	private String remitToStateProvince;
	private String remitToZipPostalCode;
	private String remitToCounty;
	private String remitToName;
	private String remitToCountry;

	public String getIssueState()
	{
		return issueState;
	}
	public void setIssueState(String issueState)
	{
		this.issueState=issueState;
	}
	public String getFipsCode()
	{
		return fipsCode;
	}
	public void setFipsCode(String fipsCode)
	{
		this.fipsCode=fipsCode;
	}
	public String getCollectingState()
	{
		return collectingState;
	}
	public void setCollectingState(String collectingState)
	{
		this.collectingState=collectingState;
	}
	
	public String getRemitToCity()
	{
		return remitToCity;
	}
	public void setRemitToCity(String remitToCity)
	{
		this.remitToCity=remitToCity;
	}

	public String getRemitToAddressLine1() {
		return remitToAddressLine1;
	}

	public void setRemitToAddressLine1(String remitToAddressLine1) {
		this.remitToAddressLine1 = remitToAddressLine1;
	}

	public String getRemitToAddressLine2() {
		return remitToAddressLine2;
	}

	public void setRemitToAddressLine2(String remitToAddressLine2) {
		this.remitToAddressLine2 = remitToAddressLine2;
	}

	public String getRemitToCountry() {
		return remitToCountry;
	}

	public void setRemitToCountry(String remitToCountry) {
		this.remitToCountry = remitToCountry;
	}

	public String getRemitToStateProvince() {
		return remitToStateProvince;
	}

	public void setRemitToStateProvince(String remitToStateProvince) {
		this.remitToStateProvince = remitToStateProvince;
	}

	public String getRemitToZipPostalCode() {
		return remitToZipPostalCode;
	}

	public void setRemitToZipPostalCode(String remitToZipPostalCode) {
		this.remitToZipPostalCode = remitToZipPostalCode;
	}
	
	public String getRemitToCounty()
	{
		return remitToCounty;
	}
	public void setRemitToCounty(String remitToCounty)
	{
		this.remitToCounty=remitToCounty;
	}
	public String getRemitToName()
	{
		return remitToName;
	}
	public void setRemitToName(String remitToName)
	{
		this.remitToName=remitToName;
	}

}

	
