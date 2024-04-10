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

//import org.hibernate.validator.Min;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;


@Entity
@Table(name = GlAccount.TABLE_NAME)
public class GlAccount extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "gl_account";
    public static final int TYPE_ACCOUNTS_RECIEVABLE = 1;
    // Fields
    private String glAccountId;
    public static final String GLACCOUNTID = "glAccountId";
    private String accountNumber;
    public static final String ACCOUNTNUMBER = "accountNumber";
    private String accountName;
    public static final String ACCOUNTNAME = "accountName";
   // @Min(value = 0, message = "Account Type must be 0 or greater")
    private int accountType;
    public static final String ACCOUNTTYPE = "accountType";
    private short defaultFlag;
    public static final String DEFAULTFLAG = "defaultFlag";
    private Set<ClientCompany> clientCompaniesForDfltArAcct = new HashSet<ClientCompany>(0);
    public static final String CLIENTCOMPANIESFORDFLTARACCT = "clientCompaniesForDfltArAcct";
    private Set<VendorCompany> vendorCompaniesForDfltApAcct = new HashSet<VendorCompany>(0);
    public static final String VENDORCOMPANIESFORDFLTAPACCT = "vendorCompaniesForDfltApAcct";
    private Set<VendorCompany> vendorCompaniesForDfltExpenseAcct = new HashSet<VendorCompany>(0);
    public static final String VENDORCOMPANIESFORDFLTEXPENSEACCT = "vendorCompaniesForDfltExpenseAcct";
    private Set<InvoiceLineItem> invoiceLineItems = new HashSet<InvoiceLineItem>(0);
    public static final String INVOICELINEITEMS = "invoiceLineItems";
    private Set<Invoice> invoices = new HashSet<Invoice>(0);
    public static final String INVOICES = "invoices";
    private Set<ClientCompany> clientCompaniesForDfltSalesAcct = new HashSet<ClientCompany>(0);
    public static final String CLIENTCOMPANIESFORDFLTSALESACCT = "clientCompaniesForDfltSalesAcct";
	public static final String COMPANY = "company";
	private CompanyDetail company;
	public static final String COMPANY_ID = "companyId";
	private String companyId;
    // Constructors
    /** default constructor */
    public GlAccount() {
    }

    /**
     * @return Returns the accountName.
     */
    @Column(name = "account_name")
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName The accountName to set.
     */
    public void setAccountName(String accountName) {
        firePropertyChange("accountName", this.accountName, accountName);
        this.accountName = accountName;
    }

    /**
     * @return Returns the accountNumber.
     */
    @Column(name = "account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber The accountNumber to set.
     */
    public void setAccountNumber(String accountNumber) {
        firePropertyChange("accountNumber", this.accountNumber, accountNumber);
        this.accountNumber = accountNumber;
    }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column (name="company_id", updatable=false, insertable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    /**
     * @return Returns the accountType.
     */
    @Column(name = "account_type")
    public int getAccountType() {
        return accountType;
    }

    /**
     * @param accountType The accountType to set.
     */
    public void setAccountType(int accountType) {
        firePropertyChange("accountType", this.accountType, accountType);
        this.accountType = accountType;
    }

    /**
     * @return Returns the clientCompaniesForDfltArAcct.
     */
    @OneToMany(mappedBy = ClientCompany.GLACCOUNTBYDFLTARACCT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ClientCompany> getClientCompaniesForDfltArAcct() {
        return clientCompaniesForDfltArAcct;
    }

    /**
     * @param clientCompaniesForDfltArAcct The clientCompaniesForDfltArAcct to set.
     */
    public void setClientCompaniesForDfltArAcct(Set<ClientCompany> clientCompaniesForDfltArAcct) {
        this.clientCompaniesForDfltArAcct = clientCompaniesForDfltArAcct;
    }

    /**
     * @return Returns the clientCompaniesForDfltSalesAcct.
     */
    @OneToMany(mappedBy = ClientCompany.GLACCOUNTBYDFLTSALESACCT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ClientCompany> getClientCompaniesForDfltSalesAcct() {
        return clientCompaniesForDfltSalesAcct;
    }

    /**
     * @param clientCompaniesForDfltSalesAcct The clientCompaniesForDfltSalesAcct to set.
     */
    public void setClientCompaniesForDfltSalesAcct(
            Set<ClientCompany> clientCompaniesForDfltSalesAcct) {
        this.clientCompaniesForDfltSalesAcct = clientCompaniesForDfltSalesAcct;
    }

    /**
     * @return Returns the defaultFlag.
     */
    @Column(name = "default_flag")
    public short getDefaultFlag() {
        return defaultFlag;
    }

    /**
     * @param defaultFlag The defaultFlag to set.
     */
    public void setDefaultFlag(short defaultFlag) {
        firePropertyChange("defaultFlag", this.defaultFlag, defaultFlag);
        this.defaultFlag = defaultFlag;
    }

    /**
     * @return Returns the glAccountId.
     */
    @Id
    @Column(name = "gl_account_id")
    public String getGlAccountId() {
        return glAccountId;
    }

    /**
     * @param glAccountId The glAccountId to set.
     */
    public void setGlAccountId(String glAccountId) {
        firePropertyChange("glAccountId", this.glAccountId, glAccountId);
        this.glAccountId = glAccountId;
    }

    /**
     * @return Returns the invoiceLineItems.
     */
    @OneToMany(mappedBy = InvoiceLineItem.GLACCOUNT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<InvoiceLineItem> getInvoiceLineItems() {
        return invoiceLineItems;
    }

    /**
     * @param invoiceLineItems The invoiceLineItems to set.
     */
    public void setInvoiceLineItems(Set<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    /**
     * @return Returns the invoices.
     */
    @OneToMany(mappedBy = Invoice.GLACCOUNT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Invoice> getInvoices() {
        return invoices;
    }

    /**
     * @param invoices The invoices to set.
     */
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return Returns the vendorCompaniesForDfltApAcct.
     */
    @OneToMany(mappedBy = VendorCompany.GLACCOUNTBYDFLTAPACCT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<VendorCompany> getVendorCompaniesForDfltApAcct() {
        return vendorCompaniesForDfltApAcct;
    }

    /**
     * @param vendorCompaniesForDfltApAcct The vendorCompaniesForDfltApAcct to set.
     */
    public void setVendorCompaniesForDfltApAcct(Set<VendorCompany> vendorCompaniesForDfltApAcct) {
        this.vendorCompaniesForDfltApAcct = vendorCompaniesForDfltApAcct;
    }

    /**
     * @return Returns the vendorCompaniesForDfltExpenseAcct.
     */
    @OneToMany(mappedBy = VendorCompany.GLACCOUNTBYDFLTEXPENSEACCT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<VendorCompany> getVendorCompaniesForDfltExpenseAcct() {
        return vendorCompaniesForDfltExpenseAcct;
    }

    /**
     * @param vendorCompaniesForDfltExpenseAcct The vendorCompaniesForDfltExpenseAcct to set.
     */
    public void setVendorCompaniesForDfltExpenseAcct(
            Set<VendorCompany> vendorCompaniesForDfltExpenseAcct) {
        this.vendorCompaniesForDfltExpenseAcct = vendorCompaniesForDfltExpenseAcct;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */
    @Override
    public String keyColumn() {

        return "gl_account_id";
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
        setGlAccountId(IDGenerator.generate(this));
        return glAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (glAccountId == null && o == null) {
            return true;
        }
        if (glAccountId != null && o instanceof GlAccount) {
            return glAccountId.equals(((GlAccount) o).getGlAccountId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (glAccountId == null) {
            return 0;
        }
        return glAccountId.hashCode();
    }

    @Override
    public String notifyId() {
        return glAccountId;
    }

    @Override
    public String notifyClassName() {
        return "GlAccount";
    }
}
