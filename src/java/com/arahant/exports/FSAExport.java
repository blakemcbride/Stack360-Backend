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

package com.arahant.exports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BVendorCompany;
import com.arahant.utils.AddressUtils;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import java.io.File;


public class FSAExport {

	ArahantLogger logger=new ArahantLogger(FSAExport.class);
		private HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(String vendorId) throws Exception {
        File csvFile = new File(FileSystemUtils.getWorkingDirectory(),DateUtils.now() + "_FSA_Enrollment_Frank Crystal.csv");
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
			writer.writeField("Employee Status");
			writer.writeField("FSA Payroll Deduction Frequency");
			writer.writeField("Date of Hire");
			writer.writeField("Date of Rehire");
			writer.writeField("Employer Health Ins. 6 Digit Char. Code");
			writer.writeField("Employer Health Insurance Effective Date");
			writer.writeField("Employer Dental Ins. 6 Digit Char. Code");
			writer.writeField("Employer Dental Insurance Effective Date");
			writer.writeField("Employer Vision Ins. Digit Char. Code");
			writer.writeField("Employer Vision Insurance Effective Date");
			writer.writeField("Beniversal Card Participant");
			writer.writeField("Account Type");
			writer.writeField("Participant's Effective Date in Plan");
			writer.writeField("Per Pay Deduction Amount");
			writer.writeField("Total Annual Election");

			writer.endRecord();

			HibernateCriteriaUtil<HrBenefitJoin> bjHcu=hsu.createCriteria(HrBenefitJoin.class);
			bjHcu.eq(HrBenefitJoin.APPROVED, 'Y');
			bjHcu.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now());
			bjHcu.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON);
			HibernateCriteriaUtil beneHcu=bjHcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT);
			beneHcu.joinTo(HrBenefit.BENEFIT_CATEGORY)
					.eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);
			beneHcu.joinTo(HrBenefit.BENEFIT_PROVIDER)
				.eq(VendorCompany.ORGGROUPID, vendorId);



			HibernateScrollUtil<HrBenefitJoin> scr = bjHcu.scroll();

			while (scr.next()) {

				BEmployee be = new BEmployee(scr.get().getPayingPersonId());

				HrBenefitJoin bj =  scr.get();

				HrBenefitJoin health = getBenefitJoin(HrBenefitCategory.HEALTH, be.getPersonId());
				HrBenefitJoin dental = getBenefitJoin(HrBenefitCategory.DENTAL, be.getPersonId());
				HrBenefitJoin vision = getBenefitJoin(HrBenefitCategory.VISION, be.getPersonId());

				writer.writeField(new BVendorCompany(vendorId).getAccountNumber());  //employer id, check vendor record for account number
				writer.writeField(fixSsn(be.getSsn()));  //member is person id in their system - normally ssn
				writer.writeField(be.getSex() + "");
				writer.writeField(be.getLastName());
				writer.writeField(be.getFirstName());
				if (!be.getMiddleName().isEmpty())
					writer.writeField(be.getMiddleName().charAt(0) + "");
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
/*change back*/	writer.writeField(fixPhone(be.getWorkPhoneNumber()));  //(be.getHomePhone()));
				writer.writeField(be.getPersonalEmail());
				writer.writeField(be.getDob());
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

				writer.writeField(be.getHireDate());
				if (be.getRehireDate() != 0)
					writer.writeField(be.getRehireDate());
				else
					writer.writeField("");

				if (health == null)
				{
					writer.writeField("NoMED");  //Employer Health Ins. 6 Digit Char. Code
					writer.writeField(bj.getCoverageStartDate());  //Employer Health Insurance Effective Date
				}
				else
				{
					System.out.println("Health\n-----------------");
					System.out.println("InsuranceCode: " + health.getHrBenefitConfig().getHrBenefit().getInsuranceCode());
					System.out.println("BenefitId:" + health.getHrBenefitConfig().getHrBenefit().getBenefitId());
					writer.writeField(health.getHrBenefitConfig().getHrBenefit().getInsuranceCode());  //Employer Health Ins. 6 Digit Char. Code
					writer.writeField(health.getCoverageStartDate());  //Employer Health Insurance Effective Date
				}

				if (dental == null)
				{
					writer.writeField("NoDEN");  //Employer Dental Ins. 6 Digit Char. Code
					writer.writeField(bj.getCoverageStartDate());  //Employer Dental Insurance Effective Date
				}
				else
				{
					System.out.println("Dental\n-----------------");
					System.out.println("InsuranceCode: " + dental.getHrBenefitConfig().getHrBenefit().getInsuranceCode());
					System.out.println("BenefitId:" + dental.getHrBenefitConfig().getHrBenefit().getBenefitId());
					writer.writeField(dental.getHrBenefitConfig().getHrBenefit().getInsuranceCode());  //Employer Dental Ins. 6 Digit Char. Code
					writer.writeField(dental.getCoverageStartDate());  //Employer Dental Insurance Effective Date
				}

				if (vision == null)
				{
					writer.writeField("NoOPT");  //Employer Vision Ins. Digit Char. Code
					writer.writeField(bj.getCoverageStartDate());  //Employer Vision Insurance Effective Date
				}
				else
				{
					System.out.println("Vision\n-----------------");
					System.out.println("InsuranceCode: " + vision.getHrBenefitConfig().getHrBenefit().getInsuranceCode());
					System.out.println("BenefitId:" + vision.getHrBenefitConfig().getHrBenefit().getBenefitId());
					writer.writeField(vision.getHrBenefitConfig().getHrBenefit().getInsuranceCode());  //Employer Vision Ins. Digit Char. Code
					writer.writeField(vision.getCoverageStartDate());  //Employer Vision Insurance Effective Date
				}

				writer.writeField("N");  //Beniversal Card Participant C = Issue Card; N = Do Not Issue Card; Y= Already Have a Card

				writer.writeField(bj.getHrBenefitConfig().getHrBenefit().getInsuranceCode());  //Account Type

				writer.writeField(bj.getPolicyStartDate());

				writer.writeField(MoneyUtils.parseMoney(bj.getCalculatedCost()));  //Per Pay Deduction Amount

				writer.writeField(bj.getAmountCovered());  //Total Annual Election

				writer.endRecord();
			}

			scr.close();
			writer.endRecord();
			

        } finally {
            writer.close();
        }
		
        return csvFile.getName();

    }


	public HrBenefitJoin getBenefitJoin(short type, String coveredPersonId)
	{
		HrBenefitJoin bj= hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.PAYING_PERSON_ID, coveredPersonId)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, type)
			.first();

//		if (bj==null)
//			System.out.println("No join found for "+type+" "+coveredPersonId);
//		else
//			System.out.println("Found join for "+type+" "+coveredPersonId);

		return bj;
	}

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
			ArahantSession.getHSU().dontAIIntegrate();
			String vendorId = "00001-0000000047";
			//BPerson bp = new BPerson("00000-0000000004");
			//ArahantSession.getHSU().setCurrentPerson(bp.getPerson());
			FSAExport fsa = new FSAExport();
			fsa.build(vendorId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

}
