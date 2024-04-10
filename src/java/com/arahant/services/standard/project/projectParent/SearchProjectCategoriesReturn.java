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
package com.arahant.services.standard.project.projectParent;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProjectCategory;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

/**
 * 
 *
 *
 */
public class SearchProjectCategoriesReturn extends TransmitReturnBase {

    private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
    private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
    private String name;


    private SearchProjectCategoriesReturnItem[] projectCategories;
    private SearchProjectCategoriesReturnItem selectedItem;

    public SearchProjectCategoriesReturnItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SearchProjectCategoriesReturnItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * @return Returns the projectCategories.
     */
    public SearchProjectCategoriesReturnItem[] getProjectCategories() {
        return projectCategories;
    }

    /**
     * @param projectCategories The projectCategories to set.
     */
    public void setProjectCategories(final SearchProjectCategoriesReturnItem[] projectCategories) {
        this.projectCategories = projectCategories;
    }

    public SearchProjectCategoriesReturn() {
        super();
    }

    /**
     * @param categories
     */
    void setProjectCategories(final BProjectCategory[] cat) {
        projectCategories = new SearchProjectCategoriesReturnItem[cat.length];
        for (int loop = 0; loop < cat.length; loop++) {
            projectCategories[loop] = new SearchProjectCategoriesReturnItem(cat[loop]);
        }
    }

    public String getName() {
        if (selectedItem==null)
            return "(none)";

        name = getSelectedItem().getCode();

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

	
