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



package com.arahant.services.standard.hr.cignaEnrollmentForm;

import com.arahant.business.BHREmplDependent;

/**
 *
 */
public class LoadCignaEnrollmentFormReturnItem2 {

    public LoadCignaEnrollmentFormReturnItem2() {
        super();
    }

    private String personFirstName;
    private String personMiddleName;
    private String personLastName;
    private String personSSN;
    private int personEffDate;
    private boolean personMedicareA;
    private boolean personMedicareB;
    private boolean personMedicaid;
    private boolean personOther;

	LoadCignaEnrollmentFormReturnItem2(BHREmplDependent dep) {
		personFirstName=dep.getFirstName();
		personMiddleName=dep.getMiddleName();
		personLastName=dep.getLastName();
		personSSN=dep.getSsn();
	}

    public int getPersonEffDate() {
        return personEffDate;
    }

    public void setPersonEffDate(int personEffDate) {
        this.personEffDate = personEffDate;
    }

    public boolean isPersonMedicaid() {
        return personMedicaid;
    }

    public void setPersonMedicaid(boolean personMedicaid) {
        this.personMedicaid = personMedicaid;
    }

    public boolean isPersonMedicareA() {
        return personMedicareA;
    }

    public void setPersonMedicareA(boolean personMedicareA) {
        this.personMedicareA = personMedicareA;
    }

    public boolean isPersonMedicareB() {
        return personMedicareB;
    }

    public void setPersonMedicareB(boolean personMedicareB) {
        this.personMedicareB = personMedicareB;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonMiddleName() {
        return personMiddleName;
    }

    public void setPersonMiddleName(String personMiddleName) {
        this.personMiddleName = personMiddleName;
    }

    public boolean isPersonOther() {
        return personOther;
    }

    public void setPersonOther(boolean personOther) {
        this.personOther = personOther;
    }

    public String getPersonSSN() {
        return personSSN;
    }

    public void setPersonSSN(String personSSN) {
        this.personSSN = personSSN;
    }

   public String toXML()
    {
       String ret="";

       ret+=tag("personFirstName", personFirstName)+
            tag("personMiddleName", personMiddleName)+
            tag("personLastName", personLastName)+
            tag("personSSN", personSSN)+
            tag("personEffDate", personEffDate)+
            tag("personMedicareA", personMedicareA)+
            tag("personMedicareB", personMedicareB)+
            tag("personMedicaid", personMedicaid)+
            tag("personOther", personOther);
           
       return ret;
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
