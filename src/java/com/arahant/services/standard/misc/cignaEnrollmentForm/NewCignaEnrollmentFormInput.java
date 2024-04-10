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
 */
package com.arahant.services.standard.misc.cignaEnrollmentForm;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BEmployee;
import com.arahant.annotation.Validation;
import com.arahant.beans.Person;
import com.arahant.business.BPerson;

public class NewCignaEnrollmentFormInput extends TransmitInputBase {

    void setData(BEmployee be) {


        be.setLastName(employeeLastName);
        be.setFirstName(employeeFirstName);
        be.setMiddleName(employeeMiddleName);
        be.setSsn(employeeSSN);
        be.setSex(employeeSex);
        be.setStreet(employeeAddress);
        be.setCity(employeeCity);
        be.setState(employeeState);
        be.setZip(employeeZip);
        be.setPersonalEmail(employeeEmail);
        be.setHomePhone(employeePhone);
        be.setWorkPhone(employeeWorkPhone);
        be.setExtRef(employeeExternalId);

        //be.setNumberDependents(numberDependents);

        Person person = new Person();

        if (spouseFirstName.length() > 0) {
            //Set Spouse
            BPerson spouse = new BPerson(person.generateId());
            spouse.setFirstName(spouseFirstName);
            spouse.setMiddleName(spouseMiddleName);
            spouse.setLastName(spouseLastName);
            spouse.setDob(spouseDOB);
            spouse.setSex(spouseSex);
        }

        if (child1FirstName.length() > 0) {
            //Set Child 1
            BPerson child1 = new BPerson(person.generateId());
            child1.setFirstName(child1FirstName);
            child1.setMiddleName(child1MiddleName);
            child1.setLastName(child1LastName);
            child1.setDob(child1DOB);
            child1.setSex(child1Sex);
            child1.setStudent(child1Student);
            child1.setSsn(child1SSN);
        }


        if (child2FirstName.length() > 0) {
            //Set Child 2
            BPerson child2 = new BPerson(person.generateId());
            child2.setFirstName(child2FirstName);
            child2.setMiddleName(child2MiddleName);
            child2.setLastName(child2LastName);
            child2.setDob(child2DOB);
            child2.setSex(child2Sex);
            child2.setStudent(child2Student);
            child2.setSsn(child2SSN);
        }


        if (child3FirstName.length() > 0) {
            //Set Child 3
            BPerson child3 = new BPerson(person.generateId());
            child3.setFirstName(child3FirstName);
            child3.setMiddleName(child3MiddleName);
            child3.setLastName(child3LastName);
            child3.setDob(child3DOB);
            child3.setSex(child3Sex);
            child3.setStudent(child3Student);
            child3.setSsn(child3SSN);
        }


        if (child4FirstName.length() > 0) {
            //Set Child 4
            BPerson child4 = new BPerson(person.generateId());
            child4.setFirstName(child4FirstName);
            child4.setMiddleName(child4MiddleName);
            child4.setLastName(child4LastName);
            child4.setDob(child4DOB);
            child4.setSex(child4Sex);
            child4.setStudent(child4Student);
            child4.setSsn(child4SSN);
        }

        /*
        be.setSpousePCPHCCChoice(spousePCPHCCChoice);
        be.setChild1PCPHCCChoice(child1PCPHCCChoice);
        be.setChild2PCPHCCChoice(child2PCPHCCChoice);
        be.setChild3PCPHCCChoice(child3PCPHCCChoice);
        be.setChild4PCPHCCChoice(child4PCPHCCChoice);
        be.setSpouseExistingPatient(spouseExistingPatient);
        be.setSpouseDentalOfficeNumber(spouseDentalOfficeNumber);
        be.setSpouseDentalOfficeNumber(spouseDentalOfficeNumber);
        be.setChild1ExistingPatient(child1ExistingPatient);
        be.setChild1DentalOfficeNumber1(child1DentalOfficeNumber1);
        be.setChild1DentalOfficeNumber2(child1DentalOfficeNumber2);
        be.setChild2ExistingPatient(child2ExistingPatient);
        be.setChild2DentalOfficeNumber1(child2DentalOfficeNumber1);
        be.setChild2DentalOfficeNumber2(child2DentalOfficeNumber2);
        be.setChild3ExistingPatient(child3ExistingPatient);
        be.setChild3DentalOfficeNumber1(child3DentalOfficeNumber1);
        be.setChild3DentalOfficeNumber2(child3DentalOfficeNumber2);
        be.setChild4ExistingPatient(child4ExistingPatient);
        be.setChild4DentalOfficeNumber1(child4DentalOfficeNumber1);
        be.setChild4DentalOfficeNumber2(child4DentalOfficeNumber2);
        be.setPointOfService(pointOfService);
        be.setHMO(HMO);
        be.setNetwork(network);
        be.setPointOfServiceOpen(pointOfServiceOpen);
        be.setHMOOpen(HMOOpen);
        be.setNetworkOpen(networkOpen);
        be.setOpenAccessPlus(openAccessPlus);
        be.setOpenAccessPlusInNetwork(openAccessPlusInNetwork);
        be.setPPO(PPO);
        be.setNetworkPPOEPO(networkPPOEPO);
        be.setPPA(PPA);
        be.setMedicalIndemnity(medicalIndemnity);
        be.setWriteIn(writeIn);
        be.setWriteInText(writeInText);
        be.setHRA(HRA);
        be.setHSA(HSA);
        be.setPharmacyHRA(pharmacyHRA);
        be.setDentalHRA(dentalHRA);
        be.setWithOpenAccessPlus(withOpenAccessPlus);
        be.setWithOpenAccessPlusNetwork(withOpenAccessPlusNetwork);
        be.setWithEPO(withEPO);
        be.setWithIndemnity(withIndemnity);
        be.setCIGNACareNetwork(CIGNACareNetwork);
        be.setDeclineCoverage(declineCoverage);
        be.setApplicableOption(applicableOption);
        be.setFlexibleSpendingHealthCare(flexibleSpendingHealthCare);
        be.setFlexibleSpendingDependentDayCare(flexibleSpendingDependentDayCare);
        be.setFlexibleSpendingDeclineCoverage(flexibleSpendingDeclineCoverage);
        be.setCIGNADentalCare(CIGNADentalCare);
        be.setDentalPPO(dentalPPO);
        be.setDentalEPO(dentalEPO);
        be.setDentalIndemnity(dentalIndemnity);
        be.setDentalCoverage(dentalCoverage);
        */

        /*
        .set(otherInsurance);
        .set(person1Name);
        .set(person1SSN);
        .set(person1Date);
        .set(person1A);
        .set(person1B);
        .set(person1Med);
        .set(person1Other);
        .set(person2Name);
        .set(person2SSN);
        .set(person2Date);
        .set(person2A);
        .set(person2B);
        .set(person2Med);
        .set(person2Other);
        .set(life);
        .set(lifeValue);
        .set(additionalLife);
        .set(additionalLifeValue);
        .set(dependentLifeSpouse);
        .set(dependentLifeSpouseValue);
        .set(dependendLifeChild);
        .set(dependendLifeChildValue);
        .set(adnd);
        .set(adndValue);
        .set(additionalAdnd);
        .set(additionalAdndValue);
        .set(std);
        .set(stdValue);
        .set(ltd);
        .set(ltdValue);
        .set(declineLife);
        .set(declineAdnd);
        .set(declineStd);
        .set(declineLtd);
        .set(cignaAcctNo);
        .set(networkId);
        .set(employeeGroupCustomerNo);
        .set(employeeDivision);
        .set(employeeClass);
        .set(employeeDeptCode);
        .set(employerAddress);
        .set(employerCity);
        .set(employerState);
        .set(employerZip);
        .set(employeeDateOfHire);
        .set(enrollmentReason);
        .set(enrollmentReasonDate);
        .set(cignaAnnualAmt);
        .set(medicalBenOption);
        .set(dentalBenOption);
        .set(addDependents);
        .set(addDependentsDate);
        .set(cancelEmployee);
        */

    }

