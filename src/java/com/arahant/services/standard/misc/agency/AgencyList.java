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
 * Created on Oct 9, 2009
 *
 */
package com.arahant.services.standard.misc.agency;

import com.arahant.business.BAgency;

/**
 *
 *
 * Created on Oct 9, 2009
 *
 */
public class AgencyList {

    public AgencyList() {
        super();
    }
    private String id;
    private String agencyName;
    private String agencyIdentifier;
    private String agencyPhone;
    private String primaryAgentLastName;
    private String primaryAgentFirstName;
    private String primaryAgentPhone;

    /**
     * @param company
     */
    AgencyList(final BAgency a) {
        super();
        id = a.getOrgGroupId();
        agencyName = a.getName();
        agencyIdentifier = a.getIdentifier();
        agencyPhone = a.getMainPhoneNumber();
        primaryAgentFirstName = a.getMainContactFname();
        primaryAgentLastName = a.getMainContactLname();
        primaryAgentPhone = a.getMainContactWorkPhone();
    }

    public String getAgencyIdentifier() {
        return agencyIdentifier;
    }

    public void setAgencyIdentifier(String agencyIdentifier) {
        this.agencyIdentifier = agencyIdentifier;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyPhone() {
        return agencyPhone;
    }

    public void setAgencyPhone(String agencyPhone) {
        this.agencyPhone = agencyPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrimaryAgentFirstName() {
        return primaryAgentFirstName;
    }

    public void setPrimaryAgentFirstName(String primaryAgentFirstName) {
        this.primaryAgentFirstName = primaryAgentFirstName;
    }

    public String getPrimaryAgentLastName() {
        return primaryAgentLastName;
    }

    public void setPrimaryAgentLastName(String primaryAgentLastName) {
        this.primaryAgentLastName = primaryAgentLastName;
    }

    public String getPrimaryAgentPhone() {
        return primaryAgentPhone;
    }

    public void setPrimaryAgentPhone(String primaryAgentPhone) {
        this.primaryAgentPhone = primaryAgentPhone;
    }
}

