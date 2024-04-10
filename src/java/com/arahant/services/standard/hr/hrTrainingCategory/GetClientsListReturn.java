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
import com.arahant.services.TransmitReturnBase;

public class GetClientsListReturn extends TransmitReturnBase {

    private GetClientsListItem[] getClientsListItems;

    public GetClientsListItem[] getGetClientsListItems() {
        return getClientsListItems;
    }

    public void setGetClientsListItems(GetClientsListItem[] getClientsListItems) {
        this.getClientsListItems = getClientsListItems;
    }

    public void setGetClientsListItems(CompanyDetail tenant, BClientCompany[] clientCompanies) {
        this.getClientsListItems = new GetClientsListItem[clientCompanies.length + 1];
        // Add the tenant as the first item.
        this.getClientsListItems[0] = new GetClientsListItem(tenant);

        int i = 1;
        for (BClientCompany company : clientCompanies) {
            GetClientsListItem clientsListItem = new GetClientsListItem(company);
            this.getClientsListItems[i++] = clientsListItem;
        }
    }
}
