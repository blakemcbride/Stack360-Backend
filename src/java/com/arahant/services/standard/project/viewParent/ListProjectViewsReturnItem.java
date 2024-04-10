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
package com.arahant.services.standard.project.viewParent;
import com.arahant.business.BProjectViewJoin;


/**
 * 
 *
 *
 */
public class ListProjectViewsReturnItem {
	
	public ListProjectViewsReturnItem()
	{
		
	}

	ListProjectViewsReturnItem (BProjectViewJoin bc)
	{
		
		id=bc.getId();
		typeFormatted=bc.getTypeFormatted();
		summary=bc.getSummary();
		description=bc.getDescription();
		projectId=bc.getProjectId();
		type=bc.getType();
		hasSubProjectViews=bc.getHasSubProjectViews();

	}
	
	private String id;
	private String typeFormatted;
	private String summary;
	private String description;
	private String projectId;
	private int type;
	private String projectName;
	private boolean hasSubProjectViews;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getTypeFormatted()
	{
		return typeFormatted;
	}
	public void setTypeFormatted(String typeFormatted)
	{
		this.typeFormatted=typeFormatted;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary=summary;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public int getType()
	{
		return type;
	}

	public boolean getHasSubProjectViews() {
		return hasSubProjectViews;
	}

	public void setHasSubProjectViews(boolean hasSubProjectViews) {
		this.hasSubProjectViews = hasSubProjectViews;
	}
	public void setType(int type)
	{
		this.type=type;
	}

}

	
