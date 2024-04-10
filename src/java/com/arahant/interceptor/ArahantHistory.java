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



package com.arahant.interceptor;

import com.arahant.beans.ArahantHistoryBean;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.IAuditedBean;
import com.arahant.beans.Person;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.hibernate.type.Type;

/*
 *      THIS CLASS IS NOT USED.
 * /

/**
 *
 * Arahant
 */
public class ArahantHistory implements IInterceptorHook {

	private HashMap historyRecordsToCreate = new HashMap();
	private static final ArahantLogger logger=new ArahantLogger(ArahantHistory.class);

	@Override
	public void add(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof IAuditedBean)
		{
			for (int loop=0;loop<propertyNames.length;loop++)
			{
				if (propertyNames[loop].equals("recordChangeDate"))
					state[loop]=new java.util.Date();
				if (propertyNames[loop].equals("recordChangeType"))
					state[loop]='N';
				if (propertyNames[loop].equals("recordPersonId"))
				{
					Person p=ArahantSession.getHSU().getCurrentPerson();
					state[loop]=p.getPersonId();
				}
			}
		}
	}

	@Override
	public void delete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if(entity instanceof HrBenefitJoin)
		{
			IAuditedBean ab=(IAuditedBean)entity;

			try
			{
				ArahantHistoryBean hb=ab.historyObject();
				hb.copy(ab); //copy everything into your history object

				//set the history information
				hb.setRecordChangeDate(ab.getRecordChangeDate());
				hb.setRecordChangeType(ab.getRecordChangeType());
				hb.setRecordPersonId(ab.getRecordPersonId());

				//need to build history from current state (doing same as hb.copy(ab) ?? )
				for (int loop=0;loop<propertyNames.length;loop++)
					hb.setField(propertyNames[loop],state[loop]);

				//set the history information if it didnt exist (shouldn't ever hit this)
				if (hb.getRecordChangeDate()==null)
					hb.setRecordChangeDate(new java.util.Date());
				if (hb.getRecordChangeType()==0)
					hb.setRecordChangeType('M');
				if (hb.getRecordPersonId()==null)
					hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());

				hb.generateId();
				historyRecordsToCreate.put(hb.getHistory_id(), hb);	

				ArahantHistoryBean hb2=ab.historyObject();
				hb2.copy(ab); //copy everything into your history object again for deleting

				//set the delete history information
				hb2.setRecordChangeDate(new java.util.Date());
				hb2.setRecordChangeType('D');
				hb2.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
				hb2.generateId();
				historyRecordsToCreate.put(hb2.getHistory_id(), hb2);

			}
			catch (Exception e)
			{
				logger.error(e);
			}
		}
	}

	@Override
	public boolean edit(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		boolean ret = false;
		if (entity instanceof IAuditedBean)
		{
			IAuditedBean ab=(IAuditedBean)entity;

			try
			{
				ArahantHistoryBean hb=ab.historyObject();
				hb.copy(ab); //copy everything into your history object

				//set the history information
				hb.setRecordChangeDate(ab.getRecordChangeDate());
				hb.setRecordChangeType(ab.getRecordChangeType());
				hb.setRecordPersonId(ab.getRecordPersonId());

				//need to build history from previous state
				for (int loop=0;loop<propertyNames.length;loop++)
					hb.setField(propertyNames[loop],previousState[loop]);

				//set the history information if it didnt exist (shouldn't ever hit this)
				if (hb.getRecordChangeDate()==null)
					hb.setRecordChangeDate(new java.util.Date());
				if (hb.getRecordChangeType()==0)
					hb.setRecordChangeType('M');
				if (hb.getRecordPersonId()==null)
					hb.setRecordPersonId(ArahantSession.getHSU().getCurrentPerson().getPersonId());
				hb.generateId();

				historyRecordsToCreate.put(hb.getHistory_id(), hb);

			}
			catch (Exception e)
			{
				logger.error(e);
			}

			//change the current record information for the later history creation
			for (int loop=0;loop<propertyNames.length;loop++)
			{
				if (propertyNames[loop].equals("recordChangeDate"))
					currentState[loop]=new java.util.Date();
				if (propertyNames[loop].equals("recordChangeType"))
					currentState[loop]='M';
				if (propertyNames[loop].equals("recordPersonId"))
				{
					Person p=ArahantSession.getHSU().getCurrentPerson();
					if (p==null)
					{
						currentState[loop]=previousState[loop];
					}
					else
					{
						currentState[loop]=p.getPersonId();
					}
				}
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public void execute() {
		//keep from sending entire list multiple times in case of recursive calls
		HashMap hash = historyRecordsToCreate;
		historyRecordsToCreate = new HashMap();

		Iterator i = hash.entrySet().iterator();
	    while (i.hasNext())
		{
			Map.Entry entry = (Map.Entry)i.next();
			ArahantSession.getHSU().insert((ArahantHistoryBean)entry.getValue());
	    }
	}

//	static class SendThread extends Thread
//	{
//		HrBenefitJoin benefitJoin;
//		String admin, subject, message, type;
//		public SendThread(String a, String s, String t, HrBenefitJoin bj)
//		{
//			super();
//			setDaemon(true);
//			admin=a;
//			subject=s;
//			message="";
//			benefitJoin = bj;
//			type = t;
//		}
//
//		@Override
//		public void run()
//		{
//			try
//			{
//				BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoin);
//				final String endline = "\r\n";
//				message = "Employee: " + bj.getPayingPerson().getNameLFM() + endline +
//						"Employee SSN: " + bj.getPayingPerson().getSsn() + endline +
//						"Covered Person: " + bj.getCoveredNameLFM() + endline +
//						"Covered SSN: " + bj.getCoveredSsn() + endline +
//						"Benefit: " + bj.getBenefitConfig().getBenefitName() + endline +
//						"Level: " + bj.getBenefitConfigName() + endline +
//						"Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline +
//						"Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline +
//						"Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline +
//						"Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline +
//						"Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline +
//						"";
//
//				BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
//				BEmployee be = new BEmployee(bj.getPayingPerson());
//
//				BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
//
//				String sb = new String();
//				sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
//				sb += ("Transaction Type: " + type + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
//				if(bh.getBean().getProvider() != null)
//				{
//					sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline;		//Carrier
//				}
//				else
//				{
//					sb += ("Carrier: (NotSpecified)") + endline;
//				}
//				sb += ("Policy: " + bh.getGroupId()) + endline;
//				sb += ("Plan: " + bh.getPlan()) + endline;
//				sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
//				sb += ("Plan Name: " + bh.getPlanName()) + endline;
//				sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
//				sb += ("Qualifying Event: " + bj.getChangeReason()) + endline;  //Manual change
//				sb += ("Qualifying Event date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate()))) + endline;  //QE date
//				sb += ("Email: " + be.getPersonalEmail()) + endline;
//				sb += ("Company: " + be.getCompanyName()) + endline;
//
//				message += sb;
//
//				if(BProperty.getBoolean("TestEnvironment"))
//				{
//					subject = "[TEST] " + subject;
//					message = "[TEST] " + endline + message;
//				}
//				ArahantLogger logger = new ArahantLogger(ArahantHistory.class);
//				logger.info("************BEGIN DRC EMAIL LOG******************");
//				logger.info("To - " + admin);
//				logger.info("Subject - " + subject);
//				logger.info("Message - " + message);
//				logger.info("************END DRC EMAIL LOG********************");
//				new com.arahant.MessageSender().sendMessage(admin, subject, message);
//			}
//			catch (Exception e)
//			{
//				ArahantLogger logger = new ArahantLogger(ArahantHistory.class);
//				logger.info("***************************FAILED**********************************");
//				logger.info("*********************BEGIN DRC EMAIL LOG***************************");
//				logger.info("***************************FAILED**********************************");
//				logger.info("Benefit Email Failed");
//				logger.info("To - " + admin);
//				logger.info("Subject - " + subject);
//				logger.info("Message - " + message);
//				logger.info("***************************FAILED**********************************");
//				logger.info("*********************END DRC EMAIL LOG*****************************");
//				logger.info("***************************FAILED**********************************");
//				e.printStackTrace();
//			}
//		}
//	}

}