    @Validation(required = true)
    private String employeeLastName;
    @Validation(required = true)
    private String employeeFirstName;
    @Validation(required = true)
    private String employeeMiddleName;
    @Validation(required = true)
    private String employeeSSN;
    @Validation(required = true)
    private String employeeSex;
    @Validation(required = true)
    private String employeeAddress;
    @Validation(required = true)
    private String employeeCity;
    @Validation(required = true)
    private String employeeState;
    @Validation(required = true)
    private String employeeZip;
    @Validation(required = true)
    private String employeeEmail;
    @Validation(required = true)
    private String employeePhone;
    @Validation(required = true)
    private int numberDependents;
    @Validation(required = false)
    private String spouseFirstName;
    @Validation(required = false)
    private String spouseMiddleName;
    @Validation(required = false)
    private String spouseLastName;
    @Validation(type="date",required = false)
    private int spouseDOB;
    @Validation(required = false)
    private String spouseSex;
    @Validation(required = false)
    private String child1FirstName;
    @Validation(required = false)
    private String child1MiddleName;
    @Validation(required = false)
    private String child1LastName;
    @Validation(type="date",required = false)
    private int child1DOB;
    @Validation(required = false)
    private String child1Sex;
    @Validation(required = false)
    private boolean child1Student;
    @Validation(required = false)
    private String child2FirstName;
    @Validation(required = false)
    private String child2MiddleName;
    @Validation(required = false)
    private String child2LastName;
    @Validation(type="date",required = false)
    private int child2DOB;
    @Validation(required = false)
    private String child2Sex;
    @Validation(required = false)
    private boolean child2Student;
    @Validation(required = false)
    private String child3FirstName;
    @Validation(required = false)
    private String child3MiddleName;
    @Validation(required = false)
    private String child3LastName;
    @Validation(type="date",required = false)
    private int child3DOB;
    @Validation(required = false)
    private String child3Sex;
    @Validation(required = false)
    private boolean child3Student;
    @Validation(required = false)
    private String child4FirstName;
    @Validation(required = false)
    private String child4MiddleName;
    @Validation(required = false)
    private String child4LastName;
    @Validation(type="date",required = false)
    private int child4DOB;
    @Validation(required = false)
    private String child4Sex;
    @Validation(required = false)
    private boolean child4Student;
    @Validation(required = true)
    private String employeeWorkPhone;
    @Validation(required = true)
    private String employeeExternalId;
    @Validation(required = false)
    private String child1SSN;
    @Validation(required = false)
    private String child2SSN;
    @Validation(required = false)
    private String child3SSN;
    @Validation(required = false)
    private String child4SSN;
    @Validation(required = false)
    private String spousePCPHCCChoice;
    @Validation(required = false)
    private String child1PCPHCCChoice;
    @Validation(required = false)
    private String child2PCPHCCChoice;
    @Validation(required = false)
    private String child3PCPHCCChoice;
    @Validation(required = false)
    private String child4PCPHCCChoice;
    @Validation(required = false)
    private boolean spouseExistingPatient;
    @Validation(required = false)
    private String spouseDentalOfficeNumber;
    @Validation(required = false)
    private boolean child1ExistingPatient;
    @Validation(required = false)
    private String child1DentalOfficeNumber1;
    @Validation(required = false)
    private String child1DentalOfficeNumber2;
    @Validation(required = false)
    private boolean child2ExistingPatient;
    @Validation(required = false)
    private String child2DentalOfficeNumber1;
    @Validation(required = false)
    private String child2DentalOfficeNumber2;
    @Validation(required = false)
    private boolean child3ExistingPatient;
    @Validation(required = false)
    private String child3DentalOfficeNumber1;
    @Validation(required = false)
    private String child3DentalOfficeNumber2;
    @Validation(required = false)
    private boolean child4ExistingPatient;
    @Validation(required = false)
    private String child4DentalOfficeNumber1;
    @Validation(required = false)
    private String child4DentalOfficeNumber2;
    @Validation(required = false)
    private boolean pointOfService;
    @Validation(required = false)
    private boolean HMO;
    @Validation(required = false)
    private boolean network;
    @Validation(required = false)
    private boolean pointOfServiceOpen;
    @Validation(required = false)
    private boolean HMOOpen;
    @Validation(required = false)
    private boolean networkOpen;
    @Validation(required = false)
    private boolean openAccessPlus;
    @Validation(required = false)
    private boolean openAccessPlusInNetwork;
    @Validation(required = false)
    private boolean PPO;
    @Validation(required = false)
    private boolean networkPPOEPO;
    @Validation(required = false)
    private boolean PPA;
    @Validation(required = false)
    private boolean medicalIndemnity;
    @Validation(required = false)
    private boolean writeIn;
    @Validation(required = false)
    private String writeInText;
    @Validation(required = false)
    private boolean HRA;
    @Validation(required = false)
    private boolean HSA;
    @Validation(required = false)
    private boolean pharmacyHRA;
    @Validation(required = false)
    private boolean dentalHRA;
    @Validation(required = false)
    private boolean withOpenAccessPlus;
    @Validation(required = false)
    private boolean withOpenAccessPlusNetwork;
    @Validation(required = false)
    private boolean withEPO;
    @Validation(required = false)
    private boolean withIndemnity;
    @Validation(required = false)
    private boolean CIGNACareNetwork;
    @Validation(required = false)
    private boolean declineCoverage;
    @Validation(required = false)
    private String applicableOption;
    @Validation(required = false)
    private boolean flexibleSpendingHealthCare;
    @Validation(required = false)
    private boolean flexibleSpendingDependentDayCare;
    @Validation(required = false)
    private boolean flexibleSpendingDeclineCoverage;
    @Validation(required = false)
    private boolean CIGNADentalCare;
    @Validation(required = false)
    private boolean dentalPPO;
    @Validation(required = false)
    private boolean dentalEPO;
    @Validation(required = false)
    private boolean dentalIndemnity;
    @Validation(required = false)
    private boolean dentalCoverage;
    @Validation(required = true)
    private boolean otherInsurance;
    @Validation(required = false)
    private String person1Name;
    @Validation(required = false)
    private String person1SSN;
    @Validation(type="date", required = false)
    private int person1Date;
    @Validation(required = false)
    private boolean person1A;
    @Validation(required = false)
    private boolean person1B;
    @Validation(required = false)
    private boolean person1Med;
    @Validation(required = false)
    private boolean person1Other;
    @Validation(required = false)
    private String person2Name;
    @Validation(required = false)
    private String person2SSN;
    @Validation(type="date", required = false)
    private int person2Date;
    @Validation(required = false)
    private boolean person2A;
    @Validation(required = false)
    private boolean person2B;
    @Validation(required = false)
    private boolean person2Med;
    @Validation(required = false)
    private boolean person2Other;
    @Validation(required = false)
    private boolean life;
    @Validation(required = false)
    private int lifeValue;
    @Validation(required = false)
    private boolean additionalLife;
    @Validation(required = false)
    private int additionalLifeValue;
    @Validation(required = false)
    private boolean dependentLifeSpouse;
    @Validation(required = false)
    private int dependentLifeSpouseValue;
    @Validation(required = false)
    private boolean dependendLifeChild;
    @Validation(required = false)
    private int dependendLifeChildValue;
    @Validation(required = false)
    private boolean adnd;
    @Validation(required = false)
    private int adndValue;
    @Validation(required = false)
    private boolean additionalAdnd;
    @Validation(required = false)
    private int additionalAdndValue;
    @Validation(required = false)
    private boolean std;
    @Validation(required = false)
    private int stdValue;
    @Validation(required = false)
    private boolean ltd;
    @Validation(required = false)
    private int ltdValue;
    @Validation(required = false)
    private boolean declineLife;
    @Validation(required = false)
    private boolean declineAdnd;
    @Validation(required = false)
    private boolean declineStd;
    @Validation(required = false)
    private boolean declineLtd;
    @Validation(required = true)
    private String cignaAcctNo;
    @Validation(required = true)
    private String networkId;
    @Validation(required = true)
    private String employeeGroupCustomerNo;
    @Validation(required = true)
    private String employeeDivision;
    @Validation(required = false)
    private String employeeClass;
    @Validation(required = false)
    private String employeeDeptCode;
    @Validation(required = true)
    private String employerAddress;
    @Validation(required = true)
    private String employerCity;
    @Validation(required = true)
    private String employerState;
    @Validation(required = true)
    private String employerZip;
    @Validation(type="date", required = true)
    private int employeeDateOfHire;
    @Validation(required = true)
    private String enrollmentReason;
    @Validation(type="date", required = true)
    private int enrollmentReasonDate;
    @Validation(required = true)
    private String cignaAnnualAmt;
    @Validation(required = false)
    private String medicalBenOption;
    @Validation(required = false)
    private String dentalBenOption;
    @Validation(required = false)
    private boolean addDependents;
    @Validation(type="date", required = false)
    private int addDependentsDate;
    @Validation(required = false)
    private String cancelEmployee;

