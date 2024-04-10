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
package com.arahant.services.standard.project.projectParent;

import com.arahant.annotation.Validation;
import com.arahant.beans.Address;
import com.arahant.beans.RateType;
import com.arahant.business.BProject;
import com.arahant.business.BRateType;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.*;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.Date;


public class NewProjectInput extends TransmitInputBase {

	@Validation (table="project",column="description",required=true)
	private String description;
	@Validation (required=true)
	private String requestingOrgGroupId;
	@Validation (required=true)
	private String projectCategoryId;
	@Validation (required=true)
	private String projectTypeId;
	@Validation (min=0,required=false)
	private String detail;
	@Validation (table="project",column="route_stop_id",required=true)
	private String routeId;
	@Validation (required=false)
	private String parentId;
	@Validation (table="project",column="project_name",required=true)
	private String projectName;
	@Validation(required=true)
    private String rateTypeId;
	@Validation(required=false) // for Flash
	private String state;
	private String city;
	private String store;
	private String shift;
	private String zipcode;
	private String subtype;
	private String locationDescription;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

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

	public NewProjectInput() {
		super();
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getSubtype() {
		return subtype;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public void makeProject(final BProject p) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		p.setProjectTypeId(getProjectTypeId());
		p.setProjectCategoryId( getProjectCategoryId());
		p.setRequestingOrgGroupId(getRequestingOrgGroupId());
		p.setDescription(getDescription());
		p.setDetailDesc(detail);
		p.setRouteId(routeId);
		p.setProjectState(state);
		if (!isEmpty(projectName))
			p.setProjectName(projectName);
		p.setStoreNumber(store);
//  XXYY		p.setShiftStart(shift);
		p.setProjectSubtypeId(subtype);
		p.setLocationDescription(locationDescription);

		if (rateTypeId == null || rateTypeId.isEmpty()) {
			Connection db = KissConnection.get();
			Record rec;
			try {
				rec = db.fetchOne("select rate_type_id from rate_type " +
								"where company_id=? and last_active_date = 0 or last_active_date >= ?",
						hsu.getCurrentCompany().getOrgGroupId(), DateUtils.today());
				if (rec == null)
					throw new ArahantException("no rate type found");
				p.setRateType(new BRateType(rec.getString("rate_type_id")));
			} catch (SQLException throwables) {
				throw new ArahantException(throwables);
			}
		} else {
			RateType rt = hsu.createCriteria(RateType.class).eq(RateType.RATE_TYPE_ID, rateTypeId).first();
			p.setRateType(new BRateType(rt));
		}

		Address ad = new Address();
		ad.generateId();
		ad.setCity(city);
		ad.setState(state);
		ad.setZip(zipcode);
		ad.setAddressType(1);
		hsu.insert(ad);
		p.setAddress(ad);
	}
}

	
