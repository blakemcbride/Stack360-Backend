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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.imports.GenericFileImport;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=ImportColumn.TABLE_NAME)
public class ImportColumn extends ArahantBean implements Serializable {

	public static final String TABLE_NAME="import_column";

	public static final String IMPORT_FILTER="importFilter";
	public static final String COLUMN_ORDER="columnOrder";
	public static final String COLUMN_NAME="columnName";

	private String importColumnId;
    private String importFilterId;
	private short columnOrder;
	private String columnName;
	private short startPos;
	private short lastPos;
	private String dateFormat;
	private static String[] availableColumnsFilter1 = {"last_name", "first_name", "ssn", "dob", "address", "state", "city", "zip", "relationship", "dep1_first_name", "dep2_first_name"};
	private static String[] availableColumnsFilter2 = {"last_name", "first_name", "ssn", "dob", "address", "state", "city", "zip", "relationship", "dep1_first_name", "dep2_first_name"};
	private static String[] noFilterColumns = {"Filter"};
	private static boolean[] availableColumnsFilter1Required;
	static {
		//Dont know if this would always work, or just get lucky availableColumnsFilter1Required=new boolean[GenericFileImport.availableColumns.length];
		availableColumnsFilter1Required=new boolean[400];
		boolean []s= {true, true, false, false, false, false, false, false, false, true, true, true, false,
																true, true, false, true, true, true, false, true, true};
															
		for (int loop=0;loop<s.length;loop++)
			availableColumnsFilter1Required[loop]=s[loop];
	}
	private ImportFilter importFilter;


	@Id
	@Column(name="import_column_id")
	public String getImportColumnId() {
		return importColumnId;
	}

	public void setImportColumnId(String importColumnId) {
		this.importColumnId = importColumnId;
	}

	@Column(name="import_filter_id", insertable=false, updatable=false)
	public String getImportFilterId() {
		return importFilterId;
	}

	public void setImportFilterId(String importFilterId) {
		this.importFilterId = importFilterId;
	}

	@Column(name="column_order")
	public short getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(short columnOrder) {
		this.columnOrder = columnOrder;
	}

	@Column(name="column_name")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Column(name="start_pos")
	public short getStartPos() {
		return startPos;
	}

	public void setStartPos(short startPos) {
		this.startPos = startPos;
	}

	@Column(name="last_pos")
	public short getLastPos() {
		return lastPos;
	}

	public void setLastPos(short lastPos) {
		this.lastPos = lastPos;
	}

	@Column(name="date_format")
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@ManyToOne
	@JoinColumn(name="import_filter_id")
	public ImportFilter getImportFilter() {
		return importFilter;
	}

	public void setImportFilter(ImportFilter importFilter) {
		importFilterId=(importFilter==null)?null:importFilter.getImportFilterId();
		this.importFilter = importFilter;
	}

	public static String[] getAvailableColumnsFilter1() {
		return availableColumnsFilter1;
	}

	public static String[] getAvailableColumnsFilter2() {
		return availableColumnsFilter2;
	}

	public static boolean[] getAvailableColumnsFilter1Required() {
		return availableColumnsFilter1Required;
	}

	public static String[] getNoFilterColumns() {
		return noFilterColumns;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "import_column_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return importColumnId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ImportColumn other = (ImportColumn) obj;
		if ((this.importColumnId == null) ? (other.importColumnId != null) : !this.importColumnId.equals(other.importColumnId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + (this.importColumnId != null ? this.importColumnId.hashCode() : 0);
		return hash;
	}
}
