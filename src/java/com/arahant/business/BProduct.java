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


package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BProduct extends SimpleBusinessObjectBase<Product> {

	public static void delete(String[] productIds) {
		for (String id : productIds)
			new BProduct(id).delete();
	}

	static BProduct[] makeArray(List<Product> l) {
		BProduct[] ret = new BProduct[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProduct(l.get(loop));
		return ret;
	}

	public BProduct(String id) {
		super(id);
	}

	public BProduct() {
	}

	public BProduct(Product o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new Product();
		bean.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public String getSellAsType() {
		return bean.getSellAsType() + "";
	}

	public void setSellAsType(String sellAsType) {
		bean.setSellAsType(sellAsType.charAt(0));
	}

	public int getAvailabilityDate() {
		return bean.getAvailabilityDate();
	}

	public double getCost() {
		return bean.getProductCost();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getProductId();
	}

	public double getRetailPrice() {
		return bean.getRetailPrice();
	}

	public String getSku() {
		return bean.getSku();
	}

	public int getTermDate() {
		return bean.getTermDate();
	}

	public String getTypeDescription() {
		return bean.getProductType().getDescription();
	}

	public String getVendorId() {
		if (bean.getVendor() == null)
			return "";
		return bean.getVendor().getOrgGroupId();
	}

	public double getWholesalePrice() {
		return bean.getWholesalePrice();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Product.class, key);
	}

	public void setAvailabilityDate(int availabilityDate) {
		bean.setAvailabilityDate(availabilityDate);
	}

	public void setCost(double cost) {
		bean.setProductCost(cost);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setProductTypeId(String productTypeId) {
		bean.setProductType(ArahantSession.getHSU().get(ProductType.class, productTypeId));
	}

	public void setRetailPrice(double retailPrice) {
		bean.setRetailPrice(retailPrice);
	}

	public void setSku(String sku) {
		bean.setSku(sku);
	}

	public void setTermDate(int termDate) {
		bean.setTermDate(termDate);
	}

	public void setVendorId(String vendorId) {
		bean.setVendor(ArahantSession.getHSU().get(VendorCompany.class, vendorId));
	}

	public void setWholesalePrice(double wholesalePrice) {
		bean.setWholesalePrice(wholesalePrice);
	}

	public String getVendorName() {
		if (bean.getVendor() == null)
			return "";

		return bean.getVendor().getName();

	}

	public static BProduct[] search(String description, String sku, int max) {
		HibernateCriteriaUtil<Product> hcu = ArahantSession.getHSU().createCriteria(Product.class).setMaxResults(max).like(Product.DESCRIPTION, description).orderBy(Product.DESCRIPTION);

		if (!isEmpty(sku))
			hcu.eq(Product.SKU, sku);

		return makeArray(hcu.list());
	}

	public static BProduct[] search(String name) {
		return BProduct.makeArray(ArahantSession.getHSU().createCriteria(Product.class).orderBy(Product.SKU).like(Product.SKU, name).list());
	}

	public BOrgGroup[] getLocations() {
		return BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).joinTo(OrgGroup.ITEMS).eq(Item.PRODUCT, bean).gt(Item.QUANTITY, 0).list());
	}

	public int getQuantityAt(BOrgGroup bo) {
		return ArahantSession.getHSU().createCriteria(Item.class).sum(Item.QUANTITY).eq(Item.LOCATION, bo.getOrgGroup()).eq(Item.PRODUCT, bean).intValue();
	}

	public double getManHours() {
		return bean.getManHours();
	}

	public void setManHours(double manHours) {
		bean.setManHours(manHours);
	}
}
