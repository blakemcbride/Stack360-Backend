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
 *
 */
package com.arahant.reports;

import com.arahant.beans.*;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BenefitChangeReport extends ReportBase {

	public BenefitChangeReport() throws ArahantException {
		super("BenChgRpt", "Benefit Changes", true);
	}

	public String build(final int fromDate, final int toDate) throws DocumentException {

		try {

			hsu.dontAIIntegrate();

			//System.out.println("current co: " + ArahantSession.getHSU().getCurrentCompany().getName());

			PdfPTable table;

			addHeaderLine();

			Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			for (OrgGroupAssociation o : oga)
				ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

// <editor-fold defaultstate="collapsed" desc="old code">
//			List<String> pids=(List)hsu.createCriteria(IHrBenefitJoinCurrent.class)
//				.selectFields(IHrBenefitJoinCurrent.PAYING_PERSON_ID)
//				.dateBetween(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate))
//				.joinTo(IHrBenefitJoinCurrent.PAYING_PERSON)
//				.eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
//				.list();
//
//            HibernateCriteriaUtil<IHrBenefitJoinCurrent> hcu = ArahantSession.getHSU().createCriteria(IHrBenefitJoinCurrent.class)
//				.in(IHrBenefitJoinCurrent.PAYING_PERSON_ID, pids)
//				.eqJoinedField(IHrBenefitJoinCurrent.PAYING_PERSON, IHrBenefitJoinCurrent.COVERED_PERSON)
//				.joinTo(IHrBenefitJoinCurrent.HR_BENEFIT_CONFIG); //only load ones with configs// </editor-fold>
			//Get all active joins
			List<String> pids;
			if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
				pids = (List) hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.PAYING_PERSON_ID).dateBetween(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate)).joinTo(HrBenefitJoin.PAYING_PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs).list();
			else
				pids = (List) hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.PAYING_PERSON_ID).dateBetween(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate)).joinTo(HrBenefitJoin.PAYING_PERSON).eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany()).list();

			HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, pids).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG); //only load ones with configs

			HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

			//Get all terminates
			List<String> pids2;
			if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
				pids2 = (List) hsu.createCriteria(HrBenefitJoinHDeletes.class).selectFields(HrBenefitJoinHDeletes.PAYING_PERSON_ID).dateBetween(HrBenefitJoinHDeletes.HISTORY_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate)).joinTo(HrBenefitJoin.PAYING_PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs).list();
			else
				pids2 = (List) hsu.createCriteria(HrBenefitJoinHDeletes.class).selectFields(HrBenefitJoinHDeletes.PAYING_PERSON_ID).dateBetween(HrBenefitJoinHDeletes.HISTORY_DATE, DateUtils.getDate(fromDate), DateUtils.getDate(toDate)).joinTo(HrBenefitJoinHDeletes.PAYING_PERSON).eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany()).list();

			HibernateCriteriaUtil<HrBenefitJoinHDeletes> hcu2 = ArahantSession.getHSU().createCriteria(HrBenefitJoinHDeletes.class).in(HrBenefitJoinHDeletes.PAYING_PERSON_ID, pids2).eqJoinedField(HrBenefitJoinHDeletes.PAYING_PERSON, HrBenefitJoinHDeletes.COVERED_PERSON).joinTo(HrBenefitJoinHDeletes.HR_BENEFIT_CONFIG); //only load ones with configs

			HibernateScrollUtil<HrBenefitJoinHDeletes> scr2 = hcu2.scroll();

			table = makeTable(new int[]{22, 2, 15, 2, 25, 2, 25, 2, 25, 2, 15});

			writeColHeader(table, "Carrier", Element.ALIGN_LEFT);
			write(table, "", false);
			writeColHeader(table, "Transaction Type", Element.ALIGN_LEFT);
			write(table, "", false);
			writeColHeader(table, "Plan", Element.ALIGN_LEFT);
			write(table, "", false);
			writeColHeader(table, "Employee / Dependent", Element.ALIGN_LEFT);
			write(table, "", false);
			writeColHeader(table, "Contract Type", Element.ALIGN_LEFT);
			write(table, "", false);
			writeColHeader(table, "Effective Date of Change", Element.ALIGN_LEFT);
			boolean alternateRow = true;


