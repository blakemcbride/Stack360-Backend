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

import com.arahant.beans.Item;
import com.arahant.beans.ItemH;
import com.arahant.beans.ItemInspection;
import com.arahant.beans.Lot;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Product;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.List;

public class BLot extends SimpleBusinessObjectBase<Lot> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BLot(id).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		//Only allow delete if no history records
		if (ArahantSession.getHSU().createCriteria(ItemH.class)
				.eq(ItemH.LOT, bean)
				.exists())
			throw new ArahantWarning("Can not delete a lot once items have been moved or edited.");
		ArahantSession.getHSU().createCriteria(Item.class)
				.eq(Item.LOT, bean)
				.delete();
		super.delete();
	}

	public static BLot[] search(String lotNumber, String productId, int highCap) {
		HibernateCriteriaUtil<Lot> hcu = ArahantSession.getHSU().createCriteria(Lot.class)
				.like(Lot.LOT_NUMBER, lotNumber)
				.ne(Lot.LOT_NUMBER, "")
				.notNull(Lot.LOT_NUMBER)
				.setMaxResults(highCap);

		if (!isEmpty(productId))
			hcu.joinTo(Lot.ITEMS)
					.joinTo(Item.PRODUCT)
					.eq(Product.PRODUCTID, productId);

		return makeArray(hcu.list());

	}

	public static BLot[] list() {
		HibernateCriteriaUtil<Lot> hcu = ArahantSession.getHSU().createCriteria(Lot.class);

		return makeArray(hcu.list());

	}

	public static BSearchOutput<BLot> search(BSearchMetaInput metaInput, String lotNumber, String orgGroupId, String productId, String serialNumber) {

		HibernateCriteriaUtil<Lot> hcu = ArahantSession.getHSU().createCriteria(Lot.class);

		hcu.isNotNull(Lot.LOT_NUMBER);
		hcu.ne(Lot.LOT_NUMBER, "");
		hcu.like(Lot.LOT_NUMBER, lotNumber);

		HibernateCriteriaUtil itemsHcu = hcu.leftJoinTo(Lot.ITEMS);

		if (!isEmpty(serialNumber))
			itemsHcu.eq(Item.SERIAL_NUMBER, serialNumber);

		HibernateCriteriaUtil prodHcu = itemsHcu.leftJoinTo(Item.PRODUCT);

		if (!isEmpty(productId))
			prodHcu.eq(Product.PRODUCTID, productId);

		HibernateCriteriaUtil orgHcu = itemsHcu.leftJoinTo(Item.LOCATION);

		if (!isEmpty(orgGroupId))
			orgHcu.eq(OrgGroup.ORGGROUPID, orgGroupId);


		//{"lotNumber","dateReceived", "lotCost",  "orgGroupName",
		//		"originalQuantity", "productDescription"});

		switch (metaInput.getSortType()) {
			case 1:
				hcu.orderBy(Lot.LOT_NUMBER, metaInput.isSortAsc());
				break;
			case 2:
				hcu.orderBy(Lot.DATE_RECEIVED, metaInput.isSortAsc());
				break;
			case 3:
				hcu.orderBy(Lot.COST, metaInput.isSortAsc());
				break;
			case 4:
				orgHcu.orderBy(OrgGroup.NAME, metaInput.isSortAsc());
				break;
			case 5:
				hcu.orderBy(Lot.ORIGINAL_QUANTITY, metaInput.isSortAsc());
				break;
			case 6:
				prodHcu.orderBy(Product.DESCRIPTION, metaInput.isSortAsc());
				break;
			default:
				hcu.orderBy(Lot.LOT_NUMBER, metaInput.isSortAsc());

		}

		return makeSearchOutput(metaInput, hcu);

	}

	public static BSearchOutput<BLot> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Lot> hcu) {
		BSearchOutput<BLot> ret = new BSearchOutput<BLot>(searchMeta);

		HibernateScrollUtil<Lot> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BLot.makeArray(scr));

		return ret;
	}

	public BLot(Lot o) {
		super();
		bean = o;
	}

	public static BLot[] makeArray(HibernateScrollUtil<Lot> scr) {
		List<Lot> l = new ArrayList<Lot>();
		while (scr.next())
			l.add(scr.get());

		return BLot.makeArray(l);
	}

	public static BLot[] makeArray(List<Lot> l) {
		BLot[] ret = new BLot[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BLot(l.get(loop));
		return ret;
	}

	public BLot() {
		super();
	}

	public BLot(String key) {
		super(key);
	}

	@Override
	public String create() throws ArahantException {
		bean = new Lot();
		return bean.generateId();
	}

	public int getDateReceived() {
		return bean.getDateReceived();
	}

	public String getDescription() {
		return bean.getLotParticulars();
	}

	public String getId() {
		return bean.getLotId();
	}

	public BItemInspection[] getInspections() {
		return BItemInspection.makeArray(ArahantSession.getHSU().createCriteria(ItemInspection.class)
				.orderByDesc(ItemInspection.DATE)
				.joinTo(ItemInspection.ITEM)
				.eq(Item.LOT, bean)
				.list());
	}

	public BItem[] getItems() {
		return BItem.makeArray(ArahantSession.getHSU().createCriteria(Item.class)
				.eq(Item.LOT, bean)
				.list());
	}

	public BOrgGroup[] getLocations() {
		return BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class)
				.joinTo(OrgGroup.ITEMS)
				.eq(Item.LOT, bean)
				.list());
	}

	public double getLotCost() {
		return bean.getLotCost();
	}

	public String getLotDescription() {
		return bean.getLotParticulars();
	}

	public String getLotNumber() {
		return bean.getLotNumber();
	}

	public String getOrgGroupName() {
		String orgName = "None";
		for (Item i : ArahantSession.getHSU().createCriteria(Item.class)
				.eq(Item.LOT, bean)
				.list()) {
			BItem bi = new BItem(i);
			String on = bi.getOrgGroupName();
			if (orgName.equals("None"))
				orgName = on;
			else if (orgName.equals(on))
				continue;
			else {
				orgName = "Multiple";
				break;
			}
		}

		return orgName;
	}

	public int getOriginalQuantity() {
		return bean.getOriginalQuantity();
	}

	public BProduct getProduct() {

		String desc = getProductDescription();
		if (desc.equals("None") || desc.equals("Multiple"))
			return null;

		Item i = ArahantSession.getHSU().createCriteria(Item.class)
				.eq(Item.LOT, bean)
				.first();

		if (i != null)
			return new BProduct(i.getProduct());

		ItemH ih = ArahantSession.getHSU().createCriteria(ItemH.class)
				.eq(ItemH.LOT, bean)
				.first();

		if (ih != null)
			return new BProduct(ih.getProduct());

		return null;

	}

	public String getProductDescription() {
		String prodName = "None";
		for (Item i : ArahantSession.getHSU().createCriteria(Item.class)
				.eq(Item.LOT, bean)
				.list()) {
			BItem bi = new BItem(i);
			String on = bi.getProductDescription();
			if (prodName.equals("None"))
				prodName = on;
			else if (prodName.equals(on))
				continue;
			else {
				prodName.equals("Multiple");
				break;
			}
		}

		if (prodName.equals("None"))
			for (ItemH i : ArahantSession.getHSU().createCriteria(ItemH.class)
					.eq(Item.LOT, bean)
					.list()) {
				BItemH bi = new BItemH(i);
				String on = bi.getProductDescription();
				if (prodName.equals("None"))
					prodName = on;
				else if (prodName.equals(on))
					continue;
				else {
					prodName.equals("Multiple");
					break;
				}
			}

		return prodName;
	}

	public String getProductId() {
		if (getProduct() == null)
			return "";
		//return any product
		return getProduct().getId();
	}

	public int getQuantityAtWithChildren(BOrgGroup borg) {
		@SuppressWarnings("unchecked")
		List<String> il = (List) ArahantSession.getHSU().createCriteria(Item.class)
				.selectFields(Item.ID)
				.eq(Item.LOT, bean)
				.eq(Item.LOCATION, borg.getOrgGroup())
				.list();

		//get all children of these items

		return ArahantSession.getHSU().createCriteria(Item.class)
				.sum(Item.QUANTITY)
				.in(Item.ID, BItem.getAllChildren(il))
				.intValue();

	}

	public int getQuantityAt(BOrgGroup borg) {
		return ArahantSession.getHSU().createCriteria(Item.class)
				.sum(Item.QUANTITY)
				.eq(Item.LOT, bean)
				.eq(Item.LOCATION, borg.getOrgGroup())
				.intValue();

	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Lot.class, key);
	}

	public void setDateReceived(int dateReceived) {
		bean.setDateReceived(dateReceived);
	}

	public void setDescription(String description) {
		bean.setLotParticulars(description);
	}

	public void setLotCost(double productCost) {
		bean.setLotCost(productCost);
	}

	public void setLotDescription(String lotDescription) {
		bean.setLotParticulars(lotDescription);
	}

	public void setLotNumber(String lotNumber) {
		bean.setLotNumber(lotNumber);
	}

	public void setOriginalQuantity(int originalQuantity) {
		bean.setOriginalQuantity(originalQuantity);
	}

	@Override
	public void updateChecks() {
		super.updateChecks();

		@SuppressWarnings("unchecked")
		List<String> prodids = (List) ArahantSession.getHSU().createCriteria(Product.class)
				.selectFields(Product.PRODUCTID)
				.joinTo(Product.ITEMS)
				.eq(Item.LOT, bean)
				.list();

		if (ArahantSession.getHSU().createCriteria(Lot.class)
				.eq(Lot.LOT_NUMBER, bean.getLotNumber())
				.ne(Lot.LOT_ID, bean.getLotId())
				.joinTo(Lot.ITEMS)
				.joinTo(Item.PRODUCT)
				.in(Product.PRODUCTID, prodids)
				.exists())
			throw new ArahantWarning("Duplicate lot number is not allowed.");
	}

	public static BLot[] list(String productId, String locationId) {
		HibernateCriteriaUtil<Lot> hcu = ArahantSession.getHSU().createCriteria(Lot.class);
		HibernateCriteriaUtil itemHcu = hcu.joinTo(Lot.ITEMS);
		itemHcu.joinTo(Item.PRODUCT)
				.eq(Product.PRODUCTID, productId);
		itemHcu.joinTo(Item.LOCATION)
				.eq(OrgGroup.ORGGROUPID, locationId);

		return makeArray(hcu.list());
	}
}
