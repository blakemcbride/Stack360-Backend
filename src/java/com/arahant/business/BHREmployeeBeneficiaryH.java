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
 * Created on Nov 12, 2007
 * 
 */
package com.arahant.business;
import java.util.Collections;
import java.util.List;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;


/**
 * 
 *
 * Created on Nov 12, 2007
 *
 */
public class BHREmployeeBeneficiaryH extends SimpleBusinessObjectBase<HrEmployeeBeneficiaryH> {


	public BHREmployeeBeneficiaryH() {
		super();
	}

	/**
	 * @param historyId
	 * @throws ArahantException 
	 */
	public BHREmployeeBeneficiaryH(String historyId) throws ArahantException {
		super();
		load(historyId);
	}

	/**
	 * @param beneficiaryH
	 */
	public BHREmployeeBeneficiaryH(HrEmployeeBeneficiaryH beneficiaryH) {
		super();
		bean=beneficiaryH;
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	public String create() throws ArahantException {
		
		throw new ArahantException("Can't create history directly.");
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(HrEmployeeBeneficiaryH.class, key);
	}

	/**
	 * @return
	 */
	public String getChangeReason() {
		
		return "N/A";
	}

	/**
	 * @return
	 */
	public String getHistoryId() {
		
		return bean.getHistory_id();
	}

	/**
	 * @return
	 */
	public String getBeneficiary() {
		
		return bean.getBeneficiary();
	}

	/**
	 * @return
	 */
	public String getBenefitName() {
		
		HrBenefitJoin ebj=ArahantSession.getHSU().get(HrBenefitJoin.class, bean.getBenefitJoinId());
		
		if (ebj!=null)
		{
			if (ebj.getHrBenefitConfig()!=null)
				return ebj.getHrBenefitConfig().getHrBenefit().getName();
			if (ebj.getHrBenefit()!=null)
				return "DECLINE - "+ebj.getHrBenefit().getName();
			if (ebj.getHrBenefitCategory()!=null)
				return "DECLINE - "+ebj.getHrBenefitCategory().getDescription();
		}
		
		HrBenefitJoinH ebjh=ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class)
			.eq(HrBenefitJoinH.BENEFIT_JOIN_ID,bean.getBenefitJoinId())
			.first();
	
		if (ebjh!=null)
		{
			if (!isEmpty(ebjh.getHrBenefitConfigId()))
			{
				HrBenefitConfig c=ArahantSession.getHSU().get(HrBenefitConfig.class, ebjh.getHrBenefitConfigId());
				if (c==null)
					return "Missing";
				return c.getHrBenefit().getName();
			}
			if (!isEmpty(ebjh.getHrBenefitId()))
			{
				HrBenefit c=ArahantSession.getHSU().get(HrBenefit.class, ebjh.getHrBenefitId());
				return  "DECLINE - "+c.getName();
			}
			if (!isEmpty(ebjh.getHrBenefitCategoryId()))
			{
				HrBenefitCategory c=ArahantSession.getHSU().get(HrBenefitCategory.class, ebjh.getHrBenefitCategoryId());
				return  "DECLINE - "+c.getDescription();
			}
		}
	
		return "MISSING";
	}

	/**
	 * @return
	 */
	public int getDob() {
		
		return bean.getDob();
	}

	/**
	 * @return
	 */
	public String getConfigName() {

		HrBenefitJoin ebj=ArahantSession.getHSU().get(HrBenefitJoin.class, bean.getBenefitJoinId());
		
		if (ebj!=null)
		{
			if (ebj.getHrBenefitConfig()!=null)
				return ebj.getHrBenefitConfig().getName();
			if (ebj.getHrBenefit()!=null)
				return "DECLINE - "+ebj.getHrBenefit().getName();
			if (ebj.getHrBenefitCategory()!=null)
				return "DECLINE - "+ebj.getHrBenefitCategory().getDescription();
		}	
			
		HrBenefitJoinH ebjh=ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class)
			.eq(HrBenefitJoinH.BENEFIT_JOIN_ID,bean.getBenefitJoinId())
			.first();
	
