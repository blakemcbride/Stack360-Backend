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

import javax.persistence.*;



import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name = InvoiceLineItem.TABLE_NAME)
public class InvoiceLineItem extends ArahantBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "invoice_line_item";
    public static final String AMOUNT = "amount";
    // Fields
    private String invoiceLineItemId;
    public static final String INVOICELINEITEMID = "invoiceLineItemId";
    private Invoice invoice;
    public static final String INVOICE = "invoice";
    private ProductService productService;
    public static final String PRODUCTSERVICE = "productService";
    private GlAccount glAccount;
    public static final String GLACCOUNT = "glAccount";
    private float adjHours;
    public static final String ADJHOURS = "adjHours";
    private double adjRate;
    public static final String ADJRATE = "adjRate";
    private String description;
    public static final String DESCRIPTION = "description";
    private Set<Timesheet> timesheets = new HashSet<>(0);
    public static final String TIMESHEETS = "timesheets";
    private double amount;
    private char billingType = 'H';
    private String benefitJoinId;
    public static final String PROJECTID = "projectId";
    private String projectId;  // used for projects that use project billing rather than hourly billing

    // Constructors
    /** default constructor */
    public InvoiceLineItem() {
    }

    /** minimal constructor */
    public InvoiceLineItem(final String invoiceLineItemId, final float adjHours,
            final double adjRate) {
        this.invoiceLineItemId = invoiceLineItemId;
        this.adjHours = adjHours;
        this.adjRate = adjRate;
    }

    // Property accessors
    @Id
    @Column(name = "invoice_line_item_id")
    public String getInvoiceLineItemId() {
        return this.invoiceLineItemId;
    }

    public void setInvoiceLineItemId(final String invoiceLineItemId) {
        this.invoiceLineItemId = invoiceLineItemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(final Invoice invoice) {
        this.invoice = invoice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public ProductService getProductService() {
        return this.productService;
    }

    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_account_id")
    public GlAccount getGlAccount() {
        return this.glAccount;
    }

    public void setGlAccount(final GlAccount glAccount) {
        this.glAccount = glAccount;
    }

    @Column(name = "adj_hours")
    public float getAdjHours() {
        return this.adjHours;
    }

    public void setAdjHours(final float adjHours) {
        this.adjHours = adjHours;
    }

    @Column(name = "adj_rate")
    public double getAdjRate() {
        return this.adjRate;
    }

    public void setAdjRate(final double adjRate) {
        this.adjRate = adjRate;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = Timesheet.INVOICELINEITEM, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }

    public void setTimesheets(final Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "benefit_join_id")
    public String getBenefitJoinId() {
        return benefitJoinId;
    }

    public void setBenefitJoinId(String benefitJoinId) {
        this.benefitJoinId = benefitJoinId;
    }

    @Column(name = "billing_type")
    public char getBillingType() {
        return billingType;
    }

    public void setBillingType(char billingType) {
        this.billingType = billingType;
    }

    @Column(name = "project_id")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String keyColumn() {
        return "invoice_line_item_id";
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String generateId() throws ArahantException {
        setInvoiceLineItemId(IDGenerator.generate(this));
        return invoiceLineItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (invoiceLineItemId == null && o == null) {
            return true;
        }
        if (invoiceLineItemId != null && o instanceof InvoiceLineItem) {
            return invoiceLineItemId.equals(((InvoiceLineItem) o).getInvoiceLineItemId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (invoiceLineItemId == null) {
            return 0;
        }
        return invoiceLineItemId.hashCode();
    }
}
