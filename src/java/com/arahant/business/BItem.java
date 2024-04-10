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
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.List;

public class BItem extends SimpleBusinessObjectBase<Item> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BItem(id).delete();
	}

	public static BItem[] getItemsAt(String id) {
		return makeArray(ArahantSession.getHSU().createCriteria(Item.class).orderBy(Item.SERIAL_NUMBER).joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, id).list());
	}

	public static BItem[] search(String productId, String lotNumber, int highCap) {
		HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class);
		if (!isEmpty(productId))
			hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID, productId);
		hcu.joinTo(Item.LOT).like(Lot.LOT_NUMBER, lotNumber).setMaxResults(highCap);

		return makeArray(hcu.list());
	}

	public static BSearchOutput<BItem> search(BSearchMetaInput metaInput, String lotId, String orgGroupId, String productId, String serialNumber, int cap) {

		HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class);

		HibernateCriteriaUtil lotHcu = hcu.joinTo(Item.LOT);
		HibernateCriteriaUtil prodHcu = hcu.joinTo(Item.PRODUCT);
		if (!isEmpty(lotId))
			lotHcu.eq(Lot.LOT_ID, lotId);

		if (!isEmpty(orgGroupId)) {
			@SuppressWarnings("unchecked")
			List<String> il = (List) ArahantSession.getHSU().createCriteria(Item.class).selectFields(Item.ID).joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, orgGroupId).list();
			hcu.in(Item.ID, getAllChildren(il));
		}

		if (!isEmpty(productId))
			prodHcu.eq(Product.PRODUCTID, productId);

		hcu.like(Item.SERIAL_NUMBER, serialNumber);


		//"dateReceived", "hasParentItem", "lotNumber", "itemCost",