    public int getChild1DOB() {
        return child1DOB;
    }

    public void setChild1DOB(int child1DOB) {
        this.child1DOB = child1DOB;
    }

    public String getChild1FirstName() {
        return child1FirstName;
    }

    public void setChild1FirstName(String child1FirstName) {
        this.child1FirstName = child1FirstName;
    }

    public String getChild1LastName() {
        return child1LastName;
    }

    public void setChild1LastName(String child1LastName) {
        this.child1LastName = child1LastName;
    }

    public String getChild1MiddleName() {
        return child1MiddleName;
    }

    public void setChild1MiddleName(String child1MiddleName) {
        this.child1MiddleName = child1MiddleName;
    }

    public String getChild1Sex() {
        return child1Sex;
    }

    public void setChild1Sex(String child1Sex) {
        this.child1Sex = child1Sex;
    }

    public boolean isChild1Student() {
        return child1Student;
    }

    public void setChild1Student(boolean child1Student) {
        this.child1Student = child1Student;
    }

    public int getChild2DOB() {
        return child2DOB;
    }

    public void setChild2DOB(int child2DOB) {
        this.child2DOB = child2DOB;
    }

    public String getChild2FirstName() {
        return child2FirstName;
    }

    public void setChild2FirstName(String child2FirstName) {
        this.child2FirstName = child2FirstName;
    }

