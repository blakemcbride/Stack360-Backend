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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "route_stop_checklist")
public class RouteStopChecklist extends ArahantBean implements Serializable {

	public static final String PRIORITY = "itemPriority";
	public static final String ROUTE_STOP = "routeStop";
	public static final String ID = "routeStopChecklistId";
	public static final String START_DATE = "dateActive";
	public static final String END_DATE = "dateInactive";
	private String routeStopChecklistId;
	private RouteStop routeStop;
	private short itemPriority;
	private String itemDescription;
	private String itemDetail;
	private char itemRequired;
	private int dateActive;
	private int dateInactive;

	public RouteStopChecklist() {
	}

	@Column(name = "date_active")
	public int getDateActive() {
		return dateActive;
	}

	public void setDateActive(int dateActive) {
		this.dateActive = dateActive;
	}

	@Column(name = "date_inactive")
	public int getDateInactive() {
		return dateInactive;
	}

	public void setDateInactive(int dateInactive) {
		this.dateInactive = dateInactive;
	}

	@Column(name = "item_description")
	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	@Column(name = "item_detail")
	public String getItemDetail() {
		return itemDetail;
	}

	public void setItemDetail(String itemDetail) {
		this.itemDetail = itemDetail;
	}

	@Column(name = "item_priority")
	public short getItemPriority() {
		return itemPriority;
	}

	public void setItemPriority(short itemPriority) {
		this.itemPriority = itemPriority;
	}

	@Column(name = "item_required")
	public char getItemRequired() {
		return itemRequired;
	}

	public void setItemRequired(char itemRequired) {
		this.itemRequired = itemRequired;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_stop_id")
	public RouteStop getRouteStop() {
		return routeStop;
	}

	public void setRouteStop(RouteStop routeStop) {
		this.routeStop = routeStop;
	}

	@Id
	@Column(name = "route_stop_checklist_id")
	public String getRouteStopChecklistId() {
		return routeStopChecklistId;
	}

	public void setRouteStopChecklistId(String routeStopChecklistId) {
		this.routeStopChecklistId = routeStopChecklistId;
	}

	@Override
	public String tableName() {
		return "route_stop_checklist";
	}

	@Override
	public String keyColumn() {
		return "route_stop_checklist_id";
	}

	@Override
	public String generateId() throws ArahantException {
		routeStopChecklistId = IDGenerator.generate(this);
		return routeStopChecklistId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RouteStopChecklist other = (RouteStopChecklist) obj;
		if (this.routeStopChecklistId != other.getRouteStopChecklistId() && (this.routeStopChecklistId == null || !this.routeStopChecklistId.equals(other.getRouteStopChecklistId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + (this.routeStopChecklistId != null ? this.routeStopChecklistId.hashCode() : 0);
		return hash;
	}
}
