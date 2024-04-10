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
import com.arahant.services.TransmitReturnBase;

public class LoadDocumentReturn extends TransmitReturnBase {

	void setData(BCompanyForm bcf)
	{
		
		comment=bcf.getComments();
		firstActiveDate=bcf.getFirstActiveDate();
		lastActiveDate=bcf.getLastActiveDate();
		formTypeId=bcf.getFormType().getFormTypeId();
                signitureRequired = bcf.getBean().getElectronicSignature() == 'Y' ? true : false;

	}
	
	private String comment;
	private int firstActiveDate;
	private int lastActiveDate;
	private String formTypeId;
	private boolean signitureRequired;

    public boolean isSignitureRequired() {
        return signitureRequired;
    }

    public void setSignitureRequired(boolean signitureRequired) {
        this.signitureRequired = signitureRequired;
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
	public String getFormTypeId()
	{
		return formTypeId;
	}
	public void setFormTypeId(String formTypeId)
	{
		this.formTypeId=formTypeId;
	}

}

	
