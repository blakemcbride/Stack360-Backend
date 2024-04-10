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

import com.arahant.beans.Product;
import com.arahant.beans.ProductType;
import com.arahant.beans.ProductTypeChild;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.List;

public class BProductType extends SimpleBusinessObjectBase<ProductType> {

	public static void delete(String[] typeIds) {
		for (String id : typeIds)
			new BProductType(id).delete();
	}

	public static BProductType[] getTopLevelTypes() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProductType.class).orderBy(ProductType.DESCRIPTION).isNull(ProductType.PARENT).list());
	}

	public static BProductType[] search(String description, String excludeProductTypeId, int highCap) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProductType.class).setMaxResults(highCap).orderBy(ProductType.DESCRIPTION).like(ProductType.DESCRIPTION, description).ne(ProductType.PRODUCT_TYPE_ID, excludeProductTypeId).list());
	}

	public static BProductType[] search(String description, String[] excludeProductTypeId, int highCap) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProductType.class).setMaxResults(highCap).orderBy(ProductType.DESCRIPTION).like(ProductType.DESCRIPTION, description).notIn(ProductType.PRODUCT_TYPE_ID, excludeProductTypeId).list());
	}

	static BProductType[] makeArray(List<ProductType> l) {
		BProductType[] ret = new BProductType[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProductType(l.get(loop));
		return ret;
	}

	public static BProductType findOrMake(String string) {
		ProductType pt = ArahantSession.getHSU().createCriteria(ProductType.class).eq(ProductType.DESCRIPTION, string).first();

		if (pt == null) {
			BProductType newpt = new BProductType();
			newpt.create();
			newpt.setDescription(string);
			newpt.insert();
			return newpt;
		} else
			return new BProductType(pt);

	}

	public BProductType(String id) {
		super(id);
	}

	public BProductType() {
		super();
	}

	public BProductType(ProductType o) {
		bean = o;
	}

	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public String create() throws ArahantException {
		bean = new ProductType();
		bean.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getProductTypeId();
	}

	public String getParentDescription() {
		if (bean.getParent() == null)
			return "";
		return bean.getParent().getDescription();
	}

	public String getParentId() {
		if (bean.getParent() == null)
			return "";
		return bean.getParent().getProductTypeId();

	}

	public List<ProductTypeChild> getChildren(BSearchMetaInput meta) {
//		HibernateScrollUtil<ProductTypeChild> scr = ArahantSession.getHSU().createCriteria(ProductTypeChild.class)
//			.eq(ProductTypeChild.PRODUCT_TYPE,bean)
//			.orderBy(ProductTypeChild.SORT_ORDER)
//			.orderBy(ProductTypeChild.DESCRIPTION)
//			.getPage(meta);

		List<ProductTypeChild> l = new ArrayList<ProductTypeChild>();

		//Add Product Types
		HibernateScrollUtil<ProductType> productTypes = ArahantSession.getHSU().createCriteria(ProductType.class).eq(ProductType.PRODUCT_TYPE, bean).orderBy(ProductType.DESCRIPTION).getPage(meta);

		while (productTypes.next())
			l.add(productTypes.get());

		//Add Products
		HibernateScrollUtil<Product> products = ArahantSession.getHSU().createCriteria(Product.class).eq(Product.PRODUCT_TYPE, bean).orderBy(Product.DESCRIPTION).getPage(meta);

		while (products.next())
			l.add(products.get());

		return l;
	}

	public BProduct[] getProducts() {
		return BProduct.makeArray(ArahantSession.getHSU().createCriteria(Product.class).orderBy(ProductTypeChild.DESCRIPTION).eq(Product.PRODUCT_TYPE, bean).list());
	}

	public BProductType[] getSubTypes() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProductType.class).orderBy(ProductType.DESCRIPTION).eq(ProductType.PARENT, bean).list());
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProductType.class, key);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setParentId(String parentId) {
		bean.setParent(ArahantSession.getHSU().get(ProductType.class, parentId));
	}
}
