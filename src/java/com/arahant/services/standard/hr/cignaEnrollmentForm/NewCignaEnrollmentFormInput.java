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
package com.arahant.services.standard.hr.cignaEnrollmentForm;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BEmployee;
import com.arahant.annotation.Validation;
import com.arahant.utils.DOMUtils;

public class NewCignaEnrollmentFormInput extends TransmitInputBase {

    void setData(BEmployee be) {

        /*
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
        */

        /*
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
        */

        /*
        //For each dependent
        for (int loop=0;loop<getDependents().length;loop++)
        {
                BPerson bp=new BPerson();
                bp.create();
                bp.setFirstName(dependents[loop].getPersonFirstName());
                bp.setMiddleName(dependents[loop].getPersonMiddleName());
                bp.setLastName(dependents[loop].getPersonLastName());
                bp.setDob(dependents[loop].getPersonDOB());
                bp.setSex(dependents[loop].getPersonGender());
                bp.setSsn(dependents[loop].getPersonSSN());
                //bp.setChoice(dependents[loop].getPersonChoice());
                //bp.setExists(dependents[loop].isPersonExisting());
                //bp.setDentalNumber1(dependents[loop].getPersonDentalNumber1());
                //bp.setDentalNumber2(dependents[loop].getPersonDentalNumber2());
                be.addPendingInsert(bp);
        }
        
        for (int loop=0;loop<getOtherCoverage().length;loop++)
        {
                BPerson bp=new BPerson();
                bp.create();
                bp.setFirstName(otherCoverage[loop].getPersonFirstName());
                bp.setMiddleName(otherCoverage[loop].getPersonMiddleName());
                bp.setLastName(otherCoverage[loop].getPersonLastName());
                bp.setSsn(otherCoverage[loop].getPersonSSN());
                //bp.setMedicareA(otherCoverage[loop].isPersonMedicareA());
                //bp.setMedicareB(otherCoverage[loop].isPersonMedicareB());
                //bp.setMedicaid(otherCoverage[loop].isPersonMedicaid());
                //bp.setOther(otherCoverage[loop].isPersonOther());
                be.addPendingInsert(bp);
        }
        */

    }
    
    @Validation(required = true, table="person", column="lname")
    private String employeeLastName;
    @Validation(required = true, table="person", column="fname")
    private String employeeFirstName;
    @Validation(required = false, table="person", column="mname")
    private String employeeMiddleName;
    @Validation(required = true, table="person", column="ssn")
    private String employeeSSN;
    @Validation(required = true)
    private String employeeSex;
    @Validation(type="date",required = true)
    private int employeeDOB;
    @Validation(required = true, table="address", column="street")
    private String employeeAddress;
    @Validation(required = true, table="address", column="city")
    private String employeeCity;
    @Validation(required = true, table="address", column="state")
    private String employeeState;
    @Validation(required = true, table="address", column="zip")
    private String employeeZip;
    @Validation(required = true, table="person", column="personal_email")
    private String employeeEmail;
    @Validation(required = true, table="phone",column="phone_number")
    private String employeePhone;
    @Validation(required = true, table="phone",column="phone_number")
    private String employeeWorkPhone;
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
    @Validation(required = false)
    private boolean openAccessCore;
    @Validation(required = false)
    private boolean openAccessPremium;
    @Validation(required = false)
    private boolean declineCoverage;
    @Validation (required=false, type="array")
    private NewCignaEnrollmentFormInputItem[] dependents;
    @Validation (required=false, type="array")
    NewCignaEnrollmentFormInputItem2[] otherCoverage;

    public int getEmployeeDOB() {
            return employeeDOB;
    }

