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

import com.arahant.business.BHREEO1;

public class ListSurveysReturnItem {
	
	public ListSurveysReturnItem()
	{
		;
	}

	ListSurveysReturnItem (BHREEO1 bc)
	{

        id = bc.getId();
		creationDate = bc.getCreatedDate();
		startPayPeriodDate = bc.getPayPeriodStartDate();
		finalPayPeriodDate = bc.getPayPeriodFinalDate();
		commonOwnership = bc.getCommonOwnership();
		governmentContractor = bc.getGovernmentContractor();
		uploadedDate = bc.getUploadedDate();

	}

    private String id;
	private int creationDate;
	private int startPayPeriodDate;
	private int finalPayPeriodDate;
	private boolean commonOwnership;
	private boolean governmentContractor;
	private int uploadedDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	public int getCreationDate()
	{
		return creationDate;
	}
	public void setCreationDate(int creationDate)
	{
		this.creationDate=creationDate;
	}
	public int getStartPayPeriodDate()
	{
		return startPayPeriodDate;
	}
	public void setStartPayPeriodDate(int startPayPeriodDate)
	{
		this.startPayPeriodDate=startPayPeriodDate;
	}
	public int getFinalPayPeriodDate()
	{
		return finalPayPeriodDate;
	}
	public void setFinalPayPeriodDate(int finalPayPeriodDate)
	{
		this.finalPayPeriodDate=finalPayPeriodDate;
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
	public int getUploadedDate()
	{
		return uploadedDate;
	}
	public void setUploadedDate(int uploadedDate)
	{
		this.uploadedDate=uploadedDate;
	}

}

	
