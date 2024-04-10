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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.business;
import java.io.File;
import java.util.Date;
import java.util.List;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.HRWageHistoryReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class BBenefitRestriction extends SimpleBusinessObjectBase<BenefitRestriction> implements IDBFunctions {


	public BBenefitRestriction() {
		super();
	}

	/**
	 * @param string
	 * @throws ArahantException 
	 */
	public BBenefitRestriction(final String key) throws ArahantException {
		super();
		load(key);
	}

	/**
	 * @param account
	 */
	public BBenefitRestriction(final BenefitRestriction br) {
		super();
		bean = br;
	}
	
	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean=new BenefitRestriction();
		bean.generateId();
		
		return getBenefitRestrictionId();
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(bean);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		insertChecks();
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void insertChecks() {
		if(ArahantSession.getHSU().createCriteria(BenefitRestriction.class).eq(BenefitRestriction.BENEFIT_CATEGORY, getBenefitCategory()).eq(BenefitRestriction.BENEFIT_CHANGE_REASON, getBenefitChangeReason()).exists())
			throw new ArahantWarning("This restriction already exists.");
	}


	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitRestriction.class, key);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		updateChecks();
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void updateChecks() {
		if(ArahantSession.getHSU().createCriteria(BenefitRestriction.class).ne(BenefitRestriction.BENEFIT_RESTRICTION_ID, getBenefitRestrictionId()).eq(BenefitRestriction.BENEFIT_CATEGORY, getBenefitCategory()).eq(BenefitRestriction.BENEFIT_CHANGE_REASON, getBenefitChangeReason()).exists())
			throw new ArahantWarning("This restriction already exists.");
	}
	/**
	 * @return
	 * @see com.arahant.beans.HrWage#getWageId()
	 */
	
	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException 
	 */
	public static void delete(final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BBenefitRestriction(element).delete();
	}

	/**
	 * @param hsu
	 * @param employeeId 
	 * @return
	 */
	public static BBenefitRestriction[] list(final String bcrId, final String categoryId) {
		
		HibernateCriteriaUtil<BenefitRestriction> hcu =ArahantSession.getHSU().createCriteria(BenefitRestriction.class);

		if(!isEmpty(bcrId))
			hcu.eq(BenefitRestriction.BENEFIT_CHANGE_REASON_ID, bcrId);
		if(!isEmpty(categoryId))
			hcu.eq(BenefitRestriction.BENEFIT_CATEGORY_ID, categoryId);

		List<BenefitRestriction> l = hcu.list();

		final BBenefitRestriction []ret=new BBenefitRestriction[l.size()];
		for (int loop=0;loop<l.size();loop++)
			ret[loop]=new BBenefitRestriction(l.get(loop));
		return ret;
	}

	public String getBenefitRestrictionId() {
		return bean.getBenefitRestrictionId();
	}

	public HrBenefitCategory getBenefitCategory() {
		return bean.getBenefitCategory();
	}

	public void setBenefitCategory(HrBenefitCategory benefitCategory) {
		bean.setBenefitCategory(benefitCategory);
	}

	public String getBenefitCategoryId() {
		return bean.getBenefitCategoryId();
	}

	public HrBenefitChangeReason getBenefitChangeReason() {
		return bean.getBenefitChangeReason();
	}

	public void setBenefitChangeReason(HrBenefitChangeReason benefitChangeReason) {
		bean.setBenefitChangeReason(benefitChangeReason);
	}

	public String getBenefitChangeReasonId() {
		return bean.getBenefitChangeReasonId();
	}
	
}

	
