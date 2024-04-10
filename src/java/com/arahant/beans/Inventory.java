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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 */
@Entity
@Table(name=Inventory.TABLE_NAME)
public class Inventory extends ArahantBean implements Serializable {
	public static final String TABLE_NAME="inventory";

	public static final String LOCATION="location";
	public static final String PRODUCT="product";
	public static final String DETAIL_COUNT="detailCount";
	public static final String REORDER_LEVEL="reorderLevel";
	public static final String TOTAL_REMAINING="totalRemaining";

	private String inventoryId;
	private Product product;
	private OrgGroup location;
	private int reorderLevel;



	@Id
	@Column(name="inventory_id")
	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id")
	public OrgGroup getLocation() {
		return location;
	}

	public void setLocation(OrgGroup location) {
		this.location = location;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name="reorder_level")
	public int getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}


	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "inventory_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return inventoryId=IDGenerator.generate(this);
	}


}
