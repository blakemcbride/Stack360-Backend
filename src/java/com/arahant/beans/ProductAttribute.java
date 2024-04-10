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
@Table(name = "product_attribute")
public class ProductAttribute extends SetupWithEndDate implements Serializable {

    private String productAttributeId;
    public static final String ID = "productAttributeId";
    //private String companyId;
    //public static final String COMPANYID = "companyId";
    //private CompanyDetail company;
    public static final String COMPANY = "orgGroup";

    private short attributeOrder;
    public static final String ATTRIBUTEORDER = "attributeOrder";
    private String attribute;
    public static final String ATTRIBUTE = "attribute";
    private char dataType;

    public static final String LAST_ACTIVE_DATE = "lastActiveDate";
    public static final char TYPE_NUMERIC = 'N';
    public static final char TYPE_DATE = 'D';
    public static final char TYPE_STRING = 'S';
    public static final char TYPE_LIST = 'L';
    public static final char TYPE_YES_NO_UNK = 'Y';

    @Override
    public String tableName() {
        return "product_attribute";
    }

    @Override
    public String keyColumn() {
        return "product_attribute_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return productAttributeId = IDGenerator.generate(this);
    }

    @Id
    @Column(name = "product_attribute_id")
    public String getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(String productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

  

    @Column(name = "attribute_order")
    public short getAttributeOrder() {
        return attributeOrder;
    }

    public void setAttributeOrder(short attributeOrder) {
        this.attributeOrder = attributeOrder;
    }

    @Column(name = "attribute")
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Column(name = "last_active_date")
    @Override
    public int getLastActiveDate() {
    return lastActiveDate;
    }

 

    @Column(name = "data_type")
    public char getDataType() {
        return dataType;
    }

    public void setDataType(char dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductAttribute other = (ProductAttribute) obj;
        if (this.productAttributeId != other.getProductAttributeId() && (this.productAttributeId == null || !this.productAttributeId.equals(other.getProductAttributeId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.productAttributeId != null ? this.productAttributeId.hashCode() : 0);
        return hash;
    }

    @Override
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public OrgGroup getOrgGroup() {
        return orgGroup;
    }
}
