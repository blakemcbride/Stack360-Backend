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

package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.beans.Person;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

/**
 *
 * Arahant
 */
public class PendingPerson {
    public HibernateSessionUtil hsu = ArahantSession.getHSU();
    private void createPendingPerson(){
        hsu.beginTransaction();
        String personId = "00001-0000000130";
        Person existingPerson=hsu.get(Person.class, personId);
        hsu.setCurrentPerson(existingPerson);
        BPerson newPerson = new BPerson();
        newPerson.create();

        HibernateSessionUtil.copyCorresponding(newPerson, existingPerson, Person.PERSONID);
        newPerson.getPerson().setRecordType('C');
        newPerson.setMiddleName("bbb");
        newPerson.insert();
        
        hsu.commitTransaction();
    }

    public static void main(String[] args) {
        new PendingPerson().createPendingPerson();
    }
}
