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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "hr_benefit_category")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrBenefitCategory extends ArahantBean implements java.io.Serializable {

	//DO NOT ADD IN THE MIDDLE, All new types must be added at the end of the list!!
	public static final String types[] = {"Health", "Dental", "Life", "Vision", "Short Term Disability", "Long Term Disability", "Flex", "Voluntary", "Misc.", "Pension", "Prescription"};
	public final static short HEALTH = 0;
	public final static short DENTAL = 1;
	public final static short LIFE = 2;
	public final static short VISION = 3;
	public final static short SHORT_TERM_CARE = 4;
	public final static short LONG_TERM_CARE = 5;
	public static final short FLEX_TYPE = 6;
	public static final short VOLUNTARY = 7;
	public static final short MISC = 8;
	public static final short PENSION = 9;
	public final static short PRESCRIPTION = 10;
	/**
	 *
	 */
	private static final long serialVersionUID = -802821841797998956L;
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "benefitType";
	public static final String BENEFIT_CATEGORY_ID = "benefitCatId";
	//public static final Object MEDICAL = null;
	public static final String HRBENEFIT = "benefits";
	private Set<HrBenefitJoin> hrBenefitJoins = new HashSet<HrBenefitJoin>(0);
	public static final String HREMPLOYEEBENEFITJOINS = "hrBenefitJoins";
	public static final String REQUIRES_DECLINE = "requiresDecline";
	public static final String EXCLUSIVE = "mutuallyExclusive";
	public static final String COMPANY = "company";
	public static final String SEQ = "sequence";
	private short sequence;
	private String avatarPath;
	public final static String OPEN_ENROLLMENT_WIZARD = "openEnrollmentWizard";
	private char openEnrollmentWizard = 'N';
	private char onboardingWizard = 'N';
	private String instructions;
	private Screen openEnrollmentScreen;
	private Screen onboardingScreen;
	private String avatarLocation;

	/**
	 * @return Returns the hrEmployeeBenefitJoins.
	 */
	@OneToMany(mappedBy = HrBenefitJoin.HR_BENEFIT_CATEGORY, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefitJoin> getHrBenefitJoins() {
		return hrBenefitJoins;
	}

	/**
	 * @param hrEmployeeBenefitJoins The hrEmployeeBenefitJoins to set.
	 */
	public void setHrBenefitJoins(final Set<HrBenefitJoin> hrEmployeeBenefitJoins) {
		this.hrBenefitJoins = hrEmployeeBenefitJoins;
	}

	public HrBenefitCategory() {
		super();
	}
	private String benefitCatId;
	private String description;
	private short benefitType;
	private char mutuallyExclusive = 'Y';
	private char requiresDecline = 'N';
	private CompanyDetail company;
	private Set<HrBenefit> benefits = new HashSet<HrBenefit>(0);


	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		benefitCatId = IDGenerator.generate(this);
		return benefitCatId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {

		return "benefit_cat_id";
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {
		return "hr_benefit_category";
	}

	/**
	 * @return Returns the benefitCatId.
	 */
	@Id
	@Column(name = "benefit_cat_id")
	public String getBenefitCatId() {
		return benefitCatId;
	}

	/**
	 * @param benefitCatId The benefitCatId to set.
	 */
	public void setBenefitCatId(String benefitCatId) {
		firePropertyChange("benefitCatId", this.benefitCatId, benefitCatId);
		this.benefitCatId = benefitCatId;
	}

	/**
	 * @return Returns the benefits.
	 */
	@OneToMany(mappedBy = HrBenefit.BENEFIT_CATEGORY, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrBenefit> getBenefits() {
		return benefits;
	}

	/**
	 * @param benefits The benefits to set.
	 */
	public void setBenefits(Set<HrBenefit> benefits) {
		this.benefits = benefits;
	}

	/**
	 * @return Returns the benefitType.
	 */
	@Column(name = "benefit_type")
	public short getBenefitType() {
		return benefitType;
	}

	/**
	 * @param benefitType The benefitType to set.
	 */
	public void setBenefitType(short benefitType) {
		firePropertyChange("benefitType", this.benefitType, benefitType);
		this.benefitType = benefitType;
	}

	/**
	 * @return Returns the description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		firePropertyChange("description", this.description, description);
		this.description = description;
	}

	/**
	 * @return Returns the mutuallyExclusive.
	 */
	@Column(name = "mutually_exclusive")
	public char getMutuallyExclusive() {
		return mutuallyExclusive;
	}

	/**
	 * @param mutuallyExclusive The mutuallyExclusive to set.
	 */
	public void setMutuallyExclusive(char mutuallyExclusive) {
		firePropertyChange("mutuallyExclusive", this.mutuallyExclusive,
				mutuallyExclusive);
		this.mutuallyExclusive = mutuallyExclusive;
	}

	/**
	 * @return Returns the requiresDecline.
	 */
	@Column(name = "requires_decline")
	public char getRequiresDecline() {
		return requiresDecline;
	}

	/**
	 * @param requiresDecline The requiresDecline to set.
	 */
	public void setRequiresDecline(char requiresDecline) {
		firePropertyChange("requiresDecline", this.requiresDecline, requiresDecline);
		this.requiresDecline = requiresDecline;
	}

	/**
	 * @return
	 */
	@Transient
	public boolean getAllowsMultipleBenefitsBoolean() {

		return mutuallyExclusive == 'N';
	}

	@Override
	public boolean equals(Object o) {
		if (benefitCatId == null && o == null)
			return true;
		if (benefitCatId != null && o instanceof HrBenefitCategory)
			return benefitCatId.equals(((HrBenefitCategory) o).getBenefitCatId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitCatId == null)
			return 0;
		return benefitCatId.hashCode();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

//    public int compareTo(HrBenefitCategory o) {
//        return o.sequence - sequence;
//    }
	@Column(name = "seqno")
	public short getSequence() {
		return sequence;
	}

	public void setSequence(short seqno) {
		this.sequence = seqno;
	}

	//@Transient
	@Column(name = "avatar_path")
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	@Column(name = "open_enrollment_wizard")
	public char getOpenEnrollmentWizard() {
		return openEnrollmentWizard;
	}

	public void setOpenEnrollmentWizard(char openEnrollmentWizard) {
		this.openEnrollmentWizard = openEnrollmentWizard;
	}

	@Column(name = "onboarding_wizard")
	public char getOnboarding() {
		return onboardingWizard;
	}

	public void setOnboarding(char onboarding) {
		this.onboardingWizard = onboarding;
	}

	@Column(name = "instructions")
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "open_enrollment_screen_id")
	public Screen getOpenEnrollmentScreen() {
		return openEnrollmentScreen;
	}

	public void setOpenEnrollmentScreen(Screen openEnrollmentScreen) {
		this.openEnrollmentScreen = openEnrollmentScreen;
	}

	@Column(name = "avatar_location")
	public String getAvatarLocation() {
		return avatarLocation;
	}

	public void setAvatarLocation(String avatarLocation) {
		this.avatarLocation = avatarLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onboarding_screen_id")
	public Screen getOnboardingScreen() {
		return onboardingScreen;
	}

	public void setOnboardingScreen(Screen onboardingScreen) {
		this.onboardingScreen = onboardingScreen;
	}
}
