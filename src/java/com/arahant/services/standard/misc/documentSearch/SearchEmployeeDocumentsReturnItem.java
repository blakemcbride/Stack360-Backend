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
 *
 */
package com.arahant.services.standard.misc.documentSearch;
import com.arahant.business.BPerson;
import com.arahant.business.BPersonForm;
import org.kissweb.StringUtils;

public class SearchEmployeeDocumentsReturnItem {
	
	public SearchEmployeeDocumentsReturnItem()
	{
		
	}

	SearchEmployeeDocumentsReturnItem (BPersonForm bc)
	{
		if (StringUtils.isEmpty(bc.getComments()))
			documentName="(No Comment)";
		else
			documentName=bc.getComments();
		documentType=bc.getFormCode();
		documentId=bc.getId();
		employeeName=new BPerson(bc.getPersonId()).getNameFML();
		date=bc.getDate();
		fileExtension=bc.getExtension();
		filename=bc.getSource();
	}
	
	private String documentName;
	private String documentType;
	private String documentId;
	private String employeeName;
	private int date;
	private String fileExtension;
	private String filename;
	

	public String getDocumentName()
	{
		return documentName;
	}
	public void setDocumentName(String documentName)
	{
		this.documentName=documentName;
	}
	public String getDocumentType()
	{
		return documentType;
	}
	public void setDocumentType(String documentType)
	{
		this.documentType=documentType;
	}
	public String getDocumentId()
	{
		return documentId;
	}
	public void setDocumentId(String documentId)
	{
		this.documentId=documentId;
	}
	public String getEmployeeName()
	{
		return employeeName;
	}
	public void setEmployeeName(String employeeName)
	{
		this.employeeName=employeeName;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}

	
