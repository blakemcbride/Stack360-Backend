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

package com.arahant.services.standard.misc.interfaceLog;

import com.arahant.utils.StandardProperty;
import com.arahant.beans.Property;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class SearchCompanyByTypeReturn extends TransmitReturnBase {

    private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
    private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
    private String name = BProperty.get(Property.NAME);
    private SearchCompanyByTypeReturnItem[] companies;
    private SearchCompanyByTypeReturnItem selectedItem;

    public SearchCompanyByTypeReturn() {
        super();
    }

    public SearchCompanyByTypeReturnItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SearchCompanyByTypeReturnItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * @return Returns the companies.
     */
    public SearchCompanyByTypeReturnItem[] getCompanies() {
        return companies;
    }

    /**
     * @param companies The companies to set.
     */
    public void setCompanies(final SearchCompanyByTypeReturnItem[] companies) {
        this.companies = companies;
    }

    /**
     * @param bases
     */
    void setCompanies(final BCompanyBase[] bases) {
        companies = new SearchCompanyByTypeReturnItem[bases.length];
        for (int loop = 0; loop < bases.length; loop++) {
            companies[loop] = new SearchCompanyByTypeReturnItem(bases[loop]);
        }
    }

    public String getName() {
        if (selectedItem==null)
            return "(none)";

        name = getSelectedItem().getName();
        
        if (name.isEmpty())
            return "(none)";
        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighCap() {
        return highCap;
    }

    public void setHighCap(int highCap) {
        this.highCap = highCap;
    }

    public int getLowCap() {
        return lowCap;
    }

    public void setLowCap(int lowCap) {
        this.lowCap = lowCap;
    }
}

	
