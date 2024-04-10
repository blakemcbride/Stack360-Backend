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

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class DependentExport {

	public String build(String sortBy, boolean includeEmployee) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("Dependents", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		List<Short> notInCategoryTypes = new ArrayList<Short>();
		notInCategoryTypes.add(HrBenefitCategory.HEALTH);
		notInCategoryTypes.add(HrBenefitCategory.DENTAL);
		notInCategoryTypes.add(HrBenefitCategory.VISION);

		try {
			if (includeEmployee) {
				writer.writeField("Employee SSN");
				writer.writeField("Employee LName");
				writer.writeField("Employee FName");
			}
			writer.writeField("SSN");
			writer.writeField("LName");
			writer.writeField("FName");
			writer.writeField("MName");
			writer.writeField("Street 1");
			writer.writeField("Street 2");
			writer.writeField("City");
			writer.writeField("State");
			writer.writeField("Zip");
			writer.writeField("Home Phone");
			writer.writeField("Work Phone");
			writer.writeField("Cell Phone");
			writer.writeField("Medical 1");
			writer.writeField("Medical 1 Coverage Type");
			writer.writeField("Medical 1 Start");
			writer.writeField("Medical 2");
			writer.writeField("Medical 2 Coverage Type");
			writer.writeField("Medical 2 Start");
			writer.writeField("Dental ");
			writer.writeField("Dental Coverage Type");
			writer.writeField("Dental Start");
			writer.writeField("Vision ");
			writer.writeField("Vision Coverage Type");
			writer.writeField("Vision Start");
			writer.writeField("Other 1");
			writer.writeField("Other 1 Coverage Type");
			writer.writeField("Other 1 Start");
			writer.writeField("Other 1 Amount");
			writer.writeField("Other 2");
			writer.writeField("Other 2 Coverage Type");
			writer.writeField("Other 2 Start");
			writer.writeField("Other 2 Amount");
			writer.writeField("Other 3");
			writer.writeField("Other 3 Coverage Type");
			writer.writeField("Other 3 Start");
			writer.writeField("Other 3 Amount");
			writer.writeField("Other 4");
			writer.writeField("Other 4 Coverage Type");
			writer.writeField("Other 4 Start");
			writer.writeField("Other 4 Amount");
			writer.endRecord();

			HibernateSessionUtil hsu = ArahantSession.getHSU();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
				for (OrgGroupAssociation o : oga)
					ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
			}

			HibernateCriteriaUtil<HrEmplDependent> hcu = ArahantSession.getHSU().createCriteria(HrEmplDependent.class);
			if (sortBy.equals("D"))
				if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(HrEmplDependent.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(HrEmplDependent.EMPLOYEE).joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
				else
					hcu.joinTo(HrEmplDependent.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany());
			else
				if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(HrEmplDependent.EMPLOYEE).orderBy(Employee.LNAME).orderBy(Person.FNAME).joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
				else
					hcu.joinTo(HrEmplDependent.EMPLOYEE).orderBy(Employee.LNAME).orderBy(Person.FNAME).eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany());

			List<HrEmplDependent> dependentList = hcu.list();

			for (HrEmplDependent hed : dependentList) {
				BPerson dep = new BPerson(hed.getPerson());
				BEmployee emp = new BEmployee(hed.getEmployee());

				if (includeEmployee) {
					writer.writeField(emp.getSsn());
					writer.writeField(emp.getLastName());
					writer.writeField(emp.getFirstName());
				}

				writer.writeField(dep.getSsn());
				writer.writeField(dep.getLastName());
				writer.writeField(dep.getFirstName());
				writer.writeField(dep.getMiddleName());
				writer.writeField(dep.getStreet());
				writer.writeField(dep.getStreet2());
				writer.writeField(dep.getCity());
				writer.writeField(dep.getState());
				writer.writeField(dep.getZip());
				writer.writeField(dep.getHomePhone());
				writer.writeField(dep.getWorkPhone());
				writer.writeField(dep.getMobilePhone());



				List<HrBenefitJoin> medicalJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, dep.getPerson()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.COVERAGE_START_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH).list();
				if (medicalJoins.size() > 2)
					throw new ArahantException("Export only handles 2 approved Medical enrollments.  " + dep.getNameFML() + " has more than 2.");
				for (HrBenefitJoin depJoin : medicalJoins) {
					BHRBenefitJoin bdepJoin = new BHRBenefitJoin(depJoin);

					writer.writeField(bdepJoin.getBenefitName());
					writer.writeField(bdepJoin.getBenefitConfigName());
					writer.writeDate(bdepJoin.getCoverageStartDate());
				}
				for (int x = medicalJoins.size(); x < 2; x++) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
				}

				List<HrBenefitJoin> dentalJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, dep.getPerson()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.COVERAGE_START_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL).list();
				if (dentalJoins.size() > 1)
					throw new ArahantException("Export only handles 1 approved Dental enrollments.  " + dep.getNameFML() + " has more than 1.");
				for (HrBenefitJoin depJoin : dentalJoins) {
					BHRBenefitJoin bdepJoin = new BHRBenefitJoin(depJoin);

					writer.writeField(bdepJoin.getBenefitName());
					writer.writeField(bdepJoin.getBenefitConfigName());
					writer.writeDate(bdepJoin.getCoverageStartDate());
				}
				for (int x = dentalJoins.size(); x < 1; x++) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
				}

				List<HrBenefitJoin> visionJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, dep.getPerson()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.COVERAGE_START_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.VISION).list();
				if (dentalJoins.size() > 1)
					throw new ArahantException("Export only handles 1 approved Vision enrollment.  " + dep.getNameFML() + " has more than 1.");
				for (HrBenefitJoin depJoin : visionJoins) {
					BHRBenefitJoin bdepJoin = new BHRBenefitJoin(depJoin);

					writer.writeField(bdepJoin.getBenefitName());
					writer.writeField(bdepJoin.getBenefitConfigName());
					writer.writeDate(bdepJoin.getCoverageStartDate());
				}
				for (int x = visionJoins.size(); x < 1; x++) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
				}

				List<HrBenefitJoin> otherJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, dep.getPerson()).eq(HrBenefitJoin.APPROVED, 'Y').ne(HrBenefitJoin.COVERAGE_START_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).notIn(HrBenefitCategory.TYPE, notInCategoryTypes).list();
				if (dentalJoins.size() > 4)
					throw new ArahantException("Export only handles 4 approved Other enrollments.  " + dep.getNameFML() + " has more than 4.");
				for (HrBenefitJoin depJoin : otherJoins) {
					BHRBenefitJoin bdepJoin = new BHRBenefitJoin(depJoin);

					writer.writeField(bdepJoin.getBenefitName());
					writer.writeField(bdepJoin.getBenefitConfigName());
					writer.writeDate(bdepJoin.getCoverageStartDate());
					writer.writeField(bdepJoin.getAmountCovered());
				}
				for (int x = otherJoins.size(); x < 4; x++) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
				}
				writer.endRecord();
			}
		} finally {
			writer.close();
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String[] args) {

		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().createCriteria(CompanyDetail.class).eq(CompanyDetail.COMPANY_ID, "00001-0000072568").first());
		try {
			new DependentExport().build("D", false);
		} catch (Exception ex) {
			Logger.getLogger(DependentExport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
