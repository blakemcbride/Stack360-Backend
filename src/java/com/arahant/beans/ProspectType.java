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
import com.arahant.utils.IDGenerator;
import javax.persistence.*;

@Entity
@Table(name = ProspectType.TABLE_NAME)
public class ProspectType extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "prospect_type";
	public static final String PROSPECT_TYPE_ID = "prospectTypeId";
	private String prospectTypeId;
	public static final String COMPANY = "company";
	private CompanyDetail company;
	public static final String COMPANY_ID = "companyId";
	private String companyId;
	public static final String TYPE_CODE = "typeCode";
	private String typeCode;
	public static final String DESCRIPTION = "description";
	private String description;
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	private int lastActiveDate = 0;

	public ProspectType() {
	}

	@Id
	@Column(name = "prospect_type_id")
	public String getProspectTypeId() {
		return prospectTypeId;
	}

	public void setProspectTypeId(String prospectTypeId) {
		this.prospectTypeId = prospectTypeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", updatable = false, insertable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "type_code")
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "prospect_type_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setProspectTypeId(IDGenerator.generate(this));
		return prospectTypeId;
	}

	@Override
	public ProspectType clone() {
		ProspectType pt = new ProspectType();
		pt.generateId();
		pt.setCompanyId(companyId);
		pt.setTypeCode(typeCode);
		pt.setDescription(description);
		pt.setTypeCode(typeCode);
		return pt;
	}

	@Override
	public boolean equals(Object o) {
		if (prospectTypeId == null && o == null)
			return true;
		if (prospectTypeId != null && o instanceof ProspectType)
			return prospectTypeId.equals(((ProspectType) o).getProspectTypeId());
		return false;
	}

	@Override
	public int hashCode() {
		if (prospectTypeId == null)
			return 0;
		return prospectTypeId.hashCode();
	}
}
