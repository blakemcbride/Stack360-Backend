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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.annotation.Validation;
import com.arahant.beans.RateType;
import com.arahant.business.BProject;
import com.arahant.business.BRateType;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectInput extends TransmitInputBase {

	
	@Validation (table="project",column="description",required=true)
	private String description;
	@Validation (required=true)
	private String requestingOrgGroupId;
	@Validation (required=true)
	private String projectCategoryId;
	@Validation (required=true)
	private String projectTypeId;
	@Validation (table="project",column="route_stop_id",required=true)
	private String routeId;
	@Validation (min=0,required=false)
	private String detail;
	@Validation (required=false)
	private String parentId;
	@Validation(required=true)
    private String rateTypeId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
			

	public NewProjectInput() {
		super();
	}



	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}



	/**
	 * @return Returns the projectCategoryId.
	 */
	public String getProjectCategoryId() {
		return projectCategoryId;
	}


	/**
	 * @param projectCategoryId The projectCategoryId to set.
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}



	/**
	 * @return Returns the projectTypeId.
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}





	/**
	 * @param projectTypeId The projectTypeId to set.
	 */
	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}


	/**
	 * @return Returns the requestingCompanyId.
	 */
	public String getRequestingOrgGroupId() {
		return requestingOrgGroupId;
	}





	/**
	 * @param requestingCompanyId The requestingCompanyId to set.
	 */
	public void setRequestingOrgGroupId(final String requestingCompanyId) {
		this.requestingOrgGroupId = requestingCompanyId;
	}

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public void makeProject(final BProject p) throws ArahantDeleteException {
				
		p.setProjectTypeId(getProjectTypeId());
		
		p.setProjectCategoryId( getProjectCategoryId());
		
		p.setRequestingOrgGroupId(getRequestingOrgGroupId());
		
		p.setDescription(getDescription());
	
		p.setRouteId(routeId);

		p.setDetailDesc(detail);
        RateType rt = ArahantSession.getHSU().createCriteria(RateType.class).eq(RateType.RATE_TYPE_ID, rateTypeId).first();
        p.setRateType(rt == null ? null : new BRateType(rt));

    }
}

	
