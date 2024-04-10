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
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *
 * assembly_template_id character(16) NOT NULL, product_id character(16) NOT
 * NULL, parent_assembly_template_id character(16), quantity integer NOT NULL,
 * item_particulars character varying(256),
 */
@Entity
@Table(name = AssemblyTemplate.TABLE_NAME)
public class AssemblyTemplate extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "assembly_template";
	private String assemblyTemplateId;
	private String assemblyName;
	private String description;

	@Column(name = "assembly_name")
	public String getAssemblyName() {
		return assemblyName;
	}

	public void setAssemblyName(String assemblyName) {
		this.assemblyName = assemblyName;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name = "assembly_template_id")
	public String getAssemblyTemplateId() {
		return assemblyTemplateId;
	}

	public void setAssemblyTemplateId(String assemblyTemplateId) {
		this.assemblyTemplateId = assemblyTemplateId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "assembly_template_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return assemblyTemplateId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AssemblyTemplate other = (AssemblyTemplate) obj;
		if ((this.assemblyTemplateId == null) ? (other.assemblyTemplateId != null) : !this.assemblyTemplateId.equals(other.assemblyTemplateId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + (this.assemblyTemplateId != null ? this.assemblyTemplateId.hashCode() : 0);
		return hash;
	}
}
