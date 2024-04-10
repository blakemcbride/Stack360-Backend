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

import com.arahant.beans.ImportColumn;
import com.arahant.beans.ImportFilter;
import com.arahant.beans.ImportType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BImportFilter extends SimpleBusinessObjectBase<ImportFilter> {

	public BImportFilter() {
	}

	public BImportFilter(String id) {
		super(id);
	}

	public BImportFilter(ImportFilter o) {
		bean = o;
	}

	public BImportFilter(String st, boolean b) {
		bean.setFilterNameId(st);
		bean.setFilterValue("");
		bean.setImportFilterDesc("");
	}

	public String addColumn(String colName, int pos) {
		return addColumn(colName, pos, -1);
	}

	public String addColumn(String colName, int startPos, int endPos) {
		BImportColumn bic = new BImportColumn();
		String ret = bic.create();
		bic.setStartPos((short) startPos);
		bic.setLastPos((short) endPos);
		bic.setName(colName);
		bic.setImportFilter(bean);
		addPendingInsert(bic);
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportFilter();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportFilter.class, key);
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BImportFilter(id).delete();
	}

	@Override
	public void delete() {
		for (BImportColumn c : BImportColumn.makeArray(ArahantSession.getHSU().createCriteria(ImportColumn.class)
				.eq(ImportColumn.IMPORT_FILTER, bean)
				.list()))
			c.delete();

		super.delete();
	}

	public String getImportFilterId() {
		return bean.getImportFilterId();
	}

	public void setImportFilterId(String importFilterId) {
		bean.setImportFilterId(importFilterId);
	}

	public String getImportFilterName() {
		return bean.getImportFilterName();
	}

	public void setImportFilterName(String importFilterName) {
		bean.setImportFilterName(importFilterName);
	}

	public String getImportFilterDesc() {
		return bean.getImportFilterDesc();
	}

	public void setImportFilterDesc(String importFilterDesc) {
		bean.setImportFilterDesc(importFilterDesc);
	}

	public String getFilterValue() {
		return bean.getFilterValue();
	}

	public void setFilterValue(String filterValue) {
		bean.setFilterValue(filterValue);
	}

	public String getImportTypeId() {
		return bean.getImportTypeId();
	}

	public void setImportTypeId(String importTypeId) {
		bean.setImportType(ArahantSession.getHSU().get(ImportType.class, importTypeId));
	}

	public String getFilterNameId() {
		return bean.getFilterNameId();
	}

	public void setFilterNameId(String filterNameId) {
		bean.setFilterNameId(filterNameId);
	}

	public BImportColumn[] listColumns() {
		HibernateCriteriaUtil<ImportColumn> hcu = ArahantSession.getHSU().createCriteria(ImportColumn.class)
				.eq(ImportColumn.IMPORT_FILTER, bean);

		return BImportColumn.makeArray(hcu.list());

	}

	static BImportFilter[] makeArray(List<ImportFilter> l) {
		BImportFilter[] ret = new BImportFilter[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BImportFilter(l.get(loop));
		return ret;
	}

	public void setImportType(BImportType bc) {
		bean.setImportType(bc.getBean());
	}
}
