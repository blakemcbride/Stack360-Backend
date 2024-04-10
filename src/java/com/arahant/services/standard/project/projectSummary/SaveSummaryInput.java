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
package com.arahant.services.standard.project.projectSummary;

import com.arahant.annotation.Validation;
import com.arahant.beans.Address;
import com.arahant.business.BProject;
import com.arahant.business.BProjectShift;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveSummaryInput extends TransmitInputBase{

	@Validation (required=true)
	private String projectId;
	private String projectShiftId;
	@Validation (table="project",column="reference", required=false)
	private String reference;
	@Validation (table="project",column="requester_name", required=false)
	private String requesterName;
	@Validation (required=false)
	private String employeeId;
	@Validation (table="project",column="description", required=true)
	private String description;
	@Validation (table="project",column="project_name", required=true)
	private String projectName;
	@Validation (required=false)
	private boolean accessibleToAll;
	@Validation (table="project",column="project_code", required=false)
	private String projectCode;
    @Validation (table="project",column="managing_employee", required=false)
    private String managerId;
    @Validation(min=0, max=21000101, required=false)
    private int estimatedFirstDate;
	@Validation(min=0, max=21000101, required=false)
    private int estimatedLastDate;
    @Validation (table="address",column="street", required=false)
    private String projectStreet1;
    @Validation (table="address",column="street2", required=false)
    private String projectStreet2;
    @Validation (table="address",column="city", required=false)
    private String projectCity;
	@Validation (max=2, required=false)
	private String projectState;
    @Validation (table="address",column="zip", required=false)
    private String projectZipCode;
    private short requiredWorkers;
    private String storeNumber;
    private String shiftStart;
    private String subTypeId;
    private String projectStatusId;
    private String locationDescription;
	private boolean autoUnassign;

	public SaveSummaryInput() {
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	 * @param bp
	 * @throws ArahantDeleteException 
	 */
	void makeProject(final BProject bp, String projectShiftId) throws ArahantDeleteException {
        bp.setEmployeeId(employeeId);
        bp.setReference(reference);
        bp.setRequesterName(requesterName);
        //bp.setRequestingOrgGroupId(requestingCompanyId);
        bp.setProjectId(projectId);
        bp.setDescription(description);
        bp.setAllEmployees(accessibleToAll ? 'Y' : 'N');
        bp.setProjectCode(projectCode);
        bp.setProjectState(projectState);
        if (managerId != null && !managerId.isEmpty())
            bp.setEmployeeId(managerId);

        if (!isEmpty(projectName))
            bp.setProjectName(projectName);
        bp.setEstimatedFirstDate(estimatedFirstDate);
        bp.setEstimatedLastDate(estimatedLastDate);

        if (!isEmpty(projectStreet1) || !isEmpty(projectStreet2) || !isEmpty(projectCity) || !isEmpty(projectState) || !isEmpty(projectZipCode)) {
            Address add = bp.getAddress();
            if (add == null) {
                add = new Address();
                add.generateId();
                add.setAddressType(1);
                add.setRecordType('R');
                bp.setAddress(add);
            }
            add.setStreet(projectStreet1);
            add.setStreet2(projectStreet2);
            add.setCity(projectCity);
            add.setState(projectState);
            add.setZip(projectZipCode);
            ArahantSession.getHSU().saveOrUpdate(add);
        } else {
            Address add = bp.getAddress();
            if (add != null) {
                bp.setAddress(null);
                ArahantSession.getHSU().delete(add);
            }
        }
        if (storeNumber != null)
        	bp.setStoreNumber(storeNumber);
        if (subTypeId != null && !subTypeId.isEmpty())
			bp.setProjectSubtypeId(subTypeId);
		if (projectStatusId != null && !projectStatusId.isEmpty())
			bp.setProjectStatusId(projectStatusId);
		bp.setLocationDescription(locationDescription);
		if (projectShiftId != null && !projectShiftId.isEmpty()) {
			BProjectShift shift = new BProjectShift(projectShiftId);
			shift.setRequiredWorkers(requiredWorkers);
		}
    }

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	public String getProjectShiftId() {
		return projectShiftId;
	}

	public void setProjectShiftId(String projectShiftId) {
		this.projectShiftId = projectShiftId;
	}

	/**
	 * @return Returns the projectSponsorId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}


	/**
	 * @param projectSponsorId The projectSponsorId to set.
	 */
	public void setEmployeeId(final String projectSponsorId) {
		this.employeeId = projectSponsorId;
	}



	/**
	 * @return Returns the reference.
	 */
	public String getReference() {
		return reference;
	}


	/**
	 * @param reference The reference to set.
	 */
	public void setReference(final String reference) {
		this.reference = reference;
	}

	public boolean getAccessibleToAll() {
		return accessibleToAll;
	}

	public void setAccessibleToAll(boolean accessibleToAll) {
		this.accessibleToAll = accessibleToAll;
	}


	/**
	 * @return Returns the requesterName.
	 */
	public String getRequesterName() {
		return requesterName;
	}


	/**
	 * @param requesterName The requesterName to set.
	 */
	public void setRequesterName(final String requesterName) {
		this.requesterName = requesterName;
	}

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

    public int getEstimatedFirstDate() {
        return estimatedFirstDate;
    }

    public void setEstimatedFirstDate(int estimatedFirstDate) {
        this.estimatedFirstDate = estimatedFirstDate;
    }

    public int getEstimatedLastDate() {
        return estimatedLastDate;
    }

    public void setEstimatedLastDate(int estimatedLastDate) {
        this.estimatedLastDate = estimatedLastDate;
    }

    public String getProjectStreet1() {
        return projectStreet1;
    }

    public void setProjectStreet1(String projectStreet1) {
        this.projectStreet1 = projectStreet1;
    }

    public String getProjectStreet2() {
        return projectStreet2;
    }

    public void setProjectStreet2(String projectStreet2) {
        this.projectStreet2 = projectStreet2;
    }

    public String getProjectCity() {
        return projectCity;
    }

    public void setProjectCity(String projectCity) {
        this.projectCity = projectCity;
    }

    public String getProjectZipCode() {
        return projectZipCode;
    }

    public void setProjectZipCode(String projectZipCode) {
        this.projectZipCode = projectZipCode;
    }

	public short getRequiredWorkers() {
		return requiredWorkers;
	}

	public void setRequiredWorkers(short requiredWorkers) {
		this.requiredWorkers = requiredWorkers;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getShiftStart() {
		return shiftStart;
	}

	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}

	public String getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(String subTypeId) {
		this.subTypeId = subTypeId;
	}

	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public boolean isAutoUnassign() {
		return autoUnassign;
	}

	public void setAutoUnassign(boolean autoUnassign) {
		this.autoUnassign = autoUnassign;
	}
}

	
