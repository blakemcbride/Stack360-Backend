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
package com.arahant.services.standard.misc.documentSearch;
import com.arahant.business.BCompanyForm;


/**
 * 
 *
 *
 */
public class SearchCompanyDocumentsReturnItem {
	
	public SearchCompanyDocumentsReturnItem()
	{
		
	}

	SearchCompanyDocumentsReturnItem (BCompanyForm bc)
	{
		documentName=bc.getComments();
		documentType=bc.getFormType().getFormCode();
		documentId=bc.getCompanyFormId();
		firstActiveDate=bc.getFirstActiveDate();
		lastActiveDate=bc.getLastActiveDate();
		fileExtension=bc.getExtension();
		filename=bc.getSource();
	}
	
	private String documentName;
	private String documentType;
	private String documentId;
	private int firstActiveDate;
	private int lastActiveDate;
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
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
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

	
