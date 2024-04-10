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
package com.arahant.beans;

import java.io.Serializable;
import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name = ProductAttributeChoice.TABLE_NAME)
public class ProductAttributeChoice extends ArahantBean implements Serializable {
    public static final String TABLE_NAME = "product_attribute_choice";
    private String productAttributeChoiceId;
    public static final String ID = "productAttributeChoiceId";
    private String description;
    public static final String DESCRIPTION = "description";
    private short choiceOrder;
    public static final String CHOICEORDER = "choiceOrder";
    private int lastActiveDate;
    public static final String LAST_ACTIVE_DATE = "lastActiveDate";
    public static final String PRODUCT_ATTRIBUTE ="attribute";
    private ProductAttribute attribute;

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "product_attribute_choice_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return productAttributeChoiceId = IDGenerator.generate(this);
    }

    @Id
    @Column(name = "product_attribute_choice_id")
    public String getProductAttributeChoiceId() {
        return productAttributeChoiceId;
    }

    public void setProductAttributeChoiceId(String productAttributeChoiceId) {
        this.productAttributeChoiceId = productAttributeChoiceId;
    }

    @Column(name = "choice_order")
    public short getChoiceOrder() {
        return choiceOrder;
    }

    public void setChoiceOrder(short choiceOrder) {
        this.choiceOrder = choiceOrder;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "last_active_date")
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    public ProductAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(ProductAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductAttributeChoice other = (ProductAttributeChoice) obj;
        if (this.productAttributeChoiceId != other.getProductAttributeChoiceId() && (this.productAttributeChoiceId == null || !this.productAttributeChoiceId.equals(other.getProductAttributeChoiceId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.productAttributeChoiceId != null ? this.productAttributeChoiceId.hashCode() : 0);
        return hash;
    }

}
