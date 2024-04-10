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
package com.arahant.business;
import java.util.List;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;

public class BBenefitDependency extends SimpleBusinessObjectBase<BenefitDependency> implements IDBFunctions {


	

	public BBenefitDependency() {
		super();
	}

	/**
	 * @param string
	 * @throws ArahantException 
	 */
	public BBenefitDependency(final String key) throws ArahantException {
		super();
		load(key);
	}

	/**
	 * @param account
	 */
	public BBenefitDependency(final BenefitDependency br) {
		super();
		bean = br;
	}
	
	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean=new BenefitDependency();
		bean.generateId();
		
		return getBenefitDependencyId();
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
		if(ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, getRequiredBenefit()).eq(BenefitDependency.DEPENDENT_BENEFIT, getDependentBenefit()).exists())
			throw new ArahantWarning("This restriction already exists.");
		if(this.getRequiredBenefit() == getDependentBenefit())
			throw new ArahantWarning("Benefit cannot depend on itself.");
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitDependency.class, key);
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
		if(ArahantSession.getHSU().createCriteria(BenefitDependency.class).ne(BenefitDependency.BENEFIT_DEPENDENCY_ID, getBenefitDependencyId()).eq(BenefitDependency.REQUIRED_BENEFIT, getRequiredBenefit()).eq(BenefitDependency.DEPENDENT_BENEFIT, getDependentBenefit()).exists())
			throw new ArahantWarning("This dependency already exists.");
		if(this.getRequiredBenefitId().equals(this.getDependentBenefitId()))
			throw new ArahantWarning("Benefit cannot depend on itself.");
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
			new BBenefitDependency(element).delete();
	}

	/**
	 * @param hsu
	 * @param employeeId 
	 * @return
	 */
	public static BBenefitDependency[] list(final String reqBenId, final String depBenId) {
		
		HibernateCriteriaUtil<BenefitDependency> hcu =ArahantSession.getHSU().createCriteria(BenefitDependency.class);

		if(!isEmpty(reqBenId))
			hcu.eq(BenefitDependency.REQUIRED_BENEFIT_ID, reqBenId);
		if(!isEmpty(depBenId))
			hcu.eq(BenefitDependency.DEPENDENT_BENEFIT_ID, depBenId);

		List<BenefitDependency> l = hcu.list();

		final BBenefitDependency []ret=new BBenefitDependency[l.size()];
		for (int loop=0;loop<l.size();loop++)
			ret[loop]=new BBenefitDependency(l.get(loop));
		return ret;
	}

	public String getBenefitDependencyId() {
		return bean.getBenefitDependencyId();
	}
	public HrBenefit getDependentBenefit() {
		return bean.getDependentBenefit();
	}

	public void setDependentBenefit(HrBenefit dependentBenefit) {
		bean.setDependentBenefit(dependentBenefit);
	}

	public String getDependentBenefitId() {
		return bean.getDependentBenefitId();
	}

	public HrBenefit getRequiredBenefit() {
		return bean.getRequiredBenefit();
	}

	public void setRequiredBenefit(HrBenefit requiredBenefit) {
		bean.setRequiredBenefit(requiredBenefit);
	}

	public String getRequiredBenefitId() {
		return bean.getRequiredBenefitId();
	}

	public char getRequired() {
		return bean.getRequired();
	}

	public boolean getRequiredBoolean() {
		return bean.getRequired() == 'Y';
	}

	public void setRequired(char req) {
		bean.setRequired(req);
	}

	public void setRequired(boolean req) {
		bean.setRequired(req ? 'Y' : 'N');
	}

	public void setRequired(String req) {
		bean.setRequired(req.charAt(0));
	}

	public char getHidden() {
		return bean.getHidden();
	}

	public boolean getHiddenBoolean() {
		return bean.getHidden() == 'Y';
	}

	public void setHidden(char h) {
		bean.setHidden(h);
	}

	public void setHidden(boolean h) {
		bean.setHidden(h ? 'Y' : 'N');
	}

	public void setHidden(String h) {
		bean.setHidden(h.charAt(0));
	}


	public static boolean hasRequirement(BHRBenefit b) {
		return ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, b.getBean()).exists();
	}

	public static List<HrBenefit> getRequirements(BHRBenefit b) {
		List<HrBenefit> ret = new ArrayList<HrBenefit>();
		List<BenefitDependency> bdl = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, b.getBean()).list();

		for(BenefitDependency bd : bdl)
		{
			ret.add(bd.getRequiredBenefit());
		}
		return ret;
	}

	public static List<BenefitDependency> getBenefitDependenciesWhereDependent(BHRBenefit b) {
		List<BenefitDependency> bdl = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.DEPENDENT_BENEFIT, b.getBean()).list();
		return bdl;
	}

	public static List<BenefitDependency> getBenefitDependenciesWhereRequired(BHRBenefit b) {
		List<BenefitDependency> bdl = ArahantSession.getHSU().createCriteria(BenefitDependency.class).eq(BenefitDependency.REQUIRED_BENEFIT, b.getBean()).list();
		return bdl;
	}

}

	
