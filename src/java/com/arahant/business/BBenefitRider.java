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
package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import java.util.List;

public class BBenefitRider extends SimpleBusinessObjectBase<BenefitRider> implements IDBFunctions {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BBenefitRider.class);

	public BBenefitRider() {
	}

	static BBenefitRider[] makeArray(List<BenefitRider> l) {
		BBenefitRider[] ret = new BBenefitRider[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBenefitRider(l.get(loop));
		return ret;
	}

	/**
	 * @param accrual
	 */
	public BBenefitRider(final BenefitRider benefitRider) {
		bean = benefitRider;
	}

	public BBenefitRider(String benefitRiderId) {
		bean = ArahantSession.getHSU().get(BenefitRider.class, benefitRiderId);
	}

	@Override
	public String create() throws ArahantException {
		bean = new BenefitRider();

		bean.setBenefitRiderId(IDGenerator.generate(bean));

		return bean.getBenefitRiderId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitRider.class, key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	public HrBenefit getBaseBenefit() {
		return bean.getBaseBenefit();
	}

	public void setBaseBenefit(HrBenefit baseBenefit) {
		bean.setBaseBenefit(baseBenefit);
	}

	public String getBaseBenefitId() {
		return bean.getBaseBenefitId();
	}

	public void setBaseBenefitId(String baseBenefitId) {
		bean.setBaseBenefit(ArahantSession.getHSU().get(HrBenefit.class, baseBenefitId));
	}

	public String getBenefitRiderId() {
		return bean.getBenefitRiderId();
	}

	public char getHidden() {
		return bean.getHidden();
	}

	public Boolean getHiddenBoolean() {
		return bean.getHidden() == 'Y';
	}

	public String getHiddenString() {
		return bean.getHidden() + "";
	}

	public void setHidden(char hidden) {
		bean.setHidden(hidden);
	}

	public void setHidden(String hidden) {
		bean.setHidden(hidden.charAt(0));
	}

	public void setHidden(Boolean hidden) {
		bean.setHidden(hidden ? 'Y' : 'N');
	}

	public char getRequired() {
		return bean.getRequired();
	}

	public String getRequiredString() {
		return bean.getRequired() + "";
	}

	public Boolean getRequiredBoolean() {
		return bean.getRequired() == 'Y';
	}

	public void setRequired(char required) {
		bean.setRequired(required);
	}

	public void setRequired(String required) {
		bean.setRequired(required.charAt(0));
	}

	public void setRequired(Boolean required) {
		bean.setRequired(required ? 'Y' : 'N');
	}

	public HrBenefit getRiderBenefit() {
		return bean.getRiderBenefit();
	}

	public void setRiderBenefit(HrBenefit riderBenefit) {
		bean.setRiderBenefit(riderBenefit);
	}

	public String getRiderBenefitId() {
		return bean.getRiderBenefitId();
	}

	public void setRiderBenefitId(String riderBenefitId) {
		bean.setRiderBenefit(ArahantSession.getHSU().get(HrBenefit.class, riderBenefitId));
	}

	public static BBenefitRider[] list(String benefitId) {
		return makeArray(ArahantSession.getHSU().createCriteria(BenefitRider.class).eq(BenefitRider.BASE_BENEFIT_ID, benefitId).joinTo(BenefitRider.RIDER_BENEFIT).orderBy(HrBenefit.NAME).list());
	}

	public static List<BenefitRider> getRidersForBaseBenefit(BHRBenefit bb) {
		return ArahantSession.getHSU().createCriteria(BenefitRider.class).eq(BenefitRider.BASE_BENEFIT, bb.getBean()).list();
	}

	public HrBenefit getMasterBaseBenefit() {
		if (bean.getBaseBenefit() != null)
			if (bean.getBaseBenefit().getRidersOnRiderBenefit() != null && !bean.getBaseBenefit().getRidersOnRiderBenefit().isEmpty())
				for (BenefitRider br : bean.getBaseBenefit().getRidersOnRiderBenefit())
					return new BBenefitRider(br).getMasterBaseBenefit();
			else
				return bean.getBaseBenefit();

		return null;
	}
}
