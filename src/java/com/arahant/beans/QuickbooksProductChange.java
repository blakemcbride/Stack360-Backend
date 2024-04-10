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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = ProductService.TABLE_NAME)
public class QuickbooksProductChange extends QuickbooksChange {

	public final static String PRODUCT = "product";
	private static final long serialVersionUID = 1L;
	private Service product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "arahant_record_id")
	public Service getProduct() {
		return product;
	}

	public void setProduct(Service product) {
		this.product = product;
	}
}
