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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import org.kissweb.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.type.Type;

/**
 *
 * Arahant
 */
public class DRCMessage implements IInterceptorHook {

	private HashMap benefitJoinsToSend = new HashMap();
	private static final ArahantLogger logger=new ArahantLogger(DRCMessage.class);
	@Override
	public void add(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if(entity instanceof HrBenefitJoin)
		{
			BHRBenefitJoin bbj = new BHRBenefitJoin((HrBenefitJoin)entity);

			if(bbj.getBenefitApproved() && bbj.getBenefitConfig() != null) //dont deal with declines
			{

//				List<Employee> emps = ArahantSession.getHSU().createCriteria(Employee.class)
//							.eq(Employee.HR_ADMIN,'Y')
//							.notNull(Employee.PERSONALEMAIL)
//							.ne(Employee.PERSONALEMAIL, "")
//							.eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
//							.list();
//
//				String adminsEmail = "";
//				for (Employee emp : emps)
//				{
//					adminsEmail += "," + emp.getPersonalEmail() ;
//				}
//				if(!StringUtils.isEmpty(adminsEmail))
//				{
//					adminsEmail=adminsEmail.substring(1);
//					benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(),new SendThread(adminsEmail, "New Benefit Enrollment", "New", bbj.getBean()));
//				}
//				else
//				{
//					logger.info("Email attempted to send but there are no HR Administrators in " + ArahantSession.getHSU().getCurrentCompany().getName());
//				}
				if(true)
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
							{
								DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
								if(!bei.getBenefitJoinList().contains(bbj.getBean()))
								{
									bei.getBenefitJoinList().add(bbj.getBean());
								}
							}
							else
							{
								List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
								newList.add(bbj.getBean());
								benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", newList));
							}
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
				else
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", bbj.getBean()));
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
			}
		}

	}

