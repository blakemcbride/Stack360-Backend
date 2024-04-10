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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 *
 */
public class LoadQuestionAnswersReturn extends TransmitReturnBase {

	
    private String smoker;
    private String smokerSpouse;
    private String unableToPerform;
    private String activelyAtWork;
    private Boolean hasSpouse;
    private String hasCancer;
    private String hasCancerSpouse;
    private String hasAids;
    private String hasAidsSpouse;
    private String hasHeartCondition;
    private String hasHeartConditionSpouse;


    public void setData(String empId)
    {

        BEmployee be = new BEmployee(empId);
        hasSpouse = be.hasSpouse();
        smoker = be.getSmoker() ? "Y" : "N";
        if(hasSpouse)
        {
            smokerSpouse = be.getSpouse().getPerson().getSmoker() + "";
            unableToPerform = be.getSpouse().getPerson().getUnableToPerform() + "";
			hasAidsSpouse = be.getSpouse().getPerson().getHasAids() + "";
			hasCancerSpouse = be.getSpouse().getPerson().getHasCancer() + "";
			hasHeartConditionSpouse = be.getSpouse().getPerson().getHasHeartCondition() + "";
        }
        else
        {
            unableToPerform = "";
            smokerSpouse = "";
			hasAidsSpouse = "";
			hasCancerSpouse = "";
			hasHeartConditionSpouse = "";
        }
        
        activelyAtWork = be.getActivelyAtWork();
		hasAids = be.getPerson().getHasAids() + "";
		hasCancer = be.getPerson().getHasCancer() + "";
		hasHeartCondition = be.getPerson().getHasHeartCondition() + "";
    }

	public String getHasAids() {
		return hasAids;
	}

	public void setHasAids(String hasAids) {
		this.hasAids = hasAids;
	}

	public String getHasAidsSpouse() {
		return hasAidsSpouse;
	}

	public void setHasAidsSpouse(String hasAidsSpouse) {
		this.hasAidsSpouse = hasAidsSpouse;
	}

	public String getHasCancer() {
		return hasCancer;
	}

	public void setHasCancer(String hasCancer) {
		this.hasCancer = hasCancer;
	}

	public String getHasCancerSpouse() {
		return hasCancerSpouse;
	}

	public void setHasCancerSpouse(String hasCancerSpouse) {
		this.hasCancerSpouse = hasCancerSpouse;
	}

	public String getHasHeartCondition() {
		return hasHeartCondition;
	}

	public void setHasHeartCondition(String hasHeartCondition) {
		this.hasHeartCondition = hasHeartCondition;
	}

	public String getHasHeartConditionSpouse() {
		return hasHeartConditionSpouse;
	}

	public void setHasHeartConditionSpouse(String hasHeartConditionSpouse) {
		this.hasHeartConditionSpouse = hasHeartConditionSpouse;
	}

    public Boolean getHasSpouse() {
        return hasSpouse;
    }

    public void setHasSpouse(Boolean hasSpouse) {
        this.hasSpouse = hasSpouse;
    }

    public String getActivelyAtWork() {
        return activelyAtWork;
    }

    public void setActivelyAtWork(String activelyAtWork) {
        this.activelyAtWork = activelyAtWork;
    }

    public String getSmoker() {
        return smoker;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public String getSmokerSpouse() {
        return smokerSpouse;
    }

    public void setSmokerSpouse(String smokerSpouse) {
        this.smokerSpouse = smokerSpouse;
    }

    public String getUnableToPerform() {
        return unableToPerform;
    }

    public void setUnableToPerform(String unableToPerform) {
        this.unableToPerform = unableToPerform;
    }


	
}

	
