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

import com.arahant.business.BEmployee;
import com.arahant.business.BHRWage;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Callbacks from engine go through here for obfuscation purposes
 *
 */
public class AICallbacks {

	public static void notifyDRCofBenefitChange(String benefitJoinId) {
//		BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoinId);
//
//		if (!bj.getBenefitApproved()) {
//			return;
//		}
//
//		//System.out.println("Got called");
//		final String endline = "\r\n";
//		String message = "Employee: " + bj.getPayingPerson().getNameLFM() + endline +
//				"Employee SSN: " + bj.getPayingPerson().getSsn() + endline +
//				"Covered Person: " + bj.getCoveredNameLFM() + endline +
//				"Covered SSN: " + bj.getCoveredSsn() + endline +
//				"Benefit: " + bj.getBenefitConfig().getBenefitName() + endline +
//				"Level: " + bj.getBenefitConfigName() + endline +
//				"Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline +
//				"Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline +
//				"Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline +
//				"Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline +
//				"Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline +
//				"";
//
//		BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
//		BEmployee be = new BEmployee(bj.getPayingPerson());
//
//		BHRBenefitChangeReason bc;
//		try
//		{
//			bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
//		}
//		catch(Exception e)
//		{
//			bc =  new BHRBenefitChangeReason(bj.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId());
//		}
//
//		String sb = new String();
//		sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
//		sb += ("Transaction Type: " + (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
//		sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline;		//Carrier
//		sb += ("Policy: " + bh.getGroupId()) + endline;
//		sb += ("Plan: " + bh.getPlan()) + endline;
//		sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
//		sb += ("Plan Name: " + bh.getPlanName()) + endline;
//		sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
//		sb += ("Qualifying Event: " + bj.getChangeReason()) + endline;  //Manual change
//		sb += ("Qualifying Event date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate()))) + endline;  //QE date
//		sb += ("Email: " + be.getPersonalEmail()) + endline;
//		sb += ("Company: " + be.getCompanyName()) + endline;
//
//		message += sb;
		//System.out.println("EMAIL: " + endline + message);
		//send to all admins for everything
		//DRCMessage.sendBenefitInfoToAdmins("Benefit Change", message);
		//send to DRC only if process was HRSP
		//if (bh.getProcessType().equalsIgnoreCase("H")) {
		//	DRCMessage.send("Benefit Change", message);
		//}
	}

	public static double getLatestWageAmountAnnualRoundedWithCap(String personId, int cap) {
		return BHRWage.getLatestWageAmountAnnualRoundedWithCap(personId, cap);
	}

	public static double getLatestWageAmountWeeklyWithCap(String personId, int cap) {
		return BHRWage.getLatestWageAmountWeeklyWithCap(personId, cap);
	}

	public static double getLatestWageAmountAnnualRounded(String personId) {
		return BHRWage.getLatestWageAmountAnnualRounded(personId);
	}

	public static void setQuickbooksChange(String id, String table) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		QuickbooksChange qc = hsu.createCriteria(QuickbooksChange.class).eq(QuickbooksChange.ARAHANT_ID, id).eq(QuickbooksChange.TABLE, table).first();

