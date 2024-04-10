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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = BenefitClass.TABLE_NAME)
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitClass extends Setup implements Serializable {

	public static final String BENEFIT_CLASS_ID = "benefitClassId";
	public static final String TABLE_NAME = "hr_benefit_class";
	public static final String CONFIGS = "configs";
	public static final String NAME = "name";
	public static final String BENEFITS = "benefits";
	private static final long serialVersionUID = 1L;
	private String benefitClassId;
	private String name;
	private String description;
	private Set<HrBenefitConfig> configs = new HashSet<HrBenefitConfig>(0);
	private Set<HrBenefit> benefits = new HashSet<HrBenefit>(0);
	
	public BenefitClass() {}

	@ManyToMany
	@JoinTable(name = "benefit_class_join",
	joinColumns = {
		@JoinColumn(name = "benefit_class_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "benefit_config_id")})
	public Set<HrBenefitConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(Set<HrBenefitConfig> configs) {
		this.configs = configs;
	}

	@ManyToMany
	@JoinTable(name = "benefit_group_class_join",
	joinColumns = {
		@JoinColumn(name = "benefit_class_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "benefit_id")})
	public Set<HrBenefit> getBenefits() {
		return benefits;
	}

	public void setBenefits(Set<HrBenefit> benefits) {
		this.benefits = benefits;
	}

	@Id
	@Column(name = "benefit_class_id")
	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}

	@Column(name = "class_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "class_description")
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
		return "benefit_class_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return benefitClassId = IDGenerator.generate(this);
	}

	@Column(name = "last_active_date")
	@Override
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@Column(name = "first_active_date")
	@Override
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	@ManyToOne
	@JoinColumn(name = "org_group_id")
	@Override
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}
}
