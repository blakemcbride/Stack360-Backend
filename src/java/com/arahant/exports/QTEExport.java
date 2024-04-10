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

package com.arahant.exports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BVendorCompany;
import com.arahant.utils.AddressUtils;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.Formatting;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import java.io.File;

/**
 *
 * Arahant
 */
public class QTEExport {

	private HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(String vendorId) throws Exception {
        File csvFile = new File(FileSystemUtils.getWorkingDirectory(),DateUtils.now() + "-QTEelections-Frank Crystal.csv");

        DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

        try {
            writer.writeField("Employer ID");
            writer.writeField("Member ID");
            writer.writeField("Gender");
			writer.writeField("Last Name");
			writer.writeField("First Name");
			writer.writeField("Middle Initial");
			writer.writeField("Address Line 1");
			writer.writeField("Address Line 2");
			writer.writeField("City");
			writer.writeField("State");
			writer.writeField("Zip");
			writer.writeField("Home Phone Number");
			writer.writeField("Email Address");
			writer.writeField("Date of Birth");
			writer.writeField("Date of Hire");
			writer.writeField("Date of Rehire");
			writer.writeField("Employment Status");
			writer.writeField("QTE Payroll Mode");
			writer.writeField("QTE Account Type");
			writer.writeField("Participant's Effective Date in QTE Plan");
			writer.writeField("Per Pay QTE Deduction");
			writer.endRecord();

			for (HrBenefit bene : hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_PROVIDER)
				.eq(VendorCompany.ORGGROUPID, vendorId)
				.list())
			{

			 HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
					.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
					.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now())
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.eq(HrBenefitConfig.HR_BENEFIT, bene);
				 
				/* For Testing
				 HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class)
				.eq(Person.PERSONID, ArahantSession.getCurrentPerson().getPersonId())
				.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
				.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now());*/

				HibernateScrollUtil<Person> scr = hcu.scroll();

				while (scr.next()) {
					BEmployee be = new BEmployee(scr.get().getPersonId());

					writer.writeField(new BVendorCompany(vendorId).getAccountNumber());  //employer id, check vendor record for account number
					writer.writeField(fixSsn(scr.get().getUnencryptedSsn()));  //member is person id in their system - normally ssn
					writer.writeField(scr.get().getSex() + "");
					writer.writeField(scr.get().getLname());
					writer.writeField(scr.get().getFname());
					if (!scr.get().getMname().isEmpty())
						writer.writeField(scr.get().getMname().charAt(0) + "");
					else
						writer.writeField("");
					writer.writeField(be.getStreet());
					writer.writeField(be.getStreet2());
					writer.writeField(be.getCity());
					if (be.getState().length() > 2)
						writer.writeField(AddressUtils.getState(be.getState()));
					else
						writer.writeField(be.getState().toUpperCase());
					writer.writeField(fixZip(be.getZip()));
					writer.writeField(fixPhone(be.getWorkPhoneNumber()));  //be.getHomePhone()
					writer.writeField(be.getPersonalEmail());
					writer.writeField(be.getDob());
					writer.writeField(be.getHireDate());
					if (be.getRehireDate() != 0)
						writer.writeField(be.getRehireDate());
					else
						writer.writeField("");
					if (be.getWorkHours() > 30)
						writer.writeField("FT");
					else
						writer.writeField("PT");

					int count=be.getPayPeriodsPerYear();

					if (count > 50)
						writer.writeField("W");
					else if (count == 12)
						writer.writeField("M");
					else if (count == 24)
						writer.writeField("S");
					else if (count > 24)
						writer.writeField("B");
					else
						writer.writeField("");

					HrBenefitJoin bj=hsu.createCriteria(HrBenefitJoin.class)
						.eq(HrBenefitJoin.PAYING_PERSON, be.getPerson())
						.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
						.eq(HrBenefitConfig.HR_BENEFIT, bene)
						.first();

					writer.writeField(bene.getInsuranceCode());

					writer.writeField(bj.getPolicyStartDate());

					writer.writeField(Formatting.formatNumber(MoneyUtils.parseMoney(bj.getCalculatedCost()),2));

					writer.endRecord();
				}

			    scr.close();
			}

        } finally {
            writer.close();
        }

        return csvFile.getName();

    }

	
	//payroll mode, read payroll schedule for employee group and check the count
	// check annual count
	//  > 50  weekly
	//  12 = monthly
	// 24 = semi monthly
	// > 24 is bi weekly

	//qte account type - use benefit insuance code  (in database as one of the codes)

	//bene join policy start date

	// benefit join get coverage amount / whatever makes sense by payroll schedule



	public String fixPhone(String x)
	{
		if (x==null)
			return "";

		return x.replace(")", "(").replace("(", "").replace("-", "").replace(" ", "");
	}

	public String fixZip(String x)
	{
		if (x==null)
			return "";

		if (x.length() > 5)
			return x.substring(0, 5);

		return x;
	}

	public String fixSsn(String x)
	{
		return x.replace("-", "");
	}
	
	public static void main(String args[])
	{
		try
		{
			String vendorId = "00001-0000000047";
			//BPerson bp = new BPerson("00000-0000000004");
			//ArahantSession.getHSU().setCurrentPerson(bp.getPerson());
			QTEExport qte = new QTEExport();
			qte.build(vendorId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}
