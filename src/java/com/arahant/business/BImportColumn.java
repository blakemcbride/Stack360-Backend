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
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BImportColumn extends SimpleBusinessObjectBase<ImportColumn> {

	public BImportColumn() {
	}

	public BImportColumn(String id) {
		super(id);
	}

	public BImportColumn(ImportColumn o) {
		bean = o;
	}

	BImportColumn(String st, boolean b) {
		short n = 0;
		bean.setColumnName(st);
		bean.setStartPos(n);
		bean.setLastPos(n);
		bean.setDateFormat("");
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportColumn();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportColumn.class, key);
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BImportColumn(id).delete();
	}

	public String getImportColumnId() {
		return bean.getImportColumnId();
	}

	public void setImportColumnId(String importColumnId) {
		bean.setImportColumnId(importColumnId);
	}

	public String getImportFilterId() {
		return bean.getImportFilterId();
	}

	public void setImportFilterId(String importFilterId) {
		bean.setImportFilter(ArahantSession.getHSU().get(ImportFilter.class, importFilterId));
	}

	public ImportFilter getImportFilter() {
		return bean.getImportFilter();
	}

	public void setImportFilter(ImportFilter importFilter) {
		bean.setImportFilter(importFilter);
	}

	public String getName() {
		return bean.getColumnName();
	}

	public void setName(String name) {
		bean.setColumnName(name);
	}

	public short getStartPos() {
		return bean.getStartPos();
	}

	public void setStartPos(int startPos) {
		bean.setStartPos((short) startPos);
	}

	public short getLastPos() {
		return bean.getLastPos();
	}

	public void setLastPos(int lastPos) {
		bean.setLastPos((short) lastPos);
	}

	public String getDateFormat() {
		if (bean.getDateFormat() == null)
			return "";
		return bean.getDateFormat();
	}

	public void setDateFormat(String dateFormat) {
		bean.setDateFormat(dateFormat);
	}

	public static String[] getAvailableColumnsFilter1() {
		return ImportColumn.getAvailableColumnsFilter1();
	}

	public static String[] getAvailableColumnsFilter2() {
		return ImportColumn.getAvailableColumnsFilter2();
	}

	public static boolean[] getAvailableColumnsFilter1Required() {
		return ImportColumn.getAvailableColumnsFilter1Required();
	}

	public static String[] getNoFilterColumns() {
		return ImportColumn.getNoFilterColumns();
	}

	static BImportColumn[] makeArray(List<ImportColumn> l) {
		BImportColumn[] ret = new BImportColumn[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BImportColumn(l.get(loop));
		return ret;
	}
}
