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

package com.arahant.business;

import com.arahant.beans.ServiceSubscribed;
import com.arahant.beans.ServiceSubscribedJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BServiceSubscribedJoin extends SimpleBusinessObjectBase<ServiceSubscribedJoin> {

    public static BServiceSubscribedJoin[] makeArray(List<ServiceSubscribedJoin> l) {
        BServiceSubscribedJoin[] bj = new BServiceSubscribedJoin[l.size()];

        for (int loop = 0; loop < bj.length; loop++) {
            bj[loop] = new BServiceSubscribedJoin(l.get(loop));
        }

        return bj;
    }

    public BServiceSubscribedJoin() {
    }

    public BServiceSubscribedJoin(ServiceSubscribedJoin o) {
        bean = o;
    }

    public BServiceSubscribedJoin(String key) {
        super(key);
    }

    public BServiceSubscribedJoin(String[] serviceIds) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String create() throws ArahantException {
        bean = new ServiceSubscribedJoin();
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ServiceSubscribedJoin.class, key);
    }

    public void setService(ServiceSubscribed service) {
        bean.setService(service);
    }

    public void setCompany(BCompany company) {
        bean.setCompany(company.getBean());
    }
	public String getExternalId(){
		return bean.getExternalId();
	}
	public void getExternalId(String id){
		bean.setExternalId(id);
	}
    public static BServiceSubscribedJoin[] searchSubscribedServices(final String name, int cap) {

        final HibernateCriteriaUtil<ServiceSubscribedJoin> hcu = ArahantSession.getHSU().createCriteria(ServiceSubscribedJoin.class).eq(ServiceSubscribedJoin.COMPANY, ArahantSession.getHSU().getCurrentCompany()).joinTo(ServiceSubscribedJoin.SERVICE).orderBy(ServiceSubscribed.SERVICENAME).like(ServiceSubscribed.SERVICENAME, name);


        hcu.setMaxResults(cap);

        return BServiceSubscribedJoin.makeArray(hcu.list());
    }

    void setServiceId(String id) {
        bean.setService(ArahantSession.getHSU().get(ServiceSubscribed.class, id));
    }

    void setLastDate(int date) {
        bean.setLastDate(date);
    }

    void setFirstDate(int date) {
        bean.setFirstDate(date);
    }

    public BServiceSubscribed getService() {
        return new BServiceSubscribed(bean.getService());
    }

    public int getBeginDate() {
        return bean.getFirstDate();
    }

    public int getEndDate() {
        return bean.getLastDate();
    }
}
