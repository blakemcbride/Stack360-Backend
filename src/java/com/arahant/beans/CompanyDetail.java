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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = CompanyDetail.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyDetail extends CompanyBase implements java.io.Serializable, ArahantSaveNotify {

    private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "company_detail";
    public static final String AGENCY_JOINS = "agencyJoins";
    public static final String AGENT_JOINS = "agentJoins";
    // Fields
    private float billingRate;
    private char accountingBasis = 'C';
    private GlAccount cashAccount;
    private GlAccount arAccount;
    private GlAccount employeeAdvanceAccount;
    private Set<HrBenefitCategory> benefitCategories = new HashSet<HrBenefitCategory>();
    private String eeo1CompanyId;
    private String dunBradstreet;
    private String naicsCode;
    private char markTimeOffOnApproval = 'N';
    private Set<AgentJoin> agentJoins = new HashSet<AgentJoin>();
    private Set<AgencyJoin> agencyJoins = new HashSet<AgencyJoin>();
    public static final String ACCOUNTINGBASIS = "accountingBasis";
	private int maxEmployees = 0;
	private byte[] logo;
	private String logoExtension;
	private String logoSource;
	private short fiscalBeginningMonth;
	private short emailOutType = 0;
	private char emailOutAuthentication = 'N';
	private String emailOutHost;
	private String emailOutDomain;
	private int emailOutPort = 25;
	private char emailOutEncryption = 'N';
	private String emailOutUserId;
	private String emailOutUserPw;
	private String emailOutFromName;
	private String emailOutFromEmail;
	
    // Constructors
    /** default constructor */
    public CompanyDetail() {}

	/**
	 * @return Returns the image.
	 */
	@Column (name="logo")
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * @param image The image to set.
	 */
	public void setLogo(final byte[] logo) {
		this.logo = logo;
	}

	@Column (name="logo_extension")
	public String getLogoExtension() {
		return logoExtension;
	}

	public void setLogoExtension(String logoExtension) {
		if (logoExtension!=null)
			logoExtension=logoExtension.toLowerCase();
		this.logoExtension = logoExtension;
	}


	/**
	 * @return Returns the source.
	 */
	@Column (name="logo_source")
	public String getLogoSource() {
		return logoSource;
	}

	/**
	 * @param source The source to set.
	 */
	public void setLogoSource(final String logoSource) {
		this.logoSource = logoSource;
	}


    /**
     * @return Returns the accountingBasis.
     */
    @Column(name = "accounting_basis")
    public char getAccountingBasis() {
        return accountingBasis;
    }

    /**
     * @param accountingBasis The accountingBasis to set.
     */
    public void setAccountingBasis(char accountingBasis) {
        firePropertyChange("accountingBasis", this.accountingBasis, accountingBasis);
        this.accountingBasis = accountingBasis;
    }
	
	@Column(name = "max_employees")
	public int getMaxEmployees() {
		return maxEmployees;
	}

	public void setMaxEmployees(int maxEmployees) {
		this.maxEmployees = maxEmployees;
	}

    @Column(name = "billing_rate")
    public float getBillingRate() {
        return billingRate;
    }

    public void setBillingRate(float billingRate) {
        this.billingRate = billingRate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ar_account_id")
    public GlAccount getArAccount() {
        return arAccount;
    }

    public void setArAccount(GlAccount arAccount) {
        this.arAccount = arAccount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_account_id")
    public GlAccount getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(GlAccount cashAccount) {
        this.cashAccount = cashAccount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_advance_account_id")
    public GlAccount getEmployeeAdvanceAccount() {
        return employeeAdvanceAccount;
    }

    public void setEmployeeAdvanceAccount(GlAccount employeeAdvanceAccount) {
        this.employeeAdvanceAccount = employeeAdvanceAccount;
    }

    @OneToMany(mappedBy = HrBenefitCategory.COMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrBenefitCategory> getBenefitCategories() {
        return benefitCategories;
    }

    public void setBenefitCategories(Set<HrBenefitCategory> benefitCategories) {
        this.benefitCategories = benefitCategories;
    }

    @Column(name = "dun_bradstreet_num")
    public String getDunBradstreet() {
        return dunBradstreet;
    }

    public void setDunBradstreet(String dunBradstreet) {
        this.dunBradstreet = dunBradstreet;
    }

    @Column(name = "eeo1_id")
    public String getEeo1CompanyId() {
        return eeo1CompanyId;
    }

    public void setEeo1CompanyId(String eeo1CompanyId) {
        this.eeo1CompanyId = eeo1CompanyId;
    }

    @Column(name = "naics")
    public String getNaicsCode() {
        return naicsCode;
    }

    public void setNaicsCode(String naicsCode) {
        this.naicsCode = naicsCode;
    }

    @Override
    public boolean equals(Object o) {
        if (getOrgGroupId() == null && o == null) {
            return true;
        }
        if (getOrgGroupId() != null && o instanceof CompanyDetail) {
            return getOrgGroupId().equals(((CompanyDetail) o).getOrgGroupId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (getOrgGroupId() == null) {
            return 0;
        }
        return getOrgGroupId().hashCode();
    }

    @Override
    public String notifyId() {
        return getOrgGroupId();
    }

    @Override
    public String notifyClassName() {
        return "CompanyDetail";
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "company_id")
    public Set<AgencyJoin> getAgencyJoins() {
        return agencyJoins;
    }

    public void setAgencyJoins(Set<AgencyJoin> agencyJoins) {
        this.agencyJoins = agencyJoins;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinColumn(name = "company_id")
    public Set<AgentJoin> getAgentJoins() {
        return agentJoins;
    }

    public void setAgentJoins(Set<AgentJoin> agentJoins) {
        this.agentJoins = agentJoins;
    }

    @Column(name = "time_off_auto_accrual")
    public char getMarkTimeOffOnApproval() {
        return markTimeOffOnApproval;
    }

    public void setMarkTimeOffOnApproval(char markTimeOffOnApproval) {
        this.markTimeOffOnApproval = markTimeOffOnApproval;
    }

	@Column(name = "fiscal_beginning_month")
	public short getFiscalBeginningMonth() {
		return fiscalBeginningMonth;
	}

	public void setFiscalBeginningMonth(short fiscalBeginningMonth) {
		this.fiscalBeginningMonth = fiscalBeginningMonth;
	}

	@Column(name = "email_out_type")
	public short getEmailOutType() {
		return emailOutType;
	}

	public void setEmailOutType(short emailOutType) {
		this.emailOutType = emailOutType;
	}

	@Column(name = "email_out_authentication")
	public char getEmailOutAuthentication() {
		return emailOutAuthentication;
	}

	public void setEmailOutAuthentication(char emailOutAuthentication) {
		this.emailOutAuthentication = emailOutAuthentication;
	}

	@Column(name = "email_out_host")
	public String getEmailOutHost() {
		return emailOutHost;
	}

	public void setEmailOutHost(String emailOutHost) {
		this.emailOutHost = emailOutHost;
	}

	@Column(name = "email_out_domain")
	public String getEmailOutDomain() {
		return emailOutDomain;
	}

	public void setEmailOutDomain(String emailOutDomain) {
		this.emailOutDomain = emailOutDomain;
	}

	@Column(name = "email_out_port")
	public int getEmailOutPort() {
		return emailOutPort;
	}

	public void setEmailOutPort(int emailOutPort) {
		this.emailOutPort = emailOutPort;
	}

	@Column(name = "email_out_encryption")
	public char getEmailOutEncryption() {
		return emailOutEncryption;
	}

	public void setEmailOutEncryption(char emailOutEncryption) {
		this.emailOutEncryption = emailOutEncryption;
	}

	@Column(name = "email_out_user_id")
	public String getEmailOutUserId() {
		return emailOutUserId;
	}

	public void setEmailOutUserId(String emailOutUserId) {
		this.emailOutUserId = emailOutUserId;
	}

	@Column(name = "email_out_user_pw")
	public String getEmailOutUserPw() {
		return emailOutUserPw;
	}

	public void setEmailOutUserPw(String emailOutUserPw) {
		this.emailOutUserPw = emailOutUserPw;
	}

	@Column(name = "email_out_from_name")
	public String getEmailOutFromName() {
		return emailOutFromName;
	}

	public void setEmailOutFromName(String emailOutFromName) {
		this.emailOutFromName = emailOutFromName;
	}

	@Column(name = "email_out_from_email")
	public String getEmailOutFromEmail() {
		return emailOutFromEmail;
	}

	public void setEmailOutFromEmail(String emailOutFromEmail) {
		this.emailOutFromEmail = emailOutFromEmail;
	}
	
}
