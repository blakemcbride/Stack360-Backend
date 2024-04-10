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

import com.arahant.beans.HrTrainingCategory;
import com.arahant.beans.OrgGroup;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

import java.util.List;

public class BHRTrainingCategory extends BusinessLogicBase implements IDBFunctions {

    private HrTrainingCategory hrTrainingCategory;

    public BHRTrainingCategory() {
    }

    public BHRTrainingCategory(final String key) throws ArahantException {
        internalLoad(key);
    }

    public BHRTrainingCategory(final HrTrainingCategory account) {
        hrTrainingCategory = account;
    }

    @Override
    public String create() throws ArahantException {
        hrTrainingCategory = new HrTrainingCategory();
        hrTrainingCategory.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
        hrTrainingCategory.generateId();

        return getTrainingCategoryId();
    }

    @Override
    public void delete() throws ArahantDeleteException {
        ArahantSession.getHSU().delete(hrTrainingCategory);
    }

    public int getLastActiveDate() {
        return hrTrainingCategory.getLastActiveDate();
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(hrTrainingCategory);
    }

    private void internalLoad(final String key) throws ArahantException {
        hrTrainingCategory = ArahantSession.getHSU().get(HrTrainingCategory.class, key);
    }

    @Override
    public void load(final String key) throws ArahantException {
        internalLoad(key);
    }

    public void setLastActiveDate(int lastActiveDate) {
        hrTrainingCategory.setLastActiveDate(lastActiveDate);
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(hrTrainingCategory);
    }

    public String getTrainingCategoryId() {
        return hrTrainingCategory.getCatId();
    }

    public String getName() {
        return hrTrainingCategory.getName();
    }

    public void setTrainingCategoryId(final String TrainingCategoryId) {
        hrTrainingCategory.setCatId(TrainingCategoryId);
    }

    public void setName(final String name) {
        hrTrainingCategory.setName(name);
    }

    public String getClientId() {
        return hrTrainingCategory.getClientId();
    }

    public OrgGroup getClient() {
        return hrTrainingCategory.getClient();
    }

    public BHRTrainingCategory setClient(OrgGroup client) {
        hrTrainingCategory.setClient(client);
        return this;
    }

    public char getRequired() {
        return hrTrainingCategory.getRequired();
    }

    public BHRTrainingCategory setRequired(char required) {
        hrTrainingCategory.setRequired(required);
        return this;
    }

    public float getHours() {
        return hrTrainingCategory.getHours();
    }

    public BHRTrainingCategory setHours(float hours) {
        hrTrainingCategory.setHours(hours);
        return this;
    }

    public HrTrainingCategory getHrTrainingCategory() {
        return hrTrainingCategory;
    }

    public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
        for (final String element : ids)
            new BHRTrainingCategory(element).delete();
    }

    @SuppressWarnings("unchecked")
    public static BHRTrainingCategory[] list(final HibernateSessionUtil hsu) {

        final List l = hsu.createCriteria(HrTrainingCategory.class)
                .orderBy(HrTrainingCategory.NAME)
                .geOrEq(HrTrainingCategory.LASTACTIVEDATE, DateUtils.now(), 0)
                .list();

        return makeArray(l);
    }

    public static BHRTrainingCategory[] list(int activeType, String clientId) {
        HibernateCriteriaUtil<HrTrainingCategory> hcu = ArahantSession.getHSU()
                .createCriteria(HrTrainingCategory.class)
                .orderBy(HrTrainingCategory.NAME);

        if (activeType == 1) //active
            hcu.geOrEq(HrTrainingCategory.LASTACTIVEDATE, DateUtils.now(), 0);
        if (activeType == 2) //inactive
            hcu.ltAndNeq(HrTrainingCategory.LASTACTIVEDATE, DateUtils.now(), 0);

        if (clientId == null)
            hcu.isNull(HrTrainingCategory.CLIENTID);
        else if (clientId.equals(""))
            hcu.isNull(HrTrainingCategory.CLIENTID);
        else
            hcu.eq(HrTrainingCategory.CLIENTID, clientId);

        return makeArray(hcu.list());
    }

    static BHRTrainingCategory[] makeArray(List<HrTrainingCategory> l) {
        final BHRTrainingCategory[] ret = new BHRTrainingCategory[l.size()];
        for (int loop = 0; loop < l.size(); loop++)
            ret[loop] = new BHRTrainingCategory(l.get(loop));
        return ret;
    }

    public short getType() {
        return hrTrainingCategory.getTrainingType();
    }

    public void setType(final short type) {
        hrTrainingCategory.setTrainingType(type);
    }
}