    public String getChild2LastName() {
        return child2LastName;
    }

    public void setChild2LastName(String child2LastName) {
        this.child2LastName = child2LastName;
    }

    public String getChild2MiddleName() {
        return child2MiddleName;
    }

    public void setChild2MiddleName(String child2MiddleName) {
        this.child2MiddleName = child2MiddleName;
    }

    public String getChild2Sex() {
        return child2Sex;
    }

    public void setChild2Sex(String child2Sex) {
        this.child2Sex = child2Sex;
    }

    public boolean isChild2Student() {
        return child2Student;
    }

    public void setChild2Student(boolean child2Student) {
        this.child2Student = child2Student;
    }

    public int getChild3DOB() {
        return child3DOB;
    }

    public void setChild3DOB(int child3DOB) {
        this.child3DOB = child3DOB;
    }

    public String getChild3FirstName() {
        return child3FirstName;
    }

    public void setChild3FirstName(String child3FirstName) {
        this.child3FirstName = child3FirstName;
    }

    public String getChild3LastName() {
        return child3LastName;
    }

    public void setChild3LastName(String child3LastName) {
        this.child3LastName = child3LastName;
    }

    public String getChild3MiddleName() {
        return child3MiddleName;
    }

    public void setChild3MiddleName(String child3MiddleName) {
        this.child3MiddleName = child3MiddleName;
    }

    public String getChild3Sex() {
        return child3Sex;
    }

    public void setChild3Sex(String child3Sex) {
        this.child3Sex = child3Sex;
    }

    public boolean isChild3Student() {
        return child3Student;
    }

    public void setChild3Student(boolean child3Student) {
        this.child3Student = child3Student;
    }

    public int getChild4DOB() {
        return child4DOB;
    }

    public void setChild4DOB(int child4DOB) {
        this.child4DOB = child4DOB;
    }

    public String getChild4FirstName() {
        return child4FirstName;
    }

    public void setChild4FirstName(String child4FirstName) {
        this.child4FirstName = child4FirstName;
    }

    public String getChild4LastName() {
        return child4LastName;
    }

    public void setChild4LastName(String child4LastName) {
        this.child4LastName = child4LastName;
    }

    public String getChild4MiddleName() {
        return child4MiddleName;
    }

    public void setChild4MiddleName(String child4MiddleName) {
        this.child4MiddleName = child4MiddleName;
    }

    public String getChild4Sex() {
        return child4Sex;
    }

    public void setChild4Sex(String child4Sex) {
        this.child4Sex = child4Sex;
    }

    public boolean isChild4Student() {
        return child4Student;
    }

