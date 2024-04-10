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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name=Item.TABLE_NAME)
public class Item extends AuditedBean implements Serializable, IItem {
	public static final String TABLE_NAME="item";

	public static final String ID="itemId";
	public static final String SERIAL_NUMBER="serialNumber";
	public static final String LOCATION="location";
	public static final String PRODUCT="product";
	public static final String PARENT="parentItem";
	public static final String LOT="lot";
	public static final String QUANTITY="quantity";

	private String itemId;
	private String serialNumber;
	private Product product;
	private OrgGroup location;
	private Item parentItem;
	private String itemParticulars;
	private Lot lot;
	private int quantity;
	private String itemName;
	private String manufacturer;
	private String model;
	private int datePurchased = 0;
	private float originalCost = 0;
	private String purchasedFrom;
	private String notes;
	private char itemStatus = 'C';
	private String retirementNotes;
	private int retirementDate = 0;
	private char retirementStatus = 'C';
	private Person reimbursementPerson;
	private float requestedReimbursementAmount = 0;
	private float reimbursementAmountReceived = 0;
	private int dateReimbursementReceived = 0;
	private Person personAcceptingReimbursement;

	@Id
	@Column(name="item_id")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Column(name="serial_number")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id")
	public OrgGroup getLocation() {
		return location;
	}

	public void setLocation(OrgGroup location) {
		this.location = location;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_item_id")
	public Item getParentItem() {
		return parentItem;
	}

	public void setParentItem(Item parentItem) {
		this.parentItem = parentItem;
	}

    @Column(name="quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name="lot_id")
    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    @Column(name="item_particulars")
    public String getItemParticulars() {
        return itemParticulars;
    }

    public void setItemParticulars(String itemParticulars) {
        this.itemParticulars = itemParticulars;
    }

    @Column(name="item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name="manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Column(name="model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name="date_purchased")
    public int getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(int datePurchased) {
        this.datePurchased = datePurchased;
    }

    @Column(name="original_cost")
    public float getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(float originalCost) {
        this.originalCost = originalCost;
    }

    @Column(name="purchased_from")
    public String getPurchasedFrom() {
        return purchasedFrom;
    }

    public void setPurchasedFrom(String purchasedFrom) {
        this.purchasedFrom = purchasedFrom;
    }

    @Column(name="notes")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name="item_status")
    public char getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(char itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Column(name="retirement_notes")
    public String getRetirementNotes() {
        return retirementNotes;
    }

    public void setRetirementNotes(String retirementNotes) {
        this.retirementNotes = retirementNotes;
    }

    @Column(name="retirement_date")
    public int getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(int retirementDate) {
        this.retirementDate = retirementDate;
    }

    @Column(name="retirement_status")
    public char getRetirementStatus() {
        return retirementStatus;
    }

    public void setRetirementStatus(char retirementStatus) {
        this.retirementStatus = retirementStatus;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="retirement_person_id")
    public Person getReimbursementPerson() {
        return reimbursementPerson;
    }

    public void setReimbursementPerson(Person reimbursementPerson) {
        this.reimbursementPerson = reimbursementPerson;
    }

    @Column(name="requested_reimbursement_amount")
    public float getRequestedReimbursementAmount() {
        return requestedReimbursementAmount;
    }

    public void setRequestedReimbursementAmount(float requestedReimbursementAmount) {
        this.requestedReimbursementAmount = requestedReimbursementAmount;
    }

    @Column(name="reimbursement_amount_received")
    public float getReimbursementAmountReceived() {
        return reimbursementAmountReceived;
    }

    public void setReimbursementAmountReceived(float reimbursementAmountReceived) {
        this.reimbursementAmountReceived = reimbursementAmountReceived;
    }

    @Column(name="date_reimbursement_received")
    public int getDateReimbursementReceived() {
        return dateReimbursementReceived;
    }

    public void setDateReimbursementReceived(int dateReimbursementReceived) {
        this.dateReimbursementReceived = dateReimbursementReceived;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="person_accepting_reimbursement")
    public Person getPersonAcceptingReimbursement() {
        return personAcceptingReimbursement;
    }

    public void setPersonAcceptingReimbursement(Person personAcceptingReimbursement) {
        this.personAcceptingReimbursement = personAcceptingReimbursement;
    }

    @Column (name="record_change_type")
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column (name="record_person_id")
	public String getRecordPersonId() {
		if (recordPersonId==null)
			if (ArahantSession.getHSU().getCurrentPerson()!=null)
				recordPersonId=ArahantSession.getHSU().getCurrentPerson().getPersonId();
			else
			{
				recordPersonId=ArahantSession.getHSU().getArahantPersonId();

			}
		return recordPersonId;
	}

	@Column (name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Override
	public ArahantHistoryBean historyObject() {
		return new ItemH();
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "item_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return itemId=IDGenerator.generate(this);
	}

	@Override
	public String keyValue() {
		return getItemId();
	}

}
