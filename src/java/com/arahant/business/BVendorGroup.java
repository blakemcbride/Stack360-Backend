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

import com.arahant.beans.OrgGroup;
import com.arahant.beans.VendorCompany;
import com.arahant.beans.VendorGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BVendorGroup extends SimpleBusinessObjectBase<VendorGroup> {

	public static void delete(String[] ids) {
		ArahantSession.getHSU().delete(ArahantSession.getHSU().createCriteria(VendorGroup.class).in(VendorGroup.VENDOR_GROUP_ID, ids).list());
	}

	private BVendorGroup(VendorGroup v) {
		bean = v;
	}

	public BVendorGroup(String id) {
		bean = ArahantSession.getHSU().get(VendorGroup.class, id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new VendorGroup();
		 return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		 bean = ArahantSession.getHSU().get(VendorGroup.class, key);
	}

	public BVendorGroup()
	{
	}

	public String getGroupVendorId() {
		return bean.getGroupVendorId();
	}

	public void setGroupVendorId(String groupVendorId) {
		bean.setGroupVendorId(groupVendorId);
	}

	public OrgGroup getOrgGroup() {
		return bean.getOrgGroup();
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		bean.setOrgGroup(orgGroup);
	}

	public VendorCompany getVendor() {
		return bean.getVendor();
	}

	public void setVendor(VendorCompany vendor) {
		bean.setVendor(vendor);
	}

	public String getVendorGroupId() {
		return bean.getVendorGroupId();
	}

	public void setVendorGroupId(String vendorGroupId) {
		bean.setVendorGroupId(vendorGroupId);
	}

	public static BVendorGroup[] list(String vendorId, int max)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(VendorGroup.class).setMaxResults(max).eq(VendorGroup.VENDOR, new BVendorCompany(vendorId).getBean()).list());
	}

	static BVendorGroup[] makeArray(List<VendorGroup> l) {
		BVendorGroup []ret=new BVendorGroup[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BVendorGroup(l.get(loop));
		return ret;
	}

}