    public void setChild4Student(boolean child4Student) {
        this.child4Student = child4Student;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeCity() {
        return employeeCity;
    }

    public void setEmployeeCity(String employeeCity) {
        this.employeeCity = employeeCity;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeMiddleName() {
        return employeeMiddleName;
    }

    public void setEmployeeMiddleName(String employeeMiddleName) {
        this.employeeMiddleName = employeeMiddleName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeSSN() {
        return employeeSSN;
    }

    public void setEmployeeSSN(String employeeSSN) {
        this.employeeSSN = employeeSSN;
    }

    public String getEmployeeSex() {
        return employeeSex;
    }

    public void setEmployeeSex(String employeeSex) {
        this.employeeSex = employeeSex;
    }

    public String getEmployeeState() {
        return employeeState;
    }

    public void setEmployeeState(String employeeState) {
        this.employeeState = employeeState;
    }

    public String getEmployeeZip() {
        return employeeZip;
    }

    public void setEmployeeZip(String employeeZip) {
        this.employeeZip = employeeZip;
    }

    public int getNumberDependents() {
        return numberDependents;
    }

    public void setNumberDependents(int numberDependents) {
        this.numberDependents = numberDependents;
    }

    public int getSpouseDOB() {
        return spouseDOB;
    }

    public void setSpouseDOB(int spouseDOB) {
        this.spouseDOB = spouseDOB;
    }

    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getSpouseMiddleName() {
        return spouseMiddleName;
    }

    public void setSpouseMiddleName(String spouseMiddleName) {
        this.spouseMiddleName = spouseMiddleName;
    }

    public String getSpouseSex() {
        return spouseSex;
    }

    public void setSpouseSex(String spouseSex) {
        this.spouseSex = spouseSex;
    }

    public String getEmployeeWorkPhone() {
        return employeeWorkPhone;
    }

    public void setEmployeeWorkPhone(String employeeWorkPhone) {
        this.employeeWorkPhone = employeeWorkPhone;
    }

    public String getEmployeeExternalId() {
        return employeeExternalId;
    }

    public void setEmployeeExternalId(String employeeExternalId) {
        this.employeeExternalId = employeeExternalId;
    }

    public String getChild1SSN() {
        return child1SSN;
    }

    public void setChild1SSN(String child1SSN) {
        this.child1SSN = child1SSN;
    }

    public String getChild2SSN() {
        return child2SSN;
    }

    public void setChild2SSN(String child2SSN) {
        this.child2SSN = child2SSN;
    }

    public String getChild3SSN() {
        return child3SSN;
    }

    public void setChild3SSN(String child3SSN) {
        this.child3SSN = child3SSN;
    }

    public String getChild4SSN() {
        return child4SSN;
    }

    public void setChild4SSN(String child4SSN) {
        this.child4SSN = child4SSN;
    }

    public String getSpousePCPHCCChoice() {
        return spousePCPHCCChoice;
    }

    public void setSpousePCPHCCChoice(String spousePCPHCCChoice) {
        this.spousePCPHCCChoice = spousePCPHCCChoice;
    }

    public String getChild1PCPHCCChoice() {
        return child1PCPHCCChoice;
    }

    public void setChild1PCPHCCChoice(String child1PCPHCCChoice) {
        this.child1PCPHCCChoice = child1PCPHCCChoice;
    }

    public String getChild2PCPHCCChoice() {
        return child2PCPHCCChoice;
    }

    public void setChild2PCPHCCChoice(String child2PCPHCCChoice) {
        this.child2PCPHCCChoice = child2PCPHCCChoice;
    }

    public String getChild3PCPHCCChoice() {
        return child3PCPHCCChoice;
    }

    public void setChild3PCPHCCChoice(String child3PCPHCCChoice) {
        this.child3PCPHCCChoice = child3PCPHCCChoice;
    }

    public String getChild4PCPHCCChoice() {
        return child4PCPHCCChoice;
    }

    public void setChild4PCPHCCChoice(String child4PCPHCCChoice) {
        this.child4PCPHCCChoice = child4PCPHCCChoice;
    }

    public boolean getSpouseExistingPatient() {
        return spouseExistingPatient;
    }

    public void setSpouseExistingPatient(boolean spouseExistingPatient) {
        this.spouseExistingPatient = spouseExistingPatient;
    }

    public String getSpouseDentalOfficeNumber() {
        return spouseDentalOfficeNumber;
    }

    public void setSpouseDentalOfficeNumber(String spouseDentalOfficeNumber) {
        this.spouseDentalOfficeNumber = spouseDentalOfficeNumber;
    }

    public boolean getChild1ExistingPatient() {
        return child1ExistingPatient;
    }

    public void setChild1ExistingPatient(boolean child1ExistingPatient) {
        this.child1ExistingPatient = child1ExistingPatient;
    }

    public String getChild1DentalOfficeNumber1() {
        return child1DentalOfficeNumber1;
    }

    public void setChild1DentalOfficeNumber1(String child1DentalOfficeNumber1) {
        this.child1DentalOfficeNumber1 = child1DentalOfficeNumber1;
    }

    public String getChild1DentalOfficeNumber2() {
        return child1DentalOfficeNumber2;
    }

    public void setChild1DentalOfficeNumber2(String child1DentalOfficeNumber2) {
        this.child1DentalOfficeNumber2 = child1DentalOfficeNumber2;
    }

    public boolean getChild2ExistingPatient() {
        return child2ExistingPatient;
    }

    public void setChild2ExistingPatient(boolean child2ExistingPatient) {
        this.child2ExistingPatient = child2ExistingPatient;
    }

    public String getChild2DentalOfficeNumber1() {
        return child2DentalOfficeNumber1;
    }

    public void setChild2DentalOfficeNumber1(String child2DentalOfficeNumber1) {
        this.child2DentalOfficeNumber1 = child2DentalOfficeNumber1;
    }

    public String getChild2DentalOfficeNumber2() {
        return child2DentalOfficeNumber2;
    }

    public void setChild2DentalOfficeNumber2(String child2DentalOfficeNumber2) {
        this.child2DentalOfficeNumber2 = child2DentalOfficeNumber2;
    }

    public boolean getChild3ExistingPatient() {
        return child3ExistingPatient;
    }

    public void setChild3ExistingPatient(boolean child3ExistingPatient) {
        this.child3ExistingPatient = child3ExistingPatient;
    }

    public String getChild3DentalOfficeNumber1() {
        return child3DentalOfficeNumber1;
    }

    public void setChild3DentalOfficeNumber1(String child3DentalOfficeNumber1) {
        this.child3DentalOfficeNumber1 = child3DentalOfficeNumber1;
    }

    public String getChild3DentalOfficeNumber2() {
        return child3DentalOfficeNumber2;
    }

    public void setChild3DentalOfficeNumber2(String child3DentalOfficeNumber2) {
        this.child3DentalOfficeNumber2 = child3DentalOfficeNumber2;
    }

    public boolean getChild4ExistingPatient() {
        return child4ExistingPatient;
    }

    public void setChild4ExistingPatient(boolean child4ExistingPatient) {
        this.child4ExistingPatient = child4ExistingPatient;
    }

    public String getChild4DentalOfficeNumber1() {
        return child4DentalOfficeNumber1;
    }

    public void setChild4DentalOfficeNumber1(String child4DentalOfficeNumber1) {
        this.child4DentalOfficeNumber1 = child4DentalOfficeNumber1;
    }

    public String getChild4DentalOfficeNumber2() {
        return child4DentalOfficeNumber2;
    }

    public void setChild4DentalOfficeNumber2(String child4DentalOfficeNumber2) {
        this.child4DentalOfficeNumber2 = child4DentalOfficeNumber2;
    }

    public boolean getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(boolean pointOfService) {
        this.pointOfService = pointOfService;
    }

    public boolean getHMO() {
        return HMO;
    }

    public void setHMO(boolean HMO) {
        this.HMO = HMO;
    }

    public boolean getNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    public boolean getPointOfServiceOpen() {
        return pointOfServiceOpen;
    }

    public void setPointOfServiceOpen(boolean pointOfServiceOpen) {
        this.pointOfServiceOpen = pointOfServiceOpen;
    }

    public boolean getHMOOpen() {
        return HMOOpen;
    }

    public void setHMOOpen(boolean HMOOpen) {
        this.HMOOpen = HMOOpen;
    }

    public boolean getNetworkOpen() {
        return networkOpen;
    }

    public void setNetworkOpen(boolean networkOpen) {
        this.networkOpen = networkOpen;
    }

    public boolean getOpenAccessPlus() {
        return openAccessPlus;
    }

    public void setOpenAccessPlus(boolean openAccessPlus) {
        this.openAccessPlus = openAccessPlus;
    }

    public boolean getOpenAccessPlusInNetwork() {
        return openAccessPlusInNetwork;
    }

    public void setOpenAccessPlusInNetwork(boolean openAccessPlusInNetwork) {
        this.openAccessPlusInNetwork = openAccessPlusInNetwork;
    }

    public boolean getPPO() {
        return PPO;
    }

    public void setPPO(boolean PPO) {
        this.PPO = PPO;
    }

    public boolean getNetworkPPOEPO() {
        return networkPPOEPO;
    }

    public void setNetworkPPOEPO(boolean networkPPOEPO) {
        this.networkPPOEPO = networkPPOEPO;
    }

    public boolean getPPA() {
        return PPA;
    }

    public void setPPA(boolean PPA) {
        this.PPA = PPA;
    }

    public boolean getMedicalIndemnity() {
        return medicalIndemnity;
    }

    public void setMedicalIndemnity(boolean medicalIndemnity) {
        this.medicalIndemnity = medicalIndemnity;
    }

    public boolean getWriteIn() {
        return writeIn;
    }

    public void setWriteIn(boolean writeIn) {
        this.writeIn = writeIn;
    }

    public String getWriteInText() {
        return writeInText;
    }

    public void setWriteInText(String writeInText) {
        this.writeInText = writeInText;
    }

    public boolean getHRA() {
        return HRA;
    }

    public void setHRA(boolean HRA) {
        this.HRA = HRA;
    }

    public boolean getHSA() {
        return HSA;
    }

    public void setHSA(boolean HSA) {
        this.HSA = HSA;
    }

    public boolean getPharmacyHRA() {
        return pharmacyHRA;
    }

    public void setPharmacyHRA(boolean pharmacyHRA) {
        this.pharmacyHRA = pharmacyHRA;
    }

    public boolean getDentalHRA() {
        return dentalHRA;
    }

    public void setDentalHRA(boolean dentalHRA) {
        this.dentalHRA = dentalHRA;
    }

    public boolean getWithOpenAccessPlus() {
        return withOpenAccessPlus;
    }

    public void setWithOpenAccessPlus(boolean withOpenAccessPlus) {
        this.withOpenAccessPlus = withOpenAccessPlus;
    }

    public boolean getWithOpenAccessPlusNetwork() {
        return withOpenAccessPlusNetwork;
    }

    public void setWithOpenAccessPlusNetwork(boolean withOpenAccessPlusNetwork) {
        this.withOpenAccessPlusNetwork = withOpenAccessPlusNetwork;
    }

    public boolean getWithEPO() {
        return withEPO;
    }

    public void setWithEPO(boolean withEPO) {
        this.withEPO = withEPO;
    }

    public boolean getWithIndemnity() {
        return withIndemnity;
    }

    public void setWithIndemnity(boolean withIndemnity) {
        this.withIndemnity = withIndemnity;
    }

    public boolean getCIGNACareNetwork() {
        return CIGNACareNetwork;
    }

    public void setCIGNACareNetwork(boolean CIGNACareNetwork) {
        this.CIGNACareNetwork = CIGNACareNetwork;
    }

    public boolean getDeclineCoverage() {
        return declineCoverage;
    }

    public void setDeclineCoverage(boolean declineCoverage) {
        this.declineCoverage = declineCoverage;
    }

    public String getApplicableOption() {
        return applicableOption;
    }

    public void setApplicableOption(String applicableOption) {
        this.applicableOption = applicableOption;
    }

    public boolean getFlexibleSpendingHealthCare() {
        return flexibleSpendingHealthCare;
    }

    public void setFlexibleSpendingHealthCare(boolean flexibleSpendingHealthCare) {
        this.flexibleSpendingHealthCare = flexibleSpendingHealthCare;
    }

    public boolean getFlexibleSpendingDependentDayCare() {
        return flexibleSpendingDependentDayCare;
    }

    public void setFlexibleSpendingDependentDayCare(boolean flexibleSpendingDependentDayCare) {
        this.flexibleSpendingDependentDayCare = flexibleSpendingDependentDayCare;
    }

    public boolean getFlexibleSpendingDeclineCoverage() {
        return flexibleSpendingDeclineCoverage;
    }

    public void setFlexibleSpendingDeclineCoverage(boolean flexibleSpendingDeclineCoverage) {
        this.flexibleSpendingDeclineCoverage = flexibleSpendingDeclineCoverage;
    }

    public boolean getCIGNADentalCare() {
        return CIGNADentalCare;
    }

    public void setCIGNADentalCare(boolean CIGNADentalCare) {
        this.CIGNADentalCare = CIGNADentalCare;
    }

    public boolean getDentalPPO() {
        return dentalPPO;
    }

    public void setDentalPPO(boolean dentalPPO) {
        this.dentalPPO = dentalPPO;
    }

    public boolean getDentalEPO() {
        return dentalEPO;
    }

    public void setDentalEPO(boolean dentalEPO) {
        this.dentalEPO = dentalEPO;
    }

    public boolean getDentalIndemnity() {
        return dentalIndemnity;
    }

    public void setDentalIndemnity(boolean dentalIndemnity) {
        this.dentalIndemnity = dentalIndemnity;
    }

    public boolean getDentalCoverage() {
        return dentalCoverage;
    }

    public void setDentalCoverage(boolean dentalCoverage) {
        this.dentalCoverage = dentalCoverage;
    }

    public boolean isAddDependents() {
        return addDependents;
    }

    public void setAddDependents(boolean addDependents) {
        this.addDependents = addDependents;
    }

    public int getAddDependentsDate() {
        return addDependentsDate;
    }

    public void setAddDependentsDate(int addDependentsDate) {
        this.addDependentsDate = addDependentsDate;
    }

    public boolean isAdditionalAdnd() {
        return additionalAdnd;
    }

    public void setAdditionalAdnd(boolean additionalAdnd) {
        this.additionalAdnd = additionalAdnd;
    }

    public int getAdditionalAdndValue() {
        return additionalAdndValue;
    }

    public void setAdditionalAdndValue(int additionalAdndValue) {
        this.additionalAdndValue = additionalAdndValue;
    }

    public boolean isAdditionalLife() {
        return additionalLife;
    }

    public void setAdditionalLife(boolean additionalLife) {
        this.additionalLife = additionalLife;
    }

    public int getAdditionalLifeValue() {
        return additionalLifeValue;
    }

    public void setAdditionalLifeValue(int additionalLifeValue) {
        this.additionalLifeValue = additionalLifeValue;
    }

    public boolean isAdnd() {
        return adnd;
    }

    public void setAdnd(boolean adnd) {
        this.adnd = adnd;
    }

    public int getAdndValue() {
        return adndValue;
    }

    public void setAdndValue(int adndValue) {
        this.adndValue = adndValue;
    }

    public String getCancelEmployee() {
        return cancelEmployee;
    }

    public void setCancelEmployee(String cancelEmployee) {
        this.cancelEmployee = cancelEmployee;
    }

    public String getCignaAcctNo() {
        return cignaAcctNo;
    }

    public void setCignaAcctNo(String cignaAcctNo) {
        this.cignaAcctNo = cignaAcctNo;
    }

    public String getCignaAnnualAmt() {
        return cignaAnnualAmt;
    }

    public void setCignaAnnualAmt(String cignaAnnualAmt) {
        this.cignaAnnualAmt = cignaAnnualAmt;
    }

    public boolean isDeclineAdnd() {
        return declineAdnd;
    }

    public void setDeclineAdnd(boolean declineAdnd) {
        this.declineAdnd = declineAdnd;
    }

    public boolean isDeclineLife() {
        return declineLife;
    }

    public void setDeclineLife(boolean declineLife) {
        this.declineLife = declineLife;
    }

    public boolean isDeclineLtd() {
        return declineLtd;
    }

    public void setDeclineLtd(boolean declineLtd) {
        this.declineLtd = declineLtd;
    }

    public boolean isDeclineStd() {
        return declineStd;
    }

    public void setDeclineStd(boolean declineStd) {
        this.declineStd = declineStd;
    }

    public String getDentalBenOption() {
        return dentalBenOption;
    }

    public void setDentalBenOption(String dentalBenOption) {
        this.dentalBenOption = dentalBenOption;
    }

    public boolean isDependendLifeChild() {
        return dependendLifeChild;
    }

    public void setDependendLifeChild(boolean dependendLifeChild) {
        this.dependendLifeChild = dependendLifeChild;
    }

    public int getDependendLifeChildValue() {
        return dependendLifeChildValue;
    }

    public void setDependendLifeChildValue(int dependendLifeChildValue) {
        this.dependendLifeChildValue = dependendLifeChildValue;
    }

    public boolean isDependentLifeSpouse() {
        return dependentLifeSpouse;
    }

    public void setDependentLifeSpouse(boolean dependentLifeSpouse) {
        this.dependentLifeSpouse = dependentLifeSpouse;
    }

    public int getDependentLifeSpouseValue() {
        return dependentLifeSpouseValue;
    }

    public void setDependentLifeSpouseValue(int dependentLifeSpouseValue) {
        this.dependentLifeSpouseValue = dependentLifeSpouseValue;
    }

    public String getEmployeeClass() {
        return employeeClass;
    }

    public void setEmployeeClass(String employeeClass) {
        this.employeeClass = employeeClass;
    }

    public int getEmployeeDateOfHire() {
        return employeeDateOfHire;
    }

    public void setEmployeeDateOfHire(int employeeDateOfHire) {
        this.employeeDateOfHire = employeeDateOfHire;
    }

    public String getEmployeeDeptCode() {
        return employeeDeptCode;
    }

    public void setEmployeeDeptCode(String employeeDeptCode) {
        this.employeeDeptCode = employeeDeptCode;
    }

    public String getEmployeeDivision() {
        return employeeDivision;
    }

    public void setEmployeeDivision(String employeeDivision) {
        this.employeeDivision = employeeDivision;
    }

    public String getEmployeeGroupCustomerNo() {
        return employeeGroupCustomerNo;
    }

    public void setEmployeeGroupCustomerNo(String employeeGroupCustomerNo) {
        this.employeeGroupCustomerNo = employeeGroupCustomerNo;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    public String getEmployerCity() {
        return employerCity;
    }

    public void setEmployerCity(String employerCity) {
        this.employerCity = employerCity;
    }

    public String getEmployerState() {
        return employerState;
    }

    public void setEmployerState(String employerState) {
        this.employerState = employerState;
    }

    public String getEmployerZip() {
        return employerZip;
    }

    public void setEmployerZip(String employerZip) {
        this.employerZip = employerZip;
    }

    public String getEnrollmentReason() {
        return enrollmentReason;
    }

    public void setEnrollmentReason(String enrollmentReason) {
        this.enrollmentReason = enrollmentReason;
    }

    public int getEnrollmentReasonDate() {
        return enrollmentReasonDate;
    }

    public void setEnrollmentReasonDate(int enrollmentReasonDate) {
        this.enrollmentReasonDate = enrollmentReasonDate;
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    public int getLifeValue() {
        return lifeValue;
    }

    public void setLifeValue(int lifeValue) {
        this.lifeValue = lifeValue;
    }

    public boolean isLtd() {
        return ltd;
    }

    public void setLtd(boolean ltd) {
        this.ltd = ltd;
    }

    public int getLtdValue() {
        return ltdValue;
    }

    public void setLtdValue(int ltdValue) {
        this.ltdValue = ltdValue;
    }

    public String getMedicalBenOption() {
        return medicalBenOption;
    }

    public void setMedicalBenOption(String medicalBenOption) {
        this.medicalBenOption = medicalBenOption;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public boolean isOtherInsurance() {
        return otherInsurance;
    }

    public void setOtherInsurance(boolean otherInsurance) {
        this.otherInsurance = otherInsurance;
    }

    public boolean isPerson1A() {
        return person1A;
    }

    public void setPerson1A(boolean person1A) {
        this.person1A = person1A;
    }

    public boolean isPerson1B() {
        return person1B;
    }

    public void setPerson1B(boolean person1B) {
        this.person1B = person1B;
    }

    public int getPerson1Date() {
        return person1Date;
    }

    public void setPerson1Date(int person1Date) {
        this.person1Date = person1Date;
    }

    public boolean isPerson1Med() {
        return person1Med;
    }

    public void setPerson1Med(boolean person1Med) {
        this.person1Med = person1Med;
    }

    public String getPerson1Name() {
        return person1Name;
    }

    public void setPerson1Name(String person1Name) {
        this.person1Name = person1Name;
    }

    public boolean isPerson1Other() {
        return person1Other;
    }

    public void setPerson1Other(boolean person1Other) {
        this.person1Other = person1Other;
    }

    public String getPerson1SSN() {
        return person1SSN;
    }

    public void setPerson1SSN(String person1SSN) {
        this.person1SSN = person1SSN;
    }

    public boolean isPerson2A() {
        return person2A;
    }

    public void setPerson2A(boolean person2A) {
        this.person2A = person2A;
    }

    public boolean isPerson2B() {
        return person2B;
    }

    public void setPerson2B(boolean person2B) {
        this.person2B = person2B;
    }

    public int getPerson2Date() {
        return person2Date;
    }

    public void setPerson2Date(int person2Date) {
        this.person2Date = person2Date;
    }

    public boolean isPerson2Med() {
        return person2Med;
    }

    public void setPerson2Med(boolean person2Med) {
        this.person2Med = person2Med;
    }

    public String getPerson2Name() {
        return person2Name;
    }

    public void setPerson2Name(String person2Name) {
        this.person2Name = person2Name;
    }

    public boolean isPerson2Other() {
        return person2Other;
    }

    public void setPerson2Other(boolean person2Other) {
        this.person2Other = person2Other;
    }

    public String getPerson2SSN() {
        return person2SSN;
    }

    public void setPerson2SSN(String person2SSN) {
        this.person2SSN = person2SSN;
    }

    public boolean isStd() {
        return std;
    }

    public void setStd(boolean std) {
        this.std = std;
    }

    public int getStdValue() {
        return stdValue;
    }

    public void setStdValue(int stdValue) {
        this.stdValue = stdValue;
    }


}

	
