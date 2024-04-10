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

/**
 *
 */
@Entity
@Table(name = AssemblyTemplateDetail.TABLE_NAME)
public class AssemblyTemplateDetail extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "assembly_template_detail";
	public static final String PARENT = "parentDetail";
	public static final String PRODUCT = "product";
	public static final String TEMPLATE = "assemblyTemplate";
	public static final String ID = "assemblyTemplateDetailId";
	private String assemblyTemplateDetailId;
	private AssemblyTemplate assemblyTemplate;
	private AssemblyTemplateDetail parentDetail;
	private Product product;
	private int quantity;
	private String itemParticulars;
	private char trackToItem = 'N';

	@ManyToOne
	@JoinColumn(name = "assembly_template_id")
	public AssemblyTemplate getAssemblyTemplate() {
		return assemblyTemplate;
	}

	public void setAssemblyTemplate(AssemblyTemplate assemblyTemplate) {
		this.assemblyTemplate = assemblyTemplate;
	}

	@Id
	@Column(name = "assembly_template_detail_id")
	public String getAssemblyTemplateDetailId() {
		return assemblyTemplateDetailId;
	}

	public void setAssemblyTemplateDetailId(String assemblyTemplateDetailId) {
		this.assemblyTemplateDetailId = assemblyTemplateDetailId;
	}

	@Column(name = "item_particulars")
	public String getItemParticulars() {
		return itemParticulars;
	}

	public void setItemParticulars(String itemParticulars) {
		this.itemParticulars = itemParticulars;
	}

	@ManyToOne
	@JoinColumn(name = "parent_detail_id")
	public AssemblyTemplateDetail getParentDetail() {
		return parentDetail;
	}

	public void setParentDetail(AssemblyTemplateDetail parentDetail) {
		this.parentDetail = parentDetail;
	}

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "quantity")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Column(name = "track_to_item")
	public char getTrackToItem() {
		return trackToItem;
	}

	public void setTrackToItem(char trackToItem) {
		this.trackToItem = trackToItem;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "assembly_template_detail_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return assemblyTemplateDetailId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AssemblyTemplateDetail other = (AssemblyTemplateDetail) obj;
		if ((this.assemblyTemplateDetailId == null) ? (other.assemblyTemplateDetailId != null) : !this.assemblyTemplateDetailId.equals(other.assemblyTemplateDetailId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.assemblyTemplateDetailId != null ? this.assemblyTemplateDetailId.hashCode() : 0);
		return hash;
	}
}
