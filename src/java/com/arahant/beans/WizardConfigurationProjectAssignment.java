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
@Table(name = WizardConfigurationProjectAssignment.TABLE_NAME)
public class WizardConfigurationProjectAssignment extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "wizard_config_project_a";
	public static final String WIZARD_CONFIG_PROJECT_ASS_ID = "wizardConfigProjectAssId";
	private String wizardConfigProjectAssId;
	public static final String WIZARD_CONFIG = "wizardConfig";
	private WizardConfiguration wizardConfig;
	public static final String WIZARD_CONFIG_ID = "wizardConfigId";
	private String wizardConfigid;
	public static final String PERSON = "person";
	private Person person;
	public static final String PERSON_ID = "personId";
	private String personId;

	public WizardConfigurationProjectAssignment() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "person_id", updatable = false, insertable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wizard_configuration_id")
	public WizardConfiguration getWizardConfig() {
		return wizardConfig;
	}

	public void setWizardConfig(WizardConfiguration wizardConfig) {
		this.wizardConfig = wizardConfig;
	}

	@Column(name = "wizard_configuration_id", updatable = false, insertable = false)
	public String getWizardConfigid() {
		return wizardConfigid;
	}

	public void setWizardConfigid(String wizardConfigid) {
		this.wizardConfigid = wizardConfigid;
	}

	@Id
	@Column(name = "wizard_config_project_a_id")
	public String getWizardConfigProjectAssId() {
		return wizardConfigProjectAssId;
	}

	public void setWizardConfigProjectAssId(String wizardConfigProjectAssId) {
		this.wizardConfigProjectAssId = wizardConfigProjectAssId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "wizard_config_project_a_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setWizardConfigProjectAssId(IDGenerator.generate(this));
		return getWizardConfigProjectAssId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WizardConfigurationProjectAssignment other = (WizardConfigurationProjectAssignment) obj;
		if ((this.wizardConfigProjectAssId == null) ? (other.wizardConfigProjectAssId != null) : !this.wizardConfigProjectAssId.equals(other.wizardConfigProjectAssId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 41 * hash + (this.wizardConfigProjectAssId != null ? this.wizardConfigProjectAssId.hashCode() : 0);
		return hash;
	}
}