//				"orgGroupName", "serialNumber", "productDescription", "quantity"});

		switch (metaInput.getSortType()) {
			case 1:
				hcu.orderBy(Item.SERIAL_NUMBER, metaInput.isSortAsc());
				break;
			case 2:
				lotHcu.orderBy(Lot.DATE_RECEIVED, metaInput.isSortAsc());
				break;
			case 3:
				lotHcu.orderBy(Lot.DATE_RECEIVED, metaInput.isSortAsc());
				break;
			case 4:
				lotHcu.orderBy(Lot.LOT_NUMBER, metaInput.isSortAsc());
				break;
			case 5:
				lotHcu.orderBy(Lot.COST, metaInput.isSortAsc());
				break;
			case 6:
				hcu.leftJoinTo(Item.LOCATION).orderBy(OrgGroup.NAME, metaInput.isSortAsc());
				break;
			case 7:
				prodHcu.orderBy(Product.DESCRIPTION, metaInput.isSortAsc());
				break;
			case 8:
				hcu.orderBy(Item.QUANTITY, metaInput.isSortAsc());
				break;
			default:
				hcu.orderBy(Item.SERIAL_NUMBER, metaInput.isSortAsc());

		}

		return makeSearchOutput(metaInput, hcu);

	}

	public static BSearchOutput<BItem> search(BSearchMetaInput metaInput, String orgGroupId, String productId, String serialNumber, int cap, String excludeId, String lotId) {
		HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class);
		hcu.ne(Item.ID, excludeId);
		HibernateCriteriaUtil lotHcu = hcu.joinTo(Item.LOT);
		HibernateCriteriaUtil prodHcu = hcu.joinTo(Item.PRODUCT);

		if (!isEmpty(lotId))
			lotHcu.eq(Lot.LOT_ID, lotId);

		if (!isEmpty(orgGroupId)) {
			@SuppressWarnings("unchecked")
			List<String> il = (List) ArahantSession.getHSU().createCriteria(Item.class).selectFields(Item.ID).joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, orgGroupId).list();
			hcu.in(Item.ID, getAllChildren(il));
		}

		if (!isEmpty(productId))
			prodHcu.eq(Product.PRODUCTID, productId);

		hcu.like(Item.SERIAL_NUMBER, serialNumber);


		switch (metaInput.getSortType()) {
			case 1:
				hcu.orderBy(Item.SERIAL_NUMBER, metaInput.isSortAsc());
				break;
			case 2:
				prodHcu.orderBy(Product.DESCRIPTION, metaInput.isSortAsc());
				break;
			case 3:
				hcu.leftJoinTo(Item.LOCATION).orderBy(OrgGroup.NAME, metaInput.isSortAsc());
				break;
			case 4:
				hcu.leftJoinTo(Item.LOCATION).orderBy(OrgGroup.NAME, metaInput.isSortAsc());
				break;
			case 5:
				lotHcu.orderBy(Lot.LOT_NUMBER, metaInput.isSortAsc());
				break;
			case 6:
				hcu.orderBy(Item.QUANTITY, metaInput.isSortAsc());
				break;
			default:
				hcu.orderBy(Item.SERIAL_NUMBER, metaInput.isSortAsc());

		}
		return makeSearchOutput(metaInput, hcu);
	}

	@Override
	public void delete() {
		deleteInspectionItems();
		super.delete();
	}

	static BItem[] makeArray(HibernateScrollUtil<Item> scr) {
		List<Item> l = new ArrayList<Item>();
		while (scr.next())
			l.add(scr.get());

		return BItem.makeArray(l);
	}

	static BItem[] makeArray(List<Item> l) {
		BItem[] ret = new BItem[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BItem(l.get(loop));
		return ret;
	}

	public BItem() {
	}

	public BItem(Item o) {
		super();
		bean = o;
	}

	public BItem(String id) {
		super(id);
	}
	/*
	 * Do not use until Java 6 @Override
	 */

	@Override
	public String create() throws ArahantException {
		bean = new Item();
		return bean.generateId();
	}

	public void deleteInspectionItems() {
		ArahantSession.getHSU().createCriteria(ItemInspection.class).eq(ItemInspection.ITEM, bean).delete();
	}

	public String getChangeMadeBy() {
		return new BPerson(bean.getRecordPersonId()).getNameLFM();
	}

	public String getChangeType() {
		return bean.getRecordChangeType() + "";
	}

	public BItem[] getChildren() {
		return makeArray(ArahantSession.getHSU().createCriteria(Item.class).eq(Item.PARENT, bean).list());
	}

	public double getCost() {
		return bean.getLot().getLotCost();
	}

	public int getDateReceived() {
		return bean.getLot().getDateReceived();
	}

	public String getDateTimeFormatted() {
		return DateUtils.getDateFormatted(bean.getRecordChangeDate());
	}

	public String getDescription() {
		return bean.getProduct().getDescription();
	}

	public boolean getHasParentItem() {
		return hasParent();
	}

	public String getId() {
		return bean.getItemId();
	}

	public BItemInspection[] getInspections() {
		return BItemInspection.makeArray(ArahantSession.getHSU().createCriteria(ItemInspection.class).eq(ItemInspection.ITEM, bean).orderByDesc(ItemInspection.DATE).list());
	}

	public String getInventoryDescription() {
		return bean.getItemParticulars();
	}

	public double getItemCost() {
		return bean.getLot().getLotCost();
	}

	public String getItemDescription() {
		return bean.getItemParticulars();
	}

	public BOrgGroup getLocation() {
		if (bean.getLocation() == null)
			return null;
		return new BOrgGroup(bean.getLocation());
	}

	public BLot getLot() {
		return new BLot(bean.getLot());
	}

	public String getLotId() {
		return bean.getLot().getLotId();
	}

	public String getLotNumber() {
		return bean.getLot().getLotNumber();
	}

	public String getOrgGroupName() {
		if (bean.getLocation() == null) {
			if (bean.getParentItem() == null)
				return "";
			return new BItem(bean.getParentItem()).getOrgGroupName();
		}
		return bean.getLocation().getName();
	}

	public int getOriginalQuantity() {
		return bean.getLot().getOriginalQuantity();
	}

	public BItem getParent() {
		if (bean.getParentItem() == null)
			return null;
		return new BItem(bean.getParentItem());
	}

	public String getParentDescription() {
		if (bean.getParentItem() == null)
			return "";
		return bean.getParentItem().getItemParticulars();
	}

	public String getParentProductDescription() {
		if (bean.getParentItem() == null)
			return "";
		return bean.getParentItem().getProduct().getDescription();
	}

	public BProduct getProduct() {
		return new BProduct(bean.getProduct());
	}

	public double getProductCost() {
		return bean.getLot().getLotCost();
	}

	public String getProductDescription() {
		return bean.getProduct().getDescription();
	}

	public String getProductId() {
		return bean.getProduct().getProductId();
	}

	public int getQuantity() {
		return bean.getQuantity();
	}

    public String getItemName() {
	    return bean.getItemName();
    }

    public void setItemName(String name) {
	    bean.setItemName(name);
    }

    public String getManufacturer() {
        return bean.getManufacturer();
    }

    public void setManufacturer(String name) {
        bean.setManufacturer(name);
    }

    public String getModel() {
        return bean.getModel();
    }

    public void setModel(String name) {
        bean.setModel(name);
    }

    public int getDatePurchased() {
        return bean.getDatePurchased();
    }

    public void setDatePurchased(int d) {
        bean.setDatePurchased(d);
    }

    public float getOriginalCost() {
        return bean.getOriginalCost();
    }

    public void setOriginalCost(float d) {
        bean.setOriginalCost(d);
    }

    public String getPurchasedFrom() {
        return bean.getPurchasedFrom();
    }

    public void setPurchasedFrom(String name) {
        bean.setPurchasedFrom(name);
    }

    public String getNotes() {
        return bean.getNotes();
    }

    public void setNotes(String name) {
        bean.setNotes(name);
    }

    public char getItemStatus() {
        return bean.getItemStatus();
    }

    public void setItemStatus(char name) {
        bean.setItemStatus(name);
    }

    public String getRetirementNotes() {
        return bean.getRetirementNotes();
    }

    public void setRetirementNotes(String name) {
        bean.setRetirementNotes(name);
    }

    public int getRetirementDate() {
        return bean.getRetirementDate();
    }

    public void setRetirementDate(int name) {
        bean.setRetirementDate(name);
    }

    public char getRetirementStatus() {
        return bean.getRetirementStatus();
    }

    public void setRetirementStatus(char name) {
        bean.setRetirementStatus(name);
    }

    public Person getReimbursementPerson() {
        return bean.getReimbursementPerson();
    }

    public void setReimbursementPerson(Person name) {
        bean.setReimbursementPerson(name);
    }

    public float getRequestedReimbursementAmount() {
        return bean.getRequestedReimbursementAmount();
    }

    public void setRequestedReimbursementAmount(float name) {
        bean.setRequestedReimbursementAmount(name);
    }

    public float getReimbursementAmountReceived() {
        return bean.getReimbursementAmountReceived();
    }

    public void setReimbursementAmountReceived(float amt) {
        bean.setReimbursementAmountReceived(amt);
    }

    public int getDateReimbursementReceived() {
        return bean.getDateReimbursementReceived();
    }

    public void setDateReimbursementReceived(int amt) {
        bean.setDateReimbursementReceived(amt);
    }

    public Person getPersonAcceptingReimbursement() {
        return bean.getPersonAcceptingReimbursement();
    }

    public void setPersonAcceptingReimbursement(Person name) {
        bean.setPersonAcceptingReimbursement(name);
    }

	public int getRemainingQuantity() {
		return bean.getLot().getRemainingQuantity();
	}

	public String getSerialNumber() {
		return bean.getSerialNumber();
	}

	public boolean hasParent() {
		return bean.getParentItem() != null;
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Item.class, key);
	}

	public void setDateReceived(int dateReceived) {
		bean.getLot().setDateReceived(dateReceived);
	}

	public void setDescription(String descripiton) {
		bean.setItemParticulars(descripiton);
	}

	public void setDetailedDescription(String descripiton) {
		bean.setItemParticulars(descripiton);
	}

	public void setLocation(String orgGroupId) {
		bean.setLocation(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setLot(BLot l) {
		bean.setLot(l.bean);
	}

	public void setLotId(String lotId) {
		bean.setLot(ArahantSession.getHSU().get(Lot.class, lotId));
	}

	public void setLotNumberId(String lotNumberId) {
		bean.setLot(ArahantSession.getHSU().get(Lot.class, lotNumberId));
	}

	public void setParentItemId(String parentItemId) {
		if (parentItemId != null && parentItemId.equals(bean.getItemId()))
			throw new ArahantWarning("You may not make an item a child of itself.");
		bean.setParentItem(ArahantSession.getHSU().get(Item.class, parentItemId));
	}

	public void setProductCost(double productCost) {
		bean.getLot().setLotCost(productCost);
	}

	public void setProductId(String productId) {
		bean.setProduct(ArahantSession.getHSU().get(Product.class, productId));
	}

	public void setSerialNumber(String serialNumber) {
		if (serialNumber == null)
			serialNumber = "";
		bean.setSerialNumber(serialNumber);
	}

	public static List<String> getAllChildren(List<String> itemIds) {
		@SuppressWarnings("unchecked")
		List<String> kids = (List) ArahantSession.getHSU().createCriteria(Item.class).selectFields(Item.ID).joinTo(Item.PARENT).in(Item.ID, itemIds).list();

		if (kids.isEmpty())
			return itemIds;

		List<String> myKids = getAllChildren(kids);
		itemIds.addAll(kids);
		itemIds.addAll(myKids);

		return itemIds;
	}

	public static BSearchOutput<BItem> search(String lotNumber,
			String orgGroupId, int productAvailable, String productId, String[] productTypeIds,
			String serialNumber,
			String[] vendorIds,
			String[] excludeIds,
			BSearchMetaInput metaInput) {

		HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class);

		//	HibernateCriteriaUtil detHcu=hcu.joinTo(Inventory.INVENTORY_DETAILS);
		HibernateCriteriaUtil prodHcu = hcu.joinTo(Item.PRODUCT);
		HibernateCriteriaUtil locHcu = hcu.leftJoinTo(Item.LOCATION);

		hcu.notIn(Item.ID, excludeIds);

		if (!isEmpty(lotNumber))
			hcu.joinTo(Item.LOT).eq(Lot.LOT_NUMBER, lotNumber);

		if (!isEmpty(orgGroupId)) {
			//get all possible ones, need to check by parents
			@SuppressWarnings("unchecked")
			List<String> il = (List) ArahantSession.getHSU().createCriteria(Item.class).selectFields(Item.ID).joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, orgGroupId).list();
			hcu.in(Item.ID, getAllChildren(il));
		}

		switch (productAvailable) {
			case 1:
				prodHcu.dateInside(Product.AVAILABLE_DATE, Product.TERM_DATE, DateUtils.now());
				break;

			case 2:
				prodHcu.dateOutside(Product.AVAILABLE_DATE, Product.TERM_DATE, DateUtils.now());
				break;
		}

		if (!isEmpty(productId))
			prodHcu.eq(Product.PRODUCTID, productId);

		if (productTypeIds.length > 0)
			prodHcu.joinTo(Product.PRODUCT_TYPE).in(ProductType.PRODUCT_TYPE_ID, productTypeIds);

		if (!isEmpty(serialNumber))
			hcu.eq(Item.SERIAL_NUMBER, serialNumber);

		if (vendorIds.length > 0)
			prodHcu.joinTo(Product.VENDOR).in(VendorCompany.ORGGROUPID, vendorIds);


		/*
		 * description - string - the associated product - orgGroupName - string
		 * - the associated org group - detailCount - number - number of
		 * associated inventory details - reOrderLevel - number
			 *
		 */
		switch (metaInput.getSortType()) {
			case 1:
				prodHcu.orderBy(Product.DESCRIPTION, metaInput.isSortAsc());
				break;
			case 2:
				locHcu.orderBy(OrgGroup.NAME, metaInput.isSortAsc());
				break;
			case 3:
				hcu.orderBy(Inventory.TOTAL_REMAINING, metaInput.isSortAsc());
				break;
			case 4:
				hcu.orderBy(Inventory.REORDER_LEVEL, metaInput.isSortAsc());
				break;
			default:
				prodHcu.orderBy(Product.DESCRIPTION, metaInput.isSortAsc());

		}

		return makeSearchOutput(metaInput, hcu);
	}

	public static BSearchOutput<BItem> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Item> hcu) {
		BSearchOutput<BItem> ret = new BSearchOutput<BItem>(searchMeta);

		HibernateScrollUtil<Item> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BItem.makeArray(scr));

		return ret;
	}

	public String moveTo(String locationId, int breakQuantity, String serialNumber) {
		if (bean.getQuantity() < breakQuantity)
			throw new ArahantWarning("There are not enough items remaining in " + bean.getProduct().getDescription() + ".  Only " + bean.getQuantity() + " are left.");

		bean.setQuantity(bean.getQuantity() - breakQuantity);

		BItem cln = new BItem();
		String id = cln.create();
		HibernateSessionUtil.copyCorresponding(cln.bean, bean);
		cln.bean.setItemId(id);
		cln.setParentItemId("");
		cln.setQuantity(breakQuantity);
		cln.setLocation(locationId);
		cln.setSerialNumber(serialNumber);
		cln.insert();

		return id;
	}

	public String splitTo(String parentId, int breakQuantity, String serialNumber) {
		if (bean.getQuantity() < breakQuantity)
			throw new ArahantWarning("There are not enough items remaining in " + bean.getProduct().getDescription() + ".  Only " + bean.getQuantity() + " are left.");

		bean.setQuantity(bean.getQuantity() - breakQuantity);

		BItem cln = new BItem();
		String id = cln.create();
		HibernateSessionUtil.copyCorresponding(cln.bean, bean);
		cln.bean.setItemId(id);
		cln.setParentItemId(parentId);
		cln.setQuantity(breakQuantity);
		cln.setLocation("");
		cln.setSerialNumber(serialNumber);
		cln.insert();

		return id;
	}

	public String splitTo(String parentId, int breakQuantity) {
		if (bean.getQuantity() < breakQuantity)
			throw new ArahantWarning("There are not enough items remaining in " + bean.getProduct().getDescription() + ".  Only " + bean.getQuantity() + " are left.");

		bean.setQuantity(bean.getQuantity() - breakQuantity);

		BItem cln = new BItem();
		String id = cln.create();
		HibernateSessionUtil.copyCorresponding(cln.bean, bean);
		cln.bean.setItemId(id);
		cln.setParentItemId(parentId);
		cln.setQuantity(breakQuantity);
		cln.setLocation("");
		cln.insert();
		return id;
	}

	public void setQuantity(int quantity) {
		bean.setQuantity(quantity);
	}

	@Override
	public void updateChecks() {
		super.updateChecks();
		if (!isEmpty(bean.getSerialNumber()) && ArahantSession.getHSU().createCriteria(Item.class).ne(Item.ID, bean.getItemId()).eq(Item.SERIAL_NUMBER, bean.getSerialNumber()).eq(Item.PRODUCT, bean.getProduct()).exists())
			throw new ArahantWarning("Duplicate serial number is not allowed.");
		if (ArahantSession.getHSU().createCriteria(Lot.class).eq(Lot.LOT_NUMBER, bean.getLot().getLotNumber()).ne(Lot.LOT_ID, bean.getLot().getLotId()).joinTo(Lot.ITEMS).eq(Item.PRODUCT, bean.getProduct()).exists())
			throw new ArahantWarning("Duplicate lot number is not allowed.");
	}

	@Override
	public void insertChecks() {
		super.insertChecks();
		if (!isEmpty(bean.getSerialNumber()) && ArahantSession.getHSU().createCriteria(Item.class).ne(Item.ID, bean.getItemId()).eq(Item.SERIAL_NUMBER, bean.getSerialNumber()).eq(Item.PRODUCT, bean.getProduct()).exists())
			throw new ArahantWarning("Duplicate serial number is not allowed.");
	}
}
