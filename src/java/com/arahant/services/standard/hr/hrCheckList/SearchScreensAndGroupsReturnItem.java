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
package com.arahant.services.standard.hr.hrCheckList;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;

public class SearchScreensAndGroupsReturnItem {
	
	public SearchScreensAndGroupsReturnItem()
	{
		
	}

	SearchScreensAndGroupsReturnItem (BScreenOrGroup bc) {

		if (bc instanceof BScreenGroup)
		{
			final BScreenGroup bsg=(BScreenGroup)bc;
			name=bsg.getName();
			groupId=bsg.getScreenGroupId();
			type = "Group";
			if (bsg.getParentScreenId() == null || bsg.getParentScreenId().length() == 0)
				parentScreenName="";
			else
				parentScreenName=bsg.getParentScreenName();

			screenId="";
			wizardName=bsg.getWizardType().equals("E") ? "Enrollment" : bsg.getWizardType().equals("O") ? "Onboarding" : "N/A";
		}
		else
		{
			final BScreen s=(BScreen)bc;
			name=s.getName();
			screenId=s.getScreenId();
			type="Screen";

			groupId="";
			wizardName="";
			parentScreenName="";
		}
	}
	
	private String name;
	private String type;
	private String screenId;
	private String groupId;
	private String parentScreenName;
	private String wizardName;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	public String getScreenId()
	{
		return screenId;
	}
	public void setScreenId(String screenId)
	{
		this.screenId=screenId;
	}
	public String getGroupId()
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId=groupId;
	}
	public String getParentScreenName()
	{
		return parentScreenName;
	}
	public void setParentScreenName(String parentScreenName)
	{
		this.parentScreenName=parentScreenName;
	}
	public String getWizardName()
	{
		return wizardName;
	}
	public void setWizardName(String wizardName)
	{
		this.wizardName=wizardName;
	}

}

	
