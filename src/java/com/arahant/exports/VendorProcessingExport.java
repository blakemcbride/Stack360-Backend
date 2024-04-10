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


package com.arahant.exports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.*;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;
import org.kissweb.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VendorProcessingExport {

	public String export(String vendorId, String typeId, int fromDate, int toDate) throws IOException, Exception {
		File csvFile = FileSystemUtils.createTempFile("VendProc", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		writeHeader(writer);

		BVendorCompany vendor = new BVendorCompany(vendorId);

		List<HrBenefitConfig> configs = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_PROVIDER, vendor.getBean()).eq(HrBenefit.PROCESS_TYPE, HrBenefit.PROCESS_VENDOR).list();

		List<HrBenefitJoin> joins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.HR_BENEFIT_CONFIG, configs).eq(HrBenefitJoin.RECORD_CHANGE_TYPE, typeId).eq(HrBenefitJoin.APPROVED, 'Y').dateBetween(HrBenefitJoin.COVERAGE_START_DATE, fromDate, toDate).list();


		for (HrBenefitJoin hbj : joins) {
			BHRBenefitJoin bj = new BHRBenefitJoin(hbj);

			writer.writeField(bj.getPayingPerson().getNameLFM());
			writer.writeField(bj.getPayingPerson().getSsn());

			try {
				BPerson bp = bj.getCoveredPerson();

				writer.writeField(bp.getNameLFM());
				writer.writeField(bp.getSsn());
			} catch (Exception e) {
				Person pper = new BPerson(bj.getCoveredPersonId()).getPerson();
				if (pper.getRecordType() != 'C')
					pper = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, bj.getCoveredPersonId()).first();

				writer.writeField(pper.getNameLFM());
				writer.writeField(pper.getUnencryptedSsn());
			}

			writer.writeField(bj.getBenefitConfig().getBenefitName());
			writer.writeField(bj.getBenefitConfigName());
			writer.writeField(DateUtils.getDateFormatted(bj.getPolicyStartDate()));
			writer.writeField(DateUtils.getDateFormatted(bj.getPolicyEndDate()));
			writer.writeField(DateUtils.getDateFormatted(bj.getCoverageStartDate()));
			writer.writeField(DateUtils.getDateFormatted(bj.getCoverageEndDate()));
			writer.writeField(MoneyUtils.formatMoney(bj.getAmountCovered()));

			BHRBenefit bh = new BHRBenefit(bj.getBenefitId());
			BEmployee be = new BEmployee(bj.getPayingPerson());

			writer.writeField(typeId);
			writer.writeField(bh.getBean().getProvider().getName());
			writer.writeField(bh.getGroupId());
			writer.writeField(bh.getPlan());
			writer.writeField(bh.getSubGroupId());
			writer.writeField(bh.getPlanName());
			writer.writeField(DateUtils.getDateFormatted(be.getDob()));

			if (StringUtils.isEmpty(bj.getBenefitChangeReasonId())) {
				BLifeEvent bl = new BLifeEvent(bj.getLifeEvent());

				if (bl.getBean() == null || bl.getChangeReason() == null) {
					writer.writeField("");
					writer.writeField("");
				} else {
					BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bl.getChangeReason());

					writer.writeField(bl.getDescription());
					writer.writeField(DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())));
				}
			} else {
				BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());

				writer.writeField(bj.getChangeReason());
				writer.writeField(DateUtils.getDateFormatted((bc.getEffectiveDate() == 0 ? DateUtils.now() : bc.getEffectiveDate())));
			}

			writer.writeField(be.getPersonalEmail());
			writer.writeField(be.getCompanyName());
			writer.writeField(DateUtils.getDateFormatted(bj.getCoverageStartDate()));

			writer.endRecord();
		}

		writer.close();
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void writeHeader(DelimitedFileWriter writer) throws Exception {
		writer.writeField("Employee");
		writer.writeField("Employee SSN");
		writer.writeField("Covered Person");
		writer.writeField("Covered SSN");
		writer.writeField("Benefit");
		writer.writeField("Level");
		writer.writeField("Policy Start");
		writer.writeField("Policy End");
		writer.writeField("Coverage Start");
		writer.writeField("Coverage End");
		writer.writeField("Coverage Amount");
		writer.writeField("Transaction Type");
		writer.writeField("Carrier");
		writer.writeField("Policy");
		writer.writeField("Plan");
		writer.writeField("Sub Group");
		writer.writeField("Plan Name");
		writer.writeField("DOB");
		writer.writeField("Qualifying Event");
		writer.writeField("Qualifying Event date");
		writer.writeField("Email");
		writer.writeField("Company");
		writer.writeField("Effective date");

		writer.endRecord();
	}

	public static void main(String[] args) throws IOException, Exception {
		new VendorProcessingExport().export("00001-0000072450", "N", 0, 0);
	}
}