		if (qc != null) {
			qc.setRecordChanged('Y');
			hsu.saveOrUpdate(qc);
		}
	}

	public static String getWmCoDefaultCategory(String projectTypeId, String personId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.dontAIIntegrate();

			BPerson bp = new BPerson(personId);

			if (bp.isEmployee()) {
				BEmployee bemp = bp.getBEmployee();

				//is person County
				String[] countyGroups = {"00001-0000000003", "00001-0000000005", "00001-0000000006",/*"00001-0000000007",
					 "00001-0000000008",*/ "00001-0000000009", "00001-0000000010"};

				LinkedList<String> ids = new LinkedList<String>();

				for (String id : countyGroups)
					ids.addAll(new BOrgGroup(id).getAllOrgGroupsInHierarchy());

				boolean isCounty = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSONID, bemp.getPersonId()).in(OrgGroupAssociation.ORG_GROUP_ID, ids).exists();

				String statusId = bemp.getLastStatusId();

				if (isCounty) {

					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000001")) // Add Dep Active
						return "00001-0000000016";
					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000006")) // Add Dep Interim
						return "00001-0000000016";
					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000003")) // Add Dep Loa
						return "00001-0000000017";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000001")) // drury
						return "00001-0000000019";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000006")) // drury
						return "00001-0000000019";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000003")) // drury
						return "00001-0000000022";

					if (projectTypeId.equals("00001-0000000001")) //  Benefit approval
						return "00001-0000000015";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000002")) // drury retiree
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000000")) // drury term
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000004")) // drury part time
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000008")) // drury deceased
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029")) //  Drury Group
						return "00001-0000000019";
				}

				String[] schoolGroups = {"00001-0000000011", "00001-0000000001", "00001-0000000002", "00001-0000000007",
					"00001-0000000008"};

				ids.clear();

				for (String id : schoolGroups)
					ids.addAll(new BOrgGroup(id).getAllOrgGroupsInHierarchy());

				boolean isSchool = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSONID, bemp.getPersonId()).in(OrgGroupAssociation.ORG_GROUP_ID, ids).exists();
				if (isSchool) {
					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000001")) // Add Dep Active
						return "00001-0000000002";
					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000006")) // Add Dep Interim
						return "00001-0000000002";
					if (projectTypeId.equals("00001-0000000002") && statusId.equals("00001-0000000003")) // Add Dep LOA
						return "00001-0000000003";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000001")) // drury
						return "00001-0000000018";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000006")) // drury
						return "00001-0000000018";
					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000003")) // drury
						return "00001-0000000021";

					if (projectTypeId.equals("00001-0000000001")) // Benefit approval
						return "00001-0000000014";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000002")) // drury retiree
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000000")) // drury term
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000004")) // drury part time
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029") && statusId.equals("00001-0000000008")) // drury deceased
						return "00001-0000000020";

					if (projectTypeId.equals("00001-0000000029")) // drury
						return "00001-0000000018";
				}

				if (projectTypeId.equals("00001-0000000005")) // COBRA
					return "00001-0000000001";

				if (projectTypeId.equals("00001-0000000022")) // Flex
					return "00001-0000000005";

				if (projectTypeId.equals("00001-0000000003")) // Drop dep
					return "00001-0000000004";

				if (projectTypeId.equals("00001-0000000007")) // New Hire BOE
					return "00001-0000000008";

				if (projectTypeId.equals("00001-0000000026")) // New Hire WC
					return "00001-0000000009";

				if (projectTypeId.equals("00001-0000000006")) // Retiree
					return "00001-0000000010";
				
				if (projectTypeId.equals("00001-0000000024")) // Term Employent
					return "00001-0000000011";
				
				if (projectTypeId.equals("00001-0000000004")) // LOA BOE
					return "00001-0000000006";
				
				if (projectTypeId.equals("00001-0000000028")) // LOA WCG
					return "00001-0000000007";

				if (projectTypeId.equals("00001-0000000030")) // Drury
					return "00001-0000000020";
				
				if (projectTypeId.equals("00001-0000000031")) // Part Time
					return "00001-0000000023";
				
				if (projectTypeId.equals("00001-0000000032")) // Deceased
					return "00001-0000000024";
			}

			return "00001-0000000000";
		} finally {
			hsu.useAIIntegrate();
		}
	}

	public static float getHolidayCount(String personId, int start) {
		try {
			Calendar cend = DateUtils.getCalendar(start);
			cend.set(Calendar.MONTH, Calendar.DECEMBER);
			cend.set(Calendar.DAY_OF_MONTH, 31);
			int end = DateUtils.getDate(cend);

			ArahantSession.getHSU().dontAIIntegrate();
			BEmployee bemp = new BEmployee(personId);
			int start2 = DateUtils.add(bemp.getStartDateInt(), 90);
			start = Math.max(start2, start);

			float ret = 0;

			//return the count of holidays
			for (Holiday h : ArahantSession.getHSU().createCriteria(Holiday.class).dateBetween(Holiday.HDATE, start, end).list())
				if (h.getPartOfDay() == Holiday.FULL_DAY)
					ret++;
				else
					ret += .5;
			return ret;
		} finally {
			ArahantSession.getHSU().useAIIntegrate();
		}
	}
}
