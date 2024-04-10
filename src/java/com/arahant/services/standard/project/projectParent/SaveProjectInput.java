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
package com.arahant.services.standard.project.projectParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProject;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProjectInput extends TransmitInputBase {

	void setData(BProject bc)
	{

            bc.copyFromAndDelete(deleteId);
            bc.setProjectId(projectId);
            bc.setDescription(summary);
            
            //if (isEmpty(orgGroupId))
                //bc.setOrgGroupId(companyId);
            //else
                //bc.setOrgGroupId(orgGroupId);

            //bc.setProjectCategoryId(categoryId);
            //bc.setProjectTypeId(typeId);
	
	}
	
	@Validation (required=false)
	private String projectId;
	@Validation (required=false, table="project",column="description")
	private String summary;
	@Validation (required=false)
	private String companyId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private String categoryId;
	@Validation (required=false)
	private String typeId;
	@Validation (required=false)
	private String deleteId;
	@Validation (required=false, table="project",column="detail_desc")
	private String detail;

	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary=summary;
	}

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
	}
        
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getTypeId()
	{
		return typeId;
	}
	public void setTypeId(String typeId)
	{
		this.typeId=typeId;
	}
	public String getDeleteId()
	{
		return deleteId;
	}
	public void setDeleteId(String deleteId)
	{
		this.deleteId=deleteId;
	}

}

	
