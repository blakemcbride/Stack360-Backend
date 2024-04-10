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

import com.arahant.beans.BenefitConfigCostAge;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.ABCL;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

public class BBenefitConfigCostAge extends SimpleBusinessObjectBase<BenefitConfigCostAge> {
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BBenefitConfigCostAge.class);

	public BBenefitConfigCostAge(String key) {
		super(key);
	}

	public BBenefitConfigCostAge() {
	}

	public BBenefitConfigCostAge(BenefitConfigCostAge o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new BenefitConfigCostAge();
		return bean.generateId();
	}

	public String getId() {
		return bean.getBenefitConfigCostAgeId();
	}

	public int getMaxAge() {
		return bean.getMaxAge();
	}

	@Deprecated
	public double getMultiplier() {
		logger.deprecated();
		return 1;
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitConfigCostAge.class, key);
	}

	public void setConfigCost(BBenefitConfigCost cost) {
		bean.setCost(cost.bean);
	}

	public void setMaxAge(int maxAge) {
		bean.setMaxAge((short) maxAge);
	}

	@Deprecated
	public void setMultiplier(double multiplier) {
		logger.deprecated();
	}
	public double getEeValue() {
		return bean.getEeValue();
	}

	public void setEeValue(double eeValue) {
		bean.setEeValue(eeValue);
	}

	public double getErValue() {
		return bean.getErValue();
	}

	public void setErValue(double erValue) {
		bean.setErValue(erValue);
	}

	public String getInsuranceId() {
		return bean.getInsuranceId();
	}

	public void setInsuranceId(String insuranceId) {
		bean.setInsuranceId(insuranceId);
	}

//	public static void main(String[] args) {
//		ABCL.init();
////		try {
//			HibernateSessionUtil hsu = ArahantSession.getHSU();
//			hsu.setCurrentPersonToArahant();
//			DelimitedFileReader fr = new DelimitedFileReader("/Users/arahant/Desktop/costAgeImport.csv");
//			//skip 2 header lines
//			fr.nextLine();
//			fr.nextLine();
//
//			hsu.beginTransaction();
//			while (fr.nextLine()) {
//				int age = fr.getInt(0);
//				double cost = fr.getDouble(2);
//				System.out.println("Doing age " + age + " / " + cost);
//				for(BenefitConfigCost bcc : hsu.createCriteria(BenefitConfigCost.class).list())
//				{
//					BBenefitConfigCostAge bca = new BBenefitConfigCostAge();
//					bca.create();
//					bca.setConfigCost(new BBenefitConfigCost(bcc));
//					bca.setMaxAge(age + 1);
//					bca.setMultiplier(cost);
//					bca.insert();
//				}
//			}
//			DelimitedFileReader fr = new DelimitedFileReader("/Users/arahant/Desktop/ltcOptionalAges.csv");
//
//			hsu.beginTransaction();
//			while (fr.nextLine()) {
//				int age = fr.getInt(0);
//				double cost = fr.getDouble(1);
//				System.out.println("Doing age " + age + " / " + cost);
//				for(BenefitConfigCost bcc : hsu.createCriteria(BenefitConfigCost.class).list())
//				{
//					if(!bcc.getConfig().getHrBenefit().getName().contains("LTC"))
//						continue;
//					bcc.setMultiplierSource('A');
//					bcc.setAgeCalcType('S');
//					ArahantSession.getHSU().update(bcc);
//					BBenefitConfigCostAge bca = new BBenefitConfigCostAge();
//					bca.create();
//					bca.setConfigCost(new BBenefitConfigCost(bcc));
//					bca.setMaxAge(age + 1);
//					bca.setMultiplier(cost);
//					bca.insert();
//				}
//			}
//			DelimitedFileReader fr = new DelimitedFileReader("/Users/arahant/Desktop/waivorOptionalAges.csv");
//
//			hsu.beginTransaction();
//			while (fr.nextLine()) {
//				int age = fr.getInt(0);
//				double cost = fr.getDouble(1);
//				System.out.println("Doing age " + age + " / " + cost);
//				for(BenefitConfigCost bcc : hsu.createCriteria(BenefitConfigCost.class).list())
//				{
//					if(!bcc.getConfig().getHrBenefit().getName().contains("Waiver"))
//						continue;
//					bcc.setMultiplierSource('A');
//					bcc.setAgeCalcType('S');
//					ArahantSession.getHSU().update(bcc);
//					BBenefitConfigCostAge bca = new BBenefitConfigCostAge();
//					bca.create();
//					bca.setConfigCost(new BBenefitConfigCost(bcc));
//					bca.setMaxAge(age + 1);
//					bca.setMultiplier(cost);
//					bca.insert();
//				}
//			}
//			hsu.commitTransaction();
//		} catch (IOException ex) {
//			Logger.getLogger(BBenefitConfigCostAge.class.getName()).log(Level.SEVERE, null, ex);
//		} catch (Exception ex) {
//			Logger.getLogger(BBenefitConfigCostAge.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
//	public static void main(String[] args) {
//		BenefitConfigCost bcc = ArahantSession.getHSU().get(BenefitConfigCost.class, "00001-0000000002");
//		BBenefitConfigCost bbcc = new BBenefitConfigCost(bcc);
//		bbcc.delete();
//	}
	public static void main(String[] args) {
		ABCL.init();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.beginTransaction();

		for (BenefitConfigCostAge ageCost : ArahantSession.getHSU().createCriteria(BenefitConfigCostAge.class).list()) {
			System.out.println(ageCost.getMaxAge() + " / " + ageCost.deprecatedGetMultiplier() + " -> " + (ageCost.deprecatedGetMultiplier() / 10.0f));
			ageCost.setMultiplier(ageCost.deprecatedGetMultiplier() / 10.0f);
			ArahantSession.getHSU().update(ageCost);
		}
		hsu.commitTransaction();
	}
}