// <editor-fold defaultstate="collapsed" desc="old code">
//			for (IHrBenefitJoinCurrent cbj : hcu.list())
//			{
//                // toggle the alternate row
//                alternateRow = !alternateRow;
//
//				if (cbj.getHrBenefitConfig().getHrBenefit().getProvider() == null)
//					write(table, "Provider Not Found", alternateRow);
//				else
//					write(table, cbj.getHrBenefitConfig().getHrBenefit().getProvider().getName(), alternateRow);
//
//				write(table, "", alternateRow);
//
//				if (cbj.getRecordChangeType() == 'N')
//					write(table, "New", alternateRow);
//				else if (cbj.getRecordChangeType() == 'M')
//					write(table, "Modified", alternateRow);
//				else if (cbj.getRecordChangeType() == 'D')
//					write(table, "Terminated", alternateRow);
//
//				write(table, "", alternateRow);
//
//				write(table, cbj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);
//
//				write(table, "", alternateRow);
//
//                write(table, cbj.getPayingPerson().getNameLFM(), alternateRow);
//
//				write(table, "", alternateRow);
//
//                write(table, cbj.getHrBenefitConfig().getName(), alternateRow);
//
//				write(table, "", alternateRow);
//
//				int x=Math.max(cbj.getPolicyStartDate(), cbj.getCoverageStartDate());
//				x=Math.max(cbj.getCoverageEndDate(), x);
//				write(table, DateUtils.getDateFormatted(x), alternateRow);
//
//				for (IHrBenefitJoinCurrent dpbj : ArahantSession.getHSU().createCriteria(IHrBenefitJoinCurrent.class)
//					.neqField(IHrBenefitJoinCurrent.PAYING_PERSON, IHrBenefitJoinCurrent.COVERED_PERSON)
//					.eq(IHrBenefitJoinCurrent.HR_BENEFIT_CONFIG_ID, cbj.getHrBenefitConfigId())
//					.eq(IHrBenefitJoinCurrent.POLICY_START_DATE, cbj.getPolicyStartDate())
//					.eq(IHrBenefitJoinCurrent.PAYING_PERSON, cbj.getPayingPerson())
//					.list())
//				{
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//
//					Person per = dpbj.getCoveredPerson();
//					Person pper;
//
//					try
//					{
//						write(table, per.getNameLFM(), alternateRow);
//					}
//					catch(Exception e)
//					{
//						pper = new BPerson(dpbj.getCoveredPersonId()).getPerson();
//						if(pper.getRecordType()!='C')
//						{
//							pper = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, dpbj.getCoveredPersonId()).first();
//						}
//						write(table, pper.getNameLFM(), alternateRow);
//					}
//
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//					write(table, "", alternateRow);
//				}
//
//            }// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="deletes">

			while (scr2.next()) {
				HrBenefitJoinHDeletes cbj = scr2.get();
//				System.out.println("***********************************************");
//				System.out.println("person: " + cbj.getPayingPerson().getNameLFM());
//				System.out.println("company: " + cbj.getPayingPerson().getCompanyBase().getName());
//				System.out.println("***********************************************");


				// toggle the alternate row
				alternateRow = !alternateRow;

				if (cbj.getHrBenefitConfig().getHrBenefit().getProvider() == null)
					write(table, "Provider Not Found", alternateRow);
				else
					write(table, cbj.getHrBenefitConfig().getHrBenefit().getProvider().getName(), alternateRow);

				write(table, "", alternateRow);

				if (cbj.getRecordChangeType() == 'N')
					write(table, "New", alternateRow);
				else if (cbj.getRecordChangeType() == 'M')
					write(table, "Modified", alternateRow);
				else if (cbj.getRecordChangeType() == 'D')
					write(table, "Terminated", alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getPayingPerson().getNameLFM(), alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getHrBenefitConfig().getName(), alternateRow);

				write(table, "", alternateRow);

				int x = Math.max(cbj.getPolicyStartDate(), cbj.getCoverageStartDate());
				x = Math.max(cbj.getCoverageEndDate(), x);
				write(table, DateUtils.getDateFormatted(x), alternateRow);

				for (HrBenefitJoinHDeletes dpbj : ArahantSession.getHSU().createCriteria(HrBenefitJoinHDeletes.class).neqField(HrBenefitJoinHDeletes.PAYING_PERSON, HrBenefitJoinHDeletes.COVERED_PERSON).eq(HrBenefitJoinHDeletes.HR_BENEFIT_CONFIG_ID, cbj.getHrBenefitConfigId()).eq(HrBenefitJoinHDeletes.POLICY_START_DATE, cbj.getPolicyStartDate()).eq(HrBenefitJoinHDeletes.PAYING_PERSON, cbj.getPayingPerson()).list()) {
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);

					Person per = dpbj.getCoveredPerson();
					Person pper;

					try {
						write(table, per.getNameLFM(), alternateRow);
					} catch (Exception e) {
						pper = new BPerson(dpbj.getCoveredPersonId()).getPerson();
						if (pper.getRecordType() != 'C')
							pper = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, dpbj.getCoveredPersonId()).first();
						write(table, pper.getNameLFM(), alternateRow);
					}

					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
				}

			}// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="actives">

			while (scr.next()) {
				HrBenefitJoin cbj = scr.get();
//				System.out.println("***********************************************");
//				System.out.println("person: " + cbj.getPayingPerson().getNameLFM());
//				System.out.println("company: " + cbj.getPayingPerson().getCompanyBase().getName());
//				System.out.println("***********************************************");


				// toggle the alternate row
				alternateRow = !alternateRow;

				if (cbj.getHrBenefitConfig().getHrBenefit().getProvider() == null)
					write(table, "Provider Not Found", alternateRow);
				else
					write(table, cbj.getHrBenefitConfig().getHrBenefit().getProvider().getName(), alternateRow);

				write(table, "", alternateRow);

				if (cbj.getRecordChangeType() == 'N')
					write(table, "New", alternateRow);
				else if (cbj.getRecordChangeType() == 'M')
					write(table, "Modified", alternateRow);
				else if (cbj.getRecordChangeType() == 'D')
					write(table, "Terminated", alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getPayingPerson().getNameLFM(), alternateRow);

				write(table, "", alternateRow);

				write(table, cbj.getHrBenefitConfig().getName(), alternateRow);

				write(table, "", alternateRow);

				int x = Math.max(cbj.getPolicyStartDate(), cbj.getCoverageStartDate());
				x = Math.max(cbj.getCoverageEndDate(), x);
				write(table, DateUtils.getDateFormatted(x), alternateRow);

				for (HrBenefitJoin dpbj : ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).neqField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, cbj.getHrBenefitConfigId()).eq(HrBenefitJoin.POLICY_START_DATE, cbj.getPolicyStartDate()).eq(HrBenefitJoin.PAYING_PERSON, cbj.getPayingPerson()).list()) {
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);

					Person per = dpbj.getCoveredPerson();
					Person pper;

					try {
						write(table, per.getNameLFM(), alternateRow);
					} catch (Exception e) {
						pper = new BPerson(dpbj.getCoveredPersonId()).getPerson();
						if (pper.getRecordType() != 'C')
							pper = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, dpbj.getCoveredPersonId()).first();
						write(table, pper.getNameLFM(), alternateRow);
					}

					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
					write(table, "", alternateRow);
				}

			}// </editor-fold>

			addTable(table);

		} finally {
			close();

		}

		return getFilename();
	}

	public static void main(String args[]) {
		try {
			new BenefitChangeReport().build(20090801, 20100101);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
