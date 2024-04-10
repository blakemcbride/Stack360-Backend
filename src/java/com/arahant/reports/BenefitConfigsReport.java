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
 *
 */
package com.arahant.reports;

import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BVendorCompany;
import com.arahant.business.BWageType;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

public class BenefitConfigsReport extends ReportBase {

    public BenefitConfigsReport() throws ArahantException {
        super("BeneCfgs", "Benefit Configurations", false);
    }

    public String build(final String benefitId) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			BHRBenefit hrb = new BHRBenefit(benefitId);

			table = makeTable(new int[]{100});

			if (!isEmpty(hrb.getUnderwriterId()))
			{

				BVendorCompany vc = new BVendorCompany(hrb.getUnderwriterId());



				writeColHeaderBold(table, "Vendor", Element.ALIGN_LEFT, 10);

				write(table, "Vendor Id: " + vc.getExternalId());

				write(table, "Vendor: " + hrb.getUnderwriter());

				write(table, "Street: " + vc.getStreet());

				if (!vc.getStreet2().equals(""))
					write(table, "Street2: " + vc.getStreet2());

				write(table, "City, State, Zip: " + vc.getCity() + ", " + vc.getState() + " " + vc.getZip());

				write(table, "Phone: " + vc.getMainPhoneNumber());

				write(table, "Fax: " + vc.getMainFaxNumber());

				write(table, "");

				writeColHeaderBold(table, "Main Contact", Element.ALIGN_LEFT, 10);

				write(table, "First Name: " + vc.getMainContactFname());

				write(table, "Last Name: " + vc.getMainContactLname());

				write(table, "Email: " + vc.getMainContactPersonalEmail());

				write(table, "Job Title: " + vc.getMainContactJobTitle());

				write(table, "Phone: " + vc.getMainContactWorkPhone());

				write(table, "Fax: " + vc.getMainContactWorkFax());

				write(table, "");
			}

			writeColHeaderBold(table, "Benefit", Element.ALIGN_LEFT, 10);

			write(table, "Benefit: " + hrb.getName());
			
			write(table, "Category: " + hrb.getCategoryName());
			
			write(table, "Wage Type: " + new BWageType(hrb.getWageTypeId()).getName());

			switch (hrb.getEligibilityType())
			{
				case 1: write(table, "Eligibility Days from Hire: First Day of Employment");
						break;
				case 2: write(table, "Eligibility Days from Hire: First Day of the Month following " + hrb.getEligibilityPeriod() + " Days of Employment");
						break;
				case 3: write(table, "Eligibility Days from Hire: First Day of the Month following " + hrb.getEligibilityPeriod() + " Months of Employment");
						break;
				case 4: write(table, "Eligibility Days from Hire: After " + hrb.getEligibilityPeriod() + " Days of Employment");
						break;
				case 5: write(table, "Eligibility Days from Hire: After " + hrb.getEligibilityPeriod() + " Months of Employment");
						break;
				default: write(table, "Eligibility Days from Hire: ");
						 break;
			}
			
			write(table, "Loss of Dependent Status: " + hrb.getDependentMaxAge() + "  If Student: " + hrb.getDependentMaxAgeStudent());

			write(table, "");

			List<String> l = hrb.getConfigIds();

			writeColHeaderBold(table, "Benefit Configurations", Element.ALIGN_LEFT, 10);
			
			for (String s: l)
			{
				write(table, "Coverage Type: " + new BHRBenefitConfig(s).getConfigName());
				write(table, "Coverage: " + new BHRBenefitConfig(s).getCoverage());
				write(table, "");
			}

			addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
			String beneId = "00000-0000000004";
            new BenefitConfigsReport().build(beneId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

