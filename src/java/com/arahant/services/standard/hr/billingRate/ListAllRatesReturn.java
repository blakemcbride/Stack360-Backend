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

package com.arahant.services.standard.hr.billingRate;

import com.arahant.beans.EmployeeRate;
import com.arahant.beans.RateType;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.HibernateSessionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListAllRatesReturn extends TransmitReturnBase {

    private ListAllRatesReturnItem [] items;

    public ListAllRatesReturn() {
    }

	void setData(HibernateSessionUtil hsu, List<RateType> rts, String personId, String allowedRateTypeId) {
        List<EmployeeRate> ers = hsu.createCriteria(EmployeeRate.class)
                .eq(EmployeeRate.PERSON, new BPerson(personId).getPerson())
                .list();
        Set<String> erates = new HashSet<>();
        for (EmployeeRate er : ers) {
            String rateTypeId = er.getRateType().getRateTypeId();
            if (!rateTypeId.equals(allowedRateTypeId))
                erates.add(rateTypeId);
        }
        ArrayList<ListAllRatesReturnItem> itms = new ArrayList<>();
        for (RateType rt : rts) {
            String rateTypeId = rt.getRateTypeId();
            if (erates.contains(rateTypeId))
                continue;
            ListAllRatesReturnItem itm = new ListAllRatesReturnItem();
            itm.setCode(rt.getRateCode());
            itm.setDescription(rt.getDescription());
            itm.setRateTypeId(rateTypeId);
            itms.add(itm);
        }
        items = itms.toArray(new ListAllRatesReturnItem[0]);
    }

    public ListAllRatesReturnItem[] getItems() {
        return items;
    }

    public void setItems(ListAllRatesReturnItem[] items) {
        this.items = items;
    }
}

	
