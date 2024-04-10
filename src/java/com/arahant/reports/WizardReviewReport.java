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
package com.arahant.reports;

import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.AddressUtils;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

public class WizardReviewReport extends ReportBase {

	public WizardReviewReport() throws ArahantException {
		super("WizRev", "Benefit Election Summary", false);
	}

	public String build(final String employeeId) throws DocumentException {

		try {
			BPerson bpp = new BPerson();

			if (!isEmpty(employeeId))
				bpp.loadPending(employeeId);
			else
				bpp.loadPending(ArahantSession.getCurrentPerson().getPersonId());

			String fullName;

			if (!isEmpty(bpp.getMiddleName()))
				fullName = bpp.getFirstName() + " " + bpp.getMiddleName() + " " + bpp.getLastName();
			else
				fullName = bpp.getFirstName() + " " + bpp.getLastName();

			String state = bpp.getState();

			if (state.length() > 2)
				state = AddressUtils.getState(state);

			String address = bpp.getCity() + ", " + state + " " + bpp.getZip();

			PdfPTable table;

			table = makeTable(new int[]{100});

			writeColHeaderBold(table, "Demographics", Element.ALIGN_CENTER, 12F);

			write(table, "Name: " + fullName);
			if (!isEmpty(bpp.getNickName()))
				write(table, "NickName: " + bpp.getNickName());
			write(table, "SSN: " + bpp.getSsn());
			write(table, "Date of Birth: " + DateUtils.getDateFormatted(bpp.getDob()));
			write(table, "Gender: " + bpp.getSex());
			write(table, "Address: " + bpp.getStreetPending());
			if (!isEmpty(bpp.getStreet2Pending()))
				write(table, "         " + bpp.getStreet2Pending());
			if (address.length() > 3)
				write(table, "         " + address);
			if (!isEmpty(bpp.getCountyPending()))
				write(table, "County: " + bpp.getCountyPending());
			if (!isEmpty(bpp.getHomePhonePending()))
				write(table, "Home Phone: " + bpp.getHomePhonePending());
			if (!isEmpty(bpp.getWorkPhoneNumber()))
				write(table, "Work Phone: " + bpp.getWorkPhoneNumber());
			if (!isEmpty(bpp.getMobilePhone()))
				write(table, "Mobil Phone: " + bpp.getMobilePhone());
			if (!isEmpty(bpp.getWorkFaxPending()))
				write(table, "Fax: " + bpp.getWorkFaxPending());
			if (!isEmpty(bpp.getPersonalEmail()))
				write(table, "Email: " + bpp.getPersonalEmail());

			writeColHeaderBold(table, "Dependent(s)", Element.ALIGN_CENTER, 12F);

			BHREmplDependent[] edp = bpp.listPendingDependents();
			int count = 0;

			for (BHREmplDependent dp : edp) {
				if (!dp.isCurrentlyActive())
					continue;
				if (!isEmpty(dp.getMiddleName()))
					fullName = dp.getFirstName() + " " + dp.getMiddleName() + " " + dp.getLastName();
				else
					fullName = dp.getFirstName() + " " + dp.getLastName();

				write(table, "Name: " + fullName);
				write(table, "SSN: " + dp.getSsn());
				write(table, "Date of Birth: " + DateUtils.getDateFormatted(dp.getDob()));
				write(table, "Gender: " + dp.getSex());
				write(table, "Student: " + (dp.getStudent() ? "Yes" : "No"));
				write(table, "Disabled: " + (dp.getHandicap() ? "Yes" : "No"));
				if (count < edp.length - 1)
					writeCentered(table, "");
				count++;
			}

			writeColHeaderBold(table, "Benefits", Element.ALIGN_CENTER, 12F);

//			List<HrBenefitJoin> hrbj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId).orderBy(HrBenefitJoin.HR_BENEFIT_CONFIG_ID).list();
			List<HrBenefitJoin> hrbj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, employeeId)
					.geOrEq(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0)
					.ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
					.orderBy(HrBenefitJoin.HR_BENEFIT_CONFIG_ID).list();
			int size = hrbj.size();

			count = 0;

			for (List<HrBenefitJoin> bjs = BHRBenefitJoin.getGroup(hrbj); !bjs.isEmpty(); bjs = BHRBenefitJoin.getGroup(hrbj)) {
				HrBenefitJoin bj = BHRBenefitJoin.getPayingBenefitJoin(bjs);
				BHRBenefitJoin bbj = bj == null ? new BHRBenefitJoin(bjs.get(0)) : new BHRBenefitJoin(bj);
				write(table, "Benefit: " + bbj.getBenefitName());
				write(table, "Config: " + (isEmpty(bbj.getBenefitConfigName()) ? "Decline" : bbj.getBenefitConfigName()));

				String names = "";
				for (HrBenefitJoin bj2 : bjs) {
					BHRBenefitJoin bbj2 = new BHRBenefitJoin(bj2);
					if (names.length() != 0)
						names += ", ";
					if (!isEmpty(bbj2.getCoveredMiddleName()))
						names += bbj2.getCoveredFirstName() + " " + bbj2.getCoveredMiddleName() + " " + bbj2.getCoveredLastName();
					else
						names += bbj2.getCoveredFirstName() + " " + bbj2.getCoveredLastName();
				}
				write(table, "Covered People: " + names);


				if (!StringUtils.isEmpty(bbj.getCalculatedCost()))
					write(table, "Per-Pay-Period Cost: " + bbj.getCalculatedCost());
//					write(table, "Per-Pay-Period Cost: " + MoneyUtils.formatMoney(bbj.getEmployeePPPCost()));
				if (count++ < size - 1)
					writeCentered(table, "");
			}

			addTable(table);

		} finally {
			close();
		}
		return getFilename();
	}

	public static void main(String args[]) {
		try {
			new WizardReviewReport().build("00000-0000000004");
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
