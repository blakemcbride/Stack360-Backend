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

import com.arahant.beans.ItemInspection;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BItemInspection extends SimpleBusinessObjectBase<ItemInspection> {

	public BItemInspection() {
	}

	public BItemInspection(String key) {
		super(key);
	}

	public BItemInspection(ItemInspection o) {
		bean = o;
	}

	static BItemInspection[] makeArray(List<ItemInspection> l) {
		BItemInspection[] ret = new BItemInspection[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BItemInspection(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ItemInspection();
		bean.setRecordPerson(ArahantSession.getHSU().getCurrentPerson());
		bean.setRecordChangeDate(new java.util.Date());
		return bean.generateId();
	}

	public String getComments() {
		return bean.getInspectionComments();
	}

	public String getId() {
		return bean.getItemInspectionId();
	}

	public int getInspectionDate() {
		return bean.getInspectionDate();
	}

	public String getInspectorId() {
		return bean.getPersonInspecting().getPersonId();
	}

	public String getInspectorName() {
		return bean.getPersonInspecting().getNameLFM();
	}

	public String getStatus() {
		return bean.getInspectionStatus() + "";
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ItemInspection.class, key);
	}

	public void setComments(String comments) {
		bean.setInspectionComments(comments);
	}

	public void setInspectionDate(int inspectionDate) {
		bean.setInspectionDate(inspectionDate);
	}

	public void setInspectorId(String inspectorId) {
		bean.setPersonInspecting(ArahantSession.getHSU().get(Person.class, inspectorId));
	}

	public void setItem(BItem d) {
		bean.setItem(d.bean);
	}

	public void setStatus(String status) {
		if (isEmpty(status))
			throw new ArahantException("Status is required.");
		bean.setInspectionStatus(status.charAt(0));
	}
}
