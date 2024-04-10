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
 *
 * Created on Feb 20, 2007
*/

package com.arahant.services.standard.project.projectSummary;

import com.arahant.beans.Address;
import com.arahant.beans.Person;
import com.arahant.beans.ProjectShift;
import com.arahant.business.BEmployee;
import com.arahant.business.BProject;
import com.arahant.business.BProjectShift;
import com.arahant.services.TransmitReturnBase;
import org.kissweb.StringUtils;
import java.util.List;

public class LoadSummaryReturn extends TransmitReturnBase {

	private boolean accessibleToAll;
	private String projectCategoryCode;
	private String projectTypeCode;
	private String description;
	private String reference;
	//private String requestingCompanyId;
	private String requestingCompanyName;
	private int dateReported;
	private int timeReported;
	private String billable;
	private String requesterName;
	private LoadSummaryReturnItem [] item;
	//private String orgGroupId;
	private String orgGroupName;
	private String sponsorNameFormatted;
	private String projectCode;
	private String managerName;
	private String managerId;
	private int estimatedFirstDate;
	private int estimatedLastDate;
	private String projectStreet1;
    private String projectStreet2;
    private String projectCity;
	private String projectState;
    private String projectZipCode;
    private short requiredWorkers;
    private String storeNumber;
    private String locationDescription;

	public LoadSummaryReturn() {
	}

	/**
	 * @return Returns the orgGroupName.
	 */
	public String getOrgGroupName() {
		return orgGroupName;
	}
	/**
	 * @param orgGroupName The orgGroupName to set.
	 */
	public void setOrgGroupName(final String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}

	public String getSponsorNameFormatted() {
		return sponsorNameFormatted;
	}

	public void setSponsorNameFormatted(String sponsorNameFormatted) {
		this.sponsorNameFormatted = sponsorNameFormatted;
	}

	void setData(final BProject bp, String projectShiftId) {
        projectCategoryCode = bp.getProjectCategoryCode();
        projectTypeCode = bp.getProjectTypeCode();
        reference = bp.getReference();
        dateReported = bp.getDateReported();
        timeReported = bp.getTimeReported();
        requesterName = bp.getRequesterName();
        requestingCompanyName = bp.getCompanyName();
        description = bp.getDescription();
        billable = bp.getBillable() + "";
        orgGroupName = bp.getOrgGroupName();
        sponsorNameFormatted = bp.getSponsorNameFormatted();
        accessibleToAll = bp.getAllEmployees() == 'Y';
        projectCode = bp.getProjectCode();
        estimatedFirstDate = bp.getEstimatedFirstDate();
        estimatedLastDate = bp.getEstimatedLastDate();
        BEmployee bemp = bp.getManagingEmployee();
        if (bemp != null) {
            managerId = bemp.getPersonId();
            managerName = bemp.getNameLFM();
        } else {
            managerName = "";
            managerId = "";
        }

		List<Person> persons = bp.getAssignedPersons2(null);
        item = new LoadSummaryReturnItem[persons.size()];
        for (int loop = 0; loop < persons.size(); loop++)
            item[loop] = new LoadSummaryReturnItem(persons.get(loop));

        Address bad = bp.getAddress();
        if (bad == null) {
            projectStreet1 = "";
            projectStreet2 = "";
            projectCity = "";
            projectState = bp.getProjectState();
            projectZipCode = "";
        } else {
            projectStreet1 = bad.getStreet();
            projectStreet2 = bad.getStreet2();
            projectCity = bad.getCity();
            projectState = bad.getState();
            projectZipCode = bad.getZip();
        }
        storeNumber = bp.getStoreNumber();
        String ld = bp.getLocationDescription();
        if ((ld == null || ld.isEmpty()) && requestingCompanyName != null && !requestingCompanyName.isEmpty()) {
        	int idx = requestingCompanyName.indexOf(' ');
        	if (idx < 0)
        		idx = requestingCompanyName.length();
        	ld = StringUtils.take(requestingCompanyName, idx);
		}
        locationDescription = ld;

		if (projectShiftId != null && !projectShiftId.isEmpty()) {
			BProjectShift bps = new BProjectShift(projectShiftId);
			requiredWorkers = bps.getRequiredWorkers();
		} else {
			List<ProjectShift> shifts = bp.getAllShifts();
			short rw = 0;
			for (ProjectShift shift : shifts)
				rw += shift.getRequiredWorkers();
			requiredWorkers = rw;
		}
    }

	/**
	 * @return Returns the billable.
	 */
	public String getBillable() {
		return billable;
	}
	/**
	 * @param billable The billable to set.
	 */
	public void setBillable(final String billable) {
		this.billable = billable;
	}

	/**
	 * @return Returns the dateReported.
	 */
	public int getDateReported() {
		return dateReported;
	}
	/**
	 * @param dateReported The dateReported to set.
	 */
	public void setDateReported(final int dateReported) {
		this.dateReported = dateReported;
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
	 * @return Returns the projectCategoryCode.
	 */
	public String getProjectCategoryCode() {
		return projectCategoryCode;
	}
	/**
	 * @param projectCategoryCode The projectCategoryCode to set.
	 */
	public void setProjectCategoryCode(final String projectCategoryCode) {
		this.projectCategoryCode = projectCategoryCode;
	}
	
	/**
	 * @return Returns the projectTypeCode.
	 */
	public String getProjectTypeCode() {
		return projectTypeCode;
	}
	/**
	 * @param projectTypeCode The projectTypeCode to set.
	 */
	public void setProjectTypeCode(final String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
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
	/**
	 * @return Returns the requestingCompanyId.
	 *
	public String getRequestingCompanyId() {
		return requestingCompanyId;
	}
	/*
	public void setRequestingCompanyId(final String requestingCompanyId) {
		this.requestingCompanyId = requestingCompanyId;
	}*/
	/**
	 * @return Returns the requestingCompanyName.
	 */
	public String getRequestingCompanyName() {
		return requestingCompanyName;
	}
	/**
	 * @param requestingCompanyName The requestingCompanyName to set.
	 */
	public void setRequestingCompanyName(final String requestingCompanyName) {
		this.requestingCompanyName = requestingCompanyName;
	}

	public boolean getAccessibleToAll() {
		return accessibleToAll;
	}

	public void setAccessibleToAll(boolean accessibleToAll) {
		this.accessibleToAll = accessibleToAll;
	}

	public LoadSummaryReturnItem[] getItem() {
		return item;
	}

	public void setItem(LoadSummaryReturnItem[] item) {
		this.item = item;
	}

	public int getTimeReported() {
		return timeReported;
	}

	public void setTimeReported(int timeReported) {
		this.timeReported = timeReported;
	}

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
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

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
}

	
