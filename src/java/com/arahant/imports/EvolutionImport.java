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


package com.arahant.imports;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvolutionImport {
	
	private HibernateSessionUtil hsu;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

	private int parseDate(String dt) {
		try {
			return DateUtils.getDate(sdf.parse(dt));
		} catch (Exception e) {
//			System.out.println("Failed to parse "+dt);
//			e.printStackTrace();
			return 0;
		}
	}

	public void importEarnings(String filename) throws Exception {

		DelimitedFileReader fr = new DelimitedFileReader(filename);

		//skip header line
		fr.nextLine();

		int count = 0;
		while (fr.nextLine()) {

			if (++count % 50 == 0) {
				System.out.println(count);
				hsu.commitTransaction();
				hsu.beginTransaction();
			}

			String empid = fr.nextString();
			String ssn = fr.nextString();

			if ("".equals(ssn))
				continue;

			String edCode = fr.nextString();
			String desc = fr.nextString();
			int percent = fr.nextInt();
			double amount = fr.nextDouble();
			String abaNumber = fr.nextString();
			String bankAccount = fr.nextString();
			String prenote = fr.nextString();
			String bankAccountType = fr.nextString();
			double targetAmount = fr.nextDouble();
			double balance = fr.nextDouble();
			int startDate = parseDate(fr.nextString());
			int endDate = parseDate(fr.nextString());
			String frequency = fr.nextString();
			String garnishmentId = fr.nextString();
			String customCase = fr.nextString();
			//String num=fr.nextString();
			String fips = fr.nextString();
			String name = fr.nextString();
			String street1 = fr.nextString();
			String street2 = fr.nextString();
			String city = fr.nextString();
			String state = fr.nextString();
			String zip = fr.nextString();

			//does person exist
			Employee p = hsu.createCriteria(Employee.class).eq(Employee.SSN, ssn).first();

			if (p == null)
				throw new ArahantWarning("Person with SSN " + ssn + " does not exist.");


			if (edCode.trim().equals("DNP") || edCode.equals("D98") || edCode.equals("D99") || edCode.equals("D96") || edCode.equals("D95")) //direct deposit, make eft record
			{
				WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PAYROLL_CODE, edCode).first();

				if (wt == null) {
					BWageType bwt = new BWageType();
					bwt.create();
					bwt.setIsDeduction(false);
					bwt.setName(desc);
					bwt.setPeriodType(WageType.PERIOD_ONE_TIME);
					bwt.setType(WageType.TYPE_DIRECT_DEPOSIT);
					bwt.setPayrollInterfaceCode(edCode);
					bwt.insert();
					wt = bwt.getBean();
				}

				ElectronicFundTransfer eft = hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, p).eq(ElectronicFundTransfer.BANK_ACCOUNT, bankAccount).eq(ElectronicFundTransfer.WAGE_TYPE, wt).first();

				BElectronicFundTransfer beft;
				boolean insert = false;

				if (eft == null) {
					beft = new BElectronicFundTransfer();
					beft.create();

					beft.setSeq((short) hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, p).count());
				} else
					beft = new BElectronicFundTransfer(eft);

				if (bankAccount.length() > 14)
					System.out.println("'" + bankAccount + "'");
				beft.setAccountNumber(bankAccount);
				beft.setPerson(new BPerson(p));
				beft.setRoutingTransitNumber(abaNumber);
				beft.setAccountType(bankAccountType);
				beft.setWageType(wt);

				if (percent == 0 && amount == 0) {
					beft.setAmount(100);
					beft.setAmountType(ElectronicFundTransfer.TYPE_PERCENTAGE + "");
				} else if (percent != 0) {
					beft.setAmount(percent);
					beft.setAmountType(ElectronicFundTransfer.TYPE_PERCENTAGE + "");
				} else {
					beft.setAmount(amount);
					beft.setAmountType(ElectronicFundTransfer.TYPE_FIXED + "");
				}

				if (insert)
					beft.insert();
				else
					beft.update();

				//if total % >100, look for largest % and cut it down
				int total = hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, p).eq(ElectronicFundTransfer.AMOUNT_TYPE, ElectronicFundTransfer.TYPE_PERCENTAGE).sum(ElectronicFundTransfer.AMOUNT).intValue();

				if (total > 100) {
					int dif = total - 100;

					while (dif > 0) {

						float top = hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, p).eq(ElectronicFundTransfer.AMOUNT_TYPE, ElectronicFundTransfer.TYPE_PERCENTAGE).max(ElectronicFundTransfer.AMOUNT).floatVal();

						eft = hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, p).eq(ElectronicFundTransfer.AMOUNT_TYPE, ElectronicFundTransfer.TYPE_PERCENTAGE).eq(ElectronicFundTransfer.AMOUNT, top).first();

						if (eft == null)
							throw new ArahantException("Can't fine EFT to reduce for " + ssn);

						if (eft.getAmount() <= dif) {
							dif -= eft.getAmount();
							eft.setAmount(0);
						} else {
							eft.setAmount(eft.getAmount() - dif);
							dif = 0;
						}

						hsu.saveOrUpdate(eft);

					}
				}
			} else if (edCode.equals("D61") || edCode.equals("D60")) {  //garnishment
				BGarnishmentType type;

				WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PAYROLL_CODE, edCode).first();

				if (wt == null) {
					BWageType bwt = new BWageType();
					bwt.create();
					bwt.setIsDeduction(true);
					bwt.setName(desc);
					bwt.setPeriodType(WageType.PERIOD_ONE_TIME);
					bwt.setType(WageType.TYPE_DEDUCTION);
					bwt.setPayrollInterfaceCode(edCode);
					bwt.insert();
					wt = bwt.getBean();
				}

				GarnishmentType t = hsu.createCriteria(GarnishmentType.class).eq(GarnishmentType.WAGE_TYPE, wt).first();

				if (t != null)
					type = new BGarnishmentType(t);
				else {
					type = new BGarnishmentType();
					type.create();
					type.setWageType(wt);
					type.setDescription(desc);
					type.insert();
				}

				if (!hsu.createCriteria(Garnishment.class).eq(Garnishment.EMPLOYEE, p).eq(Garnishment.GARNISHMENT_TYPE, type.getBean()).eq(Garnishment.DOCKET, garnishmentId).exists()) {
					BGarnishment garn = new BGarnishment();
					garn.create();
					garn.setDocketNumber(garnishmentId);
					garn.setGarnishmentType(type.getId());
					garn.setAmount(amount, "F");
					garn.setFipsCode(fips);
					garn.setCollectingState(state);
					garn.setFinalDate(endDate);
					garn.setStartDate(startDate);
					garn.setIssueState(state);
					garn.setPersonId(p.getPersonId());
					garn.setRemitToName(name);
					garn.setRemitToCity(city);
					garn.setRemitToCountry("");
					garn.setRemitToCounty("");
					garn.setRemitToStreet(street1);
					garn.setRemitToStreet2(street2);
					garn.setRemitToState(state);
					garn.setRemitToZip(zip);
					garn.setType("N");
					//set the seq
					garn.setSequence(hsu.createCriteria(Garnishment.class).eq(Garnishment.EMPLOYEE, p).count());
					garn.insert();
				}
			} else  {  //find this bene
				WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PAYROLL_CODE, edCode).first();

				if (wt == null) {
					BWageType bwt = new BWageType();
					bwt.create();
					bwt.setIsDeduction(true);
					bwt.setName(desc);
					bwt.setPeriodType(WageType.PERIOD_ONE_TIME);
					bwt.setType(WageType.TYPE_DEDUCTION);
					bwt.setPayrollInterfaceCode(edCode);
					bwt.insert();
					wt = bwt.getBean();
				}

				HrBenefitConfig c = hsu.createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.WAGE_TYPE, wt).first();

				BHRBenefitConfig cfg;

				if (c == null) {
					short type = HrBenefitCategory.VOLUNTARY;

					if (desc.toLowerCase().indexOf("medical") != -1)
						type = HrBenefitCategory.HEALTH;

					if (desc.toLowerCase().indexOf("dental") != -1)
						type = HrBenefitCategory.DENTAL;

					if (desc.toLowerCase().indexOf("fsa") != -1)
						type = HrBenefitCategory.FLEX_TYPE;

					if (desc.toLowerCase().indexOf("vision") != -1)
						type = HrBenefitCategory.VISION;

					String catid;

					HrBenefitCategory cat = hsu.createCriteria(HrBenefitCategory.class).eq(HrBenefitCategory.TYPE, type).first();
					if (cat == null) {
						BHRBenefitCategory category = new BHRBenefitCategory();
						catid = category.create();
						category.setDescription(type != HrBenefitCategory.VOLUNTARY ? desc : "Voluntary");
						category.setTypeId(type);
						category.setAllowsMultipleBenefits(true);
						category.insert();
					} else
						catid = cat.getBenefitCatId();

					BHRBenefit bene = new BHRBenefit();
					bene.create();
					bene.setName(desc);
					bene.setInsuranceCode(edCode);
					bene.setBenefitCategoryId(catid);
					bene.setEmployeeIsProvider(true);
					bene.setWageType(wt);
					bene.insert();

					cfg = new BHRBenefitConfig();
					cfg.create();
					cfg.setCoversEmployee(true);
					cfg.setName(desc);
					cfg.setBenefitId(bene.getId());
					cfg.insert();
				} else
					cfg = new BHRBenefitConfig(c);

				if (!hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.POLICY_START_DATE, startDate).eq(HrBenefitJoin.PAYING_PERSON, p).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, cfg.getBean()).exists()) {
					System.out.println(count + " " + ssn);
					BHRBenefitJoin j = new BHRBenefitJoin();
					j.create();
					j.setHrBenefitConfig(cfg.getBean());
					j.setPayingPerson(p);
					j.setCoveredPerson(p);
					j.setPolicyStartDate(startDate);
					j.setPolicyEndDate(endDate);
					j.setCoverageStartDate(startDate);
					j.setCoverageEndDate(endDate);
					j.setAmountPaid(amount * 24);
					j.setAmountPaidType("F");
					j.setUseAmountOverride(true);
					j.setChangeDescription("Import");
					j.insert();
				}
			}
		}
	}

	public void importDemographics(String filename) throws FileNotFoundException, IOException, Exception {
		DelimitedFileReader fr = new DelimitedFileReader(filename);

		//skip header line
		fr.nextLine();

		int count = 0;
		while (fr.nextLine()) {

			if (++count % 50 == 0) {
				System.out.println(count);
				hsu.commitTransaction();
				hsu.beginTransaction();
			}

			//skip blank rows
			if (fr.getString(0) == null || fr.getString(0).trim().equals(""))
				continue;

			String empid = fr.nextString();
			String ssn = fr.nextString();
			String fname = fr.nextString();
			String mname = fr.nextString();
			String lname = fr.nextString();
			String street1 = fr.nextString();
			String street2 = fr.nextString();
			String city = fr.nextString();
			String state = fr.nextString();
			String zip = fr.nextString();
			String phone1 = fr.nextString();
			String currentStatus = fr.nextString();
			String originalHireDate = fr.nextString();
			String currentHireDate = fr.nextString();
			String currentTermDate = fr.nextString();
			String birthday = fr.nextString();
			String gender = fr.nextString();
			String ethnicity = fr.nextString();
			String positionStatus = fr.nextString();
			String payFrequency = fr.nextString();
			double stdHours = fr.nextDouble();
			String fedMarital = fr.nextString();
			String salary = fr.nextString();
			String rateAmount = fr.nextString();
			String timeClock = fr.nextString();
			String taxState = fr.nextString();
			String taxCode = fr.nextString();
			String eic = fr.nextString();
			String overrideFedTax = fr.nextString();
			String overrideFedTaxValue = fr.nextString();
			String overrideStateTax = fr.nextString();
			String overrideStateTaxValue = fr.nextString();
			int deps = fr.nextInt();
			int stateDeps = fr.nextInt();
			String taxName = fr.nextString();


			//new fields
			String department = fr.nextString();
			String wci = fr.nextString();
			String position = fr.nextString();
			String eeoCatName = fr.nextString();


			//does person exist
			Employee p = hsu.createCriteria(Employee.class).eq(Employee.SSN, ssn).first();

			BEmployee bp;
			boolean insert = p == null;
			if (insert) {
				bp = new BEmployee();
				bp.create();
			} else
				bp = new BEmployee(p);

			if (ethnicity.equals("C"))
				bp.setEEORaceId("00000-0000000000");
			bp.setExpectedHoursPerPayPeriod(stdHours);
			bp.setExtRef(empid.trim());
			bp.setSsn(ssn);
			bp.setFirstName(fname);
			bp.setLastName(lname);
			bp.setMiddleName(mname);
			bp.setStreet(street1);
			bp.setStreet2(street2);
			bp.setCity(city);
			bp.setState(state);
			bp.setZip(zip);
			bp.setWorkPhone(phone1);
			bp.setDob(parseDate(birthday));
			bp.setSex(gender);
			bp.setMaritalStatus(fedMarital);
			bp.setJobTitle(position);
			bp.setWorkersCompCode(wci);
			bp.setW4Status(fedMarital.charAt(0));

			//EEO Job Category
			if (!"".equals(eeoCatName)) {
				HrEeoCategory ec = hsu.createCriteria(HrEeoCategory.class).eq(HrEeoCategory.NAME, eeoCatName).first();

				if (ec == null) {
					BHREEOCategory eeoCat = new BHREEOCategory();
					eeoCat.create();
					eeoCat.setName(eeoCatName);
					eeoCat.insert();
					ec = eeoCat.getBean();
				}

				bp.setEEOCategoryId(ec.getEeoCategoryId());
			}


			String wageId;
			if (salary == null || salary.trim().equals(""))
				salary = "0";

			if (Double.parseDouble(salary) > 0.01) {
				wageId = hsu.createCriteria(WageType.class).selectFields(WageType.ID).eq(WageType.PERIOD_TYPE, WageType.PERIOD_SALARY).stringVal();

				if (wageId.equals("")) {
					BWageType sal = new BWageType();
					wageId = sal.create();
					sal.setIsDeduction(false);
					sal.setName("Salary");
					sal.setPayrollInterfaceCode("");
					sal.setPeriodType(WageType.PERIOD_SALARY);
					sal.setType(WageType.TYPE_REGULAR);
					sal.insert();
					//throw new ArahantException("Please set up wage type for salary.");
				}
			} else {
				wageId = hsu.createCriteria(WageType.class).selectFields(WageType.ID).eq(WageType.PERIOD_TYPE, WageType.PERIOD_HOURLY).stringVal();

				if (wageId.equals("")) {
					BWageType sal = new BWageType();
					wageId = sal.create();
					sal.setIsDeduction(false);
					sal.setName("Hourly");
					sal.setPayrollInterfaceCode("");
					sal.setPeriodType(WageType.PERIOD_HOURLY);
					sal.setType(WageType.TYPE_REGULAR);
					sal.insert();
					//throw new ArahantException("Please set up wage type for hourly.");
				}
			}

			bp.setTaxState(taxState);
			if (eic.equals("N"))
				bp.setEarnedIncomeCreditStatus(" ");
			else
				bp.setEarnedIncomeCreditStatus("I");

			if ("Additional Amount".equals(overrideFedTax)) {
				bp.setAddFederalIncomeTaxAmount(Double.parseDouble(overrideFedTaxValue));
				bp.setAddFederalIncomeTaxType(Employee.TYPE_ADDITIONAL_AMOUNT + "");
			} else {
				bp.setAddFederalIncomeTaxType("N");
				bp.setAddFederalIncomeTaxAmount(0);
			}

			bp.setFederalExemptions(deps);
			bp.setStateExemptions(stateDeps);



			//		String currentStatus=fr.nextString();

			//		String currentHireDate=fr.nextString();
			//		String currentTermDate=fr.nextString();


			//		String ethnicity=fr.nextString();
			//		String positionStatus=fr.nextString();



			if (insert)
				bp.insert();
			else
				bp.update();


			if (!"".equals(department)) {
				//assign them to an org group
				OrgGroup og = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.EXTERNAL_REF, department.trim()).first();

				if (og == null) //group not found, add it
				{
					BOrgGroup borg = new BOrgGroup();
					borg.create();
					borg.setCompanyId(bp.getCompany().getCompanyId());
					borg.setName(department.trim());
					borg.setExternalId(department.trim());
					borg.setOrgGroupType(ArahantConstants.COMPANY_TYPE);
					borg.insert();
					borg.setParent(bp.getCompany().getOrgGroupId());
					og = borg.getOrgGroup();
				}

				bp.assignToSingleOrgGroup(og.getOrgGroupId(), false);
			}


			if (!hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, bp.getPerson()).exists())
				bp.assignToOrgGroup(bp.getCompany().getOrgGroupId(), false);



			double wages;

			if (Double.parseDouble(salary) > .01)
				try {
					wages = Double.parseDouble(salary) * 24;
				} catch (Exception e) {
					wages = 0;
				}
			else
				try {
					wages = Double.parseDouble(rateAmount);
				} catch (Exception e) {
					wages = 0;
				}

			bp.setWageAndPosition(null, wageId, wages, parseDate(currentHireDate));

			if (insert) {
				String statId = BProperty.get(StandardProperty.DEFAULT_EMP_STAT_ID);

				if (statId == null || statId.equals(""))
					throw new ArahantException("Please configure system property " + StandardProperty.DEFAULT_EMP_STAT_ID);

				BHREmplStatusHistory hist = new BHREmplStatusHistory();
				hist.create();
				hist.setEmployee(bp.getEmployee());
				hist.setStatusId(statId);
				hist.setNotes("From import");
				hist.setEffectiveDate(parseDate(currentHireDate));
				hist.insert();

			}

			if (insert && currentStatus.equals("Terminated")) {
				String statId = hsu.createCriteria(HrEmployeeStatus.class).selectFields(HrEmployeeStatus.STATUSID).eq(HrEmployeeStatus.ACTIVE, 'N').orderBy(HrEmployeeStatus.STATUSID).stringVal();

				BHREmplStatusHistory hist = new BHREmplStatusHistory();
				hist.create();
				hist.setEmployee(bp.getEmployee());
				hist.setStatusId(statId);
				hist.setNotes("From import");

				int tdate = parseDate(currentTermDate);
				if (currentTermDate == null || currentTermDate.equals(""))
					tdate = DateUtils.addDays(parseDate(currentHireDate), 1);

				hist.setEffectiveDate(tdate);
				hist.insert();
			}

		}

	}
	
	public static void main(String args[]) {
		EvolutionImport imp = new EvolutionImport();
		try {

			imp.hsu = ArahantSession.getHSU();
			imp.hsu.dontAIIntegrate();
			imp.hsu.setCurrentPersonToArahant();

			imp.importDemographics("/Users/Arahant/girlscouts/moredata/demog.csv");
			imp.importEarnings("/Users/Arahant/girlscouts/moredata/earnings.csv");
			imp.hsu.commitTransaction();
		} catch (Exception ex) {
			imp.hsu.rollbackTransaction();
			ex.printStackTrace();
			Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
