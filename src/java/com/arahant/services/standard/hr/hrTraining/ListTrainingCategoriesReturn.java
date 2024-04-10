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
package com.arahant.services.standard.hr.hrTraining;

import com.arahant.business.BHRTrainingCategory;
import com.arahant.services.TransmitReturnBase;

/**
 * Created on Feb 22, 2007
 * Modified on Dec 14, 2018
 */
public class ListTrainingCategoriesReturn extends TransmitReturnBase {

    private ListTrainingCategoryItem[] item;

    public ListTrainingCategoryItem[] getItem() {
        return item;
    }

    public void setItem(final ListTrainingCategoryItem[] item) {
        this.item = item;
    }

    void setItem(final BHRTrainingCategory[] category) {
        item = new ListTrainingCategoryItem[category.length];
        for (int loop = 0; loop < category.length; loop++)
            item[loop] = new ListTrainingCategoryItem(category[loop]);
    }
}

	
