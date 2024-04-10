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
package com.arahant.services.standard.inventory.quote;

import com.arahant.beans.QuoteProduct;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BQuoteTable;
import java.util.List;


public class LoadQuoteReturn extends TransmitReturnBase {

	private String description;
	private String name;
	private String clientId;
	private String locationId;
	private Boolean finalized;
	private double markupPercent;
	private double adjustmentsTotalCost;
	private double additionalCost;

	private QuoteProductReturnItem[] item;

	public double getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}

	public double getMarkupPercent() {
		return markupPercent;
	}

	public void setMarkupPercent(double markupPercent) {
		this.markupPercent = markupPercent;
	}

	public Boolean getFinalized() {
		return finalized;
	}

	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getLocationId()
	{
		return locationId;
	}
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}

	public double getAdjustmentsTotalCost() {
		return adjustmentsTotalCost;
	}

	public void setAdjustmentsTotalCost(double adjustmentsTotalCost) {
		this.adjustmentsTotalCost = adjustmentsTotalCost;
	}

	public QuoteProductReturnItem[] getItem() {
		return item;
	}

	public void setItem(QuoteProductReturnItem[] item) {
		this.item = item;
	}

	void setItem(final List<QuoteProduct> a) {
		item=new QuoteProductReturnItem[a.size()];
		int loop = 0;
		for (QuoteProduct i : a)
			item[loop++]=new QuoteProductReturnItem(i);
	}

	void setData(BQuoteTable bc)
	{
		description = bc.getQuoteDescription();
		name = bc.getQuoteName();
		clientId = bc.getClient().getOrgGroupId();
		if(bc.getLocationCost() != null)
			locationId = bc.getLocationCost().getLocationCostId();
		finalized = bc.getFinalizedDate() > 0;
		markupPercent = bc.getMarkupPercent();
		additionalCost = bc.getAdditionalCost();
		setItem(bc.getQuoteProducts());
	}
	
}

	
