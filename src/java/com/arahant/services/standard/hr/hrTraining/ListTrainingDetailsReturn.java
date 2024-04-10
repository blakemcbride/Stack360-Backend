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

import com.arahant.beans.OrgGroup;
import com.arahant.business.BHRTrainingCategory;
import com.arahant.business.BHRTrainingDetail;
import com.arahant.services.TransmitReturnBase;

/**
 * Created on Feb 25, 2007
 * Modified on Dec 12, 2018
 */
public class ListTrainingDetailsReturn extends TransmitReturnBase {

    private ListTrainingDetailsItem[] item;

    /**
     * @return Returns the item.
     */
    public ListTrainingDetailsItem[] getItem() {
        return item;
    }

    /**
     * @param item The item to set.
     */
    public void setItem(final ListTrainingDetailsItem[] item) {
        this.item = item;
    }

    public ListTrainingDetailsReturn() {
        super();
    }

    /**
     * Sets training details for each <code>{@link ListTrainingDetailsItem}</code>.
     *
     * @param details <code>{@link BHRTrainingDetail}</code> array to return.
     */
    void setItem(final BHRTrainingDetail[] details, OrgGroup orgGroup) {
        item = new ListTrainingDetailsItem[details.length];
        for (int loop = 0; loop < details.length; loop++)
            item[loop] = new ListTrainingDetailsItem(details[loop], orgGroup);
    }
}

	
