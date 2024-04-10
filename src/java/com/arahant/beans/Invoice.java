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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice extends ArahantBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "invoice";
    public static final String PERSON = "billedPerson";
    public static final String RECEIPT_JOINS = "receiptJoins";
    // Fields
    private String invoiceId;
    public static final String INVOICEID = "invoiceId";
    private CompanyBase companyBase;
    public static final String COMPANYBASE = "companyBase";
    public static final String CLIENTCOMPANY = "clientCompany";
    private GlAccount glAccount;
    public static final String GLACCOUNT = "glAccount";
    private String description;
    public static final String DESCRIPTION = "description";
    private Date createDate;
    public static final String CREATEDATE = "createDate";
    private Date exportDate;
    public static final String EXPORTDATE = "exportDate";
    private String accountingInvoiceIdentifier;
    public static final String ACCOUNTINGINVOICEIDENTIFIER = "accountingInvoiceIdentifier";
    private Set<InvoiceLineItem> invoiceLineItems = new HashSet<InvoiceLineItem>(0);
    public static final String INVOICELINEITEMS = "invoiceLineItems";
    private char invoiceType = 'C';
    private Person billedPerson;
    private char payedOff = 'N';
    private Set<ReceiptJoin> receiptJoins = new HashSet<ReceiptJoin>(0);
	private String purchaseOrder;
	private short paymentTerms;

    // Constructors
    /** default constructor */
    public Invoice() {
    }

    // Property accessors
    @Id
    @Column(name = "invoice_id")
    public String getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(final String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public CompanyBase getCompanyBase() {
        return this.companyBase;
    }

    public void setCompanyBase(final CompanyBase companyBase) {
        this.companyBase = companyBase;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable=false, updatable=false)
    public ClientCompany getClientCompany() {
        return ArahantSession.getHSU().get(ClientCompany.class, this.companyBase.getOrgGroupId());
//      return (ClientCompany) this.companyBase;
    }

    public void setClientCompany(final ClientCompany company) {
        this.companyBase = company;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ar_account_id")
    public GlAccount getGlAccount() {
        return this.glAccount;
    }

    public void setGlAccount(final GlAccount glAccount) {
        this.glAccount = glAccount;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "export_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getExportDate() {
        return this.exportDate;
    }

    public void setExportDate(final Date exportDate) {
        this.exportDate = exportDate;
    }

    @Column(name = "accounting_invoice_identifier")
    public String getAccountingInvoiceIdentifier() {
        return this.accountingInvoiceIdentifier;
    }

    public void setAccountingInvoiceIdentifier(
            final String accountingInvoiceIdentifier) {
        this.accountingInvoiceIdentifier = accountingInvoiceIdentifier;
    }

    @OneToMany(mappedBy = InvoiceLineItem.INVOICE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<InvoiceLineItem> getInvoiceLineItems() {
        return this.invoiceLineItems;
    }

    public void setInvoiceLineItems(final Set<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    public Person getBilledPerson() {
        return billedPerson;
    }

    public void setBilledPerson(Person billedPerson) {
        this.billedPerson = billedPerson;
    }

    @Column(name = "invoice_type")
    public char getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(char invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Column(name = "payed_off")
    public char getPayedOff() {
        return payedOff;
    }

    public void setPayedOff(char payedOff) {
        this.payedOff = payedOff;
    }

	@Column(name = "purchase_order")
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@Column(name = "payment_terms")
	public short getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(short paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

    @OneToMany(mappedBy = ReceiptJoin.INVOICE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ReceiptJoin> getReceiptJoins() {
        return receiptJoins;
    }

    public void setReceiptJoins(Set<ReceiptJoin> receiptJoins) {
        this.receiptJoins = receiptJoins;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */
    @Override
    public String keyColumn() {

        return "invoice_id";
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#tableName()
     */
    @Override
    public String tableName() {

        return TABLE_NAME;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#generateId()
     */
    @Override
    public String generateId() throws ArahantException {
        setInvoiceId(IDGenerator.generate(this));
        return invoiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (invoiceId == null && o == null) {
            return true;
        }
        if (invoiceId != null && o instanceof Invoice) {
            return invoiceId.equals(((Invoice) o).getInvoiceId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (invoiceId == null) {
            return 0;
        }
        return invoiceId.hashCode();
    }
}
