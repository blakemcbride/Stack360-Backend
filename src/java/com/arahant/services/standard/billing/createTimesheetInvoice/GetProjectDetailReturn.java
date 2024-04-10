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

package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.services.TransmitReturnBase;

public class GetProjectDetailReturn extends TransmitReturnBase {

	private boolean found;
	private String parentProjectName;
	private double estimatedHours;
	private double invoicedHours;
	private double remainingEstimatedHours;
	private double remainingEstimatedRate;
	private double remainingEstimatedAmount;

	// If project-based billing
	private double fixedPrice;
	private double invoicedAmount;  // amount previously invoiced

	public double getRemainingEstimatedAmount() {
		return remainingEstimatedAmount;
	}

	public void setRemainingEstimatedAmount(double remainingEstimatedAmount) {
		this.remainingEstimatedAmount = remainingEstimatedAmount;
	}

	public double getRemainingEstimatedRate() {
		return remainingEstimatedRate;
	}

	public void setRemainingEstimatedRate(double remainingEstimatedRate) {
		this.remainingEstimatedRate = remainingEstimatedRate;
	}

	public boolean getFound()
	{
		return found;
	}

	public void setFound(boolean found)
	{
		this.found=found;
	}

	public String getParentProjectName()
	{
		return parentProjectName;
	}

	public void setParentProjectName(String parentProjectName)
	{
		this.parentProjectName=parentProjectName;
	}

	public double getEstimatedHours()
	{
		return estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours)
	{
		this.estimatedHours=estimatedHours;
	}

	public double getInvoicedHours()
	{
		return invoicedHours;
	}

	public void setInvoicedHours(double invoicedHours)
	{
		this.invoicedHours=invoicedHours;
	}

	public double getRemainingEstimatedHours()
	{
		return remainingEstimatedHours;
	}

	public void setRemainingEstimatedHours(double remainingEstimatedHours)
	{
		this.remainingEstimatedHours=remainingEstimatedHours;
	}

	public double getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(double fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public double getInvoicedAmount() {
		return invoicedAmount;
	}

	public void setInvoicedAmount(double invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
	}
}

	
