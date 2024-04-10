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
/**
 *
 */
@Entity
@Table(name=ImportType.TABLE_NAME)
public class ImportType extends Filtered implements Serializable {

	public static final String TABLE_NAME="import_type";

	public static final char DELIMITED_FILE='D';
	public static final char FIXED_LENGTH_FILE='F';

	public static final String COLUMNS="columns";
	public static final String FILTERS="filters";
	public static final String IMPORT_NAME="importName";
	public static final String ID="importTypeId";

	private String importTypeId;
	private String importProgramName;
	private String importName;
	private String importSource;
	private char fileFormat;
	private char delimChar;
	private char quoteChar;
	private int filterStartPos;
	private int filterEndPos;

	private static String[] dateFormat = {"MM/DD/YYYY", "MM-DD-YYYY", "MM/DD/YY", "MM-DD-YY"};
	private static String[] program = {"DRC Benefit Import"};

	private Set<ImportColumn> columns=new HashSet<ImportColumn>();
	private Set<ImportFilter> filters=new HashSet<ImportFilter>();

	@Id
	@Column(name="import_type_id")
	public String getImportTypeId() {
		return importTypeId;
	}

	public void setImportTypeId(String importTypeId) {
		this.importTypeId = importTypeId;
	}

	@Column(name="filter_end_pos")
	public int getFilterEndPos() {
		return filterEndPos;
	}

	public void setFilterEndPos(int filterEndPos) {
		this.filterEndPos = filterEndPos;
	}

	@Column(name="filter_start_pos")
	public int getFilterStartPos() {
		return filterStartPos;
	}

	public void setFilterStartPos(int filterStartPos) {
		this.filterStartPos = filterStartPos;
	}

	@Column(name="import_program_name")
	public String getImportProgramName() {
		return importProgramName;
	}

	public void setImportProgramName(String importProgramName) {
		this.importProgramName = importProgramName;
	}

	@Column(name="import_name")
	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	@Column(name="import_source")
	public String getImportSource() {
		return importSource;
	}

	public void setImportSource(String importSource) {
		this.importSource = importSource;
	}

	@Column(name="file_format")
	public char getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(char fileFormat) {
		this.fileFormat = fileFormat;
	}

	@Column(name="delim_char")
	public char getDelimChar() {
		return delimChar;
	}

	public void setDelimChar(char delimChar) {
		this.delimChar = delimChar;
	}

	@Column(name="quote_char")
	public char getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}

	@OneToMany
	@JoinColumn(name="import_type_id")
	public Set<ImportColumn> getColumns() {
		return columns;
	}

	public void setColumns(Set<ImportColumn> columns) {
		this.columns = columns;
	}

	@OneToMany
	@JoinColumn(name="import_type_id")
	public Set<ImportFilter> getFilters() {
		return filters;
	}

	public void setFilters(Set<ImportFilter> filters) {
		this.filters = filters;
	}

	public static String[] getDateFormat() {
		return dateFormat;
	}

	public static void setDateFormat(String[] dateFormat) {
		ImportType.dateFormat = dateFormat;
	}

	public static String[] getProgram() {
		return program;
	}

	public static void setProgram(String[] program) {
		ImportType.program = program;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "import_type_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return importTypeId=IDGenerator.generate(this);
	}

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}
}
