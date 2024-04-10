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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BGarnishment;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveGarnishmentInput extends TransmitInputBase {

	void setData(BGarnishment bc)
	{
		
		bc.setType(deductType);
		bc.setMaxAmount(maxAmount,amountType);
		bc.setAmount(amount,amountType);
		bc.setStartDate(startDate);
		bc.setFinalDate(finalDate);
		bc.setDocketNumber(docketNumber);
		bc.setIssueState(issueState);
		bc.setFipsCode(fipsCode);
		bc.setCollectingState(collectingState);
		bc.setRemitToStreet(remitToAddressLine1);
		bc.setRemitToStreet2(remitToAddressLine2);
		bc.setRemitToCity(remitToCity);
		bc.setRemitToState(remitToStateProvince);
		bc.setRemitToZip(remitToZipPostalCode);
		bc.setRemitToCounty(remitToCounty);
		bc.setRemitToCountry(remitToCountry);
		bc.setRemitToName(remitToName);
		bc.setGarnishmentType(typeId);
	}
	
	@Validation (required=false)
	private String amountType;
	@Validation (required=false)
	private String deductType;
	@Validation (table="garnishment",column="max_dollars",required=false)
	private double maxAmount;
	@Validation (table="garnishment",column="deduction_amount",required=false)
	private double amount;
	@Validation (table="garnishment",column="start_date",required=false,type="date")
	private int startDate;
	@Validation (table="garnishment",column="start_date",required=false,type="date")
	private int finalDate;
	@Validation (table="garnishment",column="docket_number",required=false)
	private String docketNumber;
	@Validation (table="garnishment",column="issue_state",required=false)
	private String issueState;
	@Validation (table="garnishment",column="fips_code",required=false)
	private String fipsCode;
	@Validation (table="garnishment",column="collecting_state",required=false)
	private String collectingState;
	@Validation (table="address",column="street",required=false)
	private String remitToAddressLine1;
	@Validation (table="address",column="street2",required=false)
	private String remitToAddressLine2;
	@Validation (table="address",column="city",required=false)
	private String remitToCity;
	@Validation (table="address",column="state",required=false)
	private String remitToStateProvince;
	@Validation (table="address",column="zip",required=false)
	private String remitToZipPostalCode;
	@Validation (table="address",column="country_code",required=false)
	private String remitToCountry;
	@Validation (table="address",column="county",required=false)
	private String remitToCounty;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false)
	private String remitToName;
	@Validation (required=true)
	private String typeId;

	public String getAmountType()
	{
		return amountType;
	}
	public void setAmountType(String amountType)
	{
		this.amountType=amountType;
	}

	public String getDeductType() {
		return deductType;
	}

	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public double getMaxAmount()
	{
		return maxAmount;
	}
	public void setMaxAmount(double maxAmount)
	{
		this.maxAmount=maxAmount;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount=amount;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public String getDocketNumber()
	{
		return docketNumber;
	}
	public void setDocketNumber(String docketNumber)
	{
		this.docketNumber=docketNumber;
	}
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
	
	public String getRemitToCounty()
	{
		return remitToCounty;
	}
	public void setRemitToCounty(String remitToCounty)
	{
		this.remitToCounty=remitToCounty;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
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

	public String getRemitToName() {
		return remitToName;
	}

	public void setRemitToName(String remitToName) {
		this.remitToName = remitToName;
	}


}

	
