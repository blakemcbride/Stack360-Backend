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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 */
@Entity
@Table(name = Lot.TABLE_NAME)
public class Lot extends ArahantBean implements Serializable {

    public static final String TABLE_NAME = "lot";
    public static final String LOT_NUMBER = "lotNumber";
    public static final String LOT_ID = "lotId";
    public static final String ITEMS = "items";
    public static final String DATE_RECEIVED = "dateReceived";
    public static final String COST = "lotCost";
    public static final String ORIGINAL_QUANTITY = "originalQuantity";
    private String lotId;
    private String lotNumber;
    private int originalQuantity;
    private int dateReceived;
    private double lotCost;
    private String lotParticulars;
    private Integer remainingQuantity;
    private Set<Item> items = new HashSet<Item>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "lot_id")
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Column(name = "date_received")
    public int getDateReceived() {
        return dateReceived;
    }

    @Formula("(select sum(item.quantity) from item where item.lot_id=lot_id)")
    public Integer getRemainingQuantity() {
        if (remainingQuantity == null) {
            return 0;
        }
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public void setDateReceived(int dateReceived) {
        this.dateReceived = dateReceived;
    }

    @Column(name = "lot_cost")
    public double getLotCost() {
        return lotCost;
    }

    public void setLotCost(double lotCost) {
        this.lotCost = lotCost;
    }

    @Id
    @Column(name = "lot_id")
    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    @Column(name = "lot_number")
    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    @Column(name = "lot_particulars")
    public String getLotParticulars() {
        return lotParticulars;
    }

    public void setLotParticulars(String lotParticulars) {
        this.lotParticulars = lotParticulars;
    }

    @Column(name = "original_quantity")
    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "lot_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return lotId = IDGenerator.generate(this);
    }
}
