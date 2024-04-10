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
public class PendingPersonHasChanges {

    public HibernateSessionUtil hsu = ArahantSession.getHSU();

    private void PendingPersonHasChanges() {
        hsu.beginTransaction();
        String empId = "00001-0000000097";
        BPerson bpp = new BPerson();


        //if there is a pending record, get it
        if (bpp.hasPending(empId)) {
            bpp.loadPending(empId);

        } else {
            //otherwise, get the Real person record
            bpp.loadRealPerson(empId);
			//bpp = new BPerson(ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, empId).first());
        }

        //now let's check if we have changes to be saved
       // bpp.saveOriginalData();
       // bpp.setHomePhone("613-567-5674");
        
       // System.out.println("Has changes" + bpp.hasChanges(bpp));

        //hsu.commitTransaction();
    }

    public static void main(String[] args) {
        new PendingPersonHasChanges().PendingPersonHasChanges();
    }
}
