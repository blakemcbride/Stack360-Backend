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

import com.arahant.beans.BankDraftBatch;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.Receipt;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import java.util.List;
import jess.JessException;

/**
 *
 */
public class BBillingBatch extends SimpleBusinessObjectBase<BankDraftBatch> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBillingBatch(id).delete();
	}

	private static BBillingBatch[] makeArray(List<BankDraftBatch> l) {
		BBillingBatch []ret=new BBillingBatch[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BBillingBatch(l.get(loop));
		return ret;
	}

	public BBillingBatch() {
		super();
	}

	public BBillingBatch(String id) {
		super(id);
	}

	public BBillingBatch(BankDraftBatch o) {
		super();
		bean=o;
	}

	public void addToBatch(String[] ids) {
		bean.getPersons().addAll(ArahantSession.getHSU().createCriteria(Person.class)
				.in(Person.PERSONID, ids)
				.list());
		
		update();
	}

	public double getAmountIfPostedNow() {
		double total=0;
		
		HibernateScrollUtil<Person> scr=ArahantSession.getHSU().createCriteria(Person.class)
			.joinTo(Person.BILLING_BATCHES)
			.eq(BankDraftBatch.BATCH_ID,bean.getBankDraftId())
			.scroll();


		ArahantSession.setCalcDate(DateUtils.now());
		
		while (scr.next())
		{
			Person p=scr.get();
			
			//calculate how much this person owes.
			List<HrBenefitJoin> bjl=ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, p)
				.eq(HrBenefitJoin.COVERED_PERSON, p)
				.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.notNull(HrBenefit.PRODUCTSERVICE)
				.list();
			
			double amt=0;
			
			for (HrBenefitJoin bj : bjl)
			{
				amt+=MoneyUtils.parseMoney(bj.getCalculatedCost())*bj.getPpy()/12;	
			}
			try
			{
				ArahantSession.getAI().removeAll(bjl); //for garbage collection
			}
			catch (JessException j)
			{
				//oh well
			}

			logger.info(p.getNameLFM()+" "+amt);
			total+=amt;

		}
		
		scr.close();
		
		return total;
	}

	public String getDescription() {
		return bean.getName();
	}

	public String getId() {
		return bean.getBankDraftId();
	}

	public int getPersonCount() {
		return bean.getPersons().size();
	}

	public void postBatch(double amountCheck, String confirmationNumber, int date) {
		//for each person in the batch, make a reciept record
		//also each receipt gets a bank draft history record
		
		double total=0;
		
		ArahantSession.setCalcDate(date);
		
		HibernateScrollUtil<Person> scr=ArahantSession.getHSU().createCriteria(Person.class)
			.joinTo(Person.BILLING_BATCHES)
			.eq(BankDraftBatch.BATCH_ID, bean.getBankDraftId())
			.scroll();
		
		while (scr.next())
		{
			Person p=scr.get();
			
			//calculate how much this person owes.
			List<HrBenefitJoin> bjl=ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, p)
				.eq(HrBenefitJoin.COVERED_PERSON, p)
				.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date)
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.notNull(HrBenefit.PRODUCTSERVICE)
				.list();
			
			double amt=0;
			
			for (HrBenefitJoin bj : bjl)
			{
				amt+=bj.getCalculatedCostMonthly();
			}
			try
			{
				ArahantSession.getAI().removeAll(bjl); //for garbage collection
			}
			catch (JessException j)
			{
				//oh well
			}
			total+=amt;
			
			BBankDraftHistory hist=new BBankDraftHistory();
			hist.create();
			hist.setDate(date);
			hist.setBankDraft(bean);
			hist.setReceipt(confirmationNumber);
			hist.insert();
			
			BReceipt r=new BReceipt();
			r.create();
			r.setPerson(p);
			r.setConfirmationNumber(confirmationNumber);
			r.setDate(date);
			r.setSource('P');
			r.setReceiptType(Receipt.TYPE_BANK_DEPOSIT);
			r.setAmount(amt);
			r.setBankDraftHistory(hist.bean);
			r.insert();
			
			//lets try to apply this
			
			//get all Receipts for the person that have not applied balances
			BReceipt []outstandingReceipts=BReceipt.getOutstandingReceipts(p);
			
			//get all invoices in date order that are not paid off
			BInvoice []outstandingInvoices=BInvoice.getOutstandingInvoices(p);
			
			int currentReceipt=0;
			//apply balances
			for (BInvoice inv : outstandingInvoices)
			{
				if (currentReceipt==outstandingReceipts.length)
						break; 
				while (inv.getBalance()>0)
				{
					if (currentReceipt==outstandingReceipts.length)
						break; //out of receipts
		
					if (outstandingReceipts[currentReceipt].getAvailableAmount()<=inv.getBalance())
					{
						//apply the whole balance
						inv.applyPayment(outstandingReceipts[currentReceipt], outstandingReceipts[currentReceipt].getAvailableAmount());
						currentReceipt++; //that receipt is done, move to next
					}
					else
					{
						//apply invoice balance
						inv.applyPayment(outstandingReceipts[currentReceipt], inv.getBalance());
					}
					
					inv.update();
				}
				
				
			}
			
		}
		
		scr.close();
		
		if (Math.abs(total-amountCheck)>.01)
			throw new ArahantWarning("Batch amount ("+MoneyUtils.formatMoney(amountCheck)+
					") and calculated receipts ("+MoneyUtils.formatMoney(total)+") do not match.");
	}

	public void removeFromBatch(String[] ids) {
		bean.getPersons().removeAll(ArahantSession.getHSU().createCriteria(Person.class)
				.in(Person.PERSONID, ids)
				.list());
	}

	public BPerson[] searchNotInBatch(String firstName, String lastName, int searchType, int cap) {
		
		//0 is all
		//1 is emps
		//2 is deps
		HibernateCriteriaUtil<Person> hcu=ArahantSession.getHSU().createCriteria(Person.class)
			.like(Person.FNAME, firstName)
			.like(Person.LNAME, lastName)
			.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
			.employeeOrDependent()
			.setMaxResults(cap);
		
		
		hcu.sql("{alias}.person_id not in (select person_id from bank_draft_detail where bank_draft_id='"+bean.getBankDraftId()+"')");
	/*	HibernateCriteriaUtil bbHcu=hcu.leftJoinTo(Person.BILLING_BATCHES);
		
		HibernateCriterionUtil h1=bbHcu.makeCriteria();
		HibernateCriterionUtil h2=bbHcu.makeCriteria();
		
		h1.eq(BankDraftBatch.BATCH_ID, null);
		h1.ne(BankDraftBatch.BATCH_ID, bean.getBankDraftId());
		
		HibernateCriterionUtil or=bbHcu.makeCriteria();
		
		or.or(h1, h2);
		
		or.add();
	 * */
		
		if (searchType==1)
			hcu.isEmployee();
		if (searchType==2)
			hcu.activeDependent();
		return BPerson.makeArray(hcu.list());
	}
	public BPerson[] searchInBatch(String firstName, String lastName, int searchType, int cap) {
	
		//0 is all
		//1 is emps
		//2 is deps
		HibernateCriteriaUtil<Person> hcu=ArahantSession.getHSU().createCriteria(Person.class)
			.like(Person.FNAME, firstName)
			.like(Person.LNAME, lastName)
			.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
			.setMaxResults(cap)
			.sql("{alias}.person_id in (select person_id from bank_draft_detail where bank_draft_id='"+bean.getBankDraftId()+"')");
			// TODO .eq(Person.BILLING_BATCHES, bean); This was giving a strange error, will figure out later
		
		if (searchType==1)
			hcu.isEmployee();
		if (searchType==2)
			hcu.activeDependent();
		
		return BPerson.makeArray(hcu.list());
	}
	
	public static BBillingBatch[] list()
	{
		return makeArray(ArahantSession.getHSU().createCriteria(BankDraftBatch.class)
				.orderBy(BankDraftBatch.NAME)
				.list());
	}

	public void setDescription(String description) {
		bean.setName(description);
	}

	public String create() throws ArahantException {
		bean=new BankDraftBatch();
		return bean.generateId();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BankDraftBatch.class, key);
	}


	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyDetail.class, companyId));
	}
}
