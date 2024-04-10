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

import com.arahant.business.BQuoteTable;


public class SearchQuotesReturnItem {

	private String quoteId;
	private String name;
	private String description;
	private int createdDate;
	private int finalizedDate;
	private double totalCost;
	private String client;
	private String location;
	private String clientId;
	private String locationId;
	

	public String getQuoteId()
	{
		return quoteId;
	}
	public void setQuoteId(String quoteId)
	{
		this.quoteId = quoteId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(int createdDate)
	{
		this.createdDate = createdDate;
	}
	public int getFinalizedDate()
	{
		return finalizedDate;
	}
	public void setFinalizedDate(int finalizedDate)
	{
		this.finalizedDate = finalizedDate;
	}
	public double getTotalCost()
	{
		return totalCost;
	}
	public void setTotalCost(double totalCost)
	{
		this.totalCost = totalCost;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public SearchQuotesReturnItem() {
		
	}

	SearchQuotesReturnItem(BQuoteTable bc)
	{
		quoteId = bc.getQuoteId();
		name = bc.getQuoteName();
		description = bc.getQuoteDescription();
		createdDate = bc.getCreatedDate();
		finalizedDate = bc.getFinalizedDate();
		totalCost = 0;
		client = bc.getClient().getName();
		location = bc.getLocationCost().getDescription();
		clientId = bc.getClient().getOrgGroupId();
		locationId = bc.getLocationCost().getLocationCostId();
	}
	
}

	