	@Override
	public void delete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if(entity instanceof HrBenefitJoin)
		{
			BHRBenefitJoin bbj = new BHRBenefitJoin((HrBenefitJoin)entity);

			if(bbj.getBenefitApproved() && bbj.getBenefitConfig() != null) //dont deal with declines
			{
//				List<Employee> emps = ArahantSession.getHSU().createCriteria(Employee.class)
//							.eq(Employee.HR_ADMIN,'Y')
//							.notNull(Employee.PERSONALEMAIL)
//							.ne(Employee.PERSONALEMAIL, "")
//							.eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
//							.list();
//
//				String adminsEmail = "";
//				for (Employee emp : emps)
//				{
//					adminsEmail += "," + emp.getPersonalEmail() ;
//				}
//				if(!StringUtils.isEmpty(adminsEmail))
//				{
//					adminsEmail=adminsEmail.substring(1);
//					if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() > DateUtils.now())  //if the end date is set in the future, email as a future term
//					{
//						benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(), new SendThread(adminsEmail, "Terminate Benefit Enrollment", "Future Terminate", bbj.getBean()));
//					}
//					else
//					{
//						benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(), new SendThread(adminsEmail, "Terminate Benefit Enrollment", "Terminate", bbj.getBean()));
//					}
//				}
//				else
//				{
//					logger.info("Email attempted to send but there are no HR Administrators in " + ArahantSession.getHSU().getCurrentCompany().getName());
//				}
				if(true)
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() > DateUtils.now())  //if the end date is set in the future, email as a future term
							{
								if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
								{
									DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
									if(!bei.getBenefitJoinList().contains(bbj.getBean()))
									{
										bei.getBenefitJoinList().add(bbj.getBean());
									}
								}
								else
								{
									List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
									newList.add(bbj.getBean());
									benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Future Terminate", newList));
								}
								//benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Future Terminate", bbj.getBean()));
							}
							else
							{
								if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
								{
									DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
									if(!bei.getBenefitJoinList().contains(bbj.getBean()))
									{
										bei.getBenefitJoinList().add(bbj.getBean());
									}
								}
								else
								{
									List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
									newList.add(bbj.getBean());
									benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Terminate", newList));
								}
								//benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Terminate", bbj.getBean()));
							}
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
				else
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() > DateUtils.now())  //if the end date is set in the future, email as a future term
							{
								benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Future Terminate", bbj.getBean()));
							}
							else
							{
								benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "Terminate Benefit Enrollment", "Terminate", bbj.getBean()));
							}
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
			}
		}
	}

	@Override
	public boolean edit(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if(entity instanceof HrBenefitJoin)
		{
			BHRBenefitJoin bbj = new BHRBenefitJoin((HrBenefitJoin)entity);

			if(bbj.getBenefitApproved() && bbj.getBenefitConfig() != null) //dont deal with declines
			{
//				List<Employee> emps = ArahantSession.getHSU().createCriteria(Employee.class)
//							.eq(Employee.HR_ADMIN,'Y')
//							.notNull(Employee.PERSONALEMAIL)
//							.ne(Employee.PERSONALEMAIL, "")
//							.eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
//							.list();
//
//				String adminsEmail = "";
//				for (Employee emp : emps) //prepare the admin emails
//				{
//					adminsEmail += "," + emp.getPersonalEmail() ;
//				}
//				if(!StringUtils.isEmpty(adminsEmail)) //send to admin emails if there are actually admins
//				{
//					adminsEmail=adminsEmail.substring(1);
//					if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() > DateUtils.now())  //if the end date is set in the future, email as a future term
//					{
//						benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(), new SendThread(adminsEmail, "Terminate Benefit Enrollment", "Future Terminate", bbj.getBean()));
//					}
//					else if(bbj.getCoverageEndDate() == 0) //make sure this isn't a delete that is being modified first
//					{
//						if(checkModifyIsApproval(previousState, currentState, propertyNames))
//						{
//							benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(), new SendThread(adminsEmail, "New Benefit Enrollment", "New", bbj.getBean()));
//						}
//						else
//						{
// 							benefitJoinsToSend.put("HRAdmin" + bbj.getBenefitJoinId(), new SendThread(adminsEmail, "Modify Benefit Enrollment", "Modify", bbj.getBean()));
//						}
//					}
//				}
//				else //log that there are no admins
//				{
//					logger.info("Email attempted to send but there are no HR Administrators in " + ArahantSession.getHSU().getCurrentCompany().getName());
//				}
				if(true)
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() >= DateUtils.now())  //if the end date is set in the future, email as a future term
							{
								if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
								{
									DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
									if(!bei.getBenefitJoinList().contains(bbj.getBean()))
									{
										bei.getBenefitJoinList().add(bbj.getBean());
									}
								}
								else
								{
									List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
									newList.add(bbj.getBean());
									benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Future Terminate", newList));
								}
								//benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Future Terminate", bbj.getBean()));
							}
							else if (bbj.getCoverageEndDate() == 0) //make sure this isn't a delete that is being modified first
							{
								if(checkModifyIsApproval(previousState, currentState, propertyNames))
								{
									if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
									{
										DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
										if(!bei.getBenefitJoinList().contains(bbj.getBean()))
										{
											bei.getBenefitJoinList().add(bbj.getBean());
										}
									}
									else
									{
										List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
										newList.add(bbj.getBean());
										benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", newList));
									}
									//benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", bbj.getBean()));
								}
								else
								{
									if(benefitJoinsToSend.containsKey("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId()))
									{
										DRCMessage.BenefitEmailInfo bei = (BenefitEmailInfo)benefitJoinsToSend.get("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId());
										if(!bei.getBenefitJoinList().contains(bbj.getBean()))
										{
											bei.getBenefitJoinList().add(bbj.getBean());
										}
									}
									else
									{
										List<HrBenefitJoin> newList = new ArrayList<HrBenefitJoin>();
										newList.add(bbj.getBean());
										benefitJoinsToSend.put("DRC" + bbj.getPayingPersonId() + bbj.getBenefitConfig().getBenefitConfigId(), new BenefitEmailInfo(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Modify", newList));
									}
									//benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Modify", bbj.getBean()));
								}
							}
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
				else
				{
					if(bbj.getBenefitConfig().getBenefit().getProcessType() == 'H')
					{
						if(!StringUtils.isEmpty(BProperty.get("AdminEmail")))
						{
							if(bbj.getCoverageEndDate() != 0 && bbj.getCoverageEndDate() >= DateUtils.now())  //if the end date is set in the future, email as a future term
							{
								benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Future Terminate", bbj.getBean()));
							}
							else if (bbj.getCoverageEndDate() == 0) //make sure this isn't a delete that is being modified first
							{
								if(checkModifyIsApproval(previousState, currentState, propertyNames))
								{
									benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "New Benefit Enrollment", "New", bbj.getBean()));
								}
								else
								{
									benefitJoinsToSend.put("DRC" + bbj.getBenefitJoinId(), new SendThread2(BProperty.get("AdminEmail"), "Modify Benefit Enrollment", "Modify", bbj.getBean()));
								}
							}
						}
						else
						{
							logger.info("Email attempted to send but there are is no DRC email set up in the property 'AdminEmail'");
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void execute() {
		//keep from sending entire list multiple times in case of recursive calls
		HashMap hash = benefitJoinsToSend;
		benefitJoinsToSend = new HashMap();

		Iterator i = hash.entrySet().iterator();
	    while (i.hasNext())
		{
			Map.Entry entry = (Map.Entry)i.next();
			if(true)
			{
				SendThread thread = new SendThread((BenefitEmailInfo)entry.getValue());
				thread.start();
			}
			else
			{
				SendThread2 thread = (SendThread2)entry.getValue();
				thread.start();
			}
			
	    }
	}

	private boolean checkModifyIsApproval(Object[] previousState, Object[] currentState, String[] propertyNames)
	{
		int count = 0;
		for(String s : propertyNames)
		{
			if(s.equalsIgnoreCase("benefitApproved"))
			{
				break;
			}
			else
			{
				count++;
			}
		}

		return (previousState[count].toString().equals("N") && currentState[count].toString().equals("Y"));
	}

	public class BenefitEmailInfo
	{
		List<HrBenefitJoin> benefitJoins;
		String admin, subject, type;
		public BenefitEmailInfo(String a, String s, String t, List<HrBenefitJoin> l)
		{
			admin = a;
			subject = s;
			type = t;
			benefitJoins = l;
		}

		public void setAdmin(String admin)
		{
			this.admin = admin;
		}

		public void setBenefitJoins(List<HrBenefitJoin> benefitJoins)
		{
			this.benefitJoins = benefitJoins;
		}

		public void setSubject(String subject)
		{
			this.subject = subject;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public List<HrBenefitJoin> getBenefitJoinList()
		{
			return benefitJoins;
		}

		public String getAdmin()
		{
			return admin;
		}

		public String getSubject()
		{
			return subject;
		}

		public String getType()
		{
			return type;
		}
	}

	static class SendThread extends Thread
	{
		BenefitEmailInfo info;
		String policyMessage;
		String dependentMessage;
		String benefitMessage;
		public SendThread(BenefitEmailInfo i)
		{
			super();
			setDaemon(true);
			info = i;
			policyMessage = "";
			dependentMessage = "";
			benefitMessage = "";
		}

		@Override
		public void run()
		{
			ArahantSession.openHSU();
			final String endline = "\r\n";
			try
			{
				for(HrBenefitJoin j : info.getBenefitJoinList())
				{
					BHRBenefitJoin bj = new BHRBenefitJoin(j);
					BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
					if(bj.isPolicyBenefitJoin())
					{
						policyMessage = "Employee: " + bj.getCoveredPerson().getNameLFM() + endline +
							"Employee SSN: " + bj.getCoveredPerson().getSsn() + endline +
							"Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline +
							"Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline +
							"Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline +
							"Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline +
							"Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline +
							"Qualifying Event: " + bj.getChangeReason() + endline +
							"Qualifying Event date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())) + endline +  //QE date
							"";
					}
					else
					{
						dependentMessage += "Covered Person: " + bj.getCoveredNameLFM() + endline +
							"Covered SSN: " + bj.getCoveredSsn() + endline +
							"Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline +
							"Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline +
							"Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline +
							"Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline +
							"Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline +
							"Qualifying Event: " + bj.getChangeReason() + endline +
							"Qualifying Event date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())) + endline +  //QE date
							"";
					}
					if(StringUtils.isEmpty(benefitMessage))
					{
						BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
						BEmployee be = new BEmployee(bj.getPayingPerson());


						String sb = new String();
						sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
						sb += ("Transaction Type: " + info.getType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
						if(bh.getBean().getProvider() != null)
						{
							sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline;		//Carrier
						}
						else
						{
							sb += ("Carrier: (Not Specified)") + endline;
						}
						sb += ("Policy Owner: " + bj.getPayingPerson().getNameFML()) + endline;
						sb += ("Policy Owner SSN: " + bj.getPayingPerson().getSsn()) + endline;
						sb += ("Benefit: " + bj.getBenefitConfig().getBenefitName()) + endline;
						sb += ("Level: " + bj.getBenefitConfigName()) + endline;
						sb += ("Policy: " + bh.getGroupId()) + endline;
						sb += ("Plan: " + bh.getPlan()) + endline;
						sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
						sb += ("Plan Name: " + bh.getPlanName()) + endline;
						sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
						sb += ("Email: " + be.getPersonalEmail()) + endline;
						sb += ("Company: " + be.getCompanyName()) + endline;

						benefitMessage = sb;
					}
				}


				if(BProperty.getBoolean("TestEnvironment"))
				{
					info.setSubject("[TEST] " + info.getSubject());
				}
				ArahantLogger logger = new ArahantLogger(DRCMessage.class);
				logger.info("************BEGIN DRC EMAIL LOG******************");
				logger.info("To - " + info.getAdmin());
				logger.info("Subject - " + info.getSubject());
				logger.info("Message - " + benefitMessage + "\n" + policyMessage + "\n" + dependentMessage);
				logger.info("************END DRC EMAIL LOG********************");
				//new com.arahant.MessageSender().sendMessageFrom(info.getAdmin(), info.getSubject(), benefitMessage + "\n" policyMessage + "\n" + dependentMessage);
			}
			catch (Exception e)
			{
				ArahantLogger logger = new ArahantLogger(DRCMessage.class);
				logger.info("***************************FAILED**********************************");
				logger.info("*********************BEGIN DRC EMAIL LOG***************************");
				logger.info("***************************FAILED**********************************");
				logger.info("Benefit Email Failed");
				logger.info("To - " + info.getAdmin());
				logger.info("Subject - " + info.getSubject());
				logger.info("Message - " + policyMessage + "\n" + dependentMessage);
				logger.info("***************************FAILED**********************************");
				logger.info("*********************END DRC EMAIL LOG*****************************");
				logger.info("***************************FAILED**********************************");
				e.printStackTrace();
			}
			ArahantSession.clearSession();    //  this closes the HSU session
		}
	}

static class SendThread2 extends Thread
	{
		HrBenefitJoin benefitJoin;
		String admin, subject, message, type;
		public SendThread2(String a, String s, String t, HrBenefitJoin bj)
		{
			super();
			setDaemon(true);
			admin=a;
			subject=s;
			message="";
			benefitJoin = bj;
			type = t;
		}

		@Override
		public void run()
		{
			ArahantSession.openHSU();
			try
			{
				BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoin);
				final String endline = "\r\n";
				message = "Employee: " + bj.getPayingPerson().getNameLFM() + endline +
						"Employee SSN: " + bj.getPayingPerson().getSsn() + endline +
						"Covered Person: " + bj.getCoveredNameLFM() + endline +
						"Covered SSN: " + bj.getCoveredSsn() + endline +
						"Benefit: " + bj.getBenefitConfig().getBenefitName() + endline +
						"Level: " + bj.getBenefitConfigName() + endline +
						"Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline +
						"Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline +
						"Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline +
						"Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline +
						"Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline +
						"";

				BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
				BEmployee be = new BEmployee(bj.getPayingPerson());

				BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());

				String sb = new String();
				sb += "Process Type: " + bh.getProcessType() + endline;	//Process Type
				sb += ("Transaction Type: " + type + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
				if(bh.getBean().getProvider() != null)
				{
					sb += ("Carrier: " + bh.getBean().getProvider().getName()) + endline;		//Carrier
				}
				else
				{
					sb += ("Carrier: (NotSpecified)") + endline;
				}
				sb += ("Policy: " + bh.getGroupId()) + endline;
				sb += ("Plan: " + bh.getPlan()) + endline;
				sb += ("Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
				sb += ("Plan Name: " + bh.getPlanName()) + endline;
				sb += ("DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
				sb += ("Qualifying Event: " + bj.getChangeReason()) + endline;  //Manual change
				sb += ("Qualifying Event date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate()))) + endline;  //QE date
				sb += ("Email: " + be.getPersonalEmail()) + endline;
				sb += ("Company: " + be.getCompanyName()) + endline;

				message += sb;

				if(BProperty.getBoolean("TestEnvironment"))
				{
					subject = "[TEST] " + subject;
					message = "[TEST] " + endline + message;
				}
				ArahantLogger logger = new ArahantLogger(DRCMessage.class);
				logger.info("************BEGIN DRC EMAIL LOG******************");
				logger.info("To - " + admin);
				logger.info("Subject - " + subject);
				logger.info("Message - " + message);
				logger.info("************END DRC EMAIL LOG********************");
				//new com.arahant.MessageSender().sendMessageFrom(admin, subject, message);
			}
			catch (Exception e)
			{
				ArahantLogger logger = new ArahantLogger(DRCMessage.class);
				logger.info("***************************FAILED**********************************");
				logger.info("*********************BEGIN DRC EMAIL LOG***************************");
				logger.info("***************************FAILED**********************************");
				logger.info("Benefit Email Failed");
				logger.info("To - " + admin);
				logger.info("Subject - " + subject);
				logger.info("Message - " + message);
				logger.info("***************************FAILED**********************************");
				logger.info("*********************END DRC EMAIL LOG*****************************");
				logger.info("***************************FAILED**********************************");
				e.printStackTrace();
			}
			ArahantSession.clearSession();    //  this closes the HSU session
		}
	}

}
