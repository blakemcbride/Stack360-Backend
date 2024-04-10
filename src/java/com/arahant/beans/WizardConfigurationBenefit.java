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
@Table(name = WizardConfigurationBenefit.TABLE_NAME)
public class WizardConfigurationBenefit extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "wizard_config_benefit";
	public static final String ID = "wizardConfigurationBenefitId";
	public static final String WIZARD_CONFIGURATION_CATEGORY = "wizardConfigurationCategory";
	public static final String SEQNO = "seqNo";
	public static final String BENEFIT = "benefit";
	public static final String NAME = "name";
	public static final String SCREEN = "screen";
	private static final long serialVersionUID = 1L;
	private String wizardConfigurationBenefitId;
	private WizardConfigurationCategory wizardConfigurationCategory;
	private int seqNo;
	private HrBenefit benefit;
	private Screen screen;
	private String name;
	private String avatarPath;
	private int avatarLocation;
	private String instructions;
	private String declineMessage;

	public WizardConfigurationBenefit() {
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
	@JoinColumn(name = "benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefit) {
		this.benefit = benefit;
	}

	@Column(name = "instructions")
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@Column(name = "benefit_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "decline_message")
	public String getDeclineMessage() {
		return declineMessage;
	}

	public void setDeclineMessage(String declineMessage) {
		this.declineMessage = declineMessage;
	}

	@Id
	@Column(name = "wizard_config_ben_id")
	public String getWizardConfigurationBenefitId() {
		return wizardConfigurationBenefitId;
	}

	public void setWizardConfigurationBenefitId(String wizardConfigurationBenefitId) {
		this.wizardConfigurationBenefitId = wizardConfigurationBenefitId;
	}

	@ManyToOne
	@JoinColumn(name = "wizard_config_cat_id")
	public WizardConfigurationCategory getWizardConfigurationCategory() {
		return wizardConfigurationCategory;
	}

	public void setWizardConfigurationCategory(WizardConfigurationCategory wizardConfigurationCategory) {
		this.wizardConfigurationCategory = wizardConfigurationCategory;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "wizard_config_ben_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setWizardConfigurationBenefitId(IDGenerator.generate(this));
		return getWizardConfigurationBenefitId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WizardConfigurationBenefit other = (WizardConfigurationBenefit) obj;
		if ((this.wizardConfigurationBenefitId == null) ? (other.wizardConfigurationBenefitId != null) : !this.wizardConfigurationBenefitId.equals(other.wizardConfigurationBenefitId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + (this.wizardConfigurationBenefitId != null ? this.wizardConfigurationBenefitId.hashCode() : 0);
		return hash;
	}
}
