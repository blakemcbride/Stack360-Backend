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


/**
 * 
 */
package com.arahant.services.standard.hr.benefitWizardStatus;

import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.HrEmplDependentChangeRequest;
import com.arahant.beans.Person;
import com.arahant.beans.PersonCR;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SearchEmployeeWizardStatusesReturnItem {

	public SearchEmployeeWizardStatusesReturnItem() {
		
	}

	SearchEmployeeWizardStatusesReturnItem(BEmployee be, List<HrBenefitJoin> empJoins, PersonCR empCR, List<PersonCR> empDeps) {
		employeeName = be.getNameLFM();
		employeeId = be.getPersonId();
		if(be.getBenefitWizardStatus().equals("N"))
			wizardStatus = "No Change";// -- Employee has not been in the wizard.";
		else if(be.getBenefitWizardStatus().equals("U"))
			wizardStatus = "Unfinalized";// -- Changes have been made but not finalized.";
		else if(be.getBenefitWizardStatus().equals("F"))
			wizardStatus = "Finalized";// -- Changes ready to be processed.";
		else if(be.getBenefitWizardStatus().equals("P"))
			wizardStatus = "Processed";// -- Changes processed.";
		wizardDate = be.getBenefitWizardDate();
		hasBenefitChange = (empJoins != null && empJoins.size() > 0);
		hasDemographicChange = (empCR != null);
		hasDependentChange = (empDeps != null && empDeps.size() > 0);
//		be.loadPending(employeeId);
//		BHREmplDependent[] depArr = be.listPendingDependents();
//		hasDependentChange = depArr.length > 0;
		changeDescription = "";
		if(hasDemographicChange || hasDependentChange || hasBenefitChange)
		{
			if(hasDemographicChange)
			{
				changeDescription += "Demographic Changes: \n";
				String temp = getPersonChanges(empCR, false);
				changeDescription += StringUtils.isEmpty(temp) ? "    [Confirmed Current] \n" : temp;
			}
			if(hasDependentChange)
			{
				changeDescription += "Dependent Changes: \n";
//				for(BHREmplDependent dep : depArr)
//				{
//					changeDescription += getDependentChanges(dep);
//				}
				for(PersonCR depCR : empDeps)
				{
					changeDescription += getPersonChanges(depCR, true);
				}
			}
			if(hasBenefitChange)
			{
				changeDescription += "Benefit Changes: \n";
				HashMap<String, List<HrBenefitJoin>> empJoinGroupMap = new HashMap<String, List<HrBenefitJoin>>();
				for(HrBenefitJoin j : empJoins)
				{
					if(j.getHrBenefitConfig() != null && j.getHrBenefit() == null)
					{
						if(empJoinGroupMap.containsKey(j.getHrBenefitConfig().getBenefitConfigId()+ ":"))
						{
							List<HrBenefitJoin> tempList = empJoinGroupMap.get(j.getHrBenefitConfig().getBenefitConfigId()+ ":");
							tempList.add(j);
							empJoinGroupMap.put(j.getHrBenefitConfig().getBenefitConfigId()+ ":", tempList);
						}
						else
						{
							List<HrBenefitJoin> tempList = new ArrayList<HrBenefitJoin>();
							tempList.add(j);
							empJoinGroupMap.put(j.getHrBenefitConfig().getBenefitConfigId()+ ":", tempList);
						}
					}
					else
					{
						if(empJoinGroupMap.containsKey(":" + j.getHrBenefit().getBenefitId()))
						{
							List<HrBenefitJoin> tempList = empJoinGroupMap.get(j.getHrBenefit().getBenefitId()+ ":");
							tempList.add(j);
							empJoinGroupMap.put(":" + j.getHrBenefit().getBenefitId(), tempList);
						}
						else
						{
							List<HrBenefitJoin> tempList = new ArrayList<HrBenefitJoin>();
							tempList.add(j);
							empJoinGroupMap.put(":" + j.getHrBenefit().getBenefitId(), tempList);
						}
					}
				}
				Iterator x = empJoinGroupMap.entrySet().iterator();
				while(x.hasNext())
				{
					Map.Entry entry = (Map.Entry)x.next();
					changeDescription += getBenefitChanges((List)entry.getValue());
				}
			}
		}
		else
		{
			changeDescription = "";
		}
	}
	private String changeDescription;
	private String employeeName;
	private String wizardStatus;
	private int wizardDate;
	private boolean hasDemographicChange;
	private boolean hasDependentChange;
	private boolean hasBenefitChange;
	private String employeeId;
	private boolean select = false;

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getChangeDescription() {
		return changeDescription;
	}

	public void setChangeDescription(String changeDescription) {
		this.changeDescription = changeDescription;
	}

	public boolean getHasBenefitChange() {
		return hasBenefitChange;
	}

	public void setHasBenefitChange(boolean hasBenefitChange) {
		this.hasBenefitChange = hasBenefitChange;
	}

	public boolean getHasDemographicChange() {
		return hasDemographicChange;
	}

	public void setHasDemographicChange(boolean hasDemographicChange) {
		this.hasDemographicChange = hasDemographicChange;
	}

	public boolean getHasDependentChange() {
		return hasDependentChange;
	}

	public void setHasDependentChange(boolean hasDependentChange) {
		this.hasDependentChange = hasDependentChange;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getWizardDate() {
		return wizardDate;
	}

	public void setWizardDate(int wizardDate) {
		this.wizardDate = wizardDate;
	}

	public String getWizardStatus() {
		return wizardStatus;
	}

	public void setWizardStatus(String wizardStatus) {
		this.wizardStatus = wizardStatus;
	}

	private String getPersonChanges(PersonCR empCR, boolean isDep) {
		Person realPerson = empCR.getRealRecord();
		Person changePerson = empCR.getChangeRecord();
		String ret = "";

		if(isDep && realPerson == null)
		{
			ret += "    Add Dependent \n";
			ret += "        First Name: " + formatString(changePerson.getFname()) + "\n";
			ret += "        Middle Name: " + formatString(changePerson.getMname()) + "\n";
			ret += "        Last Name: " + formatString(changePerson.getLname()) + "\n";
			ret += "        SSN: " + formatString(changePerson.getUnencryptedSsn()) + "\n";
			ret += "        DOB: " + formatString(DateUtils.getDateFormatted(changePerson.getDob())) + "\n";
			ret += "        Gender: " + formatString(changePerson.getSex() + "") + "\n";
		}
		else
		{
			if(isDep)
			{
				ret += "      Modify Dependent: \n";
			}
			BPerson bp = new BPerson(realPerson);
			if(!realPerson.getFname().equals(changePerson.getFname()))
				ret += "        First Name: " + formatString(realPerson.getFname()) + " -> " + formatString(changePerson.getFname()) + "\n";
			if(!realPerson.getMname().equals(changePerson.getMname()))
				ret += "        Middle Name: " + formatString(realPerson.getMname()) + " -> " + formatString(changePerson.getMname()) + "\n";
			if(!realPerson.getLname().equals(changePerson.getLname()))
				ret += "        Last Name: " + formatString(realPerson.getLname()) + " -> " + formatString(changePerson.getLname()) + "\n";
//			if(!realPerson.getNickName().equals(changePerson.getNickName()))
//				ret += "        Nickname: " + formatString(realPerson.getNickName()) + " -> " + formatString(changePerson.getNickName()) + "\n";
			if(!realPerson.getUnencryptedSsn().equals(changePerson.getUnencryptedSsn()))
				ret += "        SSN: " + formatString(realPerson.getUnencryptedSsn()) + " -> " + formatString(changePerson.getUnencryptedSsn()) + "\n";
			if(!realPerson.getPersonalEmail().equals(changePerson.getPersonalEmail()))
				ret += "        Email: " + formatString(realPerson.getPersonalEmail()) + " -> " + formatString(changePerson.getPersonalEmail()) + "\n";
			if(!DateUtils.getDateFormatted(realPerson.getDob()).equals(DateUtils.getDateFormatted(changePerson.getDob())))
				ret += "        DOB: " + formatString(DateUtils.getDateFormatted(realPerson.getDob())) + " -> " + formatString(DateUtils.getDateFormatted(changePerson.getDob())) + "\n";
			if(realPerson.getSex() != changePerson.getSex())
				ret += "        Gender: " + formatString(realPerson.getSex() + "") + " -> " + formatString(changePerson.getSex() + "") + "\n";
			try{
				if(!bp.getStreet().equals(bp.loadPendingAddress(BPerson.ADDR_HOME).getStreet()))
					ret += "        Street1: " + formatString(bp.getStreet()) + " -> " + formatString(bp.loadPendingAddress(BPerson.ADDR_HOME).getStreet()) + "\n";
				if(!bp.getStreet2().equals(bp.loadPendingAddress(BPerson.ADDR_HOME).getStreet2()))
					ret += "        Street2: " + formatString(bp.getStreet2()) + " -> " + formatString(bp.loadPendingAddress(BPerson.ADDR_HOME).getStreet2()) + "\n";
				if(!bp.getCity().equals(bp.loadPendingAddress(BPerson.ADDR_HOME).getCity()))
					ret += "        City: " + formatString(bp.getCity()) + " -> " + formatString(bp.loadPendingAddress(BPerson.ADDR_HOME).getCity()) + "\n";
				if(!bp.getState().equals(bp.loadPendingAddress(BPerson.ADDR_HOME).getState()))
					ret += "        State: " + formatString(bp.getState()) + " -> " + formatString(bp.loadPendingAddress(BPerson.ADDR_HOME).getState()) + "\n";
				if(!bp.getZip().equals(bp.loadPendingAddress(BPerson.ADDR_HOME).getZip()))
					ret += "        Zip: " + formatString(bp.getZip()) + " -> " + formatString(bp.loadPendingAddress(BPerson.ADDR_HOME).getZip()) + "\n";
			} catch (Exception e) {
				//just skip it
			}
			try {
				if(!bp.getHomePhone().equals(bp.loadPendingPhone(BPerson.PHONE_HOME).getPhoneNumber()))
					ret += "        Home Phone: " + formatString(bp.getHomePhone()) + " -> " + formatString(bp.loadPendingPhone(BPerson.PHONE_HOME).getPhoneNumber()) + "\n";
				if(!bp.getWorkPhone().equals(bp.loadPendingPhone(BPerson.PHONE_WORK).getPhoneNumber()))
					ret += "        Work Phone: " + formatString(bp.getWorkPhone()) + " -> " + formatString(bp.loadPendingPhone(BPerson.PHONE_WORK).getPhoneNumber()) + "\n";
				if(!bp.getMobilePhone().equals(bp.loadPendingPhone(BPerson.PHONE_CELL).getPhoneNumber()))
					ret += "        Mobile Phone: " + formatString(bp.getMobilePhone()) + " -> " + formatString(bp.loadPendingPhone(BPerson.PHONE_CELL).getPhoneNumber()) + "\n";
				if(!bp.getWorkFaxNumber().equals(bp.loadPendingPhone(BPerson.PHONE_FAX).getPhoneNumber()))
					ret += "        Fax Phone: " + formatString(bp.getWorkFaxNumber()) + " -> " + formatString(bp.loadPendingPhone(BPerson.PHONE_FAX).getPhoneNumber()) + "\n";
			} catch (Exception e) {
				//just skip it
			}
		}


		return ret;
	}

	private String formatString(String str)
	{
		if(StringUtils.isEmpty(str))
			return "[Empty]";
		return str;
	}

	private String getBenefitChanges(List<HrBenefitJoin> joins)
	{
		String ret = "";

		if(joins == null || joins.size() == 0)
			return "";

		//enrollments
		if(joins.get(0).getHrBenefitConfig() != null)
		{
			ret += "    Benefit: " + joins.get(0).getHrBenefitConfig().getHrBenefit().getName() + "\n";
			ret += "    Coverage: " + joins.get(0).getHrBenefitConfig().getName() + "\n";
			ret += "    Policy Start: " + DateUtils.getDateFormatted(joins.get(0).getPolicyStartDate()) + "\n";
			ret += "    Policy End: " + DateUtils.getDateFormatted(joins.get(0).getPolicyEndDate()) + "\n";
			for(HrBenefitJoin j : joins)
			{
				if(j.getRecordChangeType() == 'N')
				{
					ret += "        New: " + j.getCoveredPerson().getNameFML() + "\n";
					ret += "        Start Date: " + DateUtils.getDateFormatted(j.getCoverageStartDate()) + "\n";
				}
				else if(j.getRecordChangeType() == 'M')
				{
					ret += "        Modify: " + j.getCoveredPerson().getNameFML() + "\n";
					ret += "        Start Date: " + DateUtils.getDateFormatted(j.getCoverageStartDate()) + "\n";
					if(j.getCoverageEndDate() > 0)
						ret += "        End Date: " + DateUtils.getDateFormatted(j.getCoverageEndDate()) + "\n";
				}
				else
				{
					ret += "        Delete: " + j.getCoveredPerson().getNameFML() + "\n";
					ret += "        Start Date: " + DateUtils.getDateFormatted(j.getCoverageStartDate()) + "\n";
					if(j.getCoverageEndDate() > 0)
						ret += "        End Date: " + DateUtils.getDateFormatted(j.getCoverageEndDate()) + "\n";
				}
			}
		}
		else //declines
		{
			ret += "    Decline: " + joins.get(0).getHrBenefit().getName() + "\n";
		}


		return ret;
	}

	private String getDependentChanges(BHREmplDependent bd)
	{
		String ret = "";

		HrEmplDependentChangeRequest depCR = ArahantSession.getHSU().createCriteria(HrEmplDependentChangeRequest.class).eq(HrEmplDependentChangeRequest.DEP_PENDING, bd.getPerson()).eq(HrEmplDependentChangeRequest.CHANGE_STATUS, HrEmplDependentChangeRequest.STATUS_PENDING).first();

		if(depCR != null)
		{
			HrEmplDependent realPerson = depCR.getRealRecord();
			HrEmplDependent changePerson = depCR.getChangeRecord();
			if(realPerson == null)
			{
				ret += "    Add Dependent \n";
				ret += "        First Name: " + formatString(changePerson.getPerson().getFname()) + "\n";
				ret += "        Middle Name: " + formatString(changePerson.getPerson().getMname()) + "\n";
				ret += "        Last Name: " + formatString(changePerson.getPerson().getLname()) + "\n";
				ret += "        SSN: " + formatString(changePerson.getPerson().getUnencryptedSsn()) + "\n";
				ret += "        DOB: " + formatString(DateUtils.getDateFormatted(changePerson.getPerson().getDob())) + "\n";
				ret += "        Gender: " + formatString(changePerson.getPerson().getSex() + "") + "\n";
			}
			else
			{
				//inactivate
				if(changePerson.getDateInactive() > 0 && realPerson.getDateInactive() == 0)
				{
					ret += "    Inactivate Dependent \n";
					ret += "        First Name: " + formatString(changePerson.getPerson().getFname()) + "\n";
					ret += "        Middle Name: " + formatString(changePerson.getPerson().getMname()) + "\n";
					ret += "        Last Name: " + formatString(changePerson.getPerson().getLname()) + "\n";
					ret += "        SSN: " + formatString(changePerson.getPerson().getUnencryptedSsn()) + "\n";
					ret += "        DOB: " + formatString(DateUtils.getDateFormatted(changePerson.getPerson().getDob())) + "\n";
					ret += "        Gender: " + formatString(changePerson.getPerson().getSex() + "") + "\n";
					ret += "        Inactive Date: " + formatString(DateUtils.getDateFormatted(changePerson.getDateInactive())) + "\n";
				}
				else
				{
					ret += "    Modify Dependent \n";
					if(!realPerson.getPerson().getFname().equals(changePerson.getPerson().getFname()))
						ret += "        First Name: " + formatString(realPerson.getPerson().getFname()) + " -> " + formatString(changePerson.getPerson().getFname()) + "\n";
					if(!realPerson.getPerson().getMname().equals(changePerson.getPerson().getMname()))
						ret += "        Middle Name: " + formatString(realPerson.getPerson().getMname()) + " -> " + formatString(changePerson.getPerson().getMname()) + "\n";
					if(!realPerson.getPerson().getLname().equals(changePerson.getPerson().getLname()))
						ret += "        Last Name: " + formatString(realPerson.getPerson().getLname()) + " -> " + formatString(changePerson.getPerson().getLname()) + "\n";
					if(!realPerson.getPerson().getUnencryptedSsn().equals(changePerson.getPerson().getUnencryptedSsn()))
						ret += "        SSN: " + formatString(realPerson.getPerson().getUnencryptedSsn()) + " -> " + formatString(changePerson.getPerson().getUnencryptedSsn()) + "\n";
					if(!DateUtils.getDateFormatted(realPerson.getPerson().getDob()).equals(DateUtils.getDateFormatted(changePerson.getPerson().getDob())))
						ret += "        DOB: " + formatString(DateUtils.getDateFormatted(realPerson.getPerson().getDob())) + " -> " + formatString(DateUtils.getDateFormatted(changePerson.getPerson().getDob())) + "\n";
					if(realPerson.getPerson().getSex() != changePerson.getPerson().getSex())
						ret += "        Gender: " + formatString(realPerson.getPerson().getSex() + "") + " -> " + formatString(changePerson.getPerson().getSex() + "") + "\n";

				}
			}
		}
		return ret;
	}


//            BPerson bp = new BPerson(original);
//
//            try {
//                ret += fixCmpDesc("Old Home Phone: ", bp.getHomePhone(), "New Home Phone: ", loadPendingPhone(PHONE_HOME).getPhoneNumber());
//                ret += fixCmpDesc("Old Work Phone: ", bp.getWorkPhoneNumber(), "New Work Phone: ", loadPendingPhone(PHONE_WORK).getPhoneNumber());
//                ret += fixCmpDesc("Old Mobile Phone: ", bp.getMobilePhone(), "New Mobile Phone: ", loadPendingPhone(PHONE_CELL).getPhoneNumber());
//                ret += fixCmpDesc("Old Fax: ", bp.getWorkFaxNumber(), "New Fax: ", loadPendingPhone(PHONE_FAX).getPhoneNumber());
//
//            } catch (Exception e) {
//                //skip it
//            }
//            ret += fixCmpDesc("Old DOB: ", DateUtils.getDateFormatted(original.getDob()), "New DOB: ", DateUtils.getDateFormatted(person.getDob()));
	
}

	
