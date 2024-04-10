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
package com.arahant.services.standard.inventory.productAttribute;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProductAttribute;
import java.util.LinkedList;
import java.util.List;
import com.arahant.annotation.Validation;


public class SaveAttributeInput extends TransmitInputBase {

	void setData(BProductAttribute ba)
	{
		ba.setId(id);
		ba.setChoiceType(choiceType);
		ba.setInactiveDate(inactiveDate);
		ba.setAttribute(attribute);

                List<String> ids=new LinkedList<String>();

		for (SaveAttributeInputItem i : getListChoice())
		{
			if (isEmpty(i.getId()))
				ids.add(ba.addChoice(i.getName()));
			else
				ids.add(ba.saveChoice(i.getId(),i.getName()));

		}
	}
	
	@Validation (required=false)
	private SaveAttributeInputItem [] listChoice;
	@Validation (required=false)
	private String id;
	@Validation (required=true)
	private String choiceType;
	@Validation (type="date",required=false)
	private int inactiveDate;
	@Validation (table="product_attribute",column="attribute",required=true)
	private String attribute;
	

	public SaveAttributeInputItem [] getListChoice()
	{
		if (listChoice==null)
			listChoice= new SaveAttributeInputItem [0];
		return listChoice;
	}
	public void setListChoice(SaveAttributeInputItem [] listAnswer)
	{
		this.listChoice=listAnswer;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getChoiceType()
	{
		return choiceType;
	}
	public void setChoiceType(String answerType)
	{
		this.choiceType=answerType;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}
	public String getAttribute()
	{
		return attribute;
	}
	public void setAttribute(String attribute)
	{
		this.attribute=attribute;
	}

}

	
