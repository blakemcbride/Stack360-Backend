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
package com.arahant.business;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProductAttribute;
import com.arahant.beans.ProductAttributeChoice;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BProductAttribute extends SimpleBusinessObjectBase<ProductAttribute> {

    public static void delete(String[] ids) {
        for (String id : ids) {
            new BProductAttribute(id).delete();
        }
    }

    @Override
    public void delete()
    {
        ArahantSession.getHSU().createCriteria(ProductAttributeChoice.class)
            .eq(ProductAttributeChoice.PRODUCT_ATTRIBUTE, bean)
            .delete();

        super.delete();
    }

    public static BProductAttribute[] makeArray(List<ProductAttribute> l) {
        BProductAttribute[] ret = new BProductAttribute[l.size()];
        for (int loop = 0; loop < ret.length; loop++) {
            ret[loop] = new BProductAttribute(l.get(loop));
        }
        return ret;
    }

    public BProductAttribute(ProductAttribute o) {
        super();
        bean = o;
    }

    public BProductAttribute(String id) {
        super(id);
    }

    public BProductAttribute() {
        super();
    }

    @Override
    public String create() throws ArahantException {
        bean = new ProductAttribute();
        bean.setAttributeOrder((short)1000);
        bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ProductAttribute.class, key);
    }

    public void setMoveUp(boolean moveUp) {
        if (moveUp) {
            moveUp();
        } else {
            moveDown();
        }
    }

    public void moveUp() {
        if (bean.getAttributeOrder() > 0) {
            ProductAttribute pa = ArahantSession.getHSU().createCriteria(ProductAttribute.class)
                    .eq(ProductAttribute.ATTRIBUTEORDER, (short) (bean.getAttributeOrder() - 1))
                    .first();
            if (pa==null)
                return;
            short temp = bean.getAttributeOrder();
            pa.setAttributeOrder((short) 99999);
            ArahantSession.getHSU().saveOrUpdate(pa);
            ArahantSession.getHSU().flush();
            bean.setAttributeOrder((short) (bean.getAttributeOrder() - 1));
            ArahantSession.getHSU().saveOrUpdate(bean);
            ArahantSession.getHSU().flush();
            pa.setAttributeOrder(temp);
            ArahantSession.getHSU().saveOrUpdate(pa);
        } else //shift them all
        {
            List<ProductAttribute> l = ArahantSession.getHSU().createCriteria(ProductAttribute.class)
                    .orderBy(ProductAttribute.ATTRIBUTEORDER)
                    .list();

            l.get(0).setAttributeOrder((short) 99999);
            ArahantSession.getHSU().saveOrUpdate(l.get(0));
            ArahantSession.getHSU().flush();
            for (int loop = 1; loop < l.size(); loop++) {
                l.get(loop).setAttributeOrder((short) (l.get(loop).getAttributeOrder() - 1));
                ArahantSession.getHSU().saveOrUpdate(l.get(loop));
                ArahantSession.getHSU().flush();
            }

            l.get(0).setAttributeOrder((short) (l.size() - 1));
            ArahantSession.getHSU().saveOrUpdate(l.get(0));
        }
    }

    public void moveDown() {
        if (bean.getAttributeOrder() != ArahantSession.getHSU().createCriteria(ProductAttribute.class)
                .count() - 1) {
            ProductAttribute pa = ArahantSession.getHSU().createCriteria(ProductAttribute.class)
                    .eq(ProductAttribute.ATTRIBUTEORDER, (short) (bean.getAttributeOrder() + 1))
                    .first();
            if (pa==null)
                return;
            short temp = bean.getAttributeOrder();
            pa.setAttributeOrder((short) 999999);
            ArahantSession.getHSU().saveOrUpdate(pa);
            ArahantSession.getHSU().flush();
            bean.setAttributeOrder((short) (bean.getAttributeOrder() + 1));
            ArahantSession.getHSU().saveOrUpdate(bean);
            pa.setAttributeOrder(temp);
            ArahantSession.getHSU().saveOrUpdate(pa);
        } else //shift them all
        {
            List<ProductAttribute> l = ArahantSession.getHSU().createCriteria(ProductAttribute.class)
                    .orderBy(ProductAttribute.ATTRIBUTEORDER)
                    .list();

            l.get(l.size() - 1).setAttributeOrder((short) 99999);
            ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
            ArahantSession.getHSU().flush();

            for (int loop = l.size() - 1; loop > -1; loop--) {
                l.get(loop).setAttributeOrder((short) (loop + 1));
                ArahantSession.getHSU().saveOrUpdate(l.get(loop));
                ArahantSession.getHSU().flush();
            }

            l.get(l.size() - 1).setAttributeOrder((short) 0);
            ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
        }
    }

    public static BProductAttribute[] list(int max) {
        return makeArray(ArahantSession.getHSU().createCriteria(ProductAttribute.class).orderBy(ProductAttribute.ATTRIBUTE).list());
    }

    public static BProductAttribute[] list (String name, int activeType)
    {
        HibernateCriteriaUtil<ProductAttribute> hcu=ArahantSession.getHSU()
                        .createCriteria(ProductAttribute.class)
                        .orderBy(ProductAttribute.ATTRIBUTEORDER);

        switch(activeType)
        {
                case 1 : hcu.eq(ProductAttribute.LAST_ACTIVE_DATE, 0);
                                break;
                case 2 : hcu.ne(ProductAttribute.LAST_ACTIVE_DATE, 0);
                                break;
        }

        return makeArray(hcu.list());
    }

    public String addChoice(String description) {
        BProductAttributeChoice c = new BProductAttributeChoice();
        String ret = c.create();
        c.setDescription(description);
        c.setAttribute(bean);
        addPendingInsert(c);

        return ret;
    }

    public String saveChoice(String id, String description)
    {
        BProductAttributeChoice c=new BProductAttributeChoice(id);
        c.setDescription(description);
        addPendingUpdate(c);

        return id;
    }

    public void setAddAfterId(String addAfterId) {
        if (isEmpty(addAfterId)) {
            bean.setAttributeOrder((short) ArahantSession.getHSU().createCriteria(ProductAttribute.class).count());
        } else {
            BProductAttribute bcq = new BProductAttribute(addAfterId);
            int initialSequence = bcq.bean.getAttributeOrder() + 1;
            bean.setAttributeOrder((short) ArahantSession.getHSU().createCriteria(ProductAttribute.class).count());

            ArahantSession.getHSU().insert(bean);
            //move up until it gets there
            while (bean.getAttributeOrder() != initialSequence) {
                moveUp();
            }
        }

    }
    
    public String getId() {
        return bean.getProductAttributeId();
    }

    public void setInactiveDate(int inactiveDate) {
        bean.setLastActiveDate(inactiveDate);
    }

    public int getInactiveDate() {
        return bean.getLastActiveDate();
    }

    public void setDataType(char dataType) {
        bean.setDataType(dataType);
    }

    public char getDataType() {
        return bean.getDataType();
    }

    public void setChoiceType(String choiceType) {
        bean.setDataType(choiceType.charAt(0));
    }

    public String getChoiceType() {
        return bean.getDataType() + "";
    }

    public void setCompany(CompanyDetail company) {
        bean.setOrgGroup(company);
    }

    public OrgGroup getCompany() {
        return bean.getOrgGroup();
    }

    public void setAttributeOrder(short attributeOrder) {
        bean.setAttributeOrder(attributeOrder);
    }

    public short getAttributeOrder() {
        return bean.getAttributeOrder();
    }

    public void setAttribute(String attribute) {
        bean.setAttribute(attribute);
    }

    public String getAttribute() {
        return bean.getAttribute();
    }

    public void setId(String id) {
        bean.setProductAttributeId(id);
    }
}
