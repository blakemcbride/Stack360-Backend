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

/**
 * ProductService generated by hbm2java
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = ProductService.TABLE_NAME)
public class ProductService extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "product_service";
    public static final String EXPENSE_ACCOUNT = "expenseAccount";
    // Fields
    private String productId;
    public static final String PRODUCTID = "productId";
    private String accsysId;
    public static final String ACCSYSID = "accsysId";
    private String description;
    public static final String DESCRIPTION = "description";
    private int serviceType;
    public static final String SERVICE_TYPE = "serviceType";
    private Set<Project> projects = new HashSet<Project>();
    public static final String PROJECTS = "projects";
    private Set<StandardProject> standardProjects = new HashSet<StandardProject>();
    public static final String STANDARDPROJECTS = "standardProjects";
    private Set<InvoiceLineItem> invoiceLineItems = new HashSet<InvoiceLineItem>();
    public static final String INVOICELINEITEMS = "invoiceLineItems";
    private GlAccount expenseAccount;
    private CompanyDetail associatedCompany;
    private String detailedDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public CompanyDetail getAssociatedCompany() {
        return associatedCompany;
    }

    public void setAssociatedCompany(CompanyDetail associatedCompany) {
        this.associatedCompany = associatedCompany;
    }

    // Constructors
    /** default constructor */
    public ProductService() {
    }

    /** minimal constructor */
    public ProductService(final String productId, final int productType) {
        this.productId = productId;
        this.serviceType = productType;
    }

    // Property accessors
    @Id
    @Column(name = "product_service_id")
    public String getProductId() {
        return this.productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    @Column(name = "accsys_id")
    public String getAccsysId() {
        return this.accsysId;
    }

    public void setAccsysId(final String accsysId) {
        this.accsysId = accsysId;
    }

    @Column(name = "detailed_description")
    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Column(name = "service_type")
    public int getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(final int serviceType) {
        this.serviceType = serviceType;
    }

    @OneToMany(mappedBy = Project.PRODUCTSERVICE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(final Set<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(mappedBy = StandardProject.PRODUCTSERVICE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<StandardProject> getStandardProjects() {
        return this.standardProjects;
    }

    public void setStandardProjects(final Set<StandardProject> standardProjects) {
        this.standardProjects = standardProjects;
    }

    @OneToMany(mappedBy = InvoiceLineItem.PRODUCTSERVICE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<InvoiceLineItem> getInvoiceLineItems() {
        return this.invoiceLineItems;
    }

    public void setInvoiceLineItems(final Set<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_account_id")
    public GlAccount getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(GlAccount expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */
    @Override
    public String keyColumn() {
        return "product_service_id";
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
        setProductId(IDGenerator.generate(this));
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (productId == null && o == null) {
            return true;
        }
        if (productId != null && o instanceof ProductService) {
            return productId.equals(((ProductService) o).getProductId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (productId == null) {
            return 0;
        }
        return productId.hashCode();
    }

    @Override
    public String notifyId() {
        return productId;
    }

    @Override
    public String notifyClassName() {
        return "ProductService";
    }
}
