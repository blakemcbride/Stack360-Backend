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
package com.arahant.groovy.exports

import com.arahant.beans.*
import com.arahant.business.BEmployee
import com.arahant.business.BHRBenefit
import com.arahant.business.BPerson
import com.arahant.lisp.ABCL
import com.arahant.utils.*
import com.arahant.business.BVendorCompany
import com.arahant.exceptions.ArahantException
import org.kissweb.DelimitedFileWriter


/**
 *
 * @author Blake McBride
 */
class NorthAmericaAdministratorsExport {

    private DelimitedFileWriter dfw
    private HibernateSessionUtil hsu
    private BVendorCompany vendor

    //  The following must match hr_benefit.internal_id
    private HrBenefit medicalBenefit		// Medical
    private HrBenefit dentalBenefit			// Dental
    private HrBenefit visionBenefit			// Vision
    private HrBenefit rxdBenefit			// RXD
    private HrBenefit flexMedicalBenefit	// Flex Medical
    private HrBenefit flexDependentBenefit	// Flex Dependent
    private LinkedList<HrBenefit> benefitList = new LinkedList<HrBenefit>() // list of applicable benefits
    private List<HrBenefitConfig> benefitConfigList // list of associated benefit configs


    public static main(String [] args) {
        FileSystemUtils.setWorkingDirectory("/Users/blake/NetBeansProjects-7.3/Arahant_NW/build/web")
        ABCL.init()
        println doExport()
    }

    public static String doExport() {
        return (new NorthAmericaAdministratorsExport()).export()
    }

    public String export() throws Exception {
        hsu = ArahantSession.getHSU()

        vendor = new BVendorCompany(VendorCompany.NORTH_AMERICA_ADMINISTRATORS)
        if (vendor.getBean() == null)
            throw new ArahantException("Vendor Company North America Administrators not found.")

        getBenefits(vendor)

        File csvFile = FileSystemUtils.createTempFile("NorthAmericaAdministrators-", ".csv")
        dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("yyyyMMdd")

        writeColumnHeader()
        spinEmployees()


        dfw.close()
        return FileSystemUtils.getHTTPPath(csvFile);
    }

    private static final String Medical = "MEDICAL"
    private static final String Dental = "DENTAL"
    private static final String Vision = "VISION"
    private static final String Rxd = "RXD"
    private static final String FlexMedical = "FLEX MEDICAL"
    private static final String FlexDependent = "FLEX DEPENDENT"

    private void getBenefits(BVendorCompany vend) {
        List<HrBenefit> benefits = BHRBenefit.listActiveByVendor(vend.getBean())
        if (benefits.size() == 0)
            throw new ArahantException("No benefits associated with Vendor Company North America Administrators found.")

        benefits.each {
            String id = it.getInternalId().toUpperCase()
            if (id == Medical)
                benefitList.add(medicalBenefit = it)
            else if (id == Dental)
                benefitList.add(dentalBenefit = it)
            else if (id == Vision)
                benefitList.add(visionBenefit = it)
            else if (id == Rxd)
                benefitList.add(rxdBenefit = it)
            else if (id == FlexMedical)
                benefitList.add(flexMedicalBenefit = it)
            else if (id == FlexDependent)
                benefitList.add(flexDependentBenefit = it)
        }
        if (benefitList.isEmpty())
            throw new ArahantException("No benefits found for vendor Company North America Administrators.")
        benefitConfigList = hsu.createCriteria(HrBenefitConfig.class).in(HrBenefitConfig.HR_BENEFIT, benefitList).leOrEq(HrBenefitConfig.END_DATE, DateUtils.today(), 0).list()
    }

    private void spinEmployees() {
		//  The following line is flagged as an error by NetBeans.  It is a bug in NetBeans.
		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.SSN).scroll()
        int n = 0
        Set<Person> members = new HashSet<Person>()

        while (true) {
            if (++n % 40 == 0)
                hsu.clear()
            if (!scr.next())
                break
            Employee emp = scr.get()
            BEmployee bemp = new BEmployee(emp)
            int today = DateUtils.today()

            List<HrBenefitJoin> bjs = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, emp)
                    .in(HrBenefitJoin.HR_BENEFIT_CONFIG, benefitConfigList).eq(HrBenefitJoin.APPROVED, (char) 'Y')
                    .eq(HrBenefitJoin.BENEFIT_DECLINED, (char) 'N').ltAndNeq(HrBenefitJoin.COVERAGE_START_DATE, today, 0).leOrEq(HrBenefitJoin.COVERAGE_END_DATE, today, 0).list()

             if (bjs.isEmpty())
                 continue

            members.clear()
            bjs.each {
                members.add it.getCoveredPerson()
            }

            // trace info
 //           println bemp.getLastName()

