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

import com.arahant.beans.ProductAttribute;
import com.arahant.beans.ProductAttributeChoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BProductAttributeChoice extends SimpleBusinessObjectBase<ProductAttributeChoice> {

    public BProductAttributeChoice() {
    }

    public BProductAttributeChoice(String id) {
        super(id);
    }

    public BProductAttributeChoice(ProductAttributeChoice o) {
        bean = o;
    }

    @Override
    public String create() throws ArahantException {
        bean = new ProductAttributeChoice();
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ProductAttributeChoice.class, key);
    }

    public static BProductAttributeChoice[] makeArray(List<ProductAttributeChoice> l) {
        BProductAttributeChoice[] ret = new BProductAttributeChoice[l.size()];
        for (int loop = 0; loop < ret.length; loop++) {
            ret[loop] = new BProductAttributeChoice(l.get(loop));
        }

        return ret;
    }

    public static BProductAttributeChoice[] list(String choiceId) {
        return makeArray(ArahantSession.getHSU()
                .createCriteria(ProductAttributeChoice.class)
                .orderBy(ProductAttributeChoice.DESCRIPTION)
                .joinTo(ProductAttribute.ATTRIBUTE)
                .eq(ProductAttribute.ID, choiceId)
                .list());
    }

    public String getId() {
        return bean.getProductAttributeChoiceId();
    }

    public String getDescription() {
        return bean.getDescription();
    }

    public void setDescription(String description) {
        bean.setDescription(description);
    }

    public void setInactiveDate(int inactiveDate) {
        bean.setLastActiveDate(inactiveDate);
    }

    public int getInactiveDate() {
        return bean.getLastActiveDate();
    }

    public short getChoiceOrder() {
        return bean.getChoiceOrder();
    }

    public void setChoiceOrder(short choiceOrder) {
        bean.setChoiceOrder(choiceOrder);
    }

    void setAttribute(ProductAttribute p) {
        bean.setAttribute(p);
    }
}
