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


/**
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name=ImportFilter.TABLE_NAME)
public class ImportFilter extends ArahantBean implements Serializable {

	public static final String TABLE_NAME="import_filter";
	
	public static final String IMPORT_TYPE="importType";
	public static final String FILTERS="filters";
	public static final String FILTER_NAME="importFilterName";

	private String importFilterId;
	private String importTypeId;
	private String importFilterName;
	private String importFilterDesc;
	private String filterValue;

	private ImportType importType;

	private Set<ImportColumn> columns = new HashSet<ImportColumn>(0);
//	private Set<ImportFilter> filters=new HashSet<ImportFilter>();

	private String filterNameId;

	@Id
	@Column(name="import_filter_id")
	public String getImportFilterId() {
		return importFilterId;
	}

	public void setImportFilterId(String importFilterId) {
		this.importFilterId = importFilterId;
	}

	@Column(name="import_type_id", insertable=false, updatable=false)
	public String getImportTypeId() {
		return importTypeId;
	}

	public void setImportTypeId(String importTypeId) {
		this.importTypeId = importTypeId;
	}


	@Column(name="import_filter_name")
	public String getImportFilterName() {
		return importFilterName;
	}

	public void setImportFilterName(String importFilterName) {
		this.importFilterName = importFilterName;
	}

	@Column(name="import_filter_desc")
	public String getImportFilterDesc() {
		return importFilterDesc;
	}

	public void setImportFilterDesc(String importFilterDesc) {
		this.importFilterDesc = importFilterDesc;
	}

	@Column(name="filter_value")
	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
/*
	@OneToMany
	@JoinColumn(name="import_filter_id")
	public Set<ImportFilter> getFilters() {
		return filters;
	}

	public void setFilters(Set<ImportFilter> filters) {
		this.filters = filters;
	}
*/
	@ManyToOne
	@JoinColumn(name="import_type_id")
	public ImportType getImportType() {
		return importType;
	}

	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	@OneToMany
	@JoinColumn(name="import_filter_id")
	public Set<ImportColumn> getColumns() {
		return columns;
	}

	public void setColumns(Set<ImportColumn> columns) {
		this.columns = columns;
	}

	@Transient
	public String getFilterNameId() {
		return filterNameId;
	}

	public void setFilterNameId(String filterNameId) {
		this.filterNameId = filterNameId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "import_filter_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return importFilterId=IDGenerator.generate(this);
	}

}