            members.each {
                writeRecord bemp, new BPerson(it), bjs
            }
        }
    }

    private static HrBenefitJoin findBenefitJoin(BPerson member, List<HrBenefitJoin> bjs, String id) {
        String memberId = member.getPersonId()
        id = id.toUpperCase()
        for (HrBenefitJoin bj : bjs)
            if (bj.getCoveredPersonId() == memberId) {
                String internalId = bj.getHrBenefitConfig().getHrBenefit().getInternalId()
                if (id == internalId?.toUpperCase())
                    return bj
            }
        return null
    }

    private void writeRecord(BEmployee employee, BPerson member, List<HrBenefitJoin> bjs) {
        writeDemographics employee, member, bjs
        writeMedical member, bjs
        writeDental member, bjs
        writeVision member, bjs
        writeRxd member, bjs
        writeFlexMedical employee, member, bjs
        writeFlexDependent employee, member, bjs
        dfw.endRecord()
    }

    private void writeDemographics(BEmployee employee, BPerson member, List<HrBenefitJoin> bjs) {
        dfw.writeField vendor.getAccountNumber()  // Group identifier

		dfw.writeField employee.getSsn().replaceAll("-", "") //  Participant ID

        if (member.getSsn() == "999-99-9999")
            dfw.writeField ""
        else
            dfw.writeField member.getSsn().replaceAll("-", "")

        switch (employee.getRelationshipType(member.getPerson())) {
            case 'E': dfw.writeField "Employee";     break;
            case 'S': dfw.writeField "Spouse";       break;
            case 'C': dfw.writeField "Child";        break;
            case 'O': dfw.writeField "Other";        break;
            case 'U': dfw.writeField "Other";        break;
        }
        dfw.writeField member.getLastName()
        dfw.writeField member.getFirstName()
        dfw.writeField member.getMiddleInitial()
        dfw.writeDate member.getDob()
        String sex = member.getSex()
        if (sex != "M"  &&  sex != "F")
            sex = ""
        dfw.writeField sex
        dfw.writeField ""  // Member marital status

        Set<Phone> phones = employee.getPhones()
        if (phones.isEmpty())
            dfw.writeField ""
        else
            dfw.writeField phones.getAt(0).getPhoneNumber()

        boolean isEmployee = member.getPersonId() == employee.getPersonId()
        if (isEmployee)
            dfw.writeField employee.getFirstActiveStatusHistory().getEffectiveDate()
        else
            dfw.writeField ""

        dfw.writeField employee.getStreet()
        dfw.writeField employee.getStreet2()
        dfw.writeField employee.getCity()
        dfw.writeField employee.getState()
        dfw.writeField employee.getZip()

        if (isEmployee)
            dfw.writeField employee.getSmoker() ? 'Y' : 'N'
        else
            dfw.writeField ""

        dfw.writeField ""  // optional Division
        dfw.writeField ""  // optional COBRA Flag
    }

    private void writeMedical(BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, Medical
        if (memberJoin != null) {
            dfw.writeDate memberJoin.getOriginalCoverageDate()
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanName()
        } else {
            dfw.writeField ""
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeDental(BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, Dental
        if (memberJoin != null) {
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
        } else {
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeVision(BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, Vision
        if (memberJoin != null) {
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
        } else {
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeRxd(BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, Rxd
        if (memberJoin != null) {
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
        } else {
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeFlexMedical(BEmployee employee, BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, FlexMedical
        if (memberJoin != null) {
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
            switch (employee.getPayPeriodsPerYear()) {
                case 52:  dfw.writeField "Weekly";          break;
                case 12:  dfw.writeField "Monthly";         break;
                case 24:  dfw.writeField "Semi-Monthly";    break;
                case 26:  dfw.writeField "Bi-Weekly";       break;
                default:  dfw.writeField "";                break;
            }
            dfw.writeField employee.getPayPeriodsPerYear()
            dfw.writeField memberJoin.getAmountPaid()
            dfw.writeField memberJoin.getAmountPaid() / employee.getPayPeriodsPerYear()
            dfw.writeDate memberJoin.getCoverageStartDate()
        } else {
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeFlexDependent(BEmployee employee, BPerson member, List<HrBenefitJoin> bjs) {
        HrBenefitJoin memberJoin = findBenefitJoin member, bjs, FlexDependent
        if (memberJoin != null) {
            dfw.writeField "Y"
            dfw.writeField memberJoin.getHrBenefitConfig().getHrBenefit().getPlanId()
            dfw.writeField memberJoin.getHrBenefitConfig().getAddInfo()
            dfw.writeDate memberJoin.getCoverageStartDate()
            dfw.writeDate memberJoin.getCoverageEndDate()
            switch (employee.getPayPeriodsPerYear()) {
                case 52:  dfw.writeField "Weekly";          break;
                case 12:  dfw.writeField "Monthly";         break;
                case 24:  dfw.writeField "Semi-Monthly";    break;
                case 26:  dfw.writeField "Bi-Weekly";       break;
                default:  dfw.writeField "";                break;
            }
            dfw.writeField employee.getPayPeriodsPerYear()
            dfw.writeField memberJoin.getAmountPaid()
            dfw.writeField memberJoin.getAmountPaid() / employee.getPayPeriodsPerYear()
            dfw.writeDate memberJoin.getCoverageStartDate()
        } else {
            dfw.writeField "N"
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
            dfw.writeField ""
        }
    }

    private void writeColumnHeader() {
        dfw.writeField "GROUP IDENTIFIER"
        dfw.writeField "PARTICIPANT ID"
        dfw.writeField "MEMBER SSN"
        dfw.writeField "MEMBER RELATIONSHIP"
        dfw.writeField "MEMBER LAST NAME"
        dfw.writeField "MEMBER FIRST NAME"
        dfw.writeField "MEMBER MI"
        dfw.writeField "MEMBER DOB"
        dfw.writeField "MEMBER GENDER"
        dfw.writeField "MEMBER MARITAL STATUS"
        dfw.writeField "EMPLOYEE PHONE"
        dfw.writeField "EMPLOYEE HIRE DATE"
        dfw.writeField "ADDRESS1"
        dfw.writeField "ADDRESS2"
        dfw.writeField "CITY"
        dfw.writeField "STATE"
        dfw.writeField "ZIP"
        dfw.writeField "SMOKER"
        dfw.writeField "DIVISION"
        dfw.writeField "COBRA FLAG"
        dfw.writeField "ORIGINAL_MEDICAL_EFFECTIVE_DATE"
        dfw.writeField "MED COVERAGE FLAG"
        dfw.writeField "MED PLAN"
        dfw.writeField "MED ENROLLMENT"
        dfw.writeField "MED EFFECTIVE DATE"
        dfw.writeField "MED TERM DATE"
        dfw.writeField "PPO Network"
        dfw.writeField "DEN COVERAGE FLAG"
        dfw.writeField "DEN PLAN"
        dfw.writeField "DEN ENROLLMENT"
        dfw.writeField "DEN EFFECTIVE DATE"
        dfw.writeField "DEN TERM DATE"
        dfw.writeField "VIS COVERAGE FLAG"
        dfw.writeField "VIS PLAN"
        dfw.writeField "VIS ENROLLMENT"
        dfw.writeField "VIS EFFECTIVE DATE"
        dfw.writeField "VIS TERM DATE"
        dfw.writeField "RXD COVERAGE FLAG"
        dfw.writeField "RXD PLAN"
        dfw.writeField "RXD ENROLLMENT"
        dfw.writeField "RXD EFFECTIVE DATE"
        dfw.writeField "RXD TERM DATE"
        dfw.writeField "FLEX MEDICAL COVERAGE FLAG"
        dfw.writeField "FLEX MEDICAL PLAN"
        dfw.writeField "FLEX MEDICAL ENROLLMENT"
        dfw.writeField "FLEX MEDICAL EFFECTIVE DATE"
        dfw.writeField "FLEX MEDICAL TERM DATE"
        dfw.writeField "FLEX MEDICAL PAYROLL SCHEDULE"
        dfw.writeField "FLEX MEDICAL NUMBER OF DEDUCTIONS"
        dfw.writeField "FLEX MEDICAL ANNUAL ELECTION AMOUNT"
        dfw.writeField "FLEX MEDICAL PER PAY PERIOD DEDUCTION"
        dfw.writeField "FLEX MEDICAL FIRST DEDUCTION DATE"
        dfw.writeField "FLEX DEPENDENT CARE COVERAGE FLAG"
        dfw.writeField "FLEX DEPENDENT CARE PLAN"
        dfw.writeField "FLEX DEPENDENT CARE ENROLLMENT"
        dfw.writeField "FLEX DEPENDENT EFFECTIVE DATE"
        dfw.writeField "FLEX DEPENDENT CARETERM DATE"
        dfw.writeField "FLEX DEPENDENT CARE PR MODE"
        dfw.writeField "FLEX DEPENDENT CARE NUMBER OF DEDUCTIONS"
        dfw.writeField "FLEX DEPENDENT CARE ANNUAL ELECTION AMOUNT"
        dfw.writeField "FLEX DEPENDENT CARE PER PAY PERIOD DEDUCTION"
        dfw.writeField "FLEX DEPENDENT CARE FIRST DEDUCTION DATE"
        dfw.endRecord()
    }

}

