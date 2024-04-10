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
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BenefitCostReport extends ReportBase {

	public BenefitCostReport() throws ArahantException {
		super("BeneCostRep", "Benefit Costs", true);
	}

	public String build() throws DocumentException {

		try {

			PdfPTable table;

			Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			for (OrgGroupAssociation o : oga)
				ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

			addHeaderLine();

			boolean alternateRow = true;

			addColumnHeader("Employee", Element.ALIGN_LEFT, 15);
			addColumnHeader("Plan", Element.ALIGN_LEFT, 15);
			addColumnHeader("Employee Contribution", Element.ALIGN_RIGHT, 15);
			addColumnHeader("Employer Contribution", Element.ALIGN_RIGHT, 15);
			addColumnHeader("Total Premium", Element.ALIGN_RIGHT, 15);

			table = makeTable(getColHeaderWidths());

			//writeColHeaders(table);
			setNoColumnHeadersOnNextPage();

			addTable(table);

			//loop through all the companies
			// <editor-fold defaultstate="collapsed" desc="comment">
			for (CompanyDetail cd : ArahantSession.getHSU().createCriteria(CompanyDetail.class).list()) {
				//ArahantSession.getHSU().setCurrentCompany(cd);

				List<VendorCompany> vendors = ArahantSession.getHSU().createCriteria(VendorCompany.class).eq(VendorCompany.ASSOCIATED_COMPANY, cd).list();

				table = makeTable(new int[]{33, 33, 33});
				write(table, "");
				writeCenteredWithBorder(table, cd.getName());
				write(table, "");
				addTable(table);

				if (vendors.size() == 0) {
					table = makeTable(new int[]{100});
					writeCenteredNoBorder(table, "No Enrolled Benefits for this Company");
					addTable(table);
				}

				//loop through all the vendors for the current company
				for (VendorCompany v : vendors) {
					table = makeTable(new int[]{33, 33, 33});
					write(table, "");
					writeCentered(table, v.getName());
					write(table, "");
					addTable(table);

					table = makeTable(getColHeaderWidths());

					writeColHeaders(table);

					//Get all the benefits offered by the current vendor
					List<HrBenefit> hb = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, v).list();

					double totalEmployeeCost = 0;
					double totalEmployerCost = 0;
					double totalPremiumCost = 0;

					//loop through all the benefits
					for (HrBenefit h : hb)
						//loop through the benefit configs for each benefit
						for (HrBenefitConfig bc : h.getBenefitConfigs()) {
							HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc).eq(HrBenefitJoin.APPROVED, 'Y');

							hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.COMPANY, cd);

							if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
								hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
							else
								hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);

							List<HrBenefitJoin> hbj = hcu.list();

							String oldName = "";

							//loop through all the benefitjoins for each config
							for (HrBenefitJoin bj : hbj) {
								String newName = bj.getPayingPerson().getLname() + ", " + bj.getPayingPerson().getFname();

								if (!newName.equals(oldName)) {
									if (bj.getHrBenefitConfig() != null) {
										alternateRow = !alternateRow;

										write(table, newName, alternateRow);

										if (bj.getHrBenefitConfig().getHrBenefit() != null)
											write(table, bj.getHrBenefitConfig().getHrBenefit().getName(), alternateRow);
										else
											write(table, "Benefit Name not found", alternateRow);

										write(table, MoneyUtils.formatMoney(BenefitCostCalculator.calculateCostNewMethodAnnual(bj, DateUtils.now())), alternateRow);
										totalEmployeeCost += BenefitCostCalculator.calculateCostNewMethodAnnual(bj, DateUtils.now());
										write(table, MoneyUtils.formatMoney(bj.getHrBenefitConfig().deprecatedGetEmployerCost()), alternateRow);
										totalEmployerCost += bj.getHrBenefitConfig().deprecatedGetEmployerCost();
										write(table, MoneyUtils.formatMoney(BenefitCostCalculator.calculateCostNewMethodAnnual(bj, DateUtils.now()) + bj.getHrBenefitConfig().deprecatedGetEmployerCost()), alternateRow);
										totalPremiumCost += BenefitCostCalculator.calculateCostNewMethodAnnual(bj, DateUtils.now()) + bj.getHrBenefitConfig().deprecatedGetEmployerCost();
									}

									oldName = newName;
								}
							}
						}

					if (totalEmployeeCost == 0 && totalEmployerCost == 0 && totalPremiumCost == 0)
						alternateRow = false;
					else
						alternateRow = !alternateRow;

					write(table, "Totals", alternateRow);
					write(table, "", alternateRow);
					write(table, MoneyUtils.formatMoney(totalEmployeeCost), alternateRow);
					write(table, MoneyUtils.formatMoney(totalEmployerCost), alternateRow);
					write(table, MoneyUtils.formatMoney(totalPremiumCost), alternateRow);

					addTable(table);
				}
			}// </editor-fold>

		} finally {
			close();
		}

		return getFilename();
	}

	public static void main(String args[]) {
		try {
			new BenefitCostReport().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
