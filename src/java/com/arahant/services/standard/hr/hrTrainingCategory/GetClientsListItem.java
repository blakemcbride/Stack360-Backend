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

package com.arahant.services.standard.hr.hrTrainingCategory;

import com.arahant.beans.CompanyDetail;
import com.arahant.business.BClientCompany;

public class GetClientsListItem {

    private String clientId;
    private String clientName;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public GetClientsListItem(CompanyDetail tenant) {
        this.clientId = null;
        this.clientName = tenant.getName();
    }

    public GetClientsListItem(BClientCompany bClientCompany) {
        this.clientId = bClientCompany.getId();
        this.clientName = bClientCompany.getName();
    }
}
