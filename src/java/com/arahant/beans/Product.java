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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Formula;

/**
 *
 */
@Entity
@Table(name = Product.TABLE_NAME)
@PrimaryKeyJoinColumn(name = "product_id")
public class Product extends ProductService implements Comparable<Product>, ProductTypeChild {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String TABLE_NAME = "product";
	public static final String SKU = "sku";
	public static final String AVAILABLE_DATE = "availabilityDate";
	public static final String TERM_DATE = "termDate";
	public static final String VENDOR = "vendor";
	public static final String ITEMS = "items";
	private ProductType productType;
	private VendorCompany vendor;
	private String sku;
	private double productCost;
	private double wholesalePrice;
	private double retailPrice;
	private int availabilityDate;
	private int termDate;
	private Set<Item> items = new HashSet<Item>();
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String DESCRIPTION = "description";
	private int sortOrder;
	private double manHours;
	private char sellAsType;

	public Product() {
	}

	@OneToMany(fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	@JoinColumn(name = "product_id")
	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	@Column(name = "availability_date")
	public int getAvailabilityDate() {
		return availabilityDate;
	}

	public void setAvailabilityDate(int availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	@Column(name = "product_cost")
	public double getProductCost() {
		return productCost;
	}

	public void setProductCost(double productCost) {
		this.productCost = productCost;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id")
	@Override
	public ProductType getProductType() {
		return productType;
	}

	@Override
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@Column(name = "retail_price")
	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	@Column(name = "sku")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(name = "term_date")
	public int getTermDate() {
		return termDate;
	}

	public void setTermDate(int termDate) {
		this.termDate = termDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	public VendorCompany getVendor() {
		return vendor;
	}

	public void setVendor(VendorCompany vendor) {
		this.vendor = vendor;
	}

	@Column(name = "wholesale_price")
	public double getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	@Override
	public int compareTo(Product o) {
		if (o == null)
			return -1;
		return productType.getDescription().compareTo(o.getProductType().getDescription());
	}

	@Formula(value = "2")
	@Override
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "man_hours")
	public double getManHours() {
		return manHours;
	}

	public void setManHours(double manHours) {
		this.manHours = manHours;
	}

	@Column(name = "sell_as_type")
	public char getSellAsType() {
		return sellAsType;
	}

	public void setSellAsType(char sellAsType) {
		this.sellAsType = sellAsType;
	}
}
