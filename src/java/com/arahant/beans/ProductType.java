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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 */
@Entity
@Table(name=ProductType.TABLE_NAME)
public class ProductType extends ArahantBean implements Serializable, ProductTypeChild {
	public static final String TABLE_NAME="product_type";

	public static final String PRODUCT_TYPE_ID="productTypeId";
	
	public static final String PARENT="parent";

	private String productTypeId;
	private ProductType parent;
	private String description;
	private CompanyDetail associatedCompany;
	private int sortOrder;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_id")
	public CompanyDetail getAssociatedCompany() {
		return associatedCompany;
	}

	public void setAssociatedCompany(CompanyDetail associatedCompany) {
		this.associatedCompany = associatedCompany;
	}


	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_product_type_id")
	public ProductType getParent() {
		return parent;
	}

	public void setParent(ProductType parent) {
		this.parent = parent;
	}


	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_type_id", insertable=false, updatable=false)
	@Override
    public ProductType getProductType() {
        return parent;
    }

    public void setProductType(ProductType productType) {
        this.parent = productType;
    }

	@Id()
	@Column(name="product_type_id")
	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "product_type_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return productTypeId=IDGenerator.generate(this);
	}

	@Formula(value="1")
	@Override
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}



}
