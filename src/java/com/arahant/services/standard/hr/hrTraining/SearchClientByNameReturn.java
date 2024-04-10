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

package com.arahant.services.standard.hr.hrTraining;

import com.arahant.utils.StandardProperty;
import com.arahant.beans.Property;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class SearchClientByNameReturn extends TransmitReturnBase {

    private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
    private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
    private String name = BProperty.get(Property.NAME);
    private SearchClientByNameReturnItem[] clients;
    private SearchClientByNameReturnItem selectedItem;

    public SearchClientByNameReturn() {
        super();
    }

    public int getLowCap() {
        return lowCap;
    }

    public void setLowCap(int lowCap) {
        this.lowCap = lowCap;
    }

    public int getHighCap() {
        return highCap;
    }

    public void setHighCap(int highCap) {
        this.highCap = highCap;
    }

    public String getName() {
        if (selectedItem == null)
            return "(none)";

        name = getSelectedItem().getClientName();

        if (name.isEmpty())
            return "(none)";
        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchClientByNameReturnItem[] getClients() {
        return clients;
    }

    public void setClients(final SearchClientByNameReturnItem[] clients) {
        this.clients = clients;
    }

    public void setClients(final BCompanyBase[] bases) {
        this.clients = new SearchClientByNameReturnItem[bases.length];

        for (int i = 0; i < bases.length; i++) {
            clients[i] = new SearchClientByNameReturnItem(bases[i]);
        }
    }

    public SearchClientByNameReturnItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(final SearchClientByNameReturnItem selectedItem) {
        this.selectedItem = selectedItem;
    }
}