		if (ebjh!=null)
		{
			if (!isEmpty(ebjh.getHrBenefitConfigId()))
			{
				HrBenefitConfig c=ArahantSession.getHSU().get(HrBenefitConfig.class, ebjh.getHrBenefitConfigId());
				if (c==null)
					return "Missing";
				return c.getName();
			}
			if (!isEmpty(ebjh.getHrBenefitId()))
			{
				HrBenefit c=ArahantSession.getHSU().get(HrBenefit.class, ebjh.getHrBenefitId());
				return  "DECLINE - "+c.getName();
			}
			if (!isEmpty(ebjh.getHrBenefitCategoryId()))
			{
				HrBenefitCategory c=ArahantSession.getHSU().get(HrBenefitCategory.class, ebjh.getHrBenefitCategoryId());
				return  "DECLINE - "+c.getDescription();
			}
		}
		return "MISSING";
		
	}

	/**
	 * @return
	 */
	public int getPercent() {
		
		return bean.getBenefitPercent();
	}

	/**
	 * @return
	 */
	public String getSsn() {
		
		return bean.getSsn();
	}

	/**
	 * @return
	 */
	public String getDateTimeFormatted() {
		
		return DateUtils.getDateTimeFormatted(bean.getRecordChangeDate());
	}

	/**
	 * @return
	 */
	public String getChangeType() {
		
		return bean.getRecordChangeType()+"";
	}

	/**
	 * @return
	 */
	public String getChangeTypeFormatted() {
		char changeType = bean.getRecordChangeType();
		String changeTypeFormatted = "";
		
		if (changeType == 'N')
		{
			changeTypeFormatted = "New";	
		}
		else if (changeType == 'M')
		{
			changeTypeFormatted = "Modify";	
		}
		else if (changeType == 'D')
		{
			changeTypeFormatted = "Delete";	
		}	
		
		return changeTypeFormatted;
	}

	/**
	 * @return
	 */
	public String getBeneficiaryType() {
		
		return bean.getBeneficiaryType()+"";
	}

	/**
	 * @return
	 */
	public String getAddress() {
		
		return bean.getAddress();
	}

	/**
	 * @return
	 */
	public String getRelationship() {
		
		return bean.getRelationship();
	}

	/**
	 * @return
	 */
	public String getPersonName() {
		
		Person p=ArahantSession.getHSU().get(Person.class, bean.getRecordPersonId());
		
		return p.getNameWithLogin();
	}

	/**
	 * @param employeeId
	 * @param benefitId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static BHREmployeeBeneficiaryH[] list(String employeeId, String benefitId) {
		
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		
		Person emp=hsu.get(Employee.class, employeeId);
		HrBenefit bene=hsu.get(HrBenefit.class, benefitId);
		
		List ebjList=hsu.createCriteria(HrBenefitJoinH.class)
			.selectFields(HrBenefitJoinH.BENEFIT_JOIN_ID)
			.eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId)
			.eq(HrBenefitJoinH.BENEFIT_ID, benefitId)
			.list();
		
		
		List benefitConfigIds=ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
			.selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.eq(HrBenefit.BENEFITID, benefitId)
			.list();
			
		
		ebjList.addAll(hsu.createCriteria(HrBenefitJoinH.class)
			.selectFields(HrBenefitJoinH.BENEFIT_JOIN_ID)
			.eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId)
			.in(HrBenefitJoinH.BENEFIT_CONFIG_ID, benefitConfigIds)
			.list());
		
		
		// check actives too
		ebjList.addAll(hsu.createCriteria(HrBenefitJoin.class)
			.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
			.eq(HrBenefitJoin.COVERED_PERSON, emp)
			.eq(HrBenefitJoin.HRBENEFIT, bene)
			.list());
		
		ebjList.addAll(hsu.createCriteria(HrBenefitJoin.class)
				.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
				.eq(HrBenefitJoin.COVERED_PERSON, emp)
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.eq(HrBenefitConfig.HR_BENEFIT, bene)
				.list());
		
		
		
		
		List <HrEmployeeBeneficiaryH>l=hsu.createCriteria(HrEmployeeBeneficiaryH.class)
			.in(HrEmployeeBeneficiaryH.BENEFIT_JOIN_ID, ebjList)
			.list();
		
		
		
//		don't forget to add actives

		List <HrBeneficiary> bList=hsu.createCriteria(HrBeneficiary.class)
			.joinTo(HrBeneficiary.EMPL_BENEFIT_JOIN)
			.eq(HrBenefitJoin.COVERED_PERSON, emp)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.eq(HrBenefitConfig.HR_BENEFIT, bene)
			.list();
		
		
		for (HrBeneficiary hb : bList)
		{
			HrEmployeeBeneficiaryH h=new HrEmployeeBeneficiaryH();
			HibernateSessionUtil.copyCorresponding(h, hb);
			h.setHistory_id(hb.getBeneficiaryId());
			l.add(h);
		}
		
		Collections.sort(l);
		
		return makeArray(l);

	}

	/**
	 * @param l
	 * @return
	 */
	private static BHREmployeeBeneficiaryH[] makeArray(List<HrEmployeeBeneficiaryH> l) {
		
		BHREmployeeBeneficiaryH [] ret=new BHREmployeeBeneficiaryH[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BHREmployeeBeneficiaryH(l.get(loop));
		
		return ret;
	}

}

	
