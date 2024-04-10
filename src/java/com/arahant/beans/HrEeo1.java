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
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name="hr_eeo1")
public class HrEeo1 extends ArahantBean implements Serializable {
	public final static String ID = "eeo1Id";
	public final static String PAY_PERIOD_START_DATE = "payPeriodStartDate";
	public final static String PAY_PERIOD_FINAL_DATE = "payPeriodFinalDate";
	public final static String COMMON_OWNERSHIP = "commonOwnership";
	public final static String GOVERNMENT_CONTRACTOR = "governmentContractor";
	public final static String CREATED_DATE = "createdDate";
	public final static String UPLOADED_DATE = "uploadedDate";
	public final static String TRANSMITTED_DATA = "transmittedData";

    private String eeo1Id;
    private int payPeriodStartDate;
	private int payPeriodFinalDate;
    private char commonOwnership;
    private char governmentContractor;
    private int createdDate;
    private int uploadedDate;
    private String transmittedData;
	private OrgGroup orgGroup;

	@Column (name="common_ownership")
    public char getCommonOwnership() {
        return commonOwnership;
    }

    public void setCommonOwnership(char commonOwnership) {
        this.commonOwnership = commonOwnership;
    }

	@Column (name="date_created")
    public int getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(int createdDate) {
        this.createdDate = createdDate;
    }

	@Id
	@Column (name="eeo1_id")
    public String getEeo1Id() {
        return eeo1Id;
    }

    public void setEeo1Id(String eeo1Id) {
        this.eeo1Id = eeo1Id;
    }

	@Column (name="gov_contractor")
    public char getGovernmentContractor() {
        return governmentContractor;
    }

    public void setGovernmentContractor(char governmentContractor) {
        this.governmentContractor = governmentContractor;
    }

	@Column (name="end_period")
    public int getPayPeriodFinalDate() {
        return payPeriodFinalDate;
    }

    public void setPayPeriodFinalDate(int payPeriodFinalDate) {
        this.payPeriodFinalDate = payPeriodFinalDate;
    }

	@Column (name="beg_period")
    public int getPayPeriodStartDate() {
        return payPeriodStartDate;
    }

    public void setPayPeriodStartDate(int payPeriodStartDate) {
        this.payPeriodStartDate = payPeriodStartDate;
    }

	@Column (name="transmitted_data")
    public String getTransmittedData() {
        return transmittedData;
    }

    public void setTransmittedData(String transmittedData) {
        this.transmittedData = transmittedData;
    }

	@Column (name="date_uploaded")
    public int getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(int uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@Override
	public String tableName() {
		return "hr_eeo1";
	}

	@Override
	public String keyColumn() {
		return "eeo1_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return eeo1Id = IDGenerator.generate(this);
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HrEeo1 other = (HrEeo1) obj;
        if ((this.eeo1Id == null) ? (other.eeo1Id != null) : !this.eeo1Id.equals(other.eeo1Id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.eeo1Id != null ? this.eeo1Id.hashCode() : 0);
        return hash;
    }
}
