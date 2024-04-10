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

public class BImportType extends SimpleBusinessObjectBase<ImportType> {

	public BImportType() {
	}

	public BImportType(String id) {
		super(id);
	}

	public BImportType(ImportType o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportType();
		return bean.generateId();
	}

	public int getFilterEndPos() {
		return bean.getFilterEndPos();
	}

	public int getFilterStartPos() {
		return bean.getFilterStartPos();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportType.class, key);
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BImportType(id).delete();
	}

	@Override
	public void delete() {
		for (BImportColumn c : BImportColumn.makeArray(ArahantSession.getHSU().createCriteria(ImportColumn.class)
				.joinTo(ImportColumn.IMPORT_FILTER)
				.eq(ImportFilter.IMPORT_TYPE, bean)
				.list()))
			c.delete();

		for (BImportFilter f : BImportFilter.makeArray(ArahantSession.getHSU().createCriteria(ImportFilter.class)
				.eq(ImportFilter.IMPORT_TYPE, bean)
				.list()))
			f.delete();

		super.delete();
	}

	public String getDelimiter() {
		String d = bean.getDelimChar() + "";
		return d;
	}

	public void setDelimiter(char c) {
		bean.setDelimChar(c);
	}

	public void setDelimiter(String delim) {
		if (delim.length() > 0)
			bean.setDelimChar(delim.charAt(0));
		else
			bean.setDelimChar(' ');
	}

	public String getQuote() {
		String q = bean.getQuoteChar() + "";
		return q;
	}

	public void setFilterEndPos(int end) {
		bean.setFilterEndPos(end);
	}

	public void setFilterStartPos(int start) {
		bean.setFilterStartPos(start);
	}

	public void setQuote(char c) {
		bean.setQuoteChar(c);
	}

	public void setQuote(String quote) {
		if (quote.length() > 0)
			bean.setQuoteChar(quote.charAt(0));
		else
			bean.setQuoteChar(' ');
	}

	public String getImportFileTypeId() {
		return bean.getImportTypeId();
	}

	public void setImportFileTypeId(String importFileTypeId) {
		bean.setImportTypeId(importFileTypeId);
	}

	public String getImportProgramName() {
		return bean.getImportProgramName();
	}

	public void setImportProgramName(String importProgramName) {
		bean.setImportProgramName(importProgramName);
	}

	public String getImportName() {
		return bean.getImportName();
	}

	public void setImportName(String importName) {
		bean.setImportName(importName);
	}

	public String getImportSource() {
		return bean.getImportSource();
	}

	public void setImportSource(String importSource) {
		bean.setImportSource(importSource);
	}

	public String getFileFormat() {
		return bean.getFileFormat() + "";
	}

	public void setFileFormat(String fileFormat) {
		bean.setFileFormat(fileFormat.charAt(0));
	}

	public static String[] getDateFormat() {
		return ImportType.getDateFormat();
	}

	public static void setDateFormat(String[] dateFormat) {
		ImportType.setDateFormat(dateFormat);
	}

	public static String[] getProgram() {
		return ImportType.getProgram();
	}

	public static void setProgram(String[] program) {
		ImportType.setProgram(program);
	}

	public BImportFilter[] listFilters() {
		HibernateCriteriaUtil<ImportFilter> hcu = ArahantSession.getHSU().createCriteria(ImportFilter.class)
				.eq(ImportFilter.IMPORT_TYPE, bean)
				.orderBy(ImportFilter.FILTER_NAME);

		return BImportFilter.makeArray(hcu.list());

	}

	public static BImportType[] searchBenefitImportConfigs(final String name) {
		List<ImportType> l = ArahantSession.getHSU().createCriteria(ImportType.class)
				.like(ImportType.IMPORT_NAME, name)
				.orderBy(ImportType.IMPORT_NAME)
				.list();

		return makeArray(l);
	}

	private static BImportType[] makeArray(List<ImportType> l) {
		BImportType[] ret = new BImportType[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BImportType(l.get(loop));
		return ret;

	}

	public void setType(char fileType) {
		bean.setFileFormat(fileType);
	}
}
