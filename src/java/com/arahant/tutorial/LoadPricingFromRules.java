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
package com.arahant.tutorial;

import com.arahant.beans.BenefitConfigCost;
import com.arahant.business.BBenefitConfigCost;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPaySchedule;
import com.arahant.business.BPaySchedulePeriod;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LoadPricingFromRules {

	public static void main(String[] args) throws Exception {

		/*
		 *
		 * (assert (benefitCost (startDate 0)(endDate 20081231) (configId "00001-0000000032")(category "Vision")(configName "Vision - Employee Only")(statusName "active")(cost 8.40)))
		 (assert (benefitCost (startDate 0)(endDate 20081231) (configId "00001-0000000032")(category "Vision")(configName "Vision - Employee Only")(statusName "retiree")(cost 8.40)))
		 * */

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();
		hsu.dontAIIntegrate();

		BPaySchedule countyPS = new BPaySchedule();
		countyPS.create();
		countyPS.setName("County");
		countyPS.setDescription("County");
		countyPS.insert();

		BOrgGroup borg = new BOrgGroup("00001-0000000000");
		borg.setPayScheduleId(countyPS.getId());
		borg.update();



		BPaySchedule boeCPS = new BPaySchedule();
		boeCPS.create();
		boeCPS.setName("BOE-C");
		boeCPS.setDescription("BOE-C");
		boeCPS.insert();

		borg = new BOrgGroup("00001-0000000002");
		borg.setPayScheduleId(boeCPS.getId());
		borg.update();


		BPaySchedule boePPS = new BPaySchedule();
		boePPS.create();
		boePPS.setName("BOE-P");
		boePPS.setDescription("BOE-P");
		boePPS.insert();


		borg = new BOrgGroup("00001-0000000001");
		borg.setPayScheduleId(boePPS.getId());
		borg.update();


		BufferedReader br = new BufferedReader(new InputStreamReader(LoadPricingFromRules.class.getClassLoader().getResourceAsStream("WilliamsonCountyRules.jess")));

		String line;

		while ((line = br.readLine()) != null) {

			if (line.trim().startsWith("(assert") && line.indexOf("benefitCost") != -1) {
				if (getValue(line, "cost").equals("N/A"))
					continue;
				if (getValue(line, "cost").equals("choose"))
					continue;
				if (getValue(line, "cost").equals("Pending Approval"))
					continue;



				System.out.println(line);
				int startDate = Integer.parseInt(getValue(line, "startDate"));
				int endDate = Integer.parseInt(getValue(line, "endDate"));
				String configId = getValue(line, "configId");
				String statusName = getValue(line, "statusName");
				float cost = Float.parseFloat(getValue(line, "cost"));

				System.out.println(configId + " " + statusName + " " + startDate + " " + endDate + " " + cost);

				BBenefitConfigCost c = new BBenefitConfigCost();
				c.create();
				c.setBenefitConfigId(configId);
				c.setBaseAmount(cost);
				c.setFirstActiveDate(startDate);
				c.setLastActiveDate(endDate);
				c.setBaseAmountSource(BenefitConfigCost.SOURCE_BASE);
				c.setMultiplier(1);
				c.setMultiplierSource(BenefitConfigCost.MULTIPLIER_COLUMN);
				c.setBaseRoundAmount(.01);
				c.setDivider(1);
				c.setStatusType(BenefitConfigCost.APPLIES_TABLE + "");
				c.setOrgGroupId("00001-0000000000");
				if (statusName.equals("cobra"))
					c.setStatusType(BenefitConfigCost.APPLIES_COBRA + "");
				if (statusName.equals("retiree"))
					c.setStatusIds(new String[]{"00001-0000000002"});
				if (statusName.equals("active"))
					c.setStatusIds(new String[]{"00001-0000000001", "00001-0000000003", "00001-0000000004", "00001-0000000006", "00001-0000000007", "00001-0000000009"});
				c.insert();

			}

			//	(assert (PayPeriod County 20080104 26))
			if (line.trim().startsWith("(assert") && line.indexOf("PayPeriod County") != -1) {
				int pos = line.indexOf("PayPeriod County") + "PayPeriod County".length() + 1;

				int date = Integer.parseInt(line.substring(pos, line.indexOf(' ', pos)).trim());

				BPaySchedulePeriod per = new BPaySchedulePeriod();
				per.create();
				per.setScheduleId(countyPS.getId());
				per.setEndDate(date);
				per.setPayDate(date);
				per.insert();

			}

			if (line.trim().startsWith("(assert") && line.indexOf("PayPeriod BOE-C") != -1) {
				int pos = line.indexOf("PayPeriod BOE-C") + "PayPeriod BOE-C".length() + 1;

				int date = Integer.parseInt(line.substring(pos, line.indexOf(' ', pos)).trim());

				BPaySchedulePeriod per = new BPaySchedulePeriod();
				per.create();
				per.setScheduleId(boeCPS.getId());
				per.setEndDate(date);
				per.setPayDate(date);
				per.insert();

			}

			if (line.trim().startsWith("(assert") && line.indexOf("PayPeriod BOE-P") != -1) {
				int pos = line.indexOf("PayPeriod BOE-P") + "PayPeriod BOE-P".length() + 1;

				int date = Integer.parseInt(line.substring(pos, line.indexOf(' ', pos)).trim());

				BPaySchedulePeriod per = new BPaySchedulePeriod();
				per.create();
				per.setScheduleId(boePPS.getId());
				per.setEndDate(date);
				per.setPayDate(date);
				per.insert();

			}
		}

		hsu.commitTransaction();
	}

	private static String getValue(String line, String field) {
		int pos = line.indexOf(field) + field.length();

		String val = line.substring(pos, line.indexOf(")", pos)).trim();

		if (val.startsWith("\""))
			val = val.substring(1);
		if (val.endsWith("\""))
			val = val.substring(0, val.length() - 1);

		return val.trim();
	}
}