    public void setEmployeeDOB(int employeeDOB) {
            this.employeeDOB = employeeDOB;
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

    public String getEmployeeWorkPhone() {
        return employeeWorkPhone;
    }

    public void setEmployeeWorkPhone(String employeeWorkPhone) {
        this.employeeWorkPhone = employeeWorkPhone;
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

    public boolean isOtherInsurance() {
        return otherInsurance;
    }

    public void setOtherInsurance(boolean otherInsurance) {
        this.otherInsurance = otherInsurance;
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

    public NewCignaEnrollmentFormInputItem[] getDependents() {
        if (dependents==null)
			dependents=new NewCignaEnrollmentFormInputItem[0];
        return dependents;
    }

    public void setDependents(NewCignaEnrollmentFormInputItem[] dependents) {
        this.dependents = dependents;
    }

    public NewCignaEnrollmentFormInputItem2[] getOtherCoverage() {
        if (otherCoverage==null)
		otherCoverage=new NewCignaEnrollmentFormInputItem2[0];
        return otherCoverage;
    }

    public void setOtherCoverage(NewCignaEnrollmentFormInputItem2[] otherCoverage) {
        this.otherCoverage = otherCoverage;
    }

    public boolean isOpenAccessCore() {
        return openAccessCore;
    }

    public void setOpenAccessCore(boolean openAccessCore) {
        this.openAccessCore = openAccessCore;
    }

    public boolean isOpenAccessPremium() {
        return openAccessPremium;
    }

    public void setOpenAccessPremium(boolean openAccessPremium) {
        this.openAccessPremium = openAccessPremium;
    }



    public String toXML()
    {
       String ret="";

       ret+=tag("CIGNADATA",
            tag("employeeLastName", DOMUtils.escapeText(employeeLastName))+
            tag("employeeFirstName", DOMUtils.escapeText(employeeFirstName))+
            tag("employeeMiddleName", DOMUtils.escapeText(employeeMiddleName))+
            tag("employeeSSN", DOMUtils.escapeText(employeeSSN))+
            tag("employeeDOB", employeeDOB)+
            tag("employeeSex", DOMUtils.escapeText(employeeSex))+
            tag("employeeAddress", DOMUtils.escapeText(employeeAddress))+
            tag("employeeCity", DOMUtils.escapeText(employeeCity))+
            tag("employeeState", DOMUtils.escapeText(employeeState))+
            tag("employeeZip", DOMUtils.escapeText(employeeZip))+
            tag("employeeEmail", DOMUtils.escapeText(employeeEmail))+
            tag("employeePhone", DOMUtils.escapeText(employeePhone))+
            tag("employeeWorkPhone", DOMUtils.escapeText(employeeWorkPhone))+
            tag("pointOfService", pointOfService)+
            tag("HMO", HMO)+
            tag("network", network)+
            tag("pointOfServiceOpen", pointOfServiceOpen)+
            tag("HMOOpen", HMOOpen)+
            tag("networkOpen", networkOpen)+
            tag("openAccessPlus", openAccessPlus)+
            tag("openAccessPlusInNetwork", openAccessPlusInNetwork)+
            tag("PPO", PPO)+
            tag("networkPPOEPO", networkPPOEPO)+
            tag("PPA", PPA)+
            tag("medicalIndemnity", medicalIndemnity)+
            tag("writeIn", writeIn)+
            tag("writeInText", DOMUtils.escapeText(writeInText))+
            tag("HRA", HRA)+
            tag("HSA", HSA)+
            tag("pharmacyHRA", pharmacyHRA)+
            tag("dentalHRA", dentalHRA)+
            tag("withOpenAccessPlus", withOpenAccessPlus)+
            tag("withOpenAccessPlusNetwork", withOpenAccessPlusNetwork)+
            tag("withEPO", withEPO)+
            tag("withIndemnity", withIndemnity)+
            tag("CIGNACareNetwork", CIGNACareNetwork)+
            tag("applicableOption", DOMUtils.escapeText(applicableOption))+
            tag("flexibleSpendingHealthCare", flexibleSpendingHealthCare)+
            tag("flexibleSpendingDependentDayCare", flexibleSpendingDependentDayCare)+
            tag("flexibleSpendingDeclineCoverage", flexibleSpendingDeclineCoverage)+
            tag("CIGNADentalCare", CIGNADentalCare)+
            tag("dentalPPO", dentalPPO)+
            tag("dentalEPO", dentalEPO)+
            tag("dentalIndemnity", dentalIndemnity)+
            tag("dentalCoverage", dentalCoverage)+
            tag("otherInsurance", otherInsurance)+
            tag("life", life)+
            tag("lifeValue", lifeValue)+
            tag("additionalLife", additionalLife)+
            tag("additionalLifeValue", additionalLifeValue)+
            tag("dependentLifeSpouse", dependentLifeSpouse)+
            tag("dependentLifeSpouseValue", dependentLifeSpouseValue)+
            tag("dependendLifeChild", dependendLifeChild)+
            tag("dependendLifeChildValue", dependendLifeChildValue)+
            tag("adnd", adnd)+
            tag("adndValue", adndValue)+
            tag("additionalAdnd", additionalAdnd)+
            tag("additionalAdndValue", additionalAdndValue)+
            tag("std", std)+
            tag("stdValue", stdValue)+
            tag("ltd", ltd)+
            tag("ltdValue", ltdValue)+
            tag("declineLife", declineLife)+
            tag("declineAdnd", declineAdnd)+
            tag("declineStd", declineStd)+
            tag("declineLtd", declineLtd)+
            tag("openAccessCore", openAccessCore)+
            tag("openAccessPremium", openAccessPremium)+
            tag("declineCoverage", declineCoverage)+
            getDependentsXml()+
            getOtherCoverageXml()
            );

       return ret;
    }

    public String getOtherCoverageXml()
    {
        String ret="<otherCoverage>";

        for (NewCignaEnrollmentFormInputItem2 i : getOtherCoverage())
        {
            ret+="<other>";
            ret+=i.toXML();
            ret+="</other>";
        }

        return ret+"</otherCoverage>";
    }
    public String getDependentsXml()
    {

        String ret="<dependents>";

        for (NewCignaEnrollmentFormInputItem i : getDependents())
        {
            ret+="<dependent>";
            ret+=i.toXML();
            ret+="</dependent>";
        }

        return ret+"</dependents>";
    }
    public String tag(String tagName, String value)
    {
        return "<"+tagName+">"+value+"</"+tagName+">\n";
    }
    public String tag(String tagName, int value)
    {
        return "<"+tagName+">"+value+"</"+tagName+">\n";
    }
    public String tag(String tagName, boolean value)
    {
        return tag(tagName,value?"TRUE":"FALSE");
    }
}

	
