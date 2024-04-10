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

import com.arahant.utils.DOMUtils;

/**
 *
 */
public class NewCignaEnrollmentFormInputItem {

    public NewCignaEnrollmentFormInputItem() {
        super();
    }

    private String personFirstName;
    private String personMiddleName;
    private String personLastName;
    private String personDOB;
    private String personGender;
    private String personSSN;

    
    public String getPersonDOB() {
        return personDOB;
    }

    public void setPersonDOB(String personDOB) {
        this.personDOB = personDOB;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonGender() {
        return personGender;
    }

    public void setPersonGender(String personGender) {
        this.personGender = personGender;
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

    public String getPersonSSN() {
        return personSSN;
    }

    public void setPersonSSN(String personSSN) {
        this.personSSN = personSSN;
    }

    public String toXML()
    {
       String ret="";

       ret+=tag("personFirstName", DOMUtils.escapeText(personFirstName))+
            tag("personMiddleName", DOMUtils.escapeText(personMiddleName))+
            tag("personLastName", DOMUtils.escapeText(personLastName))+
            tag("personDOB", DOMUtils.escapeText(personDOB))+
            tag("personGender", DOMUtils.escapeText(personGender))+
            tag("personSSN", DOMUtils.escapeText(personSSN));

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
