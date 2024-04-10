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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BImportType;

public class LoadBenefitImportConfigReturn extends TransmitReturnBase {

	void setData(BImportType bc)
	{
		name=bc.getImportName();
		source=bc.getImportSource();
		fileType=bc.getFileFormat();
		programName=bc.getImportProgramName();
		allCompanies=bc.getAllCompanies();
		delimiter=bc.getDelimiter();
		quoteChar=bc.getQuote();
		startPos=""+bc.getFilterStartPos();
		lastPos=""+bc.getFilterEndPos();

		if (bc.getFilterStartPos() == 0)
			noFilter=true;
		else
			noFilter=false;

	}
	
	private String name;
	private String source;
	private String fileType;
	private String programName;
	private boolean allCompanies;
	private String delimiter;
	private String quoteChar;
	private String startPos;
	private String lastPos;
	private boolean noFilter;
	

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

	public String getLastPos() {
		return lastPos;
	}

	public void setLastPos(String lastPos) {
		this.lastPos = lastPos;
	}

	public String getStartPos() {
		return startPos;
	}

	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}

	public boolean getNoFilter() {
		return noFilter;
	}

	public void setNoFilter(boolean noFilter) {
		this.noFilter = noFilter;
	}
}

	
