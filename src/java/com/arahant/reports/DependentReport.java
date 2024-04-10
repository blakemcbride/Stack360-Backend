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

package com.arahant.reports;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BAgent;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Arahant
 */
public class DependentReport extends ReportBase {

	public DependentReport() {
		super("dependent", "Dependent Report");
	}

	public String build(String sortBy, boolean includeEmployee) {
		try {
			PdfPTable table = makeTable(new int[]{100});

			writeHeaderLine("Company", ArahantSession.getHSU().getCurrentCompany().getName());
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();

			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
				for(OrgGroupAssociation o : oga)
					ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
			}

			HibernateCriteriaUtil<HrEmplDependent> hcu = ArahantSession.getHSU().createCriteria(HrEmplDependent.class);
			if(sortBy.equals("D"))
			{
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
					hcu.joinTo(HrEmplDependent.PERSON)
						.orderBy(Person.LNAME)
						.orderBy(Person.FNAME)
						.joinTo(HrEmplDependent.EMPLOYEE)
						.joinTo(Employee.ORGGROUPASSOCIATIONS)
						.in(OrgGroupAssociation.ORGGROUP, ogs);
				}
				else {
					hcu.joinTo(HrEmplDependent.PERSON)
						.orderBy(Person.LNAME)
						.orderBy(Person.FNAME)
						.eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany());
				}
			}
			else
			{
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
					hcu.joinTo(HrEmplDependent.EMPLOYEE)
						.orderBy(Employee.LNAME)
						.orderBy(Person.FNAME)
						.joinTo(Employee.ORGGROUPASSOCIATIONS)
						.in(OrgGroupAssociation.ORGGROUP, ogs);
				}
				else {
					hcu.joinTo(HrEmplDependent.EMPLOYEE)
						.orderBy(Employee.LNAME)
						.orderBy(Person.FNAME)
						.eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany());
				}
			}

			List<HrEmplDependent> dependentList = hcu.list();

			for(HrEmplDependent hed : dependentList)
			{
				BPerson dep = new BPerson(hed.getPerson());
				BEmployee emp = new BEmployee(hed.getEmployee());

				if(includeEmployee)
				{
					writeLeft(table, dep.getNameLFM() + " (" + (hed.getRelationshipType() == 'S' ? "Spouse" : hed.getRelationshipType() == 'C' ? "Child" : "Dependent") + " of " + emp.getNameFML() + ")", true);
				}
				else
				{
					writeLeft(table, dep.getNameLFM(), true);
				}
				if(!isEmpty(dep.getSsn()))
				{
					write(table, "            SSN: " + dep.getSsn());
				}
				else
				{
					write(table, "            SSN: ");
				}
				if(dep.getDob() > 0)
				{
					write(table, "            DOB: " + DateUtils.getDateFormatted(dep.getDob()));
				}
				else
				{
					write(table, "            DOB: ");
				}
				if(dep.getAddresses().size() > 0 && !isEmpty(dep.getStreet()))
				{
					write(table, "        Address: " + dep.getStreet() + " " + dep.getStreet2());
					write(table, "                 " + dep.getCity() + ", " + dep.getState() + " " + dep.getZip());
				}
				else
				{
					write(table, "        Address: ");
				}
				if(!isEmpty(dep.getHomePhone()))
				{
					write(table, "     Home Phone: " + dep.getHomePhone());
				}
				else
				{
					write(table, "     Home Phone: ");
				}
				if(!isEmpty(dep.getWorkPhone()))
				{
					write(table, "     Work Phone: " + dep.getWorkPhone());
				}
				else
				{
					write(table, "     Work Phone: ");
				}
				if(!isEmpty(dep.getMobilePhone()))
				{
					write(table, "     Cell Phone: " + dep.getMobilePhone());
				}
				else
				{
					write(table, "     Cell Phone: ");
				}
				boolean writeBenefit = true;
				for(HrBenefitJoin depJoin : ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, dep.getPerson()).notNull(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitJoin.APPROVED, 'Y').list())
				{
					BHRBenefitJoin bdepJoin = new BHRBenefitJoin(depJoin);
					if(writeBenefit)
					{
						write(table, "       Benefits: " + bdepJoin.getBenefitName() + " - " + bdepJoin.getBenefitConfigName());
						writeBenefit = false;
					}
					else
					{
						write(table, "                 " + bdepJoin.getBenefitName() + " - " + bdepJoin.getBenefitConfigName());
					}
				}
				write(table, "");

			}

			addTable(table);
			return getFilename();
		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();
		}

	}


	public static void main(String args[]) {
		try {
			ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().createCriteria(CompanyDetail.class).eq(CompanyDetail.COMPANY_ID, "00001-0000072568").first());
			new DependentReport().build("E", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
