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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.business;

import com.arahant.beans.Address;
import com.arahant.beans.Employee;
import com.arahant.beans.Garnishment;
import com.arahant.beans.GarnishmentType;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.standard.hr.hrEEOCategory.GetEEOCategoriesReportInput;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BGarnishment extends SimpleBusinessObjectBase<Garnishment> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BGarnishment(id).delete();
	}

	static BGarnishment[] makeArray(List<Garnishment> l) {
		BGarnishment [] ret=new BGarnishment[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BGarnishment(l.get(loop));
		return ret;
	}

	public BGarnishment(String id) {
		super(id);
	}

	public BGarnishment()
	{
	}

	public BGarnishment(Garnishment o) {
		bean=o;
	}
	
	public String create() throws ArahantException {
		bean=new Garnishment();
		Address addr=new Address();
		addr.generateId();
		ArahantSession.getHSU().insert(addr);
		bean.setRemitTo(addr);
		return bean.generateId();
	}

	public double getAmount() {
		if (bean.getDeductionAmount()>.001)
			return bean.getDeductionAmount();
		else
			return bean.getDeductionPercentage();
	}

	public String getAmountType() {
		if (bean.getDeductionAmount()>.001)
			return "F";
		else
			return "P";
	}

	public String getCollectingState() {
		return bean.getCollectionState().trim();
	}

	public String getDocketNumber() {
		return bean.getDocketNumber();
	}

	public int getFinalDate() {
		return bean.getEndDate();
	}

	public String getFipsCode() {
		return bean.getFipsCode();
	}

	public String getId() {
		return bean.getGarnishmentId();
	}

	public String getIssueState() {
		return bean.getIssueState().trim();
	}

	public double getMaxAmount() {
		if (bean.getMaxDollars()>.001)
			return bean.getMaxDollars();
		else
			return bean.getMaxPercent();
	}

	public String getRemitToCity() {
		return bean.getRemitTo().getCity();
	}

	public String getRemitToCounty() {
		return bean.getRemitTo().getCounty();
	}


	public String getRemitToCountry() {
		return bean.getRemitTo().getCountry();
	}


	public String getRemitToName() {
		return bean.getRemitToName();
	}

	public String getRemitToState() {
		return bean.getRemitTo().getState();
	}

	public String getRemitToStreet() {
		return bean.getRemitTo().getStreet();
	}

	public String getRemitToStreet2() {
		return bean.getRemitTo().getStreet2();
	}

	public String getRemitToZip() {
		return bean.getRemitTo().getZip();
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public String getDeductType() {
		return bean.getNetOrGross()+"";
	}

	public String getTypeDescription() {
		return bean.getGarnishmentType().getDescription();
	}

	public String getTypeId() {
		return bean.getGarnishmentType().getGarnishmentTypeId();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(Garnishment.class,key);
	}


	public void setAmount(double amount, String amountType) {
		if (amountType.equals("P"))
		{
			bean.setDeductionAmount(0);
			bean.setDeductionPercentage((float)amount);
		}
		else
		{
			bean.setDeductionAmount(amount);
			bean.setDeductionPercentage(0);
		}
	}

	public void setCollectingState(String collectingState) {
		bean.setCollectionState(collectingState.trim());
	}

	public void setDocketNumber(String docketNumber) {
		bean.setDocketNumber(docketNumber);
	}

	public void setFinalDate(int finalDate) {
		bean.setEndDate(finalDate);
	}

	public void setFipsCode(String fipsCode) {
		bean.setFipsCode(fipsCode);
	}

	public void setGarnishmentType(String typeId) {
		bean.setGarnishmentType(ArahantSession.getHSU().get(GarnishmentType.class, typeId));
	}

	public void setIssueState(String issueState) {
		bean.setIssueState(issueState.trim());
	}

	public void setMaxAmount(double maxAmount, String amountType) {
		if (amountType.equals("P"))
		{
			bean.setMaxDollars(0);
			bean.setMaxPercent((float)maxAmount);
		}
		else
		{
			bean.setMaxDollars(maxAmount);
			bean.setMaxPercent(0);
		}
	}

	public void setMoveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void setPersonId(String personId) {
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, personId));
		if (bean.getEmployee()==null)
			throw new ArahantWarning("Could not load employee with key "+personId);
	}

	public void setRemitToCity(String remitToCity) {
		bean.getRemitTo().setCity(remitToCity);
	}

	public void setRemitToCounty(String remitToCounty) {
		bean.getRemitTo().setCounty(remitToCounty);
	}

	public void setRemitToCountry(String remitToCountry) {
		bean.getRemitTo().setCountry(remitToCountry);
	}

	public void setRemitToName(String remitToName) {
		bean.setRemitToName(remitToName);
	}

	public void setRemitToState(String remitToState) {
		bean.getRemitTo().setState(remitToState);
	}

	public void setRemitToStreet(String remitToStreet) {
		bean.getRemitTo().setStreet(remitToStreet);
	}

	public void setRemitToStreet2(String remitToStreet2) {
		bean.getRemitTo().setStreet2(remitToStreet2);
	}

	public void setRemitToZip(String remitToZip) {
		bean.getRemitTo().setZip(remitToZip);
	}

	public void setSequence(int priority) {
		bean.setPriority((short)priority);
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public void setType(String type) {
		bean.setNetOrGross(type.charAt(0));
	}
	
	public static BGarnishment [] list(String personId)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(Garnishment.class)
			.orderBy(Garnishment.PRIORITY)
			.joinTo(Garnishment.EMPLOYEE)
			.eq(Employee.PERSONID,personId)
			.list());
	}

	public void moveUp()
	{
		if (bean.getPriority()>0)
		{
			Garnishment pvj=ArahantSession.getHSU().createCriteria(Garnishment.class)
				.eq(Garnishment.EMPLOYEE,bean.getEmployee())
				.eq(Garnishment.PRIORITY, (short)(bean.getPriority()-1))
				.first();

			short temp=bean.getPriority();
			pvj.setPriority((short)99999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setPriority((short)(bean.getPriority()-1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			pvj.setPriority(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <Garnishment> l=ArahantSession.getHSU().createCriteria(Garnishment.class)
				.eq(Garnishment.EMPLOYEE,bean.getEmployee())
				.orderBy(Garnishment.PRIORITY)
				.list();
			
			l.get(0).setPriority((short)99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop=1;loop<l.size();loop++)
			{
				l.get(loop).setPriority((short)(l.get(loop).getPriority()-1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}
			
			l.get(0).setPriority((short)(l.size()-1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}
	
	public void moveDown()
	{
		if (bean.getPriority()!=ArahantSession.getHSU().createCriteria(Garnishment.class)
				.count()-1)
		{
			Garnishment pvj=ArahantSession.getHSU().createCriteria(Garnishment.class)
				.eq(Garnishment.EMPLOYEE,bean.getEmployee())
				.eq(Garnishment.PRIORITY, (short)(bean.getPriority()+1))
				.first();

			short temp=bean.getPriority();
			pvj.setPriority((short)999999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setPriority((short)(bean.getPriority()+1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			pvj.setPriority(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <Garnishment> l=ArahantSession.getHSU().createCriteria(Garnishment.class)
				.eq(Garnishment.EMPLOYEE,bean.getEmployee())
				.orderBy(Garnishment.PRIORITY)
				.list();
			
			l.get(l.size()-1).setPriority((short)99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size()-1));
			ArahantSession.getHSU().flush();
			
			for (int loop=l.size()-1;loop>-1;loop--)
			{
				l.get(loop).setPriority((short)(loop+1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}
			
			l.get(l.size()-1).setPriority((short)0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size()-1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId))
		{
			bean.setPriority((short) ArahantSession.getHSU().createCriteria(Garnishment.class)
					.eq(Garnishment.EMPLOYEE,bean.getEmployee())
					.count());
		}
		else
		{
			BGarnishment bcq=new BGarnishment(addAfterId);
			int initialSequence=bcq.bean.getPriority() + 1;
			bean.setPriority((short) ArahantSession.getHSU().createCriteria(Garnishment.class)
					.eq(Garnishment.EMPLOYEE,bean.getEmployee())
					.count());
			
			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getPriority()!=initialSequence)
				moveUp();
		}
		
	}

}
