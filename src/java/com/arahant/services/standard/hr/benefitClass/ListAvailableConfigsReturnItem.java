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
package com.arahant.services.standard.hr.benefitClass;

import com.arahant.beans.HrBenefitConfig;
import com.arahant.business.BAgency;
import com.arahant.business.BHRBenefitConfig;

/**
 * 
 *
 *
 */
public class ListAvailableConfigsReturnItem {

    public ListAvailableConfigsReturnItem() {
        ;
    }
    private String configName;
    private String configId;
    private String benefitName;
    private String coverage;

    ListAvailableConfigsReturnItem(HrBenefitConfig bc) {
        this.benefitName = bc.getHrBenefit().getName();
        this.configId = bc.getBenefitConfigId();
        this.configName = bc.getName();
        BHRBenefitConfig con = new BHRBenefitConfig(bc);
        this.coverage = con.getCoverage();
    }
    private String[] item;

    public String[] getItem() {
        return item;
    }

    public void setItem(String[] item) {
        this.item = item;
    }

    /**
     * @return the configName
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * @param configName the configName to set
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * @return the configId
     */
    public String getConfigId() {
        return configId;
    }

    /**
     * @param configId the configId to set
     */
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    /**
     * @return the benefitName
     */
    public String getBenefitName() {
        return benefitName;
    }

    /**
     * @param benefitName the benefitName to set
     */
    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    /**
     * @return the coverage
     */
    public String getCoverage() {
        return coverage;
    }

    /**
     * @param coverage the coverage to set
     */
    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }
}

	
