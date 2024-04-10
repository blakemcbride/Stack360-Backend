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


package com.arahant.timertasks;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.InterfaceLog;
import com.arahant.beans.ServiceSubscribed;
import com.arahant.beans.ServiceSubscribedJoin;
import com.arahant.business.BCompany;
import com.arahant.business.BInterfaceLog;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CobraGuard extends TimerTaskBase {

	private String logFilePath = "COBRA_LOG.txt";
	private FileWriter fstream;
	private BufferedWriter out;

	@Override
	public void execute() throws Exception {
		Date currentRun = new Date();
		//System.out.println("Cobra Running - " + DateUtils.getDateAndTimeFormatted(currentRun));
		try {
			if (BProperty.getBoolean("LogCobraFeed")) {
				try {
					fstream = new FileWriter(logFilePath, true);
					out = new BufferedWriter(fstream);
					out.write("******************************************************* \n");
					out.write("COBRA FEED: Started at " + DateUtils.getDateTimeFormatted(DateUtils.getDate(DateUtils.now())) + "\n");
					out.flush();
				} catch (Exception e) {
					//just continue
				}
			}
			//get all companies subscribing hsu.createCriteriaNoCompanyFilter(null)
			//List<CompanyDetail> subscribers = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).in
			ServiceSubscribed service = ArahantSession.getHSU().createCriteriaNoCompanyFilter(ServiceSubscribed.class).eq(ServiceSubscribed.INTERFACE_CODE, ServiceSubscribed.COBRA_GUARD_INTERFACE).first();
			List<ServiceSubscribedJoin> subscribers = new ArrayList<ServiceSubscribedJoin>();
			if (service != null) {
				//check if company joined this service
				subscribers = ArahantSession.getHSU().createCriteriaNoCompanyFilter(ServiceSubscribedJoin.class)
						.eq(ServiceSubscribedJoin.SERVICE, service)
						.gtOrEq(ServiceSubscribedJoin.LASTDATE, DateUtils.now(), 0)
						.notNull(ServiceSubscribedJoin.EXTERNAL_ID)
						.list();
			}
			for (ServiceSubscribedJoin sub : subscribers) {
				if (BProperty.getBoolean("LogCobraFeed")) {
					try {
//						FileWriter fstream = new FileWriter(logFilePath,true);
//						BufferedWriter out = new BufferedWriter(fstream);
						out.write("COBRA FEED: Subscriber found " + sub.getCompany().getName() + " - " + sub.getExternalId() + "\n");
						out.flush();
					} catch (Exception e) {
						//just continue
					}
				}
				CompanyDetail cd = sub.getCompany();
				ArahantSession.getHSU().setCurrentCompany(cd);
				String companyId = sub.getExternalId();
				InterfaceLog il = ArahantSession.getHSU().createCriteria(InterfaceLog.class).eq(InterfaceLog.INTERFACE, InterfaceLog.INTERFACE_COBRA_GUARD).eq(InterfaceLog.STATUS, InterfaceLog.STATUS_OK).eq(InterfaceLog.COMPANY_ID, cd.getCompanyId()).orderByDesc(InterfaceLog.LAST_RUN).first();

				Date lastRun = new Date(0);

				if (il != null) {
					lastRun = il.getLastRun();
				}
				//find ID's of everybody's joins that is still active
				HibernateCriteriaUtil hcuActives = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);

				//hcu.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
				hcuActives.eq(HrBenefitJoin.COVERAGE_END_DATE, 0)
						.eq(HrBenefitJoin.USING_COBRA, 'N') //Only want non-COBRA terms
						.eq(HrBenefitJoin.APPROVED, 'Y')
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.eq(HrBenefit.COVERED_UNDER_COBRA, 'Y') //Only interested in benefits where they could have COBRA
						.joinTo(HrBenefit.BENEFIT_CATEGORY)
						.eq(HrBenefitCategory.COMPANY, cd)
				;

				//List<HrBenefitJoin> idsListActives = hcuActives.list();


				//remove any duplicates
				//HashSet<HrBenefitJoin> sActives = new HashSet<HrBenefitJoin>();
				//sActives.addAll(idsListActives);


				//find ID's of everybody's joins that had their benefit termed
				//since the last run
				HibernateCriteriaUtil hcuTermActives = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);

				//hcu.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
				hcuTermActives.ne(HrBenefitJoin.COVERAGE_END_DATE, 0)
						.eq(HrBenefitJoin.USING_COBRA, 'N') //Only want non-COBRA terms
						.eq(HrBenefitJoin.APPROVED, 'Y')
						.gtJoinedField(HrBenefitJoin.COVERAGE_END_DATE, HrBenefitJoin.COVERAGE_START_DATE)//if covered date ends before start date, they didn't really have the benefit
						.ge(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(DateUtils.addDays(DateUtils.getDate(lastRun), -1)))  //change should be after last run - 1 day
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.eq(HrBenefit.COVERED_UNDER_COBRA, 'Y') //Only interested in benefits where they could have COBRA
						.joinTo(HrBenefit.BENEFIT_CATEGORY)
						.eq(HrBenefitCategory.COMPANY, cd)
				;

				HibernateCriteriaUtil hcuTermInactives = ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class);

				//hcu.selectFields(HrBenefitJoin.BENEFIT_JOIN_ID)
				hcuTermInactives.ne(HrBenefitJoinH.COVERAGE_END_DATE, 0)
						.eq(HrBenefitJoinH.USING_COBRA, 'N') //Only want non-COBRA terms
						.eq(HrBenefitJoinH.APPROVED, 'Y')
						.gtJoinedField(HrBenefitJoinH.COVERAGE_END_DATE, HrBenefitJoinH.COVERAGE_START_DATE)//if covered date ends before start date, they didn't really have the benefit
						.ge(HrBenefitJoinH.HISTORY_DATE, lastRun)  //change should be after last run
						.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
						.joinTo(HrBenefitConfig.HR_BENEFIT)
						.eq(HrBenefit.COVERED_UNDER_COBRA, 'Y') //Only interested in benefits where they could have COBRA
						.joinTo(HrBenefit.BENEFIT_CATEGORY)
						.eq(HrBenefitCategory.COMPANY, cd)
				;


				//List<HrBenefitJoin> idsList = hcu.list();


				//remove any duplicates
				//HashSet<HrBenefitJoin> s = new HashSet<HrBenefitJoin>();
				//s.addAll(idsList);

				//s contains the ids to process
				if (BProperty.getBoolean("LogCobraFeed")) {
					try {
//						FileWriter fstream = new FileWriter(logFilePath,true);
//						BufferedWriter out = new BufferedWriter(fstream);
						out.write("COBRA FEED: Records prepared for " + sub.getCompany().getName() + " - " + companyId + "\n");
						out.flush();
					} catch (Exception e) {
						//just continue
					}
				}
				//TODO: call the processing here
				try {
					com.arahant.exports.CobraGuard cg = new com.arahant.exports.CobraGuard(hcuTermActives, hcuTermInactives, hcuActives, companyId);
				} catch (Exception e) {
					BInterfaceLog bil = new BInterfaceLog();
					bil.create();
					bil.setStatus(InterfaceLog.STATUS_ERROR);
					bil.setLastRun(currentRun);
					bil.setInterface(InterfaceLog.INTERFACE_COBRA_GUARD);
					bil.setMessage(e.getMessage());
					bil.setCompany(cd);
					bil.insert();
				}
				//now if it worked ok, save that I ran ok
				BInterfaceLog bil = new BInterfaceLog();
				bil.create();
				bil.setStatus(InterfaceLog.STATUS_OK);
				bil.setLastRun(currentRun);
				bil.setInterface(InterfaceLog.INTERFACE_COBRA_GUARD);
				bil.setMessage("OK");
				bil.setCompany(cd);
				bil.insert();

			}

		} catch (Exception e) {
			//save the error

			BInterfaceLog bil = new BInterfaceLog();
			bil.create();
			bil.setStatus(InterfaceLog.STATUS_ERROR);
			bil.setLastRun(currentRun);
			bil.setInterface(InterfaceLog.INTERFACE_COBRA_GUARD);
			bil.setMessage(e.getMessage());
			bil.setCompany(ArahantSession.getHSU().getCurrentCompany());
			bil.insert();
		}
		if (BProperty.getBoolean("LogCobraFeed")) {
			out.close();
		}
		//ArahantSession.getHSU().commitTransaction(); this was causing an error
	}

	public static void main(String[] args) {
		try {
			BCompany b = new BCompany("00001-0000073561");
			ArahantSession.getHSU().setCurrentCompany(b.getBean());
			new CobraGuard().run();
		} catch (Exception ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
