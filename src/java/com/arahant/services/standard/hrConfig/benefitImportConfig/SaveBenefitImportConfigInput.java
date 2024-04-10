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
package com.arahant.services.standard.hrConfig.benefitImportConfig;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BImportType;
import com.arahant.annotation.Validation;
import com.arahant.exceptions.ArahantWarning;

public class SaveBenefitImportConfigInput extends TransmitInputBase {

	void setData(BImportType bc)
	{
		if (bc.getFilterStartPos() != 0 && startPos == 0)
			throw new ArahantWarning("Benefit Import Configuration cannot be changed from filtered to non-filtered.");
		else
		{
			bc.setImportName(name);
			bc.setImportSource(source);
			bc.setFileFormat(fileType);
			bc.setDelimiter(delimiter);
			bc.setQuote(quoteChar);
			bc.setImportProgramName(programName);
			bc.setAllCompanies(allCompanies);
			bc.setFilterStartPos(startPos);
			bc.setFilterEndPos(lastPos);
		}
	}
	
	@Validation (table="import_type",column="import_type_id",required=true)
	private String id;
	@Validation (table="import_type",column="import_name",required=true)
	private String name;
	@Validation (table="import_type",column="import_source",required=false)
	private String source;
	@Validation (table="import_type",column="file_format",required=true)
	private String fileType;
	@Validation (max=1,table="import_type",column="delim_char",required=false)
	private String delimiter;
	@Validation (max=1,table="import_type",column="quote_char",required=false)
	private String quoteChar;
	@Validation (table="import_type",column="import_program_name",required=true)
	private String programName;
	@Validation (table="import_type",column="filter_start_pos",required=false)
	private int startPos;
	@Validation (table="import_type",column="filter_end_pos",required=false)
	private int lastPos;
	@Validation (required=false)
	private boolean allCompanies;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source=source;
	}
	public String getFileType()
	{
		return fileType;
	}
	public void setFileType(String fileType)
	{
		this.fileType=fileType;
	}
	public String getDelimiter()
	{
		return delimiter;
	}
	public void setDelimiter(String delimiter)
	{
		this.delimiter=delimiter;
	}
	public String getQuoteChar()
	{
		return quoteChar;
	}
	public void setQuoteChar(String quoteChar)
	{
		this.quoteChar=quoteChar;
	}
	public String getProgramName()
	{
		return programName;
	}
	public void setProgramName(String programName)
	{
		this.programName=programName;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastPos() {
		return lastPos;
	}

	public void setLastPos(int lastPos) {
		this.lastPos = lastPos;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
}

	
