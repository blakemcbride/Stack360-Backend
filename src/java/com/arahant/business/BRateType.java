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

package com.arahant.business;

import com.arahant.beans.AIProperty;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.RateType;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

import java.util.List;

/**
 * Author: Blake McBride
 * Date: 11/19/16
 */
public class BRateType extends SimpleBusinessObjectBase<RateType> {

    public BRateType() {
    }

    public BRateType(final String key) throws ArahantException {
        internalLoad(key);
    }

    public BRateType(final RateType bean) {
        this.bean = bean;
    }


    public String getRateTypeId() {
        return bean.getRateTypeId();
    }

    public String getCode() {
        return bean.getRateCode();
    }
    
    public void setCode(final String code) {
        bean.setRateCode(code);
    }
    
    public String getDescription() {
        return bean.getDescription();
    }

    public void setDescription(String description) {
        bean.setDescription(description);
    }

    public RateType getRateType() {
        return bean;
    }

    public void setRateType(final char c) {
        bean.setRateType(c);
    }

    public String getId() {
        return bean.getRateTypeId();
    }


    public int getLastActiveDate() {
        return bean.getLastActiveDate();
    }

    public void setLastActiveDate(int lastActiveDate) {
        bean.setLastActiveDate(lastActiveDate);
    }

    public String getCompanyId() {
        return bean.getCompanyId();
    }

    public void setCompanyId(String companyId) {
        bean.setCompanyId(companyId);
    }
    private void internalLoad(final String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(RateType.class, key);
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().update(bean);
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(bean);
    }

    @Override
    public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
        ArahantSession.getHSU().delete(bean);
    }

    @Override
    public String create() throws ArahantException {
        bean = new RateType();
        bean.generateId();
        return getRateTypeId();
    }

    @Override
    public void load(String key) throws ArahantException {
        internalLoad(key);
    }

    public static BRateType[] list(final char type) {
        final char[] types ={type,'B'};
        final HibernateCriteriaUtil<RateType> hcu = ArahantSession.getHSU().createCriteria(RateType.class)
                .in(RateType.TYPE, types)
                .orderBy(RateType.TYPE)
                .orderBy(RateType.RATE_CODE);
        return makeArray(hcu.list());
    }

    public static BRateType[] list() {
        final HibernateCriteriaUtil<RateType> hcu = ArahantSession.getHSU().createCriteria(RateType.class)
                .orderBy(RateType.TYPE)
                .orderBy(RateType.RATE_CODE);

        List<String> restrictedList = AIProperty.getList("RestrictedRateTypes");
        if (restrictedList.size()>0)
            hcu.in(RateType.ID, restrictedList);
        return makeArray(hcu.list());
    }

    static BRateType[] makeArray(final List<RateType> l) {
        final BRateType[] ret = new BRateType[l.size()];
        for (int loop = 0; loop < l.size(); loop++)
            ret[loop] = new BRateType(l.get(loop));
        return ret;
    }

    public static void delete(final String[] ids) throws ArahantException {
        for (final String element : ids)
            new BRateType(element).delete();
    }

}
