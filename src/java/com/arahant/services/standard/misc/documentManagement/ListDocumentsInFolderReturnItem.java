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
package com.arahant.services.standard.misc.documentManagement;
import com.arahant.business.BCompanyForm;

public class ListDocumentsInFolderReturnItem {
	  
	public ListDocumentsInFolderReturnItem()
	{
		
	}

	ListDocumentsInFolderReturnItem (BCompanyForm bc)
	{
		id=bc.getCompanyFormId();
		comment=bc.getComments();
		firstActiveDate=bc.getFirstActiveDate();
		lastActiveDate=bc.getLastActiveDate();
		isSigned = bc.getBean().getElectronicSignature() == 'Y'? "Yes" : "No";
		extension = bc.getBean().getFileNameExtension();
		filename = bc.getSource();
	}
	
	private String id;
	private String comment;
	private int firstActiveDate;
	private int lastActiveDate;
	private String isSigned;
	private String extension;
	private String filename;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment=comment;
	}
	public int getFirstActiveDate()
	{
		return firstActiveDate;
	}
	public void setFirstActiveDate(int firstActiveDate)
	{
		this.firstActiveDate=firstActiveDate;
	}
	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}

    /**
     * @return the isSigned
     */
    public String getIsSigned() {
        return isSigned;
    }

    /**
     * @param isSigned the isSigned to set
     */
    public void setIsSigned(String isSigned) {
        this.isSigned = isSigned;
    }

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}

	
