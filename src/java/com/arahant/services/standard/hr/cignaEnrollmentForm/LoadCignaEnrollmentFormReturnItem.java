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
import com.arahant.utils.DateUtils;
import java.text.SimpleDateFormat;

/**
 *
 */
public class LoadCignaEnrollmentFormReturnItem {

    public LoadCignaEnrollmentFormReturnItem() {
        super();
    }

    private String personFirstName;
    private String personMiddleName;
    private String personLastName;
    private int personDOB;
    private String personGender;
    private String personSSN;
    private String personChoice;
    private boolean personExisting;
    private String personDentalNumber1;
    private String personDentalNumber2;

	private SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz Z yyyy");
	
	LoadCignaEnrollmentFormReturnItem(BHREmplDependent dep) {
		personFirstName=dep.getFirstName();
		personMiddleName=dep.getMiddleName();
		personLastName=dep.getLastName();
		personGender=dep.getSex();
		personSSN=dep.getSsn();
		//personDOB=sdf.format(DateUtils.getDate(dep.getDob()));;//TODO: need to format for front end
                personDOB=dep.getDob();
	}

    public String getPersonChoice() {
        return personChoice;
    }

    public void setPersonChoice(String personChoice) {
        this.personChoice = personChoice;
    }

    public int getPersonDOB() {
        return personDOB;
    }

    public void setPersonDOB(int personDOB) {
        this.personDOB = personDOB;
    }

    public String getPersonDentalNumber1() {
        return personDentalNumber1;
    }

    public void setPersonDentalNumber1(String personDentalNumber1) {
        this.personDentalNumber1 = personDentalNumber1;
    }

    public String getPersonDentalNumber2() {
        return personDentalNumber2;
    }

    public void setPersonDentalNumber2(String personDentalNumber2) {
        this.personDentalNumber2 = personDentalNumber2;
    }

    public boolean isPersonExisting() {
        return personExisting;
    }

    public void setPersonExisting(boolean personExisting) {
        this.personExisting = personExisting;
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

       ret+=tag("personFirstName", personFirstName)+
            tag("personMiddleName", personMiddleName)+
            tag("personLastName", personLastName)+
            tag("personDOB", personDOB)+
            tag("personGender", personGender)+
            tag("personSSN", personSSN)+
            tag("personChoice", personChoice)+
            tag("personExisting", personExisting)+
            tag("personDentalNumber1", personDentalNumber1)+
            tag("personDentalNumber2", personDentalNumber2);

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
