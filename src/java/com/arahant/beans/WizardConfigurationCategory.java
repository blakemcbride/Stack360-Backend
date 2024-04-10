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
import javax.persistence.*;

@Entity
@Table(name = "wizard_config_category")
public class WizardConfigurationCategory extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "wizard_config_category";
	public static final String WIZARD_CONFIGURATION = "wizardConfiguration";
	public static final String SEQNO = "seqNo";
	public static final String CATEGORY = "benefitCategory";
	private String wizardConfigurationCategoryId;
	private WizardConfiguration wizardConfiguration;
	private int seqNo;
	private HrBenefitCategory benefitCategory;
	private Screen screen;
	private String description;
	private String avatarPath;
	private int avatarLocation;
	private String instructions;

	public WizardConfigurationCategory() {
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "avatar_location")
	public int getAvatarLocation() {
		return avatarLocation;
	}

	public void setAvatarLocation(int avatarLocation) {
		this.avatarLocation = avatarLocation;
	}

	@Column(name = "avatar_path")
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_cat_id")
	public HrBenefitCategory getBenefitCategory() {
		return benefitCategory;
	}

	public void setBenefitCategory(HrBenefitCategory benefitCategory) {
		this.benefitCategory = benefitCategory;
	}

	@Column(name = "instructions")
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@ManyToOne
	@JoinColumn(name = "screen_id")
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Column(name = "seqno")
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	@Id
	@Column(name = "wizard_config_cat_id")
	public String getWizardConfigurationCategoryId() {
		return wizardConfigurationCategoryId;
	}

	public void setWizardConfigurationCategoryId(String wizardConfigurationCategoryId) {
		this.wizardConfigurationCategoryId = wizardConfigurationCategoryId;
	}

	@ManyToOne
	@JoinColumn(name = "wizard_configuration_id")
	public WizardConfiguration getWizardConfiguration() {
		return wizardConfiguration;
	}

	public void setWizardConfiguration(WizardConfiguration wizardConfiguration) {
		this.wizardConfiguration = wizardConfiguration;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "wizard_config_cat_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setWizardConfigurationCategoryId(IDGenerator.generate(this));
		return getWizardConfigurationCategoryId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WizardConfigurationCategory other = (WizardConfigurationCategory) obj;
		if ((this.wizardConfigurationCategoryId == null) ? (other.wizardConfigurationCategoryId != null) : !this.wizardConfigurationCategoryId.equals(other.wizardConfigurationCategoryId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + (this.wizardConfigurationCategoryId != null ? this.wizardConfigurationCategoryId.hashCode() : 0);
		return hash;
	}
}
