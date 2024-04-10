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
 * ProspectStatus
 */
@Entity
@Table(name = "prospect_status")
public class ProspectStatus extends ArahantBean implements java.io.Serializable, Comparable<ProspectStatus> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "prospect_status";
    // Fields
    private String prospectStatusId;
    public static final String PROSPECT_STATUS_ID = "prospectStatusId";
    private String code;
    public static final String CODE = "code";
    private String description;
    public static final String DESCRIPTION = "description";
    private short sequence;
    public static final String SEQ = "sequence";
    private char active = 'Y';
    public static final String ACTIVE = "active";
    private Set<ProspectCompany> prospectCompanies = new HashSet<ProspectCompany>(0);

	private String companyId;
	public static final String COMPANY_ID = "companyId";
	private char showInSalesPipeline='N';
	public static final String IN_PIPELINE = "showInSalesPipeline";
	private short salesPoints;
	public static final String SALES_POINTS = "salesPoints";
	private short fallbackDays;
	public static final String FALLBACK_DAYS = "fallbackDays";
	private int lastActiveDate;
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	private short certanity;
	public static final String CERTAINTY = "certainty";

    @Override
    public boolean equals(Object o) {
        if (prospectStatusId == null && o == null) {
            return true;
        }
        if (prospectStatusId != null && o instanceof ProspectStatus) {
            return prospectStatusId.equals(((ProspectStatus) o).getProspectStatusId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (prospectStatusId == null) {
            return 0;
        }
        return prospectStatusId.hashCode();
    }

    @Override
    public int compareTo(ProspectStatus o) {
        return o.sequence - sequence;
    }

    @Column(name = "active")
    public char getActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @Column(name = "prospect_status_id")
    public String getProspectStatusId() {
        return prospectStatusId;
    }

    public void setProspectStatusId(String prospectStatusId) {
        this.prospectStatusId = prospectStatusId;
    }

    @Column(name = "seqno")
    public short getSequence() {
        return sequence;
    }

    public void setSequence(short sequence) {
        //if initialSequence is -1, add to end
        this.sequence = sequence;
    }
	
	@Column(name = "certainty")
	public short getCertanity() {
		return certanity;
	}

	public void setCertanity(short certanity) {
		this.certanity = certanity;
	}
	
    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "prospect_status_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return prospectStatusId = IDGenerator.generate(this);
    }

    @OneToMany(mappedBy = ProspectCompany.PROSPECT_STATUS, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProspectCompany> getProspectCompanies() {
        return prospectCompanies;
    }

    public void setProspectCompanies(Set<ProspectCompany> prospectCompanies) {
        this.prospectCompanies = prospectCompanies;
    }

	@Column(name="company_id")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name="fallback_days")
	public short getFallbackDays() {
		return fallbackDays;
	}

	public void setFallbackDays(short fallbackDays) {
		this.fallbackDays = fallbackDays;
	}

	@Column(name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name="sales_points")
	public short getSalesPoints() {
		return salesPoints;
	}

	public void setSalesPoints(short salesPoints) {
		this.salesPoints = salesPoints;
	}

	@Column(name="show_in_sales_pipeline")
	public char getShowInSalesPipeline() {
		return showInSalesPipeline;
	}

	public void setShowInSalesPipeline(char showInSalesPipeline) {
		this.showInSalesPipeline = showInSalesPipeline;
	}

}
