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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

public class VendorProcessingReport extends ReportBase {

	public VendorProcessingReport() throws ArahantException {
		super("VendProc", "Vendor Processing Report", false);
	}

	public String build(String vendorId, String typeId, int fromDate, int toDate) throws DocumentException {

		try {

			PdfPTable table;

			addHeaderLine();

			BVendorCompany vendor = new BVendorCompany(vendorId);

			List<HrBenefitConfig> configs = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_PROVIDER, vendor.getBean()).eq(HrBenefit.PROCESS_TYPE, HrBenefit.PROCESS_VENDOR).list();

			List<HrBenefitJoin> joins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.HR_BENEFIT_CONFIG, configs).eq(HrBenefitJoin.RECORD_CHANGE_TYPE, typeId.charAt(0)).eq(HrBenefitJoin.APPROVED, 'Y').dateBetween(HrBenefitJoin.COVERAGE_START_DATE, fromDate, toDate).list();

			for (HrBenefitJoin hbj : joins) {
				BHRBenefitJoin bj = new BHRBenefitJoin(hbj);

				table = makeTable(new int[]{33, 33, 33});
				write(table, "");
				writeCentered(table, bj.getPayingPerson().getNameFML());//LFM());
				write(table, "");
				addTable(table);

				table = makeTable(new int[]{15, 85});

//                write(table, "");
//                write(table, "Employee: " + bj.getPayingPerson().getNameLFM());
				write(table, "");
				write(table, "Employee SSN: " + bj.getPayingPerson().getSsn());
				write(table, "");

				try {
					BPerson bp = bj.getCoveredPerson();

					write(table, "Covered Person: " + bp.getNameLFM());
					write(table, "");
					write(table, "Covered SSN: " + bp.getSsn());
					write(table, "");
				} catch (Exception e) {
					Person pper = new BPerson(bj.getCoveredPersonId()).getPerson();
					if (pper.getRecordType() != 'C')
						pper = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, bj.getCoveredPersonId()).first();

					write(table, "Covered Person: " + pper.getNameLFM());
					write(table, "");
					write(table, "Covered SSN: " + pper.getUnencryptedSsn());
					write(table, "");
				}

				write(table, "Benefit: " + bj.getBenefitConfig().getBenefitName());
				write(table, "");
				write(table, "Level: " + bj.getBenefitConfigName());
				write(table, "");
				write(table, "Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()));
				write(table, "");
				write(table, "Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()));
				write(table, "");
				write(table, "Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()));
				write(table, "");
				write(table, "Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()));
				write(table, "");
				write(table, "Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()));

				BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
				BEmployee be = new BEmployee(bj.getPayingPerson());

				write(table, "");
				write(table, "Transaction Type: " + typeId);
				write(table, "");
				write(table, "Carrier: " + bh.getBean().getProvider().getName());
				write(table, "");
				write(table, "Policy: " + bh.getGroupId());
				write(table, "");
				write(table, "Plan: " + bh.getPlan());
				write(table, "");
				write(table, "Sub Group: " + bh.getSubGroupId());
				write(table, "");
				write(table, "Plan Name: " + bh.getPlanName());
				write(table, "");
				write(table, "DOB: " + DateUtils.getDateFormatted(be.getDob()));
				write(table, "");

				if (isEmpty(bj.getBenefitChangeReasonId())) {
					BLifeEvent bl = new BLifeEvent(bj.getLifeEvent());

					if (bl.getBean() == null || bl.getChangeReason() == null) {
						write(table, "Qualifying Event: ");
						write(table, "");
						write(table, "Qualifying Event Date: ");
						write(table, "");
					} else {
						BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bl.getChangeReason());

						write(table, "Qualifying Event: " + bl.getDescription());
						write(table, "");
						write(table, "Qualifying Event Date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())));
						write(table, "");
					}
				} else {
					BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());

					write(table, "Qualifying Event: " + bj.getChangeReason());
					write(table, "");
					write(table, "Qualifying Event Date: " + DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())));
					write(table, "");
				}

				write(table, "Email: " + be.getPersonalEmail());
				write(table, "");
				write(table, "Company: " + be.getCompanyName());
				write(table, "");
				write(table, "Effective Date: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()));

				addTable(table);

			}

		} finally {
			close();
		}

		return getFilename();
	}

	public static void main(String args[]) {
		try {
			new VendorProcessingReport().build("00001-0000072468", "N", 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
