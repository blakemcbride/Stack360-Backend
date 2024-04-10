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

import java.util.Date;
import javax.persistence.*;


import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 * ProjectStatus generated by hbm2java
 */
@Entity
@Table(name = "prospect_source")
public class ProspectSource extends ArahantBean implements java.io.Serializable, Comparable<ProspectSource> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "prospect_source";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
    // Fields
    private String prospectSourceId;
    public static final String PROSPECT_SOURCE_ID = "prospectSourceId";
    private String sourceCode;
    public static final String SOURCE_CODE = "sourceCode";
    private String description;
    public static final String DESCRIPTION = "description";
    private Set<ProspectCompany> prospectCompanies = new HashSet<ProspectCompany>(0);
	final public static String COMPANY_ID = "companyId";

	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY="company";

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column (name="company_id", insertable=false, updatable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	private int lastActiveDate = 0;
	@Column (name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	
    @Override
    public boolean equals(Object o) {
        if (prospectSourceId == null && o == null) {
            return true;
        }
        if (prospectSourceId != null && o instanceof ProspectSource) {
            return prospectSourceId.equals(((ProspectSource) o).getProspectSourceId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (prospectSourceId == null) {
            return 0;
        }
        return prospectSourceId.hashCode();
    }

    @Override
    public int compareTo(ProspectSource o) {
        return sourceCode.compareTo(o.sourceCode);
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "prospect_source_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return prospectSourceId = IDGenerator.generate(this);
    }

    @Id
    @Column(name = "prospect_source_id")
    public String getProspectSourceId() {
        return prospectSourceId;
    }

    public void setProspectSourceId(String prospectSourceId) {
        this.prospectSourceId = prospectSourceId;
    }

    @Column(name = "source_code")
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @OneToMany(mappedBy = ProspectCompany.PROSPECT_SOURCE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProspectCompany> getProspectCompanies() {
        return prospectCompanies;
    }

    public void setProspectCompanies(Set<ProspectCompany> prospectCompanies) {
        this.prospectCompanies = prospectCompanies;
    }
}
