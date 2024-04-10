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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = BenefitConfigCost.TABLE_NAME)
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitConfigCost extends ArahantBean implements Serializable {
	
	private static final transient ArahantLogger logger = new ArahantLogger(BenefitConfigCost.class);

    public static final String TABLE_NAME = "benefit_config_cost";
    public static final char APPLIES_COBRA = 'C';
    public static final char APPLIES_ALL = 'A';
    public static final char APPLIES_TABLE = 'S';
    public static final char SOURCE_BASE = 'B';
    public static final char SOURCE_COVERAGE = 'C';
    public static final char SOURCE_ANNUAL_PAY = 'S';
    public static final char MULTIPLIER_COLUMN = 'M';
    public static final char MULTIPLIER_AGE_TABLE = 'A';
    public static final String ORG_GROUP = "appliesToOrgGroup";
    public static final String APPLIES_TO = "appliesToStatus";
    public static final String STATUSES = "statuses";
    public static final String FIRSTACTIVEDATE = "firstActiveDate";
    public static final String LASTACTIVEDATE = "lastActiveDate";
    public static final String BENEFIT_CONFIG = "config";
	private static final long serialVersionUID = 1L;
    protected int lastActiveDate;
    protected int firstActiveDate;
    private String benefitConfigCostId;
    private HrBenefitConfig config;
    private char appliesToStatus;
    private OrgGroup appliesToOrgGroup;
	private char ageCalcType='N';
	private double fixedEmployeeCost;
	private double fixedEmployerCost;
	private double benefitAmount;
	private double minValue;
	private double maxValue;
	private double stepValue;
	private double maxMultipleOfSalary;
	private double ratePerUnit;
	private short rateFrequency = 12;
	private char rateRelatesTo;
	private char salaryRoundType;
	private double salaryRoundAmount;
	private char benefitRoundType;
	private double benefitRoundAmount;
	private char capType;
	private char guaranteedIssueType;
	private double guaranteedIssueAmount;

	public BenefitConfigCost() {
	}
	
	@Column(name = "age_calc_type")
	public char getAgeCalcType() {
		return ageCalcType;
	}

	public void setAgeCalcType(char ageCalcType) {
		this.ageCalcType = ageCalcType;
	}

    private Set<BenefitConfigCostStatus> statuses = new HashSet<BenefitConfigCostStatus>(0);

    @OneToMany(mappedBy = BenefitConfigCostStatus.CONFIG_COST, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<BenefitConfigCostStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<BenefitConfigCostStatus> statuses) {
        this.statuses = statuses;
    }

	@Deprecated
    public double deprecatedGetMultiplier() {
		logger.deprecated();
        return 1;
    }

	@Deprecated
    public void setMultiplier(double multiplier) {
		logger.deprecated();
    }

    @Column(name = "applies_to_status")
    public char getAppliesToStatus() {
        return appliesToStatus;
    }

    public void setAppliesToStatus(char appliesToStatus) {
        this.appliesToStatus = appliesToStatus;
    }

	@Deprecated
    public double deprecatedGetBaseAmount() {
		logger.deprecated();
        return 1;
    }

	@Deprecated
    public void setBaseAmount(double baseAmount) {
        logger.deprecated();
    }

	@Deprecated
    public char deprecatedGetBaseAmountSource() {
		logger.deprecated();
        return 'B';
    }

	@Deprecated
    public void setBaseAmountSource(char baseAmountSource) {
        logger.deprecated();
    }
	
	@Deprecated
    public double deprecatedGetBaseCapAmount() {
		logger.deprecated();
        return 100000;
    }

	@Deprecated
    public void setBaseCapAmount(double baseCapAmount) {
        logger.deprecated();
    }

	@Deprecated
    public double deprecatedGetBaseRoundAmount() {
		logger.deprecated();
        return 1;
    }

	@Deprecated
    public void setBaseRoundAmount(double baseRoundAmount) {
        logger.deprecated();
    }

    @Id
    @Column(name = "benefit_config_cost_id")
    public String getBenefitConfigCostId() {
        return benefitConfigCostId;
    }

    public void setBenefitConfigCostId(String benefitConfigCostId) {
        this.benefitConfigCostId = benefitConfigCostId;
    }

    @ManyToOne
    @JoinColumn(name = "benefit_config_id")
    public HrBenefitConfig getConfig() {
        return config;
    }

    public void setConfig(HrBenefitConfig config) {
        this.config = config;
    }

	@Deprecated
    public double deprecatedGetDivider() {
		logger.deprecated();
        return 1;
    }

	@Deprecated
    public void setDivider(double divider) {
        logger.deprecated();
    }

	@Deprecated
    public char deprecatedGetMultiplierSource() {
		logger.deprecated();
        return 'M';
    }

	@Deprecated
    public void setMultiplierSource(char multiplierSource) {
        logger.deprecated();
    }

    @ManyToOne
    @JoinColumn(name = "org_group_id")
    public OrgGroup getAppliesToOrgGroup() {
        return appliesToOrgGroup;
    }

    public void setAppliesToOrgGroup(OrgGroup orgGroup) {
        this.appliesToOrgGroup = orgGroup;
    }

    @Column(name = "last_active_date")
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public void setFirstActiveDate(int firstActiveDate) {
        this.firstActiveDate = firstActiveDate;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "benefit_config_cost_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return benefitConfigCostId = IDGenerator.generate(this);
    }

    @Column(name = "first_active_date")
    public int getFirstActiveDate() {
        return firstActiveDate;
    }

	@Deprecated
	public char deprecatedGetCostPerEnrollee() {
		logger.deprecated();
		return 'N';
	}

	@Deprecated
	public void setCostPerEnrollee(char costPerEnrollee) {
		logger.deprecated();
	}

	@Column(name = "fixed_employee_cost")
	public double getFixedEmployeeCost() {
		return fixedEmployeeCost;
	}

	public void setFixedEmployeeCost(double fixedEmployeeCost) {
		this.fixedEmployeeCost = fixedEmployeeCost;
	}

	@Column(name = "fixed_employer_cost")
	public double getFixedEmployerCost() {
		return fixedEmployerCost;
	}

	public void setFixedEmployerCost(double fixedEmployerCost) {
		this.fixedEmployerCost = fixedEmployerCost;
	}

	@Column(name = "benefit_amount")
	public double getBenefitAmount() {
		return benefitAmount;
	}

	public void setBenefitAmount(double benefitAmount) {
		this.benefitAmount = benefitAmount;
	}

	@Column(name = "min_value")
	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	@Column(name = "max_value")
	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "step_value")
	public double getStepValue() {
		return stepValue;
	}

	public void setStepValue(double stepValue) {
		this.stepValue = stepValue;
	}

	@Column(name = "max_multiple_of_salary")
	public double getMaxMultipleOfSalary() {
		return maxMultipleOfSalary;
	}

	public void setMaxMultipleOfSalary(double maxMultipleOfSalary) {
		this.maxMultipleOfSalary = maxMultipleOfSalary;
	}

	@Column(name = "rate_per_unit")
	public double getRatePerUnit() {
		return ratePerUnit;
	}

	public void setRatePerUnit(double ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}

	@Column(name = "rate_frequency")
	public short getRateFrequency() {
		return rateFrequency;
	}

	public void setRateFrequency(short rateFrequency) {
		this.rateFrequency = rateFrequency;
	}

	@Column(name = "rate_relates_to")
	public char getRateRelatesTo() {
		return rateRelatesTo;
	}

	public void setRateRelatesTo(char rateRelatesTo) {
		this.rateRelatesTo = rateRelatesTo;
	}

	@Column(name = "salary_round_type")
	public char getSalaryRoundType() {
		return salaryRoundType;
	}

	public void setSalaryRoundType(char salaryRoundType) {
		this.salaryRoundType = salaryRoundType;
	}

	@Column(name = "salary_round_amount")
	public double getSalaryRoundAmount() {
		return salaryRoundAmount;
	}

	public void setSalaryRoundAmount(double salaryRoundAmount) {
		this.salaryRoundAmount = salaryRoundAmount;
	}

	@Column(name = "benefit_round_type")
	public char getBenefitRoundType() {
		return benefitRoundType;
	}

	public void setBenefitRoundType(char benefitRoundType) {
		this.benefitRoundType = benefitRoundType;
	}

	@Column(name = "benefit_round_amount")
	public double getBenefitRoundAmount() {
		return benefitRoundAmount;
	}

	public void setBenefitRoundAmount(double benefitRoundAmount) {
		this.benefitRoundAmount = benefitRoundAmount;
	}

	@Column(name = "cap_type")
	public char getCapType() {
		return capType;
	}

	public void setCapType(char cap_type) {
		this.capType = cap_type;
	}

	@Column(name = "guaranteed_issue_type")
	public char getGuaranteedIssueType() {
		return guaranteedIssueType;
	}

	public void setGuaranteedIssueType(char guaranteedIssueType) {
		this.guaranteedIssueType = guaranteedIssueType;
	}

	@Column(name = "guaranteed_issue_amount")
	public double getGuaranteedIssueAmount() {
		return guaranteedIssueAmount;
	}

	public void setGuaranteedIssueAmount(double guaranteedIssueAmount) {
		this.guaranteedIssueAmount = guaranteedIssueAmount;
	}

}
