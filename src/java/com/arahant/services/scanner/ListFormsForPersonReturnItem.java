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
package com.arahant.services.scanner;
import com.arahant.business.BPersonForm;


/**
 * 
 *
 *
 */
public class ListFormsForPersonReturnItem {
	
	public ListFormsForPersonReturnItem()
	{
		;
	}

	ListFormsForPersonReturnItem (BPersonForm bc)
	{
		
		formId=bc.getId();
		formCode=bc.getFormCode();
		formDescription=bc.getDescription();
		formDateFormatted=bc.getDateFormatted();
		formComments=bc.getComments();

	}
	
	private String formId;
	private String formCode;
	private String formDescription;
	private String formDateFormatted;
	private String formComments;

	public String getFormId()
	{
		return formId;
	}
	public void setFormId(String formId)
	{
		this.formId=formId;
	}
	public String getFormCode()
	{
		return formCode;
	}
	public void setFormCode(String formCode)
	{
		this.formCode=formCode;
	}
	public String getFormDescription()
	{
		return formDescription;
	}
	public void setFormDescription(String formDescription)
	{
		this.formDescription=formDescription;
	}
	public String getFormDateFormatted()
	{
		return formDateFormatted;
	}
	public void setFormDateFormatted(String formDateFormatted)
	{
		this.formDateFormatted=formDateFormatted;
	}

	public String getFormComments() {
		return formComments;
	}
	public void setFormComments(String formComments) {
		this.formComments = formComments;
	}
}

	
