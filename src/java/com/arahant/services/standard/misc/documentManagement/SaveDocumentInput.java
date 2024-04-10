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
  
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BCompanyForm;
import com.arahant.annotation.Validation;

public class SaveDocumentInput extends TransmitInputBase {

	void setData(BCompanyForm bc)
	{
		
		bc.setComments(comment);
		bc.setFirstActiveDate(firstActiveDate);
		bc.setLastActiveDate(lastActiveDate);
		bc.setFormTypeId(formTypeId);
                bc.getBean().setElectronicSignature(signitureRequired == true ? 'Y' : 'N');


	}
	
	@Validation (required=true)
	private String comment;
	@Validation (type="date", required=true)
	private int firstActiveDate;
	@Validation (type="date", required=true)
	private int lastActiveDate;
	@Validation (required=true)
	private String formTypeId;
	@Validation(required=true)
	private String id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

	
