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


package com.arahant.services.standard.hr.eeo1Survey;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREEO1;
import com.arahant.utils.DateUtils;

public class NewSurveyInput extends TransmitInputBase {

	@Validation (table="hr_eeo1",column="beg_period",required=true,type="date")
	private int startDate;
	@Validation (table="hr_eeo1",column="end_period",required=true,type="date")
	private int finalDate;
	@Validation (table="hr_eeo1",column="common_ownership",required=true)
	private boolean commonOwnership;
	@Validation (table="hr_eeo1",column="gov_contractor",required=true)
	private boolean governmentContractor;
    @Validation (type="string",required=true,min=1,max=35)
	private String certifierTitle;
    @Validation (type="string",required=true,min=1,max=35)
    private String certifierName;
    @Validation (type="string",required=true,min=1,max=10)
    private String certifierPhone;
    @Validation (type="string",required=true,min=1,max=40)
    private String certifierEmail;
    @Validation (required=false, type="array")
	private NewSurveyInputEstablishment[] establishments;

    public NewSurveyInputEstablishment[] getEstablishments() {
        return establishments;
    }

    public void setEstablishments(NewSurveyInputEstablishment[] establishments) {
        this.establishments = establishments;
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
	public boolean getCommonOwnership()
	{
		return commonOwnership;
	}
	public void setCommonOwnership(boolean commonOwnership)
	{
		this.commonOwnership=commonOwnership;
	}
	public boolean getGovernmentContractor()
	{
		return governmentContractor;
	}
	public void setGovernmentContractor(boolean governmentContractor)
	{
		this.governmentContractor=governmentContractor;
	}

    public String getCertifierEmail() {
        return certifierEmail;
    }

    public void setCertifierEmail(String certifierEmail) {
        this.certifierEmail = certifierEmail;
    }

    public String getCertifierName() {
        return certifierName;
    }

    public void setCertifierName(String certifierName) {
        this.certifierName = certifierName;
    }

    public String getCertifierPhone() {
        return certifierPhone;
    }

    public void setCertifierPhone(String certifierPhone) {
        this.certifierPhone = certifierPhone;
    }

    public String getCertifierTitle() {
        return certifierTitle;
    }

    public void setCertifierTitle(String certifierTitle) {
        this.certifierTitle = certifierTitle;
    }


	void setData(BHREEO1 eeo1)
	{

        eeo1.setCommonOwnership(commonOwnership);
        eeo1.setCreatedDate(DateUtils.now());
        eeo1.setGovernmentContractor(governmentContractor);
        eeo1.setPayPeriodFinalDate(finalDate);
        eeo1.setPayPeriodStartDate(startDate);
        eeo1.setTransmittedData(BHREEO1.generateReportData(startDate, finalDate, governmentContractor, commonOwnership, certifierTitle, certifierName, certifierPhone, certifierEmail, establishments));

	}
}

	
