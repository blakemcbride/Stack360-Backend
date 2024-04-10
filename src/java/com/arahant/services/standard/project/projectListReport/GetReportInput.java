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
package com.arahant.services.standard.project.projectListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private boolean includeName;
	@Validation (required=false)
private boolean includeDescription;
	@Validation (required=false)
private boolean includeDateReported;
	@Validation (required=false)
private boolean includeReference;
	@Validation (required=false)
private boolean includeRequestingCompany;
	@Validation (required=false)
private boolean includeCategory;
	@Validation (required=false)
private boolean includeType;
	@Validation (required=false)
private boolean includeStatus;
	@Validation (required=false)
private boolean includeAssignedOrgGroup;
	@Validation (required=false)
private boolean includeAssignedPerson;
	@Validation (required=false)
private boolean sortAsc;
	@Validation (min=1,max=10,required=true)
private int sortType;
	@Validation (min=0,max=3,required=false)
private int statusType;
	@Validation (required=false)
private String [] categoryIds;
	@Validation (required=false)
private String [] typeIds;
	@Validation (required=false)
private String [] statusIds;
	@Validation (required=false)
private String requestingCompanyId;
;

	public boolean getIncludeName()
	{
		return includeName;
	}
	public void setIncludeName(boolean includeName)
	{
		this.includeName=includeName;
	}
	public boolean getIncludeDescription()
	{
		return includeDescription;
	}
	public void setIncludeDescription(boolean includeDescription)
	{
		this.includeDescription=includeDescription;
	}
	public boolean getIncludeDateReported()
	{
		return includeDateReported;
	}
	public void setIncludeDateReported(boolean includeDateReported)
	{
		this.includeDateReported=includeDateReported;
	}
	public boolean getIncludeReference()
	{
		return includeReference;
	}
	public void setIncludeReference(boolean includeReference)
	{
		this.includeReference=includeReference;
	}
	public boolean getIncludeRequestingCompany()
	{
		return includeRequestingCompany;
	}
	public void setIncludeRequestingCompany(boolean includeRequestingCompany)
	{
		this.includeRequestingCompany=includeRequestingCompany;
	}
	public boolean getIncludeCategory()
	{
		return includeCategory;
	}
	public void setIncludeCategory(boolean includeCategory)
	{
		this.includeCategory=includeCategory;
	}
	public boolean getIncludeType()
	{
		return includeType;
	}
	public void setIncludeType(boolean includeType)
	{
		this.includeType=includeType;
	}
	public boolean getIncludeStatus()
	{
		return includeStatus;
	}
	public void setIncludeStatus(boolean includeStatus)
	{
		this.includeStatus=includeStatus;
	}
	public boolean getIncludeAssignedOrgGroup()
	{
		return includeAssignedOrgGroup;
	}
	public void setIncludeAssignedOrgGroup(boolean includeAssignedOrgGroup)
	{
		this.includeAssignedOrgGroup=includeAssignedOrgGroup;
	}
	public boolean getIncludeAssignedPerson()
	{
		return includeAssignedPerson;
	}
	public void setIncludeAssignedPerson(boolean includeAssignedPerson)
	{
		this.includeAssignedPerson=includeAssignedPerson;
	}
	public boolean getSortAsc()
	{
		return sortAsc;
	}
	public void setSortAsc(boolean sortAsc)
	{
		this.sortAsc=sortAsc;
	}
	public int getSortType()
	{
		return sortType;
	}
	public void setSortType(int sortType)
	{
		this.sortType=sortType;
	}
	public int getStatusType()
	{
		return statusType;
	}
	public void setStatusType(int statusType)
	{
		this.statusType=statusType;
	}
	public String [] getCategoryIds()
	{
		if (categoryIds==null)
			categoryIds= new String [0];
		return categoryIds;
	}
	public void setCategoryIds(String [] categoryIds)
	{
		this.categoryIds=categoryIds;
	}
	public String [] getTypeIds()
	{
		if (typeIds==null)
			typeIds= new String [0];
		return typeIds;
	}
	public void setTypeIds(String [] typeIds)
	{
		this.typeIds=typeIds;
	}
	public String [] getStatusIds()
	{
		if (statusIds==null)
			statusIds= new String [0];
		return statusIds;
	}
	public void setStatusIds(String [] statusIds)
	{
		this.statusIds=statusIds;
	}
	public String getRequestingCompanyId()
	{
		return requestingCompanyId;
	}
	public void setRequestingCompanyId(String requestingCompanyId)
	{
		this.requestingCompanyId=requestingCompanyId;
	}


}

	
