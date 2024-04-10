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

import com.arahant.beans.Inventory;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Product;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.List;

public class BInventory extends SimpleBusinessObjectBase<Inventory> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BInventory(id).delete();
	}

	@Override
	public void delete() {
		deleteDetailItems();
		super.delete();
	}

	public static BSearchOutput<BInventory> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Inventory> hcu) {
		BSearchOutput<BInventory> ret = new BSearchOutput<BInventory>(searchMeta);

		HibernateScrollUtil<Inventory> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BInventory.makeArray(scr));

		return ret;
	}

	static BInventory[] makeArray(HibernateScrollUtil<Inventory> scr) {
		List<Inventory> l = new ArrayList<Inventory>();
		while (scr.next())
			l.add(scr.get());

		return BInventory.makeArray(l);
	}

	static BInventory[] makeArray(List<Inventory> l) {
		BInventory[] ret = new BInventory[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BInventory(l.get(loop));
		return ret;
	}

	public BInventory(String id) {
		super(id);
	}

	public BInventory() {
		super();
	}

	public BInventory(Inventory o) {
		super();
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new Inventory();
		return bean.generateId();
	}

	public void deleteDetailItems() {
		/*		hsu.createCriteria(Item.class)
		 .eq(Item.INVENTORY,bean)
		 .delete();
		 * */
	}

	public String getDescription() {
		return bean.getProduct().getDescription();
	}

	public String getId() {
		return bean.getInventoryId();
	}

	public BItem[] getInventoryDetails() {
		/*	return BItem.makeArray(hsu.createCriteria(Item.class)
		 .eq(Item.INVENTORY, bean)
		 .orderBy(Item.LOT_NUMBER)
		 .list());
		 */
		return new BItem[0];
	}

	public BOrgGroup getLocation() {
		if (bean.getLocation() == null)
			return null;
		return new BOrgGroup(bean.getLocation());
	}

	public String getOrgGroupName() {
		if (bean.getLocation() == null)
			return "";
		return bean.getLocation().getName();
	}

	public BProduct getProduct() {
		return new BProduct(bean.getProduct());
	}

	public int getReOrderLevel() {
		return bean.getReorderLevel();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Inventory.class, key);
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setLocation(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setProductId(String productId) {
		bean.setProduct(ArahantSession.getHSU().get(Product.class, productId));
	}

	public void setReOrderLevel(int reOrderLevel) {
		bean.setReorderLevel(reOrderLevel);
	}
}
